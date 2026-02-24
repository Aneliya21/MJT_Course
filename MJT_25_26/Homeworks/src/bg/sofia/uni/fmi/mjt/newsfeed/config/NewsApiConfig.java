package Homeworks.src.bg.sofia.uni.fmi.mjt.newsfeed.config;

/**
 * Configuration contract for the News API client.
 * <p>
 *     This interface defines the essential parameters required to authenticate
 *     and communicate with the News API service. Implementations should ensure
 *     that sensitive data, such as the API key, is handled securely and not
 *     exposed through logs or console output.
 * </p>
 */
public interface NewsApiConfig {

    /**
     * Returns the secret API key used for authentication.
     * <p>
     *     This key is required for every request to the News API.
     * </p>
     * @return The API key as a String.
     */
    String apiKey();

    /**
     * Returns the base URL of the News API service.
     * @return The root endpoint URL.
     */
    String baseURL();

    /**
     * Returns the maximum time (in milliseconds) allowed to establish a
     * connection to the API server.
     * @return The connection timeout duration is milliseconds.
     */
    int connectTimeoutMillis();

    /**
     * Returns the maximum time (in milliseconds) to wait for data to
     * arrive after a connection has been established.
     * @return The read timeout duration in milliseconds.
     */
    int readTimeoutMillis();
}
