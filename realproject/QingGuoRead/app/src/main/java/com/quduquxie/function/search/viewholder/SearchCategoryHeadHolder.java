package com.quduquxie.function.search.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.quduquxie.R;

/**
 * Created on 16/10/18.
 * Created by WangLei.
 */

public class SearchCategoryHeadHolder extends RecyclerView.ViewHolder {

    private TextView search_category_head;

    public SearchCategoryHeadHolder(View itemView) {
        super(itemView);
        search_category_head = (TextView) itemView.findViewById(R.id.search_category_head);
    }

    public void initView(String message) {
        if (search_category_head != null) {
            search_category_head.setText(message);
        }
    }
}
