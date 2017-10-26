package com.quduquxie.function.comment.list.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.quduquxie.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/3/10.
 * Created by crazylei.
 */

public class CommentFlagHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.comment_flat_image)
    public ImageView comment_flat_image;

    public CommentFlagHolder(View view) {
        super(view);

        ButterKnife.bind(this, view);

    }
}
