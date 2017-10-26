package com.quduquxie.base.module.reading.catalog.presenter;

import com.quduquxie.base.RxPresenter;
import com.quduquxie.base.module.reading.catalog.ReadingCatalogInterface;
import com.quduquxie.base.module.reading.catalog.view.ReadingCatalogActivity;

/**
 * Created on 17/7/27.
 * Created by crazylei.
 */

public class ReadingCatalogPresenter extends RxPresenter implements ReadingCatalogInterface.Presenter {

    private ReadingCatalogActivity readingCatalogActivity;

    public ReadingCatalogPresenter(ReadingCatalogActivity readingCatalogActivity) {
        this.readingCatalogActivity = readingCatalogActivity;
    }

    @Override
    public void recycle() {
        if (readingCatalogActivity != null) {
            readingCatalogActivity = null;
        }
    }
}