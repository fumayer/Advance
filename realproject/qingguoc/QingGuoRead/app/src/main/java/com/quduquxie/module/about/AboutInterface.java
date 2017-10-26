package com.quduquxie.module.about;

import com.quduquxie.BasePresenter;
import com.quduquxie.BaseView;

/**
 * Created on 16/11/25.
 * Created by crazylei.
 */

public interface AboutInterface {

    interface Presenter extends BasePresenter {

        void initData();
    }

    interface View extends BaseView<Presenter> {

        void initView();

        void initVersion(String message);
    }
}
