package com.huangshihe.ecommerce.hbasegui.model;

import javafx.beans.property.StringProperty;

/**
 * simple
 * <p>
 * Create Date: 2018-01-13 19:31
 *
 * @author huangshihe
 */
public class Simple {
    private StringProperty text;

    public String getText() {
        return text.get();
    }

    public StringProperty textProperty() {
        return text;
    }

    public void setText(String text) {
        this.text.set(text);
    }
}
