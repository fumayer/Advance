package com.quduquxie.creation.write.widget.utils;

import com.quduquxie.model.v2.SensitiveWord;

import java.util.ArrayList;

/**
 * Created on 16/11/18.
 * Created by crazylei.
 */

public interface LiteratureWriteListener {

    void setTitleLimit(int length);

    void initChapterInformation(String title, String content);

    void setSensitiveWord(ArrayList<SensitiveWord> sensitiveWords);

    String getTitle();

    String getContent();

    void recycleData();
}
