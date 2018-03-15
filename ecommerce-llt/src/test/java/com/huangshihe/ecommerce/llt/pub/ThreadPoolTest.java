package com.huangshihe.ecommerce.llt.pub;

import com.huangshihe.ecommerce.llt.common.Simple;
import com.huangshihe.ecommerce.llt.pub.threadpool.SimpleWork1;
import com.huangshihe.ecommerce.pub.config.ConfigEntity;
import com.huangshihe.ecommerce.pub.config.threadpool.ExecutorManager;
import com.huangshihe.ecommerce.pub.config.threadpool.ServiceConfigEntity;
import com.huangshihe.ecommerce.pub.config.threadpool.TaskEntity;
import com.huangshihe.ecommerce.pub.config.threadpool.ThreadPoolEntity;
import com.huangshihe.ecommerce.pub.config.threadpool.ThreadTaskInterceptor;
import com.jfinal.aop.Enhancer;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.InputStream;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Create Date: 2018-03-03 19:11
 *
 * @author huangshihe
 */
public class ThreadPoolTest {

    private static String xmlFileName;
    private static ConfigEntity configEntity;

    @Given("^待转换的xml文件名为\"([^\"]*)\"$")
    public void 待转换的xml文件名为(String arg0) throws Throwable {
        xmlFileName = arg0;
    }

    @When("^线程池xml转bean$")
    public void 线程池xml转bean() throws Throwable {
        String threadPoolXmlFile = "data" + File.separator + "pub" + File.separator + "threadpool" +
                File.separator + xmlFileName;
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(threadPoolXmlFile);

        JAXBContext configContext = JAXBContext.newInstance(ConfigEntity.class);
        Unmarshaller unmarshaller = configContext.createUnmarshaller();
        configEntity = (ConfigEntity) unmarshaller.unmarshal(inputStream);
    }

    @Then("^线程池xml转bean成功$")
    public void 线程池xml转bean成功() throws Throwable {
        Assert.assertNotNull(configEntity);
    }

    @And("^新建线程池$")
    public void 新建线程池() throws Throwable {
        for (ServiceConfigEntity serviceConfig : configEntity.getServiceConfigEntities()) {

//            // 注册动态代理
//            new InvocationHandler() {
//                @Override
//                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//
//                    return null;
//                }
//            };


            // 新建线程池
            ThreadPoolEntity pool = serviceConfig.getThreadPoolEntity();
            ThreadPoolExecutor executor = new ThreadPoolExecutor(pool.getPoolSize(), pool.getMaxPoolSize(),
                    pool.getKeepAliveTime(), TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
            // TODO 将线程池执行对象增加到map中，之后抽象成Manager
//            ExecutorManager.getInstance().add(serviceConfig.getIdentity(), executor);

            for (TaskEntity task : serviceConfig.getTaskEntities()) {
                Class classType = Class.forName(task.getClassName());
                // TODO 这里先不管是哪个方法，统一拦截所有方法
                Object object = Enhancer.enhance(classType, ThreadTaskInterceptor.class);

                // 增强对象
//                ExecutorManager.getInstance().add(classType, object);
                ExecutorManager.getInstance().add(classType, object, serviceConfig.getIdentity(), executor);
//                executor.submit(new ThreadTask(task));
            }
        }
    }

    @And("^显式调用任务$")
    public void 显式调用任务() throws Throwable {
        // 获取加强对象，TODO 服务使用工厂方式获取实例，首先到manager中找，如果没有的话，再创建实例，不要直接显式的创建实例
        SimpleWork1 work = (SimpleWork1) ExecutorManager.getInstance().get(SimpleWork1.class);
        work.drawLine();


//        Simple simple = Enhancer.enhance(Simple.class, new Interceptor() {
//            @Override
//            public void intercept(Invocation invocation) {
//                System.out.println(invocation.getMethodName());
//                System.out.println("-----------");
//                invocation.setArg(0, new String[]{"nice"});
//                invocation.invoke();
//            }
//        });
//        simple.setName("fail");
//        System.out.println(simple.getName());


    }

    @Then("^任务运行在线程池中$")
    public void 任务运行在线程池中() throws Throwable {

    }
}
