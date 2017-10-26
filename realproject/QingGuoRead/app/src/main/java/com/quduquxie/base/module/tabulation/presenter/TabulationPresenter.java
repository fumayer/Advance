package com.quduquxie.base.module.tabulation.presenter;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.quduquxie.base.RxPresenter;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.config.BaseConfig;
import com.quduquxie.base.module.tabulation.TabulationInterface;
import com.quduquxie.base.module.tabulation.view.TabulationActivity;
import com.quduquxie.base.retrofit.DataRequestFactory;
import com.quduquxie.base.retrofit.DataRequestInterface;
import com.quduquxie.base.retrofit.bean.CommunalResult;
import com.quduquxie.base.rxjava.RxSchedulers;

import java.util.ArrayList;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created on 17/7/25.
 * Created by crazylei.
 */

public class TabulationPresenter extends RxPresenter implements TabulationInterface.Presenter {

    private TabulationActivity tabulationActivity;

    private boolean loadingMoreState = true;

    public TabulationPresenter(TabulationActivity tabulationActivity) {
        this.tabulationActivity = tabulationActivity;
    }

    @Override
    public void loadTabulationData(String uri, String title, String type, int page) {

        tabulationActivity.showLoadingPage();

        DataRequestInterface dataRequestInterface = DataRequestFactory.instantiation(DataRequestInterface.class, DataRequestFactory.TYPE_HTTP, false);

        ResourceSubscriber<CommunalResult<ArrayList<Book>>> resultResourceSubscriber = new ResourceSubscriber<CommunalResult<ArrayList<Book>>>() {
            @Override
            public void onNext(CommunalResult<ArrayList<Book>> tabulationListResult) {
                if (tabulationListResult != null) {
                    if (tabulationListResult.getCode() == 0 && tabulationListResult.getModel() != null && tabulationListResult.getModel().size() > 0) {

                        loadingMoreState = tabulationListResult.getModel().size() >= BaseConfig.LIST_PAGINATION_COUNT;

                        tabulationActivity.initializeTabulationData(tabulationListResult.getModel());
                        tabulationActivity.hideLoadingPage();

                    } else if (!TextUtils.isEmpty(tabulationListResult.getMessage())) {
                        loadingMoreState = false;
                        tabulationActivity.showPromptMessage(tabulationListResult.getMessage());
                        tabulationActivity.showLoadingError();
                    } else {
                        loadingMoreState = false;
                        tabulationActivity.showLoadingError();
                    }
                } else {
                    loadingMoreState = false;
                    tabulationActivity.showLoadingError();
                }

                Logger.d("LoadTabulation onNext");

            }

            @Override
            public void onError(Throwable throwable) {
                loadingMoreState = false;
                tabulationActivity.showLoadingError();
                tabulationActivity.resetRefreshViewState(false);

                throwable.printStackTrace();

                Logger.d("LoadTabulation onError: " + throwable.toString());
            }

            @Override
            public void onComplete() {
                tabulationActivity.resetRefreshViewState(false);

                Logger.d("LoadTabulation onComplete");
            }
        };

        if ("category".equals(type)) {
            insertDisposable(dataRequestInterface.loadCategoryTabulation(title, page, BaseConfig.LIST_PAGINATION_COUNT)
                    .compose(RxSchedulers.<CommunalResult<ArrayList<Book>>>schedulerHelper())
                    .subscribeWith(resultResourceSubscriber));
        } else {
            insertDisposable(dataRequestInterface.loadTabulationData(uri, title, page, BaseConfig.LIST_PAGINATION_COUNT)
                    .compose(RxSchedulers.<CommunalResult<ArrayList<Book>>>schedulerHelper())
                    .subscribeWith(resultResourceSubscriber));
        }
    }

    @Override
    public void loadTabulationDataMore(String uri, String title, String type, int page) {

        DataRequestInterface dataRequestInterface = DataRequestFactory.instantiation(DataRequestInterface.class, DataRequestFactory.TYPE_HTTP, false);

        ResourceSubscriber<CommunalResult<ArrayList<Book>>> resultResourceSubscriber = new ResourceSubscriber<CommunalResult<ArrayList<Book>>>() {

            @Override
            public void onNext(CommunalResult<ArrayList<Book>> tabulationListResult) {
                if (tabulationListResult != null) {
                    if (tabulationListResult.getCode() == 0 && tabulationListResult.getModel() != null && tabulationListResult.getModel().size() > 0) {

                        loadingMoreState = tabulationListResult.getModel().size() >= BaseConfig.LIST_PAGINATION_COUNT;

                        tabulationActivity.initializeTabulationDataMore(tabulationListResult.getModel());

                    } else if (!TextUtils.isEmpty(tabulationListResult.getMessage())) {
                        loadingMoreState = false;
                        tabulationActivity.showPromptMessage(tabulationListResult.getMessage());
                    } else {
                        loadingMoreState = false;
                    }
                } else {
                    loadingMoreState = false;
                }

                Logger.d("LoadTabulationMore onNext");
            }

            @Override
            public void onError(Throwable throwable) {
                loadingMoreState = false;
                tabulationActivity.resetRefreshViewState(false);

                throwable.printStackTrace();

                Logger.d("LoadTabulationMore onError: " + throwable.toString());
            }

            @Override
            public void onComplete() {
                tabulationActivity.resetRefreshViewState(false);

                Logger.d("LoadTabulationMore onComplete");
            }
        };

        if ("category".equals(type)) {
            insertDisposable(dataRequestInterface.loadCategoryTabulation(title, page, BaseConfig.LIST_PAGINATION_COUNT)
                    .compose(RxSchedulers.<CommunalResult<ArrayList<Book>>>schedulerHelper())
                    .subscribeWith(resultResourceSubscriber));
        } else {
            insertDisposable(dataRequestInterface.loadTabulationData(uri, title, page, BaseConfig.LIST_PAGINATION_COUNT)
                    .compose(RxSchedulers.<CommunalResult<ArrayList<Book>>>schedulerHelper())
                    .subscribeWith(resultResourceSubscriber));
        }
    }

    @Override
    public boolean loadingMoreState() {
        return loadingMoreState;
    }

    @Override
    public void recycle() {
        if (tabulationActivity != null) {
            tabulationActivity = null;
        }
    }
}