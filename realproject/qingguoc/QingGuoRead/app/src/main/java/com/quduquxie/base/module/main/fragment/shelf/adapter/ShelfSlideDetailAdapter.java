package com.quduquxie.base.module.main.fragment.shelf.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;
import com.quduquxie.R;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.listener.ShelfSlideDetailListener;
import com.quduquxie.base.module.main.fragment.shelf.view.ShelfSlideFragment;
import com.quduquxie.base.viewholder.FillerHolder;
import com.quduquxie.base.viewholder.ShelfSlideDetailHolder;
import com.quduquxie.base.viewholder.ShelfSlideDetailInsertHolder;
import com.quduquxie.base.viewholder.ShelfSlideDetailLocalHolder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created on 17/7/29.
 * Created by crazylei.
 */

public class ShelfSlideDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private WeakReference<Context> contextReference;

    private ArrayList<Book> books;

    private LayoutInflater layoutInflater;

    private ShelfSlideDetailListener shelfSlideDetailListener;

    private static final int TYPE_BOOK_LOCAL = 0x80;
    private static final int TYPE_BOOK_ONLINE = 0x81;
    private static final int TYPE_EMPTY_FILLER = 0x82;

    public ShelfSlideDetailAdapter(Context context, ArrayList<Book> books) {
        this.contextReference = new WeakReference<>(context);
        this.books = books;
        this.layoutInflater = LayoutInflater.from(contextReference.get());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_BOOK_ONLINE:
                return new ShelfSlideDetailHolder(layoutInflater.inflate(R.layout.layout_item_shelf_slide_detail, parent, false));
            case TYPE_BOOK_LOCAL:
                return new ShelfSlideDetailLocalHolder(layoutInflater.inflate(R.layout.layout_item_shelf_slide_detail_local, parent, false));
            case ShelfSlideFragment.TYPE_INSERT_BOOK:
                return new ShelfSlideDetailInsertHolder(layoutInflater.inflate(R.layout.layout_item_shelf_slide_detail_insert, parent, false));
            default:
                return new FillerHolder(layoutInflater.inflate(R.layout.layout_view_filler, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Book book = books.get(position);
        if (book == null) {
            return;
        }

        if (viewHolder instanceof ShelfSlideDetailHolder) {
            ((ShelfSlideDetailHolder) viewHolder).initializeBook(contextReference.get(), book);
            ((ShelfSlideDetailHolder) viewHolder).shelf_slide_detail.setTag(R.id.click_object, book);
            ((ShelfSlideDetailHolder) viewHolder).shelf_slide_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (shelfSlideDetailListener != null) {
                        Book clickedBook = (Book) view.getTag(R.id.click_object);
                        String type = (String) view.getTag(R.id.click_type);
                        if (clickedBook != null && !TextUtils.isEmpty(type)) {
                            shelfSlideDetailListener.onClickedOnlineBook(clickedBook, type);
                        }
                    }
                }
            });
        } else if (viewHolder instanceof ShelfSlideDetailLocalHolder) {
            ((ShelfSlideDetailLocalHolder) viewHolder).shelf_slide_detail_local.setTag(R.id.click_object, book);
            ((ShelfSlideDetailLocalHolder) viewHolder).shelf_slide_detail_local.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (shelfSlideDetailListener != null) {
                        Book clickedBook = (Book) view.getTag(R.id.click_object);
                        if (clickedBook != null) {
                            shelfSlideDetailListener.onClickedLocalBook(clickedBook);
                        }
                    }
                }
            });
        } else if (viewHolder instanceof ShelfSlideDetailInsertHolder) {
            ((ShelfSlideDetailInsertHolder) viewHolder).shelf_slide_detail_insert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (shelfSlideDetailListener != null) {
                        shelfSlideDetailListener.insertBook();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (books == null || books.isEmpty()) {
            return 0;
        } else {
            return books.size();
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (position > -1 && position < books.size()) {
            Book book = books.get(position);
            if (book != null) {
                if (book.item_type == ShelfSlideFragment.TYPE_INSERT_BOOK) {
                    return ShelfSlideFragment.TYPE_INSERT_BOOK;
                } else {
                    if (book.book_type == Book.TYPE_ONLINE) {
                        return TYPE_BOOK_ONLINE;
                    } else {
                        return TYPE_BOOK_LOCAL;
                    }
                }
            } else {
                return TYPE_EMPTY_FILLER;
            }
        } else {
            return TYPE_EMPTY_FILLER;
        }
    }

    public void notifyItemChanged(Book book) {
        if (book != null && !TextUtils.isEmpty(book.id)) {
            int index = books.indexOf(book);
            Logger.e("notifyItemChanged: " + index);
            if (index != -1) {
                notifyItemChanged(index);
            }
        }
    }

    public void setShelfSlideDetailListener(ShelfSlideDetailListener shelfSlideDetailListener) {
        this.shelfSlideDetailListener = shelfSlideDetailListener;
    }
}