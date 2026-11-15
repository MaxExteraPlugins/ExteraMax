package ni.shikatu.re_extera.ui;

import android.content.Context;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.AlertDialog;

/* JADX INFO: compiled from: Dialogs.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J@\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\b\u0010\n\u001a\u0004\u0018\u00010\t2\u0006\u0010\u000b\u001a\u00020\t2\b\b\u0002\u0010\f\u001a\u00020\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00050\u000f¨\u0006\u0010"}, d2 = {"Lni/shikatu/re_extera/ui/Confirm;", "", "<init>", "()V", "show", "", "context", "Landroid/content/Context;", "title", "", "message", "positive", "destructive", "", "onConfirm", "Lkotlin/Function0;", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Confirm {
    public static final Confirm INSTANCE = new Confirm();

    private Confirm() {
    }

    public static /* synthetic */ void show$default(Confirm confirm, Context context, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, boolean z, Function0 function0, int i, Object obj) {
        if ((i & 16) != 0) {
            z = true;
        }
        confirm.show(context, charSequence, charSequence2, charSequence3, z, function0);
    }

    public final void show(Context context, CharSequence title, CharSequence message, CharSequence positive, boolean destructive, final Function0<Unit> onConfirm) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(title, "title");
        Intrinsics.checkNotNullParameter(positive, "positive");
        Intrinsics.checkNotNullParameter(onConfirm, "onConfirm");
        AlertDialog.Builder title2 = new AlertDialog.Builder(context).setTitle(title);
        if (message != null) {
            title2.setMessage(message);
        }
        AlertDialog alertDialogCreate = title2.setPositiveButton(positive, new AlertDialog.OnButtonClickListener() { // from class: ni.shikatu.re_extera.ui.Confirm.0
            public final void onClick(AlertDialog alertDialog, int i) {
                onConfirm.invoke();
            }
        }).setNegativeButton(LocaleController.getString(R.string.Cancel), (AlertDialog.OnButtonClickListener) null).create();
        alertDialogCreate.show();
        if (destructive) {
            alertDialogCreate.redPositive();
        }
    }
}
