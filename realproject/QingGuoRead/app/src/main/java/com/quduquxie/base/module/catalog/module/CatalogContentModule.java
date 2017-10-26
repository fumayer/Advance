package com.quduquxie.base.module.catalog.module;

import com.quduquxie.base.FragmentScope;
import com.quduquxie.base.module.catalog.presenter.CatalogContentPresenter;
import com.quduquxie.base.module.catalog.view.fragment.CatalogContentFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 17/7/27.
 * Created by crazylei.
 */

@Module
@FragmentScope
public class CatalogContentModule {

    private CatalogContentFragment catalogContentFragment;

    public CatalogContentModule(CatalogContentFragment catalogContentFragment) {
        this.catalogContentFragment = catalogContentFragment;
    }

    @Provides
    @FragmentScope
    CatalogContentFragment provideCatalogContentFragment() {
        return catalogContentFragment;
    }

    @Provides
    @FragmentScope
    CatalogContentPresenter provideCatalogContentPresenter() {
        return new CatalogContentPresenter(catalogContentFragment);
    }
}