package com.quduquxie.function.creation.literature.module;

import com.quduquxie.base.ActivityScope;
import com.quduquxie.function.creation.literature.presenter.LiteratureListPresenter;
import com.quduquxie.function.creation.literature.view.LiteratureListActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 17/4/10.
 * Created by crazylei.
 */

@Module
public class LiteratureListModule {
    private LiteratureListActivity literatureListActivity;

    public LiteratureListModule(LiteratureListActivity literatureListActivity) {
        this.literatureListActivity = literatureListActivity;
    }

    @Provides
    @ActivityScope
    LiteratureListActivity provideLiteratureActivity() {
        return literatureListActivity;
    }

    @Provides
    @ActivityScope
    LiteratureListPresenter provideLiteraturePresenter() {
        return new LiteratureListPresenter(literatureListActivity);
    }
}
