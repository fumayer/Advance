package com.newsdemo.base.contract.gold;

import com.newsdemo.base.BasePresenter;
import com.newsdemo.base.BaseView;
import com.newsdemo.model.bean.GoldListBean;

import java.util.List;

/**
 * Created by jianqiang.hu on 2017/5/26.
 */

public interface GoldContract {
    interface View extends BaseView{
        void showContent(List<GoldListBean> goldListBeen);

        void showMoreContent(List<GoldListBean> goldListBeen,int start,int end);
    }

    interface Presenter extends BasePresenter<View>{
        void getGoldData(String type);

        void getModeGoldData();
    }
}
