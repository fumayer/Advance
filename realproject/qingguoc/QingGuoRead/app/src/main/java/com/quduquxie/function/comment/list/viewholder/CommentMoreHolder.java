package com.quduquxie.function.comment.list.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.quduquxie.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/2/17.
 * Created by crazylei.
 */

public class CommentMoreHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.comment_more)
    public RelativeLayout comment_more;

    public CommentMoreHolder(View view) {
        super(view);

        ButterKnife.bind(this, view);
    }
}