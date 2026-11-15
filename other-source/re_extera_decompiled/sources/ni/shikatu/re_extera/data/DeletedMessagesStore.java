package ni.shikatu.re_extera.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.telegram.messenger.MessageObject;

/* JADX INFO: compiled from: DeletedMessagesStore.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\u0010#\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010$\n\u0002\b\u0002\n\u0002\u0010\u001e\n\u0002\b\u0004\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fJ\u0016\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\bJ\u0010\u0010\r\u001a\u00020\u000e2\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012J\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00060\u0014J\u0014\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\b0\u00142\u0006\u0010\u000f\u001a\u00020\u0006J.\u0010\u0016\u001a\u00020\n2\u0006\u0010\u000f\u001a\u00020\u00062\u001e\u0010\u0017\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00060\u0019\u0012\u0004\u0012\u00020\n0\u0018J\u001c\u0010\u001a\u001a\u00020\n2\u0006\u0010\u000f\u001a\u00020\u00062\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\b0\u001cJ\u001c\u0010\u001d\u001a\u00020\n2\u0006\u0010\u000f\u001a\u00020\u00062\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\b0\u001cJ\u0006\u0010\u001e\u001a\u00020\nJ\u0016\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u0006\u0010\u000f\u001a\u00020\u0006H\u0002R \u0010\u0004\u001a\u0014\u0012\u0004\u0012\u00020\u0006\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006 "}, d2 = {"Lni/shikatu/re_extera/data/DeletedMessagesStore;", "", "<init>", "()V", "byDialog", "Ljava/util/concurrent/ConcurrentHashMap;", "", "", "", "load", "", "db", "Landroid/database/sqlite/SQLiteDatabase;", "isDeleted", "", "dialogId", "messageId", "message", "Lorg/telegram/messenger/MessageObject;", "dialogs", "", "ids", "deletionTimes", "onResult", "Lkotlin/Function1;", "", "add", "messageIds", "", "remove", "clear", "idsOf", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class DeletedMessagesStore {
    public static final DeletedMessagesStore INSTANCE = new DeletedMessagesStore();
    private static final ConcurrentHashMap<Long, Set<Integer>> byDialog = new ConcurrentHashMap<>();

    private DeletedMessagesStore() {
    }

    public final void load(SQLiteDatabase db) {
        Intrinsics.checkNotNullParameter(db, "db");
        byDialog.clear();
        ReDatabaseKt.forEachRow(db, "SELECT did, mid FROM deleted_keys", new String[0], new Function1() { // from class: ni.shikatu.re_extera.data.DeletedMessagesStore.1
            public final Object invoke(Object obj) {
                return DeletedMessagesStore.load$lambda$0((Cursor) obj);
            }
        });
    }

    static final Unit load$lambda$0(Cursor cursor) {
        Intrinsics.checkNotNullParameter(cursor, "it");
        INSTANCE.idsOf(cursor.getLong(0)).add(Integer.valueOf(cursor.getInt(1)));
        return Unit.INSTANCE;
    }

    public final boolean isDeleted(long dialogId, int messageId) {
        Set<Integer> set = byDialog.get(Long.valueOf(dialogId));
        return set != null && set.contains(Integer.valueOf(messageId));
    }

    public final boolean isDeleted(MessageObject message) {
        return message != null && isDeleted(message.getDialogId(), message.getId());
    }

    public final List<Long> dialogs() {
        ConcurrentHashMap<Long, Set<Integer>> concurrentHashMap = byDialog;
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Map.Entry<Long, Set<Integer>> entry : concurrentHashMap.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                linkedHashMap.put(entry.getKey(), entry.getValue());
            }
        }
        return CollectionsKt.toList(linkedHashMap.keySet());
    }

    public final List<Integer> ids(long dialogId) {
        Set<Integer> set = byDialog.get(Long.valueOf(dialogId));
        List<Integer> listSorted = set != null ? CollectionsKt.sorted(set) : null;
        return listSorted == null ? CollectionsKt.emptyList() : listSorted;
    }

    public final void deletionTimes(final long dialogId, Function1<? super Map<Integer, Long>, Unit> onResult) {
        Intrinsics.checkNotNullParameter(onResult, "onResult");
        ReDatabase.INSTANCE.getDb().read(new Function1() { // from class: ni.shikatu.re_extera.data.DeletedMessagesStore.0
            public final Object invoke(Object obj) {
                return DeletedMessagesStore.deletionTimes$lambda$3(dialogId, (SQLiteDatabase) obj);
            }
        }, onResult);
    }

    static final HashMap deletionTimes$lambda$3(long j, SQLiteDatabase sQLiteDatabase) {
        Intrinsics.checkNotNullParameter(sQLiteDatabase, "$this$read");
        final HashMap map = new HashMap();
        ReDatabaseKt.forEachRow(sQLiteDatabase, "SELECT mid, ts FROM deleted_keys WHERE did = ?", new String[]{String.valueOf(j)}, new Function1() { // from class: ni.shikatu.re_extera.data.DeletedMessagesStore.2
            public final Object invoke(Object obj) {
                return DeletedMessagesStore.deletionTimes$lambda$3$lambda$2(map, (Cursor) obj);
            }
        });
        return map;
    }

    static final Unit deletionTimes$lambda$3$lambda$2(HashMap map, Cursor cursor) {
        Intrinsics.checkNotNullParameter(cursor, "it");
        map.put(Integer.valueOf(cursor.getInt(0)), Long.valueOf(cursor.getLong(1)));
        return Unit.INSTANCE;
    }

    public final void add(final long dialogId, final Collection<Integer> messageIds) {
        Intrinsics.checkNotNullParameter(messageIds, "messageIds");
        if (messageIds.isEmpty()) {
            return;
        }
        idsOf(dialogId).addAll(messageIds);
        final long jCurrentTimeMillis = System.currentTimeMillis();
        ReDatabase.INSTANCE.getDb().write(new Function1() { // from class: ni.shikatu.re_extera.data.DeletedMessagesStore.5
            public final Object invoke(Object obj) {
                return DeletedMessagesStore.add$lambda$5(messageIds, dialogId, jCurrentTimeMillis, (SQLiteDatabase) obj);
            }
        });
    }

    static final Unit add$lambda$5(Collection collection, long j, long j2, SQLiteDatabase sQLiteDatabase) {
        Intrinsics.checkNotNullParameter(sQLiteDatabase, "$this$write");
        SQLiteStatement sQLiteStatementCompileStatement = sQLiteDatabase.compileStatement("INSERT OR IGNORE INTO deleted_keys(did, mid, ts) VALUES(?, ?, ?)");
        try {
            SQLiteStatement sQLiteStatement = sQLiteStatementCompileStatement;
            Iterator it = collection.iterator();
            while (it.hasNext()) {
                int iIntValue = ((Number) it.next()).intValue();
                sQLiteStatement.bindLong(1, j);
                sQLiteStatement.bindLong(2, iIntValue);
                sQLiteStatement.bindLong(3, j2);
                sQLiteStatement.executeInsert();
                sQLiteStatement.clearBindings();
            }
            Unit unit = Unit.INSTANCE;
            CloseableKt.closeFinally(sQLiteStatementCompileStatement, (Throwable) null);
            return Unit.INSTANCE;
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                CloseableKt.closeFinally(sQLiteStatementCompileStatement, th);
                throw th2;
            }
        }
    }

    public final void remove(final long dialogId, final Collection<Integer> messageIds) {
        Intrinsics.checkNotNullParameter(messageIds, "messageIds");
        if (messageIds.isEmpty()) {
            return;
        }
        Set<Integer> set = byDialog.get(Long.valueOf(dialogId));
        if (set != null) {
            set.removeAll(CollectionsKt.toSet(messageIds));
        }
        ReDatabase.INSTANCE.getDb().write(new Function1() { // from class: ni.shikatu.re_extera.data.DeletedMessagesStore.4
            public final Object invoke(Object obj) {
                return DeletedMessagesStore.remove$lambda$6(messageIds, dialogId, (SQLiteDatabase) obj);
            }
        });
    }

    static final Unit remove$lambda$6(Collection collection, long j, SQLiteDatabase sQLiteDatabase) {
        Intrinsics.checkNotNullParameter(sQLiteDatabase, "$this$write");
        sQLiteDatabase.delete("deleted_keys", "did = ? AND mid IN (" + ReDatabaseKt.sqlList(collection) + ")", new String[]{String.valueOf(j)});
        return Unit.INSTANCE;
    }

    public final void clear() {
        byDialog.clear();
        ReDatabase.INSTANCE.getDb().write(new Function1() { // from class: ni.shikatu.re_extera.data.DeletedMessagesStore.3
            public final Object invoke(Object obj) {
                return DeletedMessagesStore.clear$lambda$7((SQLiteDatabase) obj);
            }
        });
    }

    static final Unit clear$lambda$7(SQLiteDatabase sQLiteDatabase) {
        Intrinsics.checkNotNullParameter(sQLiteDatabase, "$this$write");
        sQLiteDatabase.delete("deleted_keys", null, null);
        return Unit.INSTANCE;
    }

    private final Set<Integer> idsOf(long dialogId) {
        Set<Integer> setPutIfAbsent;
        ConcurrentHashMap<Long, Set<Integer>> concurrentHashMap = byDialog;
        Long lValueOf = Long.valueOf(dialogId);
        ConcurrentHashMap.KeySetView keySetViewNewKeySet = concurrentHashMap.get(lValueOf);
        if (keySetViewNewKeySet == null && (setPutIfAbsent = concurrentHashMap.putIfAbsent(lValueOf, (keySetViewNewKeySet = ConcurrentHashMap.newKeySet()))) != null) {
            keySetViewNewKeySet = setPutIfAbsent;
        }
        Intrinsics.checkNotNullExpressionValue(keySetViewNewKeySet, "getOrPut(...)");
        return keySetViewNewKeySet;
    }
}
