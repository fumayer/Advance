package com.quduquxie.read.animation;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;

import com.quduquxie.read.ReadStatus;


public class ShiftAnimationProvider extends SlideAnimationProvider {

	private GradientDrawable mFolderShadowDrawableLR;
	private GradientDrawable mFolderShadowDrawableRL;

	public ShiftAnimationProvider(BitmapManager manager, ReadStatus readStatus) {
		super(manager, readStatus);
	}
    private static  int shadow_width = 16;
	@Override
	public void drawInternal(Canvas canvas) {
		if (readPosition != 0) {
			canvas.drawBitmap(mNextPageBitmap, 0, 0, paint);
			canvas.save();  
			canvas.clipRect(0, readPosition, readStatus.screen_width, readStatus.screen_height);
			canvas.drawBitmap(mCurPageBitmap, 0, 0, paint);
			if (null != dividerBmp) {
				Rect src = new Rect();
				Rect dsf = new Rect();
				src.left = 0;
				src.right = dividerBmp.getWidth();
				src.top = 0;
				src.bottom = dividerBmp.getHeight();
				dsf.left = 0;
				dsf.right = readStatus.screen_width;
				dsf.top = readPosition;
				dsf.bottom = readPosition + dividerBmp.getHeight();
				canvas.drawBitmap(dividerBmp, src, dsf, paint);
			}
			canvas.restore();  
		}else {
			canvas.drawBitmap(mNextPageBitmap, 0, 0, paint);
//			doStep();
			if (moveX > 0) {
				if (moveX <= 0) {
					canvas.drawBitmap(mCurPageBitmap, moveX, 0, paint);
				}else {
					canvas.drawBitmap(mCurPageBitmap, moveX + shadow_width, 0, paint);
				}
				mFolderShadowDrawableLR.setBounds((int) moveX, 0, (int) moveX + shadow_width, mHeight);
				mFolderShadowDrawableLR.draw(canvas);
			} else {
				if (moveX >= 0) {
					canvas.drawBitmap(mCurPageBitmap, moveX, 0, paint);
				}else {
					canvas.drawBitmap(mCurPageBitmap, new Rect(0, 0, mWidth, mHeight),
							new Rect((int)moveX - shadow_width, 0, mWidth + (int)moveX - shadow_width, mHeight), paint);
					mFolderShadowDrawableRL.setBounds(mWidth + (int) moveX - shadow_width, 0, mWidth + (int) moveX , mHeight);
					mFolderShadowDrawableRL.draw(canvas);
				}
				
			}
		}
		
	}

	@Override
	protected void init() {
		super.init();

        int[] color = { 0x333333, 0x80333333 };
		mFolderShadowDrawableRL = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, color);
		mFolderShadowDrawableRL.setGradientType(GradientDrawable.LINEAR_GRADIENT);// linear_gradient
																					// 线性梯度
		mFolderShadowDrawableLR = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, color);
		mFolderShadowDrawableLR.setGradientType(GradientDrawable.LINEAR_GRADIENT);
	}
}
