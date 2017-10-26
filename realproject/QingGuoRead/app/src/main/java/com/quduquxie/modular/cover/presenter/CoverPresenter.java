package com.quduquxie.modular.cover.presenter;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.quduquxie.base.RxPresenter;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.rxjava.RxSchedulers;
import com.quduquxie.function.comment.list.adapter.CommentAdapter;
import com.quduquxie.model.Comment;
import com.quduquxie.model.CommentItem;
import com.quduquxie.model.CommentList;
import com.quduquxie.model.CommentReply;
import com.quduquxie.model.CommentUser;
import com.quduquxie.modular.cover.CoverInterface;
import com.quduquxie.modular.cover.view.CoverActivity;
import com.quduquxie.retrofit.DataRequestService;
import com.quduquxie.retrofit.ServiceGenerator;
import com.quduquxie.retrofit.model.CommunalResult;
import com.quduquxie.widget.LoadingPage;

import java.util.ArrayList;

import io.reactivex.subscribers.ResourceSubscriber;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created on 17/4/13.
 * Created by crazylei.
 */

public class CoverPresenter extends RxPresenter implements CoverInterface.Presenter {

    private CoverActivity coverActivity;

    private LoadingPage loadingPage;

    public CoverPresenter(CoverActivity coverActivity) {
        this.coverActivity = coverActivity;
    }

    @Override
    public void loadCoverInformation(String id) {
        coverActivity.showLoadingPage();

        if (TextUtils.isEmpty(id)) {
            coverActivity.showLoadingError();
        }

        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, false);
        insertDisposable(dataRequestService.loadBookInformation(id)
                .compose(RxSchedulers.<CommunalResult<Book>>schedulerHelper())
                .subscribeWith(new ResourceSubscriber<CommunalResult<Book>>() {
                    @Override
                    public void onNext(CommunalResult<Book> bookResult) {
                        Logger.d("LoadBookInformation onNext");
                        if (bookResult != null) {
                            if (bookResult.getCode() == 0 && bookResult.getModel() != null) {
                                coverActivity.hideLoadingPage();
                                coverActivity.initBookInformation(bookResult.getModel());
                            } else if (!TextUtils.isEmpty(bookResult.getMessage())) {
                                coverActivity.showPromptMessage(bookResult.getMessage());
                                coverActivity.showLoadingError();
                            }
                        } else {
                            coverActivity.showLoadingError();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("LoadBookInformation onError: " + throwable.toString());
                        coverActivity.showLoadingError();
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("LoadBookInformation onComplete");
                    }
                })
        );
    }

    @Override
    public void loadCoverRecommend(String id) {
        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, false);
        insertDisposable(dataRequestService.loadCoverRecommend(id)
                .compose(RxSchedulers.<CommunalResult<ArrayList<Book>>>schedulerHelper())
                .subscribeWith(new ResourceSubscriber<CommunalResult<ArrayList<Book>>>() {

                    @Override
                    public void onNext(CommunalResult<ArrayList<Book>> opusListResult) {
                        Logger.d("LoadCoverRecommend onNext");
                        if (opusListResult != null) {
                            if (opusListResult.getModel() != null && opusListResult.getModel().size() > 0) {
                                coverActivity.setCoverRecommend(opusListResult.getModel());
                            } else if (!TextUtils.isEmpty(opusListResult.getMessage())) {
                                coverActivity.showPromptMessage(opusListResult.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("LoadCoverRecommend onError: " + throwable.toString());
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("LoadCoverRecommend onComplete");
                    }
                })
        );
    }

    @Override
    public void loadCommentList(String id, int page) {
        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, false);
        insertDisposable(dataRequestService.loadBookComments(id, page)
                .compose(RxSchedulers.<CommunalResult<CommentList>>schedulerHelper())
                .subscribeWith(new ResourceSubscriber<CommunalResult<CommentList>>() {
                    @Override
                    public void onNext(CommunalResult<CommentList> commentListResult) {
                        Logger.d("LoadBookComments: onNext");

                        ArrayList<CommentItem> commentItems = new ArrayList<>();

                        int count = 0;

                        if (commentListResult != null) {

                            commentItems.add(createCommentItem(CommentAdapter.TYPE_COMMENT_NUMBER, commentListResult.getModel().count));

                            if (commentListResult.getCode() == 0 && commentListResult.getModel() != null) {

                                CommentList commentList = commentListResult.getModel();

                                if (commentList.count == 0) {
                                    commentItems.add(createCommentItem(CommentAdapter.TYPE_COMMENT_EMPTY));

                                } else {
                                    if (commentList.hot_comments != null && commentList.hot_comments.size() > 0) {
                                        commentItems.add(createCommentItem(CommentAdapter.TYPE_COMMENT_HOT_FLAG));

                                        if (commentList.hot_comments.size() >= 5) {
                                            count = 5;
                                        } else {
                                            count = commentList.hot_comments.size();
                                        }

                                        for (int i = 0; i < count; i++) {
                                            Comment comment = commentList.hot_comments.get(i);
                                            commentItems.add(createCommentItem(CommentAdapter.TYPE_COMMENT_HOT, comment));
                                        }
                                    }

                                    if (count < 5 &&  commentList.comments != null && commentList.comments.size() > 0) {

                                        commentItems.add(createCommentItem(CommentAdapter.TYPE_COMMENT_NEW_FLAG));

                                        int total = Math.min(5 - count, commentList.comments.size());

                                        for (int i = 0; i < total; i++) {
                                            Comment comment = commentList.comments.get(i);
                                            commentItems.add(createCommentItem(CommentAdapter.TYPE_COMMENT_NEW, comment));
                                        }
                                    }

                                    commentItems.add(createCommentItem(CommentAdapter.TYPE_COMMENT_MORE));
                                }

                            } else if (!TextUtils.isEmpty(commentListResult.getMessage())) {
                                coverActivity.showPromptMessage(commentListResult.getMessage());
                                commentItems.add(createCommentItem(CommentAdapter.TYPE_COMMENT_EMPTY));
                            }
                        } else {
                            commentItems.add(createCommentItem(CommentAdapter.TYPE_COMMENT_EMPTY));
                        }

                        coverActivity.setCommentItemList(commentItems);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("LoadBookComments onError: " + throwable.toString());
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("LoadBookComments onComplete");
                    }
                })
        );
    }

    @Override
    public void publishBookComment(String id, String user_id, String content) {
        coverActivity.hideReviewWriteDialog();
        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
        insertDisposable(dataRequestService.publishBookComment(id, user_id, content)
                .compose(RxSchedulers.<CommunalResult<Comment>>schedulerHelper())
                .subscribeWith(new ResourceSubscriber<CommunalResult<Comment>>() {
                    @Override
                    public void onNext(CommunalResult<Comment> commentResult) {
                        Logger.d("PublishBookComment onNext");
                        if (commentResult != null) {
                            if (commentResult.getCode() == 0 && commentResult.getModel() != null) {
                                coverActivity.showPromptMessage("书评发布成功！");
                                coverActivity.addComment(commentResult.getModel());
                            } else {
                                if (!TextUtils.isEmpty(commentResult.getMessage())) {
                                    coverActivity.showPromptMessage(commentResult.getMessage());
                                } else {
                                    coverActivity.showPromptMessage("请求失败！");
                                }
                            }
                        } else {
                            coverActivity.showPromptMessage("请求发送失败，请您检查网络设置！");
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
                })
        );
    }

    @Override
    public void publishReviewReply(Comment comment, String id_book, String content) {
        coverActivity.hideReviewReplyDialog();
        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
        insertDisposable(dataRequestService.publishCommentReply(id_book, comment.id, comment.sender.id, content)
                .compose(RxSchedulers.<CommunalResult<Comment>>schedulerHelper())
                .subscribeWith(new ResourceSubscriber<CommunalResult<Comment>>() {
                    @Override
                    public void onNext(CommunalResult<Comment> commentResult) {
                        Logger.d("PublishCommentReply onNext");
                        if (commentResult != null) {
                            if (commentResult.getCode() == 0 && commentResult.getModel() != null) {
                                coverActivity.showPromptMessage("回复成功！");
                                coverActivity.addCommentReply(commentResult.getModel());
                            } else {
                                if (!TextUtils.isEmpty(commentResult.getMessage())) {
                                    coverActivity.showPromptMessage(commentResult.getMessage());
                                } else {
                                    coverActivity.showPromptMessage("请求失败！");
                                }
                            }
                        } else {
                            coverActivity.showPromptMessage("请求发送失败，请您检查网络设置！");
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
                })
        );
    }

    @Override
    public void publishCommentReply(Comment comment, CommentUser commentUser, String id_book, String content) {
        coverActivity.hideCommentReplyDialog();
        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
        insertDisposable(dataRequestService.publishCommentReply(id_book, comment.id, commentUser.id, content)
                .compose(RxSchedulers.<CommunalResult<Comment>>schedulerHelper())
                .subscribeWith(new ResourceSubscriber<CommunalResult<Comment>>() {
                    @Override
                    public void onNext(CommunalResult<Comment> commentResult) {
                        Logger.d("PublishCommentReply onNext");
                        if (commentResult != null) {
                            if (commentResult.getCode() == 0 && commentResult.getModel() != null) {
                                coverActivity.showPromptMessage("回复成功！");
                                coverActivity.addCommentReply(commentResult.getModel());
                            } else {
                                if (!TextUtils.isEmpty(commentResult.getMessage())) {
                                    coverActivity.showPromptMessage(commentResult.getMessage());
                                } else {
                                    coverActivity.showPromptMessage("请求失败！");
                                }
                            }
                        } else {
                            coverActivity.showPromptMessage("请求发送失败，请您检查网络设置！");
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
                })
        );
    }

    @Override
    public void reportCommentReply(Comment comment, CommentReply commentReply) {
        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
        insertDisposable(dataRequestService.reportCommentReply(comment.id, commentReply.sn)
                .compose(RxSchedulers.<CommunalResult>schedulerHelper())
                .subscribeWith(new ResourceSubscriber<CommunalResult>() {

                    @Override
                    public void onNext(CommunalResult communalResult) {
                        Logger.d("ReportCommentReply onNext");
                        if (communalResult.getCode() == 0) {
                            coverActivity.showPromptMessage("举报成功");
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
                })
        );
    }

    @Override
    public void deleteCommentReplyRequest(String id_book, final Comment comment, final CommentReply commentReply) {
        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
        insertDisposable(dataRequestService.deleteCommentReply(id_book, comment.id, commentReply.sn)
                .compose(RxSchedulers.<Response<ResponseBody>>schedulerHelper())
                .subscribeWith(new ResourceSubscriber<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> responseResult) {
                        Logger.d("DeleteCommentReply onNext");
                        if (responseResult.code() == 200) {
                            coverActivity.showPromptMessage("删除成功！");
                            coverActivity.deleteCommentReplyAction(comment, commentReply);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("DeleteCommentReply onError: " + throwable.toString());
                        coverActivity.showPromptMessage("删除失败！");
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("DeleteCommentReply onComplete");
                    }
                })
        );
    }

    @Override
    public void reportComment(Comment comment) {
        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
        insertDisposable(dataRequestService.reportComment(comment.id)
                .compose(RxSchedulers.<CommunalResult>schedulerHelper())
                .subscribeWith(new ResourceSubscriber<CommunalResult>() {
                    @Override
                    public void onNext(CommunalResult communalResult) {
                        Logger.d("ReportComment onNext");
                        if (communalResult.getCode() == 0) {
                            coverActivity.showPromptMessage("举报成功");
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
                })
        );
    }

    @Override
    public void deleteCommentRequest(String id_book, final Comment comment, final int position) {
        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
        insertDisposable(dataRequestService.deleteComment(id_book, comment.id)
                .compose(RxSchedulers.<Response<ResponseBody>>schedulerHelper())
                .subscribeWith(new ResourceSubscriber<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> responseResult) {
                        Logger.d("DeleteComment onNext");
                        if (responseResult.code() == 200) {
                            coverActivity.showPromptMessage("删除成功！");
                            coverActivity.deleteCommentAction(comment, position);
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
                })
        );
    }

    @Override
    public void commentCommendAction(String id_book, final Comment comment, boolean state, final int position) {
        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
        if (state) {
            insertDisposable(dataRequestService.commentCommendLike(id_book, comment.id)
                    .compose(RxSchedulers.<CommunalResult>schedulerHelper())
                    .subscribeWith(new ResourceSubscriber<CommunalResult>() {
                        @Override
                        public void onNext(CommunalResult result) {
                            Logger.d("CommentCommendLike onNext");
                            if (result.getCode() == 0) {
                                coverActivity.showPromptMessage("点赞成功！");
                                coverActivity.commendLike(comment, position);
                            } else {
                                if (!TextUtils.isEmpty(result.getMessage())) {
                                    coverActivity.showPromptMessage(result.getMessage());
                                } else {
                                    coverActivity.showPromptMessage("点赞失败！");
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Logger.d("CommentCommendLike onError: " + throwable.toString());
                            coverActivity.showPromptMessage("点赞失败！");
                        }

                        @Override
                        public void onComplete() {
                            Logger.d("CommentCommendLike onComplete");
                        }
                    })
            );
        } else {
            insertDisposable(dataRequestService.commentCommendDislike(id_book, comment.id)
                    .compose(RxSchedulers.<Response<ResponseBody>>schedulerHelper())
                    .subscribeWith(new ResourceSubscriber<Response<ResponseBody>>() {
                        @Override
                        public void onNext(Response<ResponseBody> responseResponse) {
                            Logger.d("CommentCommendDislike onNext");
                            if (responseResponse.code() == 200) {
                                coverActivity.showPromptMessage("取消点赞成功！");
                                coverActivity.commendDislike(comment, position);
                            } else {
                                coverActivity.showPromptMessage("点赞失败！");
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Logger.d("CommentCommendDislike onError: " + throwable.toString());
                            coverActivity.showPromptMessage("取消点赞失败！");
                        }

                        @Override
                        public void onComplete() {
                            Logger.d("CommentCommendDislike onComplete");
                        }
                    })
            );
        }
    }

    @Override
    public void recycle() {
        if (loadingPage != null) {
            this.loadingPage.removeAllViews();
            this.loadingPage = null;
        }

        if (coverActivity != null) {
            coverActivity = null;
        }
    }

    private CommentItem createCommentItem(int type) {
        CommentItem commentItem = new CommentItem();
        commentItem.type = type;
        return commentItem;
    }

    private CommentItem createCommentItem(int type, int count) {
        CommentItem commentItem = new CommentItem();
        commentItem.type = type;
        commentItem.count = count;
        return commentItem;
    }

    private CommentItem createCommentItem(int type, Comment comment) {
        CommentItem commentItem = new CommentItem();
        commentItem.type = type;
        commentItem.comment = comment;
        return commentItem;
    }
}
