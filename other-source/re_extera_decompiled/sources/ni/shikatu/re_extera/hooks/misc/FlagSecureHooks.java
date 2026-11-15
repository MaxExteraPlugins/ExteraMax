package ni.shikatu.re_extera.hooks.misc;

import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import de.robv.android.xposed.XC_MethodHook;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import ni.shikatu.extensions.HookUtilsKt;
import ni.shikatu.re_extera.hooks.HookerClass;
import ni.shikatu.re_extera.settings.ReSettings;
import org.telegram.messenger.FlagSecureReason;

/* JADX INFO: compiled from: MiscHooks.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u0004\u001a\u00020\u0005H\u0014¨\u0006\u0006"}, d2 = {"Lni/shikatu/re_extera/hooks/misc/FlagSecureHooks;", "Lni/shikatu/re_extera/hooks/HookerClass;", "<init>", "()V", "setup", "", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class FlagSecureHooks extends HookerClass {
    @Override // ni.shikatu.re_extera.hooks.HookerClass
    protected void setup() {
        Class<?> cls;
        try {
            cls = Class.forName("android.view.WindowManagerImpl");
        } catch (ClassNotFoundException unused) {
            cls = null;
        }
        if (cls != null) {
            hookBefore(cls, "addView", new Class[]{View.class, ViewGroup.LayoutParams.class}, new Function1() { // from class: ni.shikatu.re_extera.hooks.misc.FlagSecureHooks.0
                public final Object invoke(Object obj) {
                    return FlagSecureHooks.setup$lambda$0((XC_MethodHook.MethodHookParam) obj);
                }
            });
        }
        hookBefore(Window.class, "setFlags", new Class[]{Integer.TYPE, Integer.TYPE}, new Function1() { // from class: ni.shikatu.re_extera.hooks.misc.FlagSecureHooks.1
            public final Object invoke(Object obj) {
                return FlagSecureHooks.setup$lambda$1((XC_MethodHook.MethodHookParam) obj);
            }
        });
        hookBefore(FlagSecureReason.class, "attach", new Class[0], new Function1() { // from class: ni.shikatu.re_extera.hooks.misc.FlagSecureHooks.2
            public final Object invoke(Object obj) {
                return FlagSecureHooks.setup$lambda$2((XC_MethodHook.MethodHookParam) obj);
            }
        });
    }

    static final Unit setup$lambda$0(XC_MethodHook.MethodHookParam methodHookParam) {
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        if (!ReSettings.INSTANCE.getIgnoreFlagSecure()) {
            return Unit.INSTANCE;
        }
        Object obj = methodHookParam.args[1];
        WindowManager.LayoutParams layoutParams = obj instanceof WindowManager.LayoutParams ? (WindowManager.LayoutParams) obj : null;
        if (layoutParams == null) {
            return Unit.INSTANCE;
        }
        layoutParams.flags &= -8193;
        return Unit.INSTANCE;
    }

    static final Unit setup$lambda$1(XC_MethodHook.MethodHookParam methodHookParam) {
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        if (!ReSettings.INSTANCE.getIgnoreFlagSecure()) {
            return Unit.INSTANCE;
        }
        if ((((Number) HookUtilsKt.arg(methodHookParam, 1)).intValue() & 8192) != 0) {
            methodHookParam.args[0] = Integer.valueOf(((Number) HookUtilsKt.arg(methodHookParam, 0)).intValue() & (-8193));
        }
        return Unit.INSTANCE;
    }

    static final Unit setup$lambda$2(XC_MethodHook.MethodHookParam methodHookParam) {
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        if (ReSettings.INSTANCE.getIgnoreFlagSecure()) {
            methodHookParam.setResult((Object) null);
        }
        return Unit.INSTANCE;
    }
}
