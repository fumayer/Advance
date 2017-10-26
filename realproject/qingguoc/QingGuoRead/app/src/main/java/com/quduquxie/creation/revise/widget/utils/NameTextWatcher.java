package com.quduquxie.creation.revise.widget.utils;

import android.text.Editable;
import android.text.TextWatcher;

import com.quduquxie.util.QGLog;

/**
 * Created on 16/11/23.
 * Created by crazylei.
 */

public class NameTextWatcher implements TextWatcher {

    private static final String TAG = NameTextWatcher.class.getSimpleName();

    private NameCountChangeListener nameCountChangeListener;

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
        if (nameCountChangeListener != null) {
            nameCountChangeListener.setNameCountChanged(s.toString());
        }
    }

    public void setNameCountChangeListener(NameCountChangeListener nameCountChangeListener) {
        this.nameCountChangeListener = nameCountChangeListener;
    }

    public interface NameCountChangeListener {
        void setNameCountChanged(String name);
    }
}
