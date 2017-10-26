package com.quduquxie.creation.write.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;

/**
 * Created on 16/11/18.
 * Created by crazylei.
 */

public class LiteratureWriteEditText extends AppCompatEditText {

    private static final String TAG = LiteratureWriteEditText.class.getSimpleName();

    public LiteratureWriteEditText(Context context) {
        super(context);
    }

    public LiteratureWriteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LiteratureWriteEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        return super.onKeyPreIme(keyCode, event);
    }
}