package com.quduquxie.creation.modify.widget.utils;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 16/11/21.
 * Created by crazylei.
 */

public class CustomInputFilter implements InputFilter {

    private static final String TAG = com.quduquxie.creation.write.widget.utils.CustomInputFilter.class.getSimpleName();

    private Pattern pattern = Pattern.compile("(\\n|\\r|\\r\\n){1,100}");
    private Pattern enterPattern = Pattern.compile("(\\n|\\r|\\r\\n){1,100}(\\s{0,100})");

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dest_start, int dest_end) {

        if ("".equals(source.toString())) {
            return null;
        }

        String string = source.toString();

        Matcher matcher = enterPattern.matcher(string);

        while (matcher.find()) {
            String group = matcher.group();
            string = string.replace(group, "\n");
        }

        matcher = pattern.matcher(string);
        while (matcher.find()) {
            String group = matcher.group();
            string = string.replace(group, "\n");
        }

        boolean flag = true;
        while (flag) {
            if (string.startsWith(" ")) {
                string = string.replace(" ", "");
            } else {
                flag = false;
            }
        }

        return string;
    }
}
