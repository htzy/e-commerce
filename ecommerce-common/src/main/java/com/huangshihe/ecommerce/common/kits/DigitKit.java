package com.huangshihe.ecommerce.common.kits;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * 数字工具类.
 * <p>
 * Create Date: 2018-01-13 22:28
 *
 * @author huangshihe
 */
public class DigitKit {

    /**
     * 日志.
     */
    private static Logger LOGGER = LoggerFactory.getLogger(DigitKit.class);

    /**
     * 将16进制的字符串转为int，
     * 如：W\x5C5\x80 => 0x575C3580 => 1465660800
     * W -> W的ASCII码（10进制为87）16进制为 0x57
     * \x5C -> 就是16进制不变 0x5C
     * 5 -> 5的ASCII码16进制 为 0x35
     * \x80 -> 就是16进制不变 0x80
     * 所以从左往右重新拼起来就是：0x575C3580 -> 转成十进制为：1465660800
     * 还原过程为：有一个十进制的int值为：1465660800，然后保存为16进制：0x575C3580，
     * 然后还是太长了，所以可以转为ASCII码的再转成ASCII码：W\x5C5\x80
     *
     * @param in 包含16进制的字符串，每一个16进制字符前都有\x标识，其他字符为ASCII码，前后可能存在空格，而空格也是ASCII码，不能删除
     * @return 十进制
     */
    public static long fromHexStr(String in) {
        if (StringKit.isEmpty(in)) {
            LOGGER.error("in is empty");
            throw new IllegalArgumentException("in is empty");
        }
        String str = "";
        try {
            for (int i = 0; i < in.length(); ++i) {
                char ch = in.charAt(i);
                if (ch == '\\' && in.length() > i + 1 && in.charAt(i + 1) == 'x') {
                    char hd1 = in.charAt(i + 2);
                    char hd2 = in.charAt(i + 3);
                    if (!isHexDigit(hd1) || !isHexDigit(hd2)) {
                        continue;
                    }
                    str += hd1;
                    str += hd2;
                    i += 3;
                } else {
                    // 先转成ASCII码（十进制），再转成16进制
                    String x = Integer.toHexString((int) ch);
                    str += x;
                }
            }
        } catch (StringIndexOutOfBoundsException e) {
            LOGGER.error("number:{} may be wrong pattern! detail:{}", in, e);
            throw new IllegalArgumentException(e);
        }

        try {
            return Long.parseLong(str, 16);
        } catch (NumberFormatException e) {
            LOGGER.error("number:{} may be too long! detail:{}", in, e);
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 检查是否为16进制的字符.
     *
     * @param c 字符
     * @return 是否为16进制的字符
     */
    public static boolean isHexDigit(char c) {
        return (c >= 'A' && c <= 'F') || (c >= '0' && c <= '9');
    }

    /**
     * 检查是否为16进制的中文字符.
     *
     * @param c 字符
     * @return 是否为16进制的中文字符
     */
    public static boolean isUHexDigit(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9');
    }

    /**
     * 将16进制的字符串解成utf-8格式.
     *
     * @param in in中不包含\x
     * @return utf-8字符串
     */
    public static String decodeHex(String in) {
        if (StringKit.isEmpty(in)) {
            LOGGER.error("in is empty");
            throw new IllegalArgumentException("in is empty");
        }
        try {
            String source = in.trim();
            return new String(Hex.decodeHex(source.toCharArray()), "UTF-8");
        } catch (UnsupportedEncodingException | DecoderException e) {
            LOGGER.error("in:{}, error:{}", in, e);
            throw new IllegalArgumentException("from Hex Str U error, {}", e);
        }
    }

    /**
     * 将含有汉字的16进制字符串转为可读的字符串.
     *
     * @param in 包含汉字的16进制的字符串，前后可能存在空格，而空格也是ASCII码，不能删除
     * @return 可读的字符串
     */
    public static String fromUHexStr(String in) {
        if (StringKit.isEmpty(in)) {
            LOGGER.error("in is empty");
            throw new IllegalArgumentException("in is empty");
        }
        StringBuilder result = new StringBuilder();

        String str = "";

        for (int i = 0; i < in.length(); i++) {
            char ch = in.charAt(i);
            if (ch == '\\' && in.length() > i + 1 && in.charAt(i + 1) == 'x') {
                char hd1 = in.charAt(i + 2);
                char hd2 = in.charAt(i + 3);
                if (!isUHexDigit(hd1) || !isUHexDigit(hd2)) {
                    continue;
                }
                str += hd1;
                str += hd2;
                i += 3;
            } else {
                if (StringKit.isNotEmpty(str)) {
                    result.append(decodeHex(str));
                    // 清空str
                    str = "";
                }
                result.append(ch);
            }
        }
        // 对于最后一个str而言
        if (StringKit.isNotEmpty(str)) {
            result.append(decodeHex(str));
        }
        return result.toString();
    }

    /**
     * 将含有16进制的mac地址字符串解码.
     *
     * @param in \x保存不变，其余为ASCII码
     * @return 解码后的mac地址
     */
    public static String fromHexMacStr(String in) {
        if (StringKit.isEmpty(in)) {
            LOGGER.error("in is empty");
            throw new IllegalArgumentException("in is empty");
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < in.length(); i++) {
            char ch = in.charAt(i);
            if (ch == '\\' && in.length() > i + 1 && in.charAt(i + 1) == 'x') {
                char hd1 = in.charAt(i + 2);
                char hd2 = in.charAt(i + 3);
                if (!isUHexDigit(hd1) || !isUHexDigit(hd2)) {
                    continue;
                }
                result.append(hd1);
                result.append(hd2);
                i += 3;
            } else {
                // 先转成ASCII码（十进制），再转成16进制
                String x = Integer.toHexString((int) ch);
                result.append(x);
            }
            // 每隔18位，将':'换成','
            if (result.length() % 17 == 0) {
                result.append(',');
            } else {
                result.append(':');
            }
        }
        // 删除最后一个':'
        if (result.charAt(result.length() - 1) == ':') {
            result.deleteCharAt(result.length() - 1);
        }
        String resStr = result.toString();
        // 转换为全大写
        if (StringKit.isNotEmpty(resStr)) {
            resStr = resStr.toUpperCase();
        }
        return resStr;
    }

    // TODO 增加是否为中文字符的方法，如String u = "好"，要求可以识别出来，因为直接输出的长度为1，而HBase中存储的字节长度是3，格外要注意！
    // 即使rowkey中不会放中文，但是也应该增加检查，否则崩了，无法定位！
    // 还有部分含有中文的，要求计算出在HBase中存储的字节长度。
    // TODO 综上，即要求通过参数：String、int、char等，增加一个计算在HBase中实际存储字节长度的方法
}
