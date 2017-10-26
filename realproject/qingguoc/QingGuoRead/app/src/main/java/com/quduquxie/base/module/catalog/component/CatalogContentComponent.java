package com.quduquxie.base.module.catalog.component;

import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.FragmentScope;
import com.quduquxie.base.module.catalog.module.CatalogContentModule;
import com.quduquxie.base.module.catalog.presenter.CatalogContentPresenter;
import com.quduquxie.base.module.catalog.view.fragment.CatalogContentFragment;

import dagger.Component;

/**
 * Created on 17/7/27.
 * Created by crazylei.
 */

@FragmentScope
@Component(modules = CatalogContentModule.class, dependencies = ApplicationComponent.class)
public interface CatalogContentComponent {

    CatalogContentFragment inject(CatalogContentFragment catalogContentFragment);

    CatalogContentPresenter presenter();

}