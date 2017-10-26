package com.quduquxie.read;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint.FontMetrics;
import android.text.TextPaint;
import android.text.TextUtils;
import android.widget.ImageView;

import com.orhanobut.logger.Logger;
import com.quduquxie.Constants;
import com.quduquxie.R;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.bean.Bookmark;
import com.quduquxie.base.bean.Chapter;
import com.quduquxie.base.config.BaseConfig;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.base.util.TypefaceUtil;
import com.quduquxie.module.read.reading.view.ReadingActivity;
import com.quduquxie.read.page.PageInterface;
import com.quduquxie.util.BookHelper;
import com.quduquxie.util.StringUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 阅读页工具类
 */
public class NovelHelper {
    public boolean isShown = false;
    private Context context;
    private WeakReference<Activity> actReference;

    private ReadStatus readStatus;
    private PageInterface pageView;

    public NovelHelper(Context context, Activity activity, ReadStatus readStatus) {
        this.context = context;
        this.actReference = new WeakReference<>(activity);
        this.readStatus = readStatus;
    }

    public void setPageView(PageInterface pageView) {
        this.pageView = pageView;
    }

    /**
     * 添加手动书签
     */
    public int addOptionMark(BookDaoHelper bookDaoHelper, IReadDataFactory dataFactory, ImageView read_head_options_bookmark, int font_count) {
        if (!bookDaoHelper.checkBookmarkExist(readStatus.book_id, readStatus.sequence, readStatus.offset)) {
            if (dataFactory.currentChapter == null || getPageContent() == null) {
                return 0;
            }

            if (BaseConfig.READING_BACKGROUND_MODE == 5 || BaseConfig.READING_BACKGROUND_MODE == 6) {
                read_head_options_bookmark.setImageResource(R.drawable.selector_dark_bookmark_remove);
            } else {
                read_head_options_bookmark.setImageResource(R.drawable.selector_light_bookmark_remove);
            }

            Bookmark bookmark = new Bookmark();
            bookmark.book_id = readStatus.book.id;
            bookmark.sequence = readStatus.sequence + 1 > readStatus.chapter_count ? readStatus.chapter_count : readStatus.sequence;
            bookmark.offset = readStatus.offset;
            bookmark.insert_time = System.currentTimeMillis();
            bookmark.chapter_name = dataFactory.currentChapter.name;
            List<String> content = getPageContent();
            StringBuilder sb = new StringBuilder();
            if (readStatus.sequence == -1) {
                bookmark.chapter_name = "《" + readStatus.book.name + "》书籍封面页";
            } else if (readStatus.current_page == 1 && content.size() - 3 >= 0) {
                for (int i = 3; i < content.size(); i++) {
                    sb.append(content.get(i));
                }
            } else {
                for (int i = 0; i < content.size(); i++) {
                    sb.append(content.get(i));
                }
            }

            // 去除第一个字符为标点符号的情况
            String content_text = sb.toString().trim();
            content_text = content_text.trim();

            content_text = StringUtils.deleteTextPoint(content_text);
            // 控制字数
            if (content_text.length() > font_count) {
                content_text = content_text.substring(0, font_count);
            }
            bookmark.chapter_content = content_text;
            bookDaoHelper.insertBookmark(bookmark);

            return 1;
        } else {

            if (BaseConfig.READING_BACKGROUND_MODE == 5 || BaseConfig.READING_BACKGROUND_MODE == 6) {
                read_head_options_bookmark.setImageResource(R.drawable.selector_dark_bookmark_insert);
            } else {
                read_head_options_bookmark.setImageResource(R.drawable.selector_light_bookmark_insert);
            }

            bookDaoHelper.deleteBookmark(readStatus.book_id, readStatus.sequence, readStatus.offset);

            return 2;
        }
    }

    private ArrayList<ArrayList<String>> initTextContent2(String content) {
        Logger.e("initTextContent2: " + content);

        float chapterHeight = 35 * readStatus.screen_scaled_density;
        float hideHeight = 15 * readStatus.screen_scaled_density;

        TextPaint mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTypeface(TypefaceUtil.loadTypeface(context, BaseConfig.READING_TYPEFACE));
        mTextPaint.setTextSize(BaseConfig.READING_TEXT_SIZE * readStatus.screen_scaled_density);
        FontMetrics fm = mTextPaint.getFontMetrics();

        TextPaint mchapterPaint = new TextPaint();
        mchapterPaint.setTextSize(Constants.FONT_CHAPTER_DEFAULT * readStatus.screen_scaled_density);

        TextPaint mbookNamePaint = new TextPaint();
        mbookNamePaint.setAntiAlias(true);
        mbookNamePaint.setTextSize(Constants.FONT_CHAPTER_SIZE * readStatus.screen_scaled_density);

        // 显示文字区域高度
        final float tHeight = fm.descent - fm.ascent;
        float height = 0;


        if (BaseConfig.READING_FLIP_UP_DOWN) {
            height = readStatus.screen_height - tHeight - 50 * readStatus.screen_scaled_density;
        } else {
            height = readStatus.screen_height - tHeight - readStatus.screen_density * BaseConfig.READING_CONTENT_TOP_SPACE * 2;
        }
        float width = readStatus.screen_width - readStatus.screen_density * BaseConfig.READING_CONTENT_LEFT_SPACE * 2;

        // 获取行高 文字高度+0.5倍行间距
        float lineHeight = fm.descent - fm.ascent + BaseConfig.READING_INTERLINEAR_SPACE * BaseConfig.READING_TEXT_SIZE * readStatus.screen_scaled_density;
        float m_duan = (BaseConfig.READING_PARAGRAPH_SPACE - BaseConfig.READING_INTERLINEAR_SPACE) * BaseConfig.READING_TEXT_SIZE * readStatus.screen_scaled_density;

        if (Constants.LANDSCAPE) {
            width = readStatus.screen_width - readStatus.screen_density * BaseConfig.READING_CONTENT_LEFT_SPACE * 2;
        }
        // 添加转换提示
        StringBuilder sb = new StringBuilder();
        if (readStatus.sequence != -1) {
            sb.append("chapter_homepage \n");
            sb.append("chapter_homepage \n");
            sb.append("chapter_homepage \n");
            if (!TextUtils.isEmpty(readStatus.chapter_name)) {
                readStatus.chapter_name_list = getNovelText(mchapterPaint, readStatus.chapter_name, width - readStatus.screen_density * 10);
            }
            if (readStatus.chapter_name_list != null && readStatus.chapter_name_list.size() > 2) {
                ArrayList<String> temp = new ArrayList<>();
                for (int i = 0; i < 2; i++) {
                    temp.add(readStatus.chapter_name_list.get(i));
                }
                readStatus.chapter_name_list = temp;
            }
        }

        if (readStatus.book.book_type == Book.TYPE_ONLINE) {
            String[] contents = content.split("\n");
            for (String temp : contents) {
                sb.append("\u3000\u3000" + temp + "\n");
            }
        } else {
            sb.append(content);
        }

        String text = "";
        if (readStatus.sequence == -1) {
            readStatus.book_name_list = getNovelText(mbookNamePaint, readStatus.book_name, width);
            String homeText = "qgnovel_hp\n";
            StringBuilder s = new StringBuilder();
            s.append(homeText);
            text = s.toString() + sb.toString();
        } else {
            text = sb.toString();
        }
        if (readStatus.offset > text.length()) {
            readStatus.offset = 0;
        } else if (readStatus.offset < 0) {
            readStatus.offset = 0;
        }

        ArrayList<String> contentList = new ArrayList<>();
        contentList = getNovelText(mTextPaint, text, width);
        final int size = contentList.size();
        int textSpace = 0;
        long textLength = 0;
        boolean can = true;
        ArrayList<String> pageLines = new ArrayList<>();
        ArrayList<ArrayList<String>> lists = new ArrayList<>();
        lists.add(pageLines);
        int chapterNameSize = 0;
        if (readStatus.chapter_name_list != null) {
            chapterNameSize = readStatus.chapter_name_list.size();
        }
        if (chapterNameSize > 1) {
            textSpace += chapterHeight;
        }
        for (int i = 0; i < size; i++) {
            boolean isDuan = false;
            String lineText = contentList.get(i);
            if (lineText.equals(" ")) {// 段间距
                isDuan = true;
                textSpace += m_duan;
            } else if (lineText.equals("chapter_homepage  ")) {
                textSpace += hideHeight;
                textLength += lineText.length();
            } else {
                textSpace += lineHeight;
                textLength += lineText.length();
            }
            if (textSpace < height) {
                pageLines.add(lineText);
            } else {
                if (isDuan) {
                    textSpace -= m_duan;
                } else {
                    pageLines = new ArrayList<>();
                    textSpace = 0;
                    pageLines.add(lineText);
                    lists.add(pageLines);
                }
                // }
            }
            if (textLength >= readStatus.offset && can) {
                readStatus.current_page = lists.size();
                can = false;
            }
        }

        readStatus.page_count = lists.size();
        if (readStatus.current_page == 0) {
            readStatus.current_page = 1;
        }

        return lists;
    }

    /**
     * getNovelText
     * 划分章节内容
     * textPaint
     * text
     * width
     * 设定文件
     * ArrayList<String> 返回类型
     */
    private ArrayList<String> getNovelText(TextPaint textPaint, String text, float width) {
        ArrayList<String> list = new ArrayList<String>();
        float w = 0;
        int istart = 0;
        char mChar;
        float[] widths = new float[1];
        float[] chineseWidth = new float[1];
        textPaint.getTextWidths("正", chineseWidth);
        if (text == null) {
            return list;
        }
        int duan_coount = 0;
        for (int i = 0; i < text.length(); i++) {
            mChar = text.charAt(i);
            if (mChar == '\n') {
                widths[0] = 0;
            } else if (StringUtils.isChinese(mChar)) {
                widths[0] = chineseWidth[0];
            } else {
                String srt = String.valueOf(mChar);
                textPaint.getTextWidths(srt, widths);
            }
            if (mChar == '\n') {
                duan_coount++;
                list.add(text.substring(istart, i) + " ");
                if (duan_coount > 3) {
                    list.add(" ");// 段间距
                }
                istart = i + 1;
                w = 0;
            } else {
                w += widths[0];
                if (w > width) {
                    list.add(text.substring(istart, i));
                    istart = i;
                    i--;
                    w = 0;
                } else {
                    if (i == (text.length() - 1)) {
                        list.add(text.substring(istart, text.length()));
                    }
                }
            }
        }
        return list;
    }

    /**
     * 保存阅读进度
     */
    public void saveBookmark(ArrayList<Chapter> chapterList, String id_book, int sequence, int offset, BookDaoHelper mBookDaoHelper) {
        if (chapterList == null || TextUtils.isEmpty(id_book)) {
            return;
        }
        Book book = new Book();
        book.id = id_book;
        book.sequence = sequence;
        book.offset = offset;
        book.sequence_time = System.currentTimeMillis();
        book.read = 1;

        mBookDaoHelper.updateBook(book);
    }

    public synchronized List<String> getPageContent() {
        if (readStatus.line_list == null) {
            return null;
        }
        if (readStatus.current_page == 0) {
            readStatus.current_page = 1;
        }
        if (readStatus.current_page > readStatus.page_count) {
            readStatus.current_page = readStatus.page_count;
        }
        readStatus.offset = 0;
        ArrayList<String> pageContent = null;
        if (readStatus.current_page - 1 < readStatus.line_list.size()) {
            pageContent = readStatus.line_list.get(readStatus.current_page - 1);
        } else {
            pageContent = new ArrayList<String>();
        }

        for (int i = 0; i < readStatus.current_page - 1 && i < readStatus.line_list.size(); i++) {
            ArrayList<String> pageList = readStatus.line_list.get(i);
            final int size = pageList.size();
            for (int j = 0; j < size; j++) {
                String string = pageList.get(j);
                if (!TextUtils.isEmpty(string) && !string.equals(" ")) {
                    readStatus.offset += string.length();
                }
            }
        }
        readStatus.offset++;
        return pageContent;
    }

    public synchronized void getPageContentScroll() {
        if (readStatus.line_list == null) {
            return;
        }
        if (readStatus.current_page == 0) {
            readStatus.current_page = 1;
        }
        if (readStatus.current_page > readStatus.page_count) {
            readStatus.current_page = readStatus.page_count;
        }
        readStatus.offset = 0;
        int count = readStatus.line_list.size();
        for (int i = 0; i < readStatus.current_page - 1 && i < count; i++) {
            ArrayList<String> pageList = readStatus.line_list.get(i);
            final int size = pageList.size();
            for (int j = 0; j < size; j++) {
                String string = pageList.get(j);
                if (!TextUtils.isEmpty(string) && !string.equals(" ")) {
                    readStatus.offset += string.length();
                }
            }
        }
        readStatus.offset++;
    }

    /**
     * 处理章节内容
     */
    public synchronized void getChapterContent(Context context, Activity activity, Chapter currentChapter, Book mBook, boolean isResize) {
        Logger.e("getChapterContent: " + currentChapter);
        if (mBook == null)
            return;

        if (currentChapter != null && readStatus != null) {
            Logger.e("getChapterContent: " + "currentChapter != null && readStatus != null");
            readStatus.image_book = mBook.image;
            readStatus.chapter_name = currentChapter.name;
        }
        if (readStatus != null) {
            Logger.e("getChapterContent: " + "readStatus != null: " + mBook.toString());
            readStatus.book_name = mBook.name;
            readStatus.book_author = mBook.author.name;
            readStatus.book_source = "青果阅读";
        }
        // 获取章节类型
        int type = BookHelper.getChapterType(currentChapter);
        Logger.e("getChapterContent: " + type);
        if (readStatus.sequence == -1) {
            type = BookHelper.CHAPTER_TYPE_WORD;
        }

        Logger.e("getChapterContent: " + mBook.toString());
        if (!isResize && mBook.book_type == 0 && activity != null && activity instanceof ReadingActivity && currentChapter != null && currentChapter.content != null) {
            ((ReadingActivity) activity).addTextLength(currentChapter.content.length());
        }

        switch (type) {
            case BookHelper.CHAPTER_TYPE_WORD:
                if (currentChapter != null) {
                    readStatus.line_list = initTextContent2(currentChapter.content);
                }
            default:
                break;
        }

    }

    public void clear() {
        if (readStatus.book_name_list != null) {
            readStatus.book_name_list.clear();
            readStatus.book_name_list = null;
        }
        isShown = false;
        readStatus.book_name = "";
        readStatus.line_list = null;

        if (this.context != null) {
            this.context = null;
        }

        if (this.actReference != null) {
            this.actReference.clear();
            this.actReference = null;
        }

        if (this.pageView != null) {
            this.pageView = null;
        }
    }

}
