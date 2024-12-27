package bg.sofia.uni.fmi.mjt.goodreads.book;

import java.util.Arrays;
import java.util.List;

public record Book(
    String ID,
    String title,
    String author,
    String description,
    List<String> genres,
    double rating,
    int ratingCount,
    String URL
) {

    private static final int TOKENS_LENGTH = 8;
    private static final int ID_INDEX = 0;
    private static final int TITLE_INDEX = 1;
    private static final int AUTHOR_INDEX = 2;
    private static final int DESCRIPTION_INDEX = 3;
    private static final int GENRES_INDEX = 4;
    private static final int RATING_INDEX = 5;
    private static final int RATING_COUNT_INDEX = 6;
    private static final int URL_INDEX = 7;

    public static Book of(String[] tokens) {
        if (tokens == null || tokens.length != TOKENS_LENGTH) {
            throw new IllegalArgumentException(
                "Invalid tokens array. Must contain exactly 8 elements.");
        }
        try {
            String id = tokens[ID_INDEX];
            String title = tokens[TITLE_INDEX];
            String author = tokens[AUTHOR_INDEX];
            String description = tokens[DESCRIPTION_INDEX];

            String genresString = tokens[GENRES_INDEX].trim();
            if (genresString.startsWith("[") && genresString.endsWith("]")) {
                genresString = genresString.substring(1, genresString.length() - 1);
            }
            List<String> genres = Arrays.stream(genresString.split(","))
                .map(String::trim)
                .toList();
            double rating = Double.parseDouble(tokens[RATING_INDEX]);
            int ratingCount = Integer.parseInt(tokens[RATING_COUNT_INDEX].replace(",", ""));
            String url = tokens[URL_INDEX];

            return new Book(id, title, author, description, genres, rating, ratingCount, url);

        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid number format in tokens: " + Arrays.toString(tokens), ex);
        }
    }
}
