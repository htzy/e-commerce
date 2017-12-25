package com.huangshihe.ecommerce.ecommercehbase.llt.hbasedao;

import com.huangshihe.ecommerce.ecommercehbase.dao.HBaseDaoImpl;
import com.huangshihe.ecommerce.ecommercehbase.dao.IHBaseDao;
import com.huangshihe.ecommerce.ecommercehbase.util.HBaseDaoUtil;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Result;
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
    private static String[] qualifiers;
    private static String insertRowKey;
    private static String[] insertRowKeys;
    private static List<Result> results;
    private static String startRowKey;
    private static String stopRowKey;
    private static int pageSize;


    @Before
    public void 准备测试环境() throws Throwable {
        // 创建hbase连接
        if (hBaseDao == null) {
            hBaseDao = new HBaseDaoImpl();
        }
    }

    @And("^要创建表的familyNames是\"([^\"]*)\"$")
    public void 要创建表的familynames是(String arg0) throws Throwable {
        familyNames = arg0.split(",");
    }

    @And("^要创建表的columnFamily老化时间都是\"([^\"]*)\"$")
    public void 要创建表的columnfamily老化时间都是(String arg0) throws Throwable {
        ttl = Integer.valueOf(arg0);
    }

    @Given("^表名为\"([^\"]*)\"$")
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
        tableNameStr = null;
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

    @And("^要插入数据的qualifiers为\"([^\"]*)\"$")
    public void 要插入数据的qualifiers为(String arg0) throws Throwable {
        qualifiers = arg0.split(",");
    }

    @When("^在表中随机生成\"([^\"]*)\"条rowKey和随机值插入$")
    public void 在表中随机生成条rowKey和随机值插入(String arg0) throws Throwable {
        // 这里将列插到所有的family中，仅仅为测试
        int count = Integer.valueOf(arg0);
        for (String familyName : familyNames) {
            for (int i = 0; i < count; i++) {
                String rowKey = UUID.randomUUID().toString();
                Map<String, String> qualifierValues = new HashMap<>(qualifiers.length);
                for (String qualifier : qualifiers) {
                    qualifierValues.put(qualifier, UUID.randomUUID().toString());
                }
                hBaseDao.insert(tableNameStr, rowKey, familyName, qualifierValues);
            }
        }
    }

    @Then("^查询表中共有\"([^\"]*)\"条rowKey$")
    public void 查询表中共有条rowKey(String arg0) throws Throwable {
        Assert.assertTrue(Integer.valueOf(arg0) == hBaseDao.queryAll(tableNameStr).size());
    }

    @And("^要创建数据的rowKey为\"([^\"]*)\"$")
    public void 要创建数据的rowkey为(String arg0) throws Throwable {
        insertRowKey = arg0;
    }

    @When("^在表中插入随机值$")
    public void 在表中插入随机值() throws Throwable {
        for (String familyName : familyNames) {
            Map<String, String> qualifierValues = new HashMap<>(qualifiers.length);
            for (String qualifier : qualifiers) {
                qualifierValues.put(qualifier, UUID.randomUUID().toString());
            }
            hBaseDao.insert(tableNameStr, insertRowKey, familyName, qualifierValues);
        }
    }

    @Then("^查询rowKey为\"([^\"]*)\"$")
    public void 查询rowkey为(String queryRowKey) throws Throwable {
        List<Cell> cells = hBaseDao.queryTableByRowKey(tableNameStr, queryRowKey);
        Assert.assertNotNull(cells);
    }

    @And("^要创建数据的rowKeys为\"([^\"]*)\"$")
    public void 要创建数据的rowkeys为(String arg0) throws Throwable {
        insertRowKeys = arg0.split(",");
    }

    @And("^在表中插入多个rowKeys随机值$")
    public void 在表中插入多个rowkeys随机值() throws Throwable {
        for (String familyName : familyNames) {
            Map<String, String> qualifierValues = new HashMap<>(qualifiers.length);
            for (String qualifier : qualifiers) {
                qualifierValues.put(qualifier, UUID.randomUUID().toString());
            }
            // 对于不同的rowKey插入相同的qualifierValues，仅测试。
            for (String rowKey : insertRowKeys) {
                hBaseDao.insert(tableNameStr, rowKey, familyName, qualifierValues);
            }
        }
    }

    @When("^根据表名查询所有数据$")
    public void 根据表名查询所有数据() throws Throwable {
        results = hBaseDao.queryAll(tableNameStr);
    }

    @Then("^查询到所有的数据$")
    public void 查询到所有的数据() throws Throwable {
        HBaseDaoUtil.printResultsInfo(results);
        Assert.assertTrue(results.size() == insertRowKeys.length * familyNames.length);
    }

    @And("^要查询的startRowKey为\"([^\"]*)\"$")
    public void 要查询的startrowkey为(String arg0) throws Throwable {
        startRowKey = arg0;
    }

    @And("^要查询的stopRowKey为\"([^\"]*)\"$")
    public void 要查询的stoprowkey为(String arg0) throws Throwable {
        stopRowKey = arg0;
    }

    @And("^要查询的pageSize为\"([^\"]*)\"$")
    public void 要查询的pagesize为(String arg0) throws Throwable {
        pageSize = Integer.valueOf(arg0);
    }

    @When("^分页查询数据$")
    public void 分页查询数据() throws Throwable {
        results = hBaseDao.query(tableNameStr, startRowKey, stopRowKey, pageSize);
    }

    @Then("^查询到\"([^\"]*)\"条数据$")
    public void 查询到条数据(String arg0) throws Throwable {
        Assert.assertTrue(results.size() == Integer.valueOf(arg0));
    }

    @After
    public void 清理测试环境() {
        // 删除表
        if (tableNameStr != null && !tableNameStr.isEmpty()) {
            hBaseDao.deleteTable(tableNameStr);
        }
    }
}
