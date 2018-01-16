package com.example.sj.app2.seekbar;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;

import com.example.sj.app2.LogUtil;
import com.example.sj.app2.R;

public class SJSeekbar extends android.support.v7.widget.AppCompatSeekBar {

    public static final int TEXTSTYLE_NOAMAL = 0;
    public static final int TEXTSTYLE_BOLD = 1;
    /**
     * 起始位置文字大小
     */
    private float startTextSize;
    /**
     * 起始位置文字颜色
     */
    private int startTextColor;
    /**
     * 起始位置字体style
     */
    private int startTextStyle;
    /**
     * 结束位置文字大小
     */
    private float endTextSize;
    /**
     * 结束位置文字颜色
     */
    private int endTextColor;
    /**
     * 结束位置字体style
     */
    private int endTextStyle;
    /**
     * 指示器位置文字大小
     */
    private float indicatorTextSize;
    /**
     * 指示器位置文字颜色
     */
    private int indicatorTextColor;
    /**
     * 指示器位置字体style
     */
    private int indicatorTextStyle;

    /**
     * 开始文本的画笔
     */
    private Paint startTextPaint;

    /**
     * 结束文本的画笔
     */
    private Paint endTextPaint;

    /**
     * 指示器文本的画笔
     */
    private Paint indicatorTextPaint;

    /**
     * 指示器位置  上/下
     */
    private int indicatorLocation;

    public static final int INDICATOR_LOCATION_TOP = 3;
    public static final int INDICATOR_LOCATION_BOTTOM = 4;
    /**
     * 控件宽度
     */
    private int width;
    /**
     * indicator bitmap
     */
    private Bitmap indicatorBitmap;
    /**
     * 控件高度
     */
    private int height;
    /**
     * 进度条的宽度 <控件减去 padding>
     */
    private int progressWidth;


    public SJSeekbar(Context context) {
        super(context);
    }

    private static float dpToPx(Context context, int dp) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    public SJSeekbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SJSeekbar);

        startTextSize = typedArray.getDimension(R.styleable.SJSeekbar_startTextSize, dpToPx(context, 12));
        startTextColor = typedArray.getColor(R.styleable.SJSeekbar_startTextColor, Color.BLACK);
        startTextStyle = typedArray.getInt(R.styleable.SJSeekbar_startTextStyle, TEXTSTYLE_NOAMAL);
        endTextSize = typedArray.getDimension(R.styleable.SJSeekbar_endTextSize, dpToPx(context, 12));
        endTextColor = typedArray.getColor(R.styleable.SJSeekbar_startTextSize, Color.BLACK);
        endTextStyle = typedArray.getInt(R.styleable.SJSeekbar_endTextStyle, TEXTSTYLE_NOAMAL);
        indicatorTextSize = typedArray.getDimension(R.styleable.SJSeekbar_indicatorTextSize, dpToPx(context, 12));
        indicatorTextColor = typedArray.getColor(R.styleable.SJSeekbar_indicatorTextColor, Color.BLACK);
        indicatorTextStyle = typedArray.getInt(R.styleable.SJSeekbar_indicatorTextStyle, TEXTSTYLE_NOAMAL);
        indicatorBitmap = BitmapFactory.decodeResource(getResources(), typedArray.getResourceId(R.styleable.SJSeekbar_indicatorDrawable, R.drawable.seekbar_indicator));

        indicatorLocation = typedArray.getInt(R.styleable.SJSeekbar_indicator_location, INDICATOR_LOCATION_TOP);
        typedArray.recycle();
        setPadding((int) (indicatorTextSize * 1), (int) (indicatorTextSize * 2), (int) (indicatorTextSize * 1), (int) (indicatorTextSize * 2));

        if (startTextPaint == null) {
            startTextPaint = new Paint();
        }
        startTextPaint.setColor(startTextColor);
        startTextPaint.setTextSize(startTextSize);
        startTextPaint.setAntiAlias(true);
        if (endTextPaint == null) {
            endTextPaint = new Paint();
        }
        endTextPaint.setColor(endTextColor);
        endTextPaint.setTextSize(endTextSize);
        startTextPaint.setAntiAlias(true);
        if (indicatorTextPaint == null) {
            indicatorTextPaint = new Paint();
        }
        indicatorTextPaint.setColor(indicatorTextColor);
        indicatorTextPaint.setTextSize(indicatorTextSize);
        indicatorTextPaint.setAntiAlias(true);
    }

    public SJSeekbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private String startText = String.valueOf(1);
    private String endText = String.valueOf(100);
    private String indicatorText = String.valueOf(1);
    private float startTextX = 0;
    private float startTextY = 0;
    private float endTextX = 0;
    private float endTextY = 0;
    private float indicatorTextX = 0, indicatorTextY = 0;
    private float indicatorBitmapRotateDegree;
    private float indicatorBitmapY;
    private int progressLocationX;


    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (width == 0) {
            width = getMeasuredWidth();
            progressWidth = width - getPaddingRight() - getPaddingLeft();
            endTextX = width - (indicatorTextSize * 2);
        }
        if (height == 0) {

            height = getHeight();
        }
        int progress = getProgress();
        int max = getMax();
        if (progress == 0) {
            progress = 1;
        }
        progressLocationX = progressWidth * progress / max;
        indicatorText = String.valueOf(progress);
        startTextX = indicatorTextSize / 2;
        endTextX = width - indicatorTextSize * 2;

        if (indicatorLocation == INDICATOR_LOCATION_TOP) {
            startTextY = endTextY = indicatorTextY = (float) (indicatorTextSize * 1.5);
            indicatorBitmapRotateDegree = 180;
            indicatorBitmapY = height - (indicatorTextSize * 2);
        } else {
            startTextY = endTextY = indicatorTextY  = height - (indicatorTextSize );
            indicatorBitmapRotateDegree = 0;
            indicatorBitmapY = (float) (indicatorTextSize/2);
        }


        RectF locationRectF = new RectF(progressLocationX, indicatorBitmapY, (float) (progressLocationX + (indicatorTextSize * 1.5)), (float) (indicatorBitmapY + (indicatorTextSize * 1.5)));
        canvas.drawText(startText, startTextX, startTextY, startTextPaint);
        canvas.drawText(endText, endTextX, endTextY, endTextPaint);
        canvas.drawText(indicatorText, progressLocationX, indicatorTextY, indicatorTextPaint);

        Matrix matrix1 = new Matrix();
        matrix1.postRotate(indicatorBitmapRotateDegree);
        Bitmap rotateBitmap = Bitmap.createBitmap(indicatorBitmap, 0, 0, indicatorBitmap.getWidth(), indicatorBitmap.getHeight(), matrix1, true);
        Rect bitmapRect = new Rect(0, 0, rotateBitmap.getWidth(), rotateBitmap.getHeight());
        canvas.drawBitmap(rotateBitmap, bitmapRect, locationRectF, null);
    }



    



}
