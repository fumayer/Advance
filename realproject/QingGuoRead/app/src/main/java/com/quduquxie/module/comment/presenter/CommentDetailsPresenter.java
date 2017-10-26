package com.quduquxie.module.comment.presenter;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.quduquxie.model.Comment;
import com.quduquxie.model.CommentReply;
import com.quduquxie.model.CommentUser;
import com.quduquxie.module.comment.CommentDetailsInterface;
import com.quduquxie.retrofit.DataRequestService;
import com.quduquxie.retrofit.ServiceGenerator;
import com.quduquxie.retrofit.model.CommunalResult;
import com.quduquxie.widget.LoadingPage;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import okhttp3.ResponseBody;
import retrofit2.Response;
/**
 * Created on 17/3/1.
 * Created by crazylei.
 */

public class CommentDetailsPresenter implements CommentDetailsInterface.Presenter {

    private CommentDetailsInterface.View commentDetailsView;

    private LoadingPage loadingPage;

    public CommentDetailsPresenter(@NonNull CommentDetailsInterface.View commentDetailsView) {
        this.commentDetailsView = commentDetailsView;
    }

    @Override
    public void init() {

    }

    @Override
    public void initParameter(String id_book, String id_comment) {
        commentDetailsView.initView();
        loadCommentDetails(id_book, id_comment);
    }

    @Override
    public void loadCommentDetails(String id_book, String id_comment) {
        commentDetailsView.showLoadingPage();
        if (!TextUtils.isEmpty(id_book) && !TextUtils.isEmpty(id_comment)) {
            DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, false);
            dataRequestService.loadCommentDetails(id_book, id_comment)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ResourceSubscriber<CommunalResult<Comment>>() {

                        @Override
                        public void onNext(CommunalResult<Comment> commentResult) {
                            Logger.d("LoadCommentDetails onNext");
                            if (commentResult != null) {
                                if (commentResult.getCode() == 0 && commentResult.getModel() != null) {
                                    commentDetailsView.setCommentDetails(commentResult.getModel());
                                } else {
                                    commentDetailsView.setCommentDetails(null);
                                }
                            } else {
                                commentDetailsView.setCommentDetails(null);
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Logger.d("LoadCommentDetails onError: " + throwable.toString());
                            commentDetailsView.showLoadingError();
                        }

                        @Override
                        public void onComplete() {
                            Logger.d("LoadCommentDetails onComplete");
                            commentDetailsView.hideLoadingPage();
                        }
                    });
        } else {
            commentDetailsView.showLoadingError();
        }
    }

    @Override
    public void setLoadingPage(LoadingPage loadingPage) {
        this.loadingPage = loadingPage;
    }

    @Override
    public void publishReviewReply(final Comment comment, String id_book, String content) {
        commentDetailsView.hideReviewReplyDialog();
        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
        dataRequestService.publishCommentReply(id_book, comment.id, comment.sender.id, content)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<CommunalResult<Comment>>() {

                    @Override
                    public void onNext(CommunalResult<Comment> commentResult) {
                        Logger.d("PublishCommentReply onNext");
                        if (commentResult != null) {
                            if (commentResult.getCode() == 0 && commentResult.getModel() != null) {
                                commentDetailsView.showToast("回复成功！");
                                commentDetailsView.addCommentReply(commentResult.getModel().replies);
                            } else {
                                if (!TextUtils.isEmpty(commentResult.getMessage())) {
                                    commentDetailsView.showToast(commentResult.getMessage());
                                } else {
                                    commentDetailsView.showToast("请求失败！");
                                }
                            }
                        } else {
                            commentDetailsView.showToast("请求发送失败，请您检查网络设置！");
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

    @Override
    public void publishCommentReply(Comment comment, CommentUser commentUser, String id_book, String content) {
        commentDetailsView.hideCommentReplyDialog();
        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
        dataRequestService.publishCommentReply(id_book, comment.id, commentUser.id, content)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<CommunalResult<Comment>>() {

                    @Override
                    public void onNext(CommunalResult<Comment> commentResult) {
                        Logger.d("PublishCommentReply onNext");
                        if (commentResult != null) {
                            if (commentResult.getCode() == 0 && commentResult.getModel() != null) {
                                commentDetailsView.showToast("回复成功！");
                                commentDetailsView.addCommentReply(commentResult.getModel().replies);
                            } else {
                                if (!TextUtils.isEmpty(commentResult.getMessage())) {
                                    commentDetailsView.showToast(commentResult.getMessage());
                                } else {
                                    commentDetailsView.showToast("请求失败！");
                                }
                            }
                        } else {
                            commentDetailsView.showToast("请求发送失败，请您检查网络设置！");
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

    @Override
    public void commentCommendAction(String id_book, final Comment comment, boolean state, final int position) {
        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);

        if (state) {
            dataRequestService.commentCommendLike(id_book, comment.id)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ResourceSubscriber<CommunalResult>() {

                        @Override
                        public void onNext(CommunalResult result) {
                            Logger.d("CommentCommendLike onNext");
                            if (result.getCode() == 0) {
                                commentDetailsView.showToast("点赞成功！");
                                commentDetailsView.commendLike(comment, position);
                            } else {
                                if (!TextUtils.isEmpty(result.getMessage())) {
                                    commentDetailsView.showToast(result.getMessage());
                                } else {
                                    commentDetailsView.showToast("点赞失败！");
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Logger.d("CommentCommendLike onError: " + throwable.toString());
                            commentDetailsView.showToast("点赞失败，请检查网络连接！");
                        }

                        @Override
                        public void onComplete() {
                            Logger.d("CommentCommendLike onComplete");
                        }
                    });
        } else {
            dataRequestService.commentCommendDislike(id_book, comment.id)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())

                    .subscribe(new ResourceSubscriber<Response<ResponseBody>>() {

                        @Override
                        public void onNext(Response<ResponseBody> responseResponse) {
                            Logger.d("CommentCommendDislike onNext");
                            if (responseResponse.code() == 200) {
                                commentDetailsView.showToast("取消点赞成功！");
                                commentDetailsView.commendDislike(comment, position);
                            } else {
                                commentDetailsView.showToast("点赞失败！");
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Logger.d("CommentCommendDislike onError: " + throwable.toString());
                            commentDetailsView.showToast("取消点赞失败，请检查网络连接！");
                        }

                        @Override
                        public void onComplete() {
                            Logger.d("CommentCommendDislike onComplete");
                        }
                    });
        }
    }

    @Override
    public void reportCommentReply(Comment comment, CommentReply commentReply) {
        Logger.d(comment.id);
        Logger.d(commentReply.sn);
        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
        dataRequestService.reportCommentReply(comment.id, commentReply.sn)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<CommunalResult>() {

                    @Override
                    public void onNext(CommunalResult communalResult) {
                        Logger.d("ReportCommentReply onNext");
                        if (communalResult.getCode() == 0) {
                            commentDetailsView.showToast("举报成功");
                        } else {
                            if (!TextUtils.isEmpty(communalResult.getMessage())) {
                                commentDetailsView.showToast(communalResult.getMessage());
                            } else {
                                commentDetailsView.showToast("举报失败！");
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("ReportCommentReply onError: " + throwable.toString());
                        commentDetailsView.showToast("举报失败！");
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("ReportCommentReply onComplete");
                    }
                });
    }

    @Override
    public void deleteCommentReply(String id_book, Comment comment, final CommentReply commentReply) {
        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
        dataRequestService.deleteCommentReply(id_book, comment.id, commentReply.sn)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<Response<ResponseBody>>() {

                    @Override
                    public void onNext(Response<ResponseBody> responseResult) {
                        Logger.d("deleteCommentReply onNext");
                        if (responseResult.code() == 200) {
                            commentDetailsView.showToast("删除成功！");
                            commentDetailsView.deleteCommentReply(commentReply);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("deleteCommentReply onError: " + throwable.toString());
                        commentDetailsView.showToast("删除失败！");
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("deleteCommentReply onComplete");
                    }
                });
    }

    @Override
    public void reportComment(Comment comment) {
        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
        dataRequestService.reportComment(comment.id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<CommunalResult>() {

                    @Override
                    public void onNext(CommunalResult communalResult) {
                        Logger.d("ReportComment onNext");
                        if (communalResult.getCode() == 0) {
                            commentDetailsView.showToast("举报成功");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("ReportComment onError: " + throwable.toString());
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("ReportComment onComplete");
                    }
                });
    }

    @Override
    public void deleteCommentRequest(String id_book, final Comment comment) {
        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
        dataRequestService.deleteComment(id_book, comment.id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<Response<ResponseBody>>() {

                    @Override
                    public void onNext(Response<ResponseBody> responseResult) {
                        Logger.d("DeleteComment onNext");
                        if (responseResult.code() == 200) {
                            commentDetailsView.showToast("删除成功！");
                            commentDetailsView.deleteCommentAction(comment);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("DeleteComment onError: " + throwable.toString());
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("DeleteComment onComplete");
                    }
                });
    }
}
