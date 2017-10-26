package com.quduquxie.base.module.main.fragment.library.view;

import android.app.Activity;
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
import com.quduquxie.base.listener.MainInteractiveListener;
import com.quduquxie.base.module.main.fragment.library.LibraryInterface;
import com.quduquxie.base.module.main.fragment.library.component.DaggerLibraryComponent;
import com.quduquxie.base.module.main.fragment.library.module.LibraryModule;
import com.quduquxie.base.module.main.fragment.library.presenter.LibraryPresenter;
import com.quduquxie.base.module.main.fragment.selected.view.SelectedFragment;
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

public class LibraryFragment extends BaseFragment<LibraryPresenter> implements LibraryInterface.View {

    @BindView(R.id.library_search)
    ImageView library_search;
    @BindView(R.id.library_navigation)
    NavigationBarStrip library_navigation;
    @BindView(R.id.library_result)
    ViewPager library_result;

    @Inject
    LibraryPresenter libraryPresenter;

    private MainInteractiveListener mainInteractiveListener;

    private MainFragmentAdapter mainFragmentAdapter;

    private int index = 0;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mainInteractiveListener = (MainInteractiveListener) activity;
        } catch (ClassCastException classCastException) {
            throw new ClassCastException(activity.toString() + " must implement MainInteractiveListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_fragment_library, container, false);
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
        DaggerLibraryComponent.builder()
                .applicationComponent(applicationComponent)
                .libraryModule(new LibraryModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void initializeParameter() {
        mainFragmentAdapter = new MainFragmentAdapter(getContext(), getFragmentManager(), library_navigation, library_result);
        libraryPresenter.initializeView(mainFragmentAdapter);

        if (library_navigation != null) {
            library_navigation.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    @OnClick({R.id.library_search})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.library_search:
                Intent intent = new Intent();
                intent.setClass(this.getContext(), SearchActivity.class);

                if (!this.getActivity().isFinishing()) {
                    startActivity(intent);
                }
                break;
        }
    }
}
