package com.huangshihe.ecommerce.ecommercehbase.llt.hbasedao;

import com.huangshihe.ecommerce.ecommercehbase.dao.HBaseDaoImpl;
import com.huangshihe.ecommerce.ecommercehbase.dao.IHBaseDao;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.hadoop.hbase.Cell;
import org.junit.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    private static String tableNameStr;
    private static List<Cell> cellList;
    private static String[] columns;

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

    @And("^表名为\"([^\"]*)\"$")
    public void 表名为(String tableName) throws Throwable {
        tableNameStr = tableName;
    }

    @When("^创建表$")
    public void 创建表() throws Throwable {
        hBaseDao.createTable(tableNameStr, familyNames, ttl);
    }

    @Then("^创建表成功$")
    public void 创建表成功() throws Throwable {
        Assert.assertTrue(hBaseDao.isExists(tableNameStr));
    }

    @And("^删除表$")
    public void 删除表() throws Throwable {
        hBaseDao.deleteTable(tableNameStr);
    }

    @Then("^删除表成功$")
    public void 删除表成功() throws Throwable {
        Assert.assertFalse(hBaseDao.isExists(tableNameStr));
    }

    @And("^数据表创建成功$")
    public void 数据表创建成功() throws Throwable {
        hBaseDao.createTable(tableNameStr, familyNames, ttl);
    }

    @When("^在表中通过rowKey\"([^\"]*)\"查询$")
    public void 在表中通过rowKey查询(String rowKey) throws Throwable {
        cellList = hBaseDao.queryTableByRowKey(tableNameStr, rowKey);
    }

    @Then("^查询该rowKey共有\"([^\"]*)\"条记录$")
    public void 查询该rowKey共有条记录(String cellsCount) throws Throwable {
        int count = Integer.valueOf(cellsCount);
        if (count == 0) {
            Assert.assertNull(cellList);
        } else {
            Assert.assertEquals(count, cellList.size());
        }
    }

    @When("^删除不存在的表$")
    public void 删除不存在的表() throws Throwable {
        hBaseDao.deleteTable(tableNameStr);
    }

    @And("^要插入数据的columns为\"([^\"]*)\"$")
    public void 要插入数据的columns为(String arg0) throws Throwable {
        columns = arg0.split(",");
    }

    @When("^在表中随机生成\"([^\"]*)\"条rowKey和随机值插入$")
    public void 在表中随机生成条rowKey和随机值插入(String arg0) throws Throwable {
        // 这里将列插到所有的family中，仅仅为测试
        int count = Integer.valueOf(arg0);
        for (String familyName : familyNames) {
            for (int i = 0; i < count; i++) {
                String rowKey = UUID.randomUUID().toString();
                Map<String, String> columnValues = new HashMap<>(columns.length);
                for (String column : columns) {
                    columnValues.put(column, UUID.randomUUID().toString());
                }
                hBaseDao.insert(tableNameStr, rowKey, familyName, columnValues);
            }
        }
    }

    @Then("^查询表中共有\"([^\"]*)\"条rowKey$")
    public void 查询表中共有条rowKey(String arg0) throws Throwable {
        Assert.assertTrue(Integer.valueOf(arg0) == hBaseDao.queryAll(tableNameStr).size());
    }

}
