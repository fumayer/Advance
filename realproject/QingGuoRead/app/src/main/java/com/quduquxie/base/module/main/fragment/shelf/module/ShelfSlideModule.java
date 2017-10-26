package com.quduquxie.base.module.main.fragment.shelf.module;

import com.quduquxie.base.FragmentScope;
import com.quduquxie.base.module.main.fragment.shelf.presenter.ShelfSlidePresenter;
import com.quduquxie.base.module.main.fragment.shelf.view.ShelfSlideFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 17/7/19.
 * Created by crazylei.
 */

@Module
@FragmentScope
public class ShelfSlideModule {

    private ShelfSlideFragment shelfSlideFragment;

    public ShelfSlideModule(ShelfSlideFragment shelfSlideFragment) {
        this.shelfSlideFragment = shelfSlideFragment;
    }

    @Provides
    @FragmentScope
    ShelfSlideFragment provideShelfSlideFragment() {
        return shelfSlideFragment;
    }

    @Provides
    @FragmentScope
    ShelfSlidePresenter provideShelfSlidePresenter() {
        return new ShelfSlidePresenter(shelfSlideFragment);
    }
}