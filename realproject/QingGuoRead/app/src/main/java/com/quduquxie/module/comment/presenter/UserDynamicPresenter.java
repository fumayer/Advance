package com.quduquxie.module.comment.presenter;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.quduquxie.model.Comment;
import com.quduquxie.model.Review;
import com.quduquxie.model.ReviewResult;
import com.quduquxie.module.comment.UserDynamicInterface;
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

public class UserDynamicPresenter implements UserDynamicInterface.Presenter {

    private UserDynamicInterface.View userDynamicView;

    private LoadingPage loadingPage;

    private boolean loadingMoreState = true;

    public UserDynamicPresenter(@NonNull UserDynamicInterface.View userDynamicView) {
        this.userDynamicView = userDynamicView;
    }

    @Override
    public void init() {

    }

    @Override
    public void initParameter(int page) {

        userDynamicView.initView();
        loadUserDynamic(page);
    }

    @Override
    public void loadUserDynamic(int page) {
        userDynamicView.showLoadingPage();
        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
        dataRequestService.loadUserDynamic(1, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<CommunalResult<ReviewResult<Review>>>() {
                    @Override
                    public void onNext(CommunalResult<ReviewResult<Review>> dynamicResult) {
                        Logger.d("LoadUserDynamic onNext");
                        if (dynamicResult != null) {
                            if (dynamicResult.getCode() == 0 && dynamicResult.getModel() != null && dynamicResult.getModel().actions != null && dynamicResult.getModel().actions.size() > 0) {

                                userDynamicView.setDynamics(dynamicResult.getModel().actions);

                                if (dynamicResult.getModel().actions.size() >= 10) {
                                    loadingMoreState = true;
                                } else {
                                    loadingMoreState = false;
                                    userDynamicView.setListEnd();
                                }

                            } else {
                                loadingMoreState = false;
                                userDynamicView.setDynamics(null);
                            }
                        } else {
                            loadingMoreState = false;
                            userDynamicView.setDynamics(null);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("LoadUserDynamic onError: " + throwable.toString());
                        userDynamicView.showLoadingError();
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("LoadUserDynamic onComplete");
                        userDynamicView.hideLoadingPage();
                    }
                });
    }

    @Override
    public void loadUserDynamicMore(int page) {
        userDynamicView.setRefreshViewState(true);
        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
        dataRequestService.loadUserDynamic(1, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<CommunalResult<ReviewResult<Review>>>() {
                    @Override
                    public void onNext(CommunalResult<ReviewResult<Review>> dynamicResult) {
                        Logger.d("LoadUserDynamic onNext");
                        if (dynamicResult != null) {
                            if (dynamicResult.getCode() == 0 && dynamicResult.getModel() != null && dynamicResult.getModel().actions != null && dynamicResult.getModel().actions.size() > 0) {

                                userDynamicView.setDynamicsMore(dynamicResult.getModel().actions);

                                if (dynamicResult.getModel().actions.size() >= 10) {
                                    loadingMoreState = true;
                                } else {
                                    loadingMoreState = false;
                                    userDynamicView.setListEnd();
                                }

                            } else {
                                loadingMoreState = false;
                                userDynamicView.setListEnd();
                                userDynamicView.setDynamicsMore(null);
                            }
                        } else {
                            loadingMoreState = false;
                            userDynamicView.setListEnd();
                            userDynamicView.setDynamicsMore(null);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("LoadUserDynamic onError: " + throwable.toString());
                        userDynamicView.setRefreshViewState(false);
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("LoadUserDynamic onComplete");
                        userDynamicView.setRefreshViewState(false);
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
        userDynamicView.hideCommentActionDialog();
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
                                userDynamicView.showToast("回复成功！");
                            } else {
                                if (!TextUtils.isEmpty(communalResult.getMessage())) {
                                    userDynamicView.showToast(communalResult.getMessage());
                                } else {
                                    userDynamicView.showToast("请求失败！");
                                }
                            }
                        } else {
                            userDynamicView.showToast("请求发送失败，请您检查网络设置！");
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
