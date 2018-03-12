package com.example.sj.app2.arc;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.example.sj.app2.R;

/**
 * Created by sunjie on 2017/12/9.
 */

public class ArcTopImageView extends android.support.v7.widget.AppCompatImageView {
    /*
     *弧形高度
     */
    private int mArcHeight;
    private static final String TAG = "ArcImageView";

    public ArcTopImageView(Context context) {
        this(context, null);
    }

    public ArcTopImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcTopImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArcTopImageView);
        mArcHeight = typedArray.getDimensionPixelSize(R.styleable.ArcImageView_arcHeight, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Path path = new Path();
//        path.moveTo(0, 0);
//        path.lineTo(0, getHeight());
//        path.quadTo(getWidth() / 2, getHeight() - 2 * mArcHeight, getWidth(), getHeight());
//        path.lineTo(getWidth(), 0);

        path.moveTo(0, mArcHeight);
        path.quadTo(getWidth()/2,-mArcHeight,getWidth(),mArcHeight);
        path.lineTo(getWidth(),getHeight());
        path.lineTo(0,getHeight());
        path.close();
        canvas.clipPath(path);
        super.onDraw(canvas);
    }

}