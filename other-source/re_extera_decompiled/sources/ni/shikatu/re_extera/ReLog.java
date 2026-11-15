package ni.shikatu.re_extera;

import android.util.Log;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;

/* JADX INFO: compiled from: ReLog.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u0003\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0005J\u001a\u0010\t\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00052\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000bR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000¨\u0006\f"}, d2 = {"Lni/shikatu/re_extera/ReLog;", "", "<init>", "()V", "TAG", "", "d", "", "message", "e", "throwable", "", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class ReLog {
    public static final ReLog INSTANCE = new ReLog();
    private static final String TAG = "re:extera";

    private ReLog() {
    }

    public final void d(String message) {
        Intrinsics.checkNotNullParameter(message, "message");
        if (BuildVars.LOGS_ENABLED) {
            Log.d(TAG, message);
            FileLog.d("[re:extera] " + message);
        }
    }

    public static /* synthetic */ void e$default(ReLog reLog, String str, Throwable th, int i, Object obj) {
        if ((i & 2) != 0) {
            th = null;
        }
        reLog.e(str, th);
    }

    public final void e(String message, Throwable throwable) {
        Intrinsics.checkNotNullParameter(message, "message");
        Log.e(TAG, message, throwable);
        FileLog.e("[re:extera] " + message, throwable);
    }
}
