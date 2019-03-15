package com.example.knowledgepointsharelib.encryption.md5;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by sunjie on 2019/3/15.
 */

public class MD5Util {

    /**
     * 获取字符串的MD5值（消息摘要）
     * 加密字符串
     */
    public static String md5(String content) throws NoSuchAlgorithmException, UnsupportedEncodingException {
//        得到一个MessageDigest对象，这个类是Java自带的一个加密类
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
//        得到了加密后的字节数组，
//        MD5的转换结果在计算机中是128位的字节，加密串太长的话，传输，使用都不方便，十六进制是比较通用的加密表示方法
        byte[] digest = messageDigest.digest(content.getBytes("UTF-8"));

//        将得到的字节数组转换为十六进制的字符串，便于传输
        StringBuilder hex = new StringBuilder(digest.length * 2);
        for (byte b : digest) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }


    /**
     * 对字符串多次MD5加密
     */
    public static String md5More(String content) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        for (int i = 0; i < 3; i++) {
            content = md5(content);
        }
        return content;
    }


    /**
     * 获取加盐字符串的MD5值（消息摘要）
     * 加盐
     */
    public static String md5AndSalt(String content, String salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] digest = messageDigest.digest((content + salt).getBytes("UTF-8"));

        StringBuilder hex = new StringBuilder(digest.length * 2);
        for (byte b : digest) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }


    /**
     * 使用字符串的hash地址作为盐，进行加密
     */
    public static String md5AndSalt(String content) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return md5AndSalt(content, content.toString());
    }


    /**
     * 计算文件的 MD5 值
     * 通过messageDigest.update方法读取文件流，获取文件的消息摘要
     */
    public static String md5ForFile1(File file) {
        if (file == null || !file.isFile() || !file.exists()) {
            return "";
        }
        FileInputStream fis = null;
        String result = "";
        byte buffer[] = new byte[8192];
        int len;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file);
//            在读入文件信息上有点不同。这里是分多次将一个文件读入，对于大型文件而言，比较推荐这种方式，占用内存比较少
            while ((len = fis.read(buffer)) != -1) {
//               update方法：使用指定的byte数组更新摘要
                messageDigest.update(buffer, 0, len);
            }
            byte[] bytes = messageDigest.digest();

            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != fis) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 计算文件的 MD5 值
     * 使用DigestInputStream获取文件摘要
     * DigestInputStream类继承了FilterInputStream类，可以通过读取输入流的方式完成摘要更新，
     * 因此我们称之为消息摘要输入流，在指定的读操作方法内部完成MessageDigest类的update()方法。
     */
    public static String md5ForFile2(File file) throws Exception {
        //创建MD5转换器和文件流
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        FileInputStream fis = new FileInputStream(file);
        DigestInputStream dis = new DigestInputStream(fis, messageDigest);
        byte[] buffer = new byte[1024];
        //DigestInputStream在读取的时候，内部完成了MessageDigest类的update()方法
        while (dis.read(buffer) > 0) {
        }
        ;

        //通过DigestInputStream对象得到一个最终的MessageDigest对象。
        messageDigest = dis.getMessageDigest();
        // 通过messageDigest拿到结果，也是字节数组
        byte[] array = messageDigest.digest();
        // 同样，把字节数组转换成十六进制的字符串
        StringBuilder hex = new StringBuilder(array.length * 2);
        for (byte b : array) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        fis.close();
        dis.close();
        return hex.toString();
    }


}
