package com.aiwue.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwue.R;
import com.aiwue.base.BaseMvpActivity;
import com.aiwue.iview.IBaseDetailView;
import com.aiwue.model.Comment;
import com.aiwue.presenter.BaseDetailPresenter;
import com.aiwue.ui.adapter.CommentAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aiwue.R.id.action_comment_count;

/**
 * Created by Administrator on 2016/11/24 0024.
 */

public abstract class BaseContentActivity<P extends BaseDetailPresenter> extends BaseMvpActivity<P> implements IBaseDetailView {
    //顶部导航条
    @BindView(R.id.back_btn)
    ImageView backBtn;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.share_btn)
    ImageView shareBtn;

    //中间评论列表
    @BindView(R.id.recyclerView)
    protected RecyclerView recyclerView;

    //底部工具条
    @BindView(R.id.write_comment_layout)
    TextView writeCommentLayout;
    @BindView(R.id.action_view_up)
    ImageView actionViewUp;
    @BindView(R.id.action_view_comment)
    ImageView actionViewComment;
    @BindView(action_comment_count)
    TextView actionCommentCount;
    @BindView(R.id.action_commont_layout)
    FrameLayout actionCommontLayout;
    @BindView(R.id.view_comment_layout)
    FrameLayout viewCommentLayout;
    @BindView(R.id.action_favor)
    ImageView actionFavor;
    @BindView(R.id.action_repost)
    ImageView actionRepost;

    private CommentAdapter mAdapter;
    public View headerView = null;
    protected List<Comment> mDatas = new ArrayList<>();
    protected int mPage = 1;
    protected int mSize = 10;

    //以下变量用于分享
    protected Integer contentId;
    protected Integer contentType;
    protected String pId;
    protected String shareImgName;
    protected String shareTitle;
    protected String shareContent;

    public void setTitle(String title) {
        this.title.setText(title);
    }
    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_content_detail);
        ButterKnife.bind(this);
    }
    @Override
    protected void bindViews() {
    }
    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initCommonRecyclerView(mAdapter = new CommentAdapter(mDatas), null);
        mAdapter.addHeaderView(headerView);

        View emptyView = View.inflate(this, R.layout.subscribe_list_empty_view, null);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mAdapter.setEmptyView(emptyView);
    }
    @Override
    protected void setListener() {
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (contentId != null && contentType != null)
                    mvpPresenter.getCommentList(contentType, contentId, ++mPage, mSize);
            }
        },recyclerView);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter var1, View var2, int i){
//                Comment comment = mAdapter.getData().get(i);
//                Intent intent = new Intent(mContext, VideoDetailActivity.class);
//                intent.putExtra("commentId",comment.getId());
//                startActivity(intent);
            }
        });
    }
    public void refreshCommentList() {
        if (contentId != null && contentType != null) {
            mPage = 1;
            mvpPresenter.getCommentList(contentType, contentId, mPage, mSize);
        }
    }

    @OnClick({R.id.back_btn, R.id.action_commont_layout, R.id.action_favor, R.id.action_repost})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.action_commont_layout:
//                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                linearLayoutManager.scrollToPositionWithOffset(1, 0);
                recyclerView.smoothScrollToPosition(1);
                break;
            case R.id.action_favor:
                break;
            case R.id.action_repost:
                break;
        }
    }
    @Override
    public void onGetCommentListSuccess(Boolean success, String err, List<Comment> response) {
        mAdapter.loadMoreComplete();
        if (success) {
            if (response == null) { //如果返回null，表明到末尾了
                if (mPage > 1)
                    mAdapter.loadMoreEnd();
                return;
            }
            if (response.size() < mSize) { //如果返回的记录数比期望的小，则表明到末尾了
                mAdapter.loadMoreEnd();
            }
            if (mPage == 1) {
                mAdapter.getData().clear();
            }
            mAdapter.addData(response);
            mAdapter.notifyDataSetChanged();

        }else {
            mAdapter.loadMoreFail();
        }
    }
}
