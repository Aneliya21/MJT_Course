package Homeworks.src.bg.sofia.uni.fmi.mjt.space.algorithm;

import Homeworks.src.bg.sofia.uni.fmi.mjt.space.exception.CipherException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import java.io.InputStream;
import java.io.OutputStream;

public class Rijndael implements SymmetricBlockCipher {

    private final SecretKey secretKey;
    private final Cipher encryptCipher;
    private final Cipher decryptCipher;

    /**
     * Encrypts/decrypts data using AES (Rijndael) algorithm with the provided secret key.
     *
     * @param secretKey the encryption/decryption key
     * @throws IllegalArgumentException if secretKey is null
     */
    public Rijndael(SecretKey secretKey) {
        if (secretKey == null) {
            throw new IllegalArgumentException("SecretKey cannot be null");
        }
        this.secretKey = secretKey;
        try {
            encryptCipher = Cipher.getInstance("AES");
            encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey);

            decryptCipher = Cipher.getInstance("AES");
            decryptCipher.init(Cipher.DECRYPT_MODE, secretKey);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize cipher", e);
        }
    }

    public Cipher getEncryptCipher() {
        return encryptCipher;
    }

    public Cipher getDecryptCipher() {
        return decryptCipher;
    }

    @Override
    public void encrypt(InputStream inputStream, OutputStream outputStream) throws CipherException {
        try (OutputStream cos = new CipherOutputStream(outputStream, encryptCipher)) {
            inputStream.transferTo(cos);
        } catch (Exception e) {
            throw new CipherException("Encryption failed", e);
        }
    }

    @Override
    public void decrypt(InputStream inputStream, OutputStream outputStream) throws CipherException {
        try (InputStream cis = new CipherInputStream(inputStream, decryptCipher)) {
            cis.transferTo(outputStream);
        } catch (Exception e) {
            throw new CipherException("Decryption failed", e);
        }
    }
}