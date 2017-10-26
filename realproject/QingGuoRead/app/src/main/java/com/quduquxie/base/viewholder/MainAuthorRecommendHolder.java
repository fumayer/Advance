package com.quduquxie.base.viewholder;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.quduquxie.R;
import com.quduquxie.base.adapter.AuthorRecommendAdapter;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.bean.MainContentItem;
import com.quduquxie.base.listener.BookListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/7/20.
 * Created by crazylei.
 */

public class MainAuthorRecommendHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.main_author_recommend)
    RecyclerView main_author_recommend;

    private ArrayList<Book> books = new ArrayList<>();

    private LinearLayoutManager linearLayoutManager;
    private AuthorRecommendAdapter authorRecommendAdapter;

    public MainAuthorRecommendHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void initializeView(Context context, ArrayList<Book> bookList, BookListener bookListener) {
        if (bookList != null && bookList.size() > 0) {
            if (books == null) {
                books = new ArrayList<>();
            } else {
                books.clear();
            }

            for (Book book : bookList) {
                books.add(book);
            }

            if (!books.isEmpty()) {

                linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

                authorRecommendAdapter = new AuthorRecommendAdapter(context, books, bookListener);

                main_author_recommend.setAdapter(authorRecommendAdapter);
                main_author_recommend.setLayoutManager(linearLayoutManager);
            }
        }
    }

    public void recycle() {

        if (books != null) {
            books.clear();
        }

        if (authorRecommendAdapter != null) {
            authorRecommendAdapter.recycle();
        }
    }
}