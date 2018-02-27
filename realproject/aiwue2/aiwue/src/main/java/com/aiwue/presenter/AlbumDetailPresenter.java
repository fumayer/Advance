package com.aiwue.presenter;

import com.aiwue.iview.IAlbumDetailView;
import com.aiwue.iview.IArticleDetailView;
import com.aiwue.model.Article;
import com.aiwue.okhttp.AiwueClient;
import com.aiwue.okhttp.SubscriberCallBack;
import com.orhanobut.logger.Logger;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *  文章详情 presenter
 * Created by Yibao on 2017年4月12日14:36:55
 * Copyright (c) 2017 aiwue.com All rights reserved
 */
public class AlbumDetailPresenter extends BaseDetailPresenter<IAlbumDetailView> {
    public AlbumDetailPresenter(IAlbumDetailView mvpView) {
        super(mvpView);
    }
  /*  public void getArticleDetail(Integer articleId) {
        AiwueClient.getArticleDetail(null, null, Schedulers.io(), AndroidSchedulers.mainThread(),articleId, new SubscriberCallBack<Article>(){
            @Override
            protected void onSuccess(Article response) {
                Logger.i(response.toString());
                mvpView.onGetArticleDetailSuccess(true, null,response);

            }
            @Override
            protected void onError(String err) {
                mvpView.onGetArticleDetailSuccess(false, err,null);
            }
        });

    }*/
}
