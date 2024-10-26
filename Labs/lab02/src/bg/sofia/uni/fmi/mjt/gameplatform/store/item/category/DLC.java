package bg.sofia.uni.fmi.mjt.gameplatform.store.item.category;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.category.Game;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.math.RoundingMode;

public class DLC implements StoreItem {

    private String title;
    private BigDecimal price;
    private double rating;
    private long rated;

    private LocalDateTime releaseDate;
    private Game game;

    public DLC(String title, BigDecimal price, LocalDateTime releaseDate, Game game) {
        setTitle(title);
        setPrice(price);
        setReleaseDate(releaseDate);
        this.game = game;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public double getRating() {
        return rating/rated;
    }

    @Override
    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setPrice(BigDecimal price) {
        this.price = price.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public void setReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public void rate(double rating) {
        if(rating >= 1 && rating <= 5) {
            this.rating += rating;
            this.rated++;
        }
    }
}
