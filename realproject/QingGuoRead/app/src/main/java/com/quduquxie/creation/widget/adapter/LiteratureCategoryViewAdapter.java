package com.quduquxie.creation.widget.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import com.quduquxie.bean.Category;
import com.quduquxie.creation.widget.LiteratureCategoryFragment;
import com.quduquxie.creation.widget.listener.LiteratureCategoryListener;

/**
 * Created on 16/11/23.
 * Created by crazylei.
 */

public class LiteratureCategoryViewAdapter extends FragmentPagerAdapter {

    private LiteratureCategoryFragment literatureCategoryFragmentMan;
    private LiteratureCategoryFragment literatureCategoryFragmentWoman;

    private LiteratureCategoryListener literatureCategoryListener;

    private int index;
    private Category category;

    public LiteratureCategoryViewAdapter(FragmentManager fragmentManager, LiteratureCategoryListener literatureCategoryListener) {
        super(fragmentManager);
        this.literatureCategoryListener = literatureCategoryListener;
    }

    public Fragment getItem(int position) {
        return initView(position);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    protected Fragment initView(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                if (literatureCategoryFragmentMan == null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("category_type", "男频");

                    if (this.index == position && this.category != null) {
                        bundle.putSerializable("category", this.category);
                    }

                    literatureCategoryFragmentMan = new LiteratureCategoryFragment();
                    literatureCategoryFragmentMan.setLiteratureCategoryListener(literatureCategoryListener);
                    literatureCategoryFragmentMan.setArguments(bundle);
                }
                fragment = literatureCategoryFragmentMan;
                break;
            case 1:
                if (literatureCategoryFragmentWoman == null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("category_type", "女频");

                    if (this.index == position && this.category != null) {
                        bundle.putSerializable("category", this.category);
                    }

                    literatureCategoryFragmentWoman = new LiteratureCategoryFragment();
                    literatureCategoryFragmentWoman.setLiteratureCategoryListener(literatureCategoryListener);
                    literatureCategoryFragmentWoman.setArguments(bundle);
                }
                fragment = literatureCategoryFragmentWoman;
                break;
        }
        return fragment;
    }

    public void clearClickedCategory(int index) {
        if (index == 0) {
            if (literatureCategoryFragmentMan != null) {
                literatureCategoryFragmentMan.clearCheckedCategory();
            }
        } else {
            if (literatureCategoryFragmentWoman != null) {
                literatureCategoryFragmentWoman.clearCheckedCategory();
            }
        }
    }

    public void setLiteratureCategory(int index, String label) {
        Category category = new Category();
        category.label = label;
        category.check = true;

        if (index == 0) {
            if (literatureCategoryFragmentMan != null) {
                literatureCategoryFragmentMan.setLiteratureCategory(category);
            } else {
                this.index = index;
                this.category = category;
            }
        } else {
            if (literatureCategoryFragmentWoman != null) {
                literatureCategoryFragmentWoman.setLiteratureCategory(category);
            } else {
                this.index = index;
                this.category = category;
            }
        }
    }
}
