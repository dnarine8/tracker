
package com.cobra.forensics.util;

import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 */
public class CryptoUtil {
    /**
     * Helper function to calculate the HMAC value.
     *
     * @param hmacAlg the HMAC algorithm
     * @param secret  the secret
     * @param data    the data
     * @return the message digest value
     * @throws CobraException in case of failure
     */
    public static byte[] hmacHash(String hmacAlg, byte[] secret, byte[] data) throws CobraException {
        byte[] hmac;

        try {
            Mac mac = Mac.getInstance(hmacAlg);
            SecretKeySpec secret_key = new SecretKeySpec(secret, hmacAlg);
            mac.init(secret_key);
            mac.update(data);
            hmac = mac.doFinal();
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            throw new CobraException("Failed to calculate the MAC.");
        }

        return hmac;
    }

    public static byte[] hash(String hashAlgorithm, String filename, long length) throws CobraException {
        if (length == 0){
            return new byte[0];
        }
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(filename))) {
            MessageDigest md = MessageDigest.getInstance(hashAlgorithm);

            int bufferSize;
            if (length > 4096) {
                bufferSize = 4096;
            } else {
                bufferSize = (int) length;
            }

            byte[] buffer = new byte[bufferSize];
            int bytesRead = 0;
            while (bytesRead < length) {
                int currentBytesRead = in.read(buffer);
                if (currentBytesRead == -1){
                    break;
                }
                bytesRead += currentBytesRead;
                md.update(buffer, 0, currentBytesRead);
            }
            return md.digest();
        } catch (IOException | NoSuchAlgorithmException e) {
                throw new CobraException(String.format("Warning: Failed to calculate hash for %s. %s ", filename,e.getMessage()));
        }
    }

    public static byte[] hash(String hashAlgorithm, byte[] data) throws CobraException {
        try {
            MessageDigest md = MessageDigest.getInstance(hashAlgorithm);
            md.update(data);
            return md.digest();
        } catch (NoSuchAlgorithmException ex) {
            throw new CobraException("Failed to calculate the message digest." + ex.getMessage());
        }
    }

    public static X509Certificate getCertificate(String cert) throws CertificateException {
        byte[] encodedCert = DataConverter.base64ToBinary(cert);
        InputStream inputStream = new ByteArrayInputStream(encodedCert);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        return (X509Certificate) cf.generateCertificate(inputStream);
    }

    public static PrivateKey getECPrivateKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] encodedPrivateKey = DataConverter.base64ToBinary(key);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
        return keyFactory.generatePrivate(privateKeySpec);
    }

    public static byte[] getRandom(int size) {
        byte[] randomBytes = new byte[size];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(randomBytes);
        return randomBytes;
    }
}
