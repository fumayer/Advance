package com.quduquxie.base.util;

import android.app.Activity;

import com.orhanobut.logger.Logger;
import com.quduquxie.application.QuApplication;
import com.quduquxie.base.bean.Update;
import com.quduquxie.base.retrofit.DataRequestFactory;
import com.quduquxie.base.retrofit.DataRequestInterface;
import com.quduquxie.base.retrofit.bean.BlanketResult;
import com.quduquxie.base.rxjava.RxSchedulers;

import java.lang.ref.WeakReference;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created on 17/7/28.
 * Created by crazylei.
 */

public class AppUpdateHelper {

    private WeakReference<Activity> activityReference;
    private CheckApplicationUpdateListener checkApplicationUpdateListener;

    public AppUpdateHelper(Activity activity) {
        activityReference = new WeakReference<>(activity);

    }

    public void setOnCheckUpdateListener(CheckApplicationUpdateListener checkApplicationUpdateListener) {
        this.checkApplicationUpdateListener = checkApplicationUpdateListener;
    }

    public void checkApiUpdate() {
        DataRequestInterface dataRequestInterface = DataRequestFactory.instantiation(DataRequestInterface.class, DataRequestFactory.TYPE_HTTP, false);
        dataRequestInterface.loadAppUpdateInformation()
                .compose(RxSchedulers.<BlanketResult<Update>>schedulerHelper())
                .subscribeWith(new ResourceSubscriber<BlanketResult<Update>>() {
                    @Override
                    public void onNext(BlanketResult<Update> updateResult) {


                        if (updateResult != null) {
                            if (updateResult.isSuccess() && updateResult.getModel() != null) {

                                Update update = updateResult.getModel();

                                if (update.update && update.code > QuApplication.getVersionCode()) {
                                    if (update.force) {
                                        AppUpdateDialogHelper.autoUpdateDownload(activityReference.get(), update, true, true);
                                    } else {
                                        if (checkApplicationUpdateListener != null) {
                                            checkApplicationUpdateListener.applicationUpdateSuccess(update);
                                        }
                                    }
                                } else {
                                    if (checkApplicationUpdateListener != null) {
                                        checkApplicationUpdateListener.applicationHasBeenUpdated();
                                    }
                                }
                            } else {
                                if (checkApplicationUpdateListener != null) {
                                    checkApplicationUpdateListener.applicationUpdateError();
                                }
                            }
                        } else {

                            if (checkApplicationUpdateListener != null) {
                                checkApplicationUpdateListener.applicationUpdateError();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if (checkApplicationUpdateListener != null) {
                            checkApplicationUpdateListener.applicationUpdateError();
                        }

                        throwable.printStackTrace();

                        Logger.d("LoadAppUpdateInformation onError: " + throwable.toString());
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("LoadAppUpdateInformation onComplete");
                    }
                });
    }

    public void recycle() {
        if (activityReference != null) {
            activityReference.clear();
            activityReference = null;
        }

        if (checkApplicationUpdateListener != null) {
            checkApplicationUpdateListener = null;
        }
    }

    public interface CheckApplicationUpdateListener {

        void applicationUpdateSuccess(Update update);

        void applicationHasBeenUpdated();

        void applicationUpdateError();
    }
}