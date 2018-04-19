package com.newsdemo.presenter.main;

import com.newsdemo.base.RxPresent;
import com.newsdemo.base.contract.main.SettingContract;
import com.newsdemo.model.DataManager;
import com.newsdemo.model.bean.VersionBean;
import com.newsdemo.model.http.response.MyHttpResponse;
import com.newsdemo.util.RxUtil;
import com.newsdemo.widget.CommonSubscriber;

import javax.inject.Inject;

/**
 * Created by jianqiang.hu on 2017/5/31.
 */

public class SettingPresenter extends RxPresent<SettingContract.View> implements SettingContract.Presenter{

   private DataManager mDateManager;

    @Inject
    public SettingPresenter(DataManager mDataManager){
        this.mDateManager=mDataManager;
    }


    @Override
    public void checkVersion(final String currentVersion) {
        addSubscribe(mDateManager.fetchVersionInfo()
                    .compose(RxUtil.<MyHttpResponse<VersionBean>>rxSchedulerHelper())
                    .compose(RxUtil.<VersionBean>handleMyResult())
                    .subscribeWith(new CommonSubscriber<VersionBean>(mView) {
                        @Override
                        public void onNext(VersionBean versionBean) {
                            if (Integer.valueOf(currentVersion.replace(".",""))<Integer.valueOf(versionBean.getCode().replace(".",""))){
                                mView.showUpdateDialog(versionBean);
                            }else{
                                mView.showErrorMsg("已经是最新版本");
                            }
                        }
                    })
        );
    }

    @Override
    public void setNightModeState(boolean b) {
        mDateManager.setNightModeState(b);
    }

    @Override
    public void setNoImageState(boolean b) {
        mDateManager.setNoImageState(b);
    }

    @Override
    public void AutoCacheState(boolean b) {
        mDateManager.setAutoCacheState(b);
    }

    @Override
    public boolean getNightModeState() {
        return mDateManager.getNightModeState();
    }

    @Override
    public boolean getNoImageState() {
        return mDateManager.getNoImageState();
    }

    @Override
    public boolean getAutoCacheState() {
        return mDateManager.getAutoCacheState();
    }
}
