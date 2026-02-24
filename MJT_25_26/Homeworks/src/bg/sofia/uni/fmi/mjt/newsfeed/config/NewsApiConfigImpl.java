package Homeworks.src.bg.sofia.uni.fmi.mjt.newsfeed.config;

/**
 * An immutable implementation of the {@link NewsApiConfig} using a Java Record.
 * <p>
 *     This class stores the necessary settings for the News API client, including
 *     credentials and network timeouts. It is designed to be thread-safe and
 *     provides a secure string representation that prevents the leakage of the
 *     API key in logs.
 * </p>
 * @param apiKey                The authentication key for News API.
 * @param baseURL               The root endpoint for API requests.
 * @param connectTimeoutMillis  The time limit for establishing a connection.
 * @param readTimeoutMillis     The time limit for receiving a response.
 */
public record NewsApiConfigImpl(
    String apiKey,
    String baseURL,
    int connectTimeoutMillis,
    int readTimeoutMillis
) implements NewsApiConfig {

    /**
     * Return a string representation of the configuration
     * @return A string containing the base URL and timeout settings,
     * with the API key hidden.
     */
    @Override
    public String toString() {
        return "NewsApiConfig{" +
            "baseURL='" + baseURL + '\'' +
            ", apiKey='*****'" +
            ", connectTimeout=" + connectTimeoutMillis +
            ", readTimeout=" + readTimeoutMillis +
            '}';
    }
}