package com.quduquxie.function.banner.component;

import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.ActivityScope;
import com.quduquxie.function.banner.module.BannerModule;
import com.quduquxie.function.banner.presenter.BannerPresenter;
import com.quduquxie.function.banner.view.BannerActivity;

import dagger.Component;

/**
 * Created on 17/3/24.
 * Created by crazylei.
 */

@ActivityScope
@Component(modules = BannerModule.class, dependencies = ApplicationComponent.class)
public interface BannerComponent {

    BannerActivity inject(BannerActivity bannerActivity);

    BannerPresenter presenter();
}
