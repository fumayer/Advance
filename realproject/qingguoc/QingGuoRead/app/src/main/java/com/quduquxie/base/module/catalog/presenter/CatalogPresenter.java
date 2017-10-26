package com.quduquxie.base.module.catalog.presenter;

import com.quduquxie.base.RxPresenter;
import com.quduquxie.base.module.catalog.CatalogInterface;
import com.quduquxie.base.module.catalog.view.CatalogActivity;

/**
 * Created on 17/7/27.
 * Created by crazylei.
 */

public class CatalogPresenter extends RxPresenter implements CatalogInterface.Presenter {

    private CatalogActivity catalogActivity;

    public CatalogPresenter(CatalogActivity catalogActivity) {
        this.catalogActivity = catalogActivity;
    }

    @Override
    public void recycle() {
        if (catalogActivity != null) {
            catalogActivity = null;
        }
    }
}
