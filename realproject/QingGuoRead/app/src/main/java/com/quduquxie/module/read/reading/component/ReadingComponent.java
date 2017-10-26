package com.quduquxie.module.read.reading.component;

import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.ActivityScope;
import com.quduquxie.module.read.reading.module.ReadingModule;
import com.quduquxie.module.read.reading.presenter.ReadingPresenter;
import com.quduquxie.module.read.reading.view.ReadingActivity;

import dagger.Component;

/**
 * Created on 17/8/1.
 * Created by crazylei.
 */

@ActivityScope
@Component(modules = ReadingModule.class, dependencies = ApplicationComponent.class)
public interface ReadingComponent {

    ReadingActivity inject(ReadingActivity readingActivity);

    ReadingPresenter presenter();
}
