package com.huangshihe.ecommerce.common.kits;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

/**
 * xml工具类.
 * <p>
 * Create Date: 2018-03-17 22:37
 *
 * @author huangshihe
 */
@SuppressWarnings("unchecked")
public class XmlKit {

    private static Logger LOGGER = LoggerFactory.getLogger(XmlKit.class);

    /**
     * 将输入转为xml实体.
     *
     * @param cls         bean类
     * @param inputStream 输入
     * @param <T>         待转类类型
     * @return 对象
     */
    public static <T> T toEntity(Class<T> cls, InputStream inputStream) {
        try {
            JAXBContext configContext = JAXBContext.newInstance(cls);
            Unmarshaller unmarshaller = configContext.createUnmarshaller();
            return (T) unmarshaller.unmarshal(inputStream);
        } catch (JAXBException e) {
            LOGGER.error("参数错误？cls:{}, inputStream:{}", cls, inputStream);
            throw new IllegalArgumentException(e);
        }
    }
}
