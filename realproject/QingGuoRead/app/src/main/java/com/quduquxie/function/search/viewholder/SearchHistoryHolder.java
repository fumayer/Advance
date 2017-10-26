package com.quduquxie.function.search.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.quduquxie.R;

/**
 * Created on 16/10/19.
 * Created by WangLei.
 */

public class SearchHistoryHolder extends RecyclerView.ViewHolder {

    public ImageView search_history_delete;

    public SearchHistoryHolder(View view) {
        super(view);
        search_history_delete = (ImageView) view.findViewById(R.id.search_history_delete);
    }
}
