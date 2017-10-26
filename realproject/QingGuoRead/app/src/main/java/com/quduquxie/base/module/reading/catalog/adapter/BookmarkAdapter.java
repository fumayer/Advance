package com.quduquxie.base.module.reading.catalog.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quduquxie.R;
import com.quduquxie.base.bean.Bookmark;
import com.quduquxie.base.listener.BookmarkListener;
import com.quduquxie.base.viewholder.BookmarkHolder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created on 17/7/27.
 * Created by crazylei.
 */

public class BookmarkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private WeakReference<Context> contextReference;

    private ArrayList<Bookmark> bookmarks;

    private LayoutInflater layoutInflater;

    private BookmarkListener bookmarkListener;

    private String form;

    public BookmarkAdapter(Context context, ArrayList<Bookmark> bookmarks) {
        this.contextReference = new WeakReference<>(context);
        this.bookmarks = bookmarks;
        this.layoutInflater = LayoutInflater.from(contextReference.get());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BookmarkHolder(layoutInflater.inflate(R.layout.layout_item_bookmark, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Bookmark bookmark = bookmarks.get(position);
        if (bookmark == null) {
            return;
        }
        ((BookmarkHolder) viewHolder).initializeView(bookmark, form);

        ((BookmarkHolder) viewHolder).bookmark_content.setTag(R.id.click_object, bookmark);
        ((BookmarkHolder) viewHolder).bookmark_content.setTag(R.id.click_position, position);
        ((BookmarkHolder) viewHolder).bookmark_content.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (bookmarkListener != null) {
                    int index = (int) view.getTag(R.id.click_position);
                    Bookmark clickedBookmark = (Bookmark) view.getTag(R.id.click_object);
                    bookmarkListener.onLongClickedBookmark(clickedBookmark, index);
                }
                return false;
            }
        });

        ((BookmarkHolder) viewHolder).bookmark_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bookmarkListener != null) {
                    Bookmark clickedBookmark = (Bookmark) view.getTag(R.id.click_object);
                    bookmarkListener.onClickedBookmark(clickedBookmark);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (bookmarks == null || bookmarks.size() == 0) {
            return 0;
        } else {
            return bookmarks.size();
        }
    }

    public void setBookmarkListener(BookmarkListener bookmarkListener) {
        this.bookmarkListener = bookmarkListener;
    }

    public void setActivityForm(String form) {
        this.form = form;
    }

    public void recycle() {

    }
}