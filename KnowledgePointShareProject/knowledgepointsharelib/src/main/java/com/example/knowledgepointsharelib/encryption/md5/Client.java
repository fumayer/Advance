package com.example.knowledgepointsharelib.encryption.md5;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

/**
 * Created by sunjie on 2019/3/15.
 */

public class Client {

    public static void main3(String[] args) throws Exception {
        String md51 = MD5Util.md5("123");
        String md52 = MD5Util.md5(md51);
        String md53 = MD5Util.md5(md52);
        System.out.println(md51);
        System.out.println(md52);
        System.out.println(md53);
        String md5More = MD5Util.md5More("123");
        System.out.println(md5More);
    }

    public static void main1(String[] args) throws Exception {
        String md5 = MD5Util.md5("123");
        System.out.println(md5);
    }

    public static void main(String[] args) throws Exception {
        File file = new File("/Users/sunjie/develop/android/as/work/own/myGit/" +
                "KnowledgePointShareProject/knowledgepointsharelib/src/main/java/com/example" +
                "/knowledgepointsharelib/encryption/md5/rgbvrShow2D-release.apk");
        System.out.println(file.getAbsoluteFile());
        System.out.println(file.exists());

        String md5ForFile = MD5Util.md5ForFile1(file);
        System.out.println("md5ForFile1： " + md5ForFile);
        String md5ForFile2 = MD5Util.md5ForFile2(file);
        System.out.println("md5ForFile2： " + md5ForFile2);

        main3(null);
    }


}
