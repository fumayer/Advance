package com.quduquxie.module.comment.presenter;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.quduquxie.model.Comment;
import com.quduquxie.model.Review;
import com.quduquxie.model.ReviewResult;
import com.quduquxie.module.comment.ReceivedCommentsInterface;
import com.quduquxie.retrofit.DataRequestService;
import com.quduquxie.retrofit.ServiceGenerator;
import com.quduquxie.retrofit.model.CommunalResult;
import com.quduquxie.widget.LoadingPage;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created on 17/3/1.
 * Created by crazylei.
 */

public class ReceivedCommentsPresenter implements ReceivedCommentsInterface.Presenter {

    private ReceivedCommentsInterface.View receivedReviewsView;

    private LoadingPage loadingPage;

    private boolean loadingMoreState = true;

    public ReceivedCommentsPresenter(@NonNull ReceivedCommentsInterface.View receivedReviewsView) {
        this.receivedReviewsView = receivedReviewsView;
    }

    @Override
    public void init() {

    }

    @Override
    public void initParameter(int page) {
        receivedReviewsView.initView();
        loadReceivedComments(page);
    }

    @Override
    public void loadReceivedComments(int page) {
        receivedReviewsView.showLoadingPage();
        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
        dataRequestService.loadReceivedBookReview(0, 1, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<CommunalResult<ReviewResult<Review>>>() {
                    @Override
                    public void onNext(CommunalResult<ReviewResult<Review>> receivedCommentResult) {
                        Logger.d("LoadReceivedReview onNext");
                        if (receivedCommentResult != null) {
                            if (receivedCommentResult.getCode() == 0 && receivedCommentResult.getModel().actions != null && receivedCommentResult.getModel().actions != null && receivedCommentResult.getModel().actions.size() > 0) {

                                receivedReviewsView.setReceivedComments(receivedCommentResult.getModel().actions);

                                if (receivedCommentResult.getModel().actions.size() >= 10) {
                                    loadingMoreState = true;
                                } else {
                                    loadingMoreState = false;
                                    receivedReviewsView.setListEnd();
                                }

                            } else {
                                loadingMoreState = false;
                                receivedReviewsView.setReceivedComments(null);
                            }
                        } else {
                            loadingMoreState = false;
                            receivedReviewsView.setReceivedComments(null);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("LoadReceivedReview onError: " + throwable.toString());
                        receivedReviewsView.showLoadingError();
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("LoadReceivedReview onComplete");
                        receivedReviewsView.hideLoadingPage();
                    }
                });
    }

    @Override
    public void loadReceivedCommentsMore(int page) {
        receivedReviewsView.setRefreshViewState(true);
        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
        dataRequestService.loadReceivedBookReview(0, 1, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<CommunalResult<ReviewResult<Review>>>() {
                    @Override
                    public void onNext(CommunalResult<ReviewResult<Review>> receivedCommentResult) {
                        Logger.d("LoadReceivedReview onNext");
                        if (receivedCommentResult != null) {
                            if (receivedCommentResult.getCode() == 0 && receivedCommentResult.getModel().actions != null && receivedCommentResult.getModel().actions != null && receivedCommentResult.getModel().actions.size() > 0) {
                                receivedReviewsView.setReceivedCommentsMore(receivedCommentResult.getModel().actions);

                                if (receivedCommentResult.getModel().actions.size() >= 10) {
                                    loadingMoreState = true;
                                } else {
                                    loadingMoreState = false;
                                    receivedReviewsView.setListEnd();
                                }

                            } else {
                                loadingMoreState = false;
                                receivedReviewsView.setListEnd();
                                receivedReviewsView.setReceivedCommentsMore(null);
                            }
                        } else {
                            loadingMoreState = false;
                            receivedReviewsView.setListEnd();
                            receivedReviewsView.setReceivedCommentsMore(null);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("LoadReceivedReview onError: " + throwable.toString());
                        receivedReviewsView.setRefreshViewState(false);
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("LoadReceivedReview onComplete");
                        receivedReviewsView.setRefreshViewState(false);
                    }
                });
    }


    @Override
    public void setLoadingPage(LoadingPage loadingPage) {
        this.loadingPage = loadingPage;
    }

    @Override
    public boolean isLoadingMoreState() {
        return loadingMoreState;
    }

    @Override
    public void publishCommentReply(Review review, String content) {
        receivedReviewsView.hideCommentActionDialog();
        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
        dataRequestService.publishCommentReply(review.book.id, review.comments.id, review.sender.id, content)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<CommunalResult<Comment>>() {
                    @Override
                    public void onNext(CommunalResult communalResult) {
                        Logger.d("PublishCommentReply onNext");
                        if (communalResult != null) {
                            if (communalResult.getCode() == 0) {
                                receivedReviewsView.showToast("回复成功！");
                            } else {
                                if (!TextUtils.isEmpty(communalResult.getMessage())) {
                                    receivedReviewsView.showToast(communalResult.getMessage());
                                } else {
                                    receivedReviewsView.showToast("请求失败！");
                                }
                            }
                        } else {
                            receivedReviewsView.showToast("请求发送失败，请您检查网络设置！");
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