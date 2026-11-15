package ni.shikatu.re_extera.ui;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import ni.shikatu.re_extera.data.ShadowbanEntry;
import ni.shikatu.re_extera.localization.Strings;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.Cells.TextCheckCell;

/* JADX INFO: compiled from: Dialogs.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0004\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003JP\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\b\u0010\b\u001a\u0004\u0018\u00010\t26\u0010\n\u001a2\u0012\u0013\u0012\u00110\f¢\u0006\f\b\r\u0012\b\b\u000e\u0012\u0004\b\b(\u000f\u0012\u0013\u0012\u00110\f¢\u0006\f\b\r\u0012\b\b\u000e\u0012\u0004\b\b(\u0010\u0012\u0004\u0012\u00020\u00050\u000b¨\u0006\u0011"}, d2 = {"Lni/shikatu/re_extera/ui/ShadowbanDialog;", "", "<init>", "()V", "show", "", "context", "Landroid/content/Context;", "current", "Lni/shikatu/re_extera/data/ShadowbanEntry;", "onSave", "Lkotlin/Function2;", "", "Lkotlin/ParameterName;", "name", "hideDialog", "hideInGroups", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class ShadowbanDialog {
    public static final ShadowbanDialog INSTANCE = new ShadowbanDialog();

    private ShadowbanDialog() {
    }

    public final void show(Context context, ShadowbanEntry current, final Function2<? super Boolean, ? super Boolean, Unit> onSave) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(onSave, "onSave");
        final Ref.BooleanRef booleanRef = new Ref.BooleanRef();
        booleanRef.element = current != null ? current.getHideDialog() : true;
        final Ref.BooleanRef booleanRef2 = new Ref.BooleanRef();
        booleanRef2.element = current != null ? current.getHideInGroups() : true;
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(0, AndroidUtilities.dp(8.0f), 0, 0);
        final TextCheckCell textCheckCell = new TextCheckCell(context);
        textCheckCell.setTextAndCheck(Strings.INSTANCE.getHIDE_DIALOG(), booleanRef.element, true);
        textCheckCell.setOnClickListener(new View.OnClickListener() { // from class: ni.shikatu.re_extera.ui.ShadowbanDialog.0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ShadowbanDialog.show$lambda$2$lambda$1(booleanRef, textCheckCell, view);
            }
        });
        final TextCheckCell textCheckCell2 = new TextCheckCell(context);
        textCheckCell2.setTextAndCheck(Strings.INSTANCE.getHIDE_IN_GROUPS(), booleanRef2.element, false);
        textCheckCell2.setOnClickListener(new View.OnClickListener() { // from class: ni.shikatu.re_extera.ui.ShadowbanDialog.1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ShadowbanDialog.show$lambda$4$lambda$3(booleanRef2, textCheckCell2, view);
            }
        });
        linearLayout.addView((View) textCheckCell);
        linearLayout.addView((View) textCheckCell2);
        new AlertDialog.Builder(context).setTitle(Strings.INSTANCE.getSHADOWBAN()).setView(linearLayout).setPositiveButton(LocaleController.getString(R.string.Save), new AlertDialog.OnButtonClickListener() { // from class: ni.shikatu.re_extera.ui.ShadowbanDialog.2
            public final void onClick(AlertDialog alertDialog, int i) {
                ShadowbanDialog.show$lambda$5(onSave, booleanRef, booleanRef2, alertDialog, i);
            }
        }).setNegativeButton(LocaleController.getString(R.string.Cancel), (AlertDialog.OnButtonClickListener) null).show();
    }

    static final void show$lambda$2$lambda$1(Ref.BooleanRef booleanRef, TextCheckCell textCheckCell, View view) {
        booleanRef.element = !booleanRef.element;
        textCheckCell.setChecked(booleanRef.element);
    }

    static final void show$lambda$4$lambda$3(Ref.BooleanRef booleanRef, TextCheckCell textCheckCell, View view) {
        booleanRef.element = !booleanRef.element;
        textCheckCell.setChecked(booleanRef.element);
    }

    static final void show$lambda$5(Function2 function2, Ref.BooleanRef booleanRef, Ref.BooleanRef booleanRef2, AlertDialog alertDialog, int i) {
        function2.invoke(Boolean.valueOf(booleanRef.element), Boolean.valueOf(booleanRef2.element));
    }
}
