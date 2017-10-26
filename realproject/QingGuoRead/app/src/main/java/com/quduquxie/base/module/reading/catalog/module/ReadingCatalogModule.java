package com.quduquxie.base.module.reading.catalog.module;

import com.quduquxie.base.ActivityScope;
import com.quduquxie.base.module.reading.catalog.presenter.ReadingCatalogPresenter;
import com.quduquxie.base.module.reading.catalog.view.ReadingCatalogActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 17/7/27.
 * Created by crazylei.
 */

@Module
@ActivityScope
public class ReadingCatalogModule {

    private ReadingCatalogActivity readingCatalogActivity;

    public ReadingCatalogModule(ReadingCatalogActivity readingCatalogActivity) {
        this.readingCatalogActivity = readingCatalogActivity;
    }

    @Provides
    @ActivityScope
    ReadingCatalogActivity provideReadingCatalogActivity() {
        return readingCatalogActivity;
    }

    @Provides
    @ActivityScope
    ReadingCatalogPresenter provideReadingCatalogPresenter() {
        return new ReadingCatalogPresenter(readingCatalogActivity);
    }
}