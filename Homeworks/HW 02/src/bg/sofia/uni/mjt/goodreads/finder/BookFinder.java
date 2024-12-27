package bg.sofia.uni.fmi.mjt.goodreads.finder;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BookFinder implements BookFinderAPI {

    private Set<Book> books;
    private TextTokenizer tokenizer;
    private Set<String> cachedGenres;
    private Map<String, Set<Book>> keywordIndex;
    private Map<String, Set<Book>> genreIndex;
    private Map<String, List<Book>> authorIndex;

    public BookFinder(Set<Book> books, TextTokenizer tokenizer) {
        this.books = books;
        this.tokenizer = tokenizer;
        this.authorIndex = buildAuthorIndex();
        this.genreIndex = buildGenreIndex();
        this.keywordIndex = buildKeywordIndex();
    }

    public Set<Book> allBooks() {
        return books;
    }

    @Override
    public List<Book> searchByAuthor(String authorName) {
        if (authorName == null || authorName.isEmpty()) {
            throw new IllegalArgumentException("Author name cannot be null or empty");
        }
        return new ArrayList<>(authorIndex.getOrDefault(authorName, List.of()));
    }

    @Override
    public Set<String> allGenres() {
        if (cachedGenres == null) {
            cachedGenres = books.stream()
                .flatMap(book -> book.genres().stream())
                .collect(Collectors.toSet());
        }
        return new HashSet<>(cachedGenres);
    }

    @Override
    public List<Book> searchByGenres(Set<String> genres, MatchOption option) {
        if (genres == null || genres.isEmpty()) {
            throw new IllegalArgumentException("Genres cannot be null or empty");
        }

        Set<Book> result = new HashSet<>();
        for (String genre : genres) {
            Set<Book> booksForGenre = genreIndex.getOrDefault(genre, Set.of());
            if (option == MatchOption.MATCH_ALL) {
                if (result.isEmpty()) {
                    result.addAll(booksForGenre);
                } else {
                    result.retainAll(booksForGenre);
                }
            } else if (option == MatchOption.MATCH_ANY) {
                result.addAll(booksForGenre);
            }
        }

        return new ArrayList<>(result);
    }

    @Override
    public List<Book> searchByKeywords(Set<String> keywords, MatchOption option) {
        if (keywords == null || keywords.isEmpty()) {
            throw new IllegalArgumentException("Keywords cannot be null or empty");
        }
        Set<Book> result = new HashSet<>();
        for (String keyword : keywords) {
            Set<Book> booksForKeyword = keywordIndex.getOrDefault(keyword, Set.of());
            if (option == MatchOption.MATCH_ALL) {
                if (result.isEmpty()) {
                    result.addAll(booksForKeyword);
                } else {
                    result.retainAll(booksForKeyword);
                }
            } else if (option == MatchOption.MATCH_ANY) {
                result.addAll(booksForKeyword);
            }
        }
        return new ArrayList<>(result);
    }

    private Map<String, Set<Book>> buildKeywordIndex() {
        Map<String, Set<Book>> index = new HashMap<>();
        for (Book book : books) {
            Set<String> tokens = new HashSet<>();
            tokens.addAll(tokenizer.tokenize(book.title()));
            tokens.addAll(tokenizer.tokenize(book.description()));
            for (String token : tokens) {
                index.computeIfAbsent(token, k -> new HashSet<>()).add(book);
            }
        }
        return index;
    }

    private Map<String, Set<Book>> buildGenreIndex() {
        Map<String, Set<Book>> index = new HashMap<>();
        for (Book book : books) {
            for (String genre : book.genres()) {
                index.computeIfAbsent(genre, k -> new HashSet<>()).add(book);
            }
        }
        return index;
    }

    private Map<String, List<Book>> buildAuthorIndex() {
        Map<String, List<Book>> index = new HashMap<>();
        for (Book book : books) {
            index.computeIfAbsent(book.author(), k -> new ArrayList<>()).add(book);
        }
        return index;
    }
}
