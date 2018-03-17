package com.huangshihe.ecommerce.llt.pub;

import com.huangshihe.ecommerce.common.kits.XmlKit;
import com.huangshihe.ecommerce.llt.pub.threadpool.SimpleService1;
import com.huangshihe.ecommerce.pub.factory.ServicesFactory;
import com.huangshihe.ecommerce.pub.config.ConfigEntity;
import com.huangshihe.ecommerce.pub.config.threadpool.ThreadPoolManager;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import java.io.File;
import java.io.InputStream;

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
        SimpleService1 service = ServicesFactory.getInstance().getServiceObject(SimpleService1.class);
        service.drawLine();
    }

    @Then("^任务运行在线程池中$")
    public void 任务运行在线程池中() throws Throwable {

    }
}
