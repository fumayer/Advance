package com.quduquxie.base.module.main.fragment.selected;

import com.quduquxie.base.BasePresenter;
import com.quduquxie.base.BaseView;
import com.quduquxie.base.module.main.activity.adapter.MainFragmentAdapter;

/**
 * Created on 17/7/17.
 * Created by crazylei.
 */

public interface SelectedInterface {

    interface Presenter extends BasePresenter {

        void initializeView(MainFragmentAdapter mainFragmentAdapter);
    }

    interface View extends BaseView {

        void refreshView();
    }
}