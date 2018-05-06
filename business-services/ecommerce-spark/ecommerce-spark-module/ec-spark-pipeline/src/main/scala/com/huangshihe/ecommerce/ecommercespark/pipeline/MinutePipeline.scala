package com.huangshihe.ecommerce.ecommercespark.pipeline

import com.huangshihe.ecommerce.common.kits.TimeKit
import com.huangshihe.ecommerce.ecommercehbase.hbaseservice.constants.{CommonConstant, OriginalConstant}
import com.huangshihe.ecommerce.ecommercespark.taskmanager.manager.SparkTaskManager
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.api.java.JavaPairRDD
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Row, SQLContext}

object MinutePipeline {

    val sc: SQLContext = SparkTaskManager.getInstance().getSqlContext("minute")

    // 定义schema
    val schema = StructType(Array(StructField("tgm", BinaryType, true),
        StructField("minute", IntegerType, true),
        StructField("time", LongType, true),
        StructField("apmac", BinaryType, true),
        StructField("cells", IntegerType, true)
    ))

    // 传入JavaPairRDD，然后转换为DataFrame，进行数据处理后，返回需要存储的数据结构
    def pipeline(input: JavaPairRDD[ImmutableBytesWritable, Result]) = {
        // 转为DataFrame
        val inputDF: DataFrame = sc.createDataFrame(toRddRow(input), schema)
        inputDF.show(10)

        /*
        2018-05-06 17:46:32,064 | [INFO] | main | org.apache.spark.internal.Logging$class.logInfo(Logging.scala:54) | Job 0 finished: show at MinutePipeline.scala:37, took 7.137145 s
+--------------------+------+-------------+-------------------+-----+
|                 tgm|minute|         time|              apmac|cells|
+--------------------+------+-------------+-------------------+-----+
|[00 00 06 1F 00 0...|     1|1524412885000|[00 00 00 01 46 46]|   -8|
|[00 00 06 1F 00 0...|     0|1524412845000|[00 00 00 02 46 46]|  -99|
|[00 00 06 1F 00 0...|     4|1524413050000|[00 00 00 00 46 46]|  -76|
|[00 00 06 1F 00 0...|     3|1524412980000|[00 00 00 00 46 46]|  -41|
|[00 00 06 1F 00 0...|     1|1524412890000|[00 00 00 02 46 46]|  -27|
|[00 00 06 1F 00 0...|     1|1524412875000|[00 00 00 02 46 46]|  -54|
|[00 00 06 1F 00 0...|     0|1524412825000|[00 00 00 00 46 46]|  -57|
|[00 00 06 1F 00 0...|     0|1524412815000|[00 00 00 00 46 46]|  -93|
|[00 00 06 1F 00 0...|     2|1524412920000|[00 00 00 02 46 46]|  -66|
|[00 00 06 1F 00 0...|     3|1524413035000|[00 00 00 00 46 46]|  -59|
+--------------------+------+-------------+-------------------+-----+
         */
    }


    /**
      * 将数据转为RDD[Row]
      *
      * @param input input
      * @return rdd
      */
    def toRddRow(input: JavaPairRDD[ImmutableBytesWritable, Result]): RDD[Row] = {
        input.rdd.map(item => {
            // 获取rowkey：byte[]
            val rowkey: Array[Byte] = item._1.copyBytes()
            // 拆解出tenantID，groupID，MAC作为一列
            val tgm: Array[Byte] = java.util.Arrays.copyOfRange(rowkey, 0, 6 * 3)
            // 拆解出time作为一列
            val time: Long = Bytes.toLong(rowkey, 6 * 3, 8)
            // 分钟数
            val minute: Int = TimeKit.getMinute(time)
            // 拆解出APMAC作为一列，但是不参与计算
            val apmac: Array[Byte] = java.util.Arrays.copyOfRange(rowkey, 6 * 3 + 8, rowkey.length)

            // 同一个租户同一个站点同一个终端在同一分钟（key）内连接的rssi数据，TODO 可是？其他数据怎么办？一项一项列出来吗？
            val rssi: Array[Byte] = item._2.getValue(Bytes.toBytes(CommonConstant.FAMILY_NAME), Bytes.toBytes(OriginalConstant.RSSI))

            val rssiInt: Int = Bytes.toInt(rssi)

            Row(tgm, minute, time, apmac, rssiInt)

        })
    }


}
