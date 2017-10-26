package com.quduquxie.function.read.end.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quduquxie.R;
import com.quduquxie.base.bean.Book;
import com.quduquxie.communal.viewholder.BookVerticalHolder;
import com.quduquxie.function.read.end.viewholder.EndPromptHolder;
import com.quduquxie.function.read.end.viewholder.EndRecommendBottomHolder;
import com.quduquxie.function.read.end.viewholder.EndRecommendHeadHolder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created on 17/4/16.
 * Created by crazylei.
 */

public class ReadEndAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private WeakReference<Context> contextReference;
    private ArrayList<Book> recommends;
    private LayoutInflater layoutInflater;

    private Book book;

    private ReadEndItemClickListener readEndItemClickListener;

    public static final int TYPE_READ_END_PROMPT = 0x80;
    public static final int TYPE_READ_END_RECOMMEND_HEAD = 0x81;
    public static final int TYPE_READ_END_RECOMMEND_BOTTOM = 0x83;
    public static final int TYPE_READ_END_BOOK = 0x84;

    public ReadEndAdapter(Context context, ArrayList<Book> recommends) {
        this.contextReference = new WeakReference<>(context);
        this.recommends = recommends;
        this.layoutInflater = LayoutInflater.from(contextReference.get());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_READ_END_PROMPT:
                return new EndPromptHolder(layoutInflater.inflate(R.layout.layout_item_read_end_prompt, parent, false));
            case TYPE_READ_END_RECOMMEND_HEAD:
                return new EndRecommendHeadHolder(layoutInflater.inflate(R.layout.layout_item_read_end_recommend_head, parent, false));
            case TYPE_READ_END_RECOMMEND_BOTTOM:
                return new EndRecommendBottomHolder(layoutInflater.inflate(R.layout.layout_item_read_end_recommend_bottom, parent, false));
            default:
                return new BookVerticalHolder(layoutInflater.inflate(R.layout.layout_view_book_vertical, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Book recommend = recommends.get(position);
        if (recommend == null) {
            return;
        }
        if (viewHolder instanceof EndPromptHolder) {
            if ("serialize".equals(book.attribute)) {
                ((EndPromptHolder) viewHolder).read_end_prompt.setImageResource(R.drawable.icon_read_end_serialize);
            } else {
                ((EndPromptHolder) viewHolder).read_end_prompt.setImageResource(R.drawable.icon_read_end_finish);
            }

            ((EndPromptHolder) viewHolder).read_end_prompt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!TextUtils.isEmpty(book.id)) {
                        if (readEndItemClickListener != null) {
                            readEndItemClickListener.startCoverActivity(book.id);
                        }
                    }
                }
            });
        } else if (viewHolder instanceof EndRecommendBottomHolder) {
            ((EndRecommendBottomHolder) viewHolder).read_end_recommend_prompt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (readEndItemClickListener != null) {
                        readEndItemClickListener.startBookStoreFragment();
                    }
                }
            });
        } else if (viewHolder instanceof BookVerticalHolder) {
            ((BookVerticalHolder) viewHolder).initView(contextReference.get(), recommend);
            ((BookVerticalHolder) viewHolder).book_vertical.setTag(R.id.click_object, recommend);
            ((BookVerticalHolder) viewHolder).book_vertical.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (readEndItemClickListener != null) {
                        Book book = (Book) view.getTag(R.id.click_object);
                        readEndItemClickListener.startCoverActivity(book.id);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return recommends.size();
    }

    @Override
    public int getItemViewType(int position) {
        return recommends.get(position).item_type;
    }

    public void setBookInformation(Book book) {
        this.book = book;
    }

    public void setReadEndItemClickListener(ReadEndItemClickListener readEndItemClickListener) {
        this.readEndItemClickListener = readEndItemClickListener;
    }

    public interface ReadEndItemClickListener {
        void startCoverActivity(String id);

        void startBookStoreFragment();
    }
}