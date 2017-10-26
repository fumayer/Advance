package com.quduquxie.base.module.main.fragment.selected.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.baidu.mobstat.StatService;
import com.quduquxie.R;
import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.BaseFragment;
import com.quduquxie.base.module.main.activity.adapter.MainFragmentAdapter;
import com.quduquxie.base.module.main.fragment.selected.SelectedInterface;
import com.quduquxie.base.module.main.fragment.selected.component.DaggerSelectedComponent;
import com.quduquxie.base.module.main.fragment.selected.module.SelectedModule;
import com.quduquxie.base.module.main.fragment.selected.presenter.SelectedPresenter;
import com.quduquxie.base.widget.NavigationBarStrip;
import com.quduquxie.function.search.view.SearchActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 17/7/17.
 * Created by crazylei.
 */

public class SelectedFragment extends BaseFragment<SelectedPresenter> implements SelectedInterface.View {

    @BindView(R.id.selected_search)
    ImageView selected_search;
    @BindView(R.id.selected_navigation)
    NavigationBarStrip selected_navigation;
    @BindView(R.id.selected_result)
    ViewPager selected_result;

    @Inject
    SelectedPresenter selectedPresenter;

    private MainFragmentAdapter mainFragmentAdapter;

    private int index = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_fragment_selected, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        initializeParameter();
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onPageStart(this.getContext(), SelectedFragment.class.getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPageEnd(this.getContext(), SelectedFragment.class.getSimpleName());
    }

    @Override
    protected void setFragmentComponent(ApplicationComponent applicationComponent) {
        DaggerSelectedComponent.builder()
                .applicationComponent(applicationComponent)
                .selectedModule(new SelectedModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void initializeParameter() {
        mainFragmentAdapter = new MainFragmentAdapter(getContext(), getFragmentManager(), selected_navigation, selected_result);
        selectedPresenter.initializeView(mainFragmentAdapter);

        selected_result.setOffscreenPageLimit(1);
        selected_result.setCurrentItem(index);

        if (selected_navigation != null) {
            selected_navigation.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    index = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    @Override
    public void recycle() {

    }

    @Override
    public void refreshView() {
        if (mainFragmentAdapter != null) {
            mainFragmentAdapter.notifyDataSetChanged();
        }
    }

    @OnClick({R.id.selected_search})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.selected_search:
                Intent intent = new Intent();
                intent.setClass(this.getContext(), SearchActivity.class);

                if (!this.getActivity().isFinishing()) {
                    startActivity(intent);
                }
                break;
        }
    }
}