package com.shortmeet.www.ui.Home.fragment;


import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shortmeet.www.Base.BaseApplication;
import com.shortmeet.www.Base.BaseLazyFragment;
import com.shortmeet.www.R;
import com.shortmeet.www.bean.home.GetRecomandVideoBean;
import com.shortmeet.www.entity.home.KindRecomanVideoEntity;
import com.shortmeet.www.entity.home.RecomandEntity;
import com.shortmeet.www.event.UpVideoEvent;
import com.shortmeet.www.mvp.IMVPView;
import com.shortmeet.www.mvp.IMVPrecenter;
import com.shortmeet.www.ui.Home.adapter.RecomandVideoAdapter;
import com.shortmeet.www.ui.PercenalCenter.activity.DialogExistActivity;
import com.shortmeet.www.ui.PercenalCenter.activity.OtherPeopleInfoActivity;
import com.shortmeet.www.ui.Video.activity.VideDetailActivity;
import com.shortmeet.www.utilsUsed.DialogUtils;
import com.shortmeet.www.utilsUsed.LogUtils;
import com.shortmeet.www.utilsUsed.NetUtil;
import com.shortmeet.www.utilsUsed.RxBus;
import com.shortmeet.www.utilsUsed.UserUtils;
import com.shortmeet.www.views.refreshPart.AutoSwipeRefresh;
import com.shortmeet.www.views.widgetPart.GoodManVideoPlayerPriview;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JZMediaManager;
import cn.jzvd.JZVideoPlayer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

import static android.R.attr.value;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecomandFragment extends BaseLazyFragment implements IMVPView, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {
   /*
    *  Fly 注：上传Title进度部分
    */
    private FrameLayout frameUploading;
    private ProgressBar pbUploadProgress;
    private TextView tvTextupload;

    private RelativeLayout relavUploadfinish;
    private ImageView imgvFmUpload;

    /*
     *  Fly 注： recycler
     */
    public RecyclerView recylerRecomand;

    /*
     *  Fly 注： 适配器
     */
    public RecomandVideoAdapter mRecomandVideoAdapter;
    /*
     *  Fenglingyue 注：数据是空  显示图片
     */
    private RelativeLayout relativeLayoutNoDatas;
    private TextView tvNodatasContent;
    private ImageView imgvNoData;
    /*
     *  Fly 注：刷新相关
     */
    private AutoSwipeRefresh autorefreshFragmentrecomand;
    /*
     *  Fly 注：是否 是刷新   是否是加载更多
     */
    private boolean isRefresh = true;
    private boolean isLoading;
    private int nextPage;
    /*
     *  Fly 注：数据整合
     */
    List<KindRecomanVideoEntity> datas = new ArrayList<>();
    KindRecomanVideoEntity kindVideoEntity = null;
    // Fly 注：precenter
    private IMVPrecenter mPrecenter;

    public static RecomandFragment newInstance() {
        Bundle args = new Bundle();
        RecomandFragment recomdfrg = new RecomandFragment();
        recomdfrg.setArguments(args);
        return recomdfrg;
    }

    @Override
    public void initView() {
        autorefreshFragmentrecomand = (AutoSwipeRefresh) rootView.findViewById(R.id.autorefresh_fragmentrecomand);
        relativeLayoutNoDatas = (RelativeLayout) rootView.findViewById(R.id.relative_layout_no_datas);
        tvNodatasContent = (TextView) rootView.findViewById(R.id.tv_nodatas_content);
        imgvNoData = (ImageView) rootView.findViewById(R.id.imgv_nodata);
        relativeLayoutNoDatas.setVisibility(View.GONE);
        tvNodatasContent.setText("还没有数据哦~");
        //imgvNoData.setImageResource();
        initUpLoadTitle();
        registObserver();
        initRecycleView();
        mPrecenter = new IMVPrecenter(this, mContext);
     }
     public void initUpLoadTitle(){
        frameUploading = (FrameLayout)rootView.findViewById(R.id.frame_uploading);
        pbUploadProgress = (ProgressBar)rootView.findViewById(R.id.pb_upload_progress);
        tvTextupload = (TextView)rootView.findViewById(R.id.tv_textupload);
        relavUploadfinish = (RelativeLayout)rootView.findViewById(R.id.relav_uploadfinish);
        imgvFmUpload = (ImageView)rootView.findViewById(R.id.imgv_fm_upload);
    }
    //订阅观察者
    public void registObserver(){
      RxBus.getDefault().toFlowable(UpVideoEvent.class)
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(new Consumer<UpVideoEvent>() {
                 @Override
                 public void accept(@NonNull UpVideoEvent upVideoEvent) throws Exception {
                 if(upVideoEvent.getCode()==0){    //上传中
                 if(frameUploading.getVisibility()==View.GONE){
                   frameUploading.setVisibility(View.VISIBLE);
                   relavUploadfinish.setVisibility(View.GONE);
                   }
                   pbUploadProgress.setMax((int) upVideoEvent.getTotalSize());
                   pbUploadProgress.setProgress((int) upVideoEvent.getCurrentSize());
                 }else if(upVideoEvent.getCode()==1){
                   relavUploadfinish.setVisibility(View.VISIBLE);
                   frameUploading.setVisibility(View.GONE);
                   showMessage("上传成功");
                   new Handler().postDelayed(new Runnable() {
                  @Override
                  public void run() {
                   relavUploadfinish.setVisibility(View.GONE);
                  }
                 },2000);
                 }else{
                     showMessage("上传失败");
                     frameUploading.setVisibility(View.GONE);
                 }
                 }
                  }, new Consumer<Throwable>() {
                     @Override
                     public void accept(@NonNull Throwable throwable) throws Exception {
                        LogUtils.e("registObserver上传接收进度异常-throwable",""+throwable);
                     }
                 });
    }
    public void initRecycleView() {
        recylerRecomand = (RecyclerView) rootView.findViewById(R.id.swipe_target);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recylerRecomand.setLayoutManager(linearLayoutManager);
        mRecomandVideoAdapter = new RecomandVideoAdapter();
        recylerRecomand.setAdapter(mRecomandVideoAdapter);
        initRecycleViewListenter();
    }
    @Override
    public void initListener() {
        autorefreshFragmentrecomand.setOnRefreshListener(new MyOnRefreshListener());
        mRecomandVideoAdapter.setOnLoadMoreListener(new MyRequestLoadMoreListener(), recylerRecomand);
        mRecomandVideoAdapter.disableLoadMoreIfNotFullPage();
        mRecomandVideoAdapter.setOnItemChildClickListener(this);
    }
    @Override
    public int getMyLazyRootView() {
        return R.layout.fragment_recomand;
    }
    @Override
    public void lazyLoad() {
        autorefreshFragmentrecomand.autoRefresh();
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

    //刷新 和加载更多 的数据接口
    public void prepareListData() {
        GetRecomandVideoBean bean = new GetRecomandVideoBean();
        if (isRefresh) {
            bean.setPage(0);
        } else {
            bean.setPage(nextPage);
        }
        bean.setSessionid(UserUtils.getSessionId(mContext));
        mPrecenter.doGetRecomandInter(bean);
    }

    @Override
    public void setData(Object o, int id) {
        switch (id) {
            case 0:
                RecomandEntity recomandentity = (RecomandEntity) o;
                if (recomandentity.getCode() == 0) {
                    if (recomandentity.getData() == null) {
                        LogUtils.e("", "recomandentity.getData()==null");
                        hideLoading();
                        return;
                    }
                    nextPage = recomandentity.getData().getNext_page();
                    System.out.println("nextpage======>" + nextPage);
                    if (recomandentity.getData().getVideo() == null) {
                        LogUtils.e("", "recomandentity.getData().getVideo()==null");
                        hideLoading();
                        return;
                    }
                    if (recomandentity.getData().getVideo().size() == 0) {
                        LogUtils.e("", "recomandentity.getData().getVideo()==0");
                        return;
                    }
                    System.out.println(recomandentity.getData().getVideo().size() + "*****************************");
                    // Fly 注：整合数据
                    List<KindRecomanVideoEntity> resoloveDadas = getResoloveDadas(recomandentity.getData().getVideo());
                    //呈现
                    if (isRefresh) {
                        isRefresh = false;
                        if (resoloveDadas.size() == 0) {
                            showMessage("没有数据~");
                            autorefreshFragmentrecomand.refreshComplete();
                            return;
                        }
                        if (resoloveDadas.size() != 0) {
                            hideNetErrorPictrue();
                            mRecomandVideoAdapter.setNewData(resoloveDadas);
                            autorefreshFragmentrecomand.refreshComplete();
                        }
                    } else if (isLoading) {
                        isLoading = false;
                        if (resoloveDadas.size() == 0) {
                            showMessage("没有数据可加载了~");
                            mRecomandVideoAdapter.loadMoreEnd();
                        }
                        if (resoloveDadas.size() != 0) {
                            hideNetErrorPictrue();
                            mRecomandVideoAdapter.setNewData(resoloveDadas);
                            mRecomandVideoAdapter.loadMoreComplete();
                        }
                    }
                } else if (recomandentity.getCode() == 20102) {
                    LogUtils.e("", "无数据le");
                    // isLoading = false;
                    mRecomandVideoAdapter.loadMoreEnd();
                    return;
                } else if (recomandentity.getCode() == 103) {
                    hideLoading();
                 startActivity(new Intent(mContext, DialogExistActivity.class));
                 DialogUtils.reLoginAct(this, mContext, "您的账号已在另一台设备上登陆，若非本人操作则账号已泄露，请立即找回密码!", false);
                } else {
                    hideLoading();
                    this.showMessage("请求失败");
                    LogUtils.e("", "请求失败" + recomandentity.getCode());
                }
                break;
            case value:

                break;
            default:
                break;
        }

    }

    /*
     *  Fly 注：无论刷新 还是  加载更多  接到数据 第一时间 整合数据
     */
    public List<KindRecomanVideoEntity> getResoloveDadas(List<RecomandEntity.DataEntity.VideoEntity> VideoEntities) {
        if (isRefresh && datas.size() != 0) {
            datas.clear();
        }
        for (RecomandEntity.DataEntity.VideoEntity videoEntity : VideoEntities) {
            if (videoEntity.getItem_type() == 1) {//橫向 1个
                KindRecomanVideoEntity kindRecomanVideoEntity = new KindRecomanVideoEntity();
                kindRecomanVideoEntity.setType(1);
                kindRecomanVideoEntity.addEntity(videoEntity);
                datas.add(kindRecomanVideoEntity);
            } else if (videoEntity.getItem_type() == 2) {  //横向2个
                if (kindVideoEntity == null) {
                    kindVideoEntity = new KindRecomanVideoEntity();
                    kindVideoEntity.setType(2);
                    kindVideoEntity.addEntity(videoEntity);
                    datas.add(kindVideoEntity);
                } else if (kindVideoEntity.isFull()) {
                    kindVideoEntity = new KindRecomanVideoEntity();
                    kindVideoEntity.setType(2);
                    kindVideoEntity.addEntity(videoEntity);
                    datas.add(kindVideoEntity);
                } else if (!kindVideoEntity.isFull()) {
                    kindVideoEntity.setType(2);
                    kindVideoEntity.addEntity(videoEntity);
                }
            }
        }
        System.out.println("getResoloveDadas data size---------------------- " + datas.size());
        for (KindRecomanVideoEntity en : datas) {
            System.out.println("类型" + en.getType() + "里面集合大小" + en.getRecomanVideoEntities().size() + "/r/n");
            if (en.getRecomanVideoEntities().size() == 1) {
            } else if (en.getRecomanVideoEntities().size() == 2) {
            }
        }
        return datas;
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        if (isRefresh) {
            isRefresh = false;
            autorefreshFragmentrecomand.setRefreshing(false);
            showNetErrorPicture();
        } else if (isLoading) {
            isLoading = false;
            mRecomandVideoAdapter.loadMoreFail();
        } else {
            showNetErrorPicture();
        }
    }

    public void hideNetErrorPictrue() {  //  错误图片隐藏的方法
        relativeLayoutNoDatas.setVisibility(View.GONE);
        recylerRecomand.setVisibility(View.VISIBLE);
    }

    public void showNetErrorPicture() {  //错误图片显示
        relativeLayoutNoDatas.setVisibility(View.VISIBLE);
        recylerRecomand.setVisibility(View.GONE);
    }

    /*
     *  Fly 注： 条目监听
     */
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) { }

    @Override
    public void onPause() {
        super.onPause();
        if(BaseApplication.getInstance().VideoPlaying!=null){
            LogUtils.e("recomandFrag","recomandFrag-onpause  VideoPlaying!=null");
            if(BaseApplication.getInstance().VideoPlaying.currentState== JZVideoPlayer.CURRENT_STATE_PLAYING){
            LogUtils.e("recomandFrag","recomandFrag-onpause  VideoPlaying.currentState== JZVideoPlayer.CURRENT_STATE_PLAYING");
                JZMediaManager.instance().mediaPlayer.pause();
            }
        }
    }

    /*
     *  Fly 注：条目内控件监听
     */
    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        KindRecomanVideoEntity kindRecomanVideoEntity = (KindRecomanVideoEntity) adapter.getData().get(position);
        switch (view.getId()) {
            case R.id.imgv_video_item_recommend_video:
                Intent intenthorizantal = new Intent(mContext, VideDetailActivity.class);
                intenthorizantal.putExtra("playurl",kindRecomanVideoEntity.getRecomanVideoEntities().get(0).getPlay_url());
                intenthorizantal.putExtra("coverurl", kindRecomanVideoEntity.getRecomanVideoEntities().get(0).getCover_url());
                intenthorizantal.putExtra("video_id", kindRecomanVideoEntity.getRecomanVideoEntities().get(0).getId());

                intenthorizantal.putExtra("name",kindRecomanVideoEntity.getRecomanVideoEntities().get(0).getNickname());
                intenthorizantal.putExtra("area",kindRecomanVideoEntity.getRecomanVideoEntities().get(0).getArea());
                intenthorizantal.putExtra("time",kindRecomanVideoEntity.getRecomanVideoEntities().get(0).getCreate_time());
                intenthorizantal.putExtra("title",kindRecomanVideoEntity.getRecomanVideoEntities().get(0).getTitle());
                intenthorizantal.putExtra("account_id",kindRecomanVideoEntity.getRecomanVideoEntities().get(0).getAccount_id());
                startActivity(intenthorizantal);
                break;
            case R.id.imgv1_video_item_recommend_video:
                Intent vertical1 = new Intent(mContext, VideDetailActivity.class);
                vertical1.putExtra("playurl", kindRecomanVideoEntity.getRecomanVideoEntities().get(0).getPlay_url());
                vertical1.putExtra("coverurl", kindRecomanVideoEntity.getRecomanVideoEntities().get(0).getCover_url());
                vertical1.putExtra("video_id", kindRecomanVideoEntity.getRecomanVideoEntities().get(0).getId());

                vertical1.putExtra("name",kindRecomanVideoEntity.getRecomanVideoEntities().get(0).getNickname());
                vertical1.putExtra("area",kindRecomanVideoEntity.getRecomanVideoEntities().get(0).getArea());
                vertical1.putExtra("time",kindRecomanVideoEntity.getRecomanVideoEntities().get(0).getCreate_time());
                vertical1.putExtra("title",kindRecomanVideoEntity.getRecomanVideoEntities().get(0).getTitle());
                vertical1.putExtra("account_id",kindRecomanVideoEntity.getRecomanVideoEntities().get(0).getAccount_id());
                startActivity(vertical1);
                break;
            case R.id.imgv2_video_item_recommend_video:
                Intent vertical2 = new Intent(mContext, VideDetailActivity.class);
                vertical2.putExtra("playurl", kindRecomanVideoEntity.getRecomanVideoEntities().get(1).getPlay_url());
                vertical2.putExtra("coverurl", kindRecomanVideoEntity.getRecomanVideoEntities().get(1).getCover_url());
                vertical2.putExtra("video_id", kindRecomanVideoEntity.getRecomanVideoEntities().get(1).getId());
                vertical2.putExtra("name",kindRecomanVideoEntity.getRecomanVideoEntities().get(1).getNickname());
                vertical2.putExtra("area",kindRecomanVideoEntity.getRecomanVideoEntities().get(1).getArea());
                vertical2.putExtra("time",kindRecomanVideoEntity.getRecomanVideoEntities().get(1).getCreate_time());
                vertical2.putExtra("title",kindRecomanVideoEntity.getRecomanVideoEntities().get(1).getTitle());
                vertical2.putExtra("account_id",kindRecomanVideoEntity.getRecomanVideoEntities().get(1).getAccount_id());
                startActivity(vertical2);
                break;
            case R.id.linear_head_recomandfrg: //横向视频  头像监听
               startActivity(new Intent(mContext, OtherPeopleInfoActivity.class));
                break;
            case  R.id.linear_head1_recomandfrag://竖向视频  头像1监听
                startActivity(new Intent(mContext, OtherPeopleInfoActivity.class));
                break;
            case R.id.linear_head2_recomandfrag://竖向视频  头像2监听
                startActivity(new Intent(mContext, OtherPeopleInfoActivity.class));
                break;
        }
    }


    /*
     *  Fly 注：recyclerView 条目滚动监听
     */
    public void initRecycleViewListenter() {
        recylerRecomand.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
               autoPlayVideo(recyclerView);
            }
        });
    }

    /*
     *  Fly 注：控制视频自动播放
     */
    public int firstVisible = 0, visibleCount = 0, totalCount = 0, lastVisiblePostion=0;
    public void autoPlayVideo(RecyclerView recyclerView) {
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        firstVisible=manager.findFirstVisibleItemPosition();
        lastVisiblePostion=manager.findLastVisibleItemPosition();
        visibleCount=lastVisiblePostion-firstVisible+1;
        totalCount=manager.getItemCount();
        Log.e("videoTest------>", "firstVisiblePos = " + firstVisible + "    visibleItemCount = " + visibleCount);
        for (int i = 0; i < visibleCount; i++) {
            if (recyclerView!=null&&recyclerView.getChildAt(i)!=null) {
                if("CardView".equals(recyclerView.getChildAt(i).getClass().getSimpleName())){
                    CardView cardView = (CardView) recyclerView.getChildAt(i);
                    GoodManVideoPlayerPriview playerPriview1= (GoodManVideoPlayerPriview) cardView.findViewById(R.id.videoplayer_item_recommend_video);
                   // Rect rect = new Rect();
                    Rect rect = new Rect(0, playerPriview1.getTop(),playerPriview1.getWidth(),playerPriview1.getHeight());
                    playerPriview1.getLocalVisibleRect(rect);
                    int videoheight3 = playerPriview1.getHeight();
                    Log.e("videoTest>>>>>>>>>>>>>","i="+i+"==="+"videoheight3:"+videoheight3+"==="+"rect.top:"+rect.top+"==="+"rect.bottom:"+rect.bottom);
                    if (rect.top<=videoheight3/2&&rect.bottom>=videoheight3/2&&rect.bottom<=videoheight3){
                        if (playerPriview1.currentState == JZVideoPlayer.CURRENT_STATE_NORMAL || playerPriview1.currentState == JZVideoPlayer.CURRENT_STATE_ERROR) {
                            Log.e("videoTest", playerPriview1.currentState + "======================performClick======================");
                            if(NetUtil.isWifi(mContext)){
                                playerPriview1.startButton.performClick();
                                BaseApplication.getInstance().VideoPlaying=playerPriview1;
                            }
                        }
                        return;
                    }
                }
            }
        }
        Log.e("videoTest", "======================releaseAllVideos=====================");
        JZVideoPlayer.releaseAllVideos();
        BaseApplication.getInstance().VideoPlaying=null;
    }
}





