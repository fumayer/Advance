package com.shortmeet.www.ui.Home.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.shortmeet.www.Base.BaseLazyFragment;
import com.shortmeet.www.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CareZhuFragment extends BaseLazyFragment {


    public static CareZhuFragment newInstance() {
        Bundle args = new Bundle();
        CareZhuFragment carfrg = new CareZhuFragment();
        carfrg.setArguments(args);
        return carfrg;
    }

    @Override
    public void initView() {

    }
    @Override
    public void initListener() {

    }
    @Override
    public int getMyLazyRootView() {
        return R.layout.fragment_care_zhu;
    }

    @Override
    public void lazyLoad() {

    }

}
