package com.huangshihe.ecommerce.llt.common;

import com.huangshihe.ecommerce.common.util.JsonUtil;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

/**
 * JacksonUtil测试类
 * <p>
 * Create Date: 2017-12-11 23:53
 *
 * @author huangshihe
 */
public class JsonUtilTest {

    private static Simple simple;

    private static String simpleStr;

    @Given("^待转换对象已存在$")
    public void 待转换对象已存在() throws Throwable {
        simple = new Simple();
        simple.setId(666);
        simple.setName("htzy");
    }

    @When("^对象转字符串$")
    public void 对象转字符串() throws Throwable {
        simpleStr = JsonUtil.objToStr(simple);
    }

    @Then("^转换字符串正确$")
    public void 转换字符串正确() throws Throwable {
        Assert.assertEquals("{\"id\":666,\"name\":\"htzy\"}", simpleStr);
    }

    @Given("^待转换json字符串已存在$")
    public void 待转换json字符串已存在() throws Throwable {
        simpleStr = "{\"id\":666,\"name\":\"htzy\"}";
    }

    @When("^字符串转对象$")
    public void 字符串转对象() throws Throwable {
        simple = JsonUtil.strToObj(simpleStr, Simple.class);
    }

    @Then("^转换对象正确$")
    public void 转换对象正确() throws Throwable {
        Simple right = new Simple();
        right.setId(666);
        right.setName("htzy");
        // 在Simple中覆盖了equals方法
        Assert.assertEquals(simple, right);
    }
}
