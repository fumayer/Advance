package com.quduquxie.function.read.end.presenter;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.quduquxie.base.bean.Book;
import com.quduquxie.function.read.end.ReadEndInterface;
import com.quduquxie.function.read.end.view.ReadEndActivity;
import com.quduquxie.retrofit.DataRequestService;
import com.quduquxie.retrofit.ServiceGenerator;
import com.quduquxie.retrofit.model.CommunalResult;
import com.quduquxie.widget.LoadingPage;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created on 17/4/16.
 * Created by crazylei.
 */

public class ReadEndPresenter implements ReadEndInterface.Presenter {

    private ReadEndActivity readEndActivity;

    private LoadingPage loadingPage;

    public ReadEndPresenter(ReadEndActivity readEndActivity) {
        this.readEndActivity = readEndActivity;
    }

    @Override
    public void initParameter() {
        readEndActivity.initView();
    }

    @Override
    public void loadRecommendData(String id) {
        if (!TextUtils.isEmpty(id)) {

            readEndActivity.showLoadingPage();

            DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, false);
            dataRequestService.loadReadEndRecommendList(id)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ResourceSubscriber<CommunalResult<List<Book>>>() {
                        @Override
                        public void onNext(CommunalResult<List<Book>> recommendResult) {
                            Logger.d("LoadReadEndRecommendList onNext");

                            readEndActivity.hideLoadingPage();

                            if (recommendResult != null) {
                                if (recommendResult.getCode() == 0 && recommendResult.getModel() != null && recommendResult.getModel().size() > 0) {
                                    readEndActivity.setRecommendData(recommendResult.getModel());
                                } else {
                                    if (!TextUtils.isEmpty(recommendResult.getMessage())) {
                                        readEndActivity.showToast(recommendResult.getMessage());
                                        readEndActivity.setRecommendData(null);
                                    }
                                }
                            } else {
                                readEndActivity.setRecommendData(null);
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Logger.d("LoadReadEndRecommendList onError: " + throwable.toString());
                            readEndActivity.showLoadingError();
                        }

                        @Override
                        public void onComplete() {
                            Logger.d("LoadReadEndRecommendList onComplete");
                        }
                    });
        } else {
            readEndActivity.setRecommendData(null);
        }
    }

    @Override
    public void setLoadingPage(LoadingPage loadingPage) {
        this.loadingPage = loadingPage;
    }
}