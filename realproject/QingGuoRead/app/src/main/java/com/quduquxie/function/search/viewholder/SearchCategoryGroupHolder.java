package com.quduquxie.function.search.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.quduquxie.R;
import com.quduquxie.function.search.listener.SearchListener;
import com.quduquxie.function.search.widget.SearchRecommendView;

import java.util.ArrayList;

/**
 * Created on 17/2/10.
 * Created by crazylei.
 */

public class SearchCategoryGroupHolder extends RecyclerView.ViewHolder {

    public SearchRecommendView search_category_group;

    public SearchCategoryGroupHolder(View view) {
        super(view);
        search_category_group = (SearchRecommendView) view.findViewById(R.id.search_category_group);
    }

    public void setRecommendData(ArrayList<String> recommends) {
        if (search_category_group != null) {
            search_category_group.setRecommendData(recommends);
        }
    }

    public void setRecommendListener(SearchListener searchListener) {
        if (search_category_group != null) {
            search_category_group.setRecommendListener(searchListener);
        }
    }
}
