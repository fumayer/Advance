package com.example.dzh.myapplication.tubatu;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.dzh.myapplication.R;

/**
 * Created by dzh on 2017/10/24.
 */

public class VpAdapter extends PagerAdapter {

    private final Context mContext;
    private ImageView mImgBg;

    private int[] mPics = {R.drawable.campaign_bg1, R.drawable.campaign_bg2, R.drawable.campaign_bg3};

    public VpAdapter(Context context) {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_vp, container, false);
        mImgBg = (ImageView) view.findViewById(R.id.img_bg);

        mImgBg.setImageResource(mPics[position]);
        container.addView(view);
        return view;
    }


    @Override
    public int getCount() {
        return mPics == null ? 0 : mPics.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

//    @Override
//    public int getItemPosition(Object object) {
//        return PagerAdapter.POSITION_NONE;
//    }
}

