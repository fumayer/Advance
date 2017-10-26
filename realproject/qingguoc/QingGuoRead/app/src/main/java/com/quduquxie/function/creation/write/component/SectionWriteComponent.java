package com.quduquxie.function.creation.write.component;

import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.ActivityScope;
import com.quduquxie.function.creation.write.module.SectionWriteModule;
import com.quduquxie.function.creation.write.presenter.SectionWritePresenter;
import com.quduquxie.function.creation.write.view.SectionWriteActivity;

import dagger.Component;

/**
 * Created on 17/4/10.
 * Created by crazylei.
 */

@ActivityScope
@Component(modules = SectionWriteModule.class, dependencies = ApplicationComponent.class)
public interface SectionWriteComponent {

    SectionWriteActivity inject(SectionWriteActivity sectionWriteActivity);

    SectionWritePresenter presenter();
}
