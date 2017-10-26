package com.quduquxie.base.retrofit.bean;

import java.io.Serializable;

/**
 * Created on 17/7/12.
 * Created by crazylei.
 */

public class CommunalResult<T> implements Serializable {
    private int code;
    private String message;
    private T model;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }
}