package ni.shikatu.re_extera;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import ni.shikatu.re_extera.data.DeletedMessagesStore;
import ni.shikatu.re_extera.data.EditHistoryStore;
import ni.shikatu.re_extera.data.ExclusionsStore;
import ni.shikatu.re_extera.data.ReDatabase;
import ni.shikatu.re_extera.data.RegexFiltersStore;
import ni.shikatu.re_extera.data.ShadowbanStore;
import ni.shikatu.re_extera.hooks.HookerClass;
import ni.shikatu.re_extera.hooks.antirecall.DeletedMessagesHooks;
import ni.shikatu.re_extera.hooks.antirecall.EditHistoryHooks;
import ni.shikatu.re_extera.hooks.antirecall.MessageCellHooks;
import ni.shikatu.re_extera.hooks.chat.ChatMenuHooks;
import ni.shikatu.re_extera.hooks.filters.MessageFilterHooks;
import ni.shikatu.re_extera.hooks.forward.NoForwardsHooks;
import ni.shikatu.re_extera.hooks.ghost.GhostMenuHooks;
import ni.shikatu.re_extera.hooks.ghost.GhostRequestsHooks;
import ni.shikatu.re_extera.hooks.ghost.OwnLastSeenHooks;
import ni.shikatu.re_extera.hooks.misc.FlagSecureHooks;
import ni.shikatu.re_extera.hooks.misc.LocalPremiumHooks;
import ni.shikatu.re_extera.hooks.misc.PluginSettingsHooks;
import ni.shikatu.re_extera.hooks.onetime.OneTimeMessagesHooks;
import ni.shikatu.re_extera.hooks.send.SendMessageHooks;
import ni.shikatu.re_extera.hooks.shadowban.ShadowbanHooks;
import ni.shikatu.re_extera.localization.Strings;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.LaunchActivity;

/* JADX INFO: compiled from: Main.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u0000 \u00122\u00020\u0001:\u0001\u0012B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u0006\u0010\u000f\u001a\u00020\u0010J\u0006\u0010\u0011\u001a\u00020\u0010R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\t8F¢\u0006\u0006\u001a\u0004\b\r\u0010\u000e¨\u0006\u0013"}, d2 = {"Lni/shikatu/re_extera/Main;", "", "applicationContext", "Landroid/content/Context;", "<init>", "(Landroid/content/Context;)V", "getApplicationContext", "()Landroid/content/Context;", "hookers", "", "Lni/shikatu/re_extera/hooks/HookerClass;", "hookFailures", "", "getHookFailures", "()Ljava/util/List;", "setup", "", "disable", "Companion", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Main {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    public static final String VERSION = "3.0.2";
    private static volatile Main instance;
    private final Context applicationContext;
    private final List<HookerClass> hookers;

    public Main(Context context) {
        Intrinsics.checkNotNullParameter(context, "applicationContext");
        this.applicationContext = context;
        this.hookers = CollectionsKt.listOf(new HookerClass[]{new GhostRequestsHooks(), new OwnLastSeenHooks(), new GhostMenuHooks(), new DeletedMessagesHooks(), new EditHistoryHooks(), new MessageCellHooks(), new OneTimeMessagesHooks(), new NoForwardsHooks(), new SendMessageHooks(), new ShadowbanHooks(), new MessageFilterHooks(), new ChatMenuHooks(), new LocalPremiumHooks(), new FlagSecureHooks(), new PluginSettingsHooks()});
    }

    public final Context getApplicationContext() {
        return this.applicationContext;
    }

    public final List<String> getHookFailures() {
        List<HookerClass> list = this.hookers;
        ArrayList arrayList = new ArrayList();
        Iterator<T> it = list.iterator();
        while (it.hasNext()) {
            CollectionsKt.addAll(arrayList, ((HookerClass) it.next()).getFailures());
        }
        return arrayList;
    }

    public final void setup() {
        ReLog.INSTANCE.d("Starting re:extera 3.0.2");
        ReDatabase.INSTANCE.init(this.applicationContext).post(new Function1() { // from class: ni.shikatu.re_extera.Main.0
            public final Object invoke(Object obj) {
                return Main.setup$lambda$1((SQLiteDatabase) obj);
            }
        });
        Iterator<T> it = this.hookers.iterator();
        while (it.hasNext()) {
            ((HookerClass) it.next()).install();
        }
        List<String> hookFailures = getHookFailures();
        if (hookFailures.isEmpty()) {
            return;
        }
        ReLog.e$default(ReLog.INSTANCE, "Hooks failed: " + CollectionsKt.joinToString$default(hookFailures, (CharSequence) null, (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 63, (Object) null), null, 2, null);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: ni.shikatu.re_extera.Main.1
            @Override // java.lang.Runnable
            public final void run() {
                Main.setup$lambda$3();
            }
        }, 1000L);
    }

    static final Unit setup$lambda$1(SQLiteDatabase sQLiteDatabase) {
        Intrinsics.checkNotNullParameter(sQLiteDatabase, "$this$post");
        DeletedMessagesStore.INSTANCE.load(sQLiteDatabase);
        EditHistoryStore.INSTANCE.load(sQLiteDatabase);
        ExclusionsStore.INSTANCE.load(sQLiteDatabase);
        RegexFiltersStore.INSTANCE.load(sQLiteDatabase);
        ShadowbanStore.INSTANCE.load(sQLiteDatabase);
        return Unit.INSTANCE;
    }

    static final void setup$lambda$3() {
        BaseFragment lastFragment = LaunchActivity.getLastFragment();
        if (lastFragment == null) {
            return;
        }
        BulletinFactory.of(lastFragment).createErrorBulletin(Strings.INSTANCE.getHOOKS_FAILED_BULLETIN()).show();
    }

    public final void disable() {
        Iterator<T> it = this.hookers.iterator();
        while (it.hasNext()) {
            ((HookerClass) it.next()).disable();
        }
    }

    /* JADX INFO: compiled from: Main.kt */
    @Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R(\u0010\b\u001a\u0004\u0018\u00010\u00072\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007@@X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\f¨\u0006\r"}, d2 = {"Lni/shikatu/re_extera/Main$Companion;", "", "<init>", "()V", "VERSION", "", "value", "Lni/shikatu/re_extera/Main;", "instance", "getInstance", "()Lni/shikatu/re_extera/Main;", "setInstance$re_extera", "(Lni/shikatu/re_extera/Main;)V", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final Main getInstance() {
            return Main.instance;
        }

        public final void setInstance$re_extera(Main main) {
            Main.instance = main;
        }
    }
}
