package com.quduquxie.read;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

public class SourcePageView extends View{
	private Paint mPaint;
	private float unit;
	public SourcePageView(Context context) {
		this(context,null);
	}
	public SourcePageView(Context context, AttributeSet attrs) {
		this(context,attrs,0);
	}
	public SourcePageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		DisplayMetrics dm = getResources().getDisplayMetrics();
		unit = dm.scaledDensity;
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
//		mPaint.setStyle(Paint.Style.STROKE);
//		mPaint.setStrokeWidth(unit);
//		float y = 10 * unit;
//		canvas.drawCircle(y, y, 8 * unit, mPaint);
		mPaint.setStyle(Paint.Style.FILL);
//		mPaint.setTextSize(16 * unit);
//		canvas.drawText("!", 7.5f * unit, (16)*unit, mPaint);
		mPaint.setTextSize(12*unit);
		mPaint.setStrokeWidth(0);
		canvas.drawText("原网页", 22 * unit, (15)*unit, mPaint);
		
	}
	public void setColor(int color) {
		mPaint.setColor(color);
		invalidate();
	}
}
