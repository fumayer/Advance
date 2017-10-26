package com.quduquxie.widget;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.quduquxie.R;
import com.quduquxie.base.config.BaseConfig;
import com.quduquxie.base.module.main.activity.view.MainActivity;
import com.quduquxie.base.util.NetworkUtil;
import com.quduquxie.function.read.end.view.ReadEndActivity;

import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;

public class LoadingPage extends FrameLayout {

    private WeakReference<Activity> activityReference;

    private TextView loading_immediately;

    private TextView loading_error_prompt;
    private ImageView loading_error_reload;

    private View loadingView;
    private View loadingErrorView;

    private ViewGroup root;
    private Runnable errorAction;
    private Runnable successAction;

    private Callable<Void> loadingAction;

    private ImageView loading_animator;

    private AnimationDrawable animationDrawable;

    public LoadingPage(Activity activity) {
        super(activity);

        activityReference = new WeakReference<>(activity);

        root = ((ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content));
        initView();
    }

    public LoadingPage(Activity activity, AttributeSet attrs) {
        super(activity, attrs);

        activityReference = new WeakReference<>(activity);

        root = ((ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content));
        initView();
    }

    public LoadingPage(Activity activity, ViewGroup layout) {
        super(activity);
        activityReference = new WeakReference<>(activity);

        root = layout;
        initView();
    }

    public void loading(final Callable<Void> callable) {
        Activity activity = activityReference.get();

        if (activity == null || activity.isFinishing()) {
            return;
        }

        activity.runOnUiThread(new Runnable() {
            public void run() {
                final Thread runner = new Thread() {
                    public void run() {
                        Activity activity = activityReference.get();

                        if (activity == null || activity.isFinishing()) {
                            return;
                        }

                        try {
                            callable.call();

                            activity.runOnUiThread(new Runnable() {
                                public void run() {
                                    onSuccess();
                                }
                            });
                        } catch (final Exception exception) {
                            if (errorAction != null) {
                                errorAction.run();
                            }

                            initializeReloadAction(callable);

                            activity.runOnUiThread(new Runnable() {
                                public void run() {
                                    onError(exception);
                                }
                            });

                            exception.printStackTrace();
                        }
                    }
                };
                runner.start();
            }
        });
    }

    boolean isCustomLoading = false;

    public void setCustomBackground() {
        Activity activity = activityReference.get();
        if (activity == null) {
            return;
        }
        isCustomLoading = true;
        if (BaseConfig.READING_BACKGROUND_MODE == 1) {
            initializeLoadingTextColor(R.color.read_background_first_text);
            initializeLoadingBackground(R.color.read_background_first);
        } else if (BaseConfig.READING_BACKGROUND_MODE == 2) {
            initializeLoadingTextColor(R.color.read_background_second_text);
            initializeLoadingBackground(R.color.read_background_second);
        } else if (BaseConfig.READING_BACKGROUND_MODE == 3) {
            initializeLoadingTextColor(R.color.read_background_third_text);
            initializeLoadingBackground(R.color.read_background_third);
        } else if (BaseConfig.READING_BACKGROUND_MODE == 4) {
            initializeLoadingTextColor(R.color.read_background_fourth_text);
            initializeLoadingBackground(R.color.read_background_fourth);
        } else if (BaseConfig.READING_BACKGROUND_MODE == 5) {
            initializeLoadingTextColor(R.color.read_background_fifth_text);
            initializeLoadingBackground(R.color.read_background_fifth);
        } else if (BaseConfig.READING_BACKGROUND_MODE == 6) {
            initializeLoadingTextColor(R.color.read_background_sixth_text);
            initializeLoadingBackground(R.color.read_background_sixth);
        }

        initializeLoadingReloadImage();
    }

    private void initializeLoadingBackground(int color) {
        Activity activity = activityReference.get();

        if (activity == null || activity.isFinishing()) {
            return;
        }

        Resources resources = activity.getResources();

        loadingView.setBackgroundColor(resources.getColor(color));
        loadingErrorView.setBackgroundColor(resources.getColor(color));
    }

    private void initializeLoadingReloadImage() {
        Activity activity = activityReference.get();

        if (activity == null) {
            return;
        }

        if (BaseConfig.READING_BACKGROUND_MODE == 5 || BaseConfig.READING_BACKGROUND_MODE == 6) {
            loading_error_reload.setImageResource(R.drawable.icon_bookshelf_find);
        } else {
            loading_error_reload.setImageResource(R.drawable.icon_bookshelf_find_deep);
        }
    }

    private void initializeLoadingTextColor(int color) {
        Activity activity = activityReference.get();
        if (activity == null || activity.isFinishing()) {
            return;
        }

        Resources resources = activity.getResources();

        if (loading_immediately != null) {
            loading_immediately.setTextColor(resources.getColor(color));
        }

        if (loading_error_prompt != null) {
            loading_error_prompt.setTextColor(resources.getColor(color));
        }
    }

    private void initView() {
        final Activity activity = activityReference.get();
        if (activity == null) {
            return;
        }

        loadingView = LayoutInflater.from(activity).inflate(R.layout.layout_view_loading_page, null);
        loading_immediately = (TextView) loadingView.findViewById(R.id.loading_immediately);
        loading_animator = (ImageView) loadingView.findViewById(R.id.loading_animator);


        loadingErrorView = LayoutInflater.from(activity).inflate(R.layout.layout_view_loading_error, null);
        loading_error_reload = (ImageView) loadingErrorView.findViewById(R.id.loading_error_reload);
        loading_error_prompt = (TextView) loadingErrorView.findViewById(R.id.loading_error_prompt);
        loadingErrorView.setVisibility(View.GONE);

        addView(loadingView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(loadingErrorView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        initializeLoadingBackground(R.color.color_white);
        initializeLoadingTextColor(R.color.color_black_444444);

        loading_error_reload.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {

                LoadingPage.this.setVisibility(View.VISIBLE);

                loadingView.setVisibility(View.VISIBLE);
                loadingErrorView.setVisibility(View.GONE);

                if (loadingAction != null) {
                    final Activity activity = activityReference.get();

                    if (activity == null || activity.isFinishing()) {
                        return;
                    }

                    try {
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    loadingAction.call();
                                } catch (final Exception exception) {
                                    exception.printStackTrace();
                                }
                            }
                        });
                    } catch (final Exception exception) {
                        exception.printStackTrace();

                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                onError(exception);
                            }
                        });
                    }
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("position", 1);
                    intent.setClass(activity, MainActivity.class);
                    activity.startActivity(intent);
                }
            }
        });

        if (root != null) {
            root.addView(this, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }
    }

    public void setReloadButton(boolean visible) {
        if (loading_error_reload != null) {
            loading_error_reload.setVisibility(visible ? VISIBLE : GONE);
        }
    }

    public void setErrorAction(Runnable error) {
        this.errorAction = error;
    }

    public void initializeReloadAction(Callable<Void> loadingAction) {
        this.loadingAction = loadingAction;
    }

    public void onSuccess() {
        if (root != null) {
            if (animationDrawable != null && animationDrawable.isRunning()) {
                animationDrawable.stop();
            }
            root.removeView(this);
        }
    }

    public void onError(Exception exception) {

        exception.printStackTrace();

        this.setVisibility(View.VISIBLE);

        loadingView.setVisibility(View.GONE);
        loadingErrorView.setVisibility(View.VISIBLE);

        if (!NetworkUtil.checkNetwork(this.getContext())) {
            if (loading_error_prompt != null) {
                loading_error_prompt.setText("网络异常，请稍后再试！");
            }
        } else {
            if (loading_error_prompt != null) {
                loading_error_prompt.setText("内容正在准备中，请稍后再试！");
            }
        }
    }

    public void onError() {
        if (animationDrawable != null) {
            animationDrawable.stop();
        }

        this.setVisibility(View.VISIBLE);

        loadingView.setVisibility(View.GONE);
        loadingErrorView.setVisibility(View.VISIBLE);

        if (!NetworkUtil.checkNetwork(this.getContext())) {
            if (loading_error_prompt != null) {
                loading_error_prompt.setText("网络异常，请稍后再试！");
            }
        } else {
            if (loading_error_prompt != null) {
                loading_error_prompt.setText("内容正在准备中，请稍后再试！");
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
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

        if (root != null) {
            root.removeAllViews();
        }

        removeAllViews();
    }
}
