package Homeworks.src.bg.sofia.uni.fmi.mjt.newsfeed.cache;

import java.util.Optional;

/**
 * Strategy interface for caching API responses.
 */
public interface NewsCache {

    /**
     * Retrieves a cached response for a given URL.
     * @param url The full request URL used as a key.
     * @return An Optional containing the JSON response if found.
     */
    Optional<String> get(String url);

    /**
     * Stores a response in the cache.
     * @param url The full request URL used as a key.
     * @param response The JSON response body to store.
     */
    void put(String url, String response);
}
