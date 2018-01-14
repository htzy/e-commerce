package com.huangshihe.ecommerce.llt.common;

import com.huangshihe.ecommerce.common.kits.DigitKit;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

/**
 * 数据工具类测试.
 * <p>
 * Create Date: 2018-01-14 16:25
 *
 * @author huangshihe
 */
public class DigitKitTest {

    private static String str16;
    private static int resultInt;

    @Given("^待转换十六进制字符串为\"([^\"]*)\"$")
    public void 待转换十六进制字符串为(String arg0) throws Throwable {
        str16 = arg0;
    }

    @When("^十六进制字符串转为int$")
    public void 十六进制字符串转为int() throws Throwable {
        resultInt = DigitKit.fromHexStr(str16);
    }

    @Then("^转换结果为\"([^\"]*)\"$")
    public void 转换结果为(String arg0) throws Throwable {
        Assert.assertTrue(Integer.valueOf(arg0) == resultInt);
    }


}
