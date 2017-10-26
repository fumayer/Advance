package com.quduquxie.function.comment.list.module;

import com.quduquxie.base.ActivityScope;
import com.quduquxie.function.comment.list.presenter.CommentPresenter;
import com.quduquxie.function.comment.list.view.CommentActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 17/4/13.
 * Created by crazylei.
 */

@Module
public class CommentModule {

    private CommentActivity commentActivity;

    public CommentModule(CommentActivity commentActivity) {
        this.commentActivity = commentActivity;
    }

    @Provides
    @ActivityScope
    CommentActivity provideCommentActivity() {
        return commentActivity;
    }

    @Provides
    @ActivityScope
    CommentPresenter provideCommentPresenter() {
        return new CommentPresenter(commentActivity);
    }
}
