package com.quduquxie.creation.revise;

import com.quduquxie.BasePresenter;
import com.quduquxie.BaseView;
import com.quduquxie.model.creation.Literature;

import java.io.File;

/**
 * Created on 16/11/22.
 * Created by crazylei.
 */

public interface LiteratureReviseInterface {

    interface Presenter extends BasePresenter {

        void initParameter(Literature literature);

        void checkLiteratureCompleteState(String key, String value);

        boolean verificationInformation();

        void reviseLiterature(File file);

    }

    interface View extends BaseView<Presenter> {

        void initView(int descLimit);

        void showErrorView();

        void checkLiteratureCreateState(boolean state);

        void showToast(String message);

        void finishActivity();

        void startLoginActivity();
    }
}
