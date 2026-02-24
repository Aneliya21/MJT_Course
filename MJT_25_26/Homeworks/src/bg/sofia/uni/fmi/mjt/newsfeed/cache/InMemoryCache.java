package Homeworks.src.bg.sofia.uni.fmi.mjt.newsfeed.cache;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A thread-safe, in-memory implementation of the {@link NewsCache} strategy.
 * <p>
 *     This implementation uses a {@link ConcurrentHashMap} to store API responses,
 *     making it suitable for multithreaded environments. It serves as the default
 *     caching mechanism to prevent redundant network calls and stay within
 *     API rate limits.
 * </p>
 */
public class InMemoryCache implements NewsCache {

    /**
     * Internal storage for cached responses, mapping fill request URLs to JSON bodies.
     */
    private final Map<String, String> cache = new ConcurrentHashMap<>();

    /**
     * Retrieves a cached response for the specified URL.
     * * @param url The full request URL used as the cache key.
     * @return An {@link Optional} containing the cached JSON response if present;
     * an empty Optional otherwise.
     */
    public Optional<String> get(String url) {
        if (url == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(cache.get(url));
    }

    /**
     * Stores a response in the cache.
     * <p>
     * If either the URL or the response is {@code null}, the operation is ignored
     * to maintain cache integrity.
     * </p>
     * * @param url      The full request URL used as the cache key.
     * @param response The JSON response body to be stored.
     */
    @Override
    public void put(String url, String response) {
        if (url != null && response != null) {
            cache.put(url, response);
        }
    }
}
