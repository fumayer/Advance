package com.quduquxie.base.module.main.fragment.shelf.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quduquxie.R;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.base.listener.ShelfListener;
import com.quduquxie.base.viewholder.FillerHolder;
import com.quduquxie.base.viewholder.ShelfGridBookHolder;
import com.quduquxie.base.viewholder.ShelfGridInsertHolder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 17/7/17.
 * Created by crazylei.
 */

public class ShelfGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private WeakReference<Context> contextReference;
    private BookDaoHelper bookDaoHelper;

    private LayoutInflater layoutInflater;

    private List<Book> books = new ArrayList<>();

    private ArrayList<Book> checkedBooks = new ArrayList<>();

    private ShelfListener shelfListener;

    private boolean delete = false;

    public static final int TYPE_INSERT_BOOK = 0x80;
    public static final int TYPE_EMPTY_FILLER = 0x81;

    public ShelfGridAdapter(Context context, List<Book> books) {
        this.contextReference = new WeakReference<>(context);
        this.books = books;
        this.bookDaoHelper = BookDaoHelper.getInstance(contextReference.get());
        this.layoutInflater = LayoutInflater.from(contextReference.get());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_INSERT_BOOK:
                return new ShelfGridInsertHolder(layoutInflater.inflate(R.layout.layout_item_shelf_grid_insert, parent, false));
            case TYPE_EMPTY_FILLER:
                return new FillerHolder(layoutInflater.inflate(R.layout.layout_view_filler_38, parent, false));
            default:
                return new ShelfGridBookHolder(layoutInflater.inflate(R.layout.layout_item_shelf_grid_book, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        Book book = books.get(position);
        if (book == null) {
            return;
        }

        if (viewHolder instanceof ShelfGridInsertHolder) {
            ((ShelfGridInsertHolder) viewHolder).shelf_insert.setVisibility(this.delete ? View.GONE : View.VISIBLE);

            ((ShelfGridInsertHolder) viewHolder).shelf_insert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (delete) {
                        return;
                    }

                    if (shelfListener != null) {
                        shelfListener.insertBook();
                    }
                }
            });
        } else if (viewHolder instanceof ShelfGridBookHolder) {
            ((ShelfGridBookHolder) viewHolder).initializeView(contextReference.get(), book, bookDaoHelper);

            ((ShelfGridBookHolder) viewHolder).shelf_book.setTag(R.id.click_object, book);
            ((ShelfGridBookHolder) viewHolder).shelf_book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (shelfListener != null) {

                        Book clickedBook = (Book) view.getTag(R.id.click_object);

                        if (delete) {

                            if (clickedBook != null && checkedBooks.contains(clickedBook)) {
                                checkedBooks.remove(clickedBook);

                                ((ShelfGridBookHolder) viewHolder).shelf_book_check.setImageResource(R.drawable.icon_shelf_delete_normal);
                                ((ShelfGridBookHolder) viewHolder).shelf_book.setSelected(false);
                            } else if (clickedBook != null) {
                                checkedBooks.add(clickedBook);

                                ((ShelfGridBookHolder) viewHolder).shelf_book_check.setImageResource(R.drawable.icon_shelf_delete_selected);
                                ((ShelfGridBookHolder) viewHolder).shelf_book.setSelected(true);
                            }
                        }
                        shelfListener.clickedBook(clickedBook, delete);
                    }
                }
            });

            ((ShelfGridBookHolder) viewHolder).shelf_book.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (delete) {
                        return true;
                    }

                    checkedBooks.clear();

                    Book clickedBook = (Book) view.getTag(R.id.click_object);

                    if (clickedBook != null && shelfListener != null) {

                        checkedBooks.add(clickedBook);

                        ((ShelfGridBookHolder) viewHolder).shelf_book.setSelected(true);

                        shelfListener.longClickedBook(clickedBook);
                        return true;
                    }
                    return false;
                }
            });

            ((ShelfGridBookHolder) viewHolder).shelf_book_check.setVisibility(delete ? View.VISIBLE : View.INVISIBLE);

            if (((ShelfGridBookHolder) viewHolder).shelf_book_check.getVisibility() == View.VISIBLE) {
                ((ShelfGridBookHolder) viewHolder).shelf_book_check.setImageResource(checkedBooks.contains(book) ? R.drawable.icon_shelf_delete_selected : R.drawable.icon_shelf_delete_normal);
            }
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
            return books.get(position).item_type;
        }
        return TYPE_EMPTY_FILLER;
    }

    public ArrayList<Book> loadCheckedBooks() {
        return checkedBooks;
    }

    public int loadCheckedBookSize() {
        return checkedBooks.size();
    }

    public void notifyItemChanged(Book book) {
        if (book != null && !TextUtils.isEmpty(book.id)) {
            int index = books.indexOf(book);
            if (index != -1) {
                notifyItemChanged(index);
            }
        }
    }

    public void updateDeleteState(boolean delete) {
        this.delete = delete;
        notifyDataSetChanged();
    }

    public void setShelfClickListener(ShelfListener shelfListener) {
        this.shelfListener = shelfListener;
    }

    public void recycle() {
        if (contextReference != null) {
            contextReference.clear();
            contextReference = null;
        }

        if (bookDaoHelper != null) {
            bookDaoHelper = null;
        }

        if (shelfListener != null) {
            shelfListener = null;
        }

        if (layoutInflater != null) {
            layoutInflater = null;
        }

        if (books != null) {
            books.clear();
        }

        if (checkedBooks != null) {
            checkedBooks.clear();
        }
    }
}