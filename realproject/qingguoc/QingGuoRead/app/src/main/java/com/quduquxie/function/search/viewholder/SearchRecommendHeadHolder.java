package com.quduquxie.function.search.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.quduquxie.R;

/**
 * Created on 16/10/18.
 * Created by WangLei.
 */

public class SearchRecommendHeadHolder extends RecyclerView.ViewHolder {

    public TextView search_recommend;

    public SearchRecommendHeadHolder(View itemView) {
        super(itemView);
        search_recommend = (TextView) itemView.findViewById(R.id.search_recommend);
        search_recommend.setText(R.string.literature_recommend);
    }
}
