package com.huangshihe.ecommercellt;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * Cucumber启动器
 * <p>
 * Create Date: 2017-12-01 23:28
 *
 * @author huangshihe
 */
@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty", "html:target/ecommercetest"}, features = "src/test/resources")
//        ,tags = {"@TestngScenario"})
public class ECommerceTest {//NOPMD

}
