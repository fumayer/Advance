package com.quduquxie.module.comment.view;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.quduquxie.base.util.TypefaceUtil;
import com.quduquxie.communal.help.comment.CommentPopupWindowHelp;
import com.quduquxie.communal.help.comment.ReplyPopupWindowHelp;
import com.quduquxie.communal.widget.CustomCommentReplyDialog;
import com.quduquxie.communal.widget.CustomInformationDialog;
import com.quduquxie.communal.widget.CustomInformationListener;
import com.quduquxie.communal.widget.CustomReviewReplyDialog;
import com.quduquxie.db.CommentLikeDao;
import com.quduquxie.model.Comment;
import com.quduquxie.model.CommentReply;
import com.quduquxie.model.CommentUser;
import com.quduquxie.module.comment.CommentDetailsInterface;
import com.quduquxie.module.comment.adapter.CommentReplyAdapter;
import com.quduquxie.module.comment.listener.CommentItemListener;
import com.quduquxie.module.comment.presenter.CommentDetailsPresenter;
import com.quduquxie.view.BaseActivity;
import com.quduquxie.widget.LoadingPage;
import com.quduquxie.wxapi.WXEntryActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 17/3/1.
 * Created by crazylei.
 */

public class CommentDetailsActivity extends BaseActivity implements CommentDetailsInterface.View, CommentItemListener, CustomReviewReplyDialog.OnReviewReplySentClickListener, CustomCommentReplyDialog.OnCommentReplySentClickListener {

    @BindView(R.id.common_head_back)
    public ImageView common_head_back;
    @BindView(R.id.common_head_title)
    public TextView common_head_title;
    @BindView(R.id.comment_details_loading)
    public FrameLayout comment_details_loading;
    @BindView(R.id.comment_details_refresh)
    public SwipeRefreshLayout comment_details_refresh;
    @BindView(R.id.comment_details_empty)
    public RelativeLayout comment_details_empty;
    @BindView(R.id.comment_details_empty_prompt)
    public TextView comment_details_empty_prompt;

    @BindView(R.id.comment_details_replies)
    public RecyclerView comment_details_replies;

    private String id_book;
    private String id_comment;

    private CommentDetailsInterface.Presenter commentDetailsPresenter;

    private CommentReplyAdapter commentReplyAdapter;

    private ArrayList<CommentReply> commentReplies = new ArrayList<>();

    private LoadingPage loadingPage;

    private CustomReviewReplyDialog customReviewReplyDialog;
    private CustomCommentReplyDialog customCommentReplyDialog;

    private Toast toast;

    private View commentPopupView;
    private PopupWindow commentPopupWindow;
    private CommentPopupWindowHelp commentPopupWindowHelp;

    private View replyPopupView;
    private PopupWindow replyPopupWindow;
    private ReplyPopupWindowHelp replyPopupWindowHelp;

    private CommentLikeDao commentLikeDao;

    private CustomInformationDialog customInformationDialog;

    private Typeface typeface_song_depict;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.layout_activity_comment_details);
        } catch (Resources.NotFoundException exception) {
            collectException(exception);
            exception.printStackTrace();
        }

        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            this.id_book = intent.getStringExtra("id_book");
            this.id_comment = intent.getStringExtra("id_comment");
        }

        typeface_song_depict = TypefaceUtil.loadTypeface(this, TypefaceUtil.TYPEFACE_SONG_DEPICT);

        this.commentLikeDao = CommentLikeDao.getInstance(this);

        commentDetailsPresenter = new CommentDetailsPresenter(this);
        commentDetailsPresenter.initParameter(id_book, id_comment);
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
    }

    @Override
    public void setPresenter(CommentDetailsInterface.Presenter commentDetailsPresenter) {
        this.commentDetailsPresenter = commentDetailsPresenter;
    }

    @Override
    public void initView() {
        if (common_head_title != null) {
            common_head_title.setText("评论详情");
            common_head_title.setTypeface(typeface_song_depict);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CommentDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
        commentReplyAdapter = new CommentReplyAdapter(this, commentReplies);
        commentReplyAdapter.setCommentItemListener(this);

        if (comment_details_replies != null) {
            comment_details_replies.setLayoutManager(linearLayoutManager);
            comment_details_replies.setAdapter(commentReplyAdapter);
        }

        if (comment_details_refresh != null) {
            comment_details_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    setRefreshViewState(false);
                    commentDetailsPresenter.loadCommentDetails(id_book, id_comment);
                }
            });
        }
    }

    @Override
    public void showLoadingPage() {
        if (loadingPage != null) {
            loadingPage.onSuccess();
        }

        if (loadingPage == null) {
            loadingPage = new LoadingPage(this, comment_details_loading);
        }
        commentDetailsPresenter.setLoadingPage(loadingPage);
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
            loadingPage.onSuccess();
        }
    }

    @OnClick({R.id.common_head_back})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.common_head_back:
                finish();
                break;
        }
    }

    @Override
    public void setCommentDetails(Comment comment) {
        if (comment != null) {

            showCommentDetailsContent();

            if (commentReplyAdapter == null) {
                commentReplyAdapter = new CommentReplyAdapter(this, commentReplies);
                commentReplyAdapter.setCommentItemListener(this);

                if (comment_details_replies != null) {
                    comment_details_replies.setAdapter(commentReplyAdapter);
                }
            }

            CommentReply commentReply = new CommentReply();
            commentReply.item_type = CommentReplyAdapter.TYPE_REPLY_COMMENT;
            commentReplies.add(commentReply);

            commentReplyAdapter.setCommentInformation(comment);
            commentReplyAdapter.notifyDataSetChanged();

            if (comment.replies != null && comment.replies.size() > 0) {
                for (int i = 0; i < comment.replies.size(); i++) {
                    commentReplies.add(comment.replies.get(i));
                }
                commentReplyAdapter.notifyDataSetChanged();
            }
        } else {
            showCommentDetailsEmpty();
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
    public void commendLike(Comment comment, int position) {
        if (position != -1 && position > -1 && position < commentReplies.size()) {
            comment.like_count += 1;
            if (commentLikeDao == null) {
                commentLikeDao = CommentLikeDao.getInstance(this);
            }
            if (!TextUtils.isEmpty(comment.id)) {
                commentLikeDao.insertComment(comment.id);
            }
            commentReplyAdapter.setCommentInformation(comment);
            commentReplyAdapter.notifyItemChanged(position);
        }
    }

    @Override
    public void commendDislike(Comment comment, int position) {
        if (position != -1 && position > -1 && position < commentReplies.size()) {
            comment.like_count -= 1;
            if (commentLikeDao == null) {
                commentLikeDao = CommentLikeDao.getInstance(this);
            }
            if (!TextUtils.isEmpty(comment.id)) {
                commentLikeDao.deleteComment(comment.id);
            }
            commentReplyAdapter.setCommentInformation(comment);
            commentReplyAdapter.notifyItemChanged(position);
        }
    }

    @Override
    public void addCommentReply(ArrayList<CommentReply> commentReplyList) {
        if (commentReplyList != null && commentReplyList.size() > 0) {
            for (int i = 0; i < commentReplyList.size(); i++) {
                commentReplies.add(commentReplyList.get(i));
            }
            commentReplyAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void deleteCommentReply(CommentReply commentReply) {
        if (commentReply != null && commentReply.sn != 0) {
            int index = -1;
            for (int i = 0; i < commentReplies.size(); i++) {
                if (commentReplies.get(i).sn == commentReply.sn) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                commentReplies.remove(index);
                commentReplyAdapter.notifyItemRemoved(index);
            }
        }
    }

    @Override
    public void deleteCommentAction(Comment comment) {
        showCommentDetailsEmpty();
    }

    @Override
    public void setRefreshViewState(boolean state) {
        if (comment_details_refresh == null) {
            return;
        }
        if (state) {
            comment_details_refresh.setRefreshing(true);
        } else {
            if (comment_details_refresh.isRefreshing()) {
                comment_details_refresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        comment_details_refresh.setRefreshing(false);
                    }
                }, 500);
            }
        }
    }

    @Override
    public void hideReviewReplyDialog() {
        if (customReviewReplyDialog != null) {
            customReviewReplyDialog.dismiss();
        }
    }

    @Override
    public void onReviewReplyClicked(Comment comment, String content) {
        if (comment != null && !TextUtils.isEmpty(comment.id) && comment.sender != null && !TextUtils.isEmpty(comment.sender.id) && !TextUtils.isEmpty(id_book)) {
            if (TextUtils.isEmpty(content)) {
                showToast("评论内容为空，请填写评论后重试！");
            } else {
                commentDetailsPresenter.publishReviewReply(comment, id_book, content);
            }
        } else {
            showToast("参数错误，请返回重试！");
        }
    }

    @Override
    public void hideCommentReplyDialog() {
        if (customCommentReplyDialog != null) {
            customCommentReplyDialog.dismiss();
        }
    }

    @Override
    public void onCommentReplySentClicked(Comment comment, CommentUser commentUser, String content) {
        if (comment != null && !TextUtils.isEmpty(comment.id) && commentUser != null && !TextUtils.isEmpty(commentUser.id) && !TextUtils.isEmpty(id_book)) {
            if (TextUtils.isEmpty(content)) {
                showToast("评论内容为空，请填写评论后重试！");
            } else {
                commentDetailsPresenter.publishCommentReply(comment, commentUser, id_book, content);
            }
        } else {
            showToast("参数错误，请返回重试！");
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

    private void initReplyPopupWindow(final Comment comment, final CommentReply commentReply, View view, int position) {
        if (replyPopupView == null) {
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
        if (!TextUtils.isEmpty(id_book) && comment != null && !TextUtils.isEmpty(comment.id)) {
            commentDetailsPresenter.commentCommendAction(id_book, comment, true, position);
        } else {
            showToast("参数错误！");
        }
    }

    @Override
    public void commentDislikeAction(Comment comment, int position) {
        if (!TextUtils.isEmpty(id_book) && comment != null && !TextUtils.isEmpty(comment.id)) {
            commentDetailsPresenter.commentCommendAction(id_book, comment, false, position);
        } else {
            showToast("参数错误！");
        }
    }

    @Override
    public void showReviewWriteView() {

    }

    @Override
    public void showReviewReplyView(Comment comment) {
        if (customReviewReplyDialog == null) {
            customReviewReplyDialog = new CustomReviewReplyDialog();
            customReviewReplyDialog.setOnReviewReplySentClickListener(this);
        }

        customReviewReplyDialog.setReplyCommentData(comment);

        if (!isFinishing() && customReviewReplyDialog != null) {
            customReviewReplyDialog.show(getSupportFragmentManager(), "setOnReviewReplySentClickListener");
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
            customCommentReplyDialog.show(getSupportFragmentManager(), "CustomCommentReplyDialog");
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
            commentDetailsPresenter.reportComment(comment);
        } else {
            showToast("参数错误！");
        }
    }

    @Override
    public void reportCommentReply(Comment comment, CommentReply commentReply) {
        if (comment != null && !TextUtils.isEmpty(comment.id)) {
            commentDetailsPresenter.reportCommentReply(comment, commentReply);
        } else {
            showToast("参数错误！");
        }
    }

    @Override
    public void deleteComment(final Comment comment, int position) {
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
                        commentDetailsPresenter.deleteCommentRequest(id_book, comment);
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
    public void deleteCommentReply(final Comment comment, final CommentReply commentReply, int position) {
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
                        commentDetailsPresenter.deleteCommentReply(id_book, comment, commentReply);
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
        startActivity(new Intent(CommentDetailsActivity.this, WXEntryActivity.class));
    }

    @Override
    public void startCatalogActivity() {

    }

    @Override
    public void startCommentDetailsActivity(String id) {

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

    private void showCommentDetailsContent() {
        if (comment_details_empty != null) {
            comment_details_empty.setVisibility(View.GONE);
        }
        if (comment_details_refresh != null) {
            comment_details_refresh.setEnabled(true);
            comment_details_refresh.setVisibility(View.VISIBLE);
        }

        if (commentReplies == null) {
            commentReplies = new ArrayList<>();
        } else {
            commentReplies.clear();
        }
    }

    private void showCommentDetailsEmpty() {
        if (comment_details_refresh != null) {
            setRefreshViewState(false);
            comment_details_refresh.setEnabled(false);
            comment_details_refresh.setVisibility(View.GONE);
        }
        if (comment_details_empty != null) {
            comment_details_empty.setVisibility(View.VISIBLE);
        }
    }
}
