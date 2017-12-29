package com.huangshihe.ecommerce.ecommercespark.task.tasks;

import com.huangshihe.ecommerce.ecommercespark.commonconfig.entity.ECConfiguration;
import com.huangshihe.ecommerce.ecommercespark.commonconfig.manager.ECConfigurationManager;
import com.huangshihe.ecommerce.ecommercespark.task.constants.SparkConstants;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Task实例
 * <p>
 * Create Date: 2017-12-27 23:25
 *
 * @author huangshihe
 */
public class DemoTask {

    /**
     * 日志.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoTask.class);


    public static void main(String[] args) {
        // 先统一写到main方法里，实现功能再优化代码
        ECConfiguration demoConf = ECConfigurationManager.getConfiguration(SparkConstants.DEMO_CONF_FILENAME);
        final String master = demoConf.getProperty(SparkConstants.CONF_SPARK_MASTER);
        LOGGER.info("master:{}", master);
        SparkConf conf = new SparkConf().setAppName(SparkConstants.DEMO_APP_NAME).setMaster(master);
        JavaSparkContext sc = new JavaSparkContext(conf);
        SQLContext sqlContext = getSqlContext(sc.sc());

        createTestData(sc, sqlContext);

        // 关闭上下文
        sc.close();

    }

    public static SQLContext getSqlContext(SparkContext sparkContext) {
        ECConfiguration demoConf = ECConfigurationManager.getConfiguration(SparkConstants.DEMO_CONF_FILENAME);
        if (demoConf.getBoolean(SparkConstants.CONF_SPARK_LOCAL)) {
            // 这里是单机模式，所以使用最简单的SQLContext
            return new SQLContext(sparkContext);
        } else {
            // TODO 如果是生产环境，数据来源为HBase，那么需要对应的上下文。
            return null;
        }
    }

    public static void createTestData(JavaSparkContext sc, SQLContext sqlContext) {
        List<Row> rows = new ArrayList<Row>();
        Row row1 = RowFactory.create(12345678L, "alian", 18, "", "alian is not my name.");
        Row row2 = RowFactory.create(2345678L, "alian2", 12, null, "alian2 is not my name.");
        rows.add(row1);
        rows.add(row2);
        JavaRDD<Row> rowsRdd = sc.parallelize(rows);

        StructType schema = DataTypes.createStructType(Arrays.asList(
                DataTypes.createStructField("user_id", DataTypes.LongType, false),
                DataTypes.createStructField("name", DataTypes.StringType, false),
                DataTypes.createStructField("age", DataTypes.IntegerType, false),
                DataTypes.createStructField("username", DataTypes.StringType, true),
                DataTypes.createStructField("info", DataTypes.StringType, true)
        ));

        Dataset<Row> dataset = sqlContext.createDataFrame(rowsRdd, schema);

        dataset.registerTempTable("demo_table");
        dataset.show(1);
    }

}
