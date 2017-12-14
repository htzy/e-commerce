package com.huangshihe.ecommerce.ecommercehbase.llt;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * Cucumber启动器
 * <p>
 * Create Date: 2017-12-14 21:46
 *
 * @author huangshihe
 */
@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty", "html:target/ecommercetest"}, features = "src/test/resources")
//        ,tags = {"@TestngScenario"})
public class ECommerceHBaseTest { //NOPMD

}
