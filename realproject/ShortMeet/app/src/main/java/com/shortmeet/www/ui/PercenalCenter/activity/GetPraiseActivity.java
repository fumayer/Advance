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
import com.shortmeet.www.ui.PercenalCenter.adapter.GetPraiseAdapter;
import com.shortmeet.www.views.refreshPart.AutoSwipeRefresh;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.value;

public class GetPraiseActivity extends BaseActivity implements IMVPView ,View.OnClickListener{
    //跟布局
    private FrameLayout activityGetPraise;
    //刷新
    private AutoSwipeRefresh autorefreshActivityGetpraise;
    //recycler
    private RecyclerView recycleGetPraise;
    private GetPraiseAdapter mGetPraiseAdapter;
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
    //mpresceter
    private IMVPrecenter mPrecenter;

    @Override
    public boolean setUseTitleBar() {
        return true;
    }

    @Override
    public int setRootView() {
        return R.layout.activity_get_praise;
    }

    @Override
    public void initView() {
        tvCenterTitleBar.setText("赞");
        imgvLeftbackTitlebar.setVisibility(View.VISIBLE);
        autorefreshActivityGetpraise = (AutoSwipeRefresh) findViewById(R.id.autorefresh_activity_getpraise);
        activityGetPraise = (FrameLayout) findViewById(R.id.activity_get_praise);
        relativeLayoutNoDatas = (RelativeLayout) findViewById(R.id.relative_layout_no_datas);
        imgvNodata = (ImageView) findViewById(R.id.imgv_nodata);
        tvNodatasContent = (TextView) findViewById(R.id.tv_nodatas_content);
        relativeLayoutNoDatas.setVisibility(View.GONE);
        tvNodatasContent.setText("还没有数据哦~");
        //imgvNoData.setImageResource();
        mPrecenter=new IMVPrecenter(this);
        initRecycleView();
    }
     public void initRecycleView(){
     recycleGetPraise = (RecyclerView) this.findViewById(R.id.swipe_target);
     LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
     recycleGetPraise.setLayoutManager(linearLayoutManager);
     mGetPraiseAdapter = new  GetPraiseAdapter(R.layout.item_recycler_getpraise,null);
     recycleGetPraise.setAdapter(mGetPraiseAdapter);
     }
    @Override
    public void initListener() {
    titleBarLeftframelayout.setOnClickListener(this);
    autorefreshActivityGetpraise.setOnRefreshListener(new  MyOnRefreshListener());
    mGetPraiseAdapter.setOnLoadMoreListener(new  MyRequestLoadMoreListener(), recycleGetPraise);
    mGetPraiseAdapter.disableLoadMoreIfNotFullPage();
    }
    @Override
    public void initData() { autorefreshActivityGetpraise.autoRefresh();}

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
    *  Fly 注：联网
    */
    List<String> sts=new ArrayList<>();
    public void prepareListData(){
        if (isRefresh) {
            if(sts.size()!=0){
                sts.clear();
            }
            for(int i=0;i<5;i++){
                sts.add("");
            }
            mGetPraiseAdapter.setNewData(sts);
            autorefreshActivityGetpraise.refreshComplete();
        } else if(isLoading){
            for(int i=0;i<8;i++){
                sts.add("");
            }
            mGetPraiseAdapter.addData(sts);
            mGetPraiseAdapter.loadMoreEnd();
        }
    }



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
            autorefreshActivityGetpraise.setRefreshing(false);
            showNetErrorPicture();
        } else if (isLoading) {
            isLoading = false;
           mGetPraiseAdapter.loadMoreFail();
        } else {
            showNetErrorPicture();
        }
    }

    public void hideNetErrorPictrue() {  //  错误图片隐藏的方法
        relativeLayoutNoDatas.setVisibility(View.GONE);
        recycleGetPraise.setVisibility(View.VISIBLE);
    }

    public void showNetErrorPicture() {  //错误图片显示
        relativeLayoutNoDatas.setVisibility(View.VISIBLE);
        recycleGetPraise.setVisibility(View.GONE);
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
}
