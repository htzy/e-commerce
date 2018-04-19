package com.huangshihe.ecommerce.hbasesimulation;

import com.huangshihe.ecommerce.ecommercehbase.hbaseservice.constants.CommonConstant;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HFile生成.
 * <p>
 * Create Date: 2018-04-12 21:00
 *
 * @author huangshihe
 */
public class HFileCreate {

    private static final Logger LOGGER = LoggerFactory.getLogger(HFileCreate.class);

    private static Simulation _simulation;

    private static List<String> _qualifiers;

    public static void buildSimulation(Simulation simulation) {
        _simulation = simulation;
        _qualifiers = simulation.getQualifierIndexs();
        LOGGER.debug("qualifiers:{}", _qualifiers);
    }

    private HFileCreate() {

    }


    public static class HFileImportMapper2 extends
            Mapper<LongWritable, Text, ImmutableBytesWritable, Put> {

        private static final String regEx = "\"?(\\[([0-9 ,-]*)\\])\"?";
        private final Pattern pattern = Pattern.compile(regEx);


        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            LOGGER.debug("line:{}", line);
            // "[0, 0, 0, 0, 0, 0][0, 0, 0, 0, 0, 0][0, 0, 31, 62, 70, 70]
            // [0, 0, 1, 98, -70, -107, -60, 0][0, 0, 0, 1, 70, 70]",
            //"[0, 0, 0, 3, 0, 0][0, 0, 0, 2][0, 0, 0, 4][-1, -1, -1, -48][0, 0, 0, 64][1][0]"
            String[] lines = line.split("\",\"");
            String rowkeyStr = lines[0];
            LOGGER.debug("rowkeyStr:{}", rowkeyStr);
            String qualifierStr = lines[1];
            LOGGER.debug("qualifierStr:{}", qualifierStr);
            Matcher matcher = pattern.matcher(rowkeyStr);
            byte[] rowkeyByte = new byte[0];
            while (matcher.find()) {
                String str = matcher.group(2);
                String[] strs = str.split(",");
                byte[] b = new byte[strs.length];
                for (int i = 0; i < strs.length; i++) {
                    b[i] = Byte.parseByte(strs[i].trim());
                }
                rowkeyByte = Bytes.add(rowkeyByte, b);
            }
            LOGGER.debug("rowkeyByte:{}", Arrays.toString(rowkeyByte));

            final ImmutableBytesWritable rowkey = new ImmutableBytesWritable(rowkeyByte);

            // 拆解qualifier
            matcher = pattern.matcher(qualifierStr);
            Put put = new Put(rowkeyByte);
            // 当前的qualifier坐标
            int qualifierIndex = 0;
            while (matcher.find()) {
                // 获取其中一个qualifier
                String str = matcher.group(2);  // 由逗号分隔的字节数组
                String[] strs = str.split(",");
                byte[] b = new byte[strs.length];
                for (int i = 0; i < strs.length; i++) {
                    // 将str中的字节还原为真实的字节，并放入数组中
                    b[i] = Byte.parseByte(strs[i].trim());
                }
                // 获取qualifier！
                String qualifier;
                if (_qualifiers.size() > qualifierIndex) {
                    qualifier = _qualifiers.get(qualifierIndex++);
                } else {
                    LOGGER.error("qualifiers不足！qualifiers:{}, qualifierIndex", _qualifiers, qualifierIndex);
                    throw new IllegalArgumentException("qualifiers不足！");
                }
                put.addColumn(Bytes.toBytes(CommonConstant.FAMILY_NAME), Bytes.toBytes(qualifier), b);
//                KeyValue keyValue = new KeyValue(rowkeyByte, Bytes.toBytes(CommonConstant.FAMILY_NAME),
//                        Bytes.toBytes(qualifier), b);
//                context.write(rowkey, keyValue);
            }
            context.write(rowkey, put);
        }
    }

}
