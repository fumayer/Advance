package com.quduquxie.function.search.widget;

import android.support.v7.widget.GridLayoutManager;

import com.quduquxie.function.search.adapter.SearchDefaultAdapter;


public class SearchDefaultSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

    private SearchDefaultAdapter searchDefaultAdapter;

    public SearchDefaultSpanSizeLookup(SearchDefaultAdapter searchDefaultAdapter) {
        this.searchDefaultAdapter = searchDefaultAdapter;
    }

    @Override
    public int getSpanSize(int position) {
        try {
            if (searchDefaultAdapter.getItemViewType(position) == SearchDefaultAdapter.TYPE_CATEGORY) {
                return 1;
            } else {
                return 3;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return 3;
        }
    }
}
