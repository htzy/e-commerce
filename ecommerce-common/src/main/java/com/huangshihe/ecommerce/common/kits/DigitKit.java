package com.huangshihe.ecommerce.common.kits;

/**
 * 数字工具类.
 * <p>
 * Create Date: 2018-01-13 22:28
 *
 * @author huangshihe
 */
public class DigitKit {

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
        String str = "";
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
        System.out.println(str);
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

}
