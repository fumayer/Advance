package com.newsdemo.base.contract.vtex;

import com.newsdemo.base.BasePresenter;
import com.newsdemo.base.BaseView;
import com.newsdemo.model.bean.NodeListBean;
import com.newsdemo.model.bean.RealmLikeBean;
import com.newsdemo.model.bean.RepliesListBean;

import java.util.List;

/**
 * Created by jianqiang.hu on 2017/5/31.
 */

public interface RepliesContract {

    interface View extends BaseView{

        void showContent(List<RepliesListBean> mList);

        void showTopInfo(NodeListBean mTopInfo);
    }

    interface Presenter extends BasePresenter<View>{
        void getContent(String topic_id);

        void getTopInfo(String topic_id);

        void insert(RealmLikeBean bean);

        void delete(String id);

        boolean query(String id);
    }


}
