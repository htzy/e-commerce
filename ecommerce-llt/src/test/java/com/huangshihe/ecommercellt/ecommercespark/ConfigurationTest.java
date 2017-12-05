package com.huangshihe.ecommercellt.ecommercespark;

import com.huangshihe.ecommerce.ecommercespark.config.ConfigurationManager;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

/**
 * 配置管理测试类
 * <p>
 * Create Date: 2017-12-01 23:02
 *
 * @author huangshihe
 */
public class ConfigurationTest {//NOPMD
    /**
     * 作者信息
     */
    private static String author;

    @Given("^配置文件已存在$")
    public void 配置文件已存在() throws Throwable {//NOPMD
        // 暂时不需要考虑，配置文件不存在的情况，因此这里暂时不检查
    }

    @When("^查询\"([^\"]*)\"的配置值$")
    public void 查询的配置值(final String arg0) throws Throwable {//NOPMD
        author = ConfigurationManager.getBasicConfiguration().getProperty(arg0);//NOPMD
    }

    @Then("^查询结果不为空$")
    public void 查询结果不为空() throws Throwable {//NOPMD
        Assert.assertNotNull(author);
    }
}
