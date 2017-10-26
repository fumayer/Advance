package com.quduquxie.read.animation;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.quduquxie.read.ReadStatus;

public class SlideAnimationProvider extends AnimationProvider {

	protected float moveX, moveY;
	private int speed = 20;
	protected Paint paint;
	private boolean isCanDoStep = false;
//	private int v;

	public SlideAnimationProvider(BitmapManager manager, ReadStatus readStatus) {
		super(manager, readStatus);
		init();
	}

	@Override
	public void drawInternal(Canvas canvas) {
		doStep();
		if (moveX > 0) {
			canvas.drawBitmap(mNextPageBitmap, moveX - mWidth, 0, paint);

		} else {
			canvas.drawBitmap(mNextPageBitmap, moveX + mWidth, 0, paint);
		}

		canvas.drawBitmap(mCurPageBitmap, moveX, 0, paint);

	}

	protected void init() {
		speed = mWidth / 6;
//		v = mWidth / 8;

		paint = new Paint();
		paint.setFilterBitmap(true);
		paint.setDither(true);
//		paint.setAntiAlias(true);

	}

	@Override
	public boolean moveEvent(MotionEvent event) {
		moveX = event.getX() - mTouch.x;
		moveY = event.getY() - mTouch.y;
		// mTouch.x = event.getX();
		// mTouch.y = event.getY();

		return true;
	}

	protected void doStep() {
		if (!isCanDoStep) {
			return;
		}
		
		if (moveToLeft) {
			moveX -= speed;
		} else {
			moveX += speed;
		}

		pageView.postInvalidate();
		if (moveToLeftUp != moveToLeft) {
			if (moveX <= 0 || moveX >= 0) {
				moveX = 0;// currentbitmap归位
				isCanDoStep = false;
				finishAnimation();
			}
		} else {
			if (moveX <= -mWidth || moveX >= mWidth) {
				moveX = 0;// currentbitmap归位
				isCanDoStep = false;
				finishAnimation();
			}
		}

	}

	boolean moveToLeft;
	boolean moveToLeftUp;

	@Override
	public void setTouchStartPosition(int startX, int startY, boolean moveToLeft) {
		mTouch.x = this.startX = startX;
		mTouch.y = this.startY = startY;
		this.moveToLeft = moveToLeft;
	}

	@Override
	public void startTurnAnimation(boolean moveToLeft) {
		this.moveToLeftUp = moveToLeft;
		isCanDoStep = true;
		pageView.invalidate();
	}

	@Override
	public void finishAnimation() {
		if (pageView != null) {
			pageView.onAnimationFinish();
		}
	}

	@Override
	public void upEvent() {
		// TODO Auto-generated method stub
		
	}

}
