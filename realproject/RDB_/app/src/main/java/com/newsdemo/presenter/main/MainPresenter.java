package com.newsdemo.presenter.main;

import android.Manifest;

import com.newsdemo.base.RxPresent;
import com.newsdemo.base.contract.main.MainContract;
import com.newsdemo.component.RxBus;
import com.newsdemo.model.DataManager;
import com.newsdemo.model.bean.VersionBean;
import com.newsdemo.model.event.NightModeEvent;
import com.newsdemo.model.http.response.MyHttpResponse;
import com.newsdemo.util.RxUtil;
import com.newsdemo.widget.CommonSubscriber;
import com.tbruyelle.rxpermissions2.RxPermissions;

import javax.inject.Inject;


import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by jianqiang.hu on 2017/5/12.
 */

public class MainPresenter extends RxPresent<MainContract.View> implements MainContract.Presenter{


    private DataManager mDataManager;
    @Inject
    public MainPresenter(DataManager mDataManager){
        this.mDataManager=mDataManager;
    }

    @Override
    public void attachView(MainContract.View view) {
        super.attachView(view);
        registerEvent();//注册事件
    }

    /**
     * 注册night mode
     */
    private void registerEvent(){
        addSubscribe(RxBus.getDefault().toFlowable(NightModeEvent.class)
                    .compose(RxUtil.<NightModeEvent>rxSchedulerHelper())
                    .map(new Function<NightModeEvent, Boolean>() {
                        @Override
                        public Boolean apply(NightModeEvent nightModeEvent){
                            return nightModeEvent.isNightMode();
                        }
                    }).subscribeWith(new CommonSubscriber<Boolean>(mView, "切换模式失败ヽ(≧Д≦)ノ") {
                        @Override
                        public void onNext(Boolean aBoolean) {
                            mView.useNightMode(aBoolean);
                        }

                        @Override
                        public void onError(Throwable t) {
                            super.onError(t);
                            registerEvent();
                        }
                })
        );
    }


    @Override
    public void checkVersion(final String curerntVersion) {
        addSubscribe(mDataManager.fetchVersionInfo()
                        .compose(RxUtil.<MyHttpResponse<VersionBean>>rxSchedulerHelper())
                        .compose(RxUtil.<VersionBean>handleMyResult())
                        .filter(new Predicate<VersionBean>() {
                            @Override
                            public boolean test(@NonNull VersionBean versionBean) throws Exception {
                                return Integer.valueOf(curerntVersion.replace(".",""))<Integer.valueOf(versionBean.getCode().replace(".",""));
                            }
                        })
                        .map(new Function<VersionBean, String>() {
                            @Override
                            public String apply(@NonNull VersionBean versionBean) throws Exception {
                                StringBuilder content = new StringBuilder("版本号: v");
                                content.append(versionBean.getCode());
                                content.append("\r\n");
                                content.append("版本大小: ");
                                content.append(versionBean.getSize());
                                content.append("\r\n");
                                content.append("更新内容:\r\n");
                                content.append(versionBean.getDes().replace("\\r\\n","\r\n"));
                                return content.toString();
                            }
                        })
                        .subscribeWith(new CommonSubscriber<String>(mView) {
                            @Override
                            public void onNext(String s) {
                                mView.showUpdateDialog(s);
                            }
                        })
        );
    }

    @Override
    public void checkPermissions(RxPermissions rxPermissions) {
        addSubscribe(rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(@NonNull Boolean aBoolean) throws Exception {
                            if (aBoolean){
                                mView.startDownloadService();
                            }else{
                                mView.showErrorMsg("下载应用需要文件写入权限哦~");
                            }
                        }
                    })
        );
    }

    @Override
    public void setNightModeState(boolean b) {
        mDataManager.setNightModeState(b);
    }

    @Override
    public void setCurrentItem(int index) {
        mDataManager.setCurrentItem(index);
    }

    @Override
    public int getCurrentItem() {
        return mDataManager.getCurrentItem();
    }

    @Override
    public void setVersionPoint(boolean b) {
        mDataManager.setVersionPoint(b);
    }

    @Override
    public boolean getVersionPoint() {
        return mDataManager.getVersionPoint();
    }
}
