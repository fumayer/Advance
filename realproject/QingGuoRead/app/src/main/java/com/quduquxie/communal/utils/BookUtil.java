package com.quduquxie.communal.utils;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.database.dao.BookDao;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.model.BookShelf;
import com.quduquxie.model.DownloadShelfResult;
import com.quduquxie.model.UploadShelf;
import com.quduquxie.model.UploadShelfResult;
import com.quduquxie.retrofit.DataRequestService;
import com.quduquxie.retrofit.ServiceGenerator;
import com.quduquxie.retrofit.model.BlanketResult;
import com.quduquxie.retrofit.model.CommunalResult;

import org.json.JSONException;

import java.util.ArrayList;

import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created on 16/12/26.
 * Created by crazylei.
 */

public class BookUtil {

    public static void uploadUserBookShelf(Context context) throws JSONException {
        BookDaoHelper bookDaoHelper = BookDaoHelper.getInstance(context);
        ArrayList<Book> localBooks = bookDaoHelper.loadLocalBooks();
        ArrayList<Book> onlineBooks = bookDaoHelper.loadOnlineBooks();

        ArrayList<UploadShelf> uploadShelfList = new ArrayList<>();
        UploadShelf uploadShelf;
        for (Book book : localBooks) {
            if (book != null) {
                uploadShelf = new UploadShelf();
                uploadShelf.is_self = 1;
                uploadShelf.book_name = book.name;
                uploadShelf.word_offset = book.offset;
                uploadShelf.serial_number = book.sequence;
                uploadShelfList.add(uploadShelf);
            }
        }

        for (Book book : onlineBooks) {
            if (book != null) {
                uploadShelf = new UploadShelf();
                uploadShelf.is_self = 0;
                uploadShelf.book_id = book.id;
                uploadShelf.book_name = book.name;
                uploadShelf.word_offset = book.offset;
                if (book.read == 1) {
                    uploadShelf.serial_number = book.sequence + 1;
                } else {
                    uploadShelf.serial_number = 0;
                }
                uploadShelfList.add(uploadShelf);
            }
        }

        if (uploadShelfList.size() == 0) {
            return;
        }

        Gson gson = new Gson();
        String json = gson.toJson(uploadShelfList);

        Logger.json(json);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), json);

        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
        dataRequestService.uploadUserBookShelf(requestBody)
                .subscribeOn(Schedulers.trampoline())
                .observeOn(Schedulers.trampoline())
                .subscribe(new ResourceSubscriber<CommunalResult<UploadShelfResult>>() {

                    @Override
                    public void onNext(CommunalResult<UploadShelfResult> uploadShelfResult) {
                        Logger.d("UploadUserBookShelf onNext: " + uploadShelfResult.getModel().toString());
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("UploadUserBookShelf onError: " + throwable.toString());
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("UploadUserBookShelf onComplete");
                    }
                });
    }

    public static RequestBody getUpdateUserBookShelf(Context context) throws JSONException {
        BookDaoHelper bookDaoHelper = BookDaoHelper.getInstance(context);
        ArrayList<Book> localBooks = bookDaoHelper.loadLocalBooks();
        ArrayList<Book> onlineBooks = bookDaoHelper.loadOnlineBooks();

        ArrayList<UploadShelf> uploadShelfList = new ArrayList<>();
        UploadShelf uploadShelf;
        for (Book book : localBooks) {
            if (book != null) {
                uploadShelf = new UploadShelf();
                uploadShelf.is_self = 1;
                uploadShelf.book_name = book.name;
                uploadShelf.word_offset = book.offset;
                uploadShelf.serial_number = book.sequence;
                uploadShelfList.add(uploadShelf);
            }
        }

        for (Book book : onlineBooks) {
            if (book != null) {
                uploadShelf = new UploadShelf();
                uploadShelf.is_self = 0;
                uploadShelf.book_id = book.id;
                uploadShelf.book_name = book.name;
                uploadShelf.word_offset = book.offset;
                if (book.read == 1) {
                    uploadShelf.serial_number = book.sequence + 1;
                } else {
                    uploadShelf.serial_number = 0;
                }
                uploadShelfList.add(uploadShelf);
            }
        }

        Gson gson = new Gson();
        String json;
        if (uploadShelfList.size() == 0) {
            json = "[]";
        } else {
            json = gson.toJson(uploadShelfList);
        }

        Logger.json(json);

        return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), json);
    }

    public static void downloadUserBookShelf(final Context context) {
        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
        dataRequestService.downloadUserBookShelf()
                .subscribeOn(Schedulers.trampoline())
                .observeOn(Schedulers.trampoline())
                .subscribe(new ResourceSubscriber<CommunalResult<DownloadShelfResult>>() {

                    @Override
                    public void onNext(CommunalResult<DownloadShelfResult> downloadShelfResult) {
                        Logger.d("DownloadUserBookShelf onNext: " + downloadShelfResult.getModel().toString());
                        if (downloadShelfResult.getCode() == 0 && downloadShelfResult.getModel().count != 0 && downloadShelfResult.getModel().shelfs != null) {
                            BookDaoHelper bookDaoHelper = BookDaoHelper.getInstance(context);
                            ArrayList<Book> onlineBooks = bookDaoHelper.loadOnlineBooks();
                            if (onlineBooks != null && onlineBooks.size() > 0) {
                                String[] book_ids = new String[onlineBooks.size()];
                                for (int i = 0; i < onlineBooks.size(); i++) {
                                    Book book = onlineBooks.get(i);
                                    if (book != null && !TextUtils.isEmpty(book.id)) {
                                        book_ids[i] = book.id;
                                    }
                                }
                                bookDaoHelper.deleteBook(book_ids);
                            }

                            //转换信息，添加书籍信息到本地
                            ArrayList<BookShelf> bookShelfList = downloadShelfResult.getModel().shelfs;
                            if (bookShelfList != null && bookShelfList.size() > 0) {
                                Book book;
                                for (int i = bookShelfList.size() - 1; i > -1; i--) {
                                    book = BeanUtil.bookTransformation(bookShelfList.get(i));
                                    if (book != null) {
                                        Logger.d("DownloadUserBookShelf Book: " + book.toString());
                                        bookDaoHelper.insertBook(book);
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("DownloadUserBookShelf onError: " + throwable.toString());
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("DownloadUserBookShelf onComplete");
                    }
                });

    }

    public static void deleteAllOnLineBook(Context context) {
        BookDaoHelper bookDaoHelper = BookDaoHelper.getInstance(context);
        ArrayList<Book> onlineBooks = bookDaoHelper.loadOnlineBooks();
        if (onlineBooks != null && onlineBooks.size() > 0) {
            String[] book_ids = new String[onlineBooks.size()];
            for (int i = 0; i < onlineBooks.size(); i++) {
                Book book = onlineBooks.get(i);
                if (book != null && !TextUtils.isEmpty(book.id)) {
                    book_ids[i] = book.id;
                }
            }
            bookDaoHelper.deleteBook(book_ids);
        }
    }
}
