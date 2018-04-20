package com.newsdemo.base.contract.zhihu;

import com.newsdemo.base.BasePresenter;
import com.newsdemo.base.BaseView;
import com.newsdemo.model.bean.ThemeListBean;

/**
 * Created by jianqiang.hu on 2017/5/23.
 */

public interface ThemeContract {


    interface View extends BaseView{
        void showContent(ThemeListBean themeListBean);
    }

    interface Present extends BasePresenter<View>{
        void getThemeData();
    }
}
