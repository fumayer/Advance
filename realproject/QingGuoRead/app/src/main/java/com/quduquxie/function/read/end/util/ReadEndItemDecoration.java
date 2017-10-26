package com.quduquxie.function.read.end.util;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.quduquxie.R;
import com.quduquxie.function.read.end.adapter.ReadEndAdapter;

/**
 * Created on 17/5/4.
 * Created by crazylei.
 */

public class ReadEndItemDecoration extends RecyclerView.ItemDecoration {

    private int left_24;
    private int left_30;
    private int left_36;

    public ReadEndItemDecoration(Context context) {
        left_24 = context.getResources().getDimensionPixelOffset(R.dimen.width_24);
        left_30 = context.getResources().getDimensionPixelOffset(R.dimen.width_30);
        left_36 = context.getResources().getDimensionPixelOffset(R.dimen.width_36);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();

        if (adapter != null) {
            int count = adapter.getItemCount();
            int position = recyclerView.getChildAdapterPosition(view);

            if (position > -1 && position < count) {
                int type = adapter.getItemViewType(position);
                if (type == ReadEndAdapter.TYPE_READ_END_BOOK) {
                    int distance = findLastItemType(adapter, type, position);
                    if (distance == 5 || distance == 2) {
                        outRect.left = left_24;
                        outRect.right = left_36;
                    } else if (distance == 4 || distance == 1) {
                        outRect.left = left_30;
                        outRect.right = left_30;
                    } else if (distance == 3 || distance == 0) {
                        outRect.left = left_36;
                        outRect.right = left_24;
                    }
                } else {
                    super.getItemOffsets(outRect, view, recyclerView, state);
                }
            }
        }
    }

    private int findLastItemType(RecyclerView.Adapter adapter, int type, int position) {
        int index = position;
        for (int i = position + 1; i < position + 6; i++) {
            if (i > -1 && i < adapter.getItemCount()) {
                if (type == adapter.getItemViewType(i)) {
                    index = i;
                } else {
                    break;
                }
            }
        }
        return index - position;
    }
}
