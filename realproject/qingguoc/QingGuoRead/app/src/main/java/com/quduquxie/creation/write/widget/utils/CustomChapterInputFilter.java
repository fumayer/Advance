package com.quduquxie.creation.write.widget.utils;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 16/11/21.
 * Created by crazylei.
 */

public class CustomChapterInputFilter implements InputFilter {

    private static final String TAG = CustomChapterInputFilter.class.getSimpleName();

    private Pattern pattern = Pattern.compile("[^\\u4e00-\\u9fa50-9a-zA-Z，。：“”——' ']");

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dest_start, int dest_end) {

        if ("".equals(source.toString())) {
            return null;
        }

        String string = source.toString();

        Matcher matcher = pattern.matcher(string);

        while (matcher.find()) {
            String group = matcher.group();
            string = string.replaceAll(group, "");
        }

        return string;
    }
}
