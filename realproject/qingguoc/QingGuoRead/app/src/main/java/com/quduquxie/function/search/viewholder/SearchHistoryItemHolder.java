package com.quduquxie.function.search.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.quduquxie.R;

/**
 * Created on 16/10/19.
 * Created by WangLei.
 */

public class SearchHistoryItemHolder extends RecyclerView.ViewHolder {

    public TextView search_history_item;

    public SearchHistoryItemHolder(View itemView) {
        super(itemView);
        search_history_item = (TextView) itemView.findViewById(R.id.search_history_item);
    }

    public void setHistoryItem(String history) {
        if (search_history_item != null) {
            search_history_item.setText(history);
        }
    }
}
