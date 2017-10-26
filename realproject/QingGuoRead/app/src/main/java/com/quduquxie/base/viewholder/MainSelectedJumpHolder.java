package com.quduquxie.base.viewholder;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quduquxie.R;
import com.quduquxie.base.bean.Category;
import com.quduquxie.base.bean.Direct;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/7/20.
 * Created by crazylei.
 */

public class MainSelectedJumpHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.main_selected_jump_first)
    public ImageView main_selected_jump_first;
    @BindView(R.id.main_selected_jump_second)
    public ImageView main_selected_jump_second;
    @BindView(R.id.main_selected_jump_third)
    public ImageView main_selected_jump_third;

    public MainSelectedJumpHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void initializeView(ArrayList<Direct> directs) {
        if (directs != null && directs.size() > 2) {
            Direct firstDirect = directs.get(0);

            initializeData(firstDirect, main_selected_jump_first);

            Direct secondDirect = directs.get(1);

            initializeData(secondDirect, main_selected_jump_second);

            Direct thirdDirect = directs.get(2);

            initializeData(thirdDirect, main_selected_jump_third);
        }
    }

    private void initializeData(Direct direct, ImageView imageView) {
        if (direct != null && !TextUtils.isEmpty(direct.name)) {

            imageView.setTag(R.id.click_object, direct);

            if ("完本".equals(direct.name)) {
                imageView.setImageResource(R.drawable.icon_main_jump_finish);
            } else if ("榜单".equals(direct.name)) {
                imageView.setImageResource(R.drawable.icon_main_jump_ranking);
            } else if ("热门".equals(direct.name)) {
                imageView.setImageResource(R.drawable.icon_main_jump_selected);
            } else if ("大神".equals(direct.name)) {
                imageView.setImageResource(R.drawable.icon_main_jump_author);
            }
        }
    }
}