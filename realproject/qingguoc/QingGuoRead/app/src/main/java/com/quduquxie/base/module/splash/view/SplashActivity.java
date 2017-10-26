package com.quduquxie.base.module.splash.view;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.baidu.mobstat.StatService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.quduquxie.R;
import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.BaseActivity;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.bean.Splash;
import com.quduquxie.base.module.main.activity.view.MainActivity;
import com.quduquxie.base.module.splash.SplashInterface;
import com.quduquxie.base.module.splash.component.DaggerSplashComponent;
import com.quduquxie.base.module.splash.module.SplashModule;
import com.quduquxie.base.module.splash.presenter.SplashPresenter;
import com.quduquxie.base.util.StatServiceUtil;
import com.quduquxie.modular.cover.view.CoverActivity;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/7/12.
 * Created by crazylei.
 */

public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashInterface.View {

    @BindView(R.id.splash_recommend)
    ImageView splash_recommend;
    @BindView(R.id.splash_logo)
    ImageView splash_logo;
    @BindView(R.id.splash_first_publish)
    ImageView splash_first_publish;

    @Inject
    SplashPresenter splashPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_splash);
        initializeParameter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void setActivityComponent(ApplicationComponent applicationComponent) {
        DaggerSplashComponent.builder()
                .applicationComponent(applicationComponent)
                .splashModule(new SplashModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void initializeParameter() {
        ButterKnife.bind(this);

        initializeWindow();

        splashPresenter.loadSplashRecommend();

        splashPresenter.checkDefaultBook();

        splashPresenter.initializePresenter();

        UiHandler uiHandler = new UiHandler(this);

        Message message = uiHandler.obtainMessage(0);
        uiHandler.sendMessageDelayed(message, 3000L);

        inspectDownloadService();

        inspectCheckNovelUpdateService();
    }

    @Override
    public void recycle() {

    }

    private void initializeWindow() {
        if (this.getWindow() != null) {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    public void showRecommend(Splash splash) {
        if (splash != null) {
            if ("book".equals(splash.type) && splash.book != null) {
                initRecommendBook(splash.book);
            }
        }
    }

    @Override
    public void startMainActivity() {
        Intent intent = new Intent();
        intent.setClass(SplashActivity.this, MainActivity.class);

        if (!isFinishing()) {
            startActivity(intent);

            finish();
        }
    }

    private void checkStartActivity() {
        splashPresenter.checkStartActivity();
    }

    private void initRecommendBook(Book book) {
        if (book != null) {
            if (splash_recommend != null) {
                if (!TextUtils.isEmpty(book.image)) {
                    Glide.with(this)
                            .load(book.image)
                            .signature(new StringSignature(book.image))
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .placeholder(R.drawable.icon_splash_recommend_default)
                            .error(R.drawable.icon_splash_recommend_default)
                            .skipMemoryCache(true)
                            .fitCenter()
                            .into(splash_recommend);

                    splash_recommend.setTag(R.id.click_object, book);

                    splash_recommend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Book clickedBook = (Book) view.getTag(R.id.click_object);
                            if (clickedBook != null && !TextUtils.isEmpty(clickedBook.id)) {

                                StatServiceUtil.statSplashRecommend(SplashActivity.this, clickedBook.id);

                                startCoverActivity(clickedBook);
                            }
                        }
                    });
                } else {
                    splash_recommend.setImageResource(R.drawable.icon_splash_recommend_default);
                }
            }
        }
    }

    private void startCoverActivity(Book book) {
        Intent intent = new Intent();
        intent.setClass(this, CoverActivity.class);
        intent.putExtra("id", book.id);
        if (!isFinishing()) {
            try {
                startActivity(intent);
                finish();
            } catch (ActivityNotFoundException | SecurityException exception) {
                collectException(exception);
                exception.printStackTrace();
            }
        }
    }

    private static class UiHandler extends Handler {

        WeakReference<SplashActivity> splashReference;

        UiHandler(SplashActivity splashActivity) {
            splashReference = new WeakReference<>(splashActivity);
        }

        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);

            SplashActivity splashActivity = splashReference.get();
            if (splashActivity == null || splashActivity.isFinishing()) {
                return;
            }

            switch (message.what) {
                case 0:
                    splashActivity.checkStartActivity();
                    break;
            }
        }
    }
}