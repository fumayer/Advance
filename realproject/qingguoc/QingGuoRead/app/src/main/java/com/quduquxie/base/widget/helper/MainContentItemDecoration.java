package com.quduquxie.base.widget.helper;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.quduquxie.R;
import com.quduquxie.base.module.main.activity.adapter.MainContentAdapter;

import java.lang.ref.WeakReference;

/**
 * Created on 17/7/20.
 * Created by crazylei.
 */

public class MainContentItemDecoration extends RecyclerView.ItemDecoration {

    private WeakReference<Context> contextReference;

    private MainContentAdapter mainContentAdapter;

    private int interval_16;
    private int interval_24;
    private int interval_32;
    private int interval_40;

    public MainContentItemDecoration(Context context, MainContentAdapter mainContentAdapter) {
        this.contextReference = new WeakReference<>(context);
        this.mainContentAdapter = mainContentAdapter;

        interval_16 = contextReference.get().getResources().getDimensionPixelSize(R.dimen.width_16);
        interval_24 = contextReference.get().getResources().getDimensionPixelSize(R.dimen.width_24);
        interval_32 = contextReference.get().getResources().getDimensionPixelSize(R.dimen.width_32);
        interval_40 = contextReference.get().getResources().getDimensionPixelSize(R.dimen.width_40);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView recyclerView, RecyclerView.State state) {

        if (mainContentAdapter != null) {
            int count = mainContentAdapter.getItemCount();
            int position = recyclerView.getChildAdapterPosition(view);

            if (position > -1 && position < count) {
                int type = mainContentAdapter.getItemViewType(position);

                switch (type) {
                    case MainContentAdapter.TYPE_LIBRARY_JUMP:
                        int distance = findLastItemTypeOffset(mainContentAdapter, type, position);

                        outRect.top = interval_40;
                        outRect.bottom = interval_24;

                        if (distance == 2) {
                            outRect.top = interval_40;
                            outRect.left = interval_32;
                            outRect.right = 0;
                        } else if (distance == 0) {
                            outRect.left = 0;
                            outRect.right = interval_32;
                        } else {
                            outRect.left = interval_16;
                            outRect.right = interval_16;
                        }
                        break;
                    case MainContentAdapter.TYPE_LIBRARY_CATEGORY:
                        int offset = findFirstItemTypeOffset(mainContentAdapter, type, position);

                        outRect.top = interval_16;
                        outRect.bottom = interval_16;

                        if (offset % 2 == 0) {
                            outRect.left = interval_32;
                            outRect.right = interval_16;
                        } else {
                            outRect.left = interval_16;
                            outRect.right = interval_32;
                        }
                        break;
                    case MainContentAdapter.TYPE_LIBRARY_CATEGORY_HOT:
                        outRect.top = interval_16;
                        outRect.bottom = interval_16;

                        outRect.left = interval_32;
                        outRect.right = interval_32;
                        break;
                }
            } else {
                super.getItemOffsets(outRect, view, recyclerView, state);
            }
        } else {
            super.getItemOffsets(outRect, view, recyclerView, state);
        }
    }


    private int findLastItemTypeOffset(MainContentAdapter mainContentAdapter, int type, int position) {
        int index = position;
        for (int i = position + 1; i < position + 6; i++) {
            if (i > -1 && i < mainContentAdapter.getItemCount()) {
                if (type == mainContentAdapter.getItemViewType(i)) {
                    index = i;
                } else {
                    break;
                }
            }
        }

        return index - position;
    }

    private int findFirstItemTypeOffset(MainContentAdapter mainContentAdapter, int type, int position) {
        int index = position;
        for (int i = position - 1; i > -1; i--) {
            if (i > -1) {
                if (type == mainContentAdapter.getItemViewType(i)) {
                    index = i;
                } else {
                    break;
                }
            }
        }
        return position - index;
    }

    public void recycle() {
        if (contextReference != null) {
            contextReference.clear();
            contextReference = null;
        }

        if (mainContentAdapter != null) {
            mainContentAdapter = null;
        }
    }
}