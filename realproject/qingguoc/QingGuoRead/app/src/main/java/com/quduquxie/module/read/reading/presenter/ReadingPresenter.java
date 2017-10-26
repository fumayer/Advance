package com.quduquxie.module.read.reading.presenter;

import com.quduquxie.base.RxPresenter;
import com.quduquxie.module.read.reading.view.ReadingActivity;

/**
 * Created on 17/8/1.
 * Created by crazylei.
 */

public class ReadingPresenter extends RxPresenter {

    private ReadingActivity readingActivity;

    public ReadingPresenter(ReadingActivity readingActivity) {
        this.readingActivity = readingActivity;
    }

    @Override
    public void recycle() {

    }
}
