package com.quduquxie.base.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.quduquxie.R;
import com.quduquxie.base.bean.Category;
import com.quduquxie.communal.widget.GlideRoundTransform;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/8/1.
 * Created by crazylei.
 */

public class MainSelectedCategoryItemHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.main_selected_category_image)
    public ImageView main_selected_category_image;

    public MainSelectedCategoryItemHolder(View view) {
        super(view);

        ButterKnife.bind(this, view);
    }

    public void initializeView(Context context, Category category) {

        if (category != null) {
            if (!TextUtils.isEmpty(category.image_url)) {
                Glide.with(context)
                        .load(category.image_url)
                        .signature(new StringSignature(category.image_url))
                        .transform(new GlideRoundTransform(context))
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .error(R.drawable.icon_category_default)
                        .placeholder(R.drawable.icon_category_default)
                        .into(main_selected_category_image);
            } else {
                main_selected_category_image.setImageResource(R.drawable.icon_category_default);
            }
        }
    }
}