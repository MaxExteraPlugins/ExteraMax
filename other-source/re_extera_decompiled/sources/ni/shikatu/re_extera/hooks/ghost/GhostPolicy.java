package ni.shikatu.re_extera.hooks.ghost;

import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import ni.shikatu.re_extera.data.ExclusionKind;
import ni.shikatu.re_extera.data.ExclusionMode;
import ni.shikatu.re_extera.data.ExclusionsStore;
import ni.shikatu.re_extera.localization.Strings;
import ni.shikatu.re_extera.settings.ReSettings;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Components.BulletinFactory;

/* JADX INFO: compiled from: GhostPolicy.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0015\u0010\u0004\u001a\u00020\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\u0002\u0010\bJ\u0015\u0010\t\u001a\u00020\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\u0002\u0010\bJ'\u0010\n\u001a\u00020\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0005H\u0002¢\u0006\u0002\u0010\u000eJ\u0010\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016R\u0011\u0010\u000f\u001a\u00020\u00108F¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u0012¨\u0006\u0017"}, d2 = {"Lni/shikatu/re_extera/hooks/ghost/GhostPolicy;", "", "<init>", "()V", "blocksReading", "", "dialogId", "", "(Ljava/lang/Long;)Z", "blocksTyping", "blocks", "kind", "Lni/shikatu/re_extera/data/ExclusionKind;", "globalBlock", "(Ljava/lang/Long;Lni/shikatu/re_extera/data/ExclusionKind;Z)Z", "toggleTitle", "", "getToggleTitle", "()Ljava/lang/String;", "toggle", "", "fragment", "Lorg/telegram/ui/ActionBar/BaseFragment;", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class GhostPolicy {
    public static final GhostPolicy INSTANCE = new GhostPolicy();

    /* JADX INFO: compiled from: GhostPolicy.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    public static final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[ExclusionMode.values().length];
            try {
                iArr[ExclusionMode.NEVER.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[ExclusionMode.ALWAYS.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[ExclusionMode.DEFAULT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    private GhostPolicy() {
    }

    public final boolean blocksReading(Long dialogId) {
        return blocks(dialogId, ExclusionKind.READING, ReSettings.INSTANCE.getGhostHideReading());
    }

    public final boolean blocksTyping(Long dialogId) {
        return blocks(dialogId, ExclusionKind.TYPING, ReSettings.INSTANCE.getGhostHideTyping());
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: kotlin.NoWhenBranchMatchedException */
    private final boolean blocks(Long dialogId, ExclusionKind kind, boolean globalBlock) throws NoWhenBranchMatchedException {
        int i = WhenMappings.$EnumSwitchMapping$0[ExclusionsStore.INSTANCE.mode(dialogId, kind).ordinal()];
        if (i == 1) {
            return true;
        }
        if (i == 2) {
            return false;
        }
        if (i == 3) {
            return globalBlock;
        }
        throw new NoWhenBranchMatchedException();
    }

    public final String getToggleTitle() {
        return ReSettings.INSTANCE.getGhostEnabled() ? Strings.INSTANCE.getGHOST_MODE_DISABLE() : Strings.INSTANCE.getGHOST_MODE_ENABLE();
    }

    public final void toggle(BaseFragment fragment) {
        boolean ghostEnabled = ReSettings.INSTANCE.getGhostEnabled();
        ReSettings.INSTANCE.setGhostEnabled(!ghostEnabled);
        if (fragment == null) {
            return;
        }
        BulletinFactory.of(fragment).createSuccessBulletin(!ghostEnabled ? Strings.INSTANCE.getGHOST_MODE_ENABLED() : Strings.INSTANCE.getGHOST_MODE_DISABLED()).show();
    }
}
