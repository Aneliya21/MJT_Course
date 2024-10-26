package bg.sofia.uni.fmi.mjt.gameplatform.store.item;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface StoreItem {

    String getTitle(); //Returns the title of the store item.

    BigDecimal getPrice(); //Returns the price of the store item.

    double getRating(); //Returns the average rating of the store item.

    LocalDateTime getReleaseDate(); //Returns the release date of the store item.

    void setTitle(String title); //Sets the title of the store item.

    void setPrice(BigDecimal price); //Sets the price of the store item.

    void setReleaseDate(LocalDateTime releaseDate); //Sets the release date of the store item.

    void rate(double rating); // Rates the store item.
}
