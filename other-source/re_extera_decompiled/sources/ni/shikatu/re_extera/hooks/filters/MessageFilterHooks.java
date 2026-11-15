package ni.shikatu.re_extera.hooks.filters;

import de.robv.android.xposed.XC_MethodHook;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import ni.shikatu.extensions.FieldRef;
import ni.shikatu.extensions.HookUtilsKt;
import ni.shikatu.re_extera.data.RegexFiltersStore;
import ni.shikatu.re_extera.data.ShadowbanStore;
import ni.shikatu.re_extera.hooks.HookerClass;
import ni.shikatu.re_extera.localization.Strings;
import ni.shikatu.re_extera.settings.ReSettings;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.Cells.DialogCell;
import org.telegram.ui.ChatActivity;

/* JADX INFO: compiled from: MessageFilterHooks.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u0007\u001a\u00020\bH\u0014J\b\u0010\t\u001a\u00020\nH\u0002J\u0018\u0010\u000b\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\nH\u0002J.\u0010\u000e\u001a\u0012\u0012\u0004\u0012\u00020\u00060\u000fj\b\u0012\u0004\u0012\u00020\u0006`\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u0013\u001a\u00020\nH\u0002R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0014"}, d2 = {"Lni/shikatu/re_extera/hooks/filters/MessageFilterHooks;", "Lni/shikatu/re_extera/hooks/HookerClass;", "<init>", "()V", "dialogCellMessage", "Lni/shikatu/extensions/FieldRef;", "Lorg/telegram/messenger/MessageObject;", "setup", "", "isActive", "", "isHidden", "message", "inGroup", "filtered", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "messages", "", "isGroup", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class MessageFilterHooks extends HookerClass {
    private final FieldRef<MessageObject> dialogCellMessage = new FieldRef<>(DialogCell.class, "message");

    @Override // ni.shikatu.re_extera.hooks.HookerClass
    protected void setup() {
        hookBefore(ChatActivity.class, "processNewMessages", new Class[]{ArrayList.class, Boolean.TYPE}, new Function1() { // from class: ni.shikatu.re_extera.hooks.filters.MessageFilterHooks.0
            public final Object invoke(Object obj) {
                return MessageFilterHooks.setup$lambda$0(MessageFilterHooks.this, (XC_MethodHook.MethodHookParam) obj);
            }
        });
        hookBefore(ChatActivity.class, "didReceivedNotification", new Class[]{Integer.TYPE, Integer.TYPE, Object[].class}, new Function1() { // from class: ni.shikatu.re_extera.hooks.filters.MessageFilterHooks.1
            public final Object invoke(Object obj) {
                return MessageFilterHooks.setup$lambda$1(MessageFilterHooks.this, (XC_MethodHook.MethodHookParam) obj);
            }
        });
        hookBefore(DialogCell.class, "buildLayout", new Class[0], new Function1() { // from class: ni.shikatu.re_extera.hooks.filters.MessageFilterHooks.2
            public final Object invoke(Object obj) {
                return MessageFilterHooks.setup$lambda$2(MessageFilterHooks.this, (XC_MethodHook.MethodHookParam) obj);
            }
        });
    }

    static final Unit setup$lambda$0(MessageFilterHooks messageFilterHooks, XC_MethodHook.MethodHookParam methodHookParam) {
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        ArrayList arrayList = (ArrayList) HookUtilsKt.arg(methodHookParam, 0);
        ArrayList arrayList2 = arrayList;
        if (arrayList2 == null || arrayList2.isEmpty() || !messageFilterHooks.isActive()) {
            return Unit.INSTANCE;
        }
        methodHookParam.args[0] = messageFilterHooks.filtered(arrayList, ((ChatActivity) HookUtilsKt.self(methodHookParam)).getDialogId() < 0);
        return Unit.INSTANCE;
    }

    static final Unit setup$lambda$1(MessageFilterHooks messageFilterHooks, XC_MethodHook.MethodHookParam methodHookParam) {
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        if (((Number) HookUtilsKt.arg(methodHookParam, 0)).intValue() != NotificationCenter.messagesDidLoad || !messageFilterHooks.isActive()) {
            return Unit.INSTANCE;
        }
        Object[] objArr = (Object[]) HookUtilsKt.arg(methodHookParam, 2);
        if (objArr != null && objArr.length >= 3) {
            Object obj = objArr[2];
            ArrayList arrayList = obj instanceof ArrayList ? (ArrayList) obj : null;
            if (arrayList == null) {
                return Unit.INSTANCE;
            }
            Object obj2 = objArr[0];
            Long l = obj2 instanceof Long ? (Long) obj2 : null;
            if (l == null) {
                return Unit.INSTANCE;
            }
            long jLongValue = l.longValue();
            int size = arrayList.size();
            ArrayList<MessageObject> arrayListFiltered = messageFilterHooks.filtered(arrayList, jLongValue < 0);
            if (arrayListFiltered.size() == size) {
                return Unit.INSTANCE;
            }
            arrayList.clear();
            arrayList.addAll(arrayListFiltered);
            objArr[1] = Integer.valueOf(arrayListFiltered.size());
            return Unit.INSTANCE;
        }
        return Unit.INSTANCE;
    }

    static final Unit setup$lambda$2(MessageFilterHooks messageFilterHooks, XC_MethodHook.MethodHookParam methodHookParam) {
        MessageObject messageObject;
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        if (messageFilterHooks.isActive() && (messageObject = messageFilterHooks.dialogCellMessage.get(methodHookParam.thisObject)) != null) {
            if (!messageObject.isOut()) {
                if (messageFilterHooks.isHidden(messageObject, messageObject.getDialogId() < 0)) {
                    String filtered_message = Strings.INSTANCE.getFILTERED_MESSAGE();
                    messageObject.messageText = filtered_message;
                    messageObject.messageTextShort = null;
                    if (messageObject.caption != null) {
                        messageObject.caption = filtered_message;
                    }
                    return Unit.INSTANCE;
                }
            }
            return Unit.INSTANCE;
        }
        return Unit.INSTANCE;
    }

    private final boolean isActive() {
        if (ShadowbanStore.INSTANCE.isEmpty()) {
            return ReSettings.INSTANCE.getFiltersEnabled() && !RegexFiltersStore.INSTANCE.all().isEmpty();
        }
        return true;
    }

    private final boolean isHidden(MessageObject message, boolean inGroup) {
        if (inGroup && ShadowbanStore.INSTANCE.hidesInGroups(message.getFromChatId())) {
            return true;
        }
        return RegexFiltersStore.INSTANCE.isFiltered(message);
    }

    private final ArrayList<MessageObject> filtered(List<? extends MessageObject> messages, boolean isGroup) {
        HashSet hashSet = new HashSet();
        for (MessageObject messageObject : messages) {
            if (!messageObject.isOut() && messageObject.getGroupId() != 0 && isHidden(messageObject, isGroup)) {
                hashSet.add(Long.valueOf(messageObject.getGroupId()));
            }
        }
        ArrayList arrayList = new ArrayList(messages.size());
        for (Object obj : messages) {
            MessageObject messageObject2 = (MessageObject) obj;
            if (messageObject2.isOut() || (!isHidden(messageObject2, isGroup) && (messageObject2.getGroupId() == 0 || !hashSet.contains(Long.valueOf(messageObject2.getGroupId()))))) {
                arrayList.add(obj);
            }
        }
        return arrayList;
    }
}
