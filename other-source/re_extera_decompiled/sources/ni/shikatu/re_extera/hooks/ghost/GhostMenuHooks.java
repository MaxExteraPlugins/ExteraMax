package ni.shikatu.re_extera.hooks.ghost;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import com.exteragram.messenger.drawer.DrawerMenuItemView;
import com.exteragram.messenger.drawer.DrawerMenuView;
import com.exteragram.messenger.utils.chats.MainMenuHelper;
import de.robv.android.xposed.XC_MethodHook;
import java.util.function.IntPredicate;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import ni.shikatu.extensions.FieldRef;
import ni.shikatu.extensions.HookUtilsKt;
import ni.shikatu.re_extera.hooks.HookerClass;
import ni.shikatu.re_extera.settings.ReSettings;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.ItemOptions;

/* JADX INFO: compiled from: GhostMenuHooks.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \u00122\u00020\u0001:\u0001\u0012B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\t\u001a\u00020\nH\u0014J\"\u0010\u000b\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0002R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0013"}, d2 = {"Lni/shikatu/re_extera/hooks/ghost/GhostMenuHooks;", "Lni/shikatu/re_extera/hooks/HookerClass;", "<init>", "()V", "drawerContainer", "Lni/shikatu/extensions/FieldRef;", "Landroid/widget/LinearLayout;", "drawerOnItemClick", "Ljava/lang/Runnable;", "setup", "", "addDrawerItem", "drawer", "Lcom/exteragram/messenger/drawer/DrawerMenuView;", "account", "", "fragment", "Lorg/telegram/ui/ActionBar/BaseFragment;", "Companion", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class GhostMenuHooks extends HookerClass {
    private static final Companion Companion = new Companion(null);

    @Deprecated
    public static final int GHOST_ITEM_ID = 910001;
    private final FieldRef<LinearLayout> drawerContainer = new FieldRef<>(DrawerMenuView.class, "container");
    private final FieldRef<Runnable> drawerOnItemClick = new FieldRef<>(DrawerMenuView.class, "onItemClick");

    @Override // ni.shikatu.re_extera.hooks.HookerClass
    protected void setup() {
        hookAfter(DrawerMenuView.class, "rebuildMenu", new Class[]{Integer.TYPE, BaseFragment.class}, new Function1() { // from class: ni.shikatu.re_extera.hooks.ghost.GhostMenuHooks.1
            public final Object invoke(Object obj) {
                return GhostMenuHooks.setup$lambda$0(GhostMenuHooks.this, (XC_MethodHook.MethodHookParam) obj);
            }
        });
        hookAfter(MainMenuHelper.class, "addConfiguredItemOptions", new Class[]{ItemOptions.class, MainMenuHelper.MenuContext.class, IntPredicate.class}, new Function1() { // from class: ni.shikatu.re_extera.hooks.ghost.GhostMenuHooks.2
            public final Object invoke(Object obj) {
                return GhostMenuHooks.setup$lambda$2((XC_MethodHook.MethodHookParam) obj);
            }
        });
    }

    static final Unit setup$lambda$0(GhostMenuHooks ghostMenuHooks, XC_MethodHook.MethodHookParam methodHookParam) {
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        if (ReSettings.INSTANCE.getGhostMenuButton()) {
            ghostMenuHooks.addDrawerItem((DrawerMenuView) HookUtilsKt.self(methodHookParam), ((Number) HookUtilsKt.arg(methodHookParam, 0)).intValue(), (BaseFragment) HookUtilsKt.arg(methodHookParam, 1));
        }
        return Unit.INSTANCE;
    }

    static final Unit setup$lambda$2(XC_MethodHook.MethodHookParam methodHookParam) {
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        if (!ReSettings.INSTANCE.getGhostMenuButton()) {
            return Unit.INSTANCE;
        }
        final ItemOptions itemOptions = (ItemOptions) HookUtilsKt.arg(methodHookParam, 0);
        MainMenuHelper.MenuContext menuContext = (MainMenuHelper.MenuContext) HookUtilsKt.arg(methodHookParam, 1);
        final BaseFragment baseFragmentFragment = menuContext != null ? menuContext.fragment() : null;
        itemOptions.addGap();
        itemOptions.add(R.drawable.ghost, GhostPolicy.INSTANCE.getToggleTitle(), new Runnable() { // from class: ni.shikatu.re_extera.hooks.ghost.GhostMenuHooks.0
            @Override // java.lang.Runnable
            public final void run() {
                GhostMenuHooks.setup$lambda$2$lambda$1(itemOptions, baseFragmentFragment);
            }
        });
        return Unit.INSTANCE;
    }

    static final void setup$lambda$2$lambda$1(ItemOptions itemOptions, BaseFragment baseFragment) {
        itemOptions.dismiss();
        GhostPolicy.INSTANCE.toggle(baseFragment);
    }

    private final void addDrawerItem(final DrawerMenuView drawer, final int account, final BaseFragment fragment) {
        LinearLayout linearLayout = this.drawerContainer.get(drawer);
        if (linearLayout == null) {
            return;
        }
        Context context = drawer.getContext();
        if (linearLayout.getChildCount() > 0) {
            View view = new View(context);
            view.setBackgroundColor(Theme.getDividerColor((Theme.ResourcesProvider) null));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, 1);
            layoutParams.setMargins(AndroidUtilities.dp(12.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(12.0f), AndroidUtilities.dp(8.0f));
            Unit unit = Unit.INSTANCE;
            linearLayout.addView(view, layoutParams);
        }
        final DrawerMenuItemView drawerMenuItemView = new DrawerMenuItemView(context);
        drawerMenuItemView.setMenuItem(GHOST_ITEM_ID, account, R.drawable.ghost, GhostPolicy.INSTANCE.getToggleTitle());
        drawerMenuItemView.setOnClickListener(new View.OnClickListener() { // from class: ni.shikatu.re_extera.hooks.ghost.GhostMenuHooks.3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                GhostMenuHooks.addDrawerItem$lambda$5(GhostMenuHooks.this, drawer, fragment, drawerMenuItemView, account, view2);
            }
        });
        linearLayout.addView((View) drawerMenuItemView);
    }

    static final void addDrawerItem$lambda$5(GhostMenuHooks ghostMenuHooks, DrawerMenuView drawerMenuView, BaseFragment baseFragment, DrawerMenuItemView drawerMenuItemView, int i, View view) {
        Runnable runnable = ghostMenuHooks.drawerOnItemClick.get(drawerMenuView);
        if (runnable != null) {
            runnable.run();
        }
        GhostPolicy.INSTANCE.toggle(baseFragment);
        drawerMenuItemView.setMenuItem(GHOST_ITEM_ID, i, R.drawable.ghost, GhostPolicy.INSTANCE.getToggleTitle());
    }

    /* JADX INFO: compiled from: GhostMenuHooks.kt */
    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0006"}, d2 = {"Lni/shikatu/re_extera/hooks/ghost/GhostMenuHooks$Companion;", "", "<init>", "()V", "GHOST_ITEM_ID", "", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
