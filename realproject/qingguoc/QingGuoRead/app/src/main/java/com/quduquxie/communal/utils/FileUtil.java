package com.quduquxie.communal.utils;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.quduquxie.Constants;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created on 17/4/20.
 * Created by crazylei.
 */

public class FileUtil {

    private static final int BUFFER_SIZE = 8192;

    public static boolean checkFileExist(String id, String book_id) {
        String filePath = Constants.APP_PATH_BOOK + book_id + "/" + id + ".text";
        File file = new File(filePath);
        return file.exists();
    }

    public static String loadChapterFromCache(String id, String book_id) {
        String filePath = Constants.APP_PATH_BOOK + book_id + "/" + id + ".text";
        File file = new File(filePath);
        String content = null;
        if (file.exists()) {
            byte[] bytes = readFileBytes(filePath);
            try {
                content = new String(copyBytes(bytes));

            } catch (Throwable throwable) {
                throwable.printStackTrace();
                System.gc();
                System.gc();
                content = new String(copyBytes(bytes));
            }
        }
        return content;
    }

    public static boolean saveChapterToCache(String content, String id, String book_id) {
        String filePath = Constants.APP_PATH_BOOK + book_id + "/" + id + ".text";
        File file = new File(filePath);
        if (file.exists()) {
            return true;
        } else {
            return !TextUtils.isEmpty(content) && writeFileBytes(filePath, copyBytes(content.getBytes()));
        }
    }

    public static byte[] readFileBytes(String filePath) {
        InputStream inputstream = readFileInputStream(filePath);
        if (inputstream == null) {
            return null;
        }
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputstream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        int length = 0;
        byte[] data = null;
        try {
            while ((length = bufferedInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }
            data = byteArrayOutputStream.toByteArray();
        } catch (IOException exception) {
            exception.printStackTrace();
            return null;
        } finally {
            try {
                bufferedInputStream.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            try {
                byteArrayOutputStream.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return data;
    }

    public static InputStream readFileInputStream(String filePath) {
        InputStream inputStream = null;
        if (fileIsExist(filePath)) {
            File file = new File(filePath);
            try {
                inputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            return null;
        }
        return inputStream;
    }

    public static boolean fileIsExist(String filePath) {
        if (filePath == null || filePath.length() < 1) {
            return false;
        }

        File file = new File(filePath);
        return file.exists();
    }

    public static byte[] copyBytes(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) ~bytes[i];
        }
        return bytes;
    }

    public static boolean writeFileBytes(String filePath, byte[] bytes) {
        boolean success = true;
        File distFile = new File(filePath);
        if (!distFile.getParentFile().exists()) {
            boolean result = distFile.getParentFile().mkdirs();
            Logger.d("WriteFileBytes: " + result);
        }
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(filePath), BUFFER_SIZE);
            bufferedOutputStream.write(bytes);
        } catch (Exception exception) {
            success = false;
            exception.printStackTrace();
        } finally {
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
        return success;
    }
}
