package com.quduquxie.base.widget.helper;

import android.support.v7.widget.GridLayoutManager;

import com.quduquxie.base.module.main.activity.adapter.MainContentAdapter;

/**
 * Created on 17/7/20.
 * Created by crazylei.
 */

public class MainContentSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

    private MainContentAdapter mainContentAdapter;

    public MainContentSpanSizeLookup(MainContentAdapter mainContentAdapter) {
        this.mainContentAdapter = mainContentAdapter;
    }

    @Override
    public int getSpanSize(int position) {

        int type = mainContentAdapter.getItemViewType(position);

        try {

            switch (type) {
                case MainContentAdapter.TYPE_BANNER:
                    return 6;
                case MainContentAdapter.TYPE_LIBRARY_JUMP:
                    return 2;
                case MainContentAdapter.TYPE_LIBRARY_CATEGORY:
                    return 3;
                case MainContentAdapter.TYPE_LIBRARY_CATEGORY_HOT:
                    return 6;
                default:
                    return 6;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return 6;
        }
    }

    public void recycle() {
        if (mainContentAdapter != null) {
            mainContentAdapter = null;
        }
    }
}