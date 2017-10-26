package com.quduquxie.base.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.quduquxie.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/7/29.
 * Created by crazylei.
 */

public class ShelfSlideDetailInsertHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.shelf_slide_detail_insert)
    public LinearLayout shelf_slide_detail_insert;

    public ShelfSlideDetailInsertHolder(View view) {
        super(view);

        ButterKnife.bind(this, view);
    }
}
