package com.quduquxie.base.module.catalog.component;

import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.ActivityScope;
import com.quduquxie.base.module.catalog.module.CatalogModule;
import com.quduquxie.base.module.catalog.presenter.CatalogPresenter;
import com.quduquxie.base.module.catalog.view.CatalogActivity;

import dagger.Component;

/**
 * Created on 17/7/27.
 * Created by crazylei.
 */

@ActivityScope
@Component(modules = CatalogModule.class, dependencies = ApplicationComponent.class)
public interface CatalogComponent {

    CatalogActivity inject(CatalogActivity catalogActivity);

    CatalogPresenter presenter();
}