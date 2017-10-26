package com.quduquxie.base.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.quduquxie.R;
import com.quduquxie.base.bean.Direct;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/7/21.
 * Created by crazylei.
 */

public class MainLibraryJumpHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.library_jump)
    public RelativeLayout library_jump;

    @BindView(R.id.library_jump_image)
    ImageView library_jump_image;
    @BindView(R.id.library_jump_title)
    TextView library_jump_title;

    public
    MainLibraryJumpHolder(View view) {
        super(view);

        ButterKnife.bind(this, view);
    }

    public void initializeView(Context context, Direct direct) {
        if (direct != null) {
            if ("留存榜".equals(direct.name)) {
                Glide.with(context)
                        .load(R.drawable.icon_main_library_jump_retained)
                        .into(library_jump_image);

            } else if ("完结榜".equals(direct.name)) {
                Glide.with(context)
                        .load(R.drawable.icon_main_library_jump_finish)
                        .into(library_jump_image);
            } else if ("热度榜".equals(direct.name)) {
                Glide.with(context)
                        .load(R.drawable.icon_main_library_jump_hot)
                        .into(library_jump_image);
            }

            library_jump_title.setText(direct.name);
        }
    }
}