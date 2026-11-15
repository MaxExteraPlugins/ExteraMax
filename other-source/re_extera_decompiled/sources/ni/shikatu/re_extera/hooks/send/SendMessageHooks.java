package ni.shikatu.re_extera.hooks.send;

import android.os.SystemClock;
import de.robv.android.xposed.XC_MethodHook;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import ni.shikatu.extensions.HookUtilsKt;
import ni.shikatu.re_extera.data.DeletedMessagesStore;
import ni.shikatu.re_extera.hooks.HookerClass;
import ni.shikatu.re_extera.settings.ReSettings;
import ni.shikatu.re_extera.utils.ChatUtils;
import ni.shikatu.re_extera.utils.TelegramStorageKt;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BaseController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ChatActivity;

/* JADX INFO: compiled from: SendMessageHooks.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u0006\u001a\u00020\u0007H\u0014J\u0018\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0002J\u0018\u0010\r\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0002J$\u0010\u000e\u001a\u00020\n2\u0006\u0010\t\u001a\u00020\n2\b\u0010\u000f\u001a\u0004\u0018\u00010\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0002J\u0010\u0010\u0013\u001a\u00020\n2\u0006\u0010\u0014\u001a\u00020\u0005H\u0002J\u0018\u0010\u0015\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0016"}, d2 = {"Lni/shikatu/re_extera/hooks/send/SendMessageHooks;", "Lni/shikatu/re_extera/hooks/HookerClass;", "<init>", "()V", "suppressScheduledUntil", "", "setup", "", "quoteDeletedReply", "account", "", "params", "Lorg/telegram/messenger/SendMessagesHelper$SendMessageParams;", "scheduleImmediately", "scheduleTime", "photo", "Lorg/telegram/tgnet/TLRPC$TL_photo;", "document", "Lorg/telegram/tgnet/TLRPC$TL_document;", "uploadSeconds", "bytes", "replaceCustomEmoji", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class SendMessageHooks extends HookerClass {
    private volatile long suppressScheduledUntil;

    @Override // ni.shikatu.re_extera.hooks.HookerClass
    protected void setup() {
        hookBefore(SendMessagesHelper.class, "sendMessage", new Class[]{SendMessagesHelper.SendMessageParams.class}, new Function1() { // from class: ni.shikatu.re_extera.hooks.send.SendMessageHooks.0
            public final Object invoke(Object obj) {
                return SendMessageHooks.setup$lambda$0(SendMessageHooks.this, (XC_MethodHook.MethodHookParam) obj);
            }
        });
        hookBefore(ChatActivity.class, "openScheduledMessages", new Class[]{Integer.TYPE, Boolean.TYPE}, new Function1() { // from class: ni.shikatu.re_extera.hooks.send.SendMessageHooks.1
            public final Object invoke(Object obj) {
                return SendMessageHooks.setup$lambda$1(SendMessageHooks.this, (XC_MethodHook.MethodHookParam) obj);
            }
        });
    }

    static final Unit setup$lambda$0(SendMessageHooks sendMessageHooks, XC_MethodHook.MethodHookParam methodHookParam) {
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        SendMessagesHelper.SendMessageParams sendMessageParams = (SendMessagesHelper.SendMessageParams) HookUtilsKt.arg(methodHookParam, 0);
        if (sendMessageParams == null) {
            return Unit.INSTANCE;
        }
        int account = TelegramStorageKt.getAccount((BaseController) HookUtilsKt.self(methodHookParam));
        sendMessageHooks.quoteDeletedReply(account, sendMessageParams);
        if (sendMessageParams.scheduleDate == 0 && ReSettings.INSTANCE.getUseSchedule() && !DialogObject.isEncryptedDialog(sendMessageParams.peer)) {
            sendMessageHooks.scheduleImmediately(account, sendMessageParams);
        }
        if (ReSettings.INSTANCE.getLocalPremium()) {
            sendMessageHooks.replaceCustomEmoji(account, sendMessageParams);
        }
        if (ReSettings.INSTANCE.getSendSilently()) {
            sendMessageParams.notify = false;
        }
        return Unit.INSTANCE;
    }

    static final Unit setup$lambda$1(SendMessageHooks sendMessageHooks, XC_MethodHook.MethodHookParam methodHookParam) {
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        if (SystemClock.elapsedRealtime() < sendMessageHooks.suppressScheduledUntil) {
            methodHookParam.setResult((Object) null);
        }
        return Unit.INSTANCE;
    }

    private final void quoteDeletedReply(int account, SendMessagesHelper.SendMessageParams params) {
        String string;
        ChatActivity.ReplyQuote replyQuote = params.replyQuote;
        MessageObject messageObject = params.replyToMsg;
        if (messageObject == null) {
            messageObject = replyQuote != null ? replyQuote.message : null;
            if (messageObject == null) {
                return;
            }
        }
        if (DeletedMessagesStore.INSTANCE.isDeleted(messageObject)) {
            MessagesController messagesController = MessagesController.getInstance(account);
            TLRPC.User currentUser = UserConfig.getInstance(account).getCurrentUser();
            TLRPC.User user = messagesController.getUser(Long.valueOf(replyQuote != null ? replyQuote.peerId : messageObject.getSenderId()));
            if (user != null) {
                currentUser = user;
            } else if (currentUser == null) {
                return;
            }
            if (replyQuote == null || (string = replyQuote.text) == null) {
                CharSequence charSequence = messageObject.messageText;
                string = charSequence != null ? charSequence.toString() : null;
                if (string == null) {
                    string = "";
                }
            }
            String str = currentUser.first_name;
            if (str == null) {
                str = "";
            }
            String str2 = str + "\n" + string + "\n";
            ArrayList arrayList = params.entities;
            if (arrayList == null) {
                arrayList = new ArrayList();
                params.entities = arrayList;
            }
            Iterator it = arrayList.iterator();
            Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
            while (it.hasNext()) {
                ((TLRPC.MessageEntity) it.next()).offset += str2.length();
            }
            ArrayList arrayList2 = arrayList;
            TLRPC.TL_messageEntityBlockquote tL_messageEntityBlockquote = new TLRPC.TL_messageEntityBlockquote();
            tL_messageEntityBlockquote.offset = 0;
            tL_messageEntityBlockquote.length = str2.length() - 1;
            tL_messageEntityBlockquote.collapsed = ReSettings.INSTANCE.getCollapsedReplyQuote();
            arrayList2.add(tL_messageEntityBlockquote);
            TLRPC.TL_inputMessageEntityMentionName tL_inputMessageEntityMentionName = new TLRPC.TL_inputMessageEntityMentionName();
            tL_inputMessageEntityMentionName.offset = 0;
            tL_inputMessageEntityMentionName.length = str.length();
            TLRPC.InputUser tL_inputUser = new TLRPC.TL_inputUser();
            ((TLRPC.TL_inputUser) tL_inputUser).user_id = currentUser.id;
            ((TLRPC.TL_inputUser) tL_inputUser).access_hash = currentUser.access_hash;
            tL_inputMessageEntityMentionName.user_id = tL_inputUser;
            arrayList2.add(tL_inputMessageEntityMentionName);
            String str3 = params.message;
            params.message = str2 + (str3 != null ? str3 : "");
            params.replyToMsg = params.replyToTopMsg;
            params.replyQuote = null;
        }
    }

    private final void scheduleImmediately(final int account, SendMessagesHelper.SendMessageParams params) {
        params.scheduleDate = scheduleTime(account, params.photo, params.document);
        this.suppressScheduledUntil = SystemClock.elapsedRealtime() + 1000;
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: ni.shikatu.re_extera.hooks.send.SendMessageHooks.2
            @Override // java.lang.Runnable
            public final void run() {
                SendMessageHooks.scheduleImmediately$lambda$6(account);
            }
        });
    }

    static final void scheduleImmediately$lambda$6(int i) {
        ChatActivity chatActivityCurrentChat$default = ChatUtils.currentChat$default(ChatUtils.INSTANCE, i, null, 2, null);
        if (chatActivityCurrentChat$default != null) {
            chatActivityCurrentChat$default.updateBottomOverlay();
        }
    }

    /* JADX WARN: Code duplicated, block: B:20:0x0049  */
    private final int scheduleTime(int account, TLRPC.TL_photo photo, TLRPC.TL_document document) {
        long jLongValue;
        int currentTime = ConnectionsManager.getInstance(account).getCurrentTime() + 12;
        if (document != null && document.access_hash != 0) {
            TLRPC.Document document2 = (TLRPC.Document) document;
            if (MessageObject.isStickerDocument(document2) || MessageObject.isAnimatedStickerDocument(document2, true) || MessageObject.isGifDocument(document2)) {
                return currentTime;
            }
        }
        if (photo == null) {
            jLongValue = 0;
        } else {
            TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(photo.sizes, AndroidUtilities.getPhotoSize());
            Long lValueOf = closestPhotoSizeWithSize != null ? Long.valueOf(closestPhotoSizeWithSize.size) : null;
            if (lValueOf != null) {
                jLongValue = lValueOf.longValue();
            } else {
                jLongValue = 0;
            }
        }
        return currentTime + uploadSeconds(document != null ? document.size : 0L) + uploadSeconds(jLongValue);
    }

    private final int uploadSeconds(long bytes) {
        if (bytes <= 0) {
            return 0;
        }
        return Math.max(6, (int) Math.ceil(((bytes / 1024.0d) / 1024.0d) * 4.5d));
    }

    private final void replaceCustomEmoji(int account, SendMessagesHelper.SendMessageParams params) {
        TLRPC.TL_messages_stickerSet groupStickerSetById;
        ArrayList arrayList;
        ArrayList arrayList2 = params.entities;
        if (arrayList2 == null) {
            return;
        }
        long j = params.peer;
        if (j <= 0 || j != UserConfig.getInstance(account).getClientUserId()) {
            HashSet hashSet = new HashSet();
            if (j < 0) {
                TLRPC.ChatFull chatFull = MessagesController.getInstance(account).getChatFull(-j);
                TLRPC.StickerSet stickerSet = chatFull != null ? chatFull.emojiset : null;
                if (stickerSet != null && (groupStickerSetById = MediaDataController.getInstance(account).getGroupStickerSetById(stickerSet)) != null && (arrayList = groupStickerSetById.documents) != null) {
                    Iterator it = arrayList.iterator();
                    while (it.hasNext()) {
                        hashSet.add(Long.valueOf(((TLRPC.Document) it.next()).id));
                    }
                }
            }
            int size = arrayList2.size();
            for (int i = 0; i < size; i++) {
                Object obj = arrayList2.get(i);
                TLRPC.TL_messageEntityCustomEmoji tL_messageEntityCustomEmoji = obj instanceof TLRPC.TL_messageEntityCustomEmoji ? (TLRPC.TL_messageEntityCustomEmoji) obj : null;
                if (tL_messageEntityCustomEmoji != null && !hashSet.contains(Long.valueOf(tL_messageEntityCustomEmoji.document_id))) {
                    TLRPC.TL_messageEntityTextUrl tL_messageEntityTextUrl = new TLRPC.TL_messageEntityTextUrl();
                    tL_messageEntityTextUrl.offset = tL_messageEntityCustomEmoji.offset;
                    tL_messageEntityTextUrl.length = tL_messageEntityCustomEmoji.length;
                    tL_messageEntityTextUrl.url = "tg://emoji?id=" + tL_messageEntityCustomEmoji.document_id;
                    Unit unit = Unit.INSTANCE;
                    arrayList2.set(i, tL_messageEntityTextUrl);
                }
            }
        }
    }
}
