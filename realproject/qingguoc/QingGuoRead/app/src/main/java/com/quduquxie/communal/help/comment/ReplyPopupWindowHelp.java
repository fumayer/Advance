package com.quduquxie.communal.help.comment;

import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.quduquxie.R;
import com.quduquxie.application.QuApplication;
import com.quduquxie.db.UserDao;
import com.quduquxie.model.Comment;
import com.quduquxie.model.CommentReply;
import com.quduquxie.model.User;
import com.quduquxie.module.comment.listener.CommentItemListener;

import java.lang.ref.WeakReference;

/**
 * Created on 17/3/8.
 * Created by crazylei.
 */

public class ReplyPopupWindowHelp {

    private View popupView;
    private WeakReference<Context> contextReference;
    private CommentItemListener commentItemListener;

    private UserDao userDao;

    private TextView comment_option_copy;
    private TextView comment_option_reply;
    private TextView comment_option_report;
    private TextView comment_option_delete;

    public ReplyPopupWindowHelp(Context context, View popupView, CommentItemListener commentItemListener) {
        this.popupView = popupView;
        this.contextReference = new WeakReference<>(context);
        this.commentItemListener = commentItemListener;

        initPopupWindowParameter();
    }

    private void initPopupWindowParameter() {
        comment_option_copy = (TextView) popupView.findViewById(R.id.comment_option_copy);

        comment_option_reply = (TextView) popupView.findViewById(R.id.comment_option_reply);

        comment_option_report = (TextView) popupView.findViewById(R.id.comment_option_report);

        comment_option_delete = (TextView) popupView.findViewById(R.id.comment_option_delete);

    }

    public void initPopupWindowView(final Comment comment, final CommentReply commentReply, final int position) {
        comment_option_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commentItemListener != null) {

                    ClipboardManager clipboardManager = (ClipboardManager) contextReference.get().getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboardManager.setText(commentReply.content);

                    commentItemListener.hideReplyPopupWindow();
                }
            }
        });

        comment_option_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commentItemListener != null) {
                    commentItemListener.hideReplyPopupWindow();
                    commentItemListener.showCommentReplyView(comment, commentReply);
                }
            }
        });

        if (userDao == null) {
            userDao = UserDao.getInstance(contextReference.get());
        }
        User user = userDao.getUser();

        if (user != null) {
            if (commentReply != null && commentReply.sender != null && !TextUtils.isEmpty(commentReply.sender.id)) {
                if (user.id.equals(commentReply.sender.id)) {
                    comment_option_report.setVisibility(View.GONE);
                    comment_option_delete.setVisibility(View.VISIBLE);
                    comment_option_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (commentItemListener != null) {
                                commentItemListener.deleteCommentReply(comment, commentReply, position);
                                commentItemListener.hideReplyPopupWindow();
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
                                commentItemListener.reportCommentReply(comment, commentReply);
                                commentItemListener.hideReplyPopupWindow();
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
