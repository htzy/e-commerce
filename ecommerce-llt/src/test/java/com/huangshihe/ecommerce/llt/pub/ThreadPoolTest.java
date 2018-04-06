package com.huangshihe.ecommerce.llt.pub;

import com.huangshihe.ecommerce.common.factory.ServicesFactory;
import com.huangshihe.ecommerce.common.kits.XmlKit;
import com.huangshihe.ecommerce.llt.pub.threadpool.SimpleService1;
import com.huangshihe.ecommerce.pub.config.ConfigEntity;
import com.huangshihe.ecommerce.pub.config.threadpool.ThreadPoolManager;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.ScheduledExecutorService;

/**
 * <p>
 * Create Date: 2018-03-03 19:11
 *
 * @author huangshihe
 */
public class ThreadPoolTest {

    private static String xmlFileName;
    private static ConfigEntity configEntity;
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPoolTest.class);

    @Given("^待转换的xml文件名为\"([^\"]*)\"$")
    public void 待转换的xml文件名为(String arg0) throws Throwable {
        xmlFileName = arg0;
    }

    @When("^线程池xml转bean$")
    public void 线程池xml转bean() throws Throwable {
        String threadPoolXmlFile = "data" + File.separator + "pub" + File.separator + "threadpool" +
                File.separator + xmlFileName;
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(threadPoolXmlFile);
        configEntity = XmlKit.toEntity(ConfigEntity.class, inputStream);
    }

    @Then("^线程池xml转bean成功$")
    public void 线程池xml转bean成功() throws Throwable {
        Assert.assertNotNull(configEntity);
    }

    @And("^新建线程池$")
    public void 新建线程池() throws Throwable {
        ThreadPoolManager.getInstance().createPools(configEntity);
    }

    @And("^显式调用任务$")
    public void 显式调用任务() throws Throwable {
        // 获取加强对象，服务使用工厂方式获取实例，首先到manager中找，如果没有的话，再创建实例，不要直接显式的创建实例
        // 这里没有设置线程池拦截器，只需要配置就可以将任务放到线程池中运行，若之后还有新的拦截器，同样可以生效
        SimpleService1 service = ServicesFactory.getInstance().getServiceObject(SimpleService1.class);
        service.drawLine();
        service.toString();
    }

    @Then("^任务运行在线程池中$")
    public void 任务运行在线程池中() throws Throwable {

    }

    @Then("^任务在线程池中定时运行$")
    public void 任务在线程池中定时运行() throws Throwable {
        ScheduledExecutorService executor = ThreadPoolManager.getInstance().
                getScheduleExecutor(SimpleService1.class, SimpleService1.class.getMethod("increaseCount"));
        LOGGER.debug("ScheduledExecutorService::{}", executor);

        // count 从0开始，每秒递增1
        for (int i = 0; i < 3; i++) {
            LOGGER.debug("count::{}", SimpleService1.getCount());
            Assert.assertTrue(SimpleService1.getCount() >= i);
            Thread.sleep(1000);
        }
        // 停止该线程池
        executor.shutdown();
        // 停止缓冲时间
        Thread.sleep(1000);
        // 判断是否已经停止
        Assert.assertTrue(executor.isShutdown());
        // 判断定时任务是否还在工作
        int count = SimpleService1.getCount();
        for (int i = 0; i < 2; i++) {
            Thread.sleep(1000);
            Assert.assertTrue(count == SimpleService1.getCount());
        }
    }
}
