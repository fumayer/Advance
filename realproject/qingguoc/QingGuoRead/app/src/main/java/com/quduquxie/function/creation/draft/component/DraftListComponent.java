package com.quduquxie.function.creation.draft.component;

import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.ActivityScope;
import com.quduquxie.function.creation.draft.module.DraftListModule;
import com.quduquxie.function.creation.draft.presenter.DraftListPresenter;
import com.quduquxie.function.creation.draft.view.DraftListActivity;

import dagger.Component;

/**
 * Created on 17/4/10.
 * Created by crazylei.
 */

@ActivityScope
@Component(modules = DraftListModule.class, dependencies = ApplicationComponent.class)
public interface DraftListComponent {

    DraftListActivity inject(DraftListActivity draftListActivity);

    DraftListPresenter presenter();
}