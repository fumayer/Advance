package com.aiwue.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import com.aiwue.R;

/**
 * Created by Administrator on 2017/2/10 0010.
 */

public class EasyJCVideoPlayer extends JCVideoPlayerStandard {

    private LinearLayout llDuration;
    private TextView tvDuration;

    public EasyJCVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EasyJCVideoPlayer(Context context) {
        super(context);
    }

    @Override
    public void init(Context context) {
        super.init(context);
        llDuration = (LinearLayout) findViewById(R.id.llDuration);
        tvDuration = (TextView) findViewById(R.id.tvDuration);
    }

    @Override
    public void setUiWitStateAndScreen(int state) {
        super.setUiWitStateAndScreen(state);
        switch (currentState) {
            case CURRENT_STATE_PREPARING:
                //隐藏时长
                llDuration.setVisibility(View.GONE);
                break;
             case CURRENT_STATE_AUTO_COMPLETE:
             case CURRENT_STATE_ERROR:
                //显示时长
                llDuration.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onCompletion() {
        super.onCompletion();
        //显示时长
        llDuration.setVisibility(View.VISIBLE);
    }

    public void setDurationText(String text)
    {
        tvDuration.setText(text);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_easy_video_player;
    }
}
