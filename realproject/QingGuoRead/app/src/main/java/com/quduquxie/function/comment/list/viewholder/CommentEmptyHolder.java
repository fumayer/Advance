package com.quduquxie.function.comment.list.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.quduquxie.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/2/24.
 * Created by crazylei.
 */

public class CommentEmptyHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.comment_empty_prompt)
    TextView comment_empty_prompt;

    public CommentEmptyHolder(View view) {
        super(view);

        ButterKnife.bind(this, view);

    }
}
