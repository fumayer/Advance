package com.quduquxie.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quduquxie.R;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.listener.BookListener;
import com.quduquxie.base.listener.TabulationListener;
import com.quduquxie.base.viewholder.BookInformationHolder;
import com.quduquxie.base.viewholder.BookRecommendHolder;
import com.quduquxie.base.viewholder.BookSelectedHolder;
import com.quduquxie.base.viewholder.FillerHolder;
import com.quduquxie.model.v2.Tabulation;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created on 17/7/25.
 * Created by crazylei.
 */

public class BookInformationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private WeakReference<Context> contextReference;
    private ArrayList<Book> books;

    private LayoutInflater layoutInflater;

    private TabulationListener tabulationListener;

    public static final int TYPE_FILLER = 0x80;
    public static final int TYPE_BOOK_SELECTED = 0x81;
    public static final int TYPE_BOOK_RECOMMEND = 0x82;
    public static final int TYPE_BOOK_INFORMATION = 0x83;

    private boolean category = true;

    public BookInformationAdapter(Context context, ArrayList<Book> books) {
        this.contextReference = new WeakReference<>(context);
        this.books = books;
        this.layoutInflater = LayoutInflater.from(contextReference.get());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_BOOK_SELECTED:
                return new BookSelectedHolder(layoutInflater.inflate(R.layout.layout_item_book_selected, parent, false));
            case TYPE_BOOK_RECOMMEND:
                return new BookRecommendHolder(layoutInflater.inflate(R.layout.layout_item_book_recommend, parent, false));
            case TYPE_BOOK_INFORMATION:
                return new BookInformationHolder(layoutInflater.inflate(R.layout.layout_item_book_information, parent, false));
            default:
                return new FillerHolder(layoutInflater.inflate(R.layout.layout_view_filler, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        Book book = books.get(position);
        if (book == null) {
            return;
        }

        if (viewHolder instanceof BookSelectedHolder) {
            ((BookSelectedHolder) viewHolder).initializeView(contextReference.get(), book);
            ((BookSelectedHolder) viewHolder).book_selected.setTag(R.id.click_object, book);
            ((BookSelectedHolder) viewHolder).book_selected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tabulationListener != null) {
                        Book clickedBook = (Book) view.getTag(R.id.click_object);
                        if (clickedBook != null) {
                            tabulationListener.startCoverActivity(clickedBook);
                        }
                    }
                }
            });
        } else if (viewHolder instanceof BookRecommendHolder) {
            ((BookRecommendHolder) viewHolder).initializeView(contextReference.get(), book);
            ((BookRecommendHolder) viewHolder).book_recommend.setTag(R.id.click_object, book);
            ((BookRecommendHolder) viewHolder).book_recommend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tabulationListener != null) {
                        Book clickedBook = (Book) view.getTag(R.id.click_object);
                        if (clickedBook != null) {
                            tabulationListener.startCoverActivity(clickedBook);
                        }
                    }
                }
            });

        } else if (viewHolder instanceof BookInformationHolder) {
            ((BookInformationHolder) viewHolder).initializeView(contextReference.get(), book, (position == books.size() - 1), category);
            ((BookInformationHolder) viewHolder).book_information.setTag(R.id.click_object, book);
            ((BookInformationHolder) viewHolder).book_information.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tabulationListener != null) {
                        Book clickedBook = (Book) view.getTag(R.id.click_object);
                        if (clickedBook != null) {
                            tabulationListener.startCoverActivity(clickedBook);
                        }
                    }
                }
            });

            ((BookInformationHolder) viewHolder).book_information_more.setTag(R.id.click_object, book);
            ((BookInformationHolder) viewHolder).book_information_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tabulationListener != null) {
                        Book clickedBook = (Book) view.getTag(R.id.click_object);
                        if (clickedBook != null) {
                            tabulationListener.showOptionMore(view, clickedBook, position);
                        }
                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        if (books == null || books.size() == 0) {
            return 0;
        } else {
            return books.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position > -1 && position < books.size()) {
            return books.get(position).item_type;
        } else {
            return TYPE_FILLER;
        }
    }

    public void setTabulationListener(TabulationListener tabulationListener) {
        this.tabulationListener = tabulationListener;
    }

    public void setShowCategory(boolean category) {
        this.category = category;
    }
}