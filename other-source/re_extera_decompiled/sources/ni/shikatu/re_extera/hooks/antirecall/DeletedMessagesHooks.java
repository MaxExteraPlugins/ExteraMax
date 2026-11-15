package ni.shikatu.re_extera.hooks.antirecall;

import androidx.collection.LongSparseArray;
import de.robv.android.xposed.XC_MethodHook;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
import ni.shikatu.extensions.HookUtilsKt;
import ni.shikatu.re_extera.data.DeletedMessagesStore;
import ni.shikatu.re_extera.hooks.HookerClass;
import ni.shikatu.re_extera.settings.ReSettings;
import ni.shikatu.re_extera.utils.ChatUtils;
import ni.shikatu.re_extera.utils.DeletionBypass;
import ni.shikatu.re_extera.utils.MessageActions;
import ni.shikatu.re_extera.utils.TelegramStorage;
import ni.shikatu.re_extera.utils.TelegramStorageKt;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BaseController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationsController;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.RecyclerListView;

/* JADX INFO: compiled from: DeletedMessagesHooks.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\"\n\u0002\u0010\b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u0004\u001a\u00020\u0005H\u0014J\u0014\u0010\u0006\u001a\u00020\u00052\n\u0010\u0007\u001a\u00060\bj\u0002`\tH\u0002J\u0014\u0010\n\u001a\u00020\u00052\n\u0010\u0007\u001a\u00060\bj\u0002`\tH\u0002J\u001e\u0010\u000b\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fH\u0002J\u0014\u0010\u0011\u001a\u00020\u00052\n\u0010\u0007\u001a\u00060\bj\u0002`\tH\u0002¨\u0006\u0012"}, d2 = {"Lni/shikatu/re_extera/hooks/antirecall/DeletedMessagesHooks;", "Lni/shikatu/re_extera/hooks/HookerClass;", "<init>", "()V", "setup", "", "onStorageDelete", "param", "Lde/robv/android/xposed/XC_MethodHook$MethodHookParam;", "Lni/shikatu/extensions/HookParam;", "onChatDelete", "clearDeletedFlag", "chat", "Lorg/telegram/ui/ChatActivity;", "ids", "", "", "onManualDelete", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class DeletedMessagesHooks extends HookerClass {
    @Override // ni.shikatu.re_extera.hooks.HookerClass
    protected void setup() {
        hookBefore(MessagesStorage.class, "markMessagesAsDeletedInternal", new Class[]{Long.TYPE, ArrayList.class, Boolean.TYPE, Integer.TYPE, Integer.TYPE}, new C00031(this));
        hookBefore(NotificationsController.class, "removeDeletedMessagesFromNotifications", new Class[]{LongSparseArray.class, Boolean.TYPE}, new Function1() { // from class: ni.shikatu.re_extera.hooks.antirecall.DeletedMessagesHooks.0
            public final Object invoke(Object obj) {
                return DeletedMessagesHooks.setup$lambda$0((XC_MethodHook.MethodHookParam) obj);
            }
        });
        hookBefore(ChatActivity.class, "processDeletedMessages", new Class[]{ArrayList.class, Long.TYPE, Boolean.TYPE, Boolean.TYPE}, new AnonymousClass3(this));
        hookBefore(MessagesController.class, "deleteMessages", new Class[]{ArrayList.class, ArrayList.class, TLRPC.EncryptedChat.class, Long.TYPE, Integer.TYPE, Boolean.TYPE, Integer.TYPE}, new AnonymousClass4(this));
        hookBefore(MessageObject.class, "canDeleteMessage", new Class[]{Boolean.TYPE, TLRPC.Chat.class}, new Function1() { // from class: ni.shikatu.re_extera.hooks.antirecall.DeletedMessagesHooks.1
            public final Object invoke(Object obj) {
                return DeletedMessagesHooks.setup$lambda$1((XC_MethodHook.MethodHookParam) obj);
            }
        });
    }

    /* JADX INFO: renamed from: ni.shikatu.re_extera.hooks.antirecall.DeletedMessagesHooks$setup$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: DeletedMessagesHooks.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    static final /* synthetic */ class C00031 extends FunctionReferenceImpl implements Function1<XC_MethodHook.MethodHookParam, Unit> {
        C00031(Object obj) {
            super(1, obj, DeletedMessagesHooks.class, "onStorageDelete", "onStorageDelete(Lde/robv/android/xposed/XC_MethodHook$MethodHookParam;)V", 0);
        }

        public /* bridge */ /* synthetic */ Object invoke(Object obj) {
            invoke((XC_MethodHook.MethodHookParam) obj);
            return Unit.INSTANCE;
        }

        public final void invoke(XC_MethodHook.MethodHookParam methodHookParam) {
            Intrinsics.checkNotNullParameter(methodHookParam, "p0");
            ((DeletedMessagesHooks) this.receiver).onStorageDelete(methodHookParam);
        }
    }

    static final Unit setup$lambda$0(XC_MethodHook.MethodHookParam methodHookParam) {
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        if (ReSettings.INSTANCE.getSaveDeleted() && !((Boolean) HookUtilsKt.arg(methodHookParam, 1)).booleanValue()) {
            methodHookParam.setResult((Object) null);
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: renamed from: ni.shikatu.re_extera.hooks.antirecall.DeletedMessagesHooks$setup$3, reason: invalid class name */
    /* JADX INFO: compiled from: DeletedMessagesHooks.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    static final /* synthetic */ class AnonymousClass3 extends FunctionReferenceImpl implements Function1<XC_MethodHook.MethodHookParam, Unit> {
        AnonymousClass3(Object obj) {
            super(1, obj, DeletedMessagesHooks.class, "onChatDelete", "onChatDelete(Lde/robv/android/xposed/XC_MethodHook$MethodHookParam;)V", 0);
        }

        public /* bridge */ /* synthetic */ Object invoke(Object obj) {
            invoke((XC_MethodHook.MethodHookParam) obj);
            return Unit.INSTANCE;
        }

        public final void invoke(XC_MethodHook.MethodHookParam methodHookParam) {
            Intrinsics.checkNotNullParameter(methodHookParam, "p0");
            ((DeletedMessagesHooks) this.receiver).onChatDelete(methodHookParam);
        }
    }

    /* JADX INFO: renamed from: ni.shikatu.re_extera.hooks.antirecall.DeletedMessagesHooks$setup$4, reason: invalid class name */
    /* JADX INFO: compiled from: DeletedMessagesHooks.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    static final /* synthetic */ class AnonymousClass4 extends FunctionReferenceImpl implements Function1<XC_MethodHook.MethodHookParam, Unit> {
        AnonymousClass4(Object obj) {
            super(1, obj, DeletedMessagesHooks.class, "onManualDelete", "onManualDelete(Lde/robv/android/xposed/XC_MethodHook$MethodHookParam;)V", 0);
        }

        public /* bridge */ /* synthetic */ Object invoke(Object obj) {
            invoke((XC_MethodHook.MethodHookParam) obj);
            return Unit.INSTANCE;
        }

        public final void invoke(XC_MethodHook.MethodHookParam methodHookParam) {
            Intrinsics.checkNotNullParameter(methodHookParam, "p0");
            ((DeletedMessagesHooks) this.receiver).onManualDelete(methodHookParam);
        }
    }

    static final Unit setup$lambda$1(XC_MethodHook.MethodHookParam methodHookParam) {
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        if (DeletedMessagesStore.INSTANCE.isDeleted((MessageObject) HookUtilsKt.self(methodHookParam))) {
            methodHookParam.setResult(true);
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void onStorageDelete(XC_MethodHook.MethodHookParam param) {
        if (ReSettings.INSTANCE.getSaveDeleted() && ((Number) HookUtilsKt.arg(param, 3)).intValue() == 0) {
            long jLongValue = ((Number) HookUtilsKt.arg(param, 0)).longValue();
            ArrayList arrayList = (ArrayList) HookUtilsKt.arg(param, 1);
            if (arrayList == null) {
                return;
            }
            ArrayList arrayList2 = new ArrayList();
            ArrayList arrayList3 = new ArrayList();
            Iterator it = arrayList.iterator();
            Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
            while (it.hasNext()) {
                Object next = it.next();
                Intrinsics.checkNotNullExpressionValue(next, "next(...)");
                int iIntValue = ((Number) next).intValue();
                ((iIntValue <= 0 || DeletionBypass.INSTANCE.consumeStorage(jLongValue, iIntValue)) ? arrayList2 : arrayList3).add(Integer.valueOf(iIntValue));
            }
            if (arrayList3.isEmpty()) {
                return;
            }
            MessagesStorage messagesStorage = (MessagesStorage) HookUtilsKt.self(param);
            int account = TelegramStorageKt.getAccount((BaseController) messagesStorage);
            for (Map.Entry<Long, List<Integer>> entry : TelegramStorage.INSTANCE.existingMessages(messagesStorage, jLongValue, arrayList3).entrySet()) {
                long jLongValue2 = entry.getKey().longValue();
                List<Integer> value = entry.getValue();
                DeletedMessagesStore.INSTANCE.add(jLongValue2, value);
                ChatUtils.INSTANCE.refreshMessages(account, jLongValue2, value);
            }
            if (arrayList2.isEmpty()) {
                param.setResult((Object) null);
            } else {
                param.args[1] = arrayList2;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void onChatDelete(XC_MethodHook.MethodHookParam param) {
        ArrayList arrayList;
        if (ReSettings.INSTANCE.getSaveDeleted()) {
            final ChatActivity chatActivity = (ChatActivity) HookUtilsKt.self(param);
            if (chatActivity.getChatMode() == 0 && (arrayList = (ArrayList) HookUtilsKt.arg(param, 0)) != null) {
                long dialogId = chatActivity.getDialogId();
                ArrayList arrayList2 = arrayList;
                ArrayList arrayList3 = new ArrayList();
                for (Object obj : arrayList2) {
                    int iIntValue = ((Number) obj).intValue();
                    if (iIntValue <= 0 || DeletionBypass.INSTANCE.consumeUi(dialogId, iIntValue)) {
                        arrayList3.add(obj);
                    }
                }
                ArrayList arrayList4 = arrayList3;
                param.args[0] = arrayList4;
                if (arrayList4.size() != arrayList.size()) {
                    final HashSet hashSet = CollectionsKt.toHashSet(arrayList2);
                    hashSet.removeAll(CollectionsKt.toSet(arrayList4));
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: ni.shikatu.re_extera.hooks.antirecall.DeletedMessagesHooks.2
                        @Override // java.lang.Runnable
                        public final void run() {
                            DeletedMessagesHooks.onChatDelete$lambda$5(DeletedMessagesHooks.this, chatActivity, hashSet);
                        }
                    });
                }
            }
        }
    }

    static final void onChatDelete$lambda$5(DeletedMessagesHooks deletedMessagesHooks, ChatActivity chatActivity, HashSet hashSet) {
        deletedMessagesHooks.clearDeletedFlag(chatActivity, hashSet);
    }

    private final void clearDeletedFlag(ChatActivity chat, Set<Integer> ids) {
        RecyclerListView chatListView;
        Iterator it = chat.messages.iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        boolean z = false;
        while (it.hasNext()) {
            MessageObject messageObject = (MessageObject) it.next();
            if (messageObject.deleted && ids.contains(Integer.valueOf(messageObject.getId()))) {
                messageObject.deleted = false;
                z = true;
            }
        }
        if (!z || (chatListView = chat.getChatListView()) == null) {
            return;
        }
        chatListView.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void onManualDelete(XC_MethodHook.MethodHookParam param) {
        ArrayList arrayList;
        HashSet hashSetEmptySet;
        if (((Number) HookUtilsKt.arg(param, 6)).intValue() == 0 && (arrayList = (ArrayList) HookUtilsKt.arg(param, 0)) != null) {
            long jLongValue = ((Number) HookUtilsKt.arg(param, 3)).longValue();
            boolean z = param.args[2] != null;
            int account = TelegramStorageKt.getAccount((BaseController) HookUtilsKt.self(param));
            if (z) {
                hashSetEmptySet = SetsKt.emptySet();
            } else {
                HashSet hashSet = new HashSet();
                for (Object obj : arrayList) {
                    if (DeletedMessagesStore.INSTANCE.isDeleted(jLongValue, ((Number) obj).intValue())) {
                        hashSet.add(obj);
                    }
                }
                hashSetEmptySet = hashSet;
            }
            ArrayList arrayList2 = new ArrayList();
            for (Object obj2 : arrayList) {
                if (!hashSetEmptySet.contains(Integer.valueOf(((Number) obj2).intValue()))) {
                    arrayList2.add(obj2);
                }
            }
            ArrayList arrayList3 = arrayList2;
            Set set = hashSetEmptySet;
            MessageActions.INSTANCE.purge(account, jLongValue, set);
            if (ReSettings.INSTANCE.getSaveDeleted() && !ReSettings.INSTANCE.getSaveManuallyDeleted()) {
                DeletionBypass.INSTANCE.allow(jLongValue, arrayList3);
            }
            if (arrayList3.isEmpty()) {
                param.setResult((Object) null);
            } else {
                if (set.isEmpty()) {
                    return;
                }
                param.args[0] = new ArrayList(arrayList3);
            }
        }
    }
}
