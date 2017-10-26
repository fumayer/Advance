package com.quduquxie.creation.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quduquxie.R;
import com.quduquxie.bean.Category;
import com.quduquxie.creation.widget.adapter.LiteratureCategoryAdapter;
import com.quduquxie.creation.widget.listener.LiteratureCategoryListener;
import com.quduquxie.function.creation.create.util.LiteratureCreateUtil;

import java.util.ArrayList;

/**
 * Created on 16/11/23.
 * Created by crazylei.
 */

public class LiteratureCategoryFragment extends Fragment {

    private RecyclerView literature_category_content;

    private GridLayoutManager gridLayoutManager;

    private Category category;
    private String category_type;
    private ArrayList<Category> categoryList;

    private float scaledDensity = 2;

    private LiteratureCategoryAdapter literatureCategoryAdapter;

    private LiteratureCategoryListener literatureCategoryListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gridLayoutManager = new GridLayoutManager(getActivity(), 4);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            this.category_type = bundle.getString("category_type");
            if (bundle.getSerializable("category") != null) {
                category = (Category) bundle.getSerializable("category");
            }
        }

        if (!TextUtils.isEmpty(category_type)) {
            if ("男频".equals(category_type)) {
                categoryList = LiteratureCreateUtil.getLiteratureCategoriesMan(getContext());
            } else {
                categoryList = LiteratureCreateUtil.getLiteratureCategoriesWoman(getContext());
            }
        }

        scaledDensity = getResources().getDisplayMetrics().scaledDensity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_literature_category, container, false);

        literature_category_content = (RecyclerView) view.findViewById(R.id.literature_category_content);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        literature_category_content.setLayoutManager(gridLayoutManager);
        literature_category_content.addItemDecoration(new LiteratureCategoryItemDecoration(scaledDensity, categoryList.size()));

        literatureCategoryAdapter = new LiteratureCategoryAdapter(categoryList, getContext());
        literatureCategoryAdapter.setLiteratureCategoryListener(literatureCategoryListener);

        literature_category_content.setAdapter(literatureCategoryAdapter);

        if (category != null) {
            literatureCategoryAdapter.setLiteratureCategory(category);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void clearCheckedCategory() {
        if (literatureCategoryAdapter != null) {
            literatureCategoryAdapter.clearCheckedCategory();
        }
    }

    public void setLiteratureCategory(Category category) {
        if (literatureCategoryAdapter != null) {
            literatureCategoryAdapter.setLiteratureCategory(category);
        }
    }

    public void setLiteratureCategoryListener(LiteratureCategoryListener literatureCategoryListener) {
        this.literatureCategoryListener = literatureCategoryListener;
    }
}