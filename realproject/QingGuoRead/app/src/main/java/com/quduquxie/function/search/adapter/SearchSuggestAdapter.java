package com.quduquxie.function.search.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quduquxie.R;
import com.quduquxie.function.search.listener.SearchListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created on 16/10/19.
 * Created by WangLei.
 */

public class SearchSuggestAdapter extends BaseAdapter {

    private WeakReference<Context> contextReference;
    private ArrayList<String> suggests;
    private SearchListener searchListener;

    public SearchSuggestAdapter(Context context, ArrayList<String> suggests) {
        this.contextReference = new WeakReference<>(context);
        this.suggests = suggests;
    }

    @Override
    public int getCount() {
        return suggests.size();
    }

    @Override
    public Object getItem(int position) {
        return suggests.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(contextReference.get()).inflate(R.layout.layout_item_search_suggest, parent ,false);
            viewHolder = new ViewHolder();
            viewHolder.search_suggest_view = (RelativeLayout) convertView.findViewById(R.id.search_suggest_view);
            viewHolder.search_suggest_title = (TextView) convertView.findViewById(R.id.search_suggest_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (viewHolder.search_suggest_view  != null) {
            viewHolder.search_suggest_view.setTag(R.id.click_object, suggests.get(position));
            viewHolder.search_suggest_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String suggest = (String) view.getTag(R.id.click_object);
                    if (searchListener != null) {
                        searchListener.suggestItemClicked(suggest);
                    }
                }
            });
        }
        viewHolder.search_suggest_title.setText(Html.fromHtml(suggests.get(position)));

        return convertView;
    }

    public class ViewHolder {
        RelativeLayout search_suggest_view;
        TextView search_suggest_title;
    }

    public void setOnItemClickListener(SearchListener searchListener) {
        this.searchListener = searchListener;
    }
}
