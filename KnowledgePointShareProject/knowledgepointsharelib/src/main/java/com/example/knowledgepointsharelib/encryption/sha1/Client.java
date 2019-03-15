package com.example.knowledgepointsharelib.encryption.sha1;

/**
 * Created by sunjie on 2019/3/15.
 */

public class Client {
    public static void main(String[] args) throws Exception {
        String str = new String("amigoxiexiexingxing");
        System.out.println("原始：" + str);
        System.out.println("SHA1后：" + SHA1Util.sha1(str));
    }
}
