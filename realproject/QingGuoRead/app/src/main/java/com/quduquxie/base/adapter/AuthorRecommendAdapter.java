package com.quduquxie.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quduquxie.R;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.listener.BookListener;
import com.quduquxie.base.viewholder.AuthorRecommendHolder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created on 17/7/24.
 * Created by crazylei.
 */

public class AuthorRecommendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private WeakReference<Context> contextReference;

    private ArrayList<Book> books;

    private BookListener bookListener;

    private LayoutInflater layoutInflater;

    public AuthorRecommendAdapter(Context context, ArrayList<Book> books, BookListener bookListener) {
        this.contextReference = new WeakReference<>(context);
        this.books = books;
        this.bookListener = bookListener;
        this.layoutInflater = LayoutInflater.from(contextReference.get());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AuthorRecommendHolder(layoutInflater.inflate(R.layout.layout_item_author_recommend, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Book book = books.get(position);
        if (book == null) {
            return;
        }

        ((AuthorRecommendHolder) viewHolder).initializeView(contextReference.get(), book);
        ((AuthorRecommendHolder) viewHolder).author_recommend.setTag(R.id.click_object, book);
        ((AuthorRecommendHolder) viewHolder).author_recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bookListener != null) {
                    Book clickedBook = (Book) view.getTag(R.id.click_object);
                    if (clickedBook != null) {
                        bookListener.onClickedBook(clickedBook);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (books == null || books.isEmpty()) {
            return 0;
        } else {
            return books.size();
        }
    }

    public void recycle() {

    }
}