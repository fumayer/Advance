package com.quduquxie.base.module.tabulation.component;

import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.ActivityScope;
import com.quduquxie.base.module.tabulation.module.TabulationModule;
import com.quduquxie.base.module.tabulation.presenter.TabulationPresenter;
import com.quduquxie.base.module.tabulation.view.TabulationActivity;

import dagger.Component;

/**
 * Created on 17/7/25.
 * Created by crazylei.
 */

@ActivityScope
@Component(modules = TabulationModule.class, dependencies = ApplicationComponent.class)
public interface TabulationComponent {

    TabulationActivity inject(TabulationActivity tabulationActivity);

    TabulationPresenter presenter();
}