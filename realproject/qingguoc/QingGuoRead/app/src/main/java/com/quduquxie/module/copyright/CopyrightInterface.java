package com.quduquxie.module.copyright;

import com.quduquxie.BasePresenter;
import com.quduquxie.BaseView;

/**
 * Created on 16/11/25.
 * Created by crazylei.
 */

public interface CopyrightInterface {

    interface Presenter extends BasePresenter {

    }

    interface View extends BaseView<Presenter> {

        void initView();
    }
}
