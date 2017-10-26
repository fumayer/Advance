package com.quduquxie.creation.modify;

import com.quduquxie.BasePresenter;
import com.quduquxie.BaseView;
import com.quduquxie.model.creation.Section;
import com.quduquxie.model.v2.SensitiveWord;

import java.util.ArrayList;

/**
 * Created on 16/11/17.
 * Created by crazylei.
 */

public interface LiteratureSectionModifyInterface {

    interface Presenter extends BasePresenter {

        void initParameter(Section section);

        void initSection(Section section);

        void filterSensitiveWord(String content);

        void publishChapter(Section section, String title, String content);
    }

    interface View extends BaseView<Presenter> {

        void initView(int limit, String title);

        void setChapterInformation(String title, String content);

        void showToast(String message);

        void finishActivity();

        void startLoginActivity();

        void refreshPublishButtonState(boolean state);

        void highlightSensitiveWord(ArrayList<SensitiveWord> sensitiveWords);

        void showPromptDialog(String message);

        void publishChapter();
    }
}
