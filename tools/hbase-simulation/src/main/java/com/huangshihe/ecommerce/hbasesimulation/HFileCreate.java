package com.huangshihe.ecommerce.hbasesimulation;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * HFile生成.
 * <p>
 * Create Date: 2018-04-12 21:00
 *
 * @author huangshihe
 */
public class HFileCreate {

    private static final Logger LOGGER = LoggerFactory.getLogger(HFileCreate.class);

    class HFileImportMapper2 extends
            Mapper<LongWritable, Text, ImmutableBytesWritable, KeyValue> {


        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {




            super.map(key, value, context);
        }
    }
}
