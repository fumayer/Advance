package com.quduquxie.function.comment.list.component;

import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.ActivityScope;
import com.quduquxie.function.comment.list.module.CommentModule;
import com.quduquxie.function.comment.list.presenter.CommentPresenter;
import com.quduquxie.function.comment.list.view.CommentActivity;

import dagger.Component;

/**
 * Created on 17/4/13.
 * Created by crazylei.
 */

@ActivityScope
@Component(modules = CommentModule.class, dependencies = ApplicationComponent.class)
public interface CommentComponent {

    CommentActivity inject(CommentActivity commentActivity);

    CommentPresenter presenter();
}
