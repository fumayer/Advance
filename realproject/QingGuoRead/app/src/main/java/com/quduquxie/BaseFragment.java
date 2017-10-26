package com.quduquxie;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quduquxie.util.QGLog;
import com.quduquxie.view.BaseActivity;

/**
 * Created on 16/11/25.
 * Created by crazylei.
 */

public abstract class BaseFragment extends Fragment {

    private static final String TAG = BaseFragment.class.getSimpleName();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initLayout(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    protected abstract View initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    protected abstract void initView();

    public void collectException(Exception exception) {

        BaseActivity baseActivity = (BaseActivity) getActivity();
        baseActivity.collectException(exception);

        QGLog.e(TAG, "收集错误信息： " + exception);
    }
}