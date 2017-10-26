package com.quduquxie.base.module.catalog.module;

import com.quduquxie.base.ActivityScope;
import com.quduquxie.base.module.catalog.presenter.CatalogPresenter;
import com.quduquxie.base.module.catalog.view.CatalogActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 17/7/27.
 * Created by crazylei.
 */

@Module
@ActivityScope
public class CatalogModule {

    private CatalogActivity catalogActivity;

    public CatalogModule(CatalogActivity catalogActivity) {
        this.catalogActivity = catalogActivity;
    }

    @Provides
    @ActivityScope
    CatalogActivity provideCatalogActivity() {
        return catalogActivity;
    }

    @Provides
    @ActivityScope
    CatalogPresenter provideCatalogPresenter() {
        return new CatalogPresenter(catalogActivity);
    }
}