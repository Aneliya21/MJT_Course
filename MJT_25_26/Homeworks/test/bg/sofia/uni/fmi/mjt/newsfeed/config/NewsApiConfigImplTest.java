package Homeworks.test.bg.sofia.uni.fmi.mjt.newsfeed.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class NewsApiConfigImplTest {

    private NewsApiConfig config;

    private static final String TEST_KEY = "secret-api-key-123";
    private static final String TEST_URL = "https://newsapi.org/v2";
    private static final int CONNECT_TIMEOUT = 1000;
    private static final int READ_TIMEOUT = 2000;

    @BeforeEach
    void setUp() {
        config = new NewsApiConfigImpl(TEST_KEY, TEST_URL, CONNECT_TIMEOUT, READ_TIMEOUT);
    }

    @Test
    void testApiKeyReturnsCorrectValue() {
        assertEquals(TEST_KEY, config.apiKey(), "The apiKey getter should return the original key");
    }

    @Test
    void testBaseURLReturnsCorrectValue() {
        assertEquals(TEST_URL, config.baseURL(), "The baseURL getter should return the provided URL");
    }

    @Test
    void testConnectTimeoutMillisReturnsCorrectValue() {
        assertEquals(CONNECT_TIMEOUT, config.connectTimeoutMillis(), "The connect timeout should match the input");
    }

    @Test
    void testReadTimeoutMillisReturnsCorrectValue() {
        assertEquals(READ_TIMEOUT, config.readTimeoutMillis(), "The read timeout should match the input");
    }

    @Test
    void testToStringMasksApiKey() {
        String result = config.toString();

        assertFalse(result.contains(TEST_KEY), "The toString representation must not contain the actual API key");
    }

    @Test
    void testToStringContainsMaskedPlaceholder() {
        String result = config.toString();

        assertTrue(result.contains("apiKey='*****'"), "The toString should show a masked placeholder for the key");
    }

    private void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new org.opentest4j.AssertionFailedError(message);
        }
    }
}
