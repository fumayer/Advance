package com.shortmeet.www;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aliyun.common.utils.StorageUtils;
import com.aliyun.struct.common.ScaleMode;
import com.aliyun.struct.common.VideoQuality;
import com.aliyun.struct.recorder.CameraType;
import com.aliyun.struct.recorder.FlashType;
import com.aliyun.struct.snap.AliyunSnapVideoParam;
import com.shortmeet.www.Base.BaseActivity;
import com.shortmeet.www.Base.BaseApplication;
import com.shortmeet.www.mvp.IMVPView;
import com.shortmeet.www.ui.Attention.fragment.AttentionFragment;
import com.shortmeet.www.ui.Group.fragment.GroupFragment;
import com.shortmeet.www.ui.Home.fragment.HomeFragment;
import com.shortmeet.www.ui.PercenalCenter.activity.LoginActivity;
import com.shortmeet.www.ui.PercenalCenter.fragment.MineFragment;
import com.shortmeet.www.utilsUsed.StatusBarUtil;
import com.shortmeet.www.utilsUsed.UserUtils;
import com.shortmeet.www.zTakePai.record.AliyunVideoRecorder;
import com.shortmeet.www.zTakePai.record.util.Common;

import java.io.File;

import cn.jzvd.JZVideoPlayer;
import rx.Subscription;

/*
 *  Fenglingyue 注：  此框架  控制标题栏的显隐
 */
public class MainActivity extends BaseActivity implements IMVPView, View.OnClickListener {
    /*
     *  Fly 注： 代替 状态栏的view
     */
    private View topStusbar;
    /*
     *  Fly 注：装 fragment 的容器
     */
    private FrameLayout mainFagcontent;
    /*
    *  Fly 注： 底部tab
    */
    private LinearLayout linearHomeFrag;
    private ImageView imgvHomeFragImage;
    private LinearLayout linearDiscoverFrag;
    private ImageView imgvDiscoverFrag;
    private ImageView imgvTakepai;
    private LinearLayout linearGroupFrag;
    private ImageView imgvGroupFragImage;
    private LinearLayout linearMineFrag;
    private ImageView imgvMineFragImage;

    /*
     *  Fly 注： 底部tab 文字默认颜色和选中颜色
     */
    public static final int BOOTM_TAB_NORMAL_COLOR= Color.WHITE;
    public static final int BOOTM_TAB_SELECTED_COLOR=Color.GREEN;
    /*
     *  Fly 注：TAG和fragment
     */
    private static final String HOME_TAG = "HomeFragment";
    private HomeFragment mHomeFragment;

    private static final String DISCOVER_TAG = "AttentionFragment";
    private AttentionFragment mAttentionFragment;

    private static final String GROUP_TAG = "GroupFragment";
    private GroupFragment mGroupFragment;

    private static final String MINE_TAG = "MineFragment";
    private MineFragment mMineFragment;


    private long exitTime; // Fly 注： back 时间（实现双击退出）
    String[] eff_dirs;////滤镜路径
    //rxbus
    private Subscription subscribe;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) { //当内存不足Activity销毁的时候可能造成Fragment重叠的现象
            mHomeFragment = (HomeFragment) mFragmentManager.findFragmentByTag(HOME_TAG);
            mGroupFragment = (GroupFragment) mFragmentManager.findFragmentByTag(GROUP_TAG);
            mAttentionFragment = (AttentionFragment) mFragmentManager.findFragmentByTag(DISCOVER_TAG);
            mMineFragment = (MineFragment) mFragmentManager.findFragmentByTag(MINE_TAG);
        }
        imgvTakepai.setEnabled(false);// Fly 注：阿里拍摄相关
        initAssetPath();
        copyAssets();
        setSelectedTab(0); //设置选中的
    }
    @Override
    public int setRootView() {
        return R.layout.activity_main;
    }
    @Override
    public void initView() {
         StatusBarUtil.translucentStatusBar(this, false);//设置状态栏透明 内容延伸到状态栏
         //StatusBarUtil.setColor(this, Color.YELLOW);  //设置状态栏颜色  原理：先设置透明 生成一个与状态栏等大的view
        //设置 去掉状态栏-- 全屏
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        topStusbar = (View) findViewById(R.id.top_stusbar);
        mainFagcontent = (FrameLayout) findViewById(R.id.main_fagcontent);
        linearHomeFrag = (LinearLayout) findViewById(R.id.linear_home_frag);
        linearDiscoverFrag = (LinearLayout) findViewById(R.id.linear_discover_frag);
        linearGroupFrag = (LinearLayout) findViewById(R.id.linear_group_frag);
        linearMineFrag = (LinearLayout) findViewById(R.id.linear_mine_frag);

        imgvHomeFragImage = (ImageView) findViewById(R.id.imgv_home_frag_image);
        imgvDiscoverFrag = (ImageView) findViewById(R.id.imgv_discover_frag);
        imgvGroupFragImage = (ImageView) findViewById(R.id.imgv_group_frag_image);
        imgvMineFragImage = (ImageView) findViewById(R.id.imgv_mine_frag_image);
        imgvTakepai = (ImageView) findViewById(R.id.imgv_takepai);
        // Fly 注： 使状态栏透明  写一个和状态栏登高的view 设置颜色充当状态栏 会自动上去
       //  initStusBar();
    }
    public  void  initStusBar(){
        int stusBarHeight= StatusBarUtil.getStatusBarHeight(this);
        LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) topStusbar.getLayoutParams();
        layoutParams.height=stusBarHeight;
        topStusbar.setBackgroundColor(Color.GREEN);
    }
    // Fly 注： 隐藏显示 stusBar
    public void  showStusBar(){ topStusbar.setVisibility(View.VISIBLE);}
    public void  hideStusBar(){topStusbar.setVisibility(View.GONE);}

    @Override
    public void initData() { }
    public void setSelectedTab(int index){
        FragmentTransaction fragmentTransaction=mFragmentManager.beginTransaction();
        //还原所有tab 为最初状态
        clearAllTab();
        //隐藏 所有页面
        hideAllFragment(fragmentTransaction);
        switch (index) {
            case 0:
                showStusBar();
                imgvHomeFragImage.setImageResource(R.mipmap.home_s);
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    fragmentTransaction.add(R.id.main_fagcontent, mHomeFragment,HOME_TAG);
                } else {
                    fragmentTransaction.show(mHomeFragment);
                }
                break;
            case 1:
                showStusBar();
                   imgvDiscoverFrag.setImageResource(R.mipmap.faxian_s);
                if (mAttentionFragment == null) {
                    mAttentionFragment = new AttentionFragment();
                    fragmentTransaction.add(R.id.main_fagcontent, mAttentionFragment,DISCOVER_TAG);//DISCOVER_TAG
                } else {
                    fragmentTransaction.show(mAttentionFragment);
                }
                break;
            case 2:
                showStusBar();
                imgvGroupFragImage.setImageResource(R.mipmap.xiaoxi_s);
                if (mGroupFragment == null) {
                    mGroupFragment = new GroupFragment();
                    fragmentTransaction.add(R.id.main_fagcontent, mGroupFragment,GROUP_TAG); //GROUP_TAG
                } else {
                    fragmentTransaction.show(mGroupFragment);
                }
                break;
            case 3:
                if(UserUtils.getUserIdintify(this)==0){    //游客身份  登录界面
                    startActivity(new Intent(this, LoginActivity.class));
                    this.finish();
                   return;
                }
                showStusBar();
                imgvMineFragImage.setImageResource(R.mipmap.mine_s);
                if (mMineFragment == null) {
                    mMineFragment = new MineFragment();
                    fragmentTransaction.add(R.id.main_fagcontent, mMineFragment,MINE_TAG);
                } else {
                    fragmentTransaction.show(mMineFragment);
                }
                break;
        }
              fragmentTransaction.commit();
    }
    public void clearAllTab(){
        imgvHomeFragImage.setImageResource(R.mipmap.home_n);
        imgvDiscoverFrag.setImageResource(R.mipmap.faxian_n);
        imgvGroupFragImage.setImageResource(R.mipmap.xiaoxi_n);
        imgvMineFragImage.setImageResource(R.mipmap.mine_n);
    }
    public void hideAllFragment(FragmentTransaction transaction){
        if(mHomeFragment!=null){
            transaction.hide(mHomeFragment);
        }
        if(mGroupFragment!=null){
            transaction.hide(mGroupFragment);
        }
        if(mAttentionFragment !=null){
            transaction.hide(mAttentionFragment);
        }
        if(mMineFragment!=null){
            transaction.hide(mMineFragment);
        }
    }
    @Override
    public void initListener() {
        linearHomeFrag.setOnClickListener(this);
        linearDiscoverFrag.setOnClickListener(this);
        imgvTakepai.setOnClickListener(this);
        linearGroupFrag.setOnClickListener(this);
        linearMineFrag.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_home_frag:
                setSelectedTab(0);
                break;
            case R.id.linear_discover_frag:
                setSelectedTab(1);
                break;
            case R.id.linear_group_frag:
                setSelectedTab(2);
                break;
            case R.id.linear_mine_frag:
                setSelectedTab(3);
                break;
            case R.id.imgv_takepai:
                if(UserUtils.getUserIdintify(this)==0){    //游客身份  登录界面
                    startActivity(new Intent(this, LoginActivity.class));
                    this.finish();
                    return;
                }
                doTakePai();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis()-exitTime>1500){
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime=System.currentTimeMillis();
        }else{
            killSelf();
        }
    }

    @Override
    public void setData(Object o, int id) {

    }
     /*
      *  Fly 注：拍摄相关
      */
     private void initAssetPath(){
         String path = StorageUtils.getCacheDirectory(this).getAbsolutePath() + File.separator+ Common.QU_NAME + File.separator;
         eff_dirs = new String[]{
                 null,
                 path + "filter/chihuang",
                 path + "filter/fentao",
                 path + "filter/hailan",
                 path + "filter/hongrun",
                 path + "filter/huibai",
                 path + "filter/jingdian",
                 path + "filter/maicha",
                 path + "filter/nonglie",
                 path + "filter/rourou",
                 path + "filter/shanyao",
                 path + "filter/xianguo",
                 path + "filter/xueli",
                 path + "filter/yangguang",
                 path + "filter/youya",
                 path + "filter/zhaoyang"
         };
     }
    private void copyAssets() {
        new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] params) {
                Common.copyAll(MainActivity.this);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                imgvTakepai.setEnabled(true);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    //吊起拍摄
    public void doTakePai(){
        // Fly 注：****************设置录制分辨 480 默认   ，设置比例 3：4 录制视图
        int min = 2000;
        int max = 30000;
        int gop = 5;
        AliyunSnapVideoParam recordParam = new AliyunSnapVideoParam.Builder()
                .setResulutionMode(AliyunSnapVideoParam.RESOLUTION_480P)  //分辨率
                .setRatioMode(AliyunSnapVideoParam.RATIO_MODE_3_4)// 比例
                .setRecordMode(AliyunSnapVideoParam.RECORD_MODE_AUTO)
                .setFilterList(eff_dirs)
                .setBeautyLevel(80)
                .setBeautyStatus(true)
                .setCameraType(CameraType.FRONT)
                .setFlashType(FlashType.ON)
                .setNeedClip(true)
                .setMaxDuration(max)
                .setMinDuration(min)
                .setVideQuality(VideoQuality.HD)//视频质量
                .setGop(gop)

                /**
                 * 裁剪参数
                 */
                .setMinVideoDuration(4000)
                .setMaxVideoDuration(29 * 1000)
                .setMinCropDuration(3000)
                .setFrameRate(25)
                .setCropMode(ScaleMode.PS)
                .build();
       AliyunVideoRecorder.startRecord(this,recordParam);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if(BaseApplication.getInstance().VideoPlaying!=null){
        if(BaseApplication.getInstance().VideoPlaying.currentState== JZVideoPlayer.CURRENT_STATE_PLAYING){
            BaseApplication.getInstance().VideoPlaying.startButton.performClick();
            }else if (BaseApplication.getInstance().VideoPlaying.currentState== JZVideoPlayer.CURRENT_STATE_PREPARING){
             JZVideoPlayer.releaseAllVideos();
            }
        }
    }
}
