package com.newsdemo.base.contract.zhihu;

import com.newsdemo.base.BasePresenter;
import com.newsdemo.base.BaseView;
import com.newsdemo.model.bean.HotListBean;

/**
 * Created by jianqiang.hu on 2017/5/23.
 */

public interface HotContract {
    interface View extends BaseView{
        void showContent(HotListBean hotListBean);
    }

    interface Present extends BasePresenter<View>{
        void getHotData();
        void inserReadToDB(int id);
    }
}
