package com.quduquxie.communal.widget.expression;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatEditText;
import android.text.style.DynamicDrawableSpan;
import android.util.AttributeSet;

import com.quduquxie.R;

/**
 * Created on 17/3/4.
 * Created by crazylei.
 */

public class ExpressionEditText extends AppCompatEditText {
    private float expression_size;
    private float expression_text_size;
    private int expression_align_type;

    public ExpressionEditText(Context context) {
        this(context, null);
    }

    public ExpressionEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpressionEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        Resources resources = context.getResources();

        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ExpressionView);
            int expressionSize = typedArray.getResourceId(R.styleable.ExpressionView_expressionSize, R.dimen.text_size_28);
            expression_size = resources.getDimension(expressionSize);
            expression_text_size = resources.getDimension(expressionSize);

            expression_align_type = typedArray.getInteger(R.styleable.ExpressionView_expressionAlignType, DynamicDrawableSpan.ALIGN_BASELINE);

            typedArray.recycle();
        } else {
            expression_size = getTextSize();
            expression_text_size = getTextSize();
            expression_align_type = DynamicDrawableSpan.ALIGN_BASELINE;
        }

        setText(getText());
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        updateText();
    }

    private void updateText() {
        ExpressionHandler.addExpression(getContext(), getText(), expression_size, expression_align_type, expression_text_size);
    }
}