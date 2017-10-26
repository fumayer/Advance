package com.quduquxie.base.module.billboard;

import com.quduquxie.base.BasePresenter;
import com.quduquxie.base.BaseView;
import com.quduquxie.base.module.billboard.adapter.BillboardAdapter;

/**
 * Created on 17/7/25.
 * Created by crazylei.
 */

public interface BillboardInterface {

    interface View extends BaseView {

    }

    interface Presenter extends BasePresenter {

        void initializeParameter(BillboardAdapter billboardAdapter, String title, String uri, String date);

        String initializeDate(String uri);
    }
}