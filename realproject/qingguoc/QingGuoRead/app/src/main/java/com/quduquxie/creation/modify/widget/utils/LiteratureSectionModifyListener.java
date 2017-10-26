package com.quduquxie.creation.modify.widget.utils;

import com.quduquxie.model.v2.SensitiveWord;

import java.util.ArrayList;

/**
 * Created on 16/11/18.
 * Created by crazylei.
 */

public interface LiteratureSectionModifyListener {

    void setTitleLimit(int length);

    void setSensitiveWord(ArrayList<SensitiveWord> sensitiveWords);

    void initChapterInformation(String title, String content);

    String getTitle();

    String getContent();

    void recycleData();
}
