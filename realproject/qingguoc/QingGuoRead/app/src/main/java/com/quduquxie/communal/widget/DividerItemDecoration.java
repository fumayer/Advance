package com.quduquxie.communal.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.quduquxie.function.comment.list.adapter.CommentAdapter;

/**
 * Created on 17/2/23.
 * Created by crazylei.
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    public static final int HORIZONTAL = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL = LinearLayoutManager.VERTICAL;

    private Paint paint;

    private int orientation;

    private int color;

    private int size = 1;

    public DividerItemDecoration() {
        this(VERTICAL);
    }

    public DividerItemDecoration(int orientation) {
        this.orientation = orientation;

        paint = new Paint();
        paint.setColor(Color.parseColor("#D8D8D8"));
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        if (orientation == VERTICAL) {
            drawHorizontal(c, parent);
        } else {
            drawVertical(c, parent);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + layoutParams.rightMargin;
            final int right = left + size;

            int position = parent.getChildAdapterPosition(child);
            int type = parent.getAdapter().getItemViewType(position);
            if (type != CommentAdapter.TYPE_COMMENT_HOT_FLAG && type != CommentAdapter.TYPE_COMMENT_NEW_FLAG) {
                c.drawRect(left, top, right, bottom, paint);
            }
        }
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + layoutParams.bottomMargin;
            final int bottom = top + size;

            int position = parent.getChildAdapterPosition(child);
            int type = parent.getAdapter().getItemViewType(position);
            if (type != CommentAdapter.TYPE_COMMENT_HOT_FLAG && type != CommentAdapter.TYPE_COMMENT_NEW_FLAG) {
                c.drawRect(left, top, right, bottom, paint);
            }
        }
    }
}
