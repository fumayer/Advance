package com.quduquxie.read.animation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Region;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;

import com.quduquxie.read.ReadStatus;
import com.quduquxie.util.QGLog;

/**
 * 仿真翻页模式
 **/
public class CurlAnimationProvider extends AnimationProvider {

	private static final String TAG = CurlAnimationProvider.class.getSimpleName();

	private PointF mBezierStart1 = new PointF(); // 贝塞尔曲线起始点
	private PointF mBezierControl1 = new PointF(); // 贝塞尔曲线控制点
	private PointF mBeziervertex1 = new PointF(); // 贝塞尔曲线顶点
	private PointF mBezierEnd1 = new PointF(); // 贝塞尔曲线结束点

	private PointF mBezierStart2 = new PointF(); // 另一条贝塞尔曲线
	private PointF mBezierControl2 = new PointF();
	private PointF mBeziervertex2 = new PointF();
	private PointF mBezierEnd2 = new PointF();

	private GradientDrawable mBackShadowDrawableLR;
	private GradientDrawable mBackShadowDrawableRL;
	private GradientDrawable mFolderShadowDrawableLR;
	private GradientDrawable mFolderShadowDrawableRL;

	private GradientDrawable mFrontShadowDrawableHBT;
	private GradientDrawable mFrontShadowDrawableHTB;
	private GradientDrawable mFrontShadowDrawableVLR;
	private GradientDrawable mFrontShadowDrawableVRL;

	private float mMiddleX;
	private float mMiddleY;
	private float mDegrees;
	private float mTouchToCornerDis;
	private ColorMatrixColorFilter mColorMatrixFilter;
	private Matrix mMatrix;
	private float[] mMatrixArray = { 0, 0, 0, 0, 0, 0, 0, 0, 1.0f };
	private int[] mBackShadowColors;
	private int[] mFrontShadowColors;

	private boolean mIsRTandLB; // 是否属于右上左下

	private int mCornerX = 0; // 拖拽点对应的页脚
	private int mCornerY = 0;
	private Path mPath0;
	private Path mPath1;

	private float startX, startY;

	private boolean isFromCenter = false;// 记录手势是否从中间开始滑动
//	private boolean isRightMoveToRight;
//	private boolean isLeftMoveToLeft;
	private float mMaxLength;
	private boolean turnToLeft;
	private Paint mPaint;
	private static final int kDefaultAniDuration = 600;
	
	public CurlAnimationProvider(BitmapManager manager, ReadStatus readStatus) {
		super(manager, readStatus);

		init();
	}

	private void init() {
		mPath0 = new Path();
		mPath1 = new Path();
		mPaint = new Paint();

		mMaxLength = (float) Math.hypot(mWidth, mHeight);

		ColorMatrix cm = new ColorMatrix();
		float array[] = { 0.55f, 0, 0, 0, 80.0f, 0, 0.55f, 0, 0, 80.0f, 0, 0, 0.55f, 0, 80.0f, 0, 0, 0, 0.2f, 0 };
		cm.set(array);
		mColorMatrixFilter = new ColorMatrixColorFilter(cm);
		mMatrix = new Matrix();

		mTouch.x = 0.01f; // 不让x,y为0,否则在点计算时会有问题
		mTouch.y = 0.01f;

		createDrawable();
	}

	@Override
	public void drawInternal(Canvas canvas) {
		if (isFromCenter) {
			mTouch.y = mHeight - 1;
		}
		canvas.drawColor(backColor);
		calcPoints();
		drawCurrentPageArea(canvas, mCurPageBitmap, mPath0);
		drawNextPageAreaAndShadow(canvas, mNextPageBitmap);
		drawCurrentPageShadow(canvas);
		drawCurrentBackArea(canvas, mCurPageBitmap);
	}

	/**
	 * 创建阴影的GradientDrawable
	 */
	private void createDrawable() {
		int[] color = { 0x333333, 0xb0333333 };
		mFolderShadowDrawableRL = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, color);
		mFolderShadowDrawableRL.setGradientType(GradientDrawable.LINEAR_GRADIENT);// linear_gradient
																					// 线性梯度

		mFolderShadowDrawableLR = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, color);
		mFolderShadowDrawableLR.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mBackShadowColors = new int[] { 0xff111111, 0x111111 };
		mBackShadowDrawableRL = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, mBackShadowColors);
		mBackShadowDrawableRL.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mBackShadowDrawableLR = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, mBackShadowColors);
		mBackShadowDrawableLR.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mFrontShadowColors = new int[] { 0x80111111, 0x111111 };
		mFrontShadowDrawableVLR = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, mFrontShadowColors);
		mFrontShadowDrawableVLR.setGradientType(GradientDrawable.LINEAR_GRADIENT);
		mFrontShadowDrawableVRL = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, mFrontShadowColors);
		mFrontShadowDrawableVRL.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mFrontShadowDrawableHTB = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, mFrontShadowColors);
		mFrontShadowDrawableHTB.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mFrontShadowDrawableHBT = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, mFrontShadowColors);
		mFrontShadowDrawableHBT.setGradientType(GradientDrawable.LINEAR_GRADIENT);
	}

	private void calcPoints() {
		mMiddleX = (mTouch.x + mCornerX) / 2;
		mMiddleY = (mTouch.y + mCornerY) / 2;
		mBezierControl1.x = mMiddleX - (mCornerY - mMiddleY)
				* (mCornerY - mMiddleY) / (mCornerX - mMiddleX);
		mBezierControl1.y = mCornerY;
		mBezierControl2.x = mCornerX;
		mBezierControl2.y = mMiddleY - (mCornerX - mMiddleX)
				* (mCornerX - mMiddleX) / (mCornerY - mMiddleY);

		// QGLog.e("hmg", "mTouchX  " + mTouch.x + "  mTouchY  " + mTouch.y);
		// QGLog.e("hmg", "mBezierControl1.x  " + mBezierControl1.x
		// + "  mBezierControl1.y  " + mBezierControl1.y);
		// QGLog.e("hmg", "mBezierControl2.x  " + mBezierControl2.x
		// + "  mBezierControl2.y  " + mBezierControl2.y);

		mBezierStart1.x = mBezierControl1.x - (mCornerX - mBezierControl1.x)
				/ 2;
		mBezierStart1.y = mCornerY;

		// 当mBezierStart1.x < 0或者mBezierStart1.x > mWidth时
		// 如果继续翻页，会出现BUG故在此限制
		if (mTouch.x > 0 && mTouch.x < mWidth) {
			if (mBezierStart1.x < 0 || mBezierStart1.x > mWidth) {
				if (mBezierStart1.x < 0)
					mBezierStart1.x = mWidth - mBezierStart1.x;

				float f1 = Math.abs(mCornerX - mTouch.x);
				float f2 = mWidth * f1 / mBezierStart1.x;
				mTouch.x = Math.abs(mCornerX - f2);

				float f3 = Math.abs(mCornerX - mTouch.x)
						* Math.abs(mCornerY - mTouch.y) / f1;
				mTouch.y = Math.abs(mCornerY - f3);

				mMiddleX = (mTouch.x + mCornerX) / 2;
				mMiddleY = (mTouch.y + mCornerY) / 2;

				mBezierControl1.x = mMiddleX - (mCornerY - mMiddleY)
						* (mCornerY - mMiddleY) / (mCornerX - mMiddleX);
				mBezierControl1.y = mCornerY;

				mBezierControl2.x = mCornerX;
				mBezierControl2.y = mMiddleY - (mCornerX - mMiddleX)
						* (mCornerX - mMiddleX) / (mCornerY - mMiddleY);
				// QGLog.e("hmg", "mTouchX --> " + mTouch.x + "  mTouchY-->  "
				// + mTouch.y);
				// QGLog.e("hmg", "mBezierControl1.x--  " + mBezierControl1.x
				// + "  mBezierControl1.y -- " + mBezierControl1.y);
				// QGLog.e("hmg", "mBezierControl2.x -- " + mBezierControl2.x
				// + "  mBezierControl2.y -- " + mBezierControl2.y);
				mBezierStart1.x = mBezierControl1.x
						- (mCornerX - mBezierControl1.x) / 2;
			}
		}
		mBezierStart2.x = mCornerX;
		mBezierStart2.y = mBezierControl2.y - (mCornerY - mBezierControl2.y)
				/ 2;

		mTouchToCornerDis = (float) Math.hypot((mTouch.x - mCornerX),
				(mTouch.y - mCornerY));

		mBezierEnd1 = getCross(mTouch, mBezierControl1, mBezierStart1,
				mBezierStart2);
		mBezierEnd2 = getCross(mTouch, mBezierControl2, mBezierStart1,
				mBezierStart2);

		// QGLog.e("hmg", "mBezierEnd1.x  " + mBezierEnd1.x + "  mBezierEnd1.y  "
		// + mBezierEnd1.y);
		// QGLog.e("hmg", "mBezierEnd2.x  " + mBezierEnd2.x + "  mBezierEnd2.y  "
		// + mBezierEnd2.y);

		/*
		 * mBeziervertex1.x 推导
		 * ((mBezierStart1.x+mBezierEnd1.x)/2+mBezierControl1.x)/2 化简等价于
		 * (mBezierStart1.x+ 2*mBezierControl1.x+mBezierEnd1.x) / 4
		 */
		mBeziervertex1.x = (mBezierStart1.x + 2 * mBezierControl1.x + mBezierEnd1.x) / 4;
		mBeziervertex1.y = (2 * mBezierControl1.y + mBezierStart1.y + mBezierEnd1.y) / 4;
		mBeziervertex2.x = (mBezierStart2.x + 2 * mBezierControl2.x + mBezierEnd2.x) / 4;
		mBeziervertex2.y = (2 * mBezierControl2.y + mBezierStart2.y + mBezierEnd2.y) / 4;
	}

	private void drawCurrentPageArea(Canvas canvas, Bitmap bitmap, Path path) {
		mPath0.reset();
		if (isFromCenter) {
			float startX = mTouch.x > 0 ? mTouch.x : 0;
			mPath0.moveTo(startX, 0);
			mPath0.lineTo(mWidth, 0);
			mPath0.lineTo(mWidth, mHeight);
			mPath0.lineTo(startX, mHeight);
		} else {
			mPath0.moveTo(mBezierStart1.x, mBezierStart1.y);
			mPath0.quadTo(mBezierControl1.x, mBezierControl1.y, mBezierEnd1.x, mBezierEnd1.y);
			mPath0.lineTo(mTouch.x, mTouch.y);
			mPath0.lineTo(mBezierEnd2.x, mBezierEnd2.y);
			mPath0.quadTo(mBezierControl2.x, mBezierControl2.y, mBezierStart2.x, mBezierStart2.y);
			mPath0.lineTo(mCornerX, mCornerY);
		}
		
		mPath0.close();

		canvas.save();
		if (mBezierStart1.x > -mWidth / 2 && mBezierStart1.x < (3 * mWidth / 2 - 1)) {
			canvas.clipPath(path, Region.Op.XOR);
		}

		canvas.drawBitmap(bitmap, 0, 0, null);
		canvas.restore();
	}

	/**
	 * 绘制翻起页的阴影
	 */

	public void drawCurrentPageShadow(Canvas canvas) {
		if (isFromCenter) {
			return;
		}
		double degree;
		if (mIsRTandLB) {
			degree = Math.PI / 4 - Math.atan2(mBezierControl1.y - mTouch.y, mTouch.x - mBezierControl1.x);
		} else {
			degree = Math.PI / 4 - Math.atan2(mTouch.y - mBezierControl1.y, mTouch.x - mBezierControl1.x);
		}
		// 翻起页阴影顶点与touch点的距离
		double d1 = (float) 25 * 1.414 * Math.cos(degree);
		double d2 = (float) 25 * 1.414 * Math.sin(degree);
		float x = (float) (mTouch.x + d1);
		float y;
		if (mIsRTandLB) {
			y = (float) (mTouch.y + d2);
		} else {
			y = (float) (mTouch.y - d2);
		}
		mPath1.reset();
		mPath1.moveTo(x, y);
		mPath1.lineTo(mTouch.x, mTouch.y);
		mPath1.lineTo(mBezierControl1.x, mBezierControl1.y);
		mPath1.lineTo(mBezierStart1.x, mBezierStart1.y);
		mPath1.close();
		float rotateDegrees;
		canvas.save();

		canvas.clipPath(mPath0, Region.Op.XOR);
		canvas.clipPath(mPath1, Region.Op.INTERSECT);
		int leftx;
		int rightx;
		GradientDrawable mCurrentPageShadow;
		if (mIsRTandLB) {
			leftx = (int) (mBezierControl1.x);
			rightx = (int) mBezierControl1.x + 25;
			mCurrentPageShadow = mFrontShadowDrawableVLR;
		} else {
			leftx = (int) (mBezierControl1.x - 25);
			rightx = (int) mBezierControl1.x + 1;
			mCurrentPageShadow = mFrontShadowDrawableVRL;
		}

		rotateDegrees = (float) Math.toDegrees(Math.atan2(mTouch.x - mBezierControl1.x, mBezierControl1.y - mTouch.y));
		canvas.rotate(rotateDegrees, mBezierControl1.x, mBezierControl1.y);
		mCurrentPageShadow.setBounds(leftx, (int) (mBezierControl1.y - mMaxLength), rightx, (int) (mBezierControl1.y));
		mCurrentPageShadow.draw(canvas);
		canvas.restore();

		mPath1.reset();
		mPath1.moveTo(x, y);
		mPath1.lineTo(mTouch.x, mTouch.y);
		mPath1.lineTo(mBezierControl2.x, mBezierControl2.y);
		mPath1.lineTo(mBezierStart2.x, mBezierStart2.y);
		mPath1.close();
		canvas.save();
		canvas.clipPath(mPath0, Region.Op.XOR);
		canvas.clipPath(mPath1, Region.Op.INTERSECT);
		if (mIsRTandLB) {
			leftx = (int) (mBezierControl2.y);
			rightx = (int) (mBezierControl2.y + 25);
			mCurrentPageShadow = mFrontShadowDrawableHTB;
		} else {
			leftx = (int) (mBezierControl2.y - 25);
			rightx = (int) (mBezierControl2.y + 1);
			mCurrentPageShadow = mFrontShadowDrawableHBT;
		}
		rotateDegrees = (float) Math.toDegrees(Math.atan2(mBezierControl2.y - mTouch.y, mBezierControl2.x - mTouch.x));
		canvas.rotate(rotateDegrees, mBezierControl2.x, mBezierControl2.y);
		float temp;
		if (mBezierControl2.y < 0)
			temp = mBezierControl2.y - mHeight;
		else
			temp = mBezierControl2.y;

		int hmg = (int) Math.hypot(mBezierControl2.x, temp);
		if (hmg > mMaxLength)
			mCurrentPageShadow.setBounds((int) (mBezierControl2.x - 25) - hmg, leftx,
					(int) (mBezierControl2.x + mMaxLength) - hmg, rightx);
		else
			mCurrentPageShadow.setBounds((int) (mBezierControl2.x - mMaxLength), leftx, (int) (mBezierControl2.x),
					rightx);

		// QGLog.e("hmg", "mBezierControl2.x   " + mBezierControl2.x
		// + "  mBezierControl2.y  " + mBezierControl2.y);
		mCurrentPageShadow.draw(canvas);
		canvas.restore();
	}

	private void drawNextPageAreaAndShadow(Canvas canvas, Bitmap bitmap) {
		mPath1.reset();
		if (isFromCenter) {
			float startX = mBeziervertex1.x;
			mPath1.moveTo(startX, 0);
			mPath1.lineTo(mWidth, 0);
			mPath1.lineTo(mWidth, mHeight);
			mPath1.lineTo(startX, mHeight);
		} else {
			mPath1.moveTo(mBezierStart1.x, mBezierStart1.y);
			mPath1.lineTo(mBeziervertex1.x, mBeziervertex1.y);
			mPath1.lineTo(mBeziervertex2.x, mBeziervertex2.y);
			mPath1.lineTo(mBezierStart2.x, mBezierStart2.y);
			mPath1.lineTo(mCornerX, mCornerY);
		}
		mPath1.close();

		if (isFromCenter) {
			mDegrees = -180;
		} else {
			mDegrees = (float) Math.toDegrees(Math.atan2(mBezierControl1.x - mCornerX, mBezierControl2.y - mCornerY));
		}

		int leftx;
		int rightx;
		GradientDrawable mBackShadowDrawable;
		if (mIsRTandLB) {
			leftx = (int) (mBezierStart1.x);
			rightx = (int) (mBezierStart1.x + mTouchToCornerDis / 4);
			mBackShadowDrawable = mBackShadowDrawableLR;
		} else {
			leftx = (int) (mBezierStart1.x - mTouchToCornerDis / 4);
			rightx = (int) mBezierStart1.x;
			mBackShadowDrawable = mBackShadowDrawableRL;
		}
		canvas.save();
		canvas.clipPath(mPath0);
		canvas.clipPath(mPath1, Region.Op.INTERSECT);
		canvas.drawBitmap(bitmap, 0, 0, null);
		canvas.rotate(mDegrees, mBezierStart1.x, mBezierStart1.y);
		mBackShadowDrawable.setBounds(leftx, (int) mBezierStart1.y, rightx, (int) (mMaxLength + mBezierStart1.y));
		mBackShadowDrawable.draw(canvas);
		canvas.restore();
	}

	/**
	 * 绘制翻起页背面
	 */
	private void drawCurrentBackArea(Canvas canvas, Bitmap bitmap) {
		int i = (int) (mBezierStart1.x + mBezierControl1.x) / 2;
		float f1 = Math.abs(i - mBezierControl1.x);
		int i1 = (int) (mBezierStart2.y + mBezierControl2.y) / 2;
		float f2 = Math.abs(i1 - mBezierControl2.y);
		float f3 = Math.min(f1, f2);
		mPath1.reset();
		if (isFromCenter) {
			float startX = mTouch.x > 0 ? mTouch.x : 0;
			mPath1.moveTo(startX, 0);
			mPath1.lineTo(mBeziervertex1.x, 0);
			mPath1.lineTo(mBeziervertex1.x, mHeight);
			mPath1.lineTo(startX, mHeight);
		} else {
			mPath1.moveTo(mBeziervertex2.x, mBeziervertex2.y);
			mPath1.lineTo(mBeziervertex1.x, mBeziervertex1.y);
			mPath1.lineTo(mBezierEnd1.x, mBezierEnd1.y);
			mPath1.lineTo(mTouch.x, mTouch.y);
			mPath1.lineTo(mBezierEnd2.x, mBezierEnd2.y);
		}
		mPath1.close();
		GradientDrawable mFolderShadowDrawable;
		int left;
		int right;
		if (mIsRTandLB) {
			left = (int) (mBezierStart1.x - 1);
			right = (int) (mBezierStart1.x + f3 + 1);
			mFolderShadowDrawable = mFolderShadowDrawableLR;
		} else {
			left = (int) (mBezierStart1.x - f3 - 1);
			right = (int) (mBezierStart1.x + 1);
			mFolderShadowDrawable = mFolderShadowDrawableRL;
		}
		canvas.save();
		canvas.clipPath(mPath0);
		canvas.clipPath(mPath1, Region.Op.INTERSECT);

		mPaint.setColorFilter(mColorMatrixFilter);

		float dis = (float) Math.hypot(mCornerX - mBezierControl1.x,
				mBezierControl2.y - mCornerY);
		float f8 = isFromCenter ? 0 : (mCornerX - mBezierControl1.x) / dis;
		float f9 = isFromCenter ? -1 : (mBezierControl2.y - mCornerY) / dis;
		mMatrixArray[0] = 1 - 2 * f9 * f9;
		mMatrixArray[1] = 2 * f8 * f9;
		mMatrixArray[3] = mMatrixArray[1];
		mMatrixArray[4] = 1 - 2 * f8 * f8;
		mMatrix.reset();
		mMatrix.setValues(mMatrixArray);
		mMatrix.preTranslate(-mBezierControl1.x, -mBezierControl1.y);
		mMatrix.postTranslate(mBezierControl1.x, mBezierControl1.y);
		if (mScroller.isFinished()) {
			canvas.drawBitmap(bitmap, mMatrix, mPaint);
		}
		mPaint.setColorFilter(null);

		canvas.rotate(mDegrees, mBezierStart1.x, mBezierStart1.y);
		mFolderShadowDrawable.setBounds(left, (int) mBezierStart1.y, right,
				(int) (mBezierStart1.y + mMaxLength));
		mFolderShadowDrawable.draw(canvas);
		canvas.restore();
	}

	/**
	 * 求解直线P1P2和直线P3P4的交点坐标
	 */
	private PointF getCross(PointF P1, PointF P2, PointF P3, PointF P4) {
		PointF CrossP = new PointF();
		// 二元函数通式： y=ax+b
		float a1 = (P2.y - P1.y) / (P2.x - P1.x);
		float b1 = ((P1.x * P2.y) - (P2.x * P1.y)) / (P1.x - P2.x);

		float a2 = (P4.y - P3.y) / (P4.x - P3.x);
		float b2 = ((P3.x * P4.y) - (P4.x * P3.y)) / (P3.x - P4.x);
		CrossP.x = (b2 - b1) / (a1 - a2);
		CrossP.y = a1 * CrossP.x + b1;
		return CrossP;
	}

	/**
	 * 在点击的时候判断
	 */
	private void calcCornerXY(float x, float y, boolean moveToLeft) {
		mTouch.x = x;
		mTouch.y = Math.max(Math.min(y, mHeight - 1), 1);

		mCornerX = mWidth;
		isFromCenter = false;
		if (y < mHeight / 3 && moveToLeft) {
			mCornerY = 0;
		} else {
			mCornerY = mHeight;
			if (!moveToLeft || y < 2 * mHeight / 3) {
				isFromCenter = true;
			}
		}

		mIsRTandLB = (mCornerX == 0 && mCornerY == mHeight) || (mCornerX == mWidth && mCornerY == 0);
	}

	@Override
	public boolean moveEvent(MotionEvent event) {
		mTouch.x = event.getX();
		mTouch.y = event.getY();
		if (isFromCenter && startY < mHeight / 2) {
			mTouch.y = 1f;
		} else if (isFromCenter && startY >= mHeight / 2) {
			mTouch.y = mHeight - 1f;
		}
		return true;
	}

	private void startAnimation(boolean turnToLeft) {
		this.turnToLeft = turnToLeft;
		int dx, dy;
		if (turnToLeft) {
			dx = -(int) (mWidth + mTouch.x);
		} else {
			dx = (int) (mWidth - mTouch.x);
		}
		if (mCornerY > 0) {
			dy = (int) (mHeight - mTouch.y - 1);
		} else {
			dy = (int) (1 - mTouch.y);
		}

		lastScrollInvalidateTime = 0;
		int delayMillis = (int) (Math.sqrt((double) Math.abs(dx) / mWidth) * kDefaultAniDuration);
		QGLog.e(TAG, "startAnimation: " + delayMillis);
		mScroller.startScroll((int) mTouch.x, (int) mTouch.y, dx, dy, delayMillis);
		delayCheckAnimation();
	}
	
	private static final int kMinScrollInvalidateInterval = 10;
	
	private static final int kCheckAnimationInterval = kMinScrollInvalidateInterval * 2;
	private Handler mCheckAnimationHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			pageView.computeScroll();
		}
	};
	
	private void delayCheckAnimation() {
		mCheckAnimationHandler.removeMessages(0);
		mCheckAnimationHandler.sendEmptyMessageDelayed(0, kCheckAnimationInterval);
	}

	@Override
	public void setTouchStartPosition(int x, int y, boolean moveToLeft) {
		calcCornerXY(x, y, moveToLeft);
	}

	@Override
	public void startTurnAnimation(boolean moveToLeft) {
		startAnimation(moveToLeft);
	}
	
	private static int scrollRemainWidth = 0;
	private long lastScrollInvalidateTime;
	
	public void dealComputerScroller(){
		if (mScroller.computeScrollOffset()) {
			float x = mScroller.getCurrX();
			float y = mScroller.getCurrY();
			mTouch.x = x;
			mTouch.y = y;

			if (turnToLeft && x > scrollRemainWidth - mWidth || !turnToLeft && x < mWidth - scrollRemainWidth) {
				long curTime = System.currentTimeMillis();
				if (lastScrollInvalidateTime + kMinScrollInvalidateInterval < curTime) {
					pageView.invalidate();
					lastScrollInvalidateTime = curTime;
				}

				if (mScroller.isFinished()) {
					finishAnimation();
				} else {
					delayCheckAnimation();
				}
			} else {
				finishAnimation();
			}
		}
	}
	@Override
	public void finishAnimation() {
		if (mScroller != null && !mScroller.isFinished()) {
			mScroller.abortAnimation();
		}
		notifyAnimationFinish();
	}
	
	private void notifyAnimationFinish() {
		if (null != pageView) {
			pageView.onAnimationFinish();
		}
	}

	@Override
	public void upEvent() {
		// TODO Auto-generated method stub
		
	}
}
