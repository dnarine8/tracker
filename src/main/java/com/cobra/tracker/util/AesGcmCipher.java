package com.cobra.tracker.util;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class AesGcmCipher {
    public byte[] encrypt(byte[] key, byte[] nonce, byte[] plaintext) throws CobraException {
        return encrypt(key, nonce, plaintext, null);
    }

    /**
     * Encrypts a message.
     *
     * @param nonce          the nonce
     * @param plaintext      the message to encrypt
     * @param associatedData the associated data
     * @return the encrypted message
     * @throws CobraException in case of errors
     */
    public byte[] encrypt(byte[] key, byte[] nonce, byte[] plaintext, byte[] associatedData) throws CobraException {
        try {
            SecretKey secretKey = new SecretKeySpec(key, Constants.AES);
            Cipher cipher = Cipher.getInstance(Constants.AES_GCM_NOPADDING);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(Constants.GCM_TAG_LENGTH, nonce);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
            if (associatedData != null) {
                cipher.updateAAD(associatedData);
            }
            return cipher.doFinal(plaintext);
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException |
                 NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new CobraException("Fail to encrypt data.");
        }
    }

    public byte[] decrypt(byte[] key, byte[] nonce, byte[] cipherText) throws CobraException {
        return decrypt(key, nonce, cipherText, null);
    }

    /**
     * Decrypts a message.
     *
     * @param nonce          the nonce
     * @param cipherText     the message to decrypt
     * @param associatedData the associated data
     * @return the decrypted message
     * @throws CobraException in case of errors
     */
    public byte[] decrypt(byte[] key, byte[] nonce, byte[] cipherText, byte[] associatedData) throws CobraException {
        try {
            SecretKey secretKey = new SecretKeySpec(key, Constants.AES);
            Cipher cipher = Cipher.getInstance(Constants.AES_GCM_NOPADDING);
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(Constants.GCM_TAG_LENGTH, nonce);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);
            if (associatedData != null) {
                cipher.updateAAD(associatedData);
            }
            return cipher.doFinal(cipherText);
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException |
                 NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new CobraException("Fail to decrypt data.");
        }
    }
}
