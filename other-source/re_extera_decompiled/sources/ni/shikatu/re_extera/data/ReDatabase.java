package ni.shikatu.re_extera.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.File;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import ni.shikatu.re_extera.ReLog;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DispatchQueue;

/* JADX INFO: compiled from: ReDatabase.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u0000 \u00182\u00020\u0001:\u0001\u0018B\u0011\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u001f\u0010\b\u001a\u00020\t2\u0017\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\t0\u000b¢\u0006\u0002\b\rJ9\u0010\u000e\u001a\u00020\t\"\u0004\b\u0000\u0010\u000f2\u0017\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u0002H\u000f0\u000b¢\u0006\u0002\b\r2\u0012\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u0002H\u000f\u0012\u0004\u0012\u00020\t0\u000bJ\u001f\u0010\u0011\u001a\u00020\t2\u0017\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\t0\u000b¢\u0006\u0002\b\rJ\u0010\u0010\u0012\u001a\u00020\t2\u0006\u0010\u0013\u001a\u00020\fH\u0016J \u0010\u0014\u001a\u00020\t2\u0006\u0010\u0013\u001a\u00020\f2\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0016H\u0016R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0019"}, d2 = {"Lni/shikatu/re_extera/data/ReDatabase;", "Landroid/database/sqlite/SQLiteOpenHelper;", "context", "Landroid/content/Context;", "<init>", "(Landroid/content/Context;)V", "queue", "Lorg/telegram/messenger/DispatchQueue;", "write", "", "block", "Lkotlin/Function1;", "Landroid/database/sqlite/SQLiteDatabase;", "Lkotlin/ExtensionFunctionType;", "read", "T", "onResult", "post", "onCreate", "db", "onUpgrade", "oldVersion", "", "newVersion", "Companion", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class ReDatabase extends SQLiteOpenHelper {
    private static final String CREATE_DELETED = "CREATE TABLE IF NOT EXISTS deleted_keys(did INTEGER NOT NULL, mid INTEGER NOT NULL, ts INTEGER NOT NULL, PRIMARY KEY(did, mid))";
    private static final String CREATE_EDITS = "CREATE TABLE IF NOT EXISTS message_edits(did INTEGER NOT NULL, mid INTEGER NOT NULL, ver INTEGER NOT NULL, date INTEGER NOT NULL, data BLOB NOT NULL, PRIMARY KEY(did, mid, ver))";
    private static final String CREATE_EXCLUSIONS = "CREATE TABLE IF NOT EXISTS exception_users(did INTEGER NOT NULL, exception_reading INTEGER DEFAULT 0 NOT NULL, exception_typing INTEGER DEFAULT 0 NOT NULL, PRIMARY KEY(did))";
    private static final String CREATE_REGEX = "CREATE TABLE IF NOT EXISTS regex_filters(regex TEXT NOT NULL, PRIMARY KEY(regex))";
    private static final String CREATE_SHADOWBAN = "CREATE TABLE IF NOT EXISTS shadowban_users(user_id INTEGER PRIMARY KEY, hide_dialog INTEGER DEFAULT 1, hide_in_groups INTEGER DEFAULT 1, added_ts INTEGER NOT NULL)";

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final String NAME = "re_extera.db";
    private static final int VERSION = 8;
    private static volatile ReDatabase instance;
    private final DispatchQueue queue;

    public /* synthetic */ ReDatabase(Context context, DefaultConstructorMarker defaultConstructorMarker) {
        this(context);
    }

    private ReDatabase(Context context) {
        super(context, new File(context.getFilesDir(), NAME).getAbsolutePath(), (SQLiteDatabase.CursorFactory) null, 8);
        this.queue = new DispatchQueue("re_extera_db");
    }

    public final void write(final Function1<? super SQLiteDatabase, Unit> block) {
        Intrinsics.checkNotNullParameter(block, "block");
        this.queue.postRunnable(new Runnable() { // from class: ni.shikatu.re_extera.data.ReDatabase.2
            @Override // java.lang.Runnable
            public final void run() {
                ReDatabase.write$lambda$0(ReDatabase.this, block);
            }
        });
    }

    static final void write$lambda$0(ReDatabase reDatabase, Function1 function1) {
        try {
            SQLiteDatabase writableDatabase = reDatabase.getWritableDatabase();
            Intrinsics.checkNotNullExpressionValue(writableDatabase, "getWritableDatabase(...)");
            writableDatabase.beginTransaction();
            try {
                function1.invoke(writableDatabase);
                writableDatabase.setTransactionSuccessful();
            } finally {
                writableDatabase.endTransaction();
            }
        } catch (Exception e) {
            ReLog.INSTANCE.e("Database write failed", e);
        }
    }

    public final <T> void read(final Function1<? super SQLiteDatabase, ? extends T> block, final Function1<? super T, Unit> onResult) {
        Intrinsics.checkNotNullParameter(block, "block");
        Intrinsics.checkNotNullParameter(onResult, "onResult");
        this.queue.postRunnable(new Runnable() { // from class: ni.shikatu.re_extera.data.ReDatabase.1
            @Override // java.lang.Runnable
            public final void run() {
                ReDatabase.read$lambda$2(block, this, onResult);
            }
        });
    }

    static final void read$lambda$2(Function1 function1, ReDatabase reDatabase, final Function1 function2) {
        final Object objInvoke;
        try {
            SQLiteDatabase readableDatabase = reDatabase.getReadableDatabase();
            Intrinsics.checkNotNullExpressionValue(readableDatabase, "getReadableDatabase(...)");
            objInvoke = function1.invoke(readableDatabase);
        } catch (Exception e) {
            ReLog.INSTANCE.e("Database read failed", e);
            objInvoke = null;
        }
        if (objInvoke != null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: ni.shikatu.re_extera.data.ReDatabase.0
                @Override // java.lang.Runnable
                public final void run() {
                    function2.invoke(objInvoke);
                }
            });
        }
    }

    public final void post(final Function1<? super SQLiteDatabase, Unit> block) {
        Intrinsics.checkNotNullParameter(block, "block");
        this.queue.postRunnable(new Runnable() { // from class: ni.shikatu.re_extera.data.ReDatabase.3
            @Override // java.lang.Runnable
            public final void run() {
                ReDatabase.post$lambda$3(block, this);
            }
        });
    }

    static final void post$lambda$3(Function1 function1, ReDatabase reDatabase) {
        try {
            SQLiteDatabase writableDatabase = reDatabase.getWritableDatabase();
            Intrinsics.checkNotNullExpressionValue(writableDatabase, "getWritableDatabase(...)");
            function1.invoke(writableDatabase);
        } catch (Exception e) {
            ReLog.INSTANCE.e("Database task failed", e);
        }
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase db) {
        Intrinsics.checkNotNullParameter(db, "db");
        db.execSQL(CREATE_DELETED);
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_deleted_ts ON deleted_keys(ts)");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_deleted_did ON deleted_keys(did)");
        db.execSQL(CREATE_EDITS);
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_edits_did_mid ON message_edits(did, mid)");
        db.execSQL(CREATE_EXCLUSIONS);
        db.execSQL(CREATE_REGEX);
        db.execSQL(CREATE_SHADOWBAN);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Intrinsics.checkNotNullParameter(db, "db");
        if (oldVersion < 3) {
            db.execSQL("DROP TABLE IF EXISTS message_edits");
            db.execSQL(CREATE_EDITS);
            db.execSQL("CREATE INDEX IF NOT EXISTS idx_edits_did_mid ON message_edits(did, mid)");
        }
        if (oldVersion < 4) {
            db.execSQL(CREATE_EXCLUSIONS);
        }
        if (oldVersion < 7) {
            db.execSQL(CREATE_REGEX);
        }
        if (oldVersion < 8) {
            db.execSQL(CREATE_SHADOWBAN);
        }
    }

    /* JADX INFO: compiled from: ReDatabase.kt */
    @Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0012\u001a\u00020\u000e2\u0006\u0010\u0013\u001a\u00020\u0014J\u0006\u0010\u0015\u001a\u00020\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u000f\u001a\u00020\u000e8F¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011¨\u0006\u0017"}, d2 = {"Lni/shikatu/re_extera/data/ReDatabase$Companion;", "", "<init>", "()V", "NAME", "", "VERSION", "", "CREATE_DELETED", "CREATE_EDITS", "CREATE_EXCLUSIONS", "CREATE_REGEX", "CREATE_SHADOWBAN", "instance", "Lni/shikatu/re_extera/data/ReDatabase;", "db", "getDb", "()Lni/shikatu/re_extera/data/ReDatabase;", "init", "context", "Landroid/content/Context;", "close", "", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final ReDatabase getDb() {
            ReDatabase reDatabase = ReDatabase.instance;
            if (reDatabase != null) {
                return reDatabase;
            }
            throw new IllegalStateException("ReDatabase is not initialized".toString());
        }

        public final synchronized ReDatabase init(Context context) {
            ReDatabase reDatabase;
            Intrinsics.checkNotNullParameter(context, "context");
            reDatabase = ReDatabase.instance;
            if (reDatabase == null) {
                Context applicationContext = context.getApplicationContext();
                Intrinsics.checkNotNullExpressionValue(applicationContext, "getApplicationContext(...)");
                reDatabase = new ReDatabase(applicationContext, null);
                Companion companion = ReDatabase.INSTANCE;
                ReDatabase.instance = reDatabase;
            }
            return reDatabase;
        }

        public final synchronized void close() {
            ReDatabase reDatabase = ReDatabase.instance;
            if (reDatabase != null) {
                reDatabase.close();
            }
            ReDatabase.instance = null;
        }
    }
}
