package com.quduquxie.base.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;

public class AutoSetTextView extends AppCompatTextView {

    private int lineY;
    private int viewWidth;

    public AutoSetTextView(Context context) {
        super(context);
    }

    public AutoSetTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas Canvas) {
        TextPaint textPaint = getPaint();
        textPaint.setColor(getCurrentTextColor());
        textPaint.drawableState = getDrawableState();
        viewWidth = getMeasuredWidth();
        String text = getText().toString();
        lineY = 0;
        lineY += getTextSize();
        Layout layout = getLayout();

        if (layout == null) {
            return;
        }

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
                    Canvas.drawText(line, 0, lineY, textPaint);
                }
            } else {
                Canvas.drawText(line, 0, lineY, textPaint);
            }
            lineY += textHeight;
        }
    }

    private void drawScaledText(Canvas Canvas, String line, float lineWidth) {
        float x = 0;
        if (isFirstLineOfParagraph(line)) {
            String blanks = "  ";
            Canvas.drawText(blanks, x, lineY, getPaint());
            float desiredWidth = StaticLayout.getDesiredWidth(blanks, getPaint());
            x += desiredWidth;

            line = line.substring(3);
        }

        int count = line.length() - 1;
        int i = 0;
        if (line.length() > 2 && line.charAt(0) == 12288 && line.charAt(1) == 12288) {
            String substring = line.substring(0, 2);
            float width = StaticLayout.getDesiredWidth(substring, getPaint());
            Canvas.drawText(substring, x, lineY, getPaint());
            x += width;
            i += 2;
        }

        float space = (viewWidth - lineWidth) / count;
        for (; i < line.length(); i++) {
            String character = String.valueOf(line.charAt(i));
            float characterWidth = StaticLayout.getDesiredWidth(character, getPaint());
            Canvas.drawText(character, x, lineY, getPaint());
            x += characterWidth + space;
        }
    }

    private boolean isFirstLineOfParagraph(String line) {
        return line.length() > 3 && line.charAt(0) == ' ' && line.charAt(1) == ' ';
    }

    private boolean needScale(String line) {
        return !(line == null || line.length() == 0) && line.charAt(line.length() - 1) != '\n';
    }
}