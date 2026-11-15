package ni.shikatu.re_extera.hooks.ghost;

import android.os.SystemClock;
import android.util.SparseArray;
import android.util.SparseLongArray;
import de.robv.android.xposed.XC_MethodHook;
import java.util.ArrayList;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import ni.shikatu.extensions.FieldRef;
import ni.shikatu.extensions.HookUtilsKt;
import ni.shikatu.extensions.MethodRef;
import ni.shikatu.re_extera.hooks.HookerClass;
import ni.shikatu.re_extera.settings.ReSettings;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.Vector;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ProfileActivity;

/* JADX INFO: compiled from: OwnLastSeenHooks.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \u001e2\u00020\u0001:\u0001\u001eB\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u0011\u001a\u00020\u0012H\u0014J\u0018\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u000eH\u0002J$\u0010\u0017\u001a\u00020\u00122\u0006\u0010\u0018\u001a\u00020\u00192\u0012\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00120\u001bH\u0002J\u0012\u0010\u001c\u001a\u00020\u000e2\b\u0010\u0016\u001a\u0004\u0018\u00010\u001dH\u0002R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\u0007\u001a\u0010\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\b0\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\rX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001f"}, d2 = {"Lni/shikatu/re_extera/hooks/ghost/OwnLastSeenHooks;", "Lni/shikatu/re_extera/hooks/HookerClass;", "<init>", "()V", "userId", "Lni/shikatu/extensions/FieldRef;", "", "onlineTextView", "", "Lorg/telegram/ui/ActionBar/SimpleTextView;", "needLayout", "Lni/shikatu/extensions/MethodRef;", "cachedStatus", "Landroid/util/SparseArray;", "", "lastRequest", "Landroid/util/SparseLongArray;", "setup", "", "show", "profile", "Lorg/telegram/ui/ProfileActivity;", "status", "requestRealStatus", "account", "", "onResult", "Lkotlin/Function1;", "formatStatus", "Lorg/telegram/tgnet/TLRPC$UserStatus;", "Companion", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class OwnLastSeenHooks extends HookerClass {
    private static final Companion Companion = new Companion(null);

    @Deprecated
    public static final long REQUEST_COOLDOWN_MS = 5000;
    private final FieldRef<Long> userId = new FieldRef<>(ProfileActivity.class, "userId");
    private final FieldRef<SimpleTextView[]> onlineTextView = new FieldRef<>(ProfileActivity.class, "onlineTextView");
    private final MethodRef needLayout = new MethodRef(ProfileActivity.class, "needLayout", Boolean.TYPE);
    private final SparseArray<String> cachedStatus = new SparseArray<>();
    private final SparseLongArray lastRequest = new SparseLongArray();

    @Override // ni.shikatu.re_extera.hooks.HookerClass
    protected void setup() {
        hookAfter(ProfileActivity.class, "updateProfileData", new Class[]{Boolean.TYPE}, new Function1() { // from class: ni.shikatu.re_extera.hooks.ghost.OwnLastSeenHooks.1
            public final Object invoke(Object obj) {
                return OwnLastSeenHooks.setup$lambda$2(OwnLastSeenHooks.this, (XC_MethodHook.MethodHookParam) obj);
            }
        });
    }

    static final Unit setup$lambda$2(OwnLastSeenHooks ownLastSeenHooks, XC_MethodHook.MethodHookParam methodHookParam) {
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        if (!ReSettings.INSTANCE.getGhostHideOnline()) {
            return Unit.INSTANCE;
        }
        final ProfileActivity profileActivity = (ProfileActivity) HookUtilsKt.self(methodHookParam);
        final int currentAccount = profileActivity.getCurrentAccount();
        Long l = ownLastSeenHooks.userId.get(profileActivity);
        long clientUserId = UserConfig.getInstance(currentAccount).getClientUserId();
        if (l == null || l.longValue() != clientUserId) {
            return Unit.INSTANCE;
        }
        String str = ownLastSeenHooks.cachedStatus.get(currentAccount);
        if (str != null) {
            ownLastSeenHooks.show(profileActivity, str);
        }
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        if (jElapsedRealtime - ownLastSeenHooks.lastRequest.get(currentAccount, 0L) < REQUEST_COOLDOWN_MS) {
            return Unit.INSTANCE;
        }
        ownLastSeenHooks.lastRequest.put(currentAccount, jElapsedRealtime);
        ownLastSeenHooks.requestRealStatus(currentAccount, new Function1() { // from class: ni.shikatu.re_extera.hooks.ghost.OwnLastSeenHooks.3
            public final Object invoke(Object obj) {
                return OwnLastSeenHooks.setup$lambda$2$lambda$1(OwnLastSeenHooks.this, currentAccount, profileActivity, (String) obj);
            }
        });
        return Unit.INSTANCE;
    }

    static final Unit setup$lambda$2$lambda$1(OwnLastSeenHooks ownLastSeenHooks, int i, ProfileActivity profileActivity, String str) {
        Intrinsics.checkNotNullParameter(str, "status");
        ownLastSeenHooks.cachedStatus.put(i, str);
        if (profileActivity.fragmentView != null && ReSettings.INSTANCE.getGhostHideOnline()) {
            ownLastSeenHooks.show(profileActivity, str);
        }
        return Unit.INSTANCE;
    }

    private final void show(ProfileActivity profile, String status) {
        SimpleTextView[] simpleTextViewArr = this.onlineTextView.get(profile);
        if (simpleTextViewArr == null) {
            return;
        }
        for (SimpleTextView simpleTextView : simpleTextViewArr) {
            if (simpleTextView != null) {
                simpleTextView.setText(status);
            }
        }
        this.needLayout.call(profile, true);
    }

    private final void requestRealStatus(int account, final Function1<? super String, Unit> onResult) {
        TLRPC.InputUser inputUser = MessagesController.getInstance(account).getInputUser(UserConfig.getInstance(account).getClientUserId());
        if (inputUser == null) {
            return;
        }
        TLObject tL_users_getUsers = new TLRPC.TL_users_getUsers();
        ((TLRPC.TL_users_getUsers) tL_users_getUsers).id.add(inputUser);
        ConnectionsManager.getInstance(account).sendRequest(tL_users_getUsers, new RequestDelegate() { // from class: ni.shikatu.re_extera.hooks.ghost.OwnLastSeenHooks.2
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                OwnLastSeenHooks.requestRealStatus$lambda$6(OwnLastSeenHooks.this, onResult, tLObject, tL_error);
            }
        });
    }

    static final void requestRealStatus$lambda$6(OwnLastSeenHooks ownLastSeenHooks, final Function1 function1, TLObject tLObject, TLRPC.TL_error tL_error) {
        ArrayList arrayList;
        Vector vector = tLObject instanceof Vector ? (Vector) tLObject : null;
        TLObject tLObject2 = (vector == null || (arrayList = vector.objects) == null) ? null : (TLObject) CollectionsKt.firstOrNull(arrayList);
        TLRPC.User user = tLObject2 instanceof TLRPC.User ? (TLRPC.User) tLObject2 : null;
        if (user == null) {
            return;
        }
        final String status = ownLastSeenHooks.formatStatus(user.status);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: ni.shikatu.re_extera.hooks.ghost.OwnLastSeenHooks.0
            @Override // java.lang.Runnable
            public final void run() {
                function1.invoke(status);
            }
        });
    }

    private final String formatStatus(TLRPC.UserStatus status) {
        if (status == null) {
            String string = LocaleController.getString(R.string.Lately);
            Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
            return string;
        }
        if (status instanceof TLRPC.TL_userStatusOnline) {
            String string2 = LocaleController.getString(R.string.Online);
            Intrinsics.checkNotNullExpressionValue(string2, "getString(...)");
            return string2;
        }
        if (status.expires > 0) {
            String dateOnline = LocaleController.formatDateOnline(status.expires, (boolean[]) null);
            Intrinsics.checkNotNullExpressionValue(dateOnline, "formatDateOnline(...)");
            return dateOnline;
        }
        String string3 = LocaleController.getString(R.string.Lately);
        Intrinsics.checkNotNullExpressionValue(string3, "getString(...)");
        return string3;
    }

    /* JADX INFO: compiled from: OwnLastSeenHooks.kt */
    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0006"}, d2 = {"Lni/shikatu/re_extera/hooks/ghost/OwnLastSeenHooks$Companion;", "", "<init>", "()V", "REQUEST_COOLDOWN_MS", "", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
