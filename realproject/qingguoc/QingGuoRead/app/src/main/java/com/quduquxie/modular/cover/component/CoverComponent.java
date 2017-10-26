package com.quduquxie.modular.cover.component;

import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.ActivityScope;
import com.quduquxie.modular.cover.module.CoverModule;
import com.quduquxie.modular.cover.presenter.CoverPresenter;
import com.quduquxie.modular.cover.view.CoverActivity;

import dagger.Component;

/**
 * Created on 17/4/13.
 * Created by crazylei.
 */

@ActivityScope
@Component(modules = CoverModule.class, dependencies = ApplicationComponent.class)
public interface CoverComponent {

    CoverActivity inject(CoverActivity coverActivity);

    CoverPresenter presenter();
}