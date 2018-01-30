package com.aiwue.base;

public interface Presenter<V> {

    void attachView(V view);

    void detachView();

}
