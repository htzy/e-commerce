package com.huangshihe.ecommerce.common.kits;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
     * 日志.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassKit.class);

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


    /**
     * 强行通过构造方法创建新实例.
     *
     * @param clazz   实例类
     * @param objects 构造方法入参
     * @param <T>     类类型
     * @return 实例
     */
    public static <T> T newInstance(Class<T> clazz, Object... objects) {
        if (clazz == null) {
            LOGGER.warn("class is null! objects:{}", objects);
            throw new IllegalArgumentException("class is null!");
        }
        Constructor<T> constructor;
        // 参数为空，则寻找该类的无参构造方法
        if (ArrayKit.isEmpty(objects)) {
            try {
                constructor = clazz.getConstructor();
            } catch (NoSuchMethodException e) {
                LOGGER.error("NoSuchMethodException, constructor of empty param...");
                throw new IllegalArgumentException(e);
            }
        } else {
            // 创建参数类类型数组
            Class[] paramsClass = new Class[objects.length];
            // 获取所有的构造方法，逐一筛选
            Constructor[] constructors = clazz.getConstructors();
            LOGGER.debug("all constructors:{}", Arrays.toString(constructors));
            for (Constructor c : constructors) {
                // 获取构造方法的所有参数类类型
                Class[] parameterTypes = c.getParameterTypes();
                // 直接跳过无参的构造方法（上面已经处理完毕无参构造方法）
                if (ArrayKit.isNotEmpty(parameterTypes)) {
                    // 逐个判断传入的参数与已有的构造方法参数是否一致，或传入的参数类型是构造参数的子类或实现类
                    for (int i = 0; i < parameterTypes.length; i++) {
                        if (parameterTypes[i].isAssignableFrom(objects[i].getClass())) {
                            paramsClass[i] = parameterTypes[i];
                        } else {
                            break;
                        }
                    }
                }
            }
            // 如果参数类数组最后元素不为空，则匹配上了，进而用于获取构造方法
            if (paramsClass[objects.length - 1] != null) {
                try {
                    constructor = clazz.getConstructor(paramsClass);
                } catch (NoSuchMethodException e) {
                    LOGGER.error("NoSuchMethodException, objects:{}", objects);
                    throw new IllegalArgumentException(e);
                }
            } else {
                LOGGER.error("paramsClass:{}", Arrays.toString(paramsClass));
                throw new IllegalArgumentException("new instance failed! class:" + clazz + " params:"
                        + ArrayKit.toString(Arrays.asList(objects)));
            }
        }
        // 检查该构造方法的可访问性，若不可访问，则强制置为可访问状态
        if (!constructor.isAccessible()) {
            constructor.setAccessible(true);
        }
        try {
            // 通过参数构造实例
            return constructor.newInstance(objects);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            LOGGER.error("constructor newInstance fail! detail:{}", e);
            throw new IllegalArgumentException(e);
        }
    }

}
