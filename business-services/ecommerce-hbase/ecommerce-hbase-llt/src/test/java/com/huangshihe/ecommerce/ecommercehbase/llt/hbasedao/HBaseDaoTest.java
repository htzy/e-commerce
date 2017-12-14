package com.huangshihe.ecommerce.ecommercehbase.llt.hbasedao;

import com.huangshihe.ecommerce.ecommercehbase.dao.HBaseDaoImpl;
import com.huangshihe.ecommerce.ecommercehbase.dao.IHBaseDao;
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
    private static String tableName;
    private static String[] familyNames;
    private static int ttl;

    @Given("^创建hbase连接成功$")
    public void 创建hbase连接成功() throws Throwable {
        hBaseDao = new HBaseDaoImpl();
    }

    @When("^创建\"([^\"]*)\"表$")
    public void 创建表(String arg0) throws Throwable {
        tableName = arg0;
    }

    @And("^该表的familyNames是\"([^\"]*)\"$")
    public void 该表的familynames是(String arg0) throws Throwable {
        familyNames = arg0.split(",");
    }

    @And("^该表的columnFamily老化时间都是\"([^\"]*)\"$")
    public void 该表的columnfamily老化时间都是(String arg0) throws Throwable {
        ttl = Integer.valueOf(arg0);
    }

    @Then("^创建hbase表成功$")
    public void 创建hbase表成功() throws Throwable {
        Assert.assertTrue(hBaseDao.createTable(tableName, familyNames, ttl));
    }
}
