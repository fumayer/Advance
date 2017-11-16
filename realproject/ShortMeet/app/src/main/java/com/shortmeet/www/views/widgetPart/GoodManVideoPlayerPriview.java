package com.shortmeet.www.views.widgetPart;

import android.content.Context;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.shortmeet.www.R;

import cn.jzvd.JZMediaManager;
import cn.jzvd.JZUtils;
import cn.jzvd.JZVideoPlayerManager;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by SHM on 2017/01/09.
 */
public class GoodManVideoPlayerPriview extends JZVideoPlayerStandard {
    private Context mcontext;
    //静音模式  默认为false
    private boolean isSilencePattern = false;
    public GoodManVideoPlayerPriview(Context context) {
        this(context, null);
    }

    public GoodManVideoPlayerPriview(Context context, AttributeSet attrs) {
        super(context, attrs);
        mcontext=context;
    }

    @Override
    public void init(Context context) {
        super.init(context);
        startButton.setImageResource(R.drawable.btn_play_s);
        batteryTimeLayout.setVisibility(GONE);
    }


    @Override
    public void updateStartImage() {
        if (currentState == CURRENT_STATE_PLAYING) {
            startButton.setImageResource(R.drawable.jz_click_pause_selector);
        } else if (currentState == CURRENT_STATE_ERROR) {
            startButton.setImageResource(R.drawable.jz_click_error_selector);
        } else {
            startButton.setImageResource(R.drawable.btn_play_s);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == cn.jzvd.R.id.fullscreen) {
            if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
                //click quit fullscreen
            } else {
                //click goto fullscreen
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return super.onTouch(v, event);
    }

    /**
     * onPrepared
     */
    @Override
    public void onVideoRendingStart() {
        super.onVideoRendingStart();
    }

    @Override
    public void onStateNormal() {
        super.onStateNormal();
    }

    @Override
    public void onStatePreparing() {
        super.onStatePreparing();
    }

    @Override
    public void onStatePause() {
        super.onStatePause();
    }

    @Override
    public void onStateError() {
        super.onStateError();
    }

    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
    }

    @Override
    public void onInfo(int what, int extra) {
        super.onInfo(what, extra);
    }

    @Override
    public void onError(int what, int extra) {
        super.onError(what, extra);
    }

    @Override
    public void startWindowTiny() {
        super.startWindowTiny();
    }
    @Override
    public void startVideo() {
      //  super.startVideo();
        JZVideoPlayerManager.completeAll();
        JZUtils.saveProgress(getContext(), JZUtils.getCurrentUrlFromMap(urlMap, currentUrlMapIndex), 0);//不保存进度
        Log.d(TAG, "startVideo [" + this.hashCode() + "] ");
        initTextureView();
        addTextureView();
        AudioManager mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        JZUtils.scanForActivity(getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        JZMediaManager.CURRENT_PLAYING_URL = JZUtils.getCurrentUrlFromMap(urlMap, currentUrlMapIndex);
        JZMediaManager.CURRENT_PLING_LOOP = loop;
        JZMediaManager.MAP_HEADER_DATA = headData;
        onStatePreparing();
        JZVideoPlayerManager.setFirstFloor(this);
        if (isSilencePattern) {
            setAudioFocus(false);
        }
    }
    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
        //在列表 或者 普通模式下，可以根据实际需求改变
        if (isSilencePattern && (currentScreen == SCREEN_LAYOUT_NORMAL ||
                currentScreen == SCREEN_LAYOUT_LIST)) {
            setVolume(true);
        }
    }
    /**
     * 进入全屏
     */
    @Override
    public void startWindowFullscreen() {
        super.startWindowFullscreen();
        if (isSilencePattern) {
            setAudioFocus(true);
            setVolume(false);
        }
    }
//    /**
//     * 退出全屏
//     */
//    @Override
//    public void playOnThisJzvd() {
//        super.playOnThisJzvd();
//        if (isSilencePattern) {
//            setAudioFocus(false);
//            setVolume(true);
//        }
//    }
  //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


    /**
     * 设置静音模式
     * @param isSilencePattern
     */
    public void setSilencePattern(boolean isSilencePattern) {
        this.isSilencePattern = isSilencePattern;
    }
    /**
     * 设置音量
     *
     * @param isSilence
     */
    public void setVolume(boolean isSilence) {
        if (isSilence) {//静音
            JZMediaManager.instance().mediaPlayer.setVolume(0f, 0f);
        } else {
            JZMediaManager.instance().mediaPlayer.setVolume(1f, 1f);
        }
    }

    @Override
    public void onAutoCompletion() {
        super.onAutoCompletion();
    }

    /**
     * 请求或者释放焦点
     * @param focus
     */
    public void setAudioFocus(boolean focus) {
        AudioManager mAudioManager = (AudioManager)
                getContext().getSystemService(Context.AUDIO_SERVICE);
        if (focus) {//请求音频焦点
            mAudioManager.requestAudioFocus(onAudioFocusChangeListener,
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        } else {//释放
            mAudioManager.abandonAudioFocus(onAudioFocusChangeListener);
        }
    }
    }
