package com.quduquxie.module.comment;

import com.quduquxie.BasePresenter;
import com.quduquxie.BaseView;
import com.quduquxie.model.Review;
import com.quduquxie.widget.LoadingPage;

import java.util.ArrayList;

/**
 * Created on 17/2/28.
 * Created by crazylei.
 */

public interface ReceivedRepliesInterface {

    interface Presenter extends BasePresenter {

        void initParameter(int page);

        void loadReceivedReplies(int page);

        void loadReceivedRepliesMore(int page);

        boolean isLoadingMoreState();

        void setLoadingPage(LoadingPage loadingPage);

        void publishCommentReply(Review review, String content);
    }

    interface View extends BaseView<Presenter> {

        void initView();

        void showLoadingPage();

        void hideLoadingPage();

        void showLoadingError();

        void setReceivedReplies(ArrayList<Review> receivedReplyList);

        void setReceivedRepliesMore(ArrayList<Review> receivedReplyList);

        void setListEnd();

        void setRefreshViewState(boolean state);

        void showToast(String message);

        void hideCommentActionDialog();
    }
}
