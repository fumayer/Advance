package com.quduquxie.util;

import android.app.Activity;

import com.orhanobut.logger.Logger;
import com.quduquxie.application.QuApplication;
import com.quduquxie.base.bean.Update;
import com.quduquxie.base.retrofit.DataRequestFactory;
import com.quduquxie.base.retrofit.DataRequestInterface;
import com.quduquxie.base.retrofit.bean.BlanketResult;

import java.lang.ref.WeakReference;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

public class APIUpdateHelper {

    private WeakReference<Activity> activityReference;
    private CheckUpdateListener checkUpdateListener;

    public APIUpdateHelper(Activity activity) {
        activityReference = new WeakReference<>(activity);

    }

    public void setOnCheckUpdateListener(CheckUpdateListener checkUpdateListener) {
        this.checkUpdateListener = checkUpdateListener;
    }

    /**
     * 自有服务器更新
     */
    public void checkApiUpdate() {
        DataRequestInterface dataRequestInterface = DataRequestFactory.instantiation(DataRequestInterface.class, DataRequestFactory.TYPE_HTTP, false);
        dataRequestInterface.loadAppUpdateInformation()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<BlanketResult<Update>>() {
                    @Override
                    public void onNext(BlanketResult<Update> updateResult) {
                        if (updateResult != null && updateResult.isSuccess() && updateResult.getModel() != null) {
                            Logger.d("LoadAppUpdateInformation onNext: " + updateResult.getModel().toString());

                            Update update = updateResult.getModel();

                            if (update.update && update.code > QuApplication.getVersionCode()) {
                                if (update.force) {
                                    UpdateDialogUtil.autoUpdateDownload(activityReference.get(), update, true, true);
                                } else {
                                    if (checkUpdateListener != null) {
                                        checkUpdateListener.apiUpdateSuccess(update);
                                    }
                                }
                            } else {
                                if (checkUpdateListener != null)
                                    checkUpdateListener.apiUpdateHasBeenUpdated();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("LoadAppUpdateInformation onError: " + throwable.toString());

                        if (checkUpdateListener != null) {
                            checkUpdateListener.apiUpdateError();
                        }
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("LoadAppUpdateInformation onComplete");
                    }
                });
    }

    public interface CheckUpdateListener {

        void apiUpdateSuccess(Update update);

        void apiUpdateHasBeenUpdated();

        void apiUpdateError();
    }
}
