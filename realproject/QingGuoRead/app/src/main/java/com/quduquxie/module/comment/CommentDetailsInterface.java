package com.quduquxie.module.comment;

import com.quduquxie.BasePresenter;
import com.quduquxie.BaseView;
import com.quduquxie.model.Comment;
import com.quduquxie.model.CommentReply;
import com.quduquxie.model.CommentUser;
import com.quduquxie.widget.LoadingPage;

import java.util.ArrayList;

/**
 * Created on 17/3/1.
 * Created by crazylei.
 */

public interface CommentDetailsInterface {

    interface Presenter extends BasePresenter {

        void initParameter(String id_book, String id_comment);

        void loadCommentDetails(String id_book, String id_comment);

        void setLoadingPage(LoadingPage loadingPage);

        void publishReviewReply(Comment comment, String id_book, String content);

        void publishCommentReply(Comment comment, CommentUser commentUser, String id_book, String content);

        void commentCommendAction(String id_book, Comment comment, boolean state, int position);

        void reportCommentReply(Comment comment, CommentReply commentReply);

        void deleteCommentReply(String id_book, Comment comment, CommentReply commentReply);

        void reportComment(Comment comment);

        void deleteCommentRequest(String id_book, Comment comment);
    }

    interface View extends BaseView<Presenter> {

        void initView();

        void showLoadingPage();

        void hideLoadingPage();

        void showLoadingError();

        void setCommentDetails(Comment comment);

        void showToast(String message);

        void commendLike(Comment comment, int position);

        void commendDislike(Comment comment, int position);

        void addCommentReply(ArrayList<CommentReply> commentReplies);

        void setRefreshViewState(boolean state);

        void hideCommentReplyDialog();

        void hideReviewReplyDialog();

        void deleteCommentReply(CommentReply commentReply);

        void deleteCommentAction(Comment comment);
    }
}
