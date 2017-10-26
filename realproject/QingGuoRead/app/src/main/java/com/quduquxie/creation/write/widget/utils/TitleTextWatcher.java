package com.quduquxie.creation.write.widget.utils;

import android.text.Editable;
import android.text.TextWatcher;

import com.orhanobut.logger.Logger;
import com.quduquxie.util.QGLog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created on 16/11/18.
 * Created by crazylei.
 */

public class TitleTextWatcher implements TextWatcher {

    private static final String TAG = TitleTextWatcher.class.getSimpleName();

    private TitleCountChangeListener titleCountChangeListener;

    private String compile = "[^\\u4e00-\\u9fa50-9a-zA-Z，。：“”——' ']";
    private Pattern pattern;

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        QGLog.e(TAG, "beforeTextChanged: " + s + " : " + start + " : " + count + " : " + after);
    }

    @Override
    public void onTextChanged(CharSequence sequence, int start, int before, int count) {
        QGLog.e(TAG, "onTextChanged: " + sequence + " : " + start + " : " + before + " : " + count);
        String content = sequence.toString();
        if (titleFilter(content)) {
            if (titleCountChangeListener != null) {
                titleCountChangeListener.checkTitleInput();
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (titleCountChangeListener != null) {
            titleCountChangeListener.setTitleCountChanged(editable.length());
        }
    }

    public void setTitleCountChangeListener(TitleCountChangeListener titleCountChangeListener) {
        this.titleCountChangeListener = titleCountChangeListener;
    }

    public interface TitleCountChangeListener {
        void setTitleCountChanged(int length);

        void checkTitleInput();
    }

    private boolean titleFilter(String content) throws PatternSyntaxException {
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
