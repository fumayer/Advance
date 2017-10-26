package com.quduquxie.communal.widget.expression;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.quduquxie.BaseFragment;
import com.quduquxie.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/2/25.
 * Created by crazylei.
 */

public class ExpressionGridFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    @BindView(R.id.expression_content_grid)
    public GridView expression_content_grid;

    private ExpressionListener expressionListener;
    private ArrayList<ExpressionItem> expressionItems;

    private int position;


    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_expression_grid, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    protected void initView() {

        Bundle bundle = getArguments();
        if (bundle == null) {
            position = 0;
            expressionItems = ExpressionFirst.DATA;
        } else {
            position = bundle.getInt("ExpressionIndex");
            expressionItems = bundle.getParcelableArrayList("ExpressionData");
            if (expressionItems == null || expressionItems.size() == 0) {
                if (position == 0) {
                    expressionItems = ExpressionFirst.DATA;
                } else if (position == 1) {
                    expressionItems = ExpressionSecond.DATA;
                } else {
                    expressionItems = ExpressionThird.DATA;
                }
            }
        }
        expression_content_grid.setAdapter(new ExpressionAdapter(getContext(), expressionItems));
        expression_content_grid.setOnItemClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("ExpressionData", expressionItems);
    }

    public void setExpressionListener(ExpressionListener expressionListener) {
        this.expressionListener = expressionListener;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (expressionListener != null) {
            expressionListener.onExpressionClicked((ExpressionItem) parent.getItemAtPosition(position));
        }
    }
}
