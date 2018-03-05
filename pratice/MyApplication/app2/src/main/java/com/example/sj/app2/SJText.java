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
        getBackgroundDrawable();

    }

    public SJText(Context context, AttributeSet attrs) {
        super(context, attrs);
        getBackgroundDrawable();
    }

    private void getBackgroundDrawable() {
        setClickable(true);
        drawable = getBackground();

    }

    public SJText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getBackgroundDrawable();

    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (drawable != null) {

            if (enabled) {
                drawable.clearColorFilter();
            } else {
                drawable.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);


            }
        }
        LogUtil.e("ColorFilterActivity", "45-----setEnabled--->" + enabled);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtil.e("ColorFilterActivity", "39-----onTouchEvent--->" + event.getAction());

        if (!isEnabled()) {
//            drawable.setColorFilter(Color.GRAY, PorterDuff.Mode.DARKEN);
            LogUtil.e("SJText", "54-----onTouchEvent---返回>" + false);
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (drawable != null) {
                    drawable.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                    LogUtil.e("SJText", "43-----onTouchEvent--->" + 1);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (drawable != null) {
                    drawable.clearColorFilter();
                    LogUtil.e("SJText", "50-----onTouchEvent--->" + 2);
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }
}
