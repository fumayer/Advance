package com.shortmeet.www.ui.Video.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.makeramen.roundedimageview.RoundedImageView;
import com.shortmeet.www.Base.BaseActivity;
import com.shortmeet.www.R;
import com.shortmeet.www.bean.video.AddCancelCareBean;
import com.shortmeet.www.bean.video.ClickZanBean;
import com.shortmeet.www.bean.video.DeleteCommenBean;
import com.shortmeet.www.bean.video.GetCommenListBean;
import com.shortmeet.www.bean.video.ReplyBean;
import com.shortmeet.www.bean.video.SendCommenBean;
import com.shortmeet.www.bean.video.ShowZanHeadsBean;
import com.shortmeet.www.bean.video.VideoDetailStatusShowBean;
import com.shortmeet.www.entity.video.AddCancelCareEntity;
import com.shortmeet.www.entity.video.ClickZanEntity;
import com.shortmeet.www.entity.video.DeleteCommenEntity;
import com.shortmeet.www.entity.video.GetCommenListEntity;
import com.shortmeet.www.entity.video.ReplyEntity;
import com.shortmeet.www.entity.video.SendCommenEntity;
import com.shortmeet.www.entity.video.ShowZanHeadsEntity;
import com.shortmeet.www.entity.video.VideoDetailStatusEntity;
import com.shortmeet.www.mvp.IMVPView;
import com.shortmeet.www.mvp.IMVPrecenter;
import com.shortmeet.www.ui.PercenalCenter.activity.LoginActivity;
import com.shortmeet.www.ui.Video.adapter.VideoDetailAdapter;
import com.shortmeet.www.ui.Video.adapter.ViedeoDetialRecyHeadsAdapter;
import com.shortmeet.www.utilsOther.SoftKeyboardStateHelper;
import com.shortmeet.www.utilsUsed.DialogUtils;
import com.shortmeet.www.utilsUsed.DimenExchangeUtil;
import com.shortmeet.www.utilsUsed.GlideLoader;
import com.shortmeet.www.utilsUsed.InputMethodUtils;
import com.shortmeet.www.utilsUsed.LogUtils;
import com.shortmeet.www.utilsUsed.StatusBarUtil;
import com.shortmeet.www.utilsUsed.UiUtils;
import com.shortmeet.www.utilsUsed.UserUtils;
import com.shortmeet.www.views.recyItemDecoraPart.ItemDecorahorizentialHead;
import com.shortmeet.www.views.recyItemDecoraPart.NoScrollLinearHorzantialLayoutManager;
import com.shortmeet.www.views.refreshPart.AutoRefreshLayout;
import com.shortmeet.www.views.widgetPart.GoodManVideoPlayerPriview;

import java.util.List;

import cn.jzvd.JZVideoPlayerStandard;

import static com.shortmeet.www.R.id.framelay_arrow_back_videodetail;
import static com.shortmeet.www.R.id.tv_comment_count;

public class VideDetailActivity extends BaseActivity implements IMVPView, View.OnClickListener, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemLongClickListener {
    // Fly 注：代替状态栏
    private View topStusbar;
    /*
     *  Fly 注：头布局
     */
    private View headView;
    //播放器
    private GoodManVideoPlayerPriview playerVideoPlayer;
    //头布局 头像
    private RoundedImageView imgvHeadVideoDetail;
    //头布局 昵称
    private TextView tvNickmameVideodetail;
    //头布局 时间
    private TextView tvTimeVideodetail;
    //头布局 关注状态
    private ImageView ivCarezhuVideodetail;
    //头布局 标题内容
    private TextView tvContentVideodetail;
    //头布局 地址
    private TextView tvAddress;
    //头布局 点赞状态
    private ImageView ivZan;
    //头布局  点赞头像 recycler
    private RecyclerView recyZanhead;
    ViedeoDetialRecyHeadsAdapter headsAdapter;
    //头布局 头像点赞数
    private TextView tvZanheadCount;
   //评论数
    private TextView tvCommentCount;

    /*
     *  Fly 注：receycler 上层 返回   分享 更多 和  下方发布
     */
    private EditText edtCommen;
    private ImageView ivRelease;
    private TextView tvRelease;
    private FrameLayout framelayArrowBackVideodetail;
    private FrameLayout framelayFenxiangVideodetail;
    private FrameLayout framelayoutMoreVideodetail;
    private ImageView imgvArrowBackVideodetail;
    private ImageView imgvFenxiangVideodetail;
    private ImageView imgvMoreVideodetail;
    private FrameLayout frameTopbarVideodetail;
    private View viewDividerVideodetail; //分割线


    //整个布局的 recycle  （即：内容recycler）
    private RecyclerView recyleVideoDetail;
    // 当前整个界面adapter
    private VideoDetailAdapter mVideoDetailAdapter;
    //刷新
    private AutoRefreshLayout autorefreshVideodetail;
    // Fly 注：状态 ：是否 是刷新   是否是加载更多
    private boolean isRefresh = true;
    private boolean isLoading;
    private int nextPage;
    //成员
    private String table_name;
    private String thumb_up_id;
    //presenter
    private IMVPrecenter mPrecenter;

    /*
    *  Fly 注： 传过来的 coverurl 和playurl   和videoid;
    */
    private String  coverurl;
    private String  playurl;
    private String  videoid;
    private String  name;
    private String  area;
    private String  time;
    private String  title;
    private String  accountId;

    // Fly 注：评论列表 实体
    private List<GetCommenListEntity.DataEntity.ListEntity> mListEntities;
    private int pos;//点击评论时 记录实体条目位置
   // replyType   1 是直接评论， 2 是 点击条目回复别人
    private  int replyType=1;
    // Fly 注：键盘帮助类
    private SoftKeyboardStateHelper softKeyboardStateHelper;
    // Fly 注：点击条目弹出 popUpWindow  提示 删除或者举报
    private View popView;
    private PopupWindow mpopDelerReport;



    @Override
    public int setRootView() {
        return R.layout.activity_vide_detail;
    }
    @Override
    public void initView() {
      // StatusBarUtil.translucentStatusBar(this, false);//设置状态栏透明 内容延伸到状态栏
        softKeyboardStateHelper = new SoftKeyboardStateHelper(this.getWindow().getDecorView());
        mPrecenter=new IMVPrecenter(this);
        topStusbar = this.findViewById(R.id.top_stusbar);

        playurl=this.getIntent().getStringExtra("playurl");
        coverurl=this.getIntent().getStringExtra("coverurl");
        videoid=this.getIntent().getStringExtra("video_id");
        name=this.getIntent().getStringExtra("name");
        area=this.getIntent().getStringExtra("area");
        time=this.getIntent().getStringExtra("time");
        title=this.getIntent().getStringExtra("title");
        accountId=this.getIntent().getStringExtra("account_id");

        autorefreshVideodetail = (AutoRefreshLayout) findViewById(R.id.autorefresh_videodetail);
        edtCommen = (EditText) findViewById(R.id.edt_commen);
        ivRelease = (ImageView) findViewById(R.id.iv_release);
        tvRelease = (TextView) findViewById(R.id.tv_release);
        imgvArrowBackVideodetail = (ImageView) findViewById(R.id.imgv_arrow_back_videodetail);
        imgvFenxiangVideodetail = (ImageView) findViewById(R.id.imgv_fenxiang_videodetail);
        imgvMoreVideodetail = (ImageView) findViewById(R.id.imgv_more_videodetail);
        framelayArrowBackVideodetail = (FrameLayout) findViewById(framelay_arrow_back_videodetail);
        framelayFenxiangVideodetail = (FrameLayout) findViewById(R.id.framelay_fenxiang_videodetail);
        framelayoutMoreVideodetail = (FrameLayout) findViewById(R.id.framelayout_more_videodetail);
        frameTopbarVideodetail = (FrameLayout) findViewById(R.id.frame_topbar_videodetail);//toolbar
        viewDividerVideodetail = (View) findViewById(R.id.view_divider_videodetail);
       // initStusBar();
        initHeadView();
        initRecyView();
        initVideoPalyer();
    }
    public  void  initStusBar(){
        int stusBarHeight= StatusBarUtil.getStatusBarHeight(this);
        FrameLayout.LayoutParams layoutParams= (FrameLayout.LayoutParams) topStusbar.getLayoutParams();
        layoutParams.height=stusBarHeight;
        topStusbar.setBackgroundColor(Color.BLACK);
    }
     public void initHeadView(){
       headView=mLayoutInflater.inflate(R.layout.item_headview_videodetail,null,false);
       playerVideoPlayer = (GoodManVideoPlayerPriview) headView.findViewById(R.id.player_video_player);
       imgvHeadVideoDetail = (RoundedImageView)headView. findViewById(R.id.imgv_head_video_detail);
       tvNickmameVideodetail = (TextView)headView. findViewById(R.id.tv_nickmame_videodetail);
       tvTimeVideodetail = (TextView)headView. findViewById(R.id.tv_time_videodetail);
       ivCarezhuVideodetail = (ImageView)headView. findViewById(R.id.iv_carezhu_videodetail);
       tvContentVideodetail = (TextView)headView. findViewById(R.id.tv_content_videodetail);
       tvAddress = (TextView) headView.findViewById(R.id.tv_address);
       ivZan = (ImageView)headView. findViewById(R.id.iv_zan);
       recyZanhead = (RecyclerView)headView. findViewById(R.id.recy_zanhead);
       initRecyzanHead();
       tvZanheadCount = (TextView)headView. findViewById(R.id.tv_zanhead_count);
       tvCommentCount = (TextView)headView. findViewById(tv_comment_count);
     }
    public void initRecyzanHead(){
        NoScrollLinearHorzantialLayoutManager  linearLayoutManager=new NoScrollLinearHorzantialLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyZanhead.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setScrollEnabled(false);
        headsAdapter=new ViedeoDetialRecyHeadsAdapter(R.layout.item_recycler_heads_videodetail,null);
        recyZanhead.addItemDecoration(new ItemDecorahorizentialHead(UiUtils.dp2px(10)));
        recyZanhead.setAdapter(headsAdapter);
    }
    public void initRecyView(){
        recyleVideoDetail = (RecyclerView) findViewById(R.id.recyle_video_detail);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyleVideoDetail.setLayoutManager(manager);
        mVideoDetailAdapter = new VideoDetailAdapter(R.layout.item_recycler_coment_videodetail, null);
        mVideoDetailAdapter.addHeaderView(headView);
        recyleVideoDetail.setAdapter(mVideoDetailAdapter);
    }
    public void initVideoPalyer(){
        playerVideoPlayer.backButton.setVisibility(View.GONE);
        playerVideoPlayer.tinyBackImageView.setVisibility(View.GONE);
        playerVideoPlayer.widthRatio = 3;
        playerVideoPlayer.heightRatio = 2;
    }
    @Override
    public void initListener() {
        ivRelease.setOnClickListener(this);//发布图标
        tvRelease.setOnClickListener(this);//发布按钮
        framelayArrowBackVideodetail.setOnClickListener(this);//左上方返回
        framelayFenxiangVideodetail.setOnClickListener(this);//分享
        framelayoutMoreVideodetail.setOnClickListener(this);//更多
        imgvHeadVideoDetail.setOnClickListener(this);//头像
        ivCarezhuVideodetail.setOnClickListener(this);//关注
        ivZan.setOnClickListener(this);//赞   取消赞
        tvZanheadCount.setOnClickListener(this);//赞数
        // Fly 注 刷新  加载 相关
        autorefreshVideodetail.setOnRefreshListener(mOnRefreshListener);
        mVideoDetailAdapter.setOnLoadMoreListener(new MyRequestLoadMoreListener(), recyleVideoDetail);//***
        mVideoDetailAdapter.disableLoadMoreIfNotFullPage();

        //标题栏渐变   获取标题和封面布局的高度
        frameTopbarVideodetail.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener());
        playerVideoPlayer.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener());
        //设置recyclerView 滑动的监听
        recyleVideoDetail.addOnScrollListener(new MyOnScrollListener());
        //条目点击监听
        mVideoDetailAdapter.setOnItemClickListener(this);
        mVideoDetailAdapter.setOnItemLongClickListener(this);

        //设置键盘弹起/收起的监听
        softKeyboardStateHelper.addSoftKeyboardStateListener(new MySoftKeyboardStateListener());
    }

    @Override
    public void initData() {
        GlideLoader.getInstance().loadCircleImg(this,coverurl,R.drawable.p1,imgvHeadVideoDetail);//头像
        tvNickmameVideodetail.setText(name);//昵称
        tvTimeVideodetail.setText(time);//时间
        tvContentVideodetail.setText(title);//地址
        tvAddress.setText(area); //地区
        playerVideoPlayer.setSilencePattern(false);
        playerVideoPlayer.setUp(playurl, JZVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");
        if (!TextUtils.isEmpty(coverurl)) {
       GlideLoader.getInstance().loadImgPlaceAndErrSame(this.getBaseContext(),coverurl,R.drawable.placeholder_w_h_32,playerVideoPlayer.thumbImageView);
        } else {
       Glide.with(this.getBaseContext()).load(R.drawable.p1).centerCrop().crossFade().into(playerVideoPlayer.thumbImageView);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
       autorefreshVideodetail.autoRefresh(mOnRefreshListener);
       showData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case framelay_arrow_back_videodetail:
                this.finish();
                break;
            case R.id.imgv_head_video_detail://头像

                break;
            case R.id.iv_carezhu_videodetail://关注
                if(UserUtils.getUserIdintify(this)==0){    //游客身份  登录界面
                    showMessage("请先登录");
                    startActivity(new Intent(this, LoginActivity.class));
                    return;
                }
                // TODO: 2017/11/14 不能对自己进行 关注 取消关注哦
                LogUtils.e("---",""+accountId+"我的id"+UserUtils.getUser(this).getAccount_id());
                if(String.valueOf(UserUtils.getUser(this).getAccount_id()).equals(accountId)){
                    showMessage("不能对自己此操作哦");
                    return;
                }
                doActAddCancelCare();
                break;
            case R.id.iv_zan://赞 取消赞
                if(UserUtils.getUserIdintify(this)==0){    //游客身份  登录界面
                    showMessage("请先登录");
                    startActivity(new Intent(this, LoginActivity.class));
                    return;
                }
                doActZanCancel();
                break;
            case R.id.tv_zanhead_count://赞数->赞过的用户
                Intent  intent=new Intent(this,ZanHeadVideDetailActivity.class);
                intent.putExtra("vodid",videoid);
                startActivity(intent);
                break;
            case R.id.iv_release://发布按钮弹出软键盘，  或 切换到表情

                break;
            case R.id.tv_release://发布评论
                if(UserUtils.getUserIdintify(this)==0){    //游客身份  登录界面
                    showMessage("请先登录");
                    startActivity(new Intent(this, LoginActivity.class));
                    return;
                }
                // TODO: 2017/11/14 不能评论自己哦
                if(String.valueOf(UserUtils.getUser(this).getAccount_id()).equals(accountId)){
                    showMessage("不能对自己此操作哦");
                    return;
                }
                if(TextUtils.isEmpty(edtCommen.getText().toString())){
                    showMessage("请输入文字");
                    return;
                }
                if(replyType==1){  //直接评论 视频详情
                 relaseCommen(edtCommen.getText().toString());
                }else if(replyType==2){// 点击条目的   回复别人
                 doReplayAction();
                }
                break;
        }
    }
    //发评论
    public void relaseCommen(String content){
        SendCommenBean  bean=new SendCommenBean();
        bean.setSessionid(UserUtils.getSessionId(this));
        bean.setVod_id(videoid);
        bean.setContent(content);
        mPrecenter.doSendCommen(bean);
    }
    //  回复评论   或者回复回复
    public void doReplayAction(){
       ReplyBean bean=new ReplyBean();
       bean.setContent(edtCommen.getText().toString());
       bean.setId(mListEntities.get(pos).getId());
       bean.setSessionid(UserUtils.getSessionId(this));
       mPrecenter.doReplyAct(bean);
    }
    // 删除评论
    public void doDeleteCommen(BaseQuickAdapter adapter,int pos){
      if(UserUtils.getUserIdintify(this)==0){    //游客身份  登录界面
            showMessage("请先登录");
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
      // TODO: 2017/11/14 只能删除自己的评论哦
      List<GetCommenListEntity.DataEntity.ListEntity> mList=adapter.getData();
      DeleteCommenBean deleteCommenBean=new DeleteCommenBean();
      deleteCommenBean.setId(mList.get(pos).getId());
      deleteCommenBean.setSessionid(UserUtils.getSessionId(this));
      mPrecenter.doDeleteCommenAct(deleteCommenBean);
    }
   //举报
    public void doReportAct(){

    }

    // 评论列表条目监听  弹出软键盘
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        mListEntities=adapter.getData();
        pos=position;
        replyType=2;
        edtCommen.setHint("回复:");
        //弹出键盘
        edtCommen.setCursorVisible(true);
        InputMethodUtils.toggleKeyboard(edtCommen);
    }
    /**
     * 键盘弹起/收起的监听
     */
    private class MySoftKeyboardStateListener implements SoftKeyboardStateHelper.SoftKeyboardStateListener {
        @Override
        public void onSoftKeyboardOpened(int keyboardHeightInPx) {
            if (edtCommen != null) {
                edtCommen.requestFocus();
                edtCommen.setFocusable(true);
                edtCommen.setCursorVisible(true);
            }
        }
        @Override
        public void onSoftKeyboardClosed() {
            if (edtCommen != null) {
                edtCommen.setCursorVisible(false);
                if(replyType==2&&TextUtils.isEmpty(edtCommen.getText())){
                 replyType=1;
                 edtCommen.setHint("评论:");
                }
            }
        }
    }

    //长按监听   弹出popWindow
    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        showPopUpWindow(view,adapter,position);
        return true;
    }
    // Fly 注：
    public  void showPopUpWindow(View itemview, final BaseQuickAdapter adapter, final int pos){
        int[] location = new int[2];
        itemview.getLocationInWindow(location);
        int px = DimenExchangeUtil.px2dip(VideDetailActivity.this, 50);
        if (popView == null) {
           popView = View.inflate(this, R.layout.layout_popup_videodetai_item, null);
           TextView tvDeleteVideodetail=(TextView)popView.findViewById(R.id.tv_delete_videodetail);
           TextView tvPopreportVideodetail= (TextView) popView.findViewById(R.id.tv_popreport_videodetail);
           //删除
            tvDeleteVideodetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doDeleteCommen(adapter,pos);
                    mpopDelerReport.dismiss();
                }
            });
            //举报
            tvPopreportVideodetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMessage("举报");
                }
            });
          }
        if (mpopDelerReport == null) {
            initPopWindow();
        }
        if (mpopDelerReport != null && mpopDelerReport.isShowing()) {
            mpopDelerReport.dismiss();
        } else {
          mpopDelerReport.showAtLocation(itemview, Gravity.CENTER_HORIZONTAL | Gravity.TOP,px, location[1]);
        }
    }
    // Fly 注：初始化popupWindow
    public void initPopWindow(){
      mpopDelerReport = new PopupWindow(popView,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT, true);//创建PopupWindow实例
      mpopDelerReport.setBackgroundDrawable(new BitmapDrawable());
      mpopDelerReport.update();
      mpopDelerReport.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
      mpopDelerReport.setTouchable(true); // 设置popupwindow可点击
      mpopDelerReport.setOutsideTouchable(true); // 设置popupwindow外部可点击
      mpopDelerReport.setFocusable(true); // 获取焦点
    }


   // 联网动作--------------------------------------------------------
      /*
       *  Fly 注：刷新监听
       */
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener= new AutoRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            isRefresh = true;
            //刷新时  也刷新展示
            showData();
            prepareCommenData();
        }
    };
    /*
     *  Fly 注：加载更多监听
     */
    private class MyRequestLoadMoreListener implements BaseQuickAdapter.RequestLoadMoreListener {
        @Override
        public void onLoadMoreRequested() {
            isLoading = true;
            prepareCommenData();
        }
    }
    /*
     *  Fly 注 ：展示
     */
     public void showData(){
         showStatus();
         showSixHeads();
     }
   // 注：  展示 获取 点赞状态  点赞总数          关注状态
    public void showStatus(){
        VideoDetailStatusShowBean  bean=new VideoDetailStatusShowBean();
        bean.setSessionid(UserUtils.getSessionId(this));
        bean.setVod_id(videoid);
        mPrecenter.doVideoDetailStatus(bean);
    }
    //Fly 注：展示6个头像
    public void showSixHeads(){
        ShowZanHeadsBean bean1=new ShowZanHeadsBean();
        bean1.setSessionid(UserUtils.getSessionId(this));
        bean1.setVod_id(videoid);
        bean1.setPage(0);
        mPrecenter.doShowAllZanHeads(bean1);
    }

    public void doActZanCancel(){  //点赞  //取消赞
        if(mVideoDetailStatusEntity.getData().getType()==1){ //表示已赞  此时掉取消赞
            ClickZanBean   clickZanBean=new ClickZanBean();
            clickZanBean.setVod_id(videoid);
            clickZanBean.setSessionid(UserUtils.getSessionId(this));
            clickZanBean.setType(2);
            clickZanBean.setTable_name(table_name);
            clickZanBean.setThumb_up_id(thumb_up_id);
            mPrecenter.doClickZanCancelVideoDetail(clickZanBean);
        } else if(mVideoDetailStatusEntity.getData().getType()==2){//表示未赞  此时调用点赞接口
            ClickZanBean   clickZanBean=new ClickZanBean();
            clickZanBean.setVod_id(videoid);
            clickZanBean.setSessionid(UserUtils.getSessionId(this));
            clickZanBean.setType(1);
            mPrecenter.doClickZanCancelVideoDetail(clickZanBean);
        }
    }
    //加关注  取消关注
    public  void doActAddCancelCare(){
        AddCancelCareBean  addCareBean=new AddCancelCareBean();
        addCareBean.setSessionid(UserUtils.getSessionId(this));
        addCareBean.setAccount_id(accountId);
    if(mVideoDetailStatusEntity.getData().isFollowStatus()){ //表示 已关注  此时调用 取消关注接口
        showMessage("调用取消关注接口");
        addCareBean.setType(2);
    }else{ //表示未关注   此时调用加关注
        showMessage("调用+关注接口");
        addCareBean.setType(1);
     }
      mPrecenter.doAddCancelCare(addCareBean);
    }

     // Fly 注：评论列表
    public void prepareCommenData(){
        GetCommenListBean bean=new GetCommenListBean();
        if (isRefresh) {
            bean.setPage(0);
        } else if(isLoading){
            bean.setPage(nextPage);
        }
        bean.setVod_id(videoid);
        mPrecenter.doGetCommenList(bean);
    }

    /*
     *  Fly 注:联网数据
     */
     ClickZanEntity  clickEntity;  //点赞取消赞 实体
     VideoDetailStatusEntity   mVideoDetailStatusEntity;//状态
    @Override
    public void setData(Object o, int id) {
        switch (id) {
         case 0:  //视频详情 获取点赞数  状态   关注状态
         mVideoDetailStatusEntity= (VideoDetailStatusEntity) o;
          if(mVideoDetailStatusEntity.getCode()==0){
          if(mVideoDetailStatusEntity.getData().getType()==1){     //点赞状态
          ivZan.setImageResource(R.mipmap.zan_s_video_detail);
          }else if(mVideoDetailStatusEntity.getData().getType()==2){ //取消状态
          ivZan.setImageResource(R.mipmap.zan_n_video_detail);
          }
         if(mVideoDetailStatusEntity.getData().isFollowStatus()){  //已关注
         ivCarezhuVideodetail.setImageResource(R.mipmap.hasguanzhu_video_detail);
         }else{//+关注
         ivCarezhuVideodetail.setImageResource(R.mipmap.addguanzhu_video_detail);
         }
         tvZanheadCount.setText(mVideoDetailStatusEntity.getData().getCountNum()+"");   //点赞总数
         table_name=mVideoDetailStatusEntity.getData().getTable_name();
         thumb_up_id=mVideoDetailStatusEntity.getData().getThumb_up_id();
         }else{
           showMessage("请求错误");
           LogUtils.e("videoDitialStatus错误",mVideoDetailStatusEntity.getCode()+"");
         }
         break;
          case 1://点赞 或 取消赞
            clickEntity= (ClickZanEntity) o;
             if(clickEntity.getCode()==0){
              if(clickEntity.getData().getResult_type()==1){
                showMessage("点赞成功");
                ivZan.setImageResource(R.mipmap.zan_s_video_detail);
                mVideoDetailStatusEntity.getData().setType(1);
                table_name=clickEntity.getData().getTable_name();
                thumb_up_id=clickEntity.getData().getThumb_up_id();
                showSixHeads();//刷新头像接口
               tvZanheadCount.setText(String.valueOf(Integer.valueOf(tvZanheadCount.getText().toString())+1));
              }else if(clickEntity.getData().getResult_type()==2){
               showMessage("取消赞成功");
               ivZan.setImageResource(R.mipmap.zan_n_video_detail);
               mVideoDetailStatusEntity.getData().setType(2);
               table_name=clickEntity.getData().getTable_name();
               thumb_up_id=clickEntity.getData().getThumb_up_id();
               showSixHeads();//刷新头像接口
              tvZanheadCount.setText(String.valueOf(Integer.valueOf(tvZanheadCount.getText().toString())-1));
              }
              }else{
               showMessage("请求错误");
               LogUtils.e("ClickZanEntity-code",clickEntity.getCode());
              }
             break;
            case 11:// 加关注   或者   取消关注
                AddCancelCareEntity mAddCancelEntity= (AddCancelCareEntity) o;
                if(mAddCancelEntity.getCode()==0){
                    if(mAddCancelEntity.getData().getResult_type()==1){// 关注成功
                        showMessage("关注成功");
                        mVideoDetailStatusEntity.getData().setFollowStatus(true);
                        ivCarezhuVideodetail.setImageResource(R.mipmap.hasguanzhu_video_detail);
                    }else if(mAddCancelEntity.getData().getResult_type()==2){
                        showMessage("取消关注成功");
                        mVideoDetailStatusEntity.getData().setFollowStatus(false);
                        ivCarezhuVideodetail.setImageResource(R.mipmap.addguanzhu_video_detail);
                    }
                }else{
                    showMessage("请求错误");
                    LogUtils.e("ClickZanEntity-code",clickEntity.getCode());
                }
                break;
            case 2:// 6个头像展示
                ShowZanHeadsEntity  entity= (ShowZanHeadsEntity) o;
               if(entity.getCode()==0){//头像显示
               if(entity.getData()==null){
                   LogUtils.e("6个头像展示失败doShowAllZanHeads","entity.getData()==null");
                   return;
               }
                if(entity.getData().getList()==null){
                    LogUtils.e("6个头像展示失败doShowAllZanHeads","entity.getData().getList()==null");
                    return;
                }
                System.out.println("------------------size>"+entity.getData().getList().size());
                headsAdapter.setNewData(entity.getData().getList());
               } else if(entity.getCode()==30702){
              LogUtils.e("头像展示无数据了","头像展示无数据了");
               }else{
              LogUtils.e("6个头像展示失败","6个头像");
               }
            break;
            case 3://发评论
                SendCommenEntity sendCommenEntity= (SendCommenEntity) o;
                if(sendCommenEntity.getCode()==0){
                    showMessage("评论成功");
                     isRefresh=true;
                    //刷新列表
                    autorefreshVideodetail.autoRefresh(mOnRefreshListener);
                }else{
                    showMessage("评论失败");
                }
                break;
            case 4://评论列表
              GetCommenListEntity getCommenListEntity = (GetCommenListEntity) o;
                if (getCommenListEntity.getCode() == 0) {
                    if (getCommenListEntity.getData() == null) {
                        LogUtils.e("", "getCommenListEntity.getData()==null");
                        hideLoading();
                        return;
                    }
                    System.out.println("mywork nextpage======>" + nextPage);
                    if (getCommenListEntity.getData().getList() == null) {
                        LogUtils.e("", "getCommenListEntity.getData().getList() == null");
                        hideLoading();
                        return;
                    }
                    nextPage = getCommenListEntity.getData().getNext_page();
                    tvCommentCount.setText("评论（"+getCommenListEntity.getData().getTotal_num()+"条）");//评论总数
                    //呈现
                    if (isRefresh) {
                        isRefresh = false;
                        if (getCommenListEntity.getData().getList().size() == 0) {
                            showMessage("暂无数据~");
                            autorefreshVideodetail.refreshComplete();
                            return;
                        }
                        if (getCommenListEntity.getData().getList().size() != 0) {
                            mVideoDetailAdapter.setNewData(getCommenListEntity.getData().getList());
                            autorefreshVideodetail.refreshComplete();
                        }
                        LogUtils.e("刷新---当前数据数目-------》",""+getCommenListEntity.getData().getList().size());
                    } else if (isLoading) {
                           isLoading = false;
                        if (getCommenListEntity.getData().getList().size() == 0) {
                            showMessage("没有数据可加载了~");
                            mVideoDetailAdapter.loadMoreEnd();
                        }
                        LogUtils.e("加载---当前行数据数目-------》",""+getCommenListEntity.getData().getList().size());
                        if (getCommenListEntity.getData().getList().size() != 0) {
                            mVideoDetailAdapter.addData(getCommenListEntity.getData().getList());
                            mVideoDetailAdapter.loadMoreComplete();
                        }
                    }
                } else if (getCommenListEntity.getCode() ==30509) {
                    LogUtils.e("", "无数据le");
                    autorefreshVideodetail.refreshComplete();
                    mVideoDetailAdapter.loadMoreEnd();
                    return;
                } else if (getCommenListEntity.getCode() == 104) {
                    DialogUtils.reLoginAct(this, this, "您的账号已在另一台设备上登陆，若非本人操作则账号已泄露，请立即找回密码!", false);
                    hideLoading();
                } else {
                    this.showMessage("请求失败");
                    hideLoading();
                    LogUtils.e("", "请求失败" + getCommenListEntity.getCode());
                }
                break;
            case 5://回复
                ReplyEntity  replyEntity= (ReplyEntity) o;
                if(replyEntity.getCode()==0){
                  showMessage("回复成功");
                  //刷新列表
                    autorefreshVideodetail.autoRefresh(mOnRefreshListener);
                }else{
                 showMessage("回复失败");
                }
                break;
            case 6://删除
                DeleteCommenEntity deleteCommenEntity=new DeleteCommenEntity();
                if(deleteCommenEntity.getCode()==0){
                    showMessage("删除成功");
                    //刷新列表
                    autorefreshVideodetail.autoRefresh(mOnRefreshListener);
                }else{
                    showMessage("删除失败");
                }
                break;
        }
    }
    @Override
    public void hideLoading() {
        super.hideLoading();
        if (isRefresh) {
            isRefresh = false;
            autorefreshVideodetail.refreshComplete();
        }else if (isLoading) {
            isLoading = false;
            mVideoDetailAdapter.loadMoreFail();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        playerVideoPlayer.releaseAllVideos();
    }
    @Override
    public void onBackPressed() {
      if (playerVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }






  //Fly 注： 标题栏  渐变 部分----------------------------------
    //标题的高度
    private int titleHeight;
    //封面的高度
    private int thumbHeight;
    //需要滑动的高度
    private int needScrollHeight;
    private class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        @Override
        public void onGlobalLayout() {
      frameTopbarVideodetail.getViewTreeObserver().removeGlobalOnLayoutListener(this);
      playerVideoPlayer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
      titleHeight = frameTopbarVideodetail.getHeight();
      thumbHeight = playerVideoPlayer.getHeight();
      needScrollHeight = thumbHeight - titleHeight;
      LogUtils.e("***","toolbarheight====>"+titleHeight+"thumbHeight 封面高=》"+thumbHeight+"  需要上滑动的高"+needScrollHeight);
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
            LogUtils.e("+++recycler滑动的距离》",scrollDistance);
            if (needScrollHeight > 0 && scrollDistance > needScrollHeight) {
                frameTopbarVideodetail.setBackgroundResource(R.color.white);
                viewDividerVideodetail.setBackgroundColor(UiUtils.getColor(R.color.black_4));
            } else {
                if (needScrollHeight <= 0) {
                    frameTopbarVideodetail.setBackgroundResource(R.color.transparent);
                    viewDividerVideodetail.setBackgroundResource(R.color.transparent);
                    LogUtils.e("needScrollHeight <= 0","needScrollHeight <= 0");
                    return;
                }
        frameTopbarVideodetail.setBackgroundDrawable(UiUtils.getDrawable(R.color.transparent));
        viewDividerVideodetail.setBackgroundDrawable(UiUtils.getDrawable(R.color.transparent));
        float percent = scrollDistance * 1.f / needScrollHeight;
        Log.d("scroll-->", "onScroll: percent: " + percent);
        int evaluateArgb = evaluateArgb(percent, Color.TRANSPARENT, Color.WHITE);
        int evaluateArgb2 = evaluateArgb(percent, Color.TRANSPARENT, UiUtils.getColor(R.color.black_change4));
        frameTopbarVideodetail.getBackground().mutate().setColorFilter(evaluateArgb, PorterDuff.Mode.SRC_OVER);
        viewDividerVideodetail.getBackground().mutate().setColorFilter(evaluateArgb2, PorterDuff.Mode.SRC_OVER);
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
