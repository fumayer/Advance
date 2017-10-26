package com.quduquxie.base.viewholder;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.quduquxie.R;
import com.quduquxie.base.module.main.activity.adapter.MainBannerAdapter;
import com.quduquxie.base.bean.Banner;
import com.quduquxie.base.listener.BannerListener;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/7/20.
 * Created by crazylei.
 */

public class MainBannerHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.main_banner_content)
    public ViewPager main_banner_content;

    private ArrayList<Banner> banners = new ArrayList<>();

    private int count;

    private int currentItem = 0;

    private ScheduledExecutorService scheduledExecutorService;

    private MainBannerAdapter mainBannerAdapter;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            main_banner_content.setCurrentItem(currentItem);
        }
    };

    public MainBannerHolder(View view) {
        super(view);

        ButterKnife.bind(this, view);

        if (main_banner_content != null) {
            main_banner_content.setOffscreenPageLimit(1);

            main_banner_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    currentItem = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    public void initializeBannerData(Context context, ArrayList<Banner> bannerList, BannerListener bannerListener) {
        if (bannerList != null && bannerList.size() > 0) {

            if (banners == null) {
                banners = new ArrayList<>();
            } else {
                banners.clear();
            }

            for (Banner banner : bannerList) {
                banners.add(banner);
            }

            if (!banners.isEmpty()) {

                currentItem = 0;

                count = banners.size();

                mainBannerAdapter = new MainBannerAdapter(context, banners, bannerListener);

                main_banner_content.setAdapter(mainBannerAdapter);

                if (scheduledExecutorService != null) {
                    stopPlay();
                    scheduledExecutorService = null;
                }

                startPlay();
            }
        }
    }

    private void startPlay() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new AutoSlideTask(), 10, 10, TimeUnit.SECONDS);
    }

    private void stopPlay() {
        scheduledExecutorService.shutdown();
    }

    private class AutoSlideTask implements Runnable {
        @Override
        public void run() {
            currentItem = (currentItem + 1) % count;
            handler.obtainMessage().sendToTarget();
        }
    }

    public void recycle() {

    }
}