package com.huangshihe.ecommerce.llt.tmp;

import org.apache.hadoop.hbase.util.Bytes;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式
 * <p>
 * Create Date: 2018-04-15 00:43
 *
 * @author huangshihe
 */
public class RegMain {

    private static void test() {
        String line = "\"[0, 0, -66, 110, 0, 0][0, 0, 6, 69, 0, 0][0, 0, 31, 4, 70, 70][0, 0, 1, 98, -39, 123, -20, 0][0, 0, 0, 1, 70, 70]\",\"[0, 0, 6, 34, 0, 0][0, 0, 0, 0][0, 0, 0, 3][-1, -1, -1, -87][0, 0, 0, 3][1][0]\"";
//        String line = "\"[0, 0, 0, 0, 0, 0][0, 0, 0, 0, 0, 0][0, 0, 31, 62, 70, 70][0, 0, 1, 98, -70, -107, -60, 0][0, 0, 0, 1, 70, 70]\",\"[0, 0, 0, 3, 0, 0][0, 0, 0, 2][0, 0, 0, 4][-1, -1, -1, -48][0, 0, 0, 64][1][0]\"";
        String regEx = "\"?(\\[([0-9 ,-]*)\\])\"?";
        Pattern pattern = Pattern.compile(regEx);
        String[] lines = line.split("\",\"");
        String rowkeyStr = lines[0];
        System.out.println(rowkeyStr);
        String qualifierStr = lines[1];

        byte[] rowkey = new byte[0];


        Matcher matcher = pattern.matcher(rowkeyStr);
        while (matcher.find()) {
            String str = matcher.group(2);
            String[] strs = str.split(",");

            byte[] b = new byte[strs.length];
            for (int i = 0; i < strs.length; i++) {
                b[i] = Byte.parseByte(strs[i].trim());
            }
            rowkey = Bytes.add(rowkey, b);
        }
        System.out.println(Arrays.toString(rowkey));
//        matcher = pattern.matcher(qualifierStr);


//        String str = "";
    }


    public static void main(String[] args) {

        test();

        /*
        String str = "\"[1,2,3,4,5][2,3,4,5,6]\",\"[5,4,3,2,1][6,5,4,3,2]\"";
        // 首先把它分开成两段
        String[] strs = str.split("\",\"");
        System.out.println(strs.length);//2
//        String regEx = "^\\(\\[.*]\\)$";
        String regEx = "\"?(\\[([0-9,]*)\\])\"?";
        Pattern pattern = Pattern.compile(regEx);

        Matcher matcher = pattern.matcher(strs[0]);
        // 查找字符串中是否有匹配正则表达式的字符/字符串
        while (matcher.find()) {
            System.out.println(matcher.group(2));//1,2,3,4,5//2,3,4,5,6
        }

        ////////////

        String tmp = "\"[1,2,3][4,5,6]";
        String regTmp = "\"?(\\[([0-9,]*)\\])\"?";

        Pattern p = Pattern.compile(regTmp);
        Matcher m = p.matcher(tmp);
        while (m.find()) {
//            System.out.println(m.group());
            System.out.println(m.group(2));//1,2,3//4,5,6
        }
        */
    }
}
