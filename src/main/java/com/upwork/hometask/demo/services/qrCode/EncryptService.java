package com.upwork.hometask.demo.services.qrCode;

import com.upwork.hometask.demo.models.exception.EncryptException;
import com.upwork.hometask.demo.utils.AESUtil;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.NoSuchAlgorithmException;

@Service
public class EncryptService {

    private static final boolean DISABLED = true;
    private static final SecretKey key;

    static {
        try {
            key = AESUtil.generateKey(128);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static final IvParameterSpec ivParameterSpec = AESUtil.generateIv();
    private static final String algorithm = "AES/CBC/PKCS5Padding";

    public String encrypt(String input) {
        if(DISABLED)
            return input;
        try {
            return AESUtil.encrypt(algorithm, input, key, ivParameterSpec);
        } catch (Throwable e) {
            throw new EncryptException();
        }
    }

    public String decrypt(String cipherText) {
        if(DISABLED)
            return cipherText;
        try {
            return AESUtil.decrypt(algorithm, cipherText, key, ivParameterSpec);
        } catch (Throwable e) {
            throw new EncryptException();
        }
    }

}
