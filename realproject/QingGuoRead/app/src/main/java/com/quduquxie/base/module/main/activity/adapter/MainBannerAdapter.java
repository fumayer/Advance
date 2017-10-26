package com.quduquxie.base.module.main.activity.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.quduquxie.R;
import com.quduquxie.base.bean.Banner;
import com.quduquxie.base.listener.BannerListener;
import com.quduquxie.communal.widget.GlideRoundTransform;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created on 17/7/20.
 * Created by crazylei.
 */

public class MainBannerAdapter extends PagerAdapter {

    private WeakReference<Context> contextReference;

    private ArrayList<Banner> banners;

    private LayoutInflater layoutInflater;

    private SparseArray<View> views = new SparseArray<>();

    private BannerListener bannerListener;

    public MainBannerAdapter(Context context, ArrayList<Banner> banners, BannerListener bannerListener) {
        this.contextReference = new WeakReference<>(context);
        this.banners = banners;
        this.bannerListener = bannerListener;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return banners.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        views.remove(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = views.get(position);

        if (view == null) {
            Banner banner = banners.get(position);

            view = layoutInflater.inflate(R.layout.layout_view_main_banner, container, false);

            if (view != null && banner != null) {
                initView(view, banner);

                views.put(position, view);
            }
        }

        container.addView(view);
        return view;
    }

    private void initView(View view, final Banner banner) {
        if (view != null && banner != null) {

            ImageView main_banner_image = (ImageView) view.findViewById(R.id.main_banner_image);

            if (main_banner_image != null) {
                if (!TextUtils.isEmpty(banner.image_icon_banner)) {
                    Glide.with(contextReference.get())
                            .load(banner.image_icon_banner)
                            .signature(new StringSignature(banner.image_icon_banner))
                            .transform(new GlideRoundTransform(contextReference.get()))
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .skipMemoryCache(true)
                            .error(R.drawable.icon_banner_default)
                            .placeholder(R.drawable.icon_banner_default)
                            .into(main_banner_image);
                } else {
                    main_banner_image.setImageResource(R.drawable.icon_banner_default);
                }

                main_banner_image.setTag(R.id.click_object, banner);
                main_banner_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (bannerListener != null) {
                            Banner clickedBanner = (Banner) view.getTag(R.id.click_object);
                            bannerListener.onClickedBanner(clickedBanner);
                        }
                    }
                });
            }
        }
    }

    public void recyclerData() {
        if (views != null && views.size() > 0) {
            for (int i = 0; i < views.size(); i++) {
                views.get(i).destroyDrawingCache();
            }
        }
        if (views != null) {
            views.clear();
        }
    }
}