package Homeworks.src.bg.sofia.uni.fmi.mjt.newsfeed.model;

import com.google.gson.annotations.SerializedName;

/**
 * Represents the origin or publisher of a news article.
 * <p>
 *     This class provides identifying information about the news outlet,
 *     such as its unique identifier and display name. It is used as a
 *     nested object within the {@link Article} class.
 * </p>
 */
public class Source {

    /**
     * The unique identifier for the news source.
     */
    @SerializedName("id")
    private String id;

    /**
     * The human-readable name of the news source.
     */
    @SerializedName("name")
    private String name;

    /**
     * Gets the unique identifier of the source.
     * @return The source ID string, or {@code null} if not available.
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the display name of the source.
     * @return The name of the publisher.
     */
    public String getName() {
        return name;
    }
}

