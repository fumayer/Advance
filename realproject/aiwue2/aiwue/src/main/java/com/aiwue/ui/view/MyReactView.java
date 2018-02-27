package com.aiwue.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/4/27.
 */

public class MyReactView extends View{
    private Paint mPaint;

    public MyReactView(Context context) {
        super(context);
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
    }

    public MyReactView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(0, 20);
        path.lineTo(5, 20);
        path.lineTo(5, 0);
        path.close();
        canvas.drawPath(path,mPaint);

    }
}
