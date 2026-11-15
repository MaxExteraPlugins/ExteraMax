package ni.shikatu.re_extera.utils;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.telegram.messenger.MessageObject;

/* JADX INFO: compiled from: MessageActions.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0002\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0000\u0010\u0003¨\u0006\u0004"}, d2 = {"isOneTime", "", "Lorg/telegram/messenger/MessageObject;", "(Lorg/telegram/messenger/MessageObject;)Z", "re_extera"}, k = 2, mv = {2, 2, 0}, xi = 48)
public final class MessageActionsKt {
    public static final boolean isOneTime(MessageObject messageObject) {
        Intrinsics.checkNotNullParameter(messageObject, "<this>");
        return messageObject.isSecret() || messageObject.isSecretMedia() || messageObject.isVoiceOnce() || messageObject.isRoundOnce();
    }
}
