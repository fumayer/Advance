package com.quduquxie.communal.widget.expression;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quduquxie.BaseFragment;
import com.quduquxie.R;
import com.quduquxie.widget.CircleIndicator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/2/25.
 * Created by crazylei.
 */

public class ExpressionFragment extends BaseFragment {
    @BindView(R.id.expression_content)
    public ViewPager expression_content;
    @BindView(R.id.expression_indicator)
    public CircleIndicator expression_indicator;

    private ExpressionListener expressionListener;

    private ExpressionViewAdapter expressionViewAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_expression, container, false);

        ButterKnife.bind(this, view);

        if (savedInstanceState != null) {
            int position = savedInstanceState.getInt("position");
            expression_content.setCurrentItem(position);
        }

        return view;
    }

    @Override
    protected void initView() {
        expression_content.setOverScrollMode(ViewPager.OVER_SCROLL_NEVER);

        expressionViewAdapter = new ExpressionViewAdapter(getFragmentManager(), expression_content, expressionListener);

        if (ExpressionFirst.DATA != null) {
            Bundle bundle = new Bundle();
            bundle.putInt("ExpressionIndex", 0);
            bundle.putParcelableArrayList("ExpressionData", ExpressionFirst.DATA);
            expressionViewAdapter.addTab("ExpressionGridFragment", "ExpressionGridFragment", ExpressionGridFragment.class, bundle);
        }

        if (ExpressionSecond.DATA != null) {
            Bundle bundle = new Bundle();
            bundle.putInt("ExpressionIndex", 1);
            bundle.putParcelableArrayList("ExpressionData", ExpressionSecond.DATA);
            expressionViewAdapter.addTab("ExpressionGridFragment", "ExpressionGridFragment", ExpressionGridFragment.class, bundle);
        }

        if (ExpressionThird.DATA != null) {
            Bundle bundle = new Bundle();
            bundle.putInt("ExpressionIndex", 2);
            bundle.putParcelableArrayList("ExpressionData", ExpressionThird.DATA);
            expressionViewAdapter.addTab("ExpressionGridFragment", "ExpressionGridFragment", ExpressionGridFragment.class, bundle);
        }

        if (expression_indicator != null) {
            expression_indicator.setViewPager(expression_content);
        }

        expressionViewAdapter.notifyDataSetChanged();
    }

    public void setExpressionListener(ExpressionListener expressionListener) {
        this.expressionListener = expressionListener;
    }
}
