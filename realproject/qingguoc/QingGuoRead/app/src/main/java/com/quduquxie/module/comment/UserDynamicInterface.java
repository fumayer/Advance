package com.quduquxie.module.comment;

import com.quduquxie.BasePresenter;
import com.quduquxie.BaseView;
import com.quduquxie.model.Review;
import com.quduquxie.widget.LoadingPage;

import java.util.ArrayList;

/**
 * Created on 17/3/1.
 * Created by crazylei.
 */

public interface UserDynamicInterface {

    interface Presenter extends BasePresenter {

        void initParameter(int page);

        void loadUserDynamic(int page);

        void loadUserDynamicMore(int page);

        void setLoadingPage(LoadingPage loadingPage);

        boolean isLoadingMoreState();

        void publishCommentReply(Review review, String content);
    }

    interface View extends BaseView<Presenter> {

        void initView();

        void showLoadingPage();

        void hideLoadingPage();

        void showLoadingError();

        void setDynamics(ArrayList<Review> dynamics);

        void setDynamicsMore(ArrayList<Review> dynamics);

        void setListEnd();

        void setRefreshViewState(boolean state);

        void showToast(String message);

        void hideCommentActionDialog();
    }
}
