package com.quduquxie.read.local;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.bean.Chapter;
import com.quduquxie.base.database.dao.ChapterDao;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.util.BaseAsyncTask;
import com.quduquxie.util.CustomUtils;
import com.quduquxie.util.MD5;
import com.quduquxie.util.QGLog;

public class DecomposeCatalogMethod {

    private static final String TAG = DecomposeCatalogMethod.class.getSimpleName();

    private Book book;
    private String model = "\r\n";
    private String id_book;
    private String fileEncode;

    public DecomposeCatalogTask decomposeCatalogTask;

    private BookDaoHelper bookDaoHelper;
    private ChapterDao chapterDao;

    private Activity activity;
    private Context context;
    private Pattern pattern;

    public ArrayList<Chapter> chapterLocalList = new ArrayList<>();
    public ArrayList<Chapter> chapterLocalResult = new ArrayList<>();

    private final static int NO_MATCH_MAX_LINE = 250;

    private static final int kHasNotParse = 1;
    private static final int kHasParse = 3;
    private static final int kParsedEmpty = 4;
    private ArrayList<OnDecomposeCatalogListener> decomposeCatalogListeners = new ArrayList<>();
    private boolean hasParser = false;
    private boolean parseResult = true;

    public DecomposeCatalogMethod(Context context, String id_book) {
        this.context = context;
        this.activity = (Activity) context;
        this.id_book = id_book;
        bookDaoHelper = BookDaoHelper.getInstance(context);
        chapterDao = new ChapterDao(context, id_book);
        book = bookDaoHelper.loadBook(id_book, Book.TYPE_LOCAL_TXT);

        pattern = Pattern.compile("((\\n|\\r|\\r\\n)(\\s{1,10}))?第[0-9一二三四五六七八九十百千零]+[章回节卷集幕计段](.*)(\\n|\\r|\\r\\n)");
    }

    public void startDecomposeCatalogTask() {
        Logger.e("startDecomposeCatalogTask");
        new Thread(new Runnable() {
            @Override
            public void run() {
                fileEncode = CustomUtils.getEncode(book.file_path);
                model = CustomUtils.getFileMode(book.file_path, fileEncode);
                decomposeCatalogTask = new DecomposeCatalogTask();
                decomposeCatalogTask.execute();
            }
        }).start();
    }

    public class DecomposeCatalogTask extends BaseAsyncTask<Object, Object, Object> {
        private boolean isParseFinished = false;

        @Override
        protected Object doInBackground(Object... params) {
            Logger.e("doInBackground");
            parseCatalogList();
            checkCatalogList();
            return null;
        }

        public void parseCatalogList() {
            Logger.e("parseCatalogList");
            long start = System.currentTimeMillis();

            InputStream inputStream;
            BufferedReader bufferedReader;
            FileInputStream fileInputStream;

            String content;
            int index = 0;

            try {
                fileInputStream = new FileInputStream(book.file_path);
                inputStream = new BufferedInputStream(fileInputStream);
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, fileEncode));

                while ((content = bufferedReader.readLine()) != null) {
                    content = content + model + readFileUtil(bufferedReader);
                    checkCatalog(content, index);
                    index += content.length();
                    if (decomposeCatalogTask.isCancelled() || activity.isFinishing()) {
                        parseResult = false;
                        break;
                    }
                }
                fileInputStream.close();
                bufferedReader.close();
                inputStream.close();
                isParseFinished = true;
            } catch (Exception e) {
                e.printStackTrace();
                isParseFinished = false;
                decomposeCatalogTask.cancel(true);
            }

            long end = System.currentTimeMillis();

            QGLog.e(TAG, "ConsumeTime: " + (end - start));
        }

        private void checkCatalogList() {
            Logger.e("checkCatalogList");
            if (chapterLocalList != null && chapterLocalList.size() > 2) {
                if (chapterLocalResult == null) {
                    chapterLocalList = new ArrayList<>();
                }

                boolean firstChapterSort = false;
                int sequence = 0;

                for (int i = 0; i < chapterLocalList.size(); i++) {
                    if (i == 0) {
                        if (chapterLocalList.get(0).index_start > 200) {
                            //添加开始章节
                            Chapter chapter = new Chapter();
                            chapter.name = "开始";
                            chapter.id = MD5.encodeMD5String(id_book + "开始" + sequence);
                            chapter.index_start = 0;
                            chapter.sequence = sequence;
                            chapter.sn = sequence + 1;
                            chapterLocalResult.add(chapter);
                            sequence++;

                            Chapter firstChapter = chapterLocalList.get(0);
                            firstChapter.sequence = sequence;
                            firstChapter.sn = sequence + 1;
                            firstChapter.id = MD5.encodeMD5String(id_book + firstChapter.name + sequence);
                            chapterLocalResult.add(firstChapter);
                            sequence++;

                            QGLog.e(TAG, "开始章节");
                        } else {
                            firstChapterSort = true;
                        }
                    } else {
                        if ((i + 1) < chapterLocalList.size() - 1) {
                            if ((chapterLocalList.get(i + 1).index_start  - chapterLocalList.get(i).index_start) > 500) {
                                Chapter chapter = chapterLocalList.get(i);
                                chapter.sequence = sequence;
                                chapter.sn = sequence + 1;
                                chapter.id = MD5.encodeMD5String(id_book + chapter.name + sequence);
                                if (firstChapterSort) {
                                    chapter.index_start = 0;
                                    firstChapterSort = false;
                                }
                                chapterLocalResult.add(chapter);
                                sequence++;
                            } else {
                                chapterLocalList.remove(i + 1);
                                i--;
                            }
                        } else {
                            Chapter chapter = chapterLocalList.get(i);
                            chapter.sequence = sequence;
                            chapter.sn = sequence + 1;
                            chapter.id = MD5.encodeMD5String(id_book + chapter.name + sequence);
                            if (firstChapterSort) {
                                chapter.index_start = 0;
                                firstChapterSort = false;
                            }
                            chapterLocalResult.add(chapter);
                            sequence++;
                        }
                    }
                }

                chapterLocalList.clear();

                for (int i = 0; i < chapterLocalResult.size(); i++) {
                    chapterLocalList.add(chapterLocalResult.get(i));
                }

                chapterLocalResult.clear();
                isParseFinished = true;
            } else if (chapterLocalList != null && chapterLocalList.size() < 2) {

                InputStream inputStream;
                BufferedReader bufferedReader;
                FileInputStream fileInputStream;

                String content;

                int index = 0;
                int chapter_count = 0;
                int line_count = 0;
                try {
                    if (chapterLocalList == null) {
                        chapterLocalList = new ArrayList<>();
                    }

                    fileInputStream = new FileInputStream(book.file_path);
                    inputStream = new BufferedInputStream(fileInputStream);
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream, fileEncode));

                    while ((content = bufferedReader.readLine()) != null) {
                        line_count++;
                        content = content + model;
                        index += content.length();
                        if((line_count % NO_MATCH_MAX_LINE) == 0){
                            chapter_count++;
                            Chapter chapter = new Chapter();
                            chapter.name = "第"+ (chapter_count) +"章";
                            chapter.id = MD5.encodeMD5String(id_book + chapter.name + (chapter_count - 1));
                            chapter.sn = chapter_count;
                            chapter.index_start = index;
                            chapter.sequence = chapter_count - 1;
                            chapterLocalList.add(chapter);

                            publishProgress(chapter);
                        }
                        if (decomposeCatalogTask.isCancelled() || activity.isFinishing()) {
                            parseResult = false;
                            break;
                        }
                    }
                    isParseFinished = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    isParseFinished = false;
                    decomposeCatalogTask.cancel(true);
                }

                if (chapterLocalList != null && chapterLocalList.size() == 0) {
                    Chapter chapter = new Chapter();
                    chapter.name = "开始";
                    chapter.id = MD5.encodeMD5String(id_book + chapter.name);
                    chapter.index_start = 0;
                    chapter.sequence = 0;
                    chapter.sn = 1;
                    chapterLocalList.add(chapter);
                    publishProgress(chapter);
                }
            }
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            Logger.e("onPostExecute");
            QGLog.e("DecomposeCatalogMethod", "DecomposeCatalogMethod: parseResult: " + parseResult);
            if (parseResult) {
                saveLocalChapterDao();
                taskOver();
            }
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
            Logger.e("onProgressUpdate");
            for (OnDecomposeCatalogListener decomposeCatalogListener : decomposeCatalogListeners) {
                decomposeCatalogListener.updateProgress(chapterLocalList.size());
            }
        }

        private void taskOver() {
            Logger.e("taskOver");
            if (isParseFinished) {
                if (chapterLocalList.size() == 0) {
                    notify(kParsedEmpty);
                } else {
                    notify(kHasParse);
                }
            } else {
                notify(kHasNotParse);
            }
        }

        private void saveLocalChapterDao() {
            Logger.e("saveLocalChapterDao");
            if (!hasParser && chapterLocalList != null && !chapterLocalList.isEmpty()) {
                boolean insertResult = chapterDao.insertBookChapters(chapterLocalList);
                if (insertResult) {
                    bookDaoHelper = BookDaoHelper.getInstance(context);
                    Book book = new Book();
                    book.id = id_book;
                    book.book_type = Book.TYPE_LOCAL_TXT;
                    book.chapter = new Chapter();
                    book.chapter.sn = chapterLocalList.size();
                    book.chapter.name = chapterLocalList.get(chapterLocalList.size() - 1).name;
                    bookDaoHelper.updateBook(book);
                }
            }
        }

        private void notify(int state) {
            Logger.e("notify");
            for (OnDecomposeCatalogListener decomposeCatalogListener : decomposeCatalogListeners) {
                decomposeCatalogListener.notifySuccess(state);
            }
        }

        private void checkCatalog(String content, int index) {
            Logger.e("checkCatalog");
            if (TextUtils.isEmpty(content)) {
                return;
            }

            Matcher matcher = pattern.matcher(content);

            while (matcher.find()) {
                String catalog = matcher.group();
                if (TextUtils.isEmpty(catalog)) {
                    return;
                }
                int position = matcher.start();
                position += index;

                Log.e(TAG, "CheckCatalog: " + catalog + " : " + position);

                catalog = replaceSpecificCharacter(catalog);

                if (checkCatalogLength(catalog)) {
                    insertCatalog(catalog, position);
                } else {
                    insertCatalog(catalog.substring(0, 30), position);
                }
            }
        }

        private boolean checkCatalogLength(String catalog) {
            Logger.e("checkCatalogLength");
            return catalog.length() <= 30;
        }

        public String replaceSpecificCharacter(String catalog) {
            Logger.e("replaceSpecificCharacter");
            if (!TextUtils.isEmpty(catalog)) {
                return catalog.replaceAll("\\s*|\t|\r|\n", "");
            }
            return catalog;
        }

        private void insertCatalog(String catalog, int position) {
            Logger.e("insertCatalog");
            if (chapterLocalList == null) {
                chapterLocalList = new ArrayList<>();
            }
            Chapter chapter = new Chapter();
            chapter.name = catalog;
            chapter.index_start = position;
            chapterLocalList.add(chapter);
            publishProgress(chapter);
        }
    }

    private String readFileUtil(BufferedReader bufferedReader) {
        Logger.e("readFileUtil");
        StringBuilder stringBuilder = new StringBuilder();
        String content;
        int lines = 0;
        try {
            while (lines < 25 && (content = bufferedReader.readLine()) != null) {
                content = content + model;
                stringBuilder.append(content);
                lines++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            decomposeCatalogTask.cancel(true);
        }
        return stringBuilder.toString();
    }

    public void addDecomposeCatalogListener(OnDecomposeCatalogListener decomposeCatalogListener) {
        decomposeCatalogListeners.add(decomposeCatalogListener);
    }

    public void removeDecomposeCatalogListener(OnDecomposeCatalogListener decomposeCatalogListener) {
        decomposeCatalogListeners.remove(decomposeCatalogListener);
    }

    public interface OnDecomposeCatalogListener {
        void updateProgress(int progress);

        void notifySuccess(int state);
    }
}
