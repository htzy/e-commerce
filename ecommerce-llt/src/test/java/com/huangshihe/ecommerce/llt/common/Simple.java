package com.huangshihe.ecommerce.llt.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用于测试的简单类
 * <p>
 * Create Date: 2017-12-12 20:52
 *
 * @author huangshihe
 */
public class Simple {
    private static final Logger LOGGER = LoggerFactory.getLogger(Simple.class);

    private int id;
    private String name;

    public int getId() {
        LOGGER.debug("getId() id:{}", id);
        return id;
    }

    public void setId(int id) {
        LOGGER.debug("setId() id:{}", id);
        this.id = id;
    }

    public String getName() {
        LOGGER.debug("getName() name:{}", name);
        return name;
    }

    public void setName(String name) {
        LOGGER.debug("setName() name:{}", name);
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        LOGGER.debug("equals(), args:{}", o);
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Simple simple = (Simple) o;

        return id == simple.id && name.equals(simple.name);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        LOGGER.debug("hashCode(), hasCode:{}", result);
        return result;
    }

    @Override
    public String toString() {
        LOGGER.debug("toString(), id:{}, name:{}", id, name);
        return "Simple{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
