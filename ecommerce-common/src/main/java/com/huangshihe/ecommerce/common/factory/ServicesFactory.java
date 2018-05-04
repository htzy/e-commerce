package com.huangshihe.ecommerce.common.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务工厂类.
 * 服务使用工厂方式获取实例，首先到manager中找，如果没有的话，再创建实例，不要直接显式的创建实例。
 * 存放在_servicesMap中的Object只有一个，若在配置文件中多次指定同一个类且不同的方法，则可以采取更新Object的方法（实际为更新Object对应Method数组）
 * <p>
 * Create Date: 2018-03-17 23:39
 *
 * @author huangshihe
 */
@SuppressWarnings("unchecked")
public class ServicesFactory extends Factory {

    private static Logger LOGGER = LoggerFactory.getLogger(ServicesFactory.class);

    private static ServicesFactory _instance = new ServicesFactory();

    private ServicesFactory() {

    }

    public static ServicesFactory getInstance() {
        return _instance;
    }

    private final Map<Class<?>, Object> _servicesMap = new ConcurrentHashMap<Class<?>, Object>();

    /**
     * 获取Service对象.
     *
     * @param serviceClass Service类
     * @param <T>          类类型
     * @return Service对象
     */
    public <T> T getServiceObjectOrNew(Class<T> serviceClass) {

        T object = getServiceObject(serviceClass);
        // 如果不存在，则新建一个Service，且该新建的Service对象将不会主动存放到map中，若需要存放，则需要手动存放
        // 这样可以保证，在map中没有该对象时，可以满足需要多个新建的service对象的场景。
        if (null == object) {
            object = createNewObject(serviceClass);
        }
        return object;
    }

    /**
     * 获取Service对象.
     *
     * @param serviceClass Service类
     * @param <T>          类类型
     * @return Service对象
     */
    public <T> T getServiceObject(Class<T> serviceClass) {
        return (T) _servicesMap.get(serviceClass);
    }

    public void addServiceObject(Class<?> serviceClass, Object serviceObject) {
        if (serviceClass == null || serviceObject == null) {
            return;
        }
        // 若该类对应的object已存在则更新
        _servicesMap.put(serviceClass, serviceObject);
    }
}
