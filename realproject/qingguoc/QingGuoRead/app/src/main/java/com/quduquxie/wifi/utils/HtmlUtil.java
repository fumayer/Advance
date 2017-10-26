package com.quduquxie.wifi.utils;

import com.quduquxie.Constants;
import com.quduquxie.util.CustomUtils;

import java.io.File;
import java.util.ArrayList;

public class HtmlUtil {

    public static String getIndexResult() {
        ArrayList<File> files = readDir(Constants.APP_PATH_LOCAL);
        StringBuilder transportResult = new StringBuilder();
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            transportResult.append("<tr class=\"c\">")
                    .append("<td class=\"i\">")
                    .append("<a href=\"")
                    .append(file.getName())
                    .append("\"></a>")
                    .append("</td>")
                    .append("<td class=\"n\">")
                    .append("<a href=\"")
                    .append(file.getName())
                    .append("\">")
                    .append(file.getName())
                    .append("</a>")
                    .append("</td>")
                    .append("<td class=\"m\">")
                    .append(CustomUtils.transformationTime(file.lastModified()))
                    .append("</td>")
                    .append("<td class=\"s\">")
                    .append(CustomUtils.convertStorage(file.length()))
                    .append("</td>")
                    .append("<td class=\"e\">")
                    .append("<a href=\"")
                    .append(file.getName())
                    .append("\"><img src=\"/download.png\" width=\"20\" height=\"20\"></a>")
                    .append("</td>")
                    .append("<td class=\"e\"> </td>")
                    .append("</tr>");
        }
        return transportResult.toString();
    }

    public static String getFileName(String fileName) {
        String name = fileName.substring(0, fileName.length() - 4);
        ArrayList<File> files = readDir(Constants.APP_PATH_LOCAL);
        int count = 0;
        for (int i = 0; i < files.size(); i++) {
            if (files.get(i).getName().startsWith(name)) {
                count++;
            }
        }
        return name + count + ".txt";
    }

    public static ArrayList<File> readDir(String filepath) {
        ArrayList<File> result = new ArrayList<>();

        File file = new File(filepath);
        if (!file.isDirectory()) {
            result.add(file);
        } else if (file.isDirectory()) {
            File[] fileList = file.listFiles();
            for (File fileObject : fileList) {
                if (!fileObject.isDirectory()) {
                    result.add(fileObject);
                }
            }
        }
        return result;
    }
}
