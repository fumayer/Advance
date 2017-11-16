/*
 * Copyright (C) 2010-2017 Alibaba Group Holding Limited.
 */

package com.shortmeet.www.zTakePai.record;


import android.animation.Animator;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.common.global.Version;
import com.aliyun.common.utils.CommonUtil;
import com.aliyun.quview.RecordTimelineView;
import com.aliyun.recorder.AliyunRecorderCreator;
import com.aliyun.recorder.supply.AliyunIClipManager;
import com.aliyun.recorder.supply.AliyunIRecorder;
import com.aliyun.recorder.supply.RecordCallback;
import com.aliyun.struct.common.AliyunVideoParam;
import com.aliyun.struct.common.CropKey;
import com.aliyun.struct.common.ScaleMode;
import com.aliyun.struct.common.VideoQuality;
import com.aliyun.struct.effect.EffectFilter;
import com.aliyun.struct.recorder.CameraParam;
import com.aliyun.struct.recorder.CameraType;
import com.aliyun.struct.recorder.FlashType;
import com.aliyun.struct.recorder.MediaInfo;
import com.aliyun.struct.snap.AliyunSnapVideoParam;
import com.qu.preview.callback.OnFrameCallBack;
import com.qu.preview.callback.OnTextureIdCallBack;
import com.shortmeet.www.R;
import com.shortmeet.www.utilsUsed.AnimationUtils;
import com.shortmeet.www.utilsUsed.LogUtils;
import com.shortmeet.www.utilsUsed.SystemUtils;
import com.shortmeet.www.zTakePai.editt.editor.EditorActivity;
import com.shortmeet.www.zTakePai.editt.effects.control.BottomAnimation;
import com.shortmeet.www.zTakePai.editt.effects.control.EditorService;
import com.shortmeet.www.zTakePai.editt.effects.control.EffectInfo;
import com.shortmeet.www.zTakePai.editt.effects.control.OnEffectChangeListener;
import com.shortmeet.www.zTakePai.editt.effects.control.ViewStack;
import com.shortmeet.www.zTakePai.editt.util.Common;
import com.shortmeet.www.zTakePai.record.util.OrientationDetector;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 录制界面
 */
public class AliyunVideoRecorder extends FragmentActivity implements View.OnClickListener, View.OnTouchListener, ScaleGestureDetector.OnScaleGestureListener,
        GestureDetector.OnGestureListener,OnEffectChangeListener,BottomAnimation {

    private static final int EFFECT_BEAUTY_LEVEL = 80;
    private static final int TIMELINE_HEIGHT = 20;

    private static final int MAX_SWITCH_VELOCITY = 2000;
    private static final float FADE_IN_START_ALPHA = 0.3f;
    private static final int FILTER_ANIMATION_DURATION = 1000;


    public static final String NEED_GALLERY = "need_gallery";


    public static final String OUTPUT_PATH = "output_path";

    private static final int REQUEST_CROP = 2001;

    public static final String RESULT_TYPE = "result_type";
    public static final int RESULT_TYPE_CROP = 4001;
    public static final int RESULT_TYPE_RECORD = 4002;


    private int mResolutionMode;
    private int minDuration;
    private int maxDuration;
    private int gop;
    private int mBeautyLevel;
    private int recordMode;
    private VideoQuality videoQuality = VideoQuality.HD;
    private int mRatioMode = AliyunSnapVideoParam.RATIO_MODE_3_4;
    private int mSortMode = AliyunSnapVideoParam.SORT_MODE_MERGE;
    private AliyunIRecorder mRecorder;
    private AliyunIClipManager mClipManager;
    private AliyunSVideoGlSurfaceView mGlSurfaceView;
    private boolean isBeautyOn = true;
    private boolean isSelected = false;
    private RecordTimelineView mRecordTimelineView;
    private ImageView mSwitchRatioBtn, mSwitchBeautyBtn, mSwitchCameraBtn, mSwitchLightBtn, mBackBtn, mDeleteBtn, mCompleteBtn, mGalleryBtn,aliyun_import_editor,aliyun_effect_filter;
    private RelativeLayout mRecordBtn;
    private TextView mRecordTimeTxt;
    private FrameLayout mToolBar, mRecorderBar;
    private FlashType mFlashType = FlashType.OFF;
    private CameraType mCameraType = CameraType.FRONT;
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;
    private float scaleFactor;
    private float lastScaleFactor;
    private float exposureCompensationRatio = 0.5f;
    private boolean isOnMaxDuration;
    private boolean isOpenFailed;
    private boolean isRecording = false;
    private AliyunVideoParam mVideoParam;
    private OrientationDetector orientationDetector;
    private int tintColor, timelineDelBgColor, timelineBgColor ,timelinePosY,lightDisableRes,lightSwitchRes;
    private long downTime;
    private String[] filterList;
    private int filterIndex = 0;
    private TextView mFilterTxt;
    private boolean isNeedClip;
    private boolean isNeedGallery;
    private boolean isRecordError;
    private MediaScannerConnection msc;

    private int mFrame = 25;
    private ScaleMode mCropMode = ScaleMode.PS;
    private int minCropDuration = 2000;
    private int maxVideoDuration = 10000;
    private int minVideoDuration = 2000;
    private ViewStack mViewStack;//滤镜view
    private EditorService mEditorService;
    private FrameLayout copy_res_tip;//copy资源

    private ImageView shortmeet_record_native;//录制按钮
    private ImageView shortmeet_record_circle;//外面的圈
    private boolean isResume=false;
    int screenWidth;
    int screenHeight;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        SystemUtils.hideBottomUIMenu(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_recorder_demo);
        getStyleParam();
        initOrientationDetector();
        screenWidth= getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;
        getData();
        initViewStack();
        initView();
        initSDK();
        reSizePreview();
        msc = new MediaScannerConnection(this,null);
        msc.connect();
        copyAssets();
    }

    public static void startRecordForResult(Activity activity, int requestCode, AliyunSnapVideoParam param){
        Intent intent = new Intent(activity,AliyunVideoRecorder.class);
        intent.putExtra(AliyunSnapVideoParam.VIDEO_RESOLUTION,param.getResolutionMode());
        intent.putExtra(AliyunSnapVideoParam.VIDEO_RATIO,param.getRatioMode());
        intent.putExtra(AliyunSnapVideoParam.RECORD_MODE,param.getRecordMode());
        intent.putExtra(AliyunSnapVideoParam.FILTER_LIST,param.getFilterList());
        intent.putExtra(AliyunSnapVideoParam.BEAUTY_LEVEL,param.getBeautyLevel());
        intent.putExtra(AliyunSnapVideoParam.BEAUTY_STATUS,param.getBeautyStatus());
        intent.putExtra(AliyunSnapVideoParam.CAMERA_TYPE, param.getCameraType());
        intent.putExtra(AliyunSnapVideoParam.FLASH_TYPE, param.getFlashType());
        intent.putExtra(AliyunSnapVideoParam.NEED_CLIP,param.isNeedClip());
        intent.putExtra(AliyunSnapVideoParam.MAX_DURATION,param.getMaxDuration());
        intent.putExtra(AliyunSnapVideoParam.MIN_DURATION,param.getMinDuration());
        intent.putExtra(AliyunSnapVideoParam.VIDEO_QUALITY,param.getVideoQuality());
        intent.putExtra(AliyunSnapVideoParam.VIDEO_GOP,param.getGop());
        intent.putExtra(AliyunSnapVideoParam.SORT_MODE,param.getSortMode());


        intent.putExtra(AliyunSnapVideoParam.VIDEO_FRAMERATE,param.getFrameRate());
        intent.putExtra(AliyunSnapVideoParam.CROP_MODE, param.getScaleMode());
        intent.putExtra(AliyunSnapVideoParam.MIN_CROP_DURATION,param.getMinCropDuration());
        intent.putExtra(AliyunSnapVideoParam.MIN_VIDEO_DURATION,param.getMinVideoDuration());
        intent.putExtra(AliyunSnapVideoParam.MAX_VIDEO_DURATION,param.getMaxVideoDuration());
        intent.putExtra(AliyunSnapVideoParam.SORT_MODE, param.getSortMode());
        activity.startActivityForResult(intent,requestCode);
    }

    public static void startRecord(Context context, AliyunSnapVideoParam param){
        Intent intent = new Intent(context,AliyunVideoRecorder.class);
        intent.putExtra(AliyunSnapVideoParam.VIDEO_RESOLUTION,param.getResolutionMode());
        intent.putExtra(AliyunSnapVideoParam.VIDEO_RATIO,param.getRatioMode());
        intent.putExtra(AliyunSnapVideoParam.RECORD_MODE,param.getRecordMode());
        intent.putExtra(AliyunSnapVideoParam.FILTER_LIST,param.getFilterList());
        intent.putExtra(AliyunSnapVideoParam.BEAUTY_LEVEL,param.getBeautyLevel());
        intent.putExtra(AliyunSnapVideoParam.BEAUTY_STATUS,param.getBeautyStatus());
        intent.putExtra(AliyunSnapVideoParam.CAMERA_TYPE, param.getCameraType());
        intent.putExtra(AliyunSnapVideoParam.FLASH_TYPE, param.getFlashType());
        intent.putExtra(AliyunSnapVideoParam.NEED_CLIP,param.isNeedClip());
        intent.putExtra(AliyunSnapVideoParam.MAX_DURATION,param.getMaxDuration());
        intent.putExtra(AliyunSnapVideoParam.MIN_DURATION,param.getMinDuration());
        intent.putExtra(AliyunSnapVideoParam.VIDEO_QUALITY,param.getVideoQuality());
        intent.putExtra(AliyunSnapVideoParam.VIDEO_GOP,param.getGop());
        intent.putExtra(AliyunSnapVideoParam.SORT_MODE,param.getSortMode());


        intent.putExtra(AliyunSnapVideoParam.VIDEO_FRAMERATE,param.getFrameRate());
        intent.putExtra(AliyunSnapVideoParam.CROP_MODE, param.getScaleMode());
        intent.putExtra(AliyunSnapVideoParam.MIN_CROP_DURATION,param.getMinCropDuration());
        intent.putExtra(AliyunSnapVideoParam.MIN_VIDEO_DURATION,param.getMinVideoDuration());
        intent.putExtra(AliyunSnapVideoParam.MAX_VIDEO_DURATION,param.getMaxVideoDuration());
        intent.putExtra(AliyunSnapVideoParam.SORT_MODE, param.getSortMode());
        context.startActivity(intent);
    }

    public static String getVersion(){
        return Version.VERSION;
    }

    private void getStyleParam() {
        TypedArray a = obtainStyledAttributes(new int[]{
                R.attr.qusnap_tint_color, R.attr.qusnap_timeline_del_backgound_color,
                R.attr.qusnap_timeline_backgound_color, R.attr.qusnap_time_line_pos_y,
                R.attr.qusnap_switch_light_icon_disable, R.attr.qusnap_switch_light_icon});
        tintColor = a.getResourceId(0, R.color.aliyun_record_fill_progress);
        timelineDelBgColor = a.getResourceId(1, android.R.color.holo_red_dark);
       // timelineBgColor = a.getResourceId(2, R.color.aliyun_editor_overlay_line);
        timelineBgColor = a.getResourceId(2, R.color.shortmeet_editor_overlay_line);
        timelinePosY = (int) a.getDimension(3,100f);
        lightDisableRes = a.getResourceId(4, R.mipmap.icon_light_dis);
        lightSwitchRes = a.getResourceId(5, R.drawable.snap_switch_light_selector);
        a.recycle();
    }

    private void reSizePreview() {
        RelativeLayout.LayoutParams previewParams = null;
        RelativeLayout.LayoutParams timeLineParams = null;
        RelativeLayout.LayoutParams durationTxtParams = null;
        switch (mRatioMode) {
            case AliyunSnapVideoParam.RATIO_MODE_1_1:
                previewParams = new RelativeLayout.LayoutParams(screenWidth, screenWidth);
                previewParams.addRule(RelativeLayout.BELOW, R.id.aliyun_tools_bar);
                timeLineParams = new RelativeLayout.LayoutParams(screenWidth, TIMELINE_HEIGHT);
                timeLineParams.addRule(RelativeLayout.BELOW, R.id.aliyun_preview);
                durationTxtParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                durationTxtParams.addRule(RelativeLayout.ABOVE, R.id.aliyun_record_timeline);
                timeLineParams.topMargin = -timelinePosY;
                mToolBar.setBackgroundColor(getResources().getColor(R.color.aliyun_transparent));
                mRecorderBar.setBackgroundColor(getResources().getColor(R.color.aliyun_transparent));
                mRecordTimelineView.setColor(tintColor, timelineDelBgColor, R.color.qupai_black_opacity_70pct, timelineBgColor);
                break;
            case AliyunSnapVideoParam.RATIO_MODE_3_4:
                int barHeight = getVirtualBarHeight();
                float ratio = (float)screenHeight /screenWidth;
                previewParams = new RelativeLayout.LayoutParams(screenWidth, screenWidth * 4 / 3);
                if(barHeight > 0 || ratio < (16f / 9.2f)){
                    mToolBar.setBackgroundColor(getResources().getColor(R.color.aliyun_tools_bar_color));
                }else{
                    previewParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, R.id.aliyun_tools_bar);
                    mToolBar.setBackgroundColor(getResources().getColor(R.color.aliyun_transparent));
                }
                timeLineParams = new RelativeLayout.LayoutParams(screenWidth, TIMELINE_HEIGHT);
                timeLineParams.addRule(RelativeLayout.BELOW, R.id.aliyun_preview);
             /*   durationTxtParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                durationTxtParams.addRule(RelativeLayout.ABOVE, R.id.aliyun_record_timeline);*/
                timeLineParams.topMargin = -timelinePosY;
                mRecorderBar.setBackgroundColor(getResources().getColor(R.color.aliyun_transparent));

                RelativeLayout.LayoutParams frame = new RelativeLayout.LayoutParams(screenWidth,screenWidth * 2 / 3);
           //     frame.addRule(RelativeLayout.CENTER_VERTICAL,R.id.aliyun_record_layout);
                frame.addRule(RelativeLayout.BELOW,R.id.aliyun_record_timeline);
                mRecorderBar.setLayoutParams(frame);


                //mRecordTimelineView.setColor(tintColor, timelineDelBgColor, R.color.qupai_black_opacity_70pct, timelineBgColor);
                /**
                 * 第一个参数：进度条颜色
                 * 第二个参数： 删除时选中的颜色
                 * 第三个参数：断点颜色
                 * 第四个参数：背景颜色
                 */
                mRecordTimelineView.setColor(R.color.shortmeet_timeline_duration, R.color.shortmeet_timeline_select, R.color.shortmeet_editor_overlay_line, R.color.shortmeet_editor_overlay_line);
                break;
            case AliyunSnapVideoParam.RATIO_MODE_9_16:
                previewParams = new RelativeLayout.LayoutParams(screenWidth, screenWidth * 16 / 9);
                if (previewParams.height > screenHeight) {
                    previewParams.height = screenHeight;
                }
                timeLineParams = new RelativeLayout.LayoutParams(screenWidth, TIMELINE_HEIGHT);
                timeLineParams.addRule(RelativeLayout.ABOVE, R.id.aliyun_record_layout);
                timeLineParams.bottomMargin = timelinePosY;
                durationTxtParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                durationTxtParams.addRule(RelativeLayout.ABOVE, R.id.aliyun_record_timeline);
                mToolBar.setBackgroundColor(getResources().getColor(R.color.aliyun_tools_bar_color));
                mRecorderBar.setBackgroundColor(getResources().getColor(R.color.aliyun_tools_bar_color));
                mRecordTimelineView.setColor(tintColor, timelineDelBgColor, R.color.qupai_black_opacity_70pct, R.color.aliyun_qupai_transparent);
                break;
        }
        if (previewParams != null) {
            mGlSurfaceView.setLayoutParams(previewParams);
        }
        if (timeLineParams != null) {
            mRecordTimelineView.setLayoutParams(timeLineParams);
        }
        if(durationTxtParams != null){
          //  mRecordTimeTxt.setLayoutParams(durationTxtParams);
        }
    }

    private void initOrientationDetector() {
        orientationDetector = new OrientationDetector(getApplicationContext());
    }

    public int getVirtualBarHeight() {
        int vh = 0;
        WindowManager windowManager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            @SuppressWarnings("rawtypes")
            Class c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            vh = dm.heightPixels - windowManager.getDefaultDisplay().getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vh;
    }

//    private void toSourceType(){
//        if(BuildConfig.source_type.equals(SAAS_CLOSE_SOURCE)){
//            toEditor();
//        }else if(BuildConfig.source_type.equals(CUSTOM_OPEN_SOURCE)){
//            toEditor();
//        }
//    }

    private void reOpenCamera(int width, int height) {
        mRecorder.stopPreview();
        MediaInfo info = new MediaInfo();
        info.setVideoWidth(width);
        info.setVideoHeight(height);
        mRecorder.setMediaInfo(info);
        mRecorder.startPreview();
    }

    private void initView() {
        mGlSurfaceView = (AliyunSVideoGlSurfaceView) findViewById(R.id.aliyun_preview);
        mGlSurfaceView.setOnTouchListener(this);
        mSwitchRatioBtn = (ImageView) findViewById(R.id.aliyun_switch_ratio);//暂未使用，不知道功能
        mSwitchRatioBtn.setOnClickListener(this);
        mSwitchBeautyBtn = (ImageView) findViewById(R.id.aliyun_switch_beauty);//磨皮美化
        mSwitchBeautyBtn.setOnClickListener(this);
        mSwitchCameraBtn = (ImageView) findViewById(R.id.aliyun_switch_camera);//切换摄像头前后
        mSwitchCameraBtn.setOnClickListener(this);
        mSwitchLightBtn = (ImageView) findViewById(R.id.aliyun_switch_light);//闪光灯
        mSwitchLightBtn.setImageResource(lightDisableRes);
        mSwitchLightBtn.setOnClickListener(this);
        mBackBtn = (ImageView) findViewById(R.id.aliyun_back); //录制界面返回按钮
        mBackBtn.setOnClickListener(this);
        mRecordBtn = (RelativeLayout) findViewById(R.id.aliyun_record_btn);//录制按钮
        mRecordBtn.setOnTouchListener(this);
        mDeleteBtn = (ImageView) findViewById(R.id.aliyun_delete_btn);//录制暂停删除按钮
        mDeleteBtn.setOnClickListener(this);
        mCompleteBtn = (ImageView) findViewById(R.id.aliyun_complete_btn);//录制完成
        mCompleteBtn.setOnClickListener(this);
        mRecordTimelineView = (RecordTimelineView) findViewById(R.id.aliyun_record_timeline);
        mRecordTimelineView.setColor(tintColor, timelineDelBgColor, R.color.qupai_black_opacity_70pct, timelineBgColor);
        mRecordTimeTxt = (TextView) findViewById(R.id.aliyun_record_time);
     //   mGalleryBtn = (ImageView) findViewById(R.id.aliyun_icon_default);//点击x以后真正删除

        aliyun_import_editor= (ImageView) findViewById(R.id.aliyun_import_editor);//导入编辑
     //   aliyun_import_editor.setOnClickListener(this);

        aliyun_effect_filter = (ImageView) findViewById(R.id.aliyun_effect_filter);//滤镜
        aliyun_effect_filter.setOnClickListener(this);
        if(!isNeedGallery){
            aliyun_import_editor.setVisibility(View.GONE);
        }
        mToolBar = (FrameLayout) findViewById(R.id.aliyun_tools_bar);
        mRecorderBar = (FrameLayout) findViewById(R.id.aliyun_record_layout);
        mFilterTxt = (TextView) findViewById(R.id.aliyun_filter_txt);
        copy_res_tip= (FrameLayout) findViewById(R.id.copy_res_tip);
        mFilterTxt.setVisibility(View.GONE);
        aliyun_import_editor.setOnClickListener(this);
        scaleGestureDetector = new ScaleGestureDetector(this, this);
        gestureDetector = new GestureDetector(this, this);

        shortmeet_record_native= (ImageView) findViewById(R.id.shortmeet_record_native);
        shortmeet_record_circle= (ImageView) findViewById(R.id.shortmeet_record_circle);

    }

    private void initViewStack(){
        mEditorService = new EditorService();
        mViewStack = new ViewStack(this);
        mViewStack.setEditorService(mEditorService);
        mViewStack.setEffectChange(this);
        mViewStack.setBottomAnimation(this);
     //   mViewStack.setDialogButtonClickListener(mDialogButtonClickListener);
    }
    private void copyAssets() {
        //copy_res_tip.setVisibility(View.GONE);
        new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] params) {
                Common.copyAll(AliyunVideoRecorder.this, copy_res_tip);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                //                resCopy.setVisibility(View.GONE);
            }
        }.execute();
    }

  /*  private OnDialogButtonClickListener mDialogButtonClickListener = new OnDialogButtonClickListener() {
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
    };*/


    private void initSDK() {
        mRecorder = AliyunRecorderCreator.getRecorderInstance(this);
        mRecorder.setDisplayView(mGlSurfaceView);
        mRecorder.setOnFrameCallback(new OnFrameCallBack() {
            @Override
            public void onFrameBack(byte[] bytes, int width, int height, Camera.CameraInfo info) {
                isOpenFailed = false;
//                File file = new File("/sdcard/capture.yuv");
//
//                try {
//                    if(!file.exists()) {
//                        file.createNewFile();
//                    }
//                    BufferedOutputStream bo = new BufferedOutputStream(new FileOutputStream(file, true));
//                    bo.write(bytes);
//                    bo.flush();
//                    bo.close();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }

            @Override
            public Camera.Size onChoosePreviewSize(List<Camera.Size> supportedPreviewSizes, Camera.Size preferredPreviewSizeForVideo) {
                return null;
            }

            @Override
            public void openFailed() {
                isOpenFailed = true;
            }
        });
        mRecorder.setOnTextureIdCallback(new OnTextureIdCallBack() {
            @Override
            public int onTextureIdBack(int textureId, int textureWidth, int textureHeight, float[] matrix) {
                return textureId;
            }

            @Override
            public int onScaledIdBack(int scaledId, int textureWidth, int textureHeight, float[] matrix) {
                return scaledId;
            }
        });
        mClipManager = mRecorder.getClipManager();
        mClipManager.setMinDuration(minDuration);
        mClipManager.setMaxDuration(maxDuration);
        mRecordTimelineView.setMaxDuration(mClipManager.getMaxDuration());
        mRecordTimelineView.setMinDuration(mClipManager.getMinDuration());
        int[] resolution = getResolution();
        final MediaInfo info = new MediaInfo();
        info.setVideoWidth(resolution[0]);
        info.setVideoHeight(resolution[1]);
//        EncoderDebugger debugger = EncoderDebugger.debug(this, 528, 704);
        mRecorder.setMediaInfo(info);
        mCameraType = mRecorder.getCameraCount() == 1 ? CameraType.BACK : mCameraType;
        mRecorder.setCamera(mCameraType);
        mRecorder.setGop(gop);
        mRecorder.setVideoQuality(videoQuality);
        mRecorder.setRecordCallback(new RecordCallback() {
            @Override
            public void onComplete(boolean validClip, long clipDuration) {
                handleRecordCallback(validClip, clipDuration);
                if (isOnMaxDuration) {
                    isOnMaxDuration = false;
                    toEditor();
//                    toEditor();
//                    toSourceType();
                }
                if(!isNeedClip){
                    toEditor();
                }
            }

            @Override
            public void onFinish(String outputPath) {
                scanFile(outputPath);
                mClipManager.deleteAllPart();
                Intent intent = new Intent();
                intent.putExtra(OUTPUT_PATH,outputPath);
                intent.putExtra(RESULT_TYPE,RESULT_TYPE_RECORD);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }

            @Override
            public void onProgress(final long duration) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRecordTimelineView.setDuration((int) duration);
                        float time = (int) (mClipManager.getDuration() + duration) / 1000.0f;
                      //  int min = time / 60;
                       // int sec = time % 60;
                      //  mRecordTimeTxt.setText(String.format("%1$02d:%2$02d", min, sec));
                        mRecordTimeTxt.setText(String .format("%.1f",time)+" 秒");
                        if(mRecordTimeTxt.getVisibility() != View.VISIBLE){
                            mRecordTimeTxt.setVisibility(View.VISIBLE);
                        }
                    }
                });

            }

            @Override
            public void onMaxDuration() {
                isOnMaxDuration = true;
            }

            @Override
            public void onError(int errorCode) {
                isRecordError = true;
                handleRecordCallback(false, 0);
            }

            @Override
            public void onInitReady() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(filterList != null && filterList.length > filterIndex){
                            EffectFilter effectFilter = new EffectFilter(filterList[filterIndex]);
                            mRecorder.applyFilter(effectFilter);
                        }
                        if(isBeautyOn){
                            mRecorder.setBeautyLevel(mBeautyLevel);
                        }
                    }
                });
            }

            @Override
            public void onDrawReady() {

            }

            @Override
            public void onPictureBack(Bitmap bitmap) {

            }

            @Override
            public void onPictureDataBack(byte[] data) {

            }
        });

        setRecordMode(getIntent().getIntExtra(AliyunSnapVideoParam.RECORD_MODE, AliyunSnapVideoParam.RECORD_MODE_AUTO));
        setFilterList(getIntent().getStringArrayExtra(AliyunSnapVideoParam.FILTER_LIST));
        mBeautyLevel = getIntent().getIntExtra(AliyunSnapVideoParam.BEAUTY_LEVEL,80);
        setBeautyLevel(mBeautyLevel);
        setBeautyStatus(getIntent().getBooleanExtra(AliyunSnapVideoParam.BEAUTY_STATUS,true));
        setCameraType((CameraType) getIntent().getSerializableExtra(AliyunSnapVideoParam.CAMERA_TYPE));
        setFlashType((FlashType) getIntent().getSerializableExtra(AliyunSnapVideoParam.FLASH_TYPE));
        mRecorder.setExposureCompensationRatio(exposureCompensationRatio);
       // mRecorder.setFocusMode(CameraParam.FOCUS_MODE_CONTINUE);
        mRecorder.setFocusMode(CameraParam.FOCUS_MODE_CONTINUE);

    }

    private void scanFile(String path){
        msc.scanFile(path, "video/mp4");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MyGlSurfaceView", "onResume");
        /**
         * 部分android4.4机型会出现跳转Activity gl为空的问题，如果不需要适配，显示视图代码可以去掉
         */
        mGlSurfaceView.setVisibility(View.VISIBLE);
        mRecorder.startPreview();
        if (orientationDetector != null && orientationDetector.canDetectOrientation()) {
            orientationDetector.enable();
        }
      //  mRecorder.setFocusMode(CameraParam.FOCUS_MODE_CONTINUE);
        if (mCameraType==CameraType.BACK){
            mRecorder.setFocus((screenWidth/2)/1080, (screenHeight/2) / 1440);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isRecording){
            mRecorder.cancelRecording();
            isRecording = false;
        }
        mRecorder.stopPreview();

        /**
         * 部分android4.4机型会出现跳转Activity gl为空的问题，如果不需要适配，隐藏视图代码可以去掉
         */
        mGlSurfaceView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (orientationDetector != null) {
            orientationDetector.disable();
        }
    }

    private void getData() {
        //录制时 设置的分辨率是480   获取不到则默认540
        mResolutionMode = getIntent().getIntExtra(AliyunSnapVideoParam.VIDEO_RESOLUTION, AliyunSnapVideoParam.RESOLUTION_540P);
        minDuration = getIntent().getIntExtra(AliyunSnapVideoParam.MIN_DURATION, 2000);
        maxDuration = getIntent().getIntExtra(AliyunSnapVideoParam.MAX_DURATION, 30000);
        // Fly 注：录制时 设置视频比例 是3:4  获取不到 默认9:16
        mRatioMode = getIntent().getIntExtra(AliyunSnapVideoParam.VIDEO_RATIO, AliyunSnapVideoParam.RATIO_MODE_9_16);
        gop = getIntent().getIntExtra(AliyunSnapVideoParam.VIDEO_GOP, 5);
        videoQuality = (VideoQuality) getIntent().getSerializableExtra(AliyunSnapVideoParam.VIDEO_QUALITY);
        if(videoQuality == null){
            videoQuality = VideoQuality.HD;
        }
        isNeedClip = getIntent().getBooleanExtra(AliyunSnapVideoParam.NEED_CLIP,true);
        isNeedGallery = getIntent().getBooleanExtra(NEED_GALLERY,true);
        mVideoParam = new AliyunVideoParam.Builder()
                .gop(gop)
                .frameRate(25)
                .videoQuality(videoQuality)
                .build();

        /**
         * 裁剪参数
         */
        mFrame = getIntent().getIntExtra(AliyunSnapVideoParam.VIDEO_FRAMERATE,25);
        mCropMode = (ScaleMode) getIntent().getSerializableExtra(AliyunSnapVideoParam.CROP_MODE);
        if(mCropMode == null){
            mCropMode = ScaleMode.PS;
        }
        minCropDuration = getIntent().getIntExtra(AliyunSnapVideoParam.MIN_CROP_DURATION,2000);
        minVideoDuration = getIntent().getIntExtra(AliyunSnapVideoParam.MIN_VIDEO_DURATION,2000);
        maxVideoDuration = getIntent().getIntExtra(AliyunSnapVideoParam.MAX_VIDEO_DURATION,10000);
        mSortMode = getIntent().getIntExtra(AliyunSnapVideoParam.SORT_MODE, AliyunSnapVideoParam.SORT_MODE_MERGE);

    }

    public void setVideoParam(int resolutionMode, int minDuration, int maxDuration, int ratioMode, int gop, VideoQuality videoQuality) {
        mResolutionMode = resolutionMode;
        this.minDuration = minDuration;
        this.maxDuration = maxDuration;
        mRatioMode = ratioMode;
        this.gop = gop;
        this.videoQuality = videoQuality;
        mVideoParam = new AliyunVideoParam.Builder()
                .gop(gop)
                .frameRate(25)
                .videoQuality(videoQuality)
                .build();
    }

    public void setRecordMode(int recordMode) {
        this.recordMode = recordMode;
    }

    public void setFilterList(String[] filterList){
        this.filterList = filterList;
    }

    public void setBeautyStatus(boolean on){
        isBeautyOn = on;
        if(isBeautyOn){
            mSwitchBeautyBtn.setActivated(true);
        }else{
            mSwitchBeautyBtn.setActivated(false);
        }
        mRecorder.setBeautyStatus(on);
    }

    public void setBeautyLevel(int level){
        if(isBeautyOn){
            mRecorder.setBeautyLevel(level);
        }
    }

    public void setCameraType(CameraType cameraType){
        if(cameraType == null){
            return;
        }
        mRecorder.setCamera(cameraType);
        mCameraType = cameraType;
        if(mCameraType == CameraType.BACK){
            mSwitchCameraBtn.setActivated(false);
        }else if(mCameraType ==  CameraType.FRONT){
            mSwitchCameraBtn.setActivated(true);
        }
    }

    public void setFlashType(FlashType flashType){
        if(flashType == null){
            return;
        }
        if(mCameraType == CameraType.FRONT){
            mSwitchLightBtn.setEnabled(false);
            mSwitchLightBtn.setImageResource(lightDisableRes);
            return;
        }else if(mCameraType ==  CameraType.BACK){
            mSwitchLightBtn.setEnabled(true);
            mSwitchLightBtn.setImageResource(lightSwitchRes);
        }
        mFlashType = flashType;
        switch (mFlashType) {
            case AUTO:
                mSwitchLightBtn.setSelected(false);
                mSwitchLightBtn.setActivated(true);
                break;
            case ON:
                mSwitchLightBtn.setSelected(true);
                mSwitchLightBtn.setActivated(false);
                break;
            case OFF:
                mSwitchLightBtn.setSelected(true);
                mSwitchLightBtn.setActivated(true);
                break;
        }
        mRecorder.setLight(mFlashType);
    }

    private int[] getResolution() {
        int[] resolution = new int[2];
        int width = 0;
        int height = 0;
        switch (mResolutionMode) {
            case AliyunSnapVideoParam.RESOLUTION_360P:
                width = 360;
                break;
            case AliyunSnapVideoParam.RESOLUTION_480P:
                width = 480;
                break;
            case AliyunSnapVideoParam.RESOLUTION_540P:
                width = 540;
                break;
            case AliyunSnapVideoParam.RESOLUTION_720P:
                width = 720;
                break;
        }
        switch (mRatioMode) {
            case AliyunSnapVideoParam.RATIO_MODE_1_1:
                height = width;
                break;
            case AliyunSnapVideoParam.RATIO_MODE_3_4:
                height = width * 4 / 3;
                break;
            case AliyunSnapVideoParam.RATIO_MODE_9_16:
                height = width * 16 / 9;
                break;
        }
        resolution[0] = width;
        resolution[1] = height;
        return resolution;
    }

    @Override
    public void onBackPressed() {
        if(!isRecording){
            setResult(Activity.RESULT_CANCELED);
            finish();
        }
        if(mRecorder != null) {
            mRecorder.getClipManager().deleteAllPart();//直接返回则删除所有临时文件
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRecorder.destroy();
        msc.disconnect();
        if (orientationDetector != null) {
            orientationDetector.setOrientationChangedListener(null);
        }
        AliyunRecorderCreator.destroyRecorderInstance();
    }

    @Override
    public void onClick(View v) {
        if (v == mSwitchBeautyBtn) {
            if (isBeautyOn) {
                isBeautyOn = false;
                mSwitchBeautyBtn.setActivated(false);
            } else {
                isBeautyOn = true;
                mSwitchBeautyBtn.setActivated(true);
            }
            mRecorder.setBeautyStatus(isBeautyOn);
        } else if (v == mSwitchCameraBtn) {
            int type = mRecorder.switchCamera();
            if (type == CameraType.BACK.getType()) {
                mCameraType = CameraType.BACK;
                mSwitchLightBtn.setEnabled(true);
                mSwitchLightBtn.setImageResource(lightSwitchRes);
               // mSwitchCameraBtn.setActivated(false);
                AnimationUtils.rotateAnimation(mSwitchCameraBtn,0,90);
                setFlashType(mFlashType);

               // mRecorder.setFocusMode(CameraParam.FOCUS_MODE_CONTINUE);

                mRecorder.setFocus((screenWidth/2)/ mGlSurfaceView.getWidth(), (screenHeight/2) / mGlSurfaceView.getHeight());


           //     LogUtils.e("Tag","screemwidth---"+screenWidth+"screenHeight:"+screenHeight+"--surfaceViewWidth:"+ mGlSurfaceView.getWidth() +"----surfaceViewHeight-"+mGlSurfaceView.getHeight());

            } else if (type == CameraType.FRONT.getType()) {
                mCameraType = CameraType.FRONT;
                mSwitchLightBtn.setEnabled(false);
                mSwitchLightBtn.setImageResource(lightDisableRes);
               // mSwitchCameraBtn.setActivated(true);
                AnimationUtils.rotateAnimation(mSwitchCameraBtn,90,0);
            }
        } else if (v == mSwitchLightBtn) {
           /* if (mFlashType == FlashType.OFF) {
                mFlashType = FlashType.AUTO;
            } else if (mFlashType == FlashType.AUTO) {
                mFlashType = FlashType.ON;
            } else if (mFlashType == FlashType.ON || mFlashType ==  FlashType.TORCH) {
                mFlashType = FlashType.OFF;
            }*/
            if (mFlashType == FlashType.OFF) {
                mFlashType = FlashType.ON;
            } else if (mFlashType == FlashType.ON) {
                mFlashType = FlashType.OFF;
            }
            switch (mFlashType) {
               /* case AUTO:
                    v.setSelected(false);
                    v.setActivated(true);
                    break;*/
                case ON:
                    v.setSelected(true);
                    v.setActivated(false);
                    break;
                case OFF:
                    v.setSelected(true);
                    v.setActivated(true);
                    break;
            }
            mRecorder.setLight(mFlashType);
        } else if (v == mSwitchRatioBtn) {
//            mRatioMode++;
//            if (mRatioMode > RATIO_MODE_9_16) {
//                mRatioMode = RATIO_MODE_3_4;
//            }
//            reSizePreview();
//            int[] resolution = getResolution();
//            reOpenCamera(resolution[0], resolution[1]);
        } else if (v == mBackBtn) {
            onBackPressed();
        } else if (v == mCompleteBtn) {
//            mRecorder.finishRecording();
//            mClipManager.deleteAllPart();
            if (mClipManager.getDuration() >= mClipManager.getMinDuration()) {
                toEditor();
//                toEditor();
//                toSourceType();
            }
        } else if (v == mDeleteBtn) {
            if (!isSelected) {
                mRecordTimelineView.selectLast();
                mDeleteBtn.setActivated(true);
                isSelected = true;
            } else {
                mRecordTimelineView.deleteLast();
                mDeleteBtn.setActivated(false);
                mClipManager.deletePart();
                isSelected = false;
                showComplete();
                if (mClipManager.getDuration() == 0) {
                    if(isNeedGallery){
                        aliyun_import_editor.setVisibility(View.VISIBLE);
                    }
                    mSwitchRatioBtn.setEnabled(true);
                    mCompleteBtn.setVisibility(View.GONE);
                    mDeleteBtn.setVisibility(View.GONE);
                }
            }
        } /*else if (v == aliyun_import_editor) {
            *//*Class crop = null;
            try {
                crop = Class.forName("com.shortmeet.www.zTakePai.crop.MediaActivity");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if(crop == null){
                Toast.makeText(this,R.string.aliyun_no_import_moudle,Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this,crop);
            intent.putExtra(AliyunSnapVideoParam.VIDEO_RESOLUTION,mResolutionMode);
            intent.putExtra(AliyunSnapVideoParam.VIDEO_RATIO,mRatioMode);
            intent.putExtra(AliyunSnapVideoParam.NEED_RECORD,false);
            intent.putExtra(AliyunSnapVideoParam.VIDEO_QUALITY,videoQuality);
            intent.putExtra(AliyunSnapVideoParam.VIDEO_GOP,gop);
            intent.putExtra(AliyunSnapVideoParam.VIDEO_FRAMERATE,mFrame);
            intent.putExtra(AliyunSnapVideoParam.CROP_MODE,mCropMode);
            intent.putExtra(AliyunSnapVideoParam.MIN_CROP_DURATION,minCropDuration);
            intent.putExtra(AliyunSnapVideoParam.MIN_VIDEO_DURATION,minVideoDuration);
            intent.putExtra(AliyunSnapVideoParam.MAX_VIDEO_DURATION,maxVideoDuration);
            intent.putExtra(AliyunSnapVideoParam.SORT_MODE, mSortMode);
            startActivityForResult(intent,REQUEST_CROP);*//*
        }*/else if(v==aliyun_import_editor){
            Class impo = null;
            try {
                impo = Class.forName("com.shortmeet.www.zTakePai.impo.MediaActivity");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if(impo == null){
                Toast.makeText(this,R.string.aliyun_no_import_moudle,Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this,impo);
            intent.putExtra(CropKey.VIDEO_RATIO, CropKey.RATIO_MODE_3_4);
            intent.putExtra(CropKey.VIDEO_SCALE, CropKey.SCALE_CROP);
            intent.putExtra(CropKey.VIDEO_QUALITY , VideoQuality.HD);
            intent.putExtra(CropKey.VIDEO_FRAMERATE,25);
            intent.putExtra(CropKey.VIDEO_GOP,125);
            startActivity(intent);
        }else if(v==aliyun_effect_filter){
            mViewStack.setActiveIndex(0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CROP && resultCode == RESULT_OK){
            data.putExtra(RESULT_TYPE,RESULT_TYPE_CROP);
            setResult(Activity.RESULT_OK,data);
            finish();
        }
    }

    private int getPictureRotation() {
        int orientation = orientationDetector.getOrientation();
        int rotation = 90;
        if ((orientation >= 45) && (orientation < 135)) {
            rotation = 180;
        }
        if ((orientation >= 135) && (orientation < 225)) {
            rotation = 270;
        }
        if ((orientation >= 225) && (orientation < 315)) {
            rotation = 0;
        }
        if (mCameraType == CameraType.FRONT) {
            if (rotation != 0) {
                rotation = 360 - rotation;
            }
        }
        Log.d("MyOrientationDetector", "generated rotation ..." + rotation);
        return rotation;
    }

    private void toEditor() {
        //file:///data/user/0/com.example.user.mytwo/files/project_json/20170916T231916.459%2B0800/project.json
        Uri projectUri = mRecorder.finishRecordingForEdit();
        AliyunIClipManager mClipManager = mRecorder.getClipManager();
        List<String> tempFileList = mClipManager.getVideoPathList();
        Class editor = null;
        try {
            editor = Class.forName("com.shortmeet.www.zTakePai.editt.editor.EditorActivity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(editor == null){
            Toast.makeText(this, "无法到达编辑界面", Toast.LENGTH_SHORT).show();
          //  toEditor();
            return;
        }
        Intent intent = new Intent(this, EditorActivity.class);
        int[] resolutions = getResolution();
        mVideoParam.setScaleMode(ScaleMode.LB);
        mVideoParam.setOutputWidth(resolutions[0]);
        mVideoParam.setOutputHeight(resolutions[1]);
        intent.putExtra("video_param", mVideoParam);
        intent.putExtra("project_json_path", projectUri.getPath());
        intent.putStringArrayListExtra("temp_file_list", (ArrayList<String>) tempFileList);
        LogUtils.e("toEditor","-----"+projectUri.getPath()+"----");
        try{
           startActivity(intent);
           // this.finish();
        }catch (ActivityNotFoundException e){
            toEditor();
        }
    }

    private void toStitch(){
        mRecorder.finishRecording();
        AliyunIClipManager mClipManager = mRecorder.getClipManager();
        mClipManager.deleteAllPart();//删除所有的临时文件
    }

    private void startRecording() {
        String videoPath = Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DCIM + File.separator + System.currentTimeMillis() + ".mp4";
        mRecorder.setOutputPath(videoPath);
         ///storage/emulated/0/DCIM/1505574651178.mp4
        handleRecordStart();
        mRecorder.setRotation(getPictureRotation());
        isRecordError = false;
        mRecorder.startRecording();
        if (mFlashType == FlashType.ON && mCameraType == CameraType.BACK) {
            mRecorder.setLight(FlashType.TORCH);
        }
    }

    private void stopRecording() {
        mRecorder.stopRecording();
        handleRecordStop();
    }

    private boolean checkIfStartRecording(){
        if (mRecordBtn.isActivated()) {
            return false;
        }
        if (CommonUtil.SDFreeSize() < 50 * 1000 * 1000) {
            Toast.makeText(this, R.string.aliyun_no_free_memory, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showFilter(String name) {
        if (name == null || name.isEmpty()) {
            name = getString(R.string.aliyun_filter_null);
        }
        mFilterTxt.animate().cancel();
        mFilterTxt.setText(name);
        mFilterTxt.setVisibility(View.VISIBLE);
        mFilterTxt.setAlpha(FADE_IN_START_ALPHA);
        txtFadeIn();
    }

    private void txtFadeIn() {
        mFilterTxt.animate().alpha(1).setDuration(FILTER_ANIMATION_DURATION / 2).setListener(
                new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        txtFadeOut();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();
    }

    private void txtFadeOut() {
        mFilterTxt.animate().alpha(0).setDuration(FILTER_ANIMATION_DURATION / 2).start();
        mFilterTxt.animate().setListener(null);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == mRecordBtn) {
            if (isOpenFailed) {
                Toast.makeText(this, R.string.aliyun_camera_permission_tip, Toast.LENGTH_SHORT).show();
                return true;
            }
            if (recordMode == AliyunSnapVideoParam.RECORD_MODE_TOUCH) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (!isRecording) {
                        if(!checkIfStartRecording()){
                            return false;
                        }
                        mRecordBtn.setHovered(true);
                        startRecording();
                        isRecording = true;
                    } else {
                        stopRecording();
                        isRecording = false;
                    }
                }
            } else if (recordMode == AliyunSnapVideoParam.RECORD_MODE_PRESS) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(!checkIfStartRecording()){
                        return false;
                    }
                    mRecordBtn.setSelected(true);
                    startRecording();
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    stopRecording();
                }
            } else if (recordMode == AliyunSnapVideoParam.RECORD_MODE_AUTO) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    downTime = System.currentTimeMillis();
                    if(!isRecording){
                        if(!checkIfStartRecording()){
                            return false;
                        }
                        mRecordBtn.setPressed(true);
                        startRecording();
                        mRecordBtn.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(mRecordBtn.isPressed()){
                                  /*  mRecordBtn.setSelected(true);
                                    mRecordBtn.setHovered(false);*/
                                    AnimationUtils.scaleAnimation(shortmeet_record_native,false);
                                    AnimationUtils.alphAnimation(shortmeet_record_circle,false);
                                }
                            }
                        },200);
                        isRecording = true;
                        isResume=true;
                    }else {
                        stopRecording();
                        isRecording = false;
                    }
                }else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL){
                    long timeOffset = System.currentTimeMillis() - downTime;
                    mRecordBtn.setPressed(false);
                    if(timeOffset > 1000){
                        LogUtils.e("mRecordBtn","long pressed");
                        stopRecording();
                        isRecording = false;
                    }else{
                        if(!isRecordError){
                         /*   mRecordBtn.setSelected(false);
                            mRecordBtn.setHovered(true);*/
                            if(isResume){
                                shortmeet_record_native.setImageResource(R.mipmap.shortmeet_record_pause);
                                AnimationUtils.scaleAnimation(shortmeet_record_native,false);
                                AnimationUtils.alphAnimation(shortmeet_record_circle,false);
                                isResume=false;
                            }
                        }else{
                            isRecording = false;
                        }
                    }
                }
            }
        } else if (v.equals(mGlSurfaceView)) {
            if (event.getPointerCount() >= 2) {
                scaleGestureDetector.onTouchEvent(event);
            } else if (event.getPointerCount() == 1) {
                gestureDetector.onTouchEvent(event);
            }


        }
        return true;
    }

    private void handleRecordStart() {
        mRecordBtn.setActivated(true);
        aliyun_import_editor.setVisibility(View.GONE);
        mCompleteBtn.setVisibility(View.VISIBLE);
        mDeleteBtn.setVisibility(View.VISIBLE);
        mSwitchRatioBtn.setEnabled(false);
       // mSwitchBeautyBtn.setEnabled(false);
        mSwitchCameraBtn.setEnabled(false);
        mSwitchLightBtn.setEnabled(false);
        mCompleteBtn.setEnabled(false);
        aliyun_effect_filter.setEnabled(false);

        mDeleteBtn.setEnabled(false);
        mDeleteBtn.setActivated(false);
        isSelected = false;
    }

    private void handleRecordStop() {
        if (mFlashType == FlashType.ON && mCameraType == CameraType.BACK) {
            mRecorder.setLight(FlashType.OFF);
        }
    }


    private void showComplete(){
        if(mClipManager.getDuration() > mClipManager.getMinDuration()){
            mCompleteBtn.setActivated(true);
        }else{
            mCompleteBtn.setActivated(false);
        }
    }

    private void handleRecordCallback(final boolean validClip, final long clipDuration) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRecordBtn.setActivated(false);
            /*    mRecordBtn.setHovered(false);
                mRecordBtn.setSelected(false);*/

                shortmeet_record_native.setImageResource(R.mipmap.shortmeet_record_native);
                AnimationUtils.scaleAnimation(shortmeet_record_native,true);
                AnimationUtils.alphAnimation(shortmeet_record_circle,true);

                if (validClip) {
                    mRecordTimelineView.setDuration((int) clipDuration);
                    mRecordTimelineView.clipComplete();
                }else {
                    mRecordTimelineView.setDuration(0);
                }
                Log.e("validClip","validClip : "+validClip);
                mRecordTimeTxt.setVisibility(View.GONE);
                mSwitchBeautyBtn.setEnabled(true);
                mSwitchCameraBtn.setEnabled(true);
                mSwitchLightBtn.setEnabled(true);
                mCompleteBtn.setEnabled(true);
                mDeleteBtn.setEnabled(true);

                aliyun_effect_filter.setEnabled(true);
                showComplete();
            }
        });
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float factorOffset = detector.getScaleFactor() - lastScaleFactor;
        scaleFactor += factorOffset;
        lastScaleFactor = detector.getScaleFactor();
        if (scaleFactor < 0) {
            scaleFactor = 0;
        }
        if (scaleFactor > 1) {
            scaleFactor = 1;
        }
        mRecorder.setZoom(scaleFactor);
        return false;

    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        lastScaleFactor = detector.getScaleFactor();
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();

        mRecorder.setFocus(x / mGlSurfaceView.getWidth(), y / mGlSurfaceView.getHeight());
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if(Math.abs(distanceX) > 20){
            return false;
        }
        exposureCompensationRatio += (distanceY / mGlSurfaceView.getHeight());
        if (exposureCompensationRatio > 1) {
            exposureCompensationRatio = 1;
        }
        if (exposureCompensationRatio < 0) {
            exposureCompensationRatio = 0;
        }
        mRecorder.setExposureCompensationRatio(exposureCompensationRatio);
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if(filterList == null || filterList.length == 0){
            return true;
        }
        if(mRecordBtn.isActivated()){
            return true;
        }
        if (velocityX > MAX_SWITCH_VELOCITY) {
            filterIndex++;
            if (filterIndex >= filterList.length) {
                filterIndex = 0;
            }
        } else if (velocityX < -MAX_SWITCH_VELOCITY) {
            filterIndex--;
            if (filterIndex < 0) {
                filterIndex = filterList.length - 1;
            }
        } else {
            return true;
        }
        EffectFilter effectFilter = new EffectFilter(filterList[filterIndex]);
        mRecorder.applyFilter(effectFilter);
        showFilter(effectFilter.getName());
        return false;
    }


    /**
     * 更改编辑
     * @param effectInfo
     */
    @Override
    public void onEffectChange(EffectInfo effectInfo) {
      /*  EffectBean effect = new EffectBean();
        effect.setId(effectInfo.id);
        effect.setPath(effectInfo.getPath());
        */
        EffectFilter effectFilter = new EffectFilter(effectInfo.getPath());
        mRecorder.applyFilter(effectFilter);
    }

    /**
     * 显示滤镜view
     */
    @Override
    public void showBottomView() {

    }

    /**
     * 隐藏滤镜view
     */
    @Override
    public void hideBottomView() {

    }
}
