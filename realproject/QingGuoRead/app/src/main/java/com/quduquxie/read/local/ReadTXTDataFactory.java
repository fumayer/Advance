package com.quduquxie.read.local;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

import android.content.Context;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.bean.Chapter;
import com.quduquxie.base.database.dao.ChapterDao;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.read.IReadDataFactory;
import com.quduquxie.read.NovelHelper;
import com.quduquxie.read.ReadStatus;
import com.quduquxie.module.read.reading.view.ReadingActivity;
import com.quduquxie.util.CustomUtils;
import com.quduquxie.widget.LoadingPage;

public class ReadTXTDataFactory extends IReadDataFactory implements DecomposeCatalogDialog.OnDecomposeSuccessListener {
    private int what;
    private Book book;
    private String fileEncode = null;
    private String model;
    private InputStream inputStream = null;
    private FileInputStream fileInputStream;
    private BufferedReader bufferedReader = null;
    private BookDaoHelper bookDaoHelper;
    private ChapterDao chapterDao;

    private final static int stepLength = 5000;

    private int reading_chapter_sequence = -1;
    private int current_chapter_start = 0;
    private int next_chapter_start = 0;


    public ReadTXTDataFactory(Context context, ReadingActivity readingActivity, ReadStatus readStatus, NovelHelper novelHelper) {
        super(context, readingActivity, readStatus, novelHelper);
    }

    private String getContent() {
        Logger.e("getContent");
        String content;
        int index = 0;
        StringBuilder stringBuffer = new StringBuilder();
        if (model == null) {
            model = CustomUtils.getFileMode(book.file_path, fileEncode);
        }
        try {
            while ((content = bufferedReader.readLine()) != null) {
                content = content + model;
                index += content.length();
                stringBuffer.append(content);
                if (checkReadingComplete(index)) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e("getContent exception: " + e.toString());
        } catch (OutOfMemoryError e) {
            Logger.e("getContent exception: " + e.toString());
            System.gc();
            System.gc();
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }

    private boolean checkReadingComplete(int index) {
        Logger.e("checkReadingComplete: " + index + " : " + chapterList.size());
        if (chapterList.size() < 1) {
            return index > stepLength;
        } else {
            if (reading_chapter_sequence < (chapterList.size() - 1)) {
                if (index >= (next_chapter_start - current_chapter_start)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isCloseIO() {
        return bufferedReader == null;
    }

    private boolean openIO() {
        Logger.e("openIO");
        if (fileEncode == null) {
            fileEncode = CustomUtils.getEncode(book.file_path);
        }
        try {
            fileInputStream = new FileInputStream(book.file_path);
            inputStream = new BufferedInputStream(fileInputStream);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, fileEncode));
        } catch (Exception e) {
            Logger.e("openIO exception: " + e.toString());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 获取章节内容
     */
    @Override
    public void getChapterByLoading(final int what, int sequence) {
        Logger.e("getChapterByLoading: " + sequence);
        if (sequence < -1) {
            sequence = -1;
        } else if (chapterList != null && sequence + 1 > chapterList.size()) {
            sequence = chapterList.size() - 1;
        }
        reading_chapter_sequence = sequence;
        this.what = what;

        chapterDao = new ChapterDao(context, readStatus.book_id);
        if (chapterDao.loadChapterCount() > 0) {
            onDecomposeSuccess();
        } else {
            DecomposeCatalogDialog chapterDialog = new DecomposeCatalogDialog(readingActivity, readStatus.book_id);
            chapterDialog.setDecomposeSuccessListener(this);
            chapterDialog.initCatalog();
        }
    }

    @Override
    public Chapter getNextChapter() {
        Logger.e("getNextChapter");
        if (nextChapter != null) {

        } else if (readStatus.sequence < readStatus.chapter_count - 1) {
            int temp_chapter_id = reading_chapter_sequence;
            if (temp_chapter_id == -1) {
                reading_chapter_sequence = 0;
                if (!isCloseIO()) {
                    closeIO();
                }
                if (!openIO()) {
                    return null;
                }
            } else {
                reading_chapter_sequence++;
            }
            initReadingPosition();
            if (isCloseIO()) {
                if (!openIO()) {
                    return null;
                }
                try {
                    bufferedReader.skip(current_chapter_start);
                    char[] chars = new char[next_chapter_start - current_chapter_start];
                    bufferedReader.read(chars);
                } catch (IOException e) {
                    Logger.e("getNextChapter exception: " + e.toString());
                    e.printStackTrace();
                }
            }
            String result = null;
            do {
                result = getContent();
                Logger.e("getNextChapter1: " + result);
            } while (result.length() == model.length());

            int listSize = chapterList.size();
            if (reading_chapter_sequence >= listSize) {
                reading_chapter_sequence--;
            }
            nextChapter = chapterList.get(reading_chapter_sequence);
            nextChapter.content = result;
            if (TextUtils.isEmpty(nextChapter.name)) {
                nextChapter.name = " ";
            }
        } else {
            if (dataListener != null) {
                dataListener.showReadToast("本地导入书籍已阅读完成！");
            }
        }
        return nextChapter;
    }

    @Override
    public Chapter getPreviousChapter() {
        Logger.e("getPreviousChapter");
        if (readStatus.sequence == 0) {
            reading_chapter_sequence = -1;
            preChapter = new Chapter();
            preChapter.content = "";
            preChapter.name = "";
        } else if (readStatus.sequence > 0) {
            reading_chapter_sequence--;
            initReadingPosition();
            if (!isCloseIO()) {
                closeIO();
            }
            if (!openIO()) {
                return null;
            }
            try {
                bufferedReader.skip(current_chapter_start);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String result = null;
            result = getContent();
            Logger.e("getPreviousChapter1: " + result);
            if (reading_chapter_sequence < 0) {
                reading_chapter_sequence++;
            }
            preChapter = chapterList.get(reading_chapter_sequence);
            preChapter.content = result;
            if (TextUtils.isEmpty(preChapter.name)) {
                preChapter.name = " ";
            }
        } else {
            if (dataListener != null) {
                dataListener.showReadToast("当前已经是第一章");
            }
        }

        return preChapter;
    }

    @Override
    protected Chapter getChapter(int what, int sequence) {
        return null;
    }

    private void closeIO() {
        Logger.e("closeIO");
        try {
            fileInputStream.close();
            fileInputStream = null;
            if (bufferedReader != null) {
                bufferedReader.close();
                bufferedReader = null;
            }
            if (inputStream != null) {
                inputStream.close();
                inputStream = null;
            }
            System.gc();
        } catch (IOException e) {
            Logger.e("closeIO exception: " + e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void onDecomposeSuccess() {
        Logger.e("onDecomposeSuccess");
        LoadingPage loadingPage = getCustomLoadingPage();
        loadingPage.loading(new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                bookDaoHelper = BookDaoHelper.getInstance(context);
                book = bookDaoHelper.loadBook(readStatus.book_id, Book.TYPE_LOCAL_TXT);
                chapterList = chapterDao.loadChapters();
                readStatus.chapter_count = chapterList.size();
                openIO();
                Logger.e("reading_chapter_sequence: " + reading_chapter_sequence);
                if (reading_chapter_sequence == -1) {
                    Chapter result = new Chapter();
                    result.name = "";
                    result.content = "";
                    handler.obtainMessage(what, result).sendToTarget();
                    return null;
                }
                Chapter chapter = chapterList.get(reading_chapter_sequence);
                Logger.e("Chapter222: " + chapter.toString());
                setChapterInReading(reading_chapter_sequence);
                String content = getContent();
                Logger.e("Chapter3333: " + content + " : " + chapter);
                chapter.content = content;
                if (TextUtils.isEmpty(chapter.name)) {
                    chapter.name = " ";
                }
                Logger.e("HandlerObtainMessage");
                handler.obtainMessage(what, chapter).sendToTarget();
                return null;
            }
        });
        loadingError(loadingPage);
    }

    public void setChapterInReading(int position) {
        Logger.e("setChapterInReading");
        reading_chapter_sequence = position;
        initReadingPosition();
        if (!isCloseIO()) {
            closeIO();
        }
        if (!openIO()) {
            return;
        }
        try {
            bufferedReader.skip(current_chapter_start);
        } catch (IOException e) {
            Logger.e("setChapterInReading exception: " + e.toString());
            e.printStackTrace();
        }
    }

    private void initReadingPosition() {
        Logger.e("initReadingPosition");
        if (chapterList != null) {
            for (int i = 0; i < chapterList.size(); i++) {
                if (reading_chapter_sequence == chapterList.get(i).sequence) {
                    currentChapter = chapterList.get(i);
                    current_chapter_start = currentChapter.index_start;
                    if (i < (chapterList.size() - 1)) {
                        nextChapter = chapterList.get(i + 1);
                        next_chapter_start = nextChapter.index_start;
                    } else {
                        nextChapter = null;
                        next_chapter_start = 0;
                    }
                    break;
                }
            }
        }
    }

}
