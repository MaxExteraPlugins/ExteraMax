package ni.shikatu.re_extera.ui.messages;

import android.os.Bundle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import ni.shikatu.re_extera.data.DeletedMessagesStore;
import ni.shikatu.re_extera.localization.Strings;
import ni.shikatu.re_extera.utils.MessageActions;
import ni.shikatu.re_extera.utils.TelegramStorage;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesStorage;
import org.telegram.tgnet.TLRPC;

/* JADX INFO: compiled from: MessageListScreens.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010$\n\u0002\u0010\b\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\r\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 '2\u00020\u0001:\u0001'B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\b\u0010\u0018\u001a\u00020\u0013H\u0016J\"\u0010\u0019\u001a\u00020\u001a2\u0018\u0010\u001b\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001e0\u001d\u0012\u0004\u0012\u00020\u001a0\u001cH\u0014J\u0010\u0010\u001f\u001a\u00020\u000f2\u0006\u0010 \u001a\u00020\bH\u0014J\u0012\u0010!\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\"\u001a\u00020#H\u0014J\u0016\u0010$\u001a\u00020\u001a2\f\u0010%\u001a\b\u0012\u0004\u0012\u00020#0\u001dH\u0014J\b\u0010&\u001a\u00020\u001aH\u0014R\u001a\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\u00020\u000b8TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\rR\u0014\u0010\u000e\u001a\u00020\u000f8TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u0014\u0010\u0012\u001a\u00020\u00138TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0015R\u0014\u0010\u0016\u001a\u00020\u00138TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b\u0017\u0010\u0015¨\u0006("}, d2 = {"Lni/shikatu/re_extera/ui/messages/DeletedMessagesFragment;", "Lni/shikatu/re_extera/ui/messages/MessageListFragment;", "args", "Landroid/os/Bundle;", "<init>", "(Landroid/os/Bundle;)V", "deletionTimes", "", "", "", "fallbackTitle", "", "getFallbackTitle", "()Ljava/lang/String;", "emptyText", "", "getEmptyText", "()Ljava/lang/CharSequence;", "canDelete", "", "getCanDelete", "()Z", "canOpenEditHistory", "getCanOpenEditHistory", "onFragmentCreate", "loadMessages", "", "onLoaded", "Lkotlin/Function1;", "", "Lorg/telegram/tgnet/TLRPC$Message;", "subtitle", "count", "menuFooter", "message", "Lorg/telegram/messenger/MessageObject;", "deleteMessages", "messages", "clearAll", "Companion", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class DeletedMessagesFragment extends MessageListFragment {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private Map<Integer, Long> deletionTimes;

    @Override // ni.shikatu.re_extera.ui.messages.MessageListFragment
    protected boolean getCanDelete() {
        return true;
    }

    @Override // ni.shikatu.re_extera.ui.messages.MessageListFragment
    protected boolean getCanOpenEditHistory() {
        return true;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public DeletedMessagesFragment(Bundle bundle) {
        super(bundle);
        Intrinsics.checkNotNullParameter(bundle, "args");
        this.deletionTimes = MapsKt.emptyMap();
    }

    @Override // ni.shikatu.re_extera.ui.messages.MessageListFragment
    protected String getFallbackTitle() {
        return Strings.INSTANCE.getDELETED_MESSAGES();
    }

    @Override // ni.shikatu.re_extera.ui.messages.MessageListFragment
    protected CharSequence getEmptyText() {
        return Strings.INSTANCE.getNO_DELETED();
    }

    static final Unit onFragmentCreate$lambda$0(DeletedMessagesFragment deletedMessagesFragment, Map map) {
        Intrinsics.checkNotNullParameter(map, "it");
        deletedMessagesFragment.deletionTimes = map;
        return Unit.INSTANCE;
    }

    @Override // ni.shikatu.re_extera.ui.messages.MessageListFragment
    public boolean onFragmentCreate() {
        DeletedMessagesStore.INSTANCE.deletionTimes(getDialogId(), new Function1() { // from class: ni.shikatu.re_extera.ui.messages.DeletedMessagesFragment.2
            public final Object invoke(Object obj) {
                return DeletedMessagesFragment.onFragmentCreate$lambda$0(DeletedMessagesFragment.this, (Map) obj);
            }
        });
        return super.onFragmentCreate();
    }

    @Override // ni.shikatu.re_extera.ui.messages.MessageListFragment
    protected void loadMessages(final Function1<? super List<? extends TLRPC.Message>, Unit> onLoaded) {
        Intrinsics.checkNotNullParameter(onLoaded, "onLoaded");
        final List<Integer> listIds = DeletedMessagesStore.INSTANCE.ids(getDialogId());
        final MessagesStorage messagesStorage = getMessagesStorage();
        messagesStorage.getStorageQueue().postRunnable(new Runnable() { // from class: ni.shikatu.re_extera.ui.messages.DeletedMessagesFragment.1
            @Override // java.lang.Runnable
            public final void run() {
                DeletedMessagesFragment.loadMessages$lambda$2(messagesStorage, this, listIds, onLoaded);
            }
        });
    }

    static final void loadMessages$lambda$2(MessagesStorage messagesStorage, DeletedMessagesFragment deletedMessagesFragment, List list, final Function1 function1) {
        TelegramStorage telegramStorage = TelegramStorage.INSTANCE;
        Intrinsics.checkNotNull(messagesStorage);
        final List<TLRPC.Message> listLoadMessages = telegramStorage.loadMessages(messagesStorage, deletedMessagesFragment.getDialogId(), list);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: ni.shikatu.re_extera.ui.messages.DeletedMessagesFragment.0
            @Override // java.lang.Runnable
            public final void run() {
                function1.invoke(listLoadMessages);
            }
        });
    }

    @Override // ni.shikatu.re_extera.ui.messages.MessageListFragment
    protected CharSequence subtitle(int count) {
        String deleted_messages;
        if (count <= 0) {
            deleted_messages = Strings.INSTANCE.getDELETED_MESSAGES();
        } else {
            StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
            deleted_messages = String.format(Strings.INSTANCE.getDELETED_SUBTITLE(), Arrays.copyOf(new Object[]{LocaleController.formatPluralString("messages", count, new Object[0])}, 1));
            Intrinsics.checkNotNullExpressionValue(deleted_messages, "format(...)");
        }
        return deleted_messages;
    }

    @Override // ni.shikatu.re_extera.ui.messages.MessageListFragment
    protected CharSequence menuFooter(MessageObject message) {
        Intrinsics.checkNotNullParameter(message, "message");
        Long l = this.deletionTimes.get(Integer.valueOf(message.getId()));
        if (l == null) {
            return null;
        }
        long jLongValue = l.longValue();
        StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
        String str = String.format(Strings.INSTANCE.getDELETED_AT(), Arrays.copyOf(new Object[]{LocaleController.formatDateTime(jLongValue / 1000, true)}, 1));
        Intrinsics.checkNotNullExpressionValue(str, "format(...)");
        return str;
    }

    @Override // ni.shikatu.re_extera.ui.messages.MessageListFragment
    protected void deleteMessages(List<? extends MessageObject> messages) {
        Intrinsics.checkNotNullParameter(messages, "messages");
        MessageActions messageActions = MessageActions.INSTANCE;
        int i = this.currentAccount;
        long dialogId = getDialogId();
        List<? extends MessageObject> list = messages;
        ArrayList arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list, 10));
        Iterator<T> it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(Integer.valueOf(((MessageObject) it.next()).getId()));
        }
        messageActions.purge(i, dialogId, arrayList);
        removeMessages(messages);
    }

    @Override // ni.shikatu.re_extera.ui.messages.MessageListFragment
    protected void clearAll() {
        MessageActions.INSTANCE.purgeDialog(this.currentAccount, getDialogId());
        removeMessages(getMessages());
    }

    /* JADX INFO: compiled from: MessageListScreens.kt */
    @Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007¨\u0006\b"}, d2 = {"Lni/shikatu/re_extera/ui/messages/DeletedMessagesFragment$Companion;", "", "<init>", "()V", "newInstance", "Lni/shikatu/re_extera/ui/messages/DeletedMessagesFragment;", "dialogId", "", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final DeletedMessagesFragment newInstance(long dialogId) {
            Bundle bundle = new Bundle();
            bundle.putLong(MessageListFragment.ARG_DIALOG_ID, dialogId);
            return new DeletedMessagesFragment(bundle);
        }
    }
}
