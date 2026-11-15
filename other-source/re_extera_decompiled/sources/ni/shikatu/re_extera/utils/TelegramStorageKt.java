package ni.shikatu.re_extera.utils;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import ni.shikatu.extensions.FieldRef;
import org.telegram.messenger.BaseController;
import org.telegram.messenger.UserConfig;

/* JADX INFO: compiled from: TelegramStorage.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000\u0014\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\"\u0014\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\"\u0015\u0010\u0003\u001a\u00020\u0002*\u00020\u00048F¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0007"}, d2 = {"controllerAccount", "Lni/shikatu/extensions/FieldRef;", "", "account", "Lorg/telegram/messenger/BaseController;", "getAccount", "(Lorg/telegram/messenger/BaseController;)I", "re_extera"}, k = 2, mv = {2, 2, 0}, xi = 48)
public final class TelegramStorageKt {
    private static final FieldRef<Integer> controllerAccount = new FieldRef<>(BaseController.class, "currentAccount");

    public static final int getAccount(BaseController baseController) {
        Intrinsics.checkNotNullParameter(baseController, "<this>");
        Integer num = controllerAccount.get(baseController);
        return num != null ? num.intValue() : UserConfig.selectedAccount;
    }
}
