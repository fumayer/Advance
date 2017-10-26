package com.quduquxie.module.comment.view;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.InflateException;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.quduquxie.R;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.manager.ActivityStackManager;
import com.quduquxie.base.module.main.activity.view.MainActivity;
import com.quduquxie.communal.help.comment.CommentPopupWindowHelp;
import com.quduquxie.communal.help.comment.ReplyPopupWindowHelp;
import com.quduquxie.communal.widget.CustomCommentReplyDialog;
import com.quduquxie.communal.widget.CustomInformationDialog;
import com.quduquxie.communal.widget.CustomInformationListener;
import com.quduquxie.communal.widget.CustomReviewReplyDialog;
import com.quduquxie.communal.widget.CustomReviewWriteDialog;
import com.quduquxie.communal.widget.DividerItemDecoration;
import com.quduquxie.db.CommentLikeDao;
import com.quduquxie.db.UserDao;
import com.quduquxie.function.comment.list.adapter.CommentAdapter;
import com.quduquxie.model.Comment;
import com.quduquxie.model.CommentItem;
import com.quduquxie.model.CommentReply;
import com.quduquxie.model.CommentUser;
import com.quduquxie.module.comment.CommentListInterface;
import com.quduquxie.module.comment.listener.CommentItemListener;
import com.quduquxie.module.comment.presenter.CommentListPresenter;
import com.quduquxie.view.BaseActivity;
import com.quduquxie.widget.LoadingPage;
import com.quduquxie.wxapi.WXEntryActivity;

import java.text.MessageFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 17/3/2.
 * Created by crazylei.
 */

public class CommentListActivity extends BaseActivity implements CommentListInterface.View, CommentItemListener, CustomReviewWriteDialog.OnReviewWriteSentClickListener, CustomReviewReplyDialog.OnReviewReplySentClickListener, CustomCommentReplyDialog.OnCommentReplySentClickListener {

    @BindView(R.id.comment_list_back)
    public ImageView comment_list_back;
    @BindView(R.id.comment_list_count)
    public TextView comment_list_count;
    @BindView(R.id.comment_list_write)
    public TextView comment_list_write;
    @BindView(R.id.comment_list_loading)
    public FrameLayout comment_list_loading;
    @BindView(R.id.comment_list_refresh)
    public SwipeRefreshLayout comment_list_refresh;
    @BindView(R.id.comment_list_content)
    public RecyclerView comment_list_content;
    @BindView(R.id.comment_list_empty_view)
    public RelativeLayout comment_list_empty_view;
    @BindView(R.id.comment_list_empty_prompt)
    public TextView comment_list_empty_prompt;

    private CommentListInterface.Presenter commentListPresenter;

    private Book book;
    private String id_book;

    private LinearLayoutManager linearLayoutManager;
    private CommentAdapter commentAdapter;

    private ArrayList<CommentItem> commentItems = new ArrayList<>();

    private Toast toast;

    private LoadingPage loadingPage;

    private int page = 1;

    private CustomReviewWriteDialog customReviewWriteDialog;
    private CustomReviewReplyDialog customReviewReplyDialog;
    private CustomCommentReplyDialog customCommentReplyDialog;

    private CommentLikeDao commentLikeDao;

    private View commentPopupView;
    private PopupWindow commentPopupWindow;
    private CommentPopupWindowHelp commentPopupWindowHelp;

    private View replyPopupView;
    private PopupWindow replyPopupWindow;
    private ReplyPopupWindowHelp replyPopupWindowHelp;

    private int count = 0;

    private CustomInformationDialog customInformationDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.layout_activity_comment_list);
        } catch (Resources.NotFoundException exception) {
            collectException(exception);
            exception.printStackTrace();
        }

        ButterKnife.bind(this);
        Intent intent = getIntent();

        if (intent != null) {
            this.id_book = intent.getStringExtra("id_book");
            this.book = (Book) intent.getSerializableExtra("book");
        }
        if (TextUtils.isEmpty(id_book)) {
            if (book != null && !TextUtils.isEmpty(book.id)) {
                id_book = book.id;
            }
        }

        commentLikeDao = CommentLikeDao.getInstance(this);

        commentListPresenter = new CommentListPresenter(this);
        commentListPresenter.initParameter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (commentItems != null) {
            commentItems.clear();
        }
        if (commentAdapter != null) {
            commentAdapter.recyclerResource();
        }
    }

    @Override
    public void setPresenter(CommentListInterface.Presenter commentListPresenter) {
        this.commentListPresenter = commentListPresenter;
    }

    @Override
    public void initView() {

        if (commentItems == null) {
            commentItems = new ArrayList<>();
        } else {
            commentItems.clear();
        }

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        commentAdapter = new CommentAdapter(this, commentItems);
        commentAdapter.setCommentItemListener(this);

        if (comment_list_content != null) {
            comment_list_content.setLayoutManager(linearLayoutManager);
            comment_list_content.setAdapter(commentAdapter);
            comment_list_content.setItemAnimator(new DefaultItemAnimator());
            comment_list_content.addItemDecoration(new DividerItemDecoration());

            comment_list_content.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    int totalItemCount = linearLayoutManager.getItemCount();
                    int visibleItemCount = linearLayoutManager.getChildCount();
                    int firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition();

                    if (!comment_list_refresh.isRefreshing() && commentListPresenter.isLoadingMoreState() && (visibleItemCount + firstVisiblePosition) == totalItemCount - 5) {
                        setRefreshViewState(true);
                        page += 1;
                        commentListPresenter.loadCommentListMore(id_book, page);
                    }
                }
            });
        }

        if (comment_list_refresh != null) {
            comment_list_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    setRefreshViewState(false);
                    page = 1;
                    commentListPresenter.loadCommentList(id_book, page);
                }
            });
        }

        commentListPresenter.loadCommentList(id_book, page);
    }

    @Override
    public void showLoadingPage() {
        if (loadingPage != null) {
            loadingPage.onSuccess();
        }

        if (loadingPage == null) {
            loadingPage = new LoadingPage(this, comment_list_loading);
        }
        commentListPresenter.setLoadingPage(loadingPage);
    }

    @Override
    public void hideLoadingPage() {
        if (loadingPage != null) {
            loadingPage.onSuccess();
        }
    }

    @Override
    public void showLoadingError() {
        if (loadingPage != null) {
            loadingPage.onError();
        }
    }

    @Override
    public void setCommentCount(int count) {
        this.count = count;
        if (comment_list_count != null) {
            comment_list_count.setText(MessageFormat.format("书评 {0}", count));
        }
    }

    @Override
    public void showCommentEmptyView() {
        if (comment_list_refresh != null) {
            setRefreshViewState(false);
            comment_list_refresh.setEnabled(false);
            comment_list_refresh.setVisibility(View.GONE);
        }
        if (comment_list_empty_view != null) {
            comment_list_empty_view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showCommentContextView() {
        if (comment_list_empty_view != null) {
            comment_list_empty_view.setVisibility(View.GONE);
        }
        if (comment_list_refresh != null) {
            comment_list_refresh.setEnabled(true);
            comment_list_refresh.setVisibility(View.VISIBLE);
        }

        if (commentItems == null) {
            commentItems = new ArrayList<>();
        } else {
            commentItems.clear();
        }
    }

    @Override
    public void setHotComments(ArrayList<Comment> comments) {
        if (comments != null && comments.size() > 0) {
            CommentItem commentItem;
            for (Comment comment : comments) {
                if (comment != null) {
                    commentItem = new CommentItem();
                    commentItem.type = CommentAdapter.TYPE_COMMENT_HOT;
                    commentItem.comment = comment;
                    commentItems.add(commentItem);
                    commentAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void setNewComments(ArrayList<Comment> comments) {
        if (comments != null && comments.size() > 0) {
            CommentItem commentItem;
            for (Comment comment : comments) {
                if (comment != null) {
                    commentItem = new CommentItem();
                    commentItem.type = CommentAdapter.TYPE_COMMENT_NEW;
                    commentItem.comment = comment;
                    commentItems.add(commentItem);
                    commentAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void showToast(String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }

        if (toast == null) {
            toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }

        if (!isFinishing() && toast != null) {
            toast.show();
        }
    }

    @Override
    public void setRefreshViewState(boolean state) {
        if (comment_list_refresh == null) {
            return;
        }
        if (state) {
            comment_list_refresh.setRefreshing(true);
        } else {
            if (comment_list_refresh.isRefreshing()) {
                comment_list_refresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        comment_list_refresh.setRefreshing(false);
                    }
                }, 500);
            }
        }
    }

    @Override
    public void addComment(Comment comment) {
        if (comment != null) {
            CommentItem commentItem = new CommentItem();
            commentItem.type = CommentAdapter.TYPE_COMMENT_NEW;
            commentItem.comment = comment;

            if (comment_list_refresh.getVisibility() == View.GONE) {
                showCommentContextView();

                if (comment_list_count != null) {
                    count += 1;
                    comment_list_count.setText(MessageFormat.format("书评 {0}", count));
                }

                setCommentFlag(CommentAdapter.TYPE_COMMENT_NEW_FLAG);

                commentItems.add(commentItem);

                setCommentEnd();
            } else {
                int size = commentItems.size();
                if (size > 0) {
                    int index = -1;
                    for (int i = 0; i < size; i++) {
                        if (commentItems.get(i).type == CommentAdapter.TYPE_COMMENT_NEW) {
                            index = i;
                            break;
                        }
                    }
                    if (index != -1) {
                        if (comment_list_count != null) {
                            count += 1;
                            comment_list_count.setText(MessageFormat.format("书评 {0}", count));
                        }
                        commentItems.add(index, commentItem);
                    } else {
                        if (commentItems.get(size - 1).type == CommentAdapter.TYPE_COMMENT_END) {
                            setCommentFlag(size - 1, CommentAdapter.TYPE_COMMENT_NEW_FLAG);
                            commentItems.add(commentItems.size() - 1, commentItem);
                        }
                    }
                }
            }
            commentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void deleteCommentAction(Comment comment, int position) {
        if (position > -1 && position < commentItems.size()) {
            if (comment_list_count != null) {
                count -= 1;
                comment_list_count.setText(MessageFormat.format("书评 {0}", count));
            }
            commentItems.remove(position);
            commentAdapter.notifyItemRemoved(position);
        }

        int comment_new_count = 0;
        int comment_hot_count = 0;
        int comment_count = 0;

        CommentItem commentItemNew = null;
        CommentItem commentItemHot = null;
        CommentItem commentItemEnd = null;

        for (int i = 0; i < commentItems.size(); i++) {
            if (commentItems.get(i).type == CommentAdapter.TYPE_COMMENT_HOT) {
                comment_count += 1;
                comment_hot_count += 1;
            } else if (commentItems.get(i).type == CommentAdapter.TYPE_COMMENT_NEW) {
                comment_count += 1;
                comment_new_count += 1;
            } else if (commentItems.get(i).type == CommentAdapter.TYPE_COMMENT_NEW_FLAG) {
                commentItemNew = commentItems.get(i);
            } else if (commentItems.get(i).type == CommentAdapter.TYPE_COMMENT_HOT_FLAG) {
                commentItemHot = commentItems.get(i);
            } else if (commentItems.get(i).type == CommentAdapter.TYPE_COMMENT_END) {
                commentItemEnd = commentItems.get(i);
            }
        }

        if (comment_hot_count == 0 && commentItemHot != null) {
            commentItems.remove(commentItemHot);
        }
        if (comment_new_count == 0 && commentItemNew != null) {
            commentItems.remove(commentItemNew);
        }
        if (comment_count == 0 && commentItemEnd != null) {
            commentItems.remove(commentItemEnd);
            showCommentEmptyView();
        }
        commentAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.comment_list_back, R.id.comment_list_write})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.comment_list_back:
                if (ActivityStackManager.getActivityStackManager().getActivities() == 1) {
                    Intent intent = new Intent();
                    intent.setClass(this, MainActivity.class);
                    startActivity(intent);
                }
                finish();
                break;
            case R.id.comment_list_write:
                if (UserDao.checkUserLogin()) {
                    showReviewWriteView();
                } else {
                    startLoginActivity();
                }
                break;
        }
    }

    @Override
    public void onReviewWriteSentClick(String content) {
        if (book != null && !TextUtils.isEmpty(book.id) && book.author != null && !TextUtils.isEmpty(book.author.id)) {
            if (TextUtils.isEmpty(content)) {
                showToast("书评内容为空，请填写评论后重试！");
            } else {
                commentListPresenter.publishBookComment(book.id, book.author.id, content);
            }
        } else {
            showToast("参数错误，请返回重试！");
        }
    }

    @Override
    public void hideReviewWriteDialog() {
        if (customReviewWriteDialog != null && !isFinishing()) {
            customReviewWriteDialog.dismiss();
        }
    }

    @Override
    public void setCommentEnd() {
        CommentItem commentItem = new CommentItem();
        commentItem.type = CommentAdapter.TYPE_COMMENT_END;
        commentItems.add(commentItem);
        commentAdapter.notifyDataSetChanged();
    }

    @Override
    public void setCommentFlag(int type) {
        CommentItem commentItem = new CommentItem();
        commentItem.type = type;
        commentItems.add(commentItem);
        commentAdapter.notifyDataSetChanged();
    }

    public void setCommentFlag(int index, int type) {
        CommentItem commentItem = new CommentItem();
        commentItem.type = type;
        commentItems.add(index, commentItem);
        commentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onReviewReplyClicked(Comment comment, String content) {
        if (comment != null && !TextUtils.isEmpty(comment.id) && comment.sender != null && !TextUtils.isEmpty(comment.sender.id) && !TextUtils.isEmpty(id_book)) {
            if (TextUtils.isEmpty(content)) {
                showToast("评论内容为空，请填写评论后重试！");
            } else {
                commentListPresenter.publishReviewReply(comment, id_book, content);
            }
        } else {
            showToast("参数错误，请返回重试！");
        }
    }


    @Override
    public void hideReviewReplyDialog() {
        if (customReviewReplyDialog != null && !isFinishing()) {
            customReviewReplyDialog.dismiss();
        }
    }

    @Override
    public void addCommentReply(Comment comment) {
        if (comment != null && !TextUtils.isEmpty(comment.id) && comment.replies != null && comment.replies.size() > 0) {
            for (CommentItem commentItem : commentItems) {
                if (commentItem.comment != null) {
                    if (commentItem.comment.id.equals(comment.id)) {
                        if (commentItem.comment.replies != null) {
                            commentItem.comment.replies.addAll(comment.replies);
                        } else {
                            commentItem.comment.replies = comment.replies;
                        }
                        break;
                    }
                }
            }
            commentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void deleteCommentReplyAction(Comment comment, CommentReply commentReply, int position) {
        if (comment != null && !TextUtils.isEmpty(comment.id) && comment.replies != null && comment.replies.size() > 0) {
            for (CommentItem commentItem : commentItems) {
                if (commentItem.comment != null) {
                    if (commentItem.comment.id.equals(comment.id)) {
                        if (position > -1 && position < comment.replies.size()) {
                            commentItem.comment.replies.remove(position);
                        }
                        break;
                    }
                }
            }
            commentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCommentReplySentClicked(Comment comment, CommentUser commentUser, String content) {
        if (comment != null && !TextUtils.isEmpty(comment.id) && commentUser != null && !TextUtils.isEmpty(commentUser.id) && !TextUtils.isEmpty(id_book)) {
            if (TextUtils.isEmpty(content)) {
                showToast("评论内容为空，请填写评论后重试！");
            } else {
                commentListPresenter.publishCommentReply(comment, commentUser, id_book, content);
            }
        } else {
            showToast("参数错误，请返回重试！");
        }
    }

    @Override
    public void hideCommentReplyDialog() {
        if (customCommentReplyDialog != null && !isFinishing()) {
            customCommentReplyDialog.dismiss();
        }
    }

    @Override
    public void commendLike(Comment comment, int position) {
        if (position != -1 && position > -1 && position < commentItems.size()) {
            commentItems.get(position).comment.like_count += 1;
            if (commentLikeDao == null) {
                commentLikeDao = CommentLikeDao.getInstance(this);
            }
            if (!TextUtils.isEmpty(comment.id)) {
                commentLikeDao.insertComment(comment.id);
            }
            commentAdapter.notifyItemChanged(position);
        }
    }

    @Override
    public void commendDislike(Comment comment, int position) {
        if (position != -1 && position > -1 && position < commentItems.size()) {
            commentItems.get(position).comment.like_count -= 1;
            if (commentLikeDao == null) {
                commentLikeDao = CommentLikeDao.getInstance(this);
            }
            if (!TextUtils.isEmpty(comment.id)) {
                commentLikeDao.deleteComment(comment.id);
            }
            commentAdapter.notifyItemChanged(position);
        }
    }

    private void initCommentPopupWindow(Comment comment, View view, int position) {
        if (commentPopupWindow == null) {
            try {
                commentPopupView = View.inflate(this, R.layout.layout_view_comment_option, null);
            } catch (InflateException exception) {
                collectException(exception);
                exception.printStackTrace();
                return;
            }

            if (commentPopupView != null) {
                commentPopupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

                commentPopupWindow = new PopupWindow(commentPopupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                commentPopupWindow.setBackgroundDrawable(new BitmapDrawable());
                commentPopupWindow.setOutsideTouchable(true);

                commentPopupWindowHelp = new CommentPopupWindowHelp(this, commentPopupView, this);
            }
        }

        if (view != null && commentPopupView != null && commentPopupWindowHelp != null) {

            commentPopupWindowHelp.initPopupWindowView(comment, position);

            int popupWidth = commentPopupView.getMeasuredWidth();
            int popupHeight = commentPopupView.getMeasuredHeight();

            int[] location = new int[2];
            view.getLocationOnScreen(location);

            commentPopupWindow.showAtLocation(view, Gravity.NO_GRAVITY, (location[0] + view.getWidth() / 2) - popupWidth / 2, (location[1] - popupHeight) + view.getHeight() / 2);
        }
    }

    private void initReplyPopupWindow(Comment comment, CommentReply commentReply, View view, int position) {
        if (replyPopupWindow == null) {
            try {
                replyPopupView = View.inflate(this, R.layout.layout_view_comment_reply_option, null);
            } catch (InflateException exception) {
                collectException(exception);
                exception.printStackTrace();
                return;
            }

            if (replyPopupView != null) {
                replyPopupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

                replyPopupWindow = new PopupWindow(replyPopupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                replyPopupWindow.setBackgroundDrawable(new BitmapDrawable());
                replyPopupWindow.setOutsideTouchable(true);

                replyPopupWindowHelp = new ReplyPopupWindowHelp(this, replyPopupView, this);
            }
        }

        if (view != null && replyPopupView != null && replyPopupWindowHelp != null) {

            replyPopupWindowHelp.initPopupWindowView(comment, commentReply, position);

            int popupWidth = replyPopupView.getMeasuredWidth();
            int popupHeight = replyPopupView.getMeasuredHeight();

            int[] location = new int[2];
            view.getLocationOnScreen(location);

            replyPopupWindow.showAtLocation(view, Gravity.NO_GRAVITY, (location[0] + view.getWidth() / 2) - popupWidth / 2, (location[1] - popupHeight) + view.getHeight() / 2);
        }
    }

    @Override
    public void commentLikeAction(Comment comment, int position) {
        if (book != null && !TextUtils.isEmpty(book.id) && comment != null && !TextUtils.isEmpty(comment.id)) {
            commentListPresenter.commentCommendAction(id_book, comment, true, position);
        }
    }

    @Override
    public void commentDislikeAction(Comment comment, int position) {
        if (book != null && !TextUtils.isEmpty(book.id) && comment != null && !TextUtils.isEmpty(comment.id)) {
            commentListPresenter.commentCommendAction(id_book, comment, false, position);
        }
    }

    @Override
    public void showReviewWriteView() {
        if (customReviewWriteDialog == null) {
            customReviewWriteDialog = new CustomReviewWriteDialog();
            customReviewWriteDialog.setOnReviewWriteSentClickListener(this);
        }

        if (customReviewWriteDialog != null && !isFinishing()) {
            if (customReviewWriteDialog.isAdded()) {
                customReviewWriteDialog.setShowsDialog(true);
            } else {
                customReviewWriteDialog.show(getSupportFragmentManager(), "CustomReviewWriteDialog");
            }
        }
    }

    @Override
    public void showReviewReplyView(Comment comment) {
        if (customReviewReplyDialog == null) {
            customReviewReplyDialog = new CustomReviewReplyDialog();
            customReviewReplyDialog.setOnReviewReplySentClickListener(this);
        }

        customReviewReplyDialog.setReplyCommentData(comment);

        if (customReviewReplyDialog != null && !isFinishing()) {
            if (customReviewReplyDialog.isAdded()) {
                customReviewReplyDialog.setShowsDialog(true);
            } else {
                customReviewReplyDialog.show(getSupportFragmentManager(), "customReviewReplyDialog");
            }
        }
    }

    @Override
    public void showCommentReplyView(Comment comment, CommentReply commentReply) {
        if (customCommentReplyDialog == null) {
            customCommentReplyDialog = new CustomCommentReplyDialog();
            customCommentReplyDialog.setOnCommentReplySentClickListener(this);
        }

        customCommentReplyDialog.setReplyUser(commentReply.sender);
        customCommentReplyDialog.setReplyCommentData(comment);

        if (customCommentReplyDialog != null && !isFinishing()) {
            if (customCommentReplyDialog.isAdded()) {
                customCommentReplyDialog.setShowsDialog(true);
            } else {
                customCommentReplyDialog.show(getSupportFragmentManager(), "CustomCommentReplyDialog");
            }
        }
    }

    @Override
    public void clickedReview(Comment comment, View view, int position) {
        if (comment != null && comment.sender != null) {
            initCommentPopupWindow(comment, view, position);
        } else {
            showToast("参数错误！");
        }
    }

    @Override
    public void clickedCommentReply(Comment comment, CommentReply commentReply, View view, int position) {
        if (comment != null && commentReply != null && commentReply.sender != null) {
            initReplyPopupWindow(comment, commentReply, view, position);
        } else {
            showToast("参数错误！");
        }
    }

    @Override
    public void reportComment(Comment comment) {
        if (comment != null && !TextUtils.isEmpty(comment.id)) {
            commentListPresenter.reportComment(comment);
        } else {
            showToast("参数错误！");
        }
    }

    @Override
    public void reportCommentReply(Comment comment, CommentReply commentReply) {
        if (comment != null && !TextUtils.isEmpty(comment.id) && commentReply != null) {
            commentListPresenter.reportCommentReply(comment, commentReply);
        } else {
            showToast("参数错误！");
        }
    }

    @Override
    public void deleteComment(final Comment comment, final int position) {
        if (customInformationDialog == null) {
            customInformationDialog = new CustomInformationDialog();
        }

        customInformationDialog.setPrompt("是否确定删除评论内容？");
        customInformationDialog.setInformationClickListener(new CustomInformationListener() {
            @Override
            public void onCancelClicked() {
                hideCustomInformationDialog();
            }

            @Override
            public void onConfirmClicked() {
                if (customInformationDialog.getShowsDialog()) {
                    if (!TextUtils.isEmpty(id_book) && comment != null && !TextUtils.isEmpty(comment.id)) {
                        commentListPresenter.deleteCommentRequest(id_book, comment, position);
                    } else {
                        showToast("参数错误！");
                    }
                    customInformationDialog.dismiss();
                }
            }
        });

        if (!isFinishing()) {
            if (customInformationDialog.isAdded()) {
                customInformationDialog.setShowsDialog(true);
            } else {
                customInformationDialog.show(getSupportFragmentManager(), "CustomInformationDialog");
            }

        }
    }


    @Override
    public void deleteCommentReply(final Comment comment, final CommentReply commentReply, final int position) {
        if (customInformationDialog == null) {
            customInformationDialog = new CustomInformationDialog();
        }

        customInformationDialog.setPrompt("是否确定删除评论内容？");
        customInformationDialog.setInformationClickListener(new CustomInformationListener() {
            @Override
            public void onCancelClicked() {
                hideCustomInformationDialog();
            }

            @Override
            public void onConfirmClicked() {
                if (customInformationDialog.getShowsDialog()) {
                    if (!TextUtils.isEmpty(id_book) && comment != null && !TextUtils.isEmpty(comment.id) && commentReply != null) {
                        commentListPresenter.deleteCommentReplyRequest(id_book, comment, commentReply, position);
                    } else {
                        showToast("参数错误！");
                    }
                    customInformationDialog.dismiss();
                }
            }
        });

        if (!isFinishing()) {
            if (customInformationDialog.isAdded()) {
                customInformationDialog.setShowsDialog(true);
            } else {
                customInformationDialog.show(getSupportFragmentManager(), "CustomInformationDialog");
            }

        }
    }

    @Override
    public void startLoginActivity() {
        startActivity(new Intent(this, WXEntryActivity.class));
    }

    @Override
    // TODO
    public void startCatalogActivity() {
//        if (book != null) {
//            Intent catalogIntent = new Intent(this, CatalogActivity.class);
//            catalogIntent.putExtra("book", book);
//            startActivity(catalogIntent);
//        }
    }

    @Override
    public void startCommentDetailsActivity(String id) {
        if (!TextUtils.isEmpty(id_book) && !TextUtils.isEmpty(id)) {
            Intent intent = new Intent();
            intent.setClass(CommentListActivity.this, CommentDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("id_book", id_book);
            bundle.putString("id_comment", id);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void startCommentListActivity() {

    }

    @Override
    public void hideCommentPopupWindow() {
        if (commentPopupWindow != null && commentPopupWindow.isShowing()) {
            commentPopupWindow.dismiss();
        }
    }

    @Override
    public void hideReplyPopupWindow() {
        if (replyPopupWindow != null && replyPopupWindow.isShowing()) {
            replyPopupWindow.dismiss();
        }
    }

    public void hideCustomInformationDialog() {
        if (!isFinishing() && customInformationDialog != null && customInformationDialog.getShowsDialog()) {
            customInformationDialog.dismiss();
        }
    }
}