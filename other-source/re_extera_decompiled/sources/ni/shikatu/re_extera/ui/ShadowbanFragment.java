package ni.shikatu.re_extera.ui;

import android.app.Activity;
import java.util.List;
import kotlin.Metadata;
import kotlin.Triple;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import ni.shikatu.re_extera.data.ShadowbanEntry;
import ni.shikatu.re_extera.data.ShadowbanStore;
import ni.shikatu.re_extera.localization.Strings;
import ni.shikatu.re_extera.utils.ChatUtils;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.BaseFragment;

/* JADX INFO: compiled from: ListFragments.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010 \n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\r\n\u0000\n\u0002\u0010\u0002\n\u0000\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007¢\u0006\u0004\b\u0003\u0010\u0004J\b\u0010\u0005\u001a\u00020\u0006H\u0016J\u000e\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00020\u000bH\u0014J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0002H\u0014J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u000e\u001a\u00020\u0002H\u0014J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u000e\u001a\u00020\u0002H\u0014R\u0014\u0010\u0007\u001a\u00020\u00068TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\t¨\u0006\u0013"}, d2 = {"Lni/shikatu/re_extera/ui/ShadowbanFragment;", "Lni/shikatu/re_extera/ui/PeerListFragment;", "Lni/shikatu/re_extera/data/ShadowbanEntry;", "<init>", "()V", "getTitle", "", "emptyText", "getEmptyText", "()Ljava/lang/String;", "load", "", "peerId", "", "entry", "status", "", "onEntryClick", "", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class ShadowbanFragment extends PeerListFragment<ShadowbanEntry> {
    public String getTitle() {
        return Strings.INSTANCE.getSHADOWBAN();
    }

    @Override // ni.shikatu.re_extera.ui.PeerListFragment
    protected String getEmptyText() {
        return Strings.INSTANCE.getNO_SHADOWBANNED();
    }

    @Override // ni.shikatu.re_extera.ui.PeerListFragment
    protected List<ShadowbanEntry> load() {
        return ShadowbanStore.INSTANCE.all();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // ni.shikatu.re_extera.ui.PeerListFragment
    public long peerId(ShadowbanEntry entry) {
        Intrinsics.checkNotNullParameter(entry, "entry");
        return entry.getUserId();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // ni.shikatu.re_extera.ui.PeerListFragment
    public CharSequence status(ShadowbanEntry entry) {
        Intrinsics.checkNotNullParameter(entry, "entry");
        List listCreateListBuilder = CollectionsKt.createListBuilder();
        if (entry.getHideDialog()) {
            listCreateListBuilder.add(Strings.INSTANCE.getHIDE_DIALOG());
        }
        if (entry.getHideInGroups()) {
            listCreateListBuilder.add(Strings.INSTANCE.getHIDE_IN_GROUPS());
        }
        return CollectionsKt.joinToString$default(CollectionsKt.build(listCreateListBuilder), ", ", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 62, (Object) null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // ni.shikatu.re_extera.ui.PeerListFragment
    public void onEntryClick(final ShadowbanEntry entry) {
        Intrinsics.checkNotNullParameter(entry, "entry");
        final Activity parentActivity = getParentActivity();
        if (parentActivity == null) {
            return;
        }
        showActions(title(entry.getUserId()), CollectionsKt.listOf(new Triple[]{new Triple(Strings.INSTANCE.getOPEN_CHAT(), Integer.valueOf(R.drawable.msg_openprofile), new Function0() { // from class: ni.shikatu.re_extera.ui.ShadowbanFragment.1
            public final Object invoke() {
                return ShadowbanFragment.onEntryClick$lambda$1(ShadowbanFragment.this, entry);
            }
        }), new Triple(Strings.INSTANCE.getEDIT_SHADOWBAN(), Integer.valueOf(R.drawable.floating_pencil), new Function0() { // from class: ni.shikatu.re_extera.ui.ShadowbanFragment.2
            public final Object invoke() {
                return ShadowbanFragment.onEntryClick$lambda$3(parentActivity, entry, this);
            }
        }), new Triple(Strings.INSTANCE.getREMOVE_FROM_SHADOWBAN(), Integer.valueOf(R.drawable.msg_delete), new Function0() { // from class: ni.shikatu.re_extera.ui.ShadowbanFragment.3
            public final Object invoke() {
                return ShadowbanFragment.onEntryClick$lambda$5(parentActivity, entry, this);
            }
        })}));
    }

    static final Unit onEntryClick$lambda$1(ShadowbanFragment shadowbanFragment, ShadowbanEntry shadowbanEntry) {
        ChatUtils.INSTANCE.openChat((BaseFragment) shadowbanFragment, shadowbanEntry.getUserId());
        return Unit.INSTANCE;
    }

    static final Unit onEntryClick$lambda$3(Activity activity, final ShadowbanEntry shadowbanEntry, final ShadowbanFragment shadowbanFragment) {
        ShadowbanDialog.INSTANCE.show(activity, shadowbanEntry, new Function2() { // from class: ni.shikatu.re_extera.ui.ShadowbanFragment.0
            public final Object invoke(Object obj, Object obj2) {
                return ShadowbanFragment.onEntryClick$lambda$3$lambda$2(shadowbanEntry, shadowbanFragment, ((Boolean) obj).booleanValue(), ((Boolean) obj2).booleanValue());
            }
        });
        return Unit.INSTANCE;
    }

    static final Unit onEntryClick$lambda$3$lambda$2(ShadowbanEntry shadowbanEntry, ShadowbanFragment shadowbanFragment, boolean z, boolean z2) {
        ShadowbanStore.INSTANCE.put(shadowbanEntry.getUserId(), z, z2);
        ShadowbanStore.INSTANCE.notifyDialogsChanged(shadowbanFragment.currentAccount);
        shadowbanFragment.reload();
        return Unit.INSTANCE;
    }

    static final Unit onEntryClick$lambda$5(Activity activity, final ShadowbanEntry shadowbanEntry, final ShadowbanFragment shadowbanFragment) {
        Confirm confirm = Confirm.INSTANCE;
        Activity activity2 = activity;
        String remove_from_shadowban = Strings.INSTANCE.getREMOVE_FROM_SHADOWBAN();
        String string = LocaleController.getString(R.string.OK);
        Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
        Confirm.show$default(confirm, activity2, remove_from_shadowban, null, string, false, new Function0() { // from class: ni.shikatu.re_extera.ui.ShadowbanFragment.4
            public final Object invoke() {
                return ShadowbanFragment.onEntryClick$lambda$5$lambda$4(shadowbanEntry, shadowbanFragment);
            }
        }, 16, null);
        return Unit.INSTANCE;
    }

    static final Unit onEntryClick$lambda$5$lambda$4(ShadowbanEntry shadowbanEntry, ShadowbanFragment shadowbanFragment) {
        ShadowbanStore.INSTANCE.remove(shadowbanEntry.getUserId());
        ShadowbanStore.INSTANCE.notifyDialogsChanged(shadowbanFragment.currentAccount);
        shadowbanFragment.reload();
        return Unit.INSTANCE;
    }
}
