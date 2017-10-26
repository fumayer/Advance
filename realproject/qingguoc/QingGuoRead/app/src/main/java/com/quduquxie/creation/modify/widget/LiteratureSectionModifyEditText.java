package com.quduquxie.creation.modify.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;
import com.quduquxie.R;
import com.quduquxie.util.QGLog;

/**
 * Created on 16/11/18.
 * Created by crazylei.
 */

public class LiteratureSectionModifyEditText extends AppCompatEditText {

    private static final String TAG = LiteratureSectionModifyEditText.class.getSimpleName();

    public LiteratureSectionModifyEditText(Context context) {
        super(context);
    }

    public LiteratureSectionModifyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomTypeface(attrs);
    }

    public LiteratureSectionModifyEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomTypeface(attrs);
    }

    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        return super.onKeyPreIme(keyCode, event);
    }

    private void setCustomTypeface(AttributeSet attrs) {
        TypedArray values = getContext().obtainStyledAttributes(attrs, R.styleable.LiteratureSectionModifyEditText);
        String typefaceName = values.getString(R.styleable.LiteratureSectionModifyEditText_fontModifyFile);
        if (typefaceName != null) {
            try {
                Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + typefaceName);
                this.setTypeface(typeface);
            } catch (RuntimeException e) {
                QGLog.e(TAG, "Could not load typeface " + typefaceName);
            }
        }
        values.recycle();
    }
}