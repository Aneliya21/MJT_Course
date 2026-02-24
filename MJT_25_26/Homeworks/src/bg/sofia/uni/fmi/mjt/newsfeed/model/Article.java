package Homeworks.src.bg.sofia.uni.fmi.mjt.newsfeed.model;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a single news article entry.
 * <p>
 *     This model is designed for JSON deserialization using the Google GSON library.
 *     It maps API response data to Java fields, specifically handling the conversion
 *     of JSON keys to camelCase Java naming conventions.
 * </p>
 */
public class Article {

    /**
     * The origin or publication source of the article.
     */
    @SerializedName("source")
    private Source source;

    /**
     * The name of the person or entity that wrote the article.
     */
    @SerializedName("author")
    private String author;

    /**
     * The headline or main title of the article.
     */
    @SerializedName("title")
    private String title;

    /**
     * A short summary or lead paragraph describing the article content.
     */
    @SerializedName("description")
    private String description;

    /**
     * The direct web link (URL) to the full article.
     */
    @SerializedName("url")
    private String url;

    /**
     * The timestamp indicating when the article was published.
     */
    @SerializedName("publishedAt")
    private String publishedAt;

    /**
     * Gets the source of the article.
     * @return A {@Link Source} object containing source details.
     */
    public Source getSource() {
        return source;
    }

    /**
     * Gets the author of the article
     * @return The author's name as a String.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Gets the title of the article.
     * @return The headline of the article.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the description or summary of the article.
     * @return The article summary.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the URL address of the article.
     * @return The web link to the article.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Gets the date and time the article was published.
     * @return The publication timestamp as a String.
     */
    public String getPublishedAt() {
        return publishedAt;
    }
}
