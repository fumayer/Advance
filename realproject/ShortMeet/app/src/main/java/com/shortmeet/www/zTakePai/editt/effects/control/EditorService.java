/*
 * Copyright (C) 2010-2017 Alibaba Group Holding Limited.
 */

package com.shortmeet.www.zTakePai.editt.effects.control;

import android.graphics.Paint;

import java.util.HashMap;
import java.util.Map;

public class EditorService {
    private Map<UIEditorPage, Integer> mMap = new HashMap<>();
    private boolean isFullScreen;
    private Paint mPaint;

    public void addTabEffect(UIEditorPage uiEditorPage, int id) {
        mMap.put(uiEditorPage, id);
    }

    public int getEffectIndex(UIEditorPage uiEditorPage) {
        if(mMap.containsKey(uiEditorPage)) {
            return mMap.get(uiEditorPage);
        } else {
            return 0;
        }
    }

    public boolean isFullScreen() {
        return isFullScreen;
    }

    public void setFullScreen(boolean fullScreen) {
        isFullScreen = fullScreen;
    }

    public Paint getPaint() {
        return mPaint;
    }

    public void setPaint(Paint mPaint) {
        this.mPaint = mPaint;
    }
}
