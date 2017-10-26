package com.quduquxie.retrofit.model;

import java.io.Serializable;

/**
 * Created on 16/12/27.
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
