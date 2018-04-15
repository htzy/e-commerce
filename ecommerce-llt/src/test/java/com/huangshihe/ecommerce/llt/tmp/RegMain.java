package com.huangshihe.ecommerce.llt.tmp;

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
    public static void main(String[] args) {

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
    }
}
