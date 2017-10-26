package com.quduquxie.base.module.main.activity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quduquxie.R;
import com.quduquxie.base.bean.Category;
import com.quduquxie.base.listener.CategoryListener;
import com.quduquxie.base.viewholder.MainSelectedCategoryItemHolder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created on 17/8/1.
 * Created by crazylei.
 */

public class MainSelectedCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private WeakReference<Context> contextReference;

    private ArrayList<Category> categories;

    private CategoryListener categoryListener;

    private LayoutInflater layoutInflater;

    public MainSelectedCategoryAdapter(Context context, ArrayList<Category> categories, CategoryListener categoryListener) {
        this.contextReference = new WeakReference<>(context);
        this.categories = categories;
        this.categoryListener = categoryListener;
        this.layoutInflater = LayoutInflater.from(contextReference.get());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainSelectedCategoryItemHolder(layoutInflater.inflate(R.layout.layout_item_selected_category, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Category category = categories.get(position);

        if (category == null) {
            return;
        }

        if (viewHolder instanceof MainSelectedCategoryItemHolder) {
            ((MainSelectedCategoryItemHolder) viewHolder).initializeView(contextReference.get(), category);
            ((MainSelectedCategoryItemHolder) viewHolder).main_selected_category_image.setTag(R.id.click_object, category);
            ((MainSelectedCategoryItemHolder) viewHolder).main_selected_category_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (categoryListener != null) {
                        Category clickedCategory = (Category) view.getTag(R.id.click_object);
                        if (clickedCategory != null) {
                            categoryListener.onClickedCategory(clickedCategory);
                        }
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (categories == null || categories.isEmpty()) {
            return 0;
        } else {
            return categories.size();
        }
    }

    public void recycle() {

    }
}