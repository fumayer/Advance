package com.quduquxie.communal.help.comment;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.quduquxie.R;
import com.quduquxie.application.QuApplication;
import com.quduquxie.db.CommentLikeDao;
import com.quduquxie.db.UserDao;
import com.quduquxie.model.Comment;
import com.quduquxie.model.User;
import com.quduquxie.module.comment.listener.CommentItemListener;

import java.lang.ref.WeakReference;

/**
 * Created on 17/3/8.
 * Created by crazylei.
 */

public class CommentPopupWindowHelp {

    private View popupView;
    private WeakReference<Context> contextReference;
    private CommentItemListener commentItemListener;

    private UserDao userDao;
    private CommentLikeDao commentLikeDao;

    private Drawable commend_normal;
    private Drawable commend_selected;

    private TextView comment_option_commend;
    private TextView comment_option_copy;
    private TextView comment_option_reply;
    private TextView comment_option_report;
    private TextView comment_option_delete;

    public CommentPopupWindowHelp(Context context, View popupView, CommentItemListener commentItemListener) {
        this.popupView = popupView;
        this.contextReference = new WeakReference<>(context);
        this.commentItemListener = commentItemListener;

        this.commentLikeDao = CommentLikeDao.getInstance(context);

        this.commend_normal = contextReference.get().getResources().getDrawable(R.drawable.icon_popup_commend_normal);
        this.commend_selected = contextReference.get().getResources().getDrawable(R.drawable.icon_popup_commend_selected);

        initPopupWindowParameter();
    }

    private void initPopupWindowParameter() {
        comment_option_commend = (TextView) popupView.findViewById(R.id.comment_option_commend);
        comment_option_copy = (TextView) popupView.findViewById(R.id.comment_option_copy);
        comment_option_reply = (TextView) popupView.findViewById(R.id.comment_option_reply);
        comment_option_report = (TextView) popupView.findViewById(R.id.comment_option_report);
        comment_option_delete = (TextView) popupView.findViewById(R.id.comment_option_delete);
    }

    public void initPopupWindowView(final Comment comment, final int position) {
        if (!TextUtils.isEmpty(comment.id)) {
            if (commentLikeDao == null) {
                commentLikeDao = CommentLikeDao.getInstance(contextReference.get());
            }
            comment_option_commend.setVisibility(View.VISIBLE);
            if (commentLikeDao.isContainsComment(comment.id)) {
                comment_option_commend.setCompoundDrawablesWithIntrinsicBounds(null, commend_selected, null, null);
                comment_option_commend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (commentItemListener != null) {
                            commentItemListener.commentDislikeAction(comment, position);
                            commentItemListener.hideCommentPopupWindow();
                        }
                    }
                });
            } else {
                comment_option_commend.setCompoundDrawablesWithIntrinsicBounds(null, commend_normal, null, null);
                comment_option_commend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (commentItemListener != null) {
                            commentItemListener.commentLikeAction(comment, position);
                            commentItemListener.hideCommentPopupWindow();
                        }
                    }
                });
            }
        } else {
            comment_option_commend.setVisibility(View.GONE);
        }

        comment_option_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commentItemListener != null) {

                    ClipboardManager clipboardManager = (ClipboardManager) contextReference.get().getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboardManager.setText(comment.content);

                    commentItemListener.hideCommentPopupWindow();
                }
            }
        });

        comment_option_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentItemListener != null) {
                    commentItemListener.hideCommentPopupWindow();
                    commentItemListener.showReviewReplyView(comment);
                }
            }
        });

        if (userDao == null) {
            userDao = UserDao.getInstance(contextReference.get());
        }
        User user = userDao.getUser();

        if (user != null) {
            if (comment.sender != null && !TextUtils.isEmpty(comment.sender.id)) {
                if (user.id.equals(comment.sender.id)) {
                    comment_option_report.setVisibility(View.GONE);
                    comment_option_delete.setVisibility(View.VISIBLE);
                    comment_option_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (commentItemListener != null) {
                                commentItemListener.deleteComment(comment, position);
                                commentItemListener.hideCommentPopupWindow();
                            }
                        }
                    });
                } else {
                    comment_option_delete.setVisibility(View.GONE);
                    comment_option_report.setVisibility(View.VISIBLE);
                    comment_option_report.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (commentItemListener != null) {
                                commentItemListener.reportComment(comment);
                                commentItemListener.hideCommentPopupWindow();
                            }
                        }
                    });
                }
            } else {
                comment_option_delete.setVisibility(View.GONE);
                comment_option_report.setVisibility(View.GONE);
            }
        }
    }
}
