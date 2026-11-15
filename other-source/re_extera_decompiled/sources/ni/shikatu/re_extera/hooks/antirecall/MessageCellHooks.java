package ni.shikatu.re_extera.hooks.antirecall;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import androidx.core.content.ContextCompat;
import com.exteragram.messenger.utils.text.LocaleUtils;
import de.robv.android.xposed.XC_MethodHook;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import ni.shikatu.extensions.FieldRef;
import ni.shikatu.extensions.HookUtilsKt;
import ni.shikatu.re_extera.data.DeletedMessagesStore;
import ni.shikatu.re_extera.hooks.HookerClass;
import ni.shikatu.re_extera.settings.ReSettings;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Components.ColoredImageSpan;

/* JADX INFO: compiled from: MessageCellHooks.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \u00162\u00020\u0001:\u0001\u0016B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u0011\u001a\u00020\u0012H\u0014J\u0010\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u0015H\u0002R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\"\u0010\n\u001a\t\u0018\u00010\u000b¢\u0006\u0002\b\f8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u000f\u0010\u0010\u001a\u0004\b\r\u0010\u000e¨\u0006\u0017"}, d2 = {"Lni/shikatu/re_extera/hooks/antirecall/MessageCellHooks;", "Lni/shikatu/re_extera/hooks/HookerClass;", "<init>", "()V", "timeString", "Lni/shikatu/extensions/FieldRef;", "Landroid/text/SpannableStringBuilder;", "timeTextWidth", "", "outboundsSaveCount", "deletedIcon", "Landroid/graphics/drawable/Drawable;", "Lorg/jspecify/annotations/Nullable;", "getDeletedIcon", "()Landroid/graphics/drawable/Drawable;", "deletedIcon$delegate", "Lkotlin/Lazy;", "setup", "", "appendDeletedMark", "cell", "Lorg/telegram/ui/Cells/ChatMessageCell;", "Companion", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class MessageCellHooks extends HookerClass {
    private static final Companion Companion = new Companion(null);

    @Deprecated
    public static final float DELETED_ALPHA = 0.5f;
    private final FieldRef<SpannableStringBuilder> timeString = new FieldRef<>(ChatMessageCell.class, "currentTimeString");
    private final FieldRef<Integer> timeTextWidth = new FieldRef<>(ChatMessageCell.class, "timeTextWidth");
    private int outboundsSaveCount = -1;

    /* JADX INFO: renamed from: deletedIcon$delegate, reason: from kotlin metadata */
    private final Lazy deletedIcon = LazyKt.lazy(new Function0() { // from class: ni.shikatu.re_extera.hooks.antirecall.MessageCellHooks.0
        public final Object invoke() {
            return MessageCellHooks.deletedIcon_delegate$lambda$0();
        }
    });

    private final Drawable getDeletedIcon() {
        return (Drawable) this.deletedIcon.getValue();
    }

    static final Drawable deletedIcon_delegate$lambda$0() {
        return ContextCompat.getDrawable(ApplicationLoader.applicationContext, R.drawable.msg_delete_filled);
    }

    @Override // ni.shikatu.re_extera.hooks.HookerClass
    protected void setup() {
        hookAfter(ChatMessageCell.class, "measureTime", new Class[]{MessageObject.class}, new Function1() { // from class: ni.shikatu.re_extera.hooks.antirecall.MessageCellHooks.1
            public final Object invoke(Object obj) {
                return MessageCellHooks.setup$lambda$1(MessageCellHooks.this, (XC_MethodHook.MethodHookParam) obj);
            }
        });
        hookAfter(ChatMessageCell.class, "setMessageObject", new Class[]{MessageObject.class, MessageObject.GroupedMessages.class, Boolean.TYPE, Boolean.TYPE, Boolean.TYPE, Boolean.TYPE}, new Function1() { // from class: ni.shikatu.re_extera.hooks.antirecall.MessageCellHooks.2
            public final Object invoke(Object obj) {
                return MessageCellHooks.setup$lambda$2((XC_MethodHook.MethodHookParam) obj);
            }
        });
        hookMethod(ChatMessageCell.class, "drawOutboundsContent", new Class[]{Canvas.class}, new Function1() { // from class: ni.shikatu.re_extera.hooks.antirecall.MessageCellHooks.3
            public final Object invoke(Object obj) {
                return MessageCellHooks.setup$lambda$3(MessageCellHooks.this, (XC_MethodHook.MethodHookParam) obj);
            }
        }, new Function1() { // from class: ni.shikatu.re_extera.hooks.antirecall.MessageCellHooks.4
            public final Object invoke(Object obj) {
                return MessageCellHooks.setup$lambda$4(MessageCellHooks.this, (XC_MethodHook.MethodHookParam) obj);
            }
        });
    }

    static final Unit setup$lambda$1(MessageCellHooks messageCellHooks, XC_MethodHook.MethodHookParam methodHookParam) throws IllegalAccessException {
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        MessageObject messageObject = (MessageObject) HookUtilsKt.arg(methodHookParam, 0);
        if (messageObject != null && DeletedMessagesStore.INSTANCE.isDeleted(messageObject)) {
            messageCellHooks.appendDeletedMark((ChatMessageCell) HookUtilsKt.self(methodHookParam));
            return Unit.INSTANCE;
        }
        return Unit.INSTANCE;
    }

    static final Unit setup$lambda$2(XC_MethodHook.MethodHookParam methodHookParam) {
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        boolean z = false;
        if (ReSettings.INSTANCE.getTranslucentDeleted() && DeletedMessagesStore.INSTANCE.isDeleted((MessageObject) HookUtilsKt.arg(methodHookParam, 0))) {
            z = true;
        }
        ((ChatMessageCell) HookUtilsKt.self(methodHookParam)).setTransitionAlpha(z ? 0.5f : 1.0f);
        return Unit.INSTANCE;
    }

    static final Unit setup$lambda$3(MessageCellHooks messageCellHooks, XC_MethodHook.MethodHookParam methodHookParam) {
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        float transitionAlpha = ((ChatMessageCell) HookUtilsKt.self(methodHookParam)).getTransitionAlpha();
        if (transitionAlpha < 1.0f) {
            messageCellHooks.outboundsSaveCount = ((Canvas) HookUtilsKt.arg(methodHookParam, 0)).saveLayerAlpha(null, (int) (transitionAlpha * 255.0f));
        }
        return Unit.INSTANCE;
    }

    static final Unit setup$lambda$4(MessageCellHooks messageCellHooks, XC_MethodHook.MethodHookParam methodHookParam) {
        Intrinsics.checkNotNullParameter(methodHookParam, "param");
        if (messageCellHooks.outboundsSaveCount < 0) {
            return Unit.INSTANCE;
        }
        ((Canvas) HookUtilsKt.arg(methodHookParam, 0)).restoreToCount(messageCellHooks.outboundsSaveCount);
        messageCellHooks.outboundsSaveCount = -1;
        return Unit.INSTANCE;
    }

    private final void appendDeletedMark(ChatMessageCell cell) throws IllegalAccessException {
        TextPaint textPaint;
        SpannableStringBuilder spannableStringBuilder;
        SpannableStringBuilder spannableStringBuilder2 = this.timeString.get(cell);
        if (spannableStringBuilder2 == null || (textPaint = Theme.chat_timePaint) == null) {
            return;
        }
        Integer numValueOf = ReSettings.INSTANCE.getRedDeletedMark() ? Integer.valueOf(cell.getThemedColor(Theme.key_color_red)) : null;
        String deletedMark = ReSettings.INSTANCE.getDeletedMark();
        if (deletedMark.length() > 0) {
            spannableStringBuilder = new SpannableStringBuilder(LocaleUtils.fullyFormatText(deletedMark));
        } else {
            Drawable deletedIcon = getDeletedIcon();
            if (deletedIcon == null) {
                return;
            }
            spannableStringBuilder = new SpannableStringBuilder("\u200b");
            ColoredImageSpan coloredImageSpan = new ColoredImageSpan(deletedIcon);
            coloredImageSpan.setRelativeSize(textPaint.getFontMetricsInt());
            if (numValueOf != null) {
                coloredImageSpan.setOverrideColor(numValueOf.intValue());
            }
            spannableStringBuilder.setSpan(coloredImageSpan, 0, spannableStringBuilder.length(), 33);
        }
        if (numValueOf != null) {
            spannableStringBuilder.setSpan(new ForegroundColorSpan(numValueOf.intValue()), 0, spannableStringBuilder.length(), 33);
        }
        spannableStringBuilder.append(' ');
        SpannableStringBuilder spannableStringBuilder3 = spannableStringBuilder;
        spannableStringBuilder2.insert(0, (CharSequence) spannableStringBuilder3);
        int iCeil = (int) Math.ceil(Layout.getDesiredWidth(spannableStringBuilder3, textPaint));
        FieldRef<Integer> fieldRef = this.timeTextWidth;
        Integer num = fieldRef.get(cell);
        fieldRef.set(cell, Integer.valueOf((num != null ? num.intValue() : 0) + iCeil));
        cell.timeWidth += iCeil;
    }

    /* JADX INFO: compiled from: MessageCellHooks.kt */
    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0000\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0006"}, d2 = {"Lni/shikatu/re_extera/hooks/antirecall/MessageCellHooks$Companion;", "", "<init>", "()V", "DELETED_ALPHA", "", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
