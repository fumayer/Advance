package com.quduquxie.base.module.billboard;

import com.quduquxie.base.BasePresenter;
import com.quduquxie.base.BaseView;
import com.quduquxie.base.bean.Book;

import java.util.ArrayList;

/**
 * Created on 17/7/25.
 * Created by crazylei.
 */

public interface BillboardContentInterface {

    interface View extends BaseView {

        void showLoadingPage();

        void hideLoadingPage();

        void showLoadingError();

        void initializeData(ArrayList<Book> bookList);

        void initializeDataMore(ArrayList<Book> bookList);

        void resetRefreshViewState(boolean state);
    }

    interface Presenter extends BasePresenter {

        void loadBillboardContent(String uri, String date, String channel, int page);

        void loadBillboardContentMore(String uri, String date, String channel, int page);

        boolean loadingMoreState();
    }
}