package ni.shikatu.re_extera.ui.settings;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.method.MovementMethod;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.exteragram.messenger.utils.text.LocaleUtils;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import ni.shikatu.re_extera.Main;
import ni.shikatu.re_extera.localization.Strings;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.StickerImageView;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;

/* JADX INFO: compiled from: RootSettingsFragment.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 #2\u00020\u0001:\u0001#B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u0004\u001a\u00020\u0005H\u0016J(\u0010\u0006\u001a\u00020\u00072\u0016\u0010\b\u001a\u0012\u0012\u0004\u0012\u00020\n0\tj\b\u0012\u0004\u0012\u00020\n`\u000b2\u0006\u0010\f\u001a\u00020\rH\u0014J0\u0010\u000e\u001a\u00020\u00072\u0006\u0010\u000f\u001a\u00020\n2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0015H\u0014J0\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u000f\u001a\u00020\n2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0015H\u0014J\b\u0010\u0019\u001a\u00020\u0011H\u0002J\b\u0010\u001a\u001a\u00020\u0011H\u0002J\u0018\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u0015H\u0002J\u0012\u0010 \u001a\u0004\u0018\u00010!2\u0006\u0010\"\u001a\u00020\u0013H\u0002¨\u0006$"}, d2 = {"Lni/shikatu/re_extera/ui/settings/RootSettingsFragment;", "Lni/shikatu/re_extera/ui/settings/ReBasePreferencesFragment;", "<init>", "()V", "getTitle", "", "fillItems", "", "items", "Ljava/util/ArrayList;", "Lorg/telegram/ui/Components/UItem;", "Lkotlin/collections/ArrayList;", "adapter", "Lorg/telegram/ui/Components/UniversalAdapter;", "onClick", "item", "view", "Landroid/view/View;", "position", "", "x", "", "y", "onLongClick", "", "createSticker", "createHeader", "headerText", "Landroid/widget/TextView;", "text", "", "size", "icon", "Landroid/graphics/drawable/Drawable;", "res", "Companion", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class RootSettingsFragment extends ReBasePreferencesFragment {
    private static final Companion Companion = new Companion(null);

    @Deprecated
    public static final int ID_GHOST = 3;

    @Deprecated
    public static final int ID_HEADER = 2;

    @Deprecated
    public static final int ID_OTHER = 5;

    @Deprecated
    public static final int ID_SPY = 4;

    @Deprecated
    public static final int ID_STICKER = 1;

    protected boolean onLongClick(UItem item, View view, int position, float x, float y) {
        Intrinsics.checkNotNullParameter(item, "item");
        Intrinsics.checkNotNullParameter(view, "view");
        return false;
    }

    public String getTitle() {
        return "re:extera";
    }

    protected void fillItems(ArrayList<UItem> items, UniversalAdapter adapter) {
        Intrinsics.checkNotNullParameter(items, "items");
        Intrinsics.checkNotNullParameter(adapter, "adapter");
        ArrayList<UItem> arrayList = items;
        arrayList.add(UItem.asCustom(1, createSticker()).setTransparent(true));
        arrayList.add(UItem.asCustom(2, createHeader()).setTransparent(true));
        arrayList.add(UItem.asShadow());
        arrayList.add(UItem.asButton(3, icon(R.drawable.ghost), Strings.INSTANCE.getGHOST_MODE()));
        arrayList.add(UItem.asButton(4, icon(R.drawable.menu_hide_gift), Strings.INSTANCE.getSPY()));
        arrayList.add(UItem.asButton(5, icon(R.drawable.msg_list), Strings.INSTANCE.getOTHER()));
        Main companion = Main.INSTANCE.getInstance();
        List<String> hookFailures = companion != null ? companion.getHookFailures() : null;
        if (hookFailures == null) {
            hookFailures = CollectionsKt.emptyList();
        }
        if (!hookFailures.isEmpty()) {
            arrayList.add(UItem.asShadow(Strings.INSTANCE.getHOOKS_FAILED() + "\n" + CollectionsKt.joinToString$default(hookFailures, "\n", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, new Function1() { // from class: ni.shikatu.re_extera.ui.settings.RootSettingsFragment.0
                public final Object invoke(Object obj) {
                    return RootSettingsFragment.fillItems$lambda$0((String) obj);
                }
            }, 30, (Object) null)));
        }
        arrayList.add(UItem.asShadow(LocaleUtils.fullyFormatText("**" + Strings.INSTANCE.getVERSION() + ": 3.0.2**")));
    }

    static final CharSequence fillItems$lambda$0(String str) {
        Intrinsics.checkNotNullParameter(str, "it");
        return "• " + str;
    }

    protected void onClick(UItem item, View view, int position, float x, float y) {
        Intrinsics.checkNotNullParameter(item, "item");
        Intrinsics.checkNotNullParameter(view, "view");
        int i = item.id;
        if (i == 3) {
            presentFragment((BaseFragment) new GhostSettingsFragment());
        } else if (i == 4) {
            presentFragment((BaseFragment) new SpySettingsFragment());
        } else {
            if (i != 5) {
                return;
            }
            presentFragment((BaseFragment) new OtherSettingsFragment());
        }
    }

    private final View createSticker() {
        FrameLayout frameLayout = new FrameLayout(getContext());
        StickerImageView stickerImageView = new StickerImageView(frameLayout.getContext(), this.currentAccount);
        stickerImageView.setStickerPackName("fuki_dum_pjsk_pack");
        stickerImageView.setStickerNum(3);
        stickerImageView.setAspectFit(true);
        frameLayout.addView((View) stickerImageView, new FrameLayout.LayoutParams(AndroidUtilities.dp(130.0f), AndroidUtilities.dp(130.0f), 17));
        frameLayout.setMinimumHeight(AndroidUtilities.dp(150.0f));
        return frameLayout;
    }

    private final View createHeader() {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(1);
        linearLayout.setPadding(0, 0, 0, AndroidUtilities.dp(8.0f));
        linearLayout.addView(headerText("re:extera", 18.0f), LayoutHelper.createLinear(-1, -2));
        CharSequence withUsernames = LocaleUtils.formatWithUsernames(Strings.INSTANCE.getTHANKS(), (BaseFragment) this);
        Intrinsics.checkNotNullExpressionValue(withUsernames, "formatWithUsernames(...)");
        TextView textViewHeaderText = headerText(withUsernames, 12.0f);
        textViewHeaderText.setMovementMethod((MovementMethod) new AndroidUtilities.LinkMovementMethodMy());
        textViewHeaderText.setHighlightColor(getThemedColor(Theme.key_windowBackgroundWhiteLinkSelection));
        linearLayout.addView(textViewHeaderText, LayoutHelper.createLinear(-1, -2));
        return linearLayout;
    }

    private final TextView headerText(CharSequence text, float size) {
        TextView textView = new TextView(getContext());
        textView.setGravity(17);
        textView.setTextSize(size);
        textView.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
        textView.setLinkTextColor(getThemedColor(Theme.key_windowBackgroundWhiteLinkText));
        textView.setText(text);
        return textView;
    }

    private final Drawable icon(int res) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), res);
        if (drawable == null) {
            return null;
        }
        int iDp = AndroidUtilities.dp(28.0f);
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(iDp, iDp, Bitmap.Config.ARGB_8888);
        Intrinsics.checkNotNullExpressionValue(bitmapCreateBitmap, "createBitmap(...)");
        drawable.setBounds(0, 0, iDp, iDp);
        drawable.draw(new Canvas(bitmapCreateBitmap));
        return new BitmapDrawable(getContext().getResources(), bitmapCreateBitmap);
    }

    /* JADX INFO: compiled from: RootSettingsFragment.kt */
    @Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\n"}, d2 = {"Lni/shikatu/re_extera/ui/settings/RootSettingsFragment$Companion;", "", "<init>", "()V", "ID_STICKER", "", "ID_HEADER", "ID_GHOST", "ID_SPY", "ID_OTHER", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
