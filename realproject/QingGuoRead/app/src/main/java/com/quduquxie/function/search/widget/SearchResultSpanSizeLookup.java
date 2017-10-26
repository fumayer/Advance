package com.quduquxie.function.search.widget;

import android.support.v7.widget.GridLayoutManager;

import com.quduquxie.function.search.adapter.SearchResultAdapter;

public class SearchResultSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

    private SearchResultAdapter searchResultAdapter;

    public SearchResultSpanSizeLookup(SearchResultAdapter searchResultAdapter) {
        this.searchResultAdapter = searchResultAdapter;
    }

    @Override
    public int getSpanSize(int position) {
        try {
            if (searchResultAdapter.getItemViewType(position) == SearchResultAdapter.TYPE_RECOMMEND_BOOK) {
                return 3;
            } else if (searchResultAdapter.getItemViewType(position) == SearchResultAdapter.TYPE_CATEGORY) {
                return 4;
            } else {
                return 12;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 12;
        }
    }
}
