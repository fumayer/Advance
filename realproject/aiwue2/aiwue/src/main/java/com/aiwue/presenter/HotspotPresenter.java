package com.aiwue.presenter;

import com.aiwue.base.BasePresenter;
import com.aiwue.iview.IHotspotView;
import com.aiwue.model.Article;
import com.aiwue.model.Banner;
import com.aiwue.model.Note;
import com.aiwue.model.RecommendFriends;
import com.aiwue.model.requestParams.GetBannerParams;
import com.aiwue.model.requestParams.GetRandomArticleListParams;
import com.aiwue.model.requestParams.GetRecFriendsListParams;
import com.aiwue.okhttp.AiwueClient;
import com.aiwue.okhttp.SubscriberCallBack;
import com.orhanobut.logger.Logger;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 主页中热点的banner页面
 * Created by Administrator on 2017/4/28.
 */

public class HotspotPresenter extends BasePresenter<IHotspotView>{
    public HotspotPresenter(IHotspotView mvpView) {
        super(mvpView);
    }

    public void getBannerList(Integer type){
        AiwueClient.getBannerList(null, null, Schedulers.io(), AndroidSchedulers.mainThread(), type, new SubscriberCallBack<List<Banner>>() {
            @Override
            protected void onSuccess(List<Banner> response) {
                if(response != null)
                    Logger.i(response.toString());
                mvpView.onGetBannerListSuccess(true, null, response);
            }
            @Override
            protected void onError(String err) {
                Logger.i(err);
                mvpView.onGetBannerListSuccess(false, err, null);
            }
        });
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

    public void getRecommendFriendsList(Integer pSize){
        GetRecFriendsListParams params = new GetRecFriendsListParams(1,pSize);
        AiwueClient.getRecFriendsList(null, null, Schedulers.io(), AndroidSchedulers.mainThread(), params, new SubscriberCallBack<List<RecommendFriends>>() {
            @Override
            protected void onSuccess(List<RecommendFriends> response) {
                if (response != null)
                    Logger.i(response.toString());
                mvpView.onGetRecFriendsListSuccess(true, null, response);

            }

            @Override
            protected void onError(String err) {
                super.onError(err);
                mvpView.onGetRecFriendsListSuccess(false, err, null);
            }
        });
    }
}
