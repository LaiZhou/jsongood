/**
 * 
 */
package com.github.jessyZu.jsongood;

import junit.framework.Assert;

import org.junit.Test;

import com.github.jessyZu.jsongood.util.AESUtil;
import com.github.jessyZu.jsongood.util.Bytes;
import com.github.jessyZu.jsongood.util.RSAUtil;

/**
 *
 */
public class SecurityTest {

    @Test
    public void testRSA2048() {
        String publicKeyString = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDIBreJrXHpJZYsM1bkNOJDXTRD"
                + "epIG97Dsk8XygBYZhPH9C2QN+tyX6+TDKEBxLhEFTkMgniPzcVMhktMOQZyb38in"
                + "rZ+Ney91etFMP7dy/r+/G2+92wq7F/eIyna2gYFyD8Nl3FhbQLhNDI4bsBkwGRXb" + "K9AV05aWW62CLXgkVwIDAQAB";

        String privateKeyString = "MIICXAIBAAKBgQDIBreJrXHpJZYsM1bkNOJDXTRDepIG97Dsk8XygBYZhPH9C2QN"
                + "+tyX6+TDKEBxLhEFTkMgniPzcVMhktMOQZyb38inrZ+Ney91etFMP7dy/r+/G2+9"
                + "2wq7F/eIyna2gYFyD8Nl3FhbQLhNDI4bsBkwGRXbK9AV05aWW62CLXgkVwIDAQAB"
                + "AoGAVPPVW5nYeUbHYvpi+wUi261oFwiloGPuNmwEdBvGRwxZojkGQ5G5wqQjQmeE"
                + "Bw2jmPdGN0Iyzp/gyK+NB88B7clnefjiDN+LFxIoGHPRvFS73FxAk2Bf7N3HdmTK"
                + "tU4bbs0K8ZB4ZJPuSLR/zjc/mf3T0Ifv3v9IyHvsEiIVffECQQDqJ5w16WUQZtq6"
                + "DsKaznhG1qOWhAD3tB9qkX+GCirwmSnx6HdXeqxX/zJC5ULaN2HRjYBaYLQgGvj5"
                + "3nvvN2DzAkEA2rABZwJHFb6z1PvCGTftp1810pybYeaXSKEG0YUoD61kRk7CC7ye"
                + "QkLInHDF7zHnUei4vnwaK+8P/k5oXAToDQJBALOMDfc08V8DT2Lt4IEuIwJGoYfP"
                + "zqIhxxlDWiKg904oVAZ/t8sncLHFKHgtLfcAEozJ0Qr8LgKUTCflVukWcMsCQG/e"
                + "O0lrc9ueyzV3eAoYfxzwaoMLk7zYDamcnfVOLXkflL0WitMgMMDqkt7cHZTHrXJM"
                + "Hz1qFKVGzKKlj2MLK30CQD3/e1wO7np4FJSqO+tZzq+sWqYL2urGM73Ho+Gqxj3K"
                + "a98X6NJzQA7NWnydMK3onmLARDV17xUcnSrAzblebng=";
        String dataString = "123abc++__|";

        String privateKeyFileString = "-----BEGIN RSA PRIVATE KEY-----\n"
                + "MIICXAIBAAKBgQDIBreJrXHpJZYsM1bkNOJDXTRDepIG97Dsk8XygBYZhPH9C2QN\n"
                + "+tyX6+TDKEBxLhEFTkMgniPzcVMhktMOQZyb38inrZ+Ney91etFMP7dy/r+/G2+9\n"
                + "2wq7F/eIyna2gYFyD8Nl3FhbQLhNDI4bsBkwGRXbK9AV05aWW62CLXgkVwIDAQAB\n"
                + "AoGAVPPVW5nYeUbHYvpi+wUi261oFwiloGPuNmwEdBvGRwxZojkGQ5G5wqQjQmeE\n"
                + "Bw2jmPdGN0Iyzp/gyK+NB88B7clnefjiDN+LFxIoGHPRvFS73FxAk2Bf7N3HdmTK\n"
                + "tU4bbs0K8ZB4ZJPuSLR/zjc/mf3T0Ifv3v9IyHvsEiIVffECQQDqJ5w16WUQZtq6\n"
                + "DsKaznhG1qOWhAD3tB9qkX+GCirwmSnx6HdXeqxX/zJC5ULaN2HRjYBaYLQgGvj5\n"
                + "3nvvN2DzAkEA2rABZwJHFb6z1PvCGTftp1810pybYeaXSKEG0YUoD61kRk7CC7ye\n"
                + "QkLInHDF7zHnUei4vnwaK+8P/k5oXAToDQJBALOMDfc08V8DT2Lt4IEuIwJGoYfP\n"
                + "zqIhxxlDWiKg904oVAZ/t8sncLHFKHgtLfcAEozJ0Qr8LgKUTCflVukWcMsCQG/e\n"
                + "O0lrc9ueyzV3eAoYfxzwaoMLk7zYDamcnfVOLXkflL0WitMgMMDqkt7cHZTHrXJM\n"
                + "Hz1qFKVGzKKlj2MLK30CQD3/e1wO7np4FJSqO+tZzq+sWqYL2urGM73Ho+Gqxj3K\n"
                + "a98X6NJzQA7NWnydMK3onmLARDV17xUcnSrAzblebng=\n" + "-----END RSA PRIVATE KEY-----";
        try {
            byte[] encryptedData = RSAUtil.encryptByPublicKey(dataString.getBytes("UTF-8"), publicKeyString);
            System.out.println(Bytes.bytes2base64(encryptedData));
            byte[] decryptedData = RSAUtil.decryptByPrivateKey(encryptedData, privateKeyString);
            Assert.assertEquals(dataString, new String(decryptedData, "UTF-8"));
            Assert.assertEquals(publicKeyString, RSAUtil.extractPublicKeyStringFromPrivateKey(privateKeyFileString));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

    }

    @Test
    public void testRSA1024() {
        String publicKeyString = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDR3pZRDSkhyi2msJcWHtepg+l6"
                + "0uGtABXhey+dybH+sB4rTLjrHpi9CknBYOohPVtrrvByPj3Q9zzEIiTKKnDeRYSN"
                + "Se08nYKWqT99pi/jbvYiR31eOiB3DfSFWZ1MSUwCn3It/3eAHMxtA+NuKTZLAoCm" + "Ux9168yHygto4PKHKQIDAQAB";

        String privateKeyString = "MIICXgIBAAKBgQDR3pZRDSkhyi2msJcWHtepg+l60uGtABXhey+dybH+sB4rTLjr"
                + "Hpi9CknBYOohPVtrrvByPj3Q9zzEIiTKKnDeRYSNSe08nYKWqT99pi/jbvYiR31e"
                + "OiB3DfSFWZ1MSUwCn3It/3eAHMxtA+NuKTZLAoCmUx9168yHygto4PKHKQIDAQAB"
                + "AoGBAJqOWhUGpCwGQOhpVFsuC5UDWIbjlt9ZGMQdfEuvpi3ydcVdkzFULxfI49hO"
                + "Dx67mQAMrXb074FdEps+qelYqBUyo+2L/fsxxVxqXLj+kw3XAnKL44nMQUH48FOy"
                + "zelB1wsHEa8ERF7d/ag8aHwiDKveYhuEcun8awfsF3e8KUQBAkEA+kqo953xozTE"
                + "Jroe0MMRry28bbsSBXC/TP08WIyLmhMWp7r5bf04WkhfqZhh4gnKH+ktGVtjfdvA"
                + "zWWaqTHFOQJBANan657XNgRblI8IYvmDqt6LoAkVDgrJ3bhMIIxzQt8AKaIaTBZR"
                + "fDy9+6WmnbnTBc/5ZkQAKnNCsWScyhUfQXECQEDzhPuVGjK/K7PdEsgcBwfNt5tG"
                + "M3wD8TDaeoGEH2ohT/zA8Z+QWcJcC68oIyaUYcZ8LT7qT1xG3bavij4j80kCQQDJ"
                + "vBbcuTWy8r6zjhY2mV/Et0Q4ROllikIuKp2MYea+3dBC4KQRdZNlIseV7vjc403b"
                + "H3DVB7LdkBhXjmL9ZtqBAkEAmDOoYc8XHfhBKmFB3NcbEQ4pFdx3HsDp8wDc/hW7"
                + "LfkkxZ345v28EzMQFk40awyR2PRVO3UT62x1sXWwNS5jJg==";
        String dataString = "123abc++__|汉字";

        String privateKeyFileString = "-----BEGIN RSA PRIVATE KEY-----\n"
                + "MIICXgIBAAKBgQDR3pZRDSkhyi2msJcWHtepg+l60uGtABXhey+dybH+sB4rTLjr\n"
                + "Hpi9CknBYOohPVtrrvByPj3Q9zzEIiTKKnDeRYSNSe08nYKWqT99pi/jbvYiR31e\n"
                + "OiB3DfSFWZ1MSUwCn3It/3eAHMxtA+NuKTZLAoCmUx9168yHygto4PKHKQIDAQAB\n"
                + "AoGBAJqOWhUGpCwGQOhpVFsuC5UDWIbjlt9ZGMQdfEuvpi3ydcVdkzFULxfI49hO\n"
                + "Dx67mQAMrXb074FdEps+qelYqBUyo+2L/fsxxVxqXLj+kw3XAnKL44nMQUH48FOy\n"
                + "zelB1wsHEa8ERF7d/ag8aHwiDKveYhuEcun8awfsF3e8KUQBAkEA+kqo953xozTE\n"
                + "Jroe0MMRry28bbsSBXC/TP08WIyLmhMWp7r5bf04WkhfqZhh4gnKH+ktGVtjfdvA\n"
                + "zWWaqTHFOQJBANan657XNgRblI8IYvmDqt6LoAkVDgrJ3bhMIIxzQt8AKaIaTBZR\n"
                + "fDy9+6WmnbnTBc/5ZkQAKnNCsWScyhUfQXECQEDzhPuVGjK/K7PdEsgcBwfNt5tG\n"
                + "M3wD8TDaeoGEH2ohT/zA8Z+QWcJcC68oIyaUYcZ8LT7qT1xG3bavij4j80kCQQDJ\n"
                + "vBbcuTWy8r6zjhY2mV/Et0Q4ROllikIuKp2MYea+3dBC4KQRdZNlIseV7vjc403b\n"
                + "H3DVB7LdkBhXjmL9ZtqBAkEAmDOoYc8XHfhBKmFB3NcbEQ4pFdx3HsDp8wDc/hW7\n"
                + "LfkkxZ345v28EzMQFk40awyR2PRVO3UT62x1sXWwNS5jJg==\n" + "-----END RSA PRIVATE KEY-----";
        try {
            byte[] encryptedData = RSAUtil.encryptByPublicKey(dataString.getBytes("UTF-8"), publicKeyString);
            System.out.println(Bytes.bytes2base64(encryptedData));
            byte[] decryptedData = RSAUtil.decryptByPrivateKey(encryptedData, privateKeyString);
            Assert.assertEquals(dataString, new String(decryptedData, "UTF-8"));
            Assert.assertEquals(publicKeyString, RSAUtil.extractPublicKeyStringFromPrivateKey(privateKeyFileString));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

    }

    @Test
    public void testAES128() {

        try {
            String input = "123abc|++/汉字";
            byte[] keyBytes = AESUtil.generate128AesKey();
            System.out.println(Bytes.bytes2base64(keyBytes));
            String aesEncryptResultString = Bytes.bytes2base64(AESUtil.aesEncrypt(input.getBytes("UTF-8"), keyBytes));
            System.out.println(aesEncryptResultString);

            String aesDecryptResultString = new String(AESUtil.aesDecrypt(
Bytes.base642bytes(aesEncryptResultString),
                    keyBytes), "UTF-8");
            System.out.println(aesDecryptResultString);
            Assert.assertEquals(input, aesDecryptResultString);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

    }

}
