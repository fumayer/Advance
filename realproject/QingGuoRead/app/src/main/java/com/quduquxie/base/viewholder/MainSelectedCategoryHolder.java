package com.quduquxie.base.viewholder;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.quduquxie.R;
import com.quduquxie.base.bean.Category;
import com.quduquxie.base.listener.CategoryListener;
import com.quduquxie.base.module.main.activity.adapter.MainSelectedCategoryAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/8/1.
 * Created by crazylei.
 */

public class MainSelectedCategoryHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.main_selected_category)
    RecyclerView main_selected_category;

    private ArrayList<Category> categories = new ArrayList<>();

    private MainSelectedCategoryAdapter mainSelectedCategoryAdapter;

    private LinearLayoutManager linearLayoutManager;

    public MainSelectedCategoryHolder(View view) {
        super(view);

        ButterKnife.bind(this, view);
    }

    public void initializeView(Context context, ArrayList<Category> categoryList, CategoryListener categoryListener) {
        if (categoryList != null && categoryList.size() > 0) {

            if (categories == null) {
                categories = new ArrayList<>();
            } else {
                categories.clear();
            }

            for (Category category : categoryList) {
                categories.add(category);
            }

            if (!categories.isEmpty()) {

                linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                main_selected_category.setLayoutManager(linearLayoutManager);

                mainSelectedCategoryAdapter = new MainSelectedCategoryAdapter(context, categories, categoryListener);
                main_selected_category.setAdapter(mainSelectedCategoryAdapter);
            }
        }
    }
}