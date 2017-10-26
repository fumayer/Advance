package com.quduquxie.wifi.service;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.quduquxie.Constants;
import com.quduquxie.R;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.bean.Chapter;
import com.quduquxie.base.bean.User;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.util.CustomUtils;
import com.quduquxie.util.MD5;
import com.quduquxie.util.QGLog;
import com.quduquxie.wifi.utils.HtmlUtil;
import com.quduquxie.wifi.utils.NanoHTTPD;
import com.quduquxie.wifi.view.WiFiTransportActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WiFiTransportWebService extends NanoHTTPD {

    private static final String TAG = WiFiTransportWebService.class.getSimpleName();
    public static AssetManager assetManager;

    public Context context;
    public BookDaoHelper bookDaoHelper;

    public Handler handler;

    private static final Integer[] ids = new Integer[]{R.drawable.icon_local_cover_1, R.drawable.icon_local_cover_2, R.drawable.icon_local_cover_3, R.drawable.icon_local_cover_4, R.drawable.icon_local_cover_5, R.drawable.icon_local_cover_6};
    private Random random;

    public WiFiTransportWebService(String hostname, int port, Context context, Handler handler) {
        super(hostname, port);
        this.bookDaoHelper = BookDaoHelper.getInstance(context);
        this.handler = handler;
        this.random = new Random();
    }

    public Response serve(IHTTPSession session) {
        String uri = session.getUri();

        QGLog.e(TAG, "URI: " + uri + " : " + session.getMethod());
        if (session.getMethod() == Method.GET) {

            String file_name = uri.substring(1);

            if (file_name.endsWith(".txt")) {
                QGLog.e(TAG, "FileName: " + file_name);
                String filePath = Constants.APP_PATH_LOCAL + file_name;

                File file = new File(filePath);
                try {
                    return newFixedLengthResponse(Response.Status.OK, "multipart/form-data", new FileInputStream(file), -1);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return newFixedLengthResponse(Response.Status.NOT_FOUND, "", "Not Found");
                }
            } else {
                // 默认的页面名称设定为index.html
                if (TextUtils.isEmpty(file_name)) {
                    file_name = "index.html";
                }
                if (file_name.equals("index.html")) {

                    String transportResult = HtmlUtil.getIndexResult();

                    try {
                        //通过AssetManager直接打开文件进行读取操作
                        InputStream inputStream = assetManager.open("wifi/" + file_name, AssetManager.ACCESS_BUFFER);
                        String resultString = inputStreamToString(inputStream);
                        resultString = resultString.replace("%%FileItemLoop%%", transportResult);
                        return newFixedLengthResponse(Response.Status.OK, "", resultString);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        //通过AssetManager直接打开文件进行读取操作
                        InputStream inputStream = assetManager.open("wifi/" + file_name, AssetManager.ACCESS_BUFFER);
                        return newFixedLengthResponse(Response.Status.OK, "", inputStream, -1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (session.getMethod() == Method.POST) {
            if (uri.startsWith("/qqfile=") && uri.endsWith(".txt")) {
                String fileName = uri.substring(8);
                String filePath = Constants.APP_PATH_LOCAL + fileName;
                if (bookDaoHelper.subscribeBook(MD5.encodeMD5String(filePath))) {
                    fileName = HtmlUtil.getFileName(fileName);
                    QGLog.e(TAG, "FileName: " + fileName);
                }
                Map<String, String> files = new HashMap<>();
                files.put(Constants.APP_PATH_LOCAL, fileName);
                Method method = session.getMethod();
                if (Method.PUT.equals(method) || Method.POST.equals(method)) {
                    try {
                        session.parseBody(files);
                    } catch (IOException ioe) {
                        return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, NanoHTTPD.MIME_PLAINTEXT, "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage());
                    } catch (ResponseException re) {
                        return newFixedLengthResponse(re.getStatus(), NanoHTTPD.MIME_PLAINTEXT, re.getMessage());
                    }
                }
                if (files.containsKey("content")) {
                    String tempFileName = files.get("content");
                    File tempFile = new File(tempFileName);
                    File file = new File(Constants.APP_PATH_LOCAL + fileName);

                    if (tempFile.canWrite()) {
                        if (tempFile.renameTo(file)) {

                            Book book = new Book();
                            book.name = CustomUtils.getTXTBookName(file.getName());
                            book.book_type = Book.TYPE_LOCAL_TXT;
                            book.description = "未知";
                            book.insert_time = System.currentTimeMillis();
                            book.sequence_time = System.currentTimeMillis();

                            book.file_path = file.getAbsolutePath();
                            book.book_size = file.length();

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
                            book.image = String.valueOf(ids[index]);
                            book.id = MD5.encodeMD5String(file.getAbsolutePath());

                            if (bookDaoHelper.insertBook(book) == 1) {
                                Message message = new Message();
                                message.obj = book.name;
                                message.what = WiFiTransportActivity.TRANSPORT_SUCCESS;
                                handler.sendMessage(message);
                                return newFixedLengthResponse(Response.Status.OK, "application/json", "{\"success\": true}");
                            } else {
                                Message message = new Message();
                                message.obj = book.name;
                                message.what = WiFiTransportActivity.TRANSPORT_ERROR;
                                handler.sendMessage(message);
                                return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "application/json", "{\"success\": false}");
                            }
                        } else {
                            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "application/json", "{\"success\": false}");
                        }
                    } else {
                        return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "application/json", "{\"success\": false}");
                    }
                }
            }
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "application/json", "{\"success\": false}");
        }
        return super.serve(session);
    }



    public String inputStreamToString(InputStream inputStream) throws IOException {
        StringBuilder stringBuffer = new StringBuilder();
        byte[] bytes = new byte[4096];
        for (int n; (n = inputStream.read(bytes)) != -1; ) {
            stringBuffer.append(new String(bytes, 0, n));
        }
        return stringBuffer.toString();
    }
}