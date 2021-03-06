package com.huangshihe.ecommerce.llt.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.huangshihe.ecommerce.common.kits.JsonKit;
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
public class JsonKitTest {

    private static Simple simple;

    private static String simpleStr;

    private static JsonNode jsonNode;
    
    private JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;

    @Given("^待转换对象已存在$")
    public void 待转换对象已存在() throws Throwable {
        simple = new Simple();
        simple.setId(666);
        simple.setName("htzy");
    }

    @When("^对象转字符串$")
    public void 对象转字符串() throws Throwable {
        simpleStr = JsonKit.objToStr(simple);
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
        simple = JsonKit.strToObj(simpleStr, Simple.class);
    }

    @Then("^转换对象正确$")
    public void 转换对象正确() throws Throwable {
        Simple right = new Simple();
        right.setId(666);
        right.setName("htzy");
        // 在Simple中覆盖了equals方法
        Assert.assertEquals(right, simple);
    }

    @When("^字符串转Tree$")
    public void 字符串转tree() throws Throwable {
        jsonNode = JsonKit.strToTree(simpleStr);
    }

    @Then("^转换Tree正确$")
    public void 转换tree正确() throws Throwable {
        Assert.assertTrue(jsonNode.path("id").asInt() == 666);
        Assert.assertEquals("htzy", jsonNode.path("name").asText());
    }

    @When("^对象转Tree$")
    public void 对象转tree() throws Throwable {
        jsonNode = JsonKit.objToTree(simple);
    }

    @Given("^待转换Tree已存在$")
    public void 待转换tree已存在() throws Throwable {
        ObjectNode rootNode = jsonNodeFactory.objectNode();
        rootNode.put("id", 666);
        rootNode.put("name", "htzy");
        jsonNode = rootNode;
    }

    @When("^Tree转字符串$")
    public void tree转字符串() throws Throwable {
        simpleStr = JsonKit.treeToStr(jsonNode);
    }

}
