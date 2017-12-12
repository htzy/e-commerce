package com.huangshihe.ecommerce.llt.common;

/**
 * 用于测试的简单类
 * <p>
 * Create Date: 2017-12-12 20:52
 *
 * @author huangshihe
 */
public class Simple {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Simple simple = (Simple) o;

        return id == simple.id && name.equals(simple.name);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        return result;
    }
}
