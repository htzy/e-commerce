package com.huangshihe.ecommerce.llt.tmp;

import org.apache.hadoop.hbase.util.Bytes;

/**
 * digit.
 * <p>
 * Create Date: 2018-05-05 19:46
 *
 * @author huangshihe
 */
public class DigitMain {
    public static void main(String[] args) {
        byte[] b = Bytes.toBytes("0");//{48}
        String s = Bytes.toHex(b);
        System.out.println(s);      //30
        b = Bytes.fromHex("01");//{1}

    }
}
