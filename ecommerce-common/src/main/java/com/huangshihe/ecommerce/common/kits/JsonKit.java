package com.huangshihe.ecommerce.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Json工具类.
 * <p>
 * Create Date: 2017-12-11 20:37
 *
 * @author huangshihe
 */
public final class JsonKit {

    /**
     * 日志.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonKit.class);

    /**
     * 对象转字符串.
     *
     * @param object 对象
     * @return json字符串
     */
    public static String objToStr(final Object object) {
        final ObjectMapper mapper = new ObjectMapper(); //转换器
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LOGGER.error("object to string fail, object: {}, error: {}", object, e);
            throw new IllegalArgumentException(e);
        }

    }

    /**
     * 字符串转对象.
     *
     * @param json   字符串
     * @param tClass 类
     * @param <T>    类
     * @return 对象
     */
    public static <T> T strToObj(final String json, final Class<T> tClass) {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, tClass);
        } catch (IOException e) {
            LOGGER.error("String to object fail, json: {}, error: {}", json, e);
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 将字符串转为json对象，也可以将对象转为json对象，然后知道field之后，可直接通过json对象path(field)获取node值.
     *
     * @param json json字符串
     * @return JsonNode
     */
    public static JsonNode strToTree(final String json) {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readTree(json);
        } catch (IOException e) {
            LOGGER.error("String to jsonNode fail, json:{}, error: {}", json, e);
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 将json对象转为字符串
     *
     * @param tree JsonNode
     * @return json字符串
     */
    public static String treeToStr(final JsonNode tree) {
        return tree == null ? null : tree.toString();
    }

    /**
     * 将object转为json对象.
     *
     * @param object 待转对象
     * @return jsonNode
     */
    public static JsonNode objToTree(final Object object) {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.valueToTree(object);
    }

    /**
     * 私有构造方法.
     */
    private JsonKit() {

    }


}
