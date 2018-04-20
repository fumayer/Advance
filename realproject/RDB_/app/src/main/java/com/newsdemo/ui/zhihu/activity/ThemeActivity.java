package com.newsdemo.ui.zhihu.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.newsdemo.R;
import com.newsdemo.app.Constants;
import com.newsdemo.base.RootActivity;
import com.newsdemo.base.contract.zhihu.ThemeChildContract;
import com.newsdemo.component.GlidUtils;
import com.newsdemo.model.bean.ThemeChildListBean;
import com.newsdemo.presenter.zhihu.ThemeChildPresent;
import com.newsdemo.ui.zhihu.adapter.ThemeChildAdapter;
import com.newsdemo.util.SystemUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by jianqiang.hu on 2017/5/23.
 */



public class ThemeActivity extends RootActivity<ThemeChildPresent> implements ThemeChildContract.View{
    @BindView(R.id.tool_bar)
    Toolbar tool_bar;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.tv_theme_child_blur)
    ImageView tv_theme_child_blur;

    @BindView(R.id.iv_theme_child_origin)
    ImageView iv_theme_child_origin;

    @BindView(R.id.tv_theme_child_des)
    TextView tv_theme_child_des;

    @BindView(R.id.view_main)
    RecyclerView rv_theme;

    @BindView(R.id.theme_child_appbar)
    AppBarLayout appbar;


    ThemeChildAdapter mAdapter;
    List<ThemeChildListBean.StoriesBean> mList;

    @Override
    protected int getLayout() {
        return R.layout.activity_theme;
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        Intent intent =getIntent();
        final int id = intent.getExtras().getInt(Constants.IT_ZHIHU_THEME_ID);
        mList=new ArrayList<>();
        mAdapter=new ThemeChildAdapter(mContext,mList);
        rv_theme.setLayoutManager(new LinearLayoutManager(mContext));
        rv_theme.setAdapter(mAdapter);
        stateLoading();
        mPresenter.getThemeChildData(id);
        mAdapter.setOnItemClickListener(new ThemeChildAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int poition, View shareView) {
                mPresenter.insertReadToDB(mList.get(poition).getId());
                mAdapter.setReadState(poition,true);
                mAdapter.notifyItemChanged(poition);
                Intent intent =new Intent();
                intent.setClass(mContext,ZhihuDetailActivity.class);
                intent.putExtra(Constants.IT_ZHIHU_DETAIL_ID,mList.get(poition).getId());

                if (shareView!=null){
                    mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(mContext, shareView, "shareView").toBundle());
                }else{
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(mContext).toBundle());
                }
            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getThemeChildData(id);
            }
        });

        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset>=0){
                    swipeRefresh.setEnabled(true);
                }else{
                    swipeRefresh.setEnabled(false);
                    float rate = (float)(SystemUtil.dp2px(mContext,256)+verticalOffset*2)/SystemUtil.dp2px(mContext,256);
                    if (rate >=0){
                        iv_theme_child_origin.setAlpha(rate);
                    }
                }
            }
        });

    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void stateError() {
        super.stateError();
        if (swipeRefresh.isRefreshing()){
            swipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void showContent(ThemeChildListBean themeChildListBean) {
            if (swipeRefresh.isRefreshing()){
                swipeRefresh.setRefreshing(false);
            }

        stateMain();
        setToolBar(tool_bar,themeChildListBean.getName());
        mList.clear();
        mList.addAll(themeChildListBean.getStories());
        mAdapter.notifyDataSetChanged();
        GlidUtils.load(mContext,themeChildListBean.getBackground(),iv_theme_child_origin);
        GlidUtils.loadBlur(mContext,themeChildListBean.getBackground(),tv_theme_child_blur,25);
        tv_theme_child_des.setText(themeChildListBean.getDescription());

    }
}
