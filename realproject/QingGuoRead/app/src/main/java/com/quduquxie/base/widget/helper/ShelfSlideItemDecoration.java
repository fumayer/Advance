package com.quduquxie.base.widget.helper;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.quduquxie.R;
import com.quduquxie.base.module.main.fragment.shelf.adapter.ShelfSlideAdapter;

import java.lang.ref.WeakReference;

/**
 * Created on 17/7/19.
 * Created by crazylei.
 */

public class ShelfSlideItemDecoration extends RecyclerView.ItemDecoration {

    private WeakReference<Context> contextReference;
    private ShelfSlideAdapter shelfSlideAdapter;

    private int interval_32;

    public ShelfSlideItemDecoration(Context context, ShelfSlideAdapter shelfSlideAdapter) {
        this.contextReference = new WeakReference<>(context);
        this.shelfSlideAdapter = shelfSlideAdapter;

        interval_32 = contextReference.get().getResources().getDimensionPixelOffset(R.dimen.width_32);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        outRect.left = interval_32;
        outRect.right = 0;
        outRect.top = interval_32;
        outRect.bottom = interval_32;
    }

    public void recycle() {

    }
}