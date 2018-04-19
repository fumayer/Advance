package com.newsdemo.presenter.gank;

import com.newsdemo.base.RxPresent;
import com.newsdemo.base.contract.gank.GirlConstarct;
import com.newsdemo.model.DataManager;
import com.newsdemo.model.bean.GankItemBean;
import com.newsdemo.model.http.response.GankHttpResponse;
import com.newsdemo.util.RxUtil;
import com.newsdemo.widget.CommonSubscriber;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by jianqiang.hu on 2017/5/25.
 */

public class GirlPresenter extends RxPresent<GirlConstarct.View> implements GirlConstarct.Presenter {
    private DataManager mDataManager;

    public static final int NUM_OF_PAGE=20;

    private int currentPage=2;
    @Inject
    public GirlPresenter(DataManager mDataManager){
        this.mDataManager=mDataManager;
    }

    @Override
    public void getGirlData() {
        currentPage=1;
        addSubscribe(mDataManager.fetchGirlList(NUM_OF_PAGE,currentPage)
                    .compose(RxUtil.<GankHttpResponse<List<GankItemBean>>>rxSchedulerHelper())
                    .compose(RxUtil.<List<GankItemBean>>handleResult())
                    .subscribeWith(new CommonSubscriber<List<GankItemBean>>(mView) {
                        @Override
                        public void onNext(List<GankItemBean> gankItemBeen) {
                            mView.showContent(gankItemBeen);
                        }
                    })
        );
    }

    @Override
    public void getMoreGirlData() {
        addSubscribe(mDataManager.fetchGirlList(NUM_OF_PAGE,++currentPage)
                    .compose(RxUtil.<GankHttpResponse<List<GankItemBean>>>rxSchedulerHelper())
                    .compose(RxUtil.<List<GankItemBean>>handleResult())
                    .subscribeWith(new CommonSubscriber<List<GankItemBean>>(mView) {
                        @Override
                        public void onNext(List<GankItemBean> gankItemBeen) {
                            mView.showMoreContent(gankItemBeen);
                        }
                    })

        );
    }
}
