package com.example.dzh.myapplication.tubatu2;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.dzh.myapplication.R;

/**
 * Created by dzh on 2017/10/25.
 */

public class ClipPagerAdapter extends PagerAdapter {
    private int[] mPics = {R.drawable.campaign_bg1, R.drawable.campaign_bg2, R.drawable.campaign_bg3};


    private final Context mContext;

    public ClipPagerAdapter(Context context) {
        mContext = context;

    }

    @Override
    public int getCount() {
        return mPics.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);

        imageView.setTag(position);
        imageView.setImageResource(mPics[position]);
        container.addView(imageView);
        return imageView;
    }
}