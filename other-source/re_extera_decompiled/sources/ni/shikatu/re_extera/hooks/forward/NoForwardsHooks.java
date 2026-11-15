package ni.shikatu.re_extera.hooks.forward;

import android.util.SparseArray;
import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Unit;
import kotlin.collections.ArrayDeque;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.ArrayIteratorKt;
import kotlin.jvm.internal.Intrinsics;
import ni.shikatu.extensions.FieldRef;
import ni.shikatu.extensions.HookUtilsKt;
import ni.shikatu.re_extera.ReLog;
import ni.shikatu.re_extera.hooks.HookerClass;
import ni.shikatu.re_extera.settings.ReSettings;
import ni.shikatu.re_extera.utils.ForwardRestrictions;
import ni.shikatu.re_extera.utils.GifReuploader;
import ni.shikatu.re_extera.utils.MessageForwarder;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessageSuggestionParams;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.ChatActivity;

/* JADX INFO: compiled from: NoForwardsHooks.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u0012\u001a\u00020\u0013H\u0014J1\u0010\u0014\u001a\u00020\u00132\u0006\u0010\u0015\u001a\u00020\u00162\u001a\u0010\u0017\u001a\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030\u00180\n\"\u0006\u0012\u0002\b\u00030\u0018H\u0002¢\u0006\u0002\u0010\u0019J\u0016\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00060\u000f2\u0006\u0010\u001b\u001a\u00020\u001cH\u0002R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R \u0010\t\u001a\u0014\u0012\u0010\u0012\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030\u000b0\n0\u0005X\u0082\u0004¢\u0006\u0002\n\u0000Rb\u0010\f\u001aV\u0012$\u0012\"\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u000f \u0011*\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u000f\u0018\u00010\u000e0\u000e \u0011**\u0012$\u0012\"\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u000f \u0011*\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u000f\u0018\u00010\u000e0\u000e\u0018\u00010\r0\rX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001d"}, d2 = {"Lni/shikatu/re_extera/hooks/forward/NoForwardsHooks;", "Lni/shikatu/re_extera/hooks/HookerClass;", "<init>", "()V", "selectedObject", "Lni/shikatu/extensions/FieldRef;", "Lorg/telegram/messenger/MessageObject;", "selectedObjectGroup", "Lorg/telegram/messenger/MessageObject$GroupedMessages;", "selectedMessagesIds", "", "Landroid/util/SparseArray;", "hiddenStack", "Ljava/lang/ThreadLocal;", "Lkotlin/collections/ArrayDeque;", "", "Lorg/telegram/tgnet/TLRPC$Message;", "kotlin.jvm.PlatformType", "setup", "", "showForwardActionsDuring", "name", "", "params", "Ljava/lang/Class;", "(Ljava/lang/String;[Ljava/lang/Class;)V", "involvedMessages", "param", "Lde/robv/android/xposed/XC_MethodHook$MethodHookParam;", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class NoForwardsHooks extends HookerClass {
    private final FieldRef<MessageObject> selectedObject = new FieldRef<>(ChatActivity.class, "selectedObject");
    private final FieldRef<MessageObject.GroupedMessages> selectedObjectGroup = new FieldRef<>(ChatActivity.class, "selectedObjectGroup");
    private final FieldRef<SparseArray<?>[]> selectedMessagesIds = new FieldRef<>(ChatActivity.class, "selectedMessagesIds");
    private final ThreadLocal<ArrayDeque<List<TLRPC.Message>>> hiddenStack = ThreadLocal.withInitial(new Supplier() { // from class: ni.shikatu.re_extera.hooks.forward.NoForwardsHooks.0
        @Override // java.util.function.Supplier
        public final Object get() {
            return NoForwardsHooks.hiddenStack$lambda$0();
        }
    });

    static final ArrayDeque hiddenStack$lambda$0() {
        return new ArrayDeque();
    }

    @Override // ni.shikatu.re_extera.hooks.HookerClass
    protected void setup() {
        hookBefore(MessagesController.class, "isChatNoForwards", new Class[]{TLRPC.Chat.class}, new Function1() { // from class: ni.shikatu.re_extera.hooks.forward.NoForwardsHooks.1
            public final Object invoke(Object obj) {
                return NoForwardsHooks.setup$lambda$1((XC_MethodHook.MethodHookParam) obj);
            }
        });
        hookBefore(MessagesController.class, "isUserNoForwards", new Class[]{TLRPC.UserFull.class}, new Function1() { // from class: ni.shikatu.re_extera.hooks.forward.NoForwardsHooks.2
            public final Object invoke(Object obj) {
                return NoForwardsHooks.setup$lambda$2((XC_MethodHook.MethodHookParam) obj);
            }
        });
        hookBefore(MessageObject.class, "canForwardMessage", new Class[0], new Function1() { // from class: ni.shikatu.re_extera.hooks.forward.NoForwardsHooks.3
            public final Object invoke(Object obj) {
                return NoForwardsHooks.setup$lambda$3((XC_MethodHook.MethodHookParam) obj);
            }
        });
        hookBefore(ChatActivity.class, "hasSelectedNoforwardsMessage", new Class[0], new Function1() { // from class: ni.shikatu.re_extera.hooks.forward.NoForwardsHooks.4
            public final Object invoke(Object obj) {
                return NoForwardsHooks.setup$lambda$4((XC_MethodHook.MethodHookParam) obj);
            }
        });
        hookBefore(ChatActivity.class, "isPeerNoForwards", new Class[0], new Function1() { // from class: ni.shikatu.re_extera.hooks.forward.NoForwardsHooks.5
            public final Object invoke(Object obj) {
                return NoForwardsHooks.setup$lambda$5((XC_MethodHook.MethodHookParam) obj);
            }
        });
        showForwardActionsDuring("createMenu", View.class, Boolean.TYPE, Boolean.TYPE, Float.TYPE, Float.TYPE, Boolean.TYPE, Boolean.TYPE, Boolean.TYPE);
        showForwardActionsDuring("fillMessageMenu", MessageObject.class, ArrayList.class, ArrayList.class, ArrayList.class);
        showForwardActionsDuring("processSelectedOption", Integer.TYPE);
        showForwardActionsDuring("addToSelectedMessages", MessageObject.class, Boolean.TYPE, Boolean.TYPE);
        showForwardActionsDuring("openForward", Boolean.TYPE);
        hookBefore(SendMessagesHelper.class, "sendMessage", new Class[]{ArrayList.class, Long.TYPE, Boolean.TYPE, Boolean.TYPE, Boolean.TYPE, Integer.TYPE, Integer.TYPE, MessageObject.class, Integer.TYPE, Long.TYPE, Long.TYPE, MessageSuggestionParams.class}, new Function1() { // from class: ni.shikatu.re_extera.hooks.forward.NoForwardsHooks.6
            public final Object invoke(Object obj) {
                return NoForwardsHooks.setup$lambda$6((XC_MethodHook.MethodHookParam) obj);
            }
        });
        hookBefore(MessagesController.class, "saveGif", new Class[]{Object.class, TLRPC.Document.class}, new Function1() { // from class: ni.shikatu.re_extera.hooks.forward.NoForwardsHooks.7
            public final Object invoke(Object obj) {
                return NoForwardsHooks.setup$lambda$7((XC_MethodHook.MethodHookParam) obj);
            }
        });
    }

    static final Unit setup$lambda$1(XC_MethodHook.MethodHookParam methodHookParam) {
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        if (ReSettings.INSTANCE.getNoForwards()) {
            methodHookParam.setResult(false);
        }
        return Unit.INSTANCE;
    }

    static final Unit setup$lambda$2(XC_MethodHook.MethodHookParam methodHookParam) {
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        if (ReSettings.INSTANCE.getNoForwards()) {
            methodHookParam.setResult(false);
        }
        return Unit.INSTANCE;
    }

    static final Unit setup$lambda$3(XC_MethodHook.MethodHookParam methodHookParam) {
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        if (ReSettings.INSTANCE.getNoForwards()) {
            methodHookParam.setResult(true);
        }
        return Unit.INSTANCE;
    }

    static final Unit setup$lambda$4(XC_MethodHook.MethodHookParam methodHookParam) {
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        if (ReSettings.INSTANCE.getNoForwards()) {
            methodHookParam.setResult(false);
        }
        return Unit.INSTANCE;
    }

    static final Unit setup$lambda$5(XC_MethodHook.MethodHookParam methodHookParam) {
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        if (ReSettings.INSTANCE.getNoForwards()) {
            methodHookParam.setResult(false);
        }
        return Unit.INSTANCE;
    }

    static final Unit setup$lambda$6(XC_MethodHook.MethodHookParam methodHookParam) {
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        ArrayList arrayList = (ArrayList) HookUtilsKt.arg(methodHookParam, 0);
        ArrayList arrayList2 = arrayList;
        if (arrayList2 == null || arrayList2.isEmpty()) {
            return Unit.INSTANCE;
        }
        ForwardRestrictions forwardRestrictions = ForwardRestrictions.INSTANCE;
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        for (Object obj : arrayList) {
            if (forwardRestrictions.isRestricted((MessageObject) obj)) {
                arrayList3.add(obj);
            } else {
                arrayList4.add(obj);
            }
        }
        Pair pair = new Pair(arrayList3, arrayList4);
        List<? extends MessageObject> list = (List) pair.component1();
        List list2 = (List) pair.component2();
        if (list.isEmpty()) {
            return Unit.INSTANCE;
        }
        MessageForwarder.INSTANCE.sendCopies(((MessageObject) CollectionsKt.first(list)).currentAccount, list, ((Number) HookUtilsKt.arg(methodHookParam, 1)).longValue(), ((Boolean) HookUtilsKt.arg(methodHookParam, 4)).booleanValue(), ((Number) HookUtilsKt.arg(methodHookParam, 5)).intValue(), (MessageObject) HookUtilsKt.arg(methodHookParam, 7));
        if (list2.isEmpty()) {
            methodHookParam.setResult(0);
        } else {
            methodHookParam.args[0] = new ArrayList(list2);
        }
        return Unit.INSTANCE;
    }

    static final Unit setup$lambda$7(XC_MethodHook.MethodHookParam methodHookParam) {
        TLRPC.Document document;
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        Object objArg = HookUtilsKt.arg(methodHookParam, 0);
        MessageObject messageObject = objArg instanceof MessageObject ? (MessageObject) objArg : null;
        if (messageObject != null && (document = (TLRPC.Document) HookUtilsKt.arg(methodHookParam, 1)) != null && GifReuploader.INSTANCE.needsReupload(messageObject, document)) {
            methodHookParam.setResult((Object) null);
            GifReuploader.INSTANCE.reupload(messageObject, document);
            return Unit.INSTANCE;
        }
        return Unit.INSTANCE;
    }

    private final void showForwardActionsDuring(final String name, Class<?>... params) {
        Method method = method(ChatActivity.class, name, (Class[]) Arrays.copyOf(params, params.length));
        if (method == null) {
            return;
        }
        register(method, new XC_MethodHook() { // from class: ni.shikatu.re_extera.hooks.forward.NoForwardsHooks.showForwardActionsDuring.1
            public void beforeHookedMethod(XC_MethodHook.MethodHookParam param) {
                List<TLRPC.Message> listEmptyList;
                Intrinsics.checkNotNullParameter(param, "param");
                try {
                    listEmptyList = ReSettings.INSTANCE.getNoForwards() ? ForwardRestrictions.INSTANCE.hideNoForwards(NoForwardsHooks.this.involvedMessages(param)) : CollectionsKt.emptyList();
                } catch (Throwable th) {
                    ReLog.INSTANCE.e("Failed to hide noforwards in " + name, th);
                    listEmptyList = CollectionsKt.emptyList();
                }
                Object obj = NoForwardsHooks.this.hiddenStack.get();
                Intrinsics.checkNotNull(obj);
                ((ArrayDeque) obj).addLast(listEmptyList);
            }

            public void afterHookedMethod(XC_MethodHook.MethodHookParam param) {
                Intrinsics.checkNotNullParameter(param, "param");
                Object obj = NoForwardsHooks.this.hiddenStack.get();
                Intrinsics.checkNotNull(obj);
                List<? extends TLRPC.Message> list = (List) ((ArrayDeque) obj).removeLastOrNull();
                if (list == null || list.isEmpty()) {
                    return;
                }
                ForwardRestrictions.INSTANCE.restoreNoForwards(list);
            }
        });
        try {
            HookUtilsKt.deoptimize(method);
        } catch (Throwable th) {
            ReLog.INSTANCE.e("Failed to deoptimize ChatActivity." + name, th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final List<MessageObject> involvedMessages(XC_MethodHook.MethodHookParam param) {
        ArrayList arrayList;
        ArrayList arrayList2;
        Object obj = param.thisObject;
        ArrayList arrayList3 = new ArrayList();
        Iterator it = ArrayIteratorKt.iterator(param.args);
        while (it.hasNext()) {
            Object next = it.next();
            if (next instanceof MessageObject) {
                arrayList3.add(next);
            } else if (next instanceof ChatMessageCell) {
                ChatMessageCell chatMessageCell = (ChatMessageCell) next;
                MessageObject messageObject = chatMessageCell.getMessageObject();
                if (messageObject != null) {
                    arrayList3.add(messageObject);
                }
                MessageObject.GroupedMessages currentMessagesGroup = chatMessageCell.getCurrentMessagesGroup();
                if (currentMessagesGroup != null && (arrayList2 = currentMessagesGroup.messages) != null) {
                    CollectionsKt.addAll(arrayList3, arrayList2);
                }
            }
        }
        MessageObject messageObject2 = this.selectedObject.get(obj);
        if (messageObject2 != null) {
            arrayList3.add(messageObject2);
        }
        MessageObject.GroupedMessages groupedMessages = this.selectedObjectGroup.get(obj);
        if (groupedMessages != null && (arrayList = groupedMessages.messages) != null) {
            CollectionsKt.addAll(arrayList3, arrayList);
        }
        SparseArray<?>[] sparseArrayArr = this.selectedMessagesIds.get(obj);
        if (sparseArrayArr != null) {
            for (SparseArray<?> sparseArray : sparseArrayArr) {
                int size = sparseArray.size();
                for (int i = 0; i < size; i++) {
                    Object objValueAt = sparseArray.valueAt(i);
                    MessageObject messageObject3 = objValueAt instanceof MessageObject ? (MessageObject) objValueAt : null;
                    if (messageObject3 != null) {
                        arrayList3.add(messageObject3);
                    }
                }
            }
        }
        return arrayList3;
    }
}
