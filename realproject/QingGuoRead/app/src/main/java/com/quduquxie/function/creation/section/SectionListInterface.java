package com.quduquxie.function.creation.section;

import com.quduquxie.function.BasePresenter;
import com.quduquxie.function.BaseView;
import com.quduquxie.model.creation.Literature;
import com.quduquxie.model.creation.Section;
import com.quduquxie.widget.LoadingPage;

import java.util.ArrayList;

/**
 * Created on 17/4/10.
 * Created by crazylei.
 */

public interface SectionListInterface {

    interface Presenter extends BasePresenter {

        void loadSectionList(Literature literature);

        void setLoadingPage(LoadingPage loadingPage);

    }

    interface View extends BaseView<Presenter> {

        void initView();

        void showLoadingPage();

        void hideLoadingPage();

        void setSectionData(ArrayList<Section> sections);

        void showToast(String message);

        void startLoginActivity();

    }
}
