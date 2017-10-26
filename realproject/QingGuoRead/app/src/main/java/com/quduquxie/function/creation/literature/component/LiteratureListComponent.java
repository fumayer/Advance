package com.quduquxie.function.creation.literature.component;

import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.ActivityScope;
import com.quduquxie.function.creation.literature.module.LiteratureListModule;
import com.quduquxie.function.creation.literature.presenter.LiteratureListPresenter;
import com.quduquxie.function.creation.literature.view.LiteratureListActivity;

import dagger.Component;

/**
 * Created on 17/4/10.
 * Created by crazylei.
 */

@ActivityScope
@Component(modules = LiteratureListModule.class, dependencies = ApplicationComponent.class)
public interface LiteratureListComponent {

    LiteratureListActivity inject(LiteratureListActivity literatureListActivity);

    LiteratureListPresenter presenter();
}
