package com.quduquxie.function.creation.write;

import com.quduquxie.function.BasePresenter;
import com.quduquxie.function.BaseView;
import com.quduquxie.model.creation.Draft;
import com.quduquxie.model.creation.Literature;
import com.quduquxie.model.v2.SensitiveWord;
import com.quduquxie.widget.LoadingPage;

import java.util.ArrayList;

/**
 * Created on 17/4/10.
 * Created by crazylei.
 */

public interface SectionWriteInterface {

    interface Presenter extends BasePresenter {
        void initParameter(Literature literature);

        void initDraft(Draft draft);

        void saveDraft(String title, String content);

        void reviseDraft(Draft draft, String title, String content);

        void filterSensitiveWord(String content);

        void publishChapter(Draft draft, String title, String content);

        void setLoadingPage(LoadingPage loadingPage);

        void createDraft(String title, String content);

        void updateDraft(Draft draft, String title, String content);
    }

    interface View extends BaseView<Presenter> {

        void initView(int limit, String title);

        void setChapterInformation(String title, String content);

        void showWriteView();

        void showErrorView();

        void showToast(String message);

        void finishActivity();

        void startLoginActivity();

        void refreshPublishButtonState(boolean state);

        void refreshSaveButtonState(boolean state);

        void showLoadingPage();

        void hideLoadingPage();

        void showLoadingError();

        void setAutoUpdateDraft(Draft draft);

        void highlightSensitiveWord(ArrayList<SensitiveWord> sensitiveWords);

        void showPromptDialog(String message);

        void publishChapter();
    }
}
