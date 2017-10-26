package com.quduquxie.communal.widget.expression;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.style.DynamicDrawableSpan;

import java.lang.ref.WeakReference;

public class ExpressionSpan extends DynamicDrawableSpan {

    private final Context context;

    private final int resourceId;

    private int viewWidth;

    private int viewHeight;

    private final int viewSize;

    private final int textSize;

    private int draw_top;

    private Drawable drawable;

    private WeakReference<Drawable> drawableReference;

    public ExpressionSpan(Context context, int resourceId, int size, int alignment, int textSize) {
        super(alignment);
        this.context = context;
        this.resourceId = resourceId;
        viewWidth = viewHeight = viewSize = size;
        this.textSize = textSize;
    }

    public Drawable getDrawable() {
        if (drawable == null) {
            try {
                drawable = context.getResources().getDrawable(resourceId);
                viewHeight = viewSize;
                viewWidth = viewHeight * drawable.getIntrinsicWidth() / drawable.getIntrinsicHeight();
                draw_top = (textSize - viewHeight) / 2;
                drawable.setBounds(0, draw_top, viewWidth, draw_top + viewHeight);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return drawable;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        Drawable drawable = getCachedDrawable();
        canvas.save();

        int transY = bottom - drawable.getBounds().bottom;
        if (mVerticalAlignment == ALIGN_BASELINE) {
            transY = top + ((bottom - top) / 2) - ((drawable.getBounds().bottom - drawable.getBounds().top) / 2) - draw_top;
        }
        canvas.translate(x, transY);
        drawable.draw(canvas);
        canvas.restore();
    }

    private Drawable getCachedDrawable() {
        if (drawableReference == null || drawableReference.get() == null) {
            drawableReference = new WeakReference<>(getDrawable());
        }
        return drawableReference.get();
    }
}