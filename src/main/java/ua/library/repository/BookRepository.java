package ua.library.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.library.model.Book;
import ua.library.model.Genre;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookRepository extends GenericRepository<Book> {
    private static final Logger log = LoggerFactory.getLogger(BookRepository.class);

    public BookRepository() {
        super("Books", Book::isbn);
    }

    public List<Book> findByTitle(String title) {
        String query = requireText(title, "Title").toLowerCase(Locale.ROOT);
        List<Book> result = stream()
                .filter(book -> book.title().toLowerCase(Locale.ROOT).contains(query))
                .sorted(Comparator.comparing(Book::title))
                .toList();
        logSearch("title containing '" + title + "'", result.size());
        return result;
    }

    public List<Book> findByPublishedYearBetween(int from, int to) {
        if (from > to) {
            throw new IllegalArgumentException("The beginning of the year range must not exceed its end");
        }
        List<Book> result = stream()
                .filter(book -> book.publishedYear() >= from && book.publishedYear() <= to)
                .sorted(Comparator.comparingInt(Book::publishedYear))
                .toList();
        logSearch("published year between " + from + " and " + to, result.size());
        return result;
    }

    public List<Book> findByGenreAndMinimumYear(String genreName, int minimumYear) {
        String query = requireText(genreName, "Genre");
        List<Book> result = stream()
                .filter(book -> book.genre().name().equalsIgnoreCase(query))
                .filter(book -> book.publishedYear() >= minimumYear)
                .sorted(Comparator.comparing(Book::title))
                .toList();
        logSearch("genre '" + genreName + "' and year at least " + minimumYear, result.size());
        return result;
    }

    public Optional<Book> findFirstByAuthorName(String authorName) {
        String query = requireText(authorName, "Author name").toLowerCase(Locale.ROOT);
        Optional<Book> result = stream()
                .filter(book -> book.author().getName().toLowerCase(Locale.ROOT).contains(query))
                .sorted(Comparator.comparing(Book::title))
                .findFirst();
        logOptionalSearch("author name containing '" + authorName + "'", result);
        return result;
    }

    public List<String> findDistinctAuthorNamesByTitle(String title, int limit) {
        requirePositiveLimit(limit);
        String query = requireText(title, "Title").toLowerCase(Locale.ROOT);
        List<String> result = stream()
                .filter(book -> book.title().toLowerCase(Locale.ROOT).contains(query))
                .map(book -> book.author().getName())
                .distinct()
                .sorted()
                .limit(limit)
                .toList();
        logSearch("distinct authors for title containing '" + title + "' limited to " + limit, result.size());
        return result;
    }

    public List<Book> findNewestByGenre(String genreName, int limit) {
        requirePositiveLimit(limit);
        String query = requireText(genreName, "Genre");
        List<Book> result = stream()
                .filter(book -> book.genre().name().equalsIgnoreCase(query))
                .sorted(Comparator.comparingInt(Book::publishedYear).reversed())
                .limit(limit)
                .toList();
        logSearch("newest books in genre '" + genreName + "' limited to " + limit, result.size());
        return result;
    }

    public List<Book> sortByTitleThenYearDescending() {
        List<Book> result = findAllSorted(Comparator.comparing(Book::title)
                .thenComparing(Book::publishedYear, Comparator.reverseOrder()));
        logSort("title ascending, publication year descending", result.size());
        return result;
    }

    public List<Book> sortNaturally() {
        List<Book> result = findAllSorted(Comparator.naturalOrder());
        logSort("natural ISBN order", result.size());
        return result;
    }

    public List<Book> sortByYearAscendingWithLambda() {
        List<Book> result = findAllSorted((first, second) -> {
            int byYear = Integer.compare(first.publishedYear(), second.publishedYear());
            return byYear != 0 ? byYear : first.title().compareTo(second.title());
        });
        logSort("publication year ascending with a lambda comparator", result.size());
        return result;
    }

    public List<Book> sortByYearDescendingThenTitle() {
        List<Book> result = findAllSorted(Comparator.comparing(Book::publishedYear)
                .reversed()
                .thenComparing(Book::title));
        logSort("publication year descending, title ascending", result.size());
        return result;
    }

    public Map<Genre, List<Book>> groupByGenre() {
        Map<Genre, List<Book>> result = stream().collect(Collectors.groupingBy(Book::genre));
        log.info("Grouped {} books into {} genre groups", size(), result.size());
        return result;
    }

    public Map<Genre, Long> countByGenre() {
        Map<Genre, Long> result = stream()
                .collect(Collectors.groupingBy(Book::genre, Collectors.counting()));
        log.info("Counted books in {} genre groups", result.size());
        return result;
    }

    public int sumPublishedYears() {
        int result = stream()
                .map(Book::publishedYear)
                .reduce(0, Integer::sum);
        log.info("Reduced publication years of {} books to {}", size(), result);
        return result;
    }

    private void logSort(String criterion, int count) {
        log.info("Sorted books by {}; returned {} items", criterion, count);
    }

    private void logSearch(String criterion, int count) {
        if (count == 0) {
            log.warn("Book search by {} returned no items", criterion);
        } else {
            log.info("Book search by {}; found {} items", criterion, count);
        }
    }

    private void logOptionalSearch(String criterion, Optional<Book> result) {
        result.ifPresentOrElse(
                book -> log.info("Book search by {}; found '{}'", criterion, book.title()),
                () -> log.warn("Book search by {} returned no item", criterion));
    }

    private static void requirePositiveLimit(int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit must be positive");
        }
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.trim();
    }
}
