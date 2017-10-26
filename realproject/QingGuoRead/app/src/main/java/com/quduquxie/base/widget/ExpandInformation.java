package com.quduquxie.base.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.quduquxie.R;

public class ExpandInformation extends FrameLayout implements View.OnClickListener {

    private Context context;

    private String content;

    private TextView description;
    private TextView promptTextView;
    private ImageView promptImageView;

    private int textSize;
    private int textColor;

    private int maxLines;

    private int promptText;
    private int promptColor;

    private int promptTextWidth;

    private int expandType = TYPE_DEFAULT;

    private static final int TYPE_DEFAULT = 0x80;
    private static final int TYPE_EXPAND = 0x81;
    private static final int TYPE_DISPLAY = 0x82;

    public ExpandInformation(Context context) {
        this(context, null);
    }

    public ExpandInformation(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandInformation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        Resources resources = context.getResources();

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandInformation);

            int size = typedArray.getResourceId(R.styleable.ExpandInformation_informationTextSize, R.dimen.text_size_28);
            textSize = resources.getDimensionPixelSize(size);

            textColor = typedArray.getColor(R.styleable.ExpandInformation_informationTextColor, 0xFF3E3E3E);

            maxLines = typedArray.getInteger(R.styleable.ExpandInformation_informationMaxLines, 2);


            promptText = typedArray.getResourceId(R.styleable.ExpandInformation_informationPromptText, R.string.read_more);
            promptColor = typedArray.getColor(R.styleable.ExpandInformation_informationTextColor, 0xFF0094D5);

            int width = typedArray.getResourceId(R.styleable.ExpandInformation_informationPromptWidth, R.dimen.width_130);

            promptTextWidth = resources.getDimensionPixelSize(width);
            typedArray.recycle();
        } else {
            textSize = resources.getDimensionPixelSize(R.dimen.text_size_28);

            textColor = Color.parseColor("#3E3E3E");

            maxLines = 2;

            promptText = R.string.read_more;

            promptColor = Color.parseColor("#0094D5");

            promptTextWidth = resources.getDimensionPixelSize(R.dimen.width_130);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);

        if (!TextUtils.isEmpty(content)) {

            if (description == null) {
                initDescription();
            }

            measureChild(description, widthMeasureSpec, heightMeasureSpec);

            if (expandType == TYPE_DISPLAY) {
                description.setMaxLines(100);
            } else if (expandType == TYPE_EXPAND) {
                description.setMaxLines(maxLines);
            } else {
                if (description.getLineCount() > maxLines) {
                    expandType = TYPE_EXPAND;
                    description.setMaxLines(maxLines);

                    if (promptTextView == null) {
                        initParameter();
                    }
                } else {
                    expandType = TYPE_DISPLAY;
                    description.setMaxLines(200);
                }
            }

            if (promptTextView != null) {
                measureChild(promptTextView, widthMeasureSpec, heightMeasureSpec);
            }

            measureChild(description, widthMeasureSpec, heightMeasureSpec);

            int measureHeight = description.getMeasuredHeight();
            setMeasuredDimension(measureWidth, measureHeight);
        } else {
            setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
        }
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    public void initializeContent(String content) {
        this.content = content;

        if (description == null) {
            initDescription();
        }
    }

    private void initDescription() {
        description = new AutoSetTextView(context);
        description.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        description.setTextColor(textColor);
        description.setLineSpacing(10f, 1f);
        description.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);

        description.setText(content);

        LayoutParams contentLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        addView(description, contentLayoutParams);
    }

    private void initParameter() {

        promptTextView = new TextView(context);
        promptTextView.setText(promptText);
        promptTextView.setLineSpacing(10f, 1f);
        promptTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        promptTextView.setTextColor(Color.parseColor("#0094D5"));
        promptTextView.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        promptTextView.setOnClickListener(this);
        promptTextView.setBackgroundResource(R.drawable.background_expand_information_prompt);

        LayoutParams textLayoutParams = new LayoutParams(promptTextWidth, LayoutParams.WRAP_CONTENT);
        textLayoutParams.gravity = Gravity.END | Gravity.BOTTOM;

        Logger.e("Height: " + description.getLineHeight());

        addView(promptTextView, textLayoutParams);
    }

    @Override
    public void onClick(View view) {
        if (promptTextView == view || promptImageView == view) {
            description.setMaxLines(100);
            expandType = TYPE_DISPLAY;

            if (promptTextView != null) {
                removeView(promptTextView);
            }

            if (promptImageView != null) {
                removeView(promptImageView);
            }
        }
    }
}