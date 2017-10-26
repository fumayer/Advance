package com.quduquxie.module.comment.presenter;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.quduquxie.model.Comment;
import com.quduquxie.model.Review;
import com.quduquxie.model.ReviewResult;
import com.quduquxie.module.comment.ReceivedRepliesInterface;
import com.quduquxie.retrofit.DataRequestService;
import com.quduquxie.retrofit.ServiceGenerator;
import com.quduquxie.retrofit.model.CommunalResult;
import com.quduquxie.widget.LoadingPage;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created on 17/2/28.
 * Created by crazylei.
 */

public class ReceivedRepliesPresenter implements ReceivedRepliesInterface.Presenter {

    private ReceivedRepliesInterface.View receivedCommentView;

    private LoadingPage loadingPage;

    private boolean loadingMoreState = true;

    public ReceivedRepliesPresenter(@NonNull ReceivedRepliesInterface.View receivedCommentView) {
        this.receivedCommentView = receivedCommentView;
    }

    @Override
    public void init() {

    }

    @Override
    public void initParameter(int page) {
        receivedCommentView.initView();
        loadReceivedReplies(page);
    }

    @Override
    public void loadReceivedReplies(int page) {
        receivedCommentView.showLoadingPage();

        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
        dataRequestService.loadReceivedComments(0, 2, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<CommunalResult<ReviewResult<Review>>>() {
                    @Override
                    public void onNext(CommunalResult<ReviewResult<Review>> receivedCommentResult) {
                        Logger.d("LoadReceivedComments onNext");
                        if (receivedCommentResult != null) {
                            if (receivedCommentResult.getCode() == 0 && receivedCommentResult.getModel() != null && receivedCommentResult.getModel().actions != null && receivedCommentResult.getModel().actions.size() > 0) {

                                receivedCommentView.setReceivedReplies(receivedCommentResult.getModel().actions);

                                if (receivedCommentResult.getModel().actions.size() >= 10) {
                                    loadingMoreState = true;
                                } else {
                                    receivedCommentView.setListEnd();
                                    loadingMoreState = false;
                                }

                            } else {
                                loadingMoreState = false;
                                receivedCommentView.setReceivedReplies(null);
                            }
                        } else {
                            loadingMoreState = false;
                            receivedCommentView.setReceivedReplies(null);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("LoadReceivedComments onError: " + throwable.toString());
                        receivedCommentView.showLoadingError();
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("LoadReceivedComments onComplete");
                        receivedCommentView.hideLoadingPage();
                    }
                });
    }

    @Override
    public void loadReceivedRepliesMore(int page) {
        receivedCommentView.setRefreshViewState(true);
        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
        dataRequestService.loadReceivedComments(0, 2, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<CommunalResult<ReviewResult<Review>>>() {
                    @Override
                    public void onNext(CommunalResult<ReviewResult<Review>> receivedCommentResult) {
                        Logger.d("LoadReceivedComments onNext");
                        if (receivedCommentResult != null) {
                            if (receivedCommentResult.getCode() == 0 && receivedCommentResult.getModel() != null && receivedCommentResult.getModel().actions != null && receivedCommentResult.getModel().actions.size() > 0) {

                                receivedCommentView.setReceivedRepliesMore(receivedCommentResult.getModel().actions);

                                if (receivedCommentResult.getModel().actions.size() >= 10) {
                                    loadingMoreState = true;
                                } else {
                                    loadingMoreState = false;
                                    receivedCommentView.setListEnd();
                                }

                            } else {
                                loadingMoreState = false;
                                receivedCommentView.setListEnd();
                                receivedCommentView.setReceivedRepliesMore(null);
                            }
                        } else {
                            loadingMoreState = false;
                            receivedCommentView.setListEnd();
                            receivedCommentView.setReceivedRepliesMore(null);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("LoadReceivedComments onError: " + throwable.toString());
                        loadingMoreState = false;
                        receivedCommentView.setRefreshViewState(false);
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("LoadReceivedComments onComplete");
                        receivedCommentView.setRefreshViewState(false);
                    }
                });
    }

    @Override
    public boolean isLoadingMoreState() {
        return loadingMoreState;
    }

    @Override
    public void setLoadingPage(LoadingPage loadingPage) {
        this.loadingPage = loadingPage;
    }

    @Override
    public void publishCommentReply(Review review, String content) {
        receivedCommentView.hideCommentActionDialog();
        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
        dataRequestService.publishCommentReply(review.book.id, review.comments.id, review.sender.id, content)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<CommunalResult>() {
                    @Override
                    public void onNext(CommunalResult communalResult) {
                        Logger.d("PublishCommentReply onNext");
                        if (communalResult != null) {
                            if (communalResult.getCode() == 0) {
                                receivedCommentView.showToast("回复成功！");
                            } else {
                                if (!TextUtils.isEmpty(communalResult.getMessage())) {
                                    receivedCommentView.showToast(communalResult.getMessage());
                                } else {
                                    receivedCommentView.showToast("请求失败！");
                                }
                            }
                        } else {
                            receivedCommentView.showToast("请求发送失败，请您检查网络设置！");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("PublishCommentReply onError: " + throwable.toString());
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("PublishCommentReply onComplete");
                    }
                });
    }
}
