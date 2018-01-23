package com.example.sj.app2.toast;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.sj.app2.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by sunjie on 2018/1/23.
 */

public class ToastImage extends Toast {

    private static ToastImage toastImage;

    private ToastImage(@NonNull Builder builder) {
        super(builder.context);
        FrameLayout layout = new FrameLayout(builder.context);
        layout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        ImageView view = new ImageView(builder.context);
        view.setLayoutParams(builder.lp);
        view.setScaleType(ImageView.ScaleType.FIT_CENTER);
        view.setImageResource(builder.drawable);
        layout.addView(view);
        setView(layout);
        setDuration(builder.duration);
        setGravity(builder.gravity, 0, 0);
    }


    public static ToastImage build(@NonNull Builder builder) {
        if (toastImage != null) {
            toastImage.cancel();
            toastImage = new ToastImage(builder);
        } else {
            toastImage = new ToastImage(builder);
        }
        return toastImage;
    }

    public static class Builder {
        private Context context;
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

        public Builder(Context context) {
            this.context = context;
        }


        public Builder setDrawable(int drawable) {
            this.drawable = drawable;
            return this;
        }

        public Builder setLp(FrameLayout.LayoutParams lp) {
            this.lp = lp;
            return this;
        }

        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder setDuration(int duration) {
            this.duration = duration;
            return this;
        }
    }
}
