package com.quduquxie.function.download;

import com.quduquxie.base.bean.Book;
import com.quduquxie.function.BasePresenter;
import com.quduquxie.function.BaseView;

import java.util.ArrayList;

/**
 * Created on 17/4/7.
 * Created by crazylei.
 */

public interface DownloadManagerInterface {

    interface Presenter extends BasePresenter {

    }

    interface View extends BaseView<Presenter> {

        void showLoadingPage();

        void hideLoadingPage();

        void setDownloadResource(ArrayList<Book> bookList);

        void showToast(String message);
    }
}
