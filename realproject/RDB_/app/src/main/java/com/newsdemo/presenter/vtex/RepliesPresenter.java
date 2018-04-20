package com.newsdemo.presenter.vtex;

import com.newsdemo.base.RxPresent;
import com.newsdemo.base.contract.vtex.RepliesContract;
import com.newsdemo.model.DataManager;
import com.newsdemo.model.bean.NodeListBean;
import com.newsdemo.model.bean.RealmLikeBean;
import com.newsdemo.model.bean.RepliesListBean;
import com.newsdemo.util.RxUtil;
import com.newsdemo.widget.CommonSubscriber;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by jianqiang.hu on 2017/5/31.
 */

public class RepliesPresenter extends RxPresent<RepliesContract.View> implements RepliesContract.Presenter {

    private  DataManager mDataManager;

    @Inject
    public RepliesPresenter(DataManager mDataManager){
        this.mDataManager=mDataManager;
    }



    @Override
    public void getContent(String topic_id) {
        addSubscribe(mDataManager.fetchRepliesList(topic_id)
                    .compose(RxUtil.<List<RepliesListBean>>rxSchedulerHelper())
                    .subscribeWith(new CommonSubscriber<List<RepliesListBean>>(mView) {
                        @Override
                        public void onNext(List<RepliesListBean> repliesListBean) {
                            mView.showContent(repliesListBean);
                        }
                    })
        );
    }

    @Override
    public void getTopInfo(String topic_id) {
        addSubscribe(mDataManager.fetchTopicInfo(topic_id)
                    .compose(RxUtil.<List<NodeListBean>>rxSchedulerHelper())
                    .filter(new Predicate<List<NodeListBean>>() {
                        @Override
                        public boolean test(@NonNull List<NodeListBean> nodeListBeen) throws Exception {
                            return nodeListBeen.size()>0;
                        }
                    })
                .map(new Function<List<NodeListBean>, NodeListBean>() {
                    @Override
                    public NodeListBean apply(@NonNull List<NodeListBean> nodeListBeen) throws Exception {
                        return nodeListBeen.get(0);
                    }
                }).subscribeWith(new CommonSubscriber<NodeListBean>(mView) {
                    @Override
                    public void onNext(NodeListBean nodeListBean) {
                        mView.showTopInfo(nodeListBean);
                    }
                })
        );
    }

    @Override
    public void insert(RealmLikeBean bean) {
        mDataManager.insertLikeBean(bean);
    }

    @Override
    public void delete(String id) {
        mDataManager.deleteLikeBean(id);
    }

    @Override
    public boolean query(String id) {
        return mDataManager.queryLikeId(id);
    }
}
