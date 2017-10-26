package com.quduquxie.communal.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.quduquxie.R;

public class GlideRoundTransform extends BitmapTransformation {

    private static float radius = 6f;

    public GlideRoundTransform(Context context) {
        super(context);
        radius = context.getResources().getDimension(R.dimen.radius_6);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return roundCrop(pool, toTransform);
    }

    private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();


        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);

        RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());

        canvas.drawRoundRect(rectF, radius, radius, paint);

        return result;
    }

    @Override
    public String getId() {
        return getClass().getName() + Math.round(radius);
    }
}