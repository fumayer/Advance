package com.quduquxie.base.module.reading.catalog;

import com.quduquxie.base.BasePresenter;
import com.quduquxie.base.BaseView;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.bean.Bookmark;

import java.util.ArrayList;

/**
 * Created on 17/7/27.
 * Created by crazylei.
 */

public interface BookmarkContentInterface {

    interface Presenter extends BasePresenter {

        void initializeBookmarks(Book book);
    }

    interface View extends BaseView {

        void showLoadingPage();

        void hideLoadingPage();

        void showLoadingError();

        void initializeBookmarks(ArrayList<Bookmark> bookmarkList);
    }
}