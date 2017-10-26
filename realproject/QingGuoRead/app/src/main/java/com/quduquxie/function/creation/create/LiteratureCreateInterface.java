package com.quduquxie.function.creation.create;

import com.quduquxie.function.BasePresenter;
import com.quduquxie.function.BaseView;
import com.quduquxie.widget.LoadingPage;

import java.io.File;

/**
 * Created on 17/4/10.
 * Created by crazylei.
 */

public interface LiteratureCreateInterface {

    interface Presenter extends BasePresenter {

        void loadLiteratureTag();

        void checkLiteratureCompleteState(String key, String value);

        boolean verificationInformation();

        void createLiterature(File file);

        void setLoadingPage(LoadingPage loadingPage);

    }

    interface View extends BaseView<Presenter> {

        void showLoadingPage();

        void hideLoadingPage();

        void initView(int nameLimit, int descLimit);

        void checkLiteratureCreateState(boolean state);

        void showToast(String message);

        void finishActivity();

        void startLoginActivity();
    }
}
