package com.quduquxie.base.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.quduquxie.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/7/19.
 * Created by crazylei.
 */

public class ShelfSlideInsertHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.shelf_slide_book_insert)
    public ImageView shelf_slide_book_insert;

    public ShelfSlideInsertHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}