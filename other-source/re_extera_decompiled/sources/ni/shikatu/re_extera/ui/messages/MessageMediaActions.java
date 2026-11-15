package ni.shikatu.re_extera.ui.messages;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.core.content.FileProvider;
import com.exteragram.messenger.utils.chats.ChatUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import ni.shikatu.re_extera.localization.Strings;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.R;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.TranslateAlert2;

/* JADX INFO: compiled from: MessageMediaActions.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0007\bÀ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007J\u000e\u0010\b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007J\u0010\u0010\t\u001a\u0004\u0018\u00010\n2\u0006\u0010\u0006\u001a\u00020\u0007J\u000e\u0010\u000b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007J\u001a\u0010\f\u001a\u0004\u0018\u00010\r2\u0006\u0010\u0006\u001a\u00020\u00072\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fJ\u001c\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00070\u0015J\u001c\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00070\u0015J\u0016\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0006\u001a\u00020\u0007J\u0016\u0010\u0018\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0006\u001a\u00020\u0007J\u0016\u0010\u0019\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0006\u001a\u00020\u0007J \u0010\u001a\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0006\u001a\u00020\u00072\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fJ\u0010\u0010\u001b\u001a\u00020\u00052\u0006\u0010\u0012\u001a\u00020\u0013H\u0002¨\u0006\u001c"}, d2 = {"Lni/shikatu/re_extera/ui/messages/MessageMediaActions;", "", "<init>", "()V", "isGalleryMedia", "", "message", "Lorg/telegram/messenger/MessageObject;", "isDownloadable", "filePath", "", "hasFile", "translatableText", "", "group", "Lorg/telegram/messenger/MessageObject$GroupedMessages;", "saveToGallery", "", "fragment", "Lorg/telegram/ui/ActionBar/BaseFragment;", "messages", "", "saveToDownloads", "saveGif", "copyPhoto", "share", "translate", "ensureStoragePermission", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class MessageMediaActions {
    public static final MessageMediaActions INSTANCE = new MessageMediaActions();

    private MessageMediaActions() {
    }

    public final boolean isGalleryMedia(MessageObject message) {
        Intrinsics.checkNotNullParameter(message, "message");
        return message.type == 1 || message.isVideo() || message.isRoundVideo();
    }

    public final boolean isDownloadable(MessageObject message) {
        Intrinsics.checkNotNullParameter(message, "message");
        return (isGalleryMedia(message) || message.isSticker() || message.isAnimatedSticker() || message.getDocument() == null) ? false : true;
    }

    public final String filePath(MessageObject message) {
        Intrinsics.checkNotNullParameter(message, "message");
        return ChatUtils.getInstance(message.currentAccount).getPathToMessage(message);
    }

    public final boolean hasFile(MessageObject message) {
        Intrinsics.checkNotNullParameter(message, "message");
        return filePath(message) != null;
    }

    public final CharSequence translatableText(MessageObject message, MessageObject.GroupedMessages group) {
        Intrinsics.checkNotNullParameter(message, "message");
        return ChatUtils.getInstance(message.currentAccount).getMessageText(message, group);
    }

    /* JADX WARN: Code duplicated, block: B:49:0x00b3  */
    /* JADX WARN: Code duplicated, block: B:50:0x00b6  */
    /* JADX WARN: Code duplicated, block: B:52:0x00bb  */
    /* JADX WARN: Code duplicated, block: B:53:0x00be  */
    public final void saveToGallery(BaseFragment fragment, List<? extends MessageObject> messages) {
        BulletinFactory.FileType fileType;
        Intrinsics.checkNotNullParameter(fragment, "fragment");
        Intrinsics.checkNotNullParameter(messages, "messages");
        Activity parentActivity = fragment.getParentActivity();
        if (parentActivity != null && ensureStoragePermission(fragment)) {
            Iterator<? extends MessageObject> it = messages.iterator();
            int i = 0;
            while (true) {
                int i2 = 1;
                if (!it.hasNext()) {
                    break;
                }
                MessageObject next = it.next();
                String strFilePath = filePath(next);
                if (strFilePath != null) {
                    Activity activity = parentActivity;
                    if (!next.isVideo() && !next.isRoundVideo()) {
                        i2 = 0;
                    }
                    MediaController.saveFile(strFilePath, activity, i2, (String) null, (String) null);
                    i++;
                }
            }
            if (i == 0) {
                BulletinFactory.of(fragment).createErrorBulletin(Strings.INSTANCE.getFILE_NOT_DOWNLOADED()).show();
                return;
            }
            List<? extends MessageObject> list = messages;
            boolean z = list instanceof Collection;
            if (!z || !list.isEmpty()) {
                Iterator<T> it2 = list.iterator();
                while (it2.hasNext()) {
                    if (((MessageObject) it2.next()).type != 1) {
                        if (!z || !list.isEmpty()) {
                            for (MessageObject messageObject : list) {
                                if (!messageObject.isVideo() && !messageObject.isRoundVideo()) {
                                    fileType = BulletinFactory.FileType.MEDIA;
                                }
                            }
                            if (i > 1) {
                                fileType = BulletinFactory.FileType.VIDEOS;
                            } else {
                                fileType = BulletinFactory.FileType.VIDEO;
                            }
                        } else if (i > 1) {
                            fileType = BulletinFactory.FileType.VIDEOS;
                        } else {
                            fileType = BulletinFactory.FileType.VIDEO;
                        }
                    }
                }
                if (i > 1) {
                    fileType = BulletinFactory.FileType.PHOTOS;
                } else {
                    fileType = BulletinFactory.FileType.PHOTO;
                }
            } else if (i > 1) {
                fileType = BulletinFactory.FileType.PHOTOS;
            } else {
                fileType = BulletinFactory.FileType.PHOTO;
            }
            BulletinFactory.of(fragment).createDownloadBulletin(fileType, i, fragment.getResourceProvider()).show();
        }
    }

    public final void saveToDownloads(final BaseFragment fragment, List<? extends MessageObject> messages) {
        Intrinsics.checkNotNullParameter(fragment, "fragment");
        Intrinsics.checkNotNullParameter(messages, "messages");
        Activity parentActivity = fragment.getParentActivity();
        if (parentActivity != null && ensureStoragePermission(fragment)) {
            List<? extends MessageObject> list = messages;
            final boolean z = true;
            if (!(list instanceof Collection) || !list.isEmpty()) {
                Iterator<T> it = list.iterator();
                while (it.hasNext()) {
                    if (!((MessageObject) it.next()).isMusic()) {
                        z = false;
                        break;
                    }
                }
            }
            MediaController.saveFilesFromMessages(parentActivity, fragment.getAccountInstance(), new ArrayList(messages), new MessagesStorage.IntCallback() { // from class: ni.shikatu.re_extera.ui.messages.MessageMediaActions.1
                public final void run(int i) {
                    MessageMediaActions.saveToDownloads$lambda$3(fragment, z, i);
                }
            });
        }
    }

    static final void saveToDownloads$lambda$3(BaseFragment baseFragment, boolean z, int i) {
        if (baseFragment.getParentActivity() == null || baseFragment.fragmentView == null) {
            return;
        }
        if (i > 0) {
            BulletinFactory.of(baseFragment).createDownloadBulletin(z ? BulletinFactory.FileType.AUDIOS : BulletinFactory.FileType.UNKNOWNS, i, baseFragment.getResourceProvider()).show();
        } else {
            BulletinFactory.of(baseFragment).createErrorBulletin(Strings.INSTANCE.getFILE_NOT_DOWNLOADED()).show();
        }
    }

    public final void saveGif(BaseFragment fragment, MessageObject message) {
        Intrinsics.checkNotNullParameter(fragment, "fragment");
        Intrinsics.checkNotNullParameter(message, "message");
        TLRPC.Document document = message.getDocument();
        if (document == null) {
            return;
        }
        fragment.getMessagesController().saveGif(message, document);
        BulletinFactory.of(fragment).createDownloadBulletin(BulletinFactory.FileType.GIF, fragment.getResourceProvider()).show();
    }

    public final void copyPhoto(final BaseFragment fragment, MessageObject message) {
        Intrinsics.checkNotNullParameter(fragment, "fragment");
        Intrinsics.checkNotNullParameter(message, "message");
        ChatUtils.getInstance(message.currentAccount).addMessageToClipboard(message, new Runnable() { // from class: ni.shikatu.re_extera.ui.messages.MessageMediaActions.0
            @Override // java.lang.Runnable
            public final void run() {
                MessageMediaActions.copyPhoto$lambda$4(fragment);
            }
        });
    }

    static final void copyPhoto$lambda$4(BaseFragment baseFragment) {
        if (BulletinFactory.canShowBulletin(baseFragment)) {
            BulletinFactory.of(baseFragment).createCopyBulletin(LocaleController.getString(R.string.PhotoCopied)).show();
        }
    }

    /* JADX WARN: Code duplicated, block: B:22:0x005e  */
    public final void share(BaseFragment fragment, MessageObject message) {
        String str;
        Intrinsics.checkNotNullParameter(fragment, "fragment");
        Intrinsics.checkNotNullParameter(message, "message");
        Activity parentActivity = fragment.getParentActivity();
        if (parentActivity == null) {
            return;
        }
        String strFilePath = filePath(message);
        if (strFilePath == null) {
            BulletinFactory.of(fragment).createErrorBulletin(Strings.INSTANCE.getFILE_NOT_DOWNLOADED()).show();
            return;
        }
        File file = new File(strFilePath);
        Intent intent = new Intent("android.intent.action.SEND");
        TLRPC.Document document = message.getDocument();
        if (document == null || (str = document.mime_type) == null) {
            str = "image/jpeg";
        } else {
            boolean z = str.length() > 0;
            Boolean.valueOf(z).getClass();
            if (!z) {
                str = null;
            }
            if (str == null) {
                str = "image/jpeg";
            }
        }
        intent.setType(str);
        try {
            intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(parentActivity, ApplicationLoader.getApplicationId() + ".provider", file));
            intent.setFlags(1);
        } catch (Exception unused) {
            Intrinsics.checkNotNull(intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(file)));
        }
        try {
            parentActivity.startActivityForResult(Intent.createChooser(intent, LocaleController.getString(R.string.ShareFile)), 500);
        } catch (Exception unused2) {
        }
    }

    public final void translate(BaseFragment fragment, MessageObject message, MessageObject.GroupedMessages group) {
        CharSequence charSequenceTranslatableText;
        Intrinsics.checkNotNullParameter(fragment, "fragment");
        Intrinsics.checkNotNullParameter(message, "message");
        Activity parentActivity = fragment.getParentActivity();
        if (parentActivity == null || (charSequenceTranslatableText = translatableText(message, group)) == null) {
            return;
        }
        TranslateAlert2.showAlert(parentActivity, fragment, fragment.getCurrentAccount(), message.messageOwner.originalLanguage, TranslateAlert2.getToLanguage(), charSequenceTranslatableText, message.messageOwner.entities, false, (Utilities.CallbackReturn) null, (Runnable) null);
    }

    private final boolean ensureStoragePermission(BaseFragment fragment) {
        Activity parentActivity = fragment.getParentActivity();
        if (parentActivity == null) {
            return false;
        }
        if ((Build.VERSION.SDK_INT > 28 && !BuildVars.NO_SCOPED_STORAGE) || parentActivity.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            return true;
        }
        parentActivity.requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 4);
        return false;
    }
}
