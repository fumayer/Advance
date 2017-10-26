package com.quduquxie.function.banner.module;

import com.quduquxie.base.ActivityScope;
import com.quduquxie.function.banner.presenter.BannerPresenter;
import com.quduquxie.function.banner.view.BannerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 17/3/24.
 * Created by crazylei.
 */

@Module
public class BannerModule {
    private BannerActivity bannerActivity;

    public BannerModule(BannerActivity bannerActivity) {
        this.bannerActivity = bannerActivity;
    }

    @Provides
    @ActivityScope
    BannerActivity provideBannerActivity() {
        return bannerActivity;
    }

    @Provides
    @ActivityScope
    BannerPresenter providesBannerPresenter() {
        return new BannerPresenter(bannerActivity);
    }
}
