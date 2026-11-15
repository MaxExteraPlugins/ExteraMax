package ni.shikatu.re_extera.ui.messages;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.GridLayoutManagerFixed;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.ranges.RangesKt;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import kotlin.text.StringsKt;
import ni.shikatu.re_extera.data.EditHistoryStore;
import ni.shikatu.re_extera.localization.Strings;
import ni.shikatu.re_extera.ui.Confirm;
import ni.shikatu.re_extera.utils.ChatUtils;
import ni.shikatu.re_extera.utils.CopyForwardMenu;
import ni.shikatu.re_extera.utils.TelegramStorage;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.messenger.utils.tlutils.TLKeyboardHelper;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_keyboard;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.BackDrawable;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.INavigationLayout;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ArticleViewer;
import org.telegram.ui.Cells.ChatActionCell;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Cells.TextSelectionHelper;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.ChatAvatarContainer;
import org.telegram.ui.Components.EmbedBottomSheet;
import org.telegram.ui.Components.ItemOptions;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.MessageBackgroundDrawable;
import org.telegram.ui.Components.NumberTextView;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.StickersAlert;
import org.telegram.ui.Components.URLSpanBotCommand;
import org.telegram.ui.Components.URLSpanMono;
import org.telegram.ui.Components.URLSpanNoUnderline;
import org.telegram.ui.Components.URLSpanReplacement;
import org.telegram.ui.Components.URLSpanUserMention;
import org.telegram.ui.Components.blur3.BlurredBackgroundDrawableViewFactory;
import org.telegram.ui.Components.blur3.drawable.color.BlurredBackgroundColorProviderThemed;
import org.telegram.ui.Components.blur3.source.BlurredBackgroundSource;
import org.telegram.ui.Components.blur3.source.BlurredBackgroundSourceColor;
import org.telegram.ui.Components.chat.layouts.ButtonOnClickListener;
import org.telegram.ui.Components.chat.layouts.ChatActivitySideControlsButtonsLayout;
import org.telegram.ui.LocationActivity;
import org.telegram.ui.PhotoViewer;

/* JADX INFO: compiled from: MessageListFragment.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000¿\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0002\b\u0013\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u001e\n\u0002\b\u001d\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005*\u0001]\b&\u0018\u0000 ì\u00012\u00020\u00012\u00020\u00022\u00020\u0003:\bé\u0001ê\u0001ë\u0001ì\u0001B\u000f\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\"\u0010C\u001a\u00020D2\u0018\u0010E\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020G06\u0012\u0004\u0012\u00020D0FH$J\u0010\u0010H\u001a\u00020I2\u0006\u0010J\u001a\u00020(H$J\u0010\u0010T\u001a\u00020D2\u0006\u0010U\u001a\u00020GH\u0014J\u0012\u0010V\u001a\u0004\u0018\u00010I2\u0006\u0010U\u001a\u00020.H\u0014J\u0016\u0010W\u001a\u00020D2\f\u0010X\u001a\b\u0012\u0004\u0012\u00020.06H\u0014J\b\u0010Y\u001a\u00020DH\u0014J\b\u0010_\u001a\u00020&H\u0016J\b\u0010`\u001a\u00020DH\u0016J\u0010\u0010a\u001a\u00020b2\u0006\u0010c\u001a\u00020dH\u0015J\u0010\u0010e\u001a\u00020D2\u0006\u0010c\u001a\u00020dH\u0002J\u0010\u0010f\u001a\u00020D2\u0006\u0010g\u001a\u00020(H\u0002J\u0018\u0010h\u001a\u00020\r2\u0006\u0010c\u001a\u00020d2\u0006\u0010i\u001a\u00020 H\u0002J\u0010\u0010j\u001a\u00020b2\u0006\u0010c\u001a\u00020dH\u0002J\u0010\u0010k\u001a\u00020b2\u0006\u0010c\u001a\u00020dH\u0002J\u0010\u0010l\u001a\u00020&2\u0006\u0010m\u001a\u00020&H\u0016J\u0012\u0010n\u001a\u00020&2\b\u0010o\u001a\u0004\u0018\u00010pH\u0016J\b\u0010q\u001a\u00020DH\u0004J\u0016\u0010r\u001a\u00020D2\f\u0010s\u001a\b\u0012\u0004\u0012\u00020.0tH\u0004J\u0010\u0010u\u001a\u00020D2\u0006\u0010v\u001a\u00020$H\u0003J\b\u0010w\u001a\u00020DH\u0002J\b\u0010x\u001a\u00020DH\u0002J\b\u0010y\u001a\u00020DH\u0002J\u0010\u0010z\u001a\u00020D2\u0006\u0010{\u001a\u00020&H\u0002J\b\u0010|\u001a\u00020DH\u0002J\b\u0010}\u001a\u00020DH\u0002J\u0010\u0010~\u001a\u00020D2\u0006\u0010g\u001a\u00020(H\u0004J\u0018\u0010~\u001a\u00020D2\u0006\u0010U\u001a\u00020.2\u0006\u0010\u007f\u001a\u00020&H\u0002J\u0012\u0010\u0080\u0001\u001a\u00020D2\u0007\u0010\u0081\u0001\u001a\u00020(H\u0002J\u0011\u0010\u0082\u0001\u001a\u00020D2\u0006\u0010{\u001a\u00020&H\u0002J\u0012\u0010\u0083\u0001\u001a\u00020D2\u0007\u0010\u0084\u0001\u001a\u000204H\u0002J\u0012\u0010\u0085\u0001\u001a\u00020D2\u0007\u0010\u0086\u0001\u001a\u00020(H\u0002J\t\u0010\u0087\u0001\u001a\u00020DH\u0002J\u001a\u0010\u0088\u0001\u001a\u00020&2\u0006\u0010U\u001a\u00020.2\u0007\u0010\u0084\u0001\u001a\u000204H\u0002J\u0012\u0010\u0089\u0001\u001a\u00020D2\u0007\u0010\u008a\u0001\u001a\u000204H\u0002J\u0011\u0010\u008b\u0001\u001a\u00020D2\u0006\u0010U\u001a\u00020.H\u0002J\t\u0010\u008c\u0001\u001a\u00020DH\u0002J\u0012\u0010\u008d\u0001\u001a\u00020D2\u0007\u0010\u008e\u0001\u001a\u00020&H\u0002J\u0012\u0010\u008f\u0001\u001a\u00020D2\u0007\u0010\u008e\u0001\u001a\u00020&H\u0002J\u001c\u0010\u0090\u0001\u001a\u00020D2\b\u0010\u0091\u0001\u001a\u00030\u0092\u00012\u0007\u0010\u008e\u0001\u001a\u00020&H\u0002J\u000f\u0010\u0093\u0001\u001a\b\u0012\u0004\u0012\u00020.06H\u0002J\u0018\u0010\u0094\u0001\u001a\u00020D2\r\u0010\u0095\u0001\u001a\b\u0012\u0004\u0012\u00020.06H\u0002J\u001f\u0010\u0096\u0001\u001a\u0004\u0018\u00010I2\u0006\u0010U\u001a\u00020.2\n\u0010\u0097\u0001\u001a\u0005\u0018\u00010\u0098\u0001H\u0002J\u0017\u0010\u0099\u0001\u001a\u00020D2\f\u0010X\u001a\b\u0012\u0004\u0012\u00020.06H\u0002J\u001b\u0010\u009a\u0001\u001a\u00020D2\u0007\u0010\u008a\u0001\u001a\u00020I2\u0007\u0010\u009b\u0001\u001a\u00020(H\u0002J\u0011\u0010\u009c\u0001\u001a\u0002042\u0006\u0010U\u001a\u00020.H\u0002J\u0013\u0010\u009d\u0001\u001a\u00020D2\b\u0010\u0091\u0001\u001a\u00030\u0092\u0001H\u0002J\u0012\u0010\u009e\u0001\u001a\u00020D2\u0007\u0010\u009f\u0001\u001a\u00020(H\u0002J\u001a\u0010 \u0001\u001a\u00020D2\u0006\u0010U\u001a\u00020.2\u0007\u0010¡\u0001\u001a\u00020&H\u0002J\u0013\u0010¢\u0001\u001a\u00020D2\b\u0010\u0091\u0001\u001a\u00030\u0092\u0001H\u0002J\u001c\u0010£\u0001\u001a\u00020D2\b\u0010\u0091\u0001\u001a\u00030\u0092\u00012\u0007\u0010¤\u0001\u001a\u000204H\u0002J;\u0010¥\u0001\u001a\u00020D2\u0006\u0010g\u001a\u00020(2\u0007\u0010¦\u0001\u001a\u00020(2\u0019\u0010\u0004\u001a\u000e\u0012\t\b\u0001\u0012\u0005\u0018\u00010¨\u00010§\u0001\"\u0005\u0018\u00010¨\u0001H\u0016¢\u0006\u0003\u0010©\u0001J\u001b\u0010ª\u0001\u001a\u00020D2\u0007\u0010«\u0001\u001a\u00020\r2\u0007\u0010¬\u0001\u001a\u00020&H\u0002J\u000b\u0010\u00ad\u0001\u001a\u0004\u0018\u00010 H\u0016J\t\u0010®\u0001\u001a\u00020&H\u0016J\t\u0010¯\u0001\u001a\u00020&H\u0016J\t\u0010°\u0001\u001a\u00020&H\u0016J\u0013\u0010±\u0001\u001a\u00020&2\b\u0010U\u001a\u0004\u0018\u00010.H\u0016J\u0015\u0010²\u0001\u001a\u00020&2\n\u0010\u0091\u0001\u001a\u0005\u0018\u00010\u0092\u0001H\u0016J\u0014\u0010³\u0001\u001a\u00020&2\t\u0010´\u0001\u001a\u0004\u0018\u00010.H\u0016J>\u0010µ\u0001\u001a\u00020D2\n\u0010\u0091\u0001\u001a\u0005\u0018\u00010\u0092\u00012\n\u0010¶\u0001\u001a\u0005\u0018\u00010·\u00012\b\u0010¸\u0001\u001a\u00030¹\u00012\b\u0010º\u0001\u001a\u00030¹\u00012\u0007\u0010»\u0001\u001a\u00020&H\u0016JG\u0010¼\u0001\u001a\u00020D2\n\u0010\u0091\u0001\u001a\u0005\u0018\u00010\u0092\u00012\n\u0010½\u0001\u001a\u0005\u0018\u00010¾\u00012\u0007\u0010¿\u0001\u001a\u00020(2\b\u0010¸\u0001\u001a\u00030¹\u00012\b\u0010º\u0001\u001a\u00030¹\u00012\u0007\u0010»\u0001\u001a\u00020&H\u0016J \u0010À\u0001\u001a\u00020D2\n\u0010\u0091\u0001\u001a\u0005\u0018\u00010\u0092\u00012\t\u0010Á\u0001\u001a\u0004\u0018\u000104H\u0016J'\u0010Â\u0001\u001a\u00020D2\b\u0010\u0091\u0001\u001a\u00030\u0092\u00012\b\u0010Ã\u0001\u001a\u00030¹\u00012\b\u0010Ä\u0001\u001a\u00030¹\u0001H\u0016J'\u0010Å\u0001\u001a\u00020D2\b\u0010\u0091\u0001\u001a\u00030\u0092\u00012\b\u0010Æ\u0001\u001a\u00030¹\u00012\b\u0010Ç\u0001\u001a\u00030¹\u0001H\u0016J:\u0010È\u0001\u001a\u00020D2\n\u0010\u0091\u0001\u001a\u0005\u0018\u00010\u0092\u00012\u0006\u0010g\u001a\u00020(2\b\u0010Ã\u0001\u001a\u00030¹\u00012\b\u0010Ä\u0001\u001a\u00030¹\u00012\u0007\u0010É\u0001\u001a\u00020&H\u0016J\u0015\u0010Ê\u0001\u001a\u00020D2\n\u0010\u0091\u0001\u001a\u0005\u0018\u00010\u0092\u0001H\u0016J!\u0010Ë\u0001\u001a\u00020D2\n\u0010\u0091\u0001\u001a\u0005\u0018\u00010\u0092\u00012\n\u0010Ì\u0001\u001a\u0005\u0018\u00010Í\u0001H\u0016J&\u0010Î\u0001\u001a\u00020D2\b\u0010\u0091\u0001\u001a\u00030\u0092\u00012\b\u0010¤\u0001\u001a\u00030Ï\u00012\u0007\u0010Ð\u0001\u001a\u00020&H\u0016J5\u0010Ñ\u0001\u001a\u00020D2\n\u0010\u0091\u0001\u001a\u0005\u0018\u00010\u0092\u00012\n\u0010Ò\u0001\u001a\u0005\u0018\u00010Ó\u00012\t\u0010¤\u0001\u001a\u0004\u0018\u0001042\u0007\u0010Ô\u0001\u001a\u00020&H\u0016J!\u0010Õ\u0001\u001a\u00020D2\n\u0010\u0091\u0001\u001a\u0005\u0018\u00010\u0092\u00012\n\u0010Ö\u0001\u001a\u0005\u0018\u00010×\u0001H\u0016J'\u0010Ø\u0001\u001a\u00020&2\n\u0010\u0091\u0001\u001a\u0005\u0018\u00010\u0092\u00012\u0007\u0010Ù\u0001\u001a\u00020.2\u0007\u0010Ú\u0001\u001a\u00020&H\u0016JQ\u0010Û\u0001\u001a\u00020D2\b\u0010U\u001a\u0004\u0018\u00010.2\t\u0010¤\u0001\u001a\u0004\u0018\u0001042\t\u0010Ü\u0001\u001a\u0004\u0018\u0001042\t\u0010Ý\u0001\u001a\u0004\u0018\u0001042\t\u0010Þ\u0001\u001a\u0004\u0018\u0001042\u0007\u0010ß\u0001\u001a\u00020(2\u0007\u0010à\u0001\u001a\u00020(H\u0016J\u001c\u0010á\u0001\u001a\u00020D2\b\u0010\u0091\u0001\u001a\u00030\u0092\u00012\u0007\u0010â\u0001\u001a\u00020(H\u0016J0\u0010ã\u0001\u001a\u00020D2\b\u0010\u0091\u0001\u001a\u00030\u0092\u00012\b\u0010Ã\u0001\u001a\u00030¹\u00012\b\u0010Ä\u0001\u001a\u00030¹\u00012\u0007\u0010ä\u0001\u001a\u00020&H\u0016J\u0011\u0010å\u0001\u001a\u00020D2\u0006\u0010U\u001a\u00020.H\u0002J\u0016\u0010æ\u0001\u001a\n\u0012\u0005\u0012\u00030\u0092\u00010ç\u0001*\u00030è\u0001H\u0002R\u0014\u0010\b\u001a\u00020\t8DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0016\u001a\u0004\u0018\u00010\u0017X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u0019X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u001a\u001a\u0004\u0018\u00010\u0013X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u001b\u001a\u0004\u0018\u00010\u001cX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u001d\u001a\u0004\u0018\u00010\u001eX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u001f\u001a\u0004\u0018\u00010 X\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u0010!\u001a\u00060\"R\u00020\u0000X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020$X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020&X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020(X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010)\u001a\u0004\u0018\u00010*X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020(X\u0082\u000e¢\u0006\u0002\n\u0000R\u001e\u0010,\u001a\u0012\u0012\u0004\u0012\u00020.0-j\b\u0012\u0004\u0012\u00020.`/X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u00100\u001a\u00020&8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b1\u00102R\u000e\u00103\u001a\u000204X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u00105\u001a\b\u0012\u0004\u0012\u00020.06X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u00107\u001a\u00020(X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u00108\u001a\u0004\u0018\u00010.X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u00109\u001a\u00020:X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010;\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010<\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010=\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010>\u001a\u00020&X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010?\u001a\u00020:X\u0082\u0004¢\u0006\u0002\n\u0000R\u0012\u0010@\u001a\u000204X¤\u0004¢\u0006\u0006\u001a\u0004\bA\u0010BR\u0014\u0010K\u001a\u00020I8TX\u0094\u0004¢\u0006\u0006\u001a\u0004\bL\u0010MR\u0014\u0010N\u001a\u00020&8TX\u0094\u0004¢\u0006\u0006\u001a\u0004\bO\u00102R\u0014\u0010P\u001a\u00020&8TX\u0094\u0004¢\u0006\u0006\u001a\u0004\bQ\u00102R\u0014\u0010R\u001a\u00020&8TX\u0094\u0004¢\u0006\u0006\u001a\u0004\bS\u00102R\u001a\u0010X\u001a\b\u0012\u0004\u0012\u00020.068DX\u0084\u0004¢\u0006\u0006\u001a\u0004\bZ\u0010[R\u0010\u0010\\\u001a\u00020]X\u0082\u0004¢\u0006\u0004\n\u0002\u0010^¨\u0006í\u0001"}, d2 = {"Lni/shikatu/re_extera/ui/messages/MessageListFragment;", "Lorg/telegram/ui/ActionBar/BaseFragment;", "Lorg/telegram/ui/Cells/ChatMessageCell$ChatMessageCellDelegate;", "Lorg/telegram/messenger/NotificationCenter$NotificationCenterDelegate;", "args", "Landroid/os/Bundle;", "<init>", "(Landroid/os/Bundle;)V", "dialogId", "", "getDialogId", "()J", "chatListView", "Lorg/telegram/ui/Components/RecyclerListView;", "layoutManager", "Landroidx/recyclerview/widget/GridLayoutManagerFixed;", "avatarContainer", "Lorg/telegram/ui/Components/ChatAvatarContainer;", "emptyView", "Landroid/widget/TextView;", "floatingDateView", "Lorg/telegram/ui/Cells/ChatActionCell;", "sideButtons", "Lorg/telegram/ui/Components/chat/layouts/ChatActivitySideControlsButtonsLayout;", "searchPanel", "Landroid/widget/FrameLayout;", "searchCountText", "selectedCountView", "Lorg/telegram/ui/Components/NumberTextView;", "copyItem", "Lorg/telegram/ui/ActionBar/ActionBarMenuItem;", "selectionHelper", "Lorg/telegram/ui/Cells/TextSelectionHelper$ChatListTextSelectionHelper;", "adapter", "Lni/shikatu/re_extera/ui/messages/MessageListFragment$MessagesAdapter;", "data", "Lni/shikatu/re_extera/ui/messages/MessageRows;", "loading", "", "generation", "", "observers", "Lorg/telegram/messenger/NotificationCenter$ObserversGroup;", "listTopInset", "selected", "Ljava/util/LinkedHashSet;", "Lorg/telegram/messenger/MessageObject;", "Lkotlin/collections/LinkedHashSet;", "selecting", "getSelecting", "()Z", "searchQuery", "", "searchResults", "", "searchIndex", "highlighted", "clearHighlight", "Ljava/lang/Runnable;", "lastMenuTime", "lastLongPressTime", "selectionAnimationUntil", "floatingDateVisible", "hideFloatingDate", "fallbackTitle", "getFallbackTitle", "()Ljava/lang/String;", "loadMessages", "", "onLoaded", "Lkotlin/Function1;", "Lorg/telegram/tgnet/TLRPC$Message;", "subtitle", "", "count", "emptyText", "getEmptyText", "()Ljava/lang/CharSequence;", "groupAlbums", "getGroupAlbums", "canDelete", "getCanDelete", "canOpenEditHistory", "getCanOpenEditHistory", "prepare", "message", "menuFooter", "deleteMessages", "messages", "clearAll", "getMessages", "()Ljava/util/List;", "photoProvider", "ni/shikatu/re_extera/ui/messages/MessageListFragment$photoProvider$1", "Lni/shikatu/re_extera/ui/messages/MessageListFragment$photoProvider$1;", "onFragmentCreate", "onFragmentDestroy", "createView", "Landroid/view/View;", "context", "Landroid/content/Context;", "setupActionBar", "onMenuItemClick", "id", "createListView", "helper", "createSearchPanel", "createSideButtons", "onBackPressed", "invoked", "isSwipeBackEnabled", "event", "Landroid/view/MotionEvent;", "reload", "removeMessages", "removed", "", "applyRows", "rows", "updateHeader", "updatePageDownButton", "scrollToBottom", "showFloatingDate", "show", "updateFloatingDate", "showJumpToDate", "scrollToMessage", "highlight", "showInChat", "messageId", "showSearchPanel", "setSearchQuery", "query", "moveSearch", "delta", "updateSearchPanel", "matches", "openSearch", "text", "toggleSelection", "clearSelection", "updateSelectionCount", "animated", "updateSelectionCells", "bindSelection", "cell", "Lorg/telegram/ui/Cells/ChatMessageCell;", "selectedInOrder", "confirmDelete", "targets", "copyText", "group", "Lorg/telegram/messenger/MessageObject$GroupedMessages;", "copyMessages", "copyToClipboard", "toast", "senderName", "onMessageLongPress", "startMultiselect", "position", "setSelected", "value", "showMessageMenu", "showLinkMenu", "url", "didReceivedNotification", "account", "", "", "(II[Ljava/lang/Object;)V", "updatePlayback", "list", "started", "getTextSelectionHelper", "hasSelectedMessages", "canPerformActions", "canDrawOutboundsContent", "shouldRepeatSticker", "shouldDrawAvatarOnlineStatus", "doNotShowLoadingReply", "msg", "didPressUserAvatar", "user", "Lorg/telegram/tgnet/TLRPC$User;", "touchX", "", "touchY", "asForward", "didPressChannelAvatar", "chat", "Lorg/telegram/tgnet/TLRPC$Chat;", "postId", "didPressViaBot", "username", "didLongPress", "x", "y", "didPressOther", "otherX", "otherY", "didPressReplyMessage", "longpress", "didPressHiddenForward", "didPressCodeCopy", "block", "Lorg/telegram/messenger/MessageObject$TextLayoutBlock;", "didPressUrl", "Landroid/text/style/CharacterStyle;", "longPress", "didPressWebPage", "webpage", "Lorg/telegram/tgnet/TLRPC$WebPage;", "safe", "didPressBotButton", "button", "Lorg/telegram/tgnet/tl/TL_keyboard$KeyboardButtonProto;", "needPlayMessage", "messageObject", "muted", "needOpenWebView", "title", "description", "originalUrl", "w", "h", "didPressInstantButton", "type", "didPressImage", "fullPreview", "openMedia", "cells", "Lkotlin/sequences/Sequence;", "Landroidx/recyclerview/widget/RecyclerView;", "GroupOffsetsDecoration", "AvatarRecyclerView", "MessagesAdapter", "Companion", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public abstract class MessageListFragment extends BaseFragment implements ChatMessageCell.ChatMessageCellDelegate, NotificationCenter.NotificationCenterDelegate {
    public static final String ARG_DIALOG_ID = "did";
    private static final int ID_CLEAR = 4;
    private static final int ID_COPY = 10;
    private static final int ID_DELETE = 13;
    private static final int ID_FORWARD = 12;
    private static final int ID_JUMP_TO_DATE = 3;
    private static final int ID_OTHER = 2;
    private static final int ID_SAVE = 11;
    private static final int ID_SEARCH = 1;
    private static final int SEARCH_PANEL_HEIGHT = 51;
    private static final long SELECTION_ANIMATION_MS = 400;
    private static final int TYPE_DATE = 1;
    private static final int TYPE_MESSAGE = 0;
    private final MessagesAdapter adapter;
    private ChatAvatarContainer avatarContainer;
    private RecyclerListView chatListView;
    private final Runnable clearHighlight;
    private ActionBarMenuItem copyItem;
    private MessageRows data;
    private TextView emptyView;
    private ChatActionCell floatingDateView;
    private boolean floatingDateVisible;
    private int generation;
    private final Runnable hideFloatingDate;
    private MessageObject highlighted;
    private long lastLongPressTime;
    private long lastMenuTime;
    private GridLayoutManagerFixed layoutManager;
    private int listTopInset;
    private boolean loading;
    private NotificationCenter.ObserversGroup observers;
    private final MessageListFragment$photoProvider$1 photoProvider;
    private TextView searchCountText;
    private int searchIndex;
    private FrameLayout searchPanel;
    private String searchQuery;
    private List<? extends MessageObject> searchResults;
    private final LinkedHashSet<MessageObject> selected;
    private NumberTextView selectedCountView;
    private long selectionAnimationUntil;
    private TextSelectionHelper.ChatListTextSelectionHelper selectionHelper;
    private ChatActivitySideControlsButtonsLayout sideButtons;

    static final boolean setupActionBar$lambda$10$lambda$9(View view, MotionEvent motionEvent) {
        return true;
    }

    public boolean canDrawOutboundsContent() {
        return true;
    }

    protected void clearAll() {
    }

    protected void deleteMessages(List<? extends MessageObject> messages) {
        Intrinsics.checkNotNullParameter(messages, "messages");
    }

    public boolean doNotShowLoadingReply(MessageObject msg) {
        return true;
    }

    protected boolean getCanDelete() {
        return false;
    }

    protected boolean getCanOpenEditHistory() {
        return false;
    }

    protected abstract String getFallbackTitle();

    protected boolean getGroupAlbums() {
        return true;
    }

    protected abstract void loadMessages(Function1<? super List<? extends TLRPC.Message>, Unit> onLoaded);

    protected CharSequence menuFooter(MessageObject message) {
        Intrinsics.checkNotNullParameter(message, "message");
        return null;
    }

    protected void prepare(TLRPC.Message message) {
        Intrinsics.checkNotNullParameter(message, "message");
    }

    public boolean shouldDrawAvatarOnlineStatus(ChatMessageCell cell) {
        return true;
    }

    public boolean shouldRepeatSticker(MessageObject message) {
        return true;
    }

    protected abstract CharSequence subtitle(int count);

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Type inference failed for: r2v10, types: [ni.shikatu.re_extera.ui.messages.MessageListFragment$photoProvider$1] */
    public MessageListFragment(Bundle bundle) {
        super(bundle);
        Intrinsics.checkNotNullParameter(bundle, "args");
        this.adapter = new MessagesAdapter();
        this.data = MessageRows.INSTANCE.getEMPTY();
        this.selected = new LinkedHashSet<>();
        this.searchQuery = "";
        this.searchResults = CollectionsKt.emptyList();
        this.searchIndex = -1;
        this.clearHighlight = new Runnable() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.25
            @Override // java.lang.Runnable
            public final void run() {
                MessageListFragment.clearHighlight$lambda$1(MessageListFragment.this);
            }
        };
        this.hideFloatingDate = new Runnable() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.26
            @Override // java.lang.Runnable
            public final void run() {
                MessageListFragment.this.showFloatingDate(false);
            }
        };
        this.photoProvider = new PhotoViewer.EmptyPhotoViewerProvider() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment$photoProvider$1
            public PhotoViewer.PlaceProviderObject getPlaceForPhoto(MessageObject messageObject, TLRPC.FileLocation fileLocation, int index, boolean needPreview, boolean closing) {
                Object next;
                ImageReceiver photoImage;
                MessageObject messageObject2;
                View view = this.this$0.chatListView;
                if (view == null) {
                    return null;
                }
                Iterator it = this.this$0.cells((RecyclerView) view).iterator();
                do {
                    if (!it.hasNext()) {
                        next = null;
                        break;
                    }
                    next = it.next();
                    ChatMessageCell chatMessageCell = (ChatMessageCell) next;
                    if (chatMessageCell.getMessageObject() == messageObject) {
                        break;
                    }
                    messageObject2 = chatMessageCell.getMessageObject();
                } while (!Intrinsics.areEqual(messageObject2 != null ? Integer.valueOf(messageObject2.getId()) : null, messageObject != null ? Integer.valueOf(messageObject.getId()) : null));
                ChatMessageCell chatMessageCell2 = (ChatMessageCell) next;
                if (chatMessageCell2 == null || (photoImage = chatMessageCell2.getPhotoImage()) == null) {
                    return null;
                }
                int[] iArr = new int[2];
                chatMessageCell2.getLocationInWindow(iArr);
                PhotoViewer.PlaceProviderObject placeProviderObject = new PhotoViewer.PlaceProviderObject();
                placeProviderObject.viewX = iArr[0];
                placeProviderObject.viewY = iArr[1];
                placeProviderObject.parentView = view;
                placeProviderObject.imageReceiver = photoImage;
                placeProviderObject.thumb = photoImage.getBitmapSafe();
                placeProviderObject.radius = photoImage.getRoundRadius(true);
                return placeProviderObject;
            }
        };
    }

    protected final long getDialogId() {
        Bundle bundle = this.arguments;
        if (bundle != null) {
            return bundle.getLong(ARG_DIALOG_ID);
        }
        return 0L;
    }

    private final boolean getSelecting() {
        return !this.selected.isEmpty();
    }

    static final void clearHighlight$lambda$1(MessageListFragment messageListFragment) {
        Sequence<ChatMessageCell> sequenceCells;
        messageListFragment.highlighted = null;
        RecyclerListView recyclerListView = messageListFragment.chatListView;
        if (recyclerListView == null || (sequenceCells = messageListFragment.cells((RecyclerView) recyclerListView)) == null) {
            return;
        }
        Iterator it = sequenceCells.iterator();
        while (it.hasNext()) {
            ((ChatMessageCell) it.next()).setHighlighted(false);
        }
    }

    protected CharSequence getEmptyText() {
        String string = LocaleController.getString(R.string.NoMessages);
        Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
        return string;
    }

    protected final List<MessageObject> getMessages() {
        return this.data.getMessages();
    }

    public boolean onFragmentCreate() {
        this.observers = getNotificationCenter().createObserversGroup(this).addGlobal(NotificationCenter.emojiLoaded).addGlobal(NotificationCenter.didSetNewWallpapper).add(NotificationCenter.messagePlayingDidStart).add(NotificationCenter.messagePlayingPlayStateChanged).add(NotificationCenter.messagePlayingDidReset).add(NotificationCenter.messagePlayingProgressDidChanged);
        return super.onFragmentCreate();
    }

    public void onFragmentDestroy() {
        NotificationCenter.ObserversGroup observersGroup = this.observers;
        if (observersGroup != null) {
            observersGroup.removeAllObservers();
        }
        this.observers = null;
        AndroidUtilities.cancelRunOnUIThread(this.clearHighlight);
        AndroidUtilities.cancelRunOnUIThread(this.hideFloatingDate);
        super.onFragmentDestroy();
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [ni.shikatu.re_extera.ui.messages.MessageListFragment$createView$helper$1] */
    /* JADX WARN: Type inference failed for: r2v12, types: [ni.shikatu.re_extera.ui.messages.MessageListFragment$createView$4] */
    /* JADX WARN: Type inference failed for: r4v0, types: [ni.shikatu.re_extera.ui.messages.MessageListFragment$createView$content$1] */
    protected View createView(final Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.hasOwnBackground = true;
        Theme.createChatResources(context, false);
        this.selected.clear();
        setupActionBar(context);
        final ?? r2 = new TextSelectionHelper.ChatListTextSelectionHelper() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment$createView$helper$1
            {
                this.resourcesProvider = this.this$0.resourceProvider;
            }

            public void invalidate() {
                super.invalidate();
                RecyclerListView recyclerListView = this.this$0.chatListView;
                if (recyclerListView != null) {
                    recyclerListView.invalidate();
                }
            }
        };
        r2.setCallback(new TextSelectionHelper.Callback() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.createView.1
            public void onStateChanged(boolean isSelected) {
                RecyclerListView recyclerListView = MessageListFragment.this.chatListView;
                if (recyclerListView != null) {
                    recyclerListView.invalidate();
                }
            }

            public void onTextCopied() {
                BulletinFactory.of(MessageListFragment.this).createCopyBulletin(LocaleController.getString(R.string.TextCopied)).show();
            }
        });
        TextSelectionHelper.ChatListTextSelectionHelper chatListTextSelectionHelper = (TextSelectionHelper.ChatListTextSelectionHelper) r2;
        this.selectionHelper = chatListTextSelectionHelper;
        ?? r4 = new SizeNotifierFrameLayout(context, r2, this) { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment$createView$content$1
            final /* synthetic */ Context $context;
            final /* synthetic */ MessageListFragment$createView$helper$1 $helper;
            final /* synthetic */ MessageListFragment this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(context);
                this.$context = context;
                this.$helper = r2;
                this.this$0 = this;
            }

            public boolean dispatchTouchEvent(MotionEvent ev) {
                Intrinsics.checkNotNullParameter(ev, "ev");
                TextSelectionHelper.TextSelectionOverlay overlayView = getOverlayView(this.$context);
                if (overlayView != null) {
                    ev.offsetLocation(-overlayView.getX(), -overlayView.getY());
                    if (isInSelectionMode() && overlayView.onTouchEvent(ev)) {
                        ev.offsetLocation(overlayView.getX(), overlayView.getY());
                        return true;
                    }
                    ev.offsetLocation(overlayView.getX(), overlayView.getY());
                    if (overlayView.checkOnTap(ev)) {
                        return true;
                    }
                    RecyclerListView recyclerListView = this.this$0.chatListView;
                    if (ev.getAction() == 0 && isInSelectionMode() && recyclerListView != null && (ev.getY() < recyclerListView.getTop() || ev.getY() > recyclerListView.getBottom())) {
                        clear();
                        return true;
                    }
                }
                return super.dispatchTouchEvent(ev);
            }
        };
        r4.setOccupyStatusBar(true ^ AndroidUtilities.isTablet());
        r4.setBackgroundImage(Theme.getCachedWallpaper(), Theme.isWallpaperMotion());
        this.listTopInset = ActionBar.getCurrentActionBarHeight() + (this.actionBar.getOccupyStatusBar() ? AndroidUtilities.getStatusBarHeight(context) : 0);
        ViewGroup viewGroupCreateListView = createListView(context, chatListTextSelectionHelper);
        this.chatListView = viewGroupCreateListView;
        r4.addView(viewGroupCreateListView, LayoutHelper.createFrame(-1, -1.0f));
        View overlayView = r2.getOverlayView(context);
        if (overlayView != null) {
            ViewParent parent = overlayView.getParent();
            ViewGroup viewGroup = parent instanceof ViewGroup ? (ViewGroup) parent : null;
            if (viewGroup != null) {
                viewGroup.removeView(overlayView);
            }
            r4.addView(overlayView);
        }
        r2.setParentView(viewGroupCreateListView);
        TextView textView = new TextView(context);
        textView.setTextColor(getThemedColor(Theme.key_chat_serviceText));
        textView.setTextSize(14.0f);
        textView.setGravity(17);
        textView.setPadding(AndroidUtilities.dp(12.0f), AndroidUtilities.dp(6.0f), AndroidUtilities.dp(12.0f), AndroidUtilities.dp(7.0f));
        View view = (View) r4;
        textView.setBackground(Theme.createServiceDrawable(AndroidUtilities.dp(12.0f), textView, view));
        textView.setVisibility(8);
        this.emptyView = textView;
        r4.addView(textView, LayoutHelper.createFrame(-2, -2.0f, 17, 24.0f, 0.0f, 24.0f, 0.0f));
        final ?? r3 = new ChatActionCell(context, this.resourceProvider) { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.createView.4
            public boolean isFloating() {
                return true;
            }
        };
        r3.setAlpha(0.0f);
        r3.setVisibility(4);
        r3.setTranslationY(this.listTopInset);
        r3.setCustomDate((int) (System.currentTimeMillis() / 1000), false, false);
        r3.setOnClickListener(new View.OnClickListener() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.27
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                MessageListFragment.createView$lambda$6$lambda$5(r3, this, view2);
            }
        });
        View view2 = (ChatActionCell) r3;
        this.floatingDateView = view2;
        r4.addView(view2, LayoutHelper.createFrame(-2, -2.0f, 49, 0.0f, 4.0f, 0.0f, 0.0f));
        r4.addView(createSearchPanel(context), LayoutHelper.createFrame(-1, SEARCH_PANEL_HEIGHT, 80));
        r4.addView(createSideButtons(context), LayoutHelper.createFrame(57, 300.0f, 85, 0.0f, 0.0f, 2.0f, 0.0f));
        r4.addView(this.actionBar);
        this.fragmentView = view;
        updateHeader();
        reload();
        return view;
    }

    static final void createView$lambda$6$lambda$5(C00144 c00144, MessageListFragment messageListFragment, View view) {
        if (c00144.getAlpha() <= 0.0f || messageListFragment.getSelecting()) {
            return;
        }
        messageListFragment.showJumpToDate();
    }

    private final void setupActionBar(Context context) {
        this.actionBar.setAddToContainer(false);
        this.actionBar.setOccupyStatusBar(!AndroidUtilities.isTablet());
        this.actionBar.setBackButtonDrawable(new BackDrawable(false));
        this.actionBar.setAllowOverlayTitle(false);
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.setupActionBar.1
            public void onItemClick(int id) {
                MessageListFragment.this.onMenuItemClick(id);
            }
        });
        ChatAvatarContainer chatAvatarContainer = new ChatAvatarContainer(context, (BaseFragment) null, false, this.resourceProvider);
        chatAvatarContainer.setOccupyStatusBar(!AndroidUtilities.isTablet());
        chatAvatarContainer.setOnClickListener(new View.OnClickListener() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MessageListFragment.setupActionBar$lambda$8$lambda$7(MessageListFragment.this, view);
            }
        });
        this.avatarContainer = chatAvatarContainer;
        this.actionBar.addView(this.avatarContainer, 0, LayoutHelper.createFrame(-2, -1.0f, SEARCH_PANEL_HEIGHT, 56.0f, 0.0f, 88.0f, 0.0f));
        ActionBarMenu actionBarMenuCreateMenu = this.actionBar.createMenu();
        actionBarMenuCreateMenu.addItem(1, R.drawable.outline_header_search).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItem.ActionBarMenuItemSearchListener() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.setupActionBar.3
            public void onSearchExpand() {
                ChatAvatarContainer chatAvatarContainer2 = MessageListFragment.this.avatarContainer;
                if (chatAvatarContainer2 != null) {
                    chatAvatarContainer2.setVisibility(8);
                }
                MessageListFragment.this.showSearchPanel(true);
            }

            public void onSearchCollapse() {
                ChatAvatarContainer chatAvatarContainer2 = MessageListFragment.this.avatarContainer;
                if (chatAvatarContainer2 != null) {
                    chatAvatarContainer2.setVisibility(0);
                }
                MessageListFragment.this.setSearchQuery("");
                MessageListFragment.this.showSearchPanel(false);
            }

            public void onTextChanged(EditText editText) {
                Intrinsics.checkNotNullParameter(editText, "editText");
                MessageListFragment.this.setSearchQuery(editText.getText().toString());
            }

            public void onSearchPressed(EditText editText) {
                Intrinsics.checkNotNullParameter(editText, "editText");
                MessageListFragment.this.setSearchQuery(editText.getText().toString());
                AndroidUtilities.hideKeyboard(editText);
            }
        }).setSearchFieldHint(LocaleController.getString(R.string.Search));
        ActionBarMenuItem actionBarMenuItemAddItem = actionBarMenuCreateMenu.addItem(2, R.drawable.ic_ab_other);
        actionBarMenuItemAddItem.addSubItem(3, R.drawable.msg_calendar2, LocaleController.getString(R.string.JumpToDate));
        if (getCanDelete()) {
            int themedColor = getThemedColor(Theme.key_text_RedRegular);
            actionBarMenuItemAddItem.addSubItem(4, R.drawable.msg_delete, Strings.INSTANCE.getCLEAR_DELETED()).setColors(themedColor, themedColor);
        }
        ActionBarMenu actionBarMenuCreateActionMode = this.actionBar.createActionMode();
        View numberTextView = new NumberTextView(actionBarMenuCreateActionMode.getContext());
        numberTextView.setTextSize(18);
        numberTextView.setTypeface(AndroidUtilities.bold());
        numberTextView.setTextColor(getThemedColor(Theme.key_actionBarActionModeDefaultIcon));
        numberTextView.setOnTouchListener(new View.OnTouchListener() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.1
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                return MessageListFragment.setupActionBar$lambda$10$lambda$9(view, motionEvent);
            }
        });
        this.selectedCountView = numberTextView;
        actionBarMenuCreateActionMode.addView(numberTextView, LayoutHelper.createLinear(0, -1, 1.0f, 65, 0, 0, 0));
        this.copyItem = actionBarMenuCreateActionMode.addItemWithWidth(10, R.drawable.msg_copy, AndroidUtilities.dp(54.0f), LocaleController.getString(R.string.Copy));
        actionBarMenuCreateActionMode.addItemWithWidth(11, R.drawable.msg_saved, AndroidUtilities.dp(54.0f), Strings.INSTANCE.getSAVE_TO_SAVED());
        actionBarMenuCreateActionMode.addItemWithWidth(ID_FORWARD, R.drawable.msg_forward, AndroidUtilities.dp(54.0f), LocaleController.getString(R.string.Forward));
        if (getCanDelete()) {
            actionBarMenuCreateActionMode.addItemWithWidth(ID_DELETE, R.drawable.msg_delete, AndroidUtilities.dp(54.0f), Strings.INSTANCE.getDELETE_FOREVER());
        }
    }

    static final void setupActionBar$lambda$8$lambda$7(MessageListFragment messageListFragment, View view) {
        ChatUtils.INSTANCE.openProfile(messageListFragment, messageListFragment.getDialogId());
    }

    static final Unit onMenuItemClick$lambda$12$lambda$11(MessageListFragment messageListFragment) {
        messageListFragment.clearAll();
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void onMenuItemClick(int id) {
        if (id == -1) {
            if (getSelecting()) {
                clearSelection();
                return;
            } else {
                finishFragment();
                return;
            }
        }
        if (id == 3) {
            showJumpToDate();
            return;
        }
        if (id == 4) {
            Activity parentActivity = getParentActivity();
            if (parentActivity != null) {
                Confirm confirm = Confirm.INSTANCE;
                Activity activity = parentActivity;
                String clear_deleted = Strings.INSTANCE.getCLEAR_DELETED();
                String clear_deleted_confirm = Strings.INSTANCE.getCLEAR_DELETED_CONFIRM();
                String string = LocaleController.getString(R.string.Delete);
                Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
                Confirm.show$default(confirm, activity, clear_deleted, clear_deleted_confirm, string, false, new Function0() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.33
                    public final Object invoke() {
                        return MessageListFragment.onMenuItemClick$lambda$12$lambda$11(MessageListFragment.this);
                    }
                }, 16, null);
                return;
            }
            return;
        }
        switch (id) {
            case 10:
                copyMessages(selectedInOrder());
                clearSelection();
                break;
            case 11:
                CopyForwardMenu.INSTANCE.saveToSaved(this, selectedInOrder());
                clearSelection();
                break;
            case ID_FORWARD /* 12 */:
                CopyForwardMenu.INSTANCE.forward(this, selectedInOrder());
                clearSelection();
                break;
            case ID_DELETE /* 13 */:
                confirmDelete(selectedInOrder());
                break;
        }
    }

    private final RecyclerListView createListView(final Context context, final TextSelectionHelper.ChatListTextSelectionHelper helper) {
        AvatarRecyclerView avatarRecyclerView = new AvatarRecyclerView(this, context);
        avatarRecyclerView.setClipChildren(false);
        avatarRecyclerView.setClipToPadding(false);
        avatarRecyclerView.setPadding(0, AndroidUtilities.dp(4.0f) + this.listTopInset, 0, AndroidUtilities.dp(4.0f));
        avatarRecyclerView.setItemAnimator(null);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManagerFixed(context) { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment$createListView$manager$1
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }

            public boolean shouldLayoutChildFromOpositeSide(View child) {
                MessageObject messageObject;
                Intrinsics.checkNotNullParameter(child, "child");
                ChatMessageCell chatMessageCell = child instanceof ChatMessageCell ? (ChatMessageCell) child : null;
                return (chatMessageCell == null || (messageObject = chatMessageCell.getMessageObject()) == null || messageObject.isOutOwner()) ? false : true;
            }

            protected boolean hasSiblingChild(int position) {
                MessageObject.GroupedMessages groupedMessagesGroup;
                MessageObject.GroupedMessagePosition position2;
                MessageObject messageObjectMessageAt = this.data.messageAt(position);
                if (messageObjectMessageAt != null && (groupedMessagesGroup = this.data.group(messageObjectMessageAt)) != null && (position2 = groupedMessagesGroup.getPosition(messageObjectMessageAt)) != null && position2.minX != position2.maxX && position2.minY == position2.maxY && position2.minY != 0) {
                    ArrayList arrayList = groupedMessagesGroup.posArray;
                    Intrinsics.checkNotNullExpressionValue(arrayList, "posArray");
                    ArrayList<MessageObject.GroupedMessagePosition> arrayList2 = arrayList;
                    if ((arrayList2 instanceof Collection) && arrayList2.isEmpty()) {
                        return false;
                    }
                    for (MessageObject.GroupedMessagePosition groupedMessagePosition : arrayList2) {
                        if (groupedMessagePosition != position2 && groupedMessagePosition.minY <= position2.minY && groupedMessagePosition.maxY >= position2.minY) {
                            return true;
                        }
                    }
                }
                return false;
            }
        };
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.createListView.1
            public int getSpanSize(int position) {
                MessageObject.GroupedMessages groupedMessagesGroup;
                MessageObject.GroupedMessagePosition position2;
                MessageObject messageObjectMessageAt = MessageListFragment.this.data.messageAt(position);
                if (messageObjectMessageAt == null || (groupedMessagesGroup = MessageListFragment.this.data.group(messageObjectMessageAt)) == null || (position2 = groupedMessagesGroup.getPosition(messageObjectMessageAt)) == null) {
                    return 1000;
                }
                return position2.spanSize;
            }
        });
        this.layoutManager = (GridLayoutManagerFixed) layoutManager;
        avatarRecyclerView.setLayoutManager(layoutManager);
        avatarRecyclerView.addItemDecoration(new GroupOffsetsDecoration());
        avatarRecyclerView.setAdapter(this.adapter);
        avatarRecyclerView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.4
            public final void onItemClick(View view, int i) {
                MessageListFragment.createListView$lambda$13(MessageListFragment.this, helper, view, i);
            }
        });
        avatarRecyclerView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListenerExtended() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.5
            public final boolean onItemClick(View view, int i, float f, float f2) {
                return MessageListFragment.createListView$lambda$14(helper, this, view, i, f, f2);
            }
        });
        avatarRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.createListView.4
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                Intrinsics.checkNotNullParameter(recyclerView, "recyclerView");
                if (newState == 0) {
                    helper.stopScrolling();
                    AndroidUtilities.cancelRunOnUIThread(this.hideFloatingDate);
                    AndroidUtilities.runOnUIThread(this.hideFloatingDate, 1000L);
                } else {
                    if (newState != 1) {
                        return;
                    }
                    AndroidUtilities.cancelRunOnUIThread(this.hideFloatingDate);
                    this.showFloatingDate(true);
                }
            }

            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                Intrinsics.checkNotNullParameter(recyclerView, "recyclerView");
                helper.onParentScrolled();
                this.updateFloatingDate();
                this.updatePageDownButton();
            }
        });
        return avatarRecyclerView;
    }

    static final void createListView$lambda$13(MessageListFragment messageListFragment, TextSelectionHelper.ChatListTextSelectionHelper chatListTextSelectionHelper, View view, int i) {
        ChatMessageCell chatMessageCell = view instanceof ChatMessageCell ? (ChatMessageCell) view : null;
        if (chatMessageCell == null) {
            return;
        }
        if (messageListFragment.getSelecting()) {
            chatListTextSelectionHelper.cancelTextSelectionRunnable();
            MessageObject messageObject = chatMessageCell.getMessageObject();
            if (messageObject != null) {
                messageListFragment.toggleSelection(messageObject);
                return;
            }
            return;
        }
        messageListFragment.showMessageMenu(chatMessageCell);
    }

    static final boolean createListView$lambda$14(TextSelectionHelper.ChatListTextSelectionHelper chatListTextSelectionHelper, MessageListFragment messageListFragment, View view, int i, float f, float f2) {
        if (chatListTextSelectionHelper.isTryingSelect() || chatListTextSelectionHelper.isInSelectionMode()) {
            return false;
        }
        ChatMessageCell chatMessageCell = view instanceof ChatMessageCell ? (ChatMessageCell) view : null;
        if (chatMessageCell == null) {
            return false;
        }
        messageListFragment.onMessageLongPress(chatMessageCell);
        return true;
    }

    private final View createSearchPanel(Context context) {
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setBackgroundColor(getThemedColor(Theme.key_chat_messagePanelBackground));
        frameLayout.setVisibility(8);
        frameLayout.setClickable(true);
        TextView textView = new TextView(context);
        textView.setTextColor(getThemedColor(Theme.key_chat_searchPanelText));
        textView.setTextSize(15.0f);
        textView.setTypeface(AndroidUtilities.bold());
        textView.setGravity(19);
        textView.setSingleLine(true);
        this.searchCountText = textView;
        frameLayout.addView(textView, LayoutHelper.createFrame(-2, -1.0f, 19, 18.0f, 0.0f, 64.0f, 0.0f));
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(R.drawable.msg_calendar2);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_chat_searchPanelIcons), PorterDuff.Mode.MULTIPLY));
        imageView.setBackground(Theme.createSelectorDrawable(getThemedColor(Theme.key_actionBarActionModeDefaultSelector), 1));
        imageView.setContentDescription(LocaleController.getString(R.string.JumpToDate));
        imageView.setOnClickListener(new View.OnClickListener() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.34
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MessageListFragment.this.showJumpToDate();
            }
        });
        frameLayout.addView(imageView, LayoutHelper.createFrame(48, 48.0f, 21, 0.0f, 0.0f, 4.0f, 0.0f));
        this.searchPanel = frameLayout;
        return frameLayout;
    }

    private final View createSideButtons(Context context) {
        BlurredBackgroundSource blurredBackgroundSourceColor = new BlurredBackgroundSourceColor();
        blurredBackgroundSourceColor.setColor(getThemedColor(Theme.key_chat_messagePanelBackground));
        View chatActivitySideControlsButtonsLayout = new ChatActivitySideControlsButtonsLayout(context, this.resourceProvider, new BlurredBackgroundColorProviderThemed(this.resourceProvider, Theme.key_chat_messagePanelBackground), new BlurredBackgroundDrawableViewFactory(blurredBackgroundSourceColor));
        chatActivitySideControlsButtonsLayout.setOnClickListener(new ButtonOnClickListener() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.30
            public final void onClick(int i, View view) {
                MessageListFragment.createSideButtons$lambda$20(MessageListFragment.this, i, view);
            }
        });
        this.sideButtons = chatActivitySideControlsButtonsLayout;
        return chatActivitySideControlsButtonsLayout;
    }

    static final void createSideButtons$lambda$20(MessageListFragment messageListFragment, int i, View view) {
        if (i == 1) {
            messageListFragment.scrollToBottom();
        } else if (i == 5) {
            messageListFragment.moveSearch(-1);
        } else {
            if (i != 6) {
                return;
            }
            messageListFragment.moveSearch(1);
        }
    }

    public boolean onBackPressed(boolean invoked) {
        if (getSelecting()) {
            if (invoked) {
                clearSelection();
            }
            return false;
        }
        ActionBar actionBar = this.actionBar;
        if (actionBar != null && actionBar.isSearchFieldVisible()) {
            if (invoked) {
                this.actionBar.closeSearchField();
            }
            return false;
        }
        TextSelectionHelper.ChatListTextSelectionHelper chatListTextSelectionHelper = this.selectionHelper;
        if (chatListTextSelectionHelper == null || !chatListTextSelectionHelper.isInSelectionMode()) {
            return super.onBackPressed(invoked);
        }
        if (invoked) {
            chatListTextSelectionHelper.clear();
        }
        return false;
    }

    public boolean isSwipeBackEnabled(MotionEvent event) {
        if (getSelecting()) {
            return false;
        }
        TextSelectionHelper.ChatListTextSelectionHelper chatListTextSelectionHelper = this.selectionHelper;
        if (chatListTextSelectionHelper == null || !(chatListTextSelectionHelper.isTryingSelect() || chatListTextSelectionHelper.isInSelectionMode() || chatListTextSelectionHelper.isTouched())) {
            return super.isSwipeBackEnabled(event);
        }
        return false;
    }

    protected final void reload() {
        final int i = this.generation + 1;
        this.generation = i;
        this.loading = true;
        updateHeader();
        loadMessages(new Function1() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.32
            public final Object invoke(Object obj) {
                return MessageListFragment.reload$lambda$27(MessageListFragment.this, i, (List) obj);
            }
        });
    }

    static final Unit reload$lambda$27(final MessageListFragment messageListFragment, final int i, final List list) {
        Intrinsics.checkNotNullParameter(list, "loaded");
        final MessagesStorage messagesStorage = messageListFragment.getMessagesStorage();
        messagesStorage.getStorageQueue().postRunnable(new Runnable() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.28
            @Override // java.lang.Runnable
            public final void run() {
                MessageListFragment.reload$lambda$27$lambda$26(messagesStorage, messageListFragment, list, i);
            }
        });
        return Unit.INSTANCE;
    }

    static final void reload$lambda$27$lambda$26(MessagesStorage messagesStorage, final MessageListFragment messageListFragment, final List list, final int i) {
        TelegramStorage telegramStorage = TelegramStorage.INSTANCE;
        Intrinsics.checkNotNull(messagesStorage);
        telegramStorage.attachReplies(messagesStorage, messageListFragment.getDialogId(), list);
        final TelegramStorage.Peers peersLoadPeers = TelegramStorage.INSTANCE.loadPeers(messagesStorage, list);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.7
            @Override // java.lang.Runnable
            public final void run() {
                MessageListFragment.reload$lambda$27$lambda$26$lambda$25(i, messageListFragment, peersLoadPeers, list);
            }
        });
    }

    static final void reload$lambda$27$lambda$26$lambda$25(final int i, final MessageListFragment messageListFragment, TelegramStorage.Peers peers, final List list) {
        if (i != messageListFragment.generation) {
            return;
        }
        messageListFragment.getMessagesController().putUsers(peers.getUsers(), true);
        messageListFragment.getMessagesController().putChats(peers.getChats(), true);
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.3
            @Override // java.lang.Runnable
            public final void run() {
                MessageListFragment.reload$lambda$27$lambda$26$lambda$25$lambda$24(list, messageListFragment, i);
            }
        });
    }

    static final void reload$lambda$27$lambda$26$lambda$25$lambda$24(List list, final MessageListFragment messageListFragment, final int i) {
        List list2 = list;
        Iterator it = list2.iterator();
        while (it.hasNext()) {
            messageListFragment.prepare((TLRPC.Message) it.next());
        }
        ArrayList arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list2, 10));
        Iterator it2 = list2.iterator();
        while (it2.hasNext()) {
            arrayList.add(new MessageObject(messageListFragment.currentAccount, (TLRPC.Message) it2.next(), true, true));
        }
        final MessageRows messageRowsBuild = MessageRows.INSTANCE.build(messageListFragment.currentAccount, arrayList, messageListFragment.getGroupAlbums());
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.6
            @Override // java.lang.Runnable
            public final void run() {
                MessageListFragment.reload$lambda$27$lambda$26$lambda$25$lambda$24$lambda$23(i, messageListFragment, messageRowsBuild);
            }
        });
    }

    static final void reload$lambda$27$lambda$26$lambda$25$lambda$24$lambda$23(int i, MessageListFragment messageListFragment, MessageRows messageRows) {
        if (i != messageListFragment.generation) {
            return;
        }
        messageListFragment.loading = false;
        messageListFragment.applyRows(messageRows);
    }

    protected final void removeMessages(Collection<? extends MessageObject> removed) {
        Intrinsics.checkNotNullParameter(removed, "removed");
        if (removed.isEmpty()) {
            return;
        }
        HashSet hashSet = CollectionsKt.toHashSet(removed);
        MessageRows.Companion companion = MessageRows.INSTANCE;
        int i = this.currentAccount;
        List<MessageObject> messages = this.data.getMessages();
        ArrayList arrayList = new ArrayList();
        for (Object obj : messages) {
            if (!hashSet.contains((MessageObject) obj)) {
                arrayList.add(obj);
            }
        }
        applyRows(companion.build(i, arrayList, getGroupAlbums()));
    }

    private final void applyRows(MessageRows rows) {
        ArrayList arrayListEmptyList;
        this.data = rows;
        if (this.selected.retainAll(CollectionsKt.toHashSet(rows.getMessages())) && this.selected.isEmpty()) {
            clearSelection();
        } else {
            updateSelectionCount(true);
        }
        if (this.searchQuery.length() == 0) {
            arrayListEmptyList = CollectionsKt.emptyList();
        } else {
            List<MessageObject> messages = rows.getMessages();
            ArrayList arrayList = new ArrayList();
            for (Object obj : messages) {
                if (matches((MessageObject) obj, this.searchQuery)) {
                    arrayList.add(obj);
                }
            }
            arrayListEmptyList = arrayList;
        }
        this.searchResults = arrayListEmptyList;
        this.searchIndex = arrayListEmptyList.isEmpty() ? -1 : RangesKt.coerceIn(this.searchIndex, 0, this.searchResults.size() - 1);
        this.adapter.notifyDataSetChanged();
        updateHeader();
        updateSearchPanel();
        RecyclerListView recyclerListView = this.chatListView;
        if (recyclerListView != null) {
            recyclerListView.post(new Runnable() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.24
                @Override // java.lang.Runnable
                public final void run() {
                    MessageListFragment.applyRows$lambda$30(MessageListFragment.this);
                }
            });
        }
    }

    static final void applyRows$lambda$30(MessageListFragment messageListFragment) {
        messageListFragment.updateFloatingDate();
        messageListFragment.updatePageDownButton();
    }

    private final void updateHeader() {
        String fallbackTitle;
        String fallbackTitle2;
        ChatAvatarContainer chatAvatarContainer = this.avatarContainer;
        if (chatAvatarContainer == null) {
            return;
        }
        MessagesController messagesController = getMessagesController();
        if (getDialogId() > 0) {
            TLRPC.User user = messagesController.getUser(Long.valueOf(getDialogId()));
            if (user == null || (fallbackTitle2 = ContactsController.formatName(user.first_name, user.last_name)) == null) {
                fallbackTitle2 = getFallbackTitle();
            }
            chatAvatarContainer.setTitle(fallbackTitle2);
            if (user != null) {
                chatAvatarContainer.setUserAvatar(user);
            }
        } else if (getDialogId() < 0) {
            TLRPC.Chat chat = messagesController.getChat(Long.valueOf(-getDialogId()));
            if (chat == null || (fallbackTitle = chat.title) == null) {
                fallbackTitle = getFallbackTitle();
            }
            chatAvatarContainer.setTitle(fallbackTitle);
            if (chat != null) {
                chatAvatarContainer.setChatAvatar(chat);
            }
        } else {
            chatAvatarContainer.setTitle(getFallbackTitle());
        }
        chatAvatarContainer.setSubtitle(this.loading ? LocaleController.getString(R.string.Loading) : subtitle(this.data.getMessages().size()));
        TextView textView = this.emptyView;
        if (textView != null) {
            textView.setText(getEmptyText());
            textView.setVisibility((this.loading || !this.data.getRows().isEmpty()) ? 8 : 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void updatePageDownButton() {
        ChatActivitySideControlsButtonsLayout chatActivitySideControlsButtonsLayout = this.sideButtons;
        if (chatActivitySideControlsButtonsLayout == null) {
            return;
        }
        GridLayoutManagerFixed gridLayoutManagerFixed = this.layoutManager;
        chatActivitySideControlsButtonsLayout.showButton(1, (gridLayoutManagerFixed != null ? gridLayoutManagerFixed.findFirstVisibleItemPosition() : -1) > 1, true);
    }

    private final void scrollToBottom() {
        GridLayoutManagerFixed gridLayoutManagerFixed;
        RecyclerListView recyclerListView = this.chatListView;
        if (recyclerListView == null || (gridLayoutManagerFixed = this.layoutManager) == null) {
            return;
        }
        if (gridLayoutManagerFixed.findFirstVisibleItemPosition() > 30) {
            gridLayoutManagerFixed.scrollToPositionWithOffset(0, 0);
        } else {
            recyclerListView.smoothScrollToPosition(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void showFloatingDate(boolean show) {
        final ChatActionCell chatActionCell = this.floatingDateView;
        if (chatActionCell == null || this.floatingDateVisible == show) {
            return;
        }
        this.floatingDateVisible = show;
        chatActionCell.animate().cancel();
        if (show) {
            chatActionCell.setVisibility(0);
        }
        chatActionCell.animate().alpha(show ? 1.0f : 0.0f).setDuration(150L).withEndAction(new Runnable() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.29
            @Override // java.lang.Runnable
            public final void run() {
                MessageListFragment.showFloatingDate$lambda$35(MessageListFragment.this, chatActionCell);
            }
        }).start();
    }

    static final void showFloatingDate$lambda$35(MessageListFragment messageListFragment, ChatActionCell chatActionCell) {
        if (messageListFragment.floatingDateVisible) {
            return;
        }
        chatActionCell.setVisibility(4);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void updateFloatingDate() {
        ChatActionCell chatActionCell;
        MessageObject messageObject;
        MessageObject messageObject2;
        RecyclerListView recyclerListView = this.chatListView;
        if (recyclerListView == null || (chatActionCell = this.floatingDateView) == null) {
            return;
        }
        int paddingTop = recyclerListView.getPaddingTop() - AndroidUtilities.dp(4.0f);
        int childCount = recyclerListView.getChildCount();
        View view = null;
        for (int i = 0; i < childCount; i++) {
            View childAt = recyclerListView.getChildAt(i);
            if (childAt.getBottom() > paddingTop && (view == null || childAt.getTop() < view.getTop())) {
                view = childAt;
            }
        }
        if (view instanceof ChatMessageCell) {
            messageObject = ((ChatMessageCell) view).getMessageObject();
        } else {
            messageObject = view instanceof ChatActionCell ? ((ChatActionCell) view).getMessageObject() : null;
        }
        if (messageObject != null) {
            chatActionCell.setCustomDate(messageObject.messageOwner.date, false, true);
        }
        float f = this.listTopInset;
        int measuredHeight = chatActionCell.getMeasuredHeight();
        int childCount2 = recyclerListView.getChildCount();
        float fMin = 0.0f;
        for (int i2 = 0; i2 < childCount2; i2++) {
            ChatActionCell childAt2 = recyclerListView.getChildAt(i2);
            ChatActionCell chatActionCell2 = childAt2 instanceof ChatActionCell ? childAt2 : null;
            if (chatActionCell2 != null && (messageObject2 = chatActionCell2.getMessageObject()) != null && messageObject2.isDateObject) {
                float top = chatActionCell2.getTop() - (AndroidUtilities.dp(4.0f) + f);
                if (top > 0.0f) {
                    float f2 = measuredHeight;
                    if (top < f2) {
                        fMin = Math.min(fMin, top - f2);
                    }
                }
            }
        }
        chatActionCell.setTranslationY(f + fMin);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void showJumpToDate() {
        MessageObject messageObject;
        TLRPC.Message message;
        Activity parentActivity = getParentActivity();
        if (parentActivity == null || (messageObject = (MessageObject) CollectionsKt.lastOrNull(this.data.getMessages())) == null || (message = messageObject.messageOwner) == null) {
            return;
        }
        showDialog((Dialog) AlertsCreator.createCalendarPickerDialog(parentActivity, ((long) message.date) * 1000, new MessagesStorage.IntCallback() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.2
            public final void run(int i) {
                MessageListFragment.showJumpToDate$lambda$37(MessageListFragment.this, i);
            }
        }, this.resourceProvider).create());
    }

    static final void showJumpToDate$lambda$37(MessageListFragment messageListFragment, int i) {
        MessageObject messageObjectPrevious;
        List<MessageObject> messages = messageListFragment.data.getMessages();
        ListIterator<MessageObject> listIterator = messages.listIterator(messages.size());
        do {
            if (!listIterator.hasPrevious()) {
                messageObjectPrevious = null;
                break;
            }
            messageObjectPrevious = listIterator.previous();
        } while (messageObjectPrevious.messageOwner.date < i);
        MessageObject messageObject = messageObjectPrevious;
        if (messageObject == null && (messageObject = (MessageObject) CollectionsKt.firstOrNull(messageListFragment.data.getMessages())) == null) {
            return;
        }
        messageListFragment.scrollToMessage(messageObject, false);
    }

    protected final void scrollToMessage(int id) {
        int iPositionOfId = this.data.positionOfId(id);
        if (iPositionOfId >= 0) {
            MessageObject messageObjectMessageAt = this.data.messageAt(iPositionOfId);
            if (messageObjectMessageAt != null) {
                scrollToMessage(messageObjectMessageAt, true);
                return;
            }
            return;
        }
        showInChat(id);
    }

    private final void scrollToMessage(MessageObject message, boolean highlight) {
        GridLayoutManagerFixed gridLayoutManagerFixed;
        int iPositionOf = this.data.positionOf(message);
        RecyclerListView recyclerListView = this.chatListView;
        if (recyclerListView == null || (gridLayoutManagerFixed = this.layoutManager) == null || iPositionOf < 0) {
            return;
        }
        gridLayoutManagerFixed.scrollToPositionWithOffset(iPositionOf, Math.max(0, (((recyclerListView.getMeasuredHeight() - recyclerListView.getPaddingTop()) - recyclerListView.getPaddingBottom()) / 2) - AndroidUtilities.dp(64.0f)));
        if (highlight) {
            AndroidUtilities.cancelRunOnUIThread(this.clearHighlight);
            this.highlighted = message;
            for (ChatMessageCell chatMessageCell : cells((RecyclerView) recyclerListView)) {
                chatMessageCell.setHighlighted(chatMessageCell.getMessageObject() == message);
            }
            AndroidUtilities.runOnUIThread(this.clearHighlight, 1500L);
        }
    }

    private final void showInChat(int messageId) {
        INavigationLayout iNavigationLayout = this.parentLayout;
        List fragmentStack = iNavigationLayout != null ? iNavigationLayout.getFragmentStack() : null;
        BaseFragment baseFragment = fragmentStack != null ? (BaseFragment) CollectionsKt.getOrNull(fragmentStack, fragmentStack.indexOf(this) - 1) : null;
        ChatActivity chatActivity = baseFragment instanceof ChatActivity ? (ChatActivity) baseFragment : null;
        if (chatActivity != null && chatActivity.getDialogId() == getDialogId() && chatActivity.getChatMode() == 0) {
            finishFragment();
            chatActivity.scrollToMessageId(messageId, 0, true, 0, true, 0);
        } else {
            Bundle bundlePeerArgs = ChatUtils.INSTANCE.peerArgs(getDialogId());
            bundlePeerArgs.putInt("message_id", messageId);
            presentFragment((BaseFragment) new ChatActivity(bundlePeerArgs));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void showSearchPanel(boolean show) {
        FrameLayout frameLayout = this.searchPanel;
        if (frameLayout != null) {
            frameLayout.setVisibility(show ? 0 : 8);
        }
        int iDp = AndroidUtilities.dp(4.0f) + (show ? AndroidUtilities.dp(51.0f) : 0);
        RecyclerListView recyclerListView = this.chatListView;
        if (recyclerListView != null) {
            recyclerListView.setPadding(recyclerListView.getPaddingLeft(), recyclerListView.getPaddingTop(), recyclerListView.getPaddingRight(), iDp);
        }
        ChatActivitySideControlsButtonsLayout chatActivitySideControlsButtonsLayout = this.sideButtons;
        ViewGroup.LayoutParams layoutParams = chatActivitySideControlsButtonsLayout != null ? chatActivitySideControlsButtonsLayout.getLayoutParams() : null;
        FrameLayout.LayoutParams layoutParams2 = layoutParams instanceof FrameLayout.LayoutParams ? (FrameLayout.LayoutParams) layoutParams : null;
        if (layoutParams2 != null) {
            layoutParams2.bottomMargin = show ? AndroidUtilities.dp(51.0f) : 0;
            ChatActivitySideControlsButtonsLayout chatActivitySideControlsButtonsLayout2 = this.sideButtons;
            if (chatActivitySideControlsButtonsLayout2 != null) {
                chatActivitySideControlsButtonsLayout2.requestLayout();
            }
        }
        updateSearchPanel();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void setSearchQuery(String query) {
        ArrayList arrayListEmptyList;
        Sequence<ChatMessageCell> sequenceCells;
        String string = StringsKt.trim(query).toString();
        if (Intrinsics.areEqual(string, this.searchQuery)) {
            return;
        }
        this.searchQuery = string;
        String str = string;
        if (str.length() == 0) {
            arrayListEmptyList = CollectionsKt.emptyList();
        } else {
            List<MessageObject> messages = this.data.getMessages();
            ArrayList arrayList = new ArrayList();
            for (Object obj : messages) {
                if (matches((MessageObject) obj, string)) {
                    arrayList.add(obj);
                }
            }
            arrayListEmptyList = arrayList;
        }
        this.searchResults = arrayListEmptyList;
        this.searchIndex = arrayListEmptyList.isEmpty() ? -1 : 0;
        if (str.length() <= 0) {
            string = null;
        }
        RecyclerListView recyclerListView = this.chatListView;
        if (recyclerListView != null && (sequenceCells = cells((RecyclerView) recyclerListView)) != null) {
            for (ChatMessageCell chatMessageCell : sequenceCells) {
                chatMessageCell.setHighlightedText(string);
                chatMessageCell.invalidate();
            }
        }
        updateSearchPanel();
        MessageObject messageObject = (MessageObject) CollectionsKt.getOrNull(this.searchResults, this.searchIndex);
        if (messageObject != null) {
            scrollToMessage(messageObject, true);
        }
    }

    private final void moveSearch(int delta) {
        int i = this.searchIndex + delta;
        if (i < 0 || i >= this.searchResults.size()) {
            return;
        }
        this.searchIndex = i;
        updateSearchPanel();
        scrollToMessage(this.searchResults.get(i), true);
    }

    private final void updateSearchPanel() {
        String string;
        ActionBar actionBar = this.actionBar;
        boolean z = actionBar != null && actionBar.isSearchFieldVisible();
        TextView textView = this.searchCountText;
        if (textView != null) {
            if (this.searchQuery.length() != 0) {
                string = this.searchResults.isEmpty() ? LocaleController.getString(R.string.NoResult) : LocaleController.formatString(R.string.Of, new Object[]{Integer.valueOf(this.searchIndex + 1), Integer.valueOf(this.searchResults.size())});
            }
            textView.setText(string);
        }
        ChatActivitySideControlsButtonsLayout chatActivitySideControlsButtonsLayout = this.sideButtons;
        if (chatActivitySideControlsButtonsLayout == null) {
            return;
        }
        boolean z2 = z && !this.searchResults.isEmpty();
        chatActivitySideControlsButtonsLayout.showButton(6, z2, true);
        chatActivitySideControlsButtonsLayout.showButton(5, z2, true);
        if (z2) {
            chatActivitySideControlsButtonsLayout.setButtonEnabled(6, this.searchIndex < this.searchResults.size() - 1, true);
            chatActivitySideControlsButtonsLayout.setButtonEnabled(5, this.searchIndex > 0, true);
        }
    }

    private final boolean matches(MessageObject message, String query) {
        TLRPC.Poll poll;
        String str;
        String str2;
        String documentName;
        TLRPC.Message message2 = message.messageOwner;
        if (message2 == null) {
            return false;
        }
        String str3 = message2.message;
        if (str3 != null && StringsKt.contains(str3, query, true)) {
            return true;
        }
        if (message.getDocument() != null && (documentName = message.getDocumentName()) != null && StringsKt.contains(documentName, query, true)) {
            return true;
        }
        TLRPC.TL_messageMediaPoll tL_messageMediaPoll = message2.media;
        TLRPC.TL_messageMediaPoll tL_messageMediaPoll2 = tL_messageMediaPoll instanceof TLRPC.TL_messageMediaPoll ? tL_messageMediaPoll : null;
        if (tL_messageMediaPoll2 != null && (poll = tL_messageMediaPoll2.poll) != null) {
            TLRPC.TL_textWithEntities tL_textWithEntities = poll.question;
            if (tL_textWithEntities == null || (str2 = tL_textWithEntities.text) == null || !StringsKt.contains(str2, query, true)) {
                ArrayList arrayList = poll.answers;
                Intrinsics.checkNotNullExpressionValue(arrayList, "answers");
                ArrayList arrayList2 = arrayList;
                if (!(arrayList2 instanceof Collection) || !arrayList2.isEmpty()) {
                    Iterator it = arrayList2.iterator();
                    while (it.hasNext()) {
                        TLRPC.TL_textWithEntities tL_textWithEntities2 = ((TLRPC.PollAnswer) it.next()).text;
                        if (tL_textWithEntities2 == null || (str = tL_textWithEntities2.text) == null || !StringsKt.contains(str, query, true)) {
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    private final void openSearch(String text) {
        if (getSelecting()) {
            clearSelection();
        }
        this.actionBar.openSearchField(text, false);
        setSearchQuery(text);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void toggleSelection(MessageObject message) {
        ArrayList arrayList;
        MessageObject.GroupedMessages groupedMessagesGroup = this.data.group(message);
        ArrayList arrayListListOf = (groupedMessagesGroup == null || (arrayList = groupedMessagesGroup.messages) == null) ? CollectionsKt.listOf(message) : arrayList;
        List list = arrayListListOf;
        if (!(list instanceof Collection) || !list.isEmpty()) {
            Iterator it = list.iterator();
            while (true) {
                if (!it.hasNext()) {
                    this.selected.removeAll(CollectionsKt.toSet(list));
                    break;
                } else if (!this.selected.contains((MessageObject) it.next())) {
                    this.selected.addAll(arrayListListOf);
                    break;
                }
            }
        } else {
            this.selected.removeAll(CollectionsKt.toSet(list));
            break;
        }
        if (this.selected.isEmpty()) {
            clearSelection();
            return;
        }
        boolean zIsActionModeShowed = this.actionBar.isActionModeShowed();
        if (!zIsActionModeShowed) {
            TextSelectionHelper.ChatListTextSelectionHelper chatListTextSelectionHelper = this.selectionHelper;
            if (chatListTextSelectionHelper != null) {
                chatListTextSelectionHelper.cancelTextSelectionRunnable();
            }
            TextSelectionHelper.ChatListTextSelectionHelper chatListTextSelectionHelper2 = this.selectionHelper;
            if (chatListTextSelectionHelper2 != null) {
                chatListTextSelectionHelper2.clear();
            }
            AndroidUtilities.hideKeyboard(this.fragmentView);
            this.actionBar.showActionMode();
        }
        updateSelectionCount(zIsActionModeShowed);
        updateSelectionCells(true);
    }

    private final void clearSelection() {
        this.selected.clear();
        ActionBar actionBar = this.actionBar;
        if (actionBar != null && actionBar.isActionModeShowed()) {
            this.actionBar.hideActionMode();
        }
        updateSelectionCells(true);
    }

    private final void updateSelectionCount(boolean animated) {
        int i;
        if (getSelecting()) {
            NumberTextView numberTextView = this.selectedCountView;
            if (numberTextView != null) {
                numberTextView.setNumber(this.selected.size(), animated);
            }
            ActionBarMenuItem actionBarMenuItem = this.copyItem;
            if (actionBarMenuItem != null) {
                LinkedHashSet<MessageObject> linkedHashSet = this.selected;
                if ((linkedHashSet instanceof Collection) && linkedHashSet.isEmpty()) {
                    i = 8;
                } else {
                    Iterator<T> it = linkedHashSet.iterator();
                    while (it.hasNext()) {
                        if (copyText((MessageObject) it.next(), null) != null) {
                            i = 0;
                        }
                    }
                    i = 8;
                }
                actionBarMenuItem.setVisibility(i);
            }
        }
    }

    private final void updateSelectionCells(boolean animated) {
        RecyclerListView recyclerListView = this.chatListView;
        if (recyclerListView == null) {
            return;
        }
        if (animated) {
            this.selectionAnimationUntil = SystemClock.uptimeMillis() + SELECTION_ANIMATION_MS;
        }
        Iterator it = cells((RecyclerView) recyclerListView).iterator();
        while (it.hasNext()) {
            bindSelection((ChatMessageCell) it.next(), animated);
        }
        recyclerListView.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void bindSelection(ChatMessageCell cell, boolean animated) {
        boolean z;
        ArrayList arrayList;
        MessageObject messageObject = cell.getMessageObject();
        if (messageObject == null) {
            return;
        }
        cell.setCheckBoxVisible(getSelecting(), animated);
        boolean zContains = this.selected.contains(messageObject);
        MessageObject.GroupedMessages currentMessagesGroup = cell.getCurrentMessagesGroup();
        boolean z2 = false;
        if (currentMessagesGroup != null && (arrayList = currentMessagesGroup.messages) != null) {
            ArrayList arrayList2 = arrayList;
            if (!(arrayList2 instanceof Collection) || !arrayList2.isEmpty()) {
                Iterator it = arrayList2.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        z = true;
                        break;
                    } else if (!this.selected.contains((MessageObject) it.next())) {
                        z = false;
                        break;
                    }
                }
            } else {
                z = true;
                break;
            }
        } else {
            z = zContains;
        }
        if (zContains && currentMessagesGroup == null) {
            z2 = true;
        }
        cell.setDrawSelectionBackground(z2);
        cell.setChecked(zContains, z, animated);
    }

    private final List<MessageObject> selectedInOrder() {
        List<MessageObject> messages = this.data.getMessages();
        ArrayList arrayList = new ArrayList();
        for (Object obj : messages) {
            if (this.selected.contains((MessageObject) obj)) {
                arrayList.add(obj);
            }
        }
        return CollectionsKt.asReversed(arrayList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void confirmDelete(final List<? extends MessageObject> targets) {
        Activity parentActivity = getParentActivity();
        if (parentActivity == null || targets.isEmpty()) {
            return;
        }
        String pluralString = LocaleController.formatPluralString("messages", targets.size(), new Object[0]);
        Confirm confirm = Confirm.INSTANCE;
        Activity activity = parentActivity;
        String delete_forever = Strings.INSTANCE.getDELETE_FOREVER();
        StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
        String str = String.format(Strings.INSTANCE.getDELETE_FOREVER_CONFIRM(), Arrays.copyOf(new Object[]{pluralString}, 1));
        Intrinsics.checkNotNullExpressionValue(str, "format(...)");
        String str2 = str;
        String string = LocaleController.getString(R.string.Delete);
        Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
        Confirm.show$default(confirm, activity, delete_forever, str2, string, false, new Function0() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.31
            public final Object invoke() {
                return MessageListFragment.confirmDelete$lambda$53(MessageListFragment.this, targets);
            }
        }, 16, null);
    }

    static final Unit confirmDelete$lambda$53(MessageListFragment messageListFragment, List list) {
        if (messageListFragment.getSelecting()) {
            messageListFragment.clearSelection();
        }
        messageListFragment.deleteMessages(list);
        return Unit.INSTANCE;
    }

    private final CharSequence copyText(MessageObject message, MessageObject.GroupedMessages group) {
        ArrayList arrayList;
        CharSequence charSequence;
        if (message.isDice()) {
            return message.getDiceEmoji();
        }
        if (group != null && (arrayList = group.messages) != null) {
            Iterator it = arrayList.iterator();
            do {
                if (!it.hasNext()) {
                    charSequence = null;
                    break;
                }
                charSequence = ((MessageObject) it.next()).caption;
                if (charSequence == null || charSequence.length() <= 0) {
                    charSequence = null;
                }
            } while (charSequence == null);
            if (charSequence != null) {
                return charSequence;
            }
        }
        TLRPC.Message message2 = message.messageOwner;
        String str = message2 != null ? message2.message : null;
        if (str == null || str.length() == 0) {
            return null;
        }
        CharSequence charSequence2 = message.caption;
        return charSequence2 == null ? message.messageText : charSequence2;
    }

    private final void copyMessages(List<? extends MessageObject> messages) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        long fromChatId = 0;
        for (MessageObject messageObject : messages) {
            CharSequence charSequenceCopyText = copyText(messageObject, null);
            if (charSequenceCopyText != null || (charSequenceCopyText = messageObject.messageText) != null) {
                if (spannableStringBuilder.length() > 0) {
                    spannableStringBuilder.append((CharSequence) "\n\n");
                }
                if (messages.size() > 1 && messageObject.getFromChatId() != fromChatId) {
                    fromChatId = messageObject.getFromChatId();
                    spannableStringBuilder.append((CharSequence) senderName(messageObject)).append((CharSequence) ":\n");
                }
                spannableStringBuilder.append(charSequenceCopyText);
            }
        }
        if (spannableStringBuilder.length() > 0) {
            copyToClipboard(spannableStringBuilder, R.string.MessageCopied);
        }
    }

    private final void copyToClipboard(CharSequence text, int toast) {
        if (AndroidUtilities.addToClipboard(text) && AndroidUtilities.shouldShowClipboardToast()) {
            BulletinFactory.of(this).createCopyBulletin(LocaleController.getString(toast)).show();
        }
    }

    private final String senderName(MessageObject message) {
        long fromChatId = message.getFromChatId();
        MessagesController messagesController = getMessagesController();
        if (fromChatId > 0) {
            String userName = UserObject.getUserName(messagesController.getUser(Long.valueOf(fromChatId)));
            Intrinsics.checkNotNullExpressionValue(userName, "getUserName(...)");
            return userName;
        }
        TLRPC.Chat chat = messagesController.getChat(Long.valueOf(-fromChatId));
        String str = chat != null ? chat.title : null;
        return str == null ? "" : str;
    }

    private final void onMessageLongPress(ChatMessageCell cell) {
        int childAdapterPosition;
        MessageObject messageObject = cell.getMessageObject();
        if (messageObject == null) {
            return;
        }
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        if (jElapsedRealtime - this.lastLongPressTime < 500) {
            return;
        }
        this.lastLongPressTime = jElapsedRealtime;
        TextSelectionHelper.ChatListTextSelectionHelper chatListTextSelectionHelper = this.selectionHelper;
        if (chatListTextSelectionHelper != null) {
            chatListTextSelectionHelper.cancelTextSelectionRunnable();
            if (chatListTextSelectionHelper.isInSelectionMode()) {
                chatListTextSelectionHelper.clear();
            }
        }
        try {
            cell.performHapticFeedback(0, 2);
        } catch (Exception unused) {
        }
        toggleSelection(messageObject);
        RecyclerListView recyclerListView = this.chatListView;
        if (recyclerListView == null || (childAdapterPosition = recyclerListView.getChildAdapterPosition((View) cell)) < 0) {
            return;
        }
        startMultiselect(childAdapterPosition);
    }

    private final void startMultiselect(int position) {
        MessageObject messageObjectMessageAt;
        final RecyclerListView recyclerListView = this.chatListView;
        if (recyclerListView == null || (messageObjectMessageAt = this.data.messageAt(position)) == null) {
            return;
        }
        final boolean zContains = this.selected.contains(messageObjectMessageAt);
        final HashSet hashSet = new HashSet(this.selected);
        recyclerListView.startMultiselect(position, false, new RecyclerListView.onMultiSelectionChanged() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.startMultiselect.1
            public boolean limitReached() {
                return false;
            }

            public void onSelectionChanged(int position2, boolean inRange, float x, float y) {
                MessageObject messageObjectMessageAt2 = MessageListFragment.this.data.messageAt(position2);
                if (messageObjectMessageAt2 == null) {
                    return;
                }
                MessageListFragment messageListFragment = MessageListFragment.this;
                if (!zContains) {
                    inRange = !inRange;
                }
                messageListFragment.setSelected(messageObjectMessageAt2, inRange);
            }

            public boolean canSelect(int position2) {
                MessageObject messageObjectMessageAt2 = MessageListFragment.this.data.messageAt(position2);
                if (messageObjectMessageAt2 == null) {
                    return false;
                }
                boolean z = zContains;
                boolean zContains2 = hashSet.contains(messageObjectMessageAt2);
                if (z) {
                    return !zContains2;
                }
                return zContains2;
            }

            public int checkPosition(int position2, boolean selectionFromTop) {
                MessageObject.GroupedMessages groupedMessagesGroup;
                Object objLast;
                MessageObject messageObjectMessageAt2 = MessageListFragment.this.data.messageAt(position2);
                if (messageObjectMessageAt2 != null && (groupedMessagesGroup = MessageListFragment.this.data.group(messageObjectMessageAt2)) != null) {
                    if (selectionFromTop) {
                        ArrayList arrayList = groupedMessagesGroup.messages;
                        Intrinsics.checkNotNullExpressionValue(arrayList, "messages");
                        objLast = CollectionsKt.first(arrayList);
                    } else {
                        ArrayList arrayList2 = groupedMessagesGroup.messages;
                        Intrinsics.checkNotNullExpressionValue(arrayList2, "messages");
                        objLast = CollectionsKt.last(arrayList2);
                    }
                    MessageObject messageObject = (MessageObject) objLast;
                    MessageRows messageRows = MessageListFragment.this.data;
                    Intrinsics.checkNotNull(messageObject);
                    Integer numValueOf = Integer.valueOf(messageRows.positionOf(messageObject));
                    if (numValueOf.intValue() < 0) {
                        numValueOf = null;
                    }
                    if (numValueOf != null) {
                        return numValueOf.intValue();
                    }
                }
                return position2;
            }

            public void getPaddings(int[] paddings) {
                Intrinsics.checkNotNullParameter(paddings, "paddings");
                paddings[0] = recyclerListView.getPaddingTop();
                paddings[1] = recyclerListView.getPaddingBottom();
            }

            public void scrollBy(int dy) {
                recyclerListView.scrollBy(0, dy);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void setSelected(MessageObject message, boolean value) {
        ArrayList arrayList;
        MessageObject.GroupedMessages groupedMessagesGroup = this.data.group(message);
        ArrayList arrayListListOf = (groupedMessagesGroup == null || (arrayList = groupedMessagesGroup.messages) == null) ? CollectionsKt.listOf(message) : arrayList;
        LinkedHashSet<MessageObject> linkedHashSet = this.selected;
        if (value ? linkedHashSet.addAll(arrayListListOf) : linkedHashSet.removeAll(CollectionsKt.toSet(arrayListListOf))) {
            if (this.selected.isEmpty()) {
                clearSelection();
            } else {
                updateSelectionCount(true);
                updateSelectionCells(true);
            }
        }
    }

    private final void showMessageMenu(ChatMessageCell cell) {
        ArrayList arrayList;
        final MessageObject messageObject = cell.getMessageObject();
        if (messageObject == null) {
            return;
        }
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        if (jElapsedRealtime - this.lastMenuTime < 500) {
            return;
        }
        this.lastMenuTime = jElapsedRealtime;
        TextSelectionHelper.ChatListTextSelectionHelper chatListTextSelectionHelper = this.selectionHelper;
        if (chatListTextSelectionHelper != null && chatListTextSelectionHelper.isInSelectionMode()) {
            chatListTextSelectionHelper.clear();
        }
        if (getSelecting()) {
            toggleSelection(messageObject);
            return;
        }
        final MessageObject.GroupedMessages groupedMessagesGroup = this.data.group(messageObject);
        final ArrayList arrayListListOf = (groupedMessagesGroup == null || (arrayList = groupedMessagesGroup.messages) == null) ? CollectionsKt.listOf(messageObject) : arrayList;
        final CharSequence charSequenceCopyText = copyText(messageObject, groupedMessagesGroup);
        ItemOptions itemOptionsMakeOptions = ItemOptions.makeOptions(this, (View) cell);
        if (charSequenceCopyText != null) {
            itemOptionsMakeOptions.add(R.drawable.msg_copy, LocaleController.getString(R.string.Copy), new Runnable() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.10
                @Override // java.lang.Runnable
                public final void run() {
                    MessageListFragment.showMessageMenu$lambda$59(MessageListFragment.this, charSequenceCopyText);
                }
            });
        }
        boolean z = false;
        itemOptionsMakeOptions.addIf(messageObject.type == 1 && MessageMediaActions.INSTANCE.hasFile(messageObject), R.drawable.msg_copy_photo, LocaleController.getString(R.string.CopyPhoto), new Runnable() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.14
            @Override // java.lang.Runnable
            public final void run() {
                MessageListFragment.showMessageMenu$lambda$60(MessageListFragment.this, messageObject);
            }
        });
        itemOptionsMakeOptions.addIf(MessageMediaActions.INSTANCE.translatableText(messageObject, groupedMessagesGroup) != null, R.drawable.msg_translate, LocaleController.getString(R.string.TranslateMessage), new Runnable() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.15
            @Override // java.lang.Runnable
            public final void run() {
                MessageListFragment.showMessageMenu$lambda$61(MessageListFragment.this, messageObject, groupedMessagesGroup);
            }
        });
        itemOptionsMakeOptions.add(R.drawable.msg_forward, LocaleController.getString(R.string.Forward), new Runnable() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.16
            @Override // java.lang.Runnable
            public final void run() {
                MessageListFragment.showMessageMenu$lambda$62(MessageListFragment.this, arrayListListOf);
            }
        });
        itemOptionsMakeOptions.add(R.drawable.msg_saved, Strings.INSTANCE.getSAVE_TO_SAVED(), new Runnable() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.17
            @Override // java.lang.Runnable
            public final void run() {
                MessageListFragment.showMessageMenu$lambda$63(MessageListFragment.this, arrayListListOf);
            }
        });
        if (MessageMediaActions.INSTANCE.isGalleryMedia(messageObject)) {
            itemOptionsMakeOptions.add(R.drawable.msg_gallery, LocaleController.getString(R.string.SaveToGallery), new Runnable() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.18
                @Override // java.lang.Runnable
                public final void run() {
                    MessageListFragment.showMessageMenu$lambda$65(MessageListFragment.this, arrayListListOf);
                }
            });
        } else if (MessageMediaActions.INSTANCE.isDownloadable(messageObject)) {
            if (messageObject.isGif()) {
                itemOptionsMakeOptions.add(R.drawable.msg_gif, LocaleController.getString(R.string.SaveToGIFs), new Runnable() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.19
                    @Override // java.lang.Runnable
                    public final void run() {
                        MessageListFragment.showMessageMenu$lambda$66(MessageListFragment.this, messageObject);
                    }
                });
            }
            itemOptionsMakeOptions.add(R.drawable.msg_download, LocaleController.getString(messageObject.isMusic() ? R.string.SaveToMusic : R.string.SaveToDownloads), new Runnable() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.20
                @Override // java.lang.Runnable
                public final void run() {
                    MessageListFragment.showMessageMenu$lambda$68(MessageListFragment.this, arrayListListOf);
                }
            });
        }
        itemOptionsMakeOptions.addIf(messageObject.getDocument() != null || messageObject.isVideo(), R.drawable.msg_shareout, LocaleController.getString(R.string.ShareFile), new Runnable() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.21
            @Override // java.lang.Runnable
            public final void run() {
                MessageListFragment.showMessageMenu$lambda$69(MessageListFragment.this, messageObject);
            }
        });
        itemOptionsMakeOptions.add(R.drawable.msg_select, LocaleController.getString(R.string.Select), new Runnable() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.22
            @Override // java.lang.Runnable
            public final void run() {
                MessageListFragment.this.toggleSelection(messageObject);
            }
        });
        itemOptionsMakeOptions.add(R.drawable.msg_message, LocaleController.getString(R.string.ShowInChat), new Runnable() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.11
            @Override // java.lang.Runnable
            public final void run() {
                MessageListFragment.showMessageMenu$lambda$71(MessageListFragment.this, messageObject);
            }
        });
        if (getCanOpenEditHistory() && EditHistoryStore.INSTANCE.hasHistory(messageObject)) {
            z = true;
        }
        itemOptionsMakeOptions.addIf(z, R.drawable.menu_premium_clock, Strings.INSTANCE.getEDIT_HISTORY_TITLE(), new Runnable() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.12
            @Override // java.lang.Runnable
            public final void run() {
                MessageListFragment.showMessageMenu$lambda$72(MessageListFragment.this, messageObject);
            }
        });
        if (getCanDelete()) {
            itemOptionsMakeOptions.addGap();
            itemOptionsMakeOptions.add(R.drawable.msg_delete, Strings.INSTANCE.getDELETE_FOREVER(), true, new Runnable() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.13
                @Override // java.lang.Runnable
                public final void run() {
                    MessageListFragment.this.confirmDelete(arrayListListOf);
                }
            });
        }
        CharSequence charSequenceMenuFooter = menuFooter(messageObject);
        if (charSequenceMenuFooter != null) {
            itemOptionsMakeOptions.addGap();
            itemOptionsMakeOptions.addText(charSequenceMenuFooter, ID_DELETE);
        }
        itemOptionsMakeOptions.setOnTopOfScrim().show();
    }

    static final void showMessageMenu$lambda$59(MessageListFragment messageListFragment, CharSequence charSequence) {
        messageListFragment.copyToClipboard(charSequence, R.string.MessageCopied);
    }

    static final void showMessageMenu$lambda$60(MessageListFragment messageListFragment, MessageObject messageObject) {
        MessageMediaActions.INSTANCE.copyPhoto(messageListFragment, messageObject);
    }

    static final void showMessageMenu$lambda$61(MessageListFragment messageListFragment, MessageObject messageObject, MessageObject.GroupedMessages groupedMessages) {
        MessageMediaActions.INSTANCE.translate(messageListFragment, messageObject, groupedMessages);
    }

    static final void showMessageMenu$lambda$62(MessageListFragment messageListFragment, List list) {
        CopyForwardMenu.INSTANCE.forward(messageListFragment, list);
    }

    static final void showMessageMenu$lambda$63(MessageListFragment messageListFragment, List list) {
        CopyForwardMenu.INSTANCE.saveToSaved(messageListFragment, list);
    }

    static final void showMessageMenu$lambda$65(MessageListFragment messageListFragment, List list) {
        MessageMediaActions messageMediaActions = MessageMediaActions.INSTANCE;
        MessageListFragment messageListFragment2 = messageListFragment;
        MessageMediaActions messageMediaActions2 = MessageMediaActions.INSTANCE;
        ArrayList arrayList = new ArrayList();
        for (Object obj : list) {
            if (messageMediaActions2.isGalleryMedia((MessageObject) obj)) {
                arrayList.add(obj);
            }
        }
        messageMediaActions.saveToGallery(messageListFragment2, arrayList);
    }

    static final void showMessageMenu$lambda$66(MessageListFragment messageListFragment, MessageObject messageObject) {
        MessageMediaActions.INSTANCE.saveGif(messageListFragment, messageObject);
    }

    static final void showMessageMenu$lambda$68(MessageListFragment messageListFragment, List list) {
        MessageMediaActions messageMediaActions = MessageMediaActions.INSTANCE;
        MessageListFragment messageListFragment2 = messageListFragment;
        MessageMediaActions messageMediaActions2 = MessageMediaActions.INSTANCE;
        ArrayList arrayList = new ArrayList();
        for (Object obj : list) {
            if (messageMediaActions2.isDownloadable((MessageObject) obj)) {
                arrayList.add(obj);
            }
        }
        messageMediaActions.saveToDownloads(messageListFragment2, arrayList);
    }

    static final void showMessageMenu$lambda$69(MessageListFragment messageListFragment, MessageObject messageObject) {
        MessageMediaActions.INSTANCE.share(messageListFragment, messageObject);
    }

    static final void showMessageMenu$lambda$71(MessageListFragment messageListFragment, MessageObject messageObject) {
        messageListFragment.showInChat(messageObject.getId());
    }

    static final void showMessageMenu$lambda$72(MessageListFragment messageListFragment, MessageObject messageObject) {
        messageListFragment.presentFragment(EditHistoryFragment.INSTANCE.newInstance(messageListFragment.getDialogId(), messageObject.getId()));
    }

    private final void showLinkMenu(ChatMessageCell cell, final String url) {
        final Activity parentActivity = getParentActivity();
        if (parentActivity == null) {
            return;
        }
        ItemOptions.makeOptions(this, (View) cell).add(R.drawable.msg_openin, LocaleController.getString(R.string.Open), new Runnable() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.8
            @Override // java.lang.Runnable
            public final void run() {
                MessageListFragment.showLinkMenu$lambda$75(parentActivity, url);
            }
        }).add(R.drawable.msg_copy, LocaleController.getString(R.string.CopyLink), new Runnable() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.9
            @Override // java.lang.Runnable
            public final void run() {
                MessageListFragment.showLinkMenu$lambda$76(MessageListFragment.this, url);
            }
        }).setOnTopOfScrim().show();
    }

    static final void showLinkMenu$lambda$75(Activity activity, String str) {
        Browser.openUrl(activity, str);
    }

    static final void showLinkMenu$lambda$76(MessageListFragment messageListFragment, String str) {
        messageListFragment.copyToClipboard(str, R.string.LinkCopied);
    }

    public void didReceivedNotification(int id, int account, Object... args) {
        MessageObject playingMessageObject;
        Intrinsics.checkNotNullParameter(args, "args");
        RecyclerListView recyclerListView = this.chatListView;
        if (recyclerListView == null) {
            return;
        }
        if (id == NotificationCenter.emojiLoaded) {
            recyclerListView.invalidate();
            return;
        }
        Object obj = null;
        if (id == NotificationCenter.didSetNewWallpapper) {
            SizeNotifierFrameLayout sizeNotifierFrameLayout = this.fragmentView;
            SizeNotifierFrameLayout sizeNotifierFrameLayout2 = sizeNotifierFrameLayout instanceof SizeNotifierFrameLayout ? sizeNotifierFrameLayout : null;
            if (sizeNotifierFrameLayout2 != null) {
                sizeNotifierFrameLayout2.setBackgroundImage(Theme.getCachedWallpaper(), Theme.isWallpaperMotion());
            }
            View view = this.fragmentView;
            if (view != null) {
                view.invalidate();
            }
            recyclerListView.invalidate();
            return;
        }
        if (id == NotificationCenter.messagePlayingDidStart) {
            updatePlayback(recyclerListView, true);
            return;
        }
        if (id == NotificationCenter.messagePlayingDidReset || id == NotificationCenter.messagePlayingPlayStateChanged) {
            updatePlayback(recyclerListView, false);
            return;
        }
        if (id == NotificationCenter.messagePlayingProgressDidChanged) {
            Object orNull = ArraysKt.getOrNull(args, 0);
            Integer num = orNull instanceof Integer ? (Integer) orNull : null;
            if (num != null) {
                int iIntValue = num.intValue();
                for (Object obj2 : cells((RecyclerView) recyclerListView)) {
                    MessageObject messageObject = ((ChatMessageCell) obj2).getMessageObject();
                    if (messageObject != null && messageObject.getId() == iIntValue) {
                        obj = obj2;
                        break;
                    }
                }
                ChatMessageCell chatMessageCell = (ChatMessageCell) obj;
                if (chatMessageCell == null || (playingMessageObject = MediaController.getInstance().getPlayingMessageObject()) == null) {
                    return;
                }
                MessageObject messageObject2 = chatMessageCell.getMessageObject();
                if (messageObject2 != null) {
                    messageObject2.audioProgress = playingMessageObject.audioProgress;
                    messageObject2.audioProgressSec = playingMessageObject.audioProgressSec;
                    messageObject2.audioPlayerDuration = playingMessageObject.audioPlayerDuration;
                }
                chatMessageCell.updatePlayingMessageProgress();
            }
        }
    }

    private final void updatePlayback(RecyclerListView list, boolean started) {
        for (ChatMessageCell chatMessageCell : cells((RecyclerView) list)) {
            MessageObject messageObject = chatMessageCell.getMessageObject();
            if (messageObject != null) {
                if (messageObject.isVoice() || messageObject.isMusic()) {
                    chatMessageCell.updateButtonState(false, true, false);
                } else if (messageObject.isRoundVideo() && (started || !MediaController.getInstance().isPlayingMessage(messageObject))) {
                    chatMessageCell.checkVideoPlayback(!started, (Bitmap) null);
                }
            }
        }
    }

    /* JADX INFO: renamed from: getTextSelectionHelper, reason: from getter */
    public TextSelectionHelper.ChatListTextSelectionHelper getSelectionHelper() {
        return this.selectionHelper;
    }

    public boolean hasSelectedMessages() {
        return getSelecting();
    }

    public boolean canPerformActions() {
        return !getSelecting();
    }

    public void didPressUserAvatar(ChatMessageCell cell, TLRPC.User user, float touchX, float touchY, boolean asForward) {
        if (user != null) {
            ChatUtils.INSTANCE.openProfile(this, user.id);
        }
    }

    public void didPressChannelAvatar(ChatMessageCell cell, TLRPC.Chat chat, int postId, float touchX, float touchY, boolean asForward) {
        if (chat != null) {
            ChatUtils.INSTANCE.openProfile(this, -chat.id);
        }
    }

    public void didPressViaBot(ChatMessageCell cell, String username) {
        String str = username;
        if (str == null || str.length() == 0) {
            return;
        }
        getMessagesController().openByUserName(username, this, 0);
    }

    public void didLongPress(ChatMessageCell cell, float x, float y) {
        Intrinsics.checkNotNullParameter(cell, "cell");
        onMessageLongPress(cell);
    }

    public void didPressOther(ChatMessageCell cell, float otherX, float otherY) {
        Intrinsics.checkNotNullParameter(cell, "cell");
        showMessageMenu(cell);
    }

    public void didPressReplyMessage(ChatMessageCell cell, int id, float x, float y, boolean longpress) {
        scrollToMessage(id);
    }

    public void didPressHiddenForward(ChatMessageCell cell) {
        BulletinFactory.of(this).createErrorBulletin(LocaleController.getString(R.string.HidAccount)).show();
    }

    public void didPressCodeCopy(ChatMessageCell cell, MessageObject.TextLayoutBlock block) {
        StaticLayout staticLayout;
        CharSequence text;
        if (block == null || (staticLayout = block.textLayout) == null || (text = staticLayout.getText()) == null) {
            return;
        }
        copyToClipboard(text.toString(), R.string.CodeCopied);
    }

    public void didPressUrl(ChatMessageCell cell, CharacterStyle url, boolean longPress) {
        Intrinsics.checkNotNullParameter(cell, "cell");
        Intrinsics.checkNotNullParameter(url, "url");
        Activity parentActivity = getParentActivity();
        if (parentActivity == null) {
            return;
        }
        if (url instanceof URLSpanMono) {
            ((URLSpanMono) url).copyToClipboard();
            if (AndroidUtilities.shouldShowClipboardToast()) {
                BulletinFactory.of(this).createCopyBulletin(LocaleController.getString(R.string.TextCopied)).show();
                return;
            }
            return;
        }
        if (url instanceof URLSpanUserMention) {
            Long l = Utilities.parseLong(((URLSpanUserMention) url).getURL());
            if (l != null) {
                long jLongValue = l.longValue();
                if (getMessagesController().getUser(l) != null) {
                    ChatUtils.INSTANCE.openProfile(this, jLongValue);
                    return;
                }
                return;
            }
            return;
        }
        if (url instanceof URLSpanBotCommand) {
            String url2 = ((URLSpanBotCommand) url).getURL();
            Intrinsics.checkNotNullExpressionValue(url2, "getURL(...)");
            copyToClipboard(url2, R.string.TextCopied);
            return;
        }
        if (url instanceof URLSpanNoUnderline) {
            String url3 = ((URLSpanNoUnderline) url).getURL();
            if (url3 == null) {
                return;
            }
            if (StringsKt.startsWith$default(url3, "@", false, 2, (Object) null)) {
                MessagesController messagesController = getMessagesController();
                String strSubstring = url3.substring(1);
                Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
                messagesController.openByUserName(strSubstring, this, 0);
                return;
            }
            if (StringsKt.startsWith$default(url3, "#", false, 2, (Object) null) || StringsKt.startsWith$default(url3, "$", false, 2, (Object) null)) {
                openSearch(url3);
                return;
            } else if (longPress) {
                showLinkMenu(cell, url3);
                return;
            } else {
                Browser.openUrl(parentActivity, url3);
                return;
            }
        }
        if (!(url instanceof URLSpan)) {
            if (url instanceof ClickableSpan) {
                ((ClickableSpan) url).onClick((View) cell);
            }
        } else if (longPress) {
            String url4 = ((URLSpan) url).getURL();
            Intrinsics.checkNotNullExpressionValue(url4, "getURL(...)");
            showLinkMenu(cell, url4);
        } else {
            if (!(url instanceof URLSpanReplacement)) {
                URLSpan uRLSpan = (URLSpan) url;
                if (!AndroidUtilities.shouldShowUrlInAlert(uRLSpan.getURL())) {
                    Browser.openUrl(parentActivity, uRLSpan.getURL());
                    return;
                }
            }
            AlertsCreator.showOpenUrlAlert(this, ((URLSpan) url).getURL(), true, true);
        }
    }

    public void didPressWebPage(ChatMessageCell cell, TLRPC.WebPage webpage, String url, boolean safe) {
        Activity parentActivity = getParentActivity();
        if (parentActivity == null) {
            return;
        }
        if (url == null) {
            url = webpage != null ? webpage.url : null;
            if (url == null) {
                return;
            }
        }
        Browser.openUrl(parentActivity, url);
    }

    public void didPressBotButton(ChatMessageCell cell, TL_keyboard.KeyboardButtonProto button) {
        TL_keyboard.TL_inlineButtonTypeUrl type;
        String str;
        Activity parentActivity = getParentActivity();
        if (parentActivity == null || (type = TLKeyboardHelper.getType(button, TL_keyboard.TL_inlineButtonTypeUrl.class)) == null || (str = type.url) == null) {
            return;
        }
        Browser.openUrl(parentActivity, str);
    }

    public boolean needPlayMessage(ChatMessageCell cell, MessageObject messageObject, boolean muted) {
        Intrinsics.checkNotNullParameter(messageObject, "messageObject");
        MediaController mediaController = MediaController.getInstance();
        if (messageObject.isVoice() || messageObject.isRoundVideo()) {
            boolean zPlayMessage = mediaController.playMessage(messageObject, muted);
            mediaController.setVoiceMessagesPlaylist((ArrayList) null, false);
            return zPlayMessage;
        }
        if (!messageObject.isMusic()) {
            return false;
        }
        List<MessageObject> messages = this.data.getMessages();
        ArrayList arrayList = new ArrayList();
        for (Object obj : messages) {
            if (((MessageObject) obj).isMusic()) {
                arrayList.add(obj);
            }
        }
        return mediaController.setPlaylist(new ArrayList(arrayList), messageObject, 0L);
    }

    public void needOpenWebView(MessageObject message, String url, String title, String description, String originalUrl, int w, int h) {
        EmbedBottomSheet.show(this, message, this.photoProvider, title, description, originalUrl, url, w, h, false);
    }

    public void didPressInstantButton(ChatMessageCell cell, int type) {
        Activity parentActivity;
        TLRPC.MessageMedia messageMedia;
        TLRPC.WebPage webPage;
        Intrinsics.checkNotNullParameter(cell, "cell");
        MessageObject messageObject = cell.getMessageObject();
        if (messageObject == null || (parentActivity = getParentActivity()) == null || (messageMedia = messageObject.messageOwner.media) == null || (webPage = messageMedia.webpage) == null) {
            return;
        }
        if (type == 0 && webPage.cached_page != null) {
            ArticleViewer.getInstance().setParentActivity(parentActivity, this);
            ArticleViewer.getInstance().open(messageObject);
        } else {
            Browser.openUrl(parentActivity, webPage.url);
        }
    }

    public void didPressImage(ChatMessageCell cell, float x, float y, boolean fullPreview) {
        Activity parentActivity;
        Object obj;
        Intrinsics.checkNotNullParameter(cell, "cell");
        MessageObject messageObject = cell.getMessageObject();
        if (messageObject == null || (parentActivity = getParentActivity()) == null) {
            return;
        }
        if (messageObject.getInputStickerSet() != null) {
            showDialog((Dialog) new StickersAlert(parentActivity, this, messageObject.getInputStickerSet(), (TLRPC.TL_messages_stickerSet) null, (StickersAlert.StickersAlertDelegate) null, false));
            return;
        }
        if (messageObject.isVideo() || messageObject.isGif() || messageObject.type == 1 || (messageObject.type == 0 && !messageObject.isWebpageDocument())) {
            openMedia(messageObject);
            Unit unit = Unit.INSTANCE;
            return;
        }
        if (messageObject.type == 4) {
            if (AndroidUtilities.isMapsInstalled(this)) {
                LocationActivity locationActivity = new LocationActivity(0);
                locationActivity.setMessageObject(messageObject);
                Boolean.valueOf(presentFragment((BaseFragment) locationActivity));
                return;
            }
            return;
        }
        try {
            Result.Companion companion = Result.Companion;
            MessageListFragment messageListFragment = this;
            obj = Result.constructor-impl(Boolean.valueOf(AndroidUtilities.openForView(messageObject, parentActivity, this.resourceProvider, false)));
        } catch (Throwable th) {
            Result.Companion companion2 = Result.Companion;
            obj = Result.constructor-impl(ResultKt.createFailure(th));
        }
        Result.box-impl(obj);
    }

    private final void openMedia(MessageObject message) {
        List<MessageObject> messages = this.data.getMessages();
        ArrayList arrayList = new ArrayList();
        for (Object obj : messages) {
            MessageObject messageObject = (MessageObject) obj;
            if (messageObject.type == 1 || messageObject.isVideo()) {
                arrayList.add(obj);
            }
        }
        ArrayList arrayList2 = new ArrayList(arrayList);
        Iterator it = arrayList2.iterator();
        int i = 0;
        while (true) {
            if (!it.hasNext()) {
                i = -1;
                break;
            } else if (((MessageObject) it.next()) == message) {
                break;
            } else {
                i++;
            }
        }
        int i2 = i;
        PhotoViewer.getInstance().setParentActivity(this);
        if (i2 >= 0) {
            PhotoViewer.getInstance().openPhoto(arrayList2, i2, 0L, 0L, 0L, this.photoProvider);
        } else {
            PhotoViewer.getInstance().openPhoto(message, (ChatActivity) null, 0L, 0L, 0L, this.photoProvider);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Sequence<ChatMessageCell> cells(final RecyclerView recyclerView) {
        return SequencesKt.mapNotNull(CollectionsKt.asSequence(RangesKt.until(0, recyclerView.getChildCount())), new Function1() { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment.23
            public final Object invoke(Object obj) {
                return MessageListFragment.cells$lambda$87(recyclerView, ((Integer) obj).intValue());
            }
        });
    }

    static final ChatMessageCell cells$lambda$87(RecyclerView recyclerView, int i) {
        ChatMessageCell childAt = recyclerView.getChildAt(i);
        if (childAt instanceof ChatMessageCell) {
            return childAt;
        }
        return null;
    }

    /* JADX INFO: compiled from: MessageListFragment.kt */
    @Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0082\u0004\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J(\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0016¨\u0006\u000e"}, d2 = {"Lni/shikatu/re_extera/ui/messages/MessageListFragment$GroupOffsetsDecoration;", "Landroidx/recyclerview/widget/RecyclerView$ItemDecoration;", "<init>", "(Lni/shikatu/re_extera/ui/messages/MessageListFragment;)V", "getItemOffsets", "", "outRect", "Landroid/graphics/Rect;", "view", "Landroid/view/View;", "parent", "Landroidx/recyclerview/widget/RecyclerView;", "state", "Landroidx/recyclerview/widget/RecyclerView$State;", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private final class GroupOffsetsDecoration extends RecyclerView.ItemDecoration {
        public GroupOffsetsDecoration() {
        }

        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            MessageObject.GroupedMessages currentMessagesGroup;
            MessageObject.GroupedMessagePosition currentPosition;
            float[] fArr;
            Intrinsics.checkNotNullParameter(outRect, "outRect");
            Intrinsics.checkNotNullParameter(view, "view");
            Intrinsics.checkNotNullParameter(parent, "parent");
            Intrinsics.checkNotNullParameter(state, "state");
            outRect.bottom = 0;
            ChatMessageCell chatMessageCell = view instanceof ChatMessageCell ? (ChatMessageCell) view : null;
            if (chatMessageCell == null || (currentMessagesGroup = chatMessageCell.getCurrentMessagesGroup()) == null || (currentPosition = chatMessageCell.getCurrentPosition()) == null || (fArr = currentPosition.siblingHeights) == null) {
                return;
            }
            float fMax = Math.max(AndroidUtilities.displaySize.x, AndroidUtilities.displaySize.y) * 0.5f;
            int extraInsetHeight = chatMessageCell.getExtraInsetHeight();
            for (float f : fArr) {
                extraInsetHeight += (int) Math.ceil(f * fMax);
            }
            int iRound = extraInsetHeight + ((currentPosition.maxY - currentPosition.minY) * Math.round(7.0f * AndroidUtilities.density));
            Iterator it = currentMessagesGroup.posArray.iterator();
            Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
            while (it.hasNext()) {
                MessageObject.GroupedMessagePosition groupedMessagePosition = (MessageObject.GroupedMessagePosition) it.next();
                if (groupedMessagePosition.minY == currentPosition.minY && (groupedMessagePosition.minX != currentPosition.minX || groupedMessagePosition.maxX != currentPosition.maxX || groupedMessagePosition.minY != currentPosition.minY || groupedMessagePosition.maxY != currentPosition.maxY)) {
                    iRound -= ((int) Math.ceil(fMax * groupedMessagePosition.ph)) - AndroidUtilities.dp(4.0f);
                    break;
                }
            }
            outRect.bottom = -iRound;
        }
    }

    /* JADX INFO: compiled from: MessageListFragment.kt */
    @Metadata(d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0082\u0004\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0016J\u0010\u0010\n\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0017J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0014J\u0010\u0010\u000f\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u000eH\u0002J \u0010\u0010\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0016J\u0018\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u0016H\u0002J\u001f\u0010\u001a\u001a\u0004\u0018\u00010\u00162\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u0016H\u0002¢\u0006\u0002\u0010\u001b¨\u0006\u001c"}, d2 = {"Lni/shikatu/re_extera/ui/messages/MessageListFragment$AvatarRecyclerView;", "Lorg/telegram/ui/Components/RecyclerListView;", "context", "Landroid/content/Context;", "<init>", "(Lni/shikatu/re_extera/ui/messages/MessageListFragment;Landroid/content/Context;)V", "onInterceptTouchEvent", "", "e", "Landroid/view/MotionEvent;", "onTouchEvent", "dispatchDraw", "", "canvas", "Landroid/graphics/Canvas;", "drawSelectionBackgrounds", "drawChild", "child", "Landroid/view/View;", "drawingTime", "", "nextPosition", "", "cell", "Lorg/telegram/ui/Cells/ChatMessageCell;", "position", "previousPosition", "(Lorg/telegram/ui/Cells/ChatMessageCell;I)Ljava/lang/Integer;", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private final class AvatarRecyclerView extends RecyclerListView {
        final /* synthetic */ MessageListFragment this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AvatarRecyclerView(MessageListFragment messageListFragment, Context context) {
            super(context, messageListFragment.resourceProvider);
            Intrinsics.checkNotNullParameter(context, "context");
            this.this$0 = messageListFragment;
        }

        public boolean onInterceptTouchEvent(MotionEvent e) {
            Intrinsics.checkNotNullParameter(e, "e");
            TextSelectionHelper.ChatListTextSelectionHelper chatListTextSelectionHelper = this.this$0.selectionHelper;
            if (chatListTextSelectionHelper != null) {
                chatListTextSelectionHelper.checkSelectionCancel(e);
            }
            return super.onInterceptTouchEvent(e);
        }

        public boolean onTouchEvent(MotionEvent e) {
            Intrinsics.checkNotNullParameter(e, "e");
            TextSelectionHelper.ChatListTextSelectionHelper chatListTextSelectionHelper = this.this$0.selectionHelper;
            if (chatListTextSelectionHelper != null) {
                chatListTextSelectionHelper.checkSelectionCancel(e);
            }
            return super.onTouchEvent(e);
        }

        protected void dispatchDraw(Canvas canvas) {
            Intrinsics.checkNotNullParameter(canvas, "canvas");
            boolean zDrawSelectionBackgrounds = drawSelectionBackgrounds(canvas);
            super.dispatchDraw(canvas);
            if (zDrawSelectionBackgrounds || SystemClock.uptimeMillis() < this.this$0.selectionAnimationUntil) {
                invalidate();
            }
        }

        private final boolean drawSelectionBackgrounds(Canvas canvas) {
            MessageObject.GroupedMessages currentMessagesGroup;
            int childCount = getChildCount();
            boolean z = false;
            MessageObject.GroupedMessages groupedMessages = null;
            for (int i = 0; i < childCount; i++) {
                ChatMessageCell childAt = getChildAt(i);
                ChatMessageCell chatMessageCell = childAt instanceof ChatMessageCell ? childAt : null;
                if (chatMessageCell != null && ((currentMessagesGroup = chatMessageCell.getCurrentMessagesGroup()) == null || currentMessagesGroup != groupedMessages)) {
                    MessageObject.GroupedMessagePosition currentPosition = chatMessageCell.getCurrentPosition();
                    MessageBackgroundDrawable backgroundDrawable = chatMessageCell.getBackgroundDrawable();
                    if (backgroundDrawable.isAnimationInProgress()) {
                        z = true;
                    }
                    boolean z2 = z;
                    if ((backgroundDrawable.isAnimationInProgress() || chatMessageCell.isDrawingSelectionBackground()) && (currentPosition == null || (currentPosition.flags & 2) != 0)) {
                        float touchX = 0.0f;
                        if (!chatMessageCell.isHighlighted() && !chatMessageCell.isHighlightedAnimated()) {
                            int y = (int) chatMessageCell.getY();
                            int measuredHeight = chatMessageCell.getMeasuredHeight() + y;
                            if (currentPosition != null && currentMessagesGroup != null) {
                                int childCount2 = getChildCount();
                                long j = 0;
                                int i2 = 0;
                                float touchY = 0.0f;
                                while (i2 < childCount2) {
                                    ChatMessageCell childAt2 = getChildAt(i2);
                                    int i3 = childCount2;
                                    ChatMessageCell chatMessageCell2 = childAt2 instanceof ChatMessageCell ? childAt2 : null;
                                    if (chatMessageCell2 != null && chatMessageCell2.getCurrentMessagesGroup() == currentMessagesGroup) {
                                        y = Math.min(y, (int) chatMessageCell2.getY());
                                        measuredHeight = Math.max(measuredHeight, ((int) chatMessageCell2.getY()) + chatMessageCell2.getMeasuredHeight());
                                        MessageBackgroundDrawable backgroundDrawable2 = chatMessageCell2.getBackgroundDrawable();
                                        if (backgroundDrawable2.getLastTouchTime() > j) {
                                            long lastTouchTime = backgroundDrawable2.getLastTouchTime();
                                            touchX = backgroundDrawable2.getTouchX() + chatMessageCell2.getX();
                                            j = lastTouchTime;
                                            touchY = backgroundDrawable2.getTouchY() + chatMessageCell2.getY();
                                        }
                                    }
                                    i2++;
                                    childCount2 = i3;
                                }
                                backgroundDrawable.setTouchCoordsOverride(touchX, touchY - y);
                            }
                            canvas.save();
                            canvas.clipRect(0, y, getMeasuredWidth(), measuredHeight);
                            backgroundDrawable.setCustomPaint((Paint) null);
                            backgroundDrawable.setColor(getThemedColor(Theme.key_chat_selectedBackground));
                            backgroundDrawable.setBounds(0, y, getMeasuredWidth(), measuredHeight);
                            backgroundDrawable.draw(canvas);
                            canvas.restore();
                        } else if (currentPosition == null) {
                            Paint paint = Theme.chat_replyLinePaint;
                            paint.setColor(getThemedColor(Theme.key_chat_selectedBackground));
                            int alpha = paint.getAlpha();
                            paint.setAlpha((int) (alpha * chatMessageCell.getHighlightAlpha() * chatMessageCell.getAlpha()));
                            canvas.save();
                            canvas.translate(0.0f, chatMessageCell.getTranslationY());
                            canvas.drawRect(0.0f, chatMessageCell.getTop(), getMeasuredWidth(), chatMessageCell.getBottom(), paint);
                            paint.setAlpha(alpha);
                            canvas.restore();
                        }
                    }
                    groupedMessages = currentMessagesGroup;
                    z = z2;
                }
            }
            return z;
        }

        public boolean drawChild(Canvas canvas, View child, long drawingTime) {
            int childAdapterPosition;
            Integer numPreviousPosition;
            RecyclerView.ViewHolder viewHolderFindViewHolderForAdapterPosition;
            View view;
            Intrinsics.checkNotNullParameter(canvas, "canvas");
            Intrinsics.checkNotNullParameter(child, "child");
            boolean zDrawChild = super.drawChild(canvas, child, drawingTime);
            ChatMessageCell chatMessageCell = child instanceof ChatMessageCell ? (ChatMessageCell) child : null;
            if (chatMessageCell != null) {
                ChatMessageCell chatMessageCell2 = (ChatMessageCell) child;
                if (chatMessageCell2.getTranslationY() != 0.0f) {
                    canvas.save();
                    canvas.translate(0.0f, chatMessageCell2.getTranslationY());
                    chatMessageCell.drawCheckBox(canvas);
                    canvas.restore();
                } else {
                    chatMessageCell.drawCheckBox(canvas);
                }
                ImageReceiver avatarImage = chatMessageCell.getAvatarImage();
                if (avatarImage != null && (childAdapterPosition = getChildAdapterPosition(child)) >= 0) {
                    if (chatMessageCell.drawPinnedBottom() && findViewHolderForAdapterPosition(nextPosition(chatMessageCell, childAdapterPosition)) != null) {
                        avatarImage.setVisible(false, false);
                        return zDrawChild;
                    }
                    int y = (int) chatMessageCell2.getY();
                    if (chatMessageCell.drawPinnedTop()) {
                        ChatMessageCell chatMessageCell3 = chatMessageCell;
                        for (int i = 0; i < 20 && (numPreviousPosition = previousPosition(chatMessageCell3, childAdapterPosition)) != null && (viewHolderFindViewHolderForAdapterPosition = findViewHolderForAdapterPosition((childAdapterPosition = numPreviousPosition.intValue()))) != null && (view = viewHolderFindViewHolderForAdapterPosition.itemView) != null; i++) {
                            y = view.getTop();
                            if (!(view instanceof ChatMessageCell)) {
                                break;
                            }
                            chatMessageCell3 = (ChatMessageCell) view;
                            if (!chatMessageCell3.drawPinnedTop()) {
                                break;
                            }
                        }
                    }
                    int iCoerceAtMost = RangesKt.coerceAtMost((int) (chatMessageCell2.getY() + chatMessageCell.getPaddingTopAnimated() + chatMessageCell.layoutHeight), getMeasuredHeight() - getPaddingBottom());
                    if (iCoerceAtMost - AndroidUtilities.dp(48.0f) < y) {
                        iCoerceAtMost = AndroidUtilities.dp(48.0f) + y;
                    }
                    if (!chatMessageCell.drawPinnedBottom()) {
                        iCoerceAtMost = RangesKt.coerceAtMost(iCoerceAtMost, (int) (chatMessageCell.getY() + chatMessageCell.getMeasuredHeight()));
                    }
                    float slidingOffsetX = chatMessageCell.getSlidingOffsetX() + chatMessageCell.checkBoxTranslation;
                    canvas.save();
                    if (slidingOffsetX != 0.0f) {
                        canvas.translate(slidingOffsetX, 0.0f);
                    }
                    avatarImage.setImageY(iCoerceAtMost - AndroidUtilities.dp(44.0f));
                    avatarImage.setAlpha(chatMessageCell.shouldDrawAlphaLayer() ? chatMessageCell.getAlpha() : 1.0f);
                    avatarImage.setVisible(true, false);
                    chatMessageCell.drawAvatarWithOnlineStatus(canvas, avatarImage);
                    canvas.restore();
                    return zDrawChild;
                }
            }
            return zDrawChild;
        }

        private final int nextPosition(ChatMessageCell cell, int position) {
            MessageObject.GroupedMessagePosition currentPosition;
            MessageObject.GroupedMessages currentMessagesGroup = cell.getCurrentMessagesGroup();
            if (currentMessagesGroup != null && (currentPosition = cell.getCurrentPosition()) != null) {
                int iIndexOf = currentMessagesGroup.posArray.indexOf(currentPosition);
                int size = currentMessagesGroup.posArray.size();
                if ((currentPosition.flags & 8) != 0) {
                    return (position - size) + iIndexOf;
                }
                do {
                    position--;
                    iIndexOf++;
                    if (iIndexOf >= size) {
                        break;
                    }
                } while (((MessageObject.GroupedMessagePosition) currentMessagesGroup.posArray.get(iIndexOf)).minY <= currentPosition.maxY);
                return position;
            }
            return position - 1;
        }

        private final Integer previousPosition(ChatMessageCell cell, int position) {
            MessageObject.GroupedMessagePosition currentPosition;
            MessageObject.GroupedMessages currentMessagesGroup = cell.getCurrentMessagesGroup();
            if (currentMessagesGroup != null && (currentPosition = cell.getCurrentPosition()) != null) {
                int iIndexOf = currentMessagesGroup.posArray.indexOf(currentPosition);
                if (iIndexOf < 0) {
                    return null;
                }
                if ((currentPosition.flags & 4) != 0) {
                    return Integer.valueOf(position + iIndexOf + 1);
                }
                do {
                    position++;
                    iIndexOf--;
                    if (-1 >= iIndexOf) {
                        break;
                    }
                } while (((MessageObject.GroupedMessagePosition) currentMessagesGroup.posArray.get(iIndexOf)).maxY >= currentPosition.minY);
                return Integer.valueOf(position);
            }
            return Integer.valueOf(position + 1);
        }
    }

    /* JADX INFO: compiled from: MessageListFragment.kt */
    @Metadata(d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u0082\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007¢\u0006\u0004\b\u0003\u0010\u0004J\b\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u0006H\u0016J\u0018\u0010\t\u001a\u00020\u00022\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0006H\u0016J\u0018\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00022\u0006\u0010\b\u001a\u00020\u0006H\u0016J \u0010\u0010\u001a\u00020\u000e2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\b\u001a\u00020\u0006H\u0002J\u001a\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00142\b\u0010\u0018\u001a\u0004\u0018\u00010\u0014H\u0002¨\u0006\u0019"}, d2 = {"Lni/shikatu/re_extera/ui/messages/MessageListFragment$MessagesAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "<init>", "(Lni/shikatu/re_extera/ui/messages/MessageListFragment;)V", "getItemCount", "", "getItemViewType", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "onBindViewHolder", "", "holder", "bindMessage", "view", "Lorg/telegram/ui/Cells/ChatMessageCell;", "message", "Lorg/telegram/messenger/MessageObject;", "sameSender", "", "first", "second", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private final class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public MessagesAdapter() {
        }

        public int getItemCount() {
            return MessageListFragment.this.data.getRows().size();
        }

        public int getItemViewType(int position) {
            return MessageListFragment.this.data.getRows().get(position).isDateObject ? 1 : 0;
        }

        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view;
            Intrinsics.checkNotNullParameter(parent, "parent");
            Context context = parent.getContext();
            if (viewType == 1) {
                view = (View) new ChatActionCell(context, false, MessageListFragment.this.resourceProvider);
            } else {
                View chatMessageCell = new ChatMessageCell(context, MessageListFragment.this.currentAccount);
                chatMessageCell.setDelegate(MessageListFragment.this);
                chatMessageCell.setAllowAssistant(false);
                view = chatMessageCell;
            }
            view.setLayoutParams((ViewGroup.LayoutParams) new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerView.ViewHolder(view) { // from class: ni.shikatu.re_extera.ui.messages.MessageListFragment$MessagesAdapter$onCreateViewHolder$1
            };
        }

        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Intrinsics.checkNotNullParameter(holder, "holder");
            MessageObject messageObject = MessageListFragment.this.data.getRows().get(position);
            ChatActionCell chatActionCell = holder.itemView;
            Intrinsics.checkNotNullExpressionValue(chatActionCell, "itemView");
            if (chatActionCell instanceof ChatActionCell) {
                ChatActionCell chatActionCell2 = chatActionCell;
                chatActionCell2.setMessageObject(messageObject);
                chatActionCell2.setAlpha(1.0f);
            } else if (chatActionCell instanceof ChatMessageCell) {
                bindMessage((ChatMessageCell) chatActionCell, messageObject, position);
            }
        }

        /* JADX WARN: Code duplicated, block: B:30:0x0098  */
        /* JADX WARN: Code duplicated, block: B:33:0x00b1  */
        /* JADX WARN: Code duplicated, block: B:34:0x00b3  */
        /* JADX WARN: Code duplicated, block: B:41:0x00c4  */
        /* JADX WARN: Code duplicated, block: B:49:0x00f3  */
        /* JADX WARN: Code duplicated, block: B:53:0x0104  */
        /* JADX WARN: Code duplicated, block: B:56:0x0117  */
        private final void bindMessage(ChatMessageCell view, MessageObject message, int position) {
            int i;
            int i2;
            boolean zSameSender;
            boolean zSameSender2;
            boolean z;
            boolean z2;
            boolean z3;
            TLRPC.User user;
            int i3;
            MessageObject.GroupedMessages groupedMessagesGroup = MessageListFragment.this.data.group(message);
            TLRPC.Chat chat = MessageListFragment.this.getDialogId() < 0 ? MessageListFragment.this.getMessagesController().getChat(Long.valueOf(-MessageListFragment.this.getDialogId())) : null;
            MessageObject.GroupedMessagePosition position2 = groupedMessagesGroup != null ? groupedMessagesGroup.getPosition(message) : null;
            if (groupedMessagesGroup != null && position2 != null) {
                int iIndexOf = groupedMessagesGroup.posArray.indexOf(position2);
                int size = groupedMessagesGroup.posArray.size();
                if (groupedMessagesGroup.isDocuments) {
                    i = position + iIndexOf + 1;
                    i2 = (position - size) + iIndexOf;
                } else {
                    if ((position2.flags & 4) != 0) {
                        i3 = position + iIndexOf + 1;
                        zSameSender = false;
                    } else {
                        zSameSender = true;
                        i3 = -1;
                    }
                    if ((position2.flags & 8) != 0) {
                        i2 = (position - size) + iIndexOf;
                        zSameSender2 = false;
                        i = i3;
                    } else {
                        zSameSender2 = true;
                        i = i3;
                        i2 = -1;
                    }
                }
                if (!zSameSender2 && !(message.messageOwner.reply_markup instanceof TLRPC.TL_replyInlineMarkup)) {
                    zSameSender2 = sameSender(message, MessageListFragment.this.data.messageAt(i2));
                }
                if (!zSameSender) {
                    zSameSender = sameSender(message, MessageListFragment.this.data.messageAt(i));
                }
                boolean z4 = zSameSender;
                if (MessageListFragment.this.getDialogId() < 0) {
                    z = true;
                } else {
                    z = false;
                }
                view.isChat = z;
                if (ChatObject.isChannel(chat) || chat == null || !chat.megagroup) {
                    z2 = false;
                } else {
                    z2 = true;
                }
                view.isMegagroup = z2;
                view.isForum = ChatObject.isForum(chat);
                if (MessageListFragment.this.getDialogId() > 0 || (user = MessageListFragment.this.getMessagesController().getUser(Long.valueOf(MessageListFragment.this.getDialogId()))) == null || !user.bot) {
                    z3 = false;
                } else {
                    z3 = true;
                }
                view.isBot = z3;
                view.setMessageObject(message, groupedMessagesGroup, zSameSender2, z4, false);
                view.setHighlighted(message == MessageListFragment.this.highlighted);
                String str = MessageListFragment.this.searchQuery;
                view.setHighlightedText(str.length() > 0 ? str : null);
                MessageListFragment.this.bindSelection(view, false);
            }
            i = position + 1;
            i2 = position - 1;
            zSameSender2 = false;
            zSameSender = false;
            if (!zSameSender2) {
                zSameSender2 = sameSender(message, MessageListFragment.this.data.messageAt(i2));
            }
            if (!zSameSender) {
                zSameSender = sameSender(message, MessageListFragment.this.data.messageAt(i));
            }
            boolean z5 = zSameSender;
            if (MessageListFragment.this.getDialogId() < 0) {
                z = true;
            } else {
                z = false;
            }
            view.isChat = z;
            if (ChatObject.isChannel(chat)) {
                z2 = false;
            } else {
                z2 = false;
            }
            view.isMegagroup = z2;
            view.isForum = ChatObject.isForum(chat);
            if (MessageListFragment.this.getDialogId() > 0) {
                z3 = false;
            } else {
                z3 = false;
            }
            view.isBot = z3;
            view.setMessageObject(message, groupedMessagesGroup, zSameSender2, z5, false);
            view.setHighlighted(message == MessageListFragment.this.highlighted);
            String str2 = MessageListFragment.this.searchQuery;
            view.setHighlightedText(str2.length() > 0 ? str2 : null);
            MessageListFragment.this.bindSelection(view, false);
        }

        private final boolean sameSender(MessageObject first, MessageObject second) {
            return second != null && first.isOutOwner() == second.isOutOwner() && first.getFromChatId() == second.getFromChatId() && Math.abs(first.messageOwner.date - second.messageOwner.date) <= 300;
        }
    }
}
