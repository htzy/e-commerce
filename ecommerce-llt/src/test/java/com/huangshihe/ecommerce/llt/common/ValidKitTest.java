package com.huangshihe.ecommerce.llt.common;

import com.huangshihe.ecommerce.common.kits.ValidKit;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

/**
 * 校验工具类测试.
 * <p>
 * Create Date: 2018-01-07 16:54
 *
 * @author huangshihe
 */
public class ValidKitTest {

    private static String jsonData;
    private static String dataField;
    private static String jsonParam;
    private static String paramField;
    private static boolean actualResult;

    @Given("^param和data数据已存在$")
    public void param和data数据已存在() throws Throwable {

    }

    @And("^json数据为\"([^\"]*)\"$")
    public void json数据为(String arg0) throws Throwable {
        jsonData = arg0;
    }

    @And("^数据项为\"([^\"]*)\"$")
    public void 数据项为(String arg0) throws Throwable {
        dataField = arg0;
    }

    @And("^json参数为\"([^\"]*)\"$")
    public void json参数为(String arg0) throws Throwable {
        jsonParam = arg0;
    }

    @And("^参数数据项为\"([^\"]*)\"$")
    public void 参数数据项为(String arg0) throws Throwable {
        paramField = arg0;
    }

    @When("^判断是否存在$")
    public void 判断是否存在() throws Throwable {
        actualResult = ValidKit.in(jsonData, dataField, jsonParam, paramField);
    }

    @Then("^结果为\"([^\"]*)\"$")
    public void 结果为(String arg0) throws Throwable {
        System.out.println("result: arg0:"+arg0 + " boolean: "+ Boolean.valueOf(arg0));
        Assert.assertEquals(actualResult, Boolean.valueOf(arg0));
    }

}
