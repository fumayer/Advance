package com.quduquxie.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;

/**
 * Created on 17/5/25.
 * Created by crazylei.
 */

public class AutoSetTextView extends AppCompatTextView {

    private int height;
    private int viewWidth;

    public AutoSetTextView(Context context) {
        this(context, null);
    }

    public AutoSetTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoSetTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas Canvas) {
        Layout layout = getLayout();

        if (layout == null) {
            return;
        }

        TextPaint textPaint = getPaint();

        textPaint.setColor(getCurrentTextColor());

        textPaint.drawableState = getDrawableState();

        viewWidth = getMeasuredWidth();

        String text = getText().toString();

        height = 0;
        height += getTextSize();


        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();

        int textHeight = (int) (Math.ceil(fontMetrics.descent - fontMetrics.ascent));

        textHeight = (int) (textHeight * layout.getSpacingMultiplier() + layout.getSpacingAdd());

        for (int i = 0; i < layout.getLineCount(); i++) {
            int lineStart = layout.getLineStart(i);
            int lineEnd = layout.getLineEnd(i);

            float width = StaticLayout.getDesiredWidth(text, lineStart, lineEnd, getPaint());

            String line = text.substring(lineStart, lineEnd);

            if (i < layout.getLineCount() - 1) {
                if (needScale(line)) {
                    drawScaledText(Canvas, line, width);
                } else {
                    Canvas.drawText(line, 0, height, textPaint);
                }
            } else {
                Canvas.drawText(line, 0, height, textPaint);
            }
            height += textHeight;
        }
    }

    private void drawScaledText(Canvas Canvas, String line, float lineWidth) {
        float width = 0;
        if (isFirstLineOfParagraph(line)) {
            String blanks = "  ";
            Canvas.drawText(blanks, width, height, getPaint());

            float desiredWidth = StaticLayout.getDesiredWidth(blanks, getPaint());
            width += desiredWidth;

            line = line.substring(3);
        }

        int count = line.length() - 1;
        int i = 0;
        if (line.length() > 2 && line.charAt(0) == 12288 && line.charAt(1) == 12288) {
            String substring = line.substring(0, 2);
            float desiredWidth = StaticLayout.getDesiredWidth(substring, getPaint());
            Canvas.drawText(substring, width, height, getPaint());
            width += desiredWidth;
            i += 2;
        }

        float spacing = (viewWidth - lineWidth) / count;

        for (; i < line.length(); i++) {
            String content = String.valueOf(line.charAt(i));
            float desiredWidth = StaticLayout.getDesiredWidth(content, getPaint());
            Canvas.drawText(content, width, height, getPaint());
            width += desiredWidth + spacing;
        }
    }

    private boolean isFirstLineOfParagraph(String line) {
        return line.length() > 3 && line.charAt(0) == ' ' && line.charAt(1) == ' ';
    }

    private boolean needScale(String line) {
        if (line == null || line.length() == 0) {
            return false;
        } else {
            return line.charAt(line.length() - 1) != '\n';
        }
    }
}
