package com.quduquxie.module.comment.listener;

import android.view.View;

import com.quduquxie.model.Comment;
import com.quduquxie.model.CommentReply;

/**
 * Created on 17/3/8.
 * Created by crazylei.
 */

public interface CommentItemListener {

    void commentLikeAction(Comment comment, int position);

    void commentDislikeAction(Comment comment, int position);

    void showReviewWriteView();

    void showReviewReplyView(Comment comment);

    void showCommentReplyView(Comment comment, CommentReply commentReply);

    void clickedReview(Comment comment, View view, int position);

    void clickedCommentReply(Comment comment, CommentReply commentReply, View view, int position);

    void reportComment(Comment comment);

    void reportCommentReply(Comment comment, CommentReply commentReply);

    void deleteComment(Comment comment, int position);

    void deleteCommentReply(Comment comment, CommentReply commentReply, int position);

    void startLoginActivity();

    void startCatalogActivity();

    void startCommentDetailsActivity(String id);

    void startCommentListActivity();

    void hideCommentPopupWindow();

    void hideReplyPopupWindow();
}