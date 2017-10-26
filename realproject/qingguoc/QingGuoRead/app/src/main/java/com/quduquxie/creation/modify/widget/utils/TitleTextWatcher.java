package com.quduquxie.creation.modify.widget.utils;

import android.text.Editable;
import android.text.TextWatcher;

import com.quduquxie.util.QGLog;

/**
 * Created on 16/11/18.
 * Created by crazylei.
 */

public class TitleTextWatcher implements TextWatcher {

    private static final String TAG = TitleTextWatcher.class.getSimpleName();

    private TitleCountChangeListener titleCountChangeListener;

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        QGLog.e(TAG, "beforeTextChanged: " + s + " : " + start + " : " + count + " : " + after);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        QGLog.e(TAG, "onTextChanged: " + s + " : " + start + " : " + before + " : " + count);
    }

    @Override
    public void afterTextChanged(Editable s) {
        QGLog.e(TAG, "afterTextChanged: " + s);
        if (titleCountChangeListener != null) {
            titleCountChangeListener.setTitleCountChanged(s.length());
        }
    }

    public void setTitleCountChangeListener(TitleCountChangeListener titleCountChangeListener) {
        this.titleCountChangeListener = titleCountChangeListener;
    }

    public interface TitleCountChangeListener {
        void setTitleCountChanged(int length);
    }
}
