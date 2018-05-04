package com.huangshihe.ecommerce.llt.tmp;

import org.apache.hadoop.hbase.util.Bytes;

import java.util.Arrays;

/**
 * 数组测试例子.
 * <p>
 * Create Date: 2018-04-11 22:21
 *
 * @author huangshihe
 */
public class ArrayMain {
    public static void main(String[] args) {
        String str = "F0";
        byte[] result = Bytes.toBytes(str); // [70,48]
        System.out.println(Arrays.toString(result));

        System.out.println((byte)'F');//70

        byte b = 1;
        System.out.println((char)b);//无输出

        result = new byte[]{1};
        System.out.println(result.length);//1
    }
}
