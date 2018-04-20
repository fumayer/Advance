package com.newsdemo.ui.vtex.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.newsdemo.R;
import com.newsdemo.app.Constants;
import com.newsdemo.base.RootActivity;
import com.newsdemo.base.contract.vtex.RepliesContract;
import com.newsdemo.model.bean.NodeListBean;
import com.newsdemo.model.bean.RealmLikeBean;
import com.newsdemo.model.bean.RepliesListBean;
import com.newsdemo.model.http.api.VtexApis;
import com.newsdemo.presenter.vtex.RepliesPresenter;
import com.newsdemo.ui.vtex.adapter.RepliesAdapter;
import com.newsdemo.util.ShareUtil;
import com.newsdemo.util.SystemUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.R.attr.id;
import static android.R.attr.top;

/**
 * Created by jianqiang.hu on 2017/5/31.
 */

public class RepliesActivity extends RootActivity<RepliesPresenter> implements RepliesContract.View{

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.view_main)
    RecyclerView rvContent;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    private boolean isLiked;
    private String topicId;
    private NodeListBean mTopBean;
    private MenuItem menuItem;
    private RepliesAdapter mAdapter;


    @Override
    protected int getLayout() {
        return R.layout.activity_replies;
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        setToolBar(toolBar,"帖子详情");
        topicId=getIntent().getExtras().getString(Constants.IT_VTEX_TOPIC_ID);
        mTopBean=getIntent().getExtras().getParcelable(Constants.IT_VTEX_REPLIES_TOP);
        mAdapter=new RepliesAdapter(mContext,new ArrayList<RepliesListBean>(),mTopBean);
        rvContent.setLayoutManager(new LinearLayoutManager(mContext));
        rvContent.setAdapter(mAdapter);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getContent(topicId);
            }
        });
        stateLoading();
        mPresenter.getContent(topicId);
        if (mTopBean==null){
            mPresenter.getTopInfo(topicId);
        }
    }

    @Override
    public void stateError() {
        super.stateError();
        if (swipeRefresh.isRefreshing()){
            swipeRefresh.setRefreshing(false);
        }
    }




    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
        setToolBar(toolBar, "帖子详情");
        topicId = getIntent().getExtras().getString(Constants.IT_VTEX_TOPIC_ID);
        mTopBean = getIntent().getParcelableExtra(Constants.IT_VTEX_REPLIES_TOP);
    }




    @Override
    public void showContent(List<RepliesListBean> mList) {
        if (swipeRefresh.isRefreshing())
            swipeRefresh.setRefreshing(false);

        stateMain();
        mAdapter.setContentData(mList);

    }

    @Override
    public void showTopInfo(NodeListBean mTopInfo) {
        mTopBean=mTopInfo;
        mAdapter.setTopData(mTopInfo);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_like:
                if (isLiked){
                    isLiked=false;
                    item.setIcon(R.mipmap.ic_toolbar_like_n);
                    mPresenter.delete(topicId);
                }else{
                    isLiked=true;
                    item.setIcon(R.mipmap.ic_toolbar_like_p);
                    RealmLikeBean bean = new RealmLikeBean();
                    bean.setId(topicId);
                    bean.setImage(mTopBean.getMember().getavatar_normal());
                    bean.setTitle(mTopBean.getTitle());
                    bean.setType(Constants.TYPE_VTEX);
                    bean.setTime(System.currentTimeMillis());
                    mPresenter.insert(bean);
                }
                break;
            case R.id.action_copy:
                SystemUtil.copyToClipBoard(mContext,VtexApis.REPLIES_URL+id);
                break;
            case R.id.action_share:
                ShareUtil.shareText(mContext, VtexApis.REPLIES_URL+id,"分享一篇文章");
                break;

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tech_meun,menu);
        menuItem=menu.findItem(R.id.action_like);
        setLikeState(mPresenter.query(topicId));
        return true;
    }

    private void setLikeState(boolean state){
        if (state){
            isLiked=true;
            menuItem.setIcon(R.mipmap.ic_toolbar_like_p);
        }else{
            isLiked=false;
            menuItem.setIcon(R.mipmap.ic_toolbar_like_n);
        }


    }
}
