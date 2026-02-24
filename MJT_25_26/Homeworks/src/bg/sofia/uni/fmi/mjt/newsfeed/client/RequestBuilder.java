package Homeworks.src.bg.sofia.uni.fmi.mjt.newsfeed.client;

/**
 * A final utility class that follows the Builder pattern to prepare parameters
 * for News API requests.
 * <p>
 *     This class ensures data integrity by validating all inputs
 *     before they are assigned to the internal state.
 * </p>
 */
public final class RequestBuilder {

    private int page;
    private int pageSize;
    private String keywords;
    private String category;
    private String country;

    /**
     * Constructs a new RequestBuilder with default values.
     */
    public RequestBuilder() {

    }

    /**
     * Sets the page number for the request.
     * @param page The page index (must be greater than 0).
     * @return This {@code RequestBuilder} instance for method chaining.
     * @throws IllegalArgumentException if page is less than or equal to zero.
     */
    public RequestBuilder withPage(int page) {
        isLessOrEqualToZero(page, "Page");
        this.page = page;
        return this;
    }

    /**
     * Sets the page number of articles to return per page.
     * @param pageSize The number of results (must be greater than 0).
     * @return This {@code RequestBuilder} instance for method chaining.
     * @throws IllegalArgumentException if pageSize is less than or equal to zero.
     */
    public RequestBuilder withPageSize(int pageSize) {
        isLessOrEqualToZero(pageSize, "Page size");
        this.pageSize = pageSize;
        return this;
    }

    /**
     * Sets the search keyword or phrases for the news query.
     * @param keywords The search terms (cannot be null or empty).
     * @return This {@code RequestBuilder} instance for method chaining.
     * @throws IllegalArgumentException if keywords is null or empty.
     */
    public RequestBuilder withKeywords(String keywords) {
        validatePresence(keywords, "Keywords");
        this.keywords = keywords;
        return this;
    }

    /**
     * Sets the news category to filter results (e.g., "business", "technology").
     * @param category The category name (cannot be null or empty).
     * @return This {@code RequestBuilder} instance for method chaining.
     * @throws IllegalArgumentException if category is null or empty.
     */
    public RequestBuilder withCategory(String category) {
        validatePresence(category, "Category");
        this.category = category;
        return this;
    }

    /**
     * Sets the country you want to get headlines for.
     * @param country The country code (cannot be null or empty).
     * @return This {@code RequestBuilder} instance for method chaining.
     * @throws IllegalArgumentException if country is null or empty.
     */
    public RequestBuilder withCounty(String country) {
        validatePresence(country, "Country");
        this.country = country;
        return this;
    }

    /** @return The current page number. */
    public int getPage() {
        return page;
    }

    /** @return The current page size. */
    public int getPageSize() {
        return pageSize;
    }

    /** @return The search keywords. */
    public String getKeywords() {
        return keywords;
    }

    /** @return The filtered category. */
    public String getCategory() {
        return category;
    }

    /** @return The target country. */
    public String getCountry() {
        return country;
    }

    /**
     * Validates that a string is neither null nor empty.
     * @param value The sting to check.
     * @param fieldName The name of the field for the error message.
     */
    static void validatePresence(String value, String fieldName) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty");
        }
    }

    /**
     * Validates that an integer is positive.
     * @param value The number to check.
     * @param fieldName Name used for the error message.
     */
    private static void isLessOrEqualToZero(int value, String fieldName) {
        if (value <= 0) {
            throw new IllegalArgumentException(fieldName + " cannot be zero or less than zero");
        }
    }
}
