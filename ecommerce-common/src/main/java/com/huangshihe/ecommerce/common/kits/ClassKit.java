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
     * @param clazz      实例类
     * @param initParams 构造方法入参
     * @param <T>        类类型
     * @return 实例
     */
    public static <T> T newInstance(Class<T> clazz, Object... initParams) {
        if (clazz == null) {
            LOGGER.warn("class is null! objects:{}", initParams);
            throw new IllegalArgumentException("class is null!");
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
        if (ArrayKit.isEmpty(initParams)) {
            try {
                // TODO 两个问题：第一个：无参的时候，这里是否可以成功？
                // TODO 第二个：为什么调用scala中的私有构造方法，通过getConstructors可以成功？应该是失败，而通过getDelcar成功才对
                // TODO 第三个：当类为非public时，所有场景下是否有异常？
                constructor = clazz.getDeclaredConstructor();
            } catch (NoSuchMethodException e) {
                LOGGER.error("NoSuchMethodException, constructor of empty param...");
                throw new IllegalArgumentException(e);
            }
        } else {
            // 创建参数类类型数组
            Class[] paramsClass = new Class[initParams.length];
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
                        // TODO 可能存在基本类型，即传入的实际的值会自动封装为包装类，而方法中指定的类型为基本类型
                        // TODO 还需要判断转换优先级？
                        // TODO 在这里增加手动拆箱方法，
                        if (parameterTypes[i].isPrimitive() && parameterTypes[i].isInstance(initParams[i].getClass())) {
                            LOGGER.info("123");
                        }

                        if (parameterTypes[i].isAssignableFrom(initParams[i].getClass())) {
                            paramsClass[i] = parameterTypes[i];
                        } else {
                            break;
                        }
                    }
                }
            }
            // 如果参数类数组最后元素不为空，则匹配上了，进而用于获取构造方法
            // TODO 若由于自动装箱、拆箱机制导致出现实际参数可以匹配多个构造方法，则以上会取最后一个？不合适吧？
            // TODO 如果参数类数组最后本来就是null，咋整？那样的话，类型也不知道，当要创建的值的null时，则需要传入参数的类类型
            if (paramsClass[initParams.length - 1] != null) {
                try {
                    constructor = clazz.getDeclaredConstructor(paramsClass);
                } catch (NoSuchMethodException e) {
                    LOGGER.error("NoSuchMethodException, objects:{}", initParams);
                    throw new IllegalArgumentException(e);
                }
            } else {
                LOGGER.error("paramsClass:{}", Arrays.toString(paramsClass));
                throw new IllegalArgumentException("new instance failed! class:" + clazz + " params:"
                        + ArrayKit.toString(Arrays.asList(initParams)));
            }
        }
        // 检查该构造方法的可访问性，若不可访问，则强制置为可访问状态
        if (!constructor.isAccessible()) {
            constructor.setAccessible(true);
        }
        try {
            // 通过参数构造实例
            return constructor.newInstance(initParams);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            LOGGER.error("constructor newInstance fail! detail:{}", e);
            throw new IllegalArgumentException(e);
        }
    }

}

class Foo {

    Foo(int id) {
        System.out.println("int");
    }

    Foo(short id) {
        System.out.println("short");
    }

    Foo(long id) {
        System.out.println("long");
    }

    public static void main(String[] args) {
        // 由变量传入之后，可以带入类型
        short i = 1;
        new Foo(i);         // short
        // 若直接传值，则默认为int
        new Foo(2);     // int
    }

}

