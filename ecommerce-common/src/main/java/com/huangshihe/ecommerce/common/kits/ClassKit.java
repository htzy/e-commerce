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
     * 通过基本类型的包装类获取基本类型，即：对基本类型进行手动拆箱。
     * 参考：
     * http://commons.apache.org/proper/commons-beanutils/apidocs/src-html/org/apache/commons/beanutils/MethodUtils.html#line.1195
     *
     * @param wrapperType 基本类型的包装类
     * @return 基本类型
     */
    public static Class<?> getPrimitiveType(final Class<?> wrapperType) {
        if (Boolean.class.equals(wrapperType)) {
            return boolean.class;
        } else if (Float.class.equals(wrapperType)) {
            return float.class;
        } else if (Long.class.equals(wrapperType)) {
            return long.class;
        } else if (Integer.class.equals(wrapperType)) {
            return int.class;
        } else if (Short.class.equals(wrapperType)) {
            return short.class;
        } else if (Byte.class.equals(wrapperType)) {
            return byte.class;
        } else if (Double.class.equals(wrapperType)) {
            return double.class;
        } else if (Character.class.equals(wrapperType)) {
            return char.class;
        } else {
            LOGGER.warn("Not a known primitive wrapper class:{}", wrapperType);
            return null;
        }
    }

    /**
     * 判断是否为类
     *
     * @param object object
     * @return 是否为类。
     */
    public static boolean isClass(Object object) {
        return object instanceof Class;
    }


    /**
     * 强行通过构造方法创建新实例.
     * 构造方法参数中可能存在基本类型，但传入的实际的值会自动封装为包装类，因此需要手动拆箱。
     * 这里不考虑隐式转换，即int值可自动转为long类型，如：
     * Foo(long param); new Foo(123);
     * 这时使用newInstance方法可能会报错：找不到方法，因为直接传入数值：123，jvm会自动包装为Integer类，应该使用如下方法调用：
     * Foo(long param); long param=123; new Foo(param);
     * 若考虑隐式转换，可能导致因自动装箱、拆箱机制导致出现实际参数可以匹配多个构造方法的问题，如上，可能匹配到int和long两类。
     *
     * @param clazz      实例类
     * @param initParams 构造方法实际入参对象，若值为null，则需传入指定的类类型，如String.class
     * @param <T>        类类型
     * @return 实例
     */
    public static <T> T newInstance(final Class<T> clazz, final Object... initParams) {
        if (clazz == null) {
            LOGGER.warn("class is null! objects:{}", initParams);
            throw new IllegalArgumentException("class is null!");
        }
        // 拷贝一份，TODO 区分深复制和浅复制
        Object[] paramsObject = null;
        if (initParams != null) {
            paramsObject = initParams.clone();
        }
        // TODO 如果该类是非public，是否需要特殊处理？
        // 在处理修饰符时，具体的值是十六进制组合而成，如final为0x10，而public为0x01，则public final的类为0x11即17
        if (!Modifier.isPublic(clazz.getModifiers())) {
            // TODO 暂时不支持？
            LOGGER.warn("new instance class:{} is not public, unsafe!", clazz);
            throw new IllegalArgumentException("new instance class is not public, unsafe!");
        }
        Constructor<T> constructor;
        // 参数为空，则寻找该类的无参构造方法
        if (ArrayKit.isEmpty(paramsObject)) {
            try {
                // TODO 为什么调用scala中的私有构造方法，通过getConstructors可以成功？应该是失败，而通过getDeclaredConstructors成功才对
                constructor = clazz.getDeclaredConstructor();
            } catch (NoSuchMethodException e) {
                LOGGER.error("NoSuchMethodException, constructor of empty param...");
                throw new IllegalArgumentException(e);
            }
        } else {
            // 检查传入的实际参数中是否有null
            if (ArrayKit.isContainEmpty(paramsObject)) {
                LOGGER.error("new instance fail! initParams:{} contains null!", paramsObject);
                throw new IllegalArgumentException("new instance fail! initParams contains null!");
            }
            // 创建实际参数类类型数组
            Class[] paramsClass = new Class[paramsObject.length];
            // 获取所有的构造方法，逐一筛选
            Constructor[] constructors = clazz.getDeclaredConstructors();
            LOGGER.debug("all constructors:{}", Arrays.toString(constructors));
            for (Constructor c : constructors) {
                // 获取构造方法的所有参数类类型
                Class[] parameterTypes = c.getParameterTypes();
                // 直接跳过无参的构造方法（上面已经处理完毕无参构造方法）
                if (ArrayKit.isNotEmpty(parameterTypes)) {
                    // 逐个判断传入的参数与已有的构造方法参数是否一致，或传入的参数类型是构造参数的子类或实现类
                    for (int i = 0; i < parameterTypes.length; i++) {
                        // 判断实际的值的类型与当前构造方法的类型是否一致
                        // 1. 基本类型的值不允许是null，所以判断基本类型在前；
                        // 当构造方法中的参数类型是基本类型时，对实际参数执行拆箱，并检查是否参数类型是否匹配，这里不考虑隐式转换的优先级
                        if (parameterTypes[i].isPrimitive()
                                && parameterTypes[i].equals(getPrimitiveType(paramsObject[i].getClass()))) {
                            paramsClass[i] = parameterTypes[i];
                        }
                        // 2. 如果传入实际参数为类类型，则判断该类类型与实际参数是否一致，若是，则将值置为null
                        else if (isClass(paramsObject[i]) && parameterTypes[i].equals(paramsObject[i])) {
                            paramsClass[i] = (Class) paramsObject[i];
                            paramsObject[i] = null;
                        }
                        // 3. 检查实际参数是否为构造方法中参数的子类或实现类
                        else if (parameterTypes[i].isAssignableFrom(paramsObject[i].getClass())) {
                            paramsClass[i] = parameterTypes[i];
                        } else {
                            break;
                        }
                    }
                    // 如果已匹配到，则不再遍历下一个构造方法
                    if (paramsClass[paramsClass.length - 1] != null) {
                        break;
                    }
                }
            }
            // 如果参数类数组最后元素不为空，则匹配上了，进而用于获取构造方法
            if (paramsClass[paramsClass.length - 1] != null) {
                try {
                    constructor = clazz.getDeclaredConstructor(paramsClass);
                } catch (NoSuchMethodException e) {
                    LOGGER.error("NoSuchMethodException, objects:{}", paramsObject);
                    throw new IllegalArgumentException(e);
                }
            } else {
                LOGGER.error("NoSuchMethodException paramsClass:{}, paramsObject:{}", Arrays.toString(paramsClass),
                        Arrays.toString(paramsObject));
                throw new IllegalArgumentException("new instance failed! noSuch method, class:" + clazz + " params:"
                        + ArrayKit.toString(Arrays.asList(paramsObject)));
            }
        }
        // 检查该构造方法的可访问性，若不可访问，则强制置为可访问状态
        if (!constructor.isAccessible()) {
            constructor.setAccessible(true);
        }
        try {
            // 通过参数构造实例
            return constructor.newInstance(paramsObject);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            LOGGER.error("constructor newInstance fail! detail:{}", e);
            throw new IllegalArgumentException(e);
        }
    }

}
