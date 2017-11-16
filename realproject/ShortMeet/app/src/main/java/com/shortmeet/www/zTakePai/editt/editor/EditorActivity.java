/*
 * Copyright (C) 2010-2017 Alibaba Group Holding Limited.
 */

package com.shortmeet.www.zTakePai.editt.editor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.common.utils.FileUtils;
import com.aliyun.common.utils.StorageUtils;
import com.aliyun.common.utils.ToastUtil;
import com.aliyun.querrorcode.AliyunErrorCode;
import com.aliyun.qupai.editor.AliyunICanvasController;
import com.aliyun.qupai.editor.AliyunIEditor;
import com.aliyun.qupai.editor.AliyunIExporter;
import com.aliyun.qupai.editor.AliyunIPlayer;
import com.aliyun.qupai.editor.AliyunIThumbnailFetcher;
import com.aliyun.qupai.editor.AliyunPasterManager;
import com.aliyun.qupai.editor.AliyunThumbnailFetcherFactory;
import com.aliyun.qupai.editor.OnComposeCallback;
import com.aliyun.qupai.editor.OnPlayCallback;
import com.aliyun.qupai.editor.OnPreparedListener;
import com.aliyun.qupai.editor.impl.AliyunEditorFactory;
import com.aliyun.qupai.import_core.AliyunIImport;
import com.aliyun.qupai.import_core.AliyunImportCreator;
import com.aliyun.struct.common.AliyunDisplayMode;
import com.aliyun.struct.common.AliyunVideoParam;
import com.aliyun.struct.common.CropKey;
import com.aliyun.struct.common.ScaleMode;
import com.aliyun.struct.common.VideoDisplayMode;
import com.aliyun.struct.common.VideoQuality;
import com.aliyun.struct.effect.EffectBean;
import com.aliyun.struct.effect.EffectPaster;
import com.aliyun.struct.snap.AliyunSnapVideoParam;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.slidebar.ScrollBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;
import com.shortmeet.www.Api.ApiConstant;
import com.shortmeet.www.Listener.DialogCallBack;
import com.shortmeet.www.R;
import com.shortmeet.www.ui.TakePai.MyUploadActivity;
import com.shortmeet.www.utilsUsed.DialogUtils;
import com.shortmeet.www.utilsUsed.FileUtil;
import com.shortmeet.www.utilsUsed.LogUtils;
import com.shortmeet.www.utilsUsed.SystemUtils;
import com.shortmeet.www.utilsUsed.UiUtils;
import com.shortmeet.www.zTakePai.crop.AliyunVideoCrop;
import com.shortmeet.www.zTakePai.editt.editor.adapter.EditorFragsViewPagerAdapter;
import com.shortmeet.www.zTakePai.editt.editor.fragment.EditorFragment;
import com.shortmeet.www.zTakePai.editt.editor.fragment.FilterFragment;
import com.shortmeet.www.zTakePai.editt.effects.control.BottomAnimation;
import com.shortmeet.www.zTakePai.editt.effects.control.EditorService;
import com.shortmeet.www.zTakePai.editt.effects.control.EffectInfo;
import com.shortmeet.www.zTakePai.editt.effects.control.OnDialogButtonClickListener;
import com.shortmeet.www.zTakePai.editt.effects.control.OnEffectChangeListener;
import com.shortmeet.www.zTakePai.editt.effects.control.UIEditorPage;
import com.shortmeet.www.zTakePai.editt.effects.control.ViewStack;
import com.shortmeet.www.zTakePai.editt.effects.control.onSeltClickListener;
import com.shortmeet.www.zTakePai.editt.util.Common;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 短视频编辑界面
 */
public class EditorActivity extends AppCompatActivity implements
  /*  OnTabChangeListener,*/ OnEffectChangeListener, BottomAnimation, View.OnClickListener, onSeltClickListener {
    private static final String TAG = "EditorActivity";
    public static final String KEY_VIDEO_PARAM = "video_param";
    public static final String KEY_PROJECT_JSON_PATH = "project_json_path";
    public static final String KEY_TEMP_FILE_LIST = "temp_file_list";

    private static final int REQUEST_CODE_VIDEO_CROP = 1;
    private static final int REQUEST_CODE_IMAGE_CROP = 2;

    //private LinearLayout mBottomLinear;
    private SurfaceView mSurfaceView;
  //  private TabGroup mTabGroup;
    private ViewStack mViewStack;
   // private EditorService mEditorService;

    private AliyunIEditor mAliyunIEditor;
    private AliyunIPlayer mAliyunIPlayer;
    private AliyunPasterManager mPasterManager;
  //  private RecyclerView mThumbnailView;
   // private TimelineBar mTimelineBar;
    private RelativeLayout mActionBar;
    private FrameLayout resCopy;

    private FrameLayout mPasterContainer;
    private FrameLayout mGlSurfaceContainer;
    private Uri mUri;
    private int mScreenWidth;
    private ImageView mIvLeft;
 //   private ImageView mIvRight;
    private TextView mTvRight;
    private TextView mTvCenter;
    private LinearLayout mBarLinear;
    private ImageView mPlayImage;
   // private TextView mTvCurrTime;
    private AliyunVideoParam mVideoParam;
    private boolean mIsComposing = false; //当前是否正在合成视频
    private boolean isFullScreen = false; //导入视频是否全屏显示
    private ProgressDialog dialog;
    private MediaScannerConnection mMediaScanner;
    private RelativeLayout mEditor;
    private AliyunICanvasController mCanvasController;
    private ArrayList<String> mTempFilePaths = null;
    private AliyunIThumbnailFetcher mThumbnailFetcher;
    private Bitmap mWatermarkBitmap;
    private File mWatermarkFile;
    //提示是否上传的dialog
    private AlertDialog updialog;
    private TextView tvTitle;
    private AppCompatButton btnNegative;
    private AppCompatButton btnPositive;

    //播放进度条
    private ProgressBar play_duration_bar;
    //进度条父控件
    private LinearLayout rl_play_progress;
    //播放当前时间
    private TextView tv_play_current_duration;
    //播放总体时间
    private TextView tv_play_total_duration;
    private long downTime;

    // Fly 注： 导航条
    private ScrollIndicatorView scrollIndicatorEditor;
    //Viewpager
    private ViewPager vpEditor;
    //指示器
    private IndicatorViewPager mIndicatorViewPager;
    private EditorService mEditorService;
    //fragviewpager 特别适配器
    EditorFragsViewPagerAdapter mEditorFragsViewPagerAdapter;
    //指示器数据集合
    private static final String[] editorthemes = {"滤镜","剪辑"};
    //fragment 集合
    private List<Fragment> fragList = new ArrayList<>();
    private Context mContext;
    private AliyunIImport mImport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        SystemUtils.hideBottomUIMenu(this);
        mContext=this;
        mWatermarkFile = new File(StorageUtils.getCacheDirectory(EditorActivity.this) + "/AliyunEditorDemo/tail/logo.png");

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        setContentView(R.layout.activity_editor);
        Intent intent = getIntent();
        if (intent.getStringExtra(KEY_PROJECT_JSON_PATH) != null) {
            mUri = Uri.fromFile(new File(intent.getStringExtra(KEY_PROJECT_JSON_PATH)));
        }
        if (intent.getSerializableExtra("video_param") != null) {
            mVideoParam = (AliyunVideoParam) intent.getSerializableExtra(KEY_VIDEO_PARAM);
        }

        mTempFilePaths = intent.getStringArrayListExtra(KEY_TEMP_FILE_LIST);
        initView();
        initPager();
        initPageData();
        musicApply();
     //   initListView();//tablayout
      //  add2Control();//tablayout
        initEditor();
        mMediaScanner = new MediaScannerConnection(this, null);
        mMediaScanner.connect();
        copyAssets();
    }

    private void initView() {
        mEditor = (RelativeLayout) findViewById(R.id.activity_editor);
        resCopy = (FrameLayout) findViewById(R.id.copy_res_tip);
        mBarLinear = (LinearLayout) findViewById(R.id.bar_linear);
        mBarLinear.bringToFront();
        mActionBar = (RelativeLayout) findViewById(R.id.action_bar);
        mIvLeft = (ImageView) findViewById(R.id.iv_left);
        mTvCenter = (TextView) findViewById(R.id.tv_center);
       // mIvRight = (ImageView) findViewById(R.id.iv_right);
        mTvRight= (TextView) findViewById(R.id.tv_right);
        mIvLeft.setImageResource(R.mipmap.shortmeet_icon_back_po);
        mTvCenter.setText(getString(R.string.edit_nav_edit));
        mTvRight.setText(getString(R.string.edit_complete));
      //  mIvRight.setImageResource(R.mipmap.icon_next);
        mIvLeft.setVisibility(View.VISIBLE);
     //   mIvRight.setVisibility(View.VISIBLE);
        mTvRight.setVisibility(View.VISIBLE);

        mTvCenter.setVisibility(View.VISIBLE);
        mIvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
   //     mTvCurrTime = (TextView) findViewById(R.id.tv_curr_duration);
        mGlSurfaceContainer = (FrameLayout) findViewById(R.id.glsurface_view);
        mSurfaceView = (SurfaceView) findViewById(R.id.play_view);
       // mBottomLinear = (LinearLayout) findViewById(R.id.edit_bottom_tab);
        mPasterContainer = (FrameLayout) findViewById(R.id.pasterView);
        mPlayImage = (ImageView) findViewById(R.id.play_button);//播放按钮
        mPlayImage.setOnClickListener(this);
        play_duration_bar = (ProgressBar) findViewById(R.id.play_duration_bar);//进度条
        rl_play_progress= (LinearLayout) findViewById(R.id.rl_play_progress);//进度条父控件
        tv_play_current_duration= (TextView) findViewById(R.id.tv_play_current_duration);//current
        tv_play_total_duration= (TextView) findViewById(R.id.tv_play_total_duration);//total

        scrollIndicatorEditor = (ScrollIndicatorView)findViewById(R.id.scroll_indicator_editor);//导航条
        vpEditor = (ViewPager)findViewById(R.id.vp_editor);//viewpage


        final GestureDetector mGesture = new GestureDetector(this,
                new MyOnGestureListener());
        View.OnTouchListener pasterTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
               // mGesture.onTouchEvent(event);以前逻辑

                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    downTime = System.currentTimeMillis();
                }else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL){
                    long timeOffset = System.currentTimeMillis() - downTime;
                    if(timeOffset > 1000){//长按
                        LogUtils.e("mRecordBtn","long pressed");
                    }else{//点击
                        if (mAliyunIPlayer != null) {
                            if (mAliyunIPlayer.isPlaying()) {
                                playingPause();
                            } else {
                                playingResume();
                                if (mCurrentEditEffect != null && !mCurrentEditEffect.isPasterRemoved()) {
                                    mCurrentEditEffect.editTimeCompleted();
                                    //要保证涂鸦永远在动图的上方，则需要每次添加动图时都把已经渲染的涂鸦remove掉，添加完动图后，再重新把涂鸦加上去
                                    mCanvasController = mAliyunIEditor.obtainCanvasController(EditorActivity.this, mGlSurfaceContainer.getWidth(), mGlSurfaceContainer.getHeight());
                                    if(mCanvasController.hasCanvasPath()){
                                        mCanvasController.removeCanvas();
                                        mCanvasController.resetPaintCanvas();
                                    }
                                }
                            }
                        }
                    }
                }
                return true;
            }
        };

        mPasterContainer.setOnTouchListener(pasterTouchListener);
    }


    private void initPager(){
        float unSelectSize = 12;
        float selectSize = unSelectSize * 1.3f;
        scrollIndicatorEditor.setOnTransitionListener(new OnTransitionTextListener().setColor(UiUtils.getColor(R.color.white), UiUtils.getColor(R.color.black_5)).setSize(selectSize, selectSize));
        ColorBar colorBar=new ColorBar(this,UiUtils.getColor(R.color.white), 10);
        colorBar.getSlideView().setBackgroundResource(R.drawable.shape_editorline);
        colorBar.setWidth(30);
        colorBar.setGravity(ScrollBar.Gravity.BOTTOM);
        scrollIndicatorEditor.setScrollBar(colorBar);
        mIndicatorViewPager = new IndicatorViewPager(scrollIndicatorEditor, vpEditor);
        mEditorFragsViewPagerAdapter= new EditorFragsViewPagerAdapter(this.getSupportFragmentManager(),mContext, Arrays.asList(editorthemes),fragList);
        mIndicatorViewPager.setAdapter(mEditorFragsViewPagerAdapter);
        mEditorService = new EditorService();
    }

    private void initPageData(){
        if (fragList!=null){
            if (fragList.size()>0){
                fragList.clear();
            }
            FilterFragment filterFragment = FilterFragment.newInstance();
            filterFragment.setmEditorService(mEditorService);
            filterFragment.setOnEffectChangeListener(this);

            EditorFragment editorFragment = EditorFragment.newInstance();
            editorFragment.setOnclickListener(this);
            fragList.add(filterFragment);
            fragList.add(editorFragment);
        }
        mEditorFragsViewPagerAdapter.notifyDataSetChanged();
    }
    private void initGlSurfaceView() {
        if (mAliyunIPlayer != null) {
            if (mVideoParam == null) {
                return;
            }
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mGlSurfaceContainer.getLayoutParams();
            RelativeLayout.LayoutParams playlayoutParams = (RelativeLayout.LayoutParams)rl_play_progress.getLayoutParams();
            int rotation = mAliyunIPlayer.getRotation();
            int outputWidth = mVideoParam.getOutputWidth();
            int outputHeight = mVideoParam.getOutputHeight();
            if (rotation == 90 || rotation == 270) {
                int temp = outputWidth;
                outputWidth = outputHeight;
                outputHeight = temp;
            }
            float percent;
            if (outputWidth >= outputHeight) {
                percent = (float) outputWidth / outputHeight;
            } else {
                percent = (float) outputHeight / outputWidth;
            }
            if (percent < 1.5 || rotation == 90 || rotation == 270) {
               // layoutParams.height = Math.round((float) outputHeight * mScreenWidth / outputWidth);
                layoutParams.height =(int) (mScreenWidth*0.9*4)/3;
                layoutParams.width= (int) (mScreenWidth*0.9);

                layoutParams.addRule(RelativeLayout.BELOW, R.id.bar_linear);
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL,R.id.activity_editor);

                playlayoutParams.width= (int) (mScreenWidth*0.9);
                playlayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL,R.id.activity_editor);
            } else {
                layoutParams.height = RelativeLayout.LayoutParams.MATCH_PARENT;
                isFullScreen = true;
             //   mBottomLinear.setBackgroundColor(getResources().getColor(R.color.tab_bg_color_50pct));
                mActionBar.setBackgroundColor(getResources().getColor(R.color.action_bar_bg_50pct));
            }
            mGlSurfaceContainer.setLayoutParams(layoutParams);
            rl_play_progress.setLayoutParams(playlayoutParams);
        }
    }

    /**
     * 初始化viewstack
     */
    private void musicApply(){
        mViewStack = new ViewStack(this);
        mViewStack.setEditorService(mEditorService);
        mViewStack.setEffectChange(this);
        mViewStack.setBottomAnimation(this);
    }


/*    private void initListView() {
        mEditorService = new EditorService();
        mTabGroup = new TabGroup();
        mViewStack = new ViewStack(this);
        mViewStack.setEditorService(mEditorService);
        mViewStack.setEffectChange(this);
        mViewStack.setBottomAnimation(this);
        mViewStack.setDialogButtonClickListener(mDialogButtonClickListener);

        mTabGroup.addView(findViewById(R.id.tab_effect_filter));//滤镜
        mTabGroup.addView(findViewById(R.id.tab_effect_overlay));//图片贴片
        mTabGroup.addView(findViewById(R.id.tab_effect_caption));//文字贴片
        mTabGroup.addView(findViewById(R.id.tab_effect_mv));//插入mv
        mTabGroup.addView(findViewById(R.id.tab_effect_audio_mix));//添加音乐
        mTabGroup.addView(findViewById(R.id.tab_paint));//纯色贴片
    }*/

/*    private void add2Control() {
        TabViewStackBinding tabViewStackBinding = new TabViewStackBinding();
        tabViewStackBinding.setViewStack(mViewStack);
        mTabGroup.setOnCheckedChangeListener(tabViewStackBinding);
        mTabGroup.setOnTabChangeListener(this);
    }*/

    private void initEditor() {
        mAliyunIEditor = AliyunEditorFactory.creatAliyunEditor(mUri);
        mAliyunIEditor.init(mSurfaceView);
        mAliyunIPlayer = mAliyunIEditor.createAliyunPlayer();
        if (mAliyunIPlayer == null) {
            ToastUtil.showToast(this, "Create AliyunPlayer failed --创建AliyunPlayer失败啦");
            finish();
            return;
        }
        initGlSurfaceView();



      /*  mEditorService.setFullScreen(isFullScreen);
        mEditorService.addTabEffect(UIEditorPage.MV, mAliyunIEditor.getMVLastApplyId());
        mEditorService.addTabEffect(UIEditorPage.FILTER_EFFECT, mAliyunIEditor.getFilterLastApplyId());
        mEditorService.addTabEffect(UIEditorPage.AUDIO_MIX, mAliyunIEditor.getMusicLastApplyId());
        mEditorService.setPaint(mAliyunIEditor.getPaintLastApply());*/
        mAliyunIPlayer.setOnPreparedListener(new OnPreparedListener() {

            @Override
            public void onPrepared() {
                ScaleMode mode = mVideoParam.getScaleMode();
                if (mode != null) {
                    switch (mode) {
                        case LB:
                            mAliyunIPlayer.setDisplayMode(VideoDisplayMode.SCALE);
                            break;
                        case PS:
                            mAliyunIPlayer.setDisplayMode(VideoDisplayMode.FILL);
                            break;
                    }
                }
                mAliyunIPlayer.setFillBackgroundColor(Color.BLACK);
         /*       if (mTimelineBar == null) {
                    mTimelineBar = new TimelineBar(
                            mAliyunIPlayer.getDuration(),
                            DensityUtil.dip2px(EditorActivity.this, 50),
                            new TimelineBar.TimelinePlayer() {
                                @Override
                                public long getCurrDuration() {
                                    return mAliyunIPlayer.getCurrentPosition();
                                }
                            });
                    mTimelineBar.setThumbnailView(new TimelineBar.ThumbnailView() {
                        @Override
                        public RecyclerView getThumbnailView() {
                            return mThumbnailView;
                        }

                        @Override
                        public ViewGroup getThumbnailParentView() {
                            return (ViewGroup) mThumbnailView.getParent();
                        }

                        @Override
                        public void updateDuration(long duration) {
                            mTvCurrTime.setText(convertDuration2Text(duration));
                        }
                    });
                    ViewGroup.MarginLayoutParams layoutParams =
                            (ViewGroup.MarginLayoutParams) mThumbnailView.getLayoutParams();
                    layoutParams.width = mScreenWidth;
                    mTimelineBar.setTimelineBarDisplayWidth(mScreenWidth);
                    mTimelineBar.setBarSeekListener(new TimelineBar.TimelineBarSeekListener() {
                        @Override
                        public void onTimelineBarSeek(long duration) {
                            mAliyunIPlayer.seek(duration);
                            mTimelineBar.pause();
                            mPlayImage.setSelected(true);
                            mPlayImage.setEnabled(false);
                            Log.d(TimelineBar.TAG, "OnTimelineSeek duration = " + duration);
                            if (mCurrentEditEffect != null
                                    && !mCurrentEditEffect.isEditCompleted()) {
                                if (!mCurrentEditEffect.isVisibleInTime(duration)) {
                                    //隐藏
                                    mCurrentEditEffect.mPasterView.setVisibility(View.GONE);
                                } else {
                                    //显示
                                    mCurrentEditEffect.mPasterView.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                        @Override
                        public void onTimelineBarSeekFinish(long duration) {
//                            Log.e(TAG, "onTimelineBarSeekFinish duration..." + duration);
                            mAliyunIPlayer.seek(duration);
                            mTimelineBar.pause();
                            mPlayImage.setSelected(true);
                        }
                    });
                }*/
                if (!mAliyunIPlayer.isPlaying()) {
                    mAliyunIPlayer.start();
                }
              //  mTimelineBar.start();
                mPasterManager.setDisplaySize(mPasterContainer.getWidth(), mPasterContainer.getHeight());
               // mPasterManager.setOnPasterRestoreListener(mOnPasterRestoreListener);

                tv_play_total_duration.setText(convertDuration2Text(mAliyunIPlayer.getDuration()));//设置total的值
                play_duration_bar.setMax((int)( mAliyunIPlayer.getDuration()/(1000*1000)));//设置进度条的总大小

            }
        });
        mAliyunIPlayer.setOnPlayCallbackListener(new OnPlayCallback() {

            @Override
            public void onPlayStarted() {
             /*   if (mTimelineBar.isPausing() && !mIsComposing) {
                    mTimelineBar.resume();
                }*/
                if (mWatermarkFile.exists()) {
                    if (mWatermarkBitmap == null) {
                        mWatermarkBitmap = BitmapFactory.decodeFile(StorageUtils.getCacheDirectory(EditorActivity.this) + "/AliyunEditorDemo/tail/logo.png");
                    }
                    /**
                     * 水印例子 水印的大小为 ：水印图片的宽高和显示区域的宽高比，注意保持图片的比例，不然显示不完全  水印的位置为 ：以水印图片中心点为基准，显示区域宽高的比例为偏移量，0,0为左上角，1,1为右下角
                     */
                    mAliyunIEditor.applyWaterMark(StorageUtils.getCacheDirectory(EditorActivity.this) + "/AliyunEditorDemo/tail/logo.png",
                            (float) mWatermarkBitmap.getWidth() * 0.5f * 0.8f / mSurfaceView.getWidth(),
                            (float) mWatermarkBitmap.getHeight() * 0.5f * 0.8f / mSurfaceView.getHeight(),
                            1f - (float) mWatermarkBitmap.getWidth() / 1.5f / mSurfaceView.getWidth() / 2,
                            0f + (float) mWatermarkBitmap.getHeight() / 1.5f / mSurfaceView.getHeight() / 2);
                }
            }

            @Override
            public void onError(int errorCode) {
                switch (errorCode) {
                    case AliyunErrorCode.ERROR_MEDIA_NOT_SUPPORTED_AUDIO:
                        ToastUtil.showToast(EditorActivity.this, R.string.not_supported_audio);
                        break;
                    case AliyunErrorCode.ERROR_MEDIA_NOT_SUPPORTED_VIDEO:
                        ToastUtil.showToast(EditorActivity.this, R.string.not_supported_video);
                        break;
                    case AliyunErrorCode.ERROR_MEDIA_NOT_SUPPORTED_PIXCEL_FORMAT:
                        ToastUtil.showToast(EditorActivity.this, R.string.not_supported_pixel_format);
                        break;
                    default:
                        ToastUtil.showToast(EditorActivity.this, R.string.play_video_error);
                }
//                mPlayImage.setEnabled(true);
//                mAliyunIPlayer.stop();
//                mTimelineBar.stop();
//                finish();
                mPlayImage.setEnabled(true);
            }

            @Override
            public void onSeekDone() {
                mPlayImage.setEnabled(true);
            }

            @Override
            public void onPlayCompleted() {
                //重播时必须先掉stop，再调用start
                mAliyunIPlayer.stop();
                mAliyunIPlayer.start();
             //   mTimelineBar.restart();
//                Log.d(TimelineBar.TAG, "TailView play restart");
            }

            @Override
            public int onTextureIDCallback(int txtID, int txtWidth, int txtHeight) {
              //  Log.d(TAG, "onTextureIDCallback: txtID "+txtID+", txtWidth "+txtWidth+", txtHeight "+txtHeight);
                if(mAliyunIPlayer.getCurrentPosition()>=mAliyunIPlayer.getDuration()){
                    play_duration_bar.setProgress((int)(mAliyunIPlayer.getDuration()/(1000*1000)));
                }
                play_duration_bar.setProgress((int) (mAliyunIPlayer.getCurrentPosition()/(1000*1000)));
                EditorActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_play_current_duration.setText(convertDuration2Text(mAliyunIPlayer.getCurrentPosition()));
                    }
                });

                return 0;
            }
        });

        mPasterManager = mAliyunIEditor.createPasterManager();
    //    mThumbnailView = (RecyclerView) findViewById(R.id.rv_thumbnail);
     //   mThumbnailView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mThumbnailFetcher = AliyunThumbnailFetcherFactory.createThumbnailFetcher();
        mThumbnailFetcher.fromConfigJson(mUri.getPath());
     //   mThumbnailView.setAdapter(new ThumbnailAdapter(10, mThumbnailFetcher, mScreenWidth));
        mTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                composeVider(true);
            }
        });
    }

    /**
     * 合成视屏
     */
    public void composeVider(final boolean isupload){
        if(mIsComposing){
            return;
        }
        dialog = new ProgressDialog(EditorActivity.this);//********************************8
        dialog.setTitle("合成中");
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setMax(100);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mAliyunIEditor.getExporter().cancel();
            }
        });
        if(mCurrentEditEffect != null){
            mCurrentEditEffect.editTimeCompleted();
        }
        dialog.show();
        //  mTimelineBar.pause();
        AliyunIExporter exporter = mAliyunIEditor.getExporter();
        File tailImg = new File(StorageUtils.getCacheDirectory(EditorActivity.this) + "/AliyunEditorDemo/tail/logo.png");
        System.out.println("aaa"+tailImg.exists());
        //添加水印
     /*   if (tailImg.exists()) {
          *//*  exporter.setTailWatermark("/storage/emulated/0/Android/data/com.aliyun.aliyunvideosdkpro/cache/" + "/AliyunEditorDemo/tail/logo.png",
                    280.0f / mSurfaceView.getMeasuredWidth(),
                    200.f / mSurfaceView.getMeasuredHeight(), 0, 0);*//*
            exporter.setTailWatermark(StorageUtils.getCacheDirectory(EditorActivity.this) + "/AliyunEditorDemo/tail/logo.png",
                    280.0f / mSurfaceView.getMeasuredWidth(),
                    200.f / mSurfaceView.getMeasuredHeight(), 0, 0);
        }*/
        long time = System.currentTimeMillis();
        //storage/emulated/0/outputVideo1505665896747.mp4
        ///storage/emulated/0/outputVideo1505665990143.mp4
        FileUtil.createFileDir(ApiConstant.COMPOSE_PATH);
        FileUtil.createFileDir(ApiConstant.COMPOSE_PATH_EDITOR);
        //   final String path = Environment.getExternalStorageDirectory()+ File.separator + "shortmeet/" + time + ".mp4";
        final String path =  ApiConstant.COMPOSE_PATH_EDITOR+ "editor_"+time + ".mp4";
        exporter.startCompose(path, new OnComposeCallback() {
            @Override
            public void onError() {
                mIsComposing = false;
                Log.e("COMPOSE", "compose error");
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "合成失败", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onComplete() {
                Log.e("COMPOSE", "compose finished");
                dialog.dismiss();

                if (mMediaScanner != null) {
                    mMediaScanner.scanFile(path, "video/mp4");
                }
                Toast.makeText(getApplicationContext(), "合成完成", Toast.LENGTH_SHORT).show();
                System.out.println("mytwo合成完成啦路径是========================》"+path);
                mAliyunIPlayer.start();
                //    mTimelineBar.resume();
                mIsComposing = false;
                if(isupload){//上传到服务器
                    //弹出对话框 询问是否上传
                    DialogUtils.showAlertDialog(EditorActivity.this, getString(R.string.update_dialog_msg), new DialogCallBack<AlertDialog>() {
                        @Override
                        public void OnSuccess(AlertDialog alertDialog) {
                            Intent intent =new Intent( EditorActivity.this, MyUploadActivity.class);
                            intent.putExtra("upPath",path);
                            intent.putExtra("vedioDuration",mThumbnailFetcher.getTotalDuration());
                            startActivity(intent);
                            EditorActivity.this.finish();
                            alertDialog.cancel();
                        }
                        @Override
                        public void OnFailed(AlertDialog alertDialog) {
                            alertDialog.cancel();
                        }
                    });

     /*               updialog = new AlertDialog.Builder(EditorActivity.this, R.style.Theme_Transparent).create();
                    //给对话框去标题栏，记得次句要写在setContentView之前。
                    // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                    View view = LayoutInflater.from(EditorActivity.this).inflate(R.layout.item_showdialog_upload_confirm, null);
                    tvTitle = (TextView) view.findViewById(R.id.tv_title);
                    btnNegative = (AppCompatButton) view.findViewById(R.id.btn_negative);
                    btnPositive = (AppCompatButton) view.findViewById(R.id.btn_positive);
                    btnNegative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            updialog.cancel();
                        }
                    });
                    btnPositive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent =new Intent( EditorActivity.this, MyUploadActivity.class);
                            intent.putExtra("upPath",path);
                            intent.putExtra("vedioDuration",mThumbnailFetcher.getTotalDuration());
                            startActivity(intent);
                            EditorActivity.this.finish();
                            updialog.cancel();
                        }
                    });
                    updialog.setView(view);
                    updialog.show();*/
                }else{//跳转到剪辑
                    Intent intent = new Intent(EditorActivity.this, AliyunVideoCrop.class);
                    intent.putExtra(CropKey.VIDEO_PATH, path);
                    intent.putExtra(CropKey.VIDEO_DURATION, mThumbnailFetcher.getTotalDuration());
                    intent.putExtra(CropKey.VIDEO_RATIO, CropKey.RATIO_MODE_3_4);
                    intent.putExtra(CropKey.VIDEO_SCALE, CropKey.SCALE_CROP);
                    intent.putExtra(CropKey.VIDEO_QUALITY,  VideoQuality.HD);
                    intent.putExtra(CropKey.VIDEO_GOP, 125);
                    intent.putExtra(CropKey.VIDEO_FRAMERATE, 25);
                    intent.putExtra(CropKey.ACTION, CropKey.ACTION_TRANSCODE);
                    intent.putExtra(AliyunSnapVideoParam.VIDEO_RESOLUTION,AliyunSnapVideoParam.RESOLUTION_480P);
                    startActivityForResult(intent, REQUEST_CODE_VIDEO_CROP);
                }
            }

            @Override
            public void onProgress(int progress) {
                Log.d(TAG, "compose progress " + progress);
                dialog.setProgress(progress);
            }

            @Override
            public void onCancel() {
                FileUtils.deleteFile(path);
                mAliyunIPlayer.start();
                //   mTimelineBar.resume();
                mIsComposing = false;
            }
        });
        mIsComposing = true;
    }

 /*   private final OnPasterRestored mOnPasterRestoreListener = new OnPasterRestored() {

        @Override
        public void onPasterRestored(List<AliyunPasterController> controllers) {
            for (AliyunPasterController c : controllers) {
                if (!c.isPasterExists()) {
                    continue;
                }
                if (c.getPasterType() == EffectPaster.PASTER_TYPE_GIF) {
                    mCurrentEditEffect = addPaster(c);
                } else if (c.getPasterType() == EffectPaster.PASTER_TYPE_TEXT) {
                    mCurrentEditEffect = addSubtitle(c, true);
                } else if (c.getPasterType() == EffectPaster.PASTER_TYPE_CAPTION) {
                    mCurrentEditEffect = addCaption(c);
                }

                mCurrentEditEffect.showTimeEdit();
                mCurrentEditEffect.editTimeCompleted();

                mCurrentEditEffect.moveToCenter();
            }
            //要保证涂鸦永远在动图的上方，则需要每次添加动图时都把已经渲染的涂鸦remove掉，添加完动图后，再重新把涂鸦加上去
            mCanvasController = mAliyunIEditor.obtainCanvasController(EditorActivity.this, mGlSurfaceContainer.getWidth(), mGlSurfaceContainer.getHeight());
            if(mCanvasController.hasCanvasPath()){
                mCanvasController.removeCanvas();
                mCanvasController.resetPaintCanvas();
            }
        }

    };*/

    @Override
    protected void onResume() {
        super.onResume();
        mAliyunIPlayer.resume();
        mPlayImage.setSelected(false);
   /*     if (mTimelineBar != null) {
            mTimelineBar.resume();
        }*/
        mAliyunIEditor.onResume();
        checkAndRemovePaster();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if(mCurrentEditEffect != null && !mCurrentEditEffect.isEditCompleted()) {
            mCurrentEditEffect.editTimeCompleted();
        }
        mAliyunIEditor.onPause();
        mAliyunIPlayer.pause();
     /*   if(mTimelineBar != null){
            mTimelineBar.pause();
        }
*/
        mPlayImage.setSelected(true);
        if (dialog != null && dialog.isShowing()) {
            mIsComposing = false;
            dialog.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAliyunIPlayer != null) {
            mAliyunIEditor.onDestroy();
        }
      /*  if (mTimelineBar != null) {
            mTimelineBar.stop();
        }*/
        if (mMediaScanner != null) {
            mMediaScanner.disconnect();
        }

        if (mThumbnailFetcher != null) {
            mThumbnailFetcher.release();
        }

        if(mCanvasController != null) {
            mCanvasController.release();
        }

        //删除录制生成的临时文件
        //deleteTempFiles();由于返回依然可以接着录，因此现在不能删除
    }

/*    @Override
    public void onTabChange() {
        //暂停播放
//        if (mAliyunIPlayer.isPlaying()) {
//            playingPause();
//        }

        //tab切换时通知
        hideBottomView();
        UIEditorPage index = UIEditorPage.get(mTabGroup.getCheckedIndex());
        int ix = mEditorService.getEffectIndex(index);
        switch (index) {
            case FILTER_EFFECT:
                break;
            case OVERLAY:
                break;
            default:
        }
        Log.e("editor", "====== onTabChange " + ix + " " + index);
    }*/
/*
    @Override
    public void onEffectChange(EffectInfo effectInfo) {
        Log.e("editor", "====== onEffectChange ");
        //返回素材属性

        EffectBean effect = new EffectBean();
        effect.setId(effectInfo.id);
        effect.setPath(effectInfo.getPath());
        UIEditorPage type = effectInfo.type;
        AliyunPasterController c;
        Log.d(TAG, "effect path " + effectInfo.getPath());
        switch (type) {
            case AUDIO_MIX:
                mAliyunIEditor.applyMusicMixWeight(effectInfo.musicWeight);
                if (!effectInfo.isAudioMixBar) {
                    mAliyunIEditor.applyMusic(effect);
                //    mTimelineBar.resume();
                    mPlayImage.setSelected(false);
                }
                break;
            case FILTER_EFFECT:
                mAliyunIEditor.applyFilter(effect);
                break;
            case MV:
                if (mCurrentEditEffect != null && !mCurrentEditEffect.isPasterRemoved()) {
                    mCurrentEditEffect.editTimeCompleted();
                }

                String path = null;
                if (effectInfo.list != null) {
                    path = Common.getMVPath(effectInfo.list, mAliyunIPlayer.getVideoWidth(), mAliyunIPlayer.getVideoHeight());
                }
                effect.setPath(path);
                mAliyunIEditor.applyMV(effect);
              //  mTimelineBar.resume();
                mPlayImage.setSelected(false);
                break;
            case CAPTION:
                c = mPasterManager.addPaster(effectInfo.getPath());
                c.setPasterStartTime(mAliyunIPlayer.getCurrentPosition());
                PasterUICaptionImpl cui = addCaption(c);
                if (mCurrentEditEffect != null && !mCurrentEditEffect.isPasterRemoved()) {
                    mCurrentEditEffect.editTimeCompleted();
                }
                playingPause();
                mCurrentEditEffect = cui;
                mCurrentEditEffect.showTimeEdit();
                Log.d("XXX", "add Caption");
                break;
            case OVERLAY:
                c = mPasterManager.addPaster(effectInfo.getPath());
                c.setPasterStartTime(mAliyunIPlayer.getCurrentPosition());
                PasterUIGifImpl gifui = addPaster(c);
                if (mCurrentEditEffect != null && !mCurrentEditEffect.isPasterRemoved()) {
                    mCurrentEditEffect.editTimeCompleted();
                }
                playingPause();
                mCurrentEditEffect = gifui;
                mCurrentEditEffect.showTimeEdit();
                Log.d("XXX", "add Overlay");

                break;
            case FONT:
                c = mPasterManager.addSubtitle(null, effectInfo.fontPath + "/font.ttf");
                c.setPasterStartTime(mAliyunIPlayer.getCurrentPosition());
                PasterUITextImpl textui = addSubtitle(c, false);
                if (mCurrentEditEffect != null && !mCurrentEditEffect.isPasterRemoved()) {
                    mCurrentEditEffect.editTimeCompleted();
                }
                playingPause();
                mCurrentEditEffect = textui;
                mCurrentEditEffect.showTimeEdit();
                textui.showTextEdit();
//                mCurrentEditEffect.setImageView((ImageView) findViewById(R.id.test_image));

                Log.d("XXX", "add Font");
                break;
            case PAINT:
                if (mCurrentEditEffect != null && !mCurrentEditEffect.isEditCompleted()) {
                    mCurrentEditEffect.editTimeCompleted();
                }
                mCanvasController = mAliyunIEditor.obtainCanvasController(EditorActivity.this, mGlSurfaceContainer.getWidth(), mGlSurfaceContainer.getHeight());
                mCanvasController.removeCanvas();
                addPaint(mCanvasController);
                break;
        }
    }*/

    private void checkAndRemovePaster() {
        int count = mPasterContainer.getChildCount();
        for (int i = count - 1; i >= 0; i--) {
            View pv = mPasterContainer.getChildAt(i);
            PasterUISimpleImpl uic = (PasterUISimpleImpl) pv.getTag();
            if (uic != null && !uic.isPasterExists()) {
                Log.e(TAG, "removePaster");
                uic.removePaster();
            }
        }
    }

    protected void playingPause() {
        if (mAliyunIPlayer.isPlaying()) {
            mAliyunIPlayer.pause();
        //    mTimelineBar.pause();
           // mPlayImage.setSelected(true);
            mPlayImage.setVisibility(View.VISIBLE);
        }
    }

    protected void playingResume() {
        if (!mAliyunIPlayer.isPlaying()) {
            mAliyunIPlayer.resume();
         //   mTimelineBar.resume();
            //mPlayImage.setSelected(false);
            mPlayImage.setVisibility(View.GONE);
        }
    }

/*    private PasterUIGifImpl addPaster(AliyunPasterController controller) {
        AliyunPasterWithImageView pasterView = (AliyunPasterWithImageView) View.inflate(this,
                R.layout.qupai_paster_gif, null);

        mPasterContainer.addView(pasterView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        return new PasterUIGifImpl(pasterView, controller, mTimelineBar);
    }*/

    /**
     * 添加字幕
     *
     * @param controller
     * @return
     */
/*    private PasterUICaptionImpl addCaption(AliyunPasterController controller) {
        AliyunPasterWithImageView captionView = (AliyunPasterWithImageView) View.inflate(this,
                R.layout.qupai_paster_caption, null);
//        ImageView content = (ImageView) captionView.findViewById(R.id.qupai_overlay_content_animation);
//        Glide.with(getApplicationContext())
//                .load("file://" + controller.getPasterIconPath())
//                .into(content);
        mPasterContainer.addView(captionView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        return new PasterUICaptionImpl(captionView, controller, mTimelineBar);
    }*/

    /**
     * 添加文字
     *
     * @param controller
     * @param restore
     * @return
     */
/*    private PasterUITextImpl addSubtitle(AliyunPasterController controller, boolean restore) {
        AliyunPasterWithTextView captionView = (AliyunPasterWithTextView) View.inflate(this,
                R.layout.qupai_paster_text, null);
        mPasterContainer.addView(captionView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        return new PasterUITextImpl(captionView, controller, mTimelineBar, restore);
    }*/

    /**
     * 添加涂鸦
     *
     * @param
     * @return
     */
/*    private View addPaint(AliyunICanvasController canvasController) {
        hideBottomView();
        View canvasView = canvasController.getCanvas();
        mPasterContainer.removeView(canvasView);
        mPasterContainer.addView(canvasView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addPaintMenu(canvasController);
        return canvasView;
    }*/

/*    private void addPaintMenu(AliyunICanvasController canvasController) {
        PaintMenuView menuView = new PaintMenuView(canvasController);
        menuView.setOnPaintOpera(onPaintOpera);
        menuView.setEditorService(mEditorService);
        View view = menuView.getPaintMenu(this);
        if (isFullScreen) {
            view.findViewById(R.id.paint_menu).setBackgroundColor(getResources().getColor(R.color.tab_bg_color_50pct));
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        view.setLayoutParams(layoutParams);
        mEditor.addView(view);
    }*/

/*    private PaintMenuView.OnPaintOpera onPaintOpera = new PaintMenuView.OnPaintOpera() {
        @Override
        public void removeView(View view) {
            mEditor.removeView(view);
            mPasterContainer.removeView(mCanvasController.getCanvas());
            showBottomView();
        }

        @Override
        public void completeView() {
            mCanvasController.applyPaintCanvas();
        }
    };*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //mViewStack.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){//刷新activity
            String path = data.getStringExtra(CropKey.RESULT_KEY_CROP_PATH);
            Log.e("Tag",path);
            mImport = AliyunImportCreator.getImportInstance(EditorActivity.this);
            mImport.setVideoParam(mVideoParam);
            mImport.addVideo(path, 0, AliyunDisplayMode.DEFAULT);
            String projectJsonPath = mImport.generateProjectConfigure();
            if (projectJsonPath != null) {
                Intent intent = new Intent(this,EditorActivity.class);
                intent.putExtra("video_param", mVideoParam);
                intent.putExtra("project_json_path", projectJsonPath);
                startActivity(intent);
            }
       /*     Intent intent = new Intent(this, EditorActivity.class);
            intent.putExtra("video_param", mVideoParam);
            intent.putExtra("project_json_path", projectUri.getPath());
            */
        }
    }

    @Override
    public void showBottomView() {
//        ViewCompat.animate(mBottomLinear)
//                .translationYBy(-mBottomLinear.getMeasuredHeight())
//                .alpha(1f)
//                .setDuration(300).start();

      //  mBottomLinear.setVisibility(View.VISIBLE);
        mActionBar.setVisibility(View.VISIBLE);
        mPlayImage.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBottomView() {
//        ViewCompat.animate(mBottomLinear)
//                .translationYBy(mBottomLinear.getMeasuredHeight())
//                .alpha(0f)
//                .setDuration(300).start();

     //   mBottomLinear.setVisibility(View.GONE);
        mActionBar.setVisibility(View.GONE);
        mPlayImage.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        if (mAliyunIPlayer != null) {
            if (mAliyunIPlayer.isPlaying()) {
                playingPause();
            } else {
                playingResume();
                if (mCurrentEditEffect != null && !mCurrentEditEffect.isPasterRemoved()) {
                    mCurrentEditEffect.editTimeCompleted();
                    //要保证涂鸦永远在动图的上方，则需要每次添加动图时都把已经渲染的涂鸦remove掉，添加完动图后，再重新把涂鸦加上去
                    mCanvasController = mAliyunIEditor.obtainCanvasController(EditorActivity.this, mGlSurfaceContainer.getWidth(), mGlSurfaceContainer.getHeight());
                    if(mCanvasController.hasCanvasPath()){
                        mCanvasController.removeCanvas();
                        mCanvasController.resetPaintCanvas();
                    }
                }
            }
        }
    }

    private PasterUISimpleImpl mCurrentEditEffect;


    /**
     * 添加滤镜回调
     * @param effectInfo
     */
    @Override
    public void onEffectChange(EffectInfo effectInfo) {
        Log.e("editor", "====== onEffectChange ");
        //返回素材属性
        EffectBean effect = new EffectBean();
        effect.setId(effectInfo.id);
        effect.setPath(effectInfo.getPath());
        UIEditorPage type = effectInfo.type;
        switch (type){
            case AUDIO_MIX:
                mAliyunIEditor.applyMusicMixWeight(effectInfo.musicWeight);
                if (!effectInfo.isAudioMixBar) {
                    mAliyunIEditor.applyMusic(effect);
                    //    mTimelineBar.resume();
                    mPlayImage.setSelected(false);
                }
                break;
            case FILTER_EFFECT:
                mAliyunIEditor.applyFilter(effect);
                break;
        }

    }

    /**
     * 剪辑activity的点击
     * @param view
     */
    @Override
    public void onSelfClick(View view) {
        int tag= (int) view.getTag();
        switch (tag){//剪裁
            case EditorFragment.CROPTAG:
                composeVider(false);
                break;
            case EditorFragment.MUSICTAG://音乐
                mViewStack.setActiveIndex(4);
                break;
        }
    }


    private class MyOnGestureListener extends
            GestureDetector.SimpleOnGestureListener {
        float mPosX;
        float mPosY;
        boolean shouldDrag = true;

        boolean shouldDrag() {
            return shouldDrag;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            Log.d("MOVE", "onDoubleTapEvent");
            return super.onDoubleTapEvent(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.d("MOVE", "onSingleTapConfirmed");

            if (!shouldDrag) {
                boolean outside = true;
                int count = mPasterContainer.getChildCount();
                for (int i = count - 1; i >= 0; i--) {
                    View pv = mPasterContainer.getChildAt(i);
                    PasterUISimpleImpl uic = (PasterUISimpleImpl) pv.getTag();
                    if (uic != null) {
                        if (uic.isVisibleInTime(mAliyunIPlayer.getCurrentPosition())
                                && uic.contentContains(e.getX(), e.getY())) {
                            outside = false;
                            if (mCurrentEditEffect != null && mCurrentEditEffect != uic
                                    && !mCurrentEditEffect.isEditCompleted()) {
                                mCurrentEditEffect.editTimeCompleted();
                            }
                            mCurrentEditEffect = uic;
                            if (uic.isEditCompleted()) {
                                playingPause();
                                uic.editTimeStart();
                            }
                            break;
                        } else {
                            if (mCurrentEditEffect != uic && uic.isVisibleInTime(mAliyunIPlayer.getCurrentPosition())) {
                                uic.editTimeCompleted();
                                playingResume();
                            }
                        }
                    }
                }

                if (outside) {
                    if (mCurrentEditEffect != null && !mCurrentEditEffect.isEditCompleted()) {
//                        Log.d("LLLL", "CurrPosition = " + mAliyunIPlayer.getCurrentPosition());
                        mCurrentEditEffect.editTimeCompleted();
                        //要保证涂鸦永远在动图的上方，则需要每次添加动图时都把已经渲染的涂鸦remove掉，添加完动图后，再重新把涂鸦加上去
                        mCanvasController = mAliyunIEditor.obtainCanvasController(EditorActivity.this.getApplicationContext(), mGlSurfaceContainer.getWidth(), mGlSurfaceContainer.getHeight());
                        if(mCanvasController.hasCanvasPath()){
                            mCanvasController.removeCanvas();
                            mCanvasController.resetPaintCanvas();
                        }
                    }
                }
            } else {
                playingPause();
                mCurrentEditEffect.showTextEdit();
            }

            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }

        @Override
        public void onShowPress(MotionEvent e) {
            Log.d("MOVE", "onShowPress");
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            if (shouldDrag()) {
                if (mPosX == 0 || mPosY == 0) {
                    mPosX = e1.getX();
                    mPosY = e1.getY();
                }
                float x = e2.getX();
                float y = e2.getY();

                mCurrentEditEffect.moveContent(x - mPosX, y - mPosY);

                mPosX = x;
                mPosY = y;
            }

//            Log.d("MOVE", "onScroll" + " shouldDrag : " + shouldDrag
//                    + " x : " + mPosX + " y : " + mPosY + " dx : "
//                    + distanceX + " dy : " + distanceY);

            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.d("MOVE", "onLongPress");
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               float velocityX, float velocityY) {
            Log.d("MOVE", "onFling");
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
//            Log.d("MOVE", "onDown" + " (" + e.getX() + " : " + e.getY()
//                    + ")");
            if (mCurrentEditEffect != null && mCurrentEditEffect.isPasterRemoved()) {
                mCurrentEditEffect = null;
            }

            if (mCurrentEditEffect != null) {
                shouldDrag = !mCurrentEditEffect.isEditCompleted()
                        && mCurrentEditEffect.contentContains(e.getX(), e.getY())
                        && mCurrentEditEffect.isVisibleInTime(mAliyunIPlayer.getCurrentPosition());
            } else {
                shouldDrag = false;
            }

            mPosX = 0;
            mPosY = 0;
            return false;
        }
    }

    StringBuilder mDurationText = new StringBuilder(5);

    private String convertDuration2Text(long duration) {
        mDurationText.delete(0, mDurationText.length());
        int sec = Math.round(((float) duration) / (1000 * 1000));// us -> s
        int min = (sec % 3600) / 60;
        sec = (sec % 60);
        //TODO:优化内存,不使用String.format
        if (min >= 10) {
            mDurationText.append(min);
        } else {
            mDurationText.append("0").append(min);
        }
        mDurationText.append(":");
        if (sec >= 10) {
            mDurationText.append(sec);
        } else {
            mDurationText.append("0").append(sec);
        }
        return mDurationText.toString();
    }

    private void copyAssets() {
        new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] params) {
                Common.copyAll(EditorActivity.this, resCopy);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
//                resCopy.setVisibility(View.GONE);
            }
        }.execute();
    }

    public AliyunIPlayer getPlayer() {
        return this.mAliyunIPlayer;
    }

    public void showMessage(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(id);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private OnDialogButtonClickListener mDialogButtonClickListener = new OnDialogButtonClickListener() {
        @Override
        public void onPositiveClickListener(int index) {

        }

        @Override
        public void onNegativeClickListener(int index) {
            UIEditorPage in = UIEditorPage.get(index);
            int count = mPasterContainer.getChildCount();
            switch (in) {
                case OVERLAY://清除所有动图
                    for (int i = count - 1; i >= 0; i--) {
                        View pv = mPasterContainer.getChildAt(i);
                        PasterUISimpleImpl uic = (PasterUISimpleImpl) pv.getTag();
                        if (uic != null && uic.mController.getPasterType() == EffectPaster.PASTER_TYPE_GIF) {
                            uic.removePaster();
                        }
                    }
                    break;
                case CAPTION:
                    for (int i = count - 1; i >= 0; i--) {
                        View pv = mPasterContainer.getChildAt(i);
                        PasterUISimpleImpl uic = (PasterUISimpleImpl) pv.getTag();
                        if (uic == null) {
                            return;
                        }
                        if (uic.mController.getPasterType() == EffectPaster.PASTER_TYPE_CAPTION
                                || uic.mController.getPasterType() == EffectPaster.PASTER_TYPE_TEXT) {
                            uic.removePaster();
                        }
                    }
                    break;
            }
        }
    };


    private void deleteTempFiles() {
        if (mTempFilePaths != null) {
            for (String path : mTempFilePaths) {
                File file = new File(path);
                if (file.exists()) {
                    file.delete();
                }
            }
        }
    }




}
