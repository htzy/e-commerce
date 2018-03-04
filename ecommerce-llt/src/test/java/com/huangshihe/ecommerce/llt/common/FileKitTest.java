package com.huangshihe.ecommerce.llt.common;

import com.huangshihe.ecommerce.common.kits.FileKit;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import java.net.URL;

/**
 * <p>
 * Create Date: 2018-03-04 21:40
 *
 * @author huangshihe
 */
public class FileKitTest {

    private static String filePath;
    private static int nums;

    @Given("^给定的文件夹路径为\"([^\"]*)\"$")
    public void 给定的文件夹路径为(String arg0) throws Throwable {
        URL url = Thread.currentThread().getContextClassLoader().getResource(arg0);
        // 这里如果抛出空指针，则用例失败
        if (url == null) {
            throw new NullPointerException("给定的文件夹路径不存在");
        }
        filePath = url.getPath();
    }

    @When("^获取文件夹下的所有文件$")
    public void 获取文件夹下的所有文件() throws Throwable {
        nums = FileKit.getAllFiles(filePath).size();
    }

    @Then("^获取文件夹下的所有文件数目为\"([^\"]*)\"$")
    public void 获取文件夹下的所有文件数目为(String arg0) throws Throwable {
        Assert.assertEquals(Integer.toString(nums), arg0);
    }
}
