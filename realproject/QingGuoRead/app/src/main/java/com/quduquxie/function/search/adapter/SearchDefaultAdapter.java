package com.quduquxie.function.search.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quduquxie.R;
import com.quduquxie.function.search.listener.SearchListener;
import com.quduquxie.function.search.viewholder.SearchCategoryGroupHolder;
import com.quduquxie.function.search.viewholder.SearchCategoryHeadHolder;
import com.quduquxie.function.search.viewholder.SearchHistoryHolder;
import com.quduquxie.function.search.viewholder.SearchHistoryItemHolder;
import com.quduquxie.model.Category;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 16/10/19.
 * Created by WangLei.
 */

public class SearchDefaultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private WeakReference<Context> contextReference;
    private List<Category> categories;
    private LayoutInflater layoutInflater;

    private SearchListener searchListener;

    private ArrayList<String> recommends = new ArrayList<>();


    public static final int TYPE_CATEGORY = 0x80;
    public static final int TYPE_HISTORY = 0x81;
    public static final int TYPE_HISTORY_ITEM = 0x82;
    public static final int TYPE_CATEGORY_HEAD = 0x83;
    public static final int TYPE_CATEGORY_GROUP = 0x84;

    public SearchDefaultAdapter(Context context, List<Category> categories, SearchListener searchListener) {
        this.contextReference = new WeakReference<>(context);
        this.categories = categories;
        this.searchListener = searchListener;
        this.layoutInflater = LayoutInflater.from(contextReference.get());
    }

    @Override
    public int getItemViewType(int position) {
       return categories.get(position).item_type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_CATEGORY_HEAD:
                return new SearchCategoryHeadHolder(layoutInflater.inflate(R.layout.layout_item_search_category_head, parent, false));
            case TYPE_HISTORY:
                return new SearchHistoryHolder(layoutInflater.inflate(R.layout.layout_item_search_history_head, parent, false));
            case TYPE_HISTORY_ITEM:
                return new SearchHistoryItemHolder(layoutInflater.inflate(R.layout.layout_item_search_history, parent, false));
            case TYPE_CATEGORY_GROUP:
                return new SearchCategoryGroupHolder(layoutInflater.inflate(R.layout.layout_item_search_category_group, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Category category = categories.get(position);

        if (viewHolder instanceof SearchCategoryHeadHolder) {
            ((SearchCategoryHeadHolder) viewHolder).initView("热门搜索");
        } else if (viewHolder instanceof SearchHistoryHolder) {
            ((SearchHistoryHolder) viewHolder).search_history_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (searchListener != null) {
                        searchListener.deleteHistory();
                    }
                }
            });
        } else if (viewHolder instanceof SearchHistoryItemHolder) {
            ((SearchHistoryItemHolder) viewHolder).setHistoryItem(category.label);
            ((SearchHistoryItemHolder) viewHolder).search_history_item.setTag(R.id.click_object, category.label);
            ((SearchHistoryItemHolder) viewHolder).search_history_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (searchListener != null) {
                        String label = (String) v.getTag(R.id.click_object);
                        searchListener.categoryListener(label);
                    }
                }
            });
        } else if (viewHolder instanceof SearchCategoryGroupHolder) {
            ((SearchCategoryGroupHolder) viewHolder).setRecommendListener(searchListener);
            ((SearchCategoryGroupHolder) viewHolder).setRecommendData(recommends);
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setSearchRecommend(ArrayList<String> recommends) {
        if (recommends != null && recommends.size() > 0) {
            this.recommends.clear();
            this.recommends.addAll(recommends);
        }
    }
}
