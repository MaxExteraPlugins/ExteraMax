package ni.shikatu.re_extera.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import ni.shikatu.re_extera.ReLog;
import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.InputSerializedData;
import org.telegram.tgnet.OutputSerializedData;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLRPC;

/* JADX INFO: compiled from: EditHistoryStore.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\u0010#\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u001e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0002\b\u0004\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fJ\u0016\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\bJ\u0010\u0010\r\u001a\u00020\u000e2\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012J&\u0010\u0013\u001a\u00020\n2\u0006\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\b2\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0015J0\u0010\u0017\u001a\u00020\n2\u0006\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\b2\u0018\u0010\u0018\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00150\u001a\u0012\u0004\u0012\u00020\n0\u0019J\u001c\u0010\u001b\u001a\u00020\n2\u0006\u0010\u000f\u001a\u00020\u00062\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\b0\u001dJ\u0006\u0010\u001e\u001a\u00020\nJ\u0016\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u0006\u0010\u000f\u001a\u00020\u0006H\u0002J4\u0010 \u001a\u00020\n*\u00020!2\u0006\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\b2\u0006\u0010\"\u001a\u00020\b2\u0006\u0010#\u001a\u00020\b2\u0006\u0010$\u001a\u00020%H\u0002J\u0012\u0010&\u001a\u0004\u0018\u00010%2\u0006\u0010\u0011\u001a\u00020\u0015H\u0002J\u0014\u0010'\u001a\u0004\u0018\u00010\u00152\b\u0010(\u001a\u0004\u0018\u00010%H\u0002R \u0010\u0004\u001a\u0014\u0012\u0004\u0012\u00020\u0006\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006)"}, d2 = {"Lni/shikatu/re_extera/data/EditHistoryStore;", "", "<init>", "()V", "edited", "Ljava/util/concurrent/ConcurrentHashMap;", "", "", "", "load", "", "db", "Landroid/database/sqlite/SQLiteDatabase;", "hasHistory", "", "dialogId", "messageId", "message", "Lorg/telegram/messenger/MessageObject;", "record", "original", "Lorg/telegram/tgnet/TLRPC$Message;", "edit", "loadVersions", "onResult", "Lkotlin/Function1;", "", "remove", "messageIds", "", "clear", "idsOf", "bindVersion", "Landroid/database/sqlite/SQLiteStatement;", "version", "date", "data", "", "serialize", "deserialize", "bytes", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class EditHistoryStore {
    public static final EditHistoryStore INSTANCE = new EditHistoryStore();
    private static final ConcurrentHashMap<Long, Set<Integer>> edited = new ConcurrentHashMap<>();

    private EditHistoryStore() {
    }

    public final void load(SQLiteDatabase db) {
        Intrinsics.checkNotNullParameter(db, "db");
        edited.clear();
        ReDatabaseKt.forEachRow(db, "SELECT did, mid FROM message_edits GROUP BY did, mid HAVING MAX(ver) > 0", new String[0], new Function1() { // from class: ni.shikatu.re_extera.data.EditHistoryStore.4
            public final Object invoke(Object obj) {
                return EditHistoryStore.load$lambda$0((Cursor) obj);
            }
        });
    }

    static final Unit load$lambda$0(Cursor cursor) {
        Intrinsics.checkNotNullParameter(cursor, "it");
        INSTANCE.idsOf(cursor.getLong(0)).add(Integer.valueOf(cursor.getInt(1)));
        return Unit.INSTANCE;
    }

    public final boolean hasHistory(long dialogId, int messageId) {
        Set<Integer> set = edited.get(Long.valueOf(dialogId));
        return set != null && set.contains(Integer.valueOf(messageId));
    }

    public final boolean hasHistory(MessageObject message) {
        return message != null && hasHistory(message.getDialogId(), message.getId());
    }

    public final void record(final long dialogId, final int messageId, final TLRPC.Message original, final TLRPC.Message edit) {
        final byte[] bArrSerialize;
        Intrinsics.checkNotNullParameter(original, "original");
        Intrinsics.checkNotNullParameter(edit, "edit");
        final byte[] bArrSerialize2 = serialize(original);
        if (bArrSerialize2 == null || (bArrSerialize = serialize(edit)) == null) {
            return;
        }
        idsOf(dialogId).add(Integer.valueOf(messageId));
        ReDatabase.INSTANCE.getDb().write(new Function1() { // from class: ni.shikatu.re_extera.data.EditHistoryStore.2
            public final Object invoke(Object obj) {
                return EditHistoryStore.record$lambda$4(dialogId, messageId, original, bArrSerialize2, edit, bArrSerialize, (SQLiteDatabase) obj);
            }
        });
    }

    static final Unit record$lambda$4(long j, int i, TLRPC.Message message, byte[] bArr, TLRPC.Message message2, byte[] bArr2, SQLiteDatabase sQLiteDatabase) {
        Intrinsics.checkNotNullParameter(sQLiteDatabase, "$this$write");
        Cursor cursorRawQuery = sQLiteDatabase.rawQuery("SELECT MAX(ver) FROM message_edits WHERE did = ? AND mid = ?", new String[]{String.valueOf(j), String.valueOf(i)});
        try {
            Cursor cursor = cursorRawQuery;
            int i2 = 0;
            int i3 = (!cursor.moveToFirst() || cursor.isNull(0)) ? -1 : cursor.getInt(0);
            Unit unit = Unit.INSTANCE;
            CloseableKt.closeFinally(cursorRawQuery, (Throwable) null);
            SQLiteStatement sQLiteStatementCompileStatement = sQLiteDatabase.compileStatement("INSERT OR IGNORE INTO message_edits(did, mid, ver, date, data) VALUES(?, ?, ?, ?, ?)");
            try {
                SQLiteStatement sQLiteStatement = sQLiteStatementCompileStatement;
                if (i3 < 0) {
                    EditHistoryStore editHistoryStore = INSTANCE;
                    Intrinsics.checkNotNull(sQLiteStatement);
                    editHistoryStore.bindVersion(sQLiteStatement, j, i, 0, message.date, bArr);
                    sQLiteStatement.executeInsert();
                } else {
                    i2 = i3;
                }
                EditHistoryStore editHistoryStore2 = INSTANCE;
                Intrinsics.checkNotNull(sQLiteStatement);
                int i4 = i2 + 1;
                Integer numValueOf = Integer.valueOf(message2.edit_date);
                if (numValueOf.intValue() == 0) {
                    numValueOf = null;
                }
                editHistoryStore2.bindVersion(sQLiteStatement, j, i, i4, numValueOf != null ? numValueOf.intValue() : message2.date, bArr2);
                sQLiteStatement.executeInsert();
                Unit unit2 = Unit.INSTANCE;
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
        } catch (Throwable th3) {
            try {
                throw th3;
            } catch (Throwable th4) {
                CloseableKt.closeFinally(cursorRawQuery, th3);
                throw th4;
            }
        }
    }

    public final void loadVersions(final long dialogId, final int messageId, Function1<? super List<? extends TLRPC.Message>, Unit> onResult) {
        Intrinsics.checkNotNullParameter(onResult, "onResult");
        ReDatabase.INSTANCE.getDb().read(new Function1() { // from class: ni.shikatu.re_extera.data.EditHistoryStore.5
            public final Object invoke(Object obj) {
                return EditHistoryStore.loadVersions$lambda$6(dialogId, messageId, (SQLiteDatabase) obj);
            }
        }, onResult);
    }

    static final ArrayList loadVersions$lambda$6(long j, int i, SQLiteDatabase sQLiteDatabase) {
        Intrinsics.checkNotNullParameter(sQLiteDatabase, "$this$read");
        final ArrayList arrayList = new ArrayList();
        ReDatabaseKt.forEachRow(sQLiteDatabase, "SELECT data FROM message_edits WHERE did = ? AND mid = ? ORDER BY ver ASC", new String[]{String.valueOf(j), String.valueOf(i)}, new Function1() { // from class: ni.shikatu.re_extera.data.EditHistoryStore.3
            public final Object invoke(Object obj) {
                return EditHistoryStore.loadVersions$lambda$6$lambda$5(arrayList, (Cursor) obj);
            }
        });
        return arrayList;
    }

    static final Unit loadVersions$lambda$6$lambda$5(ArrayList arrayList, Cursor cursor) {
        Intrinsics.checkNotNullParameter(cursor, "cursor");
        TLRPC.Message messageDeserialize = INSTANCE.deserialize(cursor.getBlob(0));
        if (messageDeserialize != null) {
            arrayList.add(messageDeserialize);
        }
        return Unit.INSTANCE;
    }

    public final void remove(final long dialogId, final Collection<Integer> messageIds) {
        Intrinsics.checkNotNullParameter(messageIds, "messageIds");
        if (messageIds.isEmpty()) {
            return;
        }
        Set<Integer> set = edited.get(Long.valueOf(dialogId));
        if (set != null) {
            set.removeAll(CollectionsKt.toSet(messageIds));
        }
        ReDatabase.INSTANCE.getDb().write(new Function1() { // from class: ni.shikatu.re_extera.data.EditHistoryStore.0
            public final Object invoke(Object obj) {
                return EditHistoryStore.remove$lambda$7(messageIds, dialogId, (SQLiteDatabase) obj);
            }
        });
    }

    static final Unit remove$lambda$7(Collection collection, long j, SQLiteDatabase sQLiteDatabase) {
        Intrinsics.checkNotNullParameter(sQLiteDatabase, "$this$write");
        sQLiteDatabase.delete("message_edits", "did = ? AND mid IN (" + ReDatabaseKt.sqlList(collection) + ")", new String[]{String.valueOf(j)});
        return Unit.INSTANCE;
    }

    public final void clear() {
        edited.clear();
        ReDatabase.INSTANCE.getDb().write(new Function1() { // from class: ni.shikatu.re_extera.data.EditHistoryStore.1
            public final Object invoke(Object obj) {
                return EditHistoryStore.clear$lambda$8((SQLiteDatabase) obj);
            }
        });
    }

    static final Unit clear$lambda$8(SQLiteDatabase sQLiteDatabase) {
        Intrinsics.checkNotNullParameter(sQLiteDatabase, "$this$write");
        sQLiteDatabase.delete("message_edits", null, null);
        return Unit.INSTANCE;
    }

    private final Set<Integer> idsOf(long dialogId) {
        Set<Integer> setPutIfAbsent;
        ConcurrentHashMap<Long, Set<Integer>> concurrentHashMap = edited;
        Long lValueOf = Long.valueOf(dialogId);
        ConcurrentHashMap.KeySetView keySetViewNewKeySet = concurrentHashMap.get(lValueOf);
        if (keySetViewNewKeySet == null && (setPutIfAbsent = concurrentHashMap.putIfAbsent(lValueOf, (keySetViewNewKeySet = ConcurrentHashMap.newKeySet()))) != null) {
            keySetViewNewKeySet = setPutIfAbsent;
        }
        Intrinsics.checkNotNullExpressionValue(keySetViewNewKeySet, "getOrPut(...)");
        return keySetViewNewKeySet;
    }

    private final void bindVersion(SQLiteStatement sQLiteStatement, long j, int i, int i2, int i3, byte[] bArr) {
        sQLiteStatement.clearBindings();
        sQLiteStatement.bindLong(1, j);
        sQLiteStatement.bindLong(2, i);
        sQLiteStatement.bindLong(3, i2);
        sQLiteStatement.bindLong(4, i3);
        sQLiteStatement.bindBlob(5, bArr);
    }

    private final byte[] serialize(TLRPC.Message message) {
        try {
            OutputSerializedData serializedData = new SerializedData(message.getObjectSize());
            message.serializeToStream(serializedData);
            byte[] byteArray = serializedData.toByteArray();
            serializedData.cleanup();
            return byteArray;
        } catch (Exception e) {
            ReLog.INSTANCE.e("Failed to serialize message " + message.id, e);
            return null;
        }
    }

    private final TLRPC.Message deserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try {
            InputSerializedData serializedData = new SerializedData(bytes);
            TLRPC.Message messageTLdeserialize = TLRPC.Message.TLdeserialize(serializedData, serializedData.readInt32(false), false);
            serializedData.cleanup();
            return messageTLdeserialize;
        } catch (Exception e) {
            ReLog.INSTANCE.e("Failed to deserialize message version", e);
            return null;
        }
    }
}
