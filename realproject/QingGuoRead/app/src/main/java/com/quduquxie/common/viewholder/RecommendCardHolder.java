package com.quduquxie.common.viewholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.quduquxie.R;
import com.quduquxie.base.adapter.HorizontalListAdapter;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.listener.BookListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/5/17.
 * Created by crazylei.
 */

public class RecommendCardHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.recommend_title)
    TextView recommend_title;
    @BindView(R.id.recommend_result)
    RecyclerView recommend_result;

    private ArrayList<Book> books = new ArrayList<>();

    private HorizontalListAdapter horizontalListAdapter;

    public RecommendCardHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void initView(@NonNull Context context, String title, ArrayList<Book> bookList, BookListener bookListener) {

        if (!TextUtils.isEmpty(title)) {
            recommend_title.setText(title);
        }

        if (bookList != null && bookList.size() > 0) {

            if (books == null) {
                books = new ArrayList<>();
            } else {
                books.clear();
            }

            for (Book book : bookList) {
                books.add(book);
            }

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

            horizontalListAdapter = new HorizontalListAdapter(context, books, bookListener);

            recommend_result.setAdapter(horizontalListAdapter);
            recommend_result.setLayoutManager(linearLayoutManager);
        }
    }

    public void recycle() {
        if (books != null) {
            books.clear();
        }

        if (horizontalListAdapter != null) {
            horizontalListAdapter.recycle();
            horizontalListAdapter = null;
        }
    }
}