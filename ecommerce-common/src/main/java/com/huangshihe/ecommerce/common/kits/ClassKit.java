package com.huangshihe.ecommerce.common.kits;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 类工具类.
 * <p>
 * Create Date: 2018-04-04 13:36
 *
 * @author huangshihe
 */
public class ClassKit {

    /**
     * 所有方法标识符.
     */
    public static final String ALL_METHOD = "*";

    /**
     * 获取类的作用域为public的所有方法，不包括继承的方法，且不包括Class自带的方法
     * TODO cls为原业务类和增强后的类，返回的方法是否一致？待测试
     *
     * @param cls 类
     * @return 方法
     */
    public static Method[] getMethods(Class<?> cls) {
        Method[] methods = cls.getDeclaredMethods();
        // 去除Class自带的所有方法
        Set<String> excludedMethodName = buildExcludedMethodName();
        Set<Method> methodSet = new HashSet<Method>(methods.length);
        for (Method method : methods) {
            if (!excludedMethodName.contains(method.getName())) {
                methodSet.add(method);
            }
        }
        return methodSet.stream().filter(method -> method.getModifiers() == Modifier.PUBLIC).toArray(Method[]::new);
//        return Arrays.stream(methods).
//                filter(method -> method.getModifiers() == Modifier.PUBLIC).toArray(Method[]::new);
        // [Ljava.lang.Object; cannot be cast to [Ljava.lang.reflect.Method;
//        return (Method[]) Arrays.stream(methods).
//                filter(method -> method.getModifiers() == Modifier.PUBLIC).toArray();
    }


    /**
     * 生成Class自带的所有作用域修饰符修饰的方法（即：private、protected、default、public；不含继承的）
     *
     * @return Class自带的所有作用域修饰符修饰的方法集合
     */
    public static final Set<String> buildExcludedMethodName() {
        Set<String> excludedMethodName = new HashSet<String>();
        Method[] methods = Object.class.getDeclaredMethods();
        for (Method m : methods) {
            excludedMethodName.add(m.getName());
        }
        // getClass() registerNatives() can not be enhanced
        // excludedMethodName.remove("getClass");
        // excludedMethodName.remove("registerNatives");
        return excludedMethodName;
    }

    /**
     * 获取方法标识符：类名+方法名+方法参数类型
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
            return cls.getName() + method.getName() + Arrays.toString(method.getParameterTypes());
        }
        return null;
    }

    /**
     * 判断是否为所有方法.
     *
     * @param method 方法名
     * @return 是否为所有方法
     */
    public static boolean isMeanAllMethod(String method) {
        if (StringKit.isEmpty(method) || ALL_METHOD.equals(method.trim())) {
            return true;
        }
        return false;
    }
}
