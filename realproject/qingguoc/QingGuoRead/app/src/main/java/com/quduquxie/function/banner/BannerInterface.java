package com.quduquxie.function.banner;

import com.quduquxie.function.BasePresenter;
import com.quduquxie.function.BaseView;

/**
 * Created on 17/3/24.
 * Created by crazylei.
 */

public interface BannerInterface {

    interface Presenter extends BasePresenter {

    }

    interface View extends BaseView<Presenter> {

        void initView();
    }
}
