package com.huangshihe.ecommerce.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Json工具类.
 * <p>
 * Create Date: 2017-12-11 20:37
 *
 * @author huangshihe
 */
public final class JsonUtil {

    /**
     * 日志.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

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
     * 私有构造方法.
     */
    private JsonUtil() {

    }

}
