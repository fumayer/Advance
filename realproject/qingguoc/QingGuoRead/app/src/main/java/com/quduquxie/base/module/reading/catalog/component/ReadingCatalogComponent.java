package com.quduquxie.base.module.reading.catalog.component;

import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.ActivityScope;
import com.quduquxie.base.module.reading.catalog.module.ReadingCatalogModule;
import com.quduquxie.base.module.reading.catalog.presenter.ReadingCatalogPresenter;
import com.quduquxie.base.module.reading.catalog.view.ReadingCatalogActivity;

import dagger.Component;

/**
 * Created on 17/7/27.
 * Created by crazylei.
 */

@ActivityScope
@Component(modules = ReadingCatalogModule.class, dependencies = ApplicationComponent.class)
public interface ReadingCatalogComponent {

    ReadingCatalogActivity inject(ReadingCatalogActivity readingCatalogActivity);

    ReadingCatalogPresenter presenter();
}
