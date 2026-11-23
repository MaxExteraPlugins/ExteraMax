#!/data/data/com.termux/files/usr/bin/bash

set -euo pipefail

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TOOLS_DIR="$PROJECT_DIR/bin/tools"
CODE_DIR="$PROJECT_DIR/code"
BIN_DIR="$PROJECT_DIR/bin"
JAVA_SRC="$CODE_DIR/java.java"
PYTHON_SRC="$CODE_DIR/python.py"
ANDROID_JAR="$BIN_DIR/android.jar"
CLIENT_JAR="$BIN_DIR/classes/ayugram/12.10.6/ayu.jar"
ANDROID_JAR_URL="https://raw.githubusercontent.com/Sable/android-platforms/master/android-34/android.jar"

for tool in javac java d8 python3 unzip curl; do
    if ! command -v "$tool" >/dev/null 2>&1; then
        echo "ERROR: '$tool' not found. Install it first (e.g. pkg install openjdk-21 d8 python unzip curl)." >&2
        exit 1
    fi
done

[ -f "$JAVA_SRC" ]   || { echo "ERROR: $JAVA_SRC not found" >&2; exit 1; }
[ -f "$PYTHON_SRC" ] || { echo "ERROR: $PYTHON_SRC not found" >&2; exit 1; }

# ---------------- bootstrap android.jar ----------------
if [ ! -f "$ANDROID_JAR" ]; then
    echo "[boot] Downloading android.jar..."
    mkdir -p "$BIN_DIR"
    curl -sL --max-time 180 -o "$ANDROID_JAR" "$ANDROID_JAR_URL"
fi

# ---------------- bootstrap client classpath ----------------
if [ ! -f "$CLIENT_JAR" ]; then
    echo "[boot] Preparing client classpath jar (one-time)..."
    bash "$TOOLS_DIR/prepare-classpath.sh"
fi

# ---------------- plugin id / package ----------------
PLUGIN_ID="$(grep -E '^__id__' "$PYTHON_SRC" | head -1 | sed -E 's/^__id__[[:space:]]*=[[:space:]]*//; s/["'"'"']//g; s/[[:space:]]//g')"
[ -n "$PLUGIN_ID" ] || { echo "ERROR: cannot extract __id__" >&2; exit 1; }
PACKAGE="$(grep -E '^package ' "$JAVA_SRC" | head -1 | sed -E 's/^package[[:space:]]+//; s/;.*//')"
CLASS_NAME="$(grep -E '^public class ' "$JAVA_SRC" | head -1 | sed -E 's/^public class[[:space:]]+//; s/[[:space:]{].*//')"
[ -n "$PACKAGE" ] || { echo "ERROR: cannot extract package" >&2; exit 1; }
[ -n "$CLASS_NAME" ] || CLASS_NAME="Main"

echo "================================================"
echo "  ExteraMax Plugin Builder (Termux)"
echo "================================================"
echo "  Plugin ID : $PLUGIN_ID"
echo "  Package   : $PACKAGE (class $CLASS_NAME)"
echo

TMP="$(mktemp -d)"
trap 'rm -rf "$TMP"' EXIT
PKG_DIR="$TMP/src/$(echo "$PACKAGE" | tr '.' '/')"
mkdir -p "$PKG_DIR" "$TMP/out"
cp "$JAVA_SRC" "$PKG_DIR/$CLASS_NAME.java"

CP="$CLIENT_JAR:$ANDROID_JAR"

echo "[1/5] Compiling Java..."
javac -source 8 -target 8 -Xlint:-options -nowarn -d "$TMP/out" -cp "$CP" "$PKG_DIR/$CLASS_NAME.java"
echo "      OK"

echo "[2/5] Converting to DEX (d8)..."
(cd "$TMP/out" && jar cf "$TMP/classes.jar" .)
d8 --lib "$ANDROID_JAR" --classpath "$CLIENT_JAR" --output "$TMP" "$TMP/classes.jar"
[ -f "$TMP/classes.dex" ] || { echo "ERROR: classes.dex not created" >&2; exit 1; }
echo "      OK"

echo "[3/5] Encoding DEX (base64+zlib)..."
python3 - "$TMP/classes.dex" "$TMP/dex_payload.txt" <<'PY'
import base64, zlib, sys
dex = open(sys.argv[1], "rb").read()
enc = base64.b64encode(zlib.compress(dex, 9)).decode()
lines = ["# " + enc[i:i+76] for i in range(0, len(enc), 76)]
open(sys.argv[2], "w").write("\n".join(lines))
PY
echo "      OK"

echo "[4/5] Assembling $PLUGIN_ID.plugin..."
python3 - "$PYTHON_SRC" "$TMP/dex_payload.txt" "$PROJECT_DIR/$PLUGIN_ID.plugin" <<'PY'
import sys
py = open(sys.argv[1], encoding="utf-8").read()
payload = open(sys.argv[2], encoding="utf-8").read()
before, rest = py.split("# __DEX_BEGIN__", 1)
after = rest.split("# __DEX_END__", 1)[1]
out = before + "# __DEX_BEGIN__\n" + payload + "\n# __DEX_END__" + after
open(sys.argv[3], "w", encoding="utf-8").write(out)
PY
echo "      OK"

echo "[5/5] Verifying..."
python3 -m py_compile "$PROJECT_DIR/$PLUGIN_ID.plugin"
SIZE="$(stat -c%s "$PROJECT_DIR/$PLUGIN_ID.plugin")"
echo
echo "================================================"
echo "  BUILD SUCCESS: $PLUGIN_ID.plugin ($SIZE bytes)"
echo "  $PROJECT_DIR/$PLUGIN_ID.plugin"
echo "================================================"