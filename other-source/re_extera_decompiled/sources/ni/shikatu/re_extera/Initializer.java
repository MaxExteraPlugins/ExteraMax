package ni.shikatu.re_extera;

import android.content.Context;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import ni.shikatu.re_extera.ui.settings.RootSettingsFragment;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.LaunchActivity;

/* JADX INFO: compiled from: Initializer.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u0004\u001a\u00020\u0005H\u0007J\b\u0010\u0006\u001a\u00020\u0005H\u0007J\b\u0010\u0007\u001a\u00020\u0005H\u0007¨\u0006\b"}, d2 = {"Lni/shikatu/re_extera/Initializer;", "", "<init>", "()V", "init", "", "unload", "showSettings", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Initializer {
    public static final Initializer INSTANCE = new Initializer();

    private Initializer() {
    }

    @JvmStatic
    public static final synchronized void init() {
        if (Main.INSTANCE.getInstance() != null) {
            return;
        }
        Context context = ApplicationLoader.applicationContext;
        if (context == null) {
            throw new IllegalStateException("applicationContext is null".toString());
        }
        Main.Companion companion = Main.INSTANCE;
        Main main = new Main(context);
        main.setup();
        companion.setInstance$re_extera(main);
    }

    @JvmStatic
    public static final synchronized void unload() {
        Main companion = Main.INSTANCE.getInstance();
        if (companion != null) {
            companion.disable();
        }
        Main.INSTANCE.setInstance$re_extera(null);
    }

    @JvmStatic
    public static final void showSettings() {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: ni.shikatu.re_extera.Initializer.0
            @Override // java.lang.Runnable
            public final void run() {
                Initializer.showSettings$lambda$2();
            }
        });
    }

    static final void showSettings$lambda$2() {
        BaseFragment lastFragment = LaunchActivity.getLastFragment();
        if (lastFragment != null) {
            lastFragment.presentFragment(new RootSettingsFragment());
        }
    }
}
