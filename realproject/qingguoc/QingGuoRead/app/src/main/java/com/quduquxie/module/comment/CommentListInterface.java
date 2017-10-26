package com.quduquxie.module.comment;

import com.quduquxie.BasePresenter;
import com.quduquxie.BaseView;
import com.quduquxie.model.Comment;
import com.quduquxie.model.CommentReply;
import com.quduquxie.model.CommentUser;
import com.quduquxie.widget.LoadingPage;

import java.util.ArrayList;

/**
 * Created on 17/3/2.
 * Created by crazylei.
 */

public interface CommentListInterface {

    interface Presenter extends BasePresenter {

        void initParameter();

        void loadCommentList(String id_book, int page);

        void loadCommentListMore(String id_book, int page);

        boolean isLoadingMoreState();

        void setLoadingPage(LoadingPage loadingPage);

        void commentCommendAction(String id_book, Comment comment, boolean state, int position);

        void publishBookComment(String id_book, String id_user, String content);

        void publishReviewReply(Comment comment, String id_book, String content);

        void publishCommentReply(Comment comment, CommentUser commentUser, String id_book, String content);

        void reportCommentReply(Comment comment, CommentReply commentReply);

        void deleteCommentReplyRequest(String id_book, Comment comment, CommentReply commentReply, int position);

        void reportComment(Comment comment);

        void deleteCommentRequest(String id_book, Comment comment, int position);
    }

    interface View extends BaseView<Presenter> {

        void initView();

        void showLoadingPage();

        void hideLoadingPage();

        void showLoadingError();

        void setCommentCount(int count);

        void showCommentEmptyView();

        void showCommentContextView();

        void setHotComments(ArrayList<Comment> comments);

        void setNewComments(ArrayList<Comment> comments);

        void setRefreshViewState(boolean state);

        void showToast(String message);

        void hideReviewWriteDialog();

        void setCommentEnd();

        void setCommentFlag(int type);

        void addComment(Comment comment);

        void deleteCommentAction(Comment comment, int position);

        void hideReviewReplyDialog();

        void addCommentReply(Comment comment);

        void deleteCommentReplyAction(Comment comment, CommentReply commentReply, int position);

        void hideCommentReplyDialog();

        void commendLike(Comment comment, int position);

        void commendDislike(Comment comment, int position);
    }
}
