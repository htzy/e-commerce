package com.huangshihe.ecommercellt;

import com.huangshihe.ecommercellt.ecommcespark.config.ConfigurationManager;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

/**
 * Created by huangshihe on 30/11/2017.
 */
public class ConfigurationTest {
    private static String author;

    @Given("^配置文件已存在$")
    public void 配置文件已存在() throws Throwable {
    }

    @When("^查询\"([^\"]*)\"的配置值$")
    public void 查询的配置值(String arg0) throws Throwable {
        author = ConfigurationManager.getProperties(arg0);
    }

    @Then("^查询结果不为空$")
    public void 查询结果不为空() throws Throwable {
        Assert.assertNotNull(author);
    }
}
