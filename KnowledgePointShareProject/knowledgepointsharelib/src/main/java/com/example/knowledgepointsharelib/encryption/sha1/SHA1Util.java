package com.example.knowledgepointsharelib.encryption.sha1;

import java.security.MessageDigest;

/**
 * Created by sunjie on 2019/3/15.
 */

public class SHA1Util {
    public static String sha1(String content) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        byte[] contentBytes = content.getBytes("UTF-8");
        byte[] sha1Bytes = messageDigest.digest(contentBytes);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < sha1Bytes.length; i++) {
            int val = ((int) sha1Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
}
