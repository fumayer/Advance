package com.example.sj.app2;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by sunjie on 2018/3/5.
 */

public class SJText extends android.support.v7.widget.AppCompatTextView {

    private Drawable drawable;

    public SJText(Context context) {
        super(context);
    }

    public SJText(Context context, AttributeSet attrs) {
        super(context, attrs);
        getBackgroundDrawable();
    }

    private void getBackgroundDrawable() {
        drawable = getBackground();

    }

    public SJText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtil.e("SJText","39-----onTouchEvent--->"+event.getAction());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (drawable != null) {
                    drawable.setColorFilter(Color.GRAY, PorterDuff.Mode.DARKEN);
                    LogUtil.e("SJText","43-----onTouchEvent--->"+1);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (drawable != null) {
                    drawable.clearColorFilter();
                    LogUtil.e("SJText","50-----onTouchEvent--->"+2);
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }
}
