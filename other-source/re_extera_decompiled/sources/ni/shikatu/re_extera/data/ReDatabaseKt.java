package ni.shikatu.re_extera.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.Collection;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: ReDatabase.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000:\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u001e\n\u0002\u0010\b\n\u0000\u001a4\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u0001*\u00020\u00022\u0017\u0010\u0003\u001a\u0013\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002H\u00010\u0004¢\u0006\u0002\b\u0005H\u0086\bø\u0001\u0000¢\u0006\u0002\u0010\u0006\u001a?\u0010\u0007\u001a\u00020\b*\u00020\u00022\u0006\u0010\t\u001a\u00020\n2\u0012\u0010\u000b\u001a\n\u0012\u0006\b\u0001\u0012\u00020\n0\f\"\u00020\n2\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\b0\u0004¢\u0006\u0002\u0010\u000f\u001a\u0010\u0010\u0010\u001a\u00020\n*\b\u0012\u0004\u0012\u00020\u00120\u0011\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u0013"}, d2 = {"inTransaction", "T", "Landroid/database/sqlite/SQLiteDatabase;", "block", "Lkotlin/Function1;", "Lkotlin/ExtensionFunctionType;", "(Landroid/database/sqlite/SQLiteDatabase;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "forEachRow", "", "sql", "", "args", "", "row", "Landroid/database/Cursor;", "(Landroid/database/sqlite/SQLiteDatabase;Ljava/lang/String;[Ljava/lang/String;Lkotlin/jvm/functions/Function1;)V", "sqlList", "", "", "re_extera"}, k = 2, mv = {2, 2, 0}, xi = 48)
public final class ReDatabaseKt {
    public static final <T> T inTransaction(SQLiteDatabase sQLiteDatabase, Function1<? super SQLiteDatabase, ? extends T> function1) {
        Intrinsics.checkNotNullParameter(sQLiteDatabase, "<this>");
        Intrinsics.checkNotNullParameter(function1, "block");
        sQLiteDatabase.beginTransaction();
        try {
            T t = (T) function1.invoke(sQLiteDatabase);
            sQLiteDatabase.setTransactionSuccessful();
            return t;
        } finally {
            sQLiteDatabase.endTransaction();
        }
    }

    public static final void forEachRow(SQLiteDatabase sQLiteDatabase, String str, String[] strArr, Function1<? super Cursor, Unit> function1) {
        Intrinsics.checkNotNullParameter(sQLiteDatabase, "<this>");
        Intrinsics.checkNotNullParameter(str, "sql");
        Intrinsics.checkNotNullParameter(strArr, "args");
        Intrinsics.checkNotNullParameter(function1, "row");
        Cursor cursorRawQuery = sQLiteDatabase.rawQuery(str, strArr);
        try {
            Cursor cursor = cursorRawQuery;
            while (cursor.moveToNext()) {
                Intrinsics.checkNotNull(cursor);
                function1.invoke(cursor);
            }
            Unit unit = Unit.INSTANCE;
            CloseableKt.closeFinally(cursorRawQuery, (Throwable) null);
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                CloseableKt.closeFinally(cursorRawQuery, th);
                throw th2;
            }
        }
    }

    public static final String sqlList(Collection<Integer> collection) {
        Intrinsics.checkNotNullParameter(collection, "<this>");
        return CollectionsKt.joinToString$default(collection, ",", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 62, (Object) null);
    }
}
