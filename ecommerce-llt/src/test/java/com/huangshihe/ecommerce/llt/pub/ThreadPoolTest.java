package com.huangshihe.ecommerce.llt.pub;

import com.huangshihe.ecommerce.llt.pub.threadpool.SimpleWork1;
import com.huangshihe.ecommerce.pub.config.ConfigEntity;
import com.huangshihe.ecommerce.pub.config.threadpool.ServiceConfigEntity;
import com.huangshihe.ecommerce.pub.config.threadpool.TaskEntity;
import com.huangshihe.ecommerce.pub.config.threadpool.ThreadPoolEntity;
import com.huangshihe.ecommerce.pub.config.threadpool.ThreadTask;
import cucumber.api.PendingException;
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
            ThreadPoolEntity pool = serviceConfig.getThreadPoolEntity();
            ThreadPoolExecutor executor = new ThreadPoolExecutor(pool.getPoolSize(), pool.getMaxPoolSize(),
                    pool.getKeepAliveTime(), TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
            for (TaskEntity task : serviceConfig.getTaskEntities()) {
                executor.submit(new ThreadTask(task));
            }
        }
    }

    @And("^显示调用任务$")
    public void 显示调用任务() throws Throwable {
//        SimpleWork1.drawLine();
    }

    @Then("^任务运行在线程池中$")
    public void 任务运行在线程池中() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
}
