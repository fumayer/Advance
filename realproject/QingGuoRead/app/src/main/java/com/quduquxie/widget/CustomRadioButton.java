package com.quduquxie.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.view.Gravity;

public class CustomRadioButton extends AppCompatRadioButton {

    public CustomRadioButton(Context context) {
        super(context);
    }

    public CustomRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        Drawable drawableLeft = drawables[0];
        if (drawableLeft != null) {

            float textWidth = getPaint().measureText(getText().toString());

            int drawableWidth = drawableLeft.getIntrinsicWidth();
            int drawablePadding = getCompoundDrawablePadding();

            float bodyWidth = textWidth + drawableWidth + drawablePadding;

            int width = (int) (getWidth() - bodyWidth);
            setPadding(0, 0, width, 0);

            canvas.translate(width / 2, 0);
        }
        super.onDraw(canvas);
    }
}