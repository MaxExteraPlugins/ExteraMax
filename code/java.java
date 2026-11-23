package com.maxplugins.sample;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.PowerManager;
import android.os.SystemClock;
import android.text.Editable;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.collection.LongSparseArray;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.exteragram.messenger.utils.ui.TextPaint;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BaseController;
import org.telegram.messenger.BotInlineKeyboard;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FlagSecureReason;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.R;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_account;
import org.telegram.tgnet.tl.TL_keyboard;
import org.telegram.tgnet.tl.TL_stars;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.BackDrawable;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.ChatAvatarContainer;
import org.telegram.ui.Components.ColorPicker;
import org.telegram.ui.Components.ColoredImageSpan;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.PopupSwipeBackLayout;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.ProfileActivity;
import org.telegram.ui.SecretVoicePlayer;
import org.telegram.ui.SettingsActivity;

public class Main {

    private static final int DEL_PAGE_SIZE = 5000;

    private static Handler H = null;

    private static final String PREFS = "exteramax_prefs";

    private static final String TAG = "ExteraMax";

    public static final ConcurrentHashMap<Long, Set<Integer>> deletedStore = new ConcurrentHashMap<>();

    public static long pk(long j, int i) {
        return (j << 32) ^ (i & 4294967295L);
    }

    public static Set<Integer> getDeletedIds(long did) {
        Set<Integer> set = deletedStore.get(Long.valueOf(did));
        if (set == null) {
            set = ConcurrentHashMap.newKeySet();
            Set<Integer> existing = deletedStore.putIfAbsent(Long.valueOf(did), set);
            if (existing != null) set = existing;
        }
        return set;
    }

    public static void addDeleted(long did, Collection<Integer> mids) {
        if (did == 0 || did == 489000L || mids == null || mids.isEmpty()) return;
        Set<Integer> validMids = new HashSet<>();
        for (Integer mid : mids) {
            if (mid != null && mid.intValue() > 0) {
                validMids.add(mid);
            }
        }
        if (validMids.isEmpty()) return;
        getDeletedIds(did).addAll(validMids);
        if (db != null) {
            for (Integer mid : validMids) {
                db.rem.add(Long.valueOf(pk(did, mid.intValue())));
            }
        }
    }

    public static void removeDeleted(long did, Collection<Integer> mids) {
        if (did == 0 || mids == null || mids.isEmpty()) return;
        Set<Integer> set = deletedStore.get(Long.valueOf(did));
        if (set != null) {
            set.removeAll(mids);
        }
        if (did < -1000000000000L) {
            long chId = -did - 1000000000000L;
            Set<Integer> set2 = deletedStore.get(Long.valueOf(-chId));
            if (set2 != null) set2.removeAll(mids);
        } else if (did < 0) {
            long chId = -did;
            Set<Integer> set2 = deletedStore.get(Long.valueOf(-1000000000000L - chId));
            if (set2 != null) set2.removeAll(mids);
        }
        if (db != null) {
            db.purge(did, new ArrayList<>(mids));
            if (did < -1000000000000L) {
                long chId = -did - 1000000000000L;
                db.purge(-chId, new ArrayList<>(mids));
            } else if (did < 0) {
                long chId = -did;
                db.purge(-1000000000000L - chId, new ArrayList<>(mids));
            }
        }
    }

    public static boolean isDeletedMsg(long did, int mid) {
        if (mid <= 0 || did == 0) return false;
        Set<Integer> set = deletedStore.get(Long.valueOf(did));
        if (set != null && set.contains(Integer.valueOf(mid))) return true;
        if (did < -1000000000000L) {
            long chId = -did - 1000000000000L;
            set = deletedStore.get(Long.valueOf(-chId));
            if (set != null && set.contains(Integer.valueOf(mid))) return true;
        } else if (did < 0) {
            long chId = -did;
            set = deletedStore.get(Long.valueOf(-1000000000000L - chId));
            if (set != null && set.contains(Integer.valueOf(mid))) return true;
        }
        return false;
    }

    public static final ConcurrentHashMap<Long, Integer> readDatesCache = new ConcurrentHashMap<>();

    public static void saveReadDate(long did, int mid, int readDate) {
        if (did == 0 || mid <= 0 || readDate <= 0) return;
        readDatesCache.put(Long.valueOf(pk(did, mid)), Integer.valueOf(readDate));
        if (db != null) {
            db.saveReadDate(did, mid, readDate);
        }
    }

    public static int getReadDate(long did, int mid) {
        if (did == 0 || mid <= 0) return 0;
        Integer cached = readDatesCache.get(Long.valueOf(pk(did, mid)));
        if (cached != null) return cached.intValue();
        if (db != null) {
            int d = db.getReadDate(did, mid);
            if (d > 0) {
                readDatesCache.put(Long.valueOf(pk(did, mid)), Integer.valueOf(d));
                return d;
            }
        }
        return 0;
    }

    public static boolean isDeletedMsg(MessageObject mo) {
        if (mo == null) return false;
        if (mo.isSending() || (mo.messageOwner != null && mo.messageOwner.send_state != 0)) return false;
        long did = mo.getDialogId();
        if (did == 0 && mo.messageOwner != null) {
            if (mo.messageOwner.dialog_id != 0) {
                did = mo.messageOwner.dialog_id;
            } else {
                try {
                    did = MessageObject.getDialogId(mo.messageOwner);
                } catch (Throwable ignored) {}
            }
        }
        return mo.deleted || isDeletedMsg(did, mo.getId());
    }

    // ==================== Feature 1: Shadowban ====================
    public static class ShadowbanEntry {
        public final long userId;
        public final boolean hideDialog;
        public final boolean hideInGroups;

        public ShadowbanEntry(long userId, boolean hideDialog, boolean hideInGroups) {
            this.userId = userId;
            this.hideDialog = hideDialog;
            this.hideInGroups = hideInGroups;
        }
    }

    public static class ShadowbanStore {
        public static final ConcurrentHashMap<Long, ShadowbanEntry> map = new ConcurrentHashMap<>();

        public static boolean isEmpty() {
            return map.isEmpty();
        }

        public static boolean hidesDialog(long did) {
            if (did <= 0) return false;
            ShadowbanEntry entry = map.get(Long.valueOf(did));
            return entry != null && entry.hideDialog;
        }

        public static boolean hidesInGroups(long fromChatId) {
            if (fromChatId <= 0) return false;
            ShadowbanEntry entry = map.get(Long.valueOf(fromChatId));
            return entry != null && entry.hideInGroups;
        }

        public static void add(long uid, boolean hideDialog, boolean hideInGroups) {
            if (uid <= 0) return;
            map.put(Long.valueOf(uid), new ShadowbanEntry(uid, hideDialog, hideInGroups));
            if (Main.db != null) {
                Main.db.saveShadowban(uid, hideDialog, hideInGroups);
            }
        }

        public static void remove(long uid) {
            map.remove(Long.valueOf(uid));
            if (Main.db != null) {
                Main.db.removeShadowban(uid);
            }
        }

        public static List<ShadowbanEntry> all() {
            return new ArrayList<>(map.values());
        }
    }

    // ==================== Feature 2: Chat Exclusions ====================
    public static class DialogExclusion {
        public final long dialogId;
        public final int reading; // 0=DEFAULT, 1=ALWAYS, 2=NEVER
        public final int typing;  // 0=DEFAULT, 1=ALWAYS, 2=NEVER

        public DialogExclusion(long dialogId, int reading, int typing) {
            this.dialogId = dialogId;
            this.reading = reading;
            this.typing = typing;
        }
    }

    public static class ExclusionsStore {
        public static final ConcurrentHashMap<Long, DialogExclusion> map = new ConcurrentHashMap<>();

        public static int getMode(long did, boolean isReading) {
            DialogExclusion exc = map.get(Long.valueOf(did));
            if (exc == null && did != 0) {
                exc = map.get(Long.valueOf(-did));
            }
            if (exc == null) return 0;
            return isReading ? exc.reading : exc.typing;
        }

        public static void set(long did, int reading, int typing) {
            if (did == 0) return;
            if (reading == 0 && typing == 0) {
                remove(did);
                return;
            }
            map.put(Long.valueOf(did), new DialogExclusion(did, reading, typing));
            if (Main.db != null) {
                Main.db.saveExclusion(did, reading, typing);
            }
        }

        public static void remove(long did) {
            map.remove(Long.valueOf(did));
            if (Main.db != null) {
                Main.db.removeExclusion(did);
            }
        }

        public static List<DialogExclusion> all() {
            return new ArrayList<>(map.values());
        }
    }

    // ==================== Feature 3: Regex Filters ====================
    public static class RegexFiltersStore {
        private static final int FLAGS = Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL;
        private static final List<String> filters = new java.util.concurrent.CopyOnWriteArrayList<>();
        private static final List<Pattern> patterns = new java.util.concurrent.CopyOnWriteArrayList<>();

        public static void load(List<String> list) {
            filters.clear();
            patterns.clear();
            if (list != null) {
                for (String s : list) {
                    addInternal(s);
                }
            }
        }

        private static void addInternal(String regex) {
            if (regex == null || regex.trim().isEmpty()) return;
            String r = regex.trim();
            if (!filters.contains(r)) {
                filters.add(r);
                try {
                    patterns.add(Pattern.compile(r, FLAGS));
                } catch (Exception e) {}
            }
        }

        public static void add(String regex) {
            addInternal(regex);
            if (Main.db != null) {
                Main.db.addRegex(regex);
            }
        }

        public static void remove(String regex) {
            if (regex == null) return;
            String r = regex.trim();
            filters.remove(r);
            patterns.clear();
            for (String f : filters) {
                try {
                    patterns.add(Pattern.compile(f, FLAGS));
                } catch (Exception e) {}
            }
            if (Main.db != null) {
                Main.db.removeRegex(r);
            }
        }

        public static boolean isEmpty() {
            return patterns.isEmpty();
        }

        public static List<String> all() {
            return new ArrayList<>(filters);
        }

        public static boolean isFiltered(MessageObject message) {
            if (message == null || message.isOut() || !Main.cfg.b("filters_enabled", true)) {
                return false;
            }
            if (patterns.isEmpty()) return false;
            String text = null;
            if (message.messageOwner != null && message.messageOwner.message != null && !message.messageOwner.message.isEmpty()) {
                text = message.messageOwner.message;
            } else if (message.messageText != null && message.messageText.length() > 0) {
                text = message.messageText.toString();
            } else if (message.caption != null && message.caption.length() > 0) {
                text = message.caption.toString();
            }
            if (text == null || text.isEmpty()) return false;
            for (Pattern p : patterns) {
                try {
                    if (p.matcher(text).find()) {
                        return true;
                    }
                } catch (Exception e) {}
            }
            return false;
        }
    }

    // ==================== Feature 5: Forward Restrictions ====================
    public static class ForwardRestrictions {
        public static boolean isRestricted(MessageObject message) {
            if (message == null) return false;
            if (message.messageOwner != null && message.messageOwner.noforwards) return true;
            try {
                if (message.isSecretMedia()) return true;
            } catch (Throwable t) {}
            long did = message.getDialogId();
            if (did < 0) {
                try {
                    TLRPC.Chat chat = MessagesController.getInstance(message.currentAccount).getChat(Long.valueOf(-did));
                    if (chat != null && chat.noforwards) return true;
                } catch (Throwable t) {}
            }
            return false;
        }
    }

    // ==================== Feature 4: Gif Reuploader ====================
    public static class GifReuploader {
        public static boolean needsReupload(Object parent, TLRPC.Document document) {
            if (document == null) return false;
            if (document instanceof TLRPC.TL_documentEncrypted) return true;
            MessageObject mo = parent instanceof MessageObject ? (MessageObject) parent : null;
            if (mo != null && ForwardRestrictions.isRestricted(mo)) return true;
            return false;
        }

        public static void reupload(final MessageObject message, final TLRPC.Document document) {
            if (message == null || document == null) return;
            final int account = message.currentAccount;
            AndroidUtilities.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        File file = FileLoader.getInstance(account).getPathToAttach(document, true);
                        if (file == null || !file.exists() || file.length() == 0) {
                            file = FileLoader.getInstance(account).getPathToAttach(document);
                        }
                        if (file != null && file.exists() && file.length() > 0) {
                            sendGifToSaved(account, document, file);
                        } else {
                            FileLoader.getInstance(account).loadFile(document, message, 1, 0);
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                int attempts = 0;
                                @Override
                                public void run() {
                                    File f = FileLoader.getInstance(account).getPathToAttach(document, true);
                                    if (f == null || !f.exists()) f = FileLoader.getInstance(account).getPathToAttach(document);
                                    if (f != null && f.exists() && f.length() > 0) {
                                        sendGifToSaved(account, document, f);
                                    } else if (++attempts < 30) {
                                        AndroidUtilities.runOnUIThread(this, 500);
                                    } else {
                                        showErrorBulletin(tr("Не удалось загрузить GIF","Failed to load GIF"));
                                    }
                                }
                            }, 500);
                        }
                    } catch (Throwable t) {
                        Main.err("gifReupload", new Exception(t));
                    }
                }
            });
        }

        private static void sendGifToSaved(final int account, final TLRPC.Document document, final File file) {
            try {
                long selfId = UserConfig.getInstance(account).getClientUserId();
                TLRPC.TL_document videoDoc = new TLRPC.TL_document();
                videoDoc.id = 0;
                videoDoc.date = ConnectionsManager.getInstance(account).getCurrentTime();
                videoDoc.mime_type = "video/mp4";
                videoDoc.size = (int) file.length();
                TLRPC.TL_documentAttributeVideo attr = new TLRPC.TL_documentAttributeVideo();
                attr.supports_streaming = true;
                attr.nosound = true;
                attr.w = 320;
                attr.h = 320;
                attr.duration = 1.0;
                videoDoc.attributes.add(attr);
                videoDoc.attributes.add(new TLRPC.TL_documentAttributeAnimated());

                SendMessagesHelper.SendMessageParams params = new SendMessagesHelper.SendMessageParams();
                params.document = videoDoc;
                params.path = file.getAbsolutePath();
                params.peer = selfId;
                SendMessagesHelper.getInstance(account).sendMessage(params);

                org.telegram.messenger.MediaDataController.getInstance(account).addRecentGif(document, (int) (System.currentTimeMillis() / 1000), true);
                showSuccessBulletin(tr("GIF сохранён в Избранное!","GIF saved to Saved Messages!"));
            } catch (Throwable t) {
                Main.err("sendGifToSaved", new Exception(t));
                showErrorBulletin(tr("Ошибка сохранения GIF","Failed to save GIF"));
            }
        }

        private static void showSuccessBulletin(final String text) {
            AndroidUtilities.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    BaseFragment fragment = LaunchActivity.getLastFragment();
                    if (fragment != null) {
                        BulletinFactory.of(fragment).createSuccessBulletin(text).show();
                    }
                }
            });
        }

        private static void showErrorBulletin(final String text) {
            AndroidUtilities.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    BaseFragment fragment = LaunchActivity.getLastFragment();
                    if (fragment != null) {
                        BulletinFactory.of(fragment).createErrorBulletin(text).show();
                    }
                }
            });
        }
    }

    public static final String VERSION = "1.2.24";

    public static final int VERSION_CODE = 144;

    private static Config cfg;

    private static VaultDb db;

    private static Drawable delIcon;

    private static HookManager hm;

    private static PowerManager.WakeLock keepLock;

    private static boolean loaded = false;

    private static final Set<String> myDelMids = ConcurrentHashMap.newKeySet();

    public static volatile boolean openDelCustPending = false;

    private static final Set<String> justPurged = ConcurrentHashMap.newKeySet();

    private static final Set<String> keepInChat = ConcurrentHashMap.newKeySet();

    private static final Set<String> reentryMids = ConcurrentHashMap.newKeySet();

    private static final Map<ChatActivity, Boolean> injectedMenus = new WeakHashMap();

    private static final Set<Long> injectedMenuIds = ConcurrentHashMap.newKeySet();

    private static final Set<Long> myOutMids = ConcurrentHashMap.newKeySet();

    private static final Map<Long, Integer> pendingReadDialogs = new ConcurrentHashMap<>();

    private static volatile long lastOffline = 0;

    private static final Set<Long> presenceTracked = ConcurrentHashMap.newKeySet();

    private static final Map<Long, ArrayList<long[]>> presenceRecords = new ConcurrentHashMap();

    private static final Map<Long, Long> presenceLastSeen = new ConcurrentHashMap();

    private static final Map<Long, Long> presenceLamp = new ConcurrentHashMap();

    private static final Map<Long, long[]> presenceOpen = new ConcurrentHashMap();

    private static boolean presenceDirty = false;

    private static final Runnable keepTick = new Runnable() {

        @Override // java.lang.Runnable

        public void run() {

            if (Main.cfg != null) {

                if (Main.cfg.b("keep_app_alive", false)) {

                    if (Main.cfg.isGhost()) {

                        Main.sendOffline();

                    }

                    if (Main.H != null) {

                        Main.H.postDelayed(this, 20000L);

                    }

                }

            }

        }

    };

    private static final Runnable presenceTick = new Runnable() {

        @Override // java.lang.Runnable

        public void run() {

            try {

                Main.presenceTickInternal();

            } catch (Exception e) {

            }

            if (Main.H != null) {

                Main.H.postDelayed(this, 5000L);

            }

        }

    };

    private static final Set<Long> purgeDialogs = ConcurrentHashMap.newKeySet();

    static boolean access$3800() {

        return ownDelSkip();

    }

    public static void load() {

        if (loaded) {

            return;

        }

        loaded = true;

        H = new Handler(Looper.getMainLooper());

        try {

            Context context = ApplicationLoader.applicationContext;

            cfg = new Config(context);

            db = new VaultDb(context);

            try {

                initDelIcon();

            } catch (Throwable ignored) {

            }

            hm = new HookManager();

            hm.init();

            if (cfg.isGhost()) {

                sendOffline();

            }

            if (cfg.b("hide_ads", true)) {

                cleanupAds();

            }

            updateKeepAlive();

            if (cfg.b("fake_premium", false)) {

                applyLocalPremium();

            }

            loadPresence();

            startPresenceLoop();

            startVaultCleanupLoop();

            new Thread(new Runnable() {

                @Override // java.lang.Runnable

                public final void run() {

                    Main.lambda$load$0();

                }

            }).start();

        } catch (Exception e2) {

            err("load", e2);

        }

    }

    static void lambda$load$0() {

        try {

            getAttachDir();

        } catch (Exception e) {

        }

    }

    public static void unload() {

        if (hm != null) {

            hm.onUnload();

        }

        if (cfg != null && cfg.b("fake_premium", false)) {

            revertPremium();

        }

        stopKeepAlive();

        savePresence();

        loaded = false;

    }

    public static void pyShowDeletedList() {

        showDeletedList();

    }

    public static void pyPurgeChatDeleted(final long j) {

        ui(new Runnable() {

            @Override // java.lang.Runnable

            public final void run() {

                Main.lambda$pyPurgeChatDeleted$1(j);

            }

        });

    }

    public static void pyClearAllDeleted() {
        ui(new Runnable() {
            @Override
            public final void run() {
                try {
                    clearAllDeletedData(null);
                } catch (Exception e) {
                    err("pyClearAll", e);
                }
            }
        });
    }

    public static void clearAllDeletedData(final Runnable onDone) {
        try {
            final Map<Long, ArrayList<Integer>> allDeleted = (db != null) ? db.getAllDeletedByDialog() : new HashMap<Long, ArrayList<Integer>>();
            for (Map.Entry<Long, Set<Integer>> entry : deletedStore.entrySet()) {
                long d = entry.getKey();
                Set<Integer> set = entry.getValue();
                if (set != null && !set.isEmpty()) {
                    ArrayList<Integer> list = allDeleted.get(d);
                    if (list == null) {
                        list = new ArrayList<>(set);
                        allDeleted.put(d, list);
                    } else {
                        for (Integer mid : set) {
                            if (!list.contains(mid)) list.add(mid);
                        }
                    }
                }
            }

            int account = UserConfig.selectedAccount;
            for (Map.Entry<Long, ArrayList<Integer>> entry : allDeleted.entrySet()) {
                long did = entry.getKey();
                ArrayList<Integer> mids = entry.getValue();
                if (mids == null || mids.isEmpty()) continue;
                purgeDialogs.add(Long.valueOf(did));
                HookManager.DeletionBypass.allow(did, mids);
                try {
                    int channelId = 0;
                    if (did < -1000000000000L) {
                        channelId = (int) (-did - 1000000000000L);
                    }
                    MessagesController.getInstance(account).deleteMessages(new ArrayList<>(mids), null, null, did, channelId, false, 0);
                } catch (Throwable t) {
                    err("delAllMsg", new Exception(t));
                }
            }

            BaseFragment frag = LaunchActivity.getLastFragment();
            if (frag instanceof ChatActivity) {
                ChatActivity ca = (ChatActivity) frag;
                long openDid = ca.getDialogId();
                ArrayList<Integer> openMids = allDeleted.get(openDid);
                if (openMids == null && openDid < -1000000000000L) {
                    openMids = allDeleted.get(-(-openDid - 1000000000000L));
                }
                if (openMids != null && !openMids.isEmpty()) {
                    animatePurge(openDid, openMids);
                }
                try {
                    Field messagesField = ChatActivity.class.getDeclaredField("messages");
                    messagesField.setAccessible(true);
                    ArrayList messages = (ArrayList) messagesField.get(ca);
                    if (messages != null) {
                        Iterator it = messages.iterator();
                        boolean changed = false;
                        while (it.hasNext()) {
                            Object obj = it.next();
                            if (obj instanceof MessageObject) {
                                MessageObject mo = (MessageObject) obj;
                                if (mo.deleted || isDeletedMsg(mo)) {
                                    it.remove();
                                    changed = true;
                                }
                            }
                        }
                        if (changed) {
                            ca.updateMessagesVisiblePart(true);
                            ViewGroup chatListView = ca.getChatListView();
                            if (chatListView instanceof RecyclerView) {
                                RecyclerView.Adapter adapter = ((RecyclerView) chatListView).getAdapter();
                                if (adapter != null) adapter.notifyDataSetChanged();
                            }
                        }
                    }
                } catch (Throwable ignored) {}
            }

            if (db != null) {
                db.clearAllDeleted();
            }
            deletedStore.clear();
            justPurged.clear();
            readDatesCache.clear();
            refreshCells();
        } catch (Throwable t) {
            err("clearAllDeletedData", new Exception(t));
        }
        if (onDone != null) onDone.run();
    }

    public static void pySyncAllChats() {
        ui(new Runnable() {
            @Override
            public final void run() {
                try {
                    syncAllChatsFromCloud(null);
                } catch (Exception e) {
                    err("pySyncAll", e);
                }
            }
        });
    }

    public static void syncAllChatsFromCloud(final Runnable onDone) {
        try {
            final int account = UserConfig.selectedAccount;
            final MessagesController mc = MessagesController.getInstance(account);
            final Set<Long> dialogIds = new LinkedHashSet<>();

            try {
                ArrayList<TLRPC.Dialog> d0 = mc.getDialogs(0);
                if (d0 != null) {
                    for (TLRPC.Dialog d : d0) {
                        if (d != null && d.id != 0) dialogIds.add(d.id);
                    }
                }
            } catch (Throwable ignored) {}

            try {
                ArrayList<TLRPC.Dialog> d1 = mc.getDialogs(1);
                if (d1 != null) {
                    for (TLRPC.Dialog d : d1) {
                        if (d != null && d.id != 0) dialogIds.add(d.id);
                    }
                }
            } catch (Throwable ignored) {}

            try {
                Field fAll = MessagesController.class.getDeclaredField("allDialogs");
                fAll.setAccessible(true);
                ArrayList all = (ArrayList) fAll.get(mc);
                if (all != null) {
                    for (Object o : all) {
                        if (o instanceof TLRPC.Dialog) {
                            TLRPC.Dialog d = (TLRPC.Dialog) o;
                            if (d != null && d.id != 0) dialogIds.add(d.id);
                        }
                    }
                }
            } catch (Throwable ignored) {}

            BaseFragment frag = LaunchActivity.getLastFragment();
            final ChatActivity openCa = (frag instanceof ChatActivity) ? (ChatActivity) frag : null;
            if (openCa != null && openCa.getDialogId() != 0) {
                dialogIds.add(openCa.getDialogId());
            }

            if (dialogIds.isEmpty()) {
                if (onDone != null) onDone.run();
                return;
            }

            final AtomicInteger pending = new AtomicInteger(dialogIds.size());
            final ConnectionsManager cm = ConnectionsManager.getInstance(account);

            for (final Long didObj : dialogIds) {
                final long did = didObj;
                TLRPC.InputPeer peer = mc.getInputPeer(did);
                if (peer == null) {
                    if (pending.decrementAndGet() == 0) {
                        finishSyncAll(account, openCa, onDone);
                    }
                    continue;
                }

                TLRPC.TL_messages_getHistory req = new TLRPC.TL_messages_getHistory();
                req.peer = peer;
                req.limit = 50;
                req.offset_id = 0;

                cm.sendRequest(req, new RequestDelegate() {
                    @Override
                    public void run(final TLObject response, final TLRPC.TL_error error) {
                        try {
                            if (response instanceof TLRPC.messages_Messages) {
                                TLRPC.messages_Messages res = (TLRPC.messages_Messages) response;
                                if (res.messages != null && !res.messages.isEmpty()) {
                                    for (TLRPC.Message m : res.messages) {
                                        if (m != null) m.dialog_id = did;
                                    }
                                    MessagesStorage.getInstance(account).putMessages(res.messages, false, true, false, 0, false, 0, 0);

                                    if (openCa != null && openCa.getDialogId() == did) {
                                        AndroidUtilities.runOnUIThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    ArrayList<MessageObject> newObjects = new ArrayList<>();
                                                    for (TLRPC.Message m : res.messages) {
                                                        if (m != null) {
                                                            newObjects.add(new MessageObject(account, m, false, false));
                                                        }
                                                    }
                                                    if (openCa.messages != null) {
                                                        for (MessageObject newMo : newObjects) {
                                                            boolean found = false;
                                                            for (int i = 0; i < openCa.messages.size(); i++) {
                                                                MessageObject cur = openCa.messages.get(i);
                                                                if (cur != null && cur.getId() == newMo.getId()) {
                                                                    cur.deleted = false;
                                                                    cur.messageOwner = newMo.messageOwner;
                                                                    found = true;
                                                                    break;
                                                                }
                                                            }
                                                            if (!found) {
                                                                openCa.messages.add(newMo);
                                                            }
                                                        }
                                                    }
                                                    openCa.updateMessagesVisiblePart(true);
                                                    Main.forceUpdateViews(account, did, null);
                                                } catch (Throwable ignored) {}
                                            }
                                        });
                                    }
                                }
                            }
                        } catch (Throwable t) {
                            err("syncChatResp", new Exception(t));
                        } finally {
                            if (pending.decrementAndGet() == 0) {
                                finishSyncAll(account, openCa, onDone);
                            }
                        }
                    }
                });
            }
        } catch (Throwable t) {
            err("syncAllChats", new Exception(t));
            if (onDone != null) onDone.run();
        }
    }

    private static void finishSyncAll(final int account, final ChatActivity openCa, final Runnable onDone) {
        AndroidUtilities.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                try {
                    refreshCells();
                    BaseFragment frag = LaunchActivity.getLastFragment();
                    if (frag != null) {
                        BulletinFactory.of(frag).createSuccessBulletin(tr("Все чаты успешно синхронизированы с сервером","All chats synced with server")).show();
                    }
                } catch (Throwable ignored) {}
                if (onDone != null) onDone.run();
            }
        });
    }

    public static void reloadChatFromCloud(final ChatActivity ca) {
        if (ca == null) return;
        try {
            final long did = ca.getDialogId();
            if (did == 0) return;
            final int account = ca.getCurrentAccount();
            TLRPC.TL_messages_getHistory req = new TLRPC.TL_messages_getHistory();
            req.peer = MessagesController.getInstance(account).getInputPeer(did);
            req.limit = 100;
            req.offset_id = 0;
            ConnectionsManager.getInstance(account).sendRequest(req, new RequestDelegate() {
                @Override
                public void run(final TLObject response, final TLRPC.TL_error error) {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (response instanceof TLRPC.messages_Messages) {
                                    TLRPC.messages_Messages res = (TLRPC.messages_Messages) response;
                                    if (res.messages != null && !res.messages.isEmpty()) {
                                        for (TLRPC.Message m : res.messages) {
                                            if (m != null) m.dialog_id = did;
                                        }
                                        MessagesStorage.getInstance(account).putMessages(res.messages, false, true, false, 0, false, 0, 0);
                                        ArrayList<MessageObject> newObjects = new ArrayList<>();
                                        for (TLRPC.Message m : res.messages) {
                                            if (m != null) {
                                                newObjects.add(new MessageObject(account, m, false, false));
                                            }
                                        }
                                        if (ca.messages != null) {
                                            for (MessageObject newMo : newObjects) {
                                                boolean found = false;
                                                for (int i = 0; i < ca.messages.size(); i++) {
                                                    MessageObject cur = ca.messages.get(i);
                                                    if (cur != null && cur.getId() == newMo.getId()) {
                                                        cur.deleted = false;
                                                        cur.messageOwner = newMo.messageOwner;
                                                        found = true;
                                                        break;
                                                    }
                                                }
                                                if (!found) {
                                                    ca.messages.add(newMo);
                                                }
                                            }
                                        }
                                        ca.updateMessagesVisiblePart(true);
                                        Main.forceUpdateViews(account, did, null);
                                        BulletinFactory.of(ca).createSuccessBulletin(tr("История чата синхронизирована с сервером","Chat history synced with server")).show();
                                    }
                                }
                            } catch (Throwable t) {
                                err("reloadChatRes", new Exception(t));
                            }
                        }
                    });
                }
            });
        } catch (Throwable t) {
            err("reloadChatFromCloud", new Exception(t));
        }
    }

    static void lambda$pyPurgeChatDeleted$1(long j) {

        try {

            purgeChatDeleted(j);

        } catch (Exception e) {

            err("purgeChat", e);

        }

    }

    

    public static void purgeChatDeleted(long j) {

        if (j == 0 || db == null || !loaded) {

            return;

        }

        final ArrayList<Integer> removedMids = db.getRemovedMids(j);

        purgeDialogs.add(Long.valueOf(j));

        if (removedMids != null && !removedMids.isEmpty()) {

            try {

                HookManager.DeletionBypass.allow(j, removedMids);

                AndroidUtilities.runOnUIThread(new Runnable() {

                    @Override

                    public void run() {

                        MessagesController.getInstance(UserConfig.selectedAccount).deleteMessages(new ArrayList<>(removedMids), null, null, j, 0, false, 0);

                    }

                });

            } catch (Exception e) {

                err("purge1", e);

            }

        }

        db.purgeAllForDialogAsync(j, new Runnable() {

            @Override

            public void run() {

                animatePurge(j, removedMids);

            }

        });

        final long purgeDialog = j;

        new Thread(new Runnable() {

            @Override // java.lang.Runnable

            public final void run() {

                try {

                    Thread.sleep(1000L);

                } catch (Exception e) {

                }

purgeDialogs.remove(Long.valueOf(purgeDialog));

            }

        }).start();

    }

    private static void animatePurge(long dialogId, ArrayList<Integer> mids) {

        if (mids == null || mids.isEmpty()) return;

        final long fDialogId = dialogId;

        final ArrayList<Integer> fMids = mids;

        ui(new Runnable() {

            @Override

            public void run() {

                try {

                    ChatActivity chatActivity = (ChatActivity) LaunchActivity.getLastFragment();

                    if (!(chatActivity instanceof ChatActivity)) return;

                    Field messagesField = ChatActivity.class.getDeclaredField("messages");

                    messagesField.setAccessible(true);

                    ArrayList messages = (ArrayList) messagesField.get(chatActivity);

                    if (messages == null) return;

                    Set<Integer> toRemove = new HashSet<>(fMids);

                    ArrayList<Integer> positions = new ArrayList<>();

                    Iterator it = messages.iterator();

                    int pos = 0;

                    while (it.hasNext()) {

                        Object msg = it.next();

                        if (!(msg instanceof MessageObject)) { pos++; continue; }

                        MessageObject mo = (MessageObject) msg;

                        if (!toRemove.contains(Integer.valueOf(mo.getId()))) { pos++; continue; }

                        it.remove();

                        positions.add(pos);

                        pos++;

                    }

                    if (positions.isEmpty()) return;

                    ViewGroup chatListView = chatActivity.getChatListView();

                    if (!(chatListView instanceof RecyclerView)) return;

                    RecyclerView.Adapter adapter = ((RecyclerView) chatListView).getAdapter();

                    if (adapter == null) return;

                    for (int i = positions.size() - 1; i >= 0; i--) {

                        adapter.notifyItemRemoved(positions.get(i));

                    }

                } catch (Exception e) {}

            }

        });

    }

    public static boolean isRu() {

        try {

            if (LocaleController.getInstance() != null && LocaleController.getInstance().getCurrentLocaleInfo() != null) {

                String shortName = LocaleController.getInstance().getCurrentLocaleInfo().shortName;

                if (shortName != null) {

                    shortName = shortName.toLowerCase();

                    return shortName.startsWith("ru") || shortName.startsWith("be") || shortName.startsWith("uk");

                }

            }

        } catch (Exception ignore) {}

        try {

            String l = java.util.Locale.getDefault().getLanguage();

            if (l != null && (l.startsWith("ru") || l.startsWith("be") || l.startsWith("uk"))) return true;

        } catch (Exception ignore) {}

        return false;

    }

    public static String tr(String ru, String en) {

        return isRu() ? ru : en;

    }

    // ==================== Shared utilities (canonical logic, used by Python side) ====================

    public static long peerDid(TLRPC.Message msg) {
        if (msg == null || msg.peer_id == null) return 0L;
        try {
            if (msg.peer_id.user_id != 0) return msg.peer_id.user_id;
            if (msg.peer_id.chat_id != 0) return -msg.peer_id.chat_id;
            if (msg.peer_id.channel_id != 0) return -msg.peer_id.channel_id;
        } catch (Throwable t) {
        }
        return 0L;
    }

    public static String mediaHash(TLRPC.Message msg) {
        if (msg == null || msg.media == null) return null;
        try {
            if (msg.media.photo != null) return "photo_" + msg.media.photo.id;
            if (msg.media.document != null) return "doc_" + msg.media.document.id;
            if (msg.media.webpage != null) return "web_" + msg.media.webpage.id;
            return "media_" + msg.media.getClass().getSimpleName();
        } catch (Throwable t) {
            return null;
        }
    }

    public static String formatDate(long ts) {
        try {
            return new SimpleDateFormat("dd.MM.yy HH:mm", Locale.getDefault()).format(new Date(ts * 1000L));
        } catch (Throwable t) {
            return "";
        }
    }

    public static int compareVersions(String v1, String v2) {
        try {
            String[] a = v1.trim().split("\\.");
            String[] b = v2.trim().split("\\.");
            int n = Math.max(a.length, b.length);
            for (int i = 0; i < n; i++) {
                int x = i < a.length ? Integer.parseInt(a[i]) : 0;
                int y = i < b.length ? Integer.parseInt(b[i]) : 0;
                if (x != y) return x < y ? -1 : 1;
            }
            return 0;
        } catch (Throwable t) {
            return 0;
        }
    }

    public static String formatSize(long size) {
        if (size < 1024) return size + " B";
        if (size < 1024 * 1024) return (size / 1024) + " KB";
        return String.format(Locale.US, "%.1f MB", size / (1024.0 * 1024.0));
    }

    public static String encodeMessageB64(TLRPC.Message msg) {
        if (msg == null) return null;
        try {
            SerializedData sd = new SerializedData(msg.getObjectSize());
            msg.serializeToStream(sd);
            byte[] data = sd.toByteArray();
            sd.cleanup();
            return Base64.encodeToString(data, Base64.NO_WRAP);
        } catch (Throwable t) {
            return null;
        }
    }

    public static TLRPC.Message decodeMessageB64(String raw) {
        if (raw == null || raw.isEmpty()) return null;
        try {
            byte[] data = Base64.decode(raw, Base64.NO_WRAP);
            SerializedData sd = new SerializedData(data);
            TLRPC.Message m = TLRPC.Message.TLdeserialize(sd, sd.readInt32(false), false);
            sd.cleanup();
            return m;
        } catch (Throwable t) {
            return null;
        }
    }

    public static int presenceDecaySeconds(int idx) {
        int[] v = {30, 45, 60, 120};
        return (idx >= 0 && idx < v.length) ? v[idx] : 45;
    }

    public static String formatDuration(long sec) {
        if (sec < 60) return sec + "s";
        if (sec < 3600) return (sec / 60) + "m";
        return (sec / 3600) + "h" + ((sec % 3600) / 60) + "m";
    }

    public static void pyShowMessageHistory(final long did, final int mid) {

        AndroidUtilities.runOnUIThread(() -> {

            try {

                BaseFragment last = LaunchActivity.getLastFragment();

                if (last != null) {

                    last.presentFragment(MessageHistoryFragment.newInstance(did, mid));

                }

            } catch (Throwable t) {

                err("pyShowMessageHistory", new Exception(t));

            }

        });

    }

    public static void pyShowMessageHistoryWithRevisions(final long did, final int mid, final String revisionsJson) {

        if (revisionsJson != null && !revisionsJson.isEmpty()) {

            try {

                JSONArray arr = new JSONArray(revisionsJson);

                for (int i = 0; i < arr.length(); i++) {

                    JSONObject obj = arr.getJSONObject(i);

                    int ver = obj.optInt("ver", i);

                    long ts = obj.optLong("timestamp", 0);

                    String text = obj.optString("text", "");

                    String dataB64 = obj.optString("data", "");

                    TLRPC.Message m = null;

                    if (!dataB64.isEmpty()) {

                        try {

                            byte[] bytes = Base64.decode(dataB64, Base64.DEFAULT);

                            m = VaultDb.deser(bytes);

                        } catch (Exception ignore) {}

                    }

                    if (m != null) {

                        if (ver == 0) {

                            db.saveOrig(did, mid, m);

                        } else {

                            db.saveVer(did, mid, m);

                        }

                    }

                }

            } catch (Throwable ignore) {}

        }

        pyShowMessageHistory(did, mid);

    }

    public static Config getConfig() {

        return cfg;

    }

    public static VaultDb getDb() {

        return db;

    }

    public static class DeletedIconSpan extends ColoredImageSpan {
        private final Drawable iconDrawable;
        private final int customColor;

        public DeletedIconSpan(Drawable drawable, int color) {
            super(drawable);
            this.iconDrawable = drawable;
            this.customColor = color;
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
            if (this.iconDrawable == null) {
                return;
            }
            if (this.customColor != 0) {
                int count = canvas.saveLayer(x - 4.0f, (float) top - 4.0f, x + 100.0f, (float) bottom + 4.0f, null);
                this.recolorDrawable = false;
                try {
                    this.iconDrawable.setColorFilter(new PorterDuffColorFilter(0xFFFFFFFF, PorterDuff.Mode.SRC_IN));
                    this.iconDrawable.setAlpha(255);
                } catch (Throwable ignored) {}
                super.draw(canvas, text, start, end, x, top, y, bottom, paint);
                int brightColor = (this.customColor & 0x00FFFFFF) | 0xFF000000;
                canvas.drawColor(brightColor, PorterDuff.Mode.SRC_IN);
                canvas.restoreToCount(count);
            } else {
                this.recolorDrawable = true;
                super.draw(canvas, text, start, end, x, top, y, bottom, paint);
            }
        }
    }

    public static class TrashIconDrawable extends Drawable {
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private int alpha = 255;
        private ColorFilter colorFilter;

        public TrashIconDrawable() {
            this(0xFFFFFFFF);
        }

        public TrashIconDrawable(int color) {
            this.paint.setStyle(Paint.Style.STROKE);
            this.paint.setStrokeCap(Paint.Cap.ROUND);
            this.paint.setStrokeJoin(Paint.Join.ROUND);
            this.paint.setColor(color != 0 ? color : 0xFFFFFFFF);
            setBounds(0, 0, AndroidUtilities.dp(14), AndroidUtilities.dp(14));
        }

        @Override
        public void draw(Canvas canvas) {
            Rect b = getBounds();
            float w = b.width();
            float h = b.height();
            if (w <= 0 || h <= 0) {
                w = AndroidUtilities.dp(14);
                h = AndroidUtilities.dp(14);
            }
            float s = Math.min(w, h);
            if (s <= 0) return;
            float left = b.left + (w - s) * 0.5f;
            float top = b.top + (h - s) * 0.5f;
            float scale = s / 24.0f;

            this.paint.setStrokeWidth(Math.max(1.5f, 1.8f * scale));

            canvas.drawLine(left + 4.0f * scale, top + 6.5f * scale, left + 20.0f * scale, top + 6.5f * scale, this.paint);

            Path handle = new Path();
            handle.moveTo(left + 9.5f * scale, top + 6.5f * scale);
            handle.lineTo(left + 9.5f * scale, top + 3.5f * scale);
            handle.lineTo(left + 14.5f * scale, top + 3.5f * scale);
            handle.lineTo(left + 14.5f * scale, top + 6.5f * scale);
            canvas.drawPath(handle, this.paint);

            Path body = new Path();
            body.moveTo(left + 6.0f * scale, top + 6.5f * scale);
            body.lineTo(left + 7.5f * scale, top + 19.0f * scale);
            body.quadTo(left + 7.7f * scale, top + 21.0f * scale, left + 9.5f * scale, top + 21.0f * scale);
            body.lineTo(left + 14.5f * scale, top + 21.0f * scale);
            body.quadTo(left + 16.3f * scale, top + 21.0f * scale, left + 16.5f * scale, top + 19.0f * scale);
            body.lineTo(left + 18.0f * scale, top + 6.5f * scale);
            canvas.drawPath(body, this.paint);

            canvas.drawLine(left + 10.2f * scale, top + 10.0f * scale, left + 10.2f * scale, top + 17.5f * scale, this.paint);
            canvas.drawLine(left + 13.8f * scale, top + 10.0f * scale, left + 13.8f * scale, top + 17.5f * scale, this.paint);
        }

        @Override
        public void setAlpha(int alpha) {
            this.alpha = alpha;
            this.paint.setAlpha(alpha);
            invalidateSelf();
        }

        @Override
        public void setColorFilter(ColorFilter colorFilter) {
            this.colorFilter = colorFilter;
            this.paint.setColorFilter(colorFilter);
            invalidateSelf();
        }

        @Override
        public int getOpacity() {
            return android.graphics.PixelFormat.TRANSLUCENT;
        }

        @Override
        public int getIntrinsicWidth() {
            return AndroidUtilities.dp(14);
        }

        @Override
        public int getIntrinsicHeight() {
            return AndroidUtilities.dp(14);
        }

        @Override
        public Drawable mutate() {
            TrashIconDrawable d = new TrashIconDrawable(this.paint.getColor());
            d.setBounds(getBounds());
            d.setAlpha(this.alpha);
            d.setColorFilter(this.colorFilter);
            return d;
        }
    }

    public static synchronized void initDelIcon() {

        if (delIcon != null && !(delIcon instanceof TrashIconDrawable)) {

            return;

        }

        Context context = ApplicationLoader.applicationContext;

        if (context == null) {

            return;

        }

        String[] names = new String[]{"msg_delete_filled", "msg_delete", "msg_delete_auto", "delete"};

        String[] pkgs = new String[]{context.getPackageName(), "org.telegram.messenger", "com.exteragram.messenger", "com.radolyn.ayugram"};

        for (String name : names) {

            for (String pkg : pkgs) {

                if (pkg == null) continue;

                try {

                    int id = context.getResources().getIdentifier(name, "drawable", pkg);

                    if (id != 0) {

                        Drawable d = ContextCompat.getDrawable(context, id);

                        if (d != null) {

                            delIcon = d.mutate();

                            delIcon.setBounds(0, 0, delIcon.getIntrinsicWidth(), delIcon.getIntrinsicHeight());

                            return;

                        }

                    }

                } catch (Throwable ignored) {}

            }

        }

        try {

            Field f = R.drawable.class.getField("msg_delete_filled");

            int id = f.getInt(null);

            if (id != 0) {

                Drawable d = ContextCompat.getDrawable(context, id);

                if (d != null) {

                    delIcon = d.mutate();

                    delIcon.setBounds(0, 0, delIcon.getIntrinsicWidth(), delIcon.getIntrinsicHeight());

                    return;

                }

            }

        } catch (Throwable ignored) {}

        try {

            Field f = R.drawable.class.getField("msg_delete");

            int id = f.getInt(null);

            if (id != 0) {

                Drawable d = ContextCompat.getDrawable(context, id);

                if (d != null) {

                    delIcon = d.mutate();

                    delIcon.setBounds(0, 0, delIcon.getIntrinsicWidth(), delIcon.getIntrinsicHeight());

                    return;

                }

            }

        } catch (Throwable ignored) {}

        try {

            Drawable d = ContextCompat.getDrawable(context, R.drawable.msg_delete_filled);

            if (d != null) {

                delIcon = d.mutate();

                delIcon.setBounds(0, 0, delIcon.getIntrinsicWidth(), delIcon.getIntrinsicHeight());

                return;

            }

        } catch (Throwable ignored) {}

        try {

            Drawable d = ContextCompat.getDrawable(context, R.drawable.msg_delete);

            if (d != null) {

                delIcon = d.mutate();

                delIcon.setBounds(0, 0, delIcon.getIntrinsicWidth(), delIcon.getIntrinsicHeight());

                return;

            }

        } catch (Throwable ignored) {}

        if (delIcon == null) {
            delIcon = new TrashIconDrawable();
        }
    }

    public static Drawable getDeletedIcon() {

        if (delIcon == null) {

            initDelIcon();

        }

        if (delIcon != null) {

            try {

                Drawable.ConstantState cs = delIcon.getConstantState();

                if (cs != null) {

                    Drawable d = cs.newDrawable().mutate();

                    d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());

                    return d;

                }

            } catch (Throwable ignored) {}

            return delIcon.mutate();

        }

        return new TrashIconDrawable();

    }

    public static boolean isGhost() {

        return cfg != null && cfg.isGhost();

    }

    public static void sendOffline() {

        try {

            TL_account.updateStatus updatestatus = new TL_account.updateStatus();

            updatestatus.offline = true;

            sendTL(updatestatus);

            lastOffline = System.currentTimeMillis();

        } catch (Exception e) {

        }

    }

    

    public static void setPremium(Object obj, boolean z) {

        if (obj == null) {

            return;

        }

        try {

            sf(obj, "premium", Boolean.valueOf(z));

        } catch (Exception e) {

        }

        try {

            Field declaredField = obj.getClass().getDeclaredField("flags");

            declaredField.setAccessible(true);

            int i = declaredField.getInt(obj);

            declaredField.setInt(obj, z ? i | 536870912 : i & (-536870913));

        } catch (Exception e2) {

        }

        try {

            Field declaredField2 = obj.getClass().getDeclaredField("flags2");

            declaredField2.setAccessible(true);

            int i2 = declaredField2.getInt(obj);

            declaredField2.setInt(obj, z ? i2 | 1 : i2 & (-2));

        } catch (Exception e3) {

        }

        if (z) {

            try {

                Field declaredField3 = obj.getClass().getDeclaredField("collectible_id");

                declaredField3.setAccessible(true);

                long val = declaredField3.getLong(obj);

                if (val == 0) {

                    declaredField3.setLong(obj, 1);

                }

            } catch (Exception e4) {

            }

        }

    }

    private static void updateKeepAlive() {

        if (cfg == null) {

            return;

        }

        if (cfg.b("keep_app_alive", false)) {

            startKeepAlive();

        } else {

            stopKeepAlive();

        }

    }

    private static void startKeepAlive() {

        try {

            if (keepLock == null) {

                keepLock = ((PowerManager) ApplicationLoader.applicationContext.getSystemService("power")).newWakeLock(1, "MAX:KeepAlive");

                keepLock.setReferenceCounted(false);

            }

            if (!keepLock.isHeld()) {

                keepLock.acquire();

            }

        } catch (Exception e) {

        }

        if (H != null) {

            H.removeCallbacks(keepTick);

            H.postDelayed(keepTick, 2000L);

        }

    }

    private static void stopKeepAlive() {

        try {

            if (keepLock != null && keepLock.isHeld()) {

                keepLock.release();

            }

        } catch (Exception e) {

        }

        if (H != null) {

            H.removeCallbacks(keepTick);

        }

    }

    private static void applyLocalPremium() {

        for (int i = 0; i < 16; i++) {

            try {

                UserConfig userConfig = UserConfig.getInstance(i);

                if (userConfig != null && userConfig.isClientActivated()) {

                    long clientUserId = userConfig.getClientUserId();

                    TLRPC.User currentUser = userConfig.getCurrentUser();

                    if (currentUser != null) {

                        setPremium(currentUser, true);

                    }

                    try {

                        TLRPC.User user = MessagesController.getInstance(i).getUser(Long.valueOf(clientUserId));

                        if (user != null) {

                            setPremium(user, true);

                        }

                    } catch (Exception e) {

                    }

                }

            } catch (Exception e2) {

            }

        }

        try {

            notifyPremiumChanged();

        } catch (Exception e3) {

        }

    }

    private static void notifyPremiumChanged() {

        for (int i = 0; i < 16; i++) {

            try {

                Class<?> cls = Class.forName("org.telegram.messenger.NotificationCenter");

                Object invoke = cls.getMethod("getInstance", Integer.TYPE).invoke(null, Integer.valueOf(i));

                Field declaredField = cls.getDeclaredField("updateInterfaces");

                declaredField.setAccessible(true);

                cls.getMethod("postNotificationName", Integer.TYPE, Object[].class).invoke(invoke, Integer.valueOf(declaredField.getInt(null)), new Object[]{3});

                try {

                    Field declaredField2 = cls.getDeclaredField("userEmojiStatusUpdated");

                    declaredField2.setAccessible(true);

                    int i2 = declaredField2.getInt(null);

                    UserConfig userConfig = UserConfig.getInstance(i);

                    TLRPC.User currentUser = userConfig != null ? userConfig.getCurrentUser() : null;

                    if (currentUser != null) {

                        cls.getMethod("postNotificationName", Integer.TYPE, Object[].class).invoke(invoke, Integer.valueOf(i2), new Object[]{currentUser});

                    }

                } catch (Exception e) {

                }

            } catch (Exception e2) {

            }

        }

    }

    private static void startPresenceLoop() {

        if (H == null) {

            return;

        }

        H.removeCallbacks(presenceTick);

        H.postDelayed(presenceTick, 5000L);

    }

    private static void startVaultCleanupLoop() {

        // Periodic cleanup of stale vault entries (messages that no longer exist in storage).

        // Runs once daily to avoid excessive I/O.

        new Thread(new Runnable() {

            @Override

            public void run() {

                try {

                    Thread.sleep(60000L); // Wait 1 minute after startup before first run

                } catch (InterruptedException e) {

                    return;

                }

                while (loaded) {

                    try {

                        purgeStaleVaultEntries();

                    } catch (Exception e) {

                        err("vaultCleanup", e);

                    }

                    try {

                        Thread.sleep(86400000L); // 24 hours

                    } catch (InterruptedException e) {

                        break;

                    }

                }

            }

        }, "VaultCleanup").start();

    }

    private static void purgeStaleVaultEntries() {

        // Ранее здесь был N+1: по одному MessagesStorage.getMessage на каждую

        // сохранённую удалёнку — тысячи последовательных запросов к базе

        // Telegram блокировали её же соединение, из-за чего медленно грузились

        // чаты после старта. Логика была и бесполезна (для записей deleted_keys

        // проверка !isRem всегда ложна). Хранилище чистится только явными

        // действиями пользователя (очистка/удаление), записи с mid<=0 — в loadKeys.

    }

    static void sendTL(TLObject tLObject) {

        Method[] methods;

        try {

            ConnectionsManager connectionsManager = ConnectionsManager.getInstance(UserConfig.selectedAccount);

            for (Method method : connectionsManager.getClass().getMethods()) {

                if ("sendRequest".equals(method.getName())) {

                    Class<?>[] parameterTypes = method.getParameterTypes();

                    if (parameterTypes.length == 2 && parameterTypes[0] == TLObject.class) {

                        method.invoke(connectionsManager, tLObject, Proxy.newProxyInstance(parameterTypes[1].getClassLoader(), new Class[]{parameterTypes[1]}, new InvocationHandler() {

                            @Override // java.lang.reflect.InvocationHandler

                            public final Object invoke(Object obj, Method method2, Object[] objArr) throws Throwable {

                                return Main.lambda$sendTL$3(obj, method2, objArr);

                            }

                        }));

                        return;

                    }

                }

            }

        } catch (Exception e) {

        }

    }

    static Object lambda$sendTL$3(Object obj, Method method, Object[] objArr) throws Throwable {

        return null;

    }

    public static void toggleGhost() {

        boolean b = cfg.b("ghost_mode", false);

        cfg.set("ghost_mode", !b);

        if (!b) {

            sendOffline();

        }

    }

    public static void markMyDel(final ArrayList<Integer> arrayList, long j) {

        if (arrayList == null) {

            return;

        }

        Iterator<Integer> it = arrayList.iterator();

        while (it.hasNext()) {

            int mid = it.next().intValue();

            myDelMids.add(j + ":" + mid);

        }

        if (H != null) {

            H.postDelayed(new Runnable() {

                @Override // java.lang.Runnable

                public final void run() {

                    Main.lambda$markMyDel$4(arrayList, j);

                }

            }, 5000L);

        }

    }

    static void lambda$markMyDel$4(ArrayList arrayList, long j) {

        Iterator it = arrayList.iterator();

        while (it.hasNext()) {

            int mid = ((Integer) it.next()).intValue();

            myDelMids.remove(j + ":" + mid);

        }

    }

    public static void markMyDelWithOut(final ArrayList<Integer> arrayList, long j) {

        if (arrayList == null) {

            return;

        }

        final long otherForm = j < 0 ? -j : (j > 0 ? -j : 0);

        Iterator<Integer> it = arrayList.iterator();

        while (it.hasNext()) {

            int intValue = it.next().intValue();

            long j2 = intValue;

            myDelMids.add(j + ":" + j2);

            myOutMids.add(Long.valueOf(j2));

            if (otherForm != 0) {

                myDelMids.add(otherForm + ":" + j2);

            }

        }

        if (H != null) {

            final long dialogId = j;

            H.postDelayed(new Runnable() {

                @Override // java.lang.Runnable

                public final void run() {

                    Main.lambda$markMyDelWithOut$5(arrayList, dialogId);

                }

            }, 30000L);

        }

    }

    static void lambda$markMyDelWithOut$5(ArrayList arrayList, long j) {

        final long otherForm = j < 0 ? -j : (j > 0 ? -j : 0);

        Iterator it = arrayList.iterator();

        while (it.hasNext()) {

            int mid = ((Integer) it.next()).intValue();

            myDelMids.remove(j + ":" + mid);

            if (otherForm != 0) {

                myDelMids.remove(otherForm + ":" + mid);

            }

            myOutMids.remove(Long.valueOf(mid));

        }

    }

    public static boolean isMyDel(long j, int i) {

        if (myDelMids.contains(j + ":" + i)) return true;

        if (j != 0 && myDelMids.contains((-j) + ":" + i)) return true;

        return myOutMids.contains(Long.valueOf(i));

    }

    // Fallback for cases where dialog ID is not available (e.g., batch updates).

    // Checks if the message ID exists in myDelMids for ANY dialog.

    // Less precise but handles edge cases where did is unknown.

    public static boolean isMyDelAny(int i) {

        for (String key : myDelMids) {

            if (key.endsWith(":" + i)) {

                return true;

            }

        }

        return false;

    }

    public static boolean isMyOutDel(int i) {

        return myOutMids.contains(Long.valueOf(i));

    }

    public static void sendRead(long j, int i) {

        TLRPC.Chat chat;

        try {

            MessagesController messagesController = MessagesController.getInstance(UserConfig.selectedAccount);

            if (i <= 0) {

                i = 999999999;

            }

            pendingReadDialogs.put(Long.valueOf(j), Integer.valueOf(i));

            if (j < 0 && (chat = messagesController.getChat(Long.valueOf(-j))) != null && ChatObject.isChannel(chat)) {

                TLRPC.TL_channels_readHistory tL_channels_readHistory = new TLRPC.TL_channels_readHistory();

                tL_channels_readHistory.channel = MessagesController.getInputChannel(chat);

                tL_channels_readHistory.max_id = i;

                sendTL(tL_channels_readHistory);

                return;

            }

            TLObject newReadHistory = newReadHistory();

            if (newReadHistory != null) {

                sf(newReadHistory, "peer", messagesController.getInputPeer(j));

                sf(newReadHistory, "max_id", Integer.valueOf(i));

                sendTL(newReadHistory);

            }

        } catch (Exception e) {

            err("sendRead", e);

        }

    }

    private static TLObject newReadHistory() {

        String[] strArr = {"org.telegram.tgnet.TLRPC$TL_messages_readHistory", "org.telegram.tgnet.tl.TL_messages$TL_messages_readHistory"};

        for (int i = 0; i < 2; i++) {

            try {

                return (TLObject) Class.forName(strArr[i]).newInstance();

            } catch (Exception e) {

            }

        }

        return null;

    }

    public static void refreshCells() {

        refreshCellsForMids(null);

    }

    public static void refreshCellsForMids(final ArrayList<Integer> arrayList) {

        Runnable runnable = new Runnable() {

            @Override // java.lang.Runnable

            public final void run() {

                Main.lambda$refreshCellsForMids$6(arrayList);

            }

        };

        if (H != null) {

            H.postDelayed(runnable, 100L);

        }

    }

    public static void refreshCellsForMidsNow(final ArrayList<Integer> arrayList) {

        Runnable runnable = new Runnable() {

            @Override

            public final void run() {

                Main.lambda$refreshCellsForMids$6(arrayList);

            }

        };

        if (H != null) {

            H.post(runnable);

        } else {

            runnable.run();

        }

    }

    public static void forceUpdateViews(final int currentAccount, final long did, final Collection<Integer> mids) {
        if (mids == null || mids.isEmpty()) {
            return;
        }
        final ArrayList<Integer> midsList = new ArrayList<>(mids);
        AndroidUtilities.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                try {
                    org.telegram.ui.ActionBar.BaseFragment lastFragment = LaunchActivity.getLastFragment();
                    if (!(lastFragment instanceof ChatActivity)) {
                        return;
                    }
                    ChatActivity activity = (ChatActivity) lastFragment;
                    if (activity.getCurrentAccount() != currentAccount || activity.getDialogId() != did) {
                        return;
                    }
                    RecyclerListView chatListView = activity.getChatListView();
                    if (chatListView == null) {
                        return;
                    }
                    androidx.recyclerview.widget.RecyclerView.Adapter<?> adapter = chatListView.getAdapter();
                    if (adapter == null) {
                        return;
                    }
                    HashSet<Integer> midSet = new HashSet<>(midsList);
                    int count = chatListView.getChildCount();
                    for (int i = 0; i < count; i++) {
                        View view = chatListView.getChildAt(i);
                        if (view instanceof ChatMessageCell) {
                            ChatMessageCell cell = (ChatMessageCell) view;
                            MessageObject mo = cell.getMessageObject();
                            if (mo != null && midSet.contains(Integer.valueOf(mo.getId()))) {
                                mo.deleted = true;
                                mo.forceUpdate = true;
                                try {
                                    cell.forceResetMessageObject();
                                } catch (Throwable ignored) {}
                                try {
                                    cell.setMessageObject(mo, cell.getCurrentMessagesGroup(), false, false, false);
                                } catch (Throwable ignored) {}
                                cell.setAlpha(1.0f);
                                adapter.notifyItemChanged(chatListView.getChildAdapterPosition(cell));
                                try {
                                    cell.relayout();
                                } catch (Throwable ignored) {}
                                cell.requestLayout();
                                cell.invalidate();
                            }
                        }
                    }
                } catch (Throwable t) {
                    Main.err("forceUpdateViews", new Exception(t));
                }
            }
        });
    }

    static void lambda$refreshCellsForMids$6(ArrayList arrayList) {

        ViewGroup chatListView;

        HashSet hashSet;

        ChatMessageCell chatMessageCell;

        MessageObject messageObject = null;

        Object obj;

        try {

            ChatActivity lastFragment = (ChatActivity) LaunchActivity.getLastFragment();

            if ((lastFragment instanceof ChatActivity) && (chatListView = lastFragment.getChatListView()) != null) {

                ViewGroup viewGroup = chatListView;

                if (arrayList != null && !arrayList.isEmpty()) {

                    hashSet = new HashSet(arrayList);

                } else {

                    hashSet = null;

                }

                for (int i = 0; i < viewGroup.getChildCount(); i++) {

                    View childView = viewGroup.getChildAt(i);

                    ChatMessageCell childAt = (childView instanceof ChatMessageCell) ? (ChatMessageCell) childView : null;

                    if (childAt != null && (messageObject = childAt.getMessageObject()) != null && (hashSet == null || hashSet.contains(Integer.valueOf(messageObject.getId())))) {
                        try {
                            if (isDeletedMsg(messageObject)) {
                                messageObject.deleted = false;
                            }
                            try {
                                childAt.forceResetMessageObject();
                            } catch (Throwable ignored) {}
                            float alpha = (Main.cfg.b("del_show_opacity", true) && isDeletedMsg(messageObject)) ? HookManager.delOp() : 1.0f;
                            try { childAt.setTransitionAlpha(alpha); } catch (Throwable ignored) {}
                            try { childAt.setAlpha(alpha); } catch (Throwable ignored) {}
                            if (chatListView instanceof RecyclerListView) {
                                RecyclerListView rlv = (RecyclerListView) chatListView;
                                RecyclerView.Adapter<?> adapter = rlv.getAdapter();
                                if (adapter != null) {
                                    int pos = rlv.getChildAdapterPosition(childAt);
                                    if (pos >= 0) {
                                        adapter.notifyItemChanged(pos);
                                    }
                                }
                            }
                            try {
                                childAt.relayout();
                            } catch (Throwable ignored) {}
                            childAt.requestLayout();
                            childAt.invalidate();
                        } catch (Throwable t) {
                            try {
                                childAt.invalidate();
                            } catch (Throwable ignored) {}
                        }
                    }

                }

            }

        } catch (Exception e5) {

        }

    }

    private static void cleanupAds() {

        for (int i = 0; i < 16; i++) {

            try {

                MessagesController messagesController = MessagesController.getInstance(i);

                if (messagesController != null) {

                    sf(messagesController, "promoDialogId", 0L);

                    sf(messagesController, "promoDialogType", 0);

                }

            } catch (Exception e) {

            }

        }

    }

    private static void revertPremium() {

        TLRPC.User currentUser;

        for (int i = 0; i < 4; i++) {

            try {

                UserConfig userConfig = UserConfig.getInstance(i);

                if (userConfig != null && userConfig.isClientActivated() && (currentUser = userConfig.getCurrentUser()) != null) {

                    setPremium(currentUser, false);

                }

            } catch (Exception e) {

                return;

            }

        }

        notifyPremiumChanged();

    }

    static void ui(Runnable runnable) {

        if (H != null) {

            if (Looper.myLooper() == Looper.getMainLooper()) {

                runnable.run();

            } else {

                H.post(runnable);

            }

        }

    }

    static void err(String str, Exception exc) {

        Log.e(TAG, "[" + str + "] " + exc.getMessage(), exc);

    }

    static boolean isBotDialog(long dialogId) {

        if (dialogId <= 0) return false;

        try {

            TLRPC.User user = MessagesController.getInstance(UserConfig.selectedAccount).getUser(Long.valueOf(dialogId));

            return user != null && user.bot;

        } catch (Exception e) {

            return false;

        }

    }

    

    public static void showDeletedList() {

        ui(new Runnable() {

            @Override // java.lang.Runnable

            public final void run() {

                Main.lambda$showDeletedList$14();

            }

        });

    }

    static void lambda$showDeletedList$14() {

        long j;

        try {

            BaseFragment lastFragment = LaunchActivity.getLastFragment();

            if (!(lastFragment instanceof ChatActivity) || db == null) {

                return;

            }

            try {

                Field declaredField = ChatActivity.class.getDeclaredField("dialog_id");

                declaredField.setAccessible(true);

                j = declaredField.getLong(lastFragment);

            } catch (Exception e) {

                j = 0;

            }

            if (j == 0) {

                try {

                    j = ((Long) ChatActivity.class.getMethod("getDialogId", new Class[0]).invoke(lastFragment, new Object[0])).longValue();

                } catch (Exception e2) {

                }

            }

            if (j == 0) {

                return;

            }

            DeletedMessagesFragment deletedMessagesFragment = new DeletedMessagesFragment();

            deletedMessagesFragment.setDialogId(j);

            BaseFragment baseFragment = lastFragment;

            lastFragment.presentFragment(deletedMessagesFragment);

        } catch (Exception e3) {

            err("showDeleted", e3);

        }

    }

    

    public static String encodeData(String str, MessageObject messageObject) {

        int i;

        if (messageObject != null) {

            try {

                if (messageObject.messageOwner != null) {

                    int i2 = 1;

                    if (messageObject.isPhoto()) {

                        i = 1;

                    } else if (messageObject.isVideo()) {

                        i = 2;

                    } else if (messageObject.isVoice()) {

                        i = 3;

                    } else if (messageObject.isRoundVideo()) {

                        i = 4;

                    } else if (messageObject.isSticker() || messageObject.isAnimatedSticker()) {

                        i = 5;

                    } else if (messageObject.isGif()) {

                        i = 6;

                    } else if (messageObject.isMusic()) {

                        i = 7;

                    } else {

                        i = messageObject.getDocument() != null ? 8 : 0;

                    }

                    long j = 0;

                    try {

                        TLRPC.Peer peer = messageObject.messageOwner.from_id;

                        if (peer != null) {

                            if (peer.user_id != 0) {

                                j = peer.user_id;

                            } else if (peer.chat_id != 0) {

                                j = -peer.chat_id;

                            } else if (peer.channel_id != 0) {

                                j = -peer.channel_id;

                            }

                        }

                    } catch (Exception e) {

                    }

                    int i3 = messageObject.messageOwner.date;

                    if (!messageObject.isOutOwner()) {

                        i2 = 0;

                    }

                    return "#MAXMETA#o=" + i2 + "|d=" + i3 + "|s=" + j + "|m=" + i + "\n" + (str == null ? "" : str);

                }

            } catch (Exception e2) {

                return str == null ? "" : str;

            }

        }

        return str == null ? "" : str;

    }

    

    

    public static class Meta {

        int d;

        int m;

        int out;

        long s;

        String text;

        private Meta() {

            this.out = 0;

            this.d = 0;

            this.s = 0L;

            this.m = 0;

            this.text = "";

        }

    }

    

    public static Meta parseData(String str) {

        Meta meta = new Meta();

        if (str == null || !str.startsWith("#MAXMETA#")) {

            if (str == null) {

                str = "";

            }

            meta.text = str;

            return meta;

        }

        int indexOf = str.indexOf(10);

        if (indexOf < 0) {

            meta.text = str;

            return meta;

        }

        meta.text = str.substring(indexOf + 1);

        try {

            for (String str2 : str.substring(9, indexOf).split("\\|")) {

                String[] split = str2.split("=", 2);

                if (split.length == 2) {

                    if ("o".equals(split[0])) {

                        meta.out = Integer.parseInt(split[1]);

                    } else if ("d".equals(split[0])) {

                        meta.d = Integer.parseInt(split[1]);

                    } else if ("s".equals(split[0])) {

                        meta.s = Long.parseLong(split[1]);

                    } else if ("m".equals(split[0])) {

                        meta.m = Integer.parseInt(split[1]);

                    }

                }

            }

        } catch (Exception e) {

        }

        return meta;

    }

    

    public static String userNameOf(TLRPC.User user) {

        if (user == null) {

            return null;

        }

        StringBuilder sb = new StringBuilder();

        if (user.first_name != null) {

            sb.append(user.first_name);

        }

        if (user.last_name != null) {

            if (sb.length() > 0) {

                sb.append(' ');

            }

            sb.append(user.last_name);

        }

        String trim = sb.toString().trim();

        if (trim.isEmpty()) {

            return null;

        }

        return trim;

    }

    private static MessageObject getMessage(int currentAccount, long did, int mid) {

        try {

            MessagesController mc = MessagesController.getInstance(currentAccount);

            // Bug fix: dialogMessagesByIds is keyed ONLY by message id, not by

            // (dialog_id, message_id). Message ids are scoped per-dialog in Telegram,

            // so two completely different chats can have a message with the same id.

            // Returning this without checking mo.getDialogId() == did could resolve to

            // a message from a totally different chat, which showed up as a phantom

            // "deleted message" entry that was never actually in this chat at all.

            Object fromById = mc.dialogMessagesByIds.get(mid);

            if (fromById instanceof MessageObject) {

                MessageObject moById = (MessageObject) fromById;

                if (moById.getDialogId() == did) return moById;

            }

            ArrayList<MessageObject> list = (ArrayList) mc.dialogMessage.get(did);

            if (list != null) {

                for (MessageObject mo : list) {

                    if (mo != null && mo.getId() == mid) return mo;

                }

            }

            TLRPC.Message stored = MessagesStorage.getInstance(currentAccount).getMessage(did, mid);

            if (stored != null) return new MessageObject(currentAccount, stored, false, false);

            org.telegram.ui.ActionBar.BaseFragment last = LaunchActivity.getLastFragment();

            if (last instanceof ChatActivity) {

                ChatActivity ca = (ChatActivity) last;

                if (ca.getCurrentAccount() == currentAccount && ca.getDialogId() == did) {

                    for (MessageObject mo : ca.messages) {

                        if (mo != null && mo.getId() == mid) return mo;

                    }

                }

            }

        } catch (Exception e) {}

        return null;

    }

    public static long getDialogIdFromMessage(TLRPC.Message msg) {

        if (msg == null) return 0L;

        if (msg.dialog_id != 0) return msg.dialog_id;

        try {

            return MessageObject.getDialogId(msg);

        } catch (Throwable t) {

            if (msg.peer_id != null) {

                if (msg.peer_id.user_id != 0) return msg.peer_id.user_id;

                if (msg.peer_id.chat_id != 0) return -msg.peer_id.chat_id;

                if (msg.peer_id.channel_id != 0) return -msg.peer_id.channel_id;

            }

            return 0L;

        }

    }

    public static int resolveAccount(Object source) {

        if (source instanceof BaseController) {

            try {

                AccountInstance ai = ((BaseController) source).getAccountInstance();

                if (ai != null) return ai.getCurrentAccount();

            } catch (Throwable ignore) {}

        }

        return UserConfig.selectedAccount;

    }

    public static void processEditsInUpdates(Object mcObj, Object updatesObj) {

        if (updatesObj == null) return;

        int currentAccount = resolveAccount(mcObj);

        try {

            if (updatesObj instanceof TLRPC.Updates) {

                TLRPC.Updates u = (TLRPC.Updates) updatesObj;

                if (u.updates != null) {

                    for (int i = 0; i < u.updates.size(); i++) {

                        checkAndSaveEdit(u.updates.get(i), currentAccount);

                    }

                }

            } else if (updatesObj instanceof TLRPC.Update) {

                checkAndSaveEdit((TLRPC.Update) updatesObj, currentAccount);

            } else {

                Object singleUpd = gf(updatesObj, "update");

                if (singleUpd instanceof TLRPC.Update) {

                    checkAndSaveEdit((TLRPC.Update) singleUpd, currentAccount);

                }

                Object multUpds = gf(updatesObj, "updates");

                if (multUpds instanceof ArrayList) {

                    ArrayList list = (ArrayList) multUpds;

                    for (int i = 0; i < list.size(); i++) {

                        Object item = list.get(i);

                        if (item instanceof TLRPC.Update) {

                            checkAndSaveEdit((TLRPC.Update) item, currentAccount);

                        }

                    }

                }

            }

        } catch (Throwable t) {

        }

    }

    private static void checkAndSaveEdit(Object upd, int currentAccount) {

        if (upd == null) return;

        String name = upd.getClass().getSimpleName();

        if ("TL_updateEditMessage".equals(name) || "TL_updateEditChannelMessage".equals(name) || "TL_updateEditEphemeralMessage".equals(name)) {

            Object msgObj = gf(upd, "message");

            if (msgObj instanceof TLRPC.Message) {

                TLRPC.Message msg = (TLRPC.Message) msgObj;

                if (!msg.out) {

                    processEditedMessage(msg, currentAccount);

                }

            }

        }

    }

    private static void processEditedMessage(final TLRPC.Message message, final int currentAccount) {

        if (message == null || message.out) return;

        final long did = getDialogIdFromMessage(message);

        if (did == 0) return;

        if (!Main.cfg.b("save_bot_chats", false)) {

            try {

                TLRPC.User user = MessagesController.getInstance(currentAccount).getUser(Long.valueOf(did));

                if (user != null && user.bot) return;

            } catch (Exception ignore) {}

        }

        final int mid = message.id;

        Main.db.postToDbThread(() -> {

            try {

                MessageObject oldObj = getMessage(currentAccount, did, mid);

                if (oldObj != null && !oldObj.isOut() && oldObj.messageOwner != null) {

                    TLRPC.Message oldMsg = oldObj.messageOwner;

                    String oldText = oldMsg.message != null ? oldMsg.message : "";

                    String newText = message.message != null ? message.message : "";

                    boolean textChanged = !oldText.equals(newText);

                    boolean editDateChanged = message.edit_date != 0 && message.edit_date != oldMsg.edit_date;

                    boolean mediaChanged = (oldMsg.media == null && message.media != null) || (oldMsg.media != null && message.media == null);

                    if (textChanged || editDateChanged || mediaChanged) {

                        if (!Main.db.hasEdits(did, mid)) {

                            Main.db.saveOrig(did, mid, oldMsg);

                        }

                        Main.db.saveVer(did, mid, message);

                    }

                }

            } catch (Throwable t) {

            }

        });

    }

    

    public static class DeletedMessagesFragment extends BaseFragment {

        private long did;

        private TLRPC.User currentUser;

        private TLRPC.Chat currentChat;

        private RecyclerListView chatListView;

        private DeletedAdapter adapter;

        private TextView emptyView;

        private SizeNotifierFrameLayout contentView;

        private Theme.ResourcesProvider resourcesProvider;

        private boolean loadingMessages;

        private int reloadGeneration;

        void setDialogId(long d) { did = d; }

        @Override public boolean onFragmentCreate() {

            try {

                MessagesController mc = MessagesController.getInstance(getCurrentAccount());

                if (did > 0) currentUser = mc.getUser(Long.valueOf(did));

                else if (did < 0) currentChat = mc.getChat(Long.valueOf(-did));

            } catch (Exception e) {}

            return super.onFragmentCreate();

        }

        @Override public View createView(Context context) {

            hasOwnBackground = true;

            Theme.createChatResources(context, false);

            resourcesProvider = getResourceProvider();

            actionBar.setAddToContainer(false);

            actionBar.setOccupyStatusBar(!AndroidUtilities.isTablet());

            actionBar.setBackButtonDrawable(new BackDrawable(false));

            actionBar.setAllowOverlayTitle(false);

            ActionBarMenu dmfMenu = actionBar.createMenu();
            ActionBarMenuItem dmfOtherItem = dmfMenu.addItem(10, R.drawable.ic_ab_other);
            dmfOtherItem.addSubItem(11, R.drawable.msg_delete, tr("Очистить все удалёнки","Clear ALL deleted"));

            actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {

                @Override public void onItemClick(int id) {
                    if (id == -1) {
                        finishFragment();
                    } else if (id == 11) {
                        try {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                            builder.setTitle("ExteraMax");
                            builder.setMessage(tr("Полностью очистить ВСЕ сохранённые удалённые сообщения во всех чатах?","Completely clear ALL saved deleted messages across all chats?"));
                            builder.setPositiveButton(tr("Очистить всё","Clear all"), (dialog, which) -> {
                                dialog.dismiss();
                                Main.clearAllDeletedData(() -> {
                                    reloadMessages();
                                });
                            });
                            builder.setNegativeButton(tr("Отмена","Cancel"), (dialog, which) -> dialog.dismiss());
                            showDialog(builder.create());
                        } catch (Throwable ignored) {}
                    }
                }

            });

            actionBar.setTitle(" ");

            contentView = new SizeNotifierFrameLayout(context);

            contentView.setOccupyStatusBar(!AndroidUtilities.isTablet());

            contentView.setBackgroundImage(Theme.getCachedWallpaper(), Theme.isWallpaperMotion());

            int topInset = ActionBar.getCurrentActionBarHeight() + (actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0);

            chatListView = new RecyclerListView(context, resourcesProvider) {

                @Override public boolean drawChild(Canvas canvas, View child, long drawingTime) {

                    ChatMessageCell chatMessageCell;

                    org.telegram.messenger.ImageReceiver imageReceiver;

                    int cellBottom;

                    RecyclerView.ViewHolder prevHolder;

                    boolean result = super.drawChild(canvas, child, drawingTime);

                    if (!(child instanceof ChatMessageCell) || (imageReceiver = (chatMessageCell = (ChatMessageCell) child).getAvatarImage()) == null) {

                        return result;

                    }

                    int top = (int) child.getY();

                    if (chatMessageCell.drawPinnedBottom()) {

                        RecyclerView.ViewHolder holder = getChildViewHolder(child);

                        int position = holder.getAdapterPosition();

                        if (position >= 0) {

                            RecyclerView.ViewHolder nextHolder = findViewHolderForAdapterPosition(position + 1);

                            if (nextHolder != null) {

                                imageReceiver.setVisible(false, false);

                                return result;

                            }

                        }

                    }

                    float tx = chatMessageCell.getSlidingOffsetX() + chatMessageCell.getCheckBoxTranslation();

                    int y = ((int) child.getY()) + chatMessageCell.getLayoutHeight();

                    int maxY = getMeasuredHeight() - getPaddingBottom();

                    if (y > maxY) {

                        y = maxY;

                    }

                    if (chatMessageCell.drawPinnedTop()) {

                        RecyclerView.ViewHolder holder2 = getChildViewHolder(child);

                        int position2 = holder2.getAdapterPosition();

                        if (position2 >= 0) {

                            int tries = 0;

                            while (true) {

                                int tries2 = tries + 1;

                                if (tries < 20 && (prevHolder = findViewHolderForAdapterPosition(position2 - 1)) != null) {

                                    top = prevHolder.itemView.getTop();

                                    ChatMessageCell chatMessageCell2 = (ChatMessageCell) prevHolder.itemView;

                                    if (!(chatMessageCell2 instanceof ChatMessageCell)) {

                                        break;

                                    }

                                    ChatMessageCell prevCell = chatMessageCell2;

                                    if (!prevCell.drawPinnedTop()) {

                                        break;

                                    }

                                    position2--;

                                    tries = tries2;

                                } else {

                                    break;

                                }

                            }

                        }

                    }

                    if (y - AndroidUtilities.dp(48.0f) < top) {

                        y = top + AndroidUtilities.dp(48.0f);

                    }

                    if (!chatMessageCell.drawPinnedBottom() && y > (cellBottom = (int) (chatMessageCell.getY() + chatMessageCell.getMeasuredHeight()))) {

                        y = cellBottom;

                    }

                    canvas.save();

                    if (tx != 0.0f) {

                        canvas.translate(tx, 0.0f);

                    }

                    imageReceiver.setImageY(y - AndroidUtilities.dp(44.0f));

                    imageReceiver.setAlpha(chatMessageCell.shouldDrawAlphaLayer() ? chatMessageCell.getAlpha() : 1.0f);

                    imageReceiver.setVisible(true, false);

                    imageReceiver.draw(canvas);

                    canvas.restore();

                    return result;

                }

            };

            chatListView.setClipChildren(false);

            chatListView.setClipToPadding(false);

            chatListView.setPadding(0, AndroidUtilities.dp(8) + topInset, 0, AndroidUtilities.dp(68));

            chatListView.setLayoutManager(new LinearLayoutManager(context));

            chatListView.setItemAnimator(null);

            adapter = new DeletedAdapter(context, did, getCurrentAccount(), resourcesProvider);

            chatListView.setAdapter(adapter);

            contentView.addView(chatListView, LayoutHelper.createFrame(-1, -1, 0));

            emptyView = new TextView(context);

            emptyView.setText(LocaleController.getString(R.string.NoResult));

            emptyView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText));

            emptyView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);

            emptyView.setGravity(Gravity.CENTER);

            emptyView.setVisibility(View.GONE);

            contentView.addView(emptyView, LayoutHelper.createFrame(-2, -2, Gravity.CENTER));

            contentView.addView(actionBar);

            fragmentView = contentView;

            fragmentView.post(() -> {

                reloadMessages();

                updateHeader(adapter != null ? adapter.getMessageCount() : 0);

            });

            return fragmentView;

        }

        private void updateHeader(int messageCount) {

            if (actionBar == null) return;

            if (currentChat != null) {

                actionBar.setTitle(currentChat.title);

            } else if (currentUser != null) {

                actionBar.setTitle(org.telegram.messenger.ContactsController.formatName(currentUser.first_name, currentUser.last_name));

            } else {

                actionBar.setTitle("Deleted messages");

            }

        }

        private void reloadMessages() {

            if (adapter == null) return;

            loadingMessages = true;

            if (emptyView != null) { emptyView.setText(LocaleController.getString(R.string.Loading)); emptyView.setVisibility(View.VISIBLE); }

            final int gen = ++reloadGeneration;

            adapter.reloadAsync(did, () -> {

                if (gen != reloadGeneration) return;

                loadingMessages = false;

                updateHeader(adapter.getMessageCount());

                if (emptyView != null) { emptyView.setText(LocaleController.getString(R.string.NoResult)); emptyView.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE); }

            });

        }

        private void openProfile(long peerId) {

            Bundle args = new Bundle();

            if (peerId > 0) args.putLong("user_id", peerId);

            else if (peerId < 0) args.putLong("chat_id", -peerId);

            else return;

            presentFragment(new org.telegram.ui.ProfileActivity(args));

        }

        public void didPressUserAvatar(ChatMessageCell cell, TLRPC.User user, float touchX, float touchY, boolean asForward) {

            if (user != null) openProfile(user.id);

        }

        public void didPressChannelAvatar(ChatMessageCell cell, TLRPC.Chat chat, int postId, float touchX, float touchY, boolean asForward) {

            if (chat != null) openProfile(-chat.id);

        }

        public boolean didLongPressUserAvatar(ChatMessageCell cell, TLRPC.User user, float touchX, float touchY) { return false; }

        public boolean didLongPressChannelAvatar(ChatMessageCell cell, TLRPC.Chat chat, int postId, float touchX, float touchY) { return false; }

        public void didPressReplyMessage(ChatMessageCell cell, int id, float x, float y, boolean longpress) {}

        public void didPressHiddenForward(ChatMessageCell cell) {}

        public void didPressImage(ChatMessageCell cell, float x, float y, boolean fullPreview) {}

        public void didPressInstantButton(ChatMessageCell cell, int type) {}

        public void didPressUrl(ChatMessageCell cell, android.text.style.CharacterStyle url, boolean longPress) {}

        public void didPressCodeCopy(ChatMessageCell cell, Object block) {}

        public void didPressWebPage(ChatMessageCell cell, TLRPC.WebPage webpage, String url, boolean safe) {}

        public void didPressViaBot(ChatMessageCell cell, String username) {}

        public void didPressViaBotNotInline(ChatMessageCell cell, long botId) {}

        public void didPressOther(ChatMessageCell cell, float otherX, float otherY) {}

        public void didPressGroupImage(ChatMessageCell cell, org.telegram.messenger.ImageReceiver imageReceiver, TLRPC.MessageExtendedMedia media, float x, float y) {}

        public void didPressSideButton(ChatMessageCell cell) {}

        public void didPressReaction(ChatMessageCell cell, TLRPC.ReactionCount reaction, boolean longpress, float x, float y) {}

        public void didPressVoteButtons(ChatMessageCell cell, ArrayList<TLRPC.PollAnswer> buttons, int showCount, int x, int y) {}

        public void didPressBotButton(ChatMessageCell cell, TL_keyboard.KeyboardButtonProto button) {}

        public void didPressCustomBotButton(ChatMessageCell cell, org.telegram.messenger.BotInlineKeyboard.ButtonCustom button) {}

        public void didLongPressBotButton(ChatMessageCell cell, TL_keyboard.KeyboardButtonProto button) {}

        public void didLongPressCustomBotButton(ChatMessageCell cell, org.telegram.messenger.BotInlineKeyboard.ButtonCustom button) {}

        public void didLongPress(ChatMessageCell cell, float x, float y) {}

        public void didPressCancelSendButton(ChatMessageCell cell) {}

        public void didPressBoostCounter(ChatMessageCell cell) {}

        public void didPressGiveawayChatButton(ChatMessageCell cell, int pressedPos) {}

        public void didPressCommentButton(ChatMessageCell cell) {}

        public void didPressHint(ChatMessageCell cell, int type) {}

        public void didPressTime(ChatMessageCell cell) {}

        public void didPressAdmin(ChatMessageCell cell) {}

        public void didPressTopicButton(ChatMessageCell cell) {}

        public void didPressDialogButton(ChatMessageCell cell) {}

        public void didPressEmojiStatus() {}

        public void didPressAboutRevenueSharingAds() {}

        public void didPressRevealSensitiveContent(ChatMessageCell cell) {}

        public void didPressEffect(ChatMessageCell cell) {}

        public void didPressFactCheckWhat(ChatMessageCell cell, int cx, int cy) {}

        public void didPressFactCheck(ChatMessageCell cell) {}

        public void didPressSummarize(ChatMessageCell cell, boolean byReply) {}

        public void didPressSponsoredClose(ChatMessageCell cell) {}

        public void didPressSponsoredInfo(ChatMessageCell cell, float x, float y) {}

        public void didPressExtendedMediaPreview(ChatMessageCell cell, TL_keyboard.KeyboardButtonProto button) {}

        public void didPressChannelRecommendation(ChatMessageCell cell, TLObject chat, boolean longPress) {}

        public void didPressMoreChannelRecommendations(ChatMessageCell cell) {}

        public void didPressChannelRecommendationsClose(ChatMessageCell cell) {}

        public void didPressUserStatus(ChatMessageCell cell, TLRPC.User user, TLRPC.Document document, String giftSlug) {}

        public void forceUpdate(ChatMessageCell cell, boolean anchorScroll) {}

        public void forceUpdateNoAnimation(ChatMessageCell cell, boolean anchorScroll) {}

        public void videoTimerReached() {}

        public boolean shouldRepeatSticker(MessageObject message) { return true; }

        public void setShouldNotRepeatSticker(MessageObject message) {}

        public boolean canPerformReply() { return false; }

        public boolean canPerformActions() { return true; }

        public boolean canDrawOutboundsContent() { return true; }

        public boolean isReplyOrSelf() { return false; }

        public boolean isAdmin(long uid) { return false; }

        public boolean isOwner(long uid) { return false; }

        public boolean isProgressLoading(ChatMessageCell cell, int type) { return false; }

        public String getProgressLoadingBotButtonUrl(ChatMessageCell cell) { return null; }

        public android.text.style.CharacterStyle getProgressLoadingLink(ChatMessageCell cell) { return null; }

        public String getAdminRank(long uid) { return null; }

        public boolean doNotShowLoadingReply(MessageObject msg) { return true; }

        public boolean shouldDrawAvatarOnlineStatus(ChatMessageCell cell) { return true; }

        public boolean isLandscape() { return false; }

        public void invalidateBlur() {}

        public boolean didPressAnimatedEmoji(ChatMessageCell cell, org.telegram.ui.Components.AnimatedEmojiSpan span) { return false; }

        public boolean shouldShowTopicButton(ChatMessageCell cell) { return false; }

        public boolean shouldShowDialogButton(ChatMessageCell cell) { return false; }

        public boolean drawingVideoPlayerContainer() { return false; }

        public boolean shouldDrawThreadProgress(ChatMessageCell cell, boolean delayed) { return false; }

        public boolean onAccessibilityAction(int action, android.os.Bundle arguments) { return false; }

        public boolean didPressToDoButton(ChatMessageCell cell, TLRPC.TodoItem task, boolean enable) { return false; }

        public boolean didLongPressToDoButton(ChatMessageCell cell, TLRPC.TodoItem task) { return false; }

        public void didStartVideoStream(MessageObject message) {}

        public void needShowPremiumFeatures(String source) {}

        public void needShowPremiumBulletin(int type) {}

        public void needReloadPolls() {}

        public void onDiceFinished() {}

        public void needOpenWebView(MessageObject message, String url, String title, String description, String originalUrl, int w, int h) {}

        public boolean needPlayMessage(ChatMessageCell cell, MessageObject messageObject, boolean muted) { return false; }

        public void didQuickShareStart(ChatMessageCell cell, float x, float y) {}

        public void didQuickShareMove(ChatMessageCell cell, float x, float y) {}

        public void didQuickShareEnd(ChatMessageCell cell, float x, float y) {}

        public boolean isSameGroup(MessageObject first, MessageObject second) {

            return first != null && second != null && first.isOutOwner() == second.isOutOwner() && first.getFromChatId() == second.getFromChatId() && Math.abs(first.messageOwner.date - second.messageOwner.date) <= 300;

        }

    }

    @SuppressWarnings({"rawtypes", "unchecked"})

    private static class DeletedAdapter extends RecyclerView.Adapter {

        private static final int VIEW_TYPE_DATE = 1;

        private static final int VIEW_TYPE_MESSAGE = 0;

        private final Context context;

        private final int currentAccount;

        private final long did;

        private final Theme.ResourcesProvider resourcesProvider;

        private int loadGeneration;

        private final ArrayList<MessageObject> items = new ArrayList<>();

        private final ArrayList<MessageObject> rows = new ArrayList<>();

        DeletedAdapter(Context context, long did, int currentAccount, Theme.ResourcesProvider resourcesProvider) {

            this.context = context;

            this.did = did;

            this.currentAccount = currentAccount;

            this.resourcesProvider = resourcesProvider;

            setHasStableIds(true);

        }

        int getMessageCount() { return items.size(); }

        void reloadAsync(long dialogId, Runnable onDone) {

            final int gen = ++loadGeneration;

            Utilities.globalQueue.postRunnable(() -> {

                final ArrayList<MessageObject> loadedItems = new ArrayList<>();

                List<VaultDb.DeletedItem> stored = db.getRemovedListPage(dialogId, 0, 500);

                int nowSec = (int) (System.currentTimeMillis() / 1000);

                int minValidDate = 1356998400; // 2013-01-01

                // Collect mids of items that no longer exist in storage for batch cleanup

                ArrayList<Integer> toPurge = new ArrayList<>();

                if (stored != null) {

                    for (VaultDb.DeletedItem item : stored) {

                        Meta meta = parseData(item.text);

                        String displayText = meta.text != null && !meta.text.isEmpty() ? meta.text : "[deleted]";

                        if (displayText.equals("\u2709") || displayText.isEmpty()) continue;

                        int itemDate = meta.d > minValidDate ? meta.d : (item.ts > 1000000000000L ? (int) (item.ts / 1000) : (int) item.ts);

                        if (itemDate < minValidDate || itemDate > nowSec + 86400) continue;

                        // Bug fix: this used to purge the vault entry whenever

                        // MessagesStorage.getMessage() came back null. But that is the

                        // EXPECTED, NORMAL state for a message that was actually deleted -

                        // Telegram's own local DB drops the row as soon as the delete syncs

                        // down, which happens almost immediately. Purging here means our own

                        // saved copy of a just-deleted message got wiped out again within

                        // seconds of being saved, defeating the entire point of "save

                        // deleted messages". The vault's own stored text/meta (displayText,

                        // already validated above) is the source of truth for whether this

                        // entry is worth keeping, not whether Telegram still has a live copy.

                        MessageObject mo = getMessage(currentAccount, dialogId, item.mid);

                        if (mo != null) {

                            loadedItems.add(mo);

                        } else if (displayText != null && !displayText.isEmpty()) {

                            try {

                                TLRPC.TL_message fallback = new TLRPC.TL_message();

                                fallback.id = item.mid;

                                fallback.message = displayText;

                                fallback.date = itemDate;

                                fallback.flags = 2;

                                fallback.dialog_id = dialogId;

                                long senderId = meta.s != 0 ? meta.s : (dialogId > 0 ? dialogId : 0);

                                boolean isOut = meta.out == 1;

                                TLRPC.Peer peer;

                                if (isOut) {

                                    long myId = UserConfig.getInstance(currentAccount).getClientUserId();

                                    peer = new TLRPC.TL_peerUser();

                                    peer.user_id = myId;

                                    fallback.from_id = new TLRPC.TL_peerUser();

                                    fallback.from_id.user_id = myId;

                                    fallback.out = true;

                                } else if (senderId != 0) {

                                    if (senderId > 0) {

                                        peer = new TLRPC.TL_peerUser();

                                        peer.user_id = senderId;

                                        fallback.from_id = new TLRPC.TL_peerUser();

                                        fallback.from_id.user_id = senderId;

                                    } else {

                                        peer = new TLRPC.TL_peerChat();

                                        peer.chat_id = -senderId;

                                        fallback.from_id = new TLRPC.TL_peerChat();

                                        fallback.from_id.chat_id = -senderId;

                                    }

                                } else {

                                    peer = new TLRPC.TL_peerUser();

                                    peer.user_id = dialogId;

                                    fallback.from_id = new TLRPC.TL_peerUser();

                                    fallback.from_id.user_id = dialogId;

                                }

                                fallback.peer_id = peer;

                                MessageObject fallbackObj = new MessageObject(currentAccount, fallback, false, false);

                                fallbackObj.messageOwner.dialog_id = dialogId;

                                loadedItems.add(fallbackObj);

                            } catch (Exception e) { err("delList", e); }

}

                        }

                    }

                    // Purge messages that no longer exist in storage

                    if (!toPurge.isEmpty()) {

                        db.purge(dialogId, toPurge);

                    }

                    loadedItems.sort((a, b) -> Integer.compare(a.messageOwner.date, b.messageOwner.date));

                final ArrayList<MessageObject> loadedRows = buildRows(loadedItems);

                AndroidUtilities.runOnUIThread(() -> {

                    if (gen != loadGeneration) return;

                    items.clear();

                    items.addAll(loadedItems);

                    rows.clear();

                    rows.addAll(loadedRows);

                    notifyDataSetChanged();

                    if (onDone != null) onDone.run();

                });

            });

        }

        private ArrayList<MessageObject> buildRows(ArrayList<MessageObject> source) {

            ArrayList<MessageObject> result = new ArrayList<>();

            String prevKey = null;

            for (MessageObject item : source) {

                String key = getDateKey(item);

                if (!key.equals(prevKey)) {

                    result.add(createDateObject(item.messageOwner.date));

                    prevKey = key;

                }

                result.add(item);

            }

            return result;

        }

        private String getDateKey(MessageObject mo) {

            java.util.Calendar cal = new java.util.GregorianCalendar();

            cal.setTimeInMillis((long) mo.messageOwner.date * 1000);

            return cal.get(java.util.Calendar.YEAR) + "_" + cal.get(java.util.Calendar.DAY_OF_YEAR);

        }

        private MessageObject createDateObject(int date) {

            TLRPC.TL_message dateMsg = new TLRPC.TL_message();

            dateMsg.message = LocaleController.formatDateChat(date);

            dateMsg.id = 0;

            dateMsg.date = date;

            MessageObject dateObject = new MessageObject(currentAccount, dateMsg, false, false);

            dateObject.type = 10;

            dateObject.contentType = 1;

            dateObject.isDateObject = true;

            return dateObject;

        }

        @Override public long getItemId(int position) {

            MessageObject row = rows.get(position);

            return row.isDateObject ? -1000000000L - row.messageOwner.date : row.getId();

        }

        @Override public int getItemCount() { return rows.size(); }

        @Override public int getItemViewType(int position) { return rows.get(position).isDateObject ? VIEW_TYPE_DATE : VIEW_TYPE_MESSAGE; }

        @Override

        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view;

            if (viewType == VIEW_TYPE_DATE) {

                view = new org.telegram.ui.Cells.ChatActionCell(context, false, resourcesProvider);

            } else {

                ChatMessageCell cell = new ChatMessageCell(context, currentAccount);

                cell.setAllowAssistant(false);

                view = cell;

            }

            view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            return new RecyclerListView.Holder(view);

        }

        @Override

        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            MessageObject current = rows.get(position);

            if (current.isDateObject) {

                org.telegram.ui.Cells.ChatActionCell actionCell = (org.telegram.ui.Cells.ChatActionCell) holder.itemView;

                actionCell.setMessageObject(current);

                actionCell.setAlpha(1.0f);

                return;

            }

            boolean pinnedBottom = false;

            boolean pinnedTop = false;

            if (position < rows.size() - 1) {

                MessageObject next = rows.get(position + 1);

                if (next.isDateObject) next = null;

                pinnedBottom = isSameGroup(current, next);

            }

            if (position > 0) {

                MessageObject prev = rows.get(position - 1);

                if (prev.isDateObject) prev = null;

                pinnedTop = isSameGroup(current, prev);

            }

            ((ChatMessageCell) holder.itemView).setMessageObject(current, null, pinnedBottom, pinnedTop, false);

            HookManager.maybeStamp((ChatMessageCell) holder.itemView, current);

        }

        private boolean isSameGroup(MessageObject first, MessageObject second) {

            return first != null && second != null && first.isOutOwner() == second.isOutOwner() && first.getFromChatId() == second.getFromChatId() && Math.abs(first.messageOwner.date - second.messageOwner.date) <= 300;

        }

    }

    public static class MessageHistoryFragment extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {

        private long did;

        private int mid;

        private TLRPC.User currentUser;

        private TLRPC.Chat currentChat;

        private MessageObject currentMessageObject;

        private ChatAvatarContainer avatarContainer;

        private RecyclerListView chatListView;

        private HistoryAdapter adapter;

        private TextView emptyView;

        private SizeNotifierFrameLayout contentView;

        private Theme.ResourcesProvider resourcesProvider;

        private boolean loadingMessages;

        private int reloadGeneration;

        public static MessageHistoryFragment newInstance(long did, int mid) {

            MessageHistoryFragment f = new MessageHistoryFragment();

            Bundle b = new Bundle();

            b.putLong("did", did);

            b.putInt("mid", mid);

            f.arguments = b;

            return f;

        }

        @Override public boolean onFragmentCreate() {

            Bundle args = getArguments();

            if (args != null) {

                did = args.getLong("did");

                mid = args.getInt("mid");

            }

            try {

                currentMessageObject = Main.getMessage(getCurrentAccount(), did, mid);

                MessagesController mc = MessagesController.getInstance(getCurrentAccount());

                if (did > 0) currentUser = mc.getUser(Long.valueOf(did));

                else if (did < 0) currentChat = mc.getChat(Long.valueOf(-did));

            } catch (Exception ignore) {}

            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiLoaded);

            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetNewWallpapper);

            return super.onFragmentCreate();

        }

        @Override public void onFragmentDestroy() {

            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiLoaded);

            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetNewWallpapper);

            super.onFragmentDestroy();

        }

        @Override public void didReceivedNotification(int id, int account, Object... args) {

            if (id == NotificationCenter.emojiLoaded) {

                if (chatListView != null) chatListView.invalidateViews();

            } else if (id == NotificationCenter.didSetNewWallpapper) {

                if (contentView != null) contentView.setBackgroundImage(Theme.getCachedWallpaper(), Theme.isWallpaperMotion());

            }

        }

        @Override public View createView(Context context) {

            hasOwnBackground = true;

            Theme.createChatResources(context, false);

            resourcesProvider = getResourceProvider();

            actionBar.setAddToContainer(false);

            actionBar.setOccupyStatusBar(!AndroidUtilities.isTablet());

            actionBar.setBackButtonDrawable(new BackDrawable(false));

            actionBar.setAllowOverlayTitle(false);

            actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {

                @Override public void onItemClick(int id) { if (id == -1) finishFragment(); }

            });

            actionBar.setTitle("");

            avatarContainer = new ChatAvatarContainer(context, (BaseFragment) null, false, resourcesProvider);

            avatarContainer.setOccupyStatusBar(!AndroidUtilities.isTablet());

            avatarContainer.setEnabled(false);

            actionBar.addView(avatarContainer, 0, LayoutHelper.createFrame(-2, -1.0f, 51, 56.0f, 0.0f, 40.0f, 0.0f));

            updateHeader(0);

            contentView = new SizeNotifierFrameLayout(context);

            contentView.setOccupyStatusBar(!AndroidUtilities.isTablet());

            contentView.setBackgroundImage(Theme.getCachedWallpaper(), Theme.isWallpaperMotion());

            int topInset = ActionBar.getCurrentActionBarHeight() + (actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0);

            chatListView = new RecyclerListView(context, resourcesProvider) {

                @Override public boolean drawChild(Canvas canvas, View child, long drawingTime) {

                    ChatMessageCell chatMessageCell;

                    org.telegram.messenger.ImageReceiver imageReceiver;

                    int cellBottom;

                    RecyclerView.ViewHolder prevHolder;

                    boolean result = super.drawChild(canvas, child, drawingTime);

                    if (!(child instanceof ChatMessageCell) || (imageReceiver = (chatMessageCell = (ChatMessageCell) child).getAvatarImage()) == null) return result;

                    int top = (int) child.getY();

                    if (chatMessageCell.drawPinnedBottom()) {

                        RecyclerView.ViewHolder holder = getChildViewHolder(child);

                        int position = holder != null ? holder.getAdapterPosition() : -1;

                        if (position >= 0) {

                            RecyclerView.ViewHolder nextHolder = findViewHolderForAdapterPosition(position + 1);

                            if (nextHolder != null) {

                                imageReceiver.setVisible(false, false);

                                return result;

                            }

                        }

                    }

                    float tx = chatMessageCell.getSlidingOffsetX() + chatMessageCell.getCheckBoxTranslation();

                    int y = ((int) child.getY()) + chatMessageCell.getLayoutHeight();

                    int maxY = getMeasuredHeight() - getPaddingBottom();

                    if (y > maxY) y = maxY;

                    if (chatMessageCell.drawPinnedTop()) {

                        RecyclerView.ViewHolder holder2 = getChildViewHolder(child);

                        int position2 = holder2 != null ? holder2.getAdapterPosition() : -1;

                        if (position2 >= 0) {

                            int tries = 0;

                            while (true) {

                                int tries2 = tries + 1;

                                if (tries < 20 && (prevHolder = findViewHolderForAdapterPosition(position2 - 1)) != null) {

                                    top = prevHolder.itemView.getTop();

                                    ChatMessageCell chatMessageCell2 = (ChatMessageCell) prevHolder.itemView;

                                    if (!(chatMessageCell2 instanceof ChatMessageCell)) break;

                                    ChatMessageCell prevCell = chatMessageCell2;

                                    if (!prevCell.drawPinnedTop()) break;

                                    position2--;

                                    tries = tries2;

                                } else break;

                            }

                        }

                    }

                    if (y - AndroidUtilities.dp(48.0f) < top) y = top + AndroidUtilities.dp(48.0f);

                    if (!chatMessageCell.drawPinnedBottom() && y > (cellBottom = (int) (chatMessageCell.getY() + chatMessageCell.getMeasuredHeight()))) {

                        y = cellBottom;

                    }

                    canvas.save();

                    if (tx != 0.0f) canvas.translate(tx, 0.0f);

                    imageReceiver.setImageY(y - AndroidUtilities.dp(44.0f));

                    imageReceiver.setAlpha(chatMessageCell.shouldDrawAlphaLayer() ? chatMessageCell.getAlpha() : 1.0f);

                    imageReceiver.setVisible(true, false);

                    imageReceiver.draw(canvas);

                    canvas.restore();

                    return result;

                }

            };

            chatListView.setClipChildren(false);

            chatListView.setClipToPadding(false);

            chatListView.setPadding(0, AndroidUtilities.dp(8) + topInset, 0, AndroidUtilities.dp(16));

            chatListView.setLayoutManager(new LinearLayoutManager(context));

            chatListView.setItemAnimator(null);

            chatListView.setOnItemLongClickListener((view, position, x, y) -> showMessageMenu(view));

            adapter = new HistoryAdapter(context, currentChat, currentUser, resourcesProvider, getCurrentAccount());

            chatListView.setAdapter(adapter);

            contentView.addView(chatListView, LayoutHelper.createFrame(-1, -1.0f));

            emptyView = new TextView(context);

            emptyView.setText(LocaleController.getString(R.string.NoResult));

            emptyView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText));

            emptyView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);

            emptyView.setGravity(Gravity.CENTER);

            emptyView.setVisibility(View.GONE);

            contentView.addView(emptyView, LayoutHelper.createFrame(-2, -2, Gravity.CENTER));

            contentView.addView(actionBar);

            fragmentView = contentView;

            fragmentView.post(() -> reloadMessages());

            return fragmentView;

        }

        private void updateHeader(int messageCount) {

            if (avatarContainer == null) return;

            if (currentChat != null) {

                avatarContainer.setTitle(currentChat.title);

                avatarContainer.setChatAvatar(currentChat);

            } else if (currentUser != null) {

                avatarContainer.setTitle(ContactsController.formatName(currentUser.first_name, currentUser.last_name));

                avatarContainer.setUserAvatar(currentUser);

            } else {

                avatarContainer.setTitle(Main.tr("История изменений", "Edit history"));

            }

            if (loadingMessages) {

                avatarContainer.setSubtitle(LocaleController.getString(R.string.Loading));

            } else {

                String sub = Main.tr("История изменений", "Edit history");

                if (messageCount > 0) sub = sub + " (" + messageCount + ")";

                avatarContainer.setSubtitle(sub);

            }

        }

        private void reloadMessages() {

            if (adapter == null) return;

            loadingMessages = true;

            updateHeader(adapter.getMessageCount());

            if (emptyView != null) {

                emptyView.setText(LocaleController.getString(R.string.Loading));

                emptyView.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);

            }

            final int gen = ++reloadGeneration;

            adapter.reloadAsync(did, mid, () -> {

                if (gen != reloadGeneration) return;

                loadingMessages = false;

                updateHeader(adapter.getMessageCount());

                if (emptyView != null) {

                    emptyView.setText(LocaleController.getString(R.string.NoResult));

                    emptyView.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);

                }

            });

        }

        private boolean showMessageMenu(View view) {

            if (view instanceof ChatMessageCell) {

                ChatMessageCell cell = (ChatMessageCell) view;

                final MessageObject mo = cell.getMessageObject();

                if (mo == null) return false;

                final CharSequence text = mo.messageText != null ? mo.messageText : (mo.messageOwner != null ? mo.messageOwner.message : null);

                ArrayList<String> items = new ArrayList<>();

                final ArrayList<Runnable> actions = new ArrayList<>();

                if (text != null && text.length() > 0) {

                    items.add(LocaleController.getString(R.string.Copy));

                    actions.add(() -> {

                        AndroidUtilities.addToClipboard(text);

                        try {

                            BulletinFactory.of(MessageHistoryFragment.this).createCopyBulletin(LocaleController.getString(R.string.TextCopied)).show();

                        } catch (Throwable t) {

                            Toast.makeText(getParentActivity(), LocaleController.getString(R.string.TextCopied), Toast.LENGTH_SHORT).show();

                        }

                    });

                }

                if (!items.isEmpty()) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());

                    builder.setItems(items.toArray(new CharSequence[0]), (dialog, which) -> {

                        if (which >= 0 && which < actions.size()) {

                            actions.get(which).run();

                        }

                    });

                    showDialog(builder.create());

                    return true;

                }

            }

            return false;

        }

        private void openProfile(long peerId) {

            Bundle args = new Bundle();

            if (peerId > 0) args.putLong("user_id", peerId);

            else if (peerId < 0) args.putLong("chat_id", -peerId);

            else return;

            presentFragment(new ProfileActivity(args));

        }

    }

    @SuppressWarnings({"rawtypes", "unchecked"})

    private static class HistoryAdapter extends RecyclerView.Adapter {

        private static final int VIEW_TYPE_DATE = 1;

        private static final int VIEW_TYPE_MESSAGE = 0;

        private final Context context;

        private final int currentAccount;

        private final TLRPC.Chat currentChat;

        private final TLRPC.User currentUser;

        private final Theme.ResourcesProvider resourcesProvider;

        private int loadGeneration;

        private final ArrayList<MessageObject> items = new ArrayList<>();

        private final ArrayList<MessageObject> rows = new ArrayList<>();

        HistoryAdapter(Context context, TLRPC.Chat currentChat, TLRPC.User currentUser, Theme.ResourcesProvider resourcesProvider, int currentAccount) {

            this.context = context;

            this.currentChat = currentChat;

            this.currentUser = currentUser;

            this.resourcesProvider = resourcesProvider;

            this.currentAccount = currentAccount;

            setHasStableIds(false);

        }

        int getMessageCount() { return items.size(); }

        private ArrayList<TLRPC.Message> loadVersionsFromPyVault(long did, int mid, TLRPC.Message template) {

            ArrayList<TLRPC.Message> list = new ArrayList<>();

            try {

                File pyDb = new File(new File(ApplicationLoader.applicationContext.getFilesDir(), "exteramax"), "vault.db");

                if (pyDb.exists()) {

                    SQLiteDatabase db = SQLiteDatabase.openDatabase(pyDb.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);

                    Cursor c = db.rawQuery("SELECT ver, timestamp, data, text FROM revisions WHERE dialog_id=? AND message_id=? ORDER BY ver ASC",

                            new String[]{String.valueOf(did), String.valueOf(mid)});

                    while (c.moveToNext()) {

                        int ver = c.getInt(0);

                        long ts = c.getLong(1);

                        String dataB64 = c.getString(2);

                        String text = c.getString(3);

                        TLRPC.Message msg = null;

                        if (dataB64 != null && !dataB64.isEmpty()) {

                            try {

                                byte[] bytes = Base64.decode(dataB64, Base64.DEFAULT);

                                msg = VaultDb.deser(bytes);

                            } catch (Exception ignore) {}

                        }

                        if (msg == null) {

                            TLRPC.TL_message fallback = new TLRPC.TL_message();

                            fallback.id = mid;

                            fallback.dialog_id = did;

                            fallback.message = text != null ? text : "";

                            fallback.date = ts > 0 ? (int) ts : (int) (System.currentTimeMillis() / 1000);

                            if (template != null) {

                                fallback.from_id = template.from_id;

                                fallback.peer_id = template.peer_id;

                                fallback.out = template.out;

                                fallback.flags = template.flags;

                                fallback.media = template.media;

                            }

                            msg = fallback;

                        } else {

                            if (ts > 0 && msg.edit_date == 0 && ver > 0) {

                                msg.edit_date = (int) ts;

                            }

                        }

                        list.add(msg);

                    }

                    c.close();

                    db.close();

                }

            } catch (Throwable t) {

            }

            return list;

        }

        void reloadAsync(final long did, final int mid, final Runnable onDone) {

            final int gen = ++loadGeneration;

            Utilities.globalQueue.postRunnable(() -> {

                MessageObject liveObj = Main.getMessage(currentAccount, did, mid);

                TLRPC.Message template = liveObj != null ? liveObj.messageOwner : null;

                ArrayList<TLRPC.Message> versions = Main.db.listVersionsOfEditedMessage(did, mid);

                ArrayList<TLRPC.Message> pyVersions = loadVersionsFromPyVault(did, mid, template);

                if (!pyVersions.isEmpty()) {

                    for (TLRPC.Message pv : pyVersions) {

                        boolean exists = false;

                        for (TLRPC.Message v : versions) {

                            String vt = v.message != null ? v.message : "";

                            String pvt = pv.message != null ? pv.message : "";

                            if (vt.equals(pvt)) { exists = true; break; }

                        }

                        if (!exists) {

                            versions.add(pv);

                        }

                    }

                }

                final ArrayList<MessageObject> loadedItems = new ArrayList<>();

                for (TLRPC.Message v : versions) {

                    if (v.edit_date != 0) v.date = v.edit_date;

                    loadedItems.add(new MessageObject(currentAccount, v, false, false));

                }

                if (liveObj != null && liveObj.messageOwner != null) {

                    TLRPC.Message liveMsg = liveObj.messageOwner;

                    boolean alreadyIn = false;

                    for (TLRPC.Message v : versions) {

                        String vt = v.message != null ? v.message : "";

                        String lt = liveMsg.message != null ? liveMsg.message : "";

                        if (vt.equals(lt)) {

                            alreadyIn = true;

                            break;

                        }

                    }

                    if (!alreadyIn) {

                        if (liveMsg.edit_date != 0) liveMsg.date = liveMsg.edit_date;

                        loadedItems.add(new MessageObject(currentAccount, liveMsg, false, false));

                    }

                }

                loadedItems.sort((a, b) -> Integer.compare(a.messageOwner.date, b.messageOwner.date));

                // Deduplicate identical adjacent versions

                ArrayList<MessageObject> deduped = new ArrayList<>();

                for (MessageObject mo : loadedItems) {

                    if (deduped.isEmpty()) {

                        deduped.add(mo);

                    } else {

                        MessageObject last = deduped.get(deduped.size() - 1);

                        String lastText = last.messageOwner != null && last.messageOwner.message != null ? last.messageOwner.message : "";

                        String curText = mo.messageOwner != null && mo.messageOwner.message != null ? mo.messageOwner.message : "";

                        boolean lastHasMedia = last.messageOwner != null && last.messageOwner.media != null;

                        boolean curHasMedia = mo.messageOwner != null && mo.messageOwner.media != null;

                        if (!lastText.equals(curText) || lastHasMedia != curHasMedia) {

                            deduped.add(mo);

                        }

                    }

                }

                final ArrayList<MessageObject> loadedRows = buildRows(deduped);

                final ArrayList<MessageObject> finalItems = new ArrayList<>(deduped);

                AndroidUtilities.runOnUIThread(() -> {

                    if (gen != loadGeneration) return;

                    items.clear();

                    items.addAll(finalItems);

                    rows.clear();

                    rows.addAll(loadedRows);

                    notifyDataSetChanged();

                    if (onDone != null) onDone.run();

                });

            });

        }

        private ArrayList<MessageObject> buildRows(ArrayList<MessageObject> source) {

            ArrayList<MessageObject> result = new ArrayList<>();

            int count = source.size();

            SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM, HH:mm", Locale.getDefault());

            for (int i = 0; i < count; i++) {

                MessageObject item = source.get(i);

                int date = item.messageOwner != null ? item.messageOwner.date : 0;

                String timeStr = date > 0 ? dateFormat.format(new Date(((long) date) * 1000)) : "";

                String label;

                if (count == 1) {

                    label = Main.tr("Текущее сообщение", "Current message");

                } else if (i == 0) {

                    label = Main.tr("Оригинал", "Original");

                } else if (i == count - 1) {

                    label = Main.tr("Текущая версия", "Current version");

                } else {

                    label = Main.tr("Изменение ", "Edit ") + i;

                }

                if (!timeStr.isEmpty()) {

                    label = label + " · " + timeStr;

                }

                result.add(createHeaderObject(label, date));

                result.add(item);

            }

            return result;

        }

        private MessageObject createHeaderObject(String title, int date) {

            TLRPC.TL_message dateMsg = new TLRPC.TL_message();

            dateMsg.message = title;

            dateMsg.id = 0;

            dateMsg.date = date;

            MessageObject dateObject = new MessageObject(currentAccount, dateMsg, false, false);

            dateObject.type = 10;

            dateObject.contentType = 1;

            dateObject.isDateObject = true;

            return dateObject;

        }

        @Override public long getItemId(int position) {

            return position;

        }

        @Override public int getItemCount() { return rows.size(); }

        @Override public int getItemViewType(int position) {

            return rows.get(position).isDateObject ? VIEW_TYPE_DATE : VIEW_TYPE_MESSAGE;

        }

        @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view;

            if (viewType == VIEW_TYPE_DATE) {

                view = new org.telegram.ui.Cells.ChatActionCell(context, false, resourcesProvider);

            } else {

                ChatMessageCell cell = new ChatMessageCell(context, currentAccount);

                cell.setAllowAssistant(false);

                view = cell;

            }

            view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            return new RecyclerListView.Holder(view);

        }

        @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            MessageObject current = rows.get(position);

            if (current.isDateObject) {

                org.telegram.ui.Cells.ChatActionCell actionCell = (org.telegram.ui.Cells.ChatActionCell) holder.itemView;

                actionCell.setMessageObject(current);

                actionCell.setAlpha(1.0f);

                return;

            }

            ((ChatMessageCell) holder.itemView).setMessageObject(current, null, false, false, false);

        }

    }

    public static String findAttachFile(long j, int i) {

        File attachDir = null;

        try {

            attachDir = getAttachDir();

        } catch (Exception e) {

        }

        if (attachDir != null && attachDir.isDirectory()) {

            String str = j + "_" + i + "_";

            File[] listFiles = attachDir.listFiles();

            if (listFiles == null) {

                return null;

            }

            for (File file : listFiles) {

                if (file.getName().startsWith(str) && file.length() > 0) {

                    return file.getAbsolutePath();

                }

            }

            return null;

        }

        return null;

    }

    private static File getAttachDir() {

        String string = "";

        try {

            if (cfg != null && (string = cfg.prefs().getString("custom_attach_dir", "")) != null && !string.isEmpty()) {

                File file = new File(string);

                if (file.isDirectory() || file.mkdirs()) {

                    ensureNomedia(file);

                    return file;

                }

            }

        } catch (Exception e) {

        }

        try {

            File file2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "ExteraMax");

            if (!file2.exists()) {

                file2.mkdirs();

            }

            if (file2.isDirectory()) {

                ensureNomedia(file2);

                return file2;

            }

        } catch (Exception e2) {

        }

        try {

            File file3 = new File(ApplicationLoader.applicationContext.getFilesDir(), "exteramax/attachments");

            if (!file3.exists()) {

                file3.mkdirs();

            }

            ensureNomedia(file3);

            return file3;

        } catch (Exception e3) {

            return null;

        }

    }

    private static void ensureNomedia(File file) {

        if (file == null) {

            return;

        }

        try {

            if (!file.exists()) {

                file.mkdirs();

            }

            File file2 = new File(file, ".nomedia");

            if (!file2.exists() && !file2.createNewFile()) {

                try {

                    new FileOutputStream(file2).close();

                } catch (Exception e) {

                    err("nomedia-fos", e);

                }

            }

        } catch (Exception e2) {

            err("nomedia", e2);

        }

    }

    public static void pyEnsureNomedia(String str) {

        if (str == null || str.isEmpty()) {

            return;

        }

        try {

            File file = new File(str);

            if (file.isDirectory()) {

                ensureNomedia(file);

            }

        } catch (Exception e) {

        }

    }

    private static void saveAttachments(long j, ArrayList<Integer> arrayList) {

        saveAttachments(j, arrayList, null);

    }

    

    public static void saveAttachments(final long j, ArrayList<Integer> arrayList, Map<Long, MessageObject> map) {

        if (arrayList == null || arrayList.isEmpty() || cfg == null || !cfg.b("save_attachments", false)) {

            return;

        }

        if (j > 0 && !cfg.b("save_attach_private", true)) return;

        if (j < 0) {

            try {

                long cid = -j;

                TLRPC.Chat ch = MessagesController.getInstance(UserConfig.selectedAccount).getChat(Long.valueOf(cid));

                if (ch != null) {

                    if ((ch.megagroup || ch.gigagroup) && !cfg.b("save_attach_groups", true)) return;

                    if (ch.broadcast && !cfg.b("save_attach_channels", true)) return;

                }

            } catch (Exception e) {}

        }

        final ArrayList arrayList2 = new ArrayList(arrayList);

        final HashMap hashMap = map != null ? new HashMap(map) : null;

        ui(new Runnable() {

            @Override // java.lang.Runnable

            public final void run() {

                Main.lambda$saveAttachments$15(arrayList2, hashMap, j);

            }

        });

    }

    static void lambda$saveAttachments$15(ArrayList arrayList, Map map, long j) {

        MessageObject messageObject = null;

        try {

            Iterator it = arrayList.iterator();

            while (it.hasNext()) {

                int intValue = ((Integer) it.next()).intValue();

                if (map != null) {

                    try {

                        messageObject = (MessageObject) map.get(Long.valueOf(intValue));

                    } catch (Exception e) {

                    }

                } else {

                    messageObject = null;

                }

                if (messageObject == null) {

                    messageObject = findMessageInChat(j, intValue);

                }

                if (messageObject == null) {

                    messageObject = findMessageInController(intValue);

                }

                if (messageObject != null && messageObject.messageOwner != null) {

                    saveMediaFile(messageObject.messageOwner, j, intValue, messageObject);

                }

            }

        } catch (Exception e2) {

        }

    }

    private static void saveMediaFile(final TLRPC.Message message, final long j, final int i, final MessageObject messageObject) {

        if (message == null || message.media == null || cfg == null || !cfg.b("save_attachments", false)) {

            return;

        }

        if (j > 0 && !cfg.b("save_attach_private", true)) return;

        if (j < 0) {

            try {

                long cid = -j;

                TLRPC.Chat ch = MessagesController.getInstance(UserConfig.selectedAccount).getChat(Long.valueOf(cid));

                if (ch != null) {

                    if ((ch.megagroup || ch.gigagroup) && !cfg.b("save_attach_groups", true)) return;

                    if (ch.broadcast && !cfg.b("save_attach_channels", true)) return;

                }

            } catch (Exception e) {}

        }

        new Thread(new Runnable() {

            @Override // java.lang.Runnable

            public final void run() {

                Main.lambda$saveMediaFile$16(message, j, i, messageObject);

            }

        }).start();

    }

    static void lambda$saveMediaFile$16(TLRPC.Message message, long j, int i, MessageObject messageObject) {

        try {

            File attachDir = getAttachDir();

            if (attachDir == null) {

                return;

            }

            int i2 = UserConfig.selectedAccount;

            FileLoader fileLoader = FileLoader.getInstance(i2);

            File findLocalFile = findLocalFile(message, fileLoader);

            if (findLocalFile != null && findLocalFile.exists()) {

                // File already cached locally - copy synchronously and mark as saved

                File file = new File(attachDir, j + "_" + i + "_" + findLocalFile.getName());

                if (!file.exists()) {

                    copyFile(findLocalFile, file);

                }

                // Mark attachment as saved in vault

                if (Main.db != null) {

                    Main.db.markAttachmentSaved(j, i);

                }

                return;

            }

            

            // File not cached - start download with polling fallback

            // Prioritize synchronous copy for already-cached files (handled above).

            // For missing files, start download and poll with increasing intervals.

            startDownload(message, messageObject, i2, fileLoader);

            

            // Use polling with progressive delays (3s, 8s, 15s, then 30s final check)

            // This is more reliable than NotificationCenter listeners which have

            // different constant names across Telegram versions.

            int[] delays = {3000, 8000, 15000, 30000};

            for (int delayIdx = 0; delayIdx < delays.length; delayIdx++) {

                try {

                    Thread.sleep(delays[delayIdx]);

                    File foundFile = findLocalFile(message, fileLoader);

                    if (foundFile != null && foundFile.exists()) {

                        File targetFile = new File(attachDir, j + "_" + i + "_" + foundFile.getName());

                        if (!targetFile.exists()) {

                            copyFile(foundFile, targetFile);

                        }

                        if (Main.db != null) {

                            Main.db.markAttachmentSaved(j, i);

                        }

                        return;

                    }

                    // On the last delay, if file still not found, mark as missing

                    if (delayIdx == delays.length - 1 && Main.db != null) {

                        Main.db.markAttachmentMissing(j, i);

                    }

                } catch (InterruptedException e) {

                    if (Main.db != null) {

                        Main.db.markAttachmentMissing(j, i);

                    }

                    return;

                }

            }

        } catch (Exception e2) {

            if (Main.db != null) {

                Main.db.markAttachmentMissing(j, i);

            }

        }

    }

    private static File findLocalFile(TLRPC.Message message, FileLoader fileLoader) {

        TLRPC.PhotoSize closestPhotoSizeWithSize;

        if (message == null || message.media == null) {

            return null;

        }

        try {

            if (message.media.document != null) {

                File pathToAttach = fileLoader.getPathToAttach(message.media.document, false);

                if (pathToAttach != null && pathToAttach.exists()) {

                    return pathToAttach;

                }

                File pathToAttach2 = fileLoader.getPathToAttach(message.media.document, true);

                if (pathToAttach2 != null && pathToAttach2.exists()) {

                    return pathToAttach2;

                }

            }

            if (message.media.photo != null && (closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(message.media.photo.sizes, AndroidUtilities.getPhotoSize())) != null) {

                File pathToAttach3 = fileLoader.getPathToAttach(closestPhotoSizeWithSize, false);

                if (pathToAttach3 != null && pathToAttach3.exists()) {

                    return pathToAttach3;

                }

                File pathToAttach4 = fileLoader.getPathToAttach(closestPhotoSizeWithSize, true);

                if (pathToAttach4 != null) {

                    if (pathToAttach4.exists()) {

                        return pathToAttach4;

                    }

                }

            }

        } catch (Exception e) {

        }

        return null;

    }

    private static void startDownload(TLRPC.Message message, MessageObject messageObject, int i, FileLoader fileLoader) {

        TLRPC.PhotoSize closestPhotoSizeWithSize;

        try {

            if (message.media.document != null) {

                if (messageObject != null) {

                    fileLoader.loadFile(message.media.document, messageObject, 1, 0);

                } else {

                    fileLoader.loadFile(message.media.document, new MessageObject(i, message, false, false), 1, 0);

                }

            } else if (message.media.photo != null && (closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(message.media.photo.sizes, AndroidUtilities.getPhotoSize())) != null) {

                if (messageObject == null) {

                    messageObject = new MessageObject(i, message, false, false);

                }

                fileLoader.loadFile(ImageLocation.getForPhoto(closestPhotoSizeWithSize, message.media.photo), messageObject, (String) null, 1, 0);

            }

        } catch (Exception e) {

        }

    }

    

    public static MessageObject findMessageInChat(long j, int i) {

        BaseFragment lastFragment = null;

        try {

            lastFragment = LaunchActivity.getLastFragment();

        } catch (Exception e) {

        }

        if (!(lastFragment instanceof ChatActivity)) {

            return null;

        }

        try {

            Field declaredField = ChatActivity.class.getDeclaredField("messages");

            declaredField.setAccessible(true);

            Object obj = declaredField.get(lastFragment);

            if (obj instanceof ArrayList) {

                Iterator it = ((ArrayList) obj).iterator();

                while (it.hasNext()) {

                    Object next = it.next();

                    if (next instanceof MessageObject) {

                        MessageObject messageObject = (MessageObject) next;

                        if (messageObject.getId() == i && (messageObject.getDialogId() == j || (j != 0 && messageObject.getDialogId() == -j))) {

                            return messageObject;

                        }

                    }

                }

            }

            return null;

        } catch (Exception e) {

            return null;

        }

    }

    static Object mapGet(Object obj, int i) {

        if (obj == null) {

            return null;

        }

        try {

            Class<?> cls = obj.getClass();

            try {

                return cls.getMethod("get", Integer.TYPE).invoke(obj, Integer.valueOf(i));

            } catch (Exception e) {

                try {

                    return cls.getMethod("get", Object.class).invoke(obj, Integer.valueOf(i));

                } catch (Exception e2) {

                    return null;

                }

            }

        } catch (Exception e3) {

        }

        return null;

    }

    

    public static MessageObject findMessageInController(int i) {

        try {

            Object mapGet = mapGet(gf(MessagesController.getInstance(UserConfig.selectedAccount), "dialogMessagesByIds"), i);

            if (mapGet instanceof MessageObject) {

                return (MessageObject) mapGet;

            }

            return null;

        } catch (Exception e) {

            return null;

        }

    }

    private static File getFileFromMessage(MessageObject messageObject) {

        FileLoader fileLoader = null;

        TLRPC.Message message = null;

        TLRPC.PhotoSize closestPhotoSizeWithSize;

        if (messageObject == null) {

            return null;

        }

        try {

            fileLoader = FileLoader.getInstance(UserConfig.selectedAccount);

            message = messageObject.messageOwner;

        } catch (Exception e) {

        }

        if (message == null) {

            return null;

        }

        if (message.media != null && message.media.document != null) {

            File pathToAttach = fileLoader.getPathToAttach(message.media.document, false);

            if (pathToAttach != null && pathToAttach.exists()) {

                return pathToAttach;

            }

            File pathToAttach2 = fileLoader.getPathToAttach(message.media.document, true);

            if (pathToAttach2 != null && pathToAttach2.exists()) {

                return pathToAttach2;

            }

        }

        if (message.media != null && message.media.photo != null && (closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(message.media.photo.sizes, AndroidUtilities.getPhotoSize())) != null) {

            File pathToAttach3 = fileLoader.getPathToAttach(closestPhotoSizeWithSize, false);

            if (pathToAttach3 != null && pathToAttach3.exists()) {

                return pathToAttach3;

            }

            File pathToAttach4 = fileLoader.getPathToAttach(closestPhotoSizeWithSize, true);

            if (pathToAttach4 != null) {

                if (pathToAttach4.exists()) {

                    return pathToAttach4;

                }

            }

        }

        return null;

    }

    private static void copyFile(File file, File file2) {

        try {

            FileInputStream fileInputStream = new FileInputStream(file);

            FileOutputStream fileOutputStream = new FileOutputStream(file2);

            byte[] bArr = new byte[8192];

            while (true) {

                int read = fileInputStream.read(bArr);

                if (read <= 0) {

                    fileInputStream.close();

                    fileOutputStream.close();

                    return;

                }

                fileOutputStream.write(bArr, 0, read);

            }

        } catch (Exception e) {

        }

    }

    private static void loadPresence() {

        if (cfg == null) {

            return;

        }

        try {

            JSONObject jSONObject = new JSONObject(cfg.prefs().getString("presence_data", "{}"));

            JSONArray optJSONArray = jSONObject.optJSONArray("tracked");

            presenceTracked.clear();

            if (optJSONArray != null) {

                for (int i = 0; i < optJSONArray.length(); i++) {

                    presenceTracked.add(Long.valueOf(optJSONArray.optLong(i)));

                }

            }

            JSONObject optJSONObject = jSONObject.optJSONObject("last_seen");

            presenceLastSeen.clear();

            if (optJSONObject != null) {

                Iterator<String> keys = optJSONObject.keys();

                while (keys.hasNext()) {

                    String next = keys.next();

                    presenceLastSeen.put(Long.valueOf(Long.parseLong(next)), Long.valueOf(optJSONObject.optLong(next)));

                }

            }

            JSONObject optJSONObject2 = jSONObject.optJSONObject("records");

            presenceRecords.clear();

            if (optJSONObject2 != null) {

                Iterator<String> keys2 = optJSONObject2.keys();

                while (keys2.hasNext()) {

                    String next2 = keys2.next();

                    long parseLong = Long.parseLong(next2);

                    JSONArray optJSONArray2 = optJSONObject2.optJSONArray(next2);

                    ArrayList<long[]> arrayList = new ArrayList<>();

                    if (optJSONArray2 != null) {

                        for (int i2 = 0; i2 < optJSONArray2.length(); i2++) {

                            JSONArray optJSONArray3 = optJSONArray2.optJSONArray(i2);

                            if (optJSONArray3 != null && optJSONArray3.length() >= 2) {

                                arrayList.add(new long[]{optJSONArray3.optLong(0), optJSONArray3.optLong(1)});

                            }

                        }

                    }

                    presenceRecords.put(Long.valueOf(parseLong), arrayList);

                }

            }

        } catch (Exception e) {

        }

    }

    private static void savePresence() {

        if (!presenceDirty || cfg == null) {

            return;

        }

        try {

            JSONObject jSONObject = new JSONObject();

            JSONArray jSONArray = new JSONArray();

            for (Long l : presenceTracked) {

                jSONArray.put(l);

            }

            jSONObject.put("tracked", jSONArray);

            JSONObject jSONObject2 = new JSONObject();

            for (Map.Entry<Long, Long> entry : presenceLastSeen.entrySet()) {

                jSONObject2.put(String.valueOf(entry.getKey()), entry.getValue());

            }

            jSONObject.put("last_seen", jSONObject2);

            JSONObject jSONObject3 = new JSONObject();

            for (Map.Entry<Long, ArrayList<long[]>> entry2 : presenceRecords.entrySet()) {

                JSONArray jSONArray2 = new JSONArray();

                Iterator<long[]> it = entry2.getValue().iterator();

                while (it.hasNext()) {

                    long[] next = it.next();

                    JSONArray jSONArray3 = new JSONArray();

                    jSONArray3.put(next[0]);

                    jSONArray3.put(next[1]);

                    jSONArray2.put(jSONArray3);

                }

                jSONObject3.put(String.valueOf(entry2.getKey()), jSONArray2);

            }

            jSONObject.put("records", jSONObject3);

            cfg.prefs().edit().putString("presence_data", jSONObject.toString()).apply();

            presenceDirty = false;

        } catch (Exception e) {

        }

    }

    

    public static void presenceTickInternal() {

        if (cfg.b("presence_track", false)) {

            long currentTimeMillis = System.currentTimeMillis() / 1000;

            int i = new int[]{30, 45, 60, 120}[Math.max(0, Math.min(cfg.i("presence_decay", 1), 3))];

            for (Map.Entry<Long, Long> entry : presenceLamp.entrySet()) {

                long longValue = entry.getKey().longValue();

                long longValue2 = entry.getValue().longValue();

                boolean z = currentTimeMillis - longValue2 < ((long) i);

                long[] jArr = presenceOpen.get(Long.valueOf(longValue));

                if (z) {

                    if (jArr == null) {

                        presenceOpen.put(Long.valueOf(longValue), new long[]{longValue2, longValue2});

                    } else {

                        jArr[1] = longValue2;

                    }

                } else if (jArr != null) {

                    ArrayList<long[]> arrayList = presenceRecords.get(Long.valueOf(longValue));

                    if (arrayList == null) {

                        arrayList = new ArrayList<>();

                        presenceRecords.put(Long.valueOf(longValue), arrayList);

                    }

                    arrayList.add(new long[]{jArr[0], jArr[1]});

                    presenceOpen.remove(Long.valueOf(longValue));

                    presenceDirty = true;

                }

            }

        }

        // tr("Сохранять последний онлайн","Save last online") пишет presenceLastSeen без трекера —

        // сбрасывать dirty на диск нужно и без presence_track.

        // savePresence строит JSON из потенциально тысяч записей — делаем это в

        // фоне, presenceTick живёт на главном потоке (H.postDelayed).

        if (presenceDirty) {

            new Thread(new Runnable() {

                @Override

                public void run() {

                    try {

                        savePresence();

                    } catch (Exception e) {

                    }

                }

            }, "MaxPresenceSave").start();

        }

    }

    

    public static void presenceRemember(long j, long j2) {

        presenceLastSeen.put(Long.valueOf(j), Long.valueOf(j2));

        presenceLamp.put(Long.valueOf(j), Long.valueOf(j2));

        presenceDirty = true;

    }

    // Запись tr("когда пользователь был в сети","when user was online") — только если время свежее уже

    // зафиксированного (апдейты могут приходить не по порядку).

    public static void presenceRecord(long uid, long ts) {

        if (uid <= 0 || ts <= 0) {

            return;

        }

        Long prev = presenceLastSeen.get(Long.valueOf(uid));

        if (prev != null && prev.longValue() >= ts) {

            return;

        }

        presenceRemember(uid, ts);

    }

    static void sf(Object obj, String str, Object obj2) {

        try {

            for (Class<?> cls = obj.getClass(); cls != null; cls = cls.getSuperclass()) {

                try {

                    Field declaredField = cls.getDeclaredField(str);

                    declaredField.setAccessible(true);

                    if (!(obj2 instanceof Long) || declaredField.getType() != Long.TYPE) {

                        if (!(obj2 instanceof Integer) || declaredField.getType() != Integer.TYPE) {

                            if (!(obj2 instanceof Boolean) || declaredField.getType() != Boolean.TYPE) {

                                declaredField.set(obj, obj2);

                                return;

                            } else {

                                declaredField.setBoolean(obj, ((Boolean) obj2).booleanValue());

                                return;

                            }

                        }

                        declaredField.setInt(obj, ((Integer) obj2).intValue());

                        return;

                    }

                    declaredField.setLong(obj, ((Long) obj2).longValue());

                    return;

                } catch (NoSuchFieldException e) {

                }

            }

        } catch (Exception e2) {

        }

    }

    static void sfFlag(Object obj, String fieldName, Object value, int bitMask) {

        sf(obj, fieldName, value);

        try {

            Field flagsField = obj.getClass().getDeclaredField("flags");

            flagsField.setAccessible(true);

            int flags = flagsField.getInt(obj);

            if (value instanceof Boolean) {

                flags = ((Boolean) value).booleanValue() ? (flags | bitMask) : (flags & ~bitMask);

            } else if (value instanceof Integer) {

                int intVal = ((Integer) value).intValue();

                flags = (intVal != 0) ? (flags | bitMask) : (flags & ~bitMask);

            } else if (value instanceof Long) {

                long longVal = ((Long) value).longValue();

                flags = (longVal != 0) ? (flags | bitMask) : (flags & ~bitMask);

            } else {

                flags = (value != null) ? (flags | bitMask) : (flags & ~bitMask);

            }

            flagsField.setInt(obj, flags);

        } catch (Exception e) {

        }

    }

    static Object gf(Object obj, String str) {

        try {

            for (Class<?> cls = obj.getClass(); cls != null; cls = cls.getSuperclass()) {

                try {

                    Field declaredField = cls.getDeclaredField(str);

                    declaredField.setAccessible(true);

                    return declaredField.get(obj);

                } catch (NoSuchFieldException e) {

                }

            }

            return null;

        } catch (Exception e2) {

            return null;

        }

    }

    static long peer2did(TLRPC.Peer peer) {

        if (peer == null) {

            return 0L;

        }

        if (peer.user_id != 0) {

            return peer.user_id;

        }

        if (peer.chat_id != 0) {

            return -peer.chat_id;

        }

        if (peer.channel_id != 0) {

            return -peer.channel_id;

        }

        return 0L;

    }

    

    public static class Config {

        private static final Map<String, String> TAB_OF = new HashMap();

        private final Map<String, Object> c = new ConcurrentHashMap();

        private final SharedPreferences p;

        static {

            String[] ghostKeys = {"ghost_mode", "ghost_no_online", "ghost_immediate_offline", "ghost_no_typing", "ghost_no_read", "ghost_no_read_stories", "read_on_action", "use_scheduled", "silent_send", "story_when_ghost_on", "story_when_ghost_off", "drawer_ghost_btn"};
            for (String key : ghostKeys) {
                TAB_OF.put(key, "tab_ghost");
            }

            String[] spyKeys = {"keep_app_alive", "save_deleted", "save_edits", "save_bot_chats", "presence_track", "track_own_deletions", "del_show_mark", "del_use_icon", "del_custom_label", "del_color", "del_show_opacity", "del_opacity_level", "save_attachments", "save_attach_private", "save_attach_groups", "save_attach_channels", "max_folder_size", "auto_cleanup_age", "presence_only_hidden", "presence_decay"};
            for (String key : spyKeys) {
                TAB_OF.put(key, "tab_spy");
            }

            String[] hackerKeys = {"hide_ads", "hide_feed_ads", "no_forward_limit", "fake_premium", "keep_once", "allow_screenshots", "bypass_noforward", "fwd_mode", "bypass_story_save"};
            for (String key : hackerKeys) {
                TAB_OF.put(key, "tab_hacker");
            }

        }

        Config(Context context) {

            this.p = context.getSharedPreferences(Main.PREFS, 0);

        }

        private String tabOf(String str) {

            String str2 = TAB_OF.get(str);

            if (str2 == null || this.p.getBoolean(str2, true)) {

                return null;

            }

            this.c.remove(str);

            return str2;

        }

        public boolean b(String str, boolean z) {

            if (tabOf(str) != null) {

                return false;

            }

            Object obj = this.c.get(str);

            if (obj instanceof Boolean) {

                return ((Boolean) obj).booleanValue();

            }

            boolean z2 = this.p.getBoolean(str, z);

            this.c.put(str, Boolean.valueOf(z2));

            return z2;

        }

        public void set(String str, boolean z) {

            this.c.put(str, Boolean.valueOf(z));

            this.p.edit().putBoolean(str, z).apply();

        }

        public int i(String str, int i) {

            if (tabOf(str) != null) {

                return 0;

            }

            Object obj = this.c.get(str);

            if (obj instanceof Integer) {

                return ((Integer) obj).intValue();

            }

            int i2 = this.p.getInt(str, i);

            this.c.put(str, Integer.valueOf(i2));

            return i2;

        }

        public void set(String str, int i) {

            this.c.put(str, Integer.valueOf(i));

            this.p.edit().putInt(str, i).apply();

        }

        public String s(String str, String str2) {

            return tabOf(str) != null ? "" : this.p.getString(str, str2);

        }

        boolean isGhost() {

            return b("ghost_mode", false);

        }

        public void flush() {

            this.c.clear();

        }

        public SharedPreferences prefs() {

            return this.p;

        }

    }

    public static void pyFlushConfig() {

        if (cfg != null) {

            cfg.flush();

            try {

                android.content.SharedPreferences prefs = ApplicationLoader.applicationContext.getSharedPreferences(PREFS, 0);

                if (prefs.getBoolean("open_del_cust", false)) {

                    prefs.edit().putBoolean("open_del_cust", false).apply();

                    showDeletedCustomize();

                }

            } catch (Throwable t) {

                Log.e(TAG, "[pyFlush] " + t.getMessage(), t);

            }

        }

    }

    public static void pySyncFilters(String str) {

        try {

            if (hm != null) {

                hm.handleSyncFilters(str);

            }

        } catch (Exception e) {

            err("syncF2", e);

        }

    }

    private static boolean ownDelSkip() {

        try {

            if (cfg != null) {

                return cfg.prefs().getBoolean("track_own_deletions", false);

            }

            return false;

        } catch (Exception e) {

            return false;

        }

    }

    

    public static class VaultDb {

        private final DbHelper helper;

        private final Set<Long> rem = ConcurrentHashMap.newKeySet();

        private final Map<Long, Long> remTsCache = new ConcurrentHashMap<>();

        private final Set<Long> readWhitelist = ConcurrentHashMap.newKeySet();

        private final ArrayList<String> regex = new ArrayList<>();

        private final HandlerThread dbThread;

        private final Handler dbHandler;

        VaultDb(Context context) {

            this.helper = new DbHelper(context, new File(context.getFilesDir(), "exteramax.db").getAbsolutePath());

            dbThread = new HandlerThread("exteramax_db");

            dbThread.start();

            dbHandler = new Handler(dbThread.getLooper());

            ensureColumns();

            loadKeys();

        }

        void postToDbThread(Runnable r) { dbHandler.post(r); }

        public void saveReadDate(long did, int mid, int readDate) {
            if (did == 0 || mid <= 0 || readDate <= 0) return;
            try {
                SQLiteDatabase db = this.helper.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put("did", Long.valueOf(did));
                cv.put("mid", Integer.valueOf(mid));
                cv.put("read_date", Integer.valueOf(readDate));
                db.insertWithOnConflict("read_dates", null, cv, SQLiteDatabase.CONFLICT_REPLACE);
            } catch (Throwable t) {}
        }

        public int getReadDate(long did, int mid) {
            if (did == 0 || mid <= 0) return 0;
            try {
                SQLiteDatabase db = this.helper.getReadableDatabase();
                Cursor c = db.rawQuery("SELECT read_date FROM read_dates WHERE did=? AND mid=?", new String[]{String.valueOf(did), String.valueOf(mid)});
                int res = 0;
                if (c != null) {
                    if (c.moveToNext()) {
                        res = c.getInt(0);
                    }
                    c.close();
                }
                return res;
            } catch (Throwable t) {
                return 0;
            }
        }

        private void ensureColumns() {

            boolean z;

            try {

                SQLiteDatabase writableDatabase = this.helper.getWritableDatabase();

                Cursor rawQuery = writableDatabase.rawQuery("PRAGMA table_info(deleted_keys)", null);

                while (true) {

                    if (!rawQuery.moveToNext()) {

                        z = false;

                        break;

                    }

                    z = true;

                    if ("data".equals(rawQuery.getString(1))) {

                        break;

                    }

                }

                rawQuery.close();

                if (!z) {

                    writableDatabase.execSQL("ALTER TABLE deleted_keys ADD COLUMN data TEXT");

                }

                // Ensure attachment_saved column exists

                Cursor rawQuery2 = writableDatabase.rawQuery("PRAGMA table_info(deleted_keys)", null);

                boolean hasAttachmentSaved = false;

                while (rawQuery2.moveToNext()) {

                    if ("attachment_saved".equals(rawQuery2.getString(1))) {

                        hasAttachmentSaved = true;

                        break;

                    }

                }

                rawQuery2.close();

                if (!hasAttachmentSaved) {
                    writableDatabase.execSQL("ALTER TABLE deleted_keys ADD COLUMN attachment_saved INTEGER NOT NULL DEFAULT 0");
                }
                writableDatabase.execSQL("CREATE TABLE IF NOT EXISTS shadowban(uid INTEGER PRIMARY KEY, hide_dialog INTEGER NOT NULL DEFAULT 0, hide_groups INTEGER NOT NULL DEFAULT 1)");
                writableDatabase.execSQL("CREATE TABLE IF NOT EXISTS dialog_exclusions(did INTEGER PRIMARY KEY, reading INTEGER NOT NULL DEFAULT 0, typing INTEGER NOT NULL DEFAULT 0)");

            } catch (Exception e) {

            }

        }

        private void loadKeys() {

            try {

                SQLiteDatabase writableDatabase = this.helper.getWritableDatabase();

                if (!Main.cfg.b("db_cleanup_v4_done", false)) {
                    try {
                        writableDatabase.delete("deleted_keys", null, null);
                        Main.deletedStore.clear();
                        this.rem.clear();
                        this.remTsCache.clear();
                        Main.cfg.set("db_cleanup_v4_done", true);
                        Log.i(TAG, "Purged corrupted legacy deleted_keys database v4");
                    } catch (Throwable t) {}
                }

                writableDatabase.delete("deleted_keys", "did=0 OR mid<=0", null);

                Main.deletedStore.clear();
                Cursor rawQuery = writableDatabase.rawQuery("SELECT did,mid,ts FROM deleted_keys", null);
                while (rawQuery.moveToNext()) {
                    long d = rawQuery.getLong(0);
                    int m = rawQuery.getInt(1);
                    long k = pk(d, m);
                    this.rem.add(Long.valueOf(k));
                    this.remTsCache.put(Long.valueOf(k), Long.valueOf(rawQuery.getLong(2)));
                    Main.getDeletedIds(d).add(Integer.valueOf(m));
                }
                rawQuery.close();

                Main.ShadowbanStore.map.clear();
                try {
                    Cursor rawSb = writableDatabase.rawQuery("SELECT uid, hide_dialog, hide_groups FROM shadowban", null);
                    while (rawSb.moveToNext()) {
                        long u = rawSb.getLong(0);
                        boolean hd = rawSb.getInt(1) == 1;
                        boolean hg = rawSb.getInt(2) == 1;
                        Main.ShadowbanStore.map.put(Long.valueOf(u), new Main.ShadowbanEntry(u, hd, hg));
                    }
                    rawSb.close();
                } catch (Exception e) {}

                Main.ExclusionsStore.map.clear();
                try {
                    Cursor rawExc = writableDatabase.rawQuery("SELECT did, reading, typing FROM dialog_exclusions", null);
                    while (rawExc.moveToNext()) {
                        long d = rawExc.getLong(0);
                        int r = rawExc.getInt(1);
                        int t = rawExc.getInt(2);
                        Main.ExclusionsStore.map.put(Long.valueOf(d), new Main.DialogExclusion(d, r, t));
                    }
                    rawExc.close();
                } catch (Exception e) {}

                Cursor rawQuery2 = writableDatabase.rawQuery("SELECT did FROM exception_users WHERE exception_reading=1", null);
                while (rawQuery2.moveToNext()) {
                    this.readWhitelist.add(Long.valueOf(rawQuery2.getLong(0)));
                }
                rawQuery2.close();

                ArrayList<String> rxList = new ArrayList<>();
                Cursor rawQuery3 = writableDatabase.rawQuery("SELECT regex FROM regex_filters ORDER BY rowid ASC", null);
                while (rawQuery3.moveToNext()) {
                    String r = rawQuery3.getString(0);
                    this.regex.add(r);
                    rxList.add(r);
                }
                rawQuery3.close();
                Main.RegexFiltersStore.load(rxList);

            } catch (Exception e) {

            }

        }

        private static long pk(long j, int i) {

            return (j << 32) ^ (i & 4294967295L);

        }

        public boolean isRem(long j, int i) {
            return Main.isDeletedMsg(j, i);
        }

        public boolean isRem(MessageObject messageObject) {
            return messageObject != null && Main.isDeletedMsg(messageObject);
        }

        public Map<Long, ArrayList<Integer>> getAllDeletedByDialog() {
            Map<Long, ArrayList<Integer>> result = new HashMap<>();
            try {
                Cursor c = this.helper.getReadableDatabase().rawQuery("SELECT did, mid FROM deleted_keys", null);
                if (c != null) {
                    while (c.moveToNext()) {
                        long d = c.getLong(0);
                        int m = c.getInt(1);
                        ArrayList<Integer> list = result.get(d);
                        if (list == null) {
                            list = new ArrayList<>();
                            result.put(d, list);
                        }
                        list.add(m);
                    }
                    c.close();
                }
            } catch (Throwable t) {
                Main.err("getAllDeletedByDialog", new Exception(t));
            }
            return result;
        }

        public void clearAllDeleted() {
            try {
                this.helper.getWritableDatabase().delete("deleted_keys", null, null);
                this.rem.clear();
                this.remTsCache.clear();
                Main.deletedStore.clear();
            } catch (Throwable t) {
                Main.err("clearAllDeleted", new Exception(t));
            }
        }

        // Check if message ID exists in vault for ANY dialog (used when dialog_id unavailable)

        public boolean isRemAny(int i) {

            for (Long key : this.rem) {

                if ((key.intValue() & 0xFFFFFFFF) == i) {

                    return true;

                }

            }

            return false;

        }

        public long remTs(long j, int i) {

            try {

                long k = pk(j, i);

                Long cached = remTsCache.get(Long.valueOf(k));

                if (cached != null) return cached.longValue();

                Cursor rawQuery = this.helper.getReadableDatabase().rawQuery("SELECT ts FROM deleted_keys WHERE did=? AND mid=? LIMIT 1", new String[]{String.valueOf(j), String.valueOf(i)});

                long j2 = rawQuery.moveToFirst() ? rawQuery.getLong(0) : 0L;

                rawQuery.close();

                if (j2 > 0) remTsCache.put(Long.valueOf(k), Long.valueOf(j2));

                return j2;

            } catch (Exception e) {

                return 0L;

            }

        }

        public void markRem(long j, Collection<Integer> collection, Map<Integer, String> map) {

            if (j == 489000L) return;

            markRemAsync(j, collection, map, null);

        }

        public void markRemAsync(long did, Collection<Integer> mids, Map<Integer, String> dataMap, Runnable onDone) {

            if (mids == null || mids.isEmpty() || did == 0 || did == 489000L) { if (onDone != null) onDone.run(); return; }

            final long fDid = did;

            final ArrayList<Integer> fMids = new ArrayList<>(mids);

            final Map<Integer, String> fDataMap = dataMap != null ? new HashMap<>(dataMap) : null;

            postToDbThread(() -> {

                try {

                    SQLiteDatabase d = helper.getWritableDatabase();

                    d.beginTransaction();

                    try {

                        SQLiteStatement s = d.compileStatement("INSERT OR IGNORE INTO deleted_keys(did,mid,ts,data,attachment_saved) VALUES(?,?,?,?,0)");

                        long now = System.currentTimeMillis();

                        for (int m : fMids) {

                            if (m <= 0) continue;

                            s.clearBindings();

                            s.bindLong(1, fDid);

                            s.bindLong(2, m);

                            s.bindLong(3, now);

                            String data = fDataMap != null ? fDataMap.get(m) : null;

                            if (data != null) s.bindString(4, data); else s.bindNull(4);

                            s.executeInsert();

                            long k = pk(fDid, m);

                            rem.add(Long.valueOf(k));

                            remTsCache.put(Long.valueOf(k), Long.valueOf(now));

                        }

                        s.close();

                        d.setTransactionSuccessful();

                    } finally { d.endTransaction(); }

                } catch (Exception e) {}

                if (onDone != null) AndroidUtilities.runOnUIThread(onDone);

            });

        }

        public int countRemovedForDialog(long j) {

            try {

                Cursor rawQuery = this.helper.getReadableDatabase().rawQuery("SELECT COUNT(*) FROM deleted_keys WHERE did=?", new String[]{String.valueOf(j)});

                int i = rawQuery.moveToFirst() ? rawQuery.getInt(0) : 0;

                rawQuery.close();

                return i;

            } catch (Exception e) {

                return 0;

            }

        }

        public List<DeletedItem> getRemovedListPage(long j, int i, int i2) {

            ArrayList arrayList = new ArrayList();

            try {

                Cursor rawQuery = this.helper.getReadableDatabase().rawQuery("SELECT mid, data, ts, attachment_saved FROM deleted_keys WHERE did=? ORDER BY ts DESC LIMIT ? OFFSET ?", new String[]{String.valueOf(j), String.valueOf(i2), String.valueOf(i)});

                while (rawQuery.moveToNext()) {

                    arrayList.add(new DeletedItem(rawQuery.getInt(0), rawQuery.getString(1), rawQuery.getLong(2), rawQuery.getInt(3)));

                }

                rawQuery.close();

            } catch (Exception e) {

            }

            return arrayList;

        }

        public void purgeAllForDialog(long j) {

            purgeAllForDialogAsync(j, null);

        }

        public void purgeAllForDialogAsync(long did, Runnable onDone) {

            if (did == 0) { if (onDone != null) onDone.run(); return; }

            final long fDid = did;

            postToDbThread(() -> {

                try {

                    SQLiteDatabase d = helper.getWritableDatabase();

                    d.delete("deleted_keys", "did=?", new String[]{String.valueOf(fDid)});

                    ArrayList<Long> toRemove = new ArrayList<>();

                    for (Long k : rem) { if ((k >> 32) == fDid) toRemove.add(k); }

                    rem.removeAll(toRemove);

                    for (Long k : toRemove) { remTsCache.remove(k); }

                } catch (Exception e) {}

                if (onDone != null) AndroidUtilities.runOnUIThread(onDone);

            });

        }

        public ArrayList<Integer> allMessageIdsByDid(long did) {

            ArrayList<Integer> out = new ArrayList<>();

            try {

                Cursor c = helper.getReadableDatabase().rawQuery("SELECT mid FROM deleted_keys WHERE did=?", new String[]{String.valueOf(did)});

                while (c.moveToNext()) out.add(c.getInt(0));

                c.close();

            } catch (Exception e) {}

            return out;

        }

        public String getData(long did, int mid) {

            try {

                Cursor c = helper.getReadableDatabase().rawQuery("SELECT data FROM deleted_keys WHERE did=? AND mid=? LIMIT 1", new String[]{String.valueOf(did), String.valueOf(mid)});

                String data = null;

                if (c.moveToFirst()) data = c.getString(0);

                c.close();

                return data;

            } catch (Exception e) { return null; }

        }

        public ArrayList<Integer> getRemovedMids(long j) {

            ArrayList<Integer> arrayList = new ArrayList<>();

            try {

                Cursor rawQuery = this.helper.getReadableDatabase().rawQuery("SELECT mid FROM deleted_keys WHERE did=? ORDER BY ts DESC", new String[]{String.valueOf(j)});

                while (rawQuery.moveToNext()) {

                    arrayList.add(Integer.valueOf(rawQuery.getInt(0)));

                }

                rawQuery.close();

            } catch (Exception e) {

            }

            return arrayList;

        }

        

        public static class DeletedItem {

            public final int mid;

            public final String text;

            public final long ts;

            public final int attachmentSaved;

            public DeletedItem(int i, String str, long j) {

                this(i, str, j, 0);

            }

            public DeletedItem(int i, String str, long j, int attachmentSaved) {

                this.mid = i;

                this.text = str;

                this.ts = j;

                this.attachmentSaved = attachmentSaved;

            }

        }

        public void updateRemovedText(long j, Map<Integer, String> map) {

            updateRemovedTextAsync(j, map, null);

        }

        public void updateRemovedTextAsync(long did, Map<Integer, String> textMap, Runnable onDone) {

            if (textMap == null || textMap.isEmpty()) { if (onDone != null) onDone.run(); return; }

            final long fDid = did;

            final Map<Integer, String> fTextMap = new HashMap<>(textMap);

            postToDbThread(() -> {

                try {

                    SQLiteDatabase d = helper.getWritableDatabase();

                    SQLiteStatement s = d.compileStatement("UPDATE deleted_keys SET data=? WHERE did=? AND mid=? AND (data IS NULL OR data='' OR data='[deleted]')");

                    for (Map.Entry<Integer, String> e : fTextMap.entrySet()) {

                        if (e.getValue() == null || e.getValue().isEmpty()) continue;

                        s.clearBindings();

                        s.bindString(1, e.getValue());

                        s.bindLong(2, fDid);

                        s.bindLong(3, e.getKey());

                        s.executeUpdateDelete();

                    }

                    s.close();

                } catch (Exception e) {}

                if (onDone != null) AndroidUtilities.runOnUIThread(onDone);

            });

        }

        // Mark attachment as successfully saved for a specific message

        public void markAttachmentSaved(long did, int mid) {

            postToDbThread(() -> {

                try {

                    SQLiteDatabase d = helper.getWritableDatabase();

                    SQLiteStatement s = d.compileStatement("UPDATE deleted_keys SET attachment_saved=1 WHERE did=? AND mid=?");

                    s.bindLong(1, did);

                    s.bindLong(2, mid);

                    s.executeUpdateDelete();

                    s.close();

                } catch (Exception e) {}

            });

        }

        // Mark attachment as failed/missing for a specific message (download failed)

        public void markAttachmentMissing(long did, int mid) {

            postToDbThread(() -> {

                try {

                    SQLiteDatabase d = helper.getWritableDatabase();

                    SQLiteStatement s = d.compileStatement("UPDATE deleted_keys SET attachment_saved=-1 WHERE did=? AND mid=?");

                    s.bindLong(1, did);

                    s.bindLong(2, mid);

                    s.executeUpdateDelete();

                    s.close();

                } catch (Exception e) {}

            });

        }

        // Check if attachment was saved for a message

        public int getAttachmentSavedStatus(long did, int mid) {

            try {

                Cursor c = helper.getReadableDatabase().rawQuery("SELECT attachment_saved FROM deleted_keys WHERE did=? AND mid=? LIMIT 1", new String[]{String.valueOf(did), String.valueOf(mid)});

                int status = 0;

                if (c.moveToFirst()) {

                    status = c.getInt(0);

                }

                c.close();

                return status; // 0 = not saved/pending, 1 = saved, -1 = failed

            } catch (Exception e) {

                return 0;

            }

        }

        public void purge(long j, ArrayList<Integer> arrayList) {

            purgeAsync(j, arrayList, null);

        }

        public void purgeAsync(long did, ArrayList<Integer> mids, Runnable onDone) {

            if (mids == null || mids.isEmpty()) { if (onDone != null) onDone.run(); return; }

            final long fDid = did;

            final ArrayList<Integer> fMids = new ArrayList<>(mids);

            postToDbThread(() -> {

                try {

                    SQLiteDatabase d = helper.getWritableDatabase();

                    d.beginTransaction();

                    try {

                        for (int m : fMids) { long k = pk(fDid, m); d.delete("deleted_keys", "did=? AND mid=?", new String[]{String.valueOf(fDid), String.valueOf(m)}); rem.remove(Long.valueOf(k)); remTsCache.remove(Long.valueOf(k)); }

                        d.setTransactionSuccessful();

                    } finally { d.endTransaction(); }

                } catch (Exception e) {}

                if (onDone != null) AndroidUtilities.runOnUIThread(onDone);

            });

        }

        public boolean isReadWhitelisted(long j) {

            return this.readWhitelist.contains(Long.valueOf(j));

        }

        public ArrayList<Long> getReadWhitelist() {

            return new ArrayList<>(this.readWhitelist);

        }

        public boolean addReadWhitelist(long j) {

            if (j <= 0) {

                return false;

            }

            this.readWhitelist.add(Long.valueOf(j));

            try {

                SQLiteStatement compileStatement = this.helper.getWritableDatabase().compileStatement("INSERT OR REPLACE INTO exception_users(did,exception_reading,exception_typing) VALUES(?,?,0)");

                compileStatement.bindLong(1, j);

                compileStatement.bindLong(2, 1L);

                compileStatement.executeInsert();

                compileStatement.close();

            } catch (Exception e) {

            }

            return true;

        }

        public boolean removeReadWhitelist(long j) {

            if (j <= 0) {

                return false;

            }

            this.readWhitelist.remove(Long.valueOf(j));

            try {

                this.helper.getWritableDatabase().delete("exception_users", "did=?", new String[]{String.valueOf(j)});

            } catch (Exception e) {

            }

            return true;

        }

        public ArrayList<String> getRegex() {

            return new ArrayList<>(this.regex);

        }

                public void saveShadowban(final long uid, final boolean hideDialog, final boolean hideInGroups) {
            postToDbThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        SQLiteStatement s = helper.getWritableDatabase().compileStatement("INSERT OR REPLACE INTO shadowban(uid, hide_dialog, hide_groups) VALUES(?,?,?)");
                        s.bindLong(1, uid);
                        s.bindLong(2, hideDialog ? 1L : 0L);
                        s.bindLong(3, hideInGroups ? 1L : 0L);
                        s.executeInsert();
                        s.close();
                    } catch (Exception e) {}
                }
            });
        }

        public void removeShadowban(final long uid) {
            postToDbThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        helper.getWritableDatabase().delete("shadowban", "uid=?", new String[]{String.valueOf(uid)});
                    } catch (Exception e) {}
                }
            });
        }

        public void saveExclusion(final long did, final int reading, final int typing) {
            postToDbThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        SQLiteStatement s = helper.getWritableDatabase().compileStatement("INSERT OR REPLACE INTO dialog_exclusions(did, reading, typing) VALUES(?,?,?)");
                        s.bindLong(1, did);
                        s.bindLong(2, (long) reading);
                        s.bindLong(3, (long) typing);
                        s.executeInsert();
                        s.close();
                    } catch (Exception e) {}
                }
            });
        }

        public void removeExclusion(final long did) {
            postToDbThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        helper.getWritableDatabase().delete("dialog_exclusions", "did=?", new String[]{String.valueOf(did)});
                    } catch (Exception e) {}
                }
            });
        }
public boolean addRegex(String str) {

            if (str == null || str.trim().isEmpty()) {

                return false;

            }

            String trim = str.trim();

            if (this.regex.contains(trim)) {

                return false;

            }

            this.regex.add(trim);

            try {

                SQLiteStatement compileStatement = this.helper.getWritableDatabase().compileStatement("INSERT OR IGNORE INTO regex_filters(regex) VALUES(?)");

                compileStatement.bindString(1, trim);

                compileStatement.executeInsert();

                compileStatement.close();

            } catch (Exception e) {

            }

            return true;

        }

        public boolean removeRegex(String str) {

            if (str == null) {

                return false;

            }

            String trim = str.trim();

            if (trim.isEmpty()) {

                return false;

            }

            this.regex.remove(trim);

            try {

                this.helper.getWritableDatabase().delete("regex_filters", "regex=?", new String[]{trim});

            } catch (Exception e) {

            }

            return true;

        }

        public boolean hasEdits(long j, int i) {

            try {

                boolean z = true;

                Cursor rawQuery = this.helper.getReadableDatabase().rawQuery("SELECT MAX(ver) FROM message_edits WHERE did=? AND mid=?", new String[]{String.valueOf(j), String.valueOf(i)});

                if (!rawQuery.moveToFirst() || rawQuery.isNull(0) || rawQuery.getInt(0) <= 0) {

                    z = false;

                }

                rawQuery.close();

                return z;

            } catch (Exception e) {

                return false;

            }

        }

        public ArrayList<TLRPC.Message> listVersionsOfEditedMessage(long did, int mid) {

            ArrayList<TLRPC.Message> out = new ArrayList<>();

            try {

                Cursor c = this.helper.getReadableDatabase().rawQuery("SELECT ver,date,data FROM message_edits WHERE did=? AND mid=? ORDER BY ver ASC", new String[]{String.valueOf(did), String.valueOf(mid)});

                while (c.moveToNext()) {

                    try {

                        TLRPC.Message m = deser(c.getBlob(2));

                        if (m != null) {

                            out.add(m);

                        }

                    } catch (Exception ignore) {}

                }

                c.close();

            } catch (Exception ignore) {}

            return out;

        }

        public List<EditItem> getEdits(long j, int i) {

            ArrayList arrayList = new ArrayList();

            try {

                Cursor rawQuery = this.helper.getReadableDatabase().rawQuery("SELECT ver,date,data FROM message_edits WHERE did=? AND mid=? ORDER BY ver ASC", new String[]{String.valueOf(j), String.valueOf(i)});

                while (rawQuery.moveToNext()) {

                    String str = "";

                    try {

                        TLRPC.Message deser = deser(rawQuery.getBlob(2));

                        if (deser != null && deser.message != null) {

                            str = deser.message;

                        }

                    } catch (Exception e) {

                    }

                    arrayList.add(new EditItem(rawQuery.getInt(0), rawQuery.getLong(1), str));

                }

                rawQuery.close();

            } catch (Exception e2) {

            }

            return arrayList;

        }

        public void saveOrig(long j, int i, TLRPC.Message message) {

            saveOrigAsync(j, i, message, null);

        }

        public void saveOrigAsync(long j, int i, TLRPC.Message message, Runnable onDone) {

            final long fJ = j;

            final int fI = i;

            final TLRPC.Message fMsg = message;

            postToDbThread(() -> {

                try {

                    SQLiteStatement compileStatement = this.helper.getWritableDatabase().compileStatement("INSERT OR IGNORE INTO message_edits(did,mid,ver,date,data) VALUES(?,?,0,?,?)");

                    compileStatement.bindLong(1, fJ);

                    compileStatement.bindLong(2, fI);

                    compileStatement.bindLong(3, System.currentTimeMillis());

                    byte[] s = ser(fMsg);

                    if (s == null) {

                        s = new byte[0];

                    }

                    compileStatement.bindBlob(4, s);

                    compileStatement.executeInsert();

                    compileStatement.close();

                } catch (Exception e) {

                }

                if (onDone != null) AndroidUtilities.runOnUIThread(onDone);

            });

        }

        public void saveVer(long j, int i, TLRPC.Message message) {

            saveVerAsync(j, i, message, null);

        }

        public void saveVerAsync(long j, int i, TLRPC.Message message, Runnable onDone) {

            final long fJ = j;

            final int fI = i;

            final TLRPC.Message fMsg = message;

            postToDbThread(() -> {

                try {

                    SQLiteDatabase writableDatabase = this.helper.getWritableDatabase();

                    writableDatabase.beginTransaction();

                    try {

                        Cursor rawQuery = writableDatabase.rawQuery("SELECT MAX(ver) FROM message_edits WHERE did=? AND mid=?", new String[]{String.valueOf(fJ), String.valueOf(fI)});

                        int max = (!rawQuery.moveToFirst() || rawQuery.isNull(0)) ? 1 : Math.max(1, rawQuery.getInt(0) + 1);

                        rawQuery.close();

                        SQLiteStatement compileStatement = writableDatabase.compileStatement("INSERT INTO message_edits(did,mid,ver,date,data) VALUES(?,?,?,?,?)");

                        compileStatement.bindLong(1, fJ);

                        compileStatement.bindLong(2, fI);

                        compileStatement.bindLong(3, max);

                        compileStatement.bindLong(4, System.currentTimeMillis());

                        byte[] s = ser(fMsg);

                        if (s == null) {

                            s = new byte[0];

                        }

                        compileStatement.bindBlob(5, s);

                        compileStatement.executeInsert();

                        compileStatement.close();

                        writableDatabase.setTransactionSuccessful();

                    } finally {

                        writableDatabase.endTransaction();

                    }

                } catch (Exception e) {

                }

                if (onDone != null) AndroidUtilities.runOnUIThread(onDone);

            });

        }

        public void wipe() {

            wipeAsync(null);

        }

        public void wipeAsync(Runnable onDone) {

            postToDbThread(() -> {

                try {

                    SQLiteDatabase writableDatabase = this.helper.getWritableDatabase();

                    writableDatabase.beginTransaction();

                    try {

                        writableDatabase.delete("deleted_keys", null, null);

                        writableDatabase.delete("message_edits", null, null);

                        writableDatabase.setTransactionSuccessful();

                        this.rem.clear();

                        this.remTsCache.clear();

                    } finally {

                        writableDatabase.endTransaction();

                    }

                } catch (Exception e) {

                }

                if (onDone != null) AndroidUtilities.runOnUIThread(onDone);

            });

        }

        private static byte[] ser(TLRPC.Message message) {

            if (message == null) {

                return null;

            }

            SerializedData serializedData = new SerializedData(message.getObjectSize());

            message.serializeToStream(serializedData);

            byte[] byteArray = serializedData.toByteArray();

            serializedData.cleanup();

            return byteArray;

        }

        static TLRPC.Message deser(byte[] bArr) {

            if (bArr == null || bArr.length == 0) {

                return null;

            }

            SerializedData serializedData = new SerializedData(bArr);

            TLRPC.Message TLdeserialize = TLRPC.Message.TLdeserialize(serializedData, serializedData.readInt32(false), false);

            serializedData.cleanup();

            return TLdeserialize;

        }

        

        public static class EditItem {

            public final String text;

            public final long ts;

            public final int ver;

            public EditItem(int i, long j, String str) {

                this.ver = i;

                this.ts = j;

                this.text = str;

            }

        }

        

        

        public static class DbHelper extends SQLiteOpenHelper {

            DbHelper(Context context, String str) {

                super(context, str, (SQLiteDatabase.CursorFactory) null, 9);

            }

            @Override

            public void onConfigure(SQLiteDatabase db) {

                db.enableWriteAheadLogging();

                db.execSQL("PRAGMA foreign_keys=ON");

            }

            @Override // android.database.sqlite.SQLiteOpenHelper

            public void onCreate(SQLiteDatabase sQLiteDatabase) {

                sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS deleted_keys(did INTEGER NOT NULL,mid INTEGER NOT NULL,ts INTEGER NOT NULL,data TEXT,attachment_saved INTEGER NOT NULL DEFAULT 0,PRIMARY KEY(did,mid))");

                sQLiteDatabase.execSQL("CREATE INDEX IF NOT EXISTS idx_del_ts ON deleted_keys(did,ts)");

                sQLiteDatabase.execSQL("CREATE INDEX IF NOT EXISTS idx_del_did ON deleted_keys(did,mid)");

                sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS message_edits(did INTEGER NOT NULL,mid INTEGER NOT NULL,ver INTEGER NOT NULL,date INTEGER NOT NULL,data BLOB NOT NULL,PRIMARY KEY(did,mid,ver))");

                sQLiteDatabase.execSQL("CREATE INDEX IF NOT EXISTS idx_edits_did_mid ON message_edits(did,mid)");

                sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS exception_users(did INTEGER NOT NULL,exception_reading INTEGER NOT NULL DEFAULT 0,exception_typing INTEGER NOT NULL DEFAULT 0,PRIMARY KEY(did))");

                sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS regex_filters(regex TEXT NOT NULL,PRIMARY KEY(regex))");

            }

            @Override // android.database.sqlite.SQLiteOpenHelper

            public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {

                onCreate(sQLiteDatabase);

            }

        }

    }

    public static class ColorCircle extends View {

        public int color;

        public boolean isCustom;

        public boolean isDefault;

        private boolean isSelected;

        private Paint fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        private Paint strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        public ColorCircle(Context context, int color, boolean isCustom) {

            super(context);

            this.color = color;

            this.isCustom = isCustom;

            setWillNotDraw(false);

            strokePaint.setStyle(Paint.Style.STROKE);

            strokePaint.setStrokeWidth(AndroidUtilities.dp(2));

            setBackground(Theme.createSelectorDrawable(Theme.getColor(Theme.key_listSelector), 1));

        }

        public void setSelectedColor(boolean s) { this.isSelected = s; invalidate(); }

        @Override

        protected void onDraw(Canvas canvas) {

            int cx = getWidth() / 2;

            int cy = getHeight() / 2;

            int r = AndroidUtilities.dp(16);

            if (isCustom) {

                android.graphics.SweepGradient shader = new android.graphics.SweepGradient(cx, cy,

                    new int[]{0xFFE53935, 0xFFFFB300, 0xFF43A047, 0xFF1E88E5, 0xFF8E24AA, 0xFFE53935}, null);

                fillPaint.setShader(shader);

                canvas.drawCircle(cx, cy, r, fillPaint);

                fillPaint.setShader(null);

                if (color != 0) {

                    fillPaint.setColor(color);

                    canvas.drawCircle(cx, cy, r - AndroidUtilities.dp(3), fillPaint);

                }

            } else if (isDefault) {

                // tr("По умолчанию","Default") — серый кружок: цвет метки не переопределяется

                fillPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText));

                canvas.drawCircle(cx, cy, r, fillPaint);

            } else {

                fillPaint.setColor(color);

                canvas.drawCircle(cx, cy, r, fillPaint);

            }

            if (isSelected) {

                strokePaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));

                canvas.drawCircle(cx, cy, r + AndroidUtilities.dp(3), strokePaint);

            }

        }

    }

    public static class DeletedCustomizeFragment extends BaseFragment {

        // Кастомизация вида удалённых сообщений (перенесено из re:extera

        // CustomizationFragment): живое превью, свой префикс + эмодзи-метка,

        // выбор цвета (стандартные + произвольный), прозрачность со слайдером.

        private ChatMessageCell previewCell;

        private LinearLayout colorsLayout;

        private View customCircleView;

        private Runnable pendingPreview;

        private android.widget.Switch opacitySwitch;

        private SeekBar alphaSlider;

        private TextView alphaValue;

        @Override

        public View createView(Context context) {

            hasOwnBackground = true;

            Theme.createChatResources(context, false);

            // Как и в DeletedMessagesFragment (соседний экран, где верхняя область

            // отрисовывается корректно): actionBar добавляется как дочерний view

            // напрямую в content, а не через addToContainer(true). Раньше actionBar

            // рисовался отдельно от content, поэтому область под ним (высотой

            // actionBar+статус-бар) не перекрывалась ничем — SizeNotifierFrameLayout

            // намеренно не рисует фон обоев под этой зоной (см. isActionBarVisible()/

            // isStatusBarVisible() в оригинале Telegram), рассчитывая, что её закроет

            // сам actionBar. Отсюда был виден чёрный прямоугольник сверху экрана.

            actionBar.setAddToContainer(false);

            actionBar.setOccupyStatusBar(!AndroidUtilities.isTablet());

            actionBar.setBackButtonDrawable(new BackDrawable(false));

            actionBar.setAllowOverlayTitle(false);

            actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {

                @Override public void onItemClick(int id) { if (id == -1) finishFragment(); }

            });

            actionBar.setTitle(tr("\u0423\u0434\u0430\u043b\u0451\u043d\u043a\u0438", "Deleted messages"));

            SizeNotifierFrameLayout content = new SizeNotifierFrameLayout(context);

            content.setOccupyStatusBar(!AndroidUtilities.isTablet());

            content.setBackgroundImage(Theme.getCachedWallpaper(), Theme.isWallpaperMotion());

            int topInset = ActionBar.getCurrentActionBarHeight() + (actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0);

            ScrollView sv = new ScrollView(context);

            sv.setFillViewport(true);

            sv.setClipToPadding(false);

            sv.setPadding(0, topInset, 0, 0);

            LinearLayout root = new LinearLayout(context);

            root.setOrientation(LinearLayout.VERTICAL);

            int side = AndroidUtilities.dp(16);

            // Карточку превью НЕ красим сплошным фоном (как остальные card()) —

            // она должна оставаться прозрачной, чтобы сквозь неё было видно те же самые

            // обои, что и на всём экране (content.setBackgroundImage выше), без

            // отдельного слоя более низкого качества. См. buildPreview().

            FrameLayout previewCard = new FrameLayout(context);

            previewCard.addView(buildPreview(context), LayoutHelper.createFrame(-1, -2));

            root.addView(previewCard, LayoutHelper.createFrame(-1, -2, 0, 0, AndroidUtilities.dp(8), 0, 0));

            FrameLayout markCard = card(context);

            markCard.addView(buildMarkSection(context), LayoutHelper.createFrame(-1, -2));

            root.addView(markCard, LayoutHelper.createFrame(-1, -2, 0, side, AndroidUtilities.dp(4), side, 0));

            FrameLayout colorCard = card(context);

            colorCard.addView(buildColorSection(context), LayoutHelper.createFrame(-1, -2));

            root.addView(colorCard, LayoutHelper.createFrame(-1, -2, 0, side, AndroidUtilities.dp(4), side, 0));

            FrameLayout opCard = card(context);

            opCard.addView(buildOpacitySection(context), LayoutHelper.createFrame(-1, -2));

            root.addView(opCard, LayoutHelper.createFrame(-1, -2, 0, side, AndroidUtilities.dp(4), side, AndroidUtilities.dp(4)));

            FrameLayout clearCard = card(context);

            clearCard.addView(buildClearSection(context), LayoutHelper.createFrame(-1, -2));

            root.addView(clearCard, LayoutHelper.createFrame(-1, -2, 0, side, AndroidUtilities.dp(4), side, AndroidUtilities.dp(16)));

            applyAlphaToPreview();

            sv.addView(root, LayoutHelper.createFrame(-1, -2));

            content.addView(sv, LayoutHelper.createFrame(-1, -1));

            content.addView(actionBar);

            fragmentView = content;

            return content;

        }

        private FrameLayout card(Context ctx) {

            FrameLayout c = new FrameLayout(ctx);

            c.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));

            return c;

        }

        private FrameLayout buildPreview(Context ctx) {

            FrameLayout wrap = new FrameLayout(ctx);

            ChatMessageCell cell = new ChatMessageCell(ctx, UserConfig.selectedAccount);

            try {

                TLRPC.TL_message replyMsg = new TLRPC.TL_message();

                replyMsg.message = tr("\u041f\u0440\u0438\u0432\u0435\u0442, \u043a\u0430\u043a \u0434\u0435\u043b\u0430?", "Hi, how are you?");

                replyMsg.out = true; replyMsg.id = 2;

                TLRPC.TL_peerUser rf = new TLRPC.TL_peerUser();

                rf.user_id = UserConfig.getInstance(UserConfig.selectedAccount).getClientUserId();

                replyMsg.from_id = rf;

                MessageObject replyMO = new MessageObject(UserConfig.selectedAccount, replyMsg, true, false);

                TLRPC.TL_message msg = new TLRPC.TL_message();

                msg.message = tr("\u042d\u0442\u043e \u0441\u043e\u043e\u0431\u0449\u0435\u043d\u0438\u0435 \u0431\u044b\u043b\u043e \u0443\u0434\u0430\u043b\u0435\u043d\u043e", "This message was deleted");

                msg.date = (int) (System.currentTimeMillis() / 1000);

                msg.dialog_id = -999999999L; msg.flags = 259 | 8;

                TLRPC.TL_peerUser fr = new TLRPC.TL_peerUser();

                fr.user_id = UserConfig.getInstance(UserConfig.selectedAccount).getClientUserId(); msg.from_id = fr; msg.id = 1;

                msg.media = new TLRPC.TL_messageMediaEmpty(); msg.out = true;

                TLRPC.TL_messageReplyHeader rh = new TLRPC.TL_messageReplyHeader();

                rh.reply_to_msg_id = 2; msg.reply_to = rh;

                TLRPC.TL_peerUser pr = new TLRPC.TL_peerUser();

                pr.user_id = -999999999L; msg.peer_id = pr;

                MessageObject mo = new MessageObject(UserConfig.selectedAccount, msg, true, false);

                mo.deleted = true; mo.eventId = 1;

                mo.customName = tr("\u041f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u0442\u0435\u043b\u044c", "User");

                mo.customReplyName = tr("\u0414\u0440\u0443\u0433", "Friend");

                mo.replyMessageObject = replyMO; mo.resetLayout();

                cell.isChat = true; cell.setFullyDraw(true);

                cell.setMessageObject(mo, null, false, false, false);

            } catch (Exception e) {}

            // Раньше здесь ставился отдельный Drawable через getCachedWallpaperNonBlocking() —

            // он ощутимо ниже качеством и сжат по высоте относительно фона всего экрана

            // (getCachedWallpaper()), из-за чего был виден шов между превью и остальным

            // экраном. Теперь wrap остаётся прозрачным (фон не выставляется вовсе), и сквозь

            // него, и сквозь прозрачную previewCard в createView(), виден один и тот же

            // полноценный фон content — переход исчезает полностью.

            wrap.addView(cell, LayoutHelper.createFrame(-1, -2, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 8, 8, 8, 8));

            previewCell = cell;

            return wrap;

        }

        // Секция tr("метка","label"): своё текстовое слово + кнопка выбора эмодзи (короткое

        // нажатие — выбор, долгое — сброс). Если и то и другое пусто — значок корзины.

        private View buildMarkSection(Context ctx) {

            LinearLayout sec = new LinearLayout(ctx);

            sec.setOrientation(LinearLayout.VERTICAL);

            int pad = AndroidUtilities.dp(12);

            sec.setPadding(pad, pad, pad, AndroidUtilities.dp(6));

            TextView hdr = new TextView(ctx);

            hdr.setText(tr("\u041c\u0435\u0442\u043a\u0430 \u0443\u0434\u0430\u043b\u0451\u043d\u043d\u043e\u0433\u043e", "Deleted mark"));

            hdr.setTextSize(16);

            hdr.setTypeface(null, android.graphics.Typeface.BOLD);

            hdr.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2));

            sec.addView(hdr);

            LinearLayout row = new LinearLayout(ctx);

            row.setOrientation(LinearLayout.HORIZONTAL);

            row.setGravity(Gravity.CENTER_VERTICAL);

            row.setPadding(0, AndroidUtilities.dp(4), 0, AndroidUtilities.dp(6));

            EditTextBoldCursor inp = new EditTextBoldCursor(ctx);

            inp.setText(HookManager.deletedMarkText());

            inp.setHint(tr("\u041f\u0443\u0441\u0442\u043e \u2014 \u0437\u043d\u0430\u0447\u043e\u043a \u043a\u043e\u0440\u0437\u0438\u043d\u044b", "Empty \u2014 trash icon"));

            inp.setSingleLine(true);

            inp.setTextSize(17);

            inp.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));

            inp.setHintTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteHintText));

            inp.setBackgroundDrawable(null);

            inp.setLineColors(Theme.getColor(Theme.key_windowBackgroundWhiteInputField), Theme.getColor(Theme.key_windowBackgroundWhiteInputFieldActivated), Theme.getColor(Theme.key_text_RedRegular));

            row.addView(inp, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));

            inp.addTextChangedListener(new TextWatcher() {

                @Override

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override

                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    try {

                        Main.cfg.prefs().edit().putString("del_custom_label", s != null ? s.toString() : "").apply();

                        refreshPreview();

                    } catch (Exception e) {

                    }

                }

                @Override

                public void afterTextChanged(Editable s) {}

            });

            TextView clearBtn = new TextView(ctx);

            clearBtn.setText(tr("\u0421\u0431\u0440\u043e\u0441\u0438\u0442\u044c", "Reset"));

            clearBtn.setTextSize(15);

            clearBtn.setPadding(AndroidUtilities.dp(12), AndroidUtilities.dp(8), AndroidUtilities.dp(12), AndroidUtilities.dp(8));

            clearBtn.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlueText));

            clearBtn.setOnClickListener(v -> {

                inp.setText("");

                Main.cfg.prefs().edit().putLong("del_mark_emoji_id", 0L).apply();

                refreshPreview();

            });

            row.addView(clearBtn, LayoutHelper.createLinear(-2, -2, Gravity.CENTER_VERTICAL, 4, 0, 0, 0));

            sec.addView(row);

            return sec;

        }

        // Цвет метки: tr("по умолчанию","default") (цвет темы), 5 стандартных и произвольный

        // (ColorPicker). Пишется в del_mark_color (int, 0 = по умолчанию).

        private View buildColorSection(Context ctx) {

            LinearLayout sec = new LinearLayout(ctx);

            sec.setOrientation(LinearLayout.VERTICAL);

            int pad = AndroidUtilities.dp(12);

            sec.setPadding(pad, pad, pad, pad);

            TextView hdr = new TextView(ctx);

            hdr.setText(tr("\u0426\u0432\u0435\u0442 \u043c\u0435\u0442\u043a\u0438", "Mark color"));

            hdr.setTextSize(16);

            hdr.setTypeface(null, android.graphics.Typeface.BOLD);

            hdr.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2));

            sec.addView(hdr);

            colorsLayout = new LinearLayout(ctx);

            colorsLayout.setOrientation(LinearLayout.HORIZONTAL);

            colorsLayout.setGravity(Gravity.CENTER_VERTICAL);

            colorsLayout.setPadding(0, AndroidUtilities.dp(4), 0, 0);

            int[] palette = {0, 0xFFE53935, 0xFF1E88E5, 0xFFFFB300, 0xFF43A047, 0xFF8E24AA};

            for (int c : palette) {

                final boolean isDefault = c == 0;

                ColorCircle circle = new ColorCircle(ctx, c, false);

                circle.isDefault = isDefault;

                colorsLayout.addView(circle, LayoutHelper.createLinear(40, 40, 0, 0, 8, 0));

                circle.setOnClickListener(v -> {

                    Main.cfg.set("del_mark_color", isDefault ? 0 : c);

                    refreshColors();

                    refreshPreview();

                });

            }

            ColorCircle custom = new ColorCircle(ctx, 0, true);

            customCircleView = custom;

            colorsLayout.addView(custom, LayoutHelper.createLinear(40, 40, 0, 0, 8, 0));

            custom.setOnClickListener(v -> {

                int current = Main.cfg.i("del_mark_custom_color", 0);

                if (current != 0 && Main.cfg.i("del_mark_color", 0) == current) {

                    showColorPickerDialog(current);

                } else if (current != 0) {

                    Main.cfg.set("del_mark_color", current);

                    refreshColors();

                    refreshPreview();

                } else {

                    showColorPickerDialog(Main.cfg.i("del_mark_color", 0));

                }

            });

            HorizontalScrollView hsv = new HorizontalScrollView(ctx);

            hsv.setHorizontalScrollBarEnabled(false);

            hsv.addView(colorsLayout);

            sec.addView(hsv);

            refreshColors();

            return sec;

        }

        private void showColorPickerDialog(int initialColor) {

            try {

                Context c = getParentActivity(); if (c == null) return;

                final int[] selectedColor = {initialColor == 0 ? 0xFFF5365A : initialColor};

                org.telegram.ui.ActionBar.AlertDialog.Builder b = new org.telegram.ui.ActionBar.AlertDialog.Builder(c);

                b.setTitle(tr("\u0426\u0432\u0435\u0442", "Color"));

                LinearLayout container = new LinearLayout(c);

                container.setOrientation(LinearLayout.VERTICAL);

                container.setPadding(AndroidUtilities.dp(16), AndroidUtilities.dp(8), AndroidUtilities.dp(16), AndroidUtilities.dp(8));

                ColorPicker colorPicker = new ColorPicker(c, false, new ColorPicker.ColorPickerDelegate() {

                    @Override

                    public void setColor(int color, int state, boolean notify) {

                        selectedColor[0] = color;

                        Main.cfg.set("del_mark_color", color);

                        refreshPreview();

                    }

                });

                container.addView(colorPicker, LayoutHelper.createFrame(-1, 240));

                try {

                    selectedColor[0] = colorPicker.getColor();

                } catch (Exception ignored) {}

                b.setView(container);

                b.setPositiveButton(tr("OK", "OK"), (d, w) -> {

                    Main.cfg.set("del_mark_custom_color", selectedColor[0]);

                    Main.cfg.set("del_mark_color", selectedColor[0]);

                    refreshColors();

                    refreshPreview();

                });

                b.setNegativeButton(tr("\u041e\u0442\u043c\u0435\u043d\u0430", "Cancel"), (d, w) -> {

                    Main.cfg.set("del_mark_color", initialColor);

                    refreshColors();

                    refreshPreview();

                });

                showDialog(b.create());

            } catch (Exception e) {

            }

        }

        private void refreshColors() {

            if (colorsLayout == null) return;

            int sel = Main.cfg.i("del_mark_color", 0);

            int custom = Main.cfg.i("del_mark_custom_color", 0);

            for (int i = 0; i < colorsLayout.getChildCount(); i++) {

                View ch = colorsLayout.getChildAt(i);

                if (ch instanceof ColorCircle) {

                    ColorCircle cc = (ColorCircle) ch;

                    if (cc.isCustom) {

                        cc.color = custom;

                        cc.setSelectedColor(sel != 0 && sel == custom);

                    } else {

                        cc.setSelectedColor(sel == cc.color);

                    }

                    cc.invalidate();

                }

            }

        }

        // Прозрачность: переключатель + слайдер 10–100%.

        private View buildOpacitySection(Context ctx) {

            LinearLayout sec = new LinearLayout(ctx);

            sec.setOrientation(LinearLayout.VERTICAL);

            int pad = AndroidUtilities.dp(12);

            sec.setPadding(pad, pad, pad, pad);

            LinearLayout headerRow = new LinearLayout(ctx);

            headerRow.setOrientation(LinearLayout.HORIZONTAL);

            headerRow.setGravity(Gravity.CENTER_VERTICAL);

            TextView hdr = new TextView(ctx);

            hdr.setText(tr("\u041f\u0440\u043e\u0437\u0440\u0430\u0447\u043d\u043e\u0441\u0442\u044c", "Opacity"));

            hdr.setTextSize(16);

            hdr.setTypeface(null, android.graphics.Typeface.BOLD);

            hdr.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2));

            headerRow.addView(hdr, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));

            alphaValue = new TextView(ctx);

            alphaValue.setTextSize(15);

            alphaValue.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlueText));

            headerRow.addView(alphaValue);

            sec.addView(headerRow);

            opacitySwitch = new android.widget.Switch(ctx);

            opacitySwitch.setChecked(Main.cfg.b("del_show_opacity", true));

            LinearLayout switchRow = new LinearLayout(ctx);

            switchRow.setOrientation(LinearLayout.HORIZONTAL);

            switchRow.setGravity(Gravity.CENTER_VERTICAL);

            TextView swLabel = new TextView(ctx);

            swLabel.setText(tr("\u041f\u043e\u043b\u0443\u043f\u0440\u043e\u0437\u0440\u0430\u0447\u043d\u044b\u0435 \u0443\u0434\u0430\u043b\u0451\u043d\u043a\u0438", "Semi-transparent deleted"));

            swLabel.setTextSize(16);

            swLabel.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));

            switchRow.addView(swLabel, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));

            switchRow.addView(opacitySwitch);

            opacitySwitch.setOnCheckedChangeListener((btn, checked) -> {

                Main.cfg.set("del_show_opacity", checked);

                HookManager.invalidateDelOpCache();

                if (alphaSlider != null) alphaSlider.setEnabled(checked);

                applyAlphaToPreview();

            });

            sec.addView(switchRow);

            int level = Main.cfg.i("del_opacity_level", 60);

            if (level >= 0 && level <= 4) {

                level = (int) (new float[]{100f, 80f, 60f, 40f, 20f}[level]);

                Main.cfg.set("del_opacity_level", level);

            }

            alphaSlider = new SeekBar(ctx);

            alphaSlider.setMax(90);

            alphaSlider.setProgress(Math.max(0, Math.min(90, level - 10)));

            alphaSlider.setEnabled(Main.cfg.b("del_show_opacity", true));

            alphaSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override

                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    if (alphaValue != null) alphaValue.setText((progress + 10) + "%");

                    if (fromUser) {

                        Main.cfg.set("del_opacity_level", progress + 10);

                        HookManager.invalidateDelOpCache();

                        applyAlphaToPreview();

                    }

                }

                @Override

                public void onStartTrackingTouch(SeekBar seekBar) {}

                @Override

                public void onStopTrackingTouch(SeekBar seekBar) {}

            });

            alphaValue.setText(level + "%");

            LinearLayout sliderRow = new LinearLayout(ctx);

            sliderRow.setOrientation(LinearLayout.HORIZONTAL);

            sliderRow.setGravity(Gravity.CENTER_VERTICAL);

            TextView lo = new TextView(ctx);

            lo.setText("10%");

            lo.setTextSize(13);

            lo.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText));

            TextView hi = new TextView(ctx);

            hi.setText("100%");

            hi.setTextSize(13);

            hi.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText));

            sliderRow.addView(lo);

            sliderRow.addView(alphaSlider, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));

            sliderRow.addView(hi);

            sec.addView(sliderRow);

            return sec;

        }

        private LinearLayout buildClearSection(final Context ctx) {
            LinearLayout sec = new LinearLayout(ctx);
            sec.setOrientation(LinearLayout.VERTICAL);
            int padH = AndroidUtilities.dp(16);
            int padV = AndroidUtilities.dp(12);
            sec.setPadding(padH, padV, padH, padV);

            TextView title = new TextView(ctx);
            title.setText(tr("Очистить базу удалённых", "Clear deleted database"));
            title.setTextSize(16);
            title.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            title.setTextColor(Theme.getColor(Theme.key_text_RedBold));
            sec.addView(title);

            TextView sub = new TextView(ctx);
            sub.setText(tr("Сбросить базу сохранённых удалённых сообщений и устранить ложные срабатывания", "Reset saved deleted messages database and fix false positives"));
            sub.setTextSize(13);
            sub.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText));
            sub.setPadding(0, AndroidUtilities.dp(4), 0, 0);
            sec.addView(sub);

            sec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    org.telegram.ui.ActionBar.AlertDialog.Builder builder = new org.telegram.ui.ActionBar.AlertDialog.Builder(getParentActivity());
                    builder.setTitle(tr("Очистка базы", "Clear database"));
                    builder.setMessage(tr("Вы уверены, что хотите полностью очистить базу сохранённых удалённых сообщений? Все текущие метки будут сброшены.", "Are you sure you want to completely clear the saved deleted messages database? All current marks will be reset."));
                    builder.setPositiveButton(tr("Очистить", "Clear"), (d, w) -> {
                        Main.clearAllDeletedData(() -> {
                            refreshPreview();
                            BulletinFactory.of(DeletedCustomizeFragment.this).createSuccessBulletin(tr("База удалённых сообщений успешно очищена", "Deleted messages database cleared")).show();
                        });
                    });
                    builder.setNegativeButton(tr("Отмена", "Cancel"), null);
                    showDialog(builder.create());
                }
            });

            return sec;
        }

        private void applyAlphaToPreview() {

            if (previewCell == null) return;

            HookManager.invalidateDelOpCache();

            if (Main.cfg.b("del_show_opacity", true)) {

                previewCell.setTransitionAlpha(HookManager.access$3600());

            } else {

                previewCell.setTransitionAlpha(1.0f);

            }

            previewCell.invalidate();

        }

        private void refreshPreview() {

            if (pendingPreview != null && H != null) H.removeCallbacks(pendingPreview);

            pendingPreview = () -> {

                if (previewCell != null && previewCell.getMessageObject() != null) {

                    try {

                        // Сбрасываем кэш прозрачности (обычно живёт 2 сек), чтобы предпросмотр

                        // отражал выбранное значение прозрачности мгновенно.

                        HookManager.invalidateDelOpCache();

                        MessageObject mo = previewCell.getMessageObject();

                        mo.forceUpdate = true;

                        // setMessageObject с forceUpdate=true пересоздаёт currentTimeString

                        // tr("с нуля","from scratch") из даты сообщения, а hkMeasureTime вставит в неё

                        // метку с текущими настройками (префикс/эмодзи/значок/цвет).

                        previewCell.setMessageObject(mo, null, false, false, false);

                        applyAlphaToPreview();

                        previewCell.invalidate();

                    } catch (Exception ignored) {}

                }

            };

            if (H != null) {

                H.post(pendingPreview);

            }

        }

        @Override

        public void onFragmentDestroy() {

            if (fragmentView != null) {

                try {

                    fragmentView.clearFocus();

                    AndroidUtilities.hideKeyboard(fragmentView);

                } catch (Exception e) {

                }

            }

            super.onFragmentDestroy();

        }

    }

    public static void showDeletedCustomize() {

        Runnable r = () -> {

            try {

                BaseFragment last = LaunchActivity.getLastFragment();

                if (last == null) {

                    android.widget.Toast.makeText(ApplicationLoader.applicationContext, "DelCust: lastFragment=null", android.widget.Toast.LENGTH_LONG).show();

                    return;

                }

                last.presentFragment(new DeletedCustomizeFragment());

            } catch (Exception e) {

                android.widget.Toast.makeText(ApplicationLoader.applicationContext, "DelCust err: " + e.getMessage(), android.widget.Toast.LENGTH_LONG).show();

                Log.e(TAG, "[showDelCust] " + e.getMessage(), e);

            }

        };

        if (H != null) {

            if (Looper.myLooper() == Looper.getMainLooper()) {

                r.run();

            } else {

                H.post(r);

            }

        } else {

            android.widget.Toast.makeText(ApplicationLoader.applicationContext, "DelCust: H is null", android.widget.Toast.LENGTH_LONG).show();

        }

    }

    public static void copyError(String text) {

        try {

            android.content.ClipboardManager cm = (android.content.ClipboardManager) ApplicationLoader.applicationContext.getSystemService("clipboard");

            cm.setPrimaryClip(android.content.ClipData.newPlainText("error", text));

        } catch (Exception e) {}

    }

    

    public static class HookManager {

        private static final int SETTINGS_ENTRY_ID = 889100;

        private static Field fTs;

        private static Field fTw;

        private static Field fW;

        private static Field fTp;

        private XC_MethodHook.Unhook sendReqHook;

        private static final Set<String> READ = mSet("TL_messages_readHistory", "TL_messages_readEncryptedHistory", "TL_messages_readDiscussion", "TL_channels_readHistory", "TL_messages_readMessageContents", "TL_channels_readMessageContents", "TL_messages_markDialogUnread");

        private static final Set<String> TYPE = mSet("TL_messages_setTyping", "TL_messages_setEncryptedTyping");

        private static final Set<String> STORY = mSet("TL_stories_readStories", "TL_stories_incrementStoryViews");

        private static final Set<String> SEND = mSet("TL_messages_sendMessage", "TL_messages_sendMedia", "TL_messages_sendMultiMedia");

        private static boolean fInit = false;

        private static final Set<Integer> deletedCellIds = ConcurrentHashMap.newKeySet();

        private static final Map<Long, MessageObject> pendingDel = new ConcurrentHashMap();

        private static volatile boolean insideStamp = false;

        private static float cachedDelOp = 0.6f;

        private static long cachedDelOpTs = 0;

        private static volatile boolean settingsErrored = false;

        private final ArrayList<XC_MethodHook.Unhook> hooks = new ArrayList<>();

        private ArrayList<Pattern> rxCache = new ArrayList<>();

        private final NflSelector nflSelector = new NflSelector();

        private final ArrayList<FilterRule> rxRules = new ArrayList<>();

        private static Set<String> mSet(Object... args) {

            HashSet<String> s = new HashSet<>(args.length);

            for (Object o : args) s.add((String) o);

            return Collections.unmodifiableSet(s);

        }

        private static Set<String> mSet2(Object... args) {

            return mSet(args);

        }

        private static Set<String> mSet3(Object... args) {

            return mSet(args);

        }

        private static Set<String> mSet4(Object... args) {

            return mSet(args);

        }

        static float access$3600() {

            return delOp();

        }

        public void init() {

            try {

                rebuildRegex();

                go();

            } catch (Exception e) {

                Main.err("hkInit", e);

            }

        }

        public void rebuildRegex() {

            ArrayList<Pattern> arrayList = new ArrayList<>();

            if (Main.db != null) {

                Iterator<String> it = Main.db.getRegex().iterator();

                while (it.hasNext()) {

                    try {

                        arrayList.add(Pattern.compile(it.next(), 10));

                    } catch (Exception e) {

                    }

                }

            }

            this.rxCache = arrayList;

        }

        

        

        public static class FilterRule {

            boolean channelOnly;

            boolean inverted;

            Pattern pattern;

            private FilterRule() {

            }

        }

        public void handleSyncFilters(String str) {

            try {

                synchronized (this.rxRules) {

                    this.rxRules.clear();

                    if (str != null && !str.isEmpty()) {

                        JSONArray jSONArray = new JSONArray(str);

                        for (int i = 0; i < jSONArray.length(); i++) {

                            try {

                                JSONObject jSONObject = jSONArray.getJSONObject(i);

                                if (jSONObject.optBoolean("enabled", true)) {

                                    String optString = jSONObject.optString("expression", "");

                                    if (!optString.isEmpty()) {

                                        Pattern compile = Pattern.compile(optString, 2);

                                        FilterRule filterRule = new FilterRule();

                                        filterRule.pattern = compile;

                                        filterRule.inverted = jSONObject.optBoolean("inverted", false);

                                        filterRule.channelOnly = jSONObject.optBoolean("channels_only", false);

                                        this.rxRules.add(filterRule);

                                    }

                                }

                            } catch (Exception e) {

                            }

                        }

                    }

                }

            } catch (Exception e2) {

                Main.err("syncF", e2);

            }

        }

        

        public boolean isCurrentChannel() {

            TLRPC.Chat chat;

            try {

                long currentDialogId = getCurrentDialogId();

                if (currentDialogId < 0 && (chat = MessagesController.getInstance(UserConfig.selectedAccount).getChat(Long.valueOf(-currentDialogId))) != null) {

                    return ChatObject.isChannel(chat);

                }

                return false;

            } catch (Exception e) {

                return false;

            }

        }

        private void hkChatMenu3dot() {

            try {

                this.hooks.add(XposedBridge.hookMethod(ChatActivity.class.getDeclaredMethod("createView", Context.class), new XC_MethodHook() {

                    public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {

                        try {

                            HookManager.inject3DotMenu((ChatActivity) methodHookParam.thisObject);

                        } catch (Throwable th) {

                        }

                    }

                }));

            } catch (Exception e) {

                Main.err("hk3dot", e);

            }

            try {

                this.hooks.add(XposedBridge.hookMethod(ChatActivity.class.getDeclaredMethod("onResume", new Class[0]), new XC_MethodHook() {

                    public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {

                        try {

                            ChatActivity chatActivity = (ChatActivity) methodHookParam.thisObject;

                            if (Main.injectedMenus.get(chatActivity) == null) {

                                HookManager.inject3DotMenu(chatActivity);

                            }

                        } catch (Throwable th) {

                        }

                    }

                }));

            } catch (Exception e) {

                Main.err("hk3dotResume", e);

            }

        }

        

        public static void inject3DotMenu(final ChatActivity chatActivity) {

            Object obj = null;

            Theme.ResourcesProvider resourcesProvider = null;

            int i = 0;

            String string = "";

            try {

                if (Main.injectedMenus.put(chatActivity, true) != null) {

                    return;

                }

                Field declaredField = ChatActivity.class.getDeclaredField("headerItem");

                declaredField.setAccessible(true);

                Object obj2 = declaredField.get(chatActivity);

                if (!(obj2 instanceof ActionBarMenuItem)) {

                    Main.injectedMenus.remove(chatActivity);

                    return;

                }

                final ActionBarMenuItem actionBarMenuItem = (ActionBarMenuItem) obj2;

                for (int ci = 0; ci < actionBarMenuItem.getChildCount(); ci++) {

                    View child = actionBarMenuItem.getChildAt(ci);

                    if (child instanceof ActionBarMenuSubItem) {

                        for (int ti = 0; ti < ((ViewGroup) child).getChildCount(); ti++) {

                            if (((ViewGroup) child).getChildAt(ti) instanceof TextView) {

                                CharSequence t = ((TextView) ((ViewGroup) child).getChildAt(ti)).getText();

                                if (t != null && t.toString().contains("ExteraMax")) {

                                    Main.injectedMenus.remove(chatActivity);

                                    Main.injectedMenuIds.remove(Long.valueOf(chatActivity.getDialogId()));

                                    return;

                                }

                            }

                        }

                    }

                }

                try {

                    Object invoke = ActionBarMenuItem.class.getMethod("getPopupLayout", new Class[0]).invoke(actionBarMenuItem, new Object[0]);

                    Object result = invoke != null ? invoke.getClass().getMethod("getSwipeBack", new Class[0]).invoke(invoke, new Object[0]) : null;

                    obj = result;

                } catch (Throwable th) {

                    obj = null;

                }

                try {

                    resourcesProvider = chatActivity.getResourceProvider();

                } catch (Throwable th2) {

                    resourcesProvider = null;

                }

                Class<?> cls = Class.forName("org.telegram.ui.ActionBar.ActionBarPopupWindow$ActionBarPopupWindowLayout");

                Object newInstance = cls.getConstructor(Context.class, Integer.TYPE, Theme.ResourcesProvider.class).newInstance(chatActivity.getContext(), 0, resourcesProvider);

                try {

                    cls.getMethod("setFitItems", Boolean.TYPE).invoke(newInstance, true);

                } catch (Throwable th3) {

                }

                ViewGroup viewGroup = (ViewGroup) newInstance;

                if (obj != null) {

                    final Object swipeBack = obj;

                    try {

                        string = LocaleController.getString("Back", R.string.Back);

                    } catch (Throwable th4) {

                    }

                    ActionBarMenuItem.addItem(viewGroup, R.drawable.msg_arrow_back, string, false, resourcesProvider).setOnClickListener(new View.OnClickListener() {

                        @Override

                        public final void onClick(View view) {

                            HookManager.lambda$inject3DotMenu$0(swipeBack, view);

                        }

                    });

                }

                try {

                    i = R.drawable.class.getField("msg_eye").getInt(null);

                } catch (Throwable th5) {

                    try {

                        i = R.drawable.class.getField("msg_document").getInt(null);

                    } catch (Throwable th6) {

                        i = 0;

                    }

                }

                ActionBarMenuItem.addItem(viewGroup, R.drawable.msg_archive_hide, tr("Посмотреть удалёнки","View deleted"), false, resourcesProvider).setOnClickListener(new View.OnClickListener() {

                    @Override

                    public final void onClick(View view) {

                        HookManager.lambda$inject3DotMenu$1(actionBarMenuItem, view);

                    }

                });

                ActionBarMenuItem.addItem(viewGroup, R.drawable.msg_delete_filled, tr("Очистить удалёнки","Clear deleted"), false, resourcesProvider).setOnClickListener(new View.OnClickListener() {

                    @Override

                    public final void onClick(View view) {

                        HookManager.lambda$inject3DotMenu$2(actionBarMenuItem, chatActivity, view);

                    }

                });

                ActionBarMenuItem.addItem(viewGroup, R.drawable.msg_delete, "Очистить ВСЕ удалёнки", false, resourcesProvider).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public final void onClick(View view) {
                        try {
                            actionBarMenuItem.closeSubMenu();
                            HookManager.confirmPurgeAll3dot(chatActivity);
                        } catch (Throwable ignored) {}
                    }
                });

                ActionBarMenuItem.addItem(viewGroup, R.drawable.msg_retry, tr("Синхронизировать чат","Sync chat"), false, resourcesProvider).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public final void onClick(View view) {
                        try {
                            actionBarMenuItem.closeSubMenu();
                            Main.reloadChatFromCloud(chatActivity);
                        } catch (Throwable ignored) {}
                    }
                });

                ActionBarMenuItem.addItem(viewGroup, R.drawable.msg_retry, tr("Синхронизировать все чаты","Sync all chats"), false, resourcesProvider).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public final void onClick(View view) {
                        try {
                            actionBarMenuItem.closeSubMenu();
                            Main.syncAllChatsFromCloud(null);
                        } catch (Throwable ignored) {}
                    }
                });

                final ActionBarMenuSubItem addSwipeBackItem = actionBarMenuItem.addSwipeBackItem(i, (Drawable) null, "ExteraMax", (View) newInstance);

                addSwipeBackItem.setOnClickListener(new View.OnClickListener() {

                    @Override

                    public final void onClick(View view) {

                        HookManager.lambda$inject3DotMenu$3(addSwipeBackItem, view);

                    }

                });

            } catch (Throwable th7) {

                Main.err("inj3dot", new Exception(th7));

            }

        }

        static void lambda$inject3DotMenu$0(Object obj, View view) {

            try {

                ((PopupSwipeBackLayout) obj).closeForeground();

            } catch (Throwable th) {

            }

        }

        static void lambda$inject3DotMenu$1(ActionBarMenuItem actionBarMenuItem, View view) {

            try {

                actionBarMenuItem.closeSubMenu();

                Main.showDeletedList();

            } catch (Throwable th) {

            }

        }

        static void lambda$inject3DotMenu$2(ActionBarMenuItem actionBarMenuItem, ChatActivity chatActivity, View view) {

            try {

                actionBarMenuItem.closeSubMenu();

                confirmPurge3dot(chatActivity);

            } catch (Throwable th) {

            }

        }

        static void lambda$inject3DotMenu$3(ActionBarMenuSubItem actionBarMenuSubItem, View view) {

            try {

                actionBarMenuSubItem.openSwipeBack();

            } catch (Throwable th) {

            }

        }

        private static void confirmPurge3dot(ChatActivity chatActivity) {

            long dialogRef;

            try {

                Field declaredField = ChatActivity.class.getDeclaredField("dialog_id");

                declaredField.setAccessible(true);

                dialogRef = declaredField.getLong(chatActivity);

            } catch (Exception e) {

                dialogRef = 0;

            }

            final long j = dialogRef;

            if (j == 0) {

                return;

            }

try {

                Context context = chatActivity.getContext();

                if (context == null) {

                    return;

                }

                org.telegram.ui.ActionBar.AlertDialog.Builder builder = new org.telegram.ui.ActionBar.AlertDialog.Builder(context);

                builder.setTitle("ExteraMax");

                builder.setMessage(tr("Удалить все сохранённые удалённые сообщения в этом чате?","Delete all saved deleted messages in this chat?"));

                builder.setPositiveButton(tr("Удалить","Delete"), (dialog, which) -> {

                    try {

                        dialog.dismiss();

                        Main.purgeChatDeleted(j);

                        Main.refreshCells();

                    } catch (Exception e2) {}

                });

                builder.setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss());

                chatActivity.showDialog(builder.create());

            } catch (Exception e2) {

                Main.err("purge3d", e2);

            }

        }

        private static void confirmPurgeAll3dot(final ChatActivity chatActivity) {
            try {
                Context context = chatActivity.getContext();
                if (context == null) return;
                org.telegram.ui.ActionBar.AlertDialog.Builder builder = new org.telegram.ui.ActionBar.AlertDialog.Builder(context);
                builder.setTitle("ExteraMax");
                builder.setMessage("Вы уверены, что хотите полностью очистить данные обо ВСЕХ сохранённых удалённых сообщениях во всех чатах?");
                builder.setPositiveButton("Очистить всё", (dialog, which) -> {
                    try {
                        dialog.dismiss();
                        Main.clearAllDeletedData(() -> {
                            try {
                                BulletinFactory.of(chatActivity).createSuccessBulletin(tr("Все сохранённые удалёнки очищены","All saved deleted cleared")).show();
                            } catch (Throwable ignored) {}
                        });
                    } catch (Exception ignored) {}
                });
                builder.setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss());
                chatActivity.showDialog(builder.create());
            } catch (Exception e) {
                Main.err("purgeAll3d", e);
            }
        }

                // ==================== Hooks: Shadowban, Filters, GifReupload ====================
        private void hkShadowban() {
            try {
                // MessagesController.getDialogs(int folderId)
                Method mGetDialogs = MessagesController.class.getDeclaredMethod("getDialogs", Integer.TYPE);
                mGetDialogs.setAccessible(true);
                this.hooks.add(XposedBridge.hookMethod(mGetDialogs, new XC_MethodHook() {
                    @Override
                    public void afterHookedMethod(MethodHookParam param) {
                        try {
                            if (ShadowbanStore.isEmpty()) return;
                            Object res = param.getResult();
                            if (res instanceof ArrayList) {
                                ArrayList list = (ArrayList) res;
                                for (int i = list.size() - 1; i >= 0; i--) {
                                    Object o = list.get(i);
                                    if (o instanceof TLRPC.Dialog) {
                                        TLRPC.Dialog d = (TLRPC.Dialog) o;
                                        if (ShadowbanStore.hidesDialog(d.id)) {
                                            list.remove(i);
                                        }
                                    }
                                }
                            }
                        } catch (Throwable t) {}
                    }
                }));

                // NotificationsController.processNewMessages
                for (Method m : NotificationsController.class.getDeclaredMethods()) {
                    if ("processNewMessages".equals(m.getName())) {
                        m.setAccessible(true);
                        this.hooks.add(XposedBridge.hookMethod(m, new XC_MethodHook() {
                            @Override
                            public void beforeHookedMethod(MethodHookParam param) {
                                try {
                                    if (ShadowbanStore.isEmpty()) return;
                                    Object arg0 = param.args[0];
                                    if (arg0 instanceof ArrayList) {
                                        ArrayList list = (ArrayList) arg0;
                                        for (int i = list.size() - 1; i >= 0; i--) {
                                            Object o = list.get(i);
                                            if (o instanceof MessageObject) {
                                                MessageObject mo = (MessageObject) o;
                                                if (!mo.isOut()) {
                                                    long from = mo.getFromChatId();
                                                    if (ShadowbanStore.hidesInGroups(from) || (mo.getDialogId() > 0 && ShadowbanStore.hidesDialog(from))) {
                                                        list.remove(i);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } catch (Throwable t) {}
                            }
                        }));
                        break;
                    }
                }

                // ProfileActivity.createActionBarMenu
                for (Method m : ProfileActivity.class.getDeclaredMethods()) {
                    if ("createActionBarMenu".equals(m.getName())) {
                        m.setAccessible(true);
                        this.hooks.add(XposedBridge.hookMethod(m, new XC_MethodHook() {
                            @Override
                            public void afterHookedMethod(MethodHookParam param) {
                                try {
                                    final ProfileActivity profile = (ProfileActivity) param.thisObject;
                                    Field fUserId = ProfileActivity.class.getDeclaredField("user_id");
                                    fUserId.setAccessible(true);
                                    final long userId = fUserId.getLong(profile);
                                    if (userId <= 0) return;

                                    Field fOtherItem = ProfileActivity.class.getDeclaredField("otherItem");
                                    fOtherItem.setAccessible(true);
                                    final org.telegram.ui.ActionBar.ActionBarMenuItem otherItem = (org.telegram.ui.ActionBar.ActionBarMenuItem) fOtherItem.get(profile);
                                    if (otherItem == null) return;

                                    final boolean isBanned = ShadowbanStore.map.containsKey(Long.valueOf(userId));
                                    String title = isBanned ? tr("Убрать из теневого бана","Remove from shadowban") : tr("В теневой бан","Shadowban");
                                    int icon = isBanned ? R.drawable.msg_block2 : R.drawable.msg_block;

                                    otherItem.addSubItem(889301, icon, title).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            otherItem.closeSubMenu();
                                            if (isBanned) {
                                                ShadowbanStore.remove(userId);
                                                BaseFragment frag = LaunchActivity.getLastFragment();
                                                if (frag != null) {
                                                    BulletinFactory.of(frag).createSuccessBulletin(tr("Убран из теневого бана","Removed from shadowban")).show();
                                                }
                                            } else {
                                                showShadowbanDialog(profile.getParentActivity(), userId);
                                            }
                                        }
                                    });
                                } catch (Throwable t) {}
                            }
                        }));
                        break;
                    }
                }
            } catch (Throwable t) {
                Main.err("hkShadowban", new Exception(t));
            }
        }

        private static void showShadowbanDialog(Context context, final long userId) {
            if (context == null) return;
            try {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(tr("Теневой бан","Shadowban"));

                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(AndroidUtilities.dp(16), AndroidUtilities.dp(8), AndroidUtilities.dp(16), AndroidUtilities.dp(8));

                final boolean[] hideDialog = new boolean[]{true};
                final boolean[] hideInGroups = new boolean[]{true};

                org.telegram.ui.Cells.TextCheckCell checkDialog = new org.telegram.ui.Cells.TextCheckCell(context);
                checkDialog.setTextAndCheck(tr("Скрыть диалог","Hide dialog"), hideDialog[0], false);
                checkDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideDialog[0] = !hideDialog[0];
                        ((org.telegram.ui.Cells.TextCheckCell) v).setChecked(hideDialog[0]);
                    }
                });
                layout.addView(checkDialog);

                org.telegram.ui.Cells.TextCheckCell checkGroups = new org.telegram.ui.Cells.TextCheckCell(context);
                checkGroups.setTextAndCheck(tr("Скрыть сообщения в группах","Hide in groups"), hideInGroups[0], false);
                checkGroups.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideInGroups[0] = !hideInGroups[0];
                        ((org.telegram.ui.Cells.TextCheckCell) v).setChecked(hideInGroups[0]);
                    }
                });
                layout.addView(checkGroups);

                builder.setView(layout);
                builder.setPositiveButton(tr("Забанить","Ban"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int which) {
                        ShadowbanStore.add(userId, hideDialog[0], hideInGroups[0]);
                        BaseFragment frag = LaunchActivity.getLastFragment();
                        if (frag != null) {
                            BulletinFactory.of(frag).createSuccessBulletin(tr("Добавлен в теневой бан","Added to shadowban")).show();
                        }
                    }
                });
                builder.setNegativeButton("Отмена", null);
                builder.show();
            } catch (Throwable t) {
                Main.err("showShadowbanDlg", new Exception(t));
            }
        }

        private void hkMessageFilters() {
            try {
                // DialogCell.buildLayout()
                Method mBuildLayout = org.telegram.ui.Cells.DialogCell.class.getDeclaredMethod("buildLayout");
                mBuildLayout.setAccessible(true);
                this.hooks.add(XposedBridge.hookMethod(mBuildLayout, new XC_MethodHook() {
                    @Override
                    public void beforeHookedMethod(MethodHookParam param) {
                        try {
                            if (ShadowbanStore.isEmpty() && RegexFiltersStore.isEmpty()) return;
                            Field fMsg = org.telegram.ui.Cells.DialogCell.class.getDeclaredField("message");
                            fMsg.setAccessible(true);
                            MessageObject mo = (MessageObject) fMsg.get(param.thisObject);
                            if (mo != null && !mo.isOut()) {
                                boolean inGroup = mo.getDialogId() < 0;
                                boolean hidden = (inGroup && ShadowbanStore.hidesInGroups(mo.getFromChatId())) || RegexFiltersStore.isFiltered(mo);
                                if (hidden) {
                                    mo.messageText = tr("Сообщение отфильтровано","Message filtered");
                                    mo.messageTextShort = null;
                                    if (mo.caption != null) mo.caption = "Сообщение отфильтровано";
                                }
                            }
                        } catch (Throwable t) {}
                    }
                }));

                // ChatActivity.didReceivedNotification for messagesDidLoad
                Method mDidRec = ChatActivity.class.getDeclaredMethod("didReceivedNotification", Integer.TYPE, Integer.TYPE, Object[].class);
                mDidRec.setAccessible(true);
                this.hooks.add(XposedBridge.hookMethod(mDidRec, new XC_MethodHook() {
                    @Override
                    public void beforeHookedMethod(MethodHookParam param) {
                        try {
                            int id = ((Integer) param.args[0]).intValue();
                            if (id == NotificationCenter.messagesDidLoad) {
                                if (ShadowbanStore.isEmpty() && RegexFiltersStore.isEmpty()) return;
                                Object[] args = (Object[]) param.args[2];
                                if (args != null && args.length >= 3 && (args[2] instanceof ArrayList)) {
                                    ArrayList list = (ArrayList) args[2];
                                    for (int i = list.size() - 1; i >= 0; i--) {
                                        Object o = list.get(i);
                                        if (o instanceof MessageObject) {
                                            MessageObject mo = (MessageObject) o;
                                            if (!mo.isOut()) {
                                                boolean inGroup = mo.getDialogId() < 0;
                                                if ((inGroup && ShadowbanStore.hidesInGroups(mo.getFromChatId())) || RegexFiltersStore.isFiltered(mo)) {
                                                    list.remove(i);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Throwable t) {}
                    }
                }));
            } catch (Throwable t) {
                Main.err("hkMsgFilters", new Exception(t));
            }
        }

        private void hkGifReuploader() {
            try {
                for (Method m : MessagesController.class.getDeclaredMethods()) {
                    if ("saveGif".equals(m.getName()) && m.getParameterTypes().length == 2) {
                        m.setAccessible(true);
                        this.hooks.add(XposedBridge.hookMethod(m, new XC_MethodHook() {
                            @Override
                            public void beforeHookedMethod(MethodHookParam param) {
                                try {
                                    if (!Main.cfg.b("gif_reuploader", true)) return;
                                    Object parent = param.args[0];
                                    TLRPC.Document doc = (TLRPC.Document) param.args[1];
                                    MessageObject mo = parent instanceof MessageObject ? (MessageObject) parent : null;
                                    if (mo != null && doc != null && GifReuploader.needsReupload(mo, doc)) {
                                        param.setResult(null);
                                        GifReuploader.reupload(mo, doc);
                                    }
                                } catch (Throwable t) {
                                    Main.err("saveGifHook", new Exception(t));
                                }
                            }
                        }));
                        break;
                    }
                }
            } catch (Throwable t) {
                Main.err("hkGifReup", new Exception(t));
            }
        }

    private static void initDeletedIcon(Context ctx) {
        Main.initDelIcon();
    }

private void go() throws Exception {

            initDeletedIcon(ApplicationLoader.applicationContext);

            hkSendReq();

            hkUpdates();

            hkMarkDel();

            hkProcDel();

            hkSetMsg();

            hkDrawOutbounds();

            hkMeasureTime();
            hkDelMsgs();
            hkShadowban();
            hkMessageFilters();
            hkGifReuploader();

            hkCanDel();

            hkCanFwd();

            hkNoFwd();

            hkMenu();

            hkOpt();

            hkOutgoingParams();

            hkFlag();

            hkPrem();

            hkOnce();

            hkCheckDel();

            hkNotif();

            hkNewMsg();

            hkStorySave();

            hkSettingsEntry();

            hkChatMenu3dot();

            hkFormatUserStatus();

        }

        

        public static long dpk(long j, int i) {

            return (j << 32) ^ (i & 4294967295L);

        }

        private static void ef() {
            if (fInit) {
                return;
            }
            fInit = true;
            try {
                fTs = ChatMessageCell.class.getDeclaredField("currentTimeString");
                fTs.setAccessible(true);
            } catch (Exception e) {
                fTs = null;
            }
            try {
                fTw = ChatMessageCell.class.getDeclaredField("timeTextWidth");
                fTw.setAccessible(true);
            } catch (Exception e) {
                fTw = null;
            }
            try {
                fW = ChatMessageCell.class.getDeclaredField("timeWidth");
                fW.setAccessible(true);
            } catch (Exception e) {
                fW = null;
            }
            try {
                fTp = Theme.class.getDeclaredField("chat_timePaint");
                fTp.setAccessible(true);
            } catch (Exception e) {
                fTp = null;
            }
        }

        public static float delOp() {

            long currentTimeMillis = System.currentTimeMillis();

            if (currentTimeMillis - cachedDelOpTs > 2000) {

                if (Main.cfg.b("del_show_opacity", true)) {

                    int level = Main.cfg.i("del_opacity_level", 60);

                    if (level >= 0 && level <= 4) {

                        // старые пресеты (0..4) — миграция на проценты

                        cachedDelOp = new float[]{1.0f, 0.8f, 0.6f, 0.4f, 0.2f}[level];

                    } else {

                        cachedDelOp = Math.max(5, Math.min(100, level)) / 100f;

                    }

                } else {

                    cachedDelOp = 1.0f;

                }

                cachedDelOpTs = currentTimeMillis;

            }

            return cachedDelOp;

        }

        /* Принудительный сброс кэша прозрачности удалёнки.

           Нужен для экрана настроек (DeletedCustomizeFragment), где предпросмотр

           должен отражать изменение прозрачности мгновенно, а не раз в 2 секунды. */

        static void invalidateDelOpCache() {

            cachedDelOpTs = 0;

        }

        private static void markCellDeleted(ChatMessageCell chatMessageCell, boolean z) {

            int identityHashCode = System.identityHashCode(chatMessageCell);

            if (z) {

                deletedCellIds.add(Integer.valueOf(identityHashCode));

                if (deletedCellIds.size() > 500) {

                    deletedCellIds.clear();

                    return;

                }

                return;

            }

            deletedCellIds.remove(Integer.valueOf(identityHashCode));

        }

        

        public static boolean isCellMarkedDeleted(ChatMessageCell chatMessageCell) {

            return deletedCellIds.contains(Integer.valueOf(System.identityHashCode(chatMessageCell)));

        }

        // Invisible guard prefix to reliably detect "stamp already inserted" regardless of custom label

        private static final String MARK_GUARD = "\u200b\u2063";

        // Cached stamp spans to avoid recreation on every bind

        private static volatile SpannableStringBuilder cachedTextStamp;

        private static volatile SpannableStringBuilder cachedIconStamp;

        private static volatile int cachedStampWidth;

        private static volatile long cachedStampConfigTs;

        private static volatile String cachedLabel;

        private static volatile int cachedColor;

        private static volatile boolean cachedUseIcon;

        private static void rebuildStampCacheIfNeeded() {

            // Использует те же настройки, что и hkMeasureTime (re:extera приоритет:

            // свой префикс > эмодзи-метка > значок корзины), чтобы список удалёнок

            // выглядел одинаково с чатом.

            long now = System.currentTimeMillis();

            if (now - cachedStampConfigTs < 1000) return;

            String label = deletedMarkText();

            int color = deletedMarkColor();

            long emojiId = deletedMarkEmojiId();

            Drawable icon = Main.getDeletedIcon();

            boolean useIcon = label.isEmpty() && emojiId == 0L;

            if (cachedLabel != null && cachedLabel.equals(label) && cachedColor == color && cachedUseIcon == useIcon) {

                cachedStampConfigTs = now;

                return;

            }

            cachedLabel = label;

            cachedColor = color;

            cachedUseIcon = useIcon;

            TextPaint textPaint = Theme.chat_timePaint;

            if (textPaint == null) return;

            if (!label.isEmpty()) {

                SpannableStringBuilder sb = new SpannableStringBuilder(MARK_GUARD + label + " ");

                int guardLen = MARK_GUARD.length();

                if (color != 0) {

                    sb.setSpan(new ForegroundColorSpan(color), guardLen, guardLen + label.length(), 33);

                }

                cachedStampWidth = (int) Math.ceil(textPaint.measureText(label + " "));

                cachedTextStamp = sb;

                cachedIconStamp = null;

            } else if (emojiId != 0L) {

                try {

                    AnimatedEmojiDrawable drawable = AnimatedEmojiDrawable.make(UserConfig.selectedAccount, EMOJI_DRAWABLE_CACHE_MESSAGES, emojiId);

                    SpannableStringBuilder sb = new SpannableStringBuilder(MARK_GUARD + ". ");

                    int guardLen = MARK_GUARD.length();

                    ColoredImageSpan span = new ColoredImageSpan(drawable);

                    span.recolorDrawable = false;

                    span.setRelativeSize(textPaint.getFontMetricsInt());

                    sb.setSpan(span, guardLen, guardLen + 1, 33);

                    Paint.FontMetricsInt fm = textPaint.getFontMetricsInt();

                    cachedStampWidth = Math.abs(fm.descent - fm.ascent) + (int) Math.ceil(textPaint.measureText(" "));

                    cachedIconStamp = sb;

                    cachedTextStamp = null;

                } catch (Throwable t) {

                    cachedIconStamp = null;

                    cachedTextStamp = null;

                }

            } else if (useIcon) {

                if (icon != null) {

                    SpannableStringBuilder sb = new SpannableStringBuilder(MARK_GUARD + "\u2009\u2009");

                    int guardLen = MARK_GUARD.length();

                    DeletedIconSpan span = new DeletedIconSpan(icon, color);

                    span.setRelativeSize(textPaint.getFontMetricsInt());

                    sb.setSpan(span, guardLen, guardLen + 1, 0);

                    Paint.FontMetricsInt fm = textPaint.getFontMetricsInt();

                    cachedStampWidth = Math.abs(fm.descent - fm.ascent) + (int) Math.ceil(textPaint.measureText(" "));

                    cachedIconStamp = sb;

                    cachedTextStamp = null;

                } else {

                    cachedIconStamp = null;

                    cachedTextStamp = null;

                }

            } else {

                cachedIconStamp = null;

                cachedTextStamp = null;

            }

            cachedStampConfigTs = now;

        }

        static void stampCell(ChatMessageCell chatMessageCell, MessageObject messageObject) {

            markCellDeleted(chatMessageCell, true);

            rebuildStampCacheIfNeeded();

            if (fTs == null) return;

            try {

                Object obj = fTs.get(chatMessageCell);

                if (obj == null) return;

                String obj2 = obj.toString();

                if (!obj2.startsWith(MARK_GUARD)) {

                    SpannableStringBuilder stamp = cachedUseIcon ? cachedIconStamp : cachedTextStamp;

                    if (stamp == null) return;

                    int ceil = cachedStampWidth;

                    if (obj instanceof SpannableStringBuilder) {

                        ((SpannableStringBuilder) obj).insert(0, stamp);

                        fTs.set(chatMessageCell, obj);

                    } else {

                        SpannableStringBuilder sb = new SpannableStringBuilder();

                        sb.append(stamp);

                        sb.append(obj2);

                        fTs.set(chatMessageCell, sb);

                    }

                    try {

                        fTw.setInt(chatMessageCell, fTw.getInt(chatMessageCell) + ceil);

                        fW.setInt(chatMessageCell, fW.getInt(chatMessageCell) + ceil);

                    } catch (Exception e2) {}

                    applyAlpha(chatMessageCell);

                    return;

                }

                applyAlpha(chatMessageCell);

            } catch (Exception e3) {}

        }

        static void clearCellMark(ChatMessageCell chatMessageCell) {

            if (isCellMarkedDeleted(chatMessageCell)) {

                markCellDeleted(chatMessageCell, false);

                insideStamp = true;

                try {

                    chatMessageCell.setAlpha(1.0f);

                } finally {

                    insideStamp = false;

                }

            }

        }

        

        public static String extractText(MessageObject messageObject) {

            String str = "";

            try {

                if (messageObject.messageOwner != null && messageObject.messageOwner.message != null && !messageObject.messageOwner.message.isEmpty()) {

                    str = messageObject.messageOwner.message;

                }

            } catch (Exception e) {

            }

            if (str.isEmpty()) {

                try {

                    CharSequence charSequence = messageObject.messageText;

                    if (charSequence != null && charSequence.length() > 0) {

                        str = charSequence.toString();

                    }

                } catch (Exception e2) {

                }

            }

            if (str.isEmpty()) {

                try {

                    CharSequence charSequence2 = messageObject.caption;

                    if (charSequence2 != null && charSequence2.length() > 0) {

                        str = charSequence2.toString();

                    }

                } catch (Exception e3) {

                }

            }

            if (str.isEmpty()) {

                try {

                    if (!messageObject.isSticker() && !messageObject.isAnimatedSticker()) {

                        if (messageObject.isVoice()) {

                            str = "🎤";

                        } else if (messageObject.isRoundVideo()) {

                            str = "📹";

                        } else if (messageObject.isVideo()) {

                            str = "🎥";

                        } else if (messageObject.isGif()) {

                            str = "GIF";

                        } else if (messageObject.isPhoto()) {

                            str = "\u1f5bc";

                        } else if (messageObject.isMusic()) {

                            str = "🎵";

                        } else if (messageObject.getDocument() != null) {

                            str = "📄";

                        }

                    }

                    str = "🎭";

                } catch (Exception e4) {

                }

            }

            if (str.isEmpty() && messageObject.messageOwner != null && messageObject.messageOwner.media != null) {

                str = "📎";

            }

            return str.isEmpty() ? "✉" : str;

        }

        private static void applyAlpha(ChatMessageCell chatMessageCell) {

            float delOp = delOp();

            if (delOp < 1.0f) {

                insideStamp = true;

                try {

                    chatMessageCell.setAlpha(delOp);

                } finally {

                    insideStamp = false;

                }

            }

        }

        

        public static void maybeStamp(ChatMessageCell chatMessageCell, MessageObject messageObject) {

            if (messageObject != null && messageObject.eventId == 1) {

                stampCell(chatMessageCell, messageObject);

                return;

            }

            if (Main.db == null) return;

            if (messageObject != null && (messageObject.isSending() || (messageObject.messageOwner != null && messageObject.messageOwner.send_state != 0))) {

                clearCellMark(chatMessageCell);

                return;

            }

            boolean isMarked = isCellMarkedDeleted(chatMessageCell);

            boolean inRem = Main.db.isRem(messageObject);

            if (!isMarked && messageObject != null && !inRem) {

                return;

            }

            if (!inRem) {

                if (!isMarked) {

                    clearCellMark(chatMessageCell);

                }

            } else {

                stampCell(chatMessageCell, messageObject);

            }

        }

        private void hkSendReq() {

            Method method;

            try {

                Method[] declaredMethods = ConnectionsManager.class.getDeclaredMethods();

                int length = declaredMethods.length;

                int i = 0;

                while (true) {

                    if (i >= length) {

                        method = null;

                        break;

                    }

                    method = declaredMethods[i];

                    if ("sendRequestInternal".equals(method.getName()) && method.getParameterTypes().length >= 5 && TLObject.class.isAssignableFrom(method.getParameterTypes()[0])) {

                        break;

                    }

                    i++;

                }

                if (method == null) {

                    return;

                }

                this.sendReqHook = XposedBridge.hookMethod(method, new XC_MethodHook() {

                    public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {

                        try {

                            // Ghost-режим полностью обрабатывается на python-стороне

                            // (pre_request_hook, перенесено из elyx). Здесь остаётся

                            // только блокировка рекламных запросов.

                            if (methodHookParam.args != null && methodHookParam.args.length >= 1 && (methodHookParam.args[0] instanceof TLObject)) {
                                TLObject reqObj = (TLObject) methodHookParam.args[0];
                                String simpleName = reqObj.getClass().getSimpleName();
                                if ("TL_messages_getOutboxReadDate".equals(simpleName) || reqObj instanceof TLRPC.TL_messages_getOutboxReadDate) {
                                    try {
                                        TLRPC.TL_messages_getOutboxReadDate req = (TLRPC.TL_messages_getOutboxReadDate) reqObj;
                                        long reqDid = 0;
                                        if (req.peer instanceof TLRPC.TL_inputPeerUser) reqDid = ((TLRPC.TL_inputPeerUser) req.peer).user_id;
                                        else if (req.peer instanceof TLRPC.TL_inputPeerChat) reqDid = -((TLRPC.TL_inputPeerChat) req.peer).chat_id;
                                        else if (req.peer instanceof TLRPC.TL_inputPeerChannel) reqDid = -1000000000000L - ((TLRPC.TL_inputPeerChannel) req.peer).channel_id;
                                        else if (req.peer instanceof TLRPC.TL_inputPeerUserFromMessage) reqDid = ((TLRPC.TL_inputPeerUserFromMessage) req.peer).user_id;
                                        else if (req.peer instanceof TLRPC.TL_inputPeerChannelFromMessage) reqDid = -1000000000000L - ((TLRPC.TL_inputPeerChannelFromMessage) req.peer).channel_id;
                                        final long targetDid = reqDid;
                                        final int targetMid = req.msg_id;
                                        final RequestDelegate origDelegate = methodHookParam.args.length > 1 ? (RequestDelegate) methodHookParam.args[1] : null;
                                        methodHookParam.args[1] = new RequestDelegate() {
                                            @Override
                                            public void run(TLObject response, TLRPC.TL_error error) {
                                                if (response instanceof TLRPC.TL_outboxReadDate) {
                                                    int date = ((TLRPC.TL_outboxReadDate) response).date;
                                                    if (date > 0 && targetDid != 0 && targetMid > 0) {
                                                        Main.saveReadDate(targetDid, targetMid, date);
                                                    }
                                                    if (origDelegate != null) origDelegate.run(response, error);
                                                } else {
                                                    int savedDate = Main.getReadDate(targetDid, targetMid);
                                                    if (savedDate > 0) {
                                                        TLRPC.TL_outboxReadDate fallback = new TLRPC.TL_outboxReadDate();
                                                        fallback.date = savedDate;
                                                        if (origDelegate != null) origDelegate.run(fallback, null);
                                                    } else {
                                                        if (origDelegate != null) origDelegate.run(response, error);
                                                    }
                                                }
                                            }
                                        };
                                    } catch (Throwable ignored) {}
                                }
                            }

                        } catch (Throwable th) {

                        }

                    }

                });

            } catch (Exception e) {

                Main.err("hkSR", e);

            }

        }

        private void hkUpdates() {

            Method method;

            try {

                Method[] declaredMethods = MessagesController.class.getDeclaredMethods();

                int length = declaredMethods.length;

                int i = 0;

                while (true) {

                    if (i >= length) {

                        method = null;

                        break;

                    }

                    method = declaredMethods[i];

                    if ("processUpdates".equals(method.getName()) && method.getParameterTypes().length == 2 && method.getParameterTypes()[1] == Boolean.TYPE) {

                        break;

                    }

                    i++;

                }

                if (method == null) {

                    return;

                }

                this.hooks.add(XposedBridge.hookMethod(method, new XC_MethodHook() {

                    public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {

                        if (Main.cfg.b("save_edits", true)) {

                            Main.processEditsInUpdates(methodHookParam.thisObject, methodHookParam.args[0]);

                        }

                    }

                    public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {

                        // Запись последнего онлайна: при включённом трекере присутствия

                        // ИЛИ "Сохранять последний онлайн" фиксируем время, когда

                        // пользователь был замечен в сети (статус, печатает, новые

                        // сообщения, короткие апдейты).

                        boolean track = Main.cfg.b("presence_track", false);

                        boolean saveLast = Main.cfg.b("save_last_online", false);

                        if (!track && !saveLast) {

                            return;

                        }

                        try {

                            long now = ConnectionsManager.getInstance(UserConfig.selectedAccount).getCurrentTime();

                            Object arg = methodHookParam.args[0];

                            if (arg != null) {

                                String cn = arg.getClass().getSimpleName();

                                if ("TL_updateShortMessage".equals(cn)) {
                                    try {
                                        long uid = ((Number) Main.gf(arg, "user_id")).longValue();
                                        int date = ((Number) Main.gf(arg, "date")).intValue();
                                        Main.presenceRecord(uid, date > 0 ? date : now);
                                    } catch (Exception e) {
                                    }
                                } else if ("TL_updateShortChatMessage".equals(cn)) {
                                    try {
                                        long uid = ((Number) Main.gf(arg, "from_id")).longValue();
                                        int date = ((Number) Main.gf(arg, "date")).intValue();
                                        Main.presenceRecord(uid, date > 0 ? date : now);
                                    } catch (Exception e) {
                                    }
                                } else if ("TL_updateReadHistoryOutbox".equals(cn)) {
                                    try {
                                        TLRPC.Peer peer = (TLRPC.Peer) Main.gf(arg, "peer");
                                        long did = peer != null ? (peer.user_id != 0 ? peer.user_id : (peer.chat_id != 0 ? -peer.chat_id : -1000000000000L - peer.channel_id)) : 0;
                                        int maxId = ((Number) Main.gf(arg, "max_id")).intValue();
                                        if (did != 0 && maxId > 0) {
                                            Main.saveReadDate(did, maxId, (int) now);
                                        }
                                    } catch (Exception e) {}
                                } else if ("TL_updateReadChannelOutbox".equals(cn)) {
                                    try {
                                        long channelId = ((Number) Main.gf(arg, "channel_id")).longValue();
                                        int maxId = ((Number) Main.gf(arg, "max_id")).intValue();
                                        if (channelId != 0 && maxId > 0) {
                                            Main.saveReadDate(-1000000000000L - channelId, maxId, (int) now);
                                        }
                                    } catch (Exception e) {}
                                } else if ("TL_updateShortChatMessage".equals(cn)) {

                                    try {

                                        long uid = ((Number) Main.gf(arg, "from_id")).longValue();

                                        int date = ((Number) Main.gf(arg, "date")).intValue();

                                        Main.presenceRecord(uid, date > 0 ? date : now);

                                    } catch (Exception e) {

                                    }

                                }

                            }

                            Object updates = Main.gf(methodHookParam.args[0], "updates");

                            if (updates instanceof ArrayList) {

                                Iterator it = ((ArrayList) updates).iterator();

                                while (it.hasNext()) {

                                    Object next = it.next();

                                    String simpleName = next.getClass().getSimpleName();

                                    if ("TL_updateUserStatus".equals(simpleName)) {

                                        try {

                                            long uid = ((Number) Main.gf(next, "user_id")).longValue();

                                            Object st = Main.gf(next, "status");

                                            if (st == null) {

                                                continue;

                                            }

                                            String stName = st.getClass().getSimpleName();

                                            if ("TL_userStatusOnline".equals(stName)) {

                                                Main.presenceRecord(uid, now);

                                            } else if ("TL_userStatusOffline".equals(stName)) {

                                                long wasOnline = 0;

                                                try {

                                                    Object w = Main.gf(st, "was_online");

                                                    if (w instanceof Number) {

                                                        wasOnline = ((Number) w).longValue();

                                                    }

                                                } catch (Exception e) {

                                                }

                                                long expires = 0;

                                                try {

                                                    Object ex = Main.gf(st, "expires");

                                                    if (ex instanceof Number) {

                                                        expires = ((Number) ex).longValue();

                                                    }

                                                } catch (Exception e2) {

                                                }

                                                if (wasOnline <= 0) {

                                                    wasOnline = expires;

                                                }

                                                if (wasOnline > now) {

                                                    wasOnline = now;

                                                }

                                                Main.presenceRecord(uid, wasOnline);

                                            }

                                        } catch (Exception e3) {

                                        }

                                    } else if ("TL_updateUserTyping".equals(simpleName)) {

                                        try {

                                            Main.presenceRecord(((Number) Main.gf(next, "user_id")).longValue(), now);

                                        } catch (Exception e4) {

                                        }

                                    } else if ("TL_updateChatUserTyping".equals(simpleName)) {

                                        try {

                                            Object fromId = Main.gf(next, "from_id");

                                            if (fromId instanceof TLRPC.Peer) {

                                                Main.presenceRecord(((TLRPC.Peer) fromId).user_id, now);

                                            }

                                        } catch (Exception e5) {

                                        }

                                    } else if ("TL_updateNewMessage".equals(simpleName) || "TL_updateNewChannelMessage".equals(simpleName)) {

                                        try {

                                            Object msg = Main.gf(next, "message");

                                            if (msg instanceof TLRPC.Message) {

                                                TLRPC.Message m = (TLRPC.Message) msg;

                                                boolean out = false;

                                                try {

                                                    out = m.out;

                                                } catch (Exception e6) {

                                                }

                                                if (!out && m.from_id instanceof TLRPC.TL_peerUser && m.from_id.user_id > 0) {

                                                    Main.presenceRecord(m.from_id.user_id, m.date > 0 ? m.date : now);

                                                }

                                            }

                                        } catch (Exception e7) {

                                        }

                                    }

                                }

                            }

                        } catch (Exception e8) {

                        }

                    }

                }));

            } catch (Exception e) {

                Main.err("hkUpd", e);

            }

        }

        // ============ re:extera DeletionBypass & capture engine ============

        public static final class DeletionBypass {

            private static final long TTL_MS = 60000L;

            private static final ConcurrentHashMap<String, Long> storage = new ConcurrentHashMap<>();

            private static final ConcurrentHashMap<String, Long> ui = new ConcurrentHashMap<>();

            private static String key(long did, int mid) {

                return did + ":" + mid;

            }

            public static void allow(long dialogId, Collection<Integer> messageIds) {

                if (messageIds == null || messageIds.isEmpty()) return;

                long exp = SystemClock.elapsedRealtime() + TTL_MS;

                evictExpired();

                for (Integer mid : messageIds) {

                    if (mid == null) continue;

                    String k = key(dialogId, mid.intValue());

                    storage.put(k, exp);

                    ui.put(k, exp);

                }

            }

            public static boolean consumeStorage(long dialogId, int messageId) {

                return consume(storage, key(dialogId, messageId));

            }

            public static boolean consumeUi(long dialogId, int messageId) {

                return consume(ui, key(dialogId, messageId));

            }

            private static boolean consume(ConcurrentHashMap<String, Long> map, String k) {

                Long exp = map.remove(k);

                return exp != null && exp.longValue() >= SystemClock.elapsedRealtime();

            }

            private static void evictExpired() {

                long now = SystemClock.elapsedRealtime();

                storage.entrySet().removeIf(e -> e.getValue() < now);

                ui.entrySet().removeIf(e -> e.getValue() < now);

            }

        }

        private static final ThreadLocal<Boolean> rangeDeleteActive = ThreadLocal.withInitial(() -> Boolean.FALSE);

        public static Map<Long, List<Integer>> resolveDialogsForMids(MessagesStorage storage, int currentAccount, Collection<Integer> messageIds) {
            if (messageIds == null || messageIds.isEmpty()) {
                return Collections.emptyMap();
            }
            Map<Long, List<Integer>> messagesByDialog = new HashMap<>();
            org.telegram.SQLite.SQLiteDatabase db = null;
            try {
                if (storage != null) {
                    db = storage.getDatabase();
                } else {
                    db = MessagesStorage.getInstance(currentAccount).getDatabase();
                }
            } catch (Throwable t) {}

            List<Integer> unresolvedIds = new ArrayList<>(messageIds);
            if (db != null) {
                try {
                    StringBuilder sb = new StringBuilder();
                    int count = 0;
                    for (Integer mid : messageIds) {
                        if (mid == null || mid.intValue() <= 0) continue;
                        if (count > 0) sb.append(",");
                        sb.append(mid.intValue());
                        count++;
                    }
                    if (count > 0) {
                        org.telegram.SQLite.SQLiteCursor cursor = db.queryFinalized("SELECT uid, mid FROM messages_v2 WHERE mid IN (" + sb.toString() + ") AND is_channel = 0");
                        while (cursor.next()) {
                            long msgUid = cursor.longValue(0);
                            int msgMid = cursor.intValue(1);
                            if (msgUid == 489000L) continue;
                            unresolvedIds.remove(Integer.valueOf(msgMid));
                            List<Integer> list = messagesByDialog.get(msgUid);
                            if (list == null) {
                                list = new ArrayList<>();
                                messagesByDialog.put(msgUid, list);
                            }
                            list.add(msgMid);
                        }
                        cursor.dispose();
                    }
                } catch (Throwable t) {
                    Main.err("resolveDialogsDb", new Exception(t));
                }
            }

            if (!unresolvedIds.isEmpty()) {
                try {
                    MessagesController mc = MessagesController.getInstance(currentAccount);
                    for (Integer mid : unresolvedIds) {
                        if (mid == null || mid.intValue() <= 0) continue;
                        long foundDid = 0;
                        try {
                            Object dialogMessagesByIds = Main.gf(mc, "dialogMessagesByIds");
                            if (dialogMessagesByIds != null) {
                                Object obj = Main.mapGet(dialogMessagesByIds, mid.intValue());
                                if (obj instanceof MessageObject) {
                                    foundDid = ((MessageObject) obj).getDialogId();
                                }
                            }
                        } catch (Throwable ignored) {}
                        if (foundDid != 0 && foundDid != 489000L) {
                            List<Integer> list = messagesByDialog.get(foundDid);
                            if (list == null) {
                                list = new ArrayList<>();
                                messagesByDialog.put(foundDid, list);
                            }
                            list.add(mid);
                        }
                    }
                } catch (Throwable ignored) {}
            }
            return messagesByDialog;
        }

        private Map<Integer, String> collectDeletedTexts(long did, List<Integer> mids) {

            HashMap<Integer, String> out = new HashMap<>();

            if (mids == null || mids.isEmpty()) return out;

            try {

                Object dialogMessagesByIds = Main.gf(MessagesController.getInstance(UserConfig.selectedAccount), "dialogMessagesByIds");

                if (dialogMessagesByIds != null) {

                    for (Integer mid : mids) {

                        Object mo = Main.mapGet(dialogMessagesByIds, mid.intValue());

                        if (mo instanceof MessageObject) {

                            out.put(mid, Main.encodeData(extractText((MessageObject) mo), (MessageObject) mo));

                        }

                    }

                }

            } catch (Exception ignored) {}

            if (out.size() < mids.size()) {

                try {

                    BaseFragment lastFragment = LaunchActivity.getLastFragment();

                    if (lastFragment instanceof ChatActivity) {

                        ChatActivity chatActivity = (ChatActivity) lastFragment;

                        Field messagesField = ChatActivity.class.getDeclaredField("messages");

                        messagesField.setAccessible(true);

                        ArrayList messages = (ArrayList) messagesField.get(chatActivity);

                        if (messages != null) {

                            HashSet<Integer> remaining = new HashSet<>(mids);

                            remaining.removeAll(out.keySet());

                            for (Object next : messages) {

                                if (remaining.isEmpty()) break;

                                if (next instanceof MessageObject) {

                                    MessageObject mo = (MessageObject) next;

                                    if (remaining.remove(Integer.valueOf(mo.getId()))) {

                                        out.put(Integer.valueOf(mo.getId()), Main.encodeData(extractText(mo), mo));

                                    }

                                }

                            }

                        }

                    }

                } catch (Exception ignored) {}

            }

            return out;

        }

        private void saveDeletedBatch(long did, List<Integer> mids) {

            if (did == 0 || did == 489000L || mids == null || mids.isEmpty() || Main.db == null) {

                return;

            }

            ArrayList<Integer> filteredMids = new ArrayList<>();

            for (Integer midObj : mids) {

                if (midObj == null) continue;

                int mid = midObj.intValue();

                if (mid <= 0) continue;

                filteredMids.add(midObj);

            }

            if (filteredMids.isEmpty()) {

                return;

            }

            Map<Integer, String> texts = collectDeletedTexts(did, filteredMids);

            HashMap<Integer, String> withFallback = new HashMap<>();

            HashMap<Long, MessageObject> objects = new HashMap<>();

            Object dialogMessagesByIds = Main.gf(MessagesController.getInstance(UserConfig.selectedAccount), "dialogMessagesByIds");

            for (Integer mid : filteredMids) {

                String text = texts.get(mid);

                withFallback.put(mid, (text == null || text.isEmpty()) ? "✉" : text);

                Object found = dialogMessagesByIds != null ? Main.mapGet(dialogMessagesByIds, mid.intValue()) : null;

                if (found instanceof MessageObject && ((MessageObject) found).getDialogId() == did) {

                    objects.put(Long.valueOf(mid.longValue()), (MessageObject) found);

                }

            }

            Main.db.markRem(did, filteredMids, withFallback);

            Main.saveAttachments(did, new ArrayList<>(filteredMids), objects);

            Main.refreshCellsForMids(new ArrayList<>(filteredMids));

        }

        private void hkMarkDel() {
            try {
                for (final Method method : MessagesStorage.class.getDeclaredMethods()) {
                    String name = method.getName();
                    Class<?>[] pTypes = method.getParameterTypes();
                    if ("markMessagesAsDeleted".equals(name) || "markMessagesAsDeletedInternal".equals(name)) {
                        if (pTypes.length >= 2 && pTypes[0] == Long.TYPE && pTypes[1] == ArrayList.class) {
                            try {
                                method.setAccessible(true);
                                this.hooks.add(XposedBridge.hookMethod(method, new XC_MethodHook() {
                                    @Override
                                    public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                                        try {
                                            if (!Main.cfg.b("save_deleted", true) || Main.db == null) return;
                                            if (rangeDeleteActive.get()) return;
                                            long did = ((Number) methodHookParam.args[0]).longValue();
                                            if (did == 489000L || Main.purgeDialogs.contains(Long.valueOf(did))) return;
                                            if (did != 0 && !Main.cfg.b("save_bot_chats", false) && Main.isBotDialog(did)) return;

                                            ArrayList<Integer> original = (ArrayList<Integer>) methodHookParam.args[1];
                                            if (original == null || original.isEmpty()) return;

                                            ArrayList<Integer> validIds = new ArrayList<>();
                                            ArrayList<Integer> tempIds = new ArrayList<>();
                                            for (Integer id : original) {
                                                if (id == null) continue;
                                                int v = id.intValue();
                                                if (v > 0 && !DeletionBypass.consumeStorage(did, v)) {
                                                    validIds.add(id);
                                                } else {
                                                    tempIds.add(id);
                                                }
                                            }

                                            methodHookParam.args[1] = tempIds;
                                            if (tempIds.isEmpty()) {
                                                methodHookParam.setResult(null);
                                            }

                                            if (!validIds.isEmpty()) {
                                                int currentAccount = Main.resolveAccount(methodHookParam.thisObject);
                                                Map<Long, List<Integer>> messagesByDialog = new HashMap<>();
                                                if (did != 0) {
                                                    messagesByDialog.put(did, validIds);
                                                } else {
                                                    MessagesStorage storage = (MessagesStorage) methodHookParam.thisObject;
                                                    messagesByDialog.putAll(resolveDialogsForMids(storage, currentAccount, validIds));
                                                }

                                                for (Map.Entry<Long, List<Integer>> entry : messagesByDialog.entrySet()) {
                                                    long targetDid = entry.getKey().longValue();
                                                    List<Integer> targetIds = entry.getValue();
                                                    if (targetDid == 0 || targetDid == 489000L) continue;
                                                    if (!Main.cfg.b("save_bot_chats", false) && Main.isBotDialog(targetDid)) continue;
                                                    Main.addDeleted(targetDid, targetIds);
                                                    saveDeletedBatch(targetDid, targetIds);
                                                    Main.forceUpdateViews(currentAccount, targetDid, targetIds);
                                                }
                                            }
                                        } catch (Throwable t) {
                                            Main.err("hkMD", new Exception(t));
                                        }
                                    }
                                }));
                            } catch (Throwable ignored) {}
                        } else if (pTypes.length >= 2 && pTypes[0] == Long.TYPE && pTypes[1] == Integer.TYPE) {
                            try {
                                method.setAccessible(true);
                                this.hooks.add(XposedBridge.hookMethod(method, new XC_MethodHook() {
                                    @Override
                                    public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                                        try {
                                            if (!Main.cfg.b("save_deleted", true) || Main.db == null) return;
                                            if (rangeDeleteActive.get()) return;
                                            long did = ((Number) methodHookParam.args[0]).longValue();
                                            int mid = ((Number) methodHookParam.args[1]).intValue();
                                            if (did == 489000L || Main.purgeDialogs.contains(Long.valueOf(did))) return;
                                            if (did != 0 && !Main.cfg.b("save_bot_chats", false) && Main.isBotDialog(did)) return;

                                            if (mid > 0 && !DeletionBypass.consumeStorage(did, mid)) {
                                                methodHookParam.setResult(null);
                                                if (did != 0) {
                                                    int currentAccount = Main.resolveAccount(methodHookParam.thisObject);
                                                    List<Integer> targetIds = Collections.singletonList(Integer.valueOf(mid));
                                                    Main.addDeleted(did, targetIds);
                                                    saveDeletedBatch(did, targetIds);
                                                    Main.forceUpdateViews(currentAccount, did, targetIds);
                                                }
                                            }
                                        } catch (Throwable t) {
                                            Main.err("hkMD_single", new Exception(t));
                                        }
                                    }
                                }));
                            } catch (Throwable ignored) {}
                        }
                    } else if ("updateDialogsWithDeletedMessages".equals(name) || "updateDialogsWithDeletedMessagesInternal".equals(name)) {
                        if (pTypes.length >= 3 && pTypes[0] == Long.TYPE && pTypes[1] == Long.TYPE && pTypes[2] == ArrayList.class) {
                            final boolean isInternal = name.endsWith("Internal");
                            try {
                                method.setAccessible(true);
                                this.hooks.add(XposedBridge.hookMethod(method, new XC_MethodHook() {
                                    @Override
                                    public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                                        try {
                                            if (!Main.cfg.b("save_deleted", true) || Main.db == null) return;
                                            if (rangeDeleteActive.get()) return;
                                            long uid = ((Number) methodHookParam.args[0]).longValue();
                                            long channelId = ((Number) methodHookParam.args[1]).longValue();
                                            long did = channelId != 0 ? -channelId : uid;
                                            if (did == 489000L || Main.purgeDialogs.contains(Long.valueOf(did))) return;
                                            if (did != 0 && !Main.cfg.b("save_bot_chats", false) && Main.isBotDialog(did)) return;

                                            ArrayList<Integer> original = (ArrayList<Integer>) methodHookParam.args[2];
                                            if (original == null || original.isEmpty()) return;

                                            ArrayList<Integer> validIds = new ArrayList<>();
                                            ArrayList<Integer> tempIds = new ArrayList<>();
                                            for (Integer id : original) {
                                                if (id == null) continue;
                                                int v = id.intValue();
                                                if (v > 0 && !DeletionBypass.consumeStorage(did, v)) {
                                                    validIds.add(id);
                                                } else {
                                                    tempIds.add(id);
                                                }
                                            }

                                            methodHookParam.args[2] = tempIds;
                                            if (isInternal && tempIds.isEmpty()) {
                                                methodHookParam.setResult(null);
                                            }

                                            if (!validIds.isEmpty()) {
                                                int currentAccount = Main.resolveAccount(methodHookParam.thisObject);
                                                Map<Long, List<Integer>> messagesByDialog = new HashMap<>();
                                                if (did != 0) {
                                                    messagesByDialog.put(did, validIds);
                                                } else {
                                                    MessagesStorage storage = (MessagesStorage) methodHookParam.thisObject;
                                                    messagesByDialog.putAll(resolveDialogsForMids(storage, currentAccount, validIds));
                                                }

                                                for (Map.Entry<Long, List<Integer>> entry : messagesByDialog.entrySet()) {
                                                    long targetDid = entry.getKey().longValue();
                                                    List<Integer> targetIds = entry.getValue();
                                                    if (targetDid == 0 || targetDid == 489000L) continue;
                                                    if (!Main.cfg.b("save_bot_chats", false) && Main.isBotDialog(targetDid)) continue;
                                                    Main.addDeleted(targetDid, targetIds);
                                                    saveDeletedBatch(targetDid, targetIds);
                                                    Main.forceUpdateViews(currentAccount, targetDid, targetIds);
                                                }
                                            }
                                        } catch (Throwable t) {
                                            Main.err("hkUD", new Exception(t));
                                        }
                                    }
                                }));
                            } catch (Throwable ignored) {}
                        }
                    }
                }
            } catch (Exception e) {
                Main.err("hkMarkDel", e);
            }

            for (final Method method : MessagesController.class.getDeclaredMethods()) {
                if ("deleteMessagesRange".equals(method.getName())) {
                    try {
                        method.setAccessible(true);
                        this.hooks.add(XposedBridge.hookMethod(method, new XC_MethodHook() {
                            public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                                rangeDeleteActive.set(Boolean.TRUE);
                            }

                            public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                                rangeDeleteActive.remove();
                            }
                        }));
                    } catch (Exception e) {}
                }
            }
        }

        private void hkProcDel() {
            try {
                this.hooks.add(XposedBridge.hookMethod(ChatActivity.class.getDeclaredMethod("processDeletedMessages", ArrayList.class, Long.TYPE, Boolean.TYPE, Boolean.TYPE), new XC_MethodHook() {
                    @Override
                    public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                        try {
                            if (!Main.cfg.b("save_deleted", true) || Main.db == null) {
                                return;
                            }
                            final ChatActivity chatActivity = (ChatActivity) methodHookParam.thisObject;
                            if (chatActivity.getChatMode() != 0) {
                                return;
                            }
                            final long dialogId = chatActivity.getDialogId();
                            if (dialogId == 489000L || Main.purgeDialogs.contains(Long.valueOf(dialogId))) {
                                return;
                            }
                            if (dialogId != 0 && !Main.cfg.b("save_bot_chats", false) && Main.isBotDialog(dialogId)) {
                                return;
                            }
                            ArrayList<Integer> originalMessages = (ArrayList<Integer>) methodHookParam.args[0];
                            if (originalMessages == null || originalMessages.isEmpty()) {
                                return;
                            }

                            ArrayList<Integer> validIds = new ArrayList<>();
                            ArrayList<Integer> tempIds = new ArrayList<>();
                            for (Integer id : originalMessages) {
                                if (id == null) continue;
                                int v = id.intValue();
                                if (v > 0 && !DeletionBypass.consumeUi(dialogId, v)) {
                                    validIds.add(id);
                                } else {
                                    tempIds.add(id);
                                }
                            }

                            methodHookParam.args[0] = tempIds;

                            if (!validIds.isEmpty()) {
                                Main.addDeleted(dialogId, validIds);
                                saveDeletedBatch(dialogId, validIds);
                                try {
                                    if (chatActivity.messages != null) {
                                        for (MessageObject mo : chatActivity.messages) {
                                            if (mo != null && mo.deleted && validIds.contains(Integer.valueOf(mo.getId()))) {
                                                mo.deleted = false;
                                            }
                                        }
                                    }
                                } catch (Throwable ignored) {}
                                Main.forceUpdateViews(chatActivity.getCurrentAccount(), dialogId, validIds);
                            }
                        } catch (Throwable t) {
                            Main.err("hkPD", new Exception(t));
                        }
                    }
                }));
            } catch (Exception e) {
                Main.err("hkProcDel", e);
            }
        }

        private void hkSetMsg() {
            try {
                for (Method method : ChatMessageCell.class.getDeclaredMethods()) {
                    if ("setMessageObject".equals(method.getName()) && method.getParameterTypes().length >= 4) {
                        method.setAccessible(true);
                        this.hooks.add(XposedBridge.hookMethod(method, new XC_MethodHook() {
                            @Override
                            public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                                try {
                                    ChatMessageCell cell = (ChatMessageCell) methodHookParam.thisObject;
                                    MessageObject mo = (MessageObject) methodHookParam.args[0];
                                    if (mo == null) mo = cell.getMessageObject();
                                    boolean isDel = Main.cfg.b("del_show_opacity", true) && mo != null && isCellDeleted(cell, mo);
                                    float a = isDel ? delOp() : 1.0f;
                                    try { cell.setTransitionAlpha(a); } catch (Throwable ignored) {}
                                    try { cell.setAlpha(a); } catch (Throwable ignored) {}
                                } catch (Throwable t) {}
                            }
                        }));
                    }
                }
            } catch (Exception e) {
                Main.err("hkSM", e);
            }
        }

        private static boolean isCellDeleted(ChatMessageCell cell, MessageObject mo) {
            if (mo == null) {
                return false;
            }
            if (mo.isSending() || (mo.messageOwner != null && mo.messageOwner.send_state != 0)) {
                return false;
            }
            try {
                if (mo.getDialogId() == -999999999L) {
                    return true;
                }
            } catch (Throwable t) {}
            try {
                MessageObject.GroupedMessages group = cell != null ? cell.getCurrentMessagesGroup() : null;
                if (group != null && group.messages != null && !group.messages.isEmpty()) {
                    for (MessageObject m : group.messages) {
                        if (m != null && (m.deleted || Main.isDeletedMsg(m))) {
                            return true;
                        }
                    }
                    return false;
                }
            } catch (Throwable t) {}
            return mo.deleted || Main.isDeletedMsg(mo);
        }

        private int outboundsSaveCount = -1;

        private void hkDrawOutbounds() {

            try {

                for (Method m : ChatMessageCell.class.getDeclaredMethods()) {

                    if ("drawOutboundsContent".equals(m.getName()) && m.getParameterTypes().length == 1 && m.getParameterTypes()[0] == Canvas.class) {

                        m.setAccessible(true);

                        this.hooks.add(XposedBridge.hookMethod(m, new XC_MethodHook() {

                            @Override

                            public void beforeHookedMethod(XC_MethodHook.MethodHookParam param) {

                                try {

                                    ChatMessageCell cell = (ChatMessageCell) param.thisObject;

                                    float transitionAlpha = cell.getTransitionAlpha();

                                    if (transitionAlpha < 1.0f) {

                                        Canvas canvas = (Canvas) param.args[0];

                                        outboundsSaveCount = canvas.saveLayerAlpha(null, (int) (transitionAlpha * 255.0f));

                                    }

                                } catch (Throwable t) {}

                            }

                            @Override

                            public void afterHookedMethod(XC_MethodHook.MethodHookParam param) {

                                try {

                                    if (outboundsSaveCount >= 0) {

                                        Canvas canvas = (Canvas) param.args[0];

                                        canvas.restoreToCount(outboundsSaveCount);

                                        outboundsSaveCount = -1;

                                    }

                                } catch (Throwable t) {}

                            }

                        }));

                        break;

                    }

                }

            } catch (Exception e) {

                Main.err("hkDrawOutbounds", e);

            }

        }

        // ============ re:extera MeasureTime: метка удалённого сообщения ============

        // Вставляется ПОСЛЕ нативного measureTime — в самый момент, когда Telegram

        // формирует строку времени, поэтому метка не теряется при перерисовках

        // (в отличие от старого подхода с правкой currentTimeString в setMessageObject).

        private static final int EMOJI_DRAWABLE_CACHE_MESSAGES = 0;

        private static final Map<ChatMessageCell, AnimatedEmojiDrawable> deletedEmojiDrawables = new WeakHashMap<>();

        private static final Map<ChatMessageCell, View.OnAttachStateChangeListener> deletedEmojiListeners = new WeakHashMap<>();

        private static long deletedMarkEmojiId() {

            try {

                return Main.cfg.prefs().getLong("del_mark_emoji_id", 0L);

            } catch (Exception e) {

                return 0L;

            }

        }

        private static int deletedMarkColor() {

            try {

                return Main.cfg.i("del_mark_color", 0);

            } catch (Exception e) {

                return 0;

            }

        }

        static String deletedMarkText() {

            try {

                String s = Main.cfg.s("del_custom_label", "");

                return s != null ? s : "";

            } catch (Exception e) {

                return "";

            }

        }

        private static synchronized void bindDeletedEmojiDrawable(ChatMessageCell cell, AnimatedEmojiDrawable drawable) {

            AnimatedEmojiDrawable oldDrawable = deletedEmojiDrawables.put(cell, drawable);

            if (oldDrawable != null && oldDrawable != drawable) {

                oldDrawable.removeView(cell);

            }

            drawable.addView(cell);

            if (!deletedEmojiListeners.containsKey(cell)) {

                WeakReference<ChatMessageCell> cellReference = new WeakReference<>(cell);

                View.OnAttachStateChangeListener listener = new View.OnAttachStateChangeListener() {

                    @Override

                    public void onViewAttachedToWindow(View view) {

                        ChatMessageCell attachedCell = cellReference.get();

                        if (attachedCell == null) {

                            return;

                        }

                        AnimatedEmojiDrawable attachedDrawable = deletedEmojiDrawables.get(attachedCell);

                        if (attachedDrawable != null) {

                            attachedDrawable.addView(attachedCell);

                        }

                    }

                    @Override

                    public void onViewDetachedFromWindow(View view) {

                        ChatMessageCell detachedCell = cellReference.get();

                        if (detachedCell == null) {

                            return;

                        }

                        AnimatedEmojiDrawable detachedDrawable = deletedEmojiDrawables.get(detachedCell);

                        if (detachedDrawable != null) {

                            detachedDrawable.removeView(detachedCell);

                        }

                    }

                };

                deletedEmojiListeners.put(cell, listener);

                cell.addOnAttachStateChangeListener(listener);

            }

        }

        private static synchronized void unbindDeletedEmojiDrawable(ChatMessageCell cell) {

            AnimatedEmojiDrawable drawable = deletedEmojiDrawables.remove(cell);

            if (drawable != null) {

                drawable.removeView(cell);

            }

            View.OnAttachStateChangeListener listener = deletedEmojiListeners.remove(cell);

            if (listener != null) {

                cell.removeOnAttachStateChangeListener(listener);

            }

        }

        private static SpannableStringBuilder buildDeletedPrefix(ChatMessageCell cell, TextPaint paint) {
            SpannableStringBuilder builder;
            String mark = deletedMarkText();
            int color = deletedMarkColor();
            if (mark != null && !mark.isEmpty()) {
                unbindDeletedEmojiDrawable(cell);
                builder = new SpannableStringBuilder(mark);
                if (color != 0) {
                    builder.setSpan(new ForegroundColorSpan(color), 0, builder.length(), 33);
                }
                return builder;
            }
            long emojiId = deletedMarkEmojiId();
            if (emojiId != 0L) {
                try {
                    AnimatedEmojiDrawable drawable = AnimatedEmojiDrawable.make(UserConfig.selectedAccount, EMOJI_DRAWABLE_CACHE_MESSAGES, emojiId);
                    bindDeletedEmojiDrawable(cell, drawable);
                    builder = new SpannableStringBuilder(".");
                    ColoredImageSpan span = new ColoredImageSpan(drawable);
                    span.recolorDrawable = false;
                    span.setRelativeSize(paint.getFontMetricsInt());
                    builder.setSpan(span, 0, builder.length(), 33);
                    return builder;
                } catch (Throwable t) {
                }
            }
            unbindDeletedEmojiDrawable(cell);
            Drawable icon = Main.getDeletedIcon();
            if (icon != null) {
                builder = new SpannableStringBuilder("....");
                DeletedIconSpan span = new DeletedIconSpan(icon, color);
                span.setRelativeSize(paint.getFontMetricsInt());
                builder.setSpan(span, 0, builder.length(), 33);
                return builder;
            }
            return new SpannableStringBuilder("\uD83D\uDDD1");
        }

        private static void appendDeletedMark(ChatMessageCell cell, MessageObject mo) {
            try {
                if (!Main.cfg.b("del_show_mark", true)) return;
                ef();
                if (fTs == null || fTw == null) return;
                Object rawTs = fTs.get(cell);
                if (rawTs == null) return;
                CharSequence currentTimeString = (CharSequence) rawTs;

                if (currentTimeString.toString().startsWith(MARK_GUARD)) {
                    return;
                }

                TextPaint paint = null;
                if (fTp != null) {
                    try {
                        paint = (TextPaint) fTp.get(null);
                    } catch (Throwable ignored) {}
                }
                if (paint == null) {
                    paint = Theme.chat_timePaint;
                }
                if (paint == null) return;

                SpannableStringBuilder prefix = buildDeletedPrefix(cell, paint);
                if (prefix == null) return;

                SpannableStringBuilder builderTime;
                if (currentTimeString instanceof SpannableStringBuilder) {
                    builderTime = (SpannableStringBuilder) currentTimeString;
                } else {
                    builderTime = new SpannableStringBuilder(currentTimeString);
                }

                prefix.append(" ");
                builderTime.insert(0, prefix);
                builderTime.insert(0, MARK_GUARD);
                fTs.set(cell, builderTime);

                int extraWidth;
                ColoredImageSpan[] imageSpans = prefix.getSpans(0, prefix.length(), ColoredImageSpan.class);
                if (imageSpans != null && imageSpans.length > 0) {
                    extraWidth = imageSpans[0].getSize(paint, prefix, 0, prefix.length(), paint.getFontMetricsInt()) + (int) Math.ceil(paint.measureText(" "));
                } else {
                    extraWidth = (int) Math.ceil(paint.measureText(prefix, 0, prefix.length()));
                }

                try {
                    int currentTw = fTw.getInt(cell);
                    fTw.setInt(cell, currentTw + extraWidth);
                } catch (Throwable ignored) {}

                if (fW != null) {
                    try {
                        int currentW = fW.getInt(cell);
                        fW.setInt(cell, currentW + extraWidth);
                    } catch (Throwable ignored) {}
                }

                if (Main.cfg.b("del_show_opacity", true)) {
                    try {
                        cell.setAlpha(1.0f);
                    } catch (Throwable ignored) {}
                }
            } catch (Throwable t) {
                Main.err("appendDeletedMark", new Exception(t));
            }
        }

        private void hkMeasureTime() {
            try {
                Method m = ChatMessageCell.class.getDeclaredMethod("measureTime", MessageObject.class);
                m.setAccessible(true);
                this.hooks.add(XposedBridge.hookMethod(m, new XC_MethodHook() {
                    @Override
                    public void afterHookedMethod(MethodHookParam param) {
                        try {
                            MessageObject mo = (MessageObject) param.args[0];
                            ChatMessageCell cell = (ChatMessageCell) param.thisObject;
                            if (mo == null) mo = cell.getMessageObject();
                            if (mo != null && isCellDeleted(cell, mo)) {
                                appendDeletedMark(cell, mo);
                            }
                        } catch (Throwable t) {}
                    }
                }));
            } catch (Exception e) {
                Main.err("hkMT", e);
            }
        }

        private void hkDelMsgs() {
            try {
                for (Method method : MessagesController.class.getDeclaredMethods()) {
                    if ("deleteMessages".equals(method.getName())) {
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        if (parameterTypes.length >= 7 && parameterTypes[0] == ArrayList.class && parameterTypes[1] == ArrayList.class && "EncryptedChat".equals(parameterTypes[2].getSimpleName()) && parameterTypes[3] == Long.TYPE && parameterTypes[4] == Integer.TYPE && parameterTypes[5] == Boolean.TYPE && parameterTypes[6] == Integer.TYPE) {
                            this.hooks.add(XposedBridge.hookMethod(method, new XC_MethodHook() {
                                public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                                    try {
                                        ArrayList<Integer> ids = (ArrayList<Integer>) methodHookParam.args[0];
                                        if (ids == null || ids.isEmpty() || !Main.cfg.b("save_deleted", true) || Main.db == null) {
                                            return;
                                        }
                                        final long did = ((Number) methodHookParam.args[3]).longValue();
                                        int channel_id = ((Number) methodHookParam.args[4]).intValue();
                                        long effDid = did;
                                        if (channel_id != 0) {
                                            effDid = -1000000000000L - channel_id;
                                        } else if (did < -1000000000000L) {
                                            channel_id = (int) (-did - 1000000000000L);
                                        }

                                        final long effectiveDid = effDid;
                                        final int effectiveChannelId = channel_id;

                                        if (effectiveDid == 0 || effectiveDid == 489000L || Main.purgeDialogs.contains(Long.valueOf(effectiveDid))) {
                                            return;
                                        }
                                        boolean isEncrypted = methodHookParam.args[2] != null;
                                        final int currentAccount = UserConfig.selectedAccount;

                                        HashSet<Integer> alreadyDeleted = new HashSet<>();
                                        if (!isEncrypted) {
                                            for (Integer id : ids) {
                                                if (id != null) {
                                                    int v = id.intValue();
                                                    if (Main.isDeletedMsg(effectiveDid, v) || (did != 0 && Main.isDeletedMsg(did, v))) {
                                                        alreadyDeleted.add(id);
                                                    }
                                                }
                                            }
                                        }

                                        ArrayList<Integer> notYetDeleted = new ArrayList<>();
                                        for (Integer id : ids) {
                                            if (id != null && !alreadyDeleted.contains(id)) {
                                                notYetDeleted.add(id);
                                            }
                                        }

                                        if (!alreadyDeleted.isEmpty()) {
                                            final ArrayList<Integer> purgeCopy = new ArrayList<>(alreadyDeleted);
                                            Main.removeDeleted(effectiveDid, purgeCopy);
                                            if (did != 0 && did != effectiveDid) {
                                                Main.removeDeleted(did, purgeCopy);
                                            }
                                            if (Main.db != null) {
                                                Main.db.purge(effectiveDid, purgeCopy);
                                                if (did != 0 && did != effectiveDid) {
                                                    Main.db.purge(did, purgeCopy);
                                                }
                                            }
                                            DeletionBypass.allow(effectiveDid, purgeCopy);
                                            if (did != 0) DeletionBypass.allow(did, purgeCopy);

                                            AndroidUtilities.runOnUIThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        MessagesController.getInstance(currentAccount).deleteMessages(purgeCopy, null, null, effectiveDid, effectiveChannelId, false, 0);
                                                    } catch (Throwable ignored) {}
                                                    try {
                                                        BaseFragment lastFrag = LaunchActivity.getLastFragment();
                                                        if (lastFrag instanceof ChatActivity) {
                                                            ChatActivity ca = (ChatActivity) lastFrag;
                                                            if (ca.getDialogId() == effectiveDid || (ca.getCurrentChat() != null && ca.getCurrentChat().id == effectiveChannelId)) {
                                                                if (ca.messages != null) {
                                                                    ArrayList<MessageObject> toRemove = new ArrayList<>();
                                                                    for (MessageObject mo : ca.messages) {
                                                                        if (mo != null && purgeCopy.contains(Integer.valueOf(mo.getId()))) {
                                                                            toRemove.add(mo);
                                                                        }
                                                                    }
                                                                    if (!toRemove.isEmpty()) {
                                                                        ca.messages.removeAll(toRemove);
                                                                        ca.updateMessagesVisiblePart(true);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    } catch (Throwable ignored) {}
                                                }
                                            });
                                        }

                                        boolean saveOwn = Main.cfg.b("track_own_deletions", true);
                                        if (!saveOwn) {
                                            DeletionBypass.allow(effectiveDid, notYetDeleted);
                                            if (did != 0) DeletionBypass.allow(did, notYetDeleted);
                                        }

                                        if (notYetDeleted.isEmpty()) {
                                            methodHookParam.setResult(null);
                                        } else {
                                            if (!alreadyDeleted.isEmpty()) {
                                                methodHookParam.args[0] = new ArrayList<>(notYetDeleted);
                                            }
                                        }
                                    } catch (Exception e) {
                                        Main.err("hkDelMsgs", e);
                                    }
                                }
                            }));
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                Main.err("hkDelMsgs", e);
            }
        }

        private void hkCanDel() {

            try {

                this.hooks.add(XposedBridge.hookMethod(MessageObject.class.getDeclaredMethod("canDeleteMessage", Boolean.TYPE, TLRPC.Chat.class), new XC_MethodHook() {

                    public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {

                        if (Main.db == null || !Main.db.isRem((MessageObject) methodHookParam.thisObject)) {

                            return;

                        }

                        methodHookParam.setResult(true);

                    }

                }));

            } catch (Exception e) {

            }

        }

        private void hkCanFwd() {

            try {

                this.hooks.add(XposedBridge.hookMethod(MessageObject.class.getDeclaredMethod("canForwardMessage", new Class[0]), new XC_MethodHook() {

                    public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {

                        if (Main.cfg.b("bypass_noforward", false)) {

                            methodHookParam.setResult(true);

                        }

                    }

                }));

            } catch (Exception e) {

            }

        }

        private void hkNoFwd() {

            Method[] declaredMethods;

            Method[] declaredMethods2;

            XC_MethodHook xC_MethodHook = new XC_MethodHook() {

                public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {

                    if (Main.cfg.b("bypass_noforward", false)) {

                        methodHookParam.setResult(false);

                    }

                }

            };

            for (Method method : MessagesController.class.getDeclaredMethods()) {

                if ("isChatNoForwards".equals(method.getName()) || "isUserNoForwards".equals(method.getName()) || "isPeerNoForwards".equals(method.getName())) {

                    try {

                        this.hooks.add(XposedBridge.hookMethod(method, xC_MethodHook));

                    } catch (Exception e) {

                    }

                }

            }

            try {

                this.hooks.add(XposedBridge.hookMethod(ChatActivity.class.getDeclaredMethod("hasSelectedNoforwardsMessage", new Class[0]), xC_MethodHook));
            } catch (Exception e2) {
            }

            // re:extera showForwardActionsDuring wrapper
            try {
                for (Method m : ChatActivity.class.getDeclaredMethods()) {
                    String name = m.getName();
                    if ("createMenu".equals(name) || "fillMessageMenu".equals(name) || "processSelectedOption".equals(name) || "addToSelectedMessages".equals(name) || "openForward".equals(name)) {
                        m.setAccessible(true);
                        this.hooks.add(XposedBridge.hookMethod(m, new XC_MethodHook() {
                            private final List<TLRPC.Message> hidden = new ArrayList<>();
                            private Boolean origReadDatesPrivate = null;
                            private Integer origExpirePeriod = null;
                            private ChatActivity currentCa = null;

                            @Override
                            public void beforeHookedMethod(MethodHookParam param) {
                                try {
                                    if ("createMenu".equals(param.method.getName()) && param.thisObject instanceof ChatActivity) {
                                        ChatActivity ca = (ChatActivity) param.thisObject;
                                        currentCa = ca;
                                        try {
                                            Field fUInfo = ChatActivity.class.getDeclaredField("userInfo");
                                            fUInfo.setAccessible(true);
                                            TLRPC.UserFull uf = (TLRPC.UserFull) fUInfo.get(ca);
                                            if (uf != null && uf.read_dates_private) {
                                                origReadDatesPrivate = Boolean.valueOf(uf.read_dates_private);
                                                uf.read_dates_private = false;
                                            }
                                        } catch (Throwable ignored) {}
                                        MessagesController mc = ca.getMessagesController();
                                        if (mc != null && mc.pmReadDateExpirePeriod < 1000000000) {
                                            origExpirePeriod = Integer.valueOf(mc.pmReadDateExpirePeriod);
                                            mc.pmReadDateExpirePeriod = Integer.MAX_VALUE;
                                        }
                                    }
                                    if (!Main.cfg.b("bypass_noforward", false)) return;
                                    hidden.clear();
                                    if (param.args != null) {
                                        for (Object a : param.args) {
                                            if (a instanceof MessageObject) {
                                                MessageObject mo = (MessageObject) a;
                                                if (mo.messageOwner != null && mo.messageOwner.noforwards) {
                                                    mo.messageOwner.noforwards = false;
                                                    hidden.add(mo.messageOwner);
                                                }
                                            }
                                        }
                                    }
                                } catch (Throwable t) {}
                            }

                            @Override
                            public void afterHookedMethod(MethodHookParam param) {
                                try {
                                    if (currentCa != null) {
                                        if (origReadDatesPrivate != null) {
                                            try {
                                                Field fUInfo = ChatActivity.class.getDeclaredField("userInfo");
                                                fUInfo.setAccessible(true);
                                                TLRPC.UserFull uf = (TLRPC.UserFull) fUInfo.get(currentCa);
                                                if (uf != null) {
                                                    uf.read_dates_private = origReadDatesPrivate.booleanValue();
                                                }
                                            } catch (Throwable ignored) {}
                                            origReadDatesPrivate = null;
                                        }
                                        MessagesController mc = currentCa.getMessagesController();
                                        if (mc != null && origExpirePeriod != null) {
                                            mc.pmReadDateExpirePeriod = origExpirePeriod.intValue();
                                            origExpirePeriod = null;
                                        }

                                        // Restore from cloud context menu item
                                        if ("createMenu".equals(param.method.getName()) && Boolean.TRUE.equals(param.getResult())) {
                                            final ChatActivity chatAct = currentCa;
                                            MessageObject selMsg = null;
                                            if (param.args != null && param.args.length > 0 && param.args[0] instanceof org.telegram.ui.Cells.ChatMessageCell) {
                                                selMsg = ((org.telegram.ui.Cells.ChatMessageCell) param.args[0]).getMessageObject();
                                            }
                                            if (selMsg == null) {
                                                try {
                                                    Field fSel = ChatActivity.class.getDeclaredField("selectedObject");
                                                    fSel.setAccessible(true);
                                                    selMsg = (MessageObject) fSel.get(chatAct);
                                                } catch (Throwable ignored) {}
                                            }
                                            final MessageObject selectedMsg = selMsg;
                                            if (selectedMsg != null && (selectedMsg.deleted || Main.isDeletedMsg(selectedMsg))) {
                                                if (chatAct.scrimPopupWindow != null && chatAct.scrimPopupWindow.getContentView() instanceof ViewGroup) {
                                                    ViewGroup contentView = (ViewGroup) chatAct.scrimPopupWindow.getContentView();
                                                    ViewGroup popupLayout = findActionBarPopupWindowLayout(contentView);
                                                    if (popupLayout != null) {
                                                        org.telegram.ui.ActionBar.ActionBarMenuSubItem restoreItem = new org.telegram.ui.ActionBar.ActionBarMenuSubItem(chatAct.getParentActivity(), true, true);
                                                        restoreItem.setTextAndIcon(tr("Восстановить из облака","Restore from cloud"), R.drawable.msg_retry);
                                                        restoreItem.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                if (chatAct.scrimPopupWindow != null) {
                                                                    chatAct.scrimPopupWindow.dismiss();
                                                                }
                                                                restoreMessageFromCloud(chatAct, selectedMsg);
                                                            }
                                                        });
                                                        popupLayout.addView(restoreItem);
                                                    }
                                                }
                                            }
                                        }
                                        currentCa = null;
                                    }
                                    for (TLRPC.Message msg : hidden) {
                                        if (msg != null) msg.noforwards = true;
                                    }
                                    hidden.clear();
                                } catch (Throwable t) {}
                            }
                        }));
                    }
                }
            } catch (Throwable t) {}

            try {
                for (Method method2 : SendMessagesHelper.class.getDeclaredMethods()) {

                    if ("sendMessage".equals(method2.getName()) && method2.getParameterTypes().length == 1 && method2.getParameterTypes()[0].getSimpleName().contains("SendMessageParams")) {

                        this.hooks.add(XposedBridge.hookMethod(method2, new XC_MethodHook() {

                            public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {

                                if (Main.cfg.b("bypass_noforward", false)) {

                                    try {

                                        Main.sf(methodHookParam.args[0], "noforwards", false);

                                    } catch (Exception e3) {

                                    }

                                }

                            }

                        }));

                        return;

                    }

                }

            } catch (Exception e3) {

            }

        }

        private static ViewGroup findActionBarPopupWindowLayout(ViewGroup root) {
            if (root == null) return null;
            if (root.getClass().getSimpleName().contains("ActionBarPopupWindowLayout")) {
                return root;
            }
            for (int i = 0; i < root.getChildCount(); i++) {
                View child = root.getChildAt(i);
                if (child instanceof ViewGroup) {
                    ViewGroup res = findActionBarPopupWindowLayout((ViewGroup) child);
                    if (res != null) return res;
                }
            }
            return null;
        }

        private static void restoreMessageFromCloud(final ChatActivity ca, final MessageObject selectedMsg) {
            if (ca == null || selectedMsg == null) return;
            try {
                final long did = selectedMsg.getDialogId();
                final int mid = selectedMsg.getId();
                final int account = ca.getCurrentAccount();

                // 1. Remove deleted markers
                Main.removeDeleted(did, Collections.singletonList(Integer.valueOf(mid)));
                if (did < -1000000000000L) {
                    long chId = -did - 1000000000000L;
                    Main.removeDeleted(-chId, Collections.singletonList(Integer.valueOf(mid)));
                } else if (did < 0) {
                    long chId = -did;
                    Main.removeDeleted(-1000000000000L - chId, Collections.singletonList(Integer.valueOf(mid)));
                }
                if (Main.db != null) {
                    Main.db.purge(did, new ArrayList<>(Collections.singletonList(Integer.valueOf(mid))));
                    if (did < -1000000000000L) {
                        Main.db.purge(-did - 1000000000000L, new ArrayList<>(Collections.singletonList(Integer.valueOf(mid))));
                    }
                }
                selectedMsg.deleted = false;

                // 2. Fetch original from server
                boolean isChannel = (did < -1000000000000L) || (ca.getCurrentChat() != null && ChatObject.isChannel(ca.getCurrentChat()));
                if (isChannel) {
                    long channelId = ca.getCurrentChat() != null ? ca.getCurrentChat().id : (-did - 1000000000000L);
                    TLRPC.TL_channels_getMessages req = new TLRPC.TL_channels_getMessages();
                    req.channel = MessagesController.getInstance(account).getInputChannel(channelId);
                    req.id.add(Integer.valueOf(mid));
                    ConnectionsManager.getInstance(account).sendRequest(req, new RequestDelegate() {
                        @Override
                        public void run(final TLObject response, final TLRPC.TL_error error) {
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    handleCloudRestoreResponse(ca, selectedMsg, response, error, did, mid, account);
                                }
                            });
                        }
                    });
                } else {
                    TLRPC.TL_messages_getMessages req = new TLRPC.TL_messages_getMessages();
                    req.id.add(Integer.valueOf(mid));
                    ConnectionsManager.getInstance(account).sendRequest(req, new RequestDelegate() {
                        @Override
                        public void run(final TLObject response, final TLRPC.TL_error error) {
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    handleCloudRestoreResponse(ca, selectedMsg, response, error, did, mid, account);
                                }
                            });
                        }
                    });
                }
            } catch (Throwable t) {
                Main.err("restoreMessageFromCloud", new Exception(t));
            }
        }

        private static void handleCloudRestoreResponse(ChatActivity ca, MessageObject selectedMsg, TLObject response, TLRPC.TL_error error, long did, int mid, int account) {
            try {
                if (response instanceof TLRPC.messages_Messages) {
                    TLRPC.messages_Messages res = (TLRPC.messages_Messages) response;
                    if (res.messages != null && !res.messages.isEmpty()) {
                        TLRPC.Message srvMsg = res.messages.get(0);
                        if (srvMsg instanceof TLRPC.TL_message) {
                            srvMsg.dialog_id = did;
                            ArrayList<TLRPC.Message> arr = new ArrayList<>();
                            arr.add(srvMsg);
                            MessagesStorage.getInstance(account).putMessages(arr, false, true, false, 0, false, 0, 0);
                            selectedMsg.messageOwner = srvMsg;
                            selectedMsg.deleted = false;
                        }
                    }
                }
                selectedMsg.deleted = false;
                if (ca.messages != null) {
                    for (MessageObject mo : ca.messages) {
                        if (mo != null && mo.getId() == mid) {
                            mo.deleted = false;
                            if (selectedMsg.messageOwner != null) mo.messageOwner = selectedMsg.messageOwner;
                        }
                    }
                }
                Main.forceUpdateViews(account, did, Collections.singletonList(Integer.valueOf(mid)));
                BulletinFactory.of(ca).createSuccessBulletin(tr("Сообщение восстановлено из облака","Message restored from cloud")).show();
            } catch (Throwable t) {
                Main.err("handleCloudRestoreResponse", new Exception(t));
            }
        }

        private void hkNoForwardLimit() {

            Method[] declaredMethods;

            try {

                Method declaredMethod = ChatActivity.class.getDeclaredMethod("addToSelectedMessages", MessageObject.class, Boolean.TYPE, Boolean.TYPE);

                declaredMethod.setAccessible(true);

                this.hooks.add(XposedBridge.hookMethod(declaredMethod, this.nflSelector));

            } catch (Exception e) {

            }

            try {

                Method declaredMethod2 = ChatActivity.class.getDeclaredMethod("hideActionMode", new Class[0]);

                declaredMethod2.setAccessible(true);

                this.hooks.add(XposedBridge.hookMethod(declaredMethod2, new XC_MethodHook() {

                    public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {

                        HookManager.this.nflSelector.clear();

                    }

                }));

            } catch (Exception e2) {

            }

            try {

                Method declaredMethod3 = ChatActivity.class.getDeclaredMethod("startMultiselect", Integer.TYPE);

                declaredMethod3.setAccessible(true);

                this.hooks.add(XposedBridge.hookMethod(declaredMethod3, new XC_MethodHook() {

                    public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {

                        if (Main.cfg.b("no_forward_limit", false)) {

                            try {

                                Field declaredField = ChatActivity.class.getDeclaredField("chatListView");

                                declaredField.setAccessible(true);

                                Object obj = declaredField.get(methodHookParam.thisObject);

                                if (obj == null) {

                                    return;

                                }

                                Field declaredField2 = obj.getClass().getDeclaredField("multiSelectionListener");

                                declaredField2.setAccessible(true);

                                Object obj2 = declaredField2.get(obj);

                                if (obj2 == null) {

                                    return;

                                }

                                HookManager.this.hooks.add(XposedBridge.hookMethod(obj2.getClass().getMethod("limitReached", new Class[0]), new XC_MethodHook() {

                                    public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam2) {

                                        if (Main.cfg.b("no_forward_limit", false)) {

                                            methodHookParam2.setResult(false);

                                        }

                                    }

                                }));

                            } catch (Exception e3) {

                            }

                        }

                    }

                }));

            } catch (Exception e3) {

            }

            try {

                for (Method method : MessagesController.class.getDeclaredMethods()) {

                    if ("deleteMessages".equals(method.getName()) && method.getParameterTypes().length >= 7) {

                        method.setAccessible(true);

                        this.hooks.add(XposedBridge.hookMethod(method, new XC_MethodHook() {

                            public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {

                                int i = 0;

                                if (Main.cfg.b("no_forward_limit", false)) {

                                    try {

                                        ArrayList arrayList = (ArrayList) methodHookParam.args[0];

                                        char c = 1;

                                        ArrayList arrayList2 = (ArrayList) methodHookParam.args[1];

                                        if (arrayList != null && arrayList.size() > 0) {

                                            arrayList2 = arrayList;

                                        }

                                        if (arrayList != null && arrayList.size() > 0) {

                                            c = 0;

                                        }

                                        if (arrayList2 != null && arrayList2.size() > 100) {

                                            while (i < arrayList2.size()) {

                                                int i2 = i + 100;

                                                methodHookParam.args[c] = new ArrayList(arrayList2.subList(i, Math.min(arrayList2.size(), i2)));

                                                if (i2 < arrayList2.size()) {

                                                    try {

                                                        ((Method) methodHookParam.method).invoke(methodHookParam.thisObject, methodHookParam.args);

                                                    } catch (Exception e4) {

                                                    }

                                                }

                                                i = i2;

                                            }

                                        }

                                    } catch (Exception e5) {

                                    }

                                }

                            }

                        }));

                        return;

                    }

                }

            } catch (Exception e4) {

            }

        }

        

        private class NflSelector extends XC_MethodHook {

            private final Map<Integer, MessageObject> msgs;

            private NflSelector() {

                this.msgs = new HashMap();

            }

            void clear() {

                this.msgs.clear();

            }

            public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {

                char c = 0;

                if (Main.cfg.b("no_forward_limit", false)) {

                    try {

                        Field declaredField = ChatActivity.class.getDeclaredField("dialog_id");

                        declaredField.setAccessible(true);

                        if (((MessageObject) methodHookParam.args[0]).getDialogId() != declaredField.getLong(methodHookParam.thisObject)) {

                            c = 1;

                        }

                    } catch (Exception e) {

                    }

                    Object gf = Main.gf(methodHookParam.thisObject, "selectedMessagesIds");

                    if (gf instanceof Object[]) {

                        Object[] objArr = (Object[]) gf;

                        objArr[c] = new SparseArray();

                        Main.sf(methodHookParam.thisObject, "selectedMessagesIds", objArr);

                    }

                }

            }

            public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {

                char c;

                SparseArray sparseArray;

                int i = 0;

                if (Main.cfg.b("no_forward_limit", false)) {

                    try {

                        Field declaredField = ChatActivity.class.getDeclaredField("dialog_id");

                        declaredField.setAccessible(true);

                        c = ((MessageObject) methodHookParam.args[0]).getDialogId() != declaredField.getLong(methodHookParam.thisObject) ? (char) 1 : (char) 0;

                    } catch (Exception e) {

                        c = 0;

                    }

                    Object gf = Main.gf(methodHookParam.thisObject, "selectedMessagesIds");

                    if ((gf instanceof Object[]) && (sparseArray = (SparseArray) ((Object[]) gf)[c]) != null) {

                        for (int size = sparseArray.size() - 1; size >= 0; size--) {

                            int keyAt = sparseArray.keyAt(size);

                            boolean containsKey = this.msgs.containsKey(Integer.valueOf(keyAt));

                            Map<Integer, MessageObject> map = this.msgs;

                            Integer valueOf = Integer.valueOf(keyAt);

                            if (containsKey) {

                                map.remove(valueOf);

                            } else {

                                map.put(valueOf, (MessageObject) sparseArray.valueAt(size));

                            }

                        }

                        sparseArray.clear();

                        for (Map.Entry<Integer, MessageObject> entry : this.msgs.entrySet()) {

                            sparseArray.put(entry.getKey().intValue(), entry.getValue());

                        }

                        if (this.msgs.isEmpty()) {

                            try {

                                Method declaredMethod = ChatActivity.class.getDeclaredMethod("hideActionMode", new Class[0]);

                                declaredMethod.setAccessible(true);

                                declaredMethod.invoke(methodHookParam.thisObject, new Object[0]);

                                return;

                            } catch (Exception e2) {

                                return;

                            }

                        }

                        try {

                            Object gf2 = Main.gf(methodHookParam.thisObject, "actionsButtonsLayout");

                            if (gf2 != null) {

                                gf2.getClass().getMethod("showReplyButton", Boolean.TYPE, Boolean.TYPE).invoke(gf2, Boolean.valueOf(this.msgs.size() == 1), true);

                            } else {

                                Object gf3 = Main.gf(methodHookParam.thisObject, "replyButton");

                                if (gf3 instanceof View) {

                                    View view = (View) gf3;

                                    if (this.msgs.size() != 1) {

                                        i = 8;

                                    }

                                    view.setVisibility(i);

                                }

                            }

                        } catch (Exception e3) {

                        }

                    }

                }

            }

        }

        private void hkMenu() {

            try {

                this.hooks.add(XposedBridge.hookMethod(ChatActivity.class.getDeclaredMethod("fillMessageMenu", MessageObject.class, ArrayList.class, ArrayList.class, ArrayList.class), new XC_MethodHook() {

                    public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {

                        try {

                            MessageObject messageObject = (MessageObject) methodHookParam.args[0];

                            ArrayList arrayList = (ArrayList) methodHookParam.args[1];

                            ArrayList arrayList2 = (ArrayList) methodHookParam.args[2];

                            ArrayList arrayList3 = (ArrayList) methodHookParam.args[3];

                            boolean hasEdits = Main.db.hasEdits(messageObject.getDialogId(), messageObject.getId()) || (messageObject.messageOwner != null && messageObject.messageOwner.edit_date != 0);

                            if (Main.cfg.b("save_edits", true) && hasEdits) {

                                arrayList2.add(0, Main.tr("История изменений", "Edit history"));

                                arrayList3.add(0, 7171);

                                try {

                                    arrayList.add(0, Integer.valueOf(R.drawable.menu_premium_clock));

                                } catch (Exception e) {

                                    arrayList.add(0, 0);

                                }

                            }

                        } catch (Exception e3) {

                        }

                    }

                }));

            } catch (Exception e) {

            }

        }

        private void hkOpt() {

            try {

                this.hooks.add(XposedBridge.hookMethod(ChatActivity.class.getDeclaredMethod("processSelectedOption", Integer.TYPE), new XC_MethodHook() {

                    @Override

                    public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {

                        try {

                            int opt = ((Integer) methodHookParam.args[0]).intValue();

                            if (opt == 7171) {

                                MessageObject mo = null;

                                try {

                                    Field declaredField = ChatActivity.class.getDeclaredField("selectedObject");

                                    declaredField.setAccessible(true);

                                    mo = (MessageObject) declaredField.get(methodHookParam.thisObject);

                                } catch (Exception ignore) {}

                                if (mo != null && methodHookParam.thisObject instanceof BaseFragment) {

                                    ((BaseFragment) methodHookParam.thisObject).presentFragment(MessageHistoryFragment.newInstance(mo.getDialogId(), mo.getId()));

                                    methodHookParam.setResult(null);

                                }

                            }

                        } catch (Throwable ignore) {}

                    }

                }));

            } catch (Exception ignore) {}

        }

        

        public static void settingsToast(final String str) {

            try {

                if (settingsErrored) {

                    return;

                }

                settingsErrored = true;

                Main.ui(new Runnable() {

                    @Override // java.lang.Runnable

                    public final void run() {

                        HookManager.lambda$settingsToast$5(str);

                    }

                });

            } catch (Exception e) {

            }

        }

        static void lambda$settingsToast$5(String str) {

            try {

                Toast.makeText(ApplicationLoader.applicationContext, str, 1).show();

            } catch (Exception e) {

            }

        }

        private void hkSettingsEntry() {

            Field uitemField = null;

            Method method = null;

            try {

                uitemField = UItem.class.getField("id");

            } catch (Exception e) {

            }

            Field field = null;

            if (uitemField == null) {

                try {

                    field = UItem.class.getDeclaredField("id");

                    field.setAccessible(true);

                } catch (Exception e2) {

                    Main.err("hkSetE", e2);

                    settingsToast("MX settings entry: install error (" + e2.getMessage() + ")");

                    return;

                }

            } else {

                field = uitemField;

            }

            final Field finalField = field;

            try {

                int i = 0;

                Method declaredMethod = SettingsActivity.class.getDeclaredMethod("fillItems", ArrayList.class, Class.forName("org.telegram.ui.Components.UniversalAdapter"));

                declaredMethod.setAccessible(true);

                this.hooks.add(XposedBridge.hookMethod(declaredMethod, new XC_MethodHook() {

                    public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {

                        try {

                            HookManager.injectSettingsEntry((ArrayList) methodHookParam.args[0], finalField);

                        } catch (Exception e3) {

                            HookManager.settingsToast("MX settings entry: inject error (" + e3.getMessage() + ")");

                        }

                    }

                }));

                Method[] declaredMethods = SettingsActivity.class.getDeclaredMethods();

                int length = declaredMethods.length;

                while (true) {

                    if (i >= length) {

                        break;

                    }

                    Method method2 = declaredMethods[i];

                    if ("onClick".equals(method2.getName()) && method2.getParameterTypes().length == 5) {

                        method = method2;

                        break;

                    }

                    i++;

                }

                if (method == null) {

                    settingsToast("MX settings entry: onClick not found");

                    return;

                }

                method.setAccessible(true);

                this.hooks.add(XposedBridge.hookMethod(method, new XC_MethodHook() {

                    public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {

                        try {

                            Object obj = methodHookParam.args[0];

                            if (obj != null && finalField.getInt(obj) == HookManager.SETTINGS_ENTRY_ID) {

                                methodHookParam.setResult((Object) null);

                                HookManager.openPluginSettings((BaseFragment) methodHookParam.thisObject);

                            }

                        } catch (Exception e3) {

                            Main.err("hkSetC", e3);

                        }

                    }

                }));

            } catch (Exception e3) {

                settingsToast("MX settings entry: fillItems not found (" + e3.getMessage() + ")");

            }

        }

        

        public static void injectSettingsEntry(ArrayList<Object> arrayList, Field field) {

            Object makeSettingsCell;

            if (arrayList == null) {

                return;

            }

            try {

                SharedPreferences prefs = ApplicationLoader.applicationContext.getSharedPreferences(Main.PREFS, 0);

                if (!prefs.getBoolean("show_in_settings", true)) {

                    for (int i2 = 0; i2 < arrayList.size(); i2++) {

                        Object obj = arrayList.get(i2);

                        if (obj != null && field.getInt(obj) == SETTINGS_ENTRY_ID) {

                            arrayList.remove(i2);

                            return;

                        }

                    }

                    return;

                }

                if (arrayList.isEmpty()) {

                    return;

                }

                int i = -1;

                for (int i22 = 0; i22 < arrayList.size(); i22++) {

                    Object obj2 = arrayList.get(i22);

                    if (obj2 != null) {

                        try {

                            int i3 = field.getInt(obj2);

                            if (i3 == SETTINGS_ENTRY_ID) {

                                return;

                            }

                            if (i3 == -1 && i < 0) {

                                i = i22;

                            }

                        } catch (Exception e) {

                        }

                    }

                }

                if (i >= 0 && (makeSettingsCell = makeSettingsCell()) != null) {

                    arrayList.add(i + 1, makeSettingsCell);

                }

            } catch (Exception e2) {

            }

        }

        private static Object makeSettingsCell() {

            int i;

            try {

                Method declaredMethod = Class.forName("org.telegram.ui.SettingsActivity$SettingCell$Factory").getDeclaredMethod("of", Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, CharSequence.class);

                declaredMethod.setAccessible(true);

                try {

                    i = R.drawable.class.getField("msg_send").getInt(null);

                } catch (Exception e) {

                    try {

                        i = R.drawable.class.getField("msg_plugins").getInt(null);

                    } catch (Exception e2) {

                        try {

                            i = R.drawable.class.getField("msg_fave").getInt(null);

                        } catch (Exception e3) {

                            i = 0;

                        }

                    }

                }

                return declaredMethod.invoke(null, Integer.valueOf((int) SETTINGS_ENTRY_ID), -4826625, -10388225, Integer.valueOf(i), "ExteraMax");

            } catch (Exception e3) {

                return null;

            }

        }

        

        public static void openPluginSettings(BaseFragment baseFragmentParam) {

            BaseFragment baseFragment = baseFragmentParam;

            Object obj;

            if (baseFragment == null) {

                try {

                    baseFragment = LaunchActivity.getLastFragment();

                    if (!(baseFragment instanceof BaseFragment)) {

                        return;

                    }

                    BaseFragment baseFragment2 = baseFragment;

                } catch (Exception e) {

                    Main.err("openPS", e);

                    return;

                }

            }

            final BaseFragment finalFragment = baseFragment;

            Object invoke;

            Object invoke2;

            try {

                invoke = Class.forName("com.exteragram.messenger.plugins.PluginsController").getMethod("getInstance", new Class[0]).invoke(null, new Object[0]);

                invoke2 = invoke.getClass().getMethod("getPlugins", new Class[0]).invoke(invoke, new Object[0]);

            } catch (Exception ex) {

                return;

            }

            if ((invoke2 instanceof Map) && (obj = ((Map) invoke2).get("ExteraMax")) != null) {

                BaseFragment baseFragment3;

                try {

                    baseFragment3 = (BaseFragment) Class.forName("com.exteragram.messenger.plugins.ui.PluginSettingsActivity").getConstructor(Class.forName("com.exteragram.messenger.plugins.Plugin")).newInstance(obj);

                } catch (Exception ex2) {

                    return;

                }

                Main.ui(new Runnable() {

                    @Override // java.lang.Runnable

                    public final void run() {

                        HookManager.lambda$openPluginSettings$6(finalFragment, baseFragment3);

                    }

                });

            }

        }

        static void lambda$openPluginSettings$6(BaseFragment baseFragment, BaseFragment baseFragment2) {

            try {

                baseFragment.presentFragment(baseFragment2);

            } catch (Exception e) {

                Main.err("openPS2", e);

            }

        }

        

        public void showEditHistory(long j, int i) {

            final List<VaultDb.EditItem> edits;

            if (Main.db == null || (edits = Main.db.getEdits(j, i)) == null || edits.isEmpty()) {

                return;

            }

            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yy HH:mm:ss", Locale.getDefault());

            Main.ui(new Runnable() {

                @Override // java.lang.Runnable

                public final void run() {

                    HookManager.lambda$showEditHistory$7(edits, simpleDateFormat);

                }

            });

        }

        static void lambda$showEditHistory$7(List list, SimpleDateFormat simpleDateFormat) {

            Activity parentActivity;

            int i;

            ScrollView scrollView;

            String str;

            String str2;

            try {

                ChatActivity lastFragment = (ChatActivity) LaunchActivity.getLastFragment();

                if ((lastFragment instanceof ChatActivity) && (parentActivity = lastFragment.getParentActivity()) != null) {

                    int dp = AndroidUtilities.dp(4.0f);

                    int dp2 = AndroidUtilities.dp(8.0f);

                    int dp3 = AndroidUtilities.dp(12.0f);

                    int dp4 = AndroidUtilities.dp(16.0f);

                    boolean isCurrentThemeDark = Theme.isCurrentThemeDark();

                    int i2 = isCurrentThemeDark ? -14803410 : -1;

                    int i3 = isCurrentThemeDark ? -13948099 : -1509911;

                    int i4 = isCurrentThemeDark ? -13815216 : -1838339;

                    int i5 = isCurrentThemeDark ? -2039584 : -14606047;

                    int i6 = isCurrentThemeDark ? -7829334 : -9079435;

                    int i7 = isCurrentThemeDark ? -4473891 : -15374912;

                    int i8 = isCurrentThemeDark ? -14342856 : -657931;

                    LinearLayout linearLayout = new LinearLayout(parentActivity);

                    linearLayout.setOrientation(1);

                    linearLayout.setBackgroundColor(i2);

                    TextView textView = new TextView(parentActivity);

                    textView.setText("Edit history  ·  " + list.size() + " versions");

                    textView.setTextSize(1, 16.0f);

                    textView.setTypeface(null, 1);

                    textView.setTextColor(i7);

                    textView.setGravity(17);

                    textView.setPadding(dp4, dp4, dp4, dp3);

                    textView.setBackgroundColor(i8);

                    linearLayout.addView(textView, new LinearLayout.LayoutParams(-1, -2));

                    View view = new View(parentActivity);

                    view.setBackgroundColor(isCurrentThemeDark ? -13421739 : -2039584);

                    linearLayout.addView(view, new LinearLayout.LayoutParams(-1, 1));

                    ScrollView scrollView2 = new ScrollView(parentActivity);

                    scrollView2.setFillViewport(true);

                    LinearLayout linearLayout2 = new LinearLayout(parentActivity);

                    linearLayout2.setOrientation(1);

                    linearLayout2.setPadding(dp3, dp2, dp3, dp4);

                    int i9 = 0;

                    while (i9 < list.size()) {

                        VaultDb.EditItem editItem = (VaultDb.EditItem) list.get(i9);

                        boolean z = editItem.ver == 0;

                        LinearLayout linearLayout3 = new LinearLayout(parentActivity);

                        int i10 = i3;

                        linearLayout3.setOrientation(1);

                        GradientDrawable gradientDrawable = new GradientDrawable();

                        int i11 = i4;

                        gradientDrawable.setCornerRadius(AndroidUtilities.dp(18.0f));

                        gradientDrawable.setColor(z ? i11 : i10);

                        linearLayout3.setBackground(gradientDrawable);

                        linearLayout3.setPadding(dp4, dp3, dp4, dp3);

                        int i12 = dp3;

                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);

                        layoutParams.bottomMargin = dp2;

                        TextView textView2 = new TextView(parentActivity);

                        String str3 = z ? "✉  Original" : "✏  v" + editItem.ver;

                        int i13 = dp2;

                        LinearLayout linearLayout4 = linearLayout;

                        if (editItem.ts > 0) {

                            i = dp4;

                            scrollView = scrollView2;

                            str = simpleDateFormat.format(new Date(editItem.ts));

                        } else {

                            i = dp4;

                            scrollView = scrollView2;

                            str = "";

                        }

                        if (!str.isEmpty()) {

                            str3 = str3 + "  ·  " + str;

                        }

                        textView2.setText(str3);

                        textView2.setTextSize(1, 12.0f);

                        textView2.setTextColor(i6);

                        textView2.setTypeface(null, 1);

                        textView2.setPadding(0, 0, 0, dp);

                        linearLayout3.addView(textView2, new LinearLayout.LayoutParams(-1, -2));

                        TextView textView3 = new TextView(parentActivity);

                        if (editItem.text != null && !editItem.text.isEmpty()) {

                            str2 = editItem.text;

                            textView3.setText(str2);

                            textView3.setTextSize(1, 15.0f);

                            textView3.setTextColor(i5);

                            textView3.setLineSpacing(0.0f, 1.15f);

                            linearLayout3.addView(textView3, new LinearLayout.LayoutParams(-1, -2));

                            linearLayout2.addView(linearLayout3, layoutParams);

                            i9++;

                            dp4 = i;

                            dp2 = i13;

                            i3 = i10;

                            dp3 = i12;

                            i4 = i11;

                            linearLayout = linearLayout4;

                            scrollView2 = scrollView;

                        }

                        str2 = "[no text]";

                        textView3.setText(str2);

                        textView3.setTextSize(1, 15.0f);

                        textView3.setTextColor(i5);

                        textView3.setLineSpacing(0.0f, 1.15f);

                        linearLayout3.addView(textView3, new LinearLayout.LayoutParams(-1, -2));

                        linearLayout2.addView(linearLayout3, layoutParams);

                        i9++;

                        dp4 = i;

                        dp2 = i13;

                        i3 = i10;

                        dp3 = i12;

                        i4 = i11;

                        linearLayout = linearLayout4;

                        scrollView2 = scrollView;

                    }

                    LinearLayout linearLayout5 = linearLayout;

                    ScrollView scrollView3 = scrollView2;

                    scrollView3.addView(linearLayout2, new FrameLayout.LayoutParams(-1, -2));

                    linearLayout5.addView(scrollView3, new LinearLayout.LayoutParams(-1, 0, 1.0f));

                    try {

                        Class<?> cls = Class.forName("org.telegram.ui.ActionBar.BottomSheet$Builder");

                        Object newInstance = cls.getConstructor(Context.class).newInstance(parentActivity);

                        cls.getMethod("setCustomView", View.class).invoke(newInstance, linearLayout5);

                        Object invoke = cls.getMethod("create", new Class[0]).invoke(newInstance, new Object[0]);

                        linearLayout5.setMinimumHeight(Math.min((int) (AndroidUtilities.displaySize.y * 0.65f), AndroidUtilities.dp(400.0f)));

                        invoke.getClass().getMethod("show", new Class[0]).invoke(invoke, new Object[0]);

                    } catch (Exception e) {

                        new AlertDialog.Builder(parentActivity).setTitle("Edit history").setView(linearLayout5).setPositiveButton("OK", (DialogInterface.OnClickListener) null).show();

                    }

                }

            } catch (Exception e2) {

                Main.err("editHistory", e2);

            }

        }

        private void hkOutgoingParams() {

            Method[] declaredMethods;

            try {

                for (Method method : SendMessagesHelper.class.getDeclaredMethods()) {

                    if ("sendMessage".equals(method.getName())) {

                        Class<?>[] parameterTypes = method.getParameterTypes();

                        if (parameterTypes.length == 1 && parameterTypes[0].getSimpleName().contains("SendMessageParams")) {

                            this.hooks.add(XposedBridge.hookMethod(method, new XC_MethodHook() {

                                

                                

                                

                                

                                

                                

                                

                                

                                

                                

                                

                                

                                

                                public void beforeHookedMethod(de.robv.android.xposed.XC_MethodHook.MethodHookParam methodHookParam) {

                                    try {

                                        Object obj = methodHookParam.args[0];

                                        if (obj != null && Main.db != null) {

                                            Object replyToMsg = Main.gf(obj, "replyToMsg");

                                            Object replyQuote = Main.gf(obj, "replyQuote");

                                            Object replyToTopMsg = Main.gf(obj, "replyToTopMsg");

                                            MessageObject msgObj = replyToMsg instanceof MessageObject ? (MessageObject) replyToMsg : null;

                                            long dialogId = 0;

                                            int msgId = 0;

                                            if (msgObj != null) {

                                                dialogId = msgObj.getDialogId();

                                                msgId = msgObj.getId();

                                            } else if (replyQuote != null) {

                                                Object quoteMsg = Main.gf(replyQuote, "message");

                                                if (quoteMsg instanceof MessageObject) {

                                                    MessageObject qm = (MessageObject) quoteMsg;

                                                    dialogId = qm.getDialogId();

                                                    msgId = qm.getId();

                                                    msgObj = qm;

                                                }

                                            }

                                            if (dialogId != 0 && msgId != 0 && Main.db.isRem(dialogId, msgId)) {

                                                long senderId = msgObj != null ? msgObj.getSenderId() : 0;

                                                String senderName = "";

                                                if (senderId != 0) {

                                                    try {

                                                        Object user = MessagesController.getInstance(UserConfig.selectedAccount).getUser(Long.valueOf(senderId));

                                                        if (user != null) {

                                                            Object fn = Main.gf(user, "first_name");

                                                            Object ln = Main.gf(user, "last_name");

                                                            senderName = ((fn instanceof String ? (String) fn : "") + " " + (ln instanceof String ? (String) ln : "")).trim();

                                                            if (senderName.isEmpty()) senderName = "Unknown";

                                                        }

                                                    } catch (Exception e) {

                                                    }

                                                }

                                                String origText = (msgObj != null && msgObj.messageOwner != null && msgObj.messageOwner.message != null) ? msgObj.messageOwner.message : "";

                                                String combined = senderName + "\n" + origText + "\n";

                                                String field = "message";

                                                if (Main.gf(obj, "photo") == null && Main.gf(obj, "document") == null && Main.gf(obj, "path") == null) {

                                                    field = "message";

                                                }

                                                String existing = Main.gf(obj, field) instanceof String ? (String) Main.gf(obj, field) : "";

                                                StringBuilder sb = new StringBuilder().append(combined);

                                                if (existing != null) {

                                                    sb.append(existing);

                                                }

                                                Main.sf(obj, field, sb.toString());

                                                ArrayList entities = new ArrayList();

                                                try {

                                                    Object blockquote = Class.forName("org.telegram.tgnet.TLRPC$TL_messageEntityBlockquote").newInstance();

                                                    Main.sf(blockquote, "offset", 0);

                                                    Main.sf(blockquote, "length", Integer.valueOf(Math.max(0, combined.length() - 1)));

                                                    Main.sf(blockquote, "collapsed", true);

                                                    entities.add(blockquote);

                                                } catch (Exception e2) {

                                                }

                                                if (dialogId != 0 && Main.db.isRem(dialogId, msgId)) {

                                                    try {

                                                        ArrayList<Integer> mids = new ArrayList<>();

                                                    mids.add(msgId);

                                                    Main.db.markRemAsync(dialogId, mids, null, null);

                                                    } catch (Exception e3) {

                                                    }

                                                }

                                                Object existingEntities = Main.gf(obj, "entities");

                                                if (existingEntities instanceof ArrayList) {

                                                    entities.addAll((ArrayList) existingEntities);

                                                }

                                                Main.sf(obj, "entities", entities);

                                                if (replyToTopMsg != null) {

                                                    Main.sf(obj, "replyQuote", null);

                                                }

                                            }

                                        }

                                    } catch (Exception e4) {

                                        Main.err("hkOutP", e4);

                                    }

                                }

                            }));

                            return;

                        }

                    }

                }

            } catch (Exception e) {

                Main.err("hkOut", e);

            }

        }

        private boolean isOutMessage(long j, int i) {

            MessageObject findMessageInChat = Main.findMessageInChat(j, i);

            if (findMessageInChat != null) {

                return findMessageInChat.isOut();

            }

            MessageObject findMessageInController = Main.findMessageInController(i);

            if (findMessageInController != null) {

                return findMessageInController.isOut();

            }

            return false;

        }

        

        public long getCurrentDialogId() {

            try {

                BaseFragment lastFragment = LaunchActivity.getLastFragment();

                if (lastFragment instanceof ChatActivity) {

                    Field declaredField = ChatActivity.class.getDeclaredField("dialog_id");

                    declaredField.setAccessible(true);

                    return declaredField.getLong(lastFragment);

                }

                return 0L;

            } catch (Exception e) {

                return 0L;

            }

        }

        

        public long getReqPeerId(Object obj) {

            long longValue;

            try {

                Object gf = Main.gf(obj, "peer");

                if (gf instanceof TLRPC.Peer) {

                    return Main.peer2did((TLRPC.Peer) gf);

                }

                Object gf2 = Main.gf(gf, "user_id");

                Object gf3 = Main.gf(gf, "chat_id");

                Object gf4 = Main.gf(gf, "channel_id");

                if (gf2 instanceof Number) {

                    return ((Number) gf2).longValue();

                }

                if (gf3 instanceof Number) {

                    longValue = ((Number) gf3).longValue();

                } else if (!(gf4 instanceof Number)) {

                    return 0L;

                } else {

                    longValue = ((Number) gf4).longValue();

                }

                return -longValue;

            } catch (Exception e) {

                return 0L;

            }

        }

        

        public int getReqMsgId(Object obj) {

            try {

                Object gf = Main.gf(obj, "msg_id");

                if (gf instanceof Number) {

                    return ((Number) gf).intValue();

                }

                return 0;

            } catch (Exception e) {

                return 0;

            }

        }

        private Map<Integer, String> collectDeletedData(long j, ArrayList<Integer> arrayList) {

            HashMap hashMap = new HashMap();

            try {

                BaseFragment lastFragment = LaunchActivity.getLastFragment();

                if (lastFragment instanceof ChatActivity) {

                    Field declaredField = ChatActivity.class.getDeclaredField("messages");

                    declaredField.setAccessible(true);

                    Object obj = declaredField.get(lastFragment);

                    if (obj instanceof ArrayList) {

                        Iterator it = ((ArrayList) obj).iterator();

                        while (it.hasNext()) {

                            Object next = it.next();

                            if (next instanceof MessageObject) {

                                MessageObject messageObject = (MessageObject) next;

                                if (messageObject.getDialogId() == j && arrayList.contains(Integer.valueOf(messageObject.getId()))) {

                                    hashMap.put(Integer.valueOf(messageObject.getId()), extractText(messageObject));

                                }

                            }

                        }

                    }

                }

            } catch (Exception e) {

            }

            try {

                Object gf = Main.gf(MessagesController.getInstance(UserConfig.selectedAccount), "dialogMessagesByIds");

                if (gf != null) {

                    Iterator<Integer> it2 = arrayList.iterator();

                    while (it2.hasNext()) {

                        int intValue = it2.next().intValue();

                        if (!hashMap.containsKey(Integer.valueOf(intValue))) {

                            Object mapGet = Main.mapGet(gf, intValue);

                            if (mapGet instanceof MessageObject) {

                                hashMap.put(Integer.valueOf(intValue), extractText((MessageObject) mapGet));

                            }

                        }

                    }

                }

            } catch (Exception e2) {

            }

            return hashMap;

        }

        private void hkFlag() {

            try {

                this.hooks.add(XposedBridge.hookMethod(FlagSecureReason.class.getDeclaredMethod("attach", new Class[0]), new XC_MethodHook() {

                    public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {

                        try {

                            if (Main.cfg.b("allow_screenshots", false)) {

                                methodHookParam.setResult((Object) null);

                            }

                        } catch (Exception ignored) {

                        }

                    }

                }));

            } catch (Throwable ignored) {

            }

            try {

                Class<?> wmClass = Class.forName("android.view.WindowManagerImpl");

                this.hooks.add(XposedBridge.hookMethod(wmClass.getDeclaredMethod("addView", View.class, ViewGroup.LayoutParams.class), new XC_MethodHook() {

                    public void beforeHookedMethod(XC_MethodHook.MethodHookParam param) {

                        try {

                            if (Main.cfg.b("allow_screenshots", false) && param.args[1] instanceof WindowManager.LayoutParams) {

                                ((WindowManager.LayoutParams) param.args[1]).flags &= ~8192;

                            }

                        } catch (Exception ignored) {}

                    }

                }));

            } catch (Throwable ignored) {

            }

            try {

                this.hooks.add(XposedBridge.hookMethod(Window.class.getDeclaredMethod("setFlags", Integer.TYPE, Integer.TYPE), new XC_MethodHook() {

                    public void beforeHookedMethod(XC_MethodHook.MethodHookParam param) {

                        try {

                            if (Main.cfg.b("allow_screenshots", false)) {

                                int mask = ((Integer) param.args[1]).intValue();

                                if ((mask & 8192) != 0) {

                                    int flags = ((Integer) param.args[0]).intValue();

                                    param.args[0] = Integer.valueOf(flags & ~8192);

                                }

                            }

                        } catch (Exception ignored) {

                        }

                    }

                }));

            } catch (Throwable ignored) {

            }

            try {

                this.hooks.add(XposedBridge.hookMethod(Window.class.getDeclaredMethod("addFlags", Integer.TYPE), new XC_MethodHook() {

                    public void beforeHookedMethod(XC_MethodHook.MethodHookParam param) {

                        try {

                            if (Main.cfg.b("allow_screenshots", false)) {

                                int flags = ((Integer) param.args[0]).intValue();

                                if ((flags & 8192) != 0) {

                                    param.args[0] = Integer.valueOf(flags & ~8192);

                                }

                            }

                        } catch (Exception ignored) {

                        }

                    }

                }));

            } catch (Throwable ignored) {

            }

        }

        private void hkPrem() {

            Method[] declaredMethods;

            try {

                this.hooks.add(XposedBridge.hookMethod(UserConfig.class.getDeclaredMethod("isPremium", new Class[0]), new XC_MethodHook() {

                    public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {

                        Object gf = null;

                        if (Main.cfg.b("fake_premium", false)) {

                            try {

                                UserConfig userConfig = (UserConfig) methodHookParam.thisObject;

                                TLRPC.User currentUser = userConfig.getCurrentUser();

                                if (currentUser == null || (gf = Main.gf(currentUser, "id")) == null || ((Number) gf).longValue() != userConfig.getClientUserId()) {

                                    return;

                                }

                                methodHookParam.setResult(true);

                            } catch (Exception e) {

                            }

                        }

                    }

                }));

            } catch (Exception e) {

            }

            try {

                this.hooks.add(XposedBridge.hookMethod(UserConfig.class.getDeclaredMethod("hasPremium", new Class[0]), new XC_MethodHook() {

                    public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {

                        Object gf = null;

                        if (Main.cfg.b("fake_premium", false)) {

                            try {

                                UserConfig userConfig = (UserConfig) methodHookParam.thisObject;

                                TLRPC.User currentUser = userConfig.getCurrentUser();

                                if (currentUser == null || (gf = Main.gf(currentUser, "id")) == null || ((Number) gf).longValue() != userConfig.getClientUserId()) {

                                    return;

                                }

                                methodHookParam.setResult(true);

                            } catch (Exception e2) {

                            }

                        }

                    }

                }));

            } catch (Exception e2) {

            }

            try {

                this.hooks.add(XposedBridge.hookMethod(UserConfig.class.getDeclaredMethod("getCurrentUser", new Class[0]), new XC_MethodHook() {

                    public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {

                        Object gf = null;

                        if (Main.cfg.b("fake_premium", false)) {

                            try {

                                Object result = methodHookParam.getResult();

                                if (result == null || (gf = Main.gf(result, "id")) == null || ((Number) gf).longValue() != UserConfig.getInstance(UserConfig.selectedAccount).getClientUserId()) {

                                    return;

                                }

                                Main.setPremium(result, true);

                            } catch (Exception e3) {

                            }

                        }

                    }

                }));

            } catch (Exception e3) {

            }

            try {

                this.hooks.add(XposedBridge.hookMethod(UserConfig.class.getDeclaredMethod("setCurrentUser", Class.forName("org.telegram.tgnet.TLRPC$User")), new XC_MethodHook() {

                    public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {

                        Object gf = null;

                        if (Main.cfg.b("fake_premium", false)) {

                            try {

                                Object obj = methodHookParam.args[0];

                                if (obj == null || (gf = Main.gf(obj, "id")) == null || ((Number) gf).longValue() != UserConfig.getInstance(UserConfig.selectedAccount).getClientUserId()) {

                                    return;

                                }

                                Main.setPremium(obj, true);

                            } catch (Exception e4) {

                            }

                        }

                    }

                }));

            } catch (Exception e4) {

            }

            try {

                for (Method method : MessagesController.class.getDeclaredMethods()) {

                    if ("putUser".equals(method.getName()) && method.getParameterTypes().length == 2) {

                        this.hooks.add(XposedBridge.hookMethod(method, new XC_MethodHook() {

                            public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {

                                Object gf = null;

                                if (Main.cfg.b("fake_premium", false)) {

                                    try {

                                        Object obj = methodHookParam.args[0];

                                        if (obj == null || (gf = Main.gf(obj, "id")) == null || ((Number) gf).longValue() != UserConfig.getInstance(UserConfig.selectedAccount).getClientUserId()) {

                                            return;

                                        }

                                        Main.setPremium(obj, true);

                                    } catch (Exception e5) {

                                    }

                                }

                            }

                        }));

                        return;

                    }

                }

            } catch (Exception e5) {

            }

        }

        private void hkOnce() {

            try {

                this.hooks.add(XposedBridge.hookMethod(ChatActivity.class.getDeclaredMethod("sendSecretMediaDelete", MessageObject.class), new XC_MethodHook() {

                    public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {

                        if (Main.cfg.b("keep_once", false)) {

                            methodHookParam.setResult((Object) null);

                        }

                    }

                }));

            } catch (Exception e) {

            }

            try {

                this.hooks.add(XposedBridge.hookMethod(ChatActivity.class.getDeclaredMethod("sendSecretMessageRead", MessageObject.class, Boolean.TYPE), new XC_MethodHook() {

                    public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {

                        if (Main.cfg.b("keep_once", false)) {

                            methodHookParam.setResult((Object) null);

                        }

                    }

                }));

            } catch (Exception e2) {

            }

            try {

                this.hooks.add(XposedBridge.hookMethod(SecretVoicePlayer.class.getDeclaredMethod("dismiss", new Class[0]), new XC_MethodHook() {

                    public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {

                        if (Main.cfg.b("keep_once", false)) {

                            try {

                                Main.sf(methodHookParam.thisObject, "closeAction", null);

                            } catch (Exception e3) {

                            }

                        }

                    }

                }));

            } catch (Exception e3) {

            }

        }

        private void hkCheckDel() {

            try {

                this.hooks.add(XposedBridge.hookMethod(MessagesController.class.getDeclaredMethod("checkDeletingTask", Boolean.TYPE), new XC_MethodHook() {

                    public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {

                        if (Main.cfg.b("keep_once", false)) {

                            methodHookParam.setResult(false);

                        }

                    }

                }));

            } catch (Exception e) {

            }

        }

        private void hkNotif() {

            try {

                this.hooks.add(XposedBridge.hookMethod(NotificationsController.class.getDeclaredMethod("removeDeletedMessagesFromNotifications", LongSparseArray.class, Boolean.TYPE), new XC_MethodHook() {

                    public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {

                        if (!Main.cfg.b("save_deleted", true) || ((Boolean) methodHookParam.args[1]).booleanValue()) {

                            return;

                        }

                        methodHookParam.setResult((Object) null);

                    }

                }));

            } catch (Exception e) {

            }

        }

        private void hkNewMsg() {

            try {

                this.hooks.add(XposedBridge.hookMethod(ChatActivity.class.getDeclaredMethod("processNewMessages", ArrayList.class, Boolean.TYPE), new XC_MethodHook() {

                    public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {

                        try {

                            boolean z = false;

                            ArrayList arrayList = (ArrayList) methodHookParam.args[0];

                            if (arrayList == null) {

                                return;

                            }

                            synchronized (HookManager.this.rxRules) {

                                if (Main.cfg.b("filters_enabled", true) && !HookManager.this.rxRules.isEmpty()) {

                                    Iterator it = HookManager.this.rxRules.iterator();

                                    while (true) {

                                        if (it.hasNext()) {

                                            if (((FilterRule) it.next()).channelOnly) {

                                                z = HookManager.this.isCurrentChannel();

                                                break;

                                            }

                                        } else {

                                            break;

                                        }

                                    }

                                    for (int size = arrayList.size() - 1; size >= 0; size--) {

                                        MessageObject messageObject = (MessageObject) arrayList.get(size);

                                        if (messageObject != null && !messageObject.isOut() && messageObject.messageOwner != null) {

                                            String str = messageObject.messageOwner.message;

                                            if (str == null) {

                                                str = "";

                                            }

                                            Iterator it2 = HookManager.this.rxRules.iterator();

                                            while (true) {

                                                if (it2.hasNext()) {

                                                    FilterRule filterRule = (FilterRule) it2.next();

                                                    try {

                                                        if (!filterRule.channelOnly || z) {

                                                            boolean find = filterRule.pattern.matcher(str).find();

                                                            if (filterRule.inverted) {

                                                                find = !find;

                                                            }

                                                            if (find) {

                                                                arrayList.remove(size);

                                                                break;

                                                            }

                                                        }

                                                    } catch (Exception e) {

                                                    }

                                                }

                                            }

                                        }

                                    }

                                }

                            }

                        } catch (Exception e2) {

                        }

                    }

                }));

            } catch (Exception e) {

            }

        }

        private void hkStorySave() {

            try {

                this.hooks.add(XposedBridge.hookMethod(Class.forName("org.telegram.ui.Stories.PeerStoriesView$StoryItemHolder").getDeclaredMethod("allowScreenshots", new Class[0]), new XC_MethodHook() {

                    public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {

                        if (Main.cfg.b("bypass_story_save", false)) {

                            methodHookParam.setResult(true);

                        }

                    }

                }));

            } catch (Exception e) {

            }

            try {

                this.hooks.add(XposedBridge.hookMethod(Class.forName("org.telegram.ui.Stories.StoryViewer").getDeclaredMethod("allowScreenshots", Boolean.TYPE), new XC_MethodHook() {

                    public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {

                        if (Main.cfg.b("bypass_story_save", false)) {

                            methodHookParam.args[0] = true;

                        }

                    }

                }));

            } catch (Exception e2) {

            }

        }

        // re:extera FormatUserStatus: при включённом "Сохранять последний онлайн"

        // скрытые статусы (tr("был(а) недавно","was recently") / tr("на прошлой неделе","last week") / tr("давно","long ago"))

        // заменяются на tr("~ был(а) в HH:MM","~ last seen at HH:MM") — время, когда плагин последний раз

        // видел пользователя в сети. Статус подменяется на точный Offline на

        // время форматирования и восстанавливается сразу после.

        private static final ThreadLocal<Object> lastOnlineSwap = new ThreadLocal<>();

        private void hkFormatUserStatus() {
            try {
                Method method = LocaleController.class.getDeclaredMethod("formatUserStatus", Integer.TYPE, TLRPC.User.class, boolean[].class, boolean[].class, boolean[].class);
                method.setAccessible(true);
                this.hooks.add(XposedBridge.hookMethod(method, new XC_MethodHook() {
                    @Override
                    public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                        try {
                            if (!Main.cfg.b("save_last_online", false)) {
                                return;
                            }
                            if (methodHookParam.args == null || methodHookParam.args.length < 2 || !(methodHookParam.args[1] instanceof TLRPC.User)) {
                                return;
                            }
                            TLRPC.User user = (TLRPC.User) methodHookParam.args[1];
                            if (user == null || user.status == null) {
                                return;
                            }
                            boolean isHidden = (user.status instanceof TLRPC.TL_userStatusRecently)
                                    || (user.status instanceof TLRPC.TL_userStatusLastWeek)
                                    || (user.status instanceof TLRPC.TL_userStatusLastMonth)
                                    || (user.status instanceof TLRPC.TL_userStatusEmpty);
                            if (!isHidden) {
                                return;
                            }
                            Long ts = presenceLastSeen.get(Long.valueOf(user.id));
                            if (ts == null || ts.longValue() <= 0) {
                                return;
                            }
                            lastOnlineSwap.set(user.status);
                            int timeSec = (int) (ts.longValue() > 10000000000L ? (ts.longValue() / 1000L) : ts.longValue());
                            TLRPC.TL_userStatusOffline exact = new TLRPC.TL_userStatusOffline();
                            exact.expires = timeSec;
                                                        user.status = exact;
                        } catch (Throwable t) {}
                    }

                    @Override
                    public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                        try {
                            Object orig = lastOnlineSwap.get();
                            if (orig == null) {
                                return;
                            }
                            lastOnlineSwap.remove();
                            if (methodHookParam.args == null || methodHookParam.args.length < 2 || !(methodHookParam.args[1] instanceof TLRPC.User)) {
                                return;
                            }
                            TLRPC.User user = (TLRPC.User) methodHookParam.args[1];
                            if (user != null) {
                                user.status = (TLRPC.UserStatus) orig;
                            }
                            Object res = methodHookParam.getResult();
                            if (res != null) {
                                methodHookParam.setResult("~ " + res);
                            }
                        } catch (Throwable t) {}
                    }
                }));
            } catch (Throwable t) {
                Main.err("hkFormatUserStatus", new Exception(t));
            }
        }

        private void hkUpdDlg() {
            // Disabled in accordance with re:extera v2 (not needed, avoids interference)
        }

        private void hkFwdMsg() {

            Method[] declaredMethods;

            try {

                for (Method method : SendMessagesHelper.class.getDeclaredMethods()) {

                    if ("sendMessage".equals(method.getName())) {

                        Class<?>[] parameterTypes = method.getParameterTypes();

                        if (parameterTypes.length >= 7 && parameterTypes[0] == ArrayList.class && parameterTypes[1] == Long.TYPE) {

                            this.hooks.add(XposedBridge.hookMethod(method, new XC_MethodHook() {

                                public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {

                                    if (Main.cfg.b("bypass_noforward", false)) {

                                        try {

                                            ArrayList arrayList = (ArrayList) methodHookParam.args[0];

                                            if (arrayList != null) {

                                                Iterator it = arrayList.iterator();

                                                while (it.hasNext()) {

                                                    MessageObject messageObject = (MessageObject) it.next();

                                                    if (messageObject != null && messageObject.messageOwner != null) {

                                                        messageObject.messageOwner.noforwards = false;

                                                    }

                                                }

                                            }

                                        } catch (Exception e) {

                                        }

                                    }

                                }

                            }));

                            return;

                        }

                    }

                }

            } catch (Exception e) {

            }

        }

        private void hkReForward() {

            Method[] declaredMethods;

            try {

                for (Method method : SendMessagesHelper.class.getDeclaredMethods()) {

                    if ("sendMessage".equals(method.getName())) {

                        Class<?>[] parameterTypes = method.getParameterTypes();

                        if (parameterTypes.length >= 7 && parameterTypes[0] == ArrayList.class && parameterTypes[1] == Long.TYPE) {

                            this.hooks.add(XposedBridge.hookMethod(method, new XC_MethodHook() {

                                public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {

                                    if (Main.cfg.b("bypass_noforward", false) && Main.cfg.i("fwd_mode", 0) != 1) {

                                        try {

                                            ArrayList arrayList = (ArrayList) methodHookParam.args[0];

                                            if (arrayList != null && !arrayList.isEmpty()) {

                                                ArrayList arrayList2 = new ArrayList();

                                                ArrayList arrayList3 = new ArrayList();

                                                Iterator it = arrayList.iterator();

                                                while (it.hasNext()) {

                                                    MessageObject messageObject = (MessageObject) it.next();

                                                    if (messageObject == null || messageObject.messageOwner == null || !(messageObject.messageOwner.noforwards || messageObject.isSecret() || messageObject.isSecretMedia())) {

                                                        arrayList3.add(messageObject);

                                                    } else {

                                                        arrayList2.add(messageObject);

                                                    }

                                                }

                                                if (arrayList2.isEmpty()) {

                                                    return;

                                                }

                                                if (arrayList3.isEmpty()) {

                                                    methodHookParam.setResult(0);

                                                } else {

                                                    methodHookParam.args[0] = arrayList3;

                                                }

                                            }

                                        } catch (Exception e) {

                                        }

                                    }

                                }

                            }));

                            return;

                        }

                    }

                }

            } catch (Exception e) {

            }

        }

        public void onUnload() {

            if (this.sendReqHook != null) {

                this.sendReqHook.unhook();

            }

            Iterator<XC_MethodHook.Unhook> it = this.hooks.iterator();

            while (it.hasNext()) {

                try {

                    it.next().unhook();

                } catch (Exception e) {

                }

            }

            this.hooks.clear();

        }

    }

    public static boolean pyIsRemoved(long j, int i) {

        return db != null && db.isRem(j, i);

    }

    public static boolean pyHasEdits(long j, int i) {

        return db != null && db.hasEdits(j, i);

    }

    public static void pyWipeAll() {

        if (db != null) {

            db.wipe();

        }

    }

    public static boolean pyGetBool(String str, boolean z) {

        return cfg != null ? cfg.b(str, z) : z;

    }

    public static void pySetBool(String str, boolean z) {

        if (cfg != null) {

            cfg.set(str, z);

        }

        if ("keep_app_alive".equals(str)) {

            updateKeepAlive();

        }

        if ("fake_premium".equals(str)) {

            if (z) {

                applyLocalPremium();

            } else {

                revertPremium();

            }

        }

    }

    public static int pyGetInt(String str, int i) {

        return cfg != null ? cfg.i(str, i) : i;

    }

    public static void pySetInt(String str, int i) {

        if (cfg != null) {

            cfg.set(str, i);

        }

    }

    public static void pyToggleGhost() {

        toggleGhost();

    }

    public static boolean pyIsGhost() {

        return isGhost();

    }

    public static ArrayList<Long> pyGetReadWhitelist() {

        return db != null ? db.getReadWhitelist() : new ArrayList<>();

    }

    public static boolean pyAddReadWhitelist(long j) {

        return db != null && db.addReadWhitelist(j);

    }

    public static boolean pyRemoveReadWhitelist(long j) {

        return db != null && db.removeReadWhitelist(j);

    }

    public static ArrayList<String> pyGetRegexPatterns() {

        return db != null ? db.getRegex() : new ArrayList<>();

    }

    public static boolean pyAddRegex(String str) {

        boolean z = db != null && db.addRegex(str);

        if (z && hm != null) {

            hm.rebuildRegex();

        }

        return z;

    }

    public static boolean pyRemoveRegex(String str) {

        boolean z = db != null && db.removeRegex(str);

        if (z && hm != null) {

            hm.rebuildRegex();

        }

        return z;

    }

    public static void pyRebuildRegex() {

        if (hm != null) {

            hm.rebuildRegex();

        }

    }

    public static ArrayList<Long> pyGetPresenceTracked() {

        return new ArrayList<>(presenceTracked);

    }

    public static void pyAddPresenceTracked(long j) {

        if (j > 0) {

            presenceTracked.add(Long.valueOf(j));

            presenceDirty = true;

        }

    }

    public static void pyRemovePresenceTracked(long j) {

        presenceTracked.remove(Long.valueOf(j));

        presenceDirty = true;

    }

    public static ArrayList<String> pyGetPresenceRecords() {

        ArrayList<String> arrayList = new ArrayList<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yy HH:mm:ss", Locale.getDefault());

        for (Map.Entry<Long, ArrayList<long[]>> entry : presenceRecords.entrySet()) {

            long longValue = entry.getKey().longValue();

            ArrayList<long[]> value = entry.getValue();

            if (value != null) {

                for (int size = value.size() - 1; size >= 0 && arrayList.size() < 200; size--) {

                    long[] jArr = value.get(size);

                    arrayList.add("id" + longValue + " · " + simpleDateFormat.format(new Date(jArr[0] * 1000)) + " - " + simpleDateFormat.format(new Date(jArr[1] * 1000)));

                }

            }

        }

        return arrayList;

    }

    public static void pyWipePresence() {

        presenceRecords.clear();

        presenceLamp.clear();

        presenceOpen.clear();

        presenceDirty = true;

        savePresence();

    }

}

