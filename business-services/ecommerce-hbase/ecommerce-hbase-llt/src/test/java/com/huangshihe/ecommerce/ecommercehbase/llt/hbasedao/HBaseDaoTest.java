package com.huangshihe.ecommerce.ecommercehbase.llt.hbasedao;

import com.huangshihe.ecommerce.ecommercehbase.dao.HBaseDaoImpl;
import com.huangshihe.ecommerce.ecommercehbase.dao.IHBaseDao;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

/**
 * HBaseDao测试类
 * <p>
 * Create Date: 2017-12-14 21:27
 *
 * @author huangshihe
 */
public class HBaseDaoTest {

    private static IHBaseDao hBaseDao;
    private static String[] familyNames;
    private static int ttl;

    @Given("^创建hbase连接成功$")
    public void 创建hbase连接成功() throws Throwable {
        hBaseDao = new HBaseDaoImpl();
    }

    @And("^要创建表的familyNames是\"([^\"]*)\"$")
    public void 要创建表的familynames是(String arg0) throws Throwable {
        familyNames = arg0.split(",");
    }

    @And("^要创建表的columnFamily老化时间都是\"([^\"]*)\"$")
    public void 要创建表的columnfamily老化时间都是(String arg0) throws Throwable {
        ttl = Integer.valueOf(arg0);
    }

    @When("^创建\"([^\"]*)\"表$")
    public void 创建表(String tableNameStr) throws Throwable {
        hBaseDao.createTable(tableNameStr, familyNames, ttl);
    }

    @Then("^创建\"([^\"]*)\"表成功$")
    public void 创建表成功(String tableNameStr) throws Throwable {
        Assert.assertTrue(hBaseDao.isExists(tableNameStr));
    }
}
