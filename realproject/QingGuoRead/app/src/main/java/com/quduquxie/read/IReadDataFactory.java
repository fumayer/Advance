package com.quduquxie.read;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.dingyueads.sdk.Bean.Novel;
import com.dingyueads.sdk.NativeInit;
import com.orhanobut.logger.Logger;
import com.quduquxie.Constants;
import com.quduquxie.application.QuApplication;
import com.quduquxie.base.bean.Chapter;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.base.util.NetworkUtil;
import com.quduquxie.manager.StatisticManager;
import com.quduquxie.module.read.reading.view.ReadingActivity;
import com.quduquxie.read.page.PageInterface;
import com.quduquxie.util.BookHelper;
import com.quduquxie.widget.LoadingPage;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public abstract class IReadDataFactory {

    protected Context context;
    protected PageInterface pageView;
    protected ReadStatus readStatus;
    private BookDaoHelper bookDaoHelper;
    public ArrayList<Chapter> chapterList;
    public boolean toChapterStart;

    public Chapter nextChapter;
    public Chapter preChapter;
    public Chapter currentChapter;

    protected int tempCurrentPage;
    protected int tempPageCount;
    protected int tempSequence;
    protected String tempChapterName;
    protected ArrayList<String> tempChapterNameList;
    protected Chapter tempNextChapter;
    protected Chapter tempPreviousChapter;
    protected Chapter tempCurrentChapter;
    protected int tempOffset;
    protected ArrayList<ArrayList<String>> tempLineList;
    protected NovelHelper myNovelHelper;
    protected WeakReference<ReadingActivity> readActivityReference;

    protected ReadDataListener dataListener;
    protected ReadingActivity readingActivity;

    protected ArrayList<Chapter> readedChapter;

    private StatisticManager statisticManager;

    protected ReadHandler handler = new ReadHandler(this);

    public IReadDataFactory(Context context, ReadingActivity readingActivity, ReadStatus readStatus, NovelHelper novelHelper) {
        this.context = context;
        this.readingActivity = readingActivity;
        this.readStatus = readStatus;
        this.myNovelHelper = novelHelper;
        this.readActivityReference = new WeakReference<>(readingActivity);

        readedChapter = new ArrayList<>();
    }


    public void setPageView(PageInterface pageView) {
        this.pageView = pageView;
    }

    public void setReadDataListener(ReadDataListener listener) {
        this.dataListener = listener;
    }

    public void saveData() {
        tempCurrentPage = readStatus.current_page;
        tempPageCount = readStatus.page_count;
        tempSequence = readStatus.sequence;
        tempOffset = readStatus.offset;
        tempChapterName = readStatus.chapter_name;
        tempChapterNameList = readStatus.chapter_name_list;

        tempNextChapter = nextChapter;
        tempCurrentChapter = currentChapter;
        tempPreviousChapter = preChapter;


        tempLineList = readStatus.line_list;
    }

    public void restore() {
        readStatus.current_page = tempCurrentPage;
        readStatus.page_count = tempPageCount;
        readStatus.sequence = tempSequence;
        readStatus.offset = tempOffset;
        readStatus.chapter_name = tempChapterName;
        readStatus.chapter_name_list = tempChapterNameList;

        nextChapter = tempNextChapter;
        currentChapter = tempCurrentChapter;
        preChapter = tempPreviousChapter;

        readStatus.line_list = tempLineList;
    }

    /**
     * 加载失败时打点
     */
    public void loadingError(LoadingPage loadingPage) {
        if (NetworkUtil.NETWORK_TYPE == NetworkUtil.NETWORK_NONE) {
            return;
        }
        loadingPage.setErrorAction(new Runnable() {

            @Override
            public void run() {
                handler.sendEmptyMessage(ReadingActivity.MESSAGE_LOAD_CHAPTER_ERROR);

            }
        });
    }

    protected LoadingPage getCustomLoadingPage() {
        LoadingPage loadingPage;
        loadingPage = new LoadingPage(readingActivity);
        loadingPage.setCustomBackground();
        return loadingPage;
    }

    private void loadCurrentChapter(Message msg) {
        currentChapter = (Chapter) msg.obj;
        if (readStatus != null && currentChapter != null && currentChapter.sequence != -1) {
            readStatus.sequence = currentChapter.sequence;
        }
        initBookCallBack();
    }

    private void loadNextChapter(Message msg) {
        nextChapter = (Chapter) msg.obj;
        nextChapterCallBack(true);
    }

    private void loadPreChapter(Message msg) {
        preChapter = (Chapter) msg.obj;
        preChapterCallBack(true);
    }

    private void loadJumpChapter(Message msg) {
        currentChapter = (Chapter) msg.obj;
        ReadingActivity readingActivity = readActivityReference.get();
        if (readingActivity != null) {
            readingActivity.jumpChapterCallBack();
        }

        Constants.readedCount++;
    }

    private void loadError(Message msg) {
        if (msg.obj != null && dataListener != null) {
            String error = msg.obj.toString();
            String chapter_name = "";
            if (chapterList != null && msg.arg1 - 1 >= 0 && msg.arg1 - 1 < chapterList.size()) {
                Chapter chapter = chapterList.get(msg.arg1 - 1);
                if (chapter != null) {
                    chapter_name = chapter.name;
                }
            }
            if (bookDaoHelper == null) {
                bookDaoHelper = BookDaoHelper.getInstance(context);
            }
        }
    }

    protected static class ReadHandler extends Handler {
        private WeakReference<IReadDataFactory> reference;

        ReadHandler(IReadDataFactory instance) {
            reference = new WeakReference<>(instance);
        }

        @Override
        public void handleMessage(Message message) {
            IReadDataFactory dataFactory = reference.get();
            if (dataFactory == null) {
                return;
            }
            switch (message.what) {
                case ReadingActivity.MESSAGE_LOAD_JUMP_CHAPTER:
                    Logger.e("MESSAGE_LOAD_JUMP_CHAPTER");
                    dataFactory.loadJumpChapter(message);
                    break;
                case ReadingActivity.MESSAGE_LOAD_NEXT_CHAPTER:
                    Logger.e("MESSAGE_LOAD_NEXT_CHAPTER");
                    dataFactory.loadNextChapter(message);
                    break;
                case ReadingActivity.MESSAGE_LOAD_CURRENT_CHAPTER:
                    Logger.e("MESSAGE_LOAD_CURRENT_CHAPTER");
                    dataFactory.loadCurrentChapter(message);
                    break;
                case ReadingActivity.MESSAGE_LOAD_PREVIOUS_CHAPTER:
                    Logger.e("MESSAGE_LOAD_PREVIOUS_CHAPTER");
                    dataFactory.loadPreChapter(message);
                    break;
                case ReadingActivity.MESSAGE_LOAD_CHAPTER_ERROR:
                    Logger.e("MESSAGE_LOAD_CHAPTER_ERROR");
                    dataFactory.loadError(message);
                    break;
            }
        }
    }

    /**
     * 打开书籍取得书签章节内容后的处理
     */
    private void initBookCallBack() {
        Logger.e("initBookCallBack");
        Constants.readedCount++;
        if (chapterList == null) {
            return;
        }
        // 章节数
        if (readStatus != null) {
            readStatus.chapter_count = chapterList.size();
        }

        // 初始化章节内容
        if (myNovelHelper != null && readStatus != null) {
            myNovelHelper.getChapterContent(context, readingActivity, currentChapter, readStatus.book, false);
        }
        // 刷新页面
        if (dataListener != null) {
            dataListener.freshPage();
        }
        if (pageView != null) {
            pageView.drawCurrentPage();
            pageView.drawNextPage();
            pageView.getChapter(true);
        }
        if (dataListener != null) {
            dataListener.initBookStateDeal();
        }
    }

    /**
     * 翻页到下一章的处理
     */
    protected void nextChapterCallBack(boolean drawCurrent) {

        if (readStatus.sequence != -1) {
            statistics();
        }

        Constants.readedCount++;
        preChapter = currentChapter;
        currentChapter = nextChapter;
        nextChapter = null;
        readStatus.sequence++;
        readStatus.offset = 0;
        myNovelHelper.isShown = false;

        myNovelHelper.getChapterContent(context, readingActivity, currentChapter, readStatus.book, false);
        readStatus.current_page = 1;
        pageView.drawNextPage();
        if (drawCurrent) {
            pageView.drawCurrentPage();
        }
        pageView.getNextChapter();
        if (dataListener != null) {
            dataListener.downLoadNovelMore();
        }
        if (dataListener != null) {
            dataListener.freshPage();
            dataListener.changeChapter();
        }
        readStatus.loading = false;
    }

    /**
     * 翻页到上一章的处理
     */
    protected void preChapterCallBack(boolean drawCurrent) {
        Constants.readedCount++;
        nextChapter = currentChapter;
        currentChapter = preChapter;
        preChapter = null;
        readStatus.sequence--;
        readStatus.offset = 0;
        myNovelHelper.isShown = false;
        myNovelHelper.getChapterContent(context, readingActivity, currentChapter, readStatus.book, false);
        if (toChapterStart) {
            readStatus.current_page = 1;
        } else {
            readStatus.current_page = readStatus.page_count;
        }
        toChapterStart = false;
        pageView.drawNextPage();
        if (drawCurrent) {
            pageView.drawCurrentPage();
        }
        pageView.getPreChapter();
        if (dataListener != null) {
            dataListener.freshPage();
            dataListener.changeChapter();
        }
        readStatus.loading = false;
    }

    public void statistics() {
        if (statisticManager == null) {
            statisticManager = StatisticManager.getStatisticManager();
        }

        if (readStatus != null) {
            //翻到下一章时处理上一章时处理当前章节广告
            if (readStatus.book != null && readStatus.book.book_type == 0) {
                Novel novel = transformation();
                statisticManager.schedulingRequest(null, novel, StatisticManager.TYPE_END, NativeInit.ad_position[1], QuApplication.getUserDeviceID());
            }
        }
    }

    public Novel transformation() {
        Novel novel = new Novel();
        if (readStatus != null && readStatus.book != null) {
            novel.novelId = readStatus.book.id;
            novel.author = readStatus.book.author.name;
            novel.label = readStatus.book.category;
        }
        if (currentChapter != null) {
            novel.chapterId = String.valueOf(currentChapter.sn);
        } else if (tempCurrentChapter != null) {
            novel.chapterId = String.valueOf(tempCurrentChapter.sn);
        }
        novel.channelCode = "A001";
        return novel;
    }

    public boolean next() {

        saveData();
        boolean isPrepared = false;

        if (readStatus.current_page < readStatus.page_count) {
            readStatus.current_page++;
            if (dataListener != null) {
                dataListener.freshPage();
            }
            isPrepared = true;

        } else {
            if (readStatus.sequence == readStatus.chapter_count - 1) {
                if (readStatus.book.book_type != 0 && dataListener != null) {
                    dataListener.showReadToast("本地导入书籍已阅读完成！");
                }
                if (readStatus.book.book_type == 0) {
                    getNextChapter();
                }
                return false;
            }

            nextChapter = null;
            isPrepared = getNextChapter() != null;
            if (isPrepared || readStatus.book.book_type != 0) {
                nextChapterCallBack(false);
                int type = BookHelper.getChapterType(currentChapter);
                if (type == BookHelper.CHAPTER_TYPE_EMPTY) {
                    isPrepared = false;
                }
            }
        }

        return isPrepared;
    }

    public boolean previous() {
        saveData();
        boolean isPrepared = false;
        if (readStatus.current_page > 1) {
            readStatus.current_page--;
            if (dataListener != null) {
                dataListener.freshPage();
            }
            isPrepared = true;
        } else {
            if (readStatus.sequence == -1) {
                if (dataListener != null) {
                    dataListener.showReadToast("当前已经是第一章");
                }
                return false;
            }

            if (readStatus.sequence > chapterList.size()) {
                readStatus.sequence = chapterList.size();
            }

            preChapter = null;
            isPrepared = getPreviousChapter() != null;
            if (isPrepared || readStatus.book.book_type != 0) {
                preChapterCallBack(false);
            }
        }
        return isPrepared;
    }

    public abstract void getChapterByLoading(final int what, int sequence);

    public abstract Chapter getNextChapter();

    public abstract Chapter getPreviousChapter();

    protected abstract Chapter getChapter(int what, int sequence);

    public interface ReadDataListener {
        void freshPage();

        void gotoOver();

        void showReadToast(String message);

        void downLoadNovelMore();

        void initBookStateDeal();

        void changeChapter();
    }

    public void recycleResource() {

        if (this.context != null) {
            this.context = null;
        }

        if (this.readingActivity != null) {
            this.readingActivity = null;
        }

        if (this.readStatus != null) {
            this.readStatus = null;
        }

        if (this.pageView != null) {
            this.pageView = null;
        }

        if (this.chapterList != null) {
            this.chapterList.clear();
            this.chapterList = null;
        }

        if (this.tempLineList != null) {
            this.tempLineList.clear();
            this.tempLineList = null;
        }

        if (this.nextChapter != null) {
            this.nextChapter = null;
        }

        if (this.tempNextChapter != null) {
            this.tempNextChapter = null;
        }

        if (this.preChapter != null) {
            this.preChapter = null;
        }

        if (this.tempPreviousChapter != null) {
            this.tempPreviousChapter = null;
        }

        if (this.currentChapter != null) {
            this.currentChapter = null;
        }

        if (this.tempCurrentChapter != null) {
            this.tempCurrentChapter = null;
        }
    }
}
