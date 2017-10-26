package com.quduquxie.module.comment.util;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;

/**
 * Created on 17/2/28.
 * Created by crazylei.
 */

public abstract class BasicClickableSpan extends ClickableSpan {

    @Override
    public void updateDrawState(TextPaint textPaint) {
        super.updateDrawState(textPaint);
        textPaint.setColor(Color.parseColor("#366590"));
        textPaint.setUnderlineText(false);
    }
}
