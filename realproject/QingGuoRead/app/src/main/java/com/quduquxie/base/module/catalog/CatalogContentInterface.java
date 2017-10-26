package com.quduquxie.base.module.catalog;

import com.quduquxie.base.BasePresenter;
import com.quduquxie.base.BaseView;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.bean.Chapter;

import java.util.ArrayList;

/**
 * Created on 17/7/27.
 * Created by crazylei.
 */

public interface CatalogContentInterface {

    interface Presenter extends BasePresenter {

        void initializeChapters(Book book);
    }

    interface View extends BaseView {

        void showLoadingPage();

        void hideLoadingPage();

        void showLoadingError();

        void insertChapters(ArrayList<Chapter> chapterList);
    }
}