package com.quduquxie.function.comment.list.view;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.function.BaseActivity;
import com.quduquxie.function.comment.list.CommentInterface;
import com.quduquxie.function.comment.list.component.DaggerCommentComponent;
import com.quduquxie.function.comment.list.module.CommentModule;

/**
 * Created on 17/4/13.
 * Created by crazylei.
 */

public class CommentActivity extends BaseActivity implements CommentInterface.View {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void setActivityComponent(ApplicationComponent applicationComponent) {
        DaggerCommentComponent.builder()
                .applicationComponent(applicationComponent)
                .commentModule(new CommentModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(CommentInterface.Presenter presenter) {

    }

    @Override
    public void initParameter() {

    }
}
