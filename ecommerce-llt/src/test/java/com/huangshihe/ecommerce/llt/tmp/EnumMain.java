package com.huangshihe.ecommerce.llt.tmp;

import java.util.Arrays;

/**
 * 枚举
 * <p>
 * Create Date: 2018-04-12 21:42
 *
 * @author huangshihe
 */
public class EnumMain {

    enum Type{
        NUM("num"), NEG_NUM("-num");

        private String str;

        Type(String str) {
            this.str = str;
        }

        public String getStr() {
            return str;
        }
    }

    public static void main(String[] args) {
//        Type type = Type.valueOf("NUM");
//        Type.NEG_NUM.getStr()

//        System.out.println(type);
    }

}
