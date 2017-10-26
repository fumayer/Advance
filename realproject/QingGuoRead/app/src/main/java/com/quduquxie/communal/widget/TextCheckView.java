package com.quduquxie.communal.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.quduquxie.R;

/**
 * Created on 17/2/15.
 * Created by crazylei.
 */

public class TextCheckView extends RelativeLayout {

    private int text;
    private int textColor;
    private int textMaxLines;

    private TextView textView;
    private Switch switchView;

    private float text_size;

    private int text_margin_left;
    private int text_margin_right;

    private int switch_margin_left;
    private int switch_margin_right;

    public TextCheckView(Context context) {
        this(context, null);
    }

    public TextCheckView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextCheckView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        Resources resources = context.getResources();

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextCheckView);

            text = typedArray.getResourceId(R.styleable.TextCheckView_checkText, R.string.push_updates);
            int textSize = typedArray.getResourceId(R.styleable.TextCheckView_checkTextSize, R.dimen.text_size_30);
            text_size = resources.getDimensionPixelSize(textSize);

            textColor = typedArray.getColor(R.styleable.TextCheckView_checkTextColor, 0xFF191919);

            textMaxLines = typedArray.getInteger(R.styleable.TextCheckView_textMaxLines, 1);

            int textMarginLeft = typedArray.getResourceId(R.styleable.TextCheckView_textMarginLeft, R.dimen.width_32);
            text_margin_left = resources.getDimensionPixelSize(textMarginLeft);

            int textMarginRight = typedArray.getResourceId(R.styleable.TextCheckView_textMarginRight, R.dimen.width_10);
            text_margin_right = resources.getDimensionPixelSize(textMarginRight);

            int switchMarginLeft = typedArray.getResourceId(R.styleable.TextCheckView_switchMarginLeft, R.dimen.width_10);
            switch_margin_left = resources.getDimensionPixelSize(switchMarginLeft);

            int switchMarginRight = typedArray.getResourceId(R.styleable.TextCheckView_switchMarginRight, R.dimen.width_32);
            switch_margin_right = resources.getDimensionPixelSize(switchMarginRight);

            typedArray.recycle();
        }

        initView(context);
    }


    private void initView(Context context) {
        textView = new TextView(context);
        textView.setText(text);
        textView.setTextColor(textColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, text_size);
        textView.setLines(textMaxLines);
        textView.setMaxLines(textMaxLines);
        textView.setGravity(Gravity.CENTER_VERTICAL);

        LayoutParams textLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        textLayoutParams.addRule(ALIGN_PARENT_LEFT);
        textLayoutParams.addRule(CENTER_VERTICAL);
        textLayoutParams.setMargins(text_margin_left, 0, text_margin_right, 0);

        addView(textView, textLayoutParams);

        switchView = new Switch(context);
        switchView.setDuplicateParentStateEnabled(false);
        switchView.setFocusable(false);
        switchView.setFocusableInTouchMode(false);
        switchView.setClickable(false);

        LayoutParams switchLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        switchLayoutParams.addRule(ALIGN_PARENT_RIGHT);
        switchLayoutParams.addRule(CENTER_VERTICAL);
        switchLayoutParams.setMargins(switch_margin_left, 0, switch_margin_right, 0);

        addView(switchView, switchLayoutParams);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setChecked(boolean checked) {
        switchView.setChecked(checked);
    }
}
