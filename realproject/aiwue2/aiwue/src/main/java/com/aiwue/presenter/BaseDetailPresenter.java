package com.aiwue.presenter;

import com.aiwue.base.BasePresenter;
import com.aiwue.model.Comment;
import com.aiwue.okhttp.AiwueClient;
import com.aiwue.okhttp.SubscriberCallBack;
import com.aiwue.iview.IBaseDetailView;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/11/24 0024.
 */

public class BaseDetailPresenter<P extends  IBaseDetailView> extends BasePresenter<P> {
    public BaseDetailPresenter(P mvpView) {
        super(mvpView);
    }

    public void getCommentList(Integer contentType, Integer contentId, int page, int size) {
        AiwueClient.getCommentList(null, null, Schedulers.io(), AndroidSchedulers.mainThread(), contentType,contentId,page,size, new SubscriberCallBack<List<Comment>>() {
            @Override
            protected void onSuccess(List<Comment> response) {
                mvpView.onGetCommentListSuccess(true, null, response);
            }
            @Override
            protected void onError(String err) {
                mvpView.onGetCommentListSuccess(false, err, null);
            }
        });
    }
}
