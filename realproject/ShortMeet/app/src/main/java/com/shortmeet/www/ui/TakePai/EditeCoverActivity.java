package com.shortmeet.www.ui.TakePai;

import android.app.Activity;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.quview.HorizontalListView;
import com.aliyun.quview.SizeChangedNotifier;
import com.aliyun.quview.VideoCropCoverView;
import com.aliyun.quview.VideoTrimFrameLayout;
import com.shortmeet.www.Api.ApiConstant;
import com.shortmeet.www.Base.BaseActivity;
import com.shortmeet.www.R;
import com.shortmeet.www.mvp.IMVPView;
import com.shortmeet.www.utilsUsed.FileUtil;
import com.shortmeet.www.zTakePai.crop.media.EditorCoverAdapter;
import com.shortmeet.www.zTakePai.crop.media.FrameExtractor10;
import com.shortmeet.www.zTakePai.crop.media.FrameExtractor11;
import com.shortmeet.www.zTakePai.crop.media.ShareableBitmap;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class EditeCoverActivity extends BaseActivity implements IMVPView, SizeChangedNotifier.Listener, VideoTrimFrameLayout.OnVideoScrollCallBack, TextureView.SurfaceTextureListener, VideoCropCoverView.SeekBarChangeListener, HorizontalListView.OnScrollCallBack, View.OnClickListener {
    private String outpath ;
    private long duration;
    private FrameExtractor10 kFrame;
    private FrameExtractor11 coverFrame;
    private VideoTrimFrameLayout frame;
    private int screenWidth;
    private TextureView textureview;
    private VideoCropCoverView aliyun_seek_bar;
    private HorizontalListView aliyun_video_tailor_image_list;
    private EditorCoverAdapter adapter;
    private MediaPlayer mPlayer;
    private Surface mSurface;
    private long mStartTime;
    private long videoPos;
    private long lastVideoSeekTime;
    private long mEndTime;
    private int maxDuration = Integer.MAX_VALUE;
    private ImageView iv_left;
    private TextView tv_center;
    private TextView tv_right;

    @Override
    public int setRootView() {
        return R.layout.activity_editor_cover;
    }

    @Override
    public void initView() {
        outpath=this.getIntent().getStringExtra("path");
        duration=this.getIntent().getLongExtra("duration",0);
        kFrame = new FrameExtractor10();
        kFrame.setDataSource(outpath);
        coverFrame=new FrameExtractor11();
        coverFrame.setDataSource(outpath);
        aliyun_seek_bar = (VideoCropCoverView) findViewById(R.id.aliyun_seek_bar);
        aliyun_seek_bar.setSeekBarChangeListener(this);
        aliyun_seek_bar.setProgressMinDiff(0);
        aliyun_video_tailor_image_list = (HorizontalListView) findViewById(R.id.aliyun_video_tailor_image_list);
        aliyun_video_tailor_image_list.setOnScrollCallBack(this);
        iv_left= (ImageView) findViewById(R.id.iv_left);
        iv_left.setImageResource(R.mipmap.shortmeet_icon_back_po);
        iv_left.setOnClickListener(this);
        tv_right= (TextView) findViewById(R.id.tv_right);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText(getString(R.string.shortmeet_crop_cover_complete));
        tv_right.setOnClickListener(this);
        tv_center= (TextView) findViewById(R.id.tv_center);
        tv_center.setText(getString(R.string.shortmeet_crop_cover));
        if (duration>0){
            adapter = new EditorCoverAdapter(this, duration, maxDuration, kFrame, aliyun_seek_bar);
            aliyun_video_tailor_image_list.setAdapter(adapter);
        }
        initSurface();
        setListViewHeight();
    }
    private void setListViewHeight() {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) aliyun_video_tailor_image_list.getLayoutParams();
        layoutParams.height = screenWidth / 8;
        aliyun_video_tailor_image_list.setLayoutParams(layoutParams);
      //  aliyun_seek_bar.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, screenWidth / 8));
    }
    public void initSurface() {
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        frame = (VideoTrimFrameLayout) findViewById(R.id.aliyun_video_surfaceLayout);
        frame.setOnSizeChangedListener(this);
        frame.setOnScrollCallBack(this);
        resizeFrame();
        textureview = (TextureView) findViewById(R.id.aliyun_video_textureview);
        textureview.setSurfaceTextureListener(this);
    }

    private void resizeFrame() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) frame.getLayoutParams();
        layoutParams.width = (int) (screenWidth*0.9);
        layoutParams.height = (int) (screenWidth *0.9* 4 / 3);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL,R.id.root_cover);
        frame.setLayoutParams(layoutParams);
    }
    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void setData(Object o, int id) {

    }



    //setOnSizeChangedListener
    @Override
    public void onSizeChanged(View view, int w, int h, int oldw, int oldh) {

    }


    //setOnScrollCallBack
    @Override
    public void onVideoScroll(float distanceX, float distanceY) {

    }
    //setOnScrollCallBack
    @Override
    public void onVideoSingleTapUp() {

    }


    //texture
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        if (mPlayer == null) {
            mSurface = new Surface(surface);
            mPlayer = new MediaPlayer();
            mPlayer.setSurface(mSurface);
            try {
                mPlayer.setDataSource(outpath);
                mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        playVideo();
                    }
                });
                mPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }



    //seekbar监听
    long seekPos = 0;
    @Override
    public void SeekBarValueChanged(float leftThumb, float rightThumb, int whitchSide) {
        if (whitchSide == 0) {
            seekPos = (long) (duration * leftThumb / 100);
            mStartTime = seekPos;
        } else if (whitchSide == 1) {
            seekPos = (long) (duration * rightThumb / 100);
            mEndTime = seekPos;
        }
        //dirationTxt.setText((float) (mEndTime - mStartTime) / 1000 + "");
        mPlayer.seekTo((int) seekPos);


    }

    @Override
    public void onSeekStart() {

    }

    @Override
    public void onSeekEnd() {

    }


    //aliyun_video_tailor_image_list
    @Override
    public void onScrollDistance(Long count, int distanceX) {

    }
    private void playVideo() {
        if (mPlayer == null) {
            return;
        }
        mPlayer.seekTo((int) mStartTime);
        //-----------------------------------------------------------------------------------------------------------------------------------------------
        mPlayer.start();
        mPlayer.pause();
        //-----------------------------------------------------------------------------------------------------------------------------------------------
        videoPos = mStartTime;
        lastVideoSeekTime = System.currentTimeMillis();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_left:
                onBackPressed();
                break;
            case R.id.tv_right:
                //seekPos
                coverFrame.newTask(new FrameExtractor11.Callback() {
                    @Override
                    public void onFrameExtracted(ShareableBitmap bitmap, long timestamp) {
                        Intent intent = new Intent();
                        long data =System.currentTimeMillis();
                        FileUtil.createFileDir(ApiConstant.COMPOSE_PATH_COVER);
                        FileUtil.saveBitmap(bitmap.getData(), ApiConstant.COMPOSE_PATH_COVER+data+".png");
                        intent.putExtra("coverpath",ApiConstant.COMPOSE_PATH_COVER+data+".png");
                        setResult(Activity.RESULT_OK,intent);
                        finish();
                    }
                }, TimeUnit.SECONDS.toNanos(seekPos/1000));
                break;
        }
    }
}
