package com.newsdemo.base.contract.main;

import com.newsdemo.base.BasePresenter;
import com.newsdemo.base.BaseView;
import com.newsdemo.model.bean.VersionBean;

/**
 * Created by jianqiang.hu on 2017/5/31.
 */

public interface SettingContract  {

    interface View extends BaseView{
        void showUpdateDialog(VersionBean bean);
    }

    interface  Presenter extends BasePresenter<View>{

        void checkVersion(String currentVersion);

        void setNightModeState(boolean b);

        void setNoImageState(boolean b);

        void AutoCacheState(boolean b);

        boolean getNightModeState();

        boolean getNoImageState();

        boolean getAutoCacheState();

    }

}



