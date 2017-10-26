package com.quduquxie.base.module.main.fragment.mine.module;

import com.quduquxie.base.FragmentScope;
import com.quduquxie.base.module.main.fragment.mine.presenter.MinePresenter;
import com.quduquxie.base.module.main.fragment.mine.view.MineFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 17/7/17.
 * Created by crazylei.
 */

@Module
@FragmentScope
public class MineModule {

    private MineFragment mineFragment;

    public MineModule(MineFragment mineFragment) {
        this.mineFragment = mineFragment;
    }

    @Provides
    @FragmentScope
    MineFragment provideMineFragment() {
        return mineFragment;
    }

    @Provides
    @FragmentScope
    MinePresenter provideMinePresenter() {
        return new MinePresenter(mineFragment);
    }
}