package com.huangshihe.ecommerce.llt.common;

import com.huangshihe.ecommerce.common.kits.ClassKit;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * <p>
 * Create Date: 2018-06-12 21:01
 *
 * @author huangshihe
 */
public class ClassKitTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassKitTest.class);

    private static Object obj;

    @Given("^简单类已存在且构造方法存在多个参数$")
    public void 简单类已存在且构造方法存在多个参数() {
        Assert.assertTrue(Arrays.stream(SimpleConstructorA.class.getDeclaredConstructors())
                .filter(constructor -> constructor.getParameterCount() > 1).count() >= 1);
    }

    @When("^通过多参私有构造方法创建类实例")
    public void 通过多参私有构造方法创建类实例() {
        obj = ClassKit.newInstance(SimpleConstructorA.class, 123, new Simple());
    }

    @Then("^创建类实例成功$")
    public void 创建类实例成功() {
        LOGGER.debug("obj:{}", obj);
        Assert.assertNotNull(obj);
    }

    @Given("^简单类已存在且构造方法存在无参数$")
    public void 简单类已存在且构造方法存在无参数() throws NoSuchMethodException {
        Assert.assertNotNull(SimpleConstructorA.class.getDeclaredConstructor());
    }

    @When("^通过无参私有构造方法创建类实例$")
    public void 通过无参私有构造方法创建类实例() {
        obj = ClassKit.newInstance(SimpleConstructorA.class);
    }

    @When("^通过多参（含空值）私有构造方法创建类实例$")
    public void 通过多参含空值私有构造方法创建类实例() {
        obj = ClassKit.newInstance(SimpleConstructorA.class, 123, Simple.class);
    }

    @When("^通过多参（含隐式转换）私有构造方法创建类实例$")
    public void 通过多参含隐式转换私有构造方法创建类实例() {
        short param = 128;
        obj = ClassKit.newInstance(SimpleConstructorA.class, param, Simple.class);
    }

    @Then("^创建类实例失败$")
    public void 创建类实例失败() {
        Assert.assertNull(obj);
    }

    @When("^通过多参（含基本类型为空）私有构造方法创建类实例$")
    public void 通过多参含基本类型为空私有构造方法创建类实例() {
        obj = ClassKit.newInstance(SimpleConstructorA.class, int.class, Simple.class);
    }
}
