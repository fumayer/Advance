package com.quduquxie.function.comment.list.util;

import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.quduquxie.R;
import com.quduquxie.db.UserDao;
import com.quduquxie.model.Comment;
import com.quduquxie.model.CommentReply;
import com.quduquxie.model.CommentUser;
import com.quduquxie.module.comment.listener.CommentItemListener;
import com.quduquxie.module.comment.listener.UserClickedListener;
import com.quduquxie.module.comment.util.RenderUtil;

import java.util.List;

/**
 * Created on 17/4/18.
 * Created by crazylei.
 */

public class CommentReplyHelper {

    private ViewGroup viewGroup;
    private Comment comment;
    private List<CommentReply> commentReplies;

    private CommentItemListener commentItemListener;

    public CommentReplyHelper(View view, Comment comment, CommentItemListener commentItemListener) {
        this.viewGroup = (ViewGroup) view;
        this.comment = comment;
        this.commentReplies = comment.replies;
        this.commentItemListener = commentItemListener;
    }

    public void refreshView() {
        if (this.commentReplies != null) {

            int size = commentReplies.size();

            for (int i = 0; i < 4; i++) {
                if (i < size) {
                    refreshItem(this.commentReplies.get(i), (ViewGroup) viewGroup.getChildAt(i), i);
                } else {
                    setViewState(viewGroup.getChildAt(i));
                }
            }

            if (commentReplies.size() > 4) {
                refreshItem(viewGroup.getChildAt(4));
            } else {
                setViewState(viewGroup.getChildAt(4));
            }
        }
    }

    private void refreshItem(CommentReply commentReply, ViewGroup viewGroup, int i) {
        TextView comment_reply_content = (TextView) viewGroup.findViewById(R.id.comment_reply_content);

        comment_reply_content.setClickable(true);
        comment_reply_content.setMovementMethod(LinkMovementMethod.getInstance());

        String content = commentReply.sender.name + "：" + "@" + commentReply.receiver.name + "  " + commentReply.content;

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(content);
        RenderUtil.renderUser(content, commentReply.sender, commentReply.receiver, spannableStringBuilder, new UserClickedListener() {
            @Override
            public void onClickedUserSpan(CommentUser commentUser) {
                Logger.d("View OnSpanClick");
            }
        });
        comment_reply_content.setText(spannableStringBuilder);

        comment_reply_content.setTag(R.id.click_object, commentReply);
        comment_reply_content.setTag(R.id.click_position, i);
        comment_reply_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserDao.checkUserLogin()) {
                    int clickedPosition = (int) view.getTag(R.id.click_position);
                    CommentReply clickedCommentReply = (CommentReply) view.getTag(R.id.click_object);
                    if (commentItemListener != null && comment != null && clickedCommentReply != null && clickedCommentReply.sender != null) {
                        commentItemListener.clickedCommentReply(comment, clickedCommentReply, view, clickedPosition);
                    }
                } else {
                    if (commentItemListener != null) {
                        commentItemListener.startLoginActivity();
                    }
                }
            }
        });
        viewGroup.setVisibility(View.VISIBLE);
    }

    private void setViewState(View view) {
        view.setVisibility(View.GONE);
    }

    private void refreshItem(View view) {
        TextView comment_reply_spread = (TextView) view.findViewById(R.id.comment_reply_spread);

        comment_reply_spread.setTag(R.id.click_object, comment);
        comment_reply_spread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comment clickedComment = (Comment) view.getTag(R.id.click_object);
                if (commentItemListener != null && clickedComment != null && !TextUtils.isEmpty(clickedComment.id)) {
                    commentItemListener.startCommentDetailsActivity(clickedComment.id);
                }
            }
        });
        view.setVisibility(View.VISIBLE);
    }

    public View getView() {
        return viewGroup;
    }
}