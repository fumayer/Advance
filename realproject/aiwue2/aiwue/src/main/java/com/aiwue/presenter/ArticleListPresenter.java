package com.aiwue.presenter;

import com.aiwue.base.BasePresenter;
import com.aiwue.model.Article;
import com.aiwue.model.requestParams.GetRandomArticleListParams;
import com.aiwue.okhttp.AiwueClient;
import com.aiwue.okhttp.SubscriberCallBack;
import com.aiwue.iview.IArticleListView;
import com.orhanobut.logger.Logger;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *  主页中的文章列表页面
 * Created by Yibao on 2017年4月12日14:36:55
 * Copyright (c) 2017 aiwue.com All rights reserved
 */
public class ArticleListPresenter extends BasePresenter<IArticleListView> {
    public ArticleListPresenter(IArticleListView mvpView) {
        super(mvpView);
    }

    public void getArticleList(Integer pSize) {
        GetRandomArticleListParams params = new GetRandomArticleListParams(1,pSize, System.currentTimeMillis());
        AiwueClient.getRandomArticleList(null, null, Schedulers.io(), AndroidSchedulers.mainThread(),params, new SubscriberCallBack<List<Article>>(){
            @Override
            protected void onSuccess(List<Article> response) {
               if (response != null)
                    Logger.i(response.toString());
                mvpView.onGetArticleListSuccess(true, null, response);

            }
            @Override
            protected void onError(String err) {
                Logger.i(err);
                mvpView.onGetArticleListSuccess(false, err, null);

            }

        });
    }
}
