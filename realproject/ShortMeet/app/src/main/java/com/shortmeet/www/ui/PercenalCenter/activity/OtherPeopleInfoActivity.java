package com.shortmeet.www.ui.PercenalCenter.activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shortmeet.www.Base.BaseActivity;
import com.shortmeet.www.R;
import com.shortmeet.www.bean.personalCenter.MyWorkBean;
import com.shortmeet.www.entity.percenalCenter.DataEntity;
import com.shortmeet.www.entity.percenalCenter.MyWorkEntity;
import com.shortmeet.www.mvp.IMVPView;
import com.shortmeet.www.mvp.IMVPrecenter;
import com.shortmeet.www.ui.PercenalCenter.adapter.OtherPeopleWorkfragRecyAdapter;
import com.shortmeet.www.utilsUsed.DialogUtils;
import com.shortmeet.www.utilsUsed.GlideLoader;
import com.shortmeet.www.utilsUsed.LogUtils;
import com.shortmeet.www.utilsUsed.StatusBarUtil;
import com.shortmeet.www.utilsUsed.UiUtils;
import com.shortmeet.www.utilsUsed.UserUtils;
import com.shortmeet.www.views.refreshPart.AutoRefreshLayout;
import com.shortmeet.www.views.widgetPart.RatioImageView;

import java.util.ArrayList;
import java.util.List;

public class OtherPeopleInfoActivity extends BaseActivity implements IMVPView,View.OnClickListener {
    //标识
    private static final String TAG = "OtherPeopleInfoActivity";
    //顶部相关
    private View topStusbarOtherpeopleinfo;  //充当状态栏View
    private LinearLayout linearTitlebarOtherpeopleinfo;//toobar
    private View viewDividerOtherpeopleinfog;//toobar分割线
    private ImageView imgvMoreOtherpeopleinfo;//更多
    private ImageView imgvMessageOtherpeopleinfo;//私信
    private FrameLayout framelayArrowbackOtherpeopleinfo;//返回箭头

    /*
     *  Fly 注：HeadView 相关
     */
    private View headView;
    //头像
    private RatioImageView imgvHeadOtherpeopleinfo;
    //关注
    private TextView tvCareOtherpeopleinfo;
    //关注数
    private TextView tvCarecountOtherpeopleinfo;
    //粉丝
    private TextView tvFansOtherpeopleinfo;
    //粉丝数
    private TextView tvFanscountOtherpeopleinfo;
    //签名
    private TextView tvSignatureOtherpeopleinfo;
    //昵称
    private TextView tvNicknameOtherpeopleinfo;
    //用户id
    private TextView tvUseridOtherpeopleinfo;
    //加关注
    private TextView tvAddcareOtherpeopleinfo;
    // 作品布局
    private LinearLayout liearWorkOtherpeopleinfo;
    // 作品
    private TextView tvWorkOtherpeopleinfo;
    //作品数
    private TextView tvWorkcountOtherpeopleinfo;
    //作品下划线
    private View viewWorklineOtherpeopleinfo;
    //赞作品布局
    private LinearLayout liearZanworkOtherpeopleinfo;
    //赞作品
    private TextView tvZanworkOtherpeopleinfo;
    //赞作品数
    private TextView tvZanworkcountOtherpeopleinfo;
    //赞作品下划线
    private View viewZanlineOtherpeopleinfo;


    /*
     *  Fly 注：recycle相关
     */
    private AutoRefreshLayout autorefreshOtherpeopleinfo;
    private RecyclerView recyOtherpeopleinfo;
    //作品  adapter
    private OtherPeopleWorkfragRecyAdapter mOtherPeopleWorkfragRecyAdapter;


    /*
     *  Fly 注：状态 ：是否 是刷新   是否是加载更多
     */
    private boolean isRefresh = true;
    private boolean isLoading;
    private int nextPage;
    //presenter
    IMVPrecenter mPrecenter;

    //Fly 注： kind   1 作品   2点赞作品
    private int kind=0;


    @Override
    public int setRootView() {
        return R.layout.activity_other_people_info;
    }

    @Override
    public void initView() {
        mPrecenter=new IMVPrecenter(this);
        StatusBarUtil.translucentStatusBar(this, false);//设置状态栏透明 内容延伸到状态栏
        topStusbarOtherpeopleinfo = (View) findViewById(R.id.top_stusbar_otherpeopleinfo);
        linearTitlebarOtherpeopleinfo = (LinearLayout) findViewById(R.id.linear_titlebar_otherpeopleinfo);
        viewDividerOtherpeopleinfog = (View) findViewById(R.id.view_divider_otherpeopleinfog);
        imgvMoreOtherpeopleinfo = (ImageView) findViewById(R.id.imgv_more_otherpeopleinfo);
        imgvMessageOtherpeopleinfo = (ImageView) findViewById(R.id.imgv_message_otherpeopleinfo);
        framelayArrowbackOtherpeopleinfo = (FrameLayout) findViewById(R.id.framelay_arrowback_otherpeopleinfo);
        initStusBar();
        initRefresh();
        initHeadView();
        initRecycler();
    }
    public  void  initStusBar(){
        int stusBarHeight= StatusBarUtil.getStatusBarHeight(this);
        LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) topStusbarOtherpeopleinfo.getLayoutParams();
        layoutParams.height=stusBarHeight;
        topStusbarOtherpeopleinfo.setBackgroundColor(UiUtils.getColor(R.color.black));
    }
    public void initRefresh(){
        autorefreshOtherpeopleinfo = (AutoRefreshLayout) findViewById(R.id.autorefresh_otherpeopleinfo);
        autorefreshOtherpeopleinfo.setColorSchemeResources(R.color.black_3);
    }
    public void  initHeadView(){
        headView=mLayoutInflater.inflate(R.layout.item_otherpeopleinfo_headview,null);
        imgvHeadOtherpeopleinfo = (RatioImageView)headView.findViewById(R.id.imgv_head_otherpeopleinfo);
        tvCareOtherpeopleinfo = (TextView)headView.findViewById(R.id.tv_care_otherpeopleinfo);
        tvCarecountOtherpeopleinfo = (TextView) headView.findViewById(R.id.tv_carecount_otherpeopleinfo);
        tvFansOtherpeopleinfo = (TextView)headView.findViewById(R.id.tv_fans_otherpeopleinfo);
        tvFanscountOtherpeopleinfo = (TextView) headView.findViewById(R.id.tv_fanscount_otherpeopleinfo);
        tvSignatureOtherpeopleinfo = (TextView)headView.findViewById(R.id.tv_signature_otherpeopleinfo);
        tvNicknameOtherpeopleinfo = (TextView) headView.findViewById(R.id.tv_nickname_otherpeopleinfo);
        tvUseridOtherpeopleinfo = (TextView)headView.findViewById(R.id.tv_userid_otherpeopleinfo);
        tvAddcareOtherpeopleinfo = (TextView)headView.findViewById(R.id.tv_addcare_otherpeopleinfo);
        liearWorkOtherpeopleinfo = (LinearLayout) headView.findViewById(R.id.liear_work_otherpeopleinfo);
        tvWorkOtherpeopleinfo = (TextView)headView.findViewById(R.id.tv_work_otherpeopleinfo);
        tvWorkcountOtherpeopleinfo = (TextView)headView.findViewById(R.id.tv_workcount_otherpeopleinfo);
        viewWorklineOtherpeopleinfo = (View)headView.findViewById(R.id.view_workline_otherpeopleinfo);
        liearZanworkOtherpeopleinfo = (LinearLayout) headView.findViewById(R.id.liear_zanwork_otherpeopleinfo);
        tvZanworkOtherpeopleinfo = (TextView) headView.findViewById(R.id.tv_zanwork_otherpeopleinfo);
        tvZanworkcountOtherpeopleinfo = (TextView) headView.findViewById(R.id.tv_zanworkcount_otherpeopleinfo);
        viewZanlineOtherpeopleinfo = (View)headView. findViewById(R.id.view_zanline_otherpeopleinfo);

    }
    public void  initRecycler(){
        recyOtherpeopleinfo = (RecyclerView) findViewById(R.id.recy_otherpeopleinfo);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyOtherpeopleinfo.setLayoutManager(linearLayoutManager);
        //作品
        mOtherPeopleWorkfragRecyAdapter=new OtherPeopleWorkfragRecyAdapter();
        mOtherPeopleWorkfragRecyAdapter.addHeaderView(headView,0);
        recyOtherpeopleinfo.setAdapter(mOtherPeopleWorkfragRecyAdapter);
    }

    @Override
    public void initListener() {
        imgvMoreOtherpeopleinfo.setOnClickListener(this);//更多
        imgvMessageOtherpeopleinfo.setOnClickListener(this);//私信
        tvCareOtherpeopleinfo.setOnClickListener(this);//关注
        tvFansOtherpeopleinfo.setOnClickListener(this);//粉丝
        tvAddcareOtherpeopleinfo.setOnClickListener(this);//加关注
        liearWorkOtherpeopleinfo.setOnClickListener(this);//作品
        liearZanworkOtherpeopleinfo.setOnClickListener(this);//赞作品
        framelayArrowbackOtherpeopleinfo.setOnClickListener(this);//返回
        autorefreshOtherpeopleinfo.setOnRefreshListener(mOnRefreshListener);
        //作品
        mOtherPeopleWorkfragRecyAdapter.setOnLoadMoreListener(new OtherPeopleInfoActivity.MyRequestLoadMoreListener(), recyOtherpeopleinfo);//***
        mOtherPeopleWorkfragRecyAdapter.disableLoadMoreIfNotFullPage();//***
        //标题栏渐变   获取标题和封面布局的高度
        linearTitlebarOtherpeopleinfo.getViewTreeObserver().addOnGlobalLayoutListener(new OtherPeopleInfoActivity.MyOnGlobalLayoutListener());
        imgvHeadOtherpeopleinfo.getViewTreeObserver().addOnGlobalLayoutListener(new OtherPeopleInfoActivity.MyOnGlobalLayoutListener());
        //设置recyclerView 滑动的监听
        recyOtherpeopleinfo.addOnScrollListener(new OtherPeopleInfoActivity.MyOnScrollListener());
    }
    @Override
    public void initData() { autorefreshOtherpeopleinfo.autoRefresh(mOnRefreshListener);}

    /*
     *  Fly 注： 回到此界面 刷新界面数据
     */
    @Override
    public void onResume() {
        super.onResume();
        DataEntity user = UserUtils.getUser(this);
        //昵称
        tvNicknameOtherpeopleinfo.setText(user.getNickname());
        //ID
        //  tvUseridMinefrg.setText(user.getUser_id());
        //签名
        tvSignatureOtherpeopleinfo.setText(user.getContent());
        //背景/头像
        GlideLoader.getInstance().loadImg(this.getBaseContext(),user.getImg(),R.drawable.p1,imgvHeadOtherpeopleinfo);
    }

    /*
     *  Fly 注：控件监听
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_more_otherpeopleinfo://更多
                showMessage("更多");
                break;
            case R.id.imgv_message_otherpeopleinfo:  //私信
                showMessage("私信");
                break;
            case R.id.tv_care_otherpeopleinfo:
                showMessage("关注");
                break;
            case R.id.tv_fans_otherpeopleinfo:
                showMessage("粉丝");
                break;
            case R.id.tv_addcare_otherpeopleinfo:
                showMessage("加关注");
                break;
            case R.id.liear_work_otherpeopleinfo://作品
                hideLine();
                viewWorklineOtherpeopleinfo.setVisibility(View.VISIBLE);
                tvWorkOtherpeopleinfo.setTextColor(UiUtils.getColor(R.color.black_3));
                tvWorkcountOtherpeopleinfo.setTextColor(UiUtils.getColor(R.color.black_3));
                kind=0;
                isSwitch=true;
                isRefresh = true;
                isLoading=false;
                prepareData();
                break;
            case R.id.liear_zanwork_otherpeopleinfo://点赞作品
                hideLine();
                viewZanlineOtherpeopleinfo.setVisibility(View.VISIBLE);
                tvZanworkOtherpeopleinfo.setTextColor(UiUtils.getColor(R.color.black_3));
                tvZanworkcountOtherpeopleinfo.setTextColor(UiUtils.getColor(R.color.black_3));
                kind=1;
                isSwitch=true;
                isRefresh = true;
                isLoading=false;
                prepareData();
                break;
            case R.id.framelay_arrowback_otherpeopleinfo:
                this.finish();
                break;
        }
    }
    //还原  作品  点赞     字体设置为默认   下划线  全都设置为隐藏
    public void  hideLine(){
        //线条
        viewWorklineOtherpeopleinfo.setVisibility(View.GONE);
        viewZanlineOtherpeopleinfo.setVisibility(View.GONE);
        //文字
        tvWorkOtherpeopleinfo.setTextColor(UiUtils.getColor(R.color.black_5));
        tvZanworkOtherpeopleinfo.setTextColor(UiUtils.getColor(R.color.black_5));
        //数字
        tvWorkcountOtherpeopleinfo.setTextColor(UiUtils.getColor(R.color.black_5));
        tvZanworkcountOtherpeopleinfo.setTextColor(UiUtils.getColor(R.color.black_5));
    }

    /*
     *  Fly 注： --渐变标题栏相关-----
     */
    //标题的高度
    private int titleHeight;
    //封面的高度
    private int thumbHeight;
    //需要滑动的高度
    private int needScrollHeight;
    //切换  作品  点赞   草稿
    private boolean isSwitch;
    private class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        @Override
        public void onGlobalLayout() {
            linearTitlebarOtherpeopleinfo.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            imgvHeadOtherpeopleinfo.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            titleHeight = linearTitlebarOtherpeopleinfo.getHeight();
            thumbHeight = imgvHeadOtherpeopleinfo.getHeight();
            needScrollHeight = thumbHeight - titleHeight;
            System.out.println("titleHeight"+titleHeight+"   thumbHeight"+thumbHeight+"     needScrollHeight"+needScrollHeight);
        }
    }
    //RecyclerView 竖直方向滑动的距离
    private int scrollDistance;
    //recyclerView滑动的监听
    private class MyOnScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            scrollDistance += dy;
            if(isSwitch){
                scrollDistance=0;
                isSwitch=!isSwitch;
            }
            LogUtils.e("+++recycler滑动的距离》",scrollDistance );
            if (needScrollHeight > 0 && scrollDistance > needScrollHeight) {
                linearTitlebarOtherpeopleinfo.setBackgroundResource(R.color.white);
                viewDividerOtherpeopleinfog.setBackgroundColor(UiUtils.getColor(R.color.black_change4));
            } else {
                if (needScrollHeight <= 0) {
                    linearTitlebarOtherpeopleinfo.setBackgroundResource(R.color.transparent);
                    viewDividerOtherpeopleinfog.setBackgroundResource(R.color.transparent);
                    LogUtils.e("needScrollHeight <= 0","needScrollHeight <= 0");
                    return;
                }
                linearTitlebarOtherpeopleinfo.setBackgroundDrawable(UiUtils.getDrawable(R.color.transparent));
                viewDividerOtherpeopleinfog.setBackgroundDrawable(UiUtils.getDrawable(R.color.transparent));
                float percent = scrollDistance * 1.f / needScrollHeight;
                Log.d("scroll-->", "onScroll: percent: " + percent);
                int evaluateArgb = evaluateArgb(percent, Color.TRANSPARENT, Color.WHITE);
                int evaluateArgb2=evaluateArgb(percent, Color.TRANSPARENT,UiUtils.getColor(R.color.black_change4));
                linearTitlebarOtherpeopleinfo.getBackground().mutate().setColorFilter(evaluateArgb, PorterDuff.Mode.SRC_OVER);
                viewDividerOtherpeopleinfog.getBackground().mutate().setColorFilter(evaluateArgb2, PorterDuff.Mode.SRC_OVER);
            }
        }
    }

    /**
     * 颜色插值器
     *
     * @param fraction
     * @param startValue
     * @param endValue
     * @return
     */
    public int evaluateArgb(float fraction, Object startValue, Object endValue) {
        int startInt = (Integer) startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = (Integer) endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return (int) ((startA + (int) (fraction * (endA - startA))) << 24) |
                (int) ((startR + (int) (fraction * (endR - startR))) << 16) |
                (int) ((startG + (int) (fraction * (endG - startG))) << 8) |
                (int) ((startB + (int) (fraction * (endB - startB))));
    }





    //// -----------------------------------数据相关------------------------------------------
    //联网操作
    public void  prepareData(){
        switch (kind) {
            case 0://作品
                MyWorkBean bean = new MyWorkBean();
                if (isRefresh) {
                    bean.setPage(0);
                } else if(isLoading){
                    bean.setPage(nextPage);
                }
                bean.setSessionid(UserUtils.getSessionId(this));
                bean.setType(0);
                mPrecenter.doGetMyWorks(bean);
                break;
            case 1://点赞作品
                MyWorkBean bean1 = new MyWorkBean();
                if (isRefresh) {
                    bean1.setPage(0);
                } else if(isLoading){
                    bean1.setPage(nextPage);
                }
                List<String> sts=new ArrayList<>();
                for(int i=0;i<10;i++){
                    sts.add("xxxx"+i);
                }
                mOtherPeopleWorkfragRecyAdapter.setMkind(1);
                mOtherPeopleWorkfragRecyAdapter.setDrafts(sts);
                mOtherPeopleWorkfragRecyAdapter.setNewData(sts);
//                bean1.setSessionid(UserUtils.getSessionId(this));
//                bean1.setType(0);
//               // mPrecenter.doGetMyZanWorks(bean1);
//                mPrecenter.doGetMyWorks(bean1);
                break;
        }
    }

    /*
     *  Fly 注：刷新监听
     */
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener= new AutoRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            isRefresh = true;
            prepareData();
        }
    };

    /*
     *  Fly 注：加载更多监听
     */
    private class MyRequestLoadMoreListener implements BaseQuickAdapter.RequestLoadMoreListener {
        @Override
        public void onLoadMoreRequested() {
            isLoading = true;
            prepareData();
        }
    }

    /*
     *  Fly 注：联网成功拿到数据
     */
    @Override
    public void setData(Object o, int id) {
        switch (id) {
            case 0:  //作品列表数据-----作品
                MyWorkEntity workEntity = (MyWorkEntity) o;
                if (workEntity.getCode() == 0) {
                    if (workEntity.getData() == null) {
                        LogUtils.e("", "myworkentity.getData()==null");
                        hideLoading();
                        return;
                    }
                    nextPage = workEntity.getData().getNext_page();
                    System.out.println("mywork nextpage======>" + nextPage);
                    if (workEntity.getData().getVideo() == null) {
                        LogUtils.e("", "myworkentity.getData().getVideo()==null");
                        hideLoading();
                        return;
                    }
                    if (workEntity.getData().getVideo().getVideos() == null) {
                        LogUtils.e("", "myworkentity.getData().getVideo().getVideos()==null");
                        hideLoading();
                        return;
                    }
                    //呈现
                    if (isRefresh) {
                        isRefresh = false;
                        if (workEntity.getData().getVideo().getVideos().size() == 0) {
                            showMessage("没有数据~");
                            autorefreshOtherpeopleinfo.refreshComplete();
                            return;
                        }
                        if (workEntity.getData().getVideo().getVideos().size() != 0) {
                            mOtherPeopleWorkfragRecyAdapter.setMkind(0);
                            mOtherPeopleWorkfragRecyAdapter.setVideoEntityList(workEntity.getData().getVideo().getVideos());
                            mOtherPeopleWorkfragRecyAdapter.setNewData(workEntity.getData().getVideo().getVideos());
                            autorefreshOtherpeopleinfo.refreshComplete();
                        }
                    } else if (isLoading) {
                        isLoading = false;
                        System.out.println("拿到集合workEntity.getData().getVideo().size()====》"+workEntity.getData().getVideo().getVideos().size());
                        if (workEntity.getData().getVideo().getVideos().size() == 0) {
                            showMessage("没有数据可加载了~");
                            mOtherPeopleWorkfragRecyAdapter.loadMoreEnd();
                        }
                        if (workEntity.getData().getVideo().getVideos().size() != 0) {
                            mOtherPeopleWorkfragRecyAdapter.setMkind(0);
                            mOtherPeopleWorkfragRecyAdapter.setVideoEntityList(workEntity.getData().getVideo().getVideos());
                            mOtherPeopleWorkfragRecyAdapter.addData(workEntity.getData().getVideo());
                            mOtherPeopleWorkfragRecyAdapter.loadMoreComplete();
                        }
                    }
                } else if (workEntity.getCode() == 20202) {
                    LogUtils.e("", "无数据le");
                    mOtherPeopleWorkfragRecyAdapter.loadMoreEnd();
                    return;
                } else if (workEntity.getCode() == 104) {
                    DialogUtils.reLoginAct(this, this.getBaseContext(), "您的账号已在另一台设备上登陆，若非本人操作则账号已泄露，请立即找回密码!", false);
                    hideLoading();
                } else {
                    this.showMessage("请求失败");
                    hideLoading();
                    LogUtils.e("", "请求失败" + workEntity.getCode());
                }
                break;



            case 1: //作品列表数据----- 点赞作品
                MyWorkEntity workEntity1 = (MyWorkEntity) o;
                if (workEntity1.getCode() == 0) {
                    if (workEntity1.getData() == null) {
                        LogUtils.e("", "zan---workentity.getData()==null");
                        hideLoading();
                        return;
                    }
                    nextPage = workEntity1.getData().getNext_page();
                    System.out.println("zan---workentity nextpage======>" + nextPage);
                    if (workEntity1.getData().getVideo() == null) {
                        LogUtils.e("", "zan---workentity.getData().getVideo()==null");
                        hideLoading();
                        return;
                    }
                    if (workEntity1.getData().getVideo().getVideos() == null) {
                        LogUtils.e("", "zan---workentity.getData().getVideo().getVideos()==null");
                        hideLoading();
                        return;
                    }
                    //呈现
                    if (isRefresh) {
                        isRefresh = false;
                        if (workEntity1.getData().getVideo().getVideos().size() == 0) {
                            showMessage("zan---workentity没有数据~");
                            autorefreshOtherpeopleinfo.refreshComplete();
                            return;
                        }
                        if (workEntity1.getData().getVideo().getVideos().size() != 0) {
                            mOtherPeopleWorkfragRecyAdapter.setMkind(1);
                            mOtherPeopleWorkfragRecyAdapter.setZanVideoEntityList(workEntity1.getData().getVideo().getVideos());
                            mOtherPeopleWorkfragRecyAdapter.setNewData(workEntity1.getData().getVideo().getVideos());
                            autorefreshOtherpeopleinfo.refreshComplete();
                        }
                    } else if (isLoading) {
                        isLoading = false;
                        System.out.println("拿到集合zan---workentity.getData().getVideo().size()====》"+workEntity1.getData().getVideo().getVideos().size());
                        if (workEntity1.getData().getVideo().getVideos().size() == 0) {
                            showMessage("zan---workentity没有数据可加载了~");
                            mOtherPeopleWorkfragRecyAdapter.loadMoreEnd();
                        }
                        if (workEntity1.getData().getVideo().getVideos().size() != 0) {
                            mOtherPeopleWorkfragRecyAdapter.setMkind(1);
                            mOtherPeopleWorkfragRecyAdapter.setZanVideoEntityList(workEntity1.getData().getVideo().getVideos());
                            mOtherPeopleWorkfragRecyAdapter.addData(workEntity1.getData().getVideo());
                            mOtherPeopleWorkfragRecyAdapter.loadMoreComplete();
                        }
                    }
                } else if (workEntity1.getCode() == 20202) {
                    LogUtils.e("", "zan---workentity无数据le");
                    mOtherPeopleWorkfragRecyAdapter.loadMoreEnd();
                    return;
                } else if (workEntity1.getCode() == 104) {
                    DialogUtils.reLoginAct(this, this.getBaseContext(), "您的账号已在另一台设备上登陆，若非本人操作则账号已泄露，请立即找回密码!", false);
                    hideLoading();
                } else {
                    this.showMessage("zan---workentity请求失败");
                    hideLoading();
                    LogUtils.e("", "zan---workentity请求失败" + workEntity1.getCode());
                }
                break;

            case 2:

                break;
        }
    }
    @Override
    public void hideLoading() {
        super.hideLoading();
        if (isRefresh) {
            isRefresh = false;
            autorefreshOtherpeopleinfo.refreshComplete();
        } else if (isLoading) {
            isLoading = false;
            mOtherPeopleWorkfragRecyAdapter.loadMoreFail();
        }
    }

}
