package com.quduquxie.function.creation.create.component;

import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.ActivityScope;
import com.quduquxie.function.creation.create.module.LiteratureCreateModule;
import com.quduquxie.function.creation.create.presenter.LiteratureCreatePresenter;
import com.quduquxie.function.creation.create.view.LiteratureCreateActivity;

import dagger.Component;

/**
 * Created on 17/4/10.
 * Created by crazylei.
 */

@ActivityScope
@Component(modules = LiteratureCreateModule.class, dependencies = ApplicationComponent.class)
public interface LiteratureCreateComponent {

    LiteratureCreateActivity inject(LiteratureCreateActivity literatureCreateActivity);

    LiteratureCreatePresenter presenter();
}
