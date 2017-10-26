package com.quduquxie.base.module.main.fragment.library;

import com.quduquxie.base.BasePresenter;
import com.quduquxie.base.BaseView;
import com.quduquxie.base.module.main.activity.adapter.MainFragmentAdapter;

/**
 * Created on 17/7/17.
 * Created by crazylei.
 */

public interface LibraryInterface {

    interface Presenter extends BasePresenter {

        void initializeView(MainFragmentAdapter mainFragmentAdapter);
    }

    interface View extends BaseView {

        void refreshView();
    }
}