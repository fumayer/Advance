package com.quduquxie.function.search.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quduquxie.R;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.base.util.TypefaceUtil;
import com.quduquxie.communal.utils.BeanUtil;
import com.quduquxie.communal.viewholder.BookHorizontalHolder;
import com.quduquxie.communal.viewholder.BookVerticalHolder;
import com.quduquxie.function.search.listener.SearchListener;
import com.quduquxie.function.search.viewholder.SearchCategoryHolder;
import com.quduquxie.function.search.viewholder.SearchEmptyHolder;
import com.quduquxie.function.search.viewholder.SearchMoreHolder;
import com.quduquxie.function.search.viewholder.SearchRecommendHeadHolder;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created on 16/10/18.
 * Created by WangLei.
 */

public class SearchResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private WeakReference<Context> contextReference;
    private List<Book> books;
    private LayoutInflater layoutInflater;

    private SearchListener searchListener;

    private BookDaoHelper bookDaoHelper;

    private Typeface typeface_song;

    public static final int TYPE_EMPTY = 0x80;
    public static final int TYPE_RECOMMEND_HEAD = 0x81;
    public static final int TYPE_RECOMMEND_BOOK = 0x82;
    public static final int TYPE_CATEGORY = 0x83;
    public static final int TYPE_MORE = 0x84;
    public static final int TYPE_BOOK = 0x85;

    public SearchResultAdapter(Context context, List<Book> books, SearchListener searchListener) {
        this.contextReference = new WeakReference<>(context);
        this.books = books;
        this.searchListener = searchListener;
        this.layoutInflater = LayoutInflater.from(contextReference.get());
        this.bookDaoHelper = BookDaoHelper.getInstance(contextReference.get());
        this.typeface_song = TypefaceUtil.loadTypeface(contextReference.get(), TypefaceUtil.TYPEFACE_SONG);
    }

    @Override
    public int getItemViewType(int position) {
        return books.get(position).item_type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_EMPTY:
                return new SearchEmptyHolder(layoutInflater.inflate(R.layout.layout_item_search_empty, parent, false));
            case TYPE_RECOMMEND_HEAD:
                return new SearchRecommendHeadHolder(layoutInflater.inflate(R.layout.layout_item_search_recommend_head, parent, false));
            case TYPE_RECOMMEND_BOOK:
                return new BookVerticalHolder(layoutInflater.inflate(R.layout.layout_view_book_vertical, parent, false));
            case TYPE_CATEGORY:
                return new SearchCategoryHolder(layoutInflater.inflate(R.layout.layout_item_search_category, parent, false));
            case TYPE_MORE:
                return new SearchMoreHolder(layoutInflater.inflate(R.layout.layout_item_search_more, parent, false));
            case TYPE_BOOK:
                return new BookHorizontalHolder(layoutInflater.inflate(R.layout.layout_view_book_horizontal, parent, false));
            default:
                return new BookHorizontalHolder(layoutInflater.inflate(R.layout.layout_view_book_horizontal, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Book book = books.get(position);
        if (book == null) {
            return;
        }

        if (viewHolder instanceof SearchEmptyHolder) {
            ((SearchEmptyHolder) viewHolder).search_empty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (searchListener != null) {
                        searchListener.feedbackListener();
                    }
                }
            });
        } else if (viewHolder instanceof BookVerticalHolder) {

            ((BookVerticalHolder) viewHolder).initView(contextReference.get(), book);

            ((BookVerticalHolder) viewHolder).book_vertical.setTag(R.id.click_object, book);
            ((BookVerticalHolder) viewHolder).book_vertical.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (searchListener != null) {
                        Book clickedBook = (Book) view.getTag(R.id.click_object);
                        searchListener.searchResultListener(clickedBook);
                    }
                }
            });

        } else if (viewHolder instanceof SearchCategoryHolder) {
            ((SearchCategoryHolder) viewHolder).initView(book.name);
            ((SearchCategoryHolder) viewHolder).search_category.setTag(R.id.click_object, book.name);
            ((SearchCategoryHolder) viewHolder).search_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (searchListener != null) {
                        String label = (String) v.getTag(R.id.click_object);
                        searchListener.categoryListener(label);
                    }
                }
            });
        } else if (viewHolder instanceof SearchMoreHolder) {
            ((SearchMoreHolder) viewHolder).search_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (searchListener != null) {
                        searchListener.recommendMoreListener();
                    }
                }
            });
        } else if (viewHolder instanceof BookHorizontalHolder) {
            ((BookHorizontalHolder) viewHolder).initView(contextReference.get(), book, typeface_song, position, null);

            ((BookHorizontalHolder) viewHolder).book_horizontal.setTag(R.id.click_object, book);
            ((BookHorizontalHolder) viewHolder).book_horizontal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (searchListener != null) {
                        Book clickedBook = (Book) view.getTag(R.id.click_object);
                        searchListener.searchResultListener(clickedBook);
                    }
                }
            });
            if (bookDaoHelper.subscribeBook(book.id)) {
                ((BookHorizontalHolder) viewHolder).book_horizontal_add_shelf.setVisibility(View.INVISIBLE);
            } else {
                ((BookHorizontalHolder) viewHolder).book_horizontal_add_shelf.setVisibility(View.VISIBLE);
                ((BookHorizontalHolder) viewHolder).book_horizontal_add_shelf.setTag(R.id.click_object, book);
                ((BookHorizontalHolder) viewHolder).book_horizontal_add_shelf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Book clickedBook = (Book) view.getTag(R.id.click_object);
                        if (clickedBook != null && !TextUtils.isEmpty(clickedBook.id)) {
                            int result = bookDaoHelper.insertBook(clickedBook);
                            if (result == 1) {
                                view.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return books.size();
    }
}
