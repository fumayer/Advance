package com.quduquxie.communal.widget.expression;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.DynamicDrawableSpan;
import android.util.AttributeSet;

import com.quduquxie.R;

public class ExpressionTextView extends AppCompatTextView {
    private float expression_icon_size;
    private float expression_text_size;
    private int expression_align_type;

    public ExpressionTextView(Context context) {
        this(context, null);
    }

    public ExpressionTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpressionTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        Resources resources = context.getResources();

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpressionView);
            int expressionSize = typedArray.getResourceId(R.styleable.ExpressionView_expressionSize, R.dimen.width_50);
            expression_icon_size = resources.getDimension(expressionSize);
            expression_text_size = resources.getDimension(expressionSize);

            expression_align_type = typedArray.getInteger(R.styleable.ExpressionView_expressionAlignType, DynamicDrawableSpan.ALIGN_BASELINE);

            typedArray.recycle();
        } else {
            expression_icon_size = getTextSize();
            expression_text_size = getTextSize();
            expression_align_type = DynamicDrawableSpan.ALIGN_BASELINE;
        }
        setText(getText());
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (!TextUtils.isEmpty(text)) {
            SpannableStringBuilder builder = new SpannableStringBuilder(text);
            ExpressionHandler.addExpression(getContext(), builder, (int) expression_icon_size, expression_align_type, (int) expression_text_size, 0, -1);
            text = builder;
        }
        super.setText(text, type);
    }
}