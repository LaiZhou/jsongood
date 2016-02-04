/**
 * 
 */
package com.github.jessyZu.jsongood.util;

import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 */
public class AESUtil {

    private static final String AES                 = "AES";
    private static final String PROVIDER            = "BC";
    private static final int    DEFAULT_AES_KEYSIZE = 128;
    private static final int    DEFAULT_IVSIZE      = 16;

    static {
        // use BouncyCastleProvider 
        Security.addProvider(new BouncyCastleProvider());
    }

    private static byte[] aes(byte[] input, byte[] key, int mode) {
        try {
            SecretKey secretKey = new SecretKeySpec(key, AES);
            Cipher cipher = Cipher.getInstance(AES, PROVIDER);
            cipher.init(mode, secretKey);
            return cipher.doFinal(input);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] aesEncrypt(byte[] input, byte[] key) {
        return aes(input, key, Cipher.ENCRYPT_MODE);
    }

    public static byte[] aesDecrypt(byte[] input, byte[] key) {
        return aes(input, key, Cipher.DECRYPT_MODE);

    }

    /**
     * 生成AES密钥,返回字节数组, 默认长度为128位(16字节).
     */
    public static byte[] generate128AesKey() {
        return generateAesKey(DEFAULT_AES_KEYSIZE);
    }


    /**
     * 生成AES密钥,可选长度为128,192,256位.
     */
    public static byte[] generateAesKey(int keysize) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
            keyGenerator.init(keysize);
            SecretKey secretKey = keyGenerator.generateKey();
            return secretKey.getEncoded();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
