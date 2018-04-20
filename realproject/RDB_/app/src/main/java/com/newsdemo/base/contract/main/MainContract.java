package com.newsdemo.base.contract.main;

import com.newsdemo.base.BasePresenter;
import com.newsdemo.base.BaseView;
import com.tbruyelle.rxpermissions2.RxPermissions;

/**
 * Created by jianqiang.hu on 2017/5/12.
 */

public interface MainContract {
    interface View extends BaseView{
        void showUpdateDialog(String versionContent);

        void startDownloadService();
    }

    interface Presenter extends BasePresenter<View>{
        void checkVersion(String curerntVersion);

        void checkPermissions(RxPermissions rxPermissions);

        void setNightModeState(boolean  b);

        void setCurrentItem(int index);

        int getCurrentItem();

        void setVersionPoint(boolean b);

        boolean getVersionPoint();
    }
}
