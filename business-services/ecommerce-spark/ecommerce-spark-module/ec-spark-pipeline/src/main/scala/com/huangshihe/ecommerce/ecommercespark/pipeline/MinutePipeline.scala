package com.huangshihe.ecommerce.ecommercespark.pipeline

import com.huangshihe.ecommerce.common.kits.TimeKit
import com.huangshihe.ecommerce.ecommercehbase.hbaseservice.constants.{CommonConstant, OriginalConstant}
import com.huangshihe.ecommerce.ecommercespark.taskmanager.manager.SparkTaskManager
import org.apache.hadoop.hbase.client.{Put, Result}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.api.java.JavaPairRDD
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Row, SQLContext}

object MinutePipeline {

    val sc: SQLContext = SparkTaskManager.getInstance().getSqlContext("minute")

    import sc.sparkSession.implicits._

    // 定义schema
    val schema = StructType(Array(StructField("tgm", BinaryType, true),
        StructField("minute", IntegerType, true),
        StructField("time", LongType, true),
        StructField("apmac", BinaryType, true),
        StructField("rssi", IntegerType, true)
    ))

    // 传入JavaPairRDD，然后转换为DataFrame，进行数据处理后，返回需要存储的数据结构
    def pipeline(input: JavaPairRDD[ImmutableBytesWritable, Result]) = {
        // 转为DataFrame
        val inputDF: DataFrame = sc.createDataFrame(toRddRow(input), schema)
        //        inputDF.show(10)
        //        println(inputDF.count())//107

        // 将tgm+minute作为key进行聚合，对应有多个time,apmac和rssi =>key:(tgm,minute);value:(time,apmac,rssi)
//        val aggrDF: DataFrame = aggr(inputDF)
//
//        toPutData(aggrDF)
        aggr(inputDF)

        //        aggrDF.show(10)
        //        println(aggrDF.count()) //107


        /*
        2018-05-06 17:46:32,064 | [INFO] | main | org.apache.spark.internal.Logging$class.logInfo(Logging.scala:54) | Job 0 finished: show at MinutePipeline.scala:37, took 7.137145 s
+--------------------+------+-------------+-------------------+-----+
|                 tgm|minute|         time|              apmac| rssi|
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

        /*
+--------------------+--------------------+
|                  _1|                  _2|
+--------------------+--------------------+
|[  !    ?    ...|[1524412885000,  ...|
|[      F    I...|[1524412910000,  ...|
|[      >     �...|[1524412885000,  ...|
|[      F     h...|[1524412925000,  ...|
...|[1524412865000,  ...|
|[  '    A     ...|[1524412955000,  ...|
|[  #    E    6...|[1524412860000,  ...|
|[  '    A    P...|[1524412945000,  ...|
|[  &    C     �...|[1524412850000,  ...|
|[  '    D     �...|[1524412890000,  ...|
+--------------------+--------------------+
         */
    }


    /**
      * 聚合函数.
      *
      * @param input 输入
      * @return 聚合后
      */
    def aggr(input: DataFrame): JavaPairRDD[ImmutableBytesWritable, Put] = {
        // 将tgm+minute作为key进行聚合，对应有多个time,apmac和rssi =>key:(tgm,minute);value:(time,apmac,rssi)
        val data = input.map(row => {
            ((row.getAs[Array[Byte]]("tgm"), row.getAs[Int]("minute")),
                (row.getAs[Long]("time"), row.getAs[Array[Byte]]("apmac"), row.getAs[Int]("rssi")))
        }).rdd.reduceByKey((item1, item2) => {
            // 这里取rssi负值更大的，也就是更小的数
            if (item1._3 >= item2._3)
                item2
            else
                item1
        }).map(item => {
            // 聚合后，将结果显示成：tgm, minute, time(小时),apmac, rssi_x即可(其中x为minute+1，1~60范围)
            //            val hour: Long = TimeKit.getHours(item._2._1)
            //            (item._1._1, item._1._2, hour, item._2._2, item._2._3)
            val tgm: Array[Byte] = item._1._1
            val hour: Long = TimeKit.getHours(item._2._1)
            val tgmm: Array[Byte] = Bytes.add(tgm, Bytes.toBytes(hour))
            val rowkey: Array[Byte] = Bytes.add(tgmm, item._2._2)

            val writable = new ImmutableBytesWritable(rowkey)

            val put = new Put(rowkey)

            val qualifier: String = "" + (item._1._2 + 1)
            put.addColumn(Bytes.toBytes(CommonConstant.FAMILY_NAME),
                Bytes.toBytes(qualifier), Bytes.toBytes(item._2._3))
            (writable, put)
        })
        new JavaPairRDD[ImmutableBytesWritable, Put](data)

        //        data.printSchema()

        //        |-- tgm: binary (nullable = true)
        //        |-- minute: integer (nullable = false)
        //        |-- time: long (nullable = false)
        //        |-- apmac: binary (nullable = true)
        //        |-- rssi: integer (nullable = false)

    }


    def toPutData(input: DataFrame): JavaPairRDD[ImmutableBytesWritable, Put] = {

        val data = input.map(row => {
            val tgm: Array[Byte] = row.getAs[Array[Byte]]("tgm")
            val tgmm = Bytes.add(tgm, Bytes.toBytes(row.getAs[Long]("time")))
            val rowkey = Bytes.add(tgmm, row.getAs[Array[Byte]]("apmac"))
            //            val rowkey = row.getAs[Array[Byte]]("apmac")

            val writable = new ImmutableBytesWritable(rowkey)

            val put = new Put(rowkey)

            val qualifier: String = "" + (row.getAs[Int]("minute") + 1)
            put.addColumn(Bytes.toBytes(CommonConstant.FAMILY_NAME),
                Bytes.toBytes(qualifier), Bytes.toBytes(row.getAs[Int]("rssi")))

            (writable, put)
        }).rdd
        new JavaPairRDD[ImmutableBytesWritable, Put](data)
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

            // 同一个租户同一个站点同一个终端在同一分钟（key）内连接的rssi数据
            // TODO 低优先级（不影响核心业务） 可是其他数据怎么办？一项一项列出来吗？
            val rssi: Array[Byte] = item._2.getValue(Bytes.toBytes(CommonConstant.FAMILY_NAME), Bytes.toBytes(OriginalConstant.RSSI))

            val rssiInt: Int = Bytes.toInt(rssi)

            Row(tgm, minute, time, apmac, rssiInt)

        })
    }


}
