package com.huangshihe.ecommerce.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 校验工具类.
 * <p>
 * Create Date: 2018-01-02 22:26
 *
 * @author huangshihe
 */
public final class ValidUtil {

    /**
     * 日志.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidUtil.class);

    /**
     * 匹配data数据中的dataField项，与param数据中的paramField项，field中数据以,分割，
     * 若field中的数据有相同的则返回true.
     *
     * @param data       给定数据的json字符串
     * @param dataField  数据项
     * @param param      参数数据的json字符串
     * @param paramField 数据项
     * @return 是否存在
     */
    public static boolean in(String data, String dataField, String param, String paramField) {
        String dataFieldValueStr = JsonUtil.strToTree(data).get(dataField).textValue();
        LOGGER.debug("[ValidUtil] in data:{}, dataField:{}, dataFieldValueStr:{}", data, dataField, dataFieldValueStr);
        String paramFieldValueStr = JsonUtil.strToTree(param).get(paramField).textValue();
        LOGGER.debug("[ValidUtil] in data:{}, paramField:{}, paramFieldValueStr:{}", param, paramField, paramFieldValueStr);
        // 如果参数中的数据项为空，则返回true，即任何数据中有含有空
        if (paramFieldValueStr == null || paramFieldValueStr.isEmpty()) {
            return true;
        }
        if (dataFieldValueStr != null && !dataFieldValueStr.isEmpty()) {
            String[] dataFieldValues = dataFieldValueStr.split(",");
            String[] paramFieldValues = paramFieldValueStr.split(",");
            for (String dataFieldValue : dataFieldValues) {
                for (String paramFieldValue : paramFieldValues) {
                    if (dataFieldValue.equals(paramFieldValue)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 私有构造方法.
     */
    private ValidUtil() {

    }
}
