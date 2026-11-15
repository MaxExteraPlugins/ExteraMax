package ni.shikatu.re_extera.ui;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.FunctionReferenceImpl;

/* JADX INFO: compiled from: ListFragments.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
final /* synthetic */ class ExclusionsFragment$onEntryClick$2$1 extends FunctionReferenceImpl implements Function0<Unit> {
    ExclusionsFragment$onEntryClick$2$1(Object obj) {
        super(0, obj, ExclusionsFragment.class, "reload", "reload()V", 0);
    }

    public /* bridge */ /* synthetic */ Object invoke() {
        m2invoke();
        return Unit.INSTANCE;
    }

    /* JADX INFO: renamed from: invoke, reason: collision with other method in class */
    public final void m2invoke() {
        ((ExclusionsFragment) this.receiver).reload();
    }
}
