package com.quduquxie.read.page;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CustomPage extends View {

    private Bitmap bitmap;
    private Paint paint;

    public CustomPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    public void drawPage(Bitmap bitmap) {
        this.bitmap = bitmap;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, paint);
    }

}
