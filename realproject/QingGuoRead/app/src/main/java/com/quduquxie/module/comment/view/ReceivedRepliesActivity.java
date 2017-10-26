package com.quduquxie.module.comment.view;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.quduquxie.R;
import com.quduquxie.base.util.TypefaceUtil;
import com.quduquxie.communal.widget.CustomCommentActionDialog;
import com.quduquxie.model.Review;
import com.quduquxie.modular.cover.view.CoverActivity;
import com.quduquxie.module.comment.adapter.ReceivedRepliesAdapter;
import com.quduquxie.module.comment.ReceivedRepliesInterface;
import com.quduquxie.module.comment.presenter.ReceivedRepliesPresenter;
import com.quduquxie.view.BaseActivity;
import com.quduquxie.widget.LoadingPage;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 17/2/28.
 * Created by crazylei.
 */

public class ReceivedRepliesActivity extends BaseActivity implements ReceivedRepliesInterface.View, ReceivedRepliesAdapter.OnReplyClickedListener, CustomCommentActionDialog.OnCommentActionReplySentClickListener {

    @BindView(R.id.common_head_back)
    public ImageView common_head_back;
    @BindView(R.id.common_head_title)
    public TextView common_head_title;
    @BindView(R.id.received_replies_loading)
    public FrameLayout received_replies_loading;
    @BindView(R.id.received_replies_refresh)
    public SwipeRefreshLayout received_replies_refresh;
    @BindView(R.id.received_replies_content)
    public RecyclerView received_replies_content;
    @BindView(R.id.comment_empty_view)
    public RelativeLayout comment_empty_view;
    @BindView(R.id.comment_empty_prompt)
    public TextView comment_empty_prompt;

    private ReceivedRepliesInterface.Presenter receivedRepliesPresenter;

    private int page = 1;

    private LoadingPage loadingPage;

    private ArrayList<Review> receivedReplies = new ArrayList<>();

    private LinearLayoutManager linearLayoutManager;
    private ReceivedRepliesAdapter receivedRepliesAdapter;

    private CustomCommentActionDialog customCommentActionDialog;

    private Toast toast;

    private Typeface typeface_song_depict;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.layout_activity_received_replies);
        } catch (Resources.NotFoundException expression) {
            collectException(expression);
            expression.printStackTrace();
        }

        ButterKnife.bind(this);

        typeface_song_depict = TypefaceUtil.loadTypeface(this, TypefaceUtil.TYPEFACE_SONG_DEPICT);

        receivedRepliesPresenter = new ReceivedRepliesPresenter(this);
        receivedRepliesPresenter.initParameter(page);
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
    public void setPresenter(ReceivedRepliesInterface.Presenter receivedCommentPresenter) {
        this.receivedRepliesPresenter = receivedCommentPresenter;
    }

    @Override
    public void initView() {
        if (common_head_title != null) {
            common_head_title.setText("收到的评论");
            common_head_title.setTypeface(typeface_song_depict);
        }

        if (comment_empty_view != null) {
            comment_empty_view.setVisibility(View.GONE);
        }

        if (comment_empty_prompt != null) {
            comment_empty_prompt.setText("要多多和书友分享你的想法哦~");
        }

        linearLayoutManager = new LinearLayoutManager(ReceivedRepliesActivity.this, LinearLayoutManager.VERTICAL, false);
        receivedRepliesAdapter = new ReceivedRepliesAdapter(this, receivedReplies);
        receivedRepliesAdapter.setOnReplyClickedListener(this);

        if (received_replies_content != null) {
            received_replies_content.setLayoutManager(linearLayoutManager);
            received_replies_content.setAdapter(receivedRepliesAdapter);

            received_replies_content.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

                    if (!received_replies_refresh.isRefreshing() && receivedRepliesPresenter.isLoadingMoreState() && (visibleItemCount + firstVisiblePosition) == totalItemCount - 5) {
                        setRefreshViewState(true);
                        page += 1;
                        receivedRepliesPresenter.loadReceivedRepliesMore(page);
                    }
                }
            });
        }

        if (received_replies_refresh != null) {
            received_replies_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    setRefreshViewState(false);
                    page = 1;
                    receivedRepliesPresenter.loadReceivedReplies(page);
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
            loadingPage = new LoadingPage(this, received_replies_loading);
        }
        receivedRepliesPresenter.setLoadingPage(loadingPage);
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
    public void setReceivedReplies(ArrayList<Review> receivedReplyList) {
        if (receivedReplyList == null || receivedReplyList.size() == 0) {
            showEmptyView();
        } else {
            showContentView();

            if (receivedReplies == null) {
                receivedReplies = new ArrayList<>();
            } else {
                receivedReplies.clear();
            }

            for (Review review : receivedReplyList) {
                receivedReplies.add(review);
            }

            if (receivedRepliesAdapter == null) {
                receivedRepliesAdapter = new ReceivedRepliesAdapter(this, receivedReplies);
                receivedRepliesAdapter.setOnReplyClickedListener(this);
                if (received_replies_content != null) {
                    received_replies_content.setAdapter(receivedRepliesAdapter);
                }
            } else {
                receivedRepliesAdapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    public void setReceivedRepliesMore(ArrayList<Review> receivedReplyList) {
        if (receivedReplyList != null && receivedReplyList.size() > 0) {

            for (Review review : receivedReplyList) {
                receivedReplies.add(review);
            }

            if (receivedRepliesAdapter == null) {
                receivedRepliesAdapter = new ReceivedRepliesAdapter(this, receivedReplies);
                receivedRepliesAdapter.setOnReplyClickedListener(this);
                if (received_replies_content != null) {
                    received_replies_content.setAdapter(receivedRepliesAdapter);
                }
            } else {
                receivedRepliesAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void setListEnd() {
        Review review = new Review();
        review.type = ReceivedRepliesAdapter.TYPE_LIST_END;
        receivedReplies.add(review);
        receivedRepliesAdapter.notifyDataSetChanged();
    }

    @Override
    public void setRefreshViewState(boolean state) {
        if (received_replies_refresh == null) {
            return;
        }
        if (state) {
            received_replies_refresh.setRefreshing(true);
        } else {
            if (received_replies_refresh.isRefreshing()) {
                received_replies_refresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        received_replies_refresh.setRefreshing(false);
                    }
                }, 500);
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
    public void hideCommentActionDialog() {
        if (customCommentActionDialog != null) {
            customCommentActionDialog.dismiss();
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
    public void startCoverActivity(String id_book) {
        if (!TextUtils.isEmpty(id_book)) {
            Intent intent = new Intent();
            intent.setClass(ReceivedRepliesActivity.this, CoverActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("id", id_book);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void startCommentDetailsActivity(String id_book, String id_comment) {
        if (!TextUtils.isEmpty(id_book) && !TextUtils.isEmpty(id_comment)) {
            Intent intent = new Intent();
            intent.setClass(ReceivedRepliesActivity.this, CommentDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("id_book", id_book);
            bundle.putString("id_comment", id_comment);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void onClickedReceivedComment(Review review) {

        if (customCommentActionDialog == null) {
            customCommentActionDialog = new CustomCommentActionDialog();
            customCommentActionDialog.setOnCommentActionSentClickListener(this);
        }

        customCommentActionDialog.setCommentActionData(review);

        if (!isFinishing()) {
            customCommentActionDialog.show(getSupportFragmentManager(), "CustomCommentReplyDialog");
        }
    }

    private void showEmptyView() {
        if (received_replies_content != null) {
            received_replies_content.setVisibility(View.GONE);
        }

        if (comment_empty_view != null) {
            comment_empty_view.setVisibility(View.VISIBLE);
        }

        if (received_replies_refresh != null) {
            if (received_replies_refresh.isRefreshing()) {
                setRefreshViewState(false);
            }
            received_replies_refresh.setEnabled(false);
        }
    }

    private void showContentView() {
        if (comment_empty_view != null) {
            comment_empty_view.setVisibility(View.GONE);
        }

        if (received_replies_content != null) {
            received_replies_content.setVisibility(View.VISIBLE);
        }

        if (received_replies_refresh != null) {
            received_replies_refresh.setEnabled(true);
        }
    }

    @Override
    public void onCommentActionReplySentClicked(Review review, String content) {
        if (review != null && review.book != null && !TextUtils.isEmpty(review.book.id) && review.comments != null && !TextUtils.isEmpty(review.comments.id) && review.sender != null && !TextUtils.isEmpty(review.sender.id)) {
            if (TextUtils.isEmpty(content)) {
                showToast("评论内容为空，请填写内容后重试！");
            } else {
                receivedRepliesPresenter.publishCommentReply(review, content);
            }
        } else {
            showToast("参数错误，请返回重试！");
        }
    }
}