package ni.shikatu.re_extera.settings;

import android.content.SharedPreferences;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.MutablePropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.properties.ReadWriteProperty;
import kotlin.reflect.KProperty;
import org.telegram.messenger.ApplicationLoader;

/* JADX INFO: compiled from: ReSettings.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b'\n\u0002\u0010\b\n\u0002\b\u001e\n\u0002\u0010\u000e\n\u0002\b\u001b\n\u0002\u0018\u0002\n\u0002\b\u0014\n\u0002\u0018\u0002\n\u0002\b\u0006\bÆ\u0002\u0018\u00002\u00020\u0001:\u0002\u0088\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\"\u0010\u0082\u0001\u001a\t\u0012\u0004\u0012\u00020\u000b0\u0083\u00012\u0007\u0010\u0084\u0001\u001a\u00020R2\u0007\u0010\u0085\u0001\u001a\u00020\u000bH\u0002J\"\u0010\u0086\u0001\u001a\t\u0012\u0004\u0012\u0002030\u0083\u00012\u0007\u0010\u0084\u0001\u001a\u00020R2\u0007\u0010\u0085\u0001\u001a\u000203H\u0002J\"\u0010\u0087\u0001\u001a\t\u0012\u0004\u0012\u00020R0\u0083\u00012\u0007\u0010\u0084\u0001\u001a\u00020R2\u0007\u0010\u0085\u0001\u001a\u00020RH\u0002R\u001b\u0010\u0004\u001a\u00020\u00058BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\b\u0010\t\u001a\u0004\b\u0006\u0010\u0007R+\u0010\f\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\u000b8F@FX\u0086\u008e\u0002¢\u0006\u0012\n\u0004\b\u0011\u0010\u0012\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R+\u0010\u0013\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\u000b8F@FX\u0086\u008e\u0002¢\u0006\u0012\n\u0004\b\u0016\u0010\u0012\u001a\u0004\b\u0014\u0010\u000e\"\u0004\b\u0015\u0010\u0010R+\u0010\u0017\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\u000b8F@FX\u0086\u008e\u0002¢\u0006\u0012\n\u0004\b\u001a\u0010\u0012\u001a\u0004\b\u0018\u0010\u000e\"\u0004\b\u0019\u0010\u0010R+\u0010\u001b\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\u000b8F@FX\u0086\u008e\u0002¢\u0006\u0012\n\u0004\b\u001e\u0010\u0012\u001a\u0004\b\u001c\u0010\u000e\"\u0004\b\u001d\u0010\u0010R+\u0010\u001f\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\u000b8F@FX\u0086\u008e\u0002¢\u0006\u0012\n\u0004\b\"\u0010\u0012\u001a\u0004\b \u0010\u000e\"\u0004\b!\u0010\u0010R+\u0010#\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\u000b8F@FX\u0086\u008e\u0002¢\u0006\u0012\n\u0004\b&\u0010\u0012\u001a\u0004\b$\u0010\u000e\"\u0004\b%\u0010\u0010R+\u0010'\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\u000b8F@FX\u0086\u008e\u0002¢\u0006\u0012\n\u0004\b*\u0010\u0012\u001a\u0004\b(\u0010\u000e\"\u0004\b)\u0010\u0010R+\u0010+\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\u000b8F@FX\u0086\u008e\u0002¢\u0006\u0012\n\u0004\b.\u0010\u0012\u001a\u0004\b,\u0010\u000e\"\u0004\b-\u0010\u0010R+\u0010/\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\u000b8F@FX\u0086\u008e\u0002¢\u0006\u0012\n\u0004\b2\u0010\u0012\u001a\u0004\b0\u0010\u000e\"\u0004\b1\u0010\u0010R+\u00104\u001a\u0002032\u0006\u0010\n\u001a\u0002038B@BX\u0082\u008e\u0002¢\u0006\u0012\n\u0004\b9\u0010\u0012\u001a\u0004\b5\u00106\"\u0004\b7\u00108R+\u0010:\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\u000b8F@FX\u0086\u008e\u0002¢\u0006\u0012\n\u0004\b=\u0010\u0012\u001a\u0004\b;\u0010\u000e\"\u0004\b<\u0010\u0010R+\u0010>\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\u000b8F@FX\u0086\u008e\u0002¢\u0006\u0012\n\u0004\bA\u0010\u0012\u001a\u0004\b?\u0010\u000e\"\u0004\b@\u0010\u0010R+\u0010B\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\u000b8F@FX\u0086\u008e\u0002¢\u0006\u0012\n\u0004\bE\u0010\u0012\u001a\u0004\bC\u0010\u000e\"\u0004\bD\u0010\u0010R+\u0010F\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\u000b8F@FX\u0086\u008e\u0002¢\u0006\u0012\n\u0004\bI\u0010\u0012\u001a\u0004\bG\u0010\u000e\"\u0004\bH\u0010\u0010R+\u0010J\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\u000b8F@FX\u0086\u008e\u0002¢\u0006\u0012\n\u0004\bM\u0010\u0012\u001a\u0004\bK\u0010\u000e\"\u0004\bL\u0010\u0010R+\u0010N\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\u000b8F@FX\u0086\u008e\u0002¢\u0006\u0012\n\u0004\bQ\u0010\u0012\u001a\u0004\bO\u0010\u000e\"\u0004\bP\u0010\u0010R+\u0010S\u001a\u00020R2\u0006\u0010\n\u001a\u00020R8F@FX\u0086\u008e\u0002¢\u0006\u0012\n\u0004\bX\u0010\u0012\u001a\u0004\bT\u0010U\"\u0004\bV\u0010WR+\u0010Y\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\u000b8F@FX\u0086\u008e\u0002¢\u0006\u0012\n\u0004\b\\\u0010\u0012\u001a\u0004\bZ\u0010\u000e\"\u0004\b[\u0010\u0010R+\u0010]\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\u000b8F@FX\u0086\u008e\u0002¢\u0006\u0012\n\u0004\b`\u0010\u0012\u001a\u0004\b^\u0010\u000e\"\u0004\b_\u0010\u0010R+\u0010a\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\u000b8F@FX\u0086\u008e\u0002¢\u0006\u0012\n\u0004\bd\u0010\u0012\u001a\u0004\bb\u0010\u000e\"\u0004\bc\u0010\u0010R+\u0010e\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\u000b8F@FX\u0086\u008e\u0002¢\u0006\u0012\n\u0004\bh\u0010\u0012\u001a\u0004\bf\u0010\u000e\"\u0004\bg\u0010\u0010R+\u0010i\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\u000b8F@FX\u0086\u008e\u0002¢\u0006\u0012\n\u0004\bl\u0010\u0012\u001a\u0004\bj\u0010\u000e\"\u0004\bk\u0010\u0010R$\u0010o\u001a\u00020n2\u0006\u0010m\u001a\u00020n8F@FX\u0086\u000e¢\u0006\f\u001a\u0004\bp\u0010q\"\u0004\br\u0010sR\u0011\u0010t\u001a\u00020\u000b8F¢\u0006\u0006\u001a\u0004\bu\u0010\u000eR\u0011\u0010v\u001a\u00020\u000b8F¢\u0006\u0006\u001a\u0004\bw\u0010\u000eR\u0011\u0010x\u001a\u00020\u000b8F¢\u0006\u0006\u001a\u0004\by\u0010\u000eR\u0011\u0010z\u001a\u00020\u000b8F¢\u0006\u0006\u001a\u0004\b{\u0010\u000eR\u0011\u0010|\u001a\u00020\u000b8F¢\u0006\u0006\u001a\u0004\b}\u0010\u000eR\u0011\u0010~\u001a\u0002038F¢\u0006\u0006\u001a\u0004\b\u007f\u00106R\u0013\u0010\u0080\u0001\u001a\u00020\u000b8F¢\u0006\u0007\u001a\u0005\b\u0081\u0001\u0010\u000e¨\u0006\u0089\u0001"}, d2 = {"Lni/shikatu/re_extera/settings/ReSettings;", "", "<init>", "()V", "prefs", "Landroid/content/SharedPreferences;", "getPrefs", "()Landroid/content/SharedPreferences;", "prefs$delegate", "Lkotlin/Lazy;", "<set-?>", "", "ghostEnabled", "getGhostEnabled", "()Z", "setGhostEnabled", "(Z)V", "ghostEnabled$delegate", "Lni/shikatu/re_extera/settings/ReSettings$Pref;", "hideOnline", "getHideOnline", "setHideOnline", "hideOnline$delegate", "immediateOffline", "getImmediateOffline", "setImmediateOffline", "immediateOffline$delegate", "hideTyping", "getHideTyping", "setHideTyping", "hideTyping$delegate", "hideReading", "getHideReading", "setHideReading", "hideReading$delegate", "noReadStories", "getNoReadStories", "setNoReadStories", "noReadStories$delegate", "readOnInteract", "getReadOnInteract", "setReadOnInteract", "readOnInteract$delegate", "useSchedule", "getUseSchedule", "setUseSchedule", "useSchedule$delegate", "ghostMenuButton", "getGhostMenuButton", "setGhostMenuButton", "ghostMenuButton$delegate", "", "sendSilenceValue", "getSendSilenceValue", "()I", "setSendSilenceValue", "(I)V", "sendSilenceValue$delegate", "saveDeleted", "getSaveDeleted", "setSaveDeleted", "saveDeleted$delegate", "saveManuallyDeleted", "getSaveManuallyDeleted", "setSaveManuallyDeleted", "saveManuallyDeleted$delegate", "saveOneTime", "getSaveOneTime", "setSaveOneTime", "saveOneTime$delegate", "saveEdits", "getSaveEdits", "setSaveEdits", "saveEdits$delegate", "redDeletedMark", "getRedDeletedMark", "setRedDeletedMark", "redDeletedMark$delegate", "translucentDeleted", "getTranslucentDeleted", "setTranslucentDeleted", "translucentDeleted$delegate", "", "deletedMark", "getDeletedMark", "()Ljava/lang/String;", "setDeletedMark", "(Ljava/lang/String;)V", "deletedMark$delegate", "collapsedReplyQuote", "getCollapsedReplyQuote", "setCollapsedReplyQuote", "collapsedReplyQuote$delegate", "ignoreFlagSecure", "getIgnoreFlagSecure", "setIgnoreFlagSecure", "ignoreFlagSecure$delegate", "localPremium", "getLocalPremium", "setLocalPremium", "localPremium$delegate", "noForwards", "getNoForwards", "setNoForwards", "noForwards$delegate", "filtersEnabled", "getFiltersEnabled", "setFiltersEnabled", "filtersEnabled$delegate", "value", "Lni/shikatu/re_extera/settings/SendSilence;", "sendSilence", "getSendSilence", "()Lni/shikatu/re_extera/settings/SendSilence;", "setSendSilence", "(Lni/shikatu/re_extera/settings/SendSilence;)V", "ghostHideOnline", "getGhostHideOnline", "ghostImmediateOffline", "getGhostImmediateOffline", "ghostHideTyping", "getGhostHideTyping", "ghostHideReading", "getGhostHideReading", "ghostNoReadStories", "getGhostNoReadStories", "ghostOptionsCount", "getGhostOptionsCount", "sendSilently", "getSendSilently", "bool", "Lni/shikatu/re_extera/settings/ReSettings$Pref;", "key", "default", "int", "string", "Pref", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class ReSettings {
    static final /* synthetic */ KProperty<Object>[] $$delegatedProperties = {Reflection.mutableProperty1(new MutablePropertyReference1Impl(ReSettings.class, "ghostEnabled", "getGhostEnabled()Z", 0)), Reflection.mutableProperty1(new MutablePropertyReference1Impl(ReSettings.class, "hideOnline", "getHideOnline()Z", 0)), Reflection.mutableProperty1(new MutablePropertyReference1Impl(ReSettings.class, "immediateOffline", "getImmediateOffline()Z", 0)), Reflection.mutableProperty1(new MutablePropertyReference1Impl(ReSettings.class, "hideTyping", "getHideTyping()Z", 0)), Reflection.mutableProperty1(new MutablePropertyReference1Impl(ReSettings.class, "hideReading", "getHideReading()Z", 0)), Reflection.mutableProperty1(new MutablePropertyReference1Impl(ReSettings.class, "noReadStories", "getNoReadStories()Z", 0)), Reflection.mutableProperty1(new MutablePropertyReference1Impl(ReSettings.class, "readOnInteract", "getReadOnInteract()Z", 0)), Reflection.mutableProperty1(new MutablePropertyReference1Impl(ReSettings.class, "useSchedule", "getUseSchedule()Z", 0)), Reflection.mutableProperty1(new MutablePropertyReference1Impl(ReSettings.class, "ghostMenuButton", "getGhostMenuButton()Z", 0)), Reflection.mutableProperty1(new MutablePropertyReference1Impl(ReSettings.class, "sendSilenceValue", "getSendSilenceValue()I", 0)), Reflection.mutableProperty1(new MutablePropertyReference1Impl(ReSettings.class, "saveDeleted", "getSaveDeleted()Z", 0)), Reflection.mutableProperty1(new MutablePropertyReference1Impl(ReSettings.class, "saveManuallyDeleted", "getSaveManuallyDeleted()Z", 0)), Reflection.mutableProperty1(new MutablePropertyReference1Impl(ReSettings.class, "saveOneTime", "getSaveOneTime()Z", 0)), Reflection.mutableProperty1(new MutablePropertyReference1Impl(ReSettings.class, "saveEdits", "getSaveEdits()Z", 0)), Reflection.mutableProperty1(new MutablePropertyReference1Impl(ReSettings.class, "redDeletedMark", "getRedDeletedMark()Z", 0)), Reflection.mutableProperty1(new MutablePropertyReference1Impl(ReSettings.class, "translucentDeleted", "getTranslucentDeleted()Z", 0)), Reflection.mutableProperty1(new MutablePropertyReference1Impl(ReSettings.class, "deletedMark", "getDeletedMark()Ljava/lang/String;", 0)), Reflection.mutableProperty1(new MutablePropertyReference1Impl(ReSettings.class, "collapsedReplyQuote", "getCollapsedReplyQuote()Z", 0)), Reflection.mutableProperty1(new MutablePropertyReference1Impl(ReSettings.class, "ignoreFlagSecure", "getIgnoreFlagSecure()Z", 0)), Reflection.mutableProperty1(new MutablePropertyReference1Impl(ReSettings.class, "localPremium", "getLocalPremium()Z", 0)), Reflection.mutableProperty1(new MutablePropertyReference1Impl(ReSettings.class, "noForwards", "getNoForwards()Z", 0)), Reflection.mutableProperty1(new MutablePropertyReference1Impl(ReSettings.class, "filtersEnabled", "getFiltersEnabled()Z", 0))};
    public static final ReSettings INSTANCE;

    /* JADX INFO: renamed from: collapsedReplyQuote$delegate, reason: from kotlin metadata */
    private static final Pref collapsedReplyQuote;

    /* JADX INFO: renamed from: deletedMark$delegate, reason: from kotlin metadata */
    private static final Pref deletedMark;

    /* JADX INFO: renamed from: filtersEnabled$delegate, reason: from kotlin metadata */
    private static final Pref filtersEnabled;

    /* JADX INFO: renamed from: ghostEnabled$delegate, reason: from kotlin metadata */
    private static final Pref ghostEnabled;

    /* JADX INFO: renamed from: ghostMenuButton$delegate, reason: from kotlin metadata */
    private static final Pref ghostMenuButton;

    /* JADX INFO: renamed from: hideOnline$delegate, reason: from kotlin metadata */
    private static final Pref hideOnline;

    /* JADX INFO: renamed from: hideReading$delegate, reason: from kotlin metadata */
    private static final Pref hideReading;

    /* JADX INFO: renamed from: hideTyping$delegate, reason: from kotlin metadata */
    private static final Pref hideTyping;

    /* JADX INFO: renamed from: ignoreFlagSecure$delegate, reason: from kotlin metadata */
    private static final Pref ignoreFlagSecure;

    /* JADX INFO: renamed from: immediateOffline$delegate, reason: from kotlin metadata */
    private static final Pref immediateOffline;

    /* JADX INFO: renamed from: localPremium$delegate, reason: from kotlin metadata */
    private static final Pref localPremium;

    /* JADX INFO: renamed from: noForwards$delegate, reason: from kotlin metadata */
    private static final Pref noForwards;

    /* JADX INFO: renamed from: noReadStories$delegate, reason: from kotlin metadata */
    private static final Pref noReadStories;

    /* JADX INFO: renamed from: prefs$delegate, reason: from kotlin metadata */
    private static final Lazy prefs;

    /* JADX INFO: renamed from: readOnInteract$delegate, reason: from kotlin metadata */
    private static final Pref readOnInteract;

    /* JADX INFO: renamed from: redDeletedMark$delegate, reason: from kotlin metadata */
    private static final Pref redDeletedMark;

    /* JADX INFO: renamed from: saveDeleted$delegate, reason: from kotlin metadata */
    private static final Pref saveDeleted;

    /* JADX INFO: renamed from: saveEdits$delegate, reason: from kotlin metadata */
    private static final Pref saveEdits;

    /* JADX INFO: renamed from: saveManuallyDeleted$delegate, reason: from kotlin metadata */
    private static final Pref saveManuallyDeleted;

    /* JADX INFO: renamed from: saveOneTime$delegate, reason: from kotlin metadata */
    private static final Pref saveOneTime;

    /* JADX INFO: renamed from: sendSilenceValue$delegate, reason: from kotlin metadata */
    private static final Pref sendSilenceValue;

    /* JADX INFO: renamed from: translucentDeleted$delegate, reason: from kotlin metadata */
    private static final Pref translucentDeleted;

    /* JADX INFO: renamed from: useSchedule$delegate, reason: from kotlin metadata */
    private static final Pref useSchedule;

    /* JADX INFO: compiled from: ReSettings.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    public static final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[SendSilence.values().length];
            try {
                iArr[SendSilence.ALWAYS.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[SendSilence.ONLY_WITH_GHOST.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[SendSilence.NEVER.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    static {
        ReSettings reSettings = new ReSettings();
        INSTANCE = reSettings;
        prefs = LazyKt.lazy(new Function0() { // from class: ni.shikatu.re_extera.settings.ReSettings.1
            public final Object invoke() {
                return ReSettings.prefs_delegate$lambda$0();
            }
        });
        ghostEnabled = reSettings.bool("ghost_mode_enabled", true);
        hideOnline = reSettings.bool("hide_online", false);
        immediateOffline = reSettings.bool("immediate_offline", false);
        hideTyping = reSettings.bool("hide_typing", false);
        hideReading = reSettings.bool("hide_reading", false);
        noReadStories = reSettings.bool("no_read_stories", false);
        readOnInteract = reSettings.bool("read_on_interact", false);
        useSchedule = reSettings.bool("use_schedule", false);
        ghostMenuButton = reSettings.bool("add_ghost_to_drawer", true);
        sendSilenceValue = reSettings.m1int("send_silence", SendSilence.NEVER.getValue());
        saveDeleted = reSettings.bool("save_deleted_messages", false);
        saveManuallyDeleted = reSettings.bool("save_manually_deleted", false);
        saveOneTime = reSettings.bool("save_one_time_messages", false);
        saveEdits = reSettings.bool("save_edited_messages", false);
        redDeletedMark = reSettings.bool("red_mark", false);
        translucentDeleted = reSettings.bool("translucent_deleted", false);
        deletedMark = reSettings.string("custom_prefix", "");
        collapsedReplyQuote = reSettings.bool("use_expandable_blockquote", false);
        ignoreFlagSecure = reSettings.bool("remove_flag_secure", false);
        localPremium = reSettings.bool("local_premium", false);
        noForwards = reSettings.bool("no_forward", false);
        filtersEnabled = reSettings.bool("filters_enabled", false);
    }

    private ReSettings() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final SharedPreferences getPrefs() {
        Object value = prefs.getValue();
        Intrinsics.checkNotNullExpressionValue(value, "getValue(...)");
        return (SharedPreferences) value;
    }

    static final SharedPreferences prefs_delegate$lambda$0() {
        return ApplicationLoader.applicationContext.getSharedPreferences("re_extera", 0);
    }

    public final boolean getGhostEnabled() {
        return ((Boolean) ghostEnabled.getValue(this, $$delegatedProperties[0])).booleanValue();
    }

    public final void setGhostEnabled(boolean z) {
        ghostEnabled.setValue(this, $$delegatedProperties[0], Boolean.valueOf(z));
    }

    public final boolean getHideOnline() {
        return ((Boolean) hideOnline.getValue(this, $$delegatedProperties[1])).booleanValue();
    }

    public final void setHideOnline(boolean z) {
        hideOnline.setValue(this, $$delegatedProperties[1], Boolean.valueOf(z));
    }

    public final boolean getImmediateOffline() {
        return ((Boolean) immediateOffline.getValue(this, $$delegatedProperties[2])).booleanValue();
    }

    public final void setImmediateOffline(boolean z) {
        immediateOffline.setValue(this, $$delegatedProperties[2], Boolean.valueOf(z));
    }

    public final boolean getHideTyping() {
        return ((Boolean) hideTyping.getValue(this, $$delegatedProperties[3])).booleanValue();
    }

    public final void setHideTyping(boolean z) {
        hideTyping.setValue(this, $$delegatedProperties[3], Boolean.valueOf(z));
    }

    public final boolean getHideReading() {
        return ((Boolean) hideReading.getValue(this, $$delegatedProperties[4])).booleanValue();
    }

    public final void setHideReading(boolean z) {
        hideReading.setValue(this, $$delegatedProperties[4], Boolean.valueOf(z));
    }

    public final boolean getNoReadStories() {
        return ((Boolean) noReadStories.getValue(this, $$delegatedProperties[5])).booleanValue();
    }

    public final void setNoReadStories(boolean z) {
        noReadStories.setValue(this, $$delegatedProperties[5], Boolean.valueOf(z));
    }

    public final boolean getReadOnInteract() {
        return ((Boolean) readOnInteract.getValue(this, $$delegatedProperties[6])).booleanValue();
    }

    public final void setReadOnInteract(boolean z) {
        readOnInteract.setValue(this, $$delegatedProperties[6], Boolean.valueOf(z));
    }

    public final boolean getUseSchedule() {
        return ((Boolean) useSchedule.getValue(this, $$delegatedProperties[7])).booleanValue();
    }

    public final void setUseSchedule(boolean z) {
        useSchedule.setValue(this, $$delegatedProperties[7], Boolean.valueOf(z));
    }

    public final boolean getGhostMenuButton() {
        return ((Boolean) ghostMenuButton.getValue(this, $$delegatedProperties[8])).booleanValue();
    }

    public final void setGhostMenuButton(boolean z) {
        ghostMenuButton.setValue(this, $$delegatedProperties[8], Boolean.valueOf(z));
    }

    private final int getSendSilenceValue() {
        return ((Number) sendSilenceValue.getValue(this, $$delegatedProperties[9])).intValue();
    }

    private final void setSendSilenceValue(int i) {
        sendSilenceValue.setValue(this, $$delegatedProperties[9], Integer.valueOf(i));
    }

    public final boolean getSaveDeleted() {
        return ((Boolean) saveDeleted.getValue(this, $$delegatedProperties[10])).booleanValue();
    }

    public final void setSaveDeleted(boolean z) {
        saveDeleted.setValue(this, $$delegatedProperties[10], Boolean.valueOf(z));
    }

    public final boolean getSaveManuallyDeleted() {
        return ((Boolean) saveManuallyDeleted.getValue(this, $$delegatedProperties[11])).booleanValue();
    }

    public final void setSaveManuallyDeleted(boolean z) {
        saveManuallyDeleted.setValue(this, $$delegatedProperties[11], Boolean.valueOf(z));
    }

    public final boolean getSaveOneTime() {
        return ((Boolean) saveOneTime.getValue(this, $$delegatedProperties[12])).booleanValue();
    }

    public final void setSaveOneTime(boolean z) {
        saveOneTime.setValue(this, $$delegatedProperties[12], Boolean.valueOf(z));
    }

    public final boolean getSaveEdits() {
        return ((Boolean) saveEdits.getValue(this, $$delegatedProperties[13])).booleanValue();
    }

    public final void setSaveEdits(boolean z) {
        saveEdits.setValue(this, $$delegatedProperties[13], Boolean.valueOf(z));
    }

    public final boolean getRedDeletedMark() {
        return ((Boolean) redDeletedMark.getValue(this, $$delegatedProperties[14])).booleanValue();
    }

    public final void setRedDeletedMark(boolean z) {
        redDeletedMark.setValue(this, $$delegatedProperties[14], Boolean.valueOf(z));
    }

    public final boolean getTranslucentDeleted() {
        return ((Boolean) translucentDeleted.getValue(this, $$delegatedProperties[15])).booleanValue();
    }

    public final void setTranslucentDeleted(boolean z) {
        translucentDeleted.setValue(this, $$delegatedProperties[15], Boolean.valueOf(z));
    }

    public final String getDeletedMark() {
        return (String) deletedMark.getValue(this, $$delegatedProperties[16]);
    }

    public final void setDeletedMark(String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        deletedMark.setValue(this, $$delegatedProperties[16], str);
    }

    public final boolean getCollapsedReplyQuote() {
        return ((Boolean) collapsedReplyQuote.getValue(this, $$delegatedProperties[17])).booleanValue();
    }

    public final void setCollapsedReplyQuote(boolean z) {
        collapsedReplyQuote.setValue(this, $$delegatedProperties[17], Boolean.valueOf(z));
    }

    public final boolean getIgnoreFlagSecure() {
        return ((Boolean) ignoreFlagSecure.getValue(this, $$delegatedProperties[18])).booleanValue();
    }

    public final void setIgnoreFlagSecure(boolean z) {
        ignoreFlagSecure.setValue(this, $$delegatedProperties[18], Boolean.valueOf(z));
    }

    public final boolean getLocalPremium() {
        return ((Boolean) localPremium.getValue(this, $$delegatedProperties[19])).booleanValue();
    }

    public final void setLocalPremium(boolean z) {
        localPremium.setValue(this, $$delegatedProperties[19], Boolean.valueOf(z));
    }

    public final boolean getNoForwards() {
        return ((Boolean) noForwards.getValue(this, $$delegatedProperties[20])).booleanValue();
    }

    public final void setNoForwards(boolean z) {
        noForwards.setValue(this, $$delegatedProperties[20], Boolean.valueOf(z));
    }

    public final boolean getFiltersEnabled() {
        return ((Boolean) filtersEnabled.getValue(this, $$delegatedProperties[21])).booleanValue();
    }

    public final void setFiltersEnabled(boolean z) {
        filtersEnabled.setValue(this, $$delegatedProperties[21], Boolean.valueOf(z));
    }

    public final SendSilence getSendSilence() {
        return SendSilence.INSTANCE.of(getSendSilenceValue());
    }

    public final void setSendSilence(SendSilence sendSilence) {
        Intrinsics.checkNotNullParameter(sendSilence, "value");
        setSendSilenceValue(sendSilence.getValue());
    }

    public final boolean getGhostHideOnline() {
        return getGhostEnabled() && getHideOnline();
    }

    public final boolean getGhostImmediateOffline() {
        return getGhostEnabled() && getImmediateOffline();
    }

    public final boolean getGhostHideTyping() {
        return getGhostEnabled() && getHideTyping();
    }

    public final boolean getGhostHideReading() {
        return getGhostEnabled() && getHideReading();
    }

    public final boolean getGhostNoReadStories() {
        return getGhostEnabled() && getNoReadStories();
    }

    public final int getGhostOptionsCount() {
        List listListOf = CollectionsKt.listOf(new Boolean[]{Boolean.valueOf(getHideOnline()), Boolean.valueOf(getImmediateOffline()), Boolean.valueOf(getHideTyping()), Boolean.valueOf(getHideReading()), Boolean.valueOf(getNoReadStories())});
        int i = 0;
        if ((listListOf instanceof Collection) && listListOf.isEmpty()) {
            return 0;
        }
        Iterator it = listListOf.iterator();
        while (it.hasNext()) {
            if (((Boolean) it.next()).booleanValue() && (i = i + 1) < 0) {
                CollectionsKt.throwCountOverflow();
            }
        }
        return i;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: kotlin.NoWhenBranchMatchedException */
    public final boolean getSendSilently() throws NoWhenBranchMatchedException {
        int i = WhenMappings.$EnumSwitchMapping$0[getSendSilence().ordinal()];
        if (i == 1) {
            return true;
        }
        if (i == 2) {
            return getGhostEnabled();
        }
        if (i == 3) {
            return false;
        }
        throw new NoWhenBranchMatchedException();
    }

    /* JADX INFO: renamed from: ni.shikatu.re_extera.settings.ReSettings$bool$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: ReSettings.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    static final /* synthetic */ class C00061 extends FunctionReferenceImpl implements Function3<SharedPreferences, String, Boolean, Boolean> {
        public static final C00061 INSTANCE = new C00061();

        C00061() {
            super(3, SharedPreferences.class, "getBoolean", "getBoolean(Ljava/lang/String;Z)Z", 0);
        }

        public final Boolean invoke(SharedPreferences sharedPreferences, String str, boolean z) {
            Intrinsics.checkNotNullParameter(sharedPreferences, "p0");
            return Boolean.valueOf(sharedPreferences.getBoolean(str, z));
        }

        public /* bridge */ /* synthetic */ Object invoke(Object obj, Object obj2, Object obj3) {
            return invoke((SharedPreferences) obj, (String) obj2, ((Boolean) obj3).booleanValue());
        }
    }

    /* JADX INFO: renamed from: ni.shikatu.re_extera.settings.ReSettings$bool$2, reason: invalid class name */
    /* JADX INFO: compiled from: ReSettings.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    static final /* synthetic */ class AnonymousClass2 extends FunctionReferenceImpl implements Function3<SharedPreferences.Editor, String, Boolean, SharedPreferences.Editor> {
        public static final AnonymousClass2 INSTANCE = new AnonymousClass2();

        AnonymousClass2() {
            super(3, SharedPreferences.Editor.class, "putBoolean", "putBoolean(Ljava/lang/String;Z)Landroid/content/SharedPreferences$Editor;", 0);
        }

        public final SharedPreferences.Editor invoke(SharedPreferences.Editor editor, String str, boolean z) {
            Intrinsics.checkNotNullParameter(editor, "p0");
            return editor.putBoolean(str, z);
        }

        public /* bridge */ /* synthetic */ Object invoke(Object obj, Object obj2, Object obj3) {
            return invoke((SharedPreferences.Editor) obj, (String) obj2, ((Boolean) obj3).booleanValue());
        }
    }

    private final Pref<Boolean> bool(String key, boolean z) {
        return new Pref<>(key, Boolean.valueOf(z), C00061.INSTANCE, AnonymousClass2.INSTANCE);
    }

    /* JADX INFO: renamed from: ni.shikatu.re_extera.settings.ReSettings$int$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: ReSettings.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    static final /* synthetic */ class C00071 extends FunctionReferenceImpl implements Function3<SharedPreferences, String, Integer, Integer> {
        public static final C00071 INSTANCE = new C00071();

        C00071() {
            super(3, SharedPreferences.class, "getInt", "getInt(Ljava/lang/String;I)I", 0);
        }

        public final Integer invoke(SharedPreferences sharedPreferences, String str, int i) {
            Intrinsics.checkNotNullParameter(sharedPreferences, "p0");
            return Integer.valueOf(sharedPreferences.getInt(str, i));
        }

        public /* bridge */ /* synthetic */ Object invoke(Object obj, Object obj2, Object obj3) {
            return invoke((SharedPreferences) obj, (String) obj2, ((Number) obj3).intValue());
        }
    }

    /* JADX INFO: renamed from: ni.shikatu.re_extera.settings.ReSettings$int$2, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: ReSettings.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    static final /* synthetic */ class C00082 extends FunctionReferenceImpl implements Function3<SharedPreferences.Editor, String, Integer, SharedPreferences.Editor> {
        public static final C00082 INSTANCE = new C00082();

        C00082() {
            super(3, SharedPreferences.Editor.class, "putInt", "putInt(Ljava/lang/String;I)Landroid/content/SharedPreferences$Editor;", 0);
        }

        public final SharedPreferences.Editor invoke(SharedPreferences.Editor editor, String str, int i) {
            Intrinsics.checkNotNullParameter(editor, "p0");
            return editor.putInt(str, i);
        }

        public /* bridge */ /* synthetic */ Object invoke(Object obj, Object obj2, Object obj3) {
            return invoke((SharedPreferences.Editor) obj, (String) obj2, ((Number) obj3).intValue());
        }
    }

    /* JADX INFO: renamed from: int, reason: not valid java name */
    private final Pref<Integer> m1int(String key, int i) {
        return new Pref<>(key, Integer.valueOf(i), C00071.INSTANCE, C00082.INSTANCE);
    }

    /* JADX INFO: renamed from: ni.shikatu.re_extera.settings.ReSettings$string$2, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: ReSettings.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    static final /* synthetic */ class C00092 extends FunctionReferenceImpl implements Function3<SharedPreferences.Editor, String, String, SharedPreferences.Editor> {
        public static final C00092 INSTANCE = new C00092();

        C00092() {
            super(3, SharedPreferences.Editor.class, "putString", "putString(Ljava/lang/String;Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;", 0);
        }

        public final SharedPreferences.Editor invoke(SharedPreferences.Editor editor, String str, String str2) {
            Intrinsics.checkNotNullParameter(editor, "p0");
            return editor.putString(str, str2);
        }
    }

    private final Pref<String> string(String key, String str) {
        return new Pref<>(key, str, new Function3() { // from class: ni.shikatu.re_extera.settings.ReSettings.0
            public final Object invoke(Object obj, Object obj2, Object obj3) {
                return ReSettings.string$lambda$2((SharedPreferences) obj, (String) obj2, (String) obj3);
            }
        }, C00092.INSTANCE);
    }

    static final String string$lambda$2(SharedPreferences sharedPreferences, String str, String str2) {
        Intrinsics.checkNotNullParameter(sharedPreferences, "$this$Pref");
        Intrinsics.checkNotNullParameter(str, "k");
        Intrinsics.checkNotNullParameter(str2, "d");
        String string = sharedPreferences.getString(str, str2);
        return string == null ? str2 : string;
    }

    /* JADX INFO: compiled from: ReSettings.kt */
    @Metadata(d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0002\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0002\u0012\u0004\u0012\u0002H\u00010\u0003Ba\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00028\u0000\u0012#\u0010\u0007\u001a\u001f\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00000\b¢\u0006\u0002\b\n\u0012#\u0010\u000b\u001a\u001f\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\f0\b¢\u0006\u0002\b\n¢\u0006\u0004\b\r\u0010\u000eJ$\u0010\u0011\u001a\u00028\u00002\b\u0010\u0012\u001a\u0004\u0018\u00010\u00022\n\u0010\u0013\u001a\u0006\u0012\u0002\b\u00030\u0014H\u0096\u0002¢\u0006\u0002\u0010\u0015J,\u0010\u0016\u001a\u00020\u00172\b\u0010\u0012\u001a\u0004\u0018\u00010\u00022\n\u0010\u0013\u001a\u0006\u0012\u0002\b\u00030\u00142\u0006\u0010\u0018\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\u0019R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u00028\u0000X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u000fR+\u0010\u0007\u001a\u001f\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00000\b¢\u0006\u0002\b\nX\u0082\u0004¢\u0006\u0002\n\u0000R+\u0010\u000b\u001a\u001f\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\f0\b¢\u0006\u0002\b\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0010\u001a\u0004\u0018\u00018\u0000X\u0082\u000e¢\u0006\u0004\n\u0002\u0010\u000f¨\u0006\u001a"}, d2 = {"Lni/shikatu/re_extera/settings/ReSettings$Pref;", "T", "", "Lkotlin/properties/ReadWriteProperty;", "key", "", "default", "read", "Lkotlin/Function3;", "Landroid/content/SharedPreferences;", "Lkotlin/ExtensionFunctionType;", "write", "Landroid/content/SharedPreferences$Editor;", "<init>", "(Ljava/lang/String;Ljava/lang/Object;Lkotlin/jvm/functions/Function3;Lkotlin/jvm/functions/Function3;)V", "Ljava/lang/Object;", "cached", "getValue", "thisRef", "property", "Lkotlin/reflect/KProperty;", "(Ljava/lang/Object;Lkotlin/reflect/KProperty;)Ljava/lang/Object;", "setValue", "", "value", "(Ljava/lang/Object;Lkotlin/reflect/KProperty;Ljava/lang/Object;)V", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Pref<T> implements ReadWriteProperty<Object, T> {
        private volatile T cached;
        private final T default;
        private final String key;
        private final Function3<SharedPreferences, String, T, T> read;
        private final Function3<SharedPreferences.Editor, String, T, SharedPreferences.Editor> write;

        /* JADX WARN: Multi-variable type inference failed */
        public Pref(String str, T t, Function3<? super SharedPreferences, ? super String, ? super T, ? extends T> function3, Function3<? super SharedPreferences.Editor, ? super String, ? super T, ? extends SharedPreferences.Editor> function4) {
            Intrinsics.checkNotNullParameter(str, "key");
            Intrinsics.checkNotNullParameter(t, "default");
            Intrinsics.checkNotNullParameter(function3, "read");
            Intrinsics.checkNotNullParameter(function4, "write");
            this.key = str;
            this.default = t;
            this.read = function3;
            this.write = function4;
        }

        public T getValue(Object thisRef, KProperty<?> property) {
            Intrinsics.checkNotNullParameter(property, "property");
            T t = this.cached;
            if (t != null) {
                return t;
            }
            T t2 = (T) this.read.invoke(ReSettings.INSTANCE.getPrefs(), this.key, this.default);
            this.cached = t2;
            return t2;
        }

        public void setValue(Object thisRef, KProperty<?> property, T value) {
            Intrinsics.checkNotNullParameter(property, "property");
            Intrinsics.checkNotNullParameter(value, "value");
            this.cached = value;
            Function3<SharedPreferences.Editor, String, T, SharedPreferences.Editor> function3 = this.write;
            SharedPreferences.Editor editorEdit = ReSettings.INSTANCE.getPrefs().edit();
            Intrinsics.checkNotNullExpressionValue(editorEdit, "edit(...)");
            ((SharedPreferences.Editor) function3.invoke(editorEdit, this.key, value)).apply();
        }
    }
}
