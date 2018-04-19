package com.newsdemo.ui.zhihu.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.newsdemo.R;
import com.newsdemo.component.GlidUtils;
import com.newsdemo.model.bean.DailyListBean;
import com.newsdemo.ui.zhihu.activity.ZhihuDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianqiang.hu on 2017/5/16.
 */

public class TopPageAdapter extends PagerAdapter {
    private List<DailyListBean.TopStoriesBean> mList = new ArrayList<>();
    private Context mContext;

    public TopPageAdapter(Context context, List<DailyListBean.TopStoriesBean> mList)
    {
        this.mList = mList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view== object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_page_top,container,false);
        ImageView ivImage= (ImageView) view.findViewById(R.id.iv_top_image);
        TextView tvTittle= (TextView) view.findViewById(R.id.tv_top_title);
        GlidUtils.load(mContext,mList.get(position).getImage(),ivImage);
        tvTittle.setText(mList.get(position).getTitle());
        final int id=mList.get(position).getId();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, ZhihuDetailActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("isNotTransition",true);
                mContext.startActivity(intent);
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }




}
