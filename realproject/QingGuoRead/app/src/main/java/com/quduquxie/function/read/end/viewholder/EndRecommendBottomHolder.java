package com.quduquxie.function.read.end.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.quduquxie.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/4/16.
 * Created by crazylei.
 */

public class EndRecommendBottomHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.read_end_recommend_prompt)
    public TextView read_end_recommend_prompt;

    public EndRecommendBottomHolder(View view) {
        super(view);

        ButterKnife.bind(this, view);

    }
}
