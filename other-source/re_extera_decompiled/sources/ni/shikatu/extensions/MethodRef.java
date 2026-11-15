package ni.shikatu.extensions;

import java.lang.reflect.Method;
import java.util.Arrays;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import ni.shikatu.re_extera.ReLog;

/* JADX INFO: compiled from: ReflectionUtils.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0007\u0018\u00002\u00020\u0001B7\u0012\n\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u001a\u0010\u0006\u001a\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030\u00030\u0007\"\u0006\u0012\u0002\b\u00030\u0003¢\u0006\u0004\b\b\u0010\tJ/\u0010\u0014\u001a\u0004\u0018\u00010\u00012\b\u0010\u0015\u001a\u0004\u0018\u00010\u00012\u0016\u0010\u0016\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010\u00010\u0007\"\u0004\u0018\u00010\u0001¢\u0006\u0002\u0010\u0017J/\u0010\u0018\u001a\u0004\u0018\u00010\u00012\b\u0010\u0015\u001a\u0004\u0018\u00010\u00012\u0016\u0010\u0016\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010\u00010\u0007\"\u0004\u0018\u00010\u0001¢\u0006\u0002\u0010\u0017R\u0012\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\u0006\u001a\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030\u00030\u0007X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\nR\u001d\u0010\u000b\u001a\u0004\u0018\u00010\f8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u000f\u0010\u0010\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0011\u001a\u00020\u00128F¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u0013¨\u0006\u0019"}, d2 = {"Lni/shikatu/extensions/MethodRef;", "", "owner", "Ljava/lang/Class;", "name", "", "params", "", "<init>", "(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Class;)V", "[Ljava/lang/Class;", "method", "Ljava/lang/reflect/Method;", "getMethod", "()Ljava/lang/reflect/Method;", "method$delegate", "Lkotlin/Lazy;", "isAvailable", "", "()Z", "call", "target", "args", "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;", "callOriginal", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class MethodRef {

    /* JADX INFO: renamed from: method$delegate, reason: from kotlin metadata */
    private final Lazy method;
    private final String name;
    private final Class<?> owner;
    private final Class<?>[] params;

    public MethodRef(Class<?> cls, String str, Class<?>... clsArr) {
        Intrinsics.checkNotNullParameter(cls, "owner");
        Intrinsics.checkNotNullParameter(str, "name");
        Intrinsics.checkNotNullParameter(clsArr, "params");
        this.owner = cls;
        this.name = str;
        this.params = clsArr;
        this.method = LazyKt.lazy(new Function0() { // from class: ni.shikatu.extensions.MethodRef.0
            public final Object invoke() {
                return MethodRef.method_delegate$lambda$0(MethodRef.this);
            }
        });
    }

    private final Method getMethod() {
        return (Method) this.method.getValue();
    }

    static final Method method_delegate$lambda$0(MethodRef methodRef) {
        try {
            Class<?> cls = methodRef.owner;
            String str = methodRef.name;
            Class<?>[] clsArr = methodRef.params;
            return ReflectionUtilsKt.findMethodExt(cls, str, (Class[]) Arrays.copyOf(clsArr, clsArr.length));
        } catch (NoSuchMethodException e) {
            ReLog.INSTANCE.e("Method " + methodRef.owner.getSimpleName() + "." + methodRef.name + " is missing", e);
            return null;
        }
    }

    public final boolean isAvailable() {
        return getMethod() != null;
    }

    public final Object call(Object target, Object... args) {
        Intrinsics.checkNotNullParameter(args, "args");
        Method method = getMethod();
        if (method != null) {
            return method.invoke(target, Arrays.copyOf(args, args.length));
        }
        return null;
    }

    public final Object callOriginal(Object target, Object... args) {
        Intrinsics.checkNotNullParameter(args, "args");
        Method method = getMethod();
        if (method != null) {
            return HookUtilsKt.invokeOriginal(method, target, Arrays.copyOf(args, args.length));
        }
        return null;
    }
}
