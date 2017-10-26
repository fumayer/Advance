package com.quduquxie.function.comment.list.presenter;

import com.quduquxie.function.comment.list.CommentInterface;
import com.quduquxie.function.comment.list.view.CommentActivity;

/**
 * Created on 17/4/13.
 * Created by crazylei.
 */

public class CommentPresenter implements CommentInterface.Presenter {

    private CommentActivity commentActivity;

    public CommentPresenter(CommentActivity commentActivity) {
        this.commentActivity = commentActivity;
    }

    @Override
    public void initParameter() {

    }
}
