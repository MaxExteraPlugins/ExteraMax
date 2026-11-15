package ni.shikatu.re_extera.hooks.onetime;

import de.robv.android.xposed.XC_MethodHook;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import ni.shikatu.extensions.FieldRef;
import ni.shikatu.extensions.HookUtilsKt;
import ni.shikatu.re_extera.hooks.HookerClass;
import ni.shikatu.re_extera.settings.ReSettings;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.SecretVoicePlayer;

/* JADX INFO: compiled from: OneTimeMessagesHooks.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\b\u001a\u00020\tH\u0014R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\n"}, d2 = {"Lni/shikatu/re_extera/hooks/onetime/OneTimeMessagesHooks;", "Lni/shikatu/re_extera/hooks/HookerClass;", "<init>", "()V", "closeAction", "Lni/shikatu/extensions/FieldRef;", "Ljava/lang/Runnable;", "noop", "setup", "", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class OneTimeMessagesHooks extends HookerClass {
    private final FieldRef<Runnable> closeAction = new FieldRef<>(SecretVoicePlayer.class, "closeAction");
    private final Runnable noop = new Runnable() { // from class: ni.shikatu.re_extera.hooks.onetime.OneTimeMessagesHooks.4
        @Override // java.lang.Runnable
        public final void run() {
            OneTimeMessagesHooks.noop$lambda$0();
        }
    };

    static final void noop$lambda$0() {
    }

    @Override // ni.shikatu.re_extera.hooks.HookerClass
    protected void setup() {
        hookBefore(MessagesController.class, "checkDeletingTask", new Class[]{Boolean.TYPE}, new Function1() { // from class: ni.shikatu.re_extera.hooks.onetime.OneTimeMessagesHooks.0
            public final Object invoke(Object obj) {
                return OneTimeMessagesHooks.setup$lambda$1((XC_MethodHook.MethodHookParam) obj);
            }
        });
        hookBefore(SecretVoicePlayer.class, "dismiss", new Class[0], new Function1() { // from class: ni.shikatu.re_extera.hooks.onetime.OneTimeMessagesHooks.1
            public final Object invoke(Object obj) {
                return OneTimeMessagesHooks.setup$lambda$2(OneTimeMessagesHooks.this, (XC_MethodHook.MethodHookParam) obj);
            }
        });
        hookBefore(ChatActivity.class, "sendSecretMediaDelete", new Class[]{MessageObject.class}, new Function1() { // from class: ni.shikatu.re_extera.hooks.onetime.OneTimeMessagesHooks.2
            public final Object invoke(Object obj) {
                return OneTimeMessagesHooks.setup$lambda$3(OneTimeMessagesHooks.this, (XC_MethodHook.MethodHookParam) obj);
            }
        });
        hookBefore(ChatActivity.class, "sendSecretMessageRead", new Class[]{MessageObject.class, Boolean.TYPE}, new Function1() { // from class: ni.shikatu.re_extera.hooks.onetime.OneTimeMessagesHooks.3
            public final Object invoke(Object obj) {
                return OneTimeMessagesHooks.setup$lambda$4(OneTimeMessagesHooks.this, (XC_MethodHook.MethodHookParam) obj);
            }
        });
    }

    static final Unit setup$lambda$1(XC_MethodHook.MethodHookParam methodHookParam) {
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        if (ReSettings.INSTANCE.getSaveOneTime()) {
            methodHookParam.setResult(false);
        }
        return Unit.INSTANCE;
    }

    static final Unit setup$lambda$2(OneTimeMessagesHooks oneTimeMessagesHooks, XC_MethodHook.MethodHookParam methodHookParam) throws IllegalAccessException {
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        if (ReSettings.INSTANCE.getSaveOneTime()) {
            oneTimeMessagesHooks.closeAction.set(methodHookParam.thisObject, null);
        }
        return Unit.INSTANCE;
    }

    static final Unit setup$lambda$3(OneTimeMessagesHooks oneTimeMessagesHooks, XC_MethodHook.MethodHookParam methodHookParam) {
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        if (!ReSettings.INSTANCE.getSaveOneTime()) {
            return Unit.INSTANCE;
        }
        MessageObject messageObject = (MessageObject) HookUtilsKt.arg(methodHookParam, 0);
        if (messageObject != null) {
            messageObject.forceExpired = false;
        }
        methodHookParam.setResult(oneTimeMessagesHooks.noop);
        return Unit.INSTANCE;
    }

    static final Unit setup$lambda$4(OneTimeMessagesHooks oneTimeMessagesHooks, XC_MethodHook.MethodHookParam methodHookParam) {
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        if (ReSettings.INSTANCE.getSaveOneTime()) {
            methodHookParam.setResult(oneTimeMessagesHooks.noop);
        }
        return Unit.INSTANCE;
    }
}
