package ni.shikatu.re_extera.hooks.ghost;

import de.robv.android.xposed.XC_MethodHook;
import java.util.Set;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.SetsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
import ni.shikatu.extensions.FieldRef;
import ni.shikatu.extensions.HookUtilsKt;
import ni.shikatu.re_extera.hooks.HookerClass;
import ni.shikatu.re_extera.settings.ReSettings;
import ni.shikatu.re_extera.utils.MessageActions;
import org.telegram.messenger.BaseController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.QuickAckDelegate;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.RequestDelegateTimestamp;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.WriteToSocketDelegate;
import org.telegram.tgnet.tl.TL_account;
import org.telegram.tgnet.tl.TL_forum;
import org.telegram.tgnet.tl.TL_phone;
import org.telegram.tgnet.tl.TL_stories;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.LaunchActivity;

/* JADX INFO: compiled from: GhostRequestsHooks.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 \u00182\u00020\u0001:\u0001\u0018B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u0007\u001a\u00020\bH\u0014J\u0014\u0010\t\u001a\u00020\u00062\n\u0010\n\u001a\u00060\u000bj\u0002`\fH\u0002J\u0014\u0010\r\u001a\u00020\b2\n\u0010\n\u001a\u00060\u000bj\u0002`\fH\u0002J\u0014\u0010\u000e\u001a\u00020\b2\n\u0010\n\u001a\u00060\u000bj\u0002`\fH\u0002J\u000f\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0002¢\u0006\u0002\u0010\u0011J\u0018\u0010\u0012\u001a\u00020\b2\u0006\u0010\u0013\u001a\u00020\u00062\u0006\u0010\u0014\u001a\u00020\u0015H\u0002J\u0017\u0010\u0016\u001a\u0004\u0018\u00010\u00102\u0006\u0010\u0014\u001a\u00020\u0015H\u0002¢\u0006\u0002\u0010\u0017R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0019"}, d2 = {"Lni/shikatu/re_extera/hooks/ghost/GhostRequestsHooks;", "Lni/shikatu/re_extera/hooks/HookerClass;", "<init>", "()V", "accountField", "Lni/shikatu/extensions/FieldRef;", "", "setup", "", "accountOf", "param", "Lde/robv/android/xposed/XC_MethodHook$MethodHookParam;", "Lni/shikatu/extensions/HookParam;", "beforeSend", "afterSend", "openedChatId", "", "()Ljava/lang/Long;", "readOnInteraction", "account", "request", "Lorg/telegram/tgnet/TLObject;", "dialogIdOf", "(Lorg/telegram/tgnet/TLObject;)Ljava/lang/Long;", "Companion", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class GhostRequestsHooks extends HookerClass {
    private static final Companion Companion = new Companion(null);
    private static final Set<Class<?>> ONLINE_REQUESTS;
    private static final Set<Class<?>> READING_REQUESTS;
    private static final Set<Class<?>> STORIES_REQUESTS;
    private static final Set<Class<?>> TYPING_REQUESTS;
    private final FieldRef<Integer> accountField = new FieldRef<>(BaseController.class, "currentAccount");

    @Override // ni.shikatu.re_extera.hooks.HookerClass
    protected void setup() {
        hookMethod(ConnectionsManager.class, "sendRequestInternal", new Class[]{TLObject.class, RequestDelegate.class, RequestDelegateTimestamp.class, QuickAckDelegate.class, WriteToSocketDelegate.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Boolean.TYPE, Integer.TYPE}, new AnonymousClass1(this), new AnonymousClass2(this));
    }

    /* JADX INFO: renamed from: ni.shikatu.re_extera.hooks.ghost.GhostRequestsHooks$setup$1, reason: invalid class name */
    /* JADX INFO: compiled from: GhostRequestsHooks.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    static final /* synthetic */ class AnonymousClass1 extends FunctionReferenceImpl implements Function1<XC_MethodHook.MethodHookParam, Unit> {
        AnonymousClass1(Object obj) {
            super(1, obj, GhostRequestsHooks.class, "beforeSend", "beforeSend(Lde/robv/android/xposed/XC_MethodHook$MethodHookParam;)V", 0);
        }

        public /* bridge */ /* synthetic */ Object invoke(Object obj) {
            invoke((XC_MethodHook.MethodHookParam) obj);
            return Unit.INSTANCE;
        }

        public final void invoke(XC_MethodHook.MethodHookParam methodHookParam) {
            Intrinsics.checkNotNullParameter(methodHookParam, "p0");
            ((GhostRequestsHooks) this.receiver).beforeSend(methodHookParam);
        }
    }

    /* JADX INFO: renamed from: ni.shikatu.re_extera.hooks.ghost.GhostRequestsHooks$setup$2, reason: invalid class name */
    /* JADX INFO: compiled from: GhostRequestsHooks.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    static final /* synthetic */ class AnonymousClass2 extends FunctionReferenceImpl implements Function1<XC_MethodHook.MethodHookParam, Unit> {
        AnonymousClass2(Object obj) {
            super(1, obj, GhostRequestsHooks.class, "afterSend", "afterSend(Lde/robv/android/xposed/XC_MethodHook$MethodHookParam;)V", 0);
        }

        public /* bridge */ /* synthetic */ Object invoke(Object obj) {
            invoke((XC_MethodHook.MethodHookParam) obj);
            return Unit.INSTANCE;
        }

        public final void invoke(XC_MethodHook.MethodHookParam methodHookParam) {
            Intrinsics.checkNotNullParameter(methodHookParam, "p0");
            ((GhostRequestsHooks) this.receiver).afterSend(methodHookParam);
        }
    }

    private final int accountOf(XC_MethodHook.MethodHookParam param) {
        Integer num = this.accountField.get(param.thisObject);
        return num != null ? num.intValue() : UserConfig.selectedAccount;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void beforeSend(XC_MethodHook.MethodHookParam param) {
        boolean ghostNoReadStories = false;
        TL_account.updateStatus updatestatus = (TLObject) HookUtilsKt.arg(param, 0);
        if (updatestatus == null || MessageActions.INSTANCE.isUnfiltered(updatestatus)) {
            return;
        }
        if (updatestatus instanceof TL_account.updateStatus) {
            if (ReSettings.INSTANCE.getGhostHideOnline()) {
                updatestatus.offline = true;
                return;
            }
            return;
        }
        if (ReSettings.INSTANCE.getReadOnInteract()) {
            readOnInteraction(accountOf(param), updatestatus);
        }
        Class<?> cls = updatestatus.getClass();
        if (READING_REQUESTS.contains(cls)) {
            GhostPolicy ghostPolicy = GhostPolicy.INSTANCE;
            Long lDialogIdOf = dialogIdOf(updatestatus);
            if (lDialogIdOf == null) {
                lDialogIdOf = openedChatId();
            }
            ghostNoReadStories = ghostPolicy.blocksReading(lDialogIdOf);
        } else if (TYPING_REQUESTS.contains(cls)) {
            GhostPolicy ghostPolicy2 = GhostPolicy.INSTANCE;
            Long lDialogIdOf2 = dialogIdOf(updatestatus);
            if (lDialogIdOf2 == null) {
                lDialogIdOf2 = openedChatId();
            }
            ghostNoReadStories = ghostPolicy2.blocksTyping(lDialogIdOf2);
        } else if (STORIES_REQUESTS.contains(cls)) {
            ghostNoReadStories = ReSettings.INSTANCE.getGhostNoReadStories();
        }
        if (ghostNoReadStories) {
            param.setResult((Object) null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void afterSend(XC_MethodHook.MethodHookParam param) {
        TLObject tLObject;
        if (ReSettings.INSTANCE.getGhostImmediateOffline() && (tLObject = (TLObject) HookUtilsKt.arg(param, 0)) != null && ONLINE_REQUESTS.contains(tLObject.getClass())) {
            MessageActions messageActions = MessageActions.INSTANCE;
            int iAccountOf = accountOf(param);
            TLObject updatestatus = new TL_account.updateStatus();
            ((TL_account.updateStatus) updatestatus).offline = true;
            Unit unit = Unit.INSTANCE;
            MessageActions.sendUnfiltered$default(messageActions, iAccountOf, updatestatus, null, 4, null);
        }
    }

    private final Long openedChatId() {
        ChatActivity lastFragment = LaunchActivity.getLastFragment();
        ChatActivity chatActivity = lastFragment instanceof ChatActivity ? lastFragment : null;
        if (chatActivity != null) {
            return Long.valueOf(chatActivity.getDialogId());
        }
        return null;
    }

    private final void readOnInteraction(int account, TLObject request) {
        if (request instanceof TLRPC.TL_messages_sendReaction) {
            TLRPC.TL_messages_sendReaction tL_messages_sendReaction = (TLRPC.TL_messages_sendReaction) request;
            MessageActions.markRead$default(MessageActions.INSTANCE, account, tL_messages_sendReaction.peer, tL_messages_sendReaction.msg_id, false, 8, null);
            return;
        }
        if (request instanceof TLRPC.TL_messages_sendVote) {
            TLRPC.TL_messages_sendVote tL_messages_sendVote = (TLRPC.TL_messages_sendVote) request;
            MessageActions.markRead$default(MessageActions.INSTANCE, account, tL_messages_sendVote.peer, tL_messages_sendVote.msg_id, false, 8, null);
        } else if (request instanceof TLRPC.TL_messages_sendMessage) {
            MessageActions.markRead$default(MessageActions.INSTANCE, account, ((TLRPC.TL_messages_sendMessage) request).peer, 0, false, 8, null);
        } else if (request instanceof TLRPC.TL_messages_sendMedia) {
            MessageActions.markRead$default(MessageActions.INSTANCE, account, ((TLRPC.TL_messages_sendMedia) request).peer, 0, false, 8, null);
        } else if (request instanceof TLRPC.TL_messages_forwardMessages) {
            MessageActions.markRead$default(MessageActions.INSTANCE, account, ((TLRPC.TL_messages_forwardMessages) request).to_peer, 0, false, 8, null);
        }
    }

    /* JADX WARN: Code duplicated, block: B:13:0x0038  */
    private final Long dialogIdOf(TLObject request) {
        TLRPC.TL_inputEncryptedChat tL_inputEncryptedChat;
        Long lValueOf;
        TLRPC.InputPeer inputPeer;
        if (request instanceof TLRPC.TL_messages_readHistory) {
            lValueOf = Long.valueOf(DialogObject.getPeerDialogId(((TLRPC.TL_messages_readHistory) request).peer));
        } else if (request instanceof TLRPC.TL_messages_readDiscussion) {
            lValueOf = Long.valueOf(DialogObject.getPeerDialogId(((TLRPC.TL_messages_readDiscussion) request).peer));
        } else if (request instanceof TLRPC.TL_channels_readHistory) {
            TLRPC.InputChannel inputChannel = ((TLRPC.TL_channels_readHistory) request).channel;
            if (inputChannel != null) {
                lValueOf = Long.valueOf(-inputChannel.channel_id);
            } else {
                lValueOf = null;
            }
        } else if (request instanceof TLRPC.TL_channels_readMessageContents) {
            TLRPC.InputChannel inputChannel2 = ((TLRPC.TL_channels_readMessageContents) request).channel;
            if (inputChannel2 != null) {
                lValueOf = Long.valueOf(-inputChannel2.channel_id);
            } else {
                lValueOf = null;
            }
        } else if (request instanceof TLRPC.TL_messages_readEncryptedHistory) {
            TLRPC.TL_inputEncryptedChat tL_inputEncryptedChat2 = ((TLRPC.TL_messages_readEncryptedHistory) request).peer;
            if (tL_inputEncryptedChat2 != null) {
                lValueOf = Long.valueOf(DialogObject.makeEncryptedDialogId(tL_inputEncryptedChat2.chat_id));
            } else {
                lValueOf = null;
            }
        } else if (request instanceof TLRPC.TL_messages_markDialogUnread) {
            TLRPC.TL_inputDialogPeer tL_inputDialogPeer = ((TLRPC.TL_messages_markDialogUnread) request).peer;
            TLRPC.TL_inputDialogPeer tL_inputDialogPeer2 = tL_inputDialogPeer instanceof TLRPC.TL_inputDialogPeer ? tL_inputDialogPeer : null;
            if (tL_inputDialogPeer2 == null || (inputPeer = tL_inputDialogPeer2.peer) == null) {
                lValueOf = null;
            } else {
                lValueOf = Long.valueOf(DialogObject.getPeerDialogId(inputPeer));
            }
        } else if (request instanceof TLRPC.TL_messages_setTyping) {
            lValueOf = Long.valueOf(DialogObject.getPeerDialogId(((TLRPC.TL_messages_setTyping) request).peer));
        } else if (!(request instanceof TLRPC.TL_messages_setEncryptedTyping) || (tL_inputEncryptedChat = ((TLRPC.TL_messages_setEncryptedTyping) request).peer) == null) {
            lValueOf = null;
        } else {
            lValueOf = Long.valueOf(DialogObject.makeEncryptedDialogId(tL_inputEncryptedChat.chat_id));
        }
        if (lValueOf == null || lValueOf.longValue() == 0) {
            return null;
        }
        return lValueOf;
    }

    /* JADX INFO: compiled from: GhostRequestsHooks.kt */
    @Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0002\b\t\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u001b\u0010\u0004\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00060\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u001b\u0010\t\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00060\u0005¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\bR\u001b\u0010\u000b\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00060\u0005¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\bR\u001b\u0010\r\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00060\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\b¨\u0006\u000f"}, d2 = {"Lni/shikatu/re_extera/hooks/ghost/GhostRequestsHooks$Companion;", "", "<init>", "()V", "READING_REQUESTS", "", "Ljava/lang/Class;", "getREADING_REQUESTS", "()Ljava/util/Set;", "TYPING_REQUESTS", "getTYPING_REQUESTS", "STORIES_REQUESTS", "getSTORIES_REQUESTS", "ONLINE_REQUESTS", "getONLINE_REQUESTS", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final Set<Class<?>> getREADING_REQUESTS() {
            return GhostRequestsHooks.READING_REQUESTS;
        }

        public final Set<Class<?>> getTYPING_REQUESTS() {
            return GhostRequestsHooks.TYPING_REQUESTS;
        }

        public final Set<Class<?>> getSTORIES_REQUESTS() {
            return GhostRequestsHooks.STORIES_REQUESTS;
        }

        public final Set<Class<?>> getONLINE_REQUESTS() {
            return GhostRequestsHooks.ONLINE_REQUESTS;
        }
    }

    static {
        Set<Class<?>> of = SetsKt.setOf(new Class[]{TLRPC.TL_messages_readHistory.class, TLRPC.TL_messages_readEncryptedHistory.class, TLRPC.TL_messages_readDiscussion.class, TLRPC.TL_channels_readHistory.class, TLRPC.TL_messages_markDialogUnread.class, TLRPC.TL_messages_readMessageContents.class, TLRPC.TL_channels_readMessageContents.class});
        READING_REQUESTS = of;
        Set<Class<?>> of2 = SetsKt.setOf(new Class[]{TLRPC.TL_messages_setTyping.class, TLRPC.TL_messages_setEncryptedTyping.class});
        TYPING_REQUESTS = of2;
        Set<Class<?>> of3 = SetsKt.setOf(new Class[]{TL_stories.TL_stories_readStories.class, TL_stories.TL_stories_incrementStoryViews.class});
        STORIES_REQUESTS = of3;
        ONLINE_REQUESTS = SetsKt.plus(SetsKt.plus(SetsKt.plus(of, of2), of3), SetsKt.setOf(new Class[]{TLRPC.TL_messages_sendMessage.class, TLRPC.TL_messages_sendMedia.class, TLRPC.TL_messages_sendMultiMedia.class, TLRPC.TL_messages_sendInlineBotResult.class, TLRPC.TL_messages_sendEncrypted.class, TLRPC.TL_messages_sendEncryptedFile.class, TLRPC.TL_messages_sendEncryptedMultiMedia.class, TLRPC.TL_messages_sendEncryptedService.class, TLRPC.TL_messages_sendReaction.class, TLRPC.TL_messages_forwardMessages.class, TLRPC.TL_messages_editMessage.class, TLRPC.TL_messages_createChat.class, TLRPC.TL_channels_createChannel.class, TLRPC.TL_channels_leaveChannel.class, TLRPC.TL_messages_updatePinnedMessage.class, TL_forum.TL_messages_createForumTopic.class, TL_forum.TL_messages_deleteTopicHistory.class, TL_forum.TL_messages_editForumTopic.class, TL_stories.TL_stories_sendStory.class, TL_phone.requestCall.class, TL_phone.acceptCall.class, TL_phone.confirmCall.class}));
    }
}
