package com.newsdemo.presenter.zhihu;

import com.newsdemo.base.RxPresent;
import com.newsdemo.base.contract.zhihu.ThemeContract;
import com.newsdemo.model.DataManager;
import com.newsdemo.model.bean.ThemeListBean;
import com.newsdemo.util.RxUtil;
import com.newsdemo.widget.CommonSubscriber;

import javax.inject.Inject;

/**
 * Created by jianqiang.hu on 2017/5/23.
 */

public class ThemePresent extends RxPresent<ThemeContract.View> implements ThemeContract.Present{
    private DataManager mDataManager;

    @Inject
    public ThemePresent(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }


    @Override
    public void getThemeData() {
        addSubscribe(mDataManager.fetchDailyThemeListInfo()
                    .compose(RxUtil.<ThemeListBean>rxSchedulerHelper())
                    .subscribeWith(new CommonSubscriber<ThemeListBean>(mView) {
                        @Override
                        public void onNext(ThemeListBean themeListBean) {
                            mView.showContent(themeListBean);
                        }
                    })
        );
    }


}
