package ni.shikatu.re_extera.utils;

import android.os.Bundle;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Collection;
import java.util.HashSet;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.ProfileActivity;

/* JADX INFO: compiled from: ChatUtils.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u001e\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J!\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u00072\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t¢\u0006\u0002\u0010\nJ$\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00070\u000eJ\u001e\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u0011\u001a\u00020\u0010J\u000e\u0010\u0012\u001a\u00020\u00132\u0006\u0010\b\u001a\u00020\tJ\u0016\u0010\u0014\u001a\u00020\f2\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\b\u001a\u00020\tJ\u0016\u0010\u0017\u001a\u00020\f2\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\b\u001a\u00020\t¨\u0006\u0018"}, d2 = {"Lni/shikatu/re_extera/utils/ChatUtils;", "", "<init>", "()V", "currentChat", "Lorg/telegram/ui/ChatActivity;", "account", "", "dialogId", "", "(ILjava/lang/Long;)Lorg/telegram/ui/ChatActivity;", "refreshMessages", "", "messageIds", "", "dialogTitle", "", "fallback", "peerArgs", "Landroid/os/Bundle;", "openChat", "fragment", "Lorg/telegram/ui/ActionBar/BaseFragment;", "openProfile", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class ChatUtils {
    public static final ChatUtils INSTANCE = new ChatUtils();

    private ChatUtils() {
    }

    public static /* synthetic */ ChatActivity currentChat$default(ChatUtils chatUtils, int i, Long l, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            l = null;
        }
        return chatUtils.currentChat(i, l);
    }

    public final ChatActivity currentChat(int account, Long dialogId) {
        ChatActivity lastFragment = LaunchActivity.getLastFragment();
        ChatActivity chatActivity = lastFragment instanceof ChatActivity ? lastFragment : null;
        if (chatActivity == null || chatActivity.getCurrentAccount() != account) {
            return null;
        }
        if (dialogId == null || chatActivity.getDialogId() == dialogId.longValue()) {
            return chatActivity;
        }
        return null;
    }

    public final void refreshMessages(final int account, final long dialogId, Collection<Integer> messageIds) {
        Intrinsics.checkNotNullParameter(messageIds, "messageIds");
        if (messageIds.isEmpty()) {
            return;
        }
        final HashSet hashSet = CollectionsKt.toHashSet(messageIds);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: ni.shikatu.re_extera.utils.ChatUtils.0
            @Override // java.lang.Runnable
            public final void run() {
                ChatUtils.refreshMessages$lambda$0(account, dialogId, hashSet);
            }
        });
    }

    static final void refreshMessages$lambda$0(int i, long j, HashSet hashSet) {
        RecyclerListView chatListView;
        RecyclerView.Adapter adapter;
        MessageObject messageObject;
        ChatActivity chatActivityCurrentChat = INSTANCE.currentChat(i, Long.valueOf(j));
        if (chatActivityCurrentChat == null || (chatListView = chatActivityCurrentChat.getChatListView()) == null || (adapter = chatListView.getAdapter()) == null) {
            return;
        }
        int childCount = chatListView.getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            ChatMessageCell childAt = chatListView.getChildAt(i2);
            ChatMessageCell chatMessageCell = childAt instanceof ChatMessageCell ? childAt : null;
            if (chatMessageCell != null && (messageObject = chatMessageCell.getMessageObject()) != null && hashSet.contains(Integer.valueOf(messageObject.getId()))) {
                int childAdapterPosition = chatListView.getChildAdapterPosition((View) chatMessageCell);
                chatMessageCell.forceResetMessageObject();
                if (childAdapterPosition >= 0) {
                    adapter.notifyItemChanged(childAdapterPosition);
                }
                chatMessageCell.requestLayout();
                chatMessageCell.invalidate();
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:10:0x0031  */
    public final String dialogTitle(int account, long dialogId, String fallback) {
        String name;
        Intrinsics.checkNotNullParameter(fallback, "fallback");
        MessagesController messagesController = MessagesController.getInstance(account);
        if (DialogObject.isUserDialog(dialogId)) {
            TLRPC.User user = messagesController.getUser(Long.valueOf(dialogId));
            if (user != null) {
                name = ContactsController.formatName(user.first_name, user.last_name);
            } else {
                name = null;
            }
        } else {
            TLRPC.Chat chat = messagesController.getChat(Long.valueOf(-dialogId));
            if (chat != null) {
                name = chat.title;
            } else {
                name = null;
            }
        }
        if (name != null) {
            String str = StringsKt.isBlank(name) ? null : name;
            if (str != null) {
                return str;
            }
        }
        return fallback;
    }

    public final Bundle peerArgs(long dialogId) {
        Bundle bundle = new Bundle();
        if (DialogObject.isEncryptedDialog(dialogId)) {
            bundle.putInt("enc_id", DialogObject.getEncryptedChatId(dialogId));
            return bundle;
        }
        if (DialogObject.isUserDialog(dialogId)) {
            bundle.putLong("user_id", dialogId);
            return bundle;
        }
        bundle.putLong("chat_id", -dialogId);
        return bundle;
    }

    public final void openChat(BaseFragment fragment, long dialogId) {
        Intrinsics.checkNotNullParameter(fragment, "fragment");
        fragment.presentFragment(new ChatActivity(peerArgs(dialogId)));
    }

    public final void openProfile(BaseFragment fragment, long dialogId) {
        Intrinsics.checkNotNullParameter(fragment, "fragment");
        if (dialogId == 0) {
            return;
        }
        fragment.presentFragment(new ProfileActivity(peerArgs(dialogId)));
    }
}
