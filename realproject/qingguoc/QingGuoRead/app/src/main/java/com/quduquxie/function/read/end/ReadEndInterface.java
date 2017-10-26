package com.quduquxie.function.read.end;

import com.quduquxie.base.bean.Book;
import com.quduquxie.function.BasePresenter;
import com.quduquxie.function.BaseView;
import com.quduquxie.widget.LoadingPage;

import java.util.List;

/**
 * Created on 17/4/16.
 * Created by crazylei.
 */

public interface ReadEndInterface {

    interface Presenter extends BasePresenter {

        void loadRecommendData(String id);

        void setLoadingPage(LoadingPage loadingPage);
    }

    interface View extends BaseView<Presenter> {

        void initView();

        void showLoadingPage();

        void hideLoadingPage();

        void showLoadingError();

        void setRecommendData(List<Book> books);

        void showToast(String message);
    }
}