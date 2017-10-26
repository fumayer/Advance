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
import com.quduquxie.module.comment.UserDynamicInterface;
import com.quduquxie.module.comment.adapter.UserDynamicAdapter;
import com.quduquxie.module.comment.presenter.UserDynamicPresenter;
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

public class UserDynamicActivity extends BaseActivity implements UserDynamicInterface.View, UserDynamicAdapter.OnDynamicClickListener, CustomCommentActionDialog.OnCommentActionReplySentClickListener {

    @BindView(R.id.common_head_back)
    public ImageView common_head_back;
    @BindView(R.id.common_head_title)
    public TextView common_head_title;
    @BindView(R.id.user_dynamic_loading)
    public FrameLayout user_dynamic_loading;
    @BindView(R.id.user_dynamic_refresh)
    public SwipeRefreshLayout user_dynamic_refresh;
    @BindView(R.id.user_dynamic_content)
    public RecyclerView user_dynamic_content;
    @BindView(R.id.user_dynamic_empty_view)
    public RelativeLayout user_dynamic_empty_view;
    @BindView(R.id.user_dynamic_empty_prompt)
    public TextView user_dynamic_empty_prompt;

    private UserDynamicInterface.Presenter userDynamicPresenter;

    private int page = 1;

    private LoadingPage loadingPage;

    private ArrayList<Review> dynamics = new ArrayList<>();

    private LinearLayoutManager linearLayoutManager;
    private UserDynamicAdapter userDynamicAdapter;

    private Toast toast;

    private CustomCommentActionDialog customCommentActionDialog;

    private Typeface typeface_song_depict;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.layout_activity_user_dynamic);
        } catch (Resources.NotFoundException exception) {
            collectException(exception);
            exception.printStackTrace();
        }

        ButterKnife.bind(this);

        typeface_song_depict = TypefaceUtil.loadTypeface(this, TypefaceUtil.TYPEFACE_SONG_DEPICT);

        userDynamicPresenter = new UserDynamicPresenter(this);
        userDynamicPresenter.initParameter(page);
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
    public void setPresenter(UserDynamicInterface.Presenter dynamicPresenter) {
        this.userDynamicPresenter = dynamicPresenter;
    }

    @Override
    public void initView() {
        if (common_head_title != null) {
            common_head_title.setText("我的动态");
            common_head_title.setTypeface(typeface_song_depict);
        }

        if (user_dynamic_empty_view != null) {
            user_dynamic_empty_view.setVisibility(View.GONE);
        }

        if (user_dynamic_empty_prompt != null) {
            user_dynamic_empty_prompt.setText("快~速来分享你的故事");
        }


        linearLayoutManager = new LinearLayoutManager(UserDynamicActivity.this, LinearLayoutManager.VERTICAL, false);
        userDynamicAdapter = new UserDynamicAdapter(this, dynamics);
        userDynamicAdapter.setDynamicClickListener(this);

        if (user_dynamic_content != null) {
            user_dynamic_content.setLayoutManager(linearLayoutManager);
            user_dynamic_content.setAdapter(userDynamicAdapter);

            user_dynamic_content.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

                    if (!user_dynamic_refresh.isRefreshing() && userDynamicPresenter.isLoadingMoreState() && (visibleItemCount + firstVisiblePosition) == totalItemCount - 5) {
                        setRefreshViewState(true);
                        page += 1;
                        userDynamicPresenter.loadUserDynamicMore(page);
                    }
                }
            });
        }

        if (user_dynamic_refresh != null) {
            user_dynamic_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    setRefreshViewState(false);
                    page = 1;
                    userDynamicPresenter.loadUserDynamic(page);
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
            loadingPage = new LoadingPage(this, user_dynamic_loading);
        }
        userDynamicPresenter.setLoadingPage(loadingPage);
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
    public void setDynamics(ArrayList<Review> dynamicList) {
        if (dynamicList == null || dynamicList.size() == 0) {
            showEmptyView();
        } else {
            showContentView();

            if (dynamics == null) {
                dynamics = new ArrayList<>();
            } else {
                dynamics.clear();
            }

            for (Review dynamic : dynamicList) {
                dynamics.add(dynamic);
            }

            if (userDynamicAdapter == null) {
                userDynamicAdapter = new UserDynamicAdapter(this, dynamics);
                userDynamicAdapter.setDynamicClickListener(this);
                if (user_dynamic_content != null) {
                    user_dynamic_content.setAdapter(userDynamicAdapter);
                }
            } else {
                userDynamicAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void setDynamicsMore(ArrayList<Review> dynamicList) {
        if (dynamicList != null && dynamicList.size() > 0) {

            for (Review dynamic : dynamicList) {
                dynamics.add(dynamic);
            }

            if (userDynamicAdapter == null) {
                userDynamicAdapter = new UserDynamicAdapter(this, dynamics);
                userDynamicAdapter.setDynamicClickListener(this);
                if (user_dynamic_content != null) {
                    user_dynamic_content.setAdapter(userDynamicAdapter);
                }
            } else {
                userDynamicAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void setListEnd() {
        Review review = new Review();
        review.type = UserDynamicAdapter.TYPE_LIST_END;
        dynamics.add(review);
        userDynamicAdapter.notifyDataSetChanged();
    }

    @Override
    public void setRefreshViewState(boolean state) {
        if (user_dynamic_refresh == null) {
            return;
        }
        if (state) {
            user_dynamic_refresh.setRefreshing(true);
        } else {
            if (user_dynamic_refresh.isRefreshing()) {
                user_dynamic_refresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        user_dynamic_refresh.setRefreshing(false);
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
            intent.setClass(UserDynamicActivity.this, CoverActivity.class);
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
            intent.setClass(UserDynamicActivity.this, CommentDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("id_book", id_book);
            bundle.putString("id_comment", id_comment);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void onClickedDynamic(Review review) {
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
        if (user_dynamic_content != null) {
            user_dynamic_content.setVisibility(View.GONE);
        }

        if (user_dynamic_empty_view != null) {
            user_dynamic_empty_view.setVisibility(View.VISIBLE);
        }

        if (user_dynamic_refresh != null) {
            if (user_dynamic_refresh.isRefreshing()) {
                setRefreshViewState(false);
            }
            user_dynamic_refresh.setEnabled(false);
        }
    }

    private void showContentView() {
        if (user_dynamic_empty_view != null) {
            user_dynamic_empty_view.setVisibility(View.GONE);
        }

        if (user_dynamic_content != null) {
            user_dynamic_content.setVisibility(View.VISIBLE);
        }

        if (user_dynamic_refresh != null) {
            user_dynamic_refresh.setEnabled(true);
        }
    }

    @Override
    public void onCommentActionReplySentClicked(Review review, String content) {
        if (review != null) {
            if (TextUtils.isEmpty(content)) {
                showToast("评论内容为空，请填写内容后重试！");
            } else {
                userDynamicPresenter.publishCommentReply(review, content);
            }
        } else {
            showToast("评论数据错误，请重试！");
        }
    }
}
