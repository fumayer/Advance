package com.quduquxie.function.search.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.quduquxie.R;

/**
 * Created on 16/10/18.
 * Created by WangLei.
 */

public class SearchCategoryHolder extends RecyclerView.ViewHolder {

    public TextView search_category;

    public SearchCategoryHolder(View itemView) {
        super(itemView);
        search_category = (TextView) itemView.findViewById(R.id.search_category);
    }

    public void initView(String name) {
        if (search_category != null) {
            search_category.setText(name);
        }
    }
}
