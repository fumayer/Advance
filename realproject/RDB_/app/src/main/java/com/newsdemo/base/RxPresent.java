package com.newsdemo.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by jianqiang.hu on 2017/5/11.
 */

public class RxPresent<T extends BaseView> implements BasePresenter<T> {
    protected T mView;
    protected CompositeDisposable mCompositeDisposable;
    protected void unSubscribe(){
        if (mCompositeDisposable!=null){
            mCompositeDisposable.dispose();
        }
    }


    protected void addSubscribe(Disposable subscription){
        if (mCompositeDisposable==null){
            mCompositeDisposable=new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);
    }

  /*  protected <T> void addRxBusSubscribe(Class<T> eventType,Consumer<T> act){
        if (mCompositeDisposable==null)
            mCompositeDisposable=new CompositeDisposable();
        mCompositeDisposable.add(RxBus.)
    }*/


    @Override
    public void attachView(T view) {
        this.mView=view;
    }

    @Override
    public void detachView() {
        this.mCompositeDisposable=null;
        unSubscribe();
    }
}
