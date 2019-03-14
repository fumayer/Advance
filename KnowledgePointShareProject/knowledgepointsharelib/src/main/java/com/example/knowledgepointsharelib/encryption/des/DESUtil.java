package com.example.knowledgepointsharelib.encryption.des;


import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * Created by sunjie on 2019/3/14.
 */

public class DESUtil {
    private final static String DES = "DES";
    private final static String ENCODE = "GBK";

    /**
     * 加密
     *
     * @param data 原始数据
     * @param key  key，通过key获取私钥
     * @return 返回密文
     */
    public static String encrypt(String data, String key) throws Exception {
//        获取cipher加密数据
        Cipher cipher = getCipher(key, DES, Cipher.ENCRYPT_MODE);
        byte[] bt = cipher.doFinal(data.getBytes(ENCODE));
//        此处使用BASE64做转码功能，同时能起到2次加密的作用
        String miwen = Base64.getEncoder().encodeToString(bt);
        return miwen;
    }

    /**
     * 解密
     *
     * @param data 密文
     * @param key  key，通过key获取私钥
     * @return 解密后的数据
     */
    public static String decrypt(String data, String key) throws Exception {
//        Base64先转码
        byte[] mi = Base64.getDecoder().decode(data.getBytes());
//        获取cipher解密数据
        Cipher cipher = getCipher(key, DES, Cipher.DECRYPT_MODE);
        byte[] ming = cipher.doFinal(mi);
        return new String(ming, ENCODE);
    }


    /**
     * 获取cipher对象
     *
     * @param key                 通过key获取私钥
     * @param encryptionAlgorithm 获取哪种加密方式 AES DES RSA...
     * @param opmode              操作模式：加密、解密。
     * @return cipher
     */
    private static Cipher getCipher(String key, String encryptionAlgorithm, int opmode) throws Exception {

        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key.getBytes(ENCODE));
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(encryptionAlgorithm);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(encryptionAlgorithm);
        // 用密钥初始化Cipher对象
        cipher.init(opmode, securekey, sr);
        return cipher;
    }

}
