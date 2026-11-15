package ni.shikatu.re_extera.ui.messages;

import android.os.Bundle;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import ni.shikatu.re_extera.data.EditHistoryStore;
import ni.shikatu.re_extera.localization.Strings;
import org.telegram.tgnet.TLRPC;

/* JADX INFO: compiled from: MessageListScreens.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\r\n\u0002\b\u0003\u0018\u0000 \u001d2\u00020\u0001:\u0001\u001dB\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\"\u0010\u0012\u001a\u00020\u00132\u0018\u0010\u0014\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00170\u0016\u0012\u0004\u0012\u00020\u00130\u0015H\u0014J\u0010\u0010\u0018\u001a\u00020\u00132\u0006\u0010\u0019\u001a\u00020\u0017H\u0014J\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u0007H\u0014R\u0014\u0010\u0006\u001a\u00020\u00078BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0014\u0010\n\u001a\u00020\u000b8TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\rR\u0014\u0010\u000e\u001a\u00020\u000f8TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011¨\u0006\u001e"}, d2 = {"Lni/shikatu/re_extera/ui/messages/EditHistoryFragment;", "Lni/shikatu/re_extera/ui/messages/MessageListFragment;", "args", "Landroid/os/Bundle;", "<init>", "(Landroid/os/Bundle;)V", "messageId", "", "getMessageId", "()I", "fallbackTitle", "", "getFallbackTitle", "()Ljava/lang/String;", "groupAlbums", "", "getGroupAlbums", "()Z", "loadMessages", "", "onLoaded", "Lkotlin/Function1;", "", "Lorg/telegram/tgnet/TLRPC$Message;", "prepare", "message", "subtitle", "", "count", "Companion", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class EditHistoryFragment extends MessageListFragment {
    private static final String ARG_MESSAGE_ID = "mid";

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);

    @Override // ni.shikatu.re_extera.ui.messages.MessageListFragment
    protected boolean getGroupAlbums() {
        return false;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public EditHistoryFragment(Bundle bundle) {
        super(bundle);
        Intrinsics.checkNotNullParameter(bundle, "args");
    }

    private final int getMessageId() {
        Bundle bundle = this.arguments;
        if (bundle != null) {
            return bundle.getInt(ARG_MESSAGE_ID);
        }
        return 0;
    }

    @Override // ni.shikatu.re_extera.ui.messages.MessageListFragment
    protected String getFallbackTitle() {
        return Strings.INSTANCE.getEDIT_HISTORY_TITLE();
    }

    @Override // ni.shikatu.re_extera.ui.messages.MessageListFragment
    protected void loadMessages(Function1<? super List<? extends TLRPC.Message>, Unit> onLoaded) {
        Intrinsics.checkNotNullParameter(onLoaded, "onLoaded");
        EditHistoryStore.INSTANCE.loadVersions(getDialogId(), getMessageId(), onLoaded);
    }

    @Override // ni.shikatu.re_extera.ui.messages.MessageListFragment
    protected void prepare(TLRPC.Message message) {
        Intrinsics.checkNotNullParameter(message, "message");
        if (message.edit_date != 0) {
            message.date = message.edit_date;
        }
    }

    @Override // ni.shikatu.re_extera.ui.messages.MessageListFragment
    protected CharSequence subtitle(int count) {
        return Strings.INSTANCE.getEDIT_HISTORY_TITLE();
    }

    /* JADX INFO: compiled from: MessageListScreens.kt */
    @Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0016\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000¨\u0006\f"}, d2 = {"Lni/shikatu/re_extera/ui/messages/EditHistoryFragment$Companion;", "", "<init>", "()V", "ARG_MESSAGE_ID", "", "newInstance", "Lni/shikatu/re_extera/ui/messages/EditHistoryFragment;", "dialogId", "", "messageId", "", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final EditHistoryFragment newInstance(long dialogId, int messageId) {
            Bundle bundle = new Bundle();
            bundle.putLong(MessageListFragment.ARG_DIALOG_ID, dialogId);
            bundle.putInt(EditHistoryFragment.ARG_MESSAGE_ID, messageId);
            return new EditHistoryFragment(bundle);
        }
    }
}
