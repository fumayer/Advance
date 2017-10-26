package com.quduquxie.creation.modify.widget.utils;

import android.text.Editable;
import android.text.TextWatcher;

import com.quduquxie.util.QGLog;

/**
 * Created on 16/11/18.
 * Created by crazylei.
 */

public class ContentTextWatcher implements TextWatcher {

    private static final String TAG = ContentTextWatcher.class.getSimpleName();

    private ContentCountChangeListener contentCountChangeListener;

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
        if (contentCountChangeListener != null) {
            contentCountChangeListener.setContentCountChanged(s.length());
        }
    }

    public void setContentCountChangeListener(ContentCountChangeListener contentCountChangeListener) {
        this.contentCountChangeListener = contentCountChangeListener;
    }

    public interface ContentCountChangeListener {
        void setContentCountChanged(int length);
    }
}
