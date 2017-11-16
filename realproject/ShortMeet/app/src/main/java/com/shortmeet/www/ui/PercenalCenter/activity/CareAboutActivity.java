package com.shortmeet.www.ui.PercenalCenter.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shortmeet.www.Base.BaseActivity;
import com.shortmeet.www.R;
import com.shortmeet.www.mvp.IMVPView;
import com.shortmeet.www.mvp.IMVPrecenter;
import com.shortmeet.www.ui.PercenalCenter.adapter.CareAboutAdapter;
import com.shortmeet.www.views.refreshPart.AutoSwipeRefresh;

import static android.R.attr.value;

/*
 *  Fly 注：关注
 */
public class CareAboutActivity extends BaseActivity implements IMVPView, View.OnClickListener {
    //跟布局
    private FrameLayout frameActivityCareAbout;
    //刷新
    private AutoSwipeRefresh autorefreshActivityCareabout;
    //recycler
    private RecyclerView recycleCareAbout;
    private CareAboutAdapter mCareAboutAdapter;

    // Fly 注：precenter
    private IMVPrecenter mPrecenter;

    /*
    *  Fly 注：是否 是刷新   是否是加载更多
    */
    private boolean isRefresh = true;
    private boolean isLoading;
    private int nextPage;

    /*
     *  Fly 注：空数据
     */
    // 没有数据  布局
    private RelativeLayout relativeLayoutNoDatas;
    //没有数据 图标
    private ImageView imgvNodata;
    //空数据tv
    private TextView tvNodatasContent;


    @Override
    public int setRootView() {
        return R.layout.activity_care_about;
    }

    @Override
    public boolean setUseTitleBar() {
        return true;
    }


    @Override
    public void initView() {
        tvCenterTitleBar.setText("关注");
        imgvLeftbackTitlebar.setVisibility(View.VISIBLE);
        autorefreshActivityCareabout = (AutoSwipeRefresh) findViewById(R.id.autorefresh_activity_careabout);
        frameActivityCareAbout = (FrameLayout) findViewById(R.id.frame_activity_care_about);
        relativeLayoutNoDatas = (RelativeLayout) findViewById(R.id.relative_layout_no_datas);
        tvNodatasContent = (TextView) findViewById(R.id.tv_nodatas_content);
        imgvNodata= (ImageView) findViewById(R.id.imgv_nodata);
        relativeLayoutNoDatas.setVisibility(View.GONE);
        tvNodatasContent.setText("还没有数据哦~");
       //imgvNoData.setImageResource();
        mPrecenter = new IMVPrecenter(this);
        initRecycleView();
    }
    public void initRecycleView(){
        recycleCareAbout = (RecyclerView) this.findViewById(R.id.swipe_target);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycleCareAbout.setLayoutManager(linearLayoutManager);
        mCareAboutAdapter = new CareAboutAdapter(R.layout.item_recycler_careabout,null);
        recycleCareAbout.setAdapter(mCareAboutAdapter);
    }
    @Override
    public void initListener() {
      titleBarLeftframelayout.setOnClickListener(this);
      autorefreshActivityCareabout.setOnRefreshListener(new MyOnRefreshListener());
      mCareAboutAdapter.setOnLoadMoreListener(new MyRequestLoadMoreListener(), recycleCareAbout);
      mCareAboutAdapter.disableLoadMoreIfNotFullPage();
    }

    @Override
    public void initData() {
     autorefreshActivityCareabout.autoRefresh();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_leftframelayout:
                this.finish();
                break;
            default:
                break;
        }
    }


    /*
    *  Fly 注：刷新监听
    */
    private class MyOnRefreshListener implements OnRefreshListener {
        @Override
        public void onRefresh() {
            isRefresh = true;
            prepareListData();
        }
    }
    /*
     *  Fly 注：加载更多监听
     */
    private class MyRequestLoadMoreListener implements BaseQuickAdapter.RequestLoadMoreListener {
        @Override
        public void onLoadMoreRequested() {
            isLoading = true;
            prepareListData();
        }
    }


    /*
     *  Fly 注：联网动作
     */
    public void prepareListData(){
     //   ShowZanHeadsBean bean=new ShowZanHeadsBean();

//        if (isRefresh) {
//            bean.setPage(0);
//        } else if(isLoading){
//            bean.setPage(nextPage);
//        }
//        bean.setSessionid(UserUtils.getSessionId(this));
//        bean.setVod_id(vodId);
//        mPrecenter.doShowAllZanHeads(bean);
    }


  /*
   *  Fly 注：联网数据----------------------------------------
   */
    @Override
    public void setData(Object o, int id) {
        switch (id) {
            case 0:
                break;
            case value:

                break;
            default:
                break;
        }

    }










    @Override
    public void hideLoading() {
        super.hideLoading();
        if (isRefresh) {
            isRefresh = false;
            autorefreshActivityCareabout.setRefreshing(false);
            showNetErrorPicture();
        } else if (isLoading) {
            isLoading = false;
            mCareAboutAdapter.loadMoreFail();
        } else {
            showNetErrorPicture();
        }
    }

    public void hideNetErrorPictrue() {  //  错误图片隐藏的方法
        relativeLayoutNoDatas.setVisibility(View.GONE);
        recycleCareAbout.setVisibility(View.VISIBLE);
    }

    public void showNetErrorPicture() {  //错误图片显示
        relativeLayoutNoDatas.setVisibility(View.VISIBLE);
        recycleCareAbout.setVisibility(View.GONE);
    }
}
