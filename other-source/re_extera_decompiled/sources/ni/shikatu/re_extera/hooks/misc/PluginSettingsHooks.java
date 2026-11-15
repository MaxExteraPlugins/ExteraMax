package ni.shikatu.re_extera.hooks.misc;

import com.exteragram.messenger.plugins.Plugin;
import com.exteragram.messenger.plugins.PythonPluginsEngine;
import de.robv.android.xposed.XC_MethodHook;
import java.util.Set;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.SetsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import ni.shikatu.extensions.HookUtilsKt;
import ni.shikatu.re_extera.hooks.HookerClass;
import ni.shikatu.re_extera.ui.settings.RootSettingsFragment;
import org.telegram.ui.ActionBar.BaseFragment;

/* JADX INFO: compiled from: MiscHooks.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u0000 \u00062\u00020\u0001:\u0001\u0006B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u0004\u001a\u00020\u0005H\u0014¨\u0006\u0007"}, d2 = {"Lni/shikatu/re_extera/hooks/misc/PluginSettingsHooks;", "Lni/shikatu/re_extera/hooks/HookerClass;", "<init>", "()V", "setup", "", "Companion", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class PluginSettingsHooks extends HookerClass {
    private static final Companion Companion = new Companion(null);
    private static final Set<String> LOADER_IDS = SetsKt.setOf(new String[]{"re_extera_dex", "re_extera_dex_local_debug", "re_extera_dex_unstable"});

    @Override // ni.shikatu.re_extera.hooks.HookerClass
    protected void setup() {
        hookBefore(PythonPluginsEngine.class, "openPluginSettings", new Class[]{Plugin.class, BaseFragment.class}, new Function1() { // from class: ni.shikatu.re_extera.hooks.misc.PluginSettingsHooks.0
            public final Object invoke(Object obj) {
                return PluginSettingsHooks.setup$lambda$0((XC_MethodHook.MethodHookParam) obj);
            }
        });
    }

    static final Unit setup$lambda$0(XC_MethodHook.MethodHookParam methodHookParam) {
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        Plugin plugin = (Plugin) HookUtilsKt.arg(methodHookParam, 0);
        if (plugin != null && LOADER_IDS.contains(plugin.getId())) {
            BaseFragment baseFragment = (BaseFragment) HookUtilsKt.arg(methodHookParam, 1);
            if (baseFragment != null) {
                baseFragment.presentFragment(new RootSettingsFragment());
            }
            methodHookParam.setResult((Object) null);
            return Unit.INSTANCE;
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: compiled from: MiscHooks.kt */
    @Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\"\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b¨\u0006\t"}, d2 = {"Lni/shikatu/re_extera/hooks/misc/PluginSettingsHooks$Companion;", "", "<init>", "()V", "LOADER_IDS", "", "", "getLOADER_IDS", "()Ljava/util/Set;", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final Set<String> getLOADER_IDS() {
            return PluginSettingsHooks.LOADER_IDS;
        }
    }
}
