package com.quduquxie.base.module.tabulation;

import com.quduquxie.base.BasePresenter;
import com.quduquxie.base.BaseView;
import com.quduquxie.base.bean.Book;

import java.util.ArrayList;

/**
 * Created on 17/7/25.
 * Created by crazylei.
 */

public interface TabulationInterface {

    interface View extends BaseView {

        void showLoadingPage();

        void hideLoadingPage();

        void showLoadingError();

        void initializeTabulationData(ArrayList<Book> bookList);

        void initializeTabulationDataMore(ArrayList<Book> bookList);

        void resetRefreshViewState(boolean state);
    }

    interface Presenter extends BasePresenter {

        void loadTabulationData(String uri, String title, String type, int page);

        void loadTabulationDataMore(String uri, String title, String type, int page);

        boolean loadingMoreState();
    }
}