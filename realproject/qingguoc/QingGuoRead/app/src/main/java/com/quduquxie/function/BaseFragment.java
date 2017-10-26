package com.quduquxie.function;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.quduquxie.application.QuApplication;
import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.application.ApplicationUtil;
import com.quduquxie.base.util.TypefaceUtil;

/**
 * Created on 16/11/25.
 * Created by crazylei.
 */

public abstract class BaseFragment extends Fragment {

    private ApplicationComponent applicationComponent;

    private Toast toast;

    public Typeface typeface_song;
    public Typeface typeface_song_depict;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        applicationComponent = QuApplication.getInstance().getApplicationComponent();

        setFragmentComponent(applicationComponent);

        typeface_song = TypefaceUtil.loadTypeface(getQuApplicationContext(), TypefaceUtil.TYPEFACE_SONG);
        typeface_song_depict = TypefaceUtil.loadTypeface(getQuApplicationContext(), TypefaceUtil.TYPEFACE_SONG_DEPICT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initLayout(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    public void collectException(Exception exception) {

        BaseActivity baseActivity = (BaseActivity) getActivity();
        if (baseActivity != null) {
            baseActivity.collectException(exception);
        }

        Logger.d(exception);
    }

    public QuApplication getQuApplication() {
        return applicationComponent.getQuApplication();
    }

    public Context getQuApplicationContext() {
        return applicationComponent.getQuApplication().getApplicationContext();
    }

    public ApplicationUtil getApplicationUtil() {
        return applicationComponent.getApplicationUtil();
    }

    public void showToastInformation(String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }

        if (toast == null) {
            toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }

        if (!getActivity().isFinishing() && toast != null) {
            toast.show();
        }
    }

    protected abstract void setFragmentComponent(ApplicationComponent applicationComponent);

    protected abstract View initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    protected abstract void initView(View view);
}