package com.quduquxie.function.creation.create.module;

import com.quduquxie.base.ActivityScope;
import com.quduquxie.function.creation.create.presenter.LiteratureCreatePresenter;
import com.quduquxie.function.creation.create.view.LiteratureCreateActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 17/4/10.
 * Created by crazylei.
 */

@Module
public class LiteratureCreateModule {

    private LiteratureCreateActivity literatureCreateActivity;

    public LiteratureCreateModule(LiteratureCreateActivity literatureCreateActivity) {
        this.literatureCreateActivity = literatureCreateActivity;
    }

    @Provides
    @ActivityScope
    LiteratureCreateActivity provideLiteratureCreateActivity() {
        return literatureCreateActivity;
    }

    @Provides
    @ActivityScope
    LiteratureCreatePresenter provideLiteratrueCreatePresenter() {
        return new LiteratureCreatePresenter(literatureCreateActivity);
    }
}
