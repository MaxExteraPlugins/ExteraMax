package ni.shikatu.re_extera.utils;

import android.app.Activity;
import android.net.Uri;
import androidx.core.view.inputmethod.InputContentInfoCompat;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.io.CloseableKt;
import kotlin.io.FilesKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import ni.shikatu.re_extera.ReLog;
import ni.shikatu.re_extera.localization.Strings;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessageSuggestionParams;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SendMessageChatArguments;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.VideoEditedInfo;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_stories;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.LaunchActivity;

/* JADX INFO: compiled from: MessageForwarder.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\bÆ\u0002\u0018\u00002\u00020\u0001:\u0002-.B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J>\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00072\b\u0010\u0010\u001a\u0004\u0018\u00010\nJ,\u0010\u0011\u001a\u00020\u00052\u0006\u0010\u0012\u001a\u00020\u00132\u0012\u0010\u0014\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\t2\u0006\u0010\u0015\u001a\u00020\u0007H\u0002J\f\u0010\u0016\u001a\u00020\u000e*\u00020\nH\u0002J\u001e\u0010\u0017\u001a\u00020\u00052\u0006\u0010\u0012\u001a\u00020\u00132\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\n0\tH\u0002J\u0018\u0010\u0019\u001a\u00020\u00052\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u001a\u001a\u00020\nH\u0002J \u0010\u001b\u001a\u00020\u00052\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u001a\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00020\u001dH\u0002J\u0018\u0010\u001e\u001a\u00020\u001f2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010 \u001a\u00020!H\u0002J\u0012\u0010\"\u001a\u0004\u0018\u00010#2\u0006\u0010\u001a\u001a\u00020$H\u0002J\u0012\u0010%\u001a\u0004\u0018\u00010&2\u0006\u0010\u001a\u001a\u00020$H\u0002J\u0018\u0010'\u001a\u0004\u0018\u00010(2\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u001a\u001a\u00020$J\f\u0010)\u001a\u00020\u000e*\u00020(H\u0002J\u001a\u0010*\u001a\u0004\u0018\u00010(2\u0006\u0010+\u001a\u00020(2\u0006\u0010,\u001a\u00020!H\u0002¨\u0006/"}, d2 = {"Lni/shikatu/re_extera/utils/MessageForwarder;", "", "<init>", "()V", "sendCopies", "", "account", "", "messages", "", "Lorg/telegram/messenger/MessageObject;", "peer", "", "notify", "", "scheduleDate", "replyToTopMsg", "sendSequentially", "target", "Lni/shikatu/re_extera/utils/MessageForwarder$Target;", "batches", "index", "hasMedia", "sendBatch", "batch", "sendSingle", "message", "sendDocument", "document", "Lorg/telegram/tgnet/TLRPC$Document;", "roundVideoInfo", "Lorg/telegram/messenger/VideoEditedInfo;", "path", "", "photoSize", "Lorg/telegram/tgnet/TLRPC$PhotoSize;", "Lorg/telegram/tgnet/TLRPC$Message;", "attachOf", "Lorg/telegram/tgnet/TLObject;", "resolveMediaFile", "Ljava/io/File;", "isUsable", "decrypt", "encrypted", "name", "Target", "MediaDownloader", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class MessageForwarder {
    public static final MessageForwarder INSTANCE = new MessageForwarder();

    /* JADX INFO: compiled from: MessageForwarder.kt */
    @Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0002\u0018\u00002\u00020\u0001B1\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\b\u0010\t\u001a\u0004\u0018\u00010\n¢\u0006\u0004\b\u000b\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u000eR\u0013\u0010\t\u001a\u0004\u0018\u00010\n¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0011\u0010\u0016\u001a\u00020\u00178F¢\u0006\u0006\u001a\u0004\b\u0018\u0010\u0019¨\u0006\u001a"}, d2 = {"Lni/shikatu/re_extera/utils/MessageForwarder$Target;", "", "account", "", "peer", "", "notify", "", "scheduleDate", "replyToTopMsg", "Lorg/telegram/messenger/MessageObject;", "<init>", "(IJZILorg/telegram/messenger/MessageObject;)V", "getAccount", "()I", "getPeer", "()J", "getNotify", "()Z", "getScheduleDate", "getReplyToTopMsg", "()Lorg/telegram/messenger/MessageObject;", "instance", "Lorg/telegram/messenger/AccountInstance;", "getInstance", "()Lorg/telegram/messenger/AccountInstance;", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Target {
        private final int account;
        private final boolean notify;
        private final long peer;
        private final MessageObject replyToTopMsg;
        private final int scheduleDate;

        public Target(int i, long j, boolean z, int i2, MessageObject messageObject) {
            this.account = i;
            this.peer = j;
            this.notify = z;
            this.scheduleDate = i2;
            this.replyToTopMsg = messageObject;
        }

        public final int getAccount() {
            return this.account;
        }

        public final long getPeer() {
            return this.peer;
        }

        public final boolean getNotify() {
            return this.notify;
        }

        public final int getScheduleDate() {
            return this.scheduleDate;
        }

        public final MessageObject getReplyToTopMsg() {
            return this.replyToTopMsg;
        }

        public final AccountInstance getInstance() {
            AccountInstance accountInstance = AccountInstance.getInstance(this.account);
            Intrinsics.checkNotNullExpressionValue(accountInstance, "getInstance(...)");
            return accountInstance;
        }
    }

    private MessageForwarder() {
    }

    public final void sendCopies(int account, List<? extends MessageObject> messages, long peer, boolean notify, int scheduleDate, MessageObject replyToTopMsg) {
        Intrinsics.checkNotNullParameter(messages, "messages");
        if (messages.isEmpty()) {
            return;
        }
        final ArrayList arrayList = new ArrayList();
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (MessageObject messageObject : messages) {
            long groupId = messageObject.getGroupId();
            if (groupId == 0) {
                arrayList.add(CollectionsKt.listOf(messageObject));
            } else {
                LinkedHashMap linkedHashMap2 = linkedHashMap;
                Long lValueOf = Long.valueOf(groupId);
                ArrayList arrayList2 = linkedHashMap2.get(lValueOf);
                if (arrayList2 == null) {
                    ArrayList arrayList3 = new ArrayList();
                    arrayList.add(arrayList3);
                    arrayList2 = arrayList3;
                    linkedHashMap2.put(lValueOf, arrayList2);
                }
                ((List) arrayList2).add(messageObject);
            }
        }
        final Target target = new Target(account, peer, notify, scheduleDate, replyToTopMsg);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: ni.shikatu.re_extera.utils.MessageForwarder.1
            @Override // java.lang.Runnable
            public final void run() {
                MessageForwarder.sendCopies$lambda$2(target, arrayList);
            }
        });
    }

    static final void sendCopies$lambda$2(Target target, ArrayList arrayList) {
        INSTANCE.sendSequentially(target, arrayList, 0);
    }

    private final void sendSequentially(final Target target, final List<? extends List<? extends MessageObject>> batches, final int index) {
        final List list = (List) CollectionsKt.getOrNull(batches, index);
        if (list == null) {
            return;
        }
        int account = target.getAccount();
        ArrayList arrayList = new ArrayList();
        for (Object obj : list) {
            MessageObject messageObject = (MessageObject) obj;
            MessageForwarder messageForwarder = INSTANCE;
            if (messageForwarder.hasMedia(messageObject)) {
                int account2 = target.getAccount();
                TLRPC.Message message = messageObject.messageOwner;
                Intrinsics.checkNotNullExpressionValue(message, "messageOwner");
                if (messageForwarder.resolveMediaFile(account2, message) == null) {
                    arrayList.add(obj);
                }
            }
        }
        new MediaDownloader(account, arrayList, new Function0() { // from class: ni.shikatu.re_extera.utils.MessageForwarder.0
            public final Object invoke() {
                return MessageForwarder.sendSequentially$lambda$4(target, list, batches, index);
            }
        }).start();
    }

    static final Unit sendSequentially$lambda$4(Target target, List list, List list2, int i) {
        MessageForwarder messageForwarder = INSTANCE;
        messageForwarder.sendBatch(target, list);
        messageForwarder.sendSequentially(target, list2, i + 1);
        return Unit.INSTANCE;
    }

    private final boolean hasMedia(MessageObject messageObject) {
        return (messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto) || (messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaDocument);
    }

    private final void sendBatch(Target target, List<? extends MessageObject> batch) {
        String str;
        ArrayList arrayList = new ArrayList();
        Iterator<? extends MessageObject> it = batch.iterator();
        while (true) {
            boolean z = false;
            if (!it.hasNext()) {
                break;
            }
            MessageObject next = it.next();
            TLRPC.Message message = next.messageOwner;
            TLRPC.MessageMedia messageMedia = message.media;
            TLRPC.Document document = messageMedia != null ? messageMedia.document : null;
            boolean z2 = message.media instanceof TLRPC.TL_messageMediaPhoto;
            if ((message.media instanceof TLRPC.TL_messageMediaDocument) && MessageObject.isVideoDocument(document) && !MessageObject.isVideoSticker(document)) {
                z = true;
            }
            if (!z2 && !z) {
                sendSingle(target, next);
            } else {
                int account = target.getAccount();
                Intrinsics.checkNotNull(message);
                File fileResolveMediaFile = resolveMediaFile(account, message);
                if (fileResolveMediaFile != null) {
                    ArrayList arrayList2 = arrayList;
                    SendMessagesHelper.SendingMediaInfo sendingMediaInfo = new SendMessagesHelper.SendingMediaInfo();
                    sendingMediaInfo.path = fileResolveMediaFile.getAbsolutePath();
                    sendingMediaInfo.isVideo = z;
                    if (arrayList.isEmpty() && (str = message.message) != null && str.length() != 0) {
                        sendingMediaInfo.caption = message.message;
                        sendingMediaInfo.entities = message.entities;
                    }
                    arrayList2.add(sendingMediaInfo);
                }
            }
        }
        if (arrayList.isEmpty()) {
            return;
        }
        SendMessagesHelper.prepareSendingMedia(target.getInstance(), arrayList, target.getPeer(), target.getReplyToTopMsg(), target.getReplyToTopMsg(), (TL_stories.StoryItem) null, (ChatActivity.ReplyQuote) null, false, arrayList.size() > 1, (MessageObject) null, target.getNotify(), target.getScheduleDate(), 0, 0, false, (InputContentInfoCompat) null, SendMessageChatArguments.EMPTY, 0L, false, 0L, 0L, (MessageSuggestionParams) null);
    }

    private final void sendSingle(Target target, MessageObject message) {
        TLRPC.Message message2 = message.messageOwner;
        TLRPC.TL_messageMediaPoll tL_messageMediaPoll = message2.media;
        SendMessagesHelper sendMessagesHelper = target.getInstance().getSendMessagesHelper();
        MessageObject replyToTopMsg = target.getReplyToTopMsg();
        if (!(tL_messageMediaPoll instanceof TLRPC.TL_messageMediaGeo) && !(tL_messageMediaPoll instanceof TLRPC.TL_messageMediaVenue)) {
            if (!(tL_messageMediaPoll instanceof TLRPC.TL_messageMediaGeoLive)) {
                if (tL_messageMediaPoll instanceof TLRPC.TL_messageMediaPoll) {
                    sendMessagesHelper.sendMessage(SendMessagesHelper.SendMessageParams.of(tL_messageMediaPoll, target.getPeer(), replyToTopMsg, replyToTopMsg, (TLRPC.ReplyMarkup) null, (HashMap) null, target.getNotify(), target.getScheduleDate(), 0));
                    return;
                }
                if (tL_messageMediaPoll instanceof TLRPC.TL_messageMediaContact) {
                    TLRPC.User tL_user = new TLRPC.TL_user();
                    TLRPC.TL_messageMediaContact tL_messageMediaContact = (TLRPC.TL_messageMediaContact) tL_messageMediaPoll;
                    ((TLRPC.TL_user) tL_user).id = tL_messageMediaContact.user_id;
                    ((TLRPC.TL_user) tL_user).first_name = tL_messageMediaContact.first_name;
                    ((TLRPC.TL_user) tL_user).last_name = tL_messageMediaContact.last_name;
                    ((TLRPC.TL_user) tL_user).phone = tL_messageMediaContact.phone_number;
                    sendMessagesHelper.sendMessage(SendMessagesHelper.SendMessageParams.of(tL_user, target.getPeer(), replyToTopMsg, replyToTopMsg, (TLRPC.ReplyMarkup) null, (HashMap) null, target.getNotify(), target.getScheduleDate(), 0));
                    return;
                }
                if (tL_messageMediaPoll instanceof TLRPC.TL_messageMediaDocument) {
                    TLRPC.Document document = ((TLRPC.TL_messageMediaDocument) tL_messageMediaPoll).document;
                    if (document == null) {
                        return;
                    }
                    sendDocument(target, message, document);
                    return;
                }
                String str = message2.message;
                String str2 = str;
                if (str2 == null || str2.length() == 0) {
                    return;
                }
                sendMessagesHelper.sendMessage(SendMessagesHelper.SendMessageParams.of(str, target.getPeer(), replyToTopMsg, replyToTopMsg, (TLRPC.WebPage) null, true, message2.entities, (TLRPC.ReplyMarkup) null, (HashMap) null, target.getNotify(), target.getScheduleDate(), 0, (MessageObject.SendAnimationData) null, false));
                return;
            }
        }
        sendMessagesHelper.sendMessage(SendMessagesHelper.SendMessageParams.of(tL_messageMediaPoll, target.getPeer(), replyToTopMsg, replyToTopMsg, (TLRPC.ReplyMarkup) null, (HashMap) null, target.getNotify(), target.getScheduleDate(), 0));
    }

    private final void sendDocument(Target target, MessageObject message, TLRPC.Document document) {
        String absolutePath;
        TLRPC.Message message2 = message.messageOwner;
        MessageObject replyToTopMsg = target.getReplyToTopMsg();
        SendMessagesHelper sendMessagesHelper = target.getInstance().getSendMessagesHelper();
        if (MessageObject.isStickerMessage(message2) || MessageObject.isAnimatedStickerMessage(message2) || MessageObject.isVideoSticker(document)) {
            sendMessagesHelper.sendSticker(document, (String) null, target.getPeer(), replyToTopMsg, replyToTopMsg, (TL_stories.StoryItem) null, (ChatActivity.ReplyQuote) null, (MessageObject.SendAnimationData) null, target.getNotify(), target.getScheduleDate(), 0, false, message, SendMessageChatArguments.EMPTY, 0L, 0L, (MessageSuggestionParams) null);
            return;
        }
        int account = target.getAccount();
        Intrinsics.checkNotNull(message2);
        File fileResolveMediaFile = resolveMediaFile(account, message2);
        if (fileResolveMediaFile == null || (absolutePath = fileResolveMediaFile.getAbsolutePath()) == null) {
            ReLog.e$default(ReLog.INSTANCE, "Copy forward: file for document " + document.id + " is unavailable", null, 2, null);
            return;
        }
        if (MessageObject.isVoiceMessage(message2) && (document instanceof TLRPC.TL_document)) {
            sendMessagesHelper.sendMessage(SendMessagesHelper.SendMessageParams.of((TLRPC.TL_document) document, (VideoEditedInfo) null, absolutePath, target.getPeer(), replyToTopMsg, replyToTopMsg, (String) null, (ArrayList) null, (TLRPC.ReplyMarkup) null, (HashMap) null, target.getNotify(), target.getScheduleDate(), 0, 0, message, (MessageObject.SendAnimationData) null, false));
        } else if (MessageObject.isRoundVideoMessage(message2)) {
            SendMessagesHelper.prepareSendingVideo(target.getInstance(), absolutePath, roundVideoInfo(document, absolutePath), (String) null, (TLRPC.Photo) null, target.getPeer(), replyToTopMsg, replyToTopMsg, (TL_stories.StoryItem) null, (ChatActivity.ReplyQuote) null, (ArrayList) null, 0, (MessageObject) null, target.getNotify(), target.getScheduleDate(), 0, false, false, (CharSequence) null, SendMessageChatArguments.EMPTY, 0L, 0L);
        } else {
            SendMessagesHelper.prepareSendingDocument(target.getInstance(), absolutePath, absolutePath, (Uri) null, message2.message, (String) null, target.getPeer(), replyToTopMsg, replyToTopMsg, (TL_stories.StoryItem) null, (ChatActivity.ReplyQuote) null, (MessageObject) null, target.getNotify(), target.getScheduleDate(), (InputContentInfoCompat) null, SendMessageChatArguments.EMPTY, false);
        }
    }

    private final VideoEditedInfo roundVideoInfo(TLRPC.Document document, String path) {
        TLRPC.TL_documentAttributeVideo tL_documentAttributeVideo;
        ArrayList arrayList = document.attributes;
        Intrinsics.checkNotNullExpressionValue(arrayList, "attributes");
        Iterator it = arrayList.iterator();
        do {
            tL_documentAttributeVideo = null;
            if (!it.hasNext()) {
                break;
            }
            TLRPC.DocumentAttribute documentAttribute = (TLRPC.DocumentAttribute) it.next();
            if (documentAttribute instanceof TLRPC.TL_documentAttributeVideo) {
                tL_documentAttributeVideo = (TLRPC.TL_documentAttributeVideo) documentAttribute;
            }
        } while (tL_documentAttributeVideo == null);
        int i = tL_documentAttributeVideo != null ? tL_documentAttributeVideo.w : -1;
        int i2 = tL_documentAttributeVideo != null ? tL_documentAttributeVideo.h : -1;
        VideoEditedInfo videoEditedInfo = new VideoEditedInfo();
        videoEditedInfo.roundVideo = true;
        videoEditedInfo.startTime = -1L;
        videoEditedInfo.endTime = -1L;
        videoEditedInfo.bitrate = -1;
        videoEditedInfo.originalPath = path;
        videoEditedInfo.estimatedSize = 0L;
        videoEditedInfo.estimatedDuration = tL_documentAttributeVideo != null ? ((long) tL_documentAttributeVideo.duration) * 1000 : -1L;
        videoEditedInfo.resultWidth = i;
        videoEditedInfo.resultHeight = i2;
        videoEditedInfo.originalWidth = i;
        videoEditedInfo.originalHeight = i2;
        return videoEditedInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final TLRPC.PhotoSize photoSize(TLRPC.Message message) {
        TLRPC.Photo photo;
        TLRPC.TL_messageMediaPhoto tL_messageMediaPhoto = message.media;
        TLRPC.TL_messageMediaPhoto tL_messageMediaPhoto2 = tL_messageMediaPhoto instanceof TLRPC.TL_messageMediaPhoto ? tL_messageMediaPhoto : null;
        if (tL_messageMediaPhoto2 == null || (photo = tL_messageMediaPhoto2.photo) == null) {
            return null;
        }
        return FileLoader.getClosestPhotoSizeWithSize(photo.sizes, AndroidUtilities.getPhotoSize());
    }

    private final TLObject attachOf(TLRPC.Message message) {
        TLRPC.TL_messageMediaDocument tL_messageMediaDocument = message.media;
        if (tL_messageMediaDocument instanceof TLRPC.TL_messageMediaPhoto) {
            return photoSize(message);
        }
        if (tL_messageMediaDocument instanceof TLRPC.TL_messageMediaDocument) {
            return tL_messageMediaDocument.document;
        }
        return null;
    }

    public final File resolveMediaFile(int account, TLRPC.Message message) {
        Intrinsics.checkNotNullParameter(message, "message");
        String str = message.attachPath;
        if (str != null) {
            if (str.length() <= 0) {
                str = null;
            }
            if (str != null) {
                File file = new File(str);
                if (!INSTANCE.isUsable(file)) {
                    file = null;
                }
                if (file != null) {
                    return file;
                }
            }
        }
        TLObject tLObjectAttachOf = attachOf(message);
        if (tLObjectAttachOf == null) {
            return null;
        }
        FileLoader fileLoader = FileLoader.getInstance(account);
        File pathToAttach = fileLoader.getPathToAttach(tLObjectAttachOf, false);
        if (pathToAttach != null) {
            if (!INSTANCE.isUsable(pathToAttach)) {
                pathToAttach = null;
            }
            if (pathToAttach != null) {
                return pathToAttach;
            }
        }
        File pathToAttach2 = fileLoader.getPathToAttach(tLObjectAttachOf, true);
        if (pathToAttach2 != null) {
            if (!INSTANCE.isUsable(pathToAttach2)) {
                pathToAttach2 = null;
            }
            if (pathToAttach2 != null) {
                return pathToAttach2;
            }
        }
        String attachFileName = FileLoader.getAttachFileName(tLObjectAttachOf);
        for (File file2 : CollectionsKt.listOf(new File[]{FileLoader.getDirectory(4), FileLoader.getDirectory(0)})) {
            if (file2 != null) {
                File file3 = new File(file2, attachFileName);
                MessageForwarder messageForwarder = INSTANCE;
                if (!messageForwarder.isUsable(file3)) {
                    file3 = null;
                }
                if (file3 != null) {
                    return file3;
                }
                File file4 = new File(file2, attachFileName + ".enc");
                if (!messageForwarder.isUsable(file4)) {
                    file4 = null;
                }
                if (file4 != null) {
                    Intrinsics.checkNotNull(attachFileName);
                    File fileDecrypt = messageForwarder.decrypt(file4, attachFileName);
                    if (fileDecrypt != null) {
                        return fileDecrypt;
                    }
                } else {
                    continue;
                }
            }
        }
        return null;
    }

    private final boolean isUsable(File file) {
        return file.exists() && file.length() > 0;
    }

    private final File decrypt(File encrypted, String name) {
        try {
            File file = new File(FileLoader.getInternalCacheDir(), encrypted.getName() + ".key");
            if (!file.exists()) {
                return null;
            }
            byte[] bArr = new byte[32];
            byte[] bArr2 = new byte[16];
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            try {
                RandomAccessFile randomAccessFile2 = randomAccessFile;
                if (randomAccessFile2.length() < 48) {
                    CloseableKt.closeFinally(randomAccessFile, (Throwable) null);
                    return null;
                }
                randomAccessFile2.readFully(bArr);
                randomAccessFile2.readFully(bArr2);
                Unit unit = Unit.INSTANCE;
                CloseableKt.closeFinally(randomAccessFile, (Throwable) null);
                byte[] bytes = FilesKt.readBytes(encrypted);
                for (int i = 12; i < 16; i++) {
                    bArr2[i] = 0;
                }
                Utilities.aesCtrDecryptionByteArray(bytes, bArr, bArr2, 0, bytes.length, 0);
                File file2 = new File(FileLoader.getDirectory(4), "forward_" + name);
                FilesKt.writeBytes(file2, bytes);
                return file2;
            } catch (Throwable th) {
                try {
                    throw th;
                } catch (Throwable th2) {
                    CloseableKt.closeFinally(randomAccessFile, th);
                    throw th2;
                }
            }
            ReLog.INSTANCE.e("Copy forward: failed to decrypt " + encrypted.getName(), e);
            return null;
        } catch (Exception e) {
            ReLog.INSTANCE.e("Copy forward: failed to decrypt " + encrypted.getName(), e);
            return null;
        }
    }

    /* JADX INFO: compiled from: MessageForwarder.kt */
    @Metadata(d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\u0005\b\u0000\u0018\u00002\u00020\u0001B+\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b¢\u0006\u0004\b\n\u0010\u000bJ\u0006\u0010\u0016\u001a\u00020\tJ5\u0010\u0017\u001a\u00020\t2\u0006\u0010\u0018\u001a\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u00032\u0016\u0010\u0019\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010\u001b0\u001a\"\u0004\u0018\u00010\u001bH\u0016¢\u0006\u0002\u0010\u001cJ\b\u0010\u001d\u001a\u00020\tH\u0002J\b\u0010\u001e\u001a\u00020\tH\u0002J\b\u0010\u001f\u001a\u00020\tH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004¢\u0006\u0002\n\u0000R*\u0010\f\u001a\u001e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00060\rj\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u0006`\u000fX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006 "}, d2 = {"Lni/shikatu/re_extera/utils/MessageForwarder$MediaDownloader;", "Lorg/telegram/messenger/NotificationCenter$NotificationCenterDelegate;", "account", "", "messages", "", "Lorg/telegram/messenger/MessageObject;", "onReady", "Lkotlin/Function0;", "", "<init>", "(ILjava/util/List;Lkotlin/jvm/functions/Function0;)V", "pending", "Ljava/util/HashMap;", "", "Lkotlin/collections/HashMap;", "progress", "Lorg/telegram/ui/ActionBar/AlertDialog;", "cancelled", "", "observers", "Lorg/telegram/messenger/NotificationCenter$ObserversGroup;", "start", "didReceivedNotification", "id", "args", "", "", "(II[Ljava/lang/Object;)V", "finish", "cancel", "showProgress", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class MediaDownloader implements NotificationCenter.NotificationCenterDelegate {
        private final int account;
        private boolean cancelled;
        private final List<MessageObject> messages;
        private NotificationCenter.ObserversGroup observers;
        private final Function0<Unit> onReady;
        private final HashMap<String, MessageObject> pending;
        private AlertDialog progress;

        public MediaDownloader(int i, List<? extends MessageObject> list, Function0<Unit> function0) {
            Intrinsics.checkNotNullParameter(list, "messages");
            Intrinsics.checkNotNullParameter(function0, "onReady");
            this.account = i;
            this.messages = list;
            this.onReady = function0;
            this.pending = new HashMap<>();
        }

        public final void start() {
            if (this.messages.isEmpty()) {
                this.onReady.invoke();
                return;
            }
            FileLoader fileLoader = FileLoader.getInstance(this.account);
            for (MessageObject messageObject : this.messages) {
                TLRPC.Message message = messageObject.messageOwner;
                TLRPC.TL_messageMediaPhoto tL_messageMediaPhoto = message.media;
                if (tL_messageMediaPhoto instanceof TLRPC.TL_messageMediaDocument) {
                    TLObject tLObject = ((TLRPC.TL_messageMediaDocument) tL_messageMediaPhoto).document;
                    if (tLObject != null) {
                        this.pending.put(FileLoader.getAttachFileName(tLObject), messageObject);
                        fileLoader.loadFile(tLObject, messageObject, 3, 0);
                    }
                } else if (tL_messageMediaPhoto instanceof TLRPC.TL_messageMediaPhoto) {
                    MessageForwarder messageForwarder = MessageForwarder.INSTANCE;
                    Intrinsics.checkNotNull(message);
                    TLObject tLObjectPhotoSize = messageForwarder.photoSize(message);
                    if (tLObjectPhotoSize != null) {
                        this.pending.put(FileLoader.getAttachFileName(tLObjectPhotoSize), messageObject);
                        fileLoader.loadFile(ImageLocation.getForPhoto(tLObjectPhotoSize, tL_messageMediaPhoto.photo), messageObject, (String) null, 3, 0);
                    }
                }
            }
            if (this.pending.isEmpty()) {
                this.onReady.invoke();
            } else {
                this.observers = NotificationCenter.getInstance(this.account).createObserversGroup(this).add(NotificationCenter.fileLoaded).add(NotificationCenter.fileLoadFailed).add(NotificationCenter.fileLoadProgressChanged);
                showProgress();
            }
        }

        public void didReceivedNotification(int id, int account, Object... args) {
            AlertDialog alertDialog;
            Intrinsics.checkNotNullParameter(args, "args");
            Object orNull = ArraysKt.getOrNull(args, 0);
            String str = orNull instanceof String ? (String) orNull : null;
            if (str != null && this.pending.containsKey(str)) {
                if (id == NotificationCenter.fileLoadProgressChanged) {
                    Object orNull2 = ArraysKt.getOrNull(args, 1);
                    Long l = orNull2 instanceof Long ? (Long) orNull2 : null;
                    if (l != null) {
                        long jLongValue = l.longValue();
                        Object orNull3 = ArraysKt.getOrNull(args, 2);
                        Long l2 = orNull3 instanceof Long ? (Long) orNull3 : null;
                        if (l2 != null) {
                            long jLongValue2 = l2.longValue();
                            if (jLongValue2 <= 0 || (alertDialog = this.progress) == null) {
                                return;
                            }
                            alertDialog.setProgress((int) ((jLongValue * 100) / jLongValue2));
                            return;
                        }
                        return;
                    }
                    return;
                }
                if (id == NotificationCenter.fileLoaded || id == NotificationCenter.fileLoadFailed) {
                    this.pending.remove(str);
                    if (this.pending.isEmpty()) {
                        finish();
                    }
                }
            }
        }

        private final void finish() {
            NotificationCenter.ObserversGroup observersGroup = this.observers;
            if (observersGroup != null) {
                observersGroup.removeAllObservers();
            }
            this.observers = null;
            AlertDialog alertDialog = this.progress;
            if (alertDialog != null) {
                alertDialog.dismiss();
            }
            this.progress = null;
            if (this.cancelled) {
                return;
            }
            this.onReady.invoke();
        }

        private final void cancel() {
            this.cancelled = true;
            FileLoader fileLoader = FileLoader.getInstance(this.account);
            for (MessageObject messageObject : this.pending.values()) {
                Intrinsics.checkNotNullExpressionValue(messageObject, "next(...)");
                TLRPC.Message message = messageObject.messageOwner;
                TLRPC.TL_messageMediaDocument tL_messageMediaDocument = message.media;
                if (tL_messageMediaDocument instanceof TLRPC.TL_messageMediaDocument) {
                    fileLoader.cancelLoadFile(tL_messageMediaDocument.document, false);
                } else if (tL_messageMediaDocument instanceof TLRPC.TL_messageMediaPhoto) {
                    MessageForwarder messageForwarder = MessageForwarder.INSTANCE;
                    Intrinsics.checkNotNull(message);
                    TLRPC.PhotoSize photoSize = messageForwarder.photoSize(message);
                    if (photoSize != null) {
                        fileLoader.cancelLoadFile(photoSize, false);
                    }
                }
            }
            this.pending.clear();
            finish();
        }

        private final void showProgress() {
            Activity parentActivity;
            BaseFragment lastFragment = LaunchActivity.getLastFragment();
            if (lastFragment == null || (parentActivity = lastFragment.getParentActivity()) == null) {
                return;
            }
            final AlertDialog alertDialog = new AlertDialog(parentActivity, 2);
            alertDialog.setMessage(Strings.INSTANCE.getFORWARDING());
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setNegativeButton(LocaleController.getString(R.string.Cancel), new AlertDialog.OnButtonClickListener() { // from class: ni.shikatu.re_extera.utils.MessageForwarder.MediaDownloader.0
                public final void onClick(AlertDialog alertDialog2, int i) {
                    alertDialog.cancel();
                }
            });
            alertDialog.show();
            this.progress = alertDialog;
        }
    }
}
