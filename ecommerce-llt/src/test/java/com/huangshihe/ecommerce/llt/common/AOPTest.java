package com.huangshihe.ecommerce.llt.common;

import com.huangshihe.ecommerce.common.aop.Enhancer;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

/**
 * <p>
 * Create Date: 2018-03-15 23:16
 *
 * @author huangshihe
 */
public class AOPTest {

    private static Simple simple;

    @Given("^对简单类进行增强$")
    public void 对简单类进行增强() throws Throwable {
        try {
            //TODO  1.  java.lang.ClassCastException: [Ljava.lang.Object; cannot be cast to java.lang.String

            // TODO 2.  Method threw 'java.lang.NullPointerException' exception.
// Cannot evaluate com.huangshihe.ecommerce.llt.common.Simple$$EnhancerByCGLIB$$f85e382d.toString()
            simple = Enhancer.enhance(Simple.class, SimpleInterceptor.class);
        } catch (Exception e) {
            System.out.println("1111");
            e.printStackTrace();
            System.out.println("2222");
        }
    }

    @When("^调用简单类的方法$")
    public void 调用简单类的方法() throws Throwable {
        simple.setName("bob");
    }

    @Then("^方法被拦截$")
    public void 方法被拦截() throws Throwable {
        Assert.assertTrue(simple.getName().equals("tom"));
    }
}
