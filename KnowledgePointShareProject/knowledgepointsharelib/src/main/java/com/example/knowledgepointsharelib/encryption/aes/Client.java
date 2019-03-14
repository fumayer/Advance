package com.example.knowledgepointsharelib.encryption.aes;

/**
 * Created by sunjie on 2019/3/14.
 */

public class Client {
    private static String KEY="abcdefghijklmnop";
    private static String str = "task_id=TSK_000000006870&ledger_id=0715-5572";

    public static void main(String[] args) {

        System.out.println("加密前："+str);
        try {
            String encrypt = AESUtil.Encrypt(str, KEY);
            System.out.println("加密后："+encrypt);
            String decrypt = AESUtil.Decrypt(encrypt, KEY);
            System.out.println("解密后"+decrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
