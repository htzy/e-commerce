package com.huangshihe.ecommerce.common.kits;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 校验工具类.
 * <p>
 * Create Date: 2018-01-02 22:26
 *
 * @author huangshihe
 */
public final class ValidKit {

    /**
     * 日志.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidKit.class);

    /**
     * 匹配data数据中的dataField项，与param数据中的paramField项，field中数据以,分割，
     * 若field中的数据有相同的则返回true.
     *
     * @param jsonData   给定数据的json字符串
     * @param dataField  数据项
     * @param jsonParam  参数数据的json字符串
     * @param paramField 数据项
     * @return 是否存在
     */
    public static boolean in(String jsonData, String dataField, String jsonParam, String paramField) {
        // TODO 测试，如果data是空？
        String dataFieldValueStr = JsonKit.getValueFromJson(jsonData, dataField);
        String paramFieldValueStr = JsonKit.getValueFromJson(jsonParam, paramField);
        // 如果参数中的数据项为空，或者不存在该数据项，则返回false
        if (StringKit.isEmpty(paramFieldValueStr)) {
            LOGGER.warn("[ValidKit] in jsonParam:{}, paramField:{}, tree:{}, paramFieldValueStr:{}",
                    jsonParam, paramField, JsonKit.strToTree(jsonParam), paramFieldValueStr);
            return false;
        }
        if (StringKit.isNotEmpty(dataFieldValueStr)) {
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
     * 判断在data中的dataField项的值，是否在参数的start数据项和stop数据项之间.
     * TODO 待修改，当前不可用
     *
     * @param jsonData        给定数据的json字符串
     * @param dataField       数据项
     * @param jsonParam       参数数据的json字符串
     * @param startParamField 起始数据项
     * @param stopParamField  终止数据项
     * @return 数据项的值是否在起始数据项值和终止数据项值之间
     */
    @Deprecated
    public static boolean between(String jsonData, String dataField, String jsonParam, String startParamField,
                                  String stopParamField) {
        String dataFieldValueStr = JsonKit.strToTree(jsonData).get(dataField).asText();
        LOGGER.debug("[ValidKit] in jsonData:{}, dataField:{}, dataFieldValueStr:{}",
                jsonData, dataField, dataFieldValueStr);
        String startParamFieldStr = JsonKit.strToTree(jsonParam).get(startParamField).asText();
        String stopParamFieldStr = JsonKit.strToTree(jsonParam).get(stopParamField).asText();

        LOGGER.debug("[ValidKit] in jsonParam:{}, startParamField:{}, startParamFieldStr:{}",
                jsonParam, startParamField, startParamFieldStr);
        LOGGER.debug("[ValidKit] in jsonParam:{}, stopParamField:{}, stopParamFieldStr:{}",
                jsonParam, stopParamField, stopParamFieldStr);
        // 如果参数中的数据项为空，则返回true，即任何数据中有含有空
        if (StringKit.isAllEmpty(startParamFieldStr, stopParamFieldStr)) {
            LOGGER.warn("[ValidKit] in jsonParam:{}, startParamField:{}, startParamFieldStr:{}",
                    jsonParam, startParamField, startParamFieldStr);
            LOGGER.warn("[ValidKit] in jsonParam:{}, stopParamField:{}, stopParamFieldStr:{}",
                    jsonParam, stopParamField, stopParamFieldStr);
            return true;
        }
        // 如果data中的dataField项的值为null，则返回不包含
        if (StringKit.isEmpty(dataFieldValueStr)) {
            LOGGER.warn("[ValidKit] in jsonData:{}, dataField:{}, dataFieldValueStr:{}",
                    jsonData, dataField, dataFieldValueStr);
            return false;
        }
        try {
            // 如果start为空，则默认值为负无穷；若stop为空，则默认值为正无穷
            int startValue = StringKit.toInt(startParamFieldStr, Integer.MAX_VALUE);
            int stopValue = StringKit.toInt(stopParamFieldStr, Integer.MIN_VALUE);
            int dataValue = StringKit.toInt(dataFieldValueStr);
            return dataValue >= startValue && dataValue <= stopValue;
        } catch (NumberFormatException e) {
            LOGGER.error("[ValidKit] dataFieldValueStr:{} between startParamFieldStr:{} and stopParamFieldStr:{}",
                    dataFieldValueStr, startParamFieldStr, stopParamFieldStr);
        }
        return false;
    }

    /**
     * 私有构造方法.
     */
    private ValidKit() {

    }
}
