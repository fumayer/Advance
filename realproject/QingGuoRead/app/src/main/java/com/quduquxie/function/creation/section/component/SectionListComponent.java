package com.quduquxie.function.creation.section.component;

import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.ActivityScope;
import com.quduquxie.function.creation.section.module.SectionListModule;
import com.quduquxie.function.creation.section.presenter.SectionListPresenter;
import com.quduquxie.function.creation.section.view.SectionListActivity;

import dagger.Component;

/**
 * Created on 17/4/10.
 * Created by crazylei.
 */

@ActivityScope
@Component(modules = SectionListModule.class, dependencies = ApplicationComponent.class)
public interface SectionListComponent {

    SectionListActivity inject(SectionListActivity sectionListActivity);

    SectionListPresenter presenter();
}
