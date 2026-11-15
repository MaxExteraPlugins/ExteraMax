package ni.shikatu.re_extera.ui.settings;

import android.content.Context;
import android.view.View;
import java.util.ArrayList;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import ni.shikatu.re_extera.Initializer;
import ni.shikatu.re_extera.data.DeletedMessagesStore;
import ni.shikatu.re_extera.data.EditHistoryStore;
import ni.shikatu.re_extera.localization.Strings;
import ni.shikatu.re_extera.settings.ReSettings;
import ni.shikatu.re_extera.ui.Confirm;
import ni.shikatu.re_extera.ui.RegexFiltersFragment;
import ni.shikatu.re_extera.ui.ShadowbanFragment;
import ni.shikatu.re_extera.utils.MessageActions;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;

/* JADX INFO: compiled from: OtherSettingsFragment.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0004\u0018\u0000 \u00182\u00020\u0001:\u0001\u0018B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u0004\u001a\u00020\u0005H\u0016J(\u0010\u0006\u001a\u00020\u00072\u0016\u0010\b\u001a\u0012\u0012\u0004\u0012\u00020\n0\tj\b\u0012\u0004\u0012\u00020\n`\u000b2\u0006\u0010\f\u001a\u00020\rH\u0014J0\u0010\u000e\u001a\u00020\u00072\u0006\u0010\u000f\u001a\u00020\n2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0015H\u0014J\b\u0010\u0017\u001a\u00020\u0007H\u0002¨\u0006\u0019"}, d2 = {"Lni/shikatu/re_extera/ui/settings/OtherSettingsFragment;", "Lni/shikatu/re_extera/ui/settings/ReBasePreferencesFragment;", "<init>", "()V", "getTitle", "", "fillItems", "", "items", "Ljava/util/ArrayList;", "Lorg/telegram/ui/Components/UItem;", "Lkotlin/collections/ArrayList;", "adapter", "Lorg/telegram/ui/Components/UniversalAdapter;", "onClick", "item", "view", "Landroid/view/View;", "position", "", "x", "", "y", "clearDatabase", "Companion", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class OtherSettingsFragment extends ReBasePreferencesFragment {
    private static final Companion Companion = new Companion(null);

    @Deprecated
    public static final int ID_CLEAR_DB = 6;

    @Deprecated
    public static final int ID_FILTERS = 4;

    @Deprecated
    public static final int ID_FLAG_SECURE = 1;

    @Deprecated
    public static final int ID_LOCAL_PREMIUM = 2;

    @Deprecated
    public static final int ID_NO_FORWARD = 3;

    @Deprecated
    public static final int ID_SHADOWBAN = 5;

    @Deprecated
    public static final int ID_UNLOAD = 7;

    public String getTitle() {
        return Strings.INSTANCE.getOTHER();
    }

    protected void fillItems(ArrayList<UItem> items, UniversalAdapter adapter) {
        Intrinsics.checkNotNullParameter(items, "items");
        Intrinsics.checkNotNullParameter(adapter, "adapter");
        ArrayList<UItem> arrayList = items;
        OtherSettingsFragment otherSettingsFragment = this;
        arrayList.add(UItem.asCheck(1, Strings.INSTANCE.getIGNORE_FLAG_SECURE()).setChecked(ReSettings.INSTANCE.getIgnoreFlagSecure()).setLinkAlias("reExteraIgnoreFlagSecure", otherSettingsFragment));
        arrayList.add(UItem.asCheck(2, Strings.INSTANCE.getLOCAL_PREMIUM()).setChecked(ReSettings.INSTANCE.getLocalPremium()).setLinkAlias("reExteraLocalPremium", otherSettingsFragment));
        arrayList.add(UItem.asCheck(3, Strings.INSTANCE.getNO_FORWARD()).setChecked(ReSettings.INSTANCE.getNoForwards()).setLinkAlias("reExteraNoForward", otherSettingsFragment));
        arrayList.add(UItem.asShadow(Strings.INSTANCE.getNO_FORWARD_ABOUT()));
        arrayList.add(UItem.asButton(4, Strings.INSTANCE.getFILTERS()).setLinkAlias("reExteraFiltersEnter", otherSettingsFragment));
        arrayList.add(UItem.asButton(5, Strings.INSTANCE.getSHADOWBAN()).setLinkAlias("reExteraShadowban", otherSettingsFragment));
        arrayList.add(UItem.asShadow(Strings.INSTANCE.getSHADOWBAN_ABOUT()));
        arrayList.add(UItem.asButton(6, Strings.INSTANCE.getCLEAR_DB()).setLinkAlias("reExteraClearDb", otherSettingsFragment));
        arrayList.add(UItem.asButton(7, Strings.INSTANCE.getUNLOAD()).setLinkAlias("reExteraUnloadHooks", otherSettingsFragment));
        arrayList.add(UItem.asShadow());
    }

    protected void onClick(UItem item, View view, int position, float x, float y) {
        Intrinsics.checkNotNullParameter(item, "item");
        Intrinsics.checkNotNullParameter(view, "view");
        switch (item.id) {
            case 1:
                toggle(item, ReSettings.INSTANCE.getIgnoreFlagSecure(), new Function1() { // from class: ni.shikatu.re_extera.ui.settings.OtherSettingsFragment.0
                    public final Object invoke(Object obj) {
                        return OtherSettingsFragment.onClick$lambda$0(((Boolean) obj).booleanValue());
                    }
                });
                break;
            case 2:
                if (!ReSettings.INSTANCE.getLocalPremium() && getUserConfig().isPremium()) {
                    BulletinFactory.of((BaseFragment) this).createEmojiBulletin("❌", Strings.INSTANCE.getCANT_USE_WITH_PREMIUM()).show();
                } else {
                    toggle(item, ReSettings.INSTANCE.getLocalPremium(), new Function1() { // from class: ni.shikatu.re_extera.ui.settings.OtherSettingsFragment.2
                        public final Object invoke(Object obj) {
                            return OtherSettingsFragment.onClick$lambda$2(((Boolean) obj).booleanValue());
                        }
                    });
                    getNotificationCenter().postNotificationName(NotificationCenter.mainUserInfoChanged, new Object[0]);
                }
                break;
            case 3:
                toggle(item, ReSettings.INSTANCE.getNoForwards(), new Function1() { // from class: ni.shikatu.re_extera.ui.settings.OtherSettingsFragment.1
                    public final Object invoke(Object obj) {
                        return OtherSettingsFragment.onClick$lambda$1(((Boolean) obj).booleanValue());
                    }
                });
                break;
            case 4:
                presentFragment((BaseFragment) new RegexFiltersFragment());
                break;
            case 5:
                presentFragment((BaseFragment) new ShadowbanFragment());
                break;
            case 6:
                Confirm confirm = Confirm.INSTANCE;
                Context context = getContext();
                Intrinsics.checkNotNullExpressionValue(context, "getContext(...)");
                String clear_db = Strings.INSTANCE.getCLEAR_DB();
                String clear_db_confirm = Strings.INSTANCE.getCLEAR_DB_CONFIRM();
                String string = LocaleController.getString(R.string.Delete);
                Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
                Confirm.show$default(confirm, context, clear_db, clear_db_confirm, string, false, new Function0() { // from class: ni.shikatu.re_extera.ui.settings.OtherSettingsFragment.3
                    public final Object invoke() {
                        return OtherSettingsFragment.onClick$lambda$3(OtherSettingsFragment.this);
                    }
                }, 16, null);
                break;
            case 7:
                Initializer.unload();
                BulletinFactory.of((BaseFragment) this).createSuccessBulletin(Strings.INSTANCE.getUNLOADED()).show();
                break;
        }
    }

    static final Unit onClick$lambda$0(boolean z) {
        ReSettings.INSTANCE.setIgnoreFlagSecure(z);
        return Unit.INSTANCE;
    }

    static final Unit onClick$lambda$1(boolean z) {
        ReSettings.INSTANCE.setNoForwards(z);
        return Unit.INSTANCE;
    }

    static final Unit onClick$lambda$2(boolean z) {
        ReSettings.INSTANCE.setLocalPremium(z);
        return Unit.INSTANCE;
    }

    static final Unit onClick$lambda$3(OtherSettingsFragment otherSettingsFragment) {
        otherSettingsFragment.clearDatabase();
        return Unit.INSTANCE;
    }

    private final void clearDatabase() {
        Iterator<Long> it = DeletedMessagesStore.INSTANCE.dialogs().iterator();
        while (it.hasNext()) {
            MessageActions.INSTANCE.purgeDialog(this.currentAccount, it.next().longValue());
        }
        DeletedMessagesStore.INSTANCE.clear();
        EditHistoryStore.INSTANCE.clear();
    }

    /* JADX INFO: compiled from: OtherSettingsFragment.kt */
    @Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0007\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\f"}, d2 = {"Lni/shikatu/re_extera/ui/settings/OtherSettingsFragment$Companion;", "", "<init>", "()V", "ID_FLAG_SECURE", "", "ID_LOCAL_PREMIUM", "ID_NO_FORWARD", "ID_FILTERS", "ID_SHADOWBAN", "ID_CLEAR_DB", "ID_UNLOAD", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
