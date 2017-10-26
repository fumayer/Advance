package com.quduquxie.local.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.quduquxie.base.bean.Book;
import com.quduquxie.base.bean.Chapter;
import com.quduquxie.base.bean.User;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.bean.ScanFile;
import com.quduquxie.util.CustomUtils;
import com.quduquxie.util.MD5;
import com.quduquxie.util.StatServiceUtils;

import java.util.ArrayList;
import java.util.Random;

public class ImportLocalBookTask extends AsyncTask<Object, Object, Object> {

    private ArrayList<ScanFile> selectedFileList;
    private BookDaoHelper bookDaoHelper;
    private Handler handler;
    private Context context;

    private String information;
//    private int insertState = 0;

    public static final int START_INSERT_BOOK = 0x51;
    public static final int REFRESH_IMPORT_VIEW = 0x52;
    public static final int COMPLETE_INSERT_BOOK = 0x53;

    private ArrayList<String> success;
    private ArrayList<String> error;

    private Random random;

    public ImportLocalBookTask(ArrayList<ScanFile> selectedFileList, Context context, Handler handler, BookDaoHelper bookDaoHelper) {
        this.selectedFileList = selectedFileList;
        this.bookDaoHelper = bookDaoHelper;
        this.handler = handler;
        this.random = new Random();
        this.context = context;
        this.success = new ArrayList<>();
        this.error = new ArrayList<>();
    }

    @Override
    protected Object doInBackground(Object... params) {
        for (int i = 0; i < selectedFileList.size(); i++) {
            if (isCancelled()) {
                break;
            }
            addBook(selectedFileList.get(i));
            information = "正在导入 " + (i + 1) + "/" + selectedFileList.size();
            publishProgress(i);
        }
        return null;
    }

    private void addBook(ScanFile scanFile) {
        Book book = new Book();
        book.name = CustomUtils.getTXTBookName(scanFile.getFile().getName());
        book.book_type = Book.TYPE_LOCAL_TXT;
        book.description = "未知";
        book.insert_time = System.currentTimeMillis();
        book.sequence_time = System.currentTimeMillis();
        book.file_path = scanFile.getFile().getAbsolutePath();
        book.book_size = scanFile.getFile().length();

        Chapter chapter = new Chapter();
        chapter.create_time = System.currentTimeMillis();
        chapter.id = MD5.encodeMD5String(book.name);
        book.chapter = chapter;

        User author = new User();
        author.name = "未知";
        author.id = MD5.encodeMD5String(book.name);
        book.author = author;

        book.attribute = "finish";
        int index = random.nextInt(6);
        book.image = String.valueOf(index);
        book.id = scanFile.getId();

        StatServiceUtils.statLocalFileImport(context, book.name);

        int result = bookDaoHelper.insertBook(book);
        if (result == 1) {
            success.add(book.name);
        } else {
            error.add(book.name);
        }
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        Message message = new Message();
        message.what = COMPLETE_INSERT_BOOK;

        StringBuilder stringBuilder = new StringBuilder();
        if (!success.isEmpty()) {
            if (success.size() > 1) {
                stringBuilder.append("《").append(success.get(0)).append("》等").append(success.size()).append("本书导入成功。");
            } else if (success.size() == 1) {
                stringBuilder.append("《").append(success.get(0)).append("》").append("导入成功。");
            }
        }

        if (!error.isEmpty()) {
            if (error.size() > 1) {
                stringBuilder.append("《").append(error.get(0)).append("》等").append(error.size()).append("本书导入失败。");
            } else if (error.size() == 1) {
                stringBuilder.append("《").append(error.get(0)).append("》").append("导入失败。");
            }
        }

        message.obj = "导入完成: " + stringBuilder.toString() ;
        handler.sendMessage(message);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        information = "正在导入  " + 0 + "/" + selectedFileList.size();
        handler.sendEmptyMessage(START_INSERT_BOOK);
        Message message = new Message();
        message.obj = information;
        message.what = REFRESH_IMPORT_VIEW;
        handler.sendMessage(message);
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        super.onProgressUpdate(values);
        information = "正在导入  " + values[0] + "/" + selectedFileList.size();
        Message message = new Message();
        message.obj = information;
        message.what = REFRESH_IMPORT_VIEW;
        handler.sendMessage(message);
    }
}
