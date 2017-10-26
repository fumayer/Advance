package com.quduquxie.base.module.main.fragment.shelf.module;

import com.quduquxie.base.FragmentScope;
import com.quduquxie.base.module.main.fragment.shelf.presenter.ShelfGridPresenter;
import com.quduquxie.base.module.main.fragment.shelf.view.ShelfGridFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 17/7/19.
 * Created by crazylei.
 */

@Module
@FragmentScope
public class ShelfGridModule {

    private ShelfGridFragment shelfGridFragment;

    public ShelfGridModule(ShelfGridFragment shelfGridFragment) {
        this.shelfGridFragment = shelfGridFragment;
    }

    @Provides
    @FragmentScope
    ShelfGridFragment provideShelfGridFragment() {
        return shelfGridFragment;
    }

    @Provides
    @FragmentScope
    ShelfGridPresenter provideShelfGridPresenter() {
        return new ShelfGridPresenter(shelfGridFragment);
    }
}