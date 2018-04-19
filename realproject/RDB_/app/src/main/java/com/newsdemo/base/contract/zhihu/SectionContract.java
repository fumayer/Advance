package com.newsdemo.base.contract.zhihu;

import com.newsdemo.base.BasePresenter;
import com.newsdemo.base.BaseView;
import com.newsdemo.model.bean.SectionListBean;

/**
 * Created by jianqiang.hu on 2017/5/24.
 */

public interface SectionContract {
    interface View extends BaseView {
        void showContent(SectionListBean sectionListBean);
    }

    interface Presenter extends BasePresenter<View> {
        void getSectionData();
    }
}
