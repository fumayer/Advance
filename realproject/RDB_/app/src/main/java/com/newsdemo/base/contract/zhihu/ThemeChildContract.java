package com.newsdemo.base.contract.zhihu;

import com.newsdemo.base.BasePresenter;
import com.newsdemo.base.BaseView;
import com.newsdemo.model.bean.ThemeChildListBean;

/**
 * Created by jianqiang.hu on 2017/5/24.
 */

public interface ThemeChildContract {
    interface  View extends BaseView{
        void showContent(ThemeChildListBean themeChildListBean);
    }

    interface Presenter extends BasePresenter<View>{
        void getThemeChildData(int id);

        void insertReadToDB(int id);
    }
}
