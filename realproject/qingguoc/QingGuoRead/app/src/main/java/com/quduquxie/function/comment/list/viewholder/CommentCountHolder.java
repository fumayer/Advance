package com.quduquxie.function.comment.list.viewholder;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.quduquxie.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/2/17.
 * Created by crazylei.
 */

public class CommentCountHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.comment_write)
    public TextView comment_write;

    @BindView(R.id.comment_count)
    TextView comment_count;

    public CommentCountHolder(View view) {
        super(view);

        ButterKnife.bind(this, view);

    }

    public void initView(int count, Typeface typeface) {
        if (comment_count != null) {
            comment_count.setText(String.valueOf(count));
            comment_count.setTypeface(typeface);
        }
    }
}