package ni.shikatu.extensions;

import java.lang.reflect.Field;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import ni.shikatu.re_extera.ReLog;

/* JADX INFO: compiled from: ReflectionUtils.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002B\u001b\u0012\n\u0010\u0003\u001a\u0006\u0012\u0002\b\u00030\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0004\b\u0007\u0010\bJ\u001a\u0010\u0012\u001a\u0004\u0018\u00018\u00002\b\u0010\u0013\u001a\u0004\u0018\u00010\u0002H\u0086\u0002¢\u0006\u0002\u0010\u0014J\"\u0010\u0015\u001a\u00020\u00162\b\u0010\u0013\u001a\u0004\u0018\u00010\u00022\b\u0010\u0017\u001a\u0004\u0018\u00018\u0000H\u0086\u0002¢\u0006\u0002\u0010\u0018R\u0012\u0010\u0003\u001a\u0006\u0012\u0002\b\u00030\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u001d\u0010\t\u001a\u0004\u0018\u00010\n8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\r\u0010\u000e\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u000f\u001a\u00020\u00108F¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u0011¨\u0006\u0019"}, d2 = {"Lni/shikatu/extensions/FieldRef;", "T", "", "owner", "Ljava/lang/Class;", "name", "", "<init>", "(Ljava/lang/Class;Ljava/lang/String;)V", "resolved", "Ljava/lang/reflect/Field;", "getResolved", "()Ljava/lang/reflect/Field;", "resolved$delegate", "Lkotlin/Lazy;", "isAvailable", "", "()Z", "get", "target", "(Ljava/lang/Object;)Ljava/lang/Object;", "set", "", "value", "(Ljava/lang/Object;Ljava/lang/Object;)V", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class FieldRef<T> {
    private final String name;
    private final Class<?> owner;

    /* JADX INFO: renamed from: resolved$delegate, reason: from kotlin metadata */
    private final Lazy resolved;

    public FieldRef(Class<?> cls, String str) {
        Intrinsics.checkNotNullParameter(cls, "owner");
        Intrinsics.checkNotNullParameter(str, "name");
        this.owner = cls;
        this.name = str;
        this.resolved = LazyKt.lazy(new Function0() { // from class: ni.shikatu.extensions.FieldRef.0
            public final Object invoke() {
                return FieldRef.resolved_delegate$lambda$0(FieldRef.this);
            }
        });
    }

    private final Field getResolved() {
        return (Field) this.resolved.getValue();
    }

    static final Field resolved_delegate$lambda$0(FieldRef fieldRef) {
        try {
            return ReflectionUtilsKt.findFieldExt(fieldRef.owner, fieldRef.name);
        } catch (NoSuchFieldException e) {
            ReLog.INSTANCE.e("Field " + fieldRef.owner.getSimpleName() + "." + fieldRef.name + " is missing", e);
            return null;
        }
    }

    public final boolean isAvailable() {
        return getResolved() != null;
    }

    public final T get(Object target) {
        Field resolved = getResolved();
        if (resolved != null) {
            return (T) resolved.get(target);
        }
        return null;
    }

    public final void set(Object target, T value) throws IllegalAccessException {
        Field resolved = getResolved();
        if (resolved != null) {
            resolved.set(target, value);
        }
    }
}
