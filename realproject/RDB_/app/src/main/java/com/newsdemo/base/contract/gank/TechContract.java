package com.newsdemo.base.contract.gank;

import com.newsdemo.base.BasePresenter;
import com.newsdemo.base.BaseView;
import com.newsdemo.model.bean.GankItemBean;

import java.util.List;

/**
 * Created by jianqiang.hu on 2017/5/25.
 */

public interface TechContract {
    interface View extends BaseView{
        void showContent(List<GankItemBean> mList);
        void showMoreContent(List<GankItemBean> mList);
        void showGirlImage(String url,String copyright);
    }

    interface Presenter extends BasePresenter<View>{
        void getGankData(String tech,int type);
        void getMoreGankData(String tech);
        void getGirlImage();
    }

}
