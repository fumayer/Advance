package com.example.sj.app2;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * Created by sunjie on 2018/3/5.
 */

public class CommonItem extends RelativeLayout {
    public CommonItem(Context context) {
        super(context);

    }

    public CommonItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initAttrs(context,attrs);
    }

    public CommonItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(Context context) {

    }
    private void initAttrs(Context context, AttributeSet attrs) {

    }




}
