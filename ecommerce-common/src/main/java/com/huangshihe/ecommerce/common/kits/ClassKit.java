package com.huangshihe.ecommerce.common.kits;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * 类工具类.
 * <p>
 * Create Date: 2018-04-04 13:36
 *
 * @author huangshihe
 */
public class ClassKit {
    /**
     * 获取类的作用域为public的所有方法，不包括继承的方法.
     *
     * @param cls 类
     * @return 方法
     */
    public static Method[] getMethods(Class<?> cls) {
        Method[] methods = cls.getDeclaredMethods();
        return Arrays.stream(methods).
                filter(method -> method.getModifiers() == Modifier.PUBLIC).toArray(Method[]::new);
        // [Ljava.lang.Object; cannot be cast to [Ljava.lang.reflect.Method;
//        return (Method[]) Arrays.stream(methods).
//                filter(method -> method.getModifiers() == Modifier.PUBLIC).toArray();
    }


    /**
     * 获取方法标识符：类名+方法名.
     * TODO 这种方法不可取，没有考虑到方法的重载！
     *
     * @param cls    类
     * @param method 方法
     * @return 方法标识符
     */
    public static String getMethodIdentity(Class<?> cls, Method method) {
        if (cls == null || method == null) {
            return null;
        }
        if (StringKit.isAllNotEmpty(cls.getName(), method.getName())) {
            return cls.getName() + method.getName();
        }
        return null;
    }

    /**
     * 获取方法标识符：类名+方法名.
     * <p>
     * TODO 这种方法不可取，没有考虑到方法的重载！
     *
     * @param cls    类
     * @param method 方法
     * @return 方法标识符
     */
    public static String getMethodIdentity(Class<?> cls, String method) {
        if (cls == null) {
            return null;
        }
        if (StringKit.isAllNotEmpty(cls.getName(), method)) {
            return cls.getName() + method;
        }
        return null;
    }
}
