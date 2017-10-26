package com.quduquxie.function.read.end.util;

import android.support.v7.widget.GridLayoutManager;

import com.quduquxie.function.read.end.adapter.ReadEndAdapter;

/**
 * Created on 17/4/16.
 * Created by crazylei.
 */

public class ReadEndSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

    private ReadEndAdapter readEndAdapter;

    public ReadEndSpanSizeLookup(ReadEndAdapter readEndAdapter) {
        this.readEndAdapter = readEndAdapter;
    }


    @Override
    public int getSpanSize(int position) {
        int type = readEndAdapter.getItemViewType(position);

        try {
            if (type == ReadEndAdapter.TYPE_READ_END_PROMPT || type == ReadEndAdapter.TYPE_READ_END_RECOMMEND_HEAD || type == ReadEndAdapter.TYPE_READ_END_RECOMMEND_BOTTOM) {
                return 3;
            } else {
                return 1;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return 1;
        }
    }
}
