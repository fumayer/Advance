package com.quduquxie.function.search.viewholder;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quduquxie.R;

/**
 * Created on 16/10/18.
 * Created by WangLei.
 */

public class SearchEmptyHolder extends RecyclerView.ViewHolder {

    public RelativeLayout search_empty;
    private TextView search_empty_text;

    public SearchEmptyHolder(View itemView) {
        super(itemView);
        search_empty = (RelativeLayout) itemView.findViewById(R.id.search_empty);
        search_empty_text = (TextView) itemView.findViewById(R.id.search_empty_text);
        search_empty_text.setText(Html.fromHtml("<font color=\"#6e6e6e\">没找到你想看的书？快把你想看的书</font><font color=\"#4d91d0\">告诉我们</font>"));
    }
}
