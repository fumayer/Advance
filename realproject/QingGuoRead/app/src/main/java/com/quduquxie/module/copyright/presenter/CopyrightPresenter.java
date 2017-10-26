package com.quduquxie.module.copyright.presenter;

import android.support.annotation.NonNull;

import com.quduquxie.module.copyright.CopyrightInterface;

/**
 * Created on 16/11/25.
 * Created by crazylei.
 */

public class CopyrightPresenter implements CopyrightInterface.Presenter {

    private CopyrightInterface.View copyrightView;

    public CopyrightPresenter(@NonNull CopyrightInterface.View copyrightView) {
        this.copyrightView = copyrightView;
    }

    @Override
    public void init() {
        if (copyrightView != null) {
            copyrightView.initView();
        }
    }
}
