package com.newsdemo.presenter.vtex;

import com.newsdemo.base.RxPresent;
import com.newsdemo.base.contract.vtex.NodeContract;
import com.newsdemo.model.DataManager;
import com.newsdemo.model.bean.NodeBean;
import com.newsdemo.model.bean.NodeListBean;
import com.newsdemo.util.RxUtil;
import com.newsdemo.widget.CommonSubscriber;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by jianqiang.hu on 2017/5/31.
 */

public class NodePresenter extends RxPresent<NodeContract.View> implements NodeContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public NodePresenter(DataManager mDataManager){
        this.mDataManager=mDataManager;
    }



    @Override
    public void getContent(String node_name) {
        addSubscribe(mDataManager.fetchTopicList(node_name)
                    .compose(RxUtil.<List<NodeListBean>>rxSchedulerHelper())
                    .subscribeWith(new CommonSubscriber<List<NodeListBean>>(mView) {
                        @Override
                        public void onNext(List<NodeListBean> nodeListBeen) {
                            mView.showContent(nodeListBeen);
                        }
                    })
        );
    }

    @Override
    public void getTopInfo(String node_name) {
        addSubscribe(mDataManager.fetchNodeInfo(node_name)
                    .compose(RxUtil.<NodeBean>rxSchedulerHelper())
                    .subscribeWith(new CommonSubscriber<NodeBean>(mView) {
                        @Override
                        public void onNext(NodeBean nodeBean) {
                            mView.showTopInfo(nodeBean);
                        }
                    })
        );
    }
}
