package ni.shikatu.re_extera.hooks;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import ni.shikatu.extensions.HookUtilsKt;
import ni.shikatu.re_extera.ReLog;

/* JADX INFO: compiled from: HookerClass.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u0003\n\u0000\b&\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u000f\u001a\u00020\u0010H$J\u0006\u0010\u0011\u001a\u00020\u0010J\u0006\u0010\u0012\u001a\u00020\u0010J;\u0010\u0013\u001a\u0004\u0018\u00010\u0014*\u0006\u0012\u0002\b\u00030\u00152\u0006\u0010\u0016\u001a\u00020\n2\u001a\u0010\u0017\u001a\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030\u00150\u0018\"\u0006\u0012\u0002\b\u00030\u0015H\u0004¢\u0006\u0002\u0010\u0019J\u0016\u0010\u001a\u001a\u00020\u0010*\u0004\u0018\u00010\u001b2\u0006\u0010\u001c\u001a\u00020\u0007H\u0004JQ\u0010\u001d\u001a\u00020\u0010*\u0006\u0012\u0002\b\u00030\u00152\u0006\u0010\u0016\u001a\u00020\n2\u001a\u0010\u0017\u001a\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030\u00150\u0018\"\u0006\u0012\u0002\b\u00030\u00152\u0016\u0010\u001e\u001a\u0012\u0012\b\u0012\u00060 j\u0002`!\u0012\u0004\u0012\u00020\u00100\u001fH\u0004¢\u0006\u0002\u0010\"JQ\u0010#\u001a\u00020\u0010*\u0006\u0012\u0002\b\u00030\u00152\u0006\u0010\u0016\u001a\u00020\n2\u001a\u0010\u0017\u001a\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030\u00150\u0018\"\u0006\u0012\u0002\b\u00030\u00152\u0016\u0010\u001e\u001a\u0012\u0012\b\u0012\u00060 j\u0002`!\u0012\u0004\u0012\u00020\u00100\u001fH\u0004¢\u0006\u0002\u0010\"JS\u0010$\u001a\u00020\u0010*\u0006\u0012\u0002\b\u00030\u00152\u0006\u0010\u0016\u001a\u00020\n2\u001a\u0010\u0017\u001a\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030\u00150\u0018\"\u0006\u0012\u0002\b\u00030\u00152\u0018\u0010\u001e\u001a\u0014\u0012\b\u0012\u00060 j\u0002`!\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u001fH\u0004¢\u0006\u0002\u0010\"JI\u0010%\u001a\u00020\u0010*\u0006\u0012\u0002\b\u00030\u00152\u0006\u0010\u0016\u001a\u00020\n2\u001a\u0010\u0017\u001a\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030\u00150\u0018\"\u0006\u0012\u0002\b\u00030\u00152\u000e\u0010&\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00010'H\u0004¢\u0006\u0002\u0010(Jq\u0010)\u001a\u00020\u0010*\u0006\u0012\u0002\b\u00030\u00152\u0006\u0010\u0016\u001a\u00020\n2\u001a\u0010\u0017\u001a\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030\u00150\u0018\"\u0006\u0012\u0002\b\u00030\u00152\u001a\b\u0002\u0010*\u001a\u0014\u0012\b\u0012\u00060 j\u0002`!\u0012\u0004\u0012\u00020\u0010\u0018\u00010\u001f2\u001a\b\u0002\u0010+\u001a\u0014\u0012\b\u0012\u00060 j\u0002`!\u0012\u0004\u0012\u00020\u0010\u0018\u00010\u001fH\u0004¢\u0006\u0002\u0010,J#\u0010-\u001a\u00020\u00102\n\u0010.\u001a\u00060 j\u0002`!2\f\u0010/\u001a\b\u0012\u0004\u0012\u00020\u00100'H\u0082\bJ\u0018\u00100\u001a\u00020\u00102\u0006\u00101\u001a\u00020\n2\u0006\u00102\u001a\u000203H\u0002R&\u0010\u0004\u001a\u001a\u0012\b\u0012\u00060\u0006R\u00020\u00070\u0005j\f\u0012\b\u0012\u00060\u0006R\u00020\u0007`\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\t\u001a\u0012\u0012\u0004\u0012\u00020\n0\u0005j\b\u0012\u0004\u0012\u00020\n`\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\n0\f8F¢\u0006\u0006\u001a\u0004\b\r\u0010\u000e¨\u00064"}, d2 = {"Lni/shikatu/re_extera/hooks/HookerClass;", "", "<init>", "()V", "unhooks", "Ljava/util/ArrayList;", "Lde/robv/android/xposed/XC_MethodHook$Unhook;", "Lde/robv/android/xposed/XC_MethodHook;", "Lkotlin/collections/ArrayList;", "failedTargets", "", "failures", "", "getFailures", "()Ljava/util/List;", "setup", "", "install", "disable", "method", "Ljava/lang/reflect/Method;", "Ljava/lang/Class;", "name", "params", "", "(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;", "register", "Ljava/lang/reflect/Member;", "hook", "hookBefore", "callback", "Lkotlin/Function1;", "Lde/robv/android/xposed/XC_MethodHook$MethodHookParam;", "Lni/shikatu/extensions/HookParam;", "(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Class;Lkotlin/jvm/functions/Function1;)V", "hookAfter", "hookReplace", "hookConstant", "value", "Lkotlin/Function0;", "(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Class;Lkotlin/jvm/functions/Function0;)V", "hookMethod", "before", "after", "(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Class;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)V", "guarded", "param", "block", "fail", "target", "t", "", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public abstract class HookerClass {
    private final ArrayList<XC_MethodHook.Unhook> unhooks = new ArrayList<>();
    private final ArrayList<String> failedTargets = new ArrayList<>();

    protected abstract void setup();

    public final List<String> getFailures() {
        return this.failedTargets;
    }

    public final void install() {
        disable();
        this.failedTargets.clear();
        try {
            setup();
        } catch (Throwable th) {
            String simpleName = getClass().getSimpleName();
            Intrinsics.checkNotNullExpressionValue(simpleName, "getSimpleName(...)");
            fail(simpleName, th);
        }
    }

    public final void disable() {
        Iterator<T> it = this.unhooks.iterator();
        while (it.hasNext()) {
            ((XC_MethodHook.Unhook) it.next()).unhook();
        }
        this.unhooks.clear();
    }

    protected final Method method(Class<?> cls, String str, Class<?>... clsArr) {
        Intrinsics.checkNotNullParameter(cls, "<this>");
        Intrinsics.checkNotNullParameter(str, "name");
        Intrinsics.checkNotNullParameter(clsArr, "params");
        try {
            return cls.getDeclaredMethod(str, (Class[]) Arrays.copyOf(clsArr, clsArr.length));
        } catch (NoSuchMethodException e) {
            fail(cls.getSimpleName() + "." + str + "(" + ArraysKt.joinToString$default(clsArr, (CharSequence) null, (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, new Function1() { // from class: ni.shikatu.re_extera.hooks.HookerClass.0
                public final Object invoke(Object obj) {
                    return HookerClass.method$lambda$1((Class) obj);
                }
            }, 31, (Object) null) + ")", e);
            return null;
        }
    }

    static final CharSequence method$lambda$1(Class cls) {
        Intrinsics.checkNotNullParameter(cls, "it");
        String simpleName = cls.getSimpleName();
        Intrinsics.checkNotNullExpressionValue(simpleName, "getSimpleName(...)");
        return simpleName;
    }

    protected final void register(Member member, XC_MethodHook xC_MethodHook) {
        Intrinsics.checkNotNullParameter(xC_MethodHook, "hook");
        if (member == null) {
            return;
        }
        try {
            this.unhooks.add(XposedBridge.hookMethod(member, xC_MethodHook));
        } catch (Throwable th) {
            fail(member.getDeclaringClass().getSimpleName() + "." + member.getName(), th);
        }
    }

    protected final void hookBefore(Class<?> cls, String str, Class<?>[] clsArr, final Function1<? super XC_MethodHook.MethodHookParam, Unit> function1) {
        Intrinsics.checkNotNullParameter(cls, "<this>");
        Intrinsics.checkNotNullParameter(str, "name");
        Intrinsics.checkNotNullParameter(clsArr, "params");
        Intrinsics.checkNotNullParameter(function1, "callback");
        register(method(cls, str, (Class[]) Arrays.copyOf(clsArr, clsArr.length)), new XC_MethodHook() { // from class: ni.shikatu.re_extera.hooks.HookerClass$hookBefore$$inlined$beforeHook$1
            public void beforeHookedMethod(XC_MethodHook.MethodHookParam param) {
                Intrinsics.checkNotNullParameter(param, "param");
                try {
                    function1.invoke(param);
                } catch (Throwable th) {
                    ReLog.INSTANCE.e("Unhandled hook exception in " + param.method.getName(), th);
                }
            }
        });
    }

    protected final void hookAfter(Class<?> cls, String str, Class<?>[] clsArr, final Function1<? super XC_MethodHook.MethodHookParam, Unit> function1) {
        Intrinsics.checkNotNullParameter(cls, "<this>");
        Intrinsics.checkNotNullParameter(str, "name");
        Intrinsics.checkNotNullParameter(clsArr, "params");
        Intrinsics.checkNotNullParameter(function1, "callback");
        register(method(cls, str, (Class[]) Arrays.copyOf(clsArr, clsArr.length)), new XC_MethodHook() { // from class: ni.shikatu.re_extera.hooks.HookerClass$hookAfter$$inlined$afterHook$1
            public void afterHookedMethod(XC_MethodHook.MethodHookParam param) {
                Intrinsics.checkNotNullParameter(param, "param");
                try {
                    function1.invoke(param);
                } catch (Throwable th) {
                    ReLog.INSTANCE.e("Unhandled hook exception in " + param.method.getName(), th);
                }
            }
        });
    }

    protected final void hookReplace(Class<?> cls, String str, Class<?>[] clsArr, final Function1<? super XC_MethodHook.MethodHookParam, ? extends Object> function1) {
        Intrinsics.checkNotNullParameter(cls, "<this>");
        Intrinsics.checkNotNullParameter(str, "name");
        Intrinsics.checkNotNullParameter(clsArr, "params");
        Intrinsics.checkNotNullParameter(function1, "callback");
        register(method(cls, str, (Class[]) Arrays.copyOf(clsArr, clsArr.length)), (XC_MethodHook) new XC_MethodReplacement() { // from class: ni.shikatu.re_extera.hooks.HookerClass$hookReplace$$inlined$replaceHook$1
            public Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) {
                Intrinsics.checkNotNullParameter(param, "param");
                try {
                    return function1.invoke(param);
                } catch (Throwable th) {
                    ReLog.INSTANCE.e("Unhandled hook exception in " + param.method.getName(), th);
                    Member member = param.method;
                    Intrinsics.checkNotNullExpressionValue(member, "method");
                    Object obj = param.thisObject;
                    Object[] objArr = param.args;
                    return HookUtilsKt.invokeOriginal(member, obj, Arrays.copyOf(objArr, objArr.length));
                }
            }
        });
    }

    protected final void hookConstant(Class<?> cls, String str, Class<?>[] clsArr, final Function0<? extends Object> function0) {
        Intrinsics.checkNotNullParameter(cls, "<this>");
        Intrinsics.checkNotNullParameter(str, "name");
        Intrinsics.checkNotNullParameter(clsArr, "params");
        Intrinsics.checkNotNullParameter(function0, "value");
        register(method(cls, str, (Class[]) Arrays.copyOf(clsArr, clsArr.length)), (XC_MethodHook) new XC_MethodReplacement() { // from class: ni.shikatu.re_extera.hooks.HookerClass.hookConstant.1
            public Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) {
                Intrinsics.checkNotNullParameter(param, "param");
                return function0.invoke();
            }
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ void hookMethod$default(HookerClass hookerClass, Class cls, String str, Class[] clsArr, Function1 function1, Function1 function2, int i, Object obj) {
        if (obj != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: hookMethod");
        }
        if ((i & 4) != 0) {
            function1 = null;
        }
        if ((i & 8) != 0) {
            function2 = null;
        }
        hookerClass.hookMethod(cls, str, clsArr, function1, function2);
    }

    protected final void hookMethod(Class<?> cls, String str, Class<?>[] clsArr, final Function1<? super XC_MethodHook.MethodHookParam, Unit> function1, final Function1<? super XC_MethodHook.MethodHookParam, Unit> function2) {
        Intrinsics.checkNotNullParameter(cls, "<this>");
        Intrinsics.checkNotNullParameter(str, "name");
        Intrinsics.checkNotNullParameter(clsArr, "params");
        register(method(cls, str, (Class[]) Arrays.copyOf(clsArr, clsArr.length)), new XC_MethodHook() { // from class: ni.shikatu.re_extera.hooks.HookerClass.hookMethod.1
            public void beforeHookedMethod(XC_MethodHook.MethodHookParam param) {
                Intrinsics.checkNotNullParameter(param, "param");
                Function1<XC_MethodHook.MethodHookParam, Unit> function3 = function1;
                if (function3 != null) {
                    try {
                        function3.invoke(param);
                    } catch (Throwable th) {
                        ReLog.INSTANCE.e("Unhandled hook exception in " + param.method.getName(), th);
                    }
                }
            }

            public void afterHookedMethod(XC_MethodHook.MethodHookParam param) {
                Intrinsics.checkNotNullParameter(param, "param");
                Function1<XC_MethodHook.MethodHookParam, Unit> function3 = function2;
                if (function3 != null) {
                    try {
                        function3.invoke(param);
                    } catch (Throwable th) {
                        ReLog.INSTANCE.e("Unhandled hook exception in " + param.method.getName(), th);
                    }
                }
            }
        });
    }

    private final void guarded(XC_MethodHook.MethodHookParam param, Function0<Unit> block) {
        try {
            block.invoke();
        } catch (Throwable th) {
            ReLog.INSTANCE.e("Unhandled hook exception in " + param.method.getName(), th);
        }
    }

    private final void fail(String target, Throwable t) {
        this.failedTargets.add(target);
        ReLog.INSTANCE.e("Failed to hook " + target, t);
    }
}
