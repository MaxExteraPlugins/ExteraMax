package ni.shikatu.re_extera.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import ni.shikatu.extensions.MethodRef;
import ni.shikatu.re_extera.data.DeletedMessagesStore;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.TLRPC;

/* JADX INFO: compiled from: ForwardRestrictions.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010#\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u001e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fJ\u001a\u0010\r\u001a\b\u0012\u0004\u0012\u00020\b0\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\f0\u0010J\u0014\u0010\u0011\u001a\u00020\u00122\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\b0\u000eJ\u0012\u0010\u0014\u001a\u00020\n2\b\u0010\u0015\u001a\u0004\u0018\u00010\bH\u0002J\u0010\u0010\u0016\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0017"}, d2 = {"Lni/shikatu/re_extera/utils/ForwardRestrictions;", "", "<init>", "()V", "canForwardOriginal", "Lni/shikatu/extensions/MethodRef;", "hidden", "", "Lorg/telegram/tgnet/TLRPC$Message;", "isRestricted", "", "message", "Lorg/telegram/messenger/MessageObject;", "hideNoForwards", "", "messages", "", "restoreNoForwards", "", "owners", "isFlagHidden", "owner", "isPeerRestricted", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class ForwardRestrictions {
    public static final ForwardRestrictions INSTANCE = new ForwardRestrictions();
    private static final MethodRef canForwardOriginal = new MethodRef(MessageObject.class, "canForwardMessage", new Class[0]);
    private static final Set<TLRPC.Message> hidden;

    private ForwardRestrictions() {
    }

    static {
        Set<TLRPC.Message> setNewSetFromMap = Collections.newSetFromMap(new IdentityHashMap());
        Intrinsics.checkNotNullExpressionValue(setNewSetFromMap, "newSetFromMap(...)");
        hidden = setNewSetFromMap;
    }

    public final boolean isRestricted(MessageObject message) {
        Intrinsics.checkNotNullParameter(message, "message");
        return !Intrinsics.areEqual(canForwardOriginal.callOriginal(message, new Object[0]), true) || isFlagHidden(message.messageOwner) || isPeerRestricted(message) || DeletedMessagesStore.INSTANCE.isDeleted(message) || MessageActionsKt.isOneTime(message);
    }

    public final List<TLRPC.Message> hideNoForwards(Collection<? extends MessageObject> messages) {
        List<TLRPC.Message> list;
        Intrinsics.checkNotNullParameter(messages, "messages");
        synchronized (hidden) {
            ArrayList arrayList = new ArrayList();
            Iterator<T> it = messages.iterator();
            while (it.hasNext()) {
                TLRPC.Message message = ((MessageObject) it.next()).messageOwner;
                if (message != null) {
                    arrayList.add(message);
                }
            }
            ArrayList arrayList2 = new ArrayList();
            for (Object obj : arrayList) {
                if (((TLRPC.Message) obj).noforwards) {
                    arrayList2.add(obj);
                }
            }
            List<TLRPC.Message> listDistinct = CollectionsKt.distinct(arrayList2);
            for (TLRPC.Message message2 : listDistinct) {
                message2.noforwards = false;
                hidden.add(message2);
            }
            list = listDistinct;
        }
        return list;
    }

    public final void restoreNoForwards(List<? extends TLRPC.Message> owners) {
        Intrinsics.checkNotNullParameter(owners, "owners");
        synchronized (hidden) {
            for (TLRPC.Message message : owners) {
                message.noforwards = true;
                hidden.remove(message);
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    private final boolean isFlagHidden(TLRPC.Message owner) {
        boolean zContains;
        if (owner == null) {
            return false;
        }
        Set<TLRPC.Message> set = hidden;
        synchronized (set) {
            zContains = set.contains(owner);
        }
        return zContains;
    }

    private final boolean isPeerRestricted(MessageObject message) {
        MessagesController messagesController = MessagesController.getInstance(message.currentAccount);
        long dialogId = message.getDialogId();
        if (dialogId > 0) {
            TLRPC.UserFull userFull = messagesController.getUserFull(dialogId);
            if (userFull != null) {
                return userFull.noforwards_peer_enabled || userFull.noforwards_my_enabled;
            }
            return false;
        }
        TLRPC.Chat chat = messagesController.getChat(Long.valueOf(-dialogId));
        if (chat == null) {
            return false;
        }
        TLRPC.InputChannel inputChannel = chat.migrated_to;
        TLRPC.Chat chat2 = inputChannel != null ? messagesController.getChat(Long.valueOf(inputChannel.channel_id)) : null;
        if (chat2 != null) {
            chat = chat2;
        }
        return chat.noforwards;
    }
}
