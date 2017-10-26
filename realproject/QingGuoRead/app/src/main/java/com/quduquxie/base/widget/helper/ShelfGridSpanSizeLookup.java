package com.quduquxie.base.widget.helper;

import android.support.v7.widget.GridLayoutManager;

import com.quduquxie.base.module.main.fragment.shelf.adapter.ShelfGridAdapter;

/**
 * Created on 17/7/19.
 * Created by crazylei.
 */

public class ShelfGridSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

    private ShelfGridAdapter shelfGridAdapter;

    public ShelfGridSpanSizeLookup(ShelfGridAdapter shelfGridAdapter) {
        this.shelfGridAdapter = shelfGridAdapter;
    }

    @Override
    public int getSpanSize(int position) {

        int type = shelfGridAdapter.getItemViewType(position);

        try {
            if (type == ShelfGridAdapter.TYPE_EMPTY_FILLER) {
                return 2;
            } else {
                return 1;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return 1;
        }
    }

    public void recycle() {
        if (shelfGridAdapter != null) {
            shelfGridAdapter = null;
        }
    }
}