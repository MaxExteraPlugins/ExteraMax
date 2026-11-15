package ni.shikatu.re_extera.ui.messages;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.TLRPC;

/* JADX INFO: compiled from: MessageRows.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\b\n\u0002\b\u0006\b\u0000\u0018\u0000 \u00172\u00020\u0001:\u0001\u0017B9\b\u0002\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u0007¢\u0006\u0004\b\n\u0010\u000bJ\u0010\u0010\u000f\u001a\u0004\u0018\u00010\t2\u0006\u0010\u0010\u001a\u00020\u0004J\u000e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0010\u001a\u00020\u0004J\u000e\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u0012J\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0016\u001a\u00020\u0012R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\rR\u001a\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u0007X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0018"}, d2 = {"Lni/shikatu/re_extera/ui/messages/MessageRows;", "", "messages", "", "Lorg/telegram/messenger/MessageObject;", "rows", "groups", "", "", "Lorg/telegram/messenger/MessageObject$GroupedMessages;", "<init>", "(Ljava/util/List;Ljava/util/List;Ljava/util/Map;)V", "getMessages", "()Ljava/util/List;", "getRows", "group", "message", "positionOf", "", "positionOfId", "id", "messageAt", "position", "Companion", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class MessageRows {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final MessageRows EMPTY = new MessageRows(CollectionsKt.emptyList(), CollectionsKt.emptyList(), MapsKt.emptyMap());
    private final Map<Long, MessageObject.GroupedMessages> groups;
    private final List<MessageObject> messages;
    private final List<MessageObject> rows;

    public /* synthetic */ MessageRows(List list, List list2, Map map, DefaultConstructorMarker defaultConstructorMarker) {
        this(list, list2, map);
    }

    private MessageRows(List<? extends MessageObject> list, List<? extends MessageObject> list2, Map<Long, ? extends MessageObject.GroupedMessages> map) {
        this.messages = list;
        this.rows = list2;
        this.groups = map;
    }

    public final List<MessageObject> getMessages() {
        return this.messages;
    }

    public final List<MessageObject> getRows() {
        return this.rows;
    }

    public final MessageObject.GroupedMessages group(MessageObject message) {
        MessageObject.GroupedMessages groupedMessages;
        Intrinsics.checkNotNullParameter(message, "message");
        long groupId = message.getGroupId();
        if (groupId == 0 || (groupedMessages = this.groups.get(Long.valueOf(groupId))) == null || groupedMessages.messages.size() <= 1 || groupedMessages.getPosition(message) == null) {
            return null;
        }
        return groupedMessages;
    }

    public final int positionOf(MessageObject message) {
        Intrinsics.checkNotNullParameter(message, "message");
        Iterator<MessageObject> it = this.rows.iterator();
        int i = 0;
        while (it.hasNext()) {
            if (it.next() == message) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public final int positionOfId(int id) {
        int i = 0;
        for (MessageObject messageObject : this.rows) {
            if (!messageObject.isDateObject && messageObject.getId() == id) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public final MessageObject messageAt(int position) {
        MessageObject messageObject = (MessageObject) CollectionsKt.getOrNull(this.rows, position);
        if (messageObject == null || messageObject.isDateObject) {
            return null;
        }
        return messageObject;
    }

    /* JADX INFO: compiled from: MessageRows.kt */
    @Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J$\u0010\b\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f2\u0006\u0010\u000e\u001a\u00020\u000fJ\u0018\u0010\u0010\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0011\u001a\u00020\nH\u0002R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u0012"}, d2 = {"Lni/shikatu/re_extera/ui/messages/MessageRows$Companion;", "", "<init>", "()V", "EMPTY", "Lni/shikatu/re_extera/ui/messages/MessageRows;", "getEMPTY", "()Lni/shikatu/re_extera/ui/messages/MessageRows;", "build", "account", "", "source", "", "Lorg/telegram/messenger/MessageObject;", "groupAlbums", "", "dateRow", "date", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final MessageRows getEMPTY() {
            return MessageRows.EMPTY;
        }

        public final MessageRows build(int account, List<? extends MessageObject> source, boolean groupAlbums) {
            Intrinsics.checkNotNullParameter(source, "source");
            final Comparator comparator = new Comparator() { // from class: ni.shikatu.re_extera.ui.messages.MessageRows$Companion$build$$inlined$compareByDescending$1
                @Override // java.util.Comparator
                public final int compare(T t, T t2) {
                    return ComparisonsKt.compareValues(Integer.valueOf(((MessageObject) t2).getId()), Integer.valueOf(((MessageObject) t).getId()));
                }
            };
            List<MessageObject> listSortedWith = CollectionsKt.sortedWith(source, new Comparator() { // from class: ni.shikatu.re_extera.ui.messages.MessageRows$Companion$build$$inlined$thenByDescending$1
                @Override // java.util.Comparator
                public final int compare(T t, T t2) {
                    int iCompare = comparator.compare(t, t2);
                    return iCompare != 0 ? iCompare : ComparisonsKt.compareValues(Integer.valueOf(((MessageObject) t2).messageOwner.date), Integer.valueOf(((MessageObject) t).messageOwner.date));
                }
            });
            HashMap map = new HashMap();
            if (groupAlbums) {
                for (MessageObject messageObject : listSortedWith) {
                    if (messageObject.hasValidGroupId()) {
                        HashMap map2 = map;
                        Long lValueOf = Long.valueOf(messageObject.getGroupId());
                        Object obj = map2.get(lValueOf);
                        Object obj2 = obj;
                        if (obj == null) {
                            MessageObject.GroupedMessages groupedMessages = new MessageObject.GroupedMessages();
                            groupedMessages.groupId = messageObject.getGroupId();
                            map2.put(lValueOf, groupedMessages);
                            obj2 = groupedMessages;
                        }
                        ArrayList arrayList = ((MessageObject.GroupedMessages) obj2).messages;
                        Intrinsics.checkNotNullExpressionValue(arrayList, "messages");
                        arrayList.add(messageObject);
                    }
                }
                for (Object obj3 : map.values()) {
                    Intrinsics.checkNotNullExpressionValue(obj3, "next(...)");
                    MessageObject.GroupedMessages groupedMessages2 = (MessageObject.GroupedMessages) obj3;
                    ArrayList arrayList2 = groupedMessages2.messages;
                    Intrinsics.checkNotNullExpressionValue(arrayList2, "messages");
                    ArrayList arrayList3 = arrayList2;
                    if (arrayList3.size() > 1) {
                        CollectionsKt.sortWith(arrayList3, new Comparator() { // from class: ni.shikatu.re_extera.ui.messages.MessageRows$Companion$build$$inlined$sortBy$1
                            @Override // java.util.Comparator
                            public final int compare(T t, T t2) {
                                return ComparisonsKt.compareValues(Integer.valueOf(((MessageObject) t).getId()), Integer.valueOf(((MessageObject) t2).getId()));
                            }
                        });
                    }
                    groupedMessages2.calculate();
                }
            }
            ArrayList arrayList4 = new ArrayList(listSortedWith.size() + 8);
            int i = 0;
            for (MessageObject messageObject2 : listSortedWith) {
                i++;
                ArrayList arrayList5 = arrayList4;
                arrayList5.add(messageObject2);
                MessageObject messageObject3 = (MessageObject) CollectionsKt.getOrNull(listSortedWith, i);
                if (messageObject3 == null || messageObject3.dateKeyInt != messageObject2.dateKeyInt) {
                    arrayList5.add(dateRow(account, messageObject2.messageOwner.date));
                }
            }
            return new MessageRows(listSortedWith, arrayList4, map, null);
        }

        private final MessageObject dateRow(int account, int date) {
            Calendar calendar = Calendar.getInstance();
            long j = date;
            calendar.setTimeInMillis(j * 1000);
            calendar.set(11, 0);
            calendar.set(12, 0);
            calendar.set(13, 0);
            calendar.set(14, 0);
            TLRPC.Message tL_message = new TLRPC.TL_message();
            ((TLRPC.TL_message) tL_message).message = LocaleController.formatDateChat(j);
            ((TLRPC.TL_message) tL_message).id = 0;
            ((TLRPC.TL_message) tL_message).date = (int) (calendar.getTimeInMillis() / 1000);
            MessageObject messageObject = new MessageObject(account, tL_message, false, false);
            messageObject.type = 10;
            messageObject.contentType = 1;
            messageObject.isDateObject = true;
            return messageObject;
        }
    }
}
