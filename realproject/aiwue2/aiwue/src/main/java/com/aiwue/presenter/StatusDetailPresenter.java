package com.aiwue.presenter;


import com.aiwue.iview.IStatusDetailView;
import com.aiwue.model.Article;
import com.aiwue.model.Note;
import com.aiwue.okhttp.AiwueClient;
import com.aiwue.okhttp.SubscriberCallBack;
import com.orhanobut.logger.Logger;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *  我的-》设置页面 presenter
 * Created by chenhui on 2017年4月19日 14:26
 * Copyright (c) 2017 aiwue.com All rights reserved
 */
public class StatusDetailPresenter extends BaseDetailPresenter<IStatusDetailView> {
    public StatusDetailPresenter(IStatusDetailView mvpView) {
        super(mvpView);
    }
    public void getStatusDetail(Integer articleId){
//        AiwueClient.getNoteDetail(null, null, Schedulers.io(), AndroidSchedulers.mainThread(), articleId, new SubscriberCallBack<Note>() {
//            @Override
//            protected  void onSuccess(Article response) {
//                Logger.i(response.toString());
//                mvpView.onGetStatusDetailSuccess(true,null,response);
//            }
//            @Override
//            protected void onError(String err) {
//                mvpView.onGetStatusDetailSuccess(false, err,null);
//            }
//        });
    }

}
