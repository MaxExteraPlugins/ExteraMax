package ni.shikatu.re_extera.utils;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Triple;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import ni.shikatu.re_extera.ReLog;
import ni.shikatu.re_extera.localization.Strings;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.VideoEditedInfo;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.LaunchActivity;

/* JADX INFO: compiled from: GifReuploader.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0018\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\u00012\u0006\u0010\f\u001a\u00020\rJ\u0016\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\f\u001a\u00020\rJ\u0018\u0010\u0012\u001a\u00020\u000f2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\f\u001a\u00020\rH\u0002J \u0010\u0015\u001a\u00020\u000f2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\r2\u0006\u0010\u0017\u001a\u00020\u0018H\u0002J \u0010\u0019\u001a\u00020\u000f2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u001a\u001a\u00020\u00072\u0006\u0010\u001b\u001a\u00020\u0005H\u0002J \u0010\u001c\u001a\u00020\u000f2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u001a\u001a\u00020\u00072\u0006\u0010\u001d\u001a\u00020\u001eH\u0002J \u0010\u001f\u001a\u00020 2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\r2\u0006\u0010\u0017\u001a\u00020\u0018H\u0002J\u0018\u0010!\u001a\u00020\"2\u0006\u0010\u0016\u001a\u00020\r2\u0006\u0010#\u001a\u00020\u0005H\u0002J$\u0010$\u001a\u0016\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020&\u0018\u00010%2\u0006\u0010#\u001a\u00020\u0005H\u0002J\u001a\u0010'\u001a\u0004\u0018\u00010(2\u0006\u0010#\u001a\u00020\u00052\u0006\u0010)\u001a\u00020\nH\u0002J\u0010\u0010*\u001a\u00020\u000f2\u0006\u0010+\u001a\u00020\u0005H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000¨\u0006,"}, d2 = {"Lni/shikatu/re_extera/utils/GifReuploader;", "", "<init>", "()V", "MARKER", "", "WAIT_TIMEOUT_MS", "", "DELETE_DELAY_MS", "needsReupload", "", "parent", "document", "Lorg/telegram/tgnet/TLRPC$Document;", "reupload", "", "message", "Lorg/telegram/messenger/MessageObject;", "dropLocalRecent", "account", "", "sendToSaved", "original", "file", "Ljava/io/File;", "awaitSent", "selfId", "token", "onSent", "sent", "Lorg/telegram/tgnet/TLRPC$Message;", "buildGifDocument", "Lorg/telegram/tgnet/TLRPC$TL_document;", "videoAttribute", "Lorg/telegram/tgnet/TLRPC$TL_documentAttributeVideo;", "path", "readVideoInfo", "Lkotlin/Triple;", "", "thumbnail", "Lorg/telegram/tgnet/TLRPC$PhotoSize;", "isGifFile", "fail", "reason", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class GifReuploader {
    private static final long DELETE_DELAY_MS = 500;
    public static final GifReuploader INSTANCE = new GifReuploader();
    private static final String MARKER = "re_extera_gif_save";
    private static final long WAIT_TIMEOUT_MS = 300000;

    private GifReuploader() {
    }

    public final boolean needsReupload(Object parent, TLRPC.Document document) {
        Intrinsics.checkNotNullParameter(document, "document");
        if (!(document instanceof TLRPC.TL_documentEncrypted)) {
            MessageObject messageObject = parent instanceof MessageObject ? (MessageObject) parent : null;
            if (messageObject == null || !ForwardRestrictions.INSTANCE.isRestricted(messageObject)) {
                return false;
            }
        }
        return true;
    }

    public final void reupload(final MessageObject message, final TLRPC.Document document) {
        Intrinsics.checkNotNullParameter(message, "message");
        Intrinsics.checkNotNullParameter(document, "document");
        final int i = message.currentAccount;
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: ni.shikatu.re_extera.utils.GifReuploader.2
            @Override // java.lang.Runnable
            public final void run() {
                GifReuploader.reupload$lambda$2(i, document, message);
            }
        });
    }

    static final void reupload$lambda$2(final int i, final TLRPC.Document document, final MessageObject messageObject) {
        INSTANCE.dropLocalRecent(i, document);
        List listListOf = CollectionsKt.listOf(messageObject);
        ArrayList arrayList = new ArrayList();
        for (Object obj : listListOf) {
            MessageForwarder messageForwarder = MessageForwarder.INSTANCE;
            TLRPC.Message message = ((MessageObject) obj).messageOwner;
            Intrinsics.checkNotNullExpressionValue(message, "messageOwner");
            if (messageForwarder.resolveMediaFile(i, message) == null) {
                arrayList.add(obj);
            }
        }
        new MessageForwarder.MediaDownloader(i, arrayList, new Function0() { // from class: ni.shikatu.re_extera.utils.GifReuploader.3
            public final Object invoke() {
                return GifReuploader.reupload$lambda$2$lambda$1(i, messageObject, document);
            }
        }).start();
    }

    static final Unit reupload$lambda$2$lambda$1(int i, MessageObject messageObject, TLRPC.Document document) {
        MessageForwarder messageForwarder = MessageForwarder.INSTANCE;
        TLRPC.Message message = messageObject.messageOwner;
        Intrinsics.checkNotNullExpressionValue(message, "messageOwner");
        File fileResolveMediaFile = messageForwarder.resolveMediaFile(i, message);
        if (fileResolveMediaFile == null) {
            INSTANCE.fail("file for document " + document.id + " is unavailable");
        } else {
            INSTANCE.sendToSaved(i, document, fileResolveMediaFile);
        }
        return Unit.INSTANCE;
    }

    private final void dropLocalRecent(int account, TLRPC.Document document) {
        MediaDataController mediaDataController = MediaDataController.getInstance(account);
        ArrayList recentGifs = mediaDataController.getRecentGifs();
        Intrinsics.checkNotNullExpressionValue(recentGifs, "getRecentGifs(...)");
        ArrayList arrayList = recentGifs;
        if ((arrayList instanceof Collection) && arrayList.isEmpty()) {
            return;
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            if (((TLRPC.Document) it.next()).id == document.id) {
                mediaDataController.removeRecentGif(document);
                NotificationCenter.getInstance(account).postNotificationName(NotificationCenter.recentDocumentsDidLoad, new Object[]{true, 0});
                return;
            }
        }
    }

    private final void sendToSaved(int account, TLRPC.Document original, File file) {
        long clientUserId = UserConfig.getInstance(account).getClientUserId();
        String absolutePath = file.getAbsolutePath();
        TLRPC.TL_document tL_documentBuildGifDocument = buildGifDocument(account, original, file);
        String strValueOf = String.valueOf(System.nanoTime());
        awaitSent(account, clientUserId, strValueOf);
        SendMessagesHelper.getInstance(account).sendMessage(SendMessagesHelper.SendMessageParams.of(tL_documentBuildGifDocument, (VideoEditedInfo) null, absolutePath, clientUserId, (MessageObject) null, (MessageObject) null, (String) null, (ArrayList) null, (TLRPC.ReplyMarkup) null, MapsKt.hashMapOf(new Pair[]{TuplesKt.to(MARKER, strValueOf)}), false, 0, 0, 0, "gif", (MessageObject.SendAnimationData) null, false));
    }

    private final void awaitSent(int account, long selfId, String token) {
        final NotificationCenter notificationCenter = NotificationCenter.getInstance(account);
        final GifReuploader$awaitSent$observer$1 gifReuploader$awaitSent$observer$1 = new GifReuploader$awaitSent$observer$1(token, notificationCenter, selfId);
        notificationCenter.addObserver(gifReuploader$awaitSent$observer$1, NotificationCenter.messageReceivedByServer);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: ni.shikatu.re_extera.utils.GifReuploader.0
            @Override // java.lang.Runnable
            public final void run() {
                GifReuploader.awaitSent$lambda$4(notificationCenter, gifReuploader$awaitSent$observer$1);
            }
        }, WAIT_TIMEOUT_MS);
    }

    static final void awaitSent$lambda$4(NotificationCenter notificationCenter, GifReuploader$awaitSent$observer$1 gifReuploader$awaitSent$observer$1) {
        notificationCenter.removeObserver(gifReuploader$awaitSent$observer$1, NotificationCenter.messageReceivedByServer);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void onSent(int account, long selfId, TLRPC.Message sent) {
        TLRPC.MessageMedia media = MessageObject.getMedia(sent);
        TLRPC.Document document = media != null ? media.document : null;
        if (document != null && MessageObject.isNewGifDocument(document)) {
            MediaDataController.getInstance(account).addRecentGif(document, sent.date, true);
            MessagesController.getInstance(account).saveGif("gif", document);
        } else {
            fail("sent document isn't a GIF");
        }
        ArrayList arrayListArrayListOf = CollectionsKt.arrayListOf(new Integer[]{Integer.valueOf(sent.id)});
        DeletionBypass.INSTANCE.allow(selfId, arrayListArrayListOf);
        MessagesController.getInstance(account).deleteMessages(arrayListArrayListOf, (ArrayList) null, (TLRPC.EncryptedChat) null, selfId, 0, true, 0);
    }

    /* JADX WARN: Code duplicated, block: B:8:0x0024  */
    private final TLRPC.TL_document buildGifDocument(int account, TLRPC.Document original, File file) {
        boolean z;
        String absolutePath = file.getAbsolutePath();
        if (Intrinsics.areEqual(original.mime_type, "image/gif")) {
            z = true;
        } else {
            String name = file.getName();
            Intrinsics.checkNotNullExpressionValue(name, "getName(...)");
            if (StringsKt.endsWith(name, ".gif", true)) {
                z = true;
            } else {
                z = false;
            }
        }
        TLRPC.TL_document tL_document = new TLRPC.TL_document();
        tL_document.id = 0L;
        tL_document.file_reference = new byte[0];
        tL_document.date = ConnectionsManager.getInstance(account).getCurrentTime();
        tL_document.dc_id = 0;
        tL_document.size = file.length();
        tL_document.mime_type = z ? "image/gif" : "video/mp4";
        ArrayList arrayList = tL_document.attributes;
        TLRPC.TL_documentAttributeFilename tL_documentAttributeFilename = new TLRPC.TL_documentAttributeFilename();
        tL_documentAttributeFilename.file_name = "animation.gif";
        arrayList.add(tL_documentAttributeFilename);
        tL_document.attributes.add(new TLRPC.TL_documentAttributeAnimated());
        if (!z) {
            ArrayList arrayList2 = tL_document.attributes;
            GifReuploader gifReuploader = INSTANCE;
            Intrinsics.checkNotNull(absolutePath);
            arrayList2.add(gifReuploader.videoAttribute(original, absolutePath));
        }
        GifReuploader gifReuploader2 = INSTANCE;
        Intrinsics.checkNotNull(absolutePath);
        TLRPC.PhotoSize photoSizeThumbnail = gifReuploader2.thumbnail(absolutePath, z);
        if (photoSizeThumbnail != null) {
            tL_document.thumbs.add(photoSizeThumbnail);
            tL_document.flags |= 1;
        }
        return tL_document;
    }

    private final TLRPC.TL_documentAttributeVideo videoAttribute(TLRPC.Document original, String path) {
        TLRPC.TL_documentAttributeVideo tL_documentAttributeVideo;
        ArrayList arrayList = original.attributes;
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
        TLRPC.TL_documentAttributeVideo tL_documentAttributeVideo2 = new TLRPC.TL_documentAttributeVideo();
        tL_documentAttributeVideo2.supports_streaming = true;
        tL_documentAttributeVideo2.nosound = true;
        if (tL_documentAttributeVideo != null && tL_documentAttributeVideo.w > 0 && tL_documentAttributeVideo.h > 0) {
            tL_documentAttributeVideo2.w = tL_documentAttributeVideo.w;
            tL_documentAttributeVideo2.h = tL_documentAttributeVideo.h;
            tL_documentAttributeVideo2.duration = tL_documentAttributeVideo.duration;
            return tL_documentAttributeVideo2;
        }
        Triple<Integer, Integer, Double> videoInfo = INSTANCE.readVideoInfo(path);
        if (videoInfo != null) {
            int iIntValue = ((Number) videoInfo.component1()).intValue();
            int iIntValue2 = ((Number) videoInfo.component2()).intValue();
            double dDoubleValue = ((Number) videoInfo.component3()).doubleValue();
            tL_documentAttributeVideo2.w = iIntValue;
            tL_documentAttributeVideo2.h = iIntValue2;
            tL_documentAttributeVideo2.duration = dDoubleValue;
        }
        return tL_documentAttributeVideo2;
    }

    private final Triple<Integer, Integer, Double> readVideoInfo(String path) {
        Long longOrNull;
        Integer intOrNull;
        Integer intOrNull2;
        try {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            try {
                mediaMetadataRetriever.setDataSource(path);
                String strExtractMetadata = mediaMetadataRetriever.extractMetadata(18);
                int iIntValue = 0;
                int iIntValue2 = (strExtractMetadata == null || (intOrNull2 = StringsKt.toIntOrNull(strExtractMetadata)) == null) ? 0 : intOrNull2.intValue();
                String strExtractMetadata2 = mediaMetadataRetriever.extractMetadata(19);
                if (strExtractMetadata2 != null && (intOrNull = StringsKt.toIntOrNull(strExtractMetadata2)) != null) {
                    iIntValue = intOrNull.intValue();
                }
                String strExtractMetadata3 = mediaMetadataRetriever.extractMetadata(9);
                return new Triple<>(Integer.valueOf(iIntValue2), Integer.valueOf(iIntValue), Double.valueOf(((strExtractMetadata3 == null || (longOrNull = StringsKt.toLongOrNull(strExtractMetadata3)) == null) ? 0L : longOrNull.longValue()) / 1000.0d));
            } finally {
                mediaMetadataRetriever.release();
            }
        } catch (Exception e) {
            ReLog.INSTANCE.e("Save to GIFs: can't read video info of " + path, e);
            return null;
        }
    }

    private final TLRPC.PhotoSize thumbnail(String path, boolean isGifFile) {
        Bitmap bitmapCreateVideoThumbnail;
        try {
            if (isGifFile) {
                bitmapCreateVideoThumbnail = ImageLoader.loadBitmap(path, (Uri) null, 320.0f, 320.0f, true);
            } else {
                bitmapCreateVideoThumbnail = SendMessagesHelper.createVideoThumbnail(path, 1);
            }
            if (bitmapCreateVideoThumbnail == null) {
                return null;
            }
            TLRPC.PhotoSize photoSizeScaleAndSaveImage = ImageLoader.scaleAndSaveImage(bitmapCreateVideoThumbnail, 320.0f, 320.0f, 80, false);
            bitmapCreateVideoThumbnail.recycle();
            return photoSizeScaleAndSaveImage;
        } catch (Exception e) {
            ReLog.INSTANCE.e("Save to GIFs: can't create thumbnail for " + path, e);
            return null;
        }
    }

    private final void fail(String reason) {
        ReLog.e$default(ReLog.INSTANCE, "Save to GIFs: " + reason, null, 2, null);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: ni.shikatu.re_extera.utils.GifReuploader.1
            @Override // java.lang.Runnable
            public final void run() {
                GifReuploader.fail$lambda$13();
            }
        });
    }

    static final void fail$lambda$13() {
        BaseFragment lastFragment = LaunchActivity.getLastFragment();
        if (lastFragment == null) {
            return;
        }
        BulletinFactory.of(lastFragment).createErrorBulletin(Strings.INSTANCE.getGIF_SAVE_FAILED()).show();
    }
}
