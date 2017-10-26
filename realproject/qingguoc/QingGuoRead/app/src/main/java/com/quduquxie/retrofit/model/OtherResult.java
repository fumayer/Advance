package com.quduquxie.retrofit.model;

import java.io.Serializable;

/**
 * Created on 17/3/27.
 * Created by crazylei.
 */

public class OtherResult<T> implements Serializable {

    private boolean success;
    private String uri;
    private T model;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }
}
