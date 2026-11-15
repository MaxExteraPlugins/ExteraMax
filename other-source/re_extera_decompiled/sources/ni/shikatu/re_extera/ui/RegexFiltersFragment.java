package ni.shikatu.re_extera.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.view.View;
import android.widget.FrameLayout;
import androidx.core.content.ContextCompat;
import com.exteragram.messenger.utils.text.LocaleUtils;
import java.util.ArrayList;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import ni.shikatu.re_extera.data.RegexFiltersStore;
import ni.shikatu.re_extera.localization.Strings;
import ni.shikatu.re_extera.settings.ReSettings;
import ni.shikatu.re_extera.ui.settings.ReBasePreferencesFragment;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;

/* JADX INFO: compiled from: ListFragments.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0007\u0018\u0000 !2\u00020\u0001:\u0001!B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0014J(\u0010\n\u001a\u00020\u000b2\u0016\u0010\f\u001a\u0012\u0012\u0004\u0012\u00020\u000e0\rj\b\u0012\u0004\u0012\u00020\u000e`\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0014J0\u0010\u0012\u001a\u00020\u000b2\u0006\u0010\u0013\u001a\u00020\u000e2\u0006\u0010\u0014\u001a\u00020\u00072\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u0018H\u0014J0\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u0013\u001a\u00020\u000e2\u0006\u0010\u0014\u001a\u00020\u00072\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u0018H\u0014J\u0010\u0010\u001c\u001a\u00020\u000b2\u0006\u0010\u001d\u001a\u00020\u0005H\u0002J\u0010\u0010\u001e\u001a\u00020\u000b2\u0006\u0010\u001d\u001a\u00020\u0005H\u0002J\u0012\u0010\u001f\u001a\u00020\u000b2\b\u0010 \u001a\u0004\u0018\u00010\u0005H\u0002¨\u0006\""}, d2 = {"Lni/shikatu/re_extera/ui/RegexFiltersFragment;", "Lni/shikatu/re_extera/ui/settings/ReBasePreferencesFragment;", "<init>", "()V", "getTitle", "", "createView", "Landroid/view/View;", "context", "Landroid/content/Context;", "fillItems", "", "items", "Ljava/util/ArrayList;", "Lorg/telegram/ui/Components/UItem;", "Lkotlin/collections/ArrayList;", "adapter", "Lorg/telegram/ui/Components/UniversalAdapter;", "onClick", "item", "view", "position", "", "x", "", "y", "onLongClick", "", "copy", "filter", "confirmDelete", "showEditor", "existing", "Companion", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class RegexFiltersFragment extends ReBasePreferencesFragment {
    private static final Companion Companion = new Companion(null);

    @Deprecated
    public static final int ID_ENABLED = 1;

    @Deprecated
    public static final int ID_FILTER_BASE = 100;

    @Deprecated
    public static final int MENU_ADD = 1;

    public String getTitle() {
        return Strings.INSTANCE.getFILTERS();
    }

    protected View createView(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        View viewCreateView = super.createView(context);
        this.actionBar.createMenu().addItem(1, R.drawable.msg_add);
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: ni.shikatu.re_extera.ui.RegexFiltersFragment.createView.1
            public void onItemClick(int id) {
                if (id == -1) {
                    RegexFiltersFragment.this.finishFragment();
                } else {
                    if (id != 1) {
                        return;
                    }
                    RegexFiltersFragment.this.showEditor(null);
                }
            }
        });
        return viewCreateView;
    }

    protected void fillItems(ArrayList<UItem> items, UniversalAdapter adapter) {
        Intrinsics.checkNotNullParameter(items, "items");
        Intrinsics.checkNotNullParameter(adapter, "adapter");
        ArrayList<UItem> arrayList = items;
        arrayList.add(UItem.asCheck(1, Strings.INSTANCE.getENABLE_FILTERS()).setChecked(ReSettings.INSTANCE.getFiltersEnabled()).setLinkAlias("reExteraFiltersEnable", this));
        arrayList.add(UItem.asShadow(LocaleUtils.fullyFormatText(Strings.INSTANCE.getFILTERS_ABOUT())));
        int i = 0;
        for (Object obj : RegexFiltersStore.INSTANCE.all()) {
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            arrayList.add(UItem.asButton(i + 100, (String) obj));
            i = i2;
        }
        arrayList.add(UItem.asShadow());
    }

    protected void onClick(UItem item, View view, int position, float x, float y) {
        Activity parentActivity;
        Intrinsics.checkNotNullParameter(item, "item");
        Intrinsics.checkNotNullParameter(view, "view");
        if (item.id == 1) {
            toggle(item, ReSettings.INSTANCE.getFiltersEnabled(), new Function1() { // from class: ni.shikatu.re_extera.ui.RegexFiltersFragment.3
                public final Object invoke(Object obj) {
                    return RegexFiltersFragment.onClick$lambda$1(((Boolean) obj).booleanValue());
                }
            });
            return;
        }
        final String str = (String) CollectionsKt.getOrNull(RegexFiltersStore.INSTANCE.all(), item.id - 100);
        if (str == null || (parentActivity = getParentActivity()) == null) {
            return;
        }
        new BottomSheet.Builder(parentActivity).setTitle(str).setItems(new CharSequence[]{Strings.INSTANCE.getEDIT_FILTER(), Strings.INSTANCE.getCOPY_FILTER(), Strings.INSTANCE.getDELETE_FILTER()}, new int[]{R.drawable.floating_pencil, R.drawable.msg_copy, R.drawable.msg_delete}, new DialogInterface.OnClickListener() { // from class: ni.shikatu.re_extera.ui.RegexFiltersFragment.4
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                RegexFiltersFragment.onClick$lambda$2(RegexFiltersFragment.this, str, dialogInterface, i);
            }
        }).show();
    }

    static final Unit onClick$lambda$1(boolean z) {
        ReSettings.INSTANCE.setFiltersEnabled(z);
        return Unit.INSTANCE;
    }

    static final void onClick$lambda$2(RegexFiltersFragment regexFiltersFragment, String str, DialogInterface dialogInterface, int i) {
        if (i == 0) {
            regexFiltersFragment.showEditor(str);
        } else if (i == 1) {
            regexFiltersFragment.copy(str);
        } else {
            if (i != 2) {
                return;
            }
            regexFiltersFragment.confirmDelete(str);
        }
    }

    protected boolean onLongClick(UItem item, View view, int position, float x, float y) {
        Intrinsics.checkNotNullParameter(item, "item");
        Intrinsics.checkNotNullParameter(view, "view");
        String str = (String) CollectionsKt.getOrNull(RegexFiltersStore.INSTANCE.all(), item.id - 100);
        if (str == null) {
            return super.onLongClick(item, view, position, x, y);
        }
        confirmDelete(str);
        return true;
    }

    private final void copy(String filter) {
        if (AndroidUtilities.addToClipboard(filter)) {
            BulletinFactory.of((BaseFragment) this).createSimpleBulletin(ContextCompat.getDrawable(getContext(), R.drawable.msg_copy), LocaleController.getString(R.string.TextCopied)).show();
        }
    }

    private final void confirmDelete(final String filter) {
        Confirm confirm = Confirm.INSTANCE;
        Activity parentActivity = getParentActivity();
        if (parentActivity == null) {
            return;
        }
        String delete_filter = Strings.INSTANCE.getDELETE_FILTER();
        String delete_filter_confirm = Strings.INSTANCE.getDELETE_FILTER_CONFIRM();
        String string = LocaleController.getString(R.string.Delete);
        Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
        Confirm.show$default(confirm, parentActivity, delete_filter, delete_filter_confirm, string, false, new Function0() { // from class: ni.shikatu.re_extera.ui.RegexFiltersFragment.2
            public final Object invoke() {
                return RegexFiltersFragment.confirmDelete$lambda$3(filter, this);
            }
        }, 16, null);
    }

    static final Unit confirmDelete$lambda$3(String str, RegexFiltersFragment regexFiltersFragment) {
        RegexFiltersStore.INSTANCE.remove(str);
        ReBasePreferencesFragment.refresh$default(regexFiltersFragment, false, 1, null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void showEditor(final String existing) {
        final Activity parentActivity = getParentActivity();
        if (parentActivity == null) {
            return;
        }
        Activity activity = parentActivity;
        final View editTextBoldCursor = new EditTextBoldCursor(activity);
        editTextBoldCursor.setBackground(Theme.createEditTextDrawable(activity, false));
        editTextBoldCursor.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
        editTextBoldCursor.setHintTextColor(Theme.getColor(Theme.key_dialogTextHint));
        editTextBoldCursor.setTextSize(18.0f);
        editTextBoldCursor.setHint(Strings.INSTANCE.getFILTER());
        editTextBoldCursor.setInputType(1);
        editTextBoldCursor.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(8.0f));
        if (existing != null) {
            editTextBoldCursor.setText(existing);
            editTextBoldCursor.setSelection(existing.length());
        }
        FrameLayout frameLayout = new FrameLayout(activity);
        frameLayout.addView(editTextBoldCursor, LayoutHelper.createFrame(-1, -2.0f, 0, 24.0f, 12.0f, 24.0f, 0.0f));
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        Strings strings = Strings.INSTANCE;
        final AlertDialog alertDialogCreate = builder.setTitle(existing == null ? strings.getADD_FILTER() : strings.getEDIT_FILTER()).setView(frameLayout).setPositiveButton(LocaleController.getString(existing == null ? R.string.Add : R.string.Save), (AlertDialog.OnButtonClickListener) null).setNegativeButton(LocaleController.getString(R.string.Cancel), (AlertDialog.OnButtonClickListener) null).create();
        alertDialogCreate.setOnShowListener(new DialogInterface.OnShowListener() { // from class: ni.shikatu.re_extera.ui.RegexFiltersFragment.0
            @Override // android.content.DialogInterface.OnShowListener
            public final void onShow(DialogInterface dialogInterface) {
                RegexFiltersFragment.showEditor$lambda$6(editTextBoldCursor, dialogInterface);
            }
        });
        showDialog((Dialog) alertDialogCreate);
        View button = alertDialogCreate.getButton(-1);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() { // from class: ni.shikatu.re_extera.ui.RegexFiltersFragment.1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    RegexFiltersFragment.showEditor$lambda$7(editTextBoldCursor, existing, alertDialogCreate, this, parentActivity, view);
                }
            });
        }
    }

    static final void showEditor$lambda$6(EditTextBoldCursor editTextBoldCursor, DialogInterface dialogInterface) {
        editTextBoldCursor.requestFocus();
        AndroidUtilities.showKeyboard((View) editTextBoldCursor);
    }

    static final void showEditor$lambda$7(EditTextBoldCursor editTextBoldCursor, String str, AlertDialog alertDialog, RegexFiltersFragment regexFiltersFragment, Activity activity, View view) {
        String string;
        Editable text = editTextBoldCursor.getText();
        String string2 = (text == null || (string = text.toString()) == null) ? null : StringsKt.trim(string).toString();
        if (string2 == null) {
            string2 = "";
        }
        String strValidate = string2.length() != 0 ? RegexFiltersStore.INSTANCE.validate(string2) : "";
        if (strValidate == null) {
            RegexFiltersStore regexFiltersStore = RegexFiltersStore.INSTANCE;
            if (str == null) {
                regexFiltersStore.add(string2);
            } else {
                regexFiltersStore.update(str, string2);
            }
            alertDialog.dismiss();
            ReBasePreferencesFragment.refresh$default(regexFiltersFragment, false, 1, null);
            return;
        }
        String str2 = strValidate;
        if (str2.length() == 0) {
            AndroidUtilities.shakeView((View) editTextBoldCursor);
        } else {
            new AlertDialog.Builder(activity).setTitle(Strings.INSTANCE.getPATTERN_ERROR()).setMessage(str2).setPositiveButton(LocaleController.getString(R.string.OK), (AlertDialog.OnButtonClickListener) null).show();
        }
    }

    /* JADX INFO: compiled from: ListFragments.kt */
    @Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\b"}, d2 = {"Lni/shikatu/re_extera/ui/RegexFiltersFragment$Companion;", "", "<init>", "()V", "MENU_ADD", "", "ID_ENABLED", "ID_FILTER_BASE", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
