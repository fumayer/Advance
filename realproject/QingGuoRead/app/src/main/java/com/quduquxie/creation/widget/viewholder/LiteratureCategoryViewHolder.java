package com.quduquxie.creation.widget.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.quduquxie.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 16/11/23.
 * Created by crazylei.
 */

public class LiteratureCategoryViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.literature_category_item)
    public TextView literature_category_item;

    public LiteratureCategoryViewHolder(View view) {
        super(view);

        ButterKnife.bind(this, view);
    }
}
