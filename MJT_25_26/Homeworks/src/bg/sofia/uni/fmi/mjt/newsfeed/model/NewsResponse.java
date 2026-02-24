package Homeworks.src.bg.sofia.uni.fmi.mjt.newsfeed.model;

import com.google.gson.annotations.SerializedName;

/**
 * Represents the top-level response object from the NewsApi.
 * <p>
 *     This class serves as a container for the API's status metadata and the
 *     resulting array if {@link Article} objects. It is designed to be used
 *     with GSON for parsing the root level of the JSON response.
 * </p>
 */
public class NewsResponse {

    /**
     * The status of the request.
     * Typically, "OK" if the request was successful, or "ERROR" if it failed.
     */
    @SerializedName("status")
    private String status;

    /**
     * The total number of results available for the query across all pages.
     */
    @SerializedName("totalResults")
    private int totalResults;

    /**
     * The list of news articles returned in the current response page.
     */
    @SerializedName("articles")
    private Article[] articles;

    /**
     * Gets the request status.
     * @return A string indicating success or failure (e.g., "OK").
     */
    public String getStatus() {
        return status;
    }

    /**
     * Gets the total number of articles available for the specific search criteria.
     * @return The total result count as an integer.
     */
    public int getTotalResults() {
        return totalResults;
    }

    /**
     * Gets the array of articles included in this response.
     * @return An array of {@link Article} objects.
     */
    public Article[] getArticles() {
        return articles;
    }
}
