package com.quduquxie.creation.revise.widget.utils;

import android.text.Editable;
import android.text.TextWatcher;

import com.quduquxie.util.QGLog;

/**
 * Created on 16/11/23.
 * Created by crazylei.
 */

public class DescTextWatcher implements TextWatcher {

    private static final String TAG = DescTextWatcher.class.getSimpleName();

    private DescCountChangeListener descCountChangeListener;

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
        if (descCountChangeListener != null) {
            descCountChangeListener.setDescCountChanged(s.toString());
        }
    }

    public void setDescCountChangeListener(DescCountChangeListener descCountChangeListener) {
        this.descCountChangeListener = descCountChangeListener;
    }

    public interface DescCountChangeListener {
        void setDescCountChanged(String description);
    }
}
