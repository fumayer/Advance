package com.example.sj.app2.toast;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sj.app2.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by sunjie on 2018/1/23.
 */

public class ImageToast extends Toast {

    private ImageView view;
    /**
     * 默认图
     */
    private @DrawableRes
    int drawable = R.mipmap.ic_launcher_round;
    /**
     * 默认布局
     */
    private FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);

    /**
     * @hide
     */
    @IntDef({Toast.LENGTH_SHORT, Toast.LENGTH_LONG})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Duration {

    }

    /**
     * Toast的Gravity
     */
    private int gravity = Gravity.CENTER;

    /**
     * 持续时长
     */
    private @Duration
    int duration = Toast.LENGTH_SHORT;

    private ImageToast(Context context) {
        super(context);
        view = new ImageView(context);
    }

    @Override
    public void show() {
        view.setLayoutParams(lp);
        view.setScaleType(ImageView.ScaleType.FIT_CENTER);
        view.setImageResource(drawable);
        setView(view);
        setDuration(duration);
        setGravity(gravity, 0, 0);
        super.show();
    }

    public static class Builder {
        private ImageToast imageToast;

        public Builder(Context context) {
            this.imageToast = new ImageToast(context);
        }

        public Builder setDrawable(@DrawableRes int drawable) {
            imageToast.drawable = drawable;
            return this;
        }

        public Builder setLayoutParams(FrameLayout.LayoutParams layoutParams) {
            imageToast.lp = layoutParams;
            return this;
        }

        public Builder setDuration(@Duration int duration) {
            imageToast.duration = duration;
            return this;
        }

        public Builder setGravity(int gravity) {
            imageToast.gravity = gravity;
            return this;
        }

        public ImageToast create() {
            return imageToast;
        }
    }
}
