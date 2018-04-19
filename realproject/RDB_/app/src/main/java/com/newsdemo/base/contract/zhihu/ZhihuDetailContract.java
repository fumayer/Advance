package com.newsdemo.base.contract.zhihu;

import com.newsdemo.base.BasePresenter;
import com.newsdemo.base.BaseView;
import com.newsdemo.model.bean.DetailExtraBean;
import com.newsdemo.model.bean.ZhihuDetailBean;

/**
 * Created by jianqiang.hu on 2017/5/17.
 */

public interface ZhihuDetailContract {
    interface View extends BaseView{
        void showContent(ZhihuDetailBean zhihuDetailBean);

        void showExtraInfo(DetailExtraBean detailExtraBean);

        void setLikeButtonState(boolean isLike);
    }
    interface Presenter extends BasePresenter<View>{

        void getDetailData(int id);

        void getExtraData(int id);

        void insertLikeData();

        void deleteLikeData();

        void queryLikeData(int id);

        boolean getNoImageState();

        boolean getAutoCacheState();
    }
}
