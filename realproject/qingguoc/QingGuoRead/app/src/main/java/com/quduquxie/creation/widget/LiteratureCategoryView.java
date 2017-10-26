package com.quduquxie.creation.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.quduquxie.R;
import com.quduquxie.creation.widget.adapter.LiteratureCategoryViewAdapter;
import com.quduquxie.creation.widget.listener.LiteratureCategoryListener;

/**
 * Created on 16/11/24.
 * Created by crazylei.
 */

public class LiteratureCategoryView extends RelativeLayout {

    private LiteratureCategoryViewPager literature_category_view_page;

    private LiteratureCategoryViewAdapter literatureCategoryViewAdapter;

    public LiteratureCategoryListener literatureCategoryListener;

    public LiteratureCategoryView(Context context) {
        super(context);
        initView(context);
    }

    public LiteratureCategoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LiteratureCategoryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LiteratureCategoryView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    public void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_view_literature_category, this, true);
        literature_category_view_page = (LiteratureCategoryViewPager) view.findViewById(R.id.literature_category_view_page);
        if (literature_category_view_page != null) {
            literature_category_view_page.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if (literatureCategoryListener != null) {
                        literatureCategoryListener.setCurrentType(position);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    public void setLiteratureCategoryListener(LiteratureCategoryListener literatureCategoryListener) {
        if (literatureCategoryListener != null) {
            this.literatureCategoryListener = literatureCategoryListener;
        }
    }

    public void setLiteratureCategoryAdapter(LiteratureCategoryViewAdapter literatureCategoryViewAdapter) {
        if (literature_category_view_page != null && literatureCategoryViewAdapter != null) {

            this.literatureCategoryViewAdapter = literatureCategoryViewAdapter;

            literature_category_view_page.setAdapter(literatureCategoryViewAdapter);
        }
    }

    public void setCurrentItem(int index) {
        if (literature_category_view_page != null && literatureCategoryViewAdapter != null && index < literatureCategoryViewAdapter.getCount() && literature_category_view_page.getCurrentItem() != index) {
            if (literature_category_view_page.getCurrentItem() != index) {

                clearCurrentChecked(literature_category_view_page.getCurrentItem());

                literature_category_view_page.setCurrentItem(index);
            }
        }
    }

    public void setLiteratureCategory(String category) {
        if (literatureCategoryViewAdapter != null) {
            literatureCategoryViewAdapter.setLiteratureCategory(literature_category_view_page.getCurrentItem(), category);
        }
    }

    private void clearCurrentChecked(int index) {
        if (literatureCategoryViewAdapter != null) {
            literatureCategoryViewAdapter.clearClickedCategory(index);

            if (literatureCategoryListener != null) {
                literatureCategoryListener.clearClickedCategory();
            }
        }
    }
}
