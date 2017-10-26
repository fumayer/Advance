package com.quduquxie.base.module.billboard.module;

import com.quduquxie.base.ActivityScope;
import com.quduquxie.base.module.billboard.presenter.BillboardPresenter;
import com.quduquxie.base.module.billboard.view.BillboardActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 17/7/25.
 * Created by crazylei.
 */

@Module
@ActivityScope
public class BillboardModule {

    private BillboardActivity billboardActivity;

    public BillboardModule(BillboardActivity billboardActivity) {
        this.billboardActivity = billboardActivity;
    }

    @Provides
    @ActivityScope
    BillboardActivity provideBillboardActivity() {
        return billboardActivity;
    }

    @Provides
    @ActivityScope
    BillboardPresenter provideBillboardPresenter() {
        return new BillboardPresenter(billboardActivity);
    }
}