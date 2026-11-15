package ni.shikatu.re_extera.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import ni.shikatu.re_extera.data.ExclusionKind;
import ni.shikatu.re_extera.localization.Strings;
import ni.shikatu.re_extera.ui.messages.DeletedMessagesFragment;
import ni.shikatu.re_extera.utils.MessageActions;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.PopupSwipeBackLayout;

/* JADX INFO: compiled from: ChatReMenu.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0016\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t¨\u0006\n"}, d2 = {"Lni/shikatu/re_extera/ui/ChatReMenu;", "", "<init>", "()V", "create", "Landroid/view/View;", "chat", "Lorg/telegram/ui/ChatActivity;", "header", "Lorg/telegram/ui/ActionBar/ActionBarMenuItem;", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class ChatReMenu {
    public static final ChatReMenu INSTANCE = new ChatReMenu();

    private ChatReMenu() {
    }

    public final View create(final ChatActivity chat, ActionBarMenuItem header) {
        final PopupSwipeBackLayout swipeBack;
        Intrinsics.checkNotNullParameter(chat, "chat");
        Intrinsics.checkNotNullParameter(header, "header");
        final Context context = chat.getContext();
        Theme.ResourcesProvider resourceProvider = chat.getResourceProvider();
        final long dialogId = chat.getDialogId();
        ViewGroup actionBarPopupWindowLayout = new ActionBarPopupWindow.ActionBarPopupWindowLayout(context, 0, resourceProvider);
        actionBarPopupWindowLayout.setFitItems(true);
        ActionBarPopupWindow.ActionBarPopupWindowLayout popupLayout = header.getPopupLayout();
        if (popupLayout != null && (swipeBack = popupLayout.getSwipeBack()) != null) {
            ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_arrow_back, LocaleController.getString(R.string.Back), false, resourceProvider).setOnClickListener(new View.OnClickListener() { // from class: ni.shikatu.re_extera.ui.ChatReMenu.0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    swipeBack.closeForeground();
                }
            });
        }
        create$item(actionBarPopupWindowLayout, resourceProvider, header, R.drawable.msg_archive_hide, Strings.INSTANCE.getREADING_EXCLUSION(), new Function0() { // from class: ni.shikatu.re_extera.ui.ChatReMenu.1
            public final Object invoke() {
                return ChatReMenu.create$lambda$3(context, dialogId);
            }
        });
        create$item(actionBarPopupWindowLayout, resourceProvider, header, R.drawable.floating_pencil, Strings.INSTANCE.getTYPING_EXCLUSION(), new Function0() { // from class: ni.shikatu.re_extera.ui.ChatReMenu.2
            public final Object invoke() {
                return ChatReMenu.create$lambda$4(context, dialogId);
            }
        });
        create$item(actionBarPopupWindowLayout, resourceProvider, header, R.drawable.chats_archive, Strings.INSTANCE.getVIEW_DELETED(), new Function0() { // from class: ni.shikatu.re_extera.ui.ChatReMenu.3
            public final Object invoke() {
                return ChatReMenu.create$lambda$5(chat, dialogId);
            }
        });
        View gapView = new ActionBarPopupWindow.GapView(context, resourceProvider);
        gapView.setTag(R.id.fit_width_tag, 1);
        actionBarPopupWindowLayout.addView(gapView, LayoutHelper.createLinear(-1, 8));
        create$item(actionBarPopupWindowLayout, resourceProvider, header, R.drawable.msg_clear, Strings.INSTANCE.getCLEAR_DELETED(), new Function0() { // from class: ni.shikatu.re_extera.ui.ChatReMenu.4
            public final Object invoke() {
                return ChatReMenu.create$lambda$7(context, chat, dialogId);
            }
        });
        return actionBarPopupWindowLayout;
    }

    private static final void create$item(ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout, Theme.ResourcesProvider resourcesProvider, final ActionBarMenuItem actionBarMenuItem, int i, CharSequence charSequence, final Function0<Unit> function0) {
        ActionBarMenuItem.addItem((ViewGroup) actionBarPopupWindowLayout, i, charSequence, false, resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: ni.shikatu.re_extera.ui.ChatReMenu.5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ChatReMenu.create$item$lambda$2(actionBarMenuItem, function0, view);
            }
        });
    }

    static final void create$item$lambda$2(ActionBarMenuItem actionBarMenuItem, Function0 function0, View view) {
        actionBarMenuItem.toggleSubMenu();
        function0.invoke();
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: kotlin.NoWhenBranchMatchedException */
    static final Unit create$lambda$3(Context context, long j) throws NoWhenBranchMatchedException {
        ExclusionDialog exclusionDialog = ExclusionDialog.INSTANCE;
        Intrinsics.checkNotNull(context);
        ExclusionDialog.show$default(exclusionDialog, context, j, ExclusionKind.READING, null, 8, null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: kotlin.NoWhenBranchMatchedException */
    static final Unit create$lambda$4(Context context, long j) throws NoWhenBranchMatchedException {
        ExclusionDialog exclusionDialog = ExclusionDialog.INSTANCE;
        Intrinsics.checkNotNull(context);
        ExclusionDialog.show$default(exclusionDialog, context, j, ExclusionKind.TYPING, null, 8, null);
        return Unit.INSTANCE;
    }

    static final Unit create$lambda$5(ChatActivity chatActivity, long j) {
        chatActivity.presentFragment(DeletedMessagesFragment.INSTANCE.newInstance(j));
        return Unit.INSTANCE;
    }

    static final Unit create$lambda$7(Context context, final ChatActivity chatActivity, final long j) {
        Confirm confirm = Confirm.INSTANCE;
        Intrinsics.checkNotNull(context);
        String clear_deleted = Strings.INSTANCE.getCLEAR_DELETED();
        String clear_deleted_confirm = Strings.INSTANCE.getCLEAR_DELETED_CONFIRM();
        String string = LocaleController.getString(R.string.Delete);
        Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
        Confirm.show$default(confirm, context, clear_deleted, clear_deleted_confirm, string, false, new Function0() { // from class: ni.shikatu.re_extera.ui.ChatReMenu.6
            public final Object invoke() {
                return ChatReMenu.create$lambda$7$lambda$6(chatActivity, j);
            }
        }, 16, null);
        return Unit.INSTANCE;
    }

    static final Unit create$lambda$7$lambda$6(ChatActivity chatActivity, long j) {
        MessageActions.INSTANCE.purgeDialog(chatActivity.getCurrentAccount(), j);
        return Unit.INSTANCE;
    }
}
