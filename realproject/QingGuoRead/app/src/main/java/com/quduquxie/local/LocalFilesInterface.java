package com.quduquxie.local;

import com.quduquxie.BasePresenter;
import com.quduquxie.BaseView;
import com.quduquxie.local.adapter.LocalViewPagerAdapter;

public interface LocalFilesInterface {

    interface Presenter extends BasePresenter {
        void initParameter(LocalViewPagerAdapter localViewPagerAdapter);
    }

    interface View extends BaseView<Presenter> {

    }
}
