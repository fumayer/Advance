package com.quduquxie.function.download.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.quduquxie.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/4/21.
 * Created by crazylei.
 */

public class DownloadHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.download_serial_number)
    public TextView download_serial_number;
    @BindView(R.id.download_state)
    public ImageView download_state;
    @BindView(R.id.download_book_name)
    public TextView download_book_name;
    @BindView(R.id.download_progress)
    public ProgressBar download_progress;
    @BindView(R.id.download_prompt)
    public TextView download_prompt;

    public DownloadHolder(View view) {
        super(view);

        ButterKnife.bind(this, view);

    }

    public void initView() {

    }
}
