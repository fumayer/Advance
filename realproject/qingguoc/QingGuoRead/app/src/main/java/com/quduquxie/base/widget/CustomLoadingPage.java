package com.quduquxie.base.widget;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.quduquxie.R;
import com.quduquxie.base.config.BaseConfig;
import com.quduquxie.base.util.NetworkUtil;

import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;

/**
 * Created on 17/7/14.
 * Created by crazylei.
 */

public class CustomLoadingPage extends FrameLayout {
    private WeakReference<Activity> activityReference;

    private ViewGroup viewGroup;

    private View loadingView;
    private View errorView;

    private TextView loading_immediately;
    private ImageView loading_animator;

    private TextView loading_error_prompt;

    private AnimationDrawable animationDrawable;

    public CustomLoadingPage(@NonNull Activity activity) {
        super(activity);

        this.activityReference = new WeakReference<>(activity);

        this.viewGroup = (ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content);
        initView();
    }

    public CustomLoadingPage(@NonNull Activity activity, ViewGroup viewGroup) {
        super(activity);

        this.activityReference = new WeakReference<>(activity);

        this.viewGroup = viewGroup;
        initView();
    }

    private void initView() {
        final Activity activity = activityReference.get();

        if (activity == null || activity.isFinishing()) {
            return;
        }

        loadingView = LayoutInflater.from(activity).inflate(R.layout.layout_view_loading_page, null);
        loading_immediately = (TextView) loadingView.findViewById(R.id.loading_immediately);
        loading_animator = (ImageView) loadingView.findViewById(R.id.loading_animator);

        errorView = LayoutInflater.from(activity).inflate(R.layout.layout_view_loading_error, null);
        loading_error_prompt = (TextView) errorView.findViewById(R.id.loading_error_prompt);
        errorView.setVisibility(View.GONE);

        addView(loadingView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(errorView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        loading_animator.setBackgroundResource(R.drawable.drawable_loading_page);
        if (animationDrawable == null) {
            animationDrawable = (AnimationDrawable) loading_animator.getBackground();
        }
        animationDrawable.start();

        setLoadingBackground(R.color.color_white);
        setLoadingTextColor(R.color.color_gray_686868);

        if (viewGroup != null) {
            viewGroup.addView(this, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }
    }

    private void setLoadingBackground(@ColorRes int color) {
        Activity activity = activityReference.get();
        if (activity == null) {
            return;
        }

        int colorResource = activity.getResources().getColor(color);

        loadingView.setBackgroundColor(colorResource);
        errorView.setBackgroundColor(colorResource);
    }

    private void setLoadingTextColor(@ColorRes int color) {
        Activity activity = activityReference.get();
        if (activity == null) {
            return;
        }

        int colorResource = activity.getResources().getColor(color);

        loading_immediately.setTextColor(colorResource);
        loading_error_prompt.setTextColor(colorResource);
    }

    public void customLoadingBackground() {
        Activity activity = activityReference.get();
        if (activity == null) {
            return;
        }
        if (BaseConfig.READING_BACKGROUND_MODE == 1) {
            setLoadingTextColor(R.color.read_background_first_text);
            setLoadingBackground(R.color.read_background_first);
        } else if (BaseConfig.READING_BACKGROUND_MODE == 2) {
            setLoadingTextColor(R.color.read_background_second_text);
            setLoadingBackground(R.color.read_background_second);
        } else if (BaseConfig.READING_BACKGROUND_MODE == 3) {
            setLoadingTextColor(R.color.read_background_third_text);
            setLoadingBackground(R.color.read_background_third);
        } else if (BaseConfig.READING_BACKGROUND_MODE == 4) {
            setLoadingTextColor(R.color.read_background_fourth_text);
            setLoadingBackground(R.color.read_background_fourth);
        } else if (BaseConfig.READING_BACKGROUND_MODE == 5) {
            setLoadingTextColor(R.color.read_background_fifth_text);
            setLoadingBackground(R.color.read_background_fifth);
        } else if (BaseConfig.READING_BACKGROUND_MODE == 6) {
            setLoadingTextColor(R.color.read_background_sixth_text);
            setLoadingBackground(R.color.read_background_sixth);
        }
    }

    public void onSuccess() {
        if (viewGroup != null) {
            if (animationDrawable != null && animationDrawable.isRunning()) {
                animationDrawable.stop();
            }
            viewGroup.removeView(this);
        }
    }

    public void onError() {
        if (animationDrawable != null && animationDrawable.isRunning()) {
            animationDrawable.stop();
        }

        loadingView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);

        Activity activity = activityReference.get();
        if (activity != null) {
            if (NetworkUtil.loadNetworkType(activity) == NetworkUtil.NETWORK_NONE) {
                if (loading_error_prompt != null) {
                    loading_error_prompt.setText(R.string.network_error);
                }
            } else {
                if (loading_error_prompt != null) {
                    loading_error_prompt.setText(R.string.network_error_prompt);
                }
            }
        } else {
            if (loading_error_prompt != null) {
                loading_error_prompt.setText(R.string.network_error);
            }
        }
    }

    public void loading(final Callable<Void> callable) {
        Activity activity = activityReference.get();
        if (activity == null || activity.isFinishing()) {
            return;
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            callable.call();

                            Activity activity = activityReference.get();
                            if (activity == null || activity.isFinishing()) {
                                return;
                            }

                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    onSuccess();
                                }
                            });
                        } catch (Exception exception) {
                            exception.printStackTrace();

                            Activity activity = activityReference.get();
                            if (activity == null || activity.isFinishing()) {
                                return;
                            }

                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    onError();
                                }
                            });
                        }
                    }
                };

                thread.run();
            }
        });
    }

    public void recycle() {
        if (activityReference != null) {
            activityReference.clear();
            activityReference = null;
        }

        if (animationDrawable != null) {
            animationDrawable.stop();
            animationDrawable = null;
        }

        if (viewGroup != null) {
            viewGroup.removeAllViews();
        }

        removeAllViews();
    }
}