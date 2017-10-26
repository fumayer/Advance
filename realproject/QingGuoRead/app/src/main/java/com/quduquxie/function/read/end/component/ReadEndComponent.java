package com.quduquxie.function.read.end.component;

import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.ActivityScope;
import com.quduquxie.function.read.end.module.ReadEndModule;
import com.quduquxie.function.read.end.presenter.ReadEndPresenter;
import com.quduquxie.function.read.end.view.ReadEndActivity;

import dagger.Component;

/**
 * Created on 17/4/16.
 * Created by crazylei.
 */

@ActivityScope
@Component(modules = ReadEndModule.class, dependencies = ApplicationComponent.class)
public interface ReadEndComponent {

    ReadEndActivity inject(ReadEndActivity readEndActivity);

    ReadEndPresenter presenter();
}
