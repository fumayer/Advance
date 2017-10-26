package com.quduquxie.function.creation.literature.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.quduquxie.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/4/10.
 * Created by crazylei.
 */

public class LiteratureCompleteHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.literature_complete_button)
    public ImageView literature_complete_button;

    public LiteratureCompleteHolder(View view) {
        super(view);

        ButterKnife.bind(this, view);

    }
}
