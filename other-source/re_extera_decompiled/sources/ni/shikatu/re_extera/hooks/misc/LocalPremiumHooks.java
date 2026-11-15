package ni.shikatu.re_extera.hooks.misc;

import de.robv.android.xposed.XC_MethodHook;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import ni.shikatu.re_extera.hooks.HookerClass;
import ni.shikatu.re_extera.settings.ReSettings;
import org.telegram.messenger.UserConfig;

/* JADX INFO: compiled from: MiscHooks.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u0004\u001a\u00020\u0005H\u0014¨\u0006\u0006"}, d2 = {"Lni/shikatu/re_extera/hooks/misc/LocalPremiumHooks;", "Lni/shikatu/re_extera/hooks/HookerClass;", "<init>", "()V", "setup", "", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class LocalPremiumHooks extends HookerClass {
    @Override // ni.shikatu.re_extera.hooks.HookerClass
    protected void setup() {
        if (ReSettings.INSTANCE.getLocalPremium() && UserConfig.getInstance(UserConfig.selectedAccount).isPremium()) {
            ReSettings.INSTANCE.setLocalPremium(false);
        }
        hookBefore(UserConfig.class, "isPremium", new Class[0], new Function1() { // from class: ni.shikatu.re_extera.hooks.misc.LocalPremiumHooks.0
            public final Object invoke(Object obj) {
                return LocalPremiumHooks.setup$lambda$0((XC_MethodHook.MethodHookParam) obj);
            }
        });
    }

    static final Unit setup$lambda$0(XC_MethodHook.MethodHookParam methodHookParam) {
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        if (ReSettings.INSTANCE.getLocalPremium()) {
            methodHookParam.setResult(true);
        }
        return Unit.INSTANCE;
    }
}
