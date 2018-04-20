package com.newsdemo.base.contract.gank;

import com.newsdemo.base.BasePresenter;
import com.newsdemo.base.BaseView;
import com.newsdemo.model.bean.GankItemBean;

import java.util.List;

/**
 * Created by jianqiang.hu on 2017/5/25.
 */

public interface GirlConstarct {
    interface View extends BaseView{
        void showContent(List<GankItemBean> list);
        void showMoreContent(List<GankItemBean> list);
    }

    interface Presenter extends BasePresenter<View>{
        void getGirlData();

        void getMoreGirlData();
    }





}
