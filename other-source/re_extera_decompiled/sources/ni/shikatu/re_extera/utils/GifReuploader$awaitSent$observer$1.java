package ni.shikatu.re_extera.utils;

import java.util.HashMap;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.NotificationCenter;
import org.telegram.tgnet.TLRPC;

/* JADX INFO: compiled from: GifReuploader.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000%\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J5\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0016\u0010\u0007\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010\t0\b\"\u0004\u0018\u00010\tH\u0016¢\u0006\u0002\u0010\n¨\u0006\u000b"}, d2 = {"ni/shikatu/re_extera/utils/GifReuploader$awaitSent$observer$1", "Lorg/telegram/messenger/NotificationCenter$NotificationCenterDelegate;", "didReceivedNotification", "", "id", "", "account", "args", "", "", "(II[Ljava/lang/Object;)V", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class GifReuploader$awaitSent$observer$1 implements NotificationCenter.NotificationCenterDelegate {
    final /* synthetic */ NotificationCenter $center;
    final /* synthetic */ long $selfId;
    final /* synthetic */ String $token;

    GifReuploader$awaitSent$observer$1(String str, NotificationCenter notificationCenter, long j) {
        this.$token = str;
        this.$center = notificationCenter;
        this.$selfId = j;
    }

    public void didReceivedNotification(int id, final int account, Object... args) {
        Intrinsics.checkNotNullParameter(args, "args");
        Object orNull = ArraysKt.getOrNull(args, 2);
        final TLRPC.Message message = orNull instanceof TLRPC.Message ? (TLRPC.Message) orNull : null;
        if (message == null) {
            return;
        }
        HashMap map = message.params;
        if (Intrinsics.areEqual(map != null ? (String) map.get("re_extera_gif_save") : null, this.$token)) {
            this.$center.removeObserver(this, NotificationCenter.messageReceivedByServer);
            final long j = this.$selfId;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: ni.shikatu.re_extera.utils.GifReuploader$awaitSent$observer$1.0
                @Override // java.lang.Runnable
                public final void run() {
                    GifReuploader$awaitSent$observer$1.didReceivedNotification$lambda$0(account, j, message);
                }
            }, 500L);
        }
    }

    static final void didReceivedNotification$lambda$0(int i, long j, TLRPC.Message message) {
        GifReuploader.INSTANCE.onSent(i, j, message);
    }
}
