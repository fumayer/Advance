package com.quduquxie.base;

/**
 * Created on 17/7/12.
 * Created by crazylei.
 */

public interface BasePresenter {

    void attachView();

    void detachView();

    void recycle();
}