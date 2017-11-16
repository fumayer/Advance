package com.shortmeet.www.ui.PercenalCenter.activity;

import android.content.Intent;
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
import com.shortmeet.www.ui.PercenalCenter.adapter.CommenAdapter;
import com.shortmeet.www.views.refreshPart.AutoSwipeRefresh;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.value;

public class CommenActivity extends BaseActivity implements IMVPView ,View.OnClickListener, BaseQuickAdapter.OnItemClickListener {
    //跟布局
    private FrameLayout activityCommen;
    //刷新
    private AutoSwipeRefresh autorefreshActivitycommen;




    //recycler
    private RecyclerView recycleCommen;
    private CommenAdapter mCommenAdapter;
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
        return R.layout.activity_commen;
    }

    @Override
    public void initView() {
        tvCenterTitleBar.setText("评论");
        imgvLeftbackTitlebar.setVisibility(View.VISIBLE);
        autorefreshActivitycommen = (AutoSwipeRefresh) findViewById(R.id.autorefresh_activitycommen);
        activityCommen = (FrameLayout) findViewById(R.id.activity_commen);
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
        recycleCommen = (RecyclerView) this.findViewById(R.id.swipe_target);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycleCommen.setLayoutManager(linearLayoutManager);
        mCommenAdapter = new  CommenAdapter(R.layout.item_recycler_commen,null);
        recycleCommen.setAdapter(mCommenAdapter);
    }
    @Override
    public void initListener() {
        titleBarLeftframelayout.setOnClickListener(this);
        autorefreshActivitycommen.setOnRefreshListener(new CommenActivity.MyOnRefreshListener());
        mCommenAdapter.setOnLoadMoreListener(new MyRequestLoadMoreListener(), recycleCommen);
        mCommenAdapter.disableLoadMoreIfNotFullPage();
        mCommenAdapter.setOnItemClickListener(this);
    }
    @Override
    public void initData() { autorefreshActivitycommen.autoRefresh();}

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
           // prepareListData();
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
            for(int i=0;i<3;i++){
                sts.add("");
            }
            mCommenAdapter.setNewData(sts);
            autorefreshActivitycommen.refreshComplete();
        } else if(isLoading){
//            for(int i=0;i<8;i++){
//                sts.add("");
//            }
//            mCommenAdapter.addData(sts);
//            mCommenAdapter.loadMoreEnd();
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
            autorefreshActivitycommen.setRefreshing(false);
            showNetErrorPicture();
        } else if (isLoading) {
            isLoading = false;
            mCommenAdapter.loadMoreFail();
        } else {
            showNetErrorPicture();
        }
    }

    public void hideNetErrorPictrue() {  //  错误图片隐藏的方法
        relativeLayoutNoDatas.setVisibility(View.GONE);
        recycleCommen.setVisibility(View.VISIBLE);
    }

    public void showNetErrorPicture() {  //错误图片显示
        relativeLayoutNoDatas.setVisibility(View.VISIBLE);
        recycleCommen.setVisibility(View.GONE);
    }

    //非条目内容 普通点击监听
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
    //条目点击监听
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        showMessage("去回复界面");
      startActivity(new Intent(this,ResponseActivity.class));
    }
}
