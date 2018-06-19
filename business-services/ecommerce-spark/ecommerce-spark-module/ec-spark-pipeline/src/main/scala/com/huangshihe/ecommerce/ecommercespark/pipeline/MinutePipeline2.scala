package com.huangshihe.ecommerce.ecommercespark.pipeline


import com.huangshihe.ecommerce.common.kits.{DigitKit, StringKit, TimeKit}
import com.huangshihe.ecommerce.ecommercespark.taskmanager.manager.SparkTaskManager
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.api.java.{JavaPairRDD, JavaSparkContext}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SQLContext

import scala.collection.mutable.ArrayBuffer

object MinutePipeline2 {

    private val sc: SQLContext = SparkTaskManager.getInstance().getSqlContext("minute")

    private val jsc: JavaSparkContext = new JavaSparkContext(sc.sparkContext)

    private var input: JavaPairRDD[ImmutableBytesWritable, Result] = _

    def load(input: JavaPairRDD[ImmutableBytesWritable, Result]): this.type = {
        this.input = input
        this
    }

    def pipeline(): Unit = {
        println(input.count())  // 1
    }

    // 构造模拟数据JavaPairRDD[ImmutableBytesWritable, Result]
    def simulateData(): JavaPairRDD[ImmutableBytesWritable, Result] = {
        // RowKey组成部分：tenantID、groupID、MAC、time、APMAC
        // tenantID：hashCode，6字节
        val tenantID: Array[Byte] = DigitKit.adjustLen(Bytes.toBytes("tenantID"), 6);
        // groupID：hashCode，6字节
        val groupID: Array[Byte] = DigitKit.adjustLen(Bytes.toBytes("groupID"), 6)
        // MAC：mac，6字节
        val mac: Array[Byte] = Bytes.toBytes(StringKit.fillChar(6, 'F'))
        // time：Time，8字节
        val time: Array[Byte] = Bytes.toBytes(TimeKit.getCurrentTime)
        // APMAC：mac，6字节
        val apmac = mac
        // 组装RowKey
        val rowKey: ArrayBuffer[Byte] = new ArrayBuffer[Byte](0)
        rowKey ++= tenantID ++= groupID ++= mac ++= time ++= apmac

//        println("rowKey:" + rowKey)
// ArrayBuffer(116, 101, 110, 97, 110, 116, 103, 114, 111, 117, 112, 73, 70, 70, 70, 70, 70, 70, 0, 0, 1, 100, 23, 79, -114, -86, 70, 70, 70, 70, 70, 70)
        val writable = new ImmutableBytesWritable(rowKey.toArray)
        val result: Result = new Result()

        val tuple2: (ImmutableBytesWritable, Result) = Tuple2(writable, result)

        val rdd: RDD[(ImmutableBytesWritable, Result)] = jsc.parallelize(Seq(tuple2))

        new JavaPairRDD[ImmutableBytesWritable, Result](rdd)
    }


    def main(args: Array[String]): Unit = {
        //        JavaPairRDD.fromJavaRDD(new JavaPairRDD[ImmutableBytesWritable, Result]())
        // 调用方式
        MinutePipeline2.load(simulateData()).pipeline()
    }
}


