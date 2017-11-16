package com.shortmeet.www.views.refreshPart;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.shortmeet.www.R;
/*
 *  Fly 注：
 */
public class ShortMeRefreshView extends RelativeLayout implements SwipeTrigger, SwipeRefreshTrigger {
    private ImageView ivRefresh;
    private AnimationDrawable mAnimDrawable;
    public ShortMeRefreshView(Context context) {
        super(context);
    }

    public ShortMeRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShortMeRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ivRefresh = (ImageView) findViewById(R.id.refresh_img);
        mAnimDrawable = (AnimationDrawable) ivRefresh.getBackground();
    }

    @Override
    public void onRefresh() {
        if (!mAnimDrawable.isRunning()) {
            mAnimDrawable.start();
        }
    }

    @Override
    public void onPrepare() {

    }

    @Override
    public void onMove(int i, boolean b, boolean b1) {

    }

    @Override
    public void onRelease() {
        if (!mAnimDrawable.isRunning()) {
            mAnimDrawable.start();
        }
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onReset() {
        mAnimDrawable.stop();
    }
}
