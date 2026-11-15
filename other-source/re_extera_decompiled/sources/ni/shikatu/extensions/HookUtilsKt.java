package ni.shikatu.extensions;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import java.lang.reflect.Member;
import java.util.Arrays;
import java.util.Set;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import ni.shikatu.re_extera.ReLog;

/* JADX INFO: compiled from: HookUtils.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000`\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\u001a3\u0010\u0002\u001a\u0004\u0018\u00010\u0003*\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u00032\u0016\u0010\u0006\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010\u00030\u0007\"\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\b\u001a\n\u0010\t\u001a\u00020\n*\u00020\u0004\u001a!\u0010\r\u001a\u0002H\u000e\"\u0004\b\u0000\u0010\u000e*\u00060\u0001j\u0002`\u000f2\u0006\u0010\u0010\u001a\u00020\u0011¢\u0006\u0002\u0010\u0012\u001a\u0019\u0010\u0013\u001a\u0002H\u000e\"\u0004\b\u0000\u0010\u000e*\u00060\u0001j\u0002`\u000f¢\u0006\u0002\u0010\u0014\u001a\u0016\u0010\u0015\u001a\u00060\u0016R\u00020\u0017*\u00020\u00042\u0006\u0010\u0015\u001a\u00020\u0017\u001a3\u0010\u0018\u001a\u00060\u0016R\u00020\u0017*\u00020\u00042\u001d\b\u0004\u0010\u0019\u001a\u0017\u0012\b\u0012\u00060\u0001j\u0002`\u000f\u0012\u0004\u0012\u00020\u001b0\u001a¢\u0006\u0002\b\u001cH\u0086\bø\u0001\u0000\u001a3\u0010\u001d\u001a\u00060\u0016R\u00020\u0017*\u00020\u00042\u001d\b\u0004\u0010\u0019\u001a\u0017\u0012\b\u0012\u00060\u0001j\u0002`\u000f\u0012\u0004\u0012\u00020\u001b0\u001a¢\u0006\u0002\b\u001cH\u0086\bø\u0001\u0000\u001a5\u0010\u001e\u001a\u00060\u0016R\u00020\u0017*\u00020\u00042\u001f\b\u0004\u0010\u0019\u001a\u0019\u0012\b\u0012\u00060\u0001j\u0002`\u000f\u0012\u0006\u0012\u0004\u0018\u00010\u00030\u001a¢\u0006\u0002\b\u001cH\u0086\bø\u0001\u0000\u001a\u0018\u0010\u001f\u001a\u00060\u0016R\u00020\u0017*\u00020\u00042\b\u0010 \u001a\u0004\u0018\u00010\u0003\u001a\u000e\u0010!\u001a\u00060\u0016R\u00020\u0017*\u00020\u0004\u001a(\u0010\u0015\u001a\f\u0012\b\u0012\u00060\u0016R\u00020\u00170\"*\u0006\u0012\u0002\b\u00030#2\u0006\u0010$\u001a\u00020%2\u0006\u0010\u0015\u001a\u00020\u0017\u001aE\u0010\u0018\u001a\f\u0012\b\u0012\u00060\u0016R\u00020\u00170\"*\u0006\u0012\u0002\b\u00030#2\u0006\u0010$\u001a\u00020%2\u001d\b\u0004\u0010\u0019\u001a\u0017\u0012\b\u0012\u00060\u0001j\u0002`\u000f\u0012\u0004\u0012\u00020\u001b0\u001a¢\u0006\u0002\b\u001cH\u0086\bø\u0001\u0000\u001aE\u0010\u001d\u001a\f\u0012\b\u0012\u00060\u0016R\u00020\u00170\"*\u0006\u0012\u0002\b\u00030#2\u0006\u0010$\u001a\u00020%2\u001d\b\u0004\u0010\u0019\u001a\u0017\u0012\b\u0012\u00060\u0001j\u0002`\u000f\u0012\u0004\u0012\u00020\u001b0\u001a¢\u0006\u0002\b\u001cH\u0086\bø\u0001\u0000\u001aG\u0010\u001e\u001a\f\u0012\b\u0012\u00060\u0016R\u00020\u00170\"*\u0006\u0012\u0002\b\u00030#2\u0006\u0010$\u001a\u00020%2\u001f\b\u0004\u0010\u0019\u001a\u0019\u0012\b\u0012\u00060\u0001j\u0002`\u000f\u0012\u0006\u0012\u0004\u0018\u00010\u00030\u001a¢\u0006\u0002\b\u001cH\u0086\bø\u0001\u0000\u001a*\u0010\u001f\u001a\f\u0012\b\u0012\u00060\u0016R\u00020\u00170\"*\u0006\u0012\u0002\b\u00030#2\u0006\u0010$\u001a\u00020%2\b\u0010 \u001a\u0004\u0018\u00010\u0003\u001a \u0010&\u001a\f\u0012\b\u0012\u00060\u0016R\u00020\u00170\"*\u0006\u0012\u0002\b\u00030#2\u0006\u0010\u0015\u001a\u00020\u0017\u001a=\u0010'\u001a\f\u0012\b\u0012\u00060\u0016R\u00020\u00170\"*\u0006\u0012\u0002\b\u00030#2\u001d\b\u0004\u0010\u0019\u001a\u0017\u0012\b\u0012\u00060\u0001j\u0002`\u000f\u0012\u0004\u0012\u00020\u001b0\u001a¢\u0006\u0002\b\u001cH\u0086\bø\u0001\u0000\u001a=\u0010(\u001a\f\u0012\b\u0012\u00060\u0016R\u00020\u00170\"*\u0006\u0012\u0002\b\u00030#2\u001d\b\u0004\u0010\u0019\u001a\u0017\u0012\b\u0012\u00060\u0001j\u0002`\u000f\u0012\u0004\u0012\u00020\u001b0\u001a¢\u0006\u0002\b\u001cH\u0086\bø\u0001\u0000\u001a+\u0010)\u001a\u00020\u00172\u001d\b\u0004\u0010\u0019\u001a\u0017\u0012\b\u0012\u00060\u0001j\u0002`\u000f\u0012\u0004\u0012\u00020\u001b0\u001a¢\u0006\u0002\b\u001cH\u0086\bø\u0001\u0000\u001a+\u0010*\u001a\u00020\u00172\u001d\b\u0004\u0010\u0019\u001a\u0017\u0012\b\u0012\u00060\u0001j\u0002`\u000f\u0012\u0004\u0012\u00020\u001b0\u001a¢\u0006\u0002\b\u001cH\u0086\bø\u0001\u0000\u001a-\u0010+\u001a\u00020\u00172\u001f\b\u0004\u0010\u0019\u001a\u0019\u0012\b\u0012\u00060\u0001j\u0002`\u000f\u0012\u0006\u0012\u0004\u0018\u00010\u00030\u001a¢\u0006\u0002\b\u001cH\u0086\bø\u0001\u0000\"\u0015\u0010\u000b\u001a\u00020\n*\u00020\u00048F¢\u0006\u0006\u001a\u0004\b\u000b\u0010\f*\n\u0010\u0000\"\u00020\u00012\u00020\u0001\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006,"}, d2 = {"HookParam", "Lde/robv/android/xposed/XC_MethodHook$MethodHookParam;", "invokeOriginal", "", "Ljava/lang/reflect/Member;", "thisObject", "args", "", "(Ljava/lang/reflect/Member;Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;", "deoptimize", "", "isHooked", "(Ljava/lang/reflect/Member;)Z", "arg", "T", "Lni/shikatu/extensions/HookParam;", "index", "", "(Lde/robv/android/xposed/XC_MethodHook$MethodHookParam;I)Ljava/lang/Object;", "self", "(Lde/robv/android/xposed/XC_MethodHook$MethodHookParam;)Ljava/lang/Object;", "hook", "Lde/robv/android/xposed/XC_MethodHook$Unhook;", "Lde/robv/android/xposed/XC_MethodHook;", "before", "callback", "Lkotlin/Function1;", "", "Lkotlin/ExtensionFunctionType;", "after", "replace", "constant", "result", "doNothing", "", "Ljava/lang/Class;", "method", "", "hookConstructors", "beforeConstructors", "afterConstructors", "beforeHook", "afterHook", "replaceHook", "re_extera"}, k = 2, mv = {2, 2, 0}, xi = 48)
public final class HookUtilsKt {
    public static final Object invokeOriginal(Member member, Object obj, Object... objArr) {
        Intrinsics.checkNotNullParameter(member, "<this>");
        Intrinsics.checkNotNullParameter(objArr, "args");
        return XposedBridge.invokeOriginalMethod(member, obj, objArr);
    }

    public static final boolean deoptimize(Member member) {
        Intrinsics.checkNotNullParameter(member, "<this>");
        return XposedBridge.deoptimizeMethod(member);
    }

    public static final boolean isHooked(Member member) {
        Intrinsics.checkNotNullParameter(member, "<this>");
        return XposedBridge.isHooked(member);
    }

    public static final <T> T arg(XC_MethodHook.MethodHookParam methodHookParam, int i) {
        Intrinsics.checkNotNullParameter(methodHookParam, "<this>");
        return (T) methodHookParam.args[i];
    }

    public static final <T> T self(XC_MethodHook.MethodHookParam methodHookParam) {
        Intrinsics.checkNotNullParameter(methodHookParam, "<this>");
        return (T) methodHookParam.thisObject;
    }

    public static final XC_MethodHook.Unhook hook(Member member, XC_MethodHook xC_MethodHook) {
        Intrinsics.checkNotNullParameter(member, "<this>");
        Intrinsics.checkNotNullParameter(xC_MethodHook, "hook");
        XC_MethodHook.Unhook unhookHookMethod = XposedBridge.hookMethod(member, xC_MethodHook);
        Intrinsics.checkNotNullExpressionValue(unhookHookMethod, "hookMethod(...)");
        return unhookHookMethod;
    }

    public static final XC_MethodHook.Unhook constant(Member member, Object obj) {
        Intrinsics.checkNotNullParameter(member, "<this>");
        XC_MethodHook.Unhook unhookHookMethod = XposedBridge.hookMethod(member, XC_MethodReplacement.returnConstant(obj));
        Intrinsics.checkNotNullExpressionValue(unhookHookMethod, "hookMethod(...)");
        return unhookHookMethod;
    }

    public static final XC_MethodHook.Unhook doNothing(Member member) {
        Intrinsics.checkNotNullParameter(member, "<this>");
        XC_MethodHook.Unhook unhookHookMethod = XposedBridge.hookMethod(member, XC_MethodReplacement.DO_NOTHING);
        Intrinsics.checkNotNullExpressionValue(unhookHookMethod, "hookMethod(...)");
        return unhookHookMethod;
    }

    public static final Set<XC_MethodHook.Unhook> hook(Class<?> cls, String str, XC_MethodHook xC_MethodHook) {
        Intrinsics.checkNotNullParameter(cls, "<this>");
        Intrinsics.checkNotNullParameter(str, "method");
        Intrinsics.checkNotNullParameter(xC_MethodHook, "hook");
        Set<XC_MethodHook.Unhook> setHookAllMethods = XposedBridge.hookAllMethods(cls, str, xC_MethodHook);
        Intrinsics.checkNotNullExpressionValue(setHookAllMethods, "hookAllMethods(...)");
        return setHookAllMethods;
    }

    public static final Set<XC_MethodHook.Unhook> constant(Class<?> cls, String str, Object obj) {
        Intrinsics.checkNotNullParameter(cls, "<this>");
        Intrinsics.checkNotNullParameter(str, "method");
        Set<XC_MethodHook.Unhook> setHookAllMethods = XposedBridge.hookAllMethods(cls, str, XC_MethodReplacement.returnConstant(obj));
        Intrinsics.checkNotNullExpressionValue(setHookAllMethods, "hookAllMethods(...)");
        return setHookAllMethods;
    }

    public static final Set<XC_MethodHook.Unhook> hookConstructors(Class<?> cls, XC_MethodHook xC_MethodHook) {
        Intrinsics.checkNotNullParameter(cls, "<this>");
        Intrinsics.checkNotNullParameter(xC_MethodHook, "hook");
        Set<XC_MethodHook.Unhook> setHookAllConstructors = XposedBridge.hookAllConstructors(cls, xC_MethodHook);
        Intrinsics.checkNotNullExpressionValue(setHookAllConstructors, "hookAllConstructors(...)");
        return setHookAllConstructors;
    }

    /* JADX INFO: renamed from: ni.shikatu.extensions.HookUtilsKt$beforeHook$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: HookUtils.kt */
    @Metadata(d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, d2 = {"ni/shikatu/extensions/HookUtilsKt$beforeHook$1", "Lde/robv/android/xposed/XC_MethodHook;", "beforeHookedMethod", "", "param", "Lde/robv/android/xposed/XC_MethodHook$MethodHookParam;", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 176)
    public static final class C00001 extends XC_MethodHook {
        final /* synthetic */ Function1<XC_MethodHook.MethodHookParam, Unit> $callback;

        public C00001(Function1<? super XC_MethodHook.MethodHookParam, Unit> function1) {
            this.$callback = function1;
        }

        public void beforeHookedMethod(XC_MethodHook.MethodHookParam param) {
            Intrinsics.checkNotNullParameter(param, "param");
            try {
                this.$callback.invoke(param);
            } catch (Throwable th) {
                ReLog.INSTANCE.e("Unhandled hook exception in " + param.method.getName(), th);
            }
        }
    }

    public static final XC_MethodHook beforeHook(Function1<? super XC_MethodHook.MethodHookParam, Unit> function1) {
        Intrinsics.checkNotNullParameter(function1, "callback");
        return new C00001(function1);
    }

    /* JADX INFO: renamed from: ni.shikatu.extensions.HookUtilsKt$afterHook$1, reason: invalid class name */
    /* JADX INFO: compiled from: HookUtils.kt */
    @Metadata(d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, d2 = {"ni/shikatu/extensions/HookUtilsKt$afterHook$1", "Lde/robv/android/xposed/XC_MethodHook;", "afterHookedMethod", "", "param", "Lde/robv/android/xposed/XC_MethodHook$MethodHookParam;", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 176)
    public static final class AnonymousClass1 extends XC_MethodHook {
        final /* synthetic */ Function1<XC_MethodHook.MethodHookParam, Unit> $callback;

        public AnonymousClass1(Function1<? super XC_MethodHook.MethodHookParam, Unit> function1) {
            this.$callback = function1;
        }

        public void afterHookedMethod(XC_MethodHook.MethodHookParam param) {
            Intrinsics.checkNotNullParameter(param, "param");
            try {
                this.$callback.invoke(param);
            } catch (Throwable th) {
                ReLog.INSTANCE.e("Unhandled hook exception in " + param.method.getName(), th);
            }
        }
    }

    public static final XC_MethodHook afterHook(Function1<? super XC_MethodHook.MethodHookParam, Unit> function1) {
        Intrinsics.checkNotNullParameter(function1, "callback");
        return new AnonymousClass1(function1);
    }

    /* JADX INFO: renamed from: ni.shikatu.extensions.HookUtilsKt$replaceHook$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: HookUtils.kt */
    @Metadata(d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, d2 = {"ni/shikatu/extensions/HookUtilsKt$replaceHook$1", "Lde/robv/android/xposed/XC_MethodReplacement;", "replaceHookedMethod", "", "param", "Lde/robv/android/xposed/XC_MethodHook$MethodHookParam;", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 176)
    public static final class C00011 extends XC_MethodReplacement {
        final /* synthetic */ Function1<XC_MethodHook.MethodHookParam, Object> $callback;

        public C00011(Function1<? super XC_MethodHook.MethodHookParam, ? extends Object> function1) {
            this.$callback = function1;
        }

        public Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) {
            Intrinsics.checkNotNullParameter(param, "param");
            try {
                return this.$callback.invoke(param);
            } catch (Throwable th) {
                ReLog.INSTANCE.e("Unhandled hook exception in " + param.method.getName(), th);
                Member member = param.method;
                Intrinsics.checkNotNullExpressionValue(member, "method");
                Object obj = param.thisObject;
                Object[] objArr = param.args;
                return HookUtilsKt.invokeOriginal(member, obj, Arrays.copyOf(objArr, objArr.length));
            }
        }
    }

    public static final XC_MethodHook replaceHook(Function1<? super XC_MethodHook.MethodHookParam, ? extends Object> function1) {
        Intrinsics.checkNotNullParameter(function1, "callback");
        return new C00011(function1);
    }

    public static final XC_MethodHook.Unhook before(Member member, Function1<? super XC_MethodHook.MethodHookParam, Unit> function1) {
        Intrinsics.checkNotNullParameter(member, "<this>");
        Intrinsics.checkNotNullParameter(function1, "callback");
        XC_MethodHook.Unhook unhookHookMethod = XposedBridge.hookMethod(member, new C00001(function1));
        Intrinsics.checkNotNullExpressionValue(unhookHookMethod, "hookMethod(...)");
        return unhookHookMethod;
    }

    public static final XC_MethodHook.Unhook after(Member member, Function1<? super XC_MethodHook.MethodHookParam, Unit> function1) {
        Intrinsics.checkNotNullParameter(member, "<this>");
        Intrinsics.checkNotNullParameter(function1, "callback");
        XC_MethodHook.Unhook unhookHookMethod = XposedBridge.hookMethod(member, new AnonymousClass1(function1));
        Intrinsics.checkNotNullExpressionValue(unhookHookMethod, "hookMethod(...)");
        return unhookHookMethod;
    }

    public static final XC_MethodHook.Unhook replace(Member member, Function1<? super XC_MethodHook.MethodHookParam, ? extends Object> function1) {
        Intrinsics.checkNotNullParameter(member, "<this>");
        Intrinsics.checkNotNullParameter(function1, "callback");
        XC_MethodHook.Unhook unhookHookMethod = XposedBridge.hookMethod(member, new C00011(function1));
        Intrinsics.checkNotNullExpressionValue(unhookHookMethod, "hookMethod(...)");
        return unhookHookMethod;
    }

    public static final Set<XC_MethodHook.Unhook> before(Class<?> cls, String str, Function1<? super XC_MethodHook.MethodHookParam, Unit> function1) {
        Intrinsics.checkNotNullParameter(cls, "<this>");
        Intrinsics.checkNotNullParameter(str, "method");
        Intrinsics.checkNotNullParameter(function1, "callback");
        Set<XC_MethodHook.Unhook> setHookAllMethods = XposedBridge.hookAllMethods(cls, str, new C00001(function1));
        Intrinsics.checkNotNullExpressionValue(setHookAllMethods, "hookAllMethods(...)");
        return setHookAllMethods;
    }

    public static final Set<XC_MethodHook.Unhook> after(Class<?> cls, String str, Function1<? super XC_MethodHook.MethodHookParam, Unit> function1) {
        Intrinsics.checkNotNullParameter(cls, "<this>");
        Intrinsics.checkNotNullParameter(str, "method");
        Intrinsics.checkNotNullParameter(function1, "callback");
        Set<XC_MethodHook.Unhook> setHookAllMethods = XposedBridge.hookAllMethods(cls, str, new AnonymousClass1(function1));
        Intrinsics.checkNotNullExpressionValue(setHookAllMethods, "hookAllMethods(...)");
        return setHookAllMethods;
    }

    public static final Set<XC_MethodHook.Unhook> replace(Class<?> cls, String str, Function1<? super XC_MethodHook.MethodHookParam, ? extends Object> function1) {
        Intrinsics.checkNotNullParameter(cls, "<this>");
        Intrinsics.checkNotNullParameter(str, "method");
        Intrinsics.checkNotNullParameter(function1, "callback");
        Set<XC_MethodHook.Unhook> setHookAllMethods = XposedBridge.hookAllMethods(cls, str, new C00011(function1));
        Intrinsics.checkNotNullExpressionValue(setHookAllMethods, "hookAllMethods(...)");
        return setHookAllMethods;
    }

    public static final Set<XC_MethodHook.Unhook> beforeConstructors(Class<?> cls, Function1<? super XC_MethodHook.MethodHookParam, Unit> function1) {
        Intrinsics.checkNotNullParameter(cls, "<this>");
        Intrinsics.checkNotNullParameter(function1, "callback");
        Set<XC_MethodHook.Unhook> setHookAllConstructors = XposedBridge.hookAllConstructors(cls, new C00001(function1));
        Intrinsics.checkNotNullExpressionValue(setHookAllConstructors, "hookAllConstructors(...)");
        return setHookAllConstructors;
    }

    public static final Set<XC_MethodHook.Unhook> afterConstructors(Class<?> cls, Function1<? super XC_MethodHook.MethodHookParam, Unit> function1) {
        Intrinsics.checkNotNullParameter(cls, "<this>");
        Intrinsics.checkNotNullParameter(function1, "callback");
        Set<XC_MethodHook.Unhook> setHookAllConstructors = XposedBridge.hookAllConstructors(cls, new AnonymousClass1(function1));
        Intrinsics.checkNotNullExpressionValue(setHookAllConstructors, "hookAllConstructors(...)");
        return setHookAllConstructors;
    }
}
