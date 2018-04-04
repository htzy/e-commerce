package com.huangshihe.ecommerce.common.kits;

/**
 * 字符串工具类.
 * <p>
 * Create Date: 2018-01-07 15:00
 *
 * @author huangshihe
 */
public final class StringKit {

    /**
     * 是否为空.
     *
     * @param param 参数
     * @return 是否为空
     */
    public static boolean isEmpty(String param) {
        return param == null || param.isEmpty();
    }

    /**
     * 是否都为空.
     *
     * @param params 参数
     * @return 是否为空，若params为null，则也返回true
     */
    public static boolean isAllEmpty(String... params) {
        for (String param : params) {
            if (isNotEmpty(param)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否包含空.
     *
     * @param params 参数
     * @return 是否包含空，若params为null，则也返回true
     */
    public static boolean isContainEmpty(String... params) {
        if (params == null) {
            return true;
        }
        for (String param : params) {
            if (isEmpty(param)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否不为空.
     *
     * @param param 参数
     * @return 是否不为空
     */
    public static boolean isNotEmpty(String param) {
        return !isEmpty(param);
    }

    /**
     * 是否都不为空.
     *
     * @param params 参数
     * @return 是否都不为空
     */
    public static boolean isAllNotEmpty(String... params) {
        if (params == null) {
            return true;
        }
        for (String param : params) {
            if (isEmpty(param)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 字符串转数字.
     *
     * @param param        字符串
     * @param defaultValue 默认值
     * @return 数字
     */
    public static int toInt(String param, int defaultValue) {
        return isNotEmpty(param) ? Integer.valueOf(param) : defaultValue;
    }

    /**
     * 字符串转数字.
     *
     * @param param 字符串
     * @return 数字
     */
    public static int toInt(String param) {
        return Integer.valueOf(param);
    }


    /**
     * 空字符串.
     */
    public static final String emptyString = "";

    /**
     * 私有构造方法.
     */
    private StringKit() {

    }
}
