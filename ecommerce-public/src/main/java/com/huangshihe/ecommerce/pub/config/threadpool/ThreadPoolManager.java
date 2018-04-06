package com.huangshihe.ecommerce.pub.config.threadpool;

import com.huangshihe.ecommerce.common.aop.Enhancer;
import com.huangshihe.ecommerce.common.factory.ServicesFactory;
import com.huangshihe.ecommerce.common.kits.AopKit;
import com.huangshihe.ecommerce.common.kits.StringKit;
import com.huangshihe.ecommerce.common.kits.TimeKit;
import com.huangshihe.ecommerce.pub.config.Config;
import com.huangshihe.ecommerce.pub.config.ConfigEntity;
import com.huangshihe.ecommerce.pub.config.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池管理类.
 * 拿到所有的配置文件实体对象进行初始化线程池.
 * <p>
 * Create Date: 2018-03-17 23:01
 *
 * @author huangshihe
 */
@SuppressWarnings("unchecked")
public class ThreadPoolManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPoolManager.class);

    private static ThreadPoolManager _instance = new ThreadPoolManager();

    private ThreadPoolManager() {
        init();
    }

    public static ThreadPoolManager getInstance() {
        return _instance;
    }

    private final ServicesFactory _serviceFactory = ServicesFactory.getInstance();

    // 存放线程池执行对象 key: 线程池执行名; value：线程池执行对象
    private Map<String, ExecutorService> _executorMap = new ConcurrentHashMap<>();

    // 存放方法标识符，key：方法标识符；value：线程池执行名
    private Map<String, String> _methodIdentityMap = new ConcurrentHashMap<String, String>();

    /**
     * 初始化，获取threadPool的配置文件，并依此创建线程池
     */
    private void init() {
        LOGGER.info("init ThreadPoolManager begin...");
        List<Config> configs = ConfigManager.getInstance().getThreadPoolConfig();
        configs.stream().map(Config::getConfigEntity).forEach(this::createPools);
        LOGGER.info("init ThreadPoolManager end...");
    }

    /**
     * 创建线程池.
     *
     * @param configEntity 配置实体
     */
    public void createPools(ConfigEntity configEntity) {
        if (configEntity == null) {
            return;
        }
        for (ServiceConfigEntity serviceConfig : configEntity.getServiceConfigEntities()) {
            LOGGER.info("begin create pool, serviceConfigEntity:{}", serviceConfig.getIdentity());
            ThreadPoolEntity threadPool = serviceConfig.getThreadPoolEntity();
            ScheduledThreadPoolEntity scheduledThreadPool = serviceConfig.getScheduledThreadPoolEntity();
            if (threadPool != null) {
                // 新建线程池
                createThreadPool(threadPool, serviceConfig.getTaskEntities(), serviceConfig.getIdentity());
            } else if (scheduledThreadPool != null) {
                // 新建定时任务线程池
                // 这里注意：定时任务中的方法必须是静态的，否则将无法执行。
                createScheduledThreadPool(scheduledThreadPool, serviceConfig.getTaskEntities(),
                        serviceConfig.getIdentity());
            }

            LOGGER.info("end create pool, serviceConfigEntity:{}", serviceConfig.getIdentity());
        }
    }

    /**
     * 创建常规线程池.
     *
     * @param pool            池子实体
     * @param tasks           任务
     * @param serviceIdentity 服务标识符
     */
    private void createThreadPool(ThreadPoolEntity pool, List<TaskEntity> tasks, String serviceIdentity) {
        LOGGER.debug("ThreadPoolEntity:{}", pool);
        if (pool == null || tasks == null || StringKit.isEmpty(serviceIdentity)) {
            return;
        }

        ThreadPoolExecutor executor = new ThreadPoolExecutor(pool.getPoolSize(), pool.getMaxPoolSize(),
                pool.getKeepAliveTime(), TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(), new ServiceThreadFactory(serviceIdentity));

        LOGGER.debug("ThreadPoolExecutor:{}", executor);
        for (TaskEntity task : tasks) {
            Class classType = null;
            Method method = null;
            try {
                classType = Class.forName(task.getClassName());
                method = classType.getMethod(task.getMethodName());

                // 根据class，从ServiceFactory中获取对象，若为空，则新建一个增强对象
                // 如果存在，则判断是否为增强对象，如果不是，则替换为增强对象；如果是增强对象，则根据新方法增强原有对象
                Object object = _serviceFactory.getServiceObject(classType);
                if (object == null) {
                    object = Enhancer.enhance(classType, method, ThreadTaskInterceptor.class);
                } else {
                    object = Enhancer.enhance(object, method, ThreadTaskInterceptor.class);
                }
                _serviceFactory.addServiceObject(classType, object);

                addToMap(classType, method, serviceIdentity, executor);
            } catch (ClassNotFoundException e) {
                LOGGER.error("class not found! detail:{}", e);
                throw new IllegalArgumentException("class not found!");
            } catch (NoSuchMethodException e) {
                LOGGER.error("no such method! detail:{}", e);
                throw new IllegalArgumentException("no such method!");
            }
        }
    }

    /**
     * 创建定期执行线程池.
     *
     * @param pool            池子实体
     * @param tasks           任务
     * @param serviceIdentity 服务标识符
     */
    private void createScheduledThreadPool(ScheduledThreadPoolEntity pool,
                                           List<TaskEntity> tasks, String serviceIdentity) {
        LOGGER.debug("ScheduledThreadPoolEntity:{}", pool);
        if (pool == null || tasks == null || StringKit.isEmpty(serviceIdentity)) {
            return;
        }
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(pool.getPoolSize(),
                new ServiceThreadFactory(serviceIdentity));
        LOGGER.debug("ScheduledExecutorService:{}", executor);
        for (TaskEntity task : tasks) {
            long initDelay = TimeKit.toTimeSec(pool.getBeginTime()) - TimeKit.getCurrentTime();
            LOGGER.debug("initDelay1:{}", initDelay);
            initDelay = initDelay >= 0 ? initDelay : TimeKit.ONE_DAY + initDelay;
            LOGGER.debug("initDelay2:{}", initDelay);
            ThreadTask threadTask = new ThreadTask(task);
            executor.scheduleAtFixedRate(threadTask, initDelay, pool.getPeriod(), TimeUnit.MILLISECONDS);

            addToMap(threadTask.getMethodIdentity(), serviceIdentity, executor);
        }
    }


    private void addToMap(Class<?> cls, Method method, String executorIdentity, ExecutorService executor) {
        LOGGER.debug("add to map, cls:{}, method:{}, executorIdentity:{}, executor:{}",
                cls, method, executorIdentity, executor);
        String methodIdentity = AopKit.getMethodIdentity(cls, method);
        LOGGER.debug("methodIdentity:{}", methodIdentity);
        addToMap(methodIdentity, executorIdentity, executor);
    }

    private void addToMap(String methodIdentity, String executorIdentity, ExecutorService executor) {
        LOGGER.debug("add to map, methodIdentity:{}, executorIdentity:{}, executor:{}",
                methodIdentity, executorIdentity, executor);
        _executorMap.putIfAbsent(executorIdentity, executor);
        _methodIdentityMap.putIfAbsent(methodIdentity, executorIdentity);
    }

    /**
     * 根据线程执行对象名获取线程执行对象.
     *
     * @param executorIdentity 线程执行对象名
     * @return 线程执行对象
     */
    public ExecutorService getExecutor(String executorIdentity) {
        // 当map.get()参数为null时，这里会报空指针
        if (executorIdentity == null) {
            return null;
        }
        return _executorMap.get(executorIdentity);
    }

    /**
     * 根据原类获取属于该类的线程池执行对象（即该线程池中运行该类的任务）.
     *
     * @param cls    类
     * @param method 方法
     * @return 线程池执行对象
     */
    public ExecutorService getExecutor(Class<?> cls, Method method) {
        // 这里可以不用检查是否为null，如果任意一个为null，最后也将返回null
        // 如果是增强类，则应获取增强类的父类才是原始的业务类
        String methodIdentity = AopKit.getMethodIdentity(cls, method);
        // 首先根据方法标识符找到对应的池子标识符
        String executorIdentity = _methodIdentityMap.get(methodIdentity);
        // 再根据池子标识符找到对应的池子
        return getExecutor(executorIdentity);
    }

    /**
     * 根据原类获取属于该类的线程池执行对象（即该线程池中运行该类的任务）.
     *
     * @param cls 被增强类
     * @return 线程池执行对象
     */
    public ThreadPoolExecutor getThreadPoolExecutor(Class<?> cls, Method method) {
        ExecutorService executor = getExecutor(cls, method);
        if (executor instanceof ThreadPoolExecutor) {
            return (ThreadPoolExecutor) executor;
        }
        return null;
    }

    /**
     * 根据原类获取属于该类的线程池执行对象，定时执行任务。
     *
     * @param cls    定时执行类
     * @param method 方法
     * @return 线程池执行对象
     */
    public ScheduledExecutorService getScheduleExecutor(Class<?> cls, Method method) {
        ExecutorService executor = getExecutor(cls, method);
        if (executor instanceof ScheduledExecutorService) {
            return (ScheduledExecutorService) executor;
        }
        return null;
    }

}
