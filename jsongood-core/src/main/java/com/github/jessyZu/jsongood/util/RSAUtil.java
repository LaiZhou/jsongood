/**
 * 
 */
package com.github.jessyZu.jsongood.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.Security;
import java.security.interfaces.RSAKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.provider.JCERSAPrivateKey;
import org.bouncycastle.jce.provider.JCERSAPublicKey;
import org.bouncycastle.openssl.PEMReader;

/**
 *
 */
public class RSAUtil {

    private static Map<String, Cipher> KEY_CIPHER_MAP = new ConcurrentHashMap<String, Cipher>();
    private static Map<String, RSAKey> KEY_MAP        = new ConcurrentHashMap<String, RSAKey>();

    private static final String        KEY_ALGORTHM   = "RSA";
    private static final String        PROVIDER       = "BC";

    static {
        // use BouncyCastleProvider 
        Security.addProvider(new BouncyCastleProvider());
    }

    public static byte[] encryptByPublicKey(byte[] data, String publicKey) {
        byte[] encryptedData = null;
        ByteArrayOutputStream out = null;
        try {
            Cipher cipher = KEY_CIPHER_MAP.get(publicKey);
            if (cipher == null) {
                byte[] publicKeyBytes = Bytes.base642bytes(publicKey);//publicKey string is base64 format,without "\n"
                X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM, PROVIDER);
                JCERSAPublicKey publicKey0 = (JCERSAPublicKey) keyFactory.generatePublic(x509EncodedKeySpec);//use bc api
                cipher = Cipher.getInstance(keyFactory.getAlgorithm());
                cipher.init(Cipher.ENCRYPT_MODE, publicKey0);
                KEY_CIPHER_MAP.put(publicKey, cipher);
                KEY_MAP.put(publicKey, publicKey0);
            }
            int max_encrypt_block_size = KEY_MAP.get(publicKey).getModulus().bitLength() / 8 - 11;
            int inputLen = data.length;
            int offSet = 0;
            byte[] cache;
            int i = 0;
            out = new ByteArrayOutputStream();
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > max_encrypt_block_size) {
                    cache = cipher.doFinal(data, offSet, max_encrypt_block_size);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * max_encrypt_block_size;
            }
            encryptedData = out.toByteArray();
            out.close();

        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {

                }
            }
        }
        return encryptedData;
    }

    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) {
        byte[] decryptedData = null;
        ByteArrayOutputStream out = null;
        try {
            Cipher cipher = KEY_CIPHER_MAP.get(privateKey);
            if (cipher == null) {
                byte[] privateKeyBytes = Bytes.base642bytes(privateKey);//privateKey string is base64 format,without "\n"
                PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM, PROVIDER);
                JCERSAPrivateKey privateKey0 = (JCERSAPrivateKey) keyFactory.generatePrivate(pkcs8EncodedKeySpec);
                cipher = Cipher.getInstance(keyFactory.getAlgorithm());
                cipher.init(Cipher.DECRYPT_MODE, privateKey0);
                KEY_CIPHER_MAP.put(privateKey, cipher);
                KEY_MAP.put(privateKey, privateKey0);
            }
            int max_decrypt_block_size = KEY_MAP.get(privateKey).getModulus().bitLength() / 8;

            int inputLen = encryptedData.length;
            out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > max_decrypt_block_size) {
                    cache = cipher.doFinal(encryptedData, offSet, max_decrypt_block_size);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * max_decrypt_block_size;
            }
            decryptedData = out.toByteArray();
            out.close();

        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {

                }
            }
        }
        return decryptedData;
    }

    public static String extractPublicKeyStringFromPrivateKey(String privateKeyFileString) {

        PEMReader pemReader = null;
        try {
            pemReader = new PEMReader(new StringReader(privateKeyFileString));//privateKeyFileString has -----BEGIN RSA PRIVATE KEY----- ,-----END RSA PRIVATE KEY----- and "\n"
            KeyPair keyPair = (KeyPair) pemReader.readObject();
            PublicKey publicKey = keyPair.getPublic();
            byte[] publicKeyBytes = publicKey.getEncoded();
            return Bytes.bytes2base64(publicKeyBytes);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            if (pemReader != null) {
                try {
                    pemReader.close();
                } catch (IOException e) {

                }
            }

        }
    }
}
