package bg.sofia.uni.fmi.mjt.gameplatform.store.item.category;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.math.RoundingMode;

public class Game  implements StoreItem{

    private String title;
    private BigDecimal price;
    private LocalDateTime releaseDate;
    private String genre;

    //private double currentAverage;

    //private double[] ratings;
    //private int numOfRatings;
    //private int currInd;

    private double rating;

    private long rated;

    public Game(String title, BigDecimal price, LocalDateTime releaseDate, String genre) {
        this.title = title;
        //this.price = price;
        setPrice(price);
        this.releaseDate = releaseDate;
        this.genre = genre;
        //this.currentAverage = 0.0;
        //ratings = new double[10];
        //numOfRatings = 10;
        //currInd = 0;
        rating = 0.0;
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
        return rating / rated;
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
        if(rating >= 0 && rating <= 5) {
            this.rating += rating;
            this.rated++;
        }
    }
}
