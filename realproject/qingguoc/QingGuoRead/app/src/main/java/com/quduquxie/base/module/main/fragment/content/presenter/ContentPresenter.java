package com.quduquxie.base.module.main.fragment.content.presenter;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.quduquxie.base.RxPresenter;
import com.quduquxie.base.bean.MainContent;
import com.quduquxie.base.module.main.fragment.content.ContentInterface;
import com.quduquxie.base.module.main.fragment.content.view.ContentFragment;
import com.quduquxie.base.retrofit.DataRequestFactory;
import com.quduquxie.base.retrofit.DataRequestInterface;
import com.quduquxie.base.retrofit.bean.CommunalResult;
import com.quduquxie.base.rxjava.RxSchedulers;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created on 17/7/20.
 * Created by crazylei.
 */

public class ContentPresenter extends RxPresenter implements ContentInterface.Presenter {

    private ContentFragment contentFragment;

    public ContentPresenter(ContentFragment contentFragment) {
        this.contentFragment = contentFragment;
    }

    @Override
    public void loadSelectedData(int type, String key) {

        contentFragment.showLoadingPage();

        ResourceSubscriber<CommunalResult<MainContent>> resourceSubscriber = new ResourceSubscriber<CommunalResult<MainContent>>() {
            @Override
            public void onNext(CommunalResult<MainContent> mainContentResult) {
                if (mainContentResult != null) {
                    if (mainContentResult.getCode() == 0 && mainContentResult.getModel() != null) {
                        contentFragment.setMainContentData(mainContentResult.getModel());
                        contentFragment.hideLoadingPage();
                    } else if (!TextUtils.isEmpty(mainContentResult.getMessage())) {
                        contentFragment.showPromptMessage(mainContentResult.getMessage());
                        contentFragment.showLoadingError();
                    } else {
                        contentFragment.showLoadingError();
                    }
                } else {
                    contentFragment.showLoadingError();
                }

                Logger.d("LoadMainContent onNext");
            }

            @Override
            public void onError(Throwable throwable) {
                contentFragment.showLoadingError();
                contentFragment.collectException(throwable);
                contentFragment.resetRefreshViewState(false);

                Logger.d("LoadMainContent onError: " + throwable.toString());
            }

            @Override
            public void onComplete() {
                contentFragment.resetRefreshViewState(false);

                Logger.d("LoadMainContent onComplete");
            }
        };

        DataRequestInterface dataRequestInterface = DataRequestFactory.instantiation(DataRequestInterface.class, DataRequestFactory.TYPE_HTTP, false);
        if (ContentFragment.TYPE_SELECTED == type) {
            dataRequestInterface.loadSelectedData(key)
                    .compose(RxSchedulers.<CommunalResult<MainContent>>schedulerHelper())
                    .subscribeWith(resourceSubscriber);
        } else if (ContentFragment.TYPE_LIBRARY == type) {
            dataRequestInterface.loadLibraryData(key)
                    .compose(RxSchedulers.<CommunalResult<MainContent>>schedulerHelper())
                    .subscribeWith(resourceSubscriber);
        }
    }

    @Override
    public void recycle() {

    }
}
