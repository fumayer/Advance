package com.newsdemo.base.contract.vtex;

import com.newsdemo.base.BasePresenter;
import com.newsdemo.base.BaseView;
import com.newsdemo.model.bean.TopicListBean;

import java.util.List;

/**
 * Created by jianqiang.hu on 2017/5/27.
 */

public interface VtexContract {

    interface View extends BaseView{
        void showContent(List<TopicListBean> mList);
    }

    interface Presenter extends BasePresenter<View> {
        void getContent(String type);
    }
}
