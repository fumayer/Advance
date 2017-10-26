package com.quduquxie.function.creation.literature;

import com.quduquxie.function.BasePresenter;
import com.quduquxie.function.BaseView;
import com.quduquxie.model.creation.Literature;
import com.quduquxie.widget.LoadingPage;

import java.util.ArrayList;

/**
 * Created on 17/4/10.
 * Created by crazylei.
 */

public interface LiteratureListInterface {

    interface Presenter extends BasePresenter {

        void loadLiteratureList();

        void deleteLiterature(int position, Literature literature);

        void setLoadingPage(LoadingPage loadingPage);

    }

    interface View extends BaseView<Presenter> {

        void initView();

        void showLoadingPage();

        void hideLoadingPage();

        void setLiteratureData(ArrayList<Literature> literatureList);

        void showToast(String message);

        void deleteLiterature(int position);

        void startLoginActivity();
    }
}
