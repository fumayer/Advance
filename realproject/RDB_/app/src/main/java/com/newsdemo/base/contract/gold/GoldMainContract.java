package com.newsdemo.base.contract.gold;

import com.newsdemo.base.BasePresenter;
import com.newsdemo.base.BaseView;
import com.newsdemo.model.bean.GoldManagerBean;
import com.newsdemo.model.bean.GoldManagerItemBean;

import java.util.List;

/**
 * Created by jianqiang.hu on 2017/5/26.
 */

public interface GoldMainContract {
    interface View extends BaseView {
        void updateTab(List<GoldManagerItemBean> mList);

        void jumpToManager(GoldManagerBean mBean);
    }

    interface Presenter extends BasePresenter<View>{
         void initManagerList();

        void setManagerList();
    }
}
