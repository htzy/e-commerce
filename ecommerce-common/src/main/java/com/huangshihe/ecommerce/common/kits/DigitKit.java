package com.huangshihe.ecommerce.common.kits;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

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
     * @param in 包含16进制的字符串，每一个16进制字符前都有\x标识，其他字符为ASCII码。
     * @return 十进制
     */
    public static int fromHexStr(String in) {
        if (StringKit.isEmpty(in)) {
            LOGGER.error("in is empty");
            throw new IllegalArgumentException("in is empty");
        }
        String source = in.trim();
        String str = "";
        for (int i = 0; i < source.length(); ++i) {
            char ch = source.charAt(i);
            if (ch == '\\' && source.length() > i + 1 && source.charAt(i + 1) == 'x') {
                char hd1 = source.charAt(i + 2);
                char hd2 = source.charAt(i + 3);
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
        return Integer.parseInt(str, 16);
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
     * @param in 包含汉字的16进制的字符串
     * @return 可读的字符串
     */
    public static String fromUHexStr(String in) {
        if (StringKit.isEmpty(in)) {
            LOGGER.error("in is empty");
            throw new IllegalArgumentException("in is empty");
        }
        String source = in.trim();
        StringBuilder result = new StringBuilder();

        String str = "";

        for (int i = 0; i < source.length(); i++) {
            char ch = source.charAt(i);
            if (ch == '\\' && source.length() > i + 1 && source.charAt(i + 1) == 'x') {
                char hd1 = source.charAt(i + 2);
                char hd2 = source.charAt(i + 3);
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
}
