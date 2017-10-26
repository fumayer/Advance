package com.quduquxie.creation.write.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 * Created on 16/11/21.
 * Created by crazylei.
 */

public class CustomHorizontalScrollView extends HorizontalScrollView {

    private HorizontalScrollListener horizontalScrollListener = null;

    public CustomHorizontalScrollView(Context context) {
        super(context);
    }

    public CustomHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onScrollChanged(int x, int y, int old_x, int old_y) {
        super.onScrollChanged(x, y, old_x, old_y);
        if (horizontalScrollListener != null) {
            horizontalScrollListener.onScrollChanged(this, x, y, old_x, old_y);
        }
    }

    public void setHorizontalScrollListener(HorizontalScrollListener horizontalScrollListener) {
        this.horizontalScrollListener = horizontalScrollListener;
    }

    public interface HorizontalScrollListener {
        void onScrollChanged(CustomHorizontalScrollView customHorizontalScrollView, int x, int y, int old_x, int old_y);
    }
}
