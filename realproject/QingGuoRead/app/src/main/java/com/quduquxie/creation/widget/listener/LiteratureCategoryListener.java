package com.quduquxie.creation.widget.listener;

/**
 * Created on 16/11/24.
 * Created by crazylei.
 */

public interface LiteratureCategoryListener {

    void setCurrentType(int index);

    void onCategoryClicked(String category);

    void clearClickedCategory();

}
