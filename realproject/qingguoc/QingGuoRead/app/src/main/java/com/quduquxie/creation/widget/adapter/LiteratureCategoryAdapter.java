package com.quduquxie.creation.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quduquxie.R;
import com.quduquxie.bean.Category;
import com.quduquxie.creation.widget.listener.LiteratureCategoryListener;
import com.quduquxie.creation.widget.viewholder.LiteratureCategoryViewHolder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created on 16/11/23.
 * Created by crazylei.
 */

public class LiteratureCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private WeakReference<Context> contextReference;
    private ArrayList<Category> categoryData;

    private int index = -1;

    private LiteratureCategoryListener literatureCategoryListener;

    public LiteratureCategoryAdapter(ArrayList<Category> categoryData, Context context) {
        this.contextReference = new WeakReference<>(context);
        this.categoryData = categoryData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LiteratureCategoryViewHolder(LayoutInflater.from(contextReference.get()).inflate(R.layout.layout_item_literature_category, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final Category category = categoryData.get(position);
        if (category == null) {
            return;
        }

        ((LiteratureCategoryViewHolder) holder).literature_category_item.setText(category.label);

        ((LiteratureCategoryViewHolder) holder).literature_category_item.setSelected(category.check);

        ((LiteratureCategoryViewHolder) holder).literature_category_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.isSelected()) {
                    category.check = false;
                    view.setSelected(false);
                } else {
                    if (index != holder.getAdapterPosition()) {
                        changeViewStatus(holder.getAdapterPosition());
                    }
                    category.check = true;
                    view.setSelected(true);

                    if (literatureCategoryListener != null) {
                        literatureCategoryListener.onCategoryClicked(category.label);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryData.size();
    }

    private void changeViewStatus(int position) {
        if (index != -1 && index < categoryData.size()) {
            categoryData.get(index).check = false;
            notifyDataSetChanged();
        }
        this.index = position;
    }

    public void setLiteratureCategory(Category category) {
        int index = categoryData.indexOf(category);
        if (index != -1) {
            categoryData.get(index).check = true;
            changeViewStatus(index);

            if (literatureCategoryListener != null) {
                literatureCategoryListener.onCategoryClicked(category.label);
            }
        }
    }

    public void clearCheckedCategory() {
        changeViewStatus(-1);
    }

    public void setLiteratureCategoryListener(LiteratureCategoryListener literatureCategoryListener) {
        this.literatureCategoryListener = literatureCategoryListener;
    }
}
