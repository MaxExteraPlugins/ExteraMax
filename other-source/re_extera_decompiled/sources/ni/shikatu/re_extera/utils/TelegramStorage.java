package ni.shikatu.re_extera.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import ni.shikatu.re_extera.ReLog;
import org.telegram.SQLite.SQLiteCursor;
import org.telegram.SQLite.SQLiteDatabase;
import org.telegram.messenger.BaseController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.InputSerializedData;
import org.telegram.tgnet.TLRPC;

/* JADX INFO: compiled from: TelegramStorage.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010$\n\u0002\u0010\t\n\u0002\u0010 \n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u001e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\bÆ\u0002\u0018\u00002\u00020\u0001:\u0001\u0017B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J6\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u00020\u0006\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u00052\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00062\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\b0\rJ*\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\u00072\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00062\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\b0\rJ$\u0010\u0010\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00062\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0007J\u001c\u0010\u0013\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\n2\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0007J \u0010\u0015\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00062\u0006\u0010\u0016\u001a\u00020\b¨\u0006\u0018"}, d2 = {"Lni/shikatu/re_extera/utils/TelegramStorage;", "", "<init>", "()V", "existingMessages", "", "", "", "", "storage", "Lorg/telegram/messenger/MessagesStorage;", "dialogId", "messageIds", "", "loadMessages", "Lorg/telegram/tgnet/TLRPC$Message;", "attachReplies", "", "messages", "loadPeers", "Lni/shikatu/re_extera/utils/TelegramStorage$Peers;", "loadMessage", "messageId", "Peers", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class TelegramStorage {
    public static final TelegramStorage INSTANCE = new TelegramStorage();

    private TelegramStorage() {
    }

    public final Map<Long, List<Integer>> existingMessages(MessagesStorage storage, long dialogId, Collection<Integer> messageIds) {
        String str;
        Intrinsics.checkNotNullParameter(storage, "storage");
        Intrinsics.checkNotNullParameter(messageIds, "messageIds");
        if (messageIds.isEmpty()) {
            return MapsKt.emptyMap();
        }
        if (dialogId != 0) {
            str = "uid = " + dialogId;
        } else {
            str = "is_channel = 0";
        }
        HashMap map = new HashMap();
        try {
            SQLiteDatabase database = storage.getDatabase();
            StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
            String str2 = String.format(Locale.US, "SELECT uid, mid FROM messages_v2 WHERE mid IN(%s) AND %s", Arrays.copyOf(new Object[]{CollectionsKt.joinToString$default(messageIds, ",", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 62, (Object) null), str}, 2));
            Intrinsics.checkNotNullExpressionValue(str2, "format(...)");
            SQLiteCursor sQLiteCursorQueryFinalized = database.queryFinalized(str2, new Object[0]);
            while (sQLiteCursorQueryFinalized.next()) {
                try {
                    HashMap map2 = map;
                    Long lValueOf = Long.valueOf(sQLiteCursorQueryFinalized.longValue(0));
                    Object obj = map2.get(lValueOf);
                    if (obj == null) {
                        obj = (List) new ArrayList();
                        map2.put(lValueOf, obj);
                    }
                    ((List) obj).add(Integer.valueOf(sQLiteCursorQueryFinalized.intValue(1)));
                } catch (Throwable th) {
                    sQLiteCursorQueryFinalized.dispose();
                    throw th;
                }
            }
            sQLiteCursorQueryFinalized.dispose();
        } catch (Exception e) {
            ReLog.INSTANCE.e("Failed to resolve deleted messages", e);
        }
        return map;
    }

    /* JADX INFO: compiled from: TelegramStorage.kt */
    @Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B7\u0012\u0016\u0010\u0002\u001a\u0012\u0012\u0004\u0012\u00020\u00040\u0003j\b\u0012\u0004\u0012\u00020\u0004`\u0005\u0012\u0016\u0010\u0006\u001a\u0012\u0012\u0004\u0012\u00020\u00070\u0003j\b\u0012\u0004\u0012\u00020\u0007`\u0005¢\u0006\u0004\b\b\u0010\tR!\u0010\u0002\u001a\u0012\u0012\u0004\u0012\u00020\u00040\u0003j\b\u0012\u0004\u0012\u00020\u0004`\u0005¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR!\u0010\u0006\u001a\u0012\u0012\u0004\u0012\u00020\u00070\u0003j\b\u0012\u0004\u0012\u00020\u0007`\u0005¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u000b¨\u0006\r"}, d2 = {"Lni/shikatu/re_extera/utils/TelegramStorage$Peers;", "", "users", "Ljava/util/ArrayList;", "Lorg/telegram/tgnet/TLRPC$User;", "Lkotlin/collections/ArrayList;", "chats", "Lorg/telegram/tgnet/TLRPC$Chat;", "<init>", "(Ljava/util/ArrayList;Ljava/util/ArrayList;)V", "getUsers", "()Ljava/util/ArrayList;", "getChats", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Peers {
        private final ArrayList<TLRPC.Chat> chats;
        private final ArrayList<TLRPC.User> users;

        public Peers(ArrayList<TLRPC.User> arrayList, ArrayList<TLRPC.Chat> arrayList2) {
            Intrinsics.checkNotNullParameter(arrayList, "users");
            Intrinsics.checkNotNullParameter(arrayList2, "chats");
            this.users = arrayList;
            this.chats = arrayList2;
        }

        public final ArrayList<TLRPC.Chat> getChats() {
            return this.chats;
        }

        public final ArrayList<TLRPC.User> getUsers() {
            return this.users;
        }
    }

    public final List<TLRPC.Message> loadMessages(MessagesStorage storage, long dialogId, Collection<Integer> messageIds) {
        Intrinsics.checkNotNullParameter(storage, "storage");
        Intrinsics.checkNotNullParameter(messageIds, "messageIds");
        if (messageIds.isEmpty()) {
            return CollectionsKt.emptyList();
        }
        ArrayList arrayList = new ArrayList(messageIds.size());
        long j = UserConfig.getInstance(TelegramStorageKt.getAccount((BaseController) storage)).clientUserId;
        try {
            for (List list : CollectionsKt.chunked(messageIds, 500)) {
                SQLiteDatabase database = storage.getDatabase();
                StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
                String str = String.format(Locale.US, "SELECT data FROM messages_v2 WHERE uid = %d AND mid IN(%s)", Arrays.copyOf(new Object[]{Long.valueOf(dialogId), CollectionsKt.joinToString$default(list, ",", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 62, (Object) null)}, 2));
                Intrinsics.checkNotNullExpressionValue(str, "format(...)");
                SQLiteCursor sQLiteCursorQueryFinalized = database.queryFinalized(str, new Object[0]);
                while (sQLiteCursorQueryFinalized.next()) {
                    try {
                        InputSerializedData inputSerializedDataByteBufferValue = sQLiteCursorQueryFinalized.byteBufferValue(0);
                        if (inputSerializedDataByteBufferValue != null) {
                            TLRPC.Message messageTLdeserialize = TLRPC.Message.TLdeserialize(inputSerializedDataByteBufferValue, inputSerializedDataByteBufferValue.readInt32(false), false);
                            if (messageTLdeserialize != null) {
                                messageTLdeserialize.readAttachPath(inputSerializedDataByteBufferValue, j);
                            }
                            inputSerializedDataByteBufferValue.reuse();
                            if (messageTLdeserialize != null) {
                                messageTLdeserialize.dialog_id = dialogId;
                                arrayList.add(messageTLdeserialize);
                            }
                        }
                    } catch (Throwable th) {
                        sQLiteCursorQueryFinalized.dispose();
                        throw th;
                    }
                }
                sQLiteCursorQueryFinalized.dispose();
            }
        } catch (Exception e) {
            ReLog.INSTANCE.e("Failed to load messages for " + dialogId, e);
        }
        return arrayList;
    }

    public final void attachReplies(MessagesStorage storage, long dialogId, List<? extends TLRPC.Message> messages) {
        TLRPC.MessageReplyHeader messageReplyHeader;
        Intrinsics.checkNotNullParameter(storage, "storage");
        Intrinsics.checkNotNullParameter(messages, "messages");
        HashMap map = new HashMap();
        for (TLRPC.Message message : messages) {
            if (message.replyMessage == null && (messageReplyHeader = message.reply_to) != null && messageReplyHeader.reply_to_msg_id != 0 && (messageReplyHeader.reply_to_peer_id == null || MessageObject.getPeerId(messageReplyHeader.reply_to_peer_id) == dialogId)) {
                HashMap map2 = map;
                Integer numValueOf = Integer.valueOf(messageReplyHeader.reply_to_msg_id);
                Object obj = map2.get(numValueOf);
                if (obj == null) {
                    obj = (List) new ArrayList();
                    map2.put(numValueOf, obj);
                }
                ((Collection) obj).add(message);
            }
        }
        if (map.isEmpty()) {
            return;
        }
        Set setKeySet = map.keySet();
        Intrinsics.checkNotNullExpressionValue(setKeySet, "<get-keys>(...)");
        for (TLRPC.Message message2 : loadMessages(storage, dialogId, setKeySet)) {
            List list = (List) map.get(Integer.valueOf(message2.id));
            if (list != null) {
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    ((TLRPC.Message) it.next()).replyMessage = message2;
                }
            }
        }
    }

    public final Peers loadPeers(MessagesStorage storage, List<? extends TLRPC.Message> messages) {
        Intrinsics.checkNotNullParameter(storage, "storage");
        Intrinsics.checkNotNullParameter(messages, "messages");
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (TLRPC.Message message : messages) {
            MessagesStorage.addUsersAndChatsFromMessage(message, arrayList, arrayList2, (ArrayList) null);
            TLRPC.Message message2 = message.replyMessage;
            if (message2 != null) {
                MessagesStorage.addUsersAndChatsFromMessage(message2, arrayList, arrayList2, (ArrayList) null);
            }
        }
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        try {
            if (!arrayList.isEmpty()) {
                storage.getUsersInternal(new ArrayList(CollectionsKt.distinct(arrayList)), arrayList3);
            }
            if (!arrayList2.isEmpty()) {
                storage.getChatsInternal(CollectionsKt.joinToString$default(CollectionsKt.distinct(arrayList2), ",", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 62, (Object) null), arrayList4);
            }
        } catch (Exception e) {
            ReLog.INSTANCE.e("Failed to load peers", e);
        }
        return new Peers(arrayList3, arrayList4);
    }

    public final TLRPC.Message loadMessage(MessagesStorage storage, long dialogId, int messageId) {
        Intrinsics.checkNotNullParameter(storage, "storage");
        try {
            SQLiteDatabase database = storage.getDatabase();
            StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
            String str = String.format(Locale.US, "SELECT data FROM messages_v2 WHERE uid = %d AND mid = %d LIMIT 1", Arrays.copyOf(new Object[]{Long.valueOf(dialogId), Integer.valueOf(messageId)}, 2));
            Intrinsics.checkNotNullExpressionValue(str, "format(...)");
            SQLiteCursor sQLiteCursorQueryFinalized = database.queryFinalized(str, new Object[0]);
            try {
                if (!sQLiteCursorQueryFinalized.next()) {
                    return null;
                }
                InputSerializedData inputSerializedDataByteBufferValue = sQLiteCursorQueryFinalized.byteBufferValue(0);
                if (inputSerializedDataByteBufferValue == null) {
                    return null;
                }
                TLRPC.Message messageTLdeserialize = TLRPC.Message.TLdeserialize(inputSerializedDataByteBufferValue, inputSerializedDataByteBufferValue.readInt32(false), false);
                inputSerializedDataByteBufferValue.reuse();
                return messageTLdeserialize;
            } finally {
                sQLiteCursorQueryFinalized.dispose();
            }
        } catch (Exception e) {
            ReLog.INSTANCE.e("Failed to load message " + dialogId + "/" + messageId, e);
        }
    }
}
