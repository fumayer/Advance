package com.quduquxie.function.search.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.quduquxie.R;

/**
 * Created on 16/10/18.
 * Created by WangLei.
 */

public class SearchMoreHolder extends RecyclerView.ViewHolder {

    public TextView search_more;

    public SearchMoreHolder(View itemView) {
        super(itemView);
        this.search_more = (TextView) itemView.findViewById(R.id.search_more);
    }
}
