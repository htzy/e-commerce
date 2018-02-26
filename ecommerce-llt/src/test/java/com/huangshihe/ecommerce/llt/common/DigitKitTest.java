package com.huangshihe.ecommerce.llt.common;

import com.huangshihe.ecommerce.common.kits.DigitKit;
import cucumber.api.PendingException;
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
    private static long resultLong;
    private static String resultStr;
    private static String str;
    private static int resultInt;

    @Given("^待转换十六进制字符串为\"([^\"]*)\"$")
    public void 待转换十六进制字符串为(String arg0) throws Throwable {
        str16 = arg0;
    }

    @When("^十六进制字符串转为long$")
    public void 十六进制字符串转为long() throws Throwable {
        resultLong = DigitKit.fromHexStr(str16);
    }

    @Then("^转换long结果为\"([^\"]*)\"$")
    public void 转换long结果为(String arg0) throws Throwable {
        Assert.assertTrue(Long.valueOf(arg0) == resultLong);
    }


    @When("^含汉字的十六进制转字符串$")
    public void 含汉字的十六进制转字符串() throws Throwable {
        resultStr = DigitKit.fromUHexStr(str16);
    }

    @Then("^转换str结果为\"([^\"]*)\"$")
    public void 转换str结果为(String arg0) throws Throwable {
        Assert.assertEquals(arg0, resultStr);
    }

    @Given("^待计算的字符串为\"([^\"]*)\"$")
    public void 待计算的字符串为(String arg0) throws Throwable {
        str = arg0;
    }

    @When("^计算utf8字符串长度$")
    public void 计算utf8字符串的长度() throws Throwable {
        resultInt = DigitKit.getUtf8Len(str);
    }

    @Then("^计算utf8字符串长度的结果为\"([^\"]*)\"$")
    public void 计算utf8字符串长度的结果为(String arg0) throws Throwable {
        Assert.assertTrue(Integer.valueOf(arg0) == resultInt);
    }
}
