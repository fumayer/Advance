package com.newsdemo.base.contract.zhihu;

import com.newsdemo.base.BasePresenter;
import com.newsdemo.base.BaseView;
import com.newsdemo.model.bean.CommentBean;

/**
 * Created by jianqiang.hu on 2017/5/19.
 */

public interface CommentConract {
    interface View extends BaseView{
        void showContent(CommentBean commentBean);
    }

    interface Presenter extends BasePresenter<View>{
        void getCommentData(int id,int commentKind);
    }
}
