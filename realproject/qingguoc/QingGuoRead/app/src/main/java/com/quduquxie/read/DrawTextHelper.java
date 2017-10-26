package com.quduquxie.read;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextUtils;

import com.quduquxie.Constants;
import com.quduquxie.application.QuApplication;
import com.quduquxie.R;
import com.quduquxie.base.config.BaseConfig;
import com.quduquxie.read.page.PageInterface;

import java.util.ArrayList;
import java.util.List;

public class DrawTextHelper {
    private Paint paint;
    private Paint duanPaint;
    private Paint textPaint;
    private Paint footPaint;
    private Paint backgroundPaint;

    private Resources resources;
    private ReadStatus readStatus;

    private Bitmap background;

    private int unit;
    private int textColor;
    private int footHeight;

    private String timeText;

    private PageInterface pageView;

    public DrawTextHelper(Resources resources, PageInterface pageView, Application application) {

        this.resources = resources;
        this.pageView = pageView;

        readStatus = ((QuApplication) application).getReadStatus();

        unit = (int) (2 * readStatus.screen_scaled_density);
        footHeight = (int) (18 * readStatus.screen_scaled_density);

        paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setDither(true);

        duanPaint = new Paint(Paint.FILTER_BITMAP_FLAG);
        duanPaint.setStyle(Paint.Style.FILL);
        duanPaint.setAntiAlias(true);
        duanPaint.setDither(true);

        footPaint = new Paint(Paint.FILTER_BITMAP_FLAG);
        footPaint.setStyle(Paint.Style.FILL);
        footPaint.setAntiAlias(true);
        footPaint.setDither(true);
        footPaint.setTextSize(12 * readStatus.screen_scaled_density);

        textPaint = new Paint(Paint.FILTER_BITMAP_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        textPaint.setColor(Color.RED);
        textPaint.setTextSize(Constants.FONT_CHAPTER_SIZE * readStatus.screen_scaled_density);

        backgroundPaint = new Paint(Paint.FILTER_BITMAP_FLAG);
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setDither(true);

        setPaintColor(paint, 1);
        setPaintColor(duanPaint, 1);
        setPaintColor(footPaint, 1);
        setPaintColor(textPaint, 1);
    }

    /**
     * 封面页
     **/
    private synchronized void drawHomePage(final Canvas canvas) {
        ArrayList<String> nameList = readStatus.book_name_list;
        if (nameList == null || nameList.isEmpty()) {
            return;
        }

        int title_height = readStatus.screen_height / 3;

        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(Constants.FONT_CHAPTER_SIZE * readStatus.screen_scaled_density);

        FontMetrics fontMetrics = paint.getFontMetrics();
        float y = fontMetrics.descent - fontMetrics.ascent + title_height;
        float text_line = fontMetrics.descent - fontMetrics.ascent;

        int name_length = nameList.size();
        name_length = (name_length > 4) ? 4 : name_length;

        int x = 0;
        if (paint.getTextAlign() == Paint.Align.CENTER) {
            x = readStatus.screen_width / 2;
        }

        /**
         * 绘制书名
         **/
        for (int i = 0; i < name_length; i++) {
            if (i == 0) {
                canvas.drawText(nameList.get(i), x, y, paint);
            } else {
                canvas.drawText(nameList.get(i), x, y + text_line * i, paint);
            }
        }

        float totalHeight = y + text_line * (nameList.size() - 1) + 15 * readStatus.screen_scaled_density;
        float other_line = 10 + 8 * readStatus.screen_scaled_density;

        paint.setTextSize(15 * readStatus.screen_scaled_density);

        /**
         * 绘制作者
         **/
        if (!TextUtils.isEmpty(readStatus.book_author)) {
            canvas.drawText(readStatus.book_author, x, totalHeight + 2 * other_line, paint);
        }

        /**
         * 绘制来源
         **/
//        if (!TextUtils.isEmpty(readStatus.book_source)) {
//            if (Constants.READ_FLIP_UP_DOWN) {
//                canvas.drawText(readStatus.book_source, x, readStatus.screen_height - other_line - 20 * readStatus.screen_scaled_density, paint);
//            } else {
//                canvas.drawText(readStatus.book_source, x, readStatus.screen_height - other_line, paint);
//            }
//        }
    }

    /**
     * 章节首页
     **/
    private void drawChapterPage(Canvas canvas, List<String> pageLines) {

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(BaseConfig.READING_TEXT_SIZE * readStatus.screen_scaled_density);

        duanPaint.setTextSize(1 * readStatus.screen_scaled_density);
        textPaint.setTextSize(Constants.FONT_CHAPTER_DEFAULT * readStatus.screen_scaled_density);

        FontMetrics textFontMetrics = textPaint.getFontMetrics();
        float chapter_text_line = textFontMetrics.descent - textFontMetrics.ascent;
        float chapter_font_height = chapter_text_line + 0.5f * Constants.FONT_CHAPTER_SIZE * readStatus.screen_scaled_density;

        FontMetrics pointFontMetrics = paint.getFontMetrics();
        float point_text_height = pointFontMetrics.descent - pointFontMetrics.ascent;
        float point_font_height = point_text_height + BaseConfig.READING_INTERLINEAR_SPACE * BaseConfig.READING_TEXT_SIZE * readStatus.screen_scaled_density;


        float start = (BaseConfig.READING_PARAGRAPH_SPACE - BaseConfig.READING_INTERLINEAR_SPACE) * BaseConfig.READING_TEXT_SIZE * readStatus.screen_scaled_density;

        float y;
        float chapter_y;

        if (BaseConfig.READING_FLIP_UP_DOWN) {
            chapter_y = chapter_text_line;
            y = point_text_height - 5 * readStatus.screen_scaled_density;
        } else {
            chapter_y = chapter_text_line + BaseConfig.READING_CONTENT_TOP_SPACE * readStatus.screen_scaled_density;
            y = point_text_height + BaseConfig.READING_CONTENT_TOP_SPACE * readStatus.screen_scaled_density;
        }

        int count;
        if (readStatus.chapter_name_list != null && !readStatus.chapter_name_list.isEmpty()) {
            textPaint.setColor(textColor);
            count = readStatus.chapter_name_list.size();
            /**
             * 绘制章节名
             **/
            for (int i = 0; i < count; i++) {
                if (i == 0) {
                    if (!TextUtils.isEmpty(readStatus.chapter_name)) {
                        String chapterName = readStatus.chapter_name_list.get(0);
                        textPaint.setTextSize(Constants.FONT_CHAPTER_DEFAULT * readStatus.screen_scaled_density);
                        canvas.drawText(chapterName, BaseConfig.READING_CONTENT_LEFT_SPACE * readStatus.screen_scaled_density, chapter_y + unit + point_font_height * i, textPaint);
                    }
                } else {
                    textFontMetrics = textPaint.getFontMetrics();
                    chapter_text_line = textFontMetrics.descent - textFontMetrics.ascent;
                    chapter_font_height = chapter_text_line + 0.5f * Constants.FONT_CHAPTER_DEFAULT * readStatus.screen_scaled_density;
                    canvas.drawText(readStatus.chapter_name_list.get(i), BaseConfig.READING_CONTENT_LEFT_SPACE * readStatus.screen_scaled_density, chapter_y + chapter_font_height * i, textPaint);
                }
            }

            float line = chapter_y + 15 * readStatus.screen_scaled_density + chapter_font_height * (count - 1);

            /**
             * 绘制分割线
             **/
            paint.setStrokeWidth(1 * readStatus.screen_scaled_density);
            canvas.drawLine(BaseConfig.READING_CONTENT_LEFT_SPACE * readStatus.screen_scaled_density, line, readStatus.screen_width - BaseConfig.READING_CONTENT_LEFT_SPACE * readStatus.screen_scaled_density, line, paint);

            paint.setStrokeWidth(0.0f);
            y = y + 50 * readStatus.screen_scaled_density + point_text_height;

            /**
             * 行间距
             **/
            float space_height = BaseConfig.READING_INTERLINEAR_SPACE * BaseConfig.READING_TEXT_SIZE * readStatus.screen_scaled_density;

            float text_height = 0;
            float start_height = 0;
            if (pageLines != null) {
                int size = pageLines.size();
                for (int i = 0; i < size; i++) {
                    String text = pageLines.get(i);
                    if (!TextUtils.isEmpty(text) && text.equals(" ")) {
                        text_height += start;
                        start_height += start;
                    } else if (!text.equals("chapter_homepage  ")) {
                        text_height += point_font_height;
                    }
                }
            }

            float height;

            if (BaseConfig.READING_FLIP_UP_DOWN) {
                height = readStatus.screen_height - 96 * readStatus.screen_scaled_density + space_height - point_text_height;
            } else {
                height = readStatus.screen_height - readStatus.screen_density * BaseConfig.READING_CONTENT_TOP_SPACE * 2 - 50 * readStatus.screen_scaled_density + space_height - point_text_height;
            }

            if (count > 1) {
                y += 30 * readStatus.screen_scaled_density;
                height -= 30 * readStatus.screen_scaled_density;
            }

            if (height - text_height > 2 && height - text_height < 120 * readStatus.screen_scaled_density) {
                /**
                 * 计算行数
                 **/
                int n = (int) Math.floor((height - start_height) / point_font_height);
                float distance = (height - text_height) / n;
                point_font_height = pointFontMetrics.descent - pointFontMetrics.ascent + BaseConfig.READING_INTERLINEAR_SPACE * BaseConfig.READING_TEXT_SIZE * readStatus.screen_scaled_density + distance;
            } else if (text_height - height > 2) {
                /**
                 * 计算行数
                 **/
                int n = (int) Math.floor((height - start_height) / point_font_height);
                float distance = (text_height - height) / n;
                point_font_height = pointFontMetrics.descent - pointFontMetrics.ascent + BaseConfig.READING_INTERLINEAR_SPACE * BaseConfig.READING_TEXT_SIZE * readStatus.screen_scaled_density - distance;
            }

            for (int i = 0; i < pageLines.size(); i++) {
                if (i > count) {
                    String text = pageLines.get(i);
                    if (!TextUtils.isEmpty(text)) {
                        if (text.equals(" ")) {
                            canvas.drawText(text, BaseConfig.READING_CONTENT_LEFT_SPACE * readStatus.screen_scaled_density, y + start * i, duanPaint);
                            y -= point_font_height - start;
                        } else if (!text.equals("chapter_homepage  ")) {
                            canvas.drawText(text, BaseConfig.READING_CONTENT_LEFT_SPACE * readStatus.screen_scaled_density, y + point_font_height * (i - 3), paint);
                        }
                    }

                }
            }
        }
    }

    /**
     * 上下滑动模式
     **/
    private void drawChapterPage(Canvas canvas, List<String> pageLines, ArrayList<String> chapterNameList) {
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(BaseConfig.READING_TEXT_SIZE * readStatus.screen_scaled_density);

        duanPaint.setTextSize(1 * readStatus.screen_scaled_density);
        textPaint.setTextSize(Constants.FONT_CHAPTER_SIZE * readStatus.screen_scaled_density);

        FontMetrics textFontMetrics = textPaint.getFontMetrics();
        float chapter_text_line = textFontMetrics.descent - textFontMetrics.ascent;
        float chapter_font_height = chapter_text_line + 0.5f * Constants.FONT_CHAPTER_SIZE * readStatus.screen_scaled_density;

        FontMetrics pointFontMetrics = paint.getFontMetrics();
        float point_text_height = pointFontMetrics.descent - pointFontMetrics.ascent;
        float point_font_height = point_text_height + BaseConfig.READING_INTERLINEAR_SPACE * BaseConfig.READING_TEXT_SIZE * readStatus.screen_scaled_density;


        float start = (BaseConfig.READING_PARAGRAPH_SPACE - BaseConfig.READING_INTERLINEAR_SPACE) * BaseConfig.READING_TEXT_SIZE * readStatus.screen_scaled_density;

        float y;
        float chapter_y;

        if (BaseConfig.READING_FLIP_UP_DOWN) {
            chapter_y = chapter_text_line;
            y = point_text_height - 5 * readStatus.screen_scaled_density;
        } else {
            chapter_y = chapter_text_line + BaseConfig.READING_CONTENT_TOP_SPACE * readStatus.screen_scaled_density;
            y = point_text_height + BaseConfig.READING_CONTENT_TOP_SPACE * readStatus.screen_scaled_density;
        }

        int count;
        if (chapterNameList != null && !chapterNameList.isEmpty()) {
            textPaint.setColor(textColor);
            count = chapterNameList.size();
            /**
             * 绘制章节名
             **/
            for (int i = 0; i < count; i++) {
                if (i == 0) {
                    if (!TextUtils.isEmpty(chapterNameList.get(0))) {
                        String chapterName = chapterNameList.get(0);
                        textPaint.setTextSize(Constants.FONT_CHAPTER_DEFAULT * readStatus.screen_scaled_density);
                        canvas.drawText(chapterName, BaseConfig.READING_CONTENT_LEFT_SPACE * readStatus.screen_scaled_density, chapter_y + unit + point_font_height * i, textPaint);
                    }
                } else {
                    textFontMetrics = textPaint.getFontMetrics();
                    chapter_text_line = textFontMetrics.descent - textFontMetrics.ascent;
                    chapter_font_height = chapter_text_line + 0.5f * Constants.FONT_CHAPTER_DEFAULT * readStatus.screen_scaled_density;
                    canvas.drawText(chapterNameList.get(i), BaseConfig.READING_CONTENT_LEFT_SPACE * readStatus.screen_scaled_density, chapter_y + chapter_font_height * i, textPaint);
                }
            }

            float line = chapter_y + 15 * readStatus.screen_scaled_density + chapter_font_height * (count - 1);

            /**
             * 绘制分割线
             **/
            paint.setStrokeWidth(1 * readStatus.screen_scaled_density);
            canvas.drawLine(BaseConfig.READING_CONTENT_LEFT_SPACE * readStatus.screen_scaled_density, line, readStatus.screen_width - BaseConfig.READING_CONTENT_LEFT_SPACE * readStatus.screen_scaled_density, line, paint);

            paint.setStrokeWidth(0.0f);
            y = y + 50 * readStatus.screen_scaled_density + point_text_height;

            /**
             * 行间距
             **/
            float space_height = BaseConfig.READING_INTERLINEAR_SPACE * BaseConfig.READING_TEXT_SIZE * readStatus.screen_scaled_density;

            float text_height = 0;
            float start_height = 0;
            if (pageLines != null) {
                int size = pageLines.size();
                for (int i = 0; i < size; i++) {
                    String text = pageLines.get(i);
                    if (!TextUtils.isEmpty(text) && text.equals(" ")) {
                        text_height += start;
                        start_height += start;
                    } else if (!text.equals("chapter_homepage  ")) {
                        text_height += point_font_height;
                    }
                }

            }

            float height;

            if (BaseConfig.READING_FLIP_UP_DOWN) {
                height = readStatus.screen_height - 96 * readStatus.screen_scaled_density + space_height - point_text_height;
            } else {
                height = readStatus.screen_height - readStatus.screen_density * BaseConfig.READING_CONTENT_TOP_SPACE * 2 - 50 * readStatus.screen_scaled_density + space_height - point_text_height;
            }

            if (count > 1) {
                y += 30 * readStatus.screen_scaled_density;
                height -= 30 * readStatus.screen_scaled_density;
            }

            if (height - text_height > 2 && height - text_height < 120 * readStatus.screen_scaled_density) {
                /**
                 * 计算行数
                 **/
                int n = (int) Math.floor((height - start_height) / point_font_height);
                float distance = (height - text_height) / n;
                point_font_height = pointFontMetrics.descent - pointFontMetrics.ascent + BaseConfig.READING_INTERLINEAR_SPACE * BaseConfig.READING_TEXT_SIZE * readStatus.screen_scaled_density + distance;
            } else if (text_height - height > 2) {
                /**
                 * 计算行数
                 **/
                int n = (int) Math.floor((height - start_height) / point_font_height);
                float distance = (text_height - height) / n;
                point_font_height = pointFontMetrics.descent - pointFontMetrics.ascent + BaseConfig.READING_INTERLINEAR_SPACE * BaseConfig.READING_TEXT_SIZE * readStatus.screen_scaled_density - distance;
            }

            for (int i = 0; i < pageLines.size(); i++) {
                if (i > count) {
                    String text = pageLines.get(i);
                    if (!TextUtils.isEmpty(text)) {
                        if (text.equals(" ")) {
                            canvas.drawText(text, BaseConfig.READING_CONTENT_LEFT_SPACE * readStatus.screen_scaled_density, y + start * i, duanPaint);
                            y -= point_font_height - start;
                        } else if (!text.equals("chapter_homepage  ")) {
                            canvas.drawText(text, BaseConfig.READING_CONTENT_LEFT_SPACE * readStatus.screen_scaled_density, y + point_font_height * (i - 3), paint);
                        }
                    }

                }
            }
        }
    }

    /**
     * 章节内容页
     **/
    public synchronized void drawText(Canvas canvas, List<String> pageLines) {

        paint.setTextSize(BaseConfig.READING_TEXT_SIZE * readStatus.screen_scaled_density);
        duanPaint.setTextSize(1 * readStatus.screen_scaled_density);

        FontMetrics fontMetrics = paint.getFontMetrics();

        float point_font_height = fontMetrics.descent - fontMetrics.ascent + BaseConfig.READING_INTERLINEAR_SPACE * BaseConfig.READING_TEXT_SIZE * readStatus.screen_scaled_density;
        float start = (BaseConfig.READING_PARAGRAPH_SPACE - BaseConfig.READING_INTERLINEAR_SPACE) * BaseConfig.READING_TEXT_SIZE * readStatus.screen_scaled_density;

        float space_height = BaseConfig.READING_INTERLINEAR_SPACE * BaseConfig.READING_TEXT_SIZE * readStatus.screen_scaled_density;

        float y;
        float height;

        if (BaseConfig.READING_FLIP_UP_DOWN) {
            y = point_font_height;
            height = readStatus.screen_height - 52 * readStatus.screen_scaled_density;
        } else {
            y = fontMetrics.descent - fontMetrics.ascent + BaseConfig.READING_CONTENT_TOP_SPACE * readStatus.screen_scaled_density;
            height = readStatus.screen_height - readStatus.screen_density * BaseConfig.READING_CONTENT_TOP_SPACE * 2 + space_height;
        }

        float text_height = 0;
        float start_height = 0;
        if (pageLines != null) {
            int size = pageLines.size();
            for (int i = 0; i < size; i++) {
                String text = pageLines.get(i);
                if (!TextUtils.isEmpty(text) && text.equals(" ")) {
                    text_height += start;
                    start_height += start;
                } else {
                    text_height += point_font_height;
                }
            }

        }
        if (height - text_height > 2 && height - text_height < 4 * (fontMetrics.descent - fontMetrics.ascent)) {
            int n = Math.round((height - start_height) / point_font_height);
            float distance = (height - text_height) / n;
            point_font_height = fontMetrics.descent - fontMetrics.ascent + BaseConfig.READING_INTERLINEAR_SPACE * BaseConfig.READING_TEXT_SIZE * readStatus.screen_scaled_density + distance;
        } else if (text_height - height > 2) {
            int n = Math.round((height - start_height) / point_font_height);
            float distance = (text_height - (height)) / n;
            point_font_height = fontMetrics.descent - fontMetrics.ascent + BaseConfig.READING_INTERLINEAR_SPACE * BaseConfig.READING_TEXT_SIZE * readStatus.screen_scaled_density - distance;
        }

        drawBackground(canvas);

        if (pageLines != null && !pageLines.isEmpty()) {
            if (pageLines.get(0).startsWith("qgnovel_hp")) {
                drawHomePage(canvas);
            } else if (pageLines.get(0).startsWith("chapter_homepage")) {
                drawChapterPage(canvas, pageLines);
            } else {
                /**
                 * 绘制顶部章节名
                 **/
                if (!BaseConfig.READING_FLIP_UP_DOWN) {
                    String chapterName = readStatus.chapter_name_list.get(0);
                    canvas.drawText(chapterName, BaseConfig.READING_CONTENT_LEFT_SPACE * readStatus.screen_scaled_density, Constants.READ_CONTENT_PAGE_CHAPTER_NAME_MARGIN_TOP * readStatus.screen_scaled_density, footPaint);
                }
                paint.setTextAlign(Paint.Align.LEFT);

                for (int i = 0; i < pageLines.size(); i++) {
                    String text = pageLines.get(i);
                    if (!TextUtils.isEmpty(text) && text.equals(" ")) {
                        y -= point_font_height - start;
                        canvas.drawText(text, BaseConfig.READING_CONTENT_LEFT_SPACE * readStatus.screen_scaled_density, y + start * i, duanPaint);
                    } else {
                        canvas.drawText(text, BaseConfig.READING_CONTENT_LEFT_SPACE * readStatus.screen_scaled_density, y + point_font_height * i, paint);
                    }
                }
            }
        }

        if (!BaseConfig.READING_FLIP_UP_DOWN) {
            drawCanvasFoot(canvas);
        }
    }

    /**
     * 上下滑动模式
     **/
    public synchronized void drawText(Canvas canvas, List<String> pageLines, ArrayList<String> chapterNameList) {
        paint.setTextSize(BaseConfig.READING_TEXT_SIZE * readStatus.screen_scaled_density);
        duanPaint.setTextSize(1 * readStatus.screen_scaled_density);

        FontMetrics fontMetrics = paint.getFontMetrics();

        float point_font_height = fontMetrics.descent - fontMetrics.ascent + BaseConfig.READING_INTERLINEAR_SPACE * BaseConfig.READING_TEXT_SIZE * readStatus.screen_scaled_density;
        float start = (BaseConfig.READING_PARAGRAPH_SPACE - BaseConfig.READING_INTERLINEAR_SPACE) * BaseConfig.READING_TEXT_SIZE * readStatus.screen_scaled_density;

        float space_height = BaseConfig.READING_INTERLINEAR_SPACE * BaseConfig.READING_TEXT_SIZE * readStatus.screen_scaled_density;

        float y;
        float height;

        if (BaseConfig.READING_FLIP_UP_DOWN) {
            y = point_font_height;
            height = readStatus.screen_height - 52 * readStatus.screen_scaled_density;
        } else {
            y = fontMetrics.descent - fontMetrics.ascent + BaseConfig.READING_CONTENT_TOP_SPACE * readStatus.screen_scaled_density;
            height = readStatus.screen_height - readStatus.screen_density * BaseConfig.READING_CONTENT_TOP_SPACE * 2 + space_height;
        }

        float text_height = 0;
        float start_height = 0;
        if (pageLines != null) {
            int size = pageLines.size();
            for (int i = 0; i < size; i++) {
                String text = pageLines.get(i);
                if (!TextUtils.isEmpty(text) && text.equals(" ")) {
                    text_height += start;
                    start_height += start;
                } else {
                    text_height += point_font_height;
                }
            }

        }
        if (height - text_height > 2 && height - text_height < 4 * (fontMetrics.descent - fontMetrics.ascent)) {
            int n = Math.round((height - start_height) / point_font_height);
            float distance = (height - text_height) / n;
            point_font_height = fontMetrics.descent - fontMetrics.ascent + BaseConfig.READING_INTERLINEAR_SPACE * BaseConfig.READING_TEXT_SIZE * readStatus.screen_scaled_density + distance;
        } else if (text_height - height > 2) {
            int n = Math.round((height - start_height) / point_font_height);
            float distance = (text_height - (height)) / n;
            point_font_height = fontMetrics.descent - fontMetrics.ascent + BaseConfig.READING_INTERLINEAR_SPACE * BaseConfig.READING_TEXT_SIZE * readStatus.screen_scaled_density - distance;
        }

        drawBackground(canvas);

        if (pageLines != null && !pageLines.isEmpty()) {
            if (pageLines.get(0).startsWith("qgnovel_hp")) {
                drawHomePage(canvas);
            } else if (pageLines.get(0).startsWith("chapter_homepage")) {
                drawChapterPage(canvas, pageLines, chapterNameList);
            } else {
                paint.setTextAlign(Paint.Align.LEFT);
                for (int i = 0; i < pageLines.size(); i++) {
                    String text = pageLines.get(i);
                    if (!TextUtils.isEmpty(text) && text.equals(" ")) {
                        y -= point_font_height - start;
                        canvas.drawText(text, BaseConfig.READING_CONTENT_LEFT_SPACE * readStatus.screen_scaled_density, y + start * i, duanPaint);
                    } else {
                        canvas.drawText(text, BaseConfig.READING_CONTENT_LEFT_SPACE * readStatus.screen_scaled_density, y + point_font_height * i, paint);
                    }
                }
            }
        }
    }

    /**
     * 绘制背景图
     **/
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void drawBackground(Canvas canvas) {
        canvas.drawRect(0, 0, readStatus.screen_width, readStatus.screen_height, setPaintColor(backgroundPaint, 0));
    }

    /**
     * 绘制底部
     **/
    public void drawCanvasFoot(Canvas canvas) {
        if (!readStatus.draw_foot_view && !BaseConfig.READING_FLIP_UP_DOWN) {
            return;
        }
        drawFootBackground(canvas);
        drawTime(canvas);
        drawChapterNumber(canvas);
        drawPageNumber(canvas);
    }

    /**
     * 绘制底部背景
     **/
    private void drawFootBackground(Canvas canvas) {
        int top = readStatus.screen_height - footHeight;
        canvas.drawRect(0, top, readStatus.screen_width, readStatus.screen_height, setPaintColor(backgroundPaint, 0));
    }

    /**
     * 绘制底部小说章节数
     **/
    private void drawChapterNumber(Canvas canvas) {
        float position;
        position = readStatus.screen_height - 5 * readStatus.screen_scaled_density;
        int chapter = readStatus.sequence + 1;
        float string_width = footPaint.measureText(chapter + "/" + readStatus.chapter_count + "章");
        canvas.drawText(chapter + "/" + readStatus.chapter_count + "章", readStatus.screen_width / 2 - string_width / 2, position, footPaint);
    }

    /**
     * 绘制底部章节分页数
     **/
    private void drawPageNumber(Canvas canvas) {
        float position;

        String page_num = readStatus.current_page + "/" + readStatus.page_count + "页";
        position = readStatus.screen_height - 5 * readStatus.screen_scaled_density;
        float string_width = footPaint.measureText(page_num);
        canvas.drawText(page_num, readStatus.screen_width - BaseConfig.READING_CONTENT_LEFT_SPACE * readStatus.screen_scaled_density - string_width, position, footPaint);
    }

    /**
     * 绘制底部时间
     **/
    private void drawTime(Canvas canvas) {
        if (timeText == null || TextUtils.isEmpty(timeText)) {
            return;
        }
        float position;
        position = readStatus.screen_height - 5 * readStatus.screen_scaled_density;
        canvas.drawText(timeText, BaseConfig.READING_CONTENT_LEFT_SPACE * readStatus.screen_scaled_density + 5 * readStatus.screen_scaled_density, position, footPaint);
    }

    /**
     * 设置背景颜色
     **/
    private Paint setPaintColor(Paint paint, int type) {
        int color = R.color.read_background_first;
        if (BaseConfig.READING_BACKGROUND_MODE == 1) {
            if (type == 0) {
                color = R.color.read_background_first;
            } else {
                color = R.color.read_background_first_text;
            }
        } else if (BaseConfig.READING_BACKGROUND_MODE == 2) {
            if (type == 0) {
                color = R.color.read_background_second;
            } else {
                color = R.color.read_background_second_text;
            }
        } else if (BaseConfig.READING_BACKGROUND_MODE == 3) {
            if (type == 0) {
                color = R.color.read_background_third;
            } else {
                color = R.color.read_background_third_text;
            }
        } else if (BaseConfig.READING_BACKGROUND_MODE == 4) {
            if (type == 0) {
                color = R.color.read_background_fourth;
            } else {
                color = R.color.read_background_fourth_text;
            }
        } else if (BaseConfig.READING_BACKGROUND_MODE == 5) {
            if (type == 0) {
                color = R.color.read_background_fifth;
            } else {
                color = R.color.read_background_fifth_text;
            }
        } else if (BaseConfig.READING_BACKGROUND_MODE == 6) {
            if (type == 0) {
                color = R.color.read_background_sixth;
            } else {
                color = R.color.read_background_sixth_text;
            }
        }
        if (type == -1) {
            color = R.color.color_transparent;
        }

        paint.setColor(resources.getColor(color));
        return paint;
    }

    public void setTimeText(String time) {
        this.timeText = time;
    }

    public void setTypeFace(Typeface typeFace) {
        paint.setTypeface(typeFace);
        duanPaint.setTypeface(typeFace);
        textPaint.setTypeface(typeFace);
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        paint.setColor(textColor);
        duanPaint.setColor(textColor);
        footPaint.setColor(textColor);
        textPaint.setColor(textColor);
    }

    public void resetBackBitmap() {
        if (background != null && !background.isRecycled()) {
            background.recycle();
            background = null;
        }
    }

    /**
     * 资源回收
     **/
    public synchronized void recyclerData() {
        if (background != null && !background.isRecycled()) {
            background.recycle();
            background = null;
        }
    }
}
