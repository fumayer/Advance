package com.quduquxie.base.module.catalog.presenter;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.quduquxie.base.RxPresenter;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.bean.Chapter;
import com.quduquxie.base.database.dao.ChapterDao;
import com.quduquxie.base.module.catalog.CatalogContentInterface;
import com.quduquxie.base.module.catalog.view.fragment.CatalogContentFragment;
import com.quduquxie.base.retrofit.DataRequestFactory;
import com.quduquxie.base.retrofit.DataRequestInterface;
import com.quduquxie.base.retrofit.bean.CommunalResult;
import com.quduquxie.base.rxjava.RxSchedulers;

import java.util.ArrayList;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created on 17/7/27.
 * Created by crazylei.
 */

public class CatalogContentPresenter extends RxPresenter implements CatalogContentInterface.Presenter {

    private CatalogContentFragment catalogContentFragment;

    public CatalogContentPresenter(CatalogContentFragment catalogContentFragment) {
        this.catalogContentFragment = catalogContentFragment;
    }

    @Override
    public void initializeChapters(Book book) {
        catalogContentFragment.showLoadingPage();

        if (book != null && !TextUtils.isEmpty(book.id)) {
            ChapterDao chapterDao = new ChapterDao(catalogContentFragment.getContext(), book.id);

            ArrayList<Chapter> chapterList = chapterDao.loadChapters();

            if (chapterList != null && chapterList.size() != 0) {
                catalogContentFragment.insertChapters(chapterList);
                catalogContentFragment.hideLoadingPage();
            } else {

                DataRequestInterface dataRequestInterface = DataRequestFactory.instantiation(DataRequestInterface.class, DataRequestFactory.TYPE_HTTP, false);
                insertDisposable(dataRequestInterface.loadBookCatalog(book.id, 1)
                .compose(RxSchedulers.<CommunalResult<ArrayList<Chapter>>>schedulerHelper())
                .subscribeWith(new ResourceSubscriber<CommunalResult<ArrayList<Chapter>>>() {
                    @Override
                    public void onNext(CommunalResult<ArrayList<Chapter>> chaptersResult) {
                        catalogContentFragment.hideLoadingPage();

                        if (chaptersResult != null) {
                            if (chaptersResult.getCode() == 0 && chaptersResult.getModel() != null && chaptersResult.getModel().size() > 0) {
                                catalogContentFragment.insertChapters(chaptersResult.getModel());
                                catalogContentFragment.hideLoadingPage();
                            } else if (!TextUtils.isEmpty(chaptersResult.getMessage())) {
                                catalogContentFragment.showPromptMessage(chaptersResult.getMessage());
                                catalogContentFragment.showLoadingError();
                            } else {
                                catalogContentFragment.showLoadingError();
                            }
                        } else {
                            catalogContentFragment.showLoadingError();
                        }

                        Logger.e("LoadBookCatalog onNext");
                    }


                    @Override
                    public void onError(Throwable throwable) {
                        catalogContentFragment.showLoadingError();
                        catalogContentFragment.collectException(throwable);

                        throwable.printStackTrace();

                        Logger.d("LoadBookCatalog onError: " + throwable.toString());
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("LoadBookCatalog onComplete");
                    }
                }));
            }
        } else {
            catalogContentFragment.showLoadingError();
        }
    }

    @Override
    public void recycle() {

    }
}
