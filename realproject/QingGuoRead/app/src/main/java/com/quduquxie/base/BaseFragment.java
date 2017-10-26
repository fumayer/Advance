package com.quduquxie.base;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.quduquxie.application.ApplicationUtil;
import com.quduquxie.application.QuApplication;
import com.quduquxie.application.SharedPreferencesUtil;
import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.handler.CrashHandler;
import com.quduquxie.base.util.TypefaceUtil;

import javax.inject.Inject;

/**
 * Created on 17/7/12.
 * Created by crazylei.
 */

public abstract class BaseFragment<T extends BasePresenter> extends Fragment implements BaseView {

    @Inject
    protected T presenter;

    public ApplicationUtil applicationUtil;

    private SharedPreferencesUtil sharedPreferencesUtil;

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

        ApplicationComponent applicationComponent = QuApplication.getInstance().getApplicationComponent();

        setFragmentComponent(applicationComponent);

        applicationUtil = applicationComponent.getApplicationUtil();
        sharedPreferencesUtil = applicationComponent.getSharedPreferencesUtil();

        typeface_song = TypefaceUtil.loadTypeface(this.getContext(), TypefaceUtil.TYPEFACE_SONG);
        typeface_song_depict = TypefaceUtil.loadTypeface(this.getContext(), TypefaceUtil.TYPEFACE_SONG_DEPICT);

        if (presenter != null) {
            presenter.attachView();
        }
    }

    @Override
    public void onDestroy() {

        if (presenter != null) {
            presenter.detachView();
        }

        recycle();

        super.onDestroy();
    }

    @Override
    public void showPromptMessage(String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }

        try {
            if (toast == null) {
                toast = Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT);
            } else {
                toast.setText(message);
                toast.setDuration(Toast.LENGTH_SHORT);
            }

            if (!this.getActivity().isFinishing() && toast != null) {
                toast.show();
            }
        } catch (Exception exception) {
            collectException(exception);
        }
    }

    @Override
    public void changeNightMode() {

    }

    public ApplicationUtil loadApplicationUtil() {
        return applicationUtil;
    }

    public SharedPreferencesUtil loadSharedPreferencesUtil() {
        return sharedPreferencesUtil;
    }

    public void collectException(Exception exception) {
        Logger.d(exception.toString());
//        CrashHandler.getCrashHandler().handleException(exception);
    }

    public void collectException(Throwable throwable) {
        Logger.d(throwable.toString());
//        CrashHandler.getCrashHandler().handleException(throwable);
    }

    protected abstract void setFragmentComponent(ApplicationComponent applicationComponent);
}