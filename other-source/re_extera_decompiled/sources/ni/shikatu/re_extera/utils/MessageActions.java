package ni.shikatu.re_extera.utils;

import android.view.View;
import android.view.Window;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import ni.shikatu.re_extera.data.DeletedMessagesStore;
import ni.shikatu.re_extera.data.EditHistoryStore;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.LaunchActivity;

/* JADX INFO: compiled from: MessageActions.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010#\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u001e\n\u0002\b\u0003\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0006J(\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u00062\u0010\b\u0002\u0010\u000e\u001a\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\u000fJ*\u0010\u0010\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\b\u0010\u0011\u001a\u0004\u0018\u00010\u00122\u0006\u0010\u0013\u001a\u00020\r2\b\b\u0002\u0010\u0014\u001a\u00020\bJ\u0010\u0010\u0010\u001a\u00020\u000b2\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016J\u0010\u0010\u0017\u001a\u00020\u000b2\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016J$\u0010\u0018\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u0019\u001a\u00020\u001a2\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\r0\u001cJ\u0016\u0010\u001d\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u0019\u001a\u00020\u001aJ\b\u0010\u001e\u001a\u00020\u000bH\u0002R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001f"}, d2 = {"Lni/shikatu/re_extera/utils/MessageActions;", "", "<init>", "()V", "unfilteredRequests", "", "Lorg/telegram/tgnet/TLObject;", "isUnfiltered", "", "request", "sendUnfiltered", "", "account", "", "onDone", "Lkotlin/Function0;", "markRead", "peer", "Lorg/telegram/tgnet/TLRPC$InputPeer;", "maxId", "haptic", "message", "Lorg/telegram/messenger/MessageObject;", "markOneTimeContentRead", "purge", "dialogId", "", "messageIds", "", "purgeDialog", "vibrate", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class MessageActions {
    public static final MessageActions INSTANCE = new MessageActions();
    private static final Set<TLObject> unfilteredRequests;

    private MessageActions() {
    }

    static {
        Set<TLObject> setNewSetFromMap = Collections.newSetFromMap(new ConcurrentHashMap());
        Intrinsics.checkNotNullExpressionValue(setNewSetFromMap, "newSetFromMap(...)");
        unfilteredRequests = setNewSetFromMap;
    }

    public final boolean isUnfiltered(TLObject request) {
        Intrinsics.checkNotNullParameter(request, "request");
        return unfilteredRequests.remove(request);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ void sendUnfiltered$default(MessageActions messageActions, int i, TLObject tLObject, Function0 function0, int i2, Object obj) {
        if ((i2 & 4) != 0) {
            function0 = null;
        }
        messageActions.sendUnfiltered(i, tLObject, function0);
    }

    public final void sendUnfiltered(int account, TLObject request, final Function0<Unit> onDone) {
        Intrinsics.checkNotNullParameter(request, "request");
        unfilteredRequests.add(request);
        ConnectionsManager.getInstance(account).sendRequest(request, new RequestDelegate() { // from class: ni.shikatu.re_extera.utils.MessageActions.1
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                MessageActions.sendUnfiltered$lambda$0(onDone, tLObject, tL_error);
            }
        });
    }

    static final void sendUnfiltered$lambda$0(Function0 function0, TLObject tLObject, TLRPC.TL_error tL_error) {
        if (function0 != null) {
            function0.invoke();
        }
    }

    public static /* synthetic */ void markRead$default(MessageActions messageActions, int i, TLRPC.InputPeer inputPeer, int i2, boolean z, int i3, Object obj) {
        if ((i3 & 8) != 0) {
            z = false;
        }
        messageActions.markRead(i, inputPeer, i2, z);
    }

    public final void markRead(int account, TLRPC.InputPeer peer, int maxId, final boolean haptic) {
        TLObject tLObject;
        if (peer == null) {
            return;
        }
        if (peer.channel_id != 0) {
            TLRPC.TL_channels_readHistory tL_channels_readHistory = new TLRPC.TL_channels_readHistory();
            tL_channels_readHistory.channel = MessagesController.getInputChannel(peer);
            tL_channels_readHistory.max_id = maxId;
            tLObject = (TLObject) tL_channels_readHistory;
        } else {
            TLRPC.TL_messages_readHistory tL_messages_readHistory = new TLRPC.TL_messages_readHistory();
            tL_messages_readHistory.peer = peer;
            tL_messages_readHistory.max_id = maxId;
            tLObject = (TLObject) tL_messages_readHistory;
        }
        sendUnfiltered(account, tLObject, new Function0() { // from class: ni.shikatu.re_extera.utils.MessageActions.2
            public final Object invoke() {
                return MessageActions.markRead$lambda$3(haptic);
            }
        });
    }

    static final Unit markRead$lambda$3(boolean z) {
        if (z) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: ni.shikatu.re_extera.utils.MessageActions.3
                @Override // java.lang.Runnable
                public final void run() {
                    MessageActions.this.vibrate();
                }
            });
        }
        return Unit.INSTANCE;
    }

    public final void markRead(MessageObject message) {
        if (message == null) {
            return;
        }
        int i = message.currentAccount;
        message.setIsRead();
        message.setContentIsRead();
        if (!DialogObject.isEncryptedDialog(message.getDialogId())) {
            markRead(i, MessagesController.getInstance(i).getInputPeer(message.getDialogId()), message.getId(), true);
        }
        if (MessageActionsKt.isOneTime(message)) {
            markOneTimeContentRead(message);
        }
    }

    public final void markOneTimeContentRead(MessageObject message) {
        TLObject tLObject;
        if (message == null || DialogObject.isEncryptedDialog(message.getDialogId()) || !message.isSecretMedia()) {
            return;
        }
        int i = message.currentAccount;
        TLRPC.InputPeer inputPeer = MessagesController.getInstance(i).getInputPeer(message.getDialogId());
        if (inputPeer != null && inputPeer.channel_id != 0) {
            TLRPC.TL_channels_readMessageContents tL_channels_readMessageContents = new TLRPC.TL_channels_readMessageContents();
            tL_channels_readMessageContents.channel = MessagesController.getInputChannel(inputPeer);
            tL_channels_readMessageContents.id.add(Integer.valueOf(message.getId()));
            tLObject = (TLObject) tL_channels_readMessageContents;
        } else {
            TLObject tL_messages_readMessageContents = new TLRPC.TL_messages_readMessageContents();
            ((TLRPC.TL_messages_readMessageContents) tL_messages_readMessageContents).id.add(Integer.valueOf(message.getId()));
            tLObject = tL_messages_readMessageContents;
        }
        sendUnfiltered$default(this, i, tLObject, null, 4, null);
        message.setContentIsRead();
        TLRPC.Message message2 = message.messageOwner;
        if (message2.ttl <= 0 || message2.ttl == Integer.MAX_VALUE) {
            return;
        }
        message2.destroyTime = message2.ttl + ConnectionsManager.getInstance(i).getCurrentTime();
        NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.updateMessageMedia, new Object[]{message2});
    }

    public final void purge(final int account, final long dialogId, Collection<Integer> messageIds) {
        Intrinsics.checkNotNullParameter(messageIds, "messageIds");
        if (messageIds.isEmpty()) {
            return;
        }
        final ArrayList arrayList = new ArrayList(messageIds);
        ArrayList arrayList2 = arrayList;
        DeletedMessagesStore.INSTANCE.remove(dialogId, arrayList2);
        EditHistoryStore.INSTANCE.remove(dialogId, arrayList2);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: ni.shikatu.re_extera.utils.MessageActions.0
            @Override // java.lang.Runnable
            public final void run() {
                MessageActions.purge$lambda$6(dialogId, arrayList, account);
            }
        });
    }

    static final void purge$lambda$6(long j, ArrayList arrayList, int i) {
        DeletionBypass.INSTANCE.allow(j, arrayList);
        MessagesController.getInstance(i).deleteMessages(arrayList, (ArrayList) null, (TLRPC.EncryptedChat) null, j, 0, false, 0, true);
    }

    public final void purgeDialog(int account, long dialogId) {
        purge(account, dialogId, DeletedMessagesStore.INSTANCE.ids(dialogId));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void vibrate() {
        Window window;
        View decorView;
        LaunchActivity launchActivity = LaunchActivity.instance;
        if (launchActivity == null || (window = launchActivity.getWindow()) == null || (decorView = window.getDecorView()) == null) {
            return;
        }
        decorView.performHapticFeedback(3);
    }
}
