package com.quduquxie.function.creation.draft;

import com.quduquxie.function.BasePresenter;
import com.quduquxie.function.BaseView;
import com.quduquxie.model.creation.Draft;
import com.quduquxie.model.creation.Literature;
import com.quduquxie.widget.LoadingPage;

import java.util.ArrayList;

/**
 * Created on 17/4/10.
 * Created by crazylei.
 */

public interface DraftListInterface {

    interface Presenter extends BasePresenter {

        void loadDraftList(Literature literature);

        void deleteDraft(Draft draft, int position);

        void setLoadingPage(LoadingPage loadingPage);
    }

    interface View extends BaseView<Presenter> {

        void initView();

        void showLoadingPage();

        void hideLoadingPage();

        void setLiteratureDraftData(ArrayList<Draft> draftData);

        void showToast(String message);

        void deleteDraft(Draft draft);

        void startLoginActivity();
    }
}
