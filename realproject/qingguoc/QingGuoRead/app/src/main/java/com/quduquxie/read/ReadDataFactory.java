package com.quduquxie.read;

import android.content.Context;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.quduquxie.application.QuApplication;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.bean.Chapter;
import com.quduquxie.base.config.BaseConfig;
import com.quduquxie.base.database.dao.ChapterDao;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.base.util.CommunalUtil;
import com.quduquxie.base.util.NetworkUtil;
import com.quduquxie.communal.utils.FileUtil;
import com.quduquxie.module.read.reading.view.ReadingActivity;
import com.quduquxie.retrofit.DataRequestService;
import com.quduquxie.retrofit.ServiceGenerator;
import com.quduquxie.retrofit.model.CommunalResult;
import com.quduquxie.util.BookHelper;
import com.quduquxie.widget.LoadingPage;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

public class ReadDataFactory extends IReadDataFactory {

    LoadingPage loadingPage;

    public ReadDataFactory(Context context, ReadingActivity readingActivity, ReadStatus readStatus, NovelHelper novelHelper) {
        super(context, readingActivity, readStatus, novelHelper);

    }

    /**
     * 获取章节内容
     */
    @Override
    public void getChapterByLoading(final int what, int sequence) {

        if (sequence < -1) {
            sequence = -1;
        } else if (chapterList != null && chapterList.size() > 0 && sequence + 1 > chapterList.size()) {
            sequence = chapterList.size() - 1;
        }
        final int temp_sequence = sequence;
        loadingPage = null;
        loadingPage = getCustomLoadingPage();
        loadingPage.loading(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Book local_book = BookDaoHelper.getInstance(context).loadBook(readStatus.book.id, Book.TYPE_ONLINE);
                if (local_book != null && local_book.chapters_update_state == 1) {
                    DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, false);
                    dataRequestService.loadBookCatalog(readStatus.book_id, local_book.chapters_update_index)
                            .subscribeOn(Schedulers.trampoline())
                            .observeOn(Schedulers.trampoline())
                            .doOnNext(new Consumer<CommunalResult<ArrayList<Chapter>>>() {
                                @Override
                                public void accept(@NonNull CommunalResult<ArrayList<Chapter>> chapterListResult) throws Exception {
                                    if (chapterListResult != null && chapterListResult.getCode() == 0 && chapterListResult.getModel() != null && chapterListResult.getModel().size() > 0) {
                                        Logger.d("LoadBookCatalog call: 数据库操作");

                                        ArrayList<Chapter> chapters = chapterListResult.getModel();

                                        ChapterDao chapterDao = new ChapterDao(context, readStatus.book_id);

                                        chapterDao.insertBookChapters(chapters);

                                        Chapter lastChapter = chapters.get(chapters.size() - 1);

                                        Book book = new Book();
                                        book.id = readStatus.book_id;
                                        book.chapter = new Chapter();
                                        book.chapter.sn = lastChapter.sn;
                                        book.chapters_update_state = 2;
                                        book.chapters_update_index = lastChapter.sn + 1;
                                        BookDaoHelper.getInstance(context).updateBook(book);

                                        ArrayList<Chapter> localChapterList = chapterDao.loadChapters();

                                        BookHelper.deleteChapterCache(book.id, localChapterList);

                                        Logger.d("LoadBookCatalog call: 数据库操作完成");
                                    }
                                }
                            })
                            .observeOn(Schedulers.trampoline())
                            .subscribe(new ResourceSubscriber<CommunalResult<ArrayList<Chapter>>>() {
                                @Override
                                public void onNext(CommunalResult<ArrayList<Chapter>> chapterListResult) {
                                    Logger.d("LoadBookCatalog onNext");

                                    ChapterDao chapterDao = new ChapterDao(context, readStatus.book_id);

                                    if (chapterListResult != null) {
                                        if (chapterListResult.getCode() == 0 && chapterListResult.getModel() != null && chapterListResult.getModel().size() > 0) {
                                            chapterList = chapterDao.loadChapters();
                                        } else {
                                            chapterList = chapterDao.loadChapters();
                                        }
                                    } else {
                                        chapterList = chapterDao.loadChapters();
                                    }
                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    Logger.d("LoadBookCatalog onError: " + throwable.toString());
                                    chapterList = null;
                                }

                                @Override
                                public void onComplete() {
                                    Logger.d("LoadBookCatalog onComplete");
                                }
                            });
                } else if (chapterList == null || chapterList.isEmpty()) {

                    ChapterDao chapterDao = new ChapterDao(context, readStatus.book_id);
                    ArrayList<Chapter> localChapterList = chapterDao.loadChapters();

                    if (localChapterList == null || localChapterList.size() == 0) {
                        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, false);
                        dataRequestService.loadBookCatalog(readStatus.book_id, 1)
                                .subscribeOn(Schedulers.trampoline())
                                .observeOn(Schedulers.trampoline())
                                .doOnNext(new Consumer<CommunalResult<ArrayList<Chapter>>>() {
                                    @Override
                                    public void accept(@NonNull CommunalResult<ArrayList<Chapter>> chapterListResult) throws Exception {
                                        if (chapterListResult != null && chapterListResult.getCode() == 0 && chapterListResult.getModel() != null && chapterListResult.getModel().size() > 0) {
                                            Logger.d("LoadBookCatalog call: 数据库操作");

                                            ArrayList<Chapter> chapters = chapterListResult.getModel();

                                            BookDaoHelper bookDaoHelper = BookDaoHelper.getInstance(context);

                                            if (bookDaoHelper.subscribeBook(readStatus.book_id)) {
                                                ChapterDao chapterDao = new ChapterDao(context, readStatus.book_id);

                                                chapterDao.insertBookChapters(chapters);

                                                Chapter lastChapter = chapters.get(chapters.size() - 1);

                                                Book book = new Book();
                                                book.id = readStatus.book_id;
                                                book.chapter = new Chapter();
                                                book.chapter.sn = lastChapter.sn;
                                                book.chapters_update_state = 2;
                                                book.chapters_update_index = lastChapter.sn + 1;
                                                bookDaoHelper.updateBook(book);
                                            }

                                            Logger.d("LoadBookCatalog call: 数据库操作完成");
                                        }
                                    }
                                })
                                .observeOn(Schedulers.trampoline())
                                .subscribe(new ResourceSubscriber<CommunalResult<ArrayList<Chapter>>>() {
                                    @Override
                                    public void onNext(CommunalResult<ArrayList<Chapter>> chapterListResult) {
                                        Logger.d("LoadBookCatalog onNext");
                                        if (chapterListResult != null) {
                                            if (chapterListResult.getCode() == 0 && chapterListResult.getModel() != null && chapterListResult.getModel().size() > 0) {
                                                chapterList = chapterListResult.getModel();
                                            } else {
                                                chapterList = null;
                                            }
                                        } else {
                                            chapterList = null;
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        Logger.d("LoadBookCatalog onError: " + throwable.toString());
                                        chapterList = null;
                                    }

                                    @Override
                                    public void onComplete() {
                                        Logger.d("LoadBookCatalog onComplete");
                                    }
                                });
                    } else {
                        if (chapterList == null) {
                            chapterList = new ArrayList<>();
                        } else {
                            chapterList.clear();
                        }

                        for (Chapter chapter : localChapterList) {
                            chapterList.add(chapter);
                        }
                    }
                }

                if (chapterList == null) {
                    throw new Exception("Book List Empty;");
                }

                readStatus.chapter_count = chapterList.size();

                if (temp_sequence == -1) {
                    Chapter result = new Chapter();
                    result.name = "";
                    result.content = "";
                    handler.obtainMessage(what, result).sendToTarget();
                    return null;
                }

                final Chapter chapter = chapterList.get(temp_sequence);

                if (NetworkUtil.loadNetworkType(context) != NetworkUtil.NETWORK_NONE || FileUtil.checkFileExist(chapter.id, readStatus.book_id)) {
                    if (chapter != null && !TextUtils.isEmpty(chapter.id)) {
                        String content = FileUtil.loadChapterFromCache(chapter.id, readStatus.book_id);

                        if (!TextUtils.isEmpty(content) && !("null".equals(content))) {
                            if (!TextUtils.equals(chapter.status, BaseConfig.ENABLE)) {
                                chapter.content = BaseConfig.DISABLE_MESSAGE;
                            }
                            if (!TextUtils.isEmpty(content)) {
                                chapter.content = content;
                            }
                            if (TextUtils.isEmpty(content)) {
                                chapter.content = BaseConfig.ERROR;
                            }
                        } else {
                            DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, false);
                            dataRequestService.loadChapter(chapter.id)
                                    .subscribeOn(Schedulers.trampoline())
                                    .observeOn(Schedulers.trampoline())
                                    .subscribe(new ResourceSubscriber<CommunalResult<Chapter>>() {
                                        @Override
                                        public void onNext(CommunalResult<Chapter> chapterResult) {
                                            Logger.d("LoadChapter onNext");

                                            if (chapterResult != null) {
                                                if (chapterResult.getCode() == 0 && chapterResult.getModel() != null) {

                                                    Chapter chapterObject = chapterResult.getModel();

                                                    String chapterContent = chapterObject.content;

                                                    if (!TextUtils.equals(chapterObject.status, BaseConfig.ENABLE)) {
                                                        chapterObject.content = BaseConfig.DISABLE_MESSAGE;
                                                        chapter.content = BaseConfig.DISABLE_MESSAGE;
                                                    } else if (TextUtils.isEmpty(chapterContent) || ("null".equals(chapterContent))) {
                                                        chapterObject.content = BaseConfig.ERROR;
                                                        chapter.content = BaseConfig.ERROR;
                                                    } else {
                                                        chapter.content = chapterContent;
                                                        ChapterDao chapterDao = new ChapterDao(context, readStatus.book_id);
                                                        chapterDao.updateChapter(chapterObject);
                                                        FileUtil.saveChapterToCache(chapterContent, chapterObject.id, readStatus.book_id);

                                                    }
                                                } else {
                                                    chapter.content = BaseConfig.ERROR;
                                                }
                                            } else {
                                                chapter.content = BaseConfig.ERROR;
                                            }
                                        }

                                        @Override
                                        public void onError(Throwable throwable) {
                                            Logger.d("LoadChapter onError: " + throwable.toString());
                                        }

                                        @Override
                                        public void onComplete() {
                                            Logger.d("LoadChapter onComplete");
                                        }
                                    });
                        }
                    }
                    chapterList.remove(temp_sequence);
                    chapterList.add(temp_sequence, chapter);
                }
                handler.obtainMessage(what, chapter).sendToTarget();
                return null;
            }
        });

        loadingError(loadingPage);
    }

    @Override
    public Chapter getNextChapter() {
        if (nextChapter != null) {

        } else if (readStatus.sequence < chapterList.size() - 1) {
            if (CommunalUtil.checkChapterExist(chapterList.get(readStatus.sequence + 1).id, readStatus.book_id)) {
                nextChapter = getChapter(ReadingActivity.MESSAGE_LOAD_NEXT_CHAPTER, readStatus.sequence + 1);
            } else {
                if (NetworkUtil.loadNetworkType(QuApplication.getInstance()) != NetworkUtil.NETWORK_NONE) {
                    readStatus.loading = true;
                    try {
                        getChapterByLoading(ReadingActivity.MESSAGE_LOAD_NEXT_CHAPTER, readStatus.sequence + 1);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                } else if (dataListener != null) {
                    dataListener.showReadToast("网络不给力，请稍后重试");
                }
            }
        } else {
            if (NetworkUtil.loadNetworkType(QuApplication.getInstance()) != NetworkUtil.NETWORK_NONE) {
                readStatus.loading = true;
                if (dataListener != null) {
                    dataListener.gotoOver();
                }
            } else if (dataListener != null) {
                dataListener.showReadToast("网络不给力，请稍后重试");
            }
        }

        return nextChapter;
    }

    @Override
    public Chapter getPreviousChapter() {
        if (readStatus.sequence == 0) {
            preChapter = new Chapter();
            preChapter.content = "";
            preChapter.name = "";

        } else if (preChapter != null) {

        } else if (readStatus.sequence > 0) {
            if (CommunalUtil.checkChapterExist(chapterList.get(readStatus.sequence - 1).id, readStatus.book_id)) {
                preChapter = getChapter(ReadingActivity.MESSAGE_LOAD_PREVIOUS_CHAPTER, readStatus.sequence - 1);
            } else {
                if (NetworkUtil.loadNetworkType(QuApplication.getInstance()) != NetworkUtil.NETWORK_NONE) {
                    readStatus.loading = true;
                    getChapterByLoading(ReadingActivity.MESSAGE_LOAD_PREVIOUS_CHAPTER, readStatus.sequence - 1);
                } else {
                    if (dataListener != null) {
                        dataListener.showReadToast("网络不给力，请稍后重试");
                    }
                }
            }
        } else {
            // 第一页
            if (dataListener != null) {
                dataListener.showReadToast("当前已经是第一章");
            }
        }

        return preChapter;
    }

    @Override
    protected Chapter getChapter(int what, int sequence) {
        if (chapterList == null || chapterList.isEmpty()) {
            chapterList = new ChapterDao(context, readStatus.book_id).loadChapters();
            return null;
        }
        if (sequence < 0) {
            sequence = 0;
        } else if (sequence >= chapterList.size()) {
            sequence = chapterList.size() - 1;
        }
        Chapter chapter = chapterList.get(sequence);
        try {
            String content = FileUtil.loadChapterFromCache(chapter.id, readStatus.book_id);
            if (!TextUtils.isEmpty(content) && !("null".equals(content) || "isChapterExists".equals(content))) {
                chapter.content = content;
            } else {
                chapter = null;
                getChapterByLoading(what, sequence);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return chapter;
    }


}
