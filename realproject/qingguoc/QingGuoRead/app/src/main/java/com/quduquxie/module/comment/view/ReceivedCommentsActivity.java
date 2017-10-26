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
import com.quduquxie.module.comment.ReceivedCommentsInterface;
import com.quduquxie.module.comment.adapter.ReceivedCommentsAdapter;
import com.quduquxie.module.comment.presenter.ReceivedCommentsPresenter;
import com.quduquxie.view.BaseActivity;
import com.quduquxie.widget.LoadingPage;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 17/3/1.
 * Created by crazylei.
 */

public class ReceivedCommentsActivity extends BaseActivity implements ReceivedCommentsInterface.View, ReceivedCommentsAdapter.OnCommentClickedListener, CustomCommentActionDialog.OnCommentActionReplySentClickListener {

    @BindView(R.id.common_head_back)
    public ImageView common_head_back;
    @BindView(R.id.common_head_title)
    public TextView common_head_title;
    @BindView(R.id.received_reviews_loading)
    public FrameLayout received_reviews_loading;
    @BindView(R.id.received_comments_refresh)
    public SwipeRefreshLayout received_comments_refresh;
    @BindView(R.id.received_comments_content)
    public RecyclerView received_comments_content;
    @BindView(R.id.comment_empty_view)
    public RelativeLayout comment_empty_view;
    @BindView(R.id.comment_empty_prompt)
    public TextView comment_empty_prompt;

    private ReceivedCommentsInterface.Presenter receivedCommentsPresenter;

    private int page = 1;

    private LoadingPage loadingPage;

    private ArrayList<Review> receivedComments = new ArrayList<>();

    private LinearLayoutManager linearLayoutManager;
    private ReceivedCommentsAdapter receivedCommentsAdapter;

    private CustomCommentActionDialog customCommentActionDialog;

    private Toast toast;

    private Typeface typeface_song_depict;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.layout_activity_received_comments);
        } catch (Resources.NotFoundException exception) {
            collectException(exception);
            exception.printStackTrace();
        }

        ButterKnife.bind(this);

        typeface_song_depict = TypefaceUtil.loadTypeface(this, TypefaceUtil.TYPEFACE_SONG_DEPICT);

        receivedCommentsPresenter = new ReceivedCommentsPresenter(this);
        receivedCommentsPresenter.initParameter(page);
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
    public void setPresenter(ReceivedCommentsInterface.Presenter receivedCommentsPresenter) {
        this.receivedCommentsPresenter = receivedCommentsPresenter;
    }

    @Override
    public void initView() {
        if (common_head_title != null) {
            common_head_title.setText("收到的书评");
            common_head_title.setTypeface(typeface_song_depict);
        }

        if (comment_empty_view != null) {
            comment_empty_view.setVisibility(View.GONE);
        }

        if (comment_empty_prompt != null) {
            comment_empty_prompt.setText("还没有书评哦，快去找小伙伴讨论");
        }

        linearLayoutManager = new LinearLayoutManager(ReceivedCommentsActivity.this, LinearLayoutManager.VERTICAL, false);
        receivedCommentsAdapter = new ReceivedCommentsAdapter(this, receivedComments);
        receivedCommentsAdapter.setOnCommentClickedListener(this);

        if (received_comments_content != null) {
            received_comments_content.setLayoutManager(linearLayoutManager);
            received_comments_content.setAdapter(receivedCommentsAdapter);

            received_comments_content.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

                    if (!received_comments_refresh.isRefreshing() && receivedCommentsPresenter.isLoadingMoreState() && (visibleItemCount + firstVisiblePosition) == totalItemCount - 5) {
                        setRefreshViewState(true);
                        page += 1;
                        receivedCommentsPresenter.loadReceivedCommentsMore(page);
                    }
                }
            });
        }

        if (received_comments_refresh != null) {
            received_comments_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    setRefreshViewState(false);
                    page = 1;
                    receivedCommentsPresenter.loadReceivedComments(page);
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
            loadingPage = new LoadingPage(this, received_reviews_loading);
        }
        receivedCommentsPresenter.setLoadingPage(loadingPage);
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
    public void setReceivedComments(ArrayList<Review> receivedReviewList) {
        if (receivedReviewList == null || receivedReviewList.size() == 0) {
            showEmptyView();
        } else {
            showContentView();

            if (receivedComments == null) {
                receivedComments = new ArrayList<>();
            } else {
                receivedComments.clear();
            }

            for (Review review : receivedReviewList) {
                receivedComments.add(review);
            }

            if (receivedCommentsAdapter == null) {
                receivedCommentsAdapter = new ReceivedCommentsAdapter(this, receivedComments);
                receivedCommentsAdapter.setOnCommentClickedListener(this);
                if (received_comments_content != null) {
                    received_comments_content.setAdapter(receivedCommentsAdapter);
                }
            } else {
                receivedCommentsAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void setReceivedCommentsMore(ArrayList<Review> receivedCommentList) {
        if (receivedCommentList != null && receivedCommentList.size() > 0) {

            for (Review review : receivedCommentList) {
                receivedComments.add(review);
            }

            if (receivedCommentsAdapter == null) {
                receivedCommentsAdapter = new ReceivedCommentsAdapter(this, receivedComments);
                receivedCommentsAdapter.setOnCommentClickedListener(this);
                if (received_comments_content != null) {
                    received_comments_content.setAdapter(receivedCommentsAdapter);
                }
            } else {
                receivedCommentsAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void setListEnd() {
        Review review = new Review();
        review.type = ReceivedCommentsAdapter.TYPE_LIST_END;
        receivedComments.add(review);
        receivedCommentsAdapter.notifyDataSetChanged();
    }

    @Override
    public void setRefreshViewState(boolean state) {
        if (received_comments_refresh == null) {
            return;
        }
        if (state) {
            received_comments_refresh.setRefreshing(true);
        } else {
            if (received_comments_refresh.isRefreshing()) {
                received_comments_refresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        received_comments_refresh.setRefreshing(false);
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
            intent.setClass(ReceivedCommentsActivity.this, CoverActivity.class);
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
            intent.setClass(ReceivedCommentsActivity.this, CommentDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("id_book", id_book);
            bundle.putString("id_comment", id_comment);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void onClickedCommentReply(Review review) {
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
        if (received_comments_content != null) {
            received_comments_content.setVisibility(View.GONE);
        }

        if (comment_empty_view != null) {
            comment_empty_view.setVisibility(View.VISIBLE);
        }

        if (received_comments_refresh != null) {
            if (received_comments_refresh.isRefreshing()) {
                setRefreshViewState(false);
            }
            received_comments_refresh.setEnabled(false);
        }
    }

    private void showContentView() {
        if (comment_empty_view != null) {
            comment_empty_view.setVisibility(View.GONE);
        }

        if (received_comments_content != null) {
            received_comments_content.setVisibility(View.VISIBLE);
        }

        if (received_comments_refresh != null) {
            received_comments_refresh.setEnabled(true);
        }
    }

    @Override
    public void onCommentActionReplySentClicked(Review review, String content) {
        if (review != null) {
            if (TextUtils.isEmpty(content)) {
                showToast("评论内容为空，请填写内容后重试！");
            } else {
                receivedCommentsPresenter.publishCommentReply(review, content);
            }
        } else {
            showToast("评论数据错误，请重试！");
        }
    }
}