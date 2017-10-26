package com.quduquxie.communal.widget.more;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class LoadMorePromptView extends View {
    private Paint paint;
    private Path path;
    private int width;
    private int height;
    private float fraction = 0;
    private float translation;
    private float lineXLeft;

    public LoadMorePromptView(Context context) {
        this(context, null);
    }

    public LoadMorePromptView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#FFFFFF"));

        path = new Path();
    }

    @Override
    protected void onSizeChanged(int width, int height, int old_width, int old_height) {
        super.onSizeChanged(width, height, old_width, old_height);
        this.width = width;
        this.height = height;
        lineXLeft = width;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        lineXLeft = width + translation;

        lineXLeft = lineXLeft > width / 4 * 3 ? width + translation : width / 4 * 3;

        float lineXLeftControl = lineXLeft - fraction * width / 2;

        path.reset();
        path.moveTo(width, 0);
        path.lineTo(width, height);
        path.lineTo(lineXLeft, height);
        path.quadTo(lineXLeftControl, height / 2, lineXLeft, 0);
        path.close();

        canvas.drawPath(path, paint);
    }

    public void setFraction(float fraction, float translation) {
        this.fraction = fraction;
        this.translation = translation;
        invalidate();
    }


    public void setColor(@ColorInt int color) {
        paint.setColor(color);
    }
}
