package com.newsdemo.presenter.zhihu;

import com.newsdemo.app.Constants;
import com.newsdemo.base.RxPresent;
import com.newsdemo.base.contract.zhihu.ZhihuDetailContract;
import com.newsdemo.model.DataManager;
import com.newsdemo.model.bean.DetailExtraBean;
import com.newsdemo.model.bean.RealmLikeBean;
import com.newsdemo.model.bean.ZhihuDetailBean;
import com.newsdemo.util.RxUtil;
import com.newsdemo.widget.CommonSubscriber;

import javax.inject.Inject;



/**
 * Created by jianqiang.hu on 2017/5/17.
 */

public class ZhihuDetailPresenter extends RxPresent<ZhihuDetailContract.View> implements ZhihuDetailContract.Presenter {
    private DataManager mDataManager;
    private ZhihuDetailBean mData;

    @Inject
    public ZhihuDetailPresenter(DataManager mDataManager){
        this.mDataManager=mDataManager;
    }

    @Override
    public void getDetailData(int id) {
        addSubscribe(mDataManager.fetchDetailInfo(id)
                    .compose(RxUtil.<ZhihuDetailBean>rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<ZhihuDetailBean>(mView) {
                    @Override
                    public void onNext(ZhihuDetailBean zhihuDetailBean) {
                        mData=zhihuDetailBean;
                        mView.showContent(zhihuDetailBean);
                    }
                })

        );
    }

    @Override
    public void getExtraData(int id) {
        addSubscribe(mDataManager.fetchDetailExtraInfo(id)
                    .compose(RxUtil.<DetailExtraBean>rxSchedulerHelper())
                    .subscribeWith(new CommonSubscriber<DetailExtraBean>(mView,"加载额外信息失败ヽ(≧Д≦)ノ"){
                        @Override
                        public void onNext(DetailExtraBean detailExtraBean) {
                            mView.showExtraInfo(detailExtraBean);
                        }
                    })
        );
    }

    @Override
    public void insertLikeData() {
        if (mData!=null){
            RealmLikeBean bean = new RealmLikeBean();
            bean.setId(String.valueOf(mData.getId()));
            bean.setImage(mData.getImage());
            bean.setTitle(mData.getTitle());
            bean.setType(Constants.TYPE_ZHIHU);
            bean.setTime(System.currentTimeMillis());
            mDataManager.insertLikeBean(bean);
        }else{
            mView.showErrorMsg("操作失败");
        }
    }

    @Override
    public void deleteLikeData() {
        if (mData!=null){
          mDataManager.deleteLikeBean(String.valueOf(mData.getId()));
        }else{
            mView.showErrorMsg("操作失败");
        }
    }

    @Override
    public void queryLikeData(int id) {
        mView.setLikeButtonState(mDataManager.queryLikeId(String.valueOf(id)));
    }

    @Override
    public boolean getNoImageState() {
        return mDataManager.getNoImageState();
    }

    @Override
    public boolean getAutoCacheState() {
        return mDataManager.getAutoCacheState();
    }
}
