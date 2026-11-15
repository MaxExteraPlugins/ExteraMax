package ni.shikatu.re_extera.ui.settings;

import android.app.Activity;
import android.app.Dialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import java.util.ArrayList;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import ni.shikatu.re_extera.localization.Strings;
import ni.shikatu.re_extera.settings.ReSettings;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Cells.EditTextSettingsCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Components.ItemOptions;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;

/* JADX INFO: compiled from: SpySettingsFragment.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 !2\u00020\u0001:\u0001!B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u0004\u001a\u00020\u0005H\u0016J(\u0010\u0006\u001a\u00020\u00072\u0016\u0010\b\u001a\u0012\u0012\u0004\u0012\u00020\n0\tj\b\u0012\u0004\u0012\u00020\n`\u000b2\u0006\u0010\f\u001a\u00020\rH\u0014J0\u0010\u000e\u001a\u00020\u00072\u0006\u0010\u000f\u001a\u00020\n2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0015H\u0014J0\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u000f\u001a\u00020\n2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0015H\u0014J\b\u0010\u0019\u001a\u00020\u0011H\u0002J\b\u0010\u001a\u001a\u00020\u0007H\u0002J4\u0010\u001b\u001a\u00020\u00112\u0006\u0010\u001c\u001a\u00020\u00052\u0006\u0010\u001d\u001a\u00020\u00052\u0006\u0010\u001e\u001a\u00020\u00182\u0012\u0010\u001f\u001a\u000e\u0012\u0004\u0012\u00020\u0018\u0012\u0004\u0012\u00020\u00070 H\u0002¨\u0006\""}, d2 = {"Lni/shikatu/re_extera/ui/settings/SpySettingsFragment;", "Lni/shikatu/re_extera/ui/settings/ReBasePreferencesFragment;", "<init>", "()V", "getTitle", "", "fillItems", "", "items", "Ljava/util/ArrayList;", "Lorg/telegram/ui/Components/UItem;", "Lkotlin/collections/ArrayList;", "adapter", "Lorg/telegram/ui/Components/UniversalAdapter;", "onClick", "item", "view", "Landroid/view/View;", "position", "", "x", "", "y", "onLongClick", "", "createMarkEditor", "showAdditionalSettings", "switchCell", "title", "description", "initial", "onChange", "Lkotlin/Function1;", "Companion", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class SpySettingsFragment extends ReBasePreferencesFragment {
    private static final Companion Companion = new Companion(null);

    @Deprecated
    public static final int ID_CUSTOM_MARK = 5;

    @Deprecated
    public static final int ID_RED_MARK = 4;

    @Deprecated
    public static final int ID_SAVE_DELETED = 1;

    @Deprecated
    public static final int ID_SAVE_EDITS = 3;

    @Deprecated
    public static final int ID_SAVE_ONE_TIME = 2;

    @Deprecated
    public static final int ID_TRANSLUCENT = 6;

    public String getTitle() {
        return Strings.INSTANCE.getSPY();
    }

    protected void fillItems(ArrayList<UItem> items, UniversalAdapter adapter) {
        Intrinsics.checkNotNullParameter(items, "items");
        Intrinsics.checkNotNullParameter(adapter, "adapter");
        ArrayList<UItem> arrayList = items;
        SpySettingsFragment spySettingsFragment = this;
        arrayList.add(UItem.asCheck(1, Strings.INSTANCE.getSAVE_DELETED(), Strings.INSTANCE.getHOLD_FOR_MORE(), true).setChecked(ReSettings.INSTANCE.getSaveDeleted()).setLinkAlias("reExteraSaveDeletedMessages", spySettingsFragment));
        arrayList.add(UItem.asCheck(2, Strings.INSTANCE.getSAVE_ONE_TIME()).setChecked(ReSettings.INSTANCE.getSaveOneTime()).setLinkAlias("reExteraSaveOneTimeMessages", spySettingsFragment));
        arrayList.add(UItem.asCheck(3, Strings.INSTANCE.getSAVE_EDITS()).setChecked(ReSettings.INSTANCE.getSaveEdits()).setLinkAlias("reExteraSaveMessageHistory", spySettingsFragment));
        arrayList.add(UItem.asShadow());
        arrayList.add(UItem.asCheck(4, Strings.INSTANCE.getRED_DELETED_MARK()).setChecked(ReSettings.INSTANCE.getRedDeletedMark()).setLinkAlias("reExteraRedDeletedMark", spySettingsFragment));
        arrayList.add(UItem.asCheck(6, Strings.INSTANCE.getTRANSLUCENT_DELETED()).setChecked(ReSettings.INSTANCE.getTranslucentDeleted()).setLinkAlias("reExteraTranslucentDeleted", spySettingsFragment));
        arrayList.add(UItem.asHeader(Strings.INSTANCE.getDELETED_MARK()));
        arrayList.add(UItem.asCustom(5, createMarkEditor()).setLinkAlias("reExteraCustomDeletedMark", spySettingsFragment));
        arrayList.add(UItem.asShadow());
    }

    protected void onClick(UItem item, View view, int position, float x, float y) {
        Intrinsics.checkNotNullParameter(item, "item");
        Intrinsics.checkNotNullParameter(view, "view");
        int i = item.id;
        if (i == 1) {
            toggle(item, ReSettings.INSTANCE.getSaveDeleted(), new Function1() { // from class: ni.shikatu.re_extera.ui.settings.SpySettingsFragment.4
                public final Object invoke(Object obj) {
                    return SpySettingsFragment.onClick$lambda$0(((Boolean) obj).booleanValue());
                }
            });
            return;
        }
        if (i == 2) {
            toggle(item, ReSettings.INSTANCE.getSaveOneTime(), new Function1() { // from class: ni.shikatu.re_extera.ui.settings.SpySettingsFragment.5
                public final Object invoke(Object obj) {
                    return SpySettingsFragment.onClick$lambda$1(((Boolean) obj).booleanValue());
                }
            });
            return;
        }
        if (i == 3) {
            toggle(item, ReSettings.INSTANCE.getSaveEdits(), new Function1() { // from class: ni.shikatu.re_extera.ui.settings.SpySettingsFragment.6
                public final Object invoke(Object obj) {
                    return SpySettingsFragment.onClick$lambda$2(((Boolean) obj).booleanValue());
                }
            });
        } else if (i == 4) {
            toggle(item, ReSettings.INSTANCE.getRedDeletedMark(), new Function1() { // from class: ni.shikatu.re_extera.ui.settings.SpySettingsFragment.7
                public final Object invoke(Object obj) {
                    return SpySettingsFragment.onClick$lambda$3(((Boolean) obj).booleanValue());
                }
            });
        } else {
            if (i != 6) {
                return;
            }
            toggle(item, ReSettings.INSTANCE.getTranslucentDeleted(), new Function1() { // from class: ni.shikatu.re_extera.ui.settings.SpySettingsFragment.8
                public final Object invoke(Object obj) {
                    return SpySettingsFragment.onClick$lambda$4(((Boolean) obj).booleanValue());
                }
            });
        }
    }

    static final Unit onClick$lambda$0(boolean z) {
        ReSettings.INSTANCE.setSaveDeleted(z);
        return Unit.INSTANCE;
    }

    static final Unit onClick$lambda$1(boolean z) {
        ReSettings.INSTANCE.setSaveOneTime(z);
        return Unit.INSTANCE;
    }

    static final Unit onClick$lambda$2(boolean z) {
        ReSettings.INSTANCE.setSaveEdits(z);
        return Unit.INSTANCE;
    }

    static final Unit onClick$lambda$3(boolean z) {
        ReSettings.INSTANCE.setRedDeletedMark(z);
        return Unit.INSTANCE;
    }

    static final Unit onClick$lambda$4(boolean z) {
        ReSettings.INSTANCE.setTranslucentDeleted(z);
        return Unit.INSTANCE;
    }

    protected boolean onLongClick(UItem item, View view, int position, float x, float y) {
        Intrinsics.checkNotNullParameter(item, "item");
        Intrinsics.checkNotNullParameter(view, "view");
        if (item.id != 1) {
            return super.onLongClick(item, view, position, x, y);
        }
        ItemOptions itemOptionsMakeOptions = ItemOptions.makeOptions((BaseFragment) this, view);
        Intrinsics.checkNotNullExpressionValue(itemOptionsMakeOptions, "makeOptions(...)");
        addCopyLink(itemOptionsMakeOptions, item).add(R.drawable.msg_settings, Strings.INSTANCE.getADDITIONAL_SETTINGS(), new Runnable() { // from class: ni.shikatu.re_extera.ui.settings.SpySettingsFragment.0
            @Override // java.lang.Runnable
            public final void run() {
                SpySettingsFragment.this.showAdditionalSettings();
            }
        }).show();
        return true;
    }

    private final View createMarkEditor() {
        FrameLayout frameLayout = new FrameLayout(getContext());
        EditTextSettingsCell editTextSettingsCell = new EditTextSettingsCell(frameLayout.getContext());
        editTextSettingsCell.setTextAndHint(ReSettings.INSTANCE.getDeletedMark(), Strings.INSTANCE.getDELETED_MARK_HINT(), false);
        editTextSettingsCell.getTextView().addTextChangedListener(new TextWatcher() { // from class: ni.shikatu.re_extera.ui.settings.SpySettingsFragment$createMarkEditor$1$1
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable s) {
                ReSettings reSettings = ReSettings.INSTANCE;
                String string = s != null ? s.toString() : null;
                if (string == null) {
                    string = "";
                }
                reSettings.setDeletedMark(string);
            }
        });
        frameLayout.addView((View) editTextSettingsCell);
        return frameLayout;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void showAdditionalSettings() {
        Activity parentActivity = getParentActivity();
        if (parentActivity == null) {
            return;
        }
        Activity activity = parentActivity;
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(1);
        linearLayout.addView(switchCell(Strings.INSTANCE.getSAVE_MANUALLY_DELETED(), Strings.INSTANCE.getSAVE_MANUALLY_DELETED_ABOUT(), ReSettings.INSTANCE.getSaveManuallyDeleted(), new Function1() { // from class: ni.shikatu.re_extera.ui.settings.SpySettingsFragment.2
            public final Object invoke(Object obj) {
                return SpySettingsFragment.showAdditionalSettings$lambda$8(((Boolean) obj).booleanValue());
            }
        }));
        linearLayout.addView(switchCell(Strings.INSTANCE.getCOLLAPSED_QUOTE(), Strings.INSTANCE.getCOLLAPSED_QUOTE_ABOUT(), ReSettings.INSTANCE.getCollapsedReplyQuote(), new Function1() { // from class: ni.shikatu.re_extera.ui.settings.SpySettingsFragment.3
            public final Object invoke(Object obj) {
                return SpySettingsFragment.showAdditionalSettings$lambda$9(((Boolean) obj).booleanValue());
            }
        }));
        showDialog((Dialog) new AlertDialog.Builder(activity).setTitle(Strings.INSTANCE.getADDITIONAL_SETTINGS()).setView(linearLayout).create());
    }

    static final Unit showAdditionalSettings$lambda$8(boolean z) {
        ReSettings.INSTANCE.setSaveManuallyDeleted(z);
        return Unit.INSTANCE;
    }

    static final Unit showAdditionalSettings$lambda$9(boolean z) {
        ReSettings.INSTANCE.setCollapsedReplyQuote(z);
        return Unit.INSTANCE;
    }

    private final View switchCell(String title, String description, boolean initial, final Function1<? super Boolean, Unit> onChange) {
        final View textCheckCell = new TextCheckCell(getContext());
        final Ref.BooleanRef booleanRef = new Ref.BooleanRef();
        booleanRef.element = initial;
        textCheckCell.setTextAndValueAndCheck(title, description, booleanRef.element, true, false);
        textCheckCell.setOnClickListener(new View.OnClickListener() { // from class: ni.shikatu.re_extera.ui.settings.SpySettingsFragment.1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SpySettingsFragment.switchCell$lambda$11$lambda$10(booleanRef, textCheckCell, onChange, view);
            }
        });
        return textCheckCell;
    }

    static final void switchCell$lambda$11$lambda$10(Ref.BooleanRef booleanRef, TextCheckCell textCheckCell, Function1 function1, View view) {
        booleanRef.element = !booleanRef.element;
        textCheckCell.setChecked(booleanRef.element);
        function1.invoke(Boolean.valueOf(booleanRef.element));
    }

    /* JADX INFO: compiled from: SpySettingsFragment.kt */
    @Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0006\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u000b"}, d2 = {"Lni/shikatu/re_extera/ui/settings/SpySettingsFragment$Companion;", "", "<init>", "()V", "ID_SAVE_DELETED", "", "ID_SAVE_ONE_TIME", "ID_SAVE_EDITS", "ID_RED_MARK", "ID_CUSTOM_MARK", "ID_TRANSLUCENT", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
