package com.quduquxie.modular.cover;

import com.quduquxie.base.BasePresenter;
import com.quduquxie.base.BaseView;
import com.quduquxie.base.bean.Book;
import com.quduquxie.model.Comment;
import com.quduquxie.model.CommentItem;
import com.quduquxie.model.CommentReply;
import com.quduquxie.model.CommentUser;
import com.quduquxie.widget.LoadingPage;

import java.util.ArrayList;

/**
 * Created on 17/5/23.
 * Created by crazylei.
 */

public interface CoverInterface {

    interface View extends BaseView {

        void initView();

        void showLoadingPage();

        void hideLoadingPage();

        void showLoadingError();

        void initBookInformation(Book book);

        void setCoverRecommend(ArrayList<Book> books);

        void setCommentItemList(ArrayList<CommentItem> commentItems);

        void setRefreshViewState(boolean state);

        void hideReviewWriteDialog();

        void addComment(Comment comment);

        void hideReviewReplyDialog();

        void addCommentReply(Comment comment);

        void hideCommentReplyDialog();

        void deleteCommentReplyAction(Comment comment, CommentReply commentReply);

        void deleteCommentAction(Comment comment, int position);

        void commendLike(Comment comment, int position);

        void commendDislike(Comment comment, int position);
    }

    interface Presenter extends BasePresenter {

        void loadCoverInformation(String id);

        void loadCoverRecommend(String id);

        void loadCommentList(String id, int page);

        void publishBookComment(String id, String user_id, String content);

        void publishReviewReply(Comment comment, String id, String content);

        void publishCommentReply(Comment comment, CommentUser commentUser, String id_book, String content);

        void reportCommentReply(Comment comment, CommentReply commentReply);

        void deleteCommentReplyRequest(String id, Comment comment, CommentReply commentReply);

        void reportComment(Comment comment);

        void deleteCommentRequest(String id, Comment comment, int position);

        void commentCommendAction(String id, Comment comment, boolean state, int position);

        void recycle();
    }
}
