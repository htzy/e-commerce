package com.huangshihe.ecommerce.llt.common;

import com.huangshihe.ecommerce.common.kits.ClassKit;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import java.util.Arrays;

/**
 * <p>
 * Create Date: 2018-06-12 21:01
 *
 * @author huangshihe
 */
public class ClassKitTest {

    static Object obj;

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
        Assert.assertNotNull(obj);
    }

    @Given("^简单类已存在且构造方法存在无参数$")
    public void 简单类已存在且构造方法存在无参数() throws NoSuchMethodException {
        Assert.assertNotNull(SimpleConstructorA.class.getDeclaredConstructor());
    }

    @When("^通过无参私有构造方法创建类实例$")
    public void 通过无参私有构造方法创建类实例() {
        obj = ClassKit.newInstance(SimpleConstructorA.class, String.class);
    }


    class A {
        private int id;

        private A() {
            id = (int) (Math.random() * 1000);
        }

        public int getId() {
            return id;
        }
    }

    class B {
        private A a;
        private String name;

        private B(A a, String name) {
            this.a = a;
            this.name = name;
        }

        public A getA() {
            return a;
        }

        public String getName() {
            return name;
        }
    }

}
