package com.newsdemo.presenter.main;

import com.newsdemo.base.RxPresent;
import com.newsdemo.base.contract.main.WelcomeContract;
import com.newsdemo.model.DataManager;
import com.newsdemo.model.bean.WelcomeBean;
import com.newsdemo.util.RxUtil;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * Created by jianqiang.hu on 2017/5/11.
 */

public class WelcomePresenter extends RxPresent<WelcomeContract.View> implements WelcomeContract.Presenter {

    private static final String RES="1080*1776";
    private static final int Count_DOWN_TIME=2200;

    private DataManager mDataManager;

    @Inject
    public WelcomePresenter(DataManager mDataManager){
        this.mDataManager=mDataManager;
    }


    @Override
    public void getWelcomeData() {
        addSubscribe(mDataManager.fetchWelcomeInfo(RES)
                    .compose(RxUtil.<WelcomeBean>rxSchedulerHelper())
                    .subscribe(new Consumer<WelcomeBean>() {
                        @Override
                        public void accept(WelcomeBean welcomeBean) {
                            mView.showContent(welcomeBean);
                            startCountDown();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept( Throwable throwable){
                            mView.jumpToMain();
                        }
                    })
        );
    }


    private void startCountDown(){
        addSubscribe(Flowable.timer(Count_DOWN_TIME, TimeUnit.MILLISECONDS)
                    .compose(RxUtil.<Long>rxSchedulerHelper())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong){
                            mView.jumpToMain();
                        }
                    })
        );
    }
}
