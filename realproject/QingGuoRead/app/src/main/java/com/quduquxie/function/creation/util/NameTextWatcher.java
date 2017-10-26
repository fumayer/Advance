package com.quduquxie.function.creation.util;

import android.text.Editable;
import android.text.TextWatcher;

import com.orhanobut.logger.Logger;
import com.quduquxie.util.QGLog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created on 16/11/23.
 * Created by crazylei.
 */

public class NameTextWatcher implements TextWatcher {

    private static final String TAG = NameTextWatcher.class.getSimpleName();

    private NameCountChangeListener nameCountChangeListener;

    private String compile = "[^\\u4e00-\\u9fa50-9a-zA-Z，。：“”——]";
    private Pattern pattern;

    @Override
    public void beforeTextChanged(CharSequence sequence, int start, int count, int after) {
        QGLog.e(TAG, "beforeTextChanged: " + sequence + " : " + start + " : " + count + " : " + after);
    }

    @Override
    public void onTextChanged(CharSequence sequence, int start, int before, int count) {
        QGLog.e(TAG, "onTextChanged: " + sequence + " : " + start + " : " + before + " : " + count);
        String content = sequence.toString();
        if (nameFilter(content)) {
            if (nameCountChangeListener != null) {
                nameCountChangeListener.checkNameInput();
            }
        }
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

        void checkNameInput();
    }

    private boolean nameFilter(String content) throws PatternSyntaxException {
        if (pattern == null) {
            pattern  = Pattern.compile(compile);
        }
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            Logger.d(matcher.group());
            return true;
        }
        return false;
    }
}
