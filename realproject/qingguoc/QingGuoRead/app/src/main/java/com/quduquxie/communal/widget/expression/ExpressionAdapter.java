package com.quduquxie.communal.widget.expression;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import com.quduquxie.R;

import java.lang.ref.WeakReference;
import java.util.List;

public class ExpressionAdapter extends BaseAdapter {

    private WeakReference<Context> contextReference;
    private List<ExpressionItem> expressionItems;

    public ExpressionAdapter(Context context, List<ExpressionItem> expressionItems) {
        this.expressionItems = expressionItems;
        this.contextReference = new WeakReference<>(context);
    }

    @Override
    public int getCount() {
        return expressionItems.size();
    }

    @Override
    public Object getItem(int position) {
        return expressionItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(contextReference.get()).inflate(R.layout.layout_item_expression, null);
            viewHolder = new ViewHolder();
            viewHolder.expressionTextView = (ExpressionTextView) convertView.findViewById(R.id.expression_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.expressionTextView.setText(expressionItems.get(position).getExpression());

        convertView.setLayoutParams(new AbsListView.LayoutParams(parent.getWidth() / 8, parent.getHeight() / 5));

        return convertView;
    }

    static class ViewHolder {
        ExpressionTextView expressionTextView;
    }
}