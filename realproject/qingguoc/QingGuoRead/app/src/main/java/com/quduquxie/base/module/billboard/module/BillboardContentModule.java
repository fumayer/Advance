package com.quduquxie.base.module.billboard.module;

import com.quduquxie.base.FragmentScope;
import com.quduquxie.base.module.billboard.presenter.BillboardContentPresenter;
import com.quduquxie.base.module.billboard.view.fragment.BillboardContentFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 17/7/25.
 * Created by crazylei.
 */

@Module
@FragmentScope
public class BillboardContentModule {

    private BillboardContentFragment billboardContentFragment;

    public BillboardContentModule(BillboardContentFragment billboardContentFragment) {
        this.billboardContentFragment = billboardContentFragment;
    }

    @Provides
    @FragmentScope
    BillboardContentFragment provideBillboardContentFragment() {
        return billboardContentFragment;
    }

    @Provides
    @FragmentScope
    BillboardContentPresenter provideBillboardContentPresenter() {
        return new BillboardContentPresenter(billboardContentFragment);
    }
}