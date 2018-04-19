package com.newsdemo.presenter.zhihu;

import com.newsdemo.base.RxPresent;
import com.newsdemo.base.contract.zhihu.SectionContract;
import com.newsdemo.model.DataManager;
import com.newsdemo.model.bean.SectionListBean;
import com.newsdemo.util.RxUtil;
import com.newsdemo.widget.CommonSubscriber;

import javax.inject.Inject;

/**
 * Created by jianqiang.hu on 2017/5/24.
 */

public class SectionPresent extends RxPresent<SectionContract.View> implements SectionContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public SectionPresent(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getSectionData() {
        addSubscribe(mDataManager.fetchSectionListInfo()
                .compose(RxUtil.<SectionListBean>rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<SectionListBean>(mView) {
                    @Override
                    public void onNext(SectionListBean sectionListBean) {
                        mView.showContent(sectionListBean);
                    }
                })
        );
    }
}
