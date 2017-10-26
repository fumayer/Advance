package com.quduquxie.communal.widget.expand;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.DynamicDrawableSpan;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.quduquxie.communal.widget.expression.ExpressionHandler;

public class ExpressionDisplayTextView extends AppCompatTextView {

    private float expression_icon_size;
    private float expression_text_size;

    public ExpressionDisplayTextView(Context context) {
        this(context, null);
    }

    public ExpressionDisplayTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpressionDisplayTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setText(getText());
    }

    public ExpressionDisplayTextView(Context context, float expression_icon_size, float expression_text_size) {
        this(context, null);
        this.expression_text_size = expression_text_size;
        this.expression_icon_size = expression_icon_size;
    }

    @Override
    public void setTextSize(float size) {
        super.setTextSize(TypedValue.COMPLEX_UNIT_PX, expression_text_size);
    }

    @Override
    public void setText(CharSequence charSequence, BufferType bufferType) {
        if (!TextUtils.isEmpty(charSequence)) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(charSequence);
            ExpressionHandler.addExpression(getContext(), spannableStringBuilder, (int) expression_icon_size, DynamicDrawableSpan.ALIGN_BASELINE, (int) expression_text_size, 0, -1);
            charSequence = spannableStringBuilder;
        }
        super.setText(charSequence, bufferType);
    }
}