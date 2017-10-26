package com.quduquxie.base.module.main.fragment.content;

import com.quduquxie.base.BasePresenter;
import com.quduquxie.base.BaseView;
import com.quduquxie.base.bean.MainContent;

/**
 * Created on 17/7/20.
 * Created by crazylei.
 */

public interface ContentInterface {

    interface Presenter extends BasePresenter {

        void loadSelectedData(int type, String key);
    }

    interface View extends BaseView {

        void setMainContentData(MainContent mainContent);

        void showLoadingPage();

        void hideLoadingPage();

        void showLoadingError();

        void resetRefreshViewState(boolean state);
    }
}