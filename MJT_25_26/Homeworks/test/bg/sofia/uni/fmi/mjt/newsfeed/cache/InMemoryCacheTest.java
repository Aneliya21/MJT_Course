package Homeworks.test.bg.sofia.uni.fmi.mjt.newsfeed.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InMemoryCacheTest {

    private NewsCache cache;
    private static final String TEST_URL = "https://newsapi.org/v2/top-headlines?q=java";
    private static final String TEST_RESPONSE = "{\"status\":\"ok\",\"totalResults\":1}";

    @BeforeEach
    void setUp() {
        cache = new InMemoryCache();
    }

    @Test
    void testGetReturnEmptyWhenUrlNotFound() {
        Optional<String> result = cache.get("non-existent-url");
        assertTrue(result.isEmpty(), "Result should be empty for a non-existent URL");
    }

    @Test
    void testPutStoresValueSuccessfully() {
        cache.put(TEST_URL, TEST_RESPONSE);

        Optional<String> result = cache.get(TEST_URL);
        assertTrue(result.isPresent(), "Result should be present after putting it in cache");
    }

    @Test
    void testGetReturnsCorrectValue() {
        cache.put(TEST_URL, TEST_RESPONSE);

        Optional<String> result = cache.get(TEST_URL);
        assertEquals(TEST_RESPONSE, result.get(), "The cached response should match the input");
    }

    @Test
    void testPutIgnoreNullUrl() {
        cache.put(null, TEST_RESPONSE);

        Optional<String> result = cache.get(null);
        assertTrue(result.isEmpty(), "Cache should not store entries with a null URL");
    }

    @Test
    void testPutIgnoreNullResponse() {
        cache.put(TEST_URL, null);

        Optional<String> result = cache.get(TEST_URL);
        assertFalse(result.isPresent(), "Cache should not store entries with a null response body");
    }

    @Test
    void testPutOverwriteExistingValue() {
        String newResponse = "{\"status\":\"ok\",\"totalResults\":100}";
        cache.put(TEST_URL, TEST_RESPONSE);
        cache.put(TEST_URL, newResponse);

        Optional<String> result = cache.get(TEST_URL);
        assertEquals(newResponse, result.get(), "The cache should update the value for an existing key");
    }
}
