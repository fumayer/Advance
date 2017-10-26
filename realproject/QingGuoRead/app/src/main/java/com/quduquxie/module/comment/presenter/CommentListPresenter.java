package com.quduquxie.module.comment.presenter;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.quduquxie.function.comment.list.adapter.CommentAdapter;
import com.quduquxie.model.Comment;
import com.quduquxie.model.CommentList;
import com.quduquxie.model.CommentReply;
import com.quduquxie.model.CommentUser;
import com.quduquxie.module.comment.CommentListInterface;
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
 * Created on 17/3/2.
 * Created by crazylei.
 */

public class CommentListPresenter implements CommentListInterface.Presenter {

    private CommentListInterface.View commentListView;

    private LoadingPage loadingPage;

    private boolean loadingMoreState = true;

    public CommentListPresenter(@NonNull CommentListInterface.View commentListView) {
        this.commentListView = commentListView;
    }

    @Override
    public void init() {

    }

    @Override
    public void initParameter() {
        commentListView.initView();
    }

    @Override
    public void loadCommentList(String id_book, int page) {
        if (!TextUtils.isEmpty(id_book)) {
            commentListView.showLoadingPage();
            DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
            dataRequestService.loadBookComments(id_book, page)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ResourceSubscriber<CommunalResult<CommentList>>() {

                        @Override
                        public void onNext(CommunalResult<CommentList> commentListResult) {
                            Logger.d("LoadBookComments: onNext");
                            commentListView.hideLoadingPage();
                            if (commentListResult != null) {
                                if (commentListResult.getCode() == 0 && commentListResult.getModel() != null) {

                                    commentListView.setCommentCount(commentListResult.getModel().count);

                                    if (commentListResult.getModel().count == 0) {
                                        loadingMoreState = false;
                                        commentListView.showCommentEmptyView();
                                    } else {
                                        loadingMoreState = true;
                                        commentListView.showCommentContextView();
                                        if (commentListResult.getModel().hot_comments != null && commentListResult.getModel().hot_comments.size() > 0) {
                                            commentListView.setCommentFlag(CommentAdapter.TYPE_COMMENT_HOT_FLAG);
                                            commentListView.setHotComments(commentListResult.getModel().hot_comments);
                                        }

                                        if (commentListResult.getModel().comments != null && commentListResult.getModel().comments.size() > 0) {
                                            commentListView.setCommentFlag(CommentAdapter.TYPE_COMMENT_NEW_FLAG);
                                            commentListView.setNewComments(commentListResult.getModel().comments);
                                            if (commentListResult.getModel().comments.size() >= 10) {
                                                loadingMoreState = true;
                                            } else {
                                                loadingMoreState = false;
                                                commentListView.setCommentEnd();
                                            }

                                        }
                                    }
                                } else {
                                    loadingMoreState = false;
                                    commentListView.showCommentEmptyView();
                                }
                            } else {
                                loadingMoreState = false;
                                commentListView.showCommentEmptyView();
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Logger.d("LoadBookComments: onError: " + throwable.toString());
                            loadingMoreState = false;
                            commentListView.showLoadingError();
                        }

                        @Override
                        public void onComplete() {
                            Logger.d("LoadBookComments: onComplete");
                        }
                    });
        } else {
            loadingMoreState = false;
            commentListView.showLoadingError();
        }
    }

    @Override
    public void loadCommentListMore(String id_book, int page) {
        if (!TextUtils.isEmpty(id_book)) {
            commentListView.setRefreshViewState(true);
            DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
            dataRequestService.loadBookComments(id_book, page)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ResourceSubscriber<CommunalResult<CommentList>>() {

                        @Override
                        public void onNext(CommunalResult<CommentList> commentListResult) {
                            Logger.d("LoadBookComments: onNext");
                            if (commentListResult != null) {
                                if (commentListResult.getCode() == 0 && commentListResult.getModel() != null) {
                                    if (commentListResult.getModel().comments != null && commentListResult.getModel().comments.size() > 0) {

                                        commentListView.setNewComments(commentListResult.getModel().comments);

                                        if (commentListResult.getModel().comments.size() >= 10) {
                                            loadingMoreState = true;
                                        } else {
                                            loadingMoreState = false;
                                            commentListView.setCommentEnd();
                                        }

                                    }
                                } else {
                                    loadingMoreState = false;
                                    commentListView.setCommentEnd();
                                }
                            } else {
                                loadingMoreState = false;
                                commentListView.setCommentEnd();
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Logger.d("LoadBookComments: onError: " + throwable);
                            loadingMoreState = false;
                            commentListView.setRefreshViewState(false);
                        }

                        @Override
                        public void onComplete() {
                            Logger.d("LoadBookComments: onComplete");
                            commentListView.setRefreshViewState(false);
                        }
                    });
        } else {
            loadingMoreState = false;
            commentListView.setCommentEnd();
            commentListView.setRefreshViewState(false);
        }
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
                                commentListView.showToast("点赞成功！");
                                commentListView.commendLike(comment, position);
                            } else {
                                if (!TextUtils.isEmpty(result.getMessage())) {
                                    commentListView.showToast(result.getMessage());
                                } else {
                                    commentListView.showToast("点赞失败！");
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Logger.d("CommentCommendLike onError: " + throwable.toString());
                            commentListView.showToast("点赞失败！");
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
                                commentListView.showToast("取消点赞成功！");
                                commentListView.commendDislike(comment, position);
                            } else {
                                commentListView.showToast("点赞失败！");
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Logger.d("CommentCommendDislike onError: " + throwable.toString());
                            commentListView.showToast("取消点赞失败");
                        }

                        @Override
                        public void onComplete() {
                            Logger.d("CommentCommendDislike onComplete");
                        }
                    });
        }
    }

    @Override
    public void publishBookComment(String id_book, String id_user, String content) {
        commentListView.hideReviewWriteDialog();
        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
        dataRequestService.publishBookComment(id_book, id_user, content)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<CommunalResult<Comment>>() {

                    @Override
                    public void onNext(CommunalResult<Comment> commentResult) {
                        Logger.d("PublishBookComment onNext");
                        if (commentResult != null) {
                            if (commentResult.getCode() == 0 && commentResult.getModel() != null) {
                                commentListView.showToast("书评发布成功！");
                                commentListView.addComment(commentResult.getModel());
                            } else {
                                if (!TextUtils.isEmpty(commentResult.getMessage())) {
                                    commentListView.showToast(commentResult.getMessage());
                                } else {
                                    commentListView.showToast("请求失败！");
                                }
                            }
                        } else {
                            commentListView.showToast("请求发送失败，请您检查网络设置！");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("PublishBookComment onError: " + throwable.toString());
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("PublishBookComment onComplete");
                    }
                });
    }

    @Override
    public void publishReviewReply(Comment comment, String id_book, String content) {
        commentListView.hideReviewReplyDialog();
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
                                commentListView.showToast("回复成功！");
                                commentListView.addCommentReply(commentResult.getModel());
                            } else {
                                if (!TextUtils.isEmpty(commentResult.getMessage())) {
                                    commentListView.showToast(commentResult.getMessage());
                                } else {
                                    commentListView.showToast("请求失败！");
                                }
                            }
                        } else {
                            commentListView.showToast("请求发送失败，请您检查网络设置！");
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
        commentListView.hideCommentReplyDialog();
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
                                commentListView.showToast("回复成功！");
                                commentListView.addCommentReply(commentResult.getModel());
                            } else {
                                if (!TextUtils.isEmpty(commentResult.getMessage())) {
                                    commentListView.showToast(commentResult.getMessage());
                                } else {
                                    commentListView.showToast("请求失败！");
                                }
                            }
                        } else {
                            commentListView.showToast("请求发送失败，请您检查网络设置！");
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
    public void reportCommentReply(Comment comment, CommentReply commentReply) {
        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
        dataRequestService.reportCommentReply(comment.id, commentReply.sn)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<CommunalResult>() {

                    @Override
                    public void onNext(CommunalResult communalResult) {
                        Logger.d("ReportCommentReply onNext");
                        if (communalResult.getCode() == 0) {
                            commentListView.showToast("举报成功");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("ReportCommentReply onError: " + throwable.toString());
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("ReportCommentReply onComplete");
                    }
                });
    }

    @Override
    public void deleteCommentReplyRequest(String id_book, final Comment comment, final CommentReply commentReply, final int position) {
        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
        dataRequestService.deleteCommentReply(id_book, comment.id, commentReply.sn)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<Response<ResponseBody>>() {

                    @Override
                    public void onNext(Response<ResponseBody> responseResult) {
                        Logger.d("deleteCommentReply onNext");
                        if (responseResult.code() == 200) {
                            commentListView.showToast("删除成功！");
                            commentListView.deleteCommentReplyAction(comment, commentReply, position);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("deleteCommentReply onError: " + throwable.toString());
                        commentListView.showToast("删除失败！");
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
                        Logger.d("ReportCommentReply onNext");
                        if (communalResult.getCode() == 0) {
                            commentListView.showToast("举报成功");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("ReportCommentReply onError: " + throwable.toString());
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("ReportCommentReply onComplete");
                    }
                });
    }

    @Override
    public void deleteCommentRequest(String id_book, final Comment comment, final int position) {
        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
        dataRequestService.deleteComment(id_book, comment.id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResourceSubscriber<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> responseResult) {
                        Logger.d("deleteCommentReply onNext");
                        if (responseResult.code() == 200) {
                            commentListView.showToast("删除成功！");
                            commentListView.deleteCommentAction(comment, position);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("ReportCommentReply onError: " + throwable.toString());
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("ReportCommentReply onComplete");
                    }
                });
    }
}
