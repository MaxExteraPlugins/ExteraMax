package ni.shikatu.re_extera.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.collection.LongSparseArray;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;

/* JADX INFO: compiled from: ShadowbanStore.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0010 \n\u0002\b\u0006\n\u0002\u0010\b\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bJ\u0013\u0010\u000f\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u0010\u001a\u00020\u0006H\u0086\u0002J\u0011\u0010\u0011\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\u0006H\u0086\u0002J\u000e\u0010\u0012\u001a\u00020\r2\u0006\u0010\u0013\u001a\u00020\u0006J\u000e\u0010\u0014\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\u0006J\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00070\u0016J\u001e\u0010\u0017\u001a\u00020\t2\u0006\u0010\u0010\u001a\u00020\u00062\u0006\u0010\u0018\u001a\u00020\r2\u0006\u0010\u0019\u001a\u00020\rJ\u000e\u0010\u001a\u001a\u00020\t2\u0006\u0010\u0010\u001a\u00020\u0006J\u000e\u0010\u001b\u001a\u00020\t2\u0006\u0010\u001c\u001a\u00020\u001dR\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\f\u001a\u00020\r8F¢\u0006\u0006\u001a\u0004\b\f\u0010\u000e¨\u0006\u001e"}, d2 = {"Lni/shikatu/re_extera/data/ShadowbanStore;", "", "<init>", "()V", "entries", "Ljava/util/concurrent/ConcurrentHashMap;", "", "Lni/shikatu/re_extera/data/ShadowbanEntry;", "load", "", "db", "Landroid/database/sqlite/SQLiteDatabase;", "isEmpty", "", "()Z", "get", "userId", "contains", "hidesDialog", "dialogId", "hidesInGroups", "all", "", "put", "hideDialog", "hideInGroups", "remove", "notifyDialogsChanged", "account", "", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class ShadowbanStore {
    public static final ShadowbanStore INSTANCE = new ShadowbanStore();
    private static final ConcurrentHashMap<Long, ShadowbanEntry> entries = new ConcurrentHashMap<>();

    private ShadowbanStore() {
    }

    public final void load(SQLiteDatabase db) {
        Intrinsics.checkNotNullParameter(db, "db");
        entries.clear();
        ReDatabaseKt.forEachRow(db, "SELECT user_id, hide_dialog, hide_in_groups, added_ts FROM shadowban_users", new String[0], new Function1() { // from class: ni.shikatu.re_extera.data.ShadowbanStore.2
            public final Object invoke(Object obj) {
                return ShadowbanStore.load$lambda$0((Cursor) obj);
            }
        });
    }

    /*  JADX ERROR: JadxRuntimeException in pass: IfRegionVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: No SSA var for result arg: (r5 I:??) from 0x0020: PHI (r5 I:??) = (r4v0 boolean), (r5v4 boolean) binds: [B:7:0x001c, B:9:0x001f] A[DONT_GENERATE, DONT_INLINE, REMOVE]
        	at jadx.core.dex.nodes.InsnNode.rebindArgs(InsnNode.java:506)
        	at jadx.core.dex.instructions.mods.TernaryInsn.rebindArgs(TernaryInsn.java:100)
        	at jadx.core.dex.visitors.regions.TernaryMod.replaceWithTernary(TernaryMod.java:353)
        	at jadx.core.dex.visitors.regions.TernaryMod.processOneBranchTernary(TernaryMod.java:284)
        	at jadx.core.dex.visitors.regions.TernaryMod.makeTernaryInsn(TernaryMod.java:82)
        	at jadx.core.dex.visitors.regions.TernaryMod.processRegion(TernaryMod.java:67)
        	at jadx.core.dex.visitors.regions.TernaryMod.enterRegion(TernaryMod.java:50)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:96)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverse(DepthRegionTraversal.java:27)
        	at jadx.core.dex.visitors.regions.TernaryMod.process(TernaryMod.java:36)
        	at jadx.core.dex.visitors.regions.IfRegionVisitor.process(IfRegionVisitor.java:44)
        	at jadx.core.dex.visitors.regions.IfRegionVisitor.visit(IfRegionVisitor.java:30)
        */
    static final kotlin.Unit load$lambda$0(android.database.Cursor r8) {
        /*
            java.lang.String r0 = "it"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r8, r0)
            ni.shikatu.re_extera.data.ShadowbanEntry r1 = new ni.shikatu.re_extera.data.ShadowbanEntry
            r0 = 0
            long r2 = r8.getLong(r0)
            r4 = 1
            int r5 = r8.getInt(r4)
            if (r5 != r4) goto L15
            r5 = r4
            goto L17
        L15:
            r5 = r4
            r4 = r0
        L17:
            r6 = 2
            int r6 = r8.getInt(r6)
            if (r6 != r5) goto L1f
            goto L20
        L1f:
            r5 = r0
        L20:
            r0 = 3
            long r6 = r8.getLong(r0)
            r1.<init>(r2, r4, r5, r6)
            java.util.concurrent.ConcurrentHashMap<java.lang.Long, ni.shikatu.re_extera.data.ShadowbanEntry> r8 = ni.shikatu.re_extera.data.ShadowbanStore.entries
            java.util.Map r8 = (java.util.Map) r8
            long r2 = r1.getUserId()
            java.lang.Long r0 = java.lang.Long.valueOf(r2)
            r8.put(r0, r1)
            kotlin.Unit r8 = kotlin.Unit.INSTANCE
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: ni.shikatu.re_extera.data.ShadowbanStore.load$lambda$0(android.database.Cursor):kotlin.Unit");
    }

    public final boolean isEmpty() {
        return entries.isEmpty();
    }

    public final ShadowbanEntry get(long userId) {
        return entries.get(Long.valueOf(userId));
    }

    public final boolean contains(long userId) {
        return entries.containsKey(Long.valueOf(userId));
    }

    public final boolean hidesDialog(long dialogId) {
        ShadowbanEntry shadowbanEntry;
        return dialogId > 0 && (shadowbanEntry = entries.get(Long.valueOf(dialogId))) != null && shadowbanEntry.getHideDialog();
    }

    public final boolean hidesInGroups(long userId) {
        ShadowbanEntry shadowbanEntry;
        return userId > 0 && (shadowbanEntry = entries.get(Long.valueOf(userId))) != null && shadowbanEntry.getHideInGroups();
    }

    public final List<ShadowbanEntry> all() {
        Collection<ShadowbanEntry> collectionValues = entries.values();
        Intrinsics.checkNotNullExpressionValue(collectionValues, "<get-values>(...)");
        return CollectionsKt.sortedWith(collectionValues, new Comparator() { // from class: ni.shikatu.re_extera.data.ShadowbanStore$all$$inlined$sortedByDescending$1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                return ComparisonsKt.compareValues(Long.valueOf(((ShadowbanEntry) t2).getAddedAt()), Long.valueOf(((ShadowbanEntry) t).getAddedAt()));
            }
        });
    }

    public final void put(final long userId, final boolean hideDialog, final boolean hideInGroups) {
        ConcurrentHashMap<Long, ShadowbanEntry> concurrentHashMap = entries;
        ShadowbanEntry shadowbanEntry = concurrentHashMap.get(Long.valueOf(userId));
        final ShadowbanEntry shadowbanEntry2 = new ShadowbanEntry(userId, hideDialog, hideInGroups, shadowbanEntry != null ? shadowbanEntry.getAddedAt() : System.currentTimeMillis());
        concurrentHashMap.put(Long.valueOf(userId), shadowbanEntry2);
        ReDatabase.INSTANCE.getDb().write(new Function1() { // from class: ni.shikatu.re_extera.data.ShadowbanStore.3
            public final Object invoke(Object obj) {
                return ShadowbanStore.put$lambda$2(userId, hideDialog, hideInGroups, shadowbanEntry2, (SQLiteDatabase) obj);
            }
        });
    }

    static final Unit put$lambda$2(long j, boolean z, boolean z2, ShadowbanEntry shadowbanEntry, SQLiteDatabase sQLiteDatabase) {
        Intrinsics.checkNotNullParameter(sQLiteDatabase, "$this$write");
        sQLiteDatabase.execSQL("INSERT OR REPLACE INTO shadowban_users(user_id, hide_dialog, hide_in_groups, added_ts) VALUES(?, ?, ?, ?)", new Object[]{Long.valueOf(j), Integer.valueOf(z ? 1 : 0), Integer.valueOf(z2 ? 1 : 0), Long.valueOf(shadowbanEntry.getAddedAt())});
        return Unit.INSTANCE;
    }

    public final void remove(final long userId) {
        entries.remove(Long.valueOf(userId));
        ReDatabase.INSTANCE.getDb().write(new Function1() { // from class: ni.shikatu.re_extera.data.ShadowbanStore.1
            public final Object invoke(Object obj) {
                return ShadowbanStore.remove$lambda$3(userId, (SQLiteDatabase) obj);
            }
        });
    }

    static final Unit remove$lambda$3(long j, SQLiteDatabase sQLiteDatabase) {
        Intrinsics.checkNotNullParameter(sQLiteDatabase, "$this$write");
        sQLiteDatabase.delete("shadowban_users", "user_id = ?", new String[]{String.valueOf(j)});
        return Unit.INSTANCE;
    }

    public final void notifyDialogsChanged(final int account) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: ni.shikatu.re_extera.data.ShadowbanStore.0
            @Override // java.lang.Runnable
            public final void run() {
                ShadowbanStore.notifyDialogsChanged$lambda$5(account);
            }
        });
    }

    static final void notifyDialogsChanged$lambda$5(int i) {
        MessagesController.getInstance(i).sortDialogs((LongSparseArray) null);
        NotificationCenter notificationCenter = NotificationCenter.getInstance(i);
        notificationCenter.postNotificationName(NotificationCenter.dialogsNeedReload, new Object[]{true});
        notificationCenter.postNotificationName(NotificationCenter.dialogsUnreadCounterChanged, new Object[]{0});
        notificationCenter.postNotificationName(NotificationCenter.updateInterfaces, new Object[]{Integer.valueOf(MessagesController.UPDATE_MASK_ALL)});
    }
}
