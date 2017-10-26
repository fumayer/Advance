package com.quduquxie.base.database.helper;

import android.content.Context;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.quduquxie.application.QuApplication;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.bean.Bookmark;
import com.quduquxie.base.config.BaseConfig;
import com.quduquxie.base.database.dao.BookDao;
import com.quduquxie.base.util.CommunalUtil;
import com.quduquxie.service.download.DownloadService;
import com.quduquxie.service.download.DownloadServiceUtil;
import com.quduquxie.util.BookHelper;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created on 17/7/17.
 * Created by crazylei.
 */

public class BookDaoHelper {

    private static BookDaoHelper bookDaoHelper;

    private ArrayList<Book> localBooks;
    private ArrayList<Book> onlineBooks;

    private BookDao bookDao;
    private Context context;

    private static final int SHELF_ONLINE_MAX_COUNT = 99;

    private BookDaoHelper(Context context) {

        this.context = context;
        bookDao = BookDao.getInstance(context);

        localBooks = bookDao.loadLocalBooks();
        onlineBooks = bookDao.loadOnlineBooks();
    }

    public synchronized static BookDaoHelper getInstance(Context context) {
        if (bookDaoHelper == null) {
            bookDaoHelper = new BookDaoHelper(context);
        }
        return bookDaoHelper;
    }

    public synchronized ArrayList<Bookmark> loadBookmarks(String book_id) {
        return bookDao.loadBookmarks(book_id);
    }

    public synchronized boolean checkBookmarkExist(String book_id, int sequence, int offset) {
        return bookDao.checkBookmarkExist(book_id, sequence, offset);
    }

    public synchronized void insertBookmark(Bookmark bookmark) {
        bookDao.insertBookmark(bookmark);
    }

    public synchronized void deleteBookmark(String book_id, int sequence, int offset) {
        bookDao.deleteBookmark(book_id, sequence, offset);
    }

    public synchronized void deleteBookmarks(String book_id) {
        bookDao.deleteBookmarks(book_id);
    }


    public synchronized Book loadBook(String id, int type) {
        switch (type) {
            case Book.TYPE_ONLINE:
                int size = onlineBooks.size();
                for (int i = 0; i < size; i++) {
                    if (onlineBooks.get(i).id.equals(id)) {
                        return onlineBooks.get(i);
                    }
                }
                return bookDao.loadBook(id);
            case Book.TYPE_LOCAL_TXT:
                size = localBooks.size();
                for (int i = 0; i < size; i++) {
                    if (localBooks.get(i).id.equals(id)) {
                        return localBooks.get(i);
                    }
                }
                return bookDao.loadBook(id);
            default:
                return null;
        }
    }

    public synchronized Book loadBook(String id) {
        return bookDao.loadBook(id);
    }

    public synchronized ArrayList<Book> loadAllBooks() {
        ArrayList<Book> books = new ArrayList<>();
        int size = onlineBooks.size();
        for (int i = 0; i < size; i++) {
            if (onlineBooks.get(i) != null) {
                books.add(onlineBooks.get(i));
            }
        }

        size = localBooks.size();
        for (int i = 0; i < size; i++) {
            if (localBooks.get(i) != null) {
                books.add(localBooks.get(i));
            }
        }

        Collections.sort(books);
        return books;
    }

    public synchronized ArrayList<Book> loadOnlineBooks() {
        ArrayList<Book> books = new ArrayList<>();
        final int size = onlineBooks.size();
        for (int i = 0; i < size; i++) {
            if (onlineBooks.get(i) != null) {
                books.add(onlineBooks.get(i));
            }
        }
        Collections.sort(books);
        return books;
    }

    public synchronized ArrayList<Book> loadOnlineReadBooks() {
        ArrayList<Book> books = new ArrayList<>();
        final int size = onlineBooks.size();
        for (int i = 0; i < size; i++) {
            Book book = onlineBooks.get(i);
            if (book != null && book.read == 1) {
                books.add(book);
            }
        }
        Collections.sort(books);
        return books;
    }

    public synchronized ArrayList<Book> loadLocalBooks() {
        ArrayList<Book> books = new ArrayList<>();
        final int size = localBooks.size();
        for (int i = 0; i < size; i++) {
            if (localBooks.get(i) != null) {
                books.add(localBooks.get(i));
            }
        }
        Collections.sort(books);
        return books;
    }

    public synchronized int insertBook(Book book) {
        if (book == null || TextUtils.isEmpty(book.id) || TextUtils.isEmpty(book.name)) {
            return 4;
        }

        if (book.book_type == Book.TYPE_LOCAL_TXT) {
            if (localBooks.contains(book)) {
                return 2;
            } else {
                book.sequence_time = System.currentTimeMillis();
                if (CommunalUtil.checkChapterDBExist(context, book.id)) {
                    context.deleteDatabase("book_chapter_" + book.id);
                }

                if (bookDao.insertBook(book)) {
                    if (book.sequence < -1) {
                        book.sequence = -1;
                    }
                    localBooks.add(book);
                    return 1;
                } else {
                    return 0;
                }
            }
        } else if (book.book_type == Book.TYPE_ONLINE) {
            if (onlineBooks.size() > SHELF_ONLINE_MAX_COUNT) {
                return 3;
            } else if (onlineBooks.contains(book)) {
                return 2;
            } else {
                book.sequence_time = System.currentTimeMillis();
                if (CommunalUtil.checkChapterDBExist(context, book.id)) {
                    context.deleteDatabase("book_chapter_" + book.id);
                }
                if (bookDao.insertBook(book)) {
                    if (book.sequence < -1) {
                        book.sequence = -1;
                    }
                    onlineBooks.add(book);
                    return 1;
                } else {
                    return 0;
                }
            }
        }
        return 0;
    }

    public synchronized boolean updateBook(Book book) {
        if (book == null || TextUtils.isEmpty(book.id)) {
            return false;
        }

        int index = onlineBooks.indexOf(book);
        if (index != -1) {
            if (bookDao.updateBook(book)) {
                onlineBooks.remove(book);
                Book onlineBook = bookDao.loadBook(book.id);
                onlineBooks.add(index, onlineBook);
                return true;
            } else {
                return false;
            }
        } else {
            index = localBooks.indexOf(book);
            if (bookDao.updateBook(book)) {
                localBooks.remove(book);
                Book localBook = bookDao.loadBook(book.id);
                localBooks.add(index, localBook);
                return true;
            } else {
                return false;
            }
        }
    }

    public synchronized boolean deleteBook(final String... ids) {
        QuApplication application = QuApplication.getInstance();
        final DownloadService downloadService = application.getDownloadService();
        final String[] deletedBooks = bookDao.deleteSubBook(ids);

        localBooks.clear();
        localBooks = bookDao.loadLocalBooks();

        onlineBooks.clear();
        onlineBooks = bookDao.loadOnlineBooks();

        BookHelper.removeMiPushNotification();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < deletedBooks.length; i++) {
                    if (downloadService != null) {
                        downloadService.deleteDownloadTask(deletedBooks[i]);
                    }

                    Logger.e("Delete: " + deletedBooks[i]);

                    context.deleteDatabase("book_chapter_" + deletedBooks[i]);
                    DownloadServiceUtil.dealDownloadIndex(context, deletedBooks[i]);
                    BookHelper.removeChapterCacheFile(deletedBooks[i]);
                }
            }
        }).start();

        return deletedBooks.length > 0;
    }

    public boolean subscribeBook(String id) {
        int size = onlineBooks.size();
        for (int i = 0; i < size; i++) {
            if (onlineBooks.get(i).id.equals(id)) {
                return true;
            }
        }

        size = localBooks.size();
        for (int i = 0; i < size; i++) {
            if (localBooks.get(i).id.equals(id)) {
                return true;
            }
        }

        return false;
    }

    public int loadOnlineBookSize() {
        int count = 0;
        if (onlineBooks != null) {
            count += onlineBooks.size();
        }
        return count;
    }

    public int loadBookSize() {
        int count = 0;

        if (onlineBooks != null) {
            count += onlineBooks.size();
        }

        if (localBooks != null) {
            count += localBooks.size();
        }

        return count;
    }
}