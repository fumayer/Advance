package com.quduquxie.module.comment.util;

import android.view.View;

import com.quduquxie.model.CommentUser;
import com.quduquxie.module.comment.listener.UserClickedListener;

/**
 * Created on 17/2/28.
 * Created by crazylei.
 */

public class UserClickableSpan extends BasicClickableSpan {

    private CommentUser commentUser;
    private UserClickedListener userClickedListener;

    public UserClickableSpan(CommentUser commentUser, UserClickedListener userClickedListener) {
        this.commentUser = commentUser;
        this.userClickedListener = userClickedListener;
    }

    @Override
    public void onClick(View widget) {
        if (userClickedListener != null) {
            userClickedListener.onClickedUserSpan(commentUser);
        }
    }
}
