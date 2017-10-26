package com.quduquxie.communal.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

public class GlideCircleTransform extends BitmapTransformation {

    private Paint border_Paint;

    public GlideCircleTransform(Context context) {
        super(context);

        border_Paint = new Paint();
        border_Paint.setDither(true);
        border_Paint.setAntiAlias(true);
        border_Paint.setColor(Color.parseColor("#DCDCDC"));
        border_Paint.setStyle(Paint.Style.STROKE);
        border_Paint.setStrokeWidth(1f);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform);
    }

    private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        if (source == null) {
            return null;
        }

        int size = Math.min(source.getWidth(), source.getHeight());

        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);

        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);

        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);

        float radius = size / 2f;

        canvas.drawCircle(radius, radius, radius, paint);

        if (border_Paint != null) {
            float borderRadius = radius - 1f;
            canvas.drawCircle(radius, radius, borderRadius, border_Paint);
        }

        return result;
    }

    @Override
    public String getId() {
        return getClass().getName();
    }
}
