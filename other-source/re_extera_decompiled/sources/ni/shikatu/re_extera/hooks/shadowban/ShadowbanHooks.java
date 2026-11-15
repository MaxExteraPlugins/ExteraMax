package ni.shikatu.re_extera.hooks.shadowban;

import android.app.Activity;
import android.view.View;
import androidx.collection.LongSparseArray;
import de.robv.android.xposed.XC_MethodHook;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import ni.shikatu.extensions.FieldRef;
import ni.shikatu.extensions.HookUtilsKt;
import ni.shikatu.re_extera.data.ShadowbanStore;
import ni.shikatu.re_extera.hooks.HookerClass;
import ni.shikatu.re_extera.localization.Strings;
import ni.shikatu.re_extera.ui.ShadowbanDialog;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.R;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.ProfileActivity;

/* JADX INFO: compiled from: ShadowbanHooks.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\r\u001a\u00020\u000eH\u0014J\u0018\u0010\u000f\u001a\u00020\u000e*\u000e\u0012\u0002\b\u00030\u0006j\u0006\u0012\u0002\b\u0003`\bH\u0002J\f\u0010\u0010\u001a\u00020\u0011*\u00020\u0012H\u0002J\u0010\u0010\u0013\u001a\u00020\u000e2\u0006\u0010\u0014\u001a\u00020\u0015H\u0002R$\u0010\u0004\u001a\u0018\u0012\u0014\u0012\u0012\u0012\u0004\u0012\u00020\u00070\u0006j\b\u0012\u0004\u0012\u00020\u0007`\b0\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0016"}, d2 = {"Lni/shikatu/re_extera/hooks/shadowban/ShadowbanHooks;", "Lni/shikatu/re_extera/hooks/HookerClass;", "<init>", "()V", "allDialogs", "Lni/shikatu/extensions/FieldRef;", "Ljava/util/ArrayList;", "Lorg/telegram/tgnet/TLRPC$Dialog;", "Lkotlin/collections/ArrayList;", "otherItem", "Lorg/telegram/ui/ActionBar/ActionBarMenuItem;", "profileUserId", "", "setup", "", "filterHidden", "isHiddenNotification", "", "Lorg/telegram/messenger/MessageObject;", "addProfileMenuItem", "profile", "Lorg/telegram/ui/ProfileActivity;", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class ShadowbanHooks extends HookerClass {
    private final FieldRef<ArrayList<TLRPC.Dialog>> allDialogs = new FieldRef<>(MessagesController.class, "allDialogs");
    private final FieldRef<ActionBarMenuItem> otherItem = new FieldRef<>(ProfileActivity.class, "otherItem");
    private final FieldRef<Long> profileUserId = new FieldRef<>(ProfileActivity.class, "userId");

    @Override // ni.shikatu.re_extera.hooks.HookerClass
    protected void setup() {
        hookAfter(MessagesController.class, "getDialogs", new Class[]{Integer.TYPE}, new Function1() { // from class: ni.shikatu.re_extera.hooks.shadowban.ShadowbanHooks.4
            public final Object invoke(Object obj) {
                return ShadowbanHooks.setup$lambda$0(ShadowbanHooks.this, (XC_MethodHook.MethodHookParam) obj);
            }
        });
        hookAfter(MessagesController.class, "sortDialogs", new Class[]{LongSparseArray.class}, new Function1() { // from class: ni.shikatu.re_extera.hooks.shadowban.ShadowbanHooks.5
            public final Object invoke(Object obj) {
                return ShadowbanHooks.setup$lambda$1(ShadowbanHooks.this, (XC_MethodHook.MethodHookParam) obj);
            }
        });
        hookBefore(MessagesController.class, "processLoadedDialogs", new Class[]{TLRPC.messages_Dialogs.class, ArrayList.class, ArrayList.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Boolean.TYPE, Boolean.TYPE, Boolean.TYPE}, new Function1() { // from class: ni.shikatu.re_extera.hooks.shadowban.ShadowbanHooks.6
            public final Object invoke(Object obj) {
                return ShadowbanHooks.setup$lambda$2(ShadowbanHooks.this, (XC_MethodHook.MethodHookParam) obj);
            }
        });
        hookAfter(DialogsActivity.class, "getDialogsArray", new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE, Boolean.TYPE}, new Function1() { // from class: ni.shikatu.re_extera.hooks.shadowban.ShadowbanHooks.7
            public final Object invoke(Object obj) {
                return ShadowbanHooks.setup$lambda$3(ShadowbanHooks.this, (XC_MethodHook.MethodHookParam) obj);
            }
        });
        hookBefore(NotificationsController.class, "processNewMessages", new Class[]{ArrayList.class, Boolean.TYPE, Boolean.TYPE, CountDownLatch.class}, new Function1() { // from class: ni.shikatu.re_extera.hooks.shadowban.ShadowbanHooks.8
            public final Object invoke(Object obj) {
                return ShadowbanHooks.setup$lambda$5(ShadowbanHooks.this, (XC_MethodHook.MethodHookParam) obj);
            }
        });
        hookAfter(ProfileActivity.class, "createActionBarMenu", new Class[]{Boolean.TYPE}, new Function1() { // from class: ni.shikatu.re_extera.hooks.shadowban.ShadowbanHooks.9
            public final Object invoke(Object obj) {
                return ShadowbanHooks.setup$lambda$6(ShadowbanHooks.this, (XC_MethodHook.MethodHookParam) obj);
            }
        });
    }

    static final Unit setup$lambda$0(ShadowbanHooks shadowbanHooks, XC_MethodHook.MethodHookParam methodHookParam) {
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        Object result = methodHookParam.getResult();
        ArrayList<?> arrayList = result instanceof ArrayList ? (ArrayList) result : null;
        if (arrayList != null) {
            shadowbanHooks.filterHidden(arrayList);
        }
        return Unit.INSTANCE;
    }

    static final Unit setup$lambda$1(ShadowbanHooks shadowbanHooks, XC_MethodHook.MethodHookParam methodHookParam) {
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        ArrayList<TLRPC.Dialog> arrayList = shadowbanHooks.allDialogs.get(methodHookParam.thisObject);
        if (arrayList != null) {
            shadowbanHooks.filterHidden(arrayList);
        }
        return Unit.INSTANCE;
    }

    static final Unit setup$lambda$2(ShadowbanHooks shadowbanHooks, XC_MethodHook.MethodHookParam methodHookParam) {
        ArrayList<?> arrayList;
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        TLRPC.messages_Dialogs messages_dialogs = (TLRPC.messages_Dialogs) HookUtilsKt.arg(methodHookParam, 0);
        if (messages_dialogs != null && (arrayList = messages_dialogs.dialogs) != null) {
            shadowbanHooks.filterHidden(arrayList);
        }
        return Unit.INSTANCE;
    }

    static final Unit setup$lambda$3(ShadowbanHooks shadowbanHooks, XC_MethodHook.MethodHookParam methodHookParam) {
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        Object result = methodHookParam.getResult();
        ArrayList<?> arrayList = result instanceof ArrayList ? (ArrayList) result : null;
        if (arrayList != null) {
            shadowbanHooks.filterHidden(arrayList);
        }
        return Unit.INSTANCE;
    }

    static final Unit setup$lambda$5(ShadowbanHooks shadowbanHooks, XC_MethodHook.MethodHookParam methodHookParam) {
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        if (ShadowbanStore.INSTANCE.isEmpty()) {
            return Unit.INSTANCE;
        }
        ArrayList arrayList = (ArrayList) HookUtilsKt.arg(methodHookParam, 0);
        if (arrayList != null) {
            CollectionsKt.removeAll(arrayList, new Function1() { // from class: ni.shikatu.re_extera.hooks.shadowban.ShadowbanHooks.3
                public final Object invoke(Object obj) {
                    return Boolean.valueOf(ShadowbanHooks.setup$lambda$5$lambda$4(ShadowbanHooks.this, (MessageObject) obj));
                }
            });
        }
        return Unit.INSTANCE;
    }

    static final boolean setup$lambda$5$lambda$4(ShadowbanHooks shadowbanHooks, MessageObject messageObject) {
        Intrinsics.checkNotNullParameter(messageObject, "it");
        return shadowbanHooks.isHiddenNotification(messageObject);
    }

    static final Unit setup$lambda$6(ShadowbanHooks shadowbanHooks, XC_MethodHook.MethodHookParam methodHookParam) {
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        shadowbanHooks.addProfileMenuItem((ProfileActivity) HookUtilsKt.self(methodHookParam));
        return Unit.INSTANCE;
    }

    private final void filterHidden(ArrayList<?> arrayList) {
        if (ShadowbanStore.INSTANCE.isEmpty() || arrayList.isEmpty()) {
            return;
        }
        CollectionsKt.removeAll(arrayList, new Function1<?, Boolean>() { // from class: ni.shikatu.re_extera.hooks.shadowban.ShadowbanHooks.filterHidden.1
            /* JADX INFO: renamed from: invoke, reason: merged with bridge method [inline-methods] */
            public final Boolean m0invoke(Object obj) {
                TLRPC.Dialog dialog = obj instanceof TLRPC.Dialog ? (TLRPC.Dialog) obj : null;
                boolean z = false;
                if (dialog != null && ShadowbanStore.INSTANCE.hidesDialog(dialog.id)) {
                    z = true;
                }
                return Boolean.valueOf(z);
            }
        });
    }

    private final boolean isHiddenNotification(MessageObject messageObject) {
        if (messageObject.isOut()) {
            return false;
        }
        long fromChatId = messageObject.getFromChatId();
        if (fromChatId <= 0) {
            return false;
        }
        if (ShadowbanStore.INSTANCE.hidesInGroups(fromChatId)) {
            return true;
        }
        return messageObject.getDialogId() > 0 && ShadowbanStore.INSTANCE.hidesDialog(fromChatId);
    }

    private final void addProfileMenuItem(final ProfileActivity profile) {
        Long l;
        final ActionBarMenuItem actionBarMenuItem = this.otherItem.get(profile);
        if (actionBarMenuItem == null || (l = this.profileUserId.get(profile)) == null) {
            return;
        }
        final long jLongValue = l.longValue();
        if (jLongValue <= 0) {
            return;
        }
        final int currentAccount = profile.getCurrentAccount();
        if (ShadowbanStore.INSTANCE.contains(jLongValue)) {
            actionBarMenuItem.addSubItem(0, R.drawable.msg_block2, Strings.INSTANCE.getREMOVE_FROM_SHADOWBAN()).setOnClickListener(new View.OnClickListener() { // from class: ni.shikatu.re_extera.hooks.shadowban.ShadowbanHooks.1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    ShadowbanHooks.addProfileMenuItem$lambda$7(actionBarMenuItem, jLongValue, currentAccount, profile, view);
                }
            });
        } else {
            actionBarMenuItem.addSubItem(0, R.drawable.msg_block, Strings.INSTANCE.getADD_TO_SHADOWBAN()).setOnClickListener(new View.OnClickListener() { // from class: ni.shikatu.re_extera.hooks.shadowban.ShadowbanHooks.2
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    ShadowbanHooks.addProfileMenuItem$lambda$9(actionBarMenuItem, profile, jLongValue, currentAccount, view);
                }
            });
        }
    }

    static final void addProfileMenuItem$lambda$7(ActionBarMenuItem actionBarMenuItem, long j, int i, ProfileActivity profileActivity, View view) {
        actionBarMenuItem.closeSubMenu();
        ShadowbanStore.INSTANCE.remove(j);
        ShadowbanStore.INSTANCE.notifyDialogsChanged(i);
        profileActivity.finishFragment();
    }

    static final void addProfileMenuItem$lambda$9(ActionBarMenuItem actionBarMenuItem, final ProfileActivity profileActivity, final long j, final int i, View view) {
        actionBarMenuItem.closeSubMenu();
        ShadowbanDialog shadowbanDialog = ShadowbanDialog.INSTANCE;
        Activity parentActivity = profileActivity.getParentActivity();
        if (parentActivity == null) {
            return;
        }
        shadowbanDialog.show(parentActivity, null, new Function2() { // from class: ni.shikatu.re_extera.hooks.shadowban.ShadowbanHooks.0
            public final Object invoke(Object obj, Object obj2) {
                return ShadowbanHooks.addProfileMenuItem$lambda$9$lambda$8(j, i, profileActivity, ((Boolean) obj).booleanValue(), ((Boolean) obj2).booleanValue());
            }
        });
    }

    static final Unit addProfileMenuItem$lambda$9$lambda$8(long j, int i, ProfileActivity profileActivity, boolean z, boolean z2) {
        ShadowbanStore.INSTANCE.put(j, z, z2);
        ShadowbanStore.INSTANCE.notifyDialogsChanged(i);
        profileActivity.finishFragment();
        return Unit.INSTANCE;
    }
}
