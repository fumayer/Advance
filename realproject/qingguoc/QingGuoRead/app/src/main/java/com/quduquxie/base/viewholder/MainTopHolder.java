package com.quduquxie.base.viewholder;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import com.quduquxie.R;
import com.quduquxie.base.adapter.TopPageAdapter;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.bean.MainContentItem;
import com.quduquxie.base.listener.BookListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/7/24.
 * Created by crazylei.
 */

public class MainTopHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.main_top)
    ViewPager main_top;

    private SparseArray<List<Book>> books = new SparseArray<>();

    private TopPageAdapter topPageAdapter;

    public MainTopHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void initializeView(Context context, MainContentItem mainContentItem, BookListener bookListener) {
        if (mainContentItem != null && mainContentItem.bookList != null && mainContentItem.bookList.size() > 0) {
            if (books == null) {
                books = new SparseArray<>();
            } else {
                books.clear();
            }

            int size = mainContentItem.bookList.size();
            for (int i = 0; i < 3; i++) {
                if (size >= (i + 1) * 3) {
                    books.append(i, mainContentItem.bookList.subList(i * 3, (i + 1) * 3));
                }
            }

            if (books.size() > 0) {

                main_top.setOffscreenPageLimit(1);

                topPageAdapter = new TopPageAdapter(context, books, bookListener);
                main_top.setAdapter(topPageAdapter);
            }
        }
    }
}