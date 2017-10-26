package com.quduquxie.function.read.end.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.quduquxie.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/4/16.
 * Created by crazylei.
 */

public class EndPromptHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.read_end_prompt)
    public ImageView read_end_prompt;

    public EndPromptHolder(View view) {
        super(view);

        ButterKnife.bind(this, view);

    }
}
