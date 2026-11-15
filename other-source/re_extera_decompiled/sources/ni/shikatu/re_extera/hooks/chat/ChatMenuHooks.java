package ni.shikatu.re_extera.hooks.chat;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import java.util.ArrayList;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import ni.shikatu.extensions.FieldRef;
import ni.shikatu.extensions.HookUtilsKt;
import ni.shikatu.re_extera.data.EditHistoryStore;
import ni.shikatu.re_extera.hooks.HookerClass;
import ni.shikatu.re_extera.hooks.ghost.GhostPolicy;
import ni.shikatu.re_extera.localization.Strings;
import ni.shikatu.re_extera.settings.ReSettings;
import ni.shikatu.re_extera.ui.ChatReMenu;
import ni.shikatu.re_extera.ui.messages.EditHistoryFragment;
import ni.shikatu.re_extera.utils.MessageActions;
import ni.shikatu.re_extera.utils.MessageActionsKt;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.R;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ChatActivity;

/* JADX INFO: compiled from: ChatMenuHooks.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u0000 \f2\u00020\u0001:\u0002\u000b\fB\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\t\u001a\u00020\nH\u0014R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"Lni/shikatu/re_extera/hooks/chat/ChatMenuHooks;", "Lni/shikatu/re_extera/hooks/HookerClass;", "<init>", "()V", "selectedObject", "Lni/shikatu/extensions/FieldRef;", "Lorg/telegram/messenger/MessageObject;", "headerItem", "Lorg/telegram/ui/ActionBar/ActionBarMenuItem;", "setup", "", "MenuBuilder", "Companion", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class ChatMenuHooks extends HookerClass {
    private static final Companion Companion = new Companion(null);

    @Deprecated
    public static final int OPTION_EDIT_HISTORY = 6363;

    @Deprecated
    public static final int OPTION_READ = 6565;
    private final FieldRef<MessageObject> selectedObject = new FieldRef<>(ChatActivity.class, "selectedObject");
    private final FieldRef<ActionBarMenuItem> headerItem = new FieldRef<>(ChatActivity.class, "headerItem");

    @Override // ni.shikatu.re_extera.hooks.HookerClass
    protected void setup() {
        hookAfter(ChatActivity.class, "fillMessageMenu", new Class[]{MessageObject.class, ArrayList.class, ArrayList.class, ArrayList.class}, new Function1() { // from class: ni.shikatu.re_extera.hooks.chat.ChatMenuHooks.1
            public final Object invoke(Object obj) {
                return ChatMenuHooks.setup$lambda$0(ChatMenuHooks.this, (XC_MethodHook.MethodHookParam) obj);
            }
        });
        hookBefore(ChatActivity.class, "processSelectedOption", new Class[]{Integer.TYPE}, new Function1() { // from class: ni.shikatu.re_extera.hooks.chat.ChatMenuHooks.2
            public final Object invoke(Object obj) {
                return ChatMenuHooks.setup$lambda$1(ChatMenuHooks.this, (XC_MethodHook.MethodHookParam) obj);
            }
        });
        hookAfter(ChatActivity.class, "createView", new Class[]{Context.class}, new Function1() { // from class: ni.shikatu.re_extera.hooks.chat.ChatMenuHooks.3
            public final Object invoke(Object obj) {
                return ChatMenuHooks.setup$lambda$3(ChatMenuHooks.this, (XC_MethodHook.MethodHookParam) obj);
            }
        });
    }

    static final Unit setup$lambda$0(ChatMenuHooks chatMenuHooks, XC_MethodHook.MethodHookParam methodHookParam) {
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        ChatActivity chatActivity = (ChatActivity) HookUtilsKt.self(methodHookParam);
        MessageObject messageObject = (MessageObject) HookUtilsKt.arg(methodHookParam, 0);
        if (messageObject == null && (messageObject = chatMenuHooks.selectedObject.get(chatActivity)) == null) {
            return Unit.INSTANCE;
        }
        new MenuBuilder((ArrayList) HookUtilsKt.arg(methodHookParam, 1), (ArrayList) HookUtilsKt.arg(methodHookParam, 2), (ArrayList) HookUtilsKt.arg(methodHookParam, 3)).fill(messageObject);
        return Unit.INSTANCE;
    }

    static final Unit setup$lambda$1(ChatMenuHooks chatMenuHooks, XC_MethodHook.MethodHookParam methodHookParam) {
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        ChatActivity chatActivity = (ChatActivity) HookUtilsKt.self(methodHookParam);
        MessageObject messageObject = chatMenuHooks.selectedObject.get(chatActivity);
        if (messageObject == null) {
            return Unit.INSTANCE;
        }
        int iIntValue = ((Number) HookUtilsKt.arg(methodHookParam, 0)).intValue();
        if (iIntValue == 6363) {
            chatActivity.presentFragment(EditHistoryFragment.INSTANCE.newInstance(messageObject.getDialogId(), messageObject.getId()));
        } else if (iIntValue == 6565) {
            MessageActions.INSTANCE.markRead(messageObject);
        }
        return Unit.INSTANCE;
    }

    static final Unit setup$lambda$3(ChatMenuHooks chatMenuHooks, XC_MethodHook.MethodHookParam methodHookParam) {
        ActionBarMenuItem actionBarMenuItem;
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        ChatActivity chatActivity = (ChatActivity) HookUtilsKt.self(methodHookParam);
        if (chatActivity.getChatMode() == 0 && (actionBarMenuItem = chatMenuHooks.headerItem.get(chatActivity)) != null) {
            final ActionBarMenuSubItem actionBarMenuSubItemAddSwipeBackItem = actionBarMenuItem.addSwipeBackItem(R.drawable.filled_giveaway_premium, (Drawable) null, "re:extera", ChatReMenu.INSTANCE.create(chatActivity, actionBarMenuItem));
            actionBarMenuSubItemAddSwipeBackItem.setOnClickListener(new View.OnClickListener() { // from class: ni.shikatu.re_extera.hooks.chat.ChatMenuHooks.0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    actionBarMenuSubItemAddSwipeBackItem.openSwipeBack();
                }
            });
            return Unit.INSTANCE;
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: compiled from: ChatMenuHooks.kt */
    @Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0002\u0018\u00002\u00020\u0001BO\u0012\u0016\u0010\u0002\u001a\u0012\u0012\u0004\u0012\u00020\u00040\u0003j\b\u0012\u0004\u0012\u00020\u0004`\u0005\u0012\u0016\u0010\u0006\u001a\u0012\u0012\u0004\u0012\u00020\u00070\u0003j\b\u0012\u0004\u0012\u00020\u0007`\u0005\u0012\u0016\u0010\b\u001a\u0012\u0012\u0004\u0012\u00020\u00040\u0003j\b\u0012\u0004\u0012\u00020\u0004`\u0005¢\u0006\u0004\b\t\u0010\nJ\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eJ\u0010\u0010\u000f\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0002J \u0010\u0010\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u00072\u0006\u0010\u0013\u001a\u00020\u0004H\u0002J \u0010\u0014\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u00072\u0006\u0010\u0013\u001a\u00020\u0004H\u0002R\u001e\u0010\u0002\u001a\u0012\u0012\u0004\u0012\u00020\u00040\u0003j\b\u0012\u0004\u0012\u00020\u0004`\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\u0006\u001a\u0012\u0012\u0004\u0012\u00020\u00070\u0003j\b\u0012\u0004\u0012\u00020\u0007`\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\b\u001a\u0012\u0012\u0004\u0012\u00020\u00040\u0003j\b\u0012\u0004\u0012\u00020\u0004`\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0015"}, d2 = {"Lni/shikatu/re_extera/hooks/chat/ChatMenuHooks$MenuBuilder;", "", "icons", "Ljava/util/ArrayList;", "", "Lkotlin/collections/ArrayList;", "items", "", "options", "<init>", "(Ljava/util/ArrayList;Ljava/util/ArrayList;Ljava/util/ArrayList;)V", "fill", "", "message", "Lorg/telegram/messenger/MessageObject;", "addOneTimeOptions", "prepend", "icon", "text", "option", "append", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class MenuBuilder {
        private final ArrayList<Integer> icons;
        private final ArrayList<CharSequence> items;
        private final ArrayList<Integer> options;

        public MenuBuilder(ArrayList<Integer> arrayList, ArrayList<CharSequence> arrayList2, ArrayList<Integer> arrayList3) {
            Intrinsics.checkNotNullParameter(arrayList, "icons");
            Intrinsics.checkNotNullParameter(arrayList2, "items");
            Intrinsics.checkNotNullParameter(arrayList3, "options");
            this.icons = arrayList;
            this.items = arrayList2;
            this.options = arrayList3;
        }

        public final void fill(MessageObject message) {
            Intrinsics.checkNotNullParameter(message, "message");
            boolean zIsOneTime = MessageActionsKt.isOneTime(message);
            if (EditHistoryStore.INSTANCE.hasHistory(message)) {
                prepend(R.drawable.menu_premium_clock, Strings.INSTANCE.getEDIT_HISTORY(), ChatMenuHooks.OPTION_EDIT_HISTORY);
            }
            if (!message.isOut() && (zIsOneTime || GhostPolicy.INSTANCE.blocksReading(Long.valueOf(message.getDialogId())))) {
                prepend(R.drawable.msg_markread, Strings.INSTANCE.getREAD_MESSAGE(), ChatMenuHooks.OPTION_READ);
            }
            if (zIsOneTime) {
                if (ReSettings.INSTANCE.getSaveOneTime() || ReSettings.INSTANCE.getNoForwards()) {
                    addOneTimeOptions(message);
                }
            }
        }

        private final void addOneTimeOptions(MessageObject message) {
            int i = R.drawable.msg_forward;
            String string = LocaleController.getString(R.string.Forward);
            Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
            append(i, string, 2);
            TLRPC.Document document = message.getDocument();
            if (message.isVideo()) {
                int i2 = R.drawable.msg_gallery;
                String string2 = LocaleController.getString(R.string.SaveToGallery);
                Intrinsics.checkNotNullExpressionValue(string2, "getString(...)");
                append(i2, string2, 4);
            } else if (message.isMusic()) {
                int i3 = R.drawable.msg_download;
                String string3 = LocaleController.getString(R.string.SaveToMusic);
                Intrinsics.checkNotNullExpressionValue(string3, "getString(...)");
                append(i3, string3, 10);
            } else if (document != null) {
                if (MessageObject.isNewGifDocument(document)) {
                    int i4 = R.drawable.msg_gif;
                    String string4 = LocaleController.getString(R.string.SaveToGIFs);
                    Intrinsics.checkNotNullExpressionValue(string4, "getString(...)");
                    append(i4, string4, 11);
                }
                int i5 = R.drawable.msg_download;
                String string5 = LocaleController.getString(R.string.SaveToDownloads);
                Intrinsics.checkNotNullExpressionValue(string5, "getString(...)");
                append(i5, string5, 10);
            } else if (message.isPhoto()) {
                int i6 = R.drawable.msg_gallery;
                String string6 = LocaleController.getString(R.string.SaveToGallery);
                Intrinsics.checkNotNullExpressionValue(string6, "getString(...)");
                append(i6, string6, 4);
            }
            if (message.isVideo() || message.isMusic() || document != null) {
                int i7 = R.drawable.msg_shareout;
                String string7 = LocaleController.getString(R.string.ShareFile);
                Intrinsics.checkNotNullExpressionValue(string7, "getString(...)");
                append(i7, string7, 6);
            }
        }

        private final void prepend(int icon, CharSequence text, int option) {
            if (this.options.contains(Integer.valueOf(option))) {
                return;
            }
            this.icons.add(0, Integer.valueOf(icon));
            this.items.add(0, text);
            this.options.add(0, Integer.valueOf(option));
        }

        private final void append(int icon, CharSequence text, int option) {
            if (this.options.contains(Integer.valueOf(option))) {
                return;
            }
            this.icons.add(Integer.valueOf(icon));
            this.items.add(text);
            this.options.add(Integer.valueOf(option));
        }
    }

    /* JADX INFO: compiled from: ChatMenuHooks.kt */
    @Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0007"}, d2 = {"Lni/shikatu/re_extera/hooks/chat/ChatMenuHooks$Companion;", "", "<init>", "()V", "OPTION_EDIT_HISTORY", "", "OPTION_READ", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
