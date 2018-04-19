package com.newsdemo.presenter.zhihu;

import com.newsdemo.base.RxPresent;
import com.newsdemo.base.contract.zhihu.CommentConract;
import com.newsdemo.model.DataManager;
import com.newsdemo.model.bean.CommentBean;
import com.newsdemo.util.RxUtil;
import com.newsdemo.widget.CommonSubscriber;

import javax.inject.Inject;

/**
 * Created by jianqiang.hu on 2017/5/19.
 */

public class CommentPresent extends RxPresent<CommentConract.View> implements CommentConract.Presenter {

    public static final int SHORT_COMMENT=0;

    public static final int LONG_COMMENT=1;

    private DataManager mDataManager;

    @Inject
    public CommentPresent(DataManager mDataManager){
        this.mDataManager=mDataManager;
    }


    @Override
    public void getCommentData(int id, int commentKind) {
        if (commentKind==SHORT_COMMENT){
            addSubscribe(mDataManager.fetchShortCommentInfo(id)
                        .compose(RxUtil.<CommentBean>rxSchedulerHelper())
                        .subscribeWith(new CommonSubscriber<CommentBean>(mView) {
                            @Override
                            public void onNext(CommentBean commentBean) {
                                mView.stateMain();
                                mView.showContent(commentBean);
                            }
                        })
            );
        }else{
            addSubscribe(mDataManager.fetchLongCommentInfo(id)
                        .compose(RxUtil.<CommentBean>rxSchedulerHelper())
                        .subscribeWith(new CommonSubscriber<CommentBean>(mView) {
                            @Override
                            public void onNext(CommentBean commentBean) {
                                mView.stateMain();
                                mView.showContent(commentBean);
                            }
                        })
            );
        }
    }
}
