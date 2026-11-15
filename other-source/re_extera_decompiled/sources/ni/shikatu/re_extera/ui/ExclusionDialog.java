package ni.shikatu.re_extera.ui;

import android.content.Context;
import com.exteragram.messenger.utils.ui.PopupUtils;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import ni.shikatu.re_extera.data.ExclusionKind;
import ni.shikatu.re_extera.data.ExclusionMode;
import ni.shikatu.re_extera.data.ExclusionsStore;
import ni.shikatu.re_extera.localization.Strings;

/* JADX INFO: compiled from: Dialogs.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J0\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0010\b\u0002\u0010\u0010\u001a\n\u0012\u0004\u0012\u00020\t\u0018\u00010\u0011J\u000e\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0006R\u0016\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0007¨\u0006\u0015"}, d2 = {"Lni/shikatu/re_extera/ui/ExclusionDialog;", "", "<init>", "()V", "ORDER", "", "Lni/shikatu/re_extera/data/ExclusionMode;", "[Lni/shikatu/re_extera/data/ExclusionMode;", "show", "", "context", "Landroid/content/Context;", "dialogId", "", "kind", "Lni/shikatu/re_extera/data/ExclusionKind;", "onChanged", "Lkotlin/Function0;", "label", "", "mode", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class ExclusionDialog {
    public static final ExclusionDialog INSTANCE = new ExclusionDialog();
    private static final ExclusionMode[] ORDER = {ExclusionMode.ALWAYS, ExclusionMode.NEVER, ExclusionMode.DEFAULT};

    /* JADX INFO: compiled from: Dialogs.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    public static final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;
        public static final /* synthetic */ int[] $EnumSwitchMapping$1;

        static {
            int[] iArr = new int[ExclusionKind.values().length];
            try {
                iArr[ExclusionKind.READING.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[ExclusionKind.TYPING.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            $EnumSwitchMapping$0 = iArr;
            int[] iArr2 = new int[ExclusionMode.values().length];
            try {
                iArr2[ExclusionMode.ALWAYS.ordinal()] = 1;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                iArr2[ExclusionMode.NEVER.ordinal()] = 2;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                iArr2[ExclusionMode.DEFAULT.ordinal()] = 3;
            } catch (NoSuchFieldError unused5) {
            }
            $EnumSwitchMapping$1 = iArr2;
        }
    }

    private ExclusionDialog() {
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: kotlin.NoWhenBranchMatchedException */
    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ void show$default(ExclusionDialog exclusionDialog, Context context, long j, ExclusionKind exclusionKind, Function0 function0, int i, Object obj) throws NoWhenBranchMatchedException {
        if ((i & 8) != 0) {
            function0 = null;
        }
        exclusionDialog.show(context, j, exclusionKind, function0);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: kotlin.NoWhenBranchMatchedException */
    public final void show(Context context, final long dialogId, final ExclusionKind kind, final Function0<Unit> onChanged) throws NoWhenBranchMatchedException {
        CharSequence[] charSequenceArr;
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(kind, "kind");
        int i = WhenMappings.$EnumSwitchMapping$0[kind.ordinal()];
        if (i == 1) {
            charSequenceArr = new CharSequence[]{Strings.INSTANCE.getALWAYS_READ(), Strings.INSTANCE.getNEVER_READ(), Strings.INSTANCE.getBASED_ON_GLOBAL()};
        } else {
            if (i != 2) {
                throw new NoWhenBranchMatchedException();
            }
            charSequenceArr = new CharSequence[]{Strings.INSTANCE.getALWAYS_TYPE(), Strings.INSTANCE.getNEVER_TYPE(), Strings.INSTANCE.getBASED_ON_GLOBAL()};
        }
        PopupUtils.showDialog(charSequenceArr, kind == ExclusionKind.READING ? Strings.INSTANCE.getREADING_EXCLUSION() : Strings.INSTANCE.getTYPING_EXCLUSION(), ArraysKt.indexOf(ORDER, ExclusionsStore.INSTANCE.get(dialogId).get(kind)), context, new PopupUtils.OnItemClickListener() { // from class: ni.shikatu.re_extera.ui.ExclusionDialog.0
            /* JADX INFO: Thrown type has an unknown type hierarchy: kotlin.NoWhenBranchMatchedException */
            public final void onClick(int i2) throws NoWhenBranchMatchedException {
                ExclusionDialog.show$lambda$0(dialogId, kind, onChanged, i2);
            }
        });
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: kotlin.NoWhenBranchMatchedException */
    static final void show$lambda$0(long j, ExclusionKind exclusionKind, Function0 function0, int i) throws NoWhenBranchMatchedException {
        ExclusionsStore.INSTANCE.set(j, exclusionKind, ORDER[i]);
        if (function0 != null) {
            function0.invoke();
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: kotlin.NoWhenBranchMatchedException */
    public final String label(ExclusionMode mode) throws NoWhenBranchMatchedException {
        Intrinsics.checkNotNullParameter(mode, "mode");
        int i = WhenMappings.$EnumSwitchMapping$1[mode.ordinal()];
        if (i == 1) {
            return Strings.INSTANCE.getALWAYS();
        }
        if (i == 2) {
            return Strings.INSTANCE.getNEVER();
        }
        if (i != 3) {
            throw new NoWhenBranchMatchedException();
        }
        return Strings.INSTANCE.getBASED_ON_GLOBAL();
    }
}
