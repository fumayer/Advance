package com.newsdemo.base.contract.wechat;

import com.newsdemo.base.BasePresenter;
import com.newsdemo.base.BaseView;
import com.newsdemo.model.bean.WXItemBean;

import java.util.List;

/**
 * Created by jianqiang.hu on 2017/5/24.
 */

public interface WechatContract {
    interface View extends BaseView{
        void showContent(List<WXItemBean> mList);
        void showMoreContent(List<WXItemBean> mList);
    }

    interface Present extends BasePresenter<View>{
        void getWeChatData();
        void getMoreWechatData();
    }
}
