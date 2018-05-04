package com.huangshihe.ecommerce.common.kits;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

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
     * 将16进制的字符串转为long，
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
        StringBuilder str = new StringBuilder();
        try {
            for (int i = 0; i < in.length(); ++i) {
                char ch = in.charAt(i);
                if (ch == '\\' && in.length() > i + 1 && in.charAt(i + 1) == 'x') {
                    char hd1 = in.charAt(i + 2);
                    char hd2 = in.charAt(i + 3);
                    if (!isHexDigit(hd1) || !isHexDigit(hd2)) {
                        continue;
                    }
                    str.append(hd1);
                    str.append(hd2);
                    i += 3;
                } else {
                    // 先转成ASCII码（十进制），再转成16进制
                    String x = Integer.toHexString((int) ch);
                    str.append(x);
                }
            }
        } catch (StringIndexOutOfBoundsException e) {
            LOGGER.error("number:{} may be wrong pattern! detail:{}", in, e);
            throw new IllegalArgumentException(e);
        }

        try {
            return Long.parseLong(str.toString(), 16);
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
     * 检查是否为16进制的utf-8字符.
     *
     * @param c 字符
     * @return 是否为16进制的utf-8字符
     */
    public static boolean isUHexDigit(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9');
    }

    /**
     * 检查是否是10进制的数字.
     *
     * @param c 字符
     * @return 是否是10进制的数字
     */
    public static boolean isTenDigit(char c) {
        return (c >= '0' && c <= '9');
    }

    /**
     * 检查是否是10进制的数字.
     *
     * @param num 数字
     * @return 是否是10进制的数字
     */
    public static boolean isTenNum(String num) {
        if (StringKit.isNotEmpty(num)) {
            for (char c : num.toCharArray()) {
                if (!isTenDigit(c)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 将16进制的字符串解成utf-8格式.
     *
     * @param in in只包含16进制，且不能包含\x，如：E794B7 （男）
     * @return utf-8字符串
     */
    public static String decodeFromHex(String in) {
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
     * @param in 包含汉字或其他转为16进制的的16进制字符串，前后可能存在空格，而空格也是ASCII码，不能删除
     * @return 可读的字符串
     */
    public static String fromUHexStr(String in) {
        if (StringKit.isEmpty(in)) {
            LOGGER.error("in is empty");
            throw new IllegalArgumentException("in is empty");
        }
        StringBuilder result = new StringBuilder();

        // 中文词或其他转为16进制的词
        String words = "";

        for (int i = 0; i < in.length(); i++) {
            char ch = in.charAt(i);
            if (ch == '\\' && in.length() > i + 1 && in.charAt(i + 1) == 'x') {
                char hd1 = in.charAt(i + 2);
                char hd2 = in.charAt(i + 3);
                if (!isUHexDigit(hd1) || !isUHexDigit(hd2)) {
                    continue;
                }
                words += hd1;
                words += hd2;
                i += 3;
            } else {
                // 直到遇到非\x开头的字符，才开始转词
                if (StringKit.isNotEmpty(words)) {
                    result.append(decodeFromHex(words));
                    // 清空words
                    words = "";
                }
                result.append(ch);
            }
        }
        // 对于最后一个words而言
        if (StringKit.isNotEmpty(words)) {
            result.append(decodeFromHex(words));
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


    /**
     * 删除十六进制的格式，如\x符号.
     *
     * @param in 包含十六进制格式的字符串
     * @return 无十六进制格式的字符串
     */
    @Deprecated
    public static String remove16format(String in) {
        if (StringKit.isEmpty(in)) {
            // 如果为空，可能是null，可能是""，则原样返回。
            return in;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < in.length(); i++) {
            char ch = in.charAt(i);
            if (ch == '\\' && in.length() > i + 1 && in.charAt(i + 1) == 'x') {
                i += 1;
            } else {
                builder.append(ch);
            }
        }
        return builder.toString();
    }

    /**
     * 计算utf-8字符串长度.
     * String u = "好"，要求可以识别出来，因为直接输出的长度为1，而HBase中存储的字节长度是3，格外要注意！
     * TODO // 即使rowkey中不会放中文，但是也应该增加检查，否则崩了，无法定位！——rowkey中只需要检查总长度是否符合标准即可！
     * TODO // 还有部分含有中文的，要求计算出在HBase中存储的字节长度。
     *
     * @param in 可能包含中文或其他转为16进制的任意字符串，16进制格式，如：\xe4\xb8\xad\xe6\x96\x87 —— 中文
     * @return 将in转为utf8格式的长度
     */
    public static int getUtf8LenFromUHex(String in) {
        if (StringKit.isEmpty(in)) {
            return 0;
        }
        return fromUHexStr(in).length();
    }


    /**
     * 计算字节数组长度.
     *
     * @param in 可能包含中文或其他转为16进制的任意字符串，16进制格式，如：\xe4\xb8\xad\xe6\x96\x87 —— 中文
     * @return 转为字节数组的长度
     */
    public static int getBytesLenFromUHex(String in) {
        int count = 0;
        if (StringKit.isEmpty(in)) {
            return count;
        }
        for (int i = 0; i < in.length(); i++) {
            char ch = in.charAt(i);
            if (ch == '\\' && in.length() > i + 1 && in.charAt(i + 1) == 'x') {
                i += 3;
            }
            ++count;
        }
        return count;
    }


    /**
     * 调整字节数组的长度，短了只需要更换一个更长的数组即可；长了就将高位去掉.
     *
     * @param bytes  源字节数组
     * @param newLen 长度
     * @return 调整后字符
     */
    public static byte[] adjustLen(byte[] bytes, int newLen) {
        if (newLen < 0) {
            return null;
        }
        if (newLen == 0) {
            return new byte[0];
        }
        if (bytes == null || bytes.length == 0) {
            return new byte[newLen];
        }
        return Arrays.copyOf(bytes, newLen);
    }

    /**
     * 调整字节数组的长度，短了只需要更换一个更长的数组即可，并填充fillByte；长了就将高位去掉.
     *
     * @param bytes  源字节数组
     * @param newLen 长度
     * @return 调整后字符
     */
    public static byte[] adjustLen(byte[] bytes, int newLen, byte fillByte) {
        if (newLen < 0) {
            return null;
        }
        if (newLen == 0) {
            return new byte[0];
        }
        if (bytes == null || bytes.length == 0) {
            byte[] result = new byte[newLen];
            for (int i = 0; i < newLen; i++) {
                result[i] = fillByte;
            }
            return result;
        }
        if (bytes.length < newLen) {
            byte[] result = Arrays.copyOf(bytes, newLen);
            byte[] fill = new byte[newLen - bytes.length];
            for (int i = 0; i < fill.length; i++) {
                fill[i] = fillByte;
            }
            System.arraycopy(fill, 0, result, bytes.length, fill.length);
            return result;
        }
        return Arrays.copyOf(bytes, newLen);
    }

}
