package com.quduquxie.revise.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.quduquxie.revise.ModifyQQInterface;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on 16/12/5.
 * Created by crazylei.
 */

public class ModifyQQPresenter implements ModifyQQInterface.Presenter {

    private ModifyQQInterface.View modifyQQView;
    private WeakReference<Context> contextReference;

    private Map<String, String> parameter = new HashMap<>();

    public ModifyQQPresenter(@NonNull ModifyQQInterface.View modifyQQView, Context context) {
        this.modifyQQView = modifyQQView;
        this.contextReference = new WeakReference<>(context);
    }

    @Override
    public void init() {

    }
}
