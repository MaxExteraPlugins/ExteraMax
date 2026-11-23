#!/data/data/com.termux/files/usr/bin/bash

set -euo pipefail

TOOLS_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(cd "$TOOLS_DIR/../.." && pwd)"
WORKSPACE_DIR="$(cd "$PROJECT_DIR/.." && pwd)"

APK="${1:-$WORKSPACE_DIR/enjarify/ayu.apk}"
OUT="${2:-$PROJECT_DIR/bin/classes/ayugram/12.10.6/ayu.jar}"

D2J_LIB="$PREFIX/opt/dex2jar/lib"
ASM_CP="$D2J_LIB/asm-9.5.jar:$D2J_LIB/asm-tree-9.5.jar"

if [ ! -f "$APK" ]; then
    echo "ERROR: APK not found: $APK" >&2
    exit 1
fi
if [ ! -d "$D2J_LIB" ]; then
    echo "ERROR: dex2jar not installed. Run: pkg install dex2jar" >&2
    exit 1
fi

TMP="$(mktemp -d)"
trap 'rm -rf "$TMP"' EXIT
mkdir -p "$TMP/dex" "$TMP/jars"

echo "[1/4] Extracting dex files..."
unzip -o -j "$APK" 'classes*.dex' -d "$TMP/dex" >/dev/null

echo "[2/4] Converting dex -> jar (signature-only, capped heap)..."
for d in "$TMP"/dex/classes*.dex; do
    [ -f "$d" ] || continue
    base="$(basename "$d" .dex)"
    java -Xms32m -Xmx256m -cp "$D2J_LIB/*" \
        com.googlecode.dex2jar.tools.Dex2jarCmd --force --no-code \
        -o "$TMP/jars/$base.jar" "$d" >/dev/null 2>&1 || true
done

echo "[3/4] Merging jars..."
mkdir -p "$(dirname "$OUT")"
python3 - "$TMP/jars" "$OUT" <<'PY'
import zipfile, glob, sys
jars_dir, out = sys.argv[1], sys.argv[2]
seen = set()
with zipfile.ZipFile(out, "w", zipfile.ZIP_STORED) as z:
    for j in sorted(glob.glob(jars_dir + "/*.jar")):
        with zipfile.ZipFile(j) as src:
            for n in src.namelist():
                if n.endswith(".class") and n not in seen:
                    seen.add(n)
                    z.writestr(n, src.read(n))
print("  merged classes:", len(seen))
PY

echo "[4/4] Repairing InnerClasses attributes (FixInner)..."
javac -cp "$ASM_CP" -d "$TMP" "$TOOLS_DIR/FixInner.java"
java -Xms64m -Xmx384m -cp "$TMP:$ASM_CP" FixInner "$OUT"

echo "DONE: $OUT"