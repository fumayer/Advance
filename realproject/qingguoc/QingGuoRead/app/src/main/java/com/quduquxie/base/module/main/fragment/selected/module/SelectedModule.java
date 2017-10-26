package com.quduquxie.base.module.main.fragment.selected.module;

import com.quduquxie.base.FragmentScope;
import com.quduquxie.base.module.main.fragment.selected.presenter.SelectedPresenter;
import com.quduquxie.base.module.main.fragment.selected.view.SelectedFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 17/7/17.
 * Created by crazylei.
 */

@Module
@FragmentScope
public class SelectedModule {

    private SelectedFragment selectedFragment;

    public SelectedModule(SelectedFragment selectedFragment) {
        this.selectedFragment = selectedFragment;
    }

    @Provides
    @FragmentScope
    SelectedFragment provideSelectedFragment() {
        return selectedFragment;
    }

    @Provides
    @FragmentScope
    SelectedPresenter provideSelectedPresenter() {
        return new SelectedPresenter(selectedFragment);
    }
}