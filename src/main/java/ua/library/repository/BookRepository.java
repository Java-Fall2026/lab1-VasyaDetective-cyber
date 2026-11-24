package ua.library.repository;

import ua.library.model.Book;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class BookRepository extends GenericRepository<Book> {
    public BookRepository() {
        super("Books", Book::isbn);
    }

    public List<Book> findByTitle(String title) {
        String query = requireText(title, "Title").toLowerCase();
        return stream()
                .filter(book -> book.title().toLowerCase().contains(query))
                .toList();
    }

    public List<Book> findByPublishedYearBetween(int from, int to) {
        if (from > to) {
            throw new IllegalArgumentException("The beginning of the year range must not exceed its end");
        }
        return stream()
                .filter(book -> book.publishedYear() >= from && book.publishedYear() <= to)
                .toList();
    }

    public List<Book> findByGenreAndMinimumYear(String genreName, int minimumYear) {
        String query = requireText(genreName, "Genre");
        return stream()
                .filter(book -> book.genre().name().equalsIgnoreCase(query))
                .filter(book -> book.publishedYear() >= minimumYear)
                .toList();
    }

    public Optional<Book> findFirstByAuthorName(String authorName) {
        String query = requireText(authorName, "Author name").toLowerCase();
        return stream()
                .filter(book -> book.author().getName().toLowerCase().contains(query))
                .findFirst();
    }

    public List<Book> sortByTitleThenYearDescending() {
        return findAllSorted(Comparator.comparing(Book::title)
                .thenComparing(Book::publishedYear, Comparator.reverseOrder()));
    }

    public List<Book> sortByYearDescendingThenTitle() {
        return findAllSorted(Comparator.comparing(Book::publishedYear)
                .reversed()
                .thenComparing(Book::title));
    }

    public List<Book> sortByFractionalPublicationYear() {
        return findAllSorted(Comparator.comparingDouble(book -> (double) book.publishedYear()));
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.trim();
    }
}
