package bg.sofia.uni.fmi.mjt.pipeline;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CacheTest {

    private Cache cache;

    @BeforeEach
    void setUp() {
        cache = new Cache();
    }

    @Test
    void testCacheValueStoresAndRetrieves() {
        cache.cacheValue("key1", "value1");
        assertEquals("value1", cache.getCachedValue("key1"),
            "cacheValue should store the value correctly");
    }

    @Test
    void testCacheValueOverwritesExistingValue() {
        cache.cacheValue("key1", "value1");
        cache.cacheValue("key1", "value2");
        assertEquals("value2", cache.getCachedValue("key1"),
            "cacheValue should overwrite the previous value if key exists");
    }

    @Test
    void testCacheValueThrowsOnNullKey() {
        assertThrows(IllegalArgumentException.class, () -> cache.cacheValue(null, "value"),
            "IllegalArgumentException should be thrown if key is null");
    }

    @Test
    void testCacheValueThrowsOnNullValue() {
        assertThrows(IllegalArgumentException.class, () -> cache.cacheValue("key", null),
            "IllegalArgumentException should be thrown if value is null");
    }

    @Test
    void testGetCachedValueReturnsNullForMissingKey() {
        assertNull(cache.getCachedValue("missingKey"),
            "getCachedValue should return null if the key is not present");
    }

    @Test
    void testGetCachedValueThrowsOnNullKey() {
        assertThrows(IllegalArgumentException.class, () -> cache.getCachedValue(null),
            "IllegalArgumentException should be thrown if key is null");
    }

    @Test
    void testContainsKeyReturnsTrueForPresentKey() {
        cache.cacheValue("key1", "value1");
        assertTrue(cache.containsKey("key1"), "containsKey should return true for a cached key");
    }

    @Test
    void testContainsKeyReturnsFalseForMissingKey() {
        assertFalse(cache.containsKey("missingKey"), "containsKey should return false for a missing key");
    }

    @Test
    void testContainsKeyThrowsOnNullKey() {
        assertThrows(IllegalArgumentException.class, () -> cache.containsKey(null),
            "IllegalArgumentException should be thrown if key is null");
    }

    @Test
    void testClearEmptiesCache() {
        cache.cacheValue("key1", "value1");
        cache.cacheValue("key2", "value2");

        cache.clear();
        assertTrue(cache.isEmpty(), "clear should remove all entries from the cache");
        assertNull(cache.getCachedValue("key1"), "getCachedValue should return null after clear");
        assertNull(cache.getCachedValue("key2"), "getCachedValue should return null after clear");
    }

    @Test
    void testIsEmptyOnNewCache() {
        assertTrue(cache.isEmpty(), "isEmpty should return true for a newly created cache");
    }

    @Test
    void testIsEmptyAfterAddingValues() {
        cache.cacheValue("key1", "value1");
        assertFalse(cache.isEmpty(), "isEmpty should return false after adding entries");
    }

    @Test
    void testIsEmptyAfterClear() {
        cache.cacheValue("key1", "value1");
        cache.clear();
        assertTrue(cache.isEmpty(), "isEmpty should return true after clearing the cache");
    }
}
