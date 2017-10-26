package com.quduquxie.communal.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * Created on 17/1/10.
 * Created by crazylei.
 */

public class CustomWebView extends WebView {

    private int motionEventY;
    private int touchStartY;
    private boolean slide_down = false;

    private OnScrollChangedCallback scrollChangedCallback;

    public CustomWebView(Context context) {
        super(context);
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStartY = this.getScrollY();
                motionEventY = this.getScrollY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (this.getScrollY() - touchStartY > 5) {
                    slide_down = false;
                    scrollChangedCallback.showBottomView(false);
                } else if (touchStartY - this.getScrollY() > 50) {
                    slide_down = true;
                }
                this.motionEventY = this.getScrollY();
                break;
            case MotionEvent.ACTION_UP:
                if (slide_down && (motionEventY - this.getScrollY() > 10)) {
                    scrollChangedCallback.showBottomView(true);
                }
                break;
        }

        return super.onTouchEvent(motionEvent);
    }

    @Override
    protected void onScrollChanged(int x, int y, int old_x, int old_y) {
        super.onScrollChanged(x, y, old_x, old_y);
    }

    public void setOnScrollChangedCallback(final OnScrollChangedCallback scrollChangedCallback) {
        this.scrollChangedCallback = scrollChangedCallback;
    }

    public interface OnScrollChangedCallback {

        void showBottomView(boolean show);
    }
}
