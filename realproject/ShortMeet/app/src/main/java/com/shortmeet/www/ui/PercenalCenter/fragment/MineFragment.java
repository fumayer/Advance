package com.shortmeet.www.ui.PercenalCenter.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.qupai.import_core.AliyunIImport;
import com.aliyun.qupai.import_core.AliyunImportCreator;
import com.aliyun.struct.common.AliyunDisplayMode;
import com.aliyun.struct.common.AliyunVideoParam;
import com.aliyun.struct.common.CropKey;
import com.aliyun.struct.common.ScaleMode;
import com.aliyun.struct.common.VideoQuality;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shortmeet.www.Api.ApiConstant;
import com.shortmeet.www.Base.BaseFragment;
import com.shortmeet.www.R;
import com.shortmeet.www.bean.personalCenter.MyWorkBean;
import com.shortmeet.www.entity.percenalCenter.DataEntity;
import com.shortmeet.www.entity.percenalCenter.MyWorkEntity;
import com.shortmeet.www.mvp.IMVPView;
import com.shortmeet.www.mvp.IMVPrecenter;
import com.shortmeet.www.ui.PercenalCenter.activity.CareAboutActivity;
import com.shortmeet.www.ui.PercenalCenter.activity.CommenActivity;
import com.shortmeet.www.ui.PercenalCenter.activity.EditUserInfoActivity;
import com.shortmeet.www.ui.PercenalCenter.activity.FansActivity;
import com.shortmeet.www.ui.PercenalCenter.activity.GetPraiseActivity;
import com.shortmeet.www.ui.PercenalCenter.activity.SettingPageActivity;
import com.shortmeet.www.ui.PercenalCenter.adapter.DraftWorkfragRecyAdapter;
import com.shortmeet.www.ui.PercenalCenter.adapter.MyWorkfragRecyAdapter;
import com.shortmeet.www.ui.PercenalCenter.adapter.ZanWorkfragRecyAdapter;
import com.shortmeet.www.utilsUsed.DialogUtils;
import com.shortmeet.www.utilsUsed.GlideLoader;
import com.shortmeet.www.utilsUsed.LogUtils;
import com.shortmeet.www.utilsUsed.StatusBarUtil;
import com.shortmeet.www.utilsUsed.UiUtils;
import com.shortmeet.www.utilsUsed.UserUtils;
import com.shortmeet.www.views.refreshPart.AutoRefreshLayout;
import com.shortmeet.www.views.widgetPart.RatioImageView;
import com.shortmeet.www.zTakePai.editt.editor.EditorActivity;

import java.io.File;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends BaseFragment implements IMVPView, BaseQuickAdapter.OnItemClickListener, View.OnClickListener, BaseQuickAdapter.OnItemChildClickListener {
    //标识
    private static final String TAG = "MineFragment";
    /*
     *  Fly 注： headView相关
     */
    private View headView;
    //头像
    private RatioImageView imgvHeadMineFrg;
    //关注
    private TextView tvCareMinefrag;
    //关注数
    private TextView tvCarecountMinefrag;
    //粉丝
    private TextView tvFansMinefrag;
    //粉丝数
    private TextView tvFanscount;
    //签名
    private TextView tvSignatureMinefrg;
    //昵称
    private TextView tvNicknameMinefrg;
    //用户id
    private TextView tvUseridMinefrg;
    //编辑资料
    private TextView tvEditInfo;
    //获赞
    private LinearLayout linearGetzanMinefrag;
    // 获赞 图标
    private ImageView imgvZanMineFrg;
    // 获赞图标红点
    private TextView viewZanredpointMinefrag;
    //评论
    private LinearLayout linearCommenMinefrag;
    //评论图标
    private ImageView imgvCommenMineFrg;
    //评论图标红点
    private TextView viewCommenredpointMinefrag;
    // 私信
    private LinearLayout linearMessageMinefrag;
    //私信图标
    private ImageView imgvMessageMineFrg;
    //私信图标红点
    private TextView viewMessageredpointMinefrag;
    //作品数
    private TextView tvMyworkcountMinefrag;
    //作品数下划线
    private View viewMyworklineMinefrag;
    //作品布局
    private RelativeLayout relavMyworkMineFrag;
    //点赞布局
    private RelativeLayout relavZanworkMineFrag;
    //点赞 作品数
    private TextView tvZanworkcountMinefrag;
    //点赞作品 下划线
    private View viewZanlineMinefrag;
    //草稿布局
    private RelativeLayout relavDraftworkMineFrag;
    //草稿作品数
    private TextView tvDraftworkcountMinefrag;
    //草稿作品 下划线
    private View viewDraftlineMinefrag;
    private TextView tvWorkMinefrag;//作品文字
    private TextView tvZanworkMinefrag;//点赞作品文字
    private TextView tvDraftworkMinefrag;//草稿文字

    /*
     *  Fly 注：作品  recycler  和 top 的设置及分享
     */
    private View topStusbarMinefrag;
    private AutoRefreshLayout autorefreshMinefrag;
    private ImageView imgvSettingMineFrg;
    private ImageView imgvShareMineFrg;
    private LinearLayout linearTitlebarMinefrag; //titlebar
    private View viewDividerMinefrag; //titlebar分割线

    /*
     *  Fly 注：状态 ：是否 是刷新   是否是加载更多
     */
    private boolean isRefresh = true;
    private boolean isLoading;
    private int nextPage;

    /*
     *  Fly 注： kind  0 作品   1点赞作品   2草稿
     */
    //recyclerView
    private RecyclerView recyMineFrag;
    private int kind=0;
    //作品  adapter
    private MyWorkfragRecyAdapter mMyWorkfragRecyAdapter;
    //点赞作品Adapter
    private ZanWorkfragRecyAdapter mZanWorkfragRecyAdapter;
    //草稿 adapter
    private DraftWorkfragRecyAdapter  mDraftWorkfragRecyAdapter;
    //presenter
    IMVPrecenter mPrecenter;

    //Recycler  流式管理器
    private  LinearLayoutManager linearLayoutManager;
    //Recycler  方格管理器
    private GridLayoutManager gridLayoutManager;
    //草稿文件列表
    private File[] files;
    //编辑video参数
    private AliyunVideoParam mVideoParam;
    private AliyunIImport mImport;
    private static final int[][] resolutions = new int[][]{new int[]{540, 720},new int[]{540, 540},new int[]{540, 960}};
    private int[] mOutputResolution = null;
    @Override
    public int setFragRootView() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initView() {
        mPrecenter=new IMVPrecenter(this);
        topStusbarMinefrag = (View) contentView.findViewById(R.id.top_stusbar_minefrag);
        autorefreshMinefrag = (AutoRefreshLayout)contentView. findViewById(R.id.autorefresh_minefrag);
        imgvSettingMineFrg = (ImageView) contentView.findViewById(R.id.imgv_setting_mine_frg);
        imgvShareMineFrg = (ImageView) contentView.findViewById(R.id.imgv_share_mine_frg);
        linearTitlebarMinefrag = (LinearLayout)contentView. findViewById(R.id.linear_titlebar_minefrag);
        viewDividerMinefrag = (View) contentView.findViewById(R.id.view_divider_minefrag);
        initStusBar();
        initRefresh();
        initHeadView();
        initRecycler();
    }
    public  void  initStusBar(){
        int stusBarHeight= StatusBarUtil.getStatusBarHeight(mActivity);
        LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) topStusbarMinefrag.getLayoutParams();
        layoutParams.height=stusBarHeight;
        topStusbarMinefrag.setBackgroundColor(UiUtils.getColor(R.color.black));
    }
    public void initRefresh(){
        autorefreshMinefrag.setColorSchemeResources(R.color.black_3);
    }
     public void  initHeadView(){
         headView=mLayoutInflater.inflate(R.layout.item_minefrag_headview,null);
         imgvHeadMineFrg = (RatioImageView) headView.findViewById(R.id.imgv_head_mine_frg);
         tvCareMinefrag = (TextView)headView. findViewById(R.id.tv_care_minefrag);
         tvFansMinefrag = (TextView)headView. findViewById(R.id.tv_fans_minefrag);
         tvCarecountMinefrag = (TextView) headView.findViewById(R.id.tv_carecount_minefrag);
         tvFanscount = (TextView) headView.findViewById(R.id.tv_fanscount);
         tvSignatureMinefrg = (TextView) headView.findViewById(R.id.tv_signature_minefrg);
         tvNicknameMinefrg = (TextView) headView.findViewById(R.id.tv_nickname_minefrg);
         tvUseridMinefrg = (TextView) headView.findViewById(R.id.tv_userid_minefrg);
         tvEditInfo = (TextView) headView.findViewById(R.id.tv_edit_info);
         linearGetzanMinefrag = (LinearLayout) headView.findViewById(R.id.linear_getzan_minefrag);
         linearCommenMinefrag = (LinearLayout) headView.findViewById(R.id.linear_commen_minefrag);
         linearMessageMinefrag = (LinearLayout)headView. findViewById(R.id.linear_message_minefrag);
         imgvZanMineFrg = (ImageView) headView.findViewById(R.id.imgv_zan_mine_frg);
         viewZanredpointMinefrag = (TextView) headView.findViewById(R.id.view_zanredpoint_minefrag);
         imgvCommenMineFrg = (ImageView) headView.findViewById(R.id.imgv_commen_mine_frg);
         viewCommenredpointMinefrag = (TextView)headView. findViewById(R.id.view_commenredpoint_minefrag);
         imgvMessageMineFrg = (ImageView)headView. findViewById(R.id.imgv_message_mine_frg);
         viewMessageredpointMinefrag = (TextView) headView.findViewById(R.id.view_messageredpoint_minefrag);
         tvMyworkcountMinefrag = (TextView)headView. findViewById(R.id.tv_myworkcount_minefrag);
         viewMyworklineMinefrag = (View) headView.findViewById(R.id.view_myworkline_minefrag);
         tvZanworkcountMinefrag = (TextView) headView.findViewById(R.id.tv_zanworkcount_minefrag);
         viewZanlineMinefrag = (View)headView. findViewById(R.id.view_zanline_minefrag);
         tvDraftworkcountMinefrag = (TextView)headView. findViewById(R.id.tv_draftworkcount_minefrag);
         viewDraftlineMinefrag = (View) headView.findViewById(R.id.view_draftline_minefrag);
         relavMyworkMineFrag = (RelativeLayout)  headView.findViewById(R.id.relav_mywork_mine_frag);
         relavZanworkMineFrag = (RelativeLayout)  headView.findViewById(R.id.relav_zanwork_mine_frag);
         relavDraftworkMineFrag = (RelativeLayout)  headView.findViewById(R.id.relav_draftwork_mine_frag);
         tvWorkMinefrag = (TextView) headView.findViewById(R.id.tv_work_minefrag);
         tvZanworkMinefrag = (TextView) headView.findViewById(R.id.tv_zanwork_minefrag);
         tvDraftworkMinefrag = (TextView) headView.findViewById(R.id.tv_draftwork_minefrag);
     }
    public void  initRecycler(){
        recyMineFrag = (RecyclerView)contentView.findViewById(R.id.recy_mine_frag);
        linearLayoutManager=new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        gridLayoutManager=new GridLayoutManager(mContext,2);

        recyMineFrag.setLayoutManager(gridLayoutManager);
        //作品
        mMyWorkfragRecyAdapter=new MyWorkfragRecyAdapter(R.layout.item_recycler_threehorizantal_minefrag,null);
        //点赞作品
        mZanWorkfragRecyAdapter=new ZanWorkfragRecyAdapter(R.layout.item_recycler_threehorizantal_minefrag,null);
        //草稿
        mDraftWorkfragRecyAdapter=new DraftWorkfragRecyAdapter(R.layout.item_recycler_mydraft_minefrag,null);
        mDraftWorkfragRecyAdapter.setOnItemChildClickListener(this);
        if(kind==0){ //作品
        LogUtils.e("作品","-------+kind===0--");
        removeHeadView();
        mMyWorkfragRecyAdapter.addHeaderView(headView,0);
        recyMineFrag.setAdapter(mMyWorkfragRecyAdapter);
        }else if(kind==1){  //点赞作品
        LogUtils.e("作品","-------+kind===1--");
        removeHeadView();
        mZanWorkfragRecyAdapter.addHeaderView(headView,0);
         recyMineFrag.setAdapter(mZanWorkfragRecyAdapter);
        }else if(kind==2){//草稿
       LogUtils.e("作品","-------+kind===2--");
        removeHeadView();
        mDraftWorkfragRecyAdapter.addHeaderView(headView,0);
         recyMineFrag.setAdapter(mDraftWorkfragRecyAdapter);
        }
    }

    @Override
    public void initListener() {
        imgvShareMineFrg.setOnClickListener(this);//分享
        imgvSettingMineFrg.setOnClickListener(this);//设置
        tvCareMinefrag.setOnClickListener(this);//关注
        tvFansMinefrag.setOnClickListener(this);//粉丝
        tvEditInfo.setOnClickListener(this);//编辑资料
        linearGetzanMinefrag.setOnClickListener(this);//获赞
        linearCommenMinefrag.setOnClickListener(this);//评论
        linearMessageMinefrag.setOnClickListener(this);//私信
        relavMyworkMineFrag.setOnClickListener(this);
        relavZanworkMineFrag.setOnClickListener(this);
        relavDraftworkMineFrag.setOnClickListener(this);
        autorefreshMinefrag.setOnRefreshListener(mOnRefreshListener);
        //作品
        mMyWorkfragRecyAdapter.setOnLoadMoreListener(new MineFragment.MyRequestLoadMoreListener(), recyMineFrag);//***
        mMyWorkfragRecyAdapter.disableLoadMoreIfNotFullPage();//***
        //点赞作品
        mZanWorkfragRecyAdapter.setOnLoadMoreListener(new MineFragment.MyRequestLoadMoreListener(), recyMineFrag);//***
        mZanWorkfragRecyAdapter.disableLoadMoreIfNotFullPage();//***
        //草稿
        mDraftWorkfragRecyAdapter.setOnLoadMoreListener(new MineFragment.MyRequestLoadMoreListener(), recyMineFrag);//***
        mDraftWorkfragRecyAdapter.disableLoadMoreIfNotFullPage();//***

        //渐变标题栏相关
        //标题栏渐变   获取标题和封面布局的高度
        linearTitlebarMinefrag.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener());
        imgvHeadMineFrg.getViewTreeObserver().addOnGlobalLayoutListener(new  MyOnGlobalLayoutListener());
        //设置recyclerView 滑动的监听
        recyMineFrag.addOnScrollListener(new MyOnScrollListener());
    }
    @Override
    public void initData() { autorefreshMinefrag.autoRefresh(mOnRefreshListener);}

   /*
    *  Fly 注： 回到此界面 刷新界面数据
    */
    @Override
    public void onResume() {
        super.onResume();
        DataEntity user = UserUtils.getUser(mContext);
        //昵称
        tvNicknameMinefrg.setText(user.getNickname());
        //ID
      //  tvUseridMinefrg.setText(user.getUser_id());
        //签名
        tvSignatureMinefrg.setText(user.getContent());
        //背景/头像
        // GlideLoader.getInstance().loadImg(mContext,user.getImg(),R.drawable.ass,imgvHeadMineFrg);
        GlideLoader.getInstance().loadImg(mContext,user.getImg(),true,true,imgvHeadMineFrg);
        showData();
    }

    /*
     *  Fly 注：控件监听
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_share_mine_frg://分享
               showMessage("分享");
                break;
            case R.id.imgv_setting_mine_frg:  //设置
                startActivity(new Intent(mContext, SettingPageActivity.class));
                break;
            case R.id.tv_care_minefrag:
                showMessage("关注");
                startActivity(new Intent(mContext, CareAboutActivity.class));
                break;
            case R.id.tv_fans_minefrag:
                showMessage("粉丝");
                startActivity(new Intent(mContext, FansActivity.class));
                break;
            case R.id.tv_edit_info:
              startActivity(new Intent(mContext,EditUserInfoActivity.class));
                break;
            case R.id.linear_getzan_minefrag:
                showMessage("获赞");
                startActivity(new Intent(mContext,GetPraiseActivity.class));
                break;
            case R.id.linear_commen_minefrag:
                showMessage("评论");
                startActivity(new Intent(mContext,CommenActivity.class));
                break;
            case R.id.linear_message_minefrag:
                showMessage("私信");
                break;
            case R.id.relav_mywork_mine_frag://作品
                hideLine();
                viewMyworklineMinefrag.setVisibility(View.VISIBLE);
                tvWorkMinefrag.setTextColor(UiUtils.getColor(R.color.black_3));
                tvMyworkcountMinefrag.setTextColor(UiUtils.getColor(R.color.black_3));
                kind=0;
                isRefresh = true;
                isLoading=false;
                scrollDistance=0;
                recyMineFrag.setLayoutManager(gridLayoutManager);//重新设置recycler的布局管理器
                removeHeadView();
                mMyWorkfragRecyAdapter.addHeaderView(headView,0);
                recyMineFrag.setAdapter(mMyWorkfragRecyAdapter);
                prepareData();
                break;
            case R.id.relav_zanwork_mine_frag://点赞作品
                hideLine();
                viewZanlineMinefrag.setVisibility(View.VISIBLE);
                tvZanworkMinefrag.setTextColor(UiUtils.getColor(R.color.black_3));
                tvZanworkcountMinefrag.setTextColor(UiUtils.getColor(R.color.black_3));
                kind=1;
                isRefresh = true;
                isLoading=false;
                scrollDistance=0;
                removeHeadView();
                mZanWorkfragRecyAdapter.addHeaderView(headView,0);
                recyMineFrag.setAdapter(mZanWorkfragRecyAdapter);
                prepareData();
                break;
            case R.id.relav_draftwork_mine_frag://草稿
                hideLine();
                viewDraftlineMinefrag.setVisibility(View.VISIBLE);
                tvDraftworkMinefrag.setTextColor(UiUtils.getColor(R.color.black_3));
                tvDraftworkcountMinefrag.setTextColor(UiUtils.getColor(R.color.black_3));
                kind=2;
                isRefresh = true;
                isLoading=false;
                scrollDistance=0;
                recyMineFrag.setLayoutManager(linearLayoutManager);//重新设置recycler的布局管理器
                removeHeadView();
                mDraftWorkfragRecyAdapter.addHeaderView(headView,0);
                recyMineFrag.setAdapter(mDraftWorkfragRecyAdapter);
                prepareData();
                break;
        }
    }
    //还原  作品  点赞   草稿  字体设置为默认   下划线  全都设置为隐藏
    public void  hideLine(){
     //线条
     viewMyworklineMinefrag.setVisibility(View.GONE);
     viewZanlineMinefrag.setVisibility(View.GONE);
     viewDraftlineMinefrag.setVisibility(View.GONE);
    //文字
     tvWorkMinefrag.setTextColor(UiUtils.getColor(R.color.black_5));
     tvZanworkMinefrag.setTextColor(UiUtils.getColor(R.color.black_5));
     tvDraftworkMinefrag.setTextColor(UiUtils.getColor(R.color.black_5));
     //数字
     tvMyworkcountMinefrag.setTextColor(UiUtils.getColor(R.color.black_5));
     tvZanworkcountMinefrag.setTextColor(UiUtils.getColor(R.color.black_5));
     tvDraftworkcountMinefrag.setTextColor(UiUtils.getColor(R.color.black_5));
    }
    public void removeHeadView(){
        mMyWorkfragRecyAdapter.removeHeaderView(headView);
        mZanWorkfragRecyAdapter.removeHeaderView(headView);
        mDraftWorkfragRecyAdapter.removeHeaderView(headView);

    }
    //条目监听
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        showMessage("我是条目"+position);
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
    private String beforePath = null;
    //条目点击
    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()){
            case R.id.ll_mine_draft_editor:
                if (files!=null &&files.length>0){
                    if (mImport!=null) {
                        mImport=null;
                    }
                    mImport = AliyunImportCreator.getImportInstance(mContext);//由于removeVideo已失效，所以每次都需要重新获取新的AliyunIImport
                    mImport.setVideoParam(mVideoParam);

                    mImport.addVideo(files[position].getAbsolutePath(), 0, AliyunDisplayMode.DEFAULT);
                    beforePath=files[position].getAbsolutePath();
                    String projectJsonPath = mImport.generateProjectConfigure();
                    if (projectJsonPath != null) {
                        Intent intent = new Intent(mContext, EditorActivity.class);
                        intent.putExtra("video_param", mVideoParam);
                        intent.putExtra("project_json_path", projectJsonPath);
                        startActivity(intent);
                    }
                }
                break;
        }
    }

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


  //联网操作-----------------------------------------------
    //展示--->数据
    public void showData(){
     showZanCount();
    }
    public void showZanCount(){
     //获赞总数  点赞总数接口
//        ZanCountMineFrgBean bean=new ZanCountMineFrgBean();
//        bean.setSessionid(UserUtils.getSessionId(mContext));
//        mPrecenter.doZanCount(bean);
    }
    public void  prepareData(){
        switch (kind) {
            case 0://作品
                MyWorkBean bean = new MyWorkBean();
                if (isRefresh) {
                    bean.setPage(0);
                } else if(isLoading){
                    bean.setPage(nextPage);
                }
                bean.setSessionid(UserUtils.getSessionId(mContext));
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
                bean1.setSessionid(UserUtils.getSessionId(mContext));
                bean1.setType(0);
                mPrecenter.doGetMyWorks(bean1);
                break;
            case 2://草稿;
                files= new File(ApiConstant.COMPOSE_PAYH_DRAFT).listFiles();
                mDraftWorkfragRecyAdapter.setNewData(Arrays.asList(files));
                mDraftWorkfragRecyAdapter.loadMoreEnd();

                if (files!=null &&files.length>0){
                    if (mVideoParam==null){
                        mOutputResolution = resolutions[CropKey.RATIO_MODE_3_4];
                        mVideoParam = new AliyunVideoParam.Builder()
                                .frameRate(25)
                                .gop(125)
                                .videoQuality(VideoQuality.HD)
                                .scaleMode(ScaleMode.PS)
                                .outputWidth(mOutputResolution[0])
                                .outputHeight(mOutputResolution[1])
                                .build();
                    }
                }

                break;
        }
    }

// Fly 注：联网成功拿到数据-------------------------------------------
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
                            autorefreshMinefrag.refreshComplete();
                            return;
                        }
                        if (workEntity.getData().getVideo().getVideos().size() != 0) {
                            mMyWorkfragRecyAdapter.setVideoEntityList(workEntity.getData().getVideo().getVideos());
                            mMyWorkfragRecyAdapter.setNewData(workEntity.getData().getVideo().getVideos());
                            autorefreshMinefrag.refreshComplete();
                        }
                        LogUtils.e("刷新---当前行大实体-------》",""+workEntity.getData().getVideo()+"    type=>"+workEntity.getData().getVideo().getType());
                        LogUtils.e("刷新---当前行数据数目-------》",""+workEntity.getData().getVideo().getVideos().size());
                    } else if (isLoading) {
                        isLoading = false;
                        System.out.println("拿到集合workEntity.getData().getVideo().size()====》"+workEntity.getData().getVideo().getVideos().size());
                        if (workEntity.getData().getVideo().getVideos().size() == 0) {
                            showMessage("没有数据可加载了~");
                            mMyWorkfragRecyAdapter.loadMoreEnd();
                        }
                        LogUtils.e("加载---当前行大实体-------》",""+workEntity.getData().getVideo()+"    type=>"+workEntity.getData().getVideo().getType());
                        LogUtils.e("加载---当前行数据数目-------》",""+workEntity.getData().getVideo().getVideos().size());
                        if (workEntity.getData().getVideo().getVideos().size() != 0) {
                            mMyWorkfragRecyAdapter.setVideoEntityList(workEntity.getData().getVideo().getVideos());
                            mMyWorkfragRecyAdapter.addData(workEntity.getData().getVideo().getVideos());
                            mMyWorkfragRecyAdapter.loadMoreComplete();
                        }
                    }
                } else if (workEntity.getCode() == 20202) {
                    LogUtils.e("", "无数据le");
                    autorefreshMinefrag.refreshComplete();
                    mMyWorkfragRecyAdapter.loadMoreEnd();
                    System.out.println("执行了。。。。");
                    return;
                } else if (workEntity.getCode() == 104) {
                    DialogUtils.reLoginAct(this, mContext, "您的账号已在另一台设备上登陆，若非本人操作则账号已泄露，请立即找回密码!", false);
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
                            autorefreshMinefrag.refreshComplete();
                            return;
                        }
                        if (workEntity1.getData().getVideo().getVideos().size() != 0) {
                            mZanWorkfragRecyAdapter.setZanVideoEntityList(workEntity1.getData().getVideo().getVideos());
                            mZanWorkfragRecyAdapter.setNewData(workEntity1.getData().getVideo().getVideos());
                            autorefreshMinefrag.refreshComplete();
                        }
                    } else if (isLoading) {
                        isLoading = false;
                        System.out.println("拿到集合zan---workentity.getData().getVideo().size()====》"+workEntity1.getData().getVideo().getVideos().size());
                        if (workEntity1.getData().getVideo().getVideos().size() == 0) {
                            showMessage("没有数据可加载了~");
                            mZanWorkfragRecyAdapter.loadMoreEnd();
                        }
                        if (workEntity1.getData().getVideo().getVideos().size() != 0) {
                            mZanWorkfragRecyAdapter.setZanVideoEntityList(workEntity1.getData().getVideo().getVideos());
                            mZanWorkfragRecyAdapter.addData(workEntity1.getData().getVideo().getVideos());
                            mZanWorkfragRecyAdapter.loadMoreComplete();
                        }
                    }
                } else if (workEntity1.getCode() == 20202) {
                    LogUtils.e("", "zan---workentity无数据le");
                    autorefreshMinefrag.refreshComplete();
                    mZanWorkfragRecyAdapter.loadMoreEnd();
                    return;
                } else if (workEntity1.getCode() == 104) {
                    DialogUtils.reLoginAct(this, mContext, "您的账号已在另一台设备上登陆，若非本人操作则账号已泄露，请立即找回密码!", false);
                    hideLoading();
                } else {
                    this.showMessage("zan---workentity请求失败");
                    hideLoading();
                    LogUtils.e("", "zan---workentity请求失败" + workEntity1.getCode());
                }
                break;
        }
    }
    @Override
    public void hideLoading() {
        super.hideLoading();
        if (isRefresh) {
            isRefresh = false;
            autorefreshMinefrag.refreshComplete();
        } else if (isLoading) {
            isLoading = false;
            if(kind==0){
                mMyWorkfragRecyAdapter.loadMoreFail();
            }else if(kind==1){
                mZanWorkfragRecyAdapter.loadMoreFail();
            }else if(kind==2){
                mDraftWorkfragRecyAdapter.loadMoreFail();
            }

        }
    }


    /*
     *  Fly 注：----------------------------渐变标题栏相关-------------------------------
     */
    //标题的高度
    private int titleHeight;
    //封面的高度
    private int thumbHeight;
    //需要滑动的高度
    private int needScrollHeight;
    private class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        @Override
        public void onGlobalLayout() {
            linearTitlebarMinefrag.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            imgvHeadMineFrg.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            titleHeight = linearTitlebarMinefrag.getHeight();
            thumbHeight = imgvHeadMineFrg.getHeight();
            needScrollHeight = thumbHeight - titleHeight;
            System.out.println("titleHeight"+titleHeight+"   thumbHeight"+thumbHeight+"     needScrollHeight"+needScrollHeight);
        }
    }
    //RecyclerView 竖直方向滑动的距离
    private int scrollDistance;
    private class MyOnScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            scrollDistance += dy;
            if (needScrollHeight > 0 && scrollDistance > needScrollHeight) {
                linearTitlebarMinefrag.setBackgroundResource(R.color.white);
                viewDividerMinefrag.setBackgroundColor(UiUtils.getColor(R.color.black_change4));
            } else {
                if (needScrollHeight <= 0) {
                    linearTitlebarMinefrag.setBackgroundResource(R.color.transparent);
                    viewDividerMinefrag.setBackgroundResource(R.color.transparent);
                    LogUtils.e("needScrollHeight <= 0","needScrollHeight <= 0");
                    return;
                }
                linearTitlebarMinefrag.setBackgroundDrawable(UiUtils.getDrawable(R.color.transparent));
                viewDividerMinefrag.setBackgroundDrawable(UiUtils.getDrawable(R.color.transparent));
                float percent = scrollDistance * 1.f / needScrollHeight;
                Log.d("scroll-->", "onScroll: percent: " + percent);
                int evaluateArgb = evaluateArgb(percent, Color.TRANSPARENT, Color.WHITE);
                int evaluateArgb2=evaluateArgb(percent, Color.TRANSPARENT,UiUtils.getColor(R.color.black_change4));
                linearTitlebarMinefrag.getBackground().mutate().setColorFilter(evaluateArgb, PorterDuff.Mode.SRC_OVER);
                viewDividerMinefrag.getBackground().mutate().setColorFilter(evaluateArgb2, PorterDuff.Mode.SRC_OVER);
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
}
