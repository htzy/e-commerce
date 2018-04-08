package com.huangshihe.ecommerce.llt.pub;

import com.huangshihe.ecommerce.pub.Main.Main;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Create Date: 2018-04-08 15:08
 *
 * @author huangshihe
 */
public class MainTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainTest.class);

    @Given("^配置文件已存在$")
    public void 配置文件已存在() throws Throwable {

    }

    @When("^执行初始化任务$")
    public void 执行初始化任务() throws Throwable {
        Main.init();
    }

    @Then("^初始化完成$")
    public void 初始化完成() throws Throwable {
        LOGGER.debug("current thread:{}", Thread.currentThread());// main
        Thread.sleep(20000);
    }
}
