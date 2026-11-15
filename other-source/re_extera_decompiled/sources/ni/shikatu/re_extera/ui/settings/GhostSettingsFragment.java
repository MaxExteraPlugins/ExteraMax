package ni.shikatu.re_extera.ui.settings;

import android.view.View;
import com.exteragram.messenger.utils.ui.PopupUtils;
import java.util.ArrayList;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.enums.EnumEntries;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import ni.shikatu.re_extera.localization.Strings;
import ni.shikatu.re_extera.settings.ReSettings;
import ni.shikatu.re_extera.settings.SendSilence;
import ni.shikatu.re_extera.ui.ExclusionsFragment;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;

/* JADX INFO: compiled from: GhostSettingsFragment.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 !2\u00020\u0001:\u0001!B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u0006\u001a\u00020\u0007H\u0016J(\u0010\b\u001a\u00020\t2\u0016\u0010\n\u001a\u0012\u0012\u0004\u0012\u00020\f0\u000bj\b\u0012\u0004\u0012\u00020\f`\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0014J0\u0010\u0010\u001a\u00020\t2\u0006\u0010\u0011\u001a\u00020\f2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0017H\u0014J(\u0010\u0019\u001a\n \u001a*\u0004\u0018\u00010\f0\f2\u0006\u0010\u001b\u001a\u00020\u00152\u0006\u0010\u001c\u001a\u00020\u00072\u0006\u0010\u001d\u001a\u00020\u0005H\u0002J\u0010\u0010\u001e\u001a\u00020\u00072\u0006\u0010\u001f\u001a\u00020 H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\""}, d2 = {"Lni/shikatu/re_extera/ui/settings/GhostSettingsFragment;", "Lni/shikatu/re_extera/ui/settings/ReBasePreferencesFragment;", "<init>", "()V", "expanded", "", "getTitle", "", "fillItems", "", "items", "Ljava/util/ArrayList;", "Lorg/telegram/ui/Components/UItem;", "Lkotlin/collections/ArrayList;", "adapter", "Lorg/telegram/ui/Components/UniversalAdapter;", "onClick", "item", "view", "Landroid/view/View;", "position", "", "x", "", "y", "option", "kotlin.jvm.PlatformType", "id", "text", "checked", "silenceLabel", "mode", "Lni/shikatu/re_extera/settings/SendSilence;", "Companion", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class GhostSettingsFragment extends ReBasePreferencesFragment {
    private static final Companion Companion = new Companion(null);

    @Deprecated
    public static final int ID_EXCLUSIONS = 11;

    @Deprecated
    public static final int ID_GHOST = 1;

    @Deprecated
    public static final int ID_HIDE_ONLINE = 2;

    @Deprecated
    public static final int ID_HIDE_READING = 5;

    @Deprecated
    public static final int ID_HIDE_TYPING = 4;

    @Deprecated
    public static final int ID_IMMEDIATE_OFFLINE = 3;

    @Deprecated
    public static final int ID_MENU_BUTTON = 10;

    @Deprecated
    public static final int ID_NO_STORIES = 6;

    @Deprecated
    public static final int ID_READ_ON_INTERACT = 7;

    @Deprecated
    public static final int ID_SEND_SILENCE = 8;

    @Deprecated
    public static final int ID_USE_SCHEDULE = 9;
    private boolean expanded;

    /* JADX INFO: compiled from: GhostSettingsFragment.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    public static final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[SendSilence.values().length];
            try {
                iArr[SendSilence.ALWAYS.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[SendSilence.ONLY_WITH_GHOST.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[SendSilence.NEVER.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public String getTitle() {
        return Strings.INSTANCE.getGHOST_MODE();
    }

    protected void fillItems(ArrayList<UItem> items, UniversalAdapter adapter) {
        Intrinsics.checkNotNullParameter(items, "items");
        Intrinsics.checkNotNullParameter(adapter, "adapter");
        ArrayList<UItem> arrayList = items;
        UItem uItemAsExteraExpandableSwitch = UItem.asExteraExpandableSwitch(1, Strings.INSTANCE.getGHOST_MODE(), ReSettings.INSTANCE.getGhostOptionsCount() + "/5", new View.OnClickListener() { // from class: ni.shikatu.re_extera.ui.settings.GhostSettingsFragment.0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                GhostSettingsFragment.fillItems$lambda$0(GhostSettingsFragment.this, view);
            }
        });
        uItemAsExteraExpandableSwitch.setChecked(ReSettings.INSTANCE.getGhostEnabled());
        uItemAsExteraExpandableSwitch.setCollapsed(this.expanded ^ true);
        uItemAsExteraExpandableSwitch.clickCallback = new View.OnClickListener() { // from class: ni.shikatu.re_extera.ui.settings.GhostSettingsFragment.1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                GhostSettingsFragment.fillItems$lambda$2$lambda$1(GhostSettingsFragment.this, view);
            }
        };
        GhostSettingsFragment ghostSettingsFragment = this;
        uItemAsExteraExpandableSwitch.setLinkAlias("reExteraGhostMode", ghostSettingsFragment);
        arrayList.add(uItemAsExteraExpandableSwitch);
        if (this.expanded) {
            arrayList.add(option(2, Strings.INSTANCE.getHIDE_ONLINE(), ReSettings.INSTANCE.getHideOnline()));
            arrayList.add(option(3, Strings.INSTANCE.getIMMEDIATE_OFFLINE(), ReSettings.INSTANCE.getImmediateOffline()));
            arrayList.add(option(4, Strings.INSTANCE.getHIDE_TYPING(), ReSettings.INSTANCE.getHideTyping()));
            arrayList.add(option(5, Strings.INSTANCE.getHIDE_READING(), ReSettings.INSTANCE.getHideReading()));
            arrayList.add(option(6, Strings.INSTANCE.getNO_READ_STORIES(), ReSettings.INSTANCE.getNoReadStories()));
        }
        arrayList.add(UItem.asShadow());
        arrayList.add(UItem.asCheck(7, Strings.INSTANCE.getREAD_ON_INTERACT()).setChecked(ReSettings.INSTANCE.getReadOnInteract()).setLinkAlias("reExteraReadOnInteract", ghostSettingsFragment));
        arrayList.add(UItem.asShadow(Strings.INSTANCE.getREAD_ON_INTERACT_ABOUT()));
        arrayList.add(UItem.asCheck(9, Strings.INSTANCE.getUSE_SCHEDULE()).setChecked(ReSettings.INSTANCE.getUseSchedule()).setLinkAlias("reExteraUseSchedule", ghostSettingsFragment));
        arrayList.add(UItem.asButton(8, Strings.INSTANCE.getSEND_SILENCE(), silenceLabel(ReSettings.INSTANCE.getSendSilence())).setLinkAlias("reExteraSendSilence", ghostSettingsFragment));
        arrayList.add(UItem.asCheck(10, Strings.INSTANCE.getGHOST_MENU_BUTTON()).setChecked(ReSettings.INSTANCE.getGhostMenuButton()).setLinkAlias("reExteraGhostMenuButton", ghostSettingsFragment));
        arrayList.add(UItem.asShadow());
        arrayList.add(UItem.asButton(11, Strings.INSTANCE.getEXCLUSIONS()).setLinkAlias("reExteraExclusions", ghostSettingsFragment));
    }

    static final void fillItems$lambda$0(GhostSettingsFragment ghostSettingsFragment, View view) {
        ReSettings.INSTANCE.setGhostEnabled(!ReSettings.INSTANCE.getGhostEnabled());
        ReBasePreferencesFragment.refresh$default(ghostSettingsFragment, false, 1, null);
    }

    static final void fillItems$lambda$2$lambda$1(GhostSettingsFragment ghostSettingsFragment, View view) {
        ghostSettingsFragment.expanded = !ghostSettingsFragment.expanded;
        ReBasePreferencesFragment.refresh$default(ghostSettingsFragment, false, 1, null);
    }

    protected void onClick(UItem item, View view, int position, float x, float y) {
        Intrinsics.checkNotNullParameter(item, "item");
        Intrinsics.checkNotNullParameter(view, "view");
        switch (item.id) {
            case 1:
                this.expanded = !this.expanded;
                ReBasePreferencesFragment.refresh$default(this, false, 1, null);
                break;
            case 2:
                toggle(item, ReSettings.INSTANCE.getHideOnline(), new Function1() { // from class: ni.shikatu.re_extera.ui.settings.GhostSettingsFragment.2
                    public final Object invoke(Object obj) {
                        return GhostSettingsFragment.onClick$lambda$3(((Boolean) obj).booleanValue());
                    }
                });
                break;
            case 3:
                toggle(item, ReSettings.INSTANCE.getImmediateOffline(), new Function1() { // from class: ni.shikatu.re_extera.ui.settings.GhostSettingsFragment.3
                    public final Object invoke(Object obj) {
                        return GhostSettingsFragment.onClick$lambda$4(((Boolean) obj).booleanValue());
                    }
                });
                break;
            case 4:
                toggle(item, ReSettings.INSTANCE.getHideTyping(), new Function1() { // from class: ni.shikatu.re_extera.ui.settings.GhostSettingsFragment.4
                    public final Object invoke(Object obj) {
                        return GhostSettingsFragment.onClick$lambda$5(((Boolean) obj).booleanValue());
                    }
                });
                break;
            case 5:
                toggle(item, ReSettings.INSTANCE.getHideReading(), new Function1() { // from class: ni.shikatu.re_extera.ui.settings.GhostSettingsFragment.5
                    public final Object invoke(Object obj) {
                        return GhostSettingsFragment.onClick$lambda$6(((Boolean) obj).booleanValue());
                    }
                });
                break;
            case 6:
                toggle(item, ReSettings.INSTANCE.getNoReadStories(), new Function1() { // from class: ni.shikatu.re_extera.ui.settings.GhostSettingsFragment.6
                    public final Object invoke(Object obj) {
                        return GhostSettingsFragment.onClick$lambda$7(((Boolean) obj).booleanValue());
                    }
                });
                break;
            case 7:
                toggle(item, ReSettings.INSTANCE.getReadOnInteract(), new Function1() { // from class: ni.shikatu.re_extera.ui.settings.GhostSettingsFragment.7
                    public final Object invoke(Object obj) {
                        return GhostSettingsFragment.onClick$lambda$8(((Boolean) obj).booleanValue());
                    }
                });
                break;
            case ID_SEND_SILENCE /* 8 */:
                final Iterable entries = SendSilence.getEntries();
                Iterable iterable = entries;
                ArrayList arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
                Iterator it = iterable.iterator();
                while (it.hasNext()) {
                    arrayList.add(silenceLabel((SendSilence) it.next()));
                }
                showListDialog(item, (CharSequence[]) arrayList.toArray(new String[0]), Strings.INSTANCE.getSEND_SILENCE(), entries.indexOf(ReSettings.INSTANCE.getSendSilence()), new PopupUtils.OnItemClickListener() { // from class: ni.shikatu.re_extera.ui.settings.GhostSettingsFragment.10
                    public final void onClick(int i) {
                        GhostSettingsFragment.onClick$lambda$11(entries, i);
                    }
                });
                break;
            case ID_USE_SCHEDULE /* 9 */:
                toggle(item, ReSettings.INSTANCE.getUseSchedule(), new Function1() { // from class: ni.shikatu.re_extera.ui.settings.GhostSettingsFragment.8
                    public final Object invoke(Object obj) {
                        return GhostSettingsFragment.onClick$lambda$9(((Boolean) obj).booleanValue());
                    }
                });
                break;
            case ID_MENU_BUTTON /* 10 */:
                toggle(item, ReSettings.INSTANCE.getGhostMenuButton(), new Function1() { // from class: ni.shikatu.re_extera.ui.settings.GhostSettingsFragment.9
                    public final Object invoke(Object obj) {
                        return GhostSettingsFragment.onClick$lambda$10(GhostSettingsFragment.this, ((Boolean) obj).booleanValue());
                    }
                });
                break;
            case ID_EXCLUSIONS /* 11 */:
                presentFragment((BaseFragment) new ExclusionsFragment());
                break;
        }
    }

    static final Unit onClick$lambda$3(boolean z) {
        ReSettings.INSTANCE.setHideOnline(z);
        return Unit.INSTANCE;
    }

    static final Unit onClick$lambda$4(boolean z) {
        ReSettings.INSTANCE.setImmediateOffline(z);
        return Unit.INSTANCE;
    }

    static final Unit onClick$lambda$5(boolean z) {
        ReSettings.INSTANCE.setHideTyping(z);
        return Unit.INSTANCE;
    }

    static final Unit onClick$lambda$6(boolean z) {
        ReSettings.INSTANCE.setHideReading(z);
        return Unit.INSTANCE;
    }

    static final Unit onClick$lambda$7(boolean z) {
        ReSettings.INSTANCE.setNoReadStories(z);
        return Unit.INSTANCE;
    }

    static final Unit onClick$lambda$8(boolean z) {
        ReSettings.INSTANCE.setReadOnInteract(z);
        return Unit.INSTANCE;
    }

    static final Unit onClick$lambda$9(boolean z) {
        ReSettings.INSTANCE.setUseSchedule(z);
        return Unit.INSTANCE;
    }

    static final Unit onClick$lambda$10(GhostSettingsFragment ghostSettingsFragment, boolean z) {
        ReSettings.INSTANCE.setGhostMenuButton(z);
        ghostSettingsFragment.getNotificationCenter().postNotificationName(NotificationCenter.mainUserInfoChanged, new Object[0]);
        return Unit.INSTANCE;
    }

    static final void onClick$lambda$11(EnumEntries enumEntries, int i) {
        ReSettings.INSTANCE.setSendSilence((SendSilence) enumEntries.get(i));
    }

    private final UItem option(int id, String text, boolean checked) {
        return UItem.asRoundCheckbox(id, text).setChecked(checked).pad();
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: kotlin.NoWhenBranchMatchedException */
    private final String silenceLabel(SendSilence mode) throws NoWhenBranchMatchedException {
        int i = WhenMappings.$EnumSwitchMapping$0[mode.ordinal()];
        if (i == 1) {
            return Strings.INSTANCE.getALWAYS();
        }
        if (i == 2) {
            return Strings.INSTANCE.getONLY_WITH_GHOST();
        }
        if (i != 3) {
            throw new NoWhenBranchMatchedException();
        }
        return Strings.INSTANCE.getNEVER();
    }

    /* JADX INFO: compiled from: GhostSettingsFragment.kt */
    @Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u000b\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0010"}, d2 = {"Lni/shikatu/re_extera/ui/settings/GhostSettingsFragment$Companion;", "", "<init>", "()V", "ID_GHOST", "", "ID_HIDE_ONLINE", "ID_IMMEDIATE_OFFLINE", "ID_HIDE_TYPING", "ID_HIDE_READING", "ID_NO_STORIES", "ID_READ_ON_INTERACT", "ID_SEND_SILENCE", "ID_USE_SCHEDULE", "ID_MENU_BUTTON", "ID_EXCLUSIONS", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
