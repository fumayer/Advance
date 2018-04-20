package com.newsdemo.presenter.zhihu;

import com.newsdemo.base.RxPresent;
import com.newsdemo.base.contract.zhihu.SectionChildContract;
import com.newsdemo.model.DataManager;
import com.newsdemo.model.bean.SectionChildListBean;
import com.newsdemo.util.RxUtil;
import com.newsdemo.widget.CommonSubscriber;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Function;

/**
 * Created by jianqiang.hu on 2017/5/24.
 */

public class SectionChildPresent extends RxPresent<SectionChildContract.View> implements SectionChildContract.Presenter{
    private DataManager mDataManager;

    @Inject
    public SectionChildPresent(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getThemeChildData(int id) {
        addSubscribe(mDataManager.fetchSectionChildListInfo(id)
                .compose(RxUtil.<SectionChildListBean>rxSchedulerHelper())
                .map(new Function<SectionChildListBean, SectionChildListBean>() {
                    @Override
                    public SectionChildListBean apply(SectionChildListBean sectionChildListBean) {
                        List<SectionChildListBean.StoriesBean> list = sectionChildListBean.getStories();
                        for(SectionChildListBean.StoriesBean item : list) {
                            item.setReadState(mDataManager.queryNewsId(item.getId()));
                        }
                        return sectionChildListBean;
                    }
                })
                .subscribeWith(new CommonSubscriber<SectionChildListBean>(mView) {
                    @Override
                    public void onNext(SectionChildListBean sectionChildListBean) {
                        mView.showContent(sectionChildListBean);
                    }
                })
        );
    }

    @Override
    public void insertReadToDB(int id) {
        mDataManager.insertNewsId(id);
    }

}
