package ni.shikatu.re_extera.hooks.antirecall;

import de.robv.android.xposed.XC_MethodHook;
import java.util.ArrayList;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import ni.shikatu.extensions.HookUtilsKt;
import ni.shikatu.re_extera.data.EditHistoryStore;
import ni.shikatu.re_extera.hooks.HookerClass;
import ni.shikatu.re_extera.settings.ReSettings;
import ni.shikatu.re_extera.utils.TelegramStorage;
import ni.shikatu.re_extera.utils.TelegramStorageKt;
import org.telegram.messenger.BaseController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.tgnet.InputSerializedData;
import org.telegram.tgnet.OutputSerializedData;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_update;

/* JADX INFO: compiled from: EditHistoryHooks.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u0004\u001a\u00020\u0005H\u0014J\u0018\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0002J\u0012\u0010\u000b\u001a\u0004\u0018\u00010\n2\u0006\u0010\f\u001a\u00020\nH\u0002¨\u0006\r"}, d2 = {"Lni/shikatu/re_extera/hooks/antirecall/EditHistoryHooks;", "Lni/shikatu/re_extera/hooks/HookerClass;", "<init>", "()V", "setup", "", "capture", "account", "", "edited", "Lorg/telegram/tgnet/TLRPC$Message;", "copyOf", "message", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class EditHistoryHooks extends HookerClass {
    @Override // ni.shikatu.re_extera.hooks.HookerClass
    protected void setup() {
        hookBefore(MessagesController.class, "processUpdateArray", new Class[]{ArrayList.class, ArrayList.class, ArrayList.class, Boolean.TYPE, Integer.TYPE}, new Function1() { // from class: ni.shikatu.re_extera.hooks.antirecall.EditHistoryHooks.1
            public final Object invoke(Object obj) {
                return EditHistoryHooks.setup$lambda$0(EditHistoryHooks.this, (XC_MethodHook.MethodHookParam) obj);
            }
        });
    }

    static final Unit setup$lambda$0(EditHistoryHooks editHistoryHooks, XC_MethodHook.MethodHookParam methodHookParam) {
        ArrayList arrayList;
        TLRPC.Message message;
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        if (ReSettings.INSTANCE.getSaveEdits() && (arrayList = (ArrayList) HookUtilsKt.arg(methodHookParam, 0)) != null) {
            int account = TelegramStorageKt.getAccount((BaseController) HookUtilsKt.self(methodHookParam));
            Iterator it = arrayList.iterator();
            Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
            while (it.hasNext()) {
                Object next = it.next();
                Intrinsics.checkNotNullExpressionValue(next, "next(...)");
                TL_update.TL_updateEditMessage tL_updateEditMessage = (TLRPC.Update) next;
                if (tL_updateEditMessage instanceof TL_update.TL_updateEditMessage) {
                    message = tL_updateEditMessage.message;
                } else {
                    message = tL_updateEditMessage instanceof TL_update.TL_updateEditChannelMessage ? ((TL_update.TL_updateEditChannelMessage) tL_updateEditMessage).message : null;
                }
                if (message != null && !message.out) {
                    editHistoryHooks.capture(account, message);
                }
            }
            return Unit.INSTANCE;
        }
        return Unit.INSTANCE;
    }

    private final void capture(int account, TLRPC.Message edited) {
        final TLRPC.Message messageCopyOf = copyOf(edited);
        if (messageCopyOf == null) {
            return;
        }
        final long dialogId = MessageObject.getDialogId(messageCopyOf);
        final MessagesStorage messagesStorage = MessagesStorage.getInstance(account);
        messagesStorage.getStorageQueue().postRunnable(new Runnable() { // from class: ni.shikatu.re_extera.hooks.antirecall.EditHistoryHooks.0
            @Override // java.lang.Runnable
            public final void run() {
                EditHistoryHooks.capture$lambda$1(messagesStorage, dialogId, messageCopyOf);
            }
        });
    }

    static final void capture$lambda$1(MessagesStorage messagesStorage, long j, TLRPC.Message message) {
        TelegramStorage telegramStorage = TelegramStorage.INSTANCE;
        Intrinsics.checkNotNull(messagesStorage);
        TLRPC.Message messageLoadMessage = telegramStorage.loadMessage(messagesStorage, j, message.id);
        if (messageLoadMessage == null || messageLoadMessage.out) {
            return;
        }
        String str = messageLoadMessage.message;
        if (str == null) {
            str = "";
        }
        String str2 = message.message;
        boolean zAreEqual = Intrinsics.areEqual(str, str2 != null ? str2 : "");
        boolean z = (message.edit_date == 0 || message.edit_date == messageLoadMessage.edit_date) ? false : true;
        if (!zAreEqual || z) {
            EditHistoryStore.INSTANCE.record(j, message.id, messageLoadMessage, message);
        }
    }

    private final TLRPC.Message copyOf(TLRPC.Message message) {
        try {
            OutputSerializedData serializedData = new SerializedData(message.getObjectSize());
            message.serializeToStream(serializedData);
            InputSerializedData serializedData2 = new SerializedData(serializedData.toByteArray());
            serializedData.cleanup();
            TLRPC.Message messageTLdeserialize = TLRPC.Message.TLdeserialize(serializedData2, serializedData2.readInt32(false), false);
            serializedData2.cleanup();
            return messageTLdeserialize;
        } catch (Exception unused) {
            return null;
        }
    }
}
