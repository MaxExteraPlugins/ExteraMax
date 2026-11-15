package ni.shikatu.re_extera.utils;

import android.os.SystemClock;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: DeletionBypass.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u001e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u001c\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u00052\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\t0\u000fJ\u0016\u0010\u0010\u001a\u00020\u00112\u0006\u0010\r\u001a\u00020\u00052\u0006\u0010\u0012\u001a\u00020\tJ\u0016\u0010\u0013\u001a\u00020\u00112\u0006\u0010\r\u001a\u00020\u00052\u0006\u0010\u0012\u001a\u00020\tJ8\u0010\u0014\u001a\u00020\u0011*\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\t0\b\u0012\u0004\u0012\u00020\u00050\u00072\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\t0\bH\u0002J\b\u0010\u0016\u001a\u00020\fH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R&\u0010\u0006\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\t0\b\u0012\u0004\u0012\u00020\u00050\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R&\u0010\n\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\t0\b\u0012\u0004\u0012\u00020\u00050\u0007X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0017"}, d2 = {"Lni/shikatu/re_extera/utils/DeletionBypass;", "", "<init>", "()V", "TTL_MS", "", "storage", "Ljava/util/concurrent/ConcurrentHashMap;", "Lkotlin/Pair;", "", "ui", "allow", "", "dialogId", "messageIds", "", "consumeStorage", "", "messageId", "consumeUi", "consume", "key", "evictExpired", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class DeletionBypass {
    private static final long TTL_MS = 60000;
    public static final DeletionBypass INSTANCE = new DeletionBypass();
    private static final ConcurrentHashMap<Pair<Long, Integer>, Long> storage = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Pair<Long, Integer>, Long> ui = new ConcurrentHashMap<>();

    private DeletionBypass() {
    }

    public final void allow(long dialogId, Collection<Integer> messageIds) {
        Intrinsics.checkNotNullParameter(messageIds, "messageIds");
        long jElapsedRealtime = SystemClock.elapsedRealtime() + TTL_MS;
        evictExpired();
        Iterator<Integer> it = messageIds.iterator();
        while (it.hasNext()) {
            Pair<Long, Integer> pair = TuplesKt.to(Long.valueOf(dialogId), Integer.valueOf(it.next().intValue()));
            storage.put(pair, Long.valueOf(jElapsedRealtime));
            ui.put(pair, Long.valueOf(jElapsedRealtime));
        }
    }

    public final boolean consumeStorage(long dialogId, int messageId) {
        return consume(storage, TuplesKt.to(Long.valueOf(dialogId), Integer.valueOf(messageId)));
    }

    public final boolean consumeUi(long dialogId, int messageId) {
        return consume(ui, TuplesKt.to(Long.valueOf(dialogId), Integer.valueOf(messageId)));
    }

    private final boolean consume(ConcurrentHashMap<Pair<Long, Integer>, Long> concurrentHashMap, Pair<Long, Integer> pair) {
        Long lRemove = concurrentHashMap.remove(pair);
        return lRemove != null && lRemove.longValue() >= SystemClock.elapsedRealtime();
    }

    private final void evictExpired() {
        final long jElapsedRealtime = SystemClock.elapsedRealtime();
        Collection<Long> collectionValues = storage.values();
        final Function1 function1 = new Function1() { // from class: ni.shikatu.re_extera.utils.DeletionBypass.0
            public final Object invoke(Object obj) {
                return Boolean.valueOf(DeletionBypass.evictExpired$lambda$0(jElapsedRealtime, (Long) obj));
            }
        };
        collectionValues.removeIf(new Predicate() { // from class: ni.shikatu.re_extera.utils.DeletionBypass.1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return DeletionBypass.evictExpired$lambda$1(function1, obj);
            }
        });
        Collection<Long> collectionValues2 = ui.values();
        final Function1 function2 = new Function1() { // from class: ni.shikatu.re_extera.utils.DeletionBypass.2
            public final Object invoke(Object obj) {
                return Boolean.valueOf(DeletionBypass.evictExpired$lambda$2(jElapsedRealtime, (Long) obj));
            }
        };
        collectionValues2.removeIf(new Predicate() { // from class: ni.shikatu.re_extera.utils.DeletionBypass.3
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return DeletionBypass.evictExpired$lambda$3(function2, obj);
            }
        });
    }

    static final boolean evictExpired$lambda$0(long j, Long l) {
        Intrinsics.checkNotNullParameter(l, "it");
        return l.longValue() < j;
    }

    static final boolean evictExpired$lambda$1(Function1 function1, Object obj) {
        return ((Boolean) function1.invoke(obj)).booleanValue();
    }

    static final boolean evictExpired$lambda$2(long j, Long l) {
        Intrinsics.checkNotNullParameter(l, "it");
        return l.longValue() < j;
    }

    static final boolean evictExpired$lambda$3(Function1 function1, Object obj) {
        return ((Boolean) function1.invoke(obj)).booleanValue();
    }
}
