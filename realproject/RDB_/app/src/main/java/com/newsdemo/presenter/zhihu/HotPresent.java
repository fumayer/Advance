package com.newsdemo.presenter.zhihu;

import com.newsdemo.base.RxPresent;
import com.newsdemo.base.contract.zhihu.HotContract;
import com.newsdemo.model.DataManager;
import com.newsdemo.model.bean.HotListBean;
import com.newsdemo.util.RxUtil;
import com.newsdemo.widget.CommonSubscriber;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by jianqiang.hu on 2017/5/23.
 */

public class HotPresent extends RxPresent<HotContract.View> implements HotContract.Present {

    private DataManager mDataManager;

    @Inject
    public HotPresent(DataManager mDataManager){
        this.mDataManager=mDataManager;
    }

    @Override
    public void getHotData() {
        addSubscribe(mDataManager.fetchHotListInfo()
                    .compose(RxUtil.<HotListBean>rxSchedulerHelper())
                    .map(new Function<HotListBean, HotListBean>() {
                        @Override
                        public HotListBean apply(@NonNull HotListBean hotListBean) throws Exception {
                            List<HotListBean.RecentBean> recent = hotListBean.getRecent();
                            for (HotListBean.RecentBean item:recent){
                                item.setReadState(mDataManager.queryNewsId(item.getNews_id()));
                            }
                            return hotListBean;
                        }
                    })
                    .subscribeWith(new CommonSubscriber<HotListBean>(mView) {
                        @Override
                        public void onNext(HotListBean hotListBean) {
                            mView.showContent(hotListBean);
                        }
                    })
        );
    }

    @Override
    public void inserReadToDB(int id) {
        mDataManager.insertNewsId(id);
    }
}
