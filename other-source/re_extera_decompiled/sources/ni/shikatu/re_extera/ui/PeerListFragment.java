package ni.shikatu.re_extera.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Triple;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import ni.shikatu.re_extera.localization.Strings;
import ni.shikatu.re_extera.ui.settings.ReBasePreferencesFragment;
import ni.shikatu.re_extera.utils.ChatUtils;
import org.telegram.messenger.DialogObject;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ProfileSearchCell;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;

/* JADX INFO: compiled from: ListFragments.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000r\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\r\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b&\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002B\u0007¢\u0006\u0004\b\u0003\u0010\u0004J\u000e\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\u0006H$J\u0015\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00028\u0000H$¢\u0006\u0002\u0010\u000bJ\u0015\u0010\f\u001a\u00020\r2\u0006\u0010\n\u001a\u00028\u0000H$¢\u0006\u0002\u0010\u000eJ\u0015\u0010\u0013\u001a\u00020\u00142\u0006\u0010\n\u001a\u00028\u0000H$¢\u0006\u0002\u0010\u0015J\b\u0010\u0016\u001a\u00020\u0017H\u0016J\b\u0010\u0018\u001a\u00020\u0014H\u0016J\b\u0010\u0019\u001a\u00020\u0014H\u0004J(\u0010\u001a\u001a\u00020\u00142\u0016\u0010\u001b\u001a\u0012\u0012\u0004\u0012\u00020\u001d0\u001cj\b\u0012\u0004\u0012\u00020\u001d`\u001e2\u0006\u0010\u001f\u001a\u00020 H\u0014J0\u0010!\u001a\u00020\u00142\u0006\u0010\"\u001a\u00020\u001d2\u0006\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020&2\u0006\u0010'\u001a\u00020(2\u0006\u0010)\u001a\u00020(H\u0014J0\u0010*\u001a\u00020\u00172\u0006\u0010\"\u001a\u00020\u001d2\u0006\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020&2\u0006\u0010'\u001a\u00020(2\u0006\u0010)\u001a\u00020(H\u0014J\u0015\u0010+\u001a\u00020$2\u0006\u0010\n\u001a\u00028\u0000H\u0002¢\u0006\u0002\u0010,J\u0010\u0010-\u001a\u00020\u00102\u0006\u0010.\u001a\u00020\tH\u0004J6\u0010/\u001a\u00020\u00142\u0006\u0010-\u001a\u00020\r2$\u00100\u001a \u0012\u001c\u0012\u001a\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020&\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001402010\u0006H\u0004R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u000f\u001a\u00020\u0010X¤\u0004¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u0012¨\u00063"}, d2 = {"Lni/shikatu/re_extera/ui/PeerListFragment;", "T", "Lni/shikatu/re_extera/ui/settings/ReBasePreferencesFragment;", "<init>", "()V", "entries", "", "load", "peerId", "", "entry", "(Ljava/lang/Object;)J", "status", "", "(Ljava/lang/Object;)Ljava/lang/CharSequence;", "emptyText", "", "getEmptyText", "()Ljava/lang/String;", "onEntryClick", "", "(Ljava/lang/Object;)V", "onFragmentCreate", "", "onResume", "reload", "fillItems", "items", "Ljava/util/ArrayList;", "Lorg/telegram/ui/Components/UItem;", "Lkotlin/collections/ArrayList;", "adapter", "Lorg/telegram/ui/Components/UniversalAdapter;", "onClick", "item", "view", "Landroid/view/View;", "position", "", "x", "", "y", "onLongClick", "createCell", "(Ljava/lang/Object;)Landroid/view/View;", "title", "dialogId", "showActions", "actions", "Lkotlin/Triple;", "Lkotlin/Function0;", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public abstract class PeerListFragment<T> extends ReBasePreferencesFragment {
    private List<? extends T> entries = CollectionsKt.emptyList();

    protected abstract String getEmptyText();

    protected abstract List<T> load();

    protected abstract void onEntryClick(T entry);

    protected boolean onLongClick(UItem item, View view, int position, float x, float y) {
        Intrinsics.checkNotNullParameter(item, "item");
        Intrinsics.checkNotNullParameter(view, "view");
        return false;
    }

    protected abstract long peerId(T entry);

    protected abstract CharSequence status(T entry);

    public boolean onFragmentCreate() {
        this.entries = load();
        return super.onFragmentCreate();
    }

    public void onResume() {
        this.entries = load();
        super.onResume();
    }

    protected final void reload() {
        this.entries = load();
        ReBasePreferencesFragment.refresh$default(this, false, 1, null);
    }

    protected void fillItems(ArrayList<UItem> items, UniversalAdapter adapter) {
        Intrinsics.checkNotNullParameter(items, "items");
        Intrinsics.checkNotNullParameter(adapter, "adapter");
        if (this.entries.isEmpty()) {
            items.add(UItem.asShadow(getEmptyText()));
            return;
        }
        int i = 0;
        for (T t : this.entries) {
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            items.add(UItem.asCustom(i2, createCell(t)));
            i = i2;
        }
        items.add(UItem.asShadow());
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void onClick(UItem item, View view, int position, float x, float y) {
        Intrinsics.checkNotNullParameter(item, "item");
        Intrinsics.checkNotNullParameter(view, "view");
        Object orNull = CollectionsKt.getOrNull(this.entries, item.id - 1);
        if (orNull != null) {
            onEntryClick(orNull);
        }
    }

    private final View createCell(T entry) {
        View profileSearchCell = new ProfileSearchCell(getContext(), this.resourceProvider);
        long jPeerId = peerId(entry);
        TLObject tLObject = (TLObject) (DialogObject.isUserDialog(jPeerId) ? getMessagesController().getUser(Long.valueOf(jPeerId)) : getMessagesController().getChat(Long.valueOf(-jPeerId)));
        profileSearchCell.setData(tLObject, (TLRPC.EncryptedChat) null, tLObject == null ? Strings.INSTANCE.getUNKNOWN_DIALOG() : null, status(entry), false, false);
        profileSearchCell.setBackground(Theme.getSelectorDrawable(true));
        return profileSearchCell;
    }

    protected final String title(long dialogId) {
        return ChatUtils.INSTANCE.dialogTitle(this.currentAccount, dialogId, Strings.INSTANCE.getUNKNOWN_DIALOG());
    }

    protected final void showActions(CharSequence title, final List<? extends Triple<? extends CharSequence, Integer, ? extends Function0<Unit>>> actions) {
        Intrinsics.checkNotNullParameter(title, "title");
        Intrinsics.checkNotNullParameter(actions, "actions");
        Activity parentActivity = getParentActivity();
        if (parentActivity == null) {
            return;
        }
        BottomSheet.Builder title2 = new BottomSheet.Builder(parentActivity).setTitle(title);
        List<? extends Triple<? extends CharSequence, Integer, ? extends Function0<Unit>>> list = actions;
        ArrayList arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list, 10));
        Iterator<T> it = list.iterator();
        while (it.hasNext()) {
            arrayList.add((CharSequence) ((Triple) it.next()).getFirst());
        }
        CharSequence[] charSequenceArr = (CharSequence[]) arrayList.toArray(new CharSequence[0]);
        ArrayList arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(list, 10));
        Iterator<T> it2 = list.iterator();
        while (it2.hasNext()) {
            arrayList2.add(Integer.valueOf(((Number) ((Triple) it2.next()).getSecond()).intValue()));
        }
        title2.setItems(charSequenceArr, CollectionsKt.toIntArray(arrayList2), new DialogInterface.OnClickListener() { // from class: ni.shikatu.re_extera.ui.PeerListFragment.0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                PeerListFragment.showActions$lambda$4(actions, dialogInterface, i);
            }
        }).show();
    }

    static final void showActions$lambda$4(List list, DialogInterface dialogInterface, int i) {
        ((Function0) ((Triple) list.get(i)).getThird()).invoke();
    }
}
