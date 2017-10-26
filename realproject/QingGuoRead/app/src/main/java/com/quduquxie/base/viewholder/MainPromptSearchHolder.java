package com.quduquxie.base.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.quduquxie.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/8/1.
 * Created by crazylei.
 */

public class MainPromptSearchHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.prompt_search)
    public RelativeLayout prompt_search;

    public MainPromptSearchHolder(View view) {
        super(view);

        ButterKnife.bind(this, view);
    }
}