package com.quduquxie.creation.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;

/**
 * Created on 16/11/18.
 * Created by crazylei.
 */

public class LiteratureEditText extends AppCompatEditText {

    public LiteratureEditText(Context context) {
        super(context);
    }

    public LiteratureEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LiteratureEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        return super.onKeyPreIme(keyCode, event);
    }
}