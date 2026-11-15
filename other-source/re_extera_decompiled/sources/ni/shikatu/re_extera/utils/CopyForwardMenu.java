package ni.shikatu.re_extera.utils;

import android.os.Bundle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.R;
import org.telegram.messenger.TopicsController;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.TopicsFragment;

/* JADX INFO: compiled from: CopyForwardMenu.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u001c\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tJ\u001c\u0010\u000b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t¨\u0006\f"}, d2 = {"Lni/shikatu/re_extera/utils/CopyForwardMenu;", "", "<init>", "()V", "forward", "", "fragment", "Lorg/telegram/ui/ActionBar/BaseFragment;", "messages", "", "Lorg/telegram/messenger/MessageObject;", "saveToSaved", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class CopyForwardMenu {
    public static final CopyForwardMenu INSTANCE = new CopyForwardMenu();

    private CopyForwardMenu() {
    }

    public final void forward(BaseFragment fragment, final List<? extends MessageObject> messages) {
        Intrinsics.checkNotNullParameter(fragment, "fragment");
        Intrinsics.checkNotNullParameter(messages, "messages");
        if (messages.isEmpty()) {
            return;
        }
        final int currentAccount = fragment.getCurrentAccount();
        Bundle bundle = new Bundle();
        bundle.putBoolean("onlySelect", true);
        bundle.putInt("dialogsType", 3);
        bundle.putBoolean("allowGroups", true);
        bundle.putBoolean("allowUsers", true);
        bundle.putBoolean("allowChannels", true);
        bundle.putBoolean("allowBots", true);
        BaseFragment dialogsActivity = new DialogsActivity(bundle);
        dialogsActivity.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: ni.shikatu.re_extera.utils.CopyForwardMenu.0
            public final boolean didSelectDialogs(DialogsActivity dialogsActivity2, ArrayList arrayList, CharSequence charSequence, boolean z, boolean z2, int i, int i2, TopicsFragment topicsFragment) {
                return CopyForwardMenu.forward$lambda$5(currentAccount, messages, dialogsActivity2, arrayList, charSequence, z, z2, i, i2, topicsFragment);
            }
        });
        fragment.presentFragment(dialogsActivity);
    }

    /* JADX WARN: Code duplicated, block: B:25:0x006a  */
    static final boolean forward$lambda$5(int i, List list, DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z, boolean z2, int i2, int i3, TopicsFragment topicsFragment) {
        int i4;
        ArrayList arrayList2 = arrayList;
        if (arrayList2 == null || arrayList2.isEmpty()) {
            return false;
        }
        TopicsController topicsController = MessagesController.getInstance(i).getTopicsController();
        Iterator it = arrayList.iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (it.hasNext()) {
            MessagesStorage.TopicKey topicKey = (MessagesStorage.TopicKey) it.next();
            Long lValueOf = Long.valueOf(topicKey.topicId);
            MessageObject messageObject = null;
            if (lValueOf.longValue() == 0) {
                lValueOf = null;
            }
            if (lValueOf != null) {
                TLRPC.TL_forumTopic tL_forumTopicFindTopic = topicsController.findTopic(-topicKey.dialogId, lValueOf.longValue());
                if (tL_forumTopicFindTopic == null) {
                    i4 = i;
                } else {
                    if (tL_forumTopicFindTopic.top_message == 0 || tL_forumTopicFindTopic.topMessage == null) {
                        tL_forumTopicFindTopic = null;
                    }
                    if (tL_forumTopicFindTopic != null) {
                        i4 = i;
                        messageObject = new MessageObject(i4, tL_forumTopicFindTopic.topMessage, false, false);
                    } else {
                        i4 = i;
                    }
                }
            } else {
                i4 = i;
            }
            MessageForwarder.INSTANCE.sendCopies(i4, list, topicKey.dialogId, z2, i2, messageObject);
        }
        dialogsActivity.finishFragment();
        BulletinFactory bulletinFactoryOf = BulletinFactory.of(LaunchActivity.getLastFragment());
        if (arrayList.size() == 1) {
            Boolean.valueOf(bulletinFactoryOf.showForwardedBulletinWithTag(((MessagesStorage.TopicKey) arrayList.get(0)).dialogId, list.size()));
        } else {
            bulletinFactoryOf.createSimpleBulletin(R.raw.forward, LocaleController.formatPluralString("ForwardedMessageCount", list.size(), new Object[0]), LocaleController.formatPluralString("ForwardedToChatsCount", arrayList.size(), new Object[0])).show();
        }
        return true;
    }

    public final void saveToSaved(BaseFragment fragment, List<? extends MessageObject> messages) {
        Intrinsics.checkNotNullParameter(fragment, "fragment");
        Intrinsics.checkNotNullParameter(messages, "messages");
        if (messages.isEmpty()) {
            return;
        }
        int currentAccount = fragment.getCurrentAccount();
        long clientUserId = UserConfig.getInstance(currentAccount).getClientUserId();
        MessageForwarder.INSTANCE.sendCopies(currentAccount, messages, clientUserId, true, 0, null);
        BulletinFactory.of(LaunchActivity.getLastFragment()).showForwardedBulletinWithTag(clientUserId, messages.size());
    }
}
