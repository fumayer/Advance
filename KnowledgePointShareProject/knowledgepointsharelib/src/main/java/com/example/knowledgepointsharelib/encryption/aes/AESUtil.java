package com.example.knowledgepointsharelib.encryption.aes;


import java.io.UnsupportedEncodingException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by sunjie on 2019/3/14.
 */


// 使用AES-128-ECB加密模式，秘钥必须为16位字符串（128bit = 16 * 8bit）；这种方式与上面JS的AES可以前后端配合一起使用
public class AESUtil {
    // 加密
    public static String Encrypt(String sSrc, String sKey) throws Exception {

        Cipher cipher = getCipher(sKey,Cipher.ENCRYPT_MODE);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
        //此处使用BASE64做转码功能，同时能起到2次加密的作用。
        return Base64.getEncoder().encodeToString(encrypted);
    }

    // 解密
    public static String Decrypt(String sSrc, String sKey) throws Exception {
        //先用base64转码
        byte[] encrypted1 = Base64.getDecoder().decode(sSrc);
//        获取cipher对象，进行解密
        Cipher cipher = getCipher(sKey, Cipher.DECRYPT_MODE);
        byte[] original = cipher.doFinal(encrypted1);
        String originalString = new String(original, "utf-8");
        return originalString;
    }

    private static Cipher getCipher(String key, int opmode) throws Exception {
        // 判断Key是否正确
        if (key == null) {
            System.out.print("Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (key.length() != 16) {
            System.out.print("Key长度不是16位");
            return null;
        }
        byte[] raw = key.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
        cipher.init(opmode, skeySpec);

        return cipher;
    }
}






