package com.quduquxie.common.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.quduquxie.R;

import java.lang.ref.WeakReference;

/**
 * Created on 17/5/24.
 * Created by crazylei.
 */

public class ExpandTextView extends FrameLayout {

    private WeakReference<Context> contextReference;

    private float textSize;
    private int textColor;

    private int maxLines;

    private String promptText;
    private int promptTextColor;

    private String content;

    private TextView contentView;

    public ExpandTextView(@NonNull Context context) {
        this(context, null);
    }

    public ExpandTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandTextView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.contextReference = new WeakReference<>(context);
        Resources resources = contextReference.get().getResources();

        if (attrs != null) {
            TypedArray typedArray = contextReference.get().obtainStyledAttributes(attrs, R.styleable.ExpandTextView);

            int text_size = typedArray.getResourceId(R.styleable.ExpandTextView_expandTextSize, R.dimen.text_size_30);
            textSize = resources.getDimensionPixelSize(text_size);

            textColor = typedArray.getColor(R.styleable.ExpandTextView_expandTextColor, 0xFF3A3A3A);

            maxLines = typedArray.getInteger(R.styleable.ExpandTextView_expandMaxLines, 4);

            int prompt_text = typedArray.getResourceId(R.styleable.ExpandTextView_expandPromptText, R.string.read_more);
            promptText = resources.getString(prompt_text);

            promptTextColor = typedArray.getColor(R.styleable.ExpandTextView_expandPromptTextColor, 0xFF366590);

            typedArray.recycle();
        } else {
            textSize = resources.getDimensionPixelSize(R.dimen.text_size_30);
            textColor = Color.parseColor("#3A3A3A");

            maxLines = 4;

            promptText = resources.getString(R.string.read_more);
            promptTextColor = Color.parseColor("#366590");
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int view_width = MeasureSpec.getSize(widthMeasureSpec);

        if (!TextUtils.isEmpty(content)) {

            initViewHeight();

            measureChildren(widthMeasureSpec, heightMeasureSpec);

            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        } else {
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private void initViewHeight() {
        if (contentView != null) {
            Logger.d("Count: " + contentView.getLineCount());
        }
    }

    public void setContent(String content) {
        this.content = content;

        contentView = new AutoSetTextView(contextReference.get());
        contentView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        contentView.setTextColor(textColor);
        contentView.setLineSpacing(10f, 1f);
        contentView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

        contentView.setText(content);

        LayoutParams contentLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        addView(contentView, contentLayoutParams);
    }
}     