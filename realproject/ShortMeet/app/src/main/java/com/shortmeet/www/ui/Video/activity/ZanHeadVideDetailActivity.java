package com.shortmeet.www.ui.Video.activity;

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
import com.shortmeet.www.bean.video.ShowZanHeadsBean;
import com.shortmeet.www.entity.video.ShowZanHeadsEntity;
import com.shortmeet.www.mvp.IMVPView;
import com.shortmeet.www.mvp.IMVPrecenter;
import com.shortmeet.www.ui.Video.adapter.VedoDetaiRecyAllHeadAdapter;
import com.shortmeet.www.utilsUsed.DialogUtils;
import com.shortmeet.www.utilsUsed.LogUtils;
import com.shortmeet.www.utilsUsed.UserUtils;
import com.shortmeet.www.views.refreshPart.AutoSwipeRefresh;
/*
 *  Fly 注：赞过的用户
 */
public class ZanHeadVideDetailActivity extends BaseActivity implements IMVPView,View.OnClickListener {
    //跟布局
    private FrameLayout activityZanHeadVideDetail;
    //刷新
    private AutoSwipeRefresh autorefreshActivityZanhead;
    //recycler
    private RecyclerView recycleZan;
    private VedoDetaiRecyAllHeadAdapter mVedoDetaiRecyAllHeadAdapter;
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
    /*
     *  Fly 注：传过来的数据
     */
    private String vodId;
    @Override
    public int setRootView() {
        return R.layout.activity_zan_head_vide_detail;
    }
    @Override
    public boolean setUseTitleBar() {
        return true;
    }

    @Override
    public void initView() {
        tvCenterTitleBar.setText("赞过的用户");
        imgvLeftbackTitlebar.setVisibility(View.VISIBLE);
        vodId=this.getIntent().getStringExtra("vodid");
        autorefreshActivityZanhead = (AutoSwipeRefresh) findViewById(R.id.autorefresh_activity_zanhead);
        activityZanHeadVideDetail = (FrameLayout) findViewById(R.id.activity_zan_head_vide_detail);
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
        recycleZan = (RecyclerView) this.findViewById(R.id.swipe_target);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycleZan.setLayoutManager(linearLayoutManager);
        mVedoDetaiRecyAllHeadAdapter = new VedoDetaiRecyAllHeadAdapter(R.layout.item_recycler_zanallheads,null);
        recycleZan.setAdapter(mVedoDetaiRecyAllHeadAdapter);
    }

    @Override
    public void initListener() {
        titleBarLeftframelayout.setOnClickListener(this);
        autorefreshActivityZanhead.setOnRefreshListener(new  MyOnRefreshListener());
        mVedoDetaiRecyAllHeadAdapter.setOnLoadMoreListener(new MyRequestLoadMoreListener(), recycleZan);
        mVedoDetaiRecyAllHeadAdapter.disableLoadMoreIfNotFullPage();
    }

    @Override
    public void initData() { autorefreshActivityZanhead.autoRefresh();}
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

   //  Fly 注：联网---------------------------------------------
    public void prepareListData(){
        ShowZanHeadsBean bean=new ShowZanHeadsBean();
        if (isRefresh) {
            bean.setPage(0);
        } else if(isLoading){
            bean.setPage(nextPage);
        }
        bean.setSessionid(UserUtils.getSessionId(this));
        bean.setVod_id(vodId);
        mPrecenter.doShowAllZanHeads(bean);
    }

    //联网数据-----------------------------------------------------
    @Override
    public void setData(Object o, int id) {
        switch (id) {
            case 2:
            ShowZanHeadsEntity entity= (ShowZanHeadsEntity) o;
            if (entity.getCode() == 0) {
                if (entity.getData() == null) {
                    LogUtils.e("ZanHeadVideDetailActivity-ShowZanHeadsEntity", "doShowAllZanHeads.getData()==null");
                    hideLoading();
                    return;
                }
                if (entity.getData().getList() == null) {
                    LogUtils.e("ZanHeadVideDetailActivity-ShowZanHeadsEntity", "doShowAllZanHeads.getData().getList()==null");
                    hideLoading();
                    return;
                }
                nextPage = entity.getData().getNext_page();
                //呈现
                if (isRefresh) {
                    isRefresh = false;
                    if (entity.getData().getList().size() == 0) {
                        showMessage("暂无数据~");
                        autorefreshActivityZanhead.refreshComplete();
                        return;
                    }
                    if (entity.getData().getList().size() != 0) {
                        mVedoDetaiRecyAllHeadAdapter.setNewData(entity.getData().getList());
                        autorefreshActivityZanhead.refreshComplete();
                    }
                    LogUtils.e("刷新---当前数据数目-------》",""+entity.getData().getList().size());
                } else if (isLoading) {
                    isLoading = false;
                    if (entity.getData().getList().size() == 0) {
                        showMessage("没有数据可加载了~");
                        mVedoDetaiRecyAllHeadAdapter.loadMoreEnd();
                    }
                    LogUtils.e("加载---当前数据数目-------》",""+entity.getData().getList().size());
                    if (entity.getData().getList().size() != 0) {
                        mVedoDetaiRecyAllHeadAdapter.addData(entity.getData().getList());
                        mVedoDetaiRecyAllHeadAdapter.loadMoreComplete();
                    }
                }
            } else if (entity.getCode() ==30702) {
                LogUtils.e("", "无数据le");
                autorefreshActivityZanhead.refreshComplete();
                mVedoDetaiRecyAllHeadAdapter.loadMoreEnd();
                System.out.println("执行了。。。。");
                return;
            } else if (entity.getCode() == 104) {
                DialogUtils.reLoginAct(this, this, "您的账号已在另一台设备上登陆，若非本人操作则账号已泄露，请立即找回密码!", false);
                hideLoading();
            } else {
                this.showMessage("请求失败");
                hideLoading();
                LogUtils.e("", "请求失败" + entity.getCode());
            }
            break;
        }

    }
    @Override
    public void hideLoading() {
        super.hideLoading();
        if (isRefresh) {
            isRefresh = false;
            autorefreshActivityZanhead.setRefreshing(false);
            showNetErrorPicture();
        } else if (isLoading) {
            isLoading = false;
            mVedoDetaiRecyAllHeadAdapter.loadMoreFail();
        } else {
            showNetErrorPicture();
        }
    }
    public void hideNetErrorPictrue() {  //  错误图片隐藏的方法
        relativeLayoutNoDatas.setVisibility(View.GONE);
        recycleZan.setVisibility(View.VISIBLE);
    }

    public void showNetErrorPicture() {  //错误图片显示
        relativeLayoutNoDatas.setVisibility(View.VISIBLE);
        recycleZan.setVisibility(View.GONE);
    }
}
