package com.huangshihe.ecommerce.llt.common;

/**
 * 简单构造方法类A
 * <p>
 * Create Date: 2018-06-12 21:39
 *
 * @author huangshihe
 */
public final class SimpleConstructorA {
    private int id;
    private Simple simple;

    private SimpleConstructorA() {
        id = (int) (Math.random() * 1000);
        simple = null;
    }

    private SimpleConstructorA(int id) {
        this.id = id;
        simple = null;
    }

    private SimpleConstructorA(int id, Simple simple) {
        this.id = id;
        this.simple = simple;
    }

    public int getId() {
        return id;
    }

    public Simple getSimple() {
        return simple;
    }
}
