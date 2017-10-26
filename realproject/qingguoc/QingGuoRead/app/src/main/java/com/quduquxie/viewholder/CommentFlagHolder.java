package com.quduquxie.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.quduquxie.R;

/**
 * Created on 17/3/10.
 * Created by crazylei.
 */

public class CommentFlagHolder extends RecyclerView.ViewHolder {

    public ImageView comment_flat_image;

    public CommentFlagHolder(View view) {
        super(view);
        comment_flat_image = (ImageView) view.findViewById(R.id.comment_flat_image);
    }
}
