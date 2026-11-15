package ni.shikatu.re_extera.ui;

import android.app.Activity;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Triple;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import ni.shikatu.re_extera.data.DialogExclusion;
import ni.shikatu.re_extera.data.ExclusionKind;
import ni.shikatu.re_extera.data.ExclusionMode;
import ni.shikatu.re_extera.data.ExclusionsStore;
import ni.shikatu.re_extera.localization.Strings;
import ni.shikatu.re_extera.utils.ChatUtils;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.BaseFragment;

/* JADX INFO: compiled from: ListFragments.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010 \n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\r\n\u0000\n\u0002\u0010\u0002\n\u0000\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007¢\u0006\u0004\b\u0003\u0010\u0004J\b\u0010\u0005\u001a\u00020\u0006H\u0016J\u000e\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00020\u000bH\u0014J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0002H\u0014J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u000e\u001a\u00020\u0002H\u0014J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u000e\u001a\u00020\u0002H\u0014R\u0014\u0010\u0007\u001a\u00020\u00068TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\t¨\u0006\u0013"}, d2 = {"Lni/shikatu/re_extera/ui/ExclusionsFragment;", "Lni/shikatu/re_extera/ui/PeerListFragment;", "Lni/shikatu/re_extera/data/DialogExclusion;", "<init>", "()V", "getTitle", "", "emptyText", "getEmptyText", "()Ljava/lang/String;", "load", "", "peerId", "", "entry", "status", "", "onEntryClick", "", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class ExclusionsFragment extends PeerListFragment<DialogExclusion> {
    public String getTitle() {
        return Strings.INSTANCE.getEXCLUSIONS();
    }

    @Override // ni.shikatu.re_extera.ui.PeerListFragment
    protected String getEmptyText() {
        return Strings.INSTANCE.getNO_EXCLUSIONS();
    }

    @Override // ni.shikatu.re_extera.ui.PeerListFragment
    protected List<DialogExclusion> load() {
        return CollectionsKt.sortedWith(ExclusionsStore.INSTANCE.all(), new Comparator() { // from class: ni.shikatu.re_extera.ui.ExclusionsFragment$load$$inlined$sortedBy$1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                String lowerCase = this.this$0.title(((DialogExclusion) t).getDialogId()).toLowerCase(Locale.ROOT);
                Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
                String lowerCase2 = this.this$0.title(((DialogExclusion) t2).getDialogId()).toLowerCase(Locale.ROOT);
                Intrinsics.checkNotNullExpressionValue(lowerCase2, "toLowerCase(...)");
                return ComparisonsKt.compareValues(lowerCase, lowerCase2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // ni.shikatu.re_extera.ui.PeerListFragment
    public long peerId(DialogExclusion entry) {
        Intrinsics.checkNotNullParameter(entry, "entry");
        return entry.getDialogId();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX INFO: Thrown type has an unknown type hierarchy: kotlin.NoWhenBranchMatchedException */
    @Override // ni.shikatu.re_extera.ui.PeerListFragment
    public CharSequence status(DialogExclusion entry) throws NoWhenBranchMatchedException {
        Intrinsics.checkNotNullParameter(entry, "entry");
        List listCreateListBuilder = CollectionsKt.createListBuilder();
        if (entry.getReading() != ExclusionMode.DEFAULT) {
            listCreateListBuilder.add(Strings.INSTANCE.getREAD_PREFIX() + ExclusionDialog.INSTANCE.label(entry.getReading()));
        }
        if (entry.getTyping() != ExclusionMode.DEFAULT) {
            listCreateListBuilder.add(Strings.INSTANCE.getTYPE_PREFIX() + ExclusionDialog.INSTANCE.label(entry.getTyping()));
        }
        return CollectionsKt.joinToString$default(CollectionsKt.build(listCreateListBuilder), ", ", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 62, (Object) null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // ni.shikatu.re_extera.ui.PeerListFragment
    public void onEntryClick(final DialogExclusion entry) {
        Intrinsics.checkNotNullParameter(entry, "entry");
        final Activity parentActivity = getParentActivity();
        if (parentActivity == null) {
            return;
        }
        showActions(title(entry.getDialogId()), CollectionsKt.listOf(new Triple[]{new Triple(Strings.INSTANCE.getOPEN_CHAT(), Integer.valueOf(R.drawable.msg_openprofile), new Function0() { // from class: ni.shikatu.re_extera.ui.ExclusionsFragment.0
            public final Object invoke() {
                return ExclusionsFragment.onEntryClick$lambda$2(ExclusionsFragment.this, entry);
            }
        }), new Triple(Strings.INSTANCE.getEDIT_READING(), Integer.valueOf(R.drawable.msg_archive_hide), new Function0() { // from class: ni.shikatu.re_extera.ui.ExclusionsFragment.1
            public final Object invoke() {
                return ExclusionsFragment.onEntryClick$lambda$3(parentActivity, entry, this);
            }
        }), new Triple(Strings.INSTANCE.getEDIT_TYPING(), Integer.valueOf(R.drawable.floating_pencil), new Function0() { // from class: ni.shikatu.re_extera.ui.ExclusionsFragment.2
            public final Object invoke() {
                return ExclusionsFragment.onEntryClick$lambda$4(parentActivity, entry, this);
            }
        }), new Triple(Strings.INSTANCE.getDELETE_FROM_EXCLUSIONS(), Integer.valueOf(R.drawable.msg_delete), new Function0() { // from class: ni.shikatu.re_extera.ui.ExclusionsFragment.3
            public final Object invoke() {
                return ExclusionsFragment.onEntryClick$lambda$6(parentActivity, entry, this);
            }
        })}));
    }

    static final Unit onEntryClick$lambda$2(ExclusionsFragment exclusionsFragment, DialogExclusion dialogExclusion) {
        ChatUtils.INSTANCE.openChat((BaseFragment) exclusionsFragment, dialogExclusion.getDialogId());
        return Unit.INSTANCE;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: kotlin.NoWhenBranchMatchedException */
    static final Unit onEntryClick$lambda$3(Activity activity, DialogExclusion dialogExclusion, ExclusionsFragment exclusionsFragment) throws NoWhenBranchMatchedException {
        ExclusionDialog.INSTANCE.show(activity, dialogExclusion.getDialogId(), ExclusionKind.READING, new ExclusionsFragment$onEntryClick$2$1(exclusionsFragment));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: kotlin.NoWhenBranchMatchedException */
    static final Unit onEntryClick$lambda$4(Activity activity, DialogExclusion dialogExclusion, ExclusionsFragment exclusionsFragment) throws NoWhenBranchMatchedException {
        ExclusionDialog.INSTANCE.show(activity, dialogExclusion.getDialogId(), ExclusionKind.TYPING, new ExclusionsFragment$onEntryClick$3$1(exclusionsFragment));
        return Unit.INSTANCE;
    }

    static final Unit onEntryClick$lambda$6(Activity activity, final DialogExclusion dialogExclusion, final ExclusionsFragment exclusionsFragment) {
        Confirm confirm = Confirm.INSTANCE;
        Activity activity2 = activity;
        String delete_from_exclusions = Strings.INSTANCE.getDELETE_FROM_EXCLUSIONS();
        String string = LocaleController.getString(R.string.Delete);
        Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
        Confirm.show$default(confirm, activity2, delete_from_exclusions, null, string, false, new Function0() { // from class: ni.shikatu.re_extera.ui.ExclusionsFragment.4
            public final Object invoke() {
                return ExclusionsFragment.onEntryClick$lambda$6$lambda$5(dialogExclusion, exclusionsFragment);
            }
        }, 16, null);
        return Unit.INSTANCE;
    }

    static final Unit onEntryClick$lambda$6$lambda$5(DialogExclusion dialogExclusion, ExclusionsFragment exclusionsFragment) {
        ExclusionsStore.INSTANCE.remove(dialogExclusion.getDialogId());
        exclusionsFragment.reload();
        return Unit.INSTANCE;
    }
}
