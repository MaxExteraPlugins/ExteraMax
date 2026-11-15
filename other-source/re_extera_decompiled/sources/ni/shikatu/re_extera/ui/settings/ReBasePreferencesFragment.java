package ni.shikatu.re_extera.ui.settings;

import android.view.View;
import com.exteragram.messenger.preferences.BasePreferencesActivity;
import com.exteragram.messenger.preferences.utils.SettingsRegistry;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import ni.shikatu.extensions.FieldRef;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.ItemOptions;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.Components.UniversalRecyclerView;

/* JADX INFO: compiled from: ReBasePreferencesFragment.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b&\u0018\u0000 \u00142\u00020\u0001:\u0001\u0014B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000bH\u0004J,\u0010\f\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000b2\u0012\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\t0\u0011H\u0004J\u0014\u0010\u0012\u001a\u00020\u0013*\u00020\u00132\u0006\u0010\r\u001a\u00020\u000eH\u0004R\u0016\u0010\u0004\u001a\u0004\u0018\u00010\u00058DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u0015"}, d2 = {"Lni/shikatu/re_extera/ui/settings/ReBasePreferencesFragment;", "Lcom/exteragram/messenger/preferences/BasePreferencesActivity;", "<init>", "()V", "list", "Lorg/telegram/ui/Components/UniversalRecyclerView;", "getList", "()Lorg/telegram/ui/Components/UniversalRecyclerView;", "refresh", "", "animated", "", "toggle", "item", "Lorg/telegram/ui/Components/UItem;", "current", "apply", "Lkotlin/Function1;", "addCopyLink", "Lorg/telegram/ui/Components/ItemOptions;", "Companion", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public abstract class ReBasePreferencesFragment extends BasePreferencesActivity {
    private static final Companion Companion = new Companion(null);
    private static final FieldRef<UniversalRecyclerView> listViewField = new FieldRef<>(BasePreferencesActivity.class, "listView");

    protected final UniversalRecyclerView getList() {
        return listViewField.get(this);
    }

    public static /* synthetic */ void refresh$default(ReBasePreferencesFragment reBasePreferencesFragment, boolean z, int i, Object obj) {
        if (obj != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: refresh");
        }
        if ((i & 1) != 0) {
            z = true;
        }
        reBasePreferencesFragment.refresh(z);
    }

    protected final void refresh(boolean animated) {
        UniversalAdapter universalAdapter;
        UniversalRecyclerView list = getList();
        if (list == null || (universalAdapter = list.adapter) == null) {
            return;
        }
        universalAdapter.update(animated);
    }

    protected final void toggle(UItem item, boolean current, Function1<? super Boolean, Unit> apply) {
        Intrinsics.checkNotNullParameter(item, "item");
        Intrinsics.checkNotNullParameter(apply, "apply");
        boolean z = !current;
        apply.invoke(Boolean.valueOf(z));
        item.setChecked(z);
        UniversalRecyclerView list = getList();
        View viewFindViewByItemId = list != null ? list.findViewByItemId(item.id) : null;
        if (viewFindViewByItemId instanceof TextCheckCell) {
            ((TextCheckCell) viewFindViewByItemId).setChecked(z);
        } else if (viewFindViewByItemId instanceof CheckBoxCell) {
            ((CheckBoxCell) viewFindViewByItemId).setChecked(z, true);
        }
        refresh$default(this, false, 1, null);
    }

    protected final ItemOptions addCopyLink(ItemOptions itemOptions, UItem uItem) {
        Intrinsics.checkNotNullParameter(itemOptions, "<this>");
        Intrinsics.checkNotNullParameter(uItem, "item");
        final String firstSettingLink = SettingsRegistry.getInstance().getFirstSettingLink(getClass(), uItem);
        if (firstSettingLink == null) {
            return itemOptions;
        }
        ItemOptions itemOptionsAdd = itemOptions.add(R.drawable.msg_copy, LocaleController.getString(R.string.CopyLink), new Runnable() { // from class: ni.shikatu.re_extera.ui.settings.ReBasePreferencesFragment.0
            @Override // java.lang.Runnable
            public final void run() {
                ReBasePreferencesFragment.addCopyLink$lambda$0(firstSettingLink, this);
            }
        });
        Intrinsics.checkNotNullExpressionValue(itemOptionsAdd, "add(...)");
        return itemOptionsAdd;
    }

    static final void addCopyLink$lambda$0(String str, ReBasePreferencesFragment reBasePreferencesFragment) {
        if (AndroidUtilities.addToClipboard(str)) {
            BulletinFactory.of((BaseFragment) reBasePreferencesFragment).createCopyBulletin(LocaleController.getString(R.string.LinkCopied)).show();
        }
    }

    /* JADX INFO: compiled from: ReBasePreferencesFragment.kt */
    @Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b¨\u0006\t"}, d2 = {"Lni/shikatu/re_extera/ui/settings/ReBasePreferencesFragment$Companion;", "", "<init>", "()V", "listViewField", "Lni/shikatu/extensions/FieldRef;", "Lorg/telegram/ui/Components/UniversalRecyclerView;", "getListViewField", "()Lni/shikatu/extensions/FieldRef;", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final FieldRef<UniversalRecyclerView> getListViewField() {
            return ReBasePreferencesFragment.listViewField;
        }
    }
}
