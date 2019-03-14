package com.example.knowledgepointsharelib.encryption.des;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * Created by sunjie on 2019/3/14.
 */

public class Client {

    private final static String KEY = "12344321";
    private static String data = "task_id=TSK_000000006870&ledger_id=0715-"; //    待加密内容

    public static void main(String[] args) throws Exception {

        System.out.println("加密前：" + data);
        String enc = DESUtil.encrypt(data,KEY);
        System.out.println("加密后：" + enc);
        String dec = DESUtil.decrypt(enc,KEY);
        System.out.println("解密后：" + dec);

    }
}
