package com.quduquxie.util;


import org.mozilla.universalchardet.UniversalDetector;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;

public class CustomUtils {

    public static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public static String convertStorage(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.2f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.2f MB" : "%.2f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.2f KB" : "%.2f KB", f);
        } else
            return String.format("%d B", size);
    }

    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    public static String getTXTBookName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > 0) && (dot < (filename.length() - 1))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    public static String transformationTime(long time) {
        String date = "";
        long disparity = System.currentTimeMillis() - time;
        if (disparity < 0) {
            return "刚刚";
        }
        long day = disparity / (24 * 60 * 60 * 1000);
        long hour = (disparity / (60 * 60 * 1000) - day * 24);
        long minute = ((disparity / (60 * 1000)) - day * 24 * 60 - hour * 60);

        if (day > 0) {
            if (day >= 1 && day <= 7) {
                date = day + "天前";
            } else {
                date = formatter.format(time);
            }
        } else if (hour > 0) {
            date = hour + "小时前";
        } else if (minute > 0) {
            date = minute + "分钟前";
        } else {
            date = "刚刚";
        }
        return date;
    }

    public static String getEncode(String filePath) {
        String fileEncode;
        byte[] bytes = new byte[4096];
        FileInputStream fileInputStream = null;
        UniversalDetector universalDetector = null;
        try {
            fileInputStream = new FileInputStream(filePath);
            universalDetector = new UniversalDetector(null);
            int index;
            while ((index = fileInputStream.read(bytes)) > 0 && !universalDetector.isDone()) {
                universalDetector.handleData(bytes, 0, index);
            }
            universalDetector.dataEnd();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String encoding = (universalDetector == null) ? null : universalDetector.getDetectedCharset();
        if (encoding != null) {
            fileEncode = encoding.toUpperCase();
        } else {
            fileEncode = "GBK";
        }
        if (universalDetector != null) {
            universalDetector.reset();
        }

        QGLog.e("CustomUtils", "CustomUtils: getEncode: " + filePath + " : " + fileEncode);
        return fileEncode;
    }

    public static String getFileMode(String path, String fileEncode) {
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        FileInputStream fileInputStream;
        String fileMode = "\r\n";
        int key = 0;
        int preKey = 0;
        try {
            fileInputStream = new FileInputStream(path);
            inputStream = new BufferedInputStream(fileInputStream);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, fileEncode));
            while ((key = bufferedReader.read()) != -1) {
                if (key == 10) {
                    if (preKey == 13) {
                        fileMode = "\r\n";
                    } else {
                        fileMode = "\n";
                    }
                    break;
                }
                preKey = key;
            }
            bufferedReader.close();
        } catch (Exception e) {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        return fileMode;
    }
}
