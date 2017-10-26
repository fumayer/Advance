package com.quduquxie.function.banner.presenter;

import com.quduquxie.function.banner.BannerInterface;
import com.quduquxie.function.banner.view.BannerActivity;

/**
 * Created on 17/3/24.
 * Created by crazylei.
 */

public class BannerPresenter implements BannerInterface.Presenter {

    private BannerActivity bannerActivity;

    public BannerPresenter(BannerActivity bannerActivity) {
        this.bannerActivity = bannerActivity;
    }

    @Override
    public void initParameter() {
        bannerActivity.initView();
    }
}
