package com.quduquxie.base.retrofit.bean;

import java.io.Serializable;

/**
 * Created on 17/7/28.
 * Created by crazylei.
 */

public class BlanketResult<T> implements Serializable {

    private boolean success;
    private T model;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }
}
