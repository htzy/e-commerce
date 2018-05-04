package com.huangshihe.ecommerce.common.aop;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Invocation.
 * 该类只针对某对象的单个方法.
 * <p>
 * Create Date: 2018-03-15 22:30
 *
 * @author huangshihe
 */
public class Invocation {
    private Object target;
    private Method method;
    private Object[] args;
    private MethodProxy methodProxy;
    private Interceptor[] inters;
    private Object returnValue;

    private int index = 0;

    /**
     * 构造方法
     *
     * @param target      被增强对象，即原类的子类对象
     * @param method      当前拦截的方法
     * @param args        当前拦截方法的参数
     * @param methodProxy 方法代理
     * @param inters      拦截器
     */
    public Invocation(Object target, Method method, Object[] args, MethodProxy methodProxy, Interceptor[] inters) {
        this.target = target;
        this.method = method;
        this.args = args;
        this.methodProxy = methodProxy;
        this.inters = inters;
    }

    public void invoke() {
        // 首先依次执行所有的拦截器，最后再执行原方法，返回结果
        // TODO 多个拦截器的情况待测试，并需要将流程补充到doc<jfinal-拦截器流程.eddx>中
        if (index < inters.length) {
            inters[index++].intercept(this);
        } else if (index++ == inters.length) {    // index++ ensure invoke action only one time
            try {
                // Invoke the method
                // if (!Modifier.isAbstract(method.getModifiers()))
                // returnValue = methodProxy.invokeSuper(target, args);
                // 由于target为被增强对象的子类对象，所以这里执行原方法，即target父类的方法，并传入参数
                // 被拦截后的方法的执行结果，若原方法为void，则这里返回null
                returnValue = methodProxy.invokeSuper(target, args);
            } catch (InvocationTargetException e) {
                Throwable t = e.getTargetException();
                throw t instanceof RuntimeException ? (RuntimeException) t : new RuntimeException(e);
            } catch (RuntimeException e) {
                throw e;
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }
    }

    public Object getArg(int index) {
        if (index >= args.length)
            throw new ArrayIndexOutOfBoundsException();
        return args[index];
    }

    public void setArg(int index, Object value) {
        if (index >= args.length)
            throw new ArrayIndexOutOfBoundsException();
        args[index] = value;
    }

    public Object[] getArgs() {
        return args;
    }

    /**
     * Get the target object which be intercepted
     * <pre>
     * Example:
     * OrderService os = getTarget();
     * </pre>
     */
    public <T> T getTarget() {
        return (T) target;
    }

    /**
     * Return the method of this action.
     * <p>
     * You can getMethod.getAnnotations() to get annotation on action method to do more things
     */
    public Method getMethod() {
        return method;
    }

    /**
     * Return the method name of this action's method.
     */
    public String getMethodName() {
        return method.getName();
    }

    /**
     * Get the return value of the target method
     */
    public <T> T getReturnValue() {
        return (T) returnValue;
    }

    /**
     * Set the return value of the target method
     */
    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }
}
