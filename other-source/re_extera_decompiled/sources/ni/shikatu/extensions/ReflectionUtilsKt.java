package ni.shikatu.extensions;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: ReflectionUtils.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000B\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\u001a\u0019\u0010\u0000\u001a\u0002H\u0001\"\b\b\u0000\u0010\u0001*\u00020\u0002*\u0002H\u0001¢\u0006\u0002\u0010\u0003\u001a\u0016\u0010\u0004\u001a\u00020\u0005*\u0006\u0012\u0002\b\u00030\u00062\u0006\u0010\u0007\u001a\u00020\b\u001a7\u0010\t\u001a\u00020\n*\u0006\u0012\u0002\b\u00030\u00062\u0006\u0010\u0007\u001a\u00020\b2\u001a\u0010\u000b\u001a\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030\u00060\f\"\u0006\u0012\u0002\b\u00030\u0006¢\u0006\u0002\u0010\r\u001a$\u0010\u000e\u001a\u0004\u0018\u0001H\u0001\"\u0006\b\u0000\u0010\u0001\u0018\u0001*\u00020\u000f2\u0006\u0010\u0007\u001a\u00020\bH\u0086\b¢\u0006\u0002\u0010\u0010\u001a(\u0010\u0011\u001a\u0004\u0018\u0001H\u0001\"\u0006\b\u0000\u0010\u0001\u0018\u0001*\u0006\u0012\u0002\b\u00030\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086\b¢\u0006\u0002\u0010\u0012\u001a\u001c\u0010\u0013\u001a\u00020\u0014*\u00020\u000f2\u0006\u0010\u0007\u001a\u00020\b2\b\u0010\u0015\u001a\u0004\u0018\u00010\u000f\u001a6\u0010\u0016\u001a\u0002H\u0001\"\u0006\b\u0000\u0010\u0001\u0018\u0001*\u0006\u0012\u0002\b\u00030\u00172\u0016\u0010\u0018\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010\u000f0\f\"\u0004\u0018\u00010\u000fH\u0086\b¢\u0006\u0002\u0010\u0019\u001a\u001e\u0010\u001a\u001a\u00020\u0014*\u00020\u00052\b\u0010\u001b\u001a\u0004\u0018\u00010\u000f2\b\u0010\u0015\u001a\u0004\u0018\u00010\u000f\u001a&\u0010\u001c\u001a\u0004\u0018\u0001H\u0001\"\u0006\b\u0000\u0010\u0001\u0018\u0001*\u00020\u00052\b\u0010\u001b\u001a\u0004\u0018\u00010\u000fH\u0086\b¢\u0006\u0002\u0010\u001d\u001a9\u0010\u001e\u001a\u0004\u0018\u0001H\u0001\"\u0004\b\u0000\u0010\u0001*\u00020\n2\b\u0010\u001b\u001a\u0004\u0018\u00010\u000f2\u0016\u0010\u0018\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010\u000f0\f\"\u0004\u0018\u00010\u000f¢\u0006\u0002\u0010\u001f¨\u0006 "}, d2 = {"accessible", "T", "Ljava/lang/reflect/AccessibleObject;", "(Ljava/lang/reflect/AccessibleObject;)Ljava/lang/reflect/AccessibleObject;", "findFieldExt", "Ljava/lang/reflect/Field;", "Ljava/lang/Class;", "name", "", "findMethodExt", "Ljava/lang/reflect/Method;", "params", "", "(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;", "getField", "", "(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;", "getStaticField", "(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Object;", "setField", "", "value", "newInstanceExt", "Ljava/lang/reflect/Constructor;", "args", "(Ljava/lang/reflect/Constructor;[Ljava/lang/Object;)Ljava/lang/Object;", "accessibleSet", "obj", "accessibleGet", "(Ljava/lang/reflect/Field;Ljava/lang/Object;)Ljava/lang/Object;", "accessibleCall", "(Ljava/lang/reflect/Method;Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;", "re_extera"}, k = 2, mv = {2, 2, 0}, xi = 48)
public final class ReflectionUtilsKt {
    public static final <T extends AccessibleObject> T accessible(T t) {
        Intrinsics.checkNotNullParameter(t, "<this>");
        t.setAccessible(true);
        return t;
    }

    public static final Field findFieldExt(Class<?> cls, String str) throws NoSuchFieldException {
        Intrinsics.checkNotNullParameter(cls, "<this>");
        Intrinsics.checkNotNullParameter(str, "name");
        for (Class<?> superclass = cls; superclass != null; superclass = superclass.getSuperclass()) {
            try {
                Field declaredField = superclass.getDeclaredField(str);
                declaredField.setAccessible(true);
                Intrinsics.checkNotNullExpressionValue(declaredField, "apply(...)");
                return declaredField;
            } catch (NoSuchFieldException unused) {
            }
        }
        throw new NoSuchFieldException("Field '" + str + "' not found in class " + cls.getName() + " or its superclasses");
    }

    public static final Method findMethodExt(Class<?> cls, String str, Class<?>... clsArr) throws NoSuchMethodException {
        Intrinsics.checkNotNullParameter(cls, "<this>");
        Intrinsics.checkNotNullParameter(str, "name");
        Intrinsics.checkNotNullParameter(clsArr, "params");
        for (Class<?> superclass = cls; superclass != null; superclass = superclass.getSuperclass()) {
            try {
                Method declaredMethod = superclass.getDeclaredMethod(str, (Class[]) Arrays.copyOf(clsArr, clsArr.length));
                declaredMethod.setAccessible(true);
                Intrinsics.checkNotNullExpressionValue(declaredMethod, "apply(...)");
                return declaredMethod;
            } catch (NoSuchMethodException unused) {
            }
        }
        throw new NoSuchMethodException("Method '" + str + "' not found in class " + cls.getName() + " or its superclasses");
    }

    public static final /* synthetic */ <T> T getField(Object obj, String str) {
        Intrinsics.checkNotNullParameter(obj, "<this>");
        Intrinsics.checkNotNullParameter(str, "name");
        T t = (T) findFieldExt(obj.getClass(), str).get(obj);
        Intrinsics.reifiedOperationMarker(1, "T?");
        return t;
    }

    public static final /* synthetic */ <T> T getStaticField(Class<?> cls, String str) {
        Intrinsics.checkNotNullParameter(cls, "<this>");
        Intrinsics.checkNotNullParameter(str, "name");
        T t = (T) findFieldExt(cls, str).get(null);
        Intrinsics.reifiedOperationMarker(1, "T?");
        return t;
    }

    public static final void setField(Object obj, String str, Object obj2) throws IllegalAccessException {
        Intrinsics.checkNotNullParameter(obj, "<this>");
        Intrinsics.checkNotNullParameter(str, "name");
        findFieldExt(obj.getClass(), str).set(obj, obj2);
    }

    public static final /* synthetic */ <T> T newInstanceExt(Constructor<?> constructor, Object... objArr) {
        Intrinsics.checkNotNullParameter(constructor, "<this>");
        Intrinsics.checkNotNullParameter(objArr, "args");
        T t = (T) constructor.newInstance(Arrays.copyOf(objArr, objArr.length));
        Intrinsics.reifiedOperationMarker(1, "T");
        return t;
    }

    public static final void accessibleSet(Field field, Object obj, Object obj2) throws IllegalAccessException {
        Intrinsics.checkNotNullParameter(field, "<this>");
        field.setAccessible(true);
        field.set(obj, obj2);
    }

    public static final /* synthetic */ <T> T accessibleGet(Field field, Object obj) {
        Intrinsics.checkNotNullParameter(field, "<this>");
        field.setAccessible(true);
        T t = (T) field.get(obj);
        Intrinsics.reifiedOperationMarker(1, "T?");
        return t;
    }

    public static final <T> T accessibleCall(Method method, Object obj, Object... objArr) {
        Intrinsics.checkNotNullParameter(method, "<this>");
        Intrinsics.checkNotNullParameter(objArr, "args");
        method.setAccessible(true);
        return (T) method.invoke(obj, Arrays.copyOf(objArr, objArr.length));
    }
}
