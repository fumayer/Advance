package com.quduquxie.base.module.billboard.presenter;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.quduquxie.base.RxPresenter;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.config.BaseConfig;
import com.quduquxie.base.module.billboard.BillboardContentInterface;
import com.quduquxie.base.module.billboard.view.fragment.BillboardContentFragment;
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

public class BillboardContentPresenter extends RxPresenter implements BillboardContentInterface.Presenter {

    private BillboardContentFragment billboardContentFragment;

    private boolean loadingMoreState = true;

    public BillboardContentPresenter(BillboardContentFragment billboardContentFragment) {
        this.billboardContentFragment = billboardContentFragment;
    }

    @Override
    public void loadBillboardContent(String uri, String date, String channel, int page) {

        billboardContentFragment.showLoadingPage();

        DataRequestInterface dataRequestInterface = DataRequestFactory.instantiation(DataRequestInterface.class, DataRequestFactory.TYPE_HTTP, false);
        insertDisposable(dataRequestInterface.loadBillboardData(uri, date, channel, page, BaseConfig.LIST_PAGINATION_COUNT)
                .compose(RxSchedulers.<CommunalResult<ArrayList<Book>>>schedulerHelper())
                .subscribeWith(new ResourceSubscriber<CommunalResult<ArrayList<Book>>>() {
                    @Override
                    public void onNext(CommunalResult<ArrayList<Book>> booksResult) {

                        if (booksResult != null) {
                            if (booksResult.getCode() == 0 && booksResult.getModel() != null && booksResult.getModel().size() > 0) {

                                loadingMoreState = booksResult.getModel().size() >= BaseConfig.LIST_PAGINATION_COUNT;

                                billboardContentFragment.initializeData(booksResult.getModel());

                                billboardContentFragment.hideLoadingPage();

                            } else if (!TextUtils.isEmpty(booksResult.getMessage())) {
                                loadingMoreState = false;
                                billboardContentFragment.showPromptMessage(booksResult.getMessage());
                                billboardContentFragment.showLoadingError();
                            } else {
                                loadingMoreState = false;
                                billboardContentFragment.showLoadingError();
                            }
                        } else {
                            loadingMoreState = false;
                            billboardContentFragment.showLoadingError();
                        }

                        Logger.e("LoadBillboardData onNext");
                    }

                    @Override
                    public void onError(Throwable throwable) {

                        loadingMoreState = false;

                        billboardContentFragment.showLoadingError();
                        billboardContentFragment.resetRefreshViewState(false);
                        billboardContentFragment.collectException(throwable);

                        throwable.printStackTrace();

                        Logger.e("LoadBillboardData onError: " + throwable);
                    }

                    @Override
                    public void onComplete() {

                        billboardContentFragment.resetRefreshViewState(false);

                        Logger.e("LoadBillboardData onComplete");
                    }
                }));
    }

    @Override
    public void loadBillboardContentMore(String uri, String date, String channel, int page) {

        DataRequestInterface dataRequestInterface = DataRequestFactory.instantiation(DataRequestInterface.class, DataRequestFactory.TYPE_HTTP, false);
        insertDisposable(dataRequestInterface.loadBillboardData(uri, date, channel, page, BaseConfig.LIST_PAGINATION_COUNT)
                .compose(RxSchedulers.<CommunalResult<ArrayList<Book>>>schedulerHelper())
                .subscribeWith(new ResourceSubscriber<CommunalResult<ArrayList<Book>>>() {
                    @Override
                    public void onNext(CommunalResult<ArrayList<Book>> booksResult) {

                        if (booksResult != null) {
                            if (booksResult.getCode() == 0 && booksResult.getModel() != null && booksResult.getModel().size() > 0) {

                                loadingMoreState = booksResult.getModel().size() >= BaseConfig.LIST_PAGINATION_COUNT;

                                billboardContentFragment.initializeDataMore(booksResult.getModel());

                            } else if (!TextUtils.isEmpty(booksResult.getMessage())) {
                                loadingMoreState = false;
                                billboardContentFragment.showPromptMessage(booksResult.getMessage());
                            } else {
                                loadingMoreState = false;
                            }
                        } else {
                            loadingMoreState = false;
                        }

                        Logger.e("LoadBillboardData onNext");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        loadingMoreState = false;

                        billboardContentFragment.resetRefreshViewState(false);
                        billboardContentFragment.collectException(throwable);

                        throwable.printStackTrace();

                        Logger.e("LoadBillboardData onError: " + throwable);
                    }

                    @Override
                    public void onComplete() {

                        billboardContentFragment.resetRefreshViewState(false);

                        Logger.e("LoadBillboardData onComplete");
                    }
                }));
    }

    @Override
    public boolean loadingMoreState() {
        return loadingMoreState;
    }

    @Override
    public void recycle() {

    }
}
