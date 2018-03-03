package com.huangshihe.ecommerce.pub;

import com.huangshihe.ecommerce.pub.threadpool.ThreadPoolEntity;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
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
    private static ThreadPoolEntity threadPoolEntity;

    @Given("^待转换的xml文件名为\"([^\"]*)\"$")
    public void 待转换的xml文件名为(String arg0) throws Throwable {
        xmlFileName = arg0;
    }

    @When("^线程池xml转bean$")
    public void 线程池xml转bean() throws Throwable {
        String threadPoolXmlFile = "data" + File.separator + "pub" + File.separator + "threadpool" + File.separator + xmlFileName;
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(threadPoolXmlFile);

        JAXBContext threadPoolContext = JAXBContext.newInstance(ThreadPoolEntity.class);
        Unmarshaller unmarshaller = threadPoolContext.createUnmarshaller();
        threadPoolEntity = (ThreadPoolEntity) unmarshaller.unmarshal(inputStream);
    }

    @Then("^线程池xml转bean成功$")
    public void 线程池xml转bean成功() throws Throwable {
        Assert.assertNotNull(threadPoolEntity);
    }
}
