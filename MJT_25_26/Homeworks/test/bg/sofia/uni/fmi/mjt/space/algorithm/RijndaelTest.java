package Homeworks.test.bg.sofia.uni.fmi.mjt.space.algorithm;

import Homeworks.src.bg.sofia.uni.fmi.mjt.space.algorithm.Rijndael;
import Homeworks.src.bg.sofia.uni.fmi.mjt.space.exception.CipherException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class RijndaelTest {

    private SecretKey secretKey;
    private Rijndael rijndael;

    @BeforeEach
    void setUp() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        secretKey = keyGen.generateKey();
        rijndael = new Rijndael(secretKey);
    }

    @Test
    void testConstructorWhenSecretKeyIsNullThenThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Rijndael(null));
    }

    @Test
    void testGetEncryptCipherWhenCalledThenReturnsNonNullCipher() {
        assertNotNull(rijndael.getEncryptCipher());
    }

    @Test
    void testGetDecryptCipherWhenCalledThenReturnsNonNullCipher() {
        assertNotNull(rijndael.getDecryptCipher());
    }

    @Test
    void testEncryptWhenInputIsValidThenOutputIsEncrypted() throws Exception {
        String original = "Hello World!";
        ByteArrayInputStream input = new ByteArrayInputStream(original.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        rijndael.encrypt(input, output);

        byte[] encrypted = output.toByteArray();
        assertNotNull(encrypted);
        assertNotEquals(original, new String(encrypted, StandardCharsets.UTF_8));
    }

    @Test
    void testDecryptWhenInputIsEncryptedThenOriginalIsRestored() throws Exception {
        String original = "Hello World!";
        ByteArrayInputStream input = new ByteArrayInputStream(original.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream encryptedOutput = new ByteArrayOutputStream();

        rijndael.encrypt(input, encryptedOutput);
        byte[] encryptedData = encryptedOutput.toByteArray();

        ByteArrayInputStream encryptedInput = new ByteArrayInputStream(encryptedData);
        ByteArrayOutputStream decryptedOutput = new ByteArrayOutputStream();

        rijndael.decrypt(encryptedInput, decryptedOutput);
        String decrypted = decryptedOutput.toString(StandardCharsets.UTF_8);

        assertEquals(original, decrypted);
    }

    @Test
    void testEncryptWhenOutputStreamFailsThenThrowsCipherException() {
        ByteArrayInputStream input = new ByteArrayInputStream("data".getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream() {
            @Override
            public void close() {
                throw new RuntimeException("Forced close");
            }
        };

        assertThrows(CipherException.class, () -> rijndael.encrypt(input, output));
    }

    @Test
    void testDecryptWhenInputStreamFailsThenThrowsCipherException() {
        ByteArrayInputStream input = new ByteArrayInputStream("data".getBytes(StandardCharsets.UTF_8)) {
            @Override
            public void close() {
                throw new RuntimeException("Forced close");
            }
        };
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        assertThrows(CipherException.class, () -> rijndael.decrypt(input, output));
    }
}
