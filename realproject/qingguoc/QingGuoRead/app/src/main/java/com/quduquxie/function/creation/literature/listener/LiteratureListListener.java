package com.quduquxie.function.creation.literature.listener;

import com.quduquxie.model.creation.Literature;

/**
 * Created on 16/12/8.
 * Created by crazylei.
 */

public interface LiteratureListListener {

    void startLiteratureDetailed(Literature literature);

    void startLiteratureChapterCreate(Literature literature);

    void startLiteratureChapterManager(Literature literature);

    void startLiteratureRevise(Literature literature);

    void deleteLiterature(int position, Literature literature);

    void showToast(String message);

    void showReviserUser();
}
