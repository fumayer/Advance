package com.quduquxie.communal.widget.expand;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Layout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.quduquxie.R;

import java.lang.ref.WeakReference;

/**
 * Created on 17/3/9.
 * Created by crazylei.
 */

public class ExpandExpressionTextView extends FrameLayout implements View.OnClickListener {

    private WeakReference<Context> contextReference;

    private float expression_text_size;
    private float expression_icon_size;
    private int expression_text_color;
    private int expression_max_lines;
    private int expression_prompt_text;
    private int expression_prompt_text_color;

    private String content;

    private TextView prompt_view;

    private ExpressionDisplayTextView content_view;

    private String ellipses = "... ";

    private String show_text;

    private static final int CONTENT_DISPLAY = 0x51;
    private static final int CONTENT_DEPLOY = 0x52;
    private static final int CONTENT_RETRACT = 0x53;
    private static final int CONTENT_DEFAULT = 0x54;

    private int content_state;

    private int deploy_height;
    private int display_height;
    private int retract_height;

    private int line_height;


    public ExpandExpressionTextView(Context context) {
        this(context, null);
    }

    public ExpandExpressionTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandExpressionTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.contextReference = new WeakReference<>(context);
        Resources resources = contextReference.get().getResources();

        if (attrs != null) {
            TypedArray typedArray = contextReference.get().obtainStyledAttributes(attrs, R.styleable.ExpandExpressionTextView);

            int expressionTextSize = typedArray.getResourceId(R.styleable.ExpandExpressionTextView_expandExpressionTextSize, R.dimen.text_size_28);
            expression_text_size = resources.getDimensionPixelSize(expressionTextSize);

            int expressionIconSize = typedArray.getResourceId(R.styleable.ExpandExpressionTextView_expandExpressionIconSize, R.dimen.width_28);
            expression_icon_size = resources.getDimensionPixelSize(expressionIconSize);

            expression_text_color = typedArray.getColor(R.styleable.ExpandExpressionTextView_expandExpressionTextColor, 0xFF191919);
            expression_max_lines = typedArray.getInteger(R.styleable.ExpandExpressionTextView_expandExpressionMaxLines, 4);

            expression_prompt_text = typedArray.getResourceId(R.styleable.ExpandExpressionTextView_expandExpressionPromptText, R.string.read_more);
            expression_prompt_text_color = typedArray.getColor(R.styleable.ExpandExpressionTextView_expandExpressionPromptTextColor, 0xFF366590);

            typedArray.recycle();
        } else {
            expression_text_size = resources.getDimensionPixelSize(R.dimen.text_size_28);
            expression_icon_size = resources.getDimensionPixelSize(R.dimen.width_28);
            expression_text_color = Color.parseColor("#191919");
            expression_max_lines = 4;
            expression_prompt_text = R.string.read_more;
            expression_prompt_text_color = Color.parseColor("#366590");
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int view_width = MeasureSpec.getSize(widthMeasureSpec);

        if (!TextUtils.isEmpty(content)) {
            int view_height;

            if (content_state == CONTENT_DEPLOY) {
                content_view.setText(show_text);

                view_height = deploy_height;

            } else if (content_state == CONTENT_DISPLAY) {
                content_view.setText(show_text);

                view_height = display_height;

            } else if (content_state == CONTENT_RETRACT) {
                content_view.setText(show_text);

                view_height = retract_height;

            } else {
                if (content_view != null) {
                    content_view.setText(content);
                }

                measureChildren(widthMeasureSpec, heightMeasureSpec);
                initViewHeight(view_width);

                removeAllViewsInLayout();

                initContentView();
                content_view.setText(show_text);

                if (content_state == CONTENT_DEPLOY) {
                    view_height = deploy_height;
                } else if (content_state == CONTENT_RETRACT) {
                    initPromptView();
                    view_height = retract_height;
                } else {
                    view_height = display_height;
                }
            }

            measureChildren(widthMeasureSpec, heightMeasureSpec);

            setMeasuredDimension(view_width, view_height);
        } else {
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    public void setContent(final String content_text) {
        if (TextUtils.isEmpty(content)) {
            initParameter();
        } else if (!content.equals(content_text)) {
            initParameter();
        } else {
            if (content_view != null) {
                removeView(content_view);
            }
        }

        this.content = content_text;
        initContentView();
    }

    private void initParameter() {
        if (content_view != null) {
            removeView(content_view);
        }

        if (prompt_view != null) {
            removeView(prompt_view);
        }

        content_state = CONTENT_DEFAULT;

        deploy_height = 0;
        display_height = 0;
        retract_height = 0;
    }


    private void initViewHeight(int width) {
        line_height = 0;

        deploy_height = 0;
        display_height = 0;
        retract_height = 0;

        TextPaint textPaint = content_view.getPaint();

        Layout layout = content_view.getLayout();

        int line_count = layout.getLineCount();

        for (int i = 0; i < line_count; i++) {
            int height = layout.getLineBottom(i) - layout.getLineTop(i);
            if (i < expression_max_lines) {
                retract_height += height;
            }
            deploy_height += height;
            display_height += height;
        }

        line_height = retract_height / 4;

        int padding_top = getPaddingTop();
        int padding_bottom = getPaddingBottom();

        deploy_height += padding_top + padding_bottom;
        retract_height += padding_top + padding_bottom;
        display_height += padding_top + padding_bottom;

        line_height += padding_top + padding_bottom;


        if (line_count > expression_max_lines) {
            content_state = CONTENT_RETRACT;

            float prompt_text_width = textPaint.measureText(" 阅读更多");

            float ellipsesWidth = textPaint.measureText(ellipses);
            float text_width = layout.getLineWidth(expression_max_lines - 1);
            float content_width = width - getPaddingLeft() - getPaddingRight();
            float prompt_width = ellipsesWidth + prompt_text_width;

            int start = layout.getLineStart(expression_max_lines - 1);
            int end = layout.getLineEnd(expression_max_lines - 1);

            show_text = content.substring(0, start);

            String text = content.substring(start, end);

            if (content_width - text_width < prompt_width) {
                String line_content;
                for (int index = text.length() - 1; index > 0; index--) {
                    line_content = text.substring(0, index);
                    float line_width = textPaint.measureText(line_content);
                    if (content_width - line_width >= prompt_width) {
                        show_text += (line_content + ellipses);
                        break;
                    }
                }
            } else {
                show_text = content.substring(0, end);
            }
        } else {
            content_state = CONTENT_DISPLAY;
            show_text = content;
        }
    }

    private void initContentView() {
        content_view = new ExpressionDisplayTextView(contextReference.get(), expression_icon_size, expression_text_size);
        content_view.setTextColor(expression_text_color);
        content_view.setLineSpacing(10f, 1f);
        content_view.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

        LayoutParams contentLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        addView(content_view, contentLayoutParams);
    }

    private void initPromptView() {
        prompt_view = new TextView(contextReference.get());
        prompt_view.setText(expression_prompt_text);
        prompt_view.setTextSize(TypedValue.COMPLEX_UNIT_PX, expression_text_size);
        prompt_view.setTextColor(expression_prompt_text_color);
        prompt_view.setGravity(Gravity.CENTER_VERTICAL);

        prompt_view.setOnClickListener(this);

        LayoutParams promptLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, line_height);
        promptLayoutParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;

        addView(prompt_view, promptLayoutParams);
    }

    @Override
    public void onClick(View view) {
        if (content_view != null) {
            content_state = CONTENT_DEPLOY;
            show_text = content;
            if (prompt_view != null) {
                removeView(prompt_view);
            }
        }
    }
}
