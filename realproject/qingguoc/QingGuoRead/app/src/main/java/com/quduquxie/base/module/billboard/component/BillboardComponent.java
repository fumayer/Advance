package com.quduquxie.base.module.billboard.component;

import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.ActivityScope;
import com.quduquxie.base.module.billboard.module.BillboardModule;
import com.quduquxie.base.module.billboard.presenter.BillboardPresenter;
import com.quduquxie.base.module.billboard.view.BillboardActivity;

import dagger.Component;

/**
 * Created on 17/7/25.
 * Created by crazylei.
 */

@ActivityScope
@Component(modules = BillboardModule.class, dependencies = ApplicationComponent.class)
public interface BillboardComponent {

    BillboardActivity inject(BillboardActivity billboardActivity);

    BillboardPresenter presenter();
}