package com.huangshihe.ecommerce.common.aop;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Invocation.
 * <p>
 * Create Date: 2018-03-15 22:30
 *
 * @author huangshihe
 */
public class Invocation {
    // Prevent new Object[0] by jvm for paras of action invocation.
    private static final Object[] NULL_ARGS = new Object[0];

    boolean useInjectTarget;
    private Object target;
    private Method method;
    private Object[] args;
    private MethodProxy methodProxy;
    private Interceptor[] inters;
    private Object returnValue;

    private int index = 0;

    public Invocation(Object target, Method method, Object[] args, MethodProxy methodProxy, Interceptor[] inters) {
        this.target = target;
        this.method = method;
        this.args = args;
        this.methodProxy = methodProxy;
        this.inters = inters;
    }

    public void invoke() {
        if (index < inters.length) {
            inters[index++].intercept(this);
        } else if (index++ == inters.length) {    // index++ ensure invoke action only one time
            try {
                // Invoke the method
                // if (!Modifier.isAbstract(method.getModifiers()))
                // returnValue = methodProxy.invokeSuper(target, args);
                if (useInjectTarget)
                    returnValue = methodProxy.invoke(target, args);
                else
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
