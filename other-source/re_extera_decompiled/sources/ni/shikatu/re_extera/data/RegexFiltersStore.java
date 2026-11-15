package ni.shikatu.re_extera.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import ni.shikatu.re_extera.ReLog;
import ni.shikatu.re_extera.settings.ReSettings;
import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.TLRPC;

/* JADX INFO: compiled from: RegexFiltersStore.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eJ\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\b0\u0007J\u0010\u0010\u0010\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0011\u001a\u00020\bJ\u000e\u0010\u0012\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00020\bJ\u0016\u0010\u0013\u001a\u00020\f2\u0006\u0010\u0014\u001a\u00020\b2\u0006\u0010\u0015\u001a\u00020\bJ\u000e\u0010\u0016\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00020\bJ\u0010\u0010\u0017\u001a\u00020\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\u001aJ\u0016\u0010\u001b\u001a\u00020\f2\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u0007X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u001d"}, d2 = {"Lni/shikatu/re_extera/data/RegexFiltersStore;", "", "<init>", "()V", "FLAGS", "", "filters", "", "", "patterns", "Ljava/util/regex/Pattern;", "load", "", "db", "Landroid/database/sqlite/SQLiteDatabase;", "all", "validate", "regex", "add", "update", "old", "new", "remove", "isFiltered", "", "message", "Lorg/telegram/messenger/MessageObject;", "apply", "list", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class RegexFiltersStore {
    private static final int FLAGS = 40;
    public static final RegexFiltersStore INSTANCE = new RegexFiltersStore();
    private static volatile List<String> filters = CollectionsKt.emptyList();
    private static volatile List<Pattern> patterns = CollectionsKt.emptyList();

    private RegexFiltersStore() {
    }

    public final void load(SQLiteDatabase db) {
        Intrinsics.checkNotNullParameter(db, "db");
        final ArrayList arrayList = new ArrayList();
        ReDatabaseKt.forEachRow(db, "SELECT regex FROM regex_filters ORDER BY regex ASC", new String[0], new Function1() { // from class: ni.shikatu.re_extera.data.RegexFiltersStore.2
            public final Object invoke(Object obj) {
                return RegexFiltersStore.load$lambda$0(arrayList, (Cursor) obj);
            }
        });
        apply(arrayList);
    }

    static final Unit load$lambda$0(ArrayList arrayList, Cursor cursor) {
        Intrinsics.checkNotNullParameter(cursor, "it");
        arrayList.add(cursor.getString(0));
        return Unit.INSTANCE;
    }

    public final List<String> all() {
        return filters;
    }

    public final String validate(String regex) {
        Intrinsics.checkNotNullParameter(regex, "regex");
        try {
            Pattern.compile(regex, FLAGS);
            return null;
        } catch (PatternSyntaxException e) {
            String description = e.getDescription();
            return description == null ? e.getMessage() : description;
        }
    }

    public final void add(final String regex) {
        Intrinsics.checkNotNullParameter(regex, "regex");
        if (filters.contains(regex)) {
            return;
        }
        apply(CollectionsKt.plus(filters, regex));
        ReDatabase.INSTANCE.getDb().write(new Function1() { // from class: ni.shikatu.re_extera.data.RegexFiltersStore.3
            public final Object invoke(Object obj) {
                return RegexFiltersStore.add$lambda$1(regex, (SQLiteDatabase) obj);
            }
        });
    }

    static final Unit add$lambda$1(String str, SQLiteDatabase sQLiteDatabase) {
        Intrinsics.checkNotNullParameter(sQLiteDatabase, "$this$write");
        sQLiteDatabase.execSQL("INSERT OR IGNORE INTO regex_filters(regex) VALUES(?)", new String[]{str});
        return Unit.INSTANCE;
    }

    public final void update(final String old, final String str) {
        Intrinsics.checkNotNullParameter(old, "old");
        Intrinsics.checkNotNullParameter(str, "new");
        if (Intrinsics.areEqual(old, str)) {
            return;
        }
        List<String> list = filters;
        ArrayList arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list, 10));
        for (String str2 : list) {
            if (Intrinsics.areEqual(str2, old)) {
                str2 = str;
            }
            arrayList.add(str2);
        }
        apply(CollectionsKt.distinct(arrayList));
        ReDatabase.INSTANCE.getDb().write(new Function1() { // from class: ni.shikatu.re_extera.data.RegexFiltersStore.0
            public final Object invoke(Object obj) {
                return RegexFiltersStore.update$lambda$3(old, str, (SQLiteDatabase) obj);
            }
        });
    }

    static final Unit update$lambda$3(String str, String str2, SQLiteDatabase sQLiteDatabase) {
        Intrinsics.checkNotNullParameter(sQLiteDatabase, "$this$write");
        sQLiteDatabase.delete("regex_filters", "regex = ?", new String[]{str});
        sQLiteDatabase.execSQL("INSERT OR IGNORE INTO regex_filters(regex) VALUES(?)", new String[]{str2});
        return Unit.INSTANCE;
    }

    public final void remove(final String regex) {
        Intrinsics.checkNotNullParameter(regex, "regex");
        apply(CollectionsKt.minus(filters, regex));
        ReDatabase.INSTANCE.getDb().write(new Function1() { // from class: ni.shikatu.re_extera.data.RegexFiltersStore.1
            public final Object invoke(Object obj) {
                return RegexFiltersStore.remove$lambda$4(regex, (SQLiteDatabase) obj);
            }
        });
    }

    static final Unit remove$lambda$4(String str, SQLiteDatabase sQLiteDatabase) {
        Intrinsics.checkNotNullParameter(sQLiteDatabase, "$this$write");
        sQLiteDatabase.delete("regex_filters", "regex = ?", new String[]{str});
        return Unit.INSTANCE;
    }

    /* JADX WARN: Code duplicated, block: B:21:0x0031  */
    /* JADX WARN: Code duplicated, block: B:29:0x0047 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:30:0x0048  */
    public final boolean isFiltered(MessageObject message) {
        CharSequence charSequence;
        String str;
        String string;
        if (message == null || message.isOut() || !ReSettings.INSTANCE.getFiltersEnabled()) {
            return false;
        }
        List<Pattern> list = patterns;
        if (list.isEmpty()) {
            return false;
        }
        TLRPC.Message message2 = message.messageOwner;
        String str2 = null;
        if (message2 == null || (str = message2.message) == null) {
            charSequence = message.messageText;
            if (charSequence != null && (string = charSequence.toString()) != null && string.length() > 0) {
                str2 = string;
            }
            if (str2 == null) {
                return false;
            }
            str = str2;
        } else {
            if (str.length() <= 0) {
                str = null;
            }
            if (str == null) {
                charSequence = message.messageText;
                if (charSequence != null) {
                    str2 = string;
                }
                if (str2 == null) {
                    return false;
                }
                str = str2;
            }
        }
        List<Pattern> list2 = list;
        if ((list2 instanceof Collection) && list2.isEmpty()) {
            return false;
        }
        Iterator<T> it = list2.iterator();
        while (it.hasNext()) {
            if (((Pattern) it.next()).matcher(str).find()) {
                return true;
            }
        }
        return false;
    }

    private final void apply(List<String> list) {
        Pattern patternCompile;
        List<String> list2 = list;
        filters = CollectionsKt.sorted(list2);
        ArrayList arrayList = new ArrayList();
        for (String str : list2) {
            try {
                patternCompile = Pattern.compile(str, FLAGS);
            } catch (PatternSyntaxException e) {
                ReLog.INSTANCE.e("Invalid regex filter: " + str, e);
                patternCompile = null;
            }
            if (patternCompile != null) {
                arrayList.add(patternCompile);
            }
        }
        patterns = arrayList;
    }
}
