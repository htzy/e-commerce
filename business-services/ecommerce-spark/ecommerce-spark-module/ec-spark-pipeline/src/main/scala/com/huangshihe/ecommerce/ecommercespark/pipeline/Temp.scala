package com.huangshihe.ecommerce.ecommercespark.pipeline

import java.util.Collections

import com.huangshihe.ecommerce.common.kits.ClassKit
import com.huangshihe.ecommerce.ecommercespark.taskmanager.manager.SparkTaskManager
import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.sql.SQLContext


/**
  * 利用pipeline将文件中的内容转为df，并删除一列，并返回
  */
object Temp {
    def main(args: Array[String]): Unit = {
        val sc: SQLContext = SparkTaskManager.getInstance().getSqlContext("test")

        val df1 = sc.read.option("header", true).format("csv")
            .load("file:///Users/huangshihe/bigdata/codes/ecommerce/business-services/ecommerce-spark/ecommerce-spark-module/ec-spark-pipeline/src/main/scala/com/huangshihe/ecommerce/ecommercespark/pipeline/test.txt")
        val pipeline = new Pipeline()
        // TODO 区别PipelineModel.getClass和classOf[PipelineModel]不一样！
        val model: PipelineModel = ClassKit.newInstance(classOf[PipelineModel], pipeline.uid, Collections.singletonList(new MyStage()))
        val res = model.transform(df1)
        println("11111")
        res.show()
        //        +----+---+------+---+
        //        |name|age|gender|num|
        //        +----+---+------+---+
        //        |  吴凡| 25|     F|1.0|
        //        |  张宏| 30|     M|1.0|
        //        |  刘婷| 31|     M|1.0|
        //        +----+---+------+---+
        println("1111")
    }

}
