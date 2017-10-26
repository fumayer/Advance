package com.quduquxie.base;

import com.orhanobut.logger.Logger;
import com.quduquxie.base.config.BaseConfig;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created on 17/7/12.
 * Created by crazylei.
 */

public abstract class RxPresenter implements BasePresenter {

    private CompositeDisposable compositeDisposable;

    private void removeDisposables() {
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }

    protected void insertDisposable(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    @Override
    public void attachView() {
        Logger.e("AttachView");
    }

    @Override
    public void detachView() {

        Logger.e("DetachView");

        removeDisposables();
    }
}