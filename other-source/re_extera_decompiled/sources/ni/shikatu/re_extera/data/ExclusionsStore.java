package ni.shikatu.re_extera.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: ExclusionsStore.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0003\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bJ\u0011\u0010\f\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u0006H\u0086\u0002J\u001d\u0010\u000e\u001a\u00020\u000f2\b\u0010\r\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0010\u001a\u00020\u0011¢\u0006\u0002\u0010\u0012J\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00070\u0014J\u001e\u0010\u0015\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u000e\u001a\u00020\u000fJ\u000e\u0010\u0016\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\u0006R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0017"}, d2 = {"Lni/shikatu/re_extera/data/ExclusionsStore;", "", "<init>", "()V", "exclusions", "Ljava/util/concurrent/ConcurrentHashMap;", "", "Lni/shikatu/re_extera/data/DialogExclusion;", "load", "", "db", "Landroid/database/sqlite/SQLiteDatabase;", "get", "dialogId", "mode", "Lni/shikatu/re_extera/data/ExclusionMode;", "kind", "Lni/shikatu/re_extera/data/ExclusionKind;", "(Ljava/lang/Long;Lni/shikatu/re_extera/data/ExclusionKind;)Lni/shikatu/re_extera/data/ExclusionMode;", "all", "", "set", "remove", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class ExclusionsStore {
    public static final ExclusionsStore INSTANCE = new ExclusionsStore();
    private static final ConcurrentHashMap<Long, DialogExclusion> exclusions = new ConcurrentHashMap<>();

    /* JADX INFO: compiled from: ExclusionsStore.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    public static final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[ExclusionKind.values().length];
            try {
                iArr[ExclusionKind.READING.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[ExclusionKind.TYPING.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    private ExclusionsStore() {
    }

    public final void load(SQLiteDatabase db) {
        Intrinsics.checkNotNullParameter(db, "db");
        exclusions.clear();
        ReDatabaseKt.forEachRow(db, "SELECT did, exception_reading, exception_typing FROM exception_users", new String[0], new Function1() { // from class: ni.shikatu.re_extera.data.ExclusionsStore.2
            public final Object invoke(Object obj) {
                return ExclusionsStore.load$lambda$0((Cursor) obj);
            }
        });
    }

    static final Unit load$lambda$0(Cursor cursor) {
        Intrinsics.checkNotNullParameter(cursor, "it");
        DialogExclusion dialogExclusion = new DialogExclusion(cursor.getLong(0), ExclusionMode.INSTANCE.of(cursor.getInt(1)), ExclusionMode.INSTANCE.of(cursor.getInt(2)));
        if (!dialogExclusion.isEmpty()) {
            exclusions.put(Long.valueOf(dialogExclusion.getDialogId()), dialogExclusion);
        }
        return Unit.INSTANCE;
    }

    public final DialogExclusion get(long dialogId) {
        DialogExclusion dialogExclusion = exclusions.get(Long.valueOf(dialogId));
        return dialogExclusion == null ? new DialogExclusion(dialogId, null, null, 6, null) : dialogExclusion;
    }

    public final ExclusionMode mode(Long dialogId, ExclusionKind kind) {
        Intrinsics.checkNotNullParameter(kind, "kind");
        if (dialogId != null) {
            DialogExclusion dialogExclusion = exclusions.get(Long.valueOf(dialogId.longValue()));
            ExclusionMode exclusionMode = dialogExclusion != null ? dialogExclusion.get(kind) : null;
            if (exclusionMode != null) {
                return exclusionMode;
            }
        }
        return ExclusionMode.DEFAULT;
    }

    public final List<DialogExclusion> all() {
        Collection<DialogExclusion> collectionValues = exclusions.values();
        Intrinsics.checkNotNullExpressionValue(collectionValues, "<get-values>(...)");
        return CollectionsKt.toList(collectionValues);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: kotlin.NoWhenBranchMatchedException */
    public final void set(final long dialogId, ExclusionKind kind, ExclusionMode mode) throws NoWhenBranchMatchedException {
        final DialogExclusion dialogExclusionCopy$default;
        Intrinsics.checkNotNullParameter(kind, "kind");
        Intrinsics.checkNotNullParameter(mode, "mode");
        DialogExclusion dialogExclusion = get(dialogId);
        int i = WhenMappings.$EnumSwitchMapping$0[kind.ordinal()];
        if (i == 1) {
            dialogExclusionCopy$default = DialogExclusion.copy$default(dialogExclusion, 0L, mode, null, 5, null);
        } else {
            if (i != 2) {
                throw new NoWhenBranchMatchedException();
            }
            dialogExclusionCopy$default = DialogExclusion.copy$default(dialogExclusion, 0L, null, mode, 3, null);
        }
        if (dialogExclusionCopy$default.isEmpty()) {
            remove(dialogId);
        } else {
            exclusions.put(Long.valueOf(dialogId), dialogExclusionCopy$default);
            ReDatabase.INSTANCE.getDb().write(new Function1() { // from class: ni.shikatu.re_extera.data.ExclusionsStore.1
                public final Object invoke(Object obj) {
                    return ExclusionsStore.set$lambda$2(dialogId, dialogExclusionCopy$default, (SQLiteDatabase) obj);
                }
            });
        }
    }

    static final Unit set$lambda$2(long j, DialogExclusion dialogExclusion, SQLiteDatabase sQLiteDatabase) {
        Intrinsics.checkNotNullParameter(sQLiteDatabase, "$this$write");
        sQLiteDatabase.execSQL("INSERT OR REPLACE INTO exception_users(did, exception_reading, exception_typing) VALUES(?, ?, ?)", new Object[]{Long.valueOf(j), Integer.valueOf(dialogExclusion.getReading().getValue()), Integer.valueOf(dialogExclusion.getTyping().getValue())});
        return Unit.INSTANCE;
    }

    public final void remove(final long dialogId) {
        exclusions.remove(Long.valueOf(dialogId));
        ReDatabase.INSTANCE.getDb().write(new Function1() { // from class: ni.shikatu.re_extera.data.ExclusionsStore.0
            public final Object invoke(Object obj) {
                return ExclusionsStore.remove$lambda$3(dialogId, (SQLiteDatabase) obj);
            }
        });
    }

    static final Unit remove$lambda$3(long j, SQLiteDatabase sQLiteDatabase) {
        Intrinsics.checkNotNullParameter(sQLiteDatabase, "$this$write");
        sQLiteDatabase.delete("exception_users", "did = ?", new String[]{String.valueOf(j)});
        return Unit.INSTANCE;
    }
}
