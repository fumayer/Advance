package com.quduquxie.module.agreement.presenter;

import android.support.annotation.NonNull;

import com.quduquxie.module.agreement.AgreementInterface;

/**
 * Created on 16/11/25.
 * Created by crazylei.
 */

public class AgreementPresenter implements AgreementInterface.Presenter {

    private AgreementInterface.View statementView;

    public AgreementPresenter(@NonNull AgreementInterface.View statementView) {
        this.statementView = statementView;
    }

    @Override
    public void init() {
        if (statementView != null) {
            statementView.initView();
        }
    }
}
