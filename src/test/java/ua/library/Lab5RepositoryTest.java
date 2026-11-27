package ua.library;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.library.model.Author;
import ua.library.model.Book;
import ua.library.model.Genre;
import ua.library.model.Reader;
import ua.library.repository.BookRepository;
import ua.library.repository.ReaderRepository;
import ua.library.repository.SortOrder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Lab 5 repository, sorting and Stream API tests")
class Lab5RepositoryTest {
    private final Author author = Author.of("AUTH-001", "Test Author", 1970);

    @Test
    @DisplayName("Natural book order sorts by ISBN")
    void naturalBookOrderSortsByIsbn() {
        Book first = book("978-000-000-000-1", "First", 2000);
        Book second = book("978-000-000-000-2", "Second", 2010);
        List<Book> books = new ArrayList<>(List.of(second, first));

        Collections.sort(books);

        assertEquals(List.of(first, second), books);
    }

    @Test
    @DisplayName("Natural reader order sorts by ticket number")
    void naturalReaderOrderSortsByTicketNumber() {
        Reader first = reader("TKT-001", "First Reader", 2000);
        Reader second = reader("TKT-002", "Second Reader", 2001);
        List<Reader> readers = new ArrayList<>(List.of(second, first));

        Collections.sort(readers);

        assertEquals(List.of(first, second), readers);
    }

    @Test
    @DisplayName("Multilevel comparator uses the second key")
    void multilevelComparatorUsesSecondKey() {
        Book older = book("978-000-000-000-1", "Same title", 2000);
        Book newer = book("978-000-000-000-2", "Same title", 2010);
        BookRepository repository = new BookRepository();
        repository.addAll(List.of(older, newer));

        List<Book> result = repository.sortByTitleThenYearDescending();

        assertEquals(newer, result.get(0));
        assertEquals(older, result.get(1));
    }

    @Test
    @DisplayName("Lambda comparator sorts by year without numeric subtraction")
    void lambdaComparatorSortsByYear() {
        Book older = book("978-000-000-000-1", "Older", 2000);
        Book newer = book("978-000-000-000-2", "Newer", 2010);
        BookRepository repository = new BookRepository();
        repository.addAll(List.of(newer, older));

        List<Book> result = repository.sortByYearAscendingWithLambda();

        assertEquals(List.of(older, newer), result);
    }

    @Test
    @DisplayName("Identity sorting in ascending and descending order is reversible")
    void identitySortingIsReversible() {
        Book first = book("978-000-000-000-1", "First", 2000);
        Book second = book("978-000-000-000-2", "Second", 2010);
        BookRepository repository = new BookRepository();
        repository.addAll(List.of(first, second));

        List<Book> ascending = repository.findAllSortedByIdentity(SortOrder.ASC);
        List<Book> descending = repository.findAllSortedByIdentity(SortOrder.DESC);
        List<Book> reversedAscending = new ArrayList<>(ascending);
        Collections.reverse(reversedAscending);

        assertEquals(reversedAscending, descending);
    }

    @Test
    @DisplayName("Double comparison preserves differences smaller than one")
    void doubleComparisonPreservesSubUnitDifference() {
        Comparator<Double> comparator = Double::compare;

        assertTrue(comparator.compare(10.10, 10.90) < 0);
        assertNotEquals(0, comparator.compare(10.10, 10.90));
    }

    @Test
    @DisplayName("Title search returns matching books")
    void titleSearchReturnsMatchingBooks() {
        BookRepository repository = repositoryWithBooks();

        List<Book> result = repository.findByTitle("alpha");

        assertEquals(1, result.size());
        assertEquals("Alpha", result.get(0).title());
    }

    @Test
    @DisplayName("Year range search includes boundaries and excludes outside values")
    void yearRangeSearchUsesInclusiveBoundaries() {
        BookRepository repository = repositoryWithBooks();

        List<Book> result = repository.findByPublishedYearBetween(2000, 2010);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(book -> book.publishedYear() >= 2000
                && book.publishedYear() <= 2010));
    }

    @Test
    @DisplayName("Two-condition search excludes objects satisfying only one condition")
    void twoConditionSearchRequiresBothConditions() {
        BookRepository repository = repositoryWithBooks();

        List<Book> result = repository.findByGenreAndMinimumYear("FICTION", 2005);

        assertEquals(1, result.size());
        assertEquals("Beta", result.get(0).title());
    }

    @Test
    @DisplayName("Single-object search returns Optional.empty when there is no match")
    void singleObjectSearchReturnsEmptyOptional() {
        BookRepository repository = repositoryWithBooks();

        assertTrue(repository.findFirstByAuthorName("Unknown").isEmpty());
    }

    @Test
    @DisplayName("Grouping returns expected groups and group sizes")
    void groupingReturnsExpectedGroups() {
        BookRepository repository = repositoryWithBooks();

        var grouped = repository.groupByGenre();

        assertEquals(2, grouped.size());
        assertEquals(2, grouped.get(Genre.FICTION).size());
    }

    @Test
    @DisplayName("Empty aggregation returns a meaningful zero")
    void emptyAggregationReturnsZero() {
        BookRepository repository = new BookRepository();

        assertEquals(0, repository.sumPublishedYears());
    }

    @Test
    @DisplayName("TreeSet drops a different object when its comparator returns zero")
    void treeSetUsesComparatorEquality() {
        Book first = book("978-000-000-000-1", "Same title", 2000);
        Book second = book("978-000-000-000-2", "Same title", 2010);
        TreeSet<Book> books = new TreeSet<>(Comparator.comparing(Book::title));

        books.add(first);
        boolean addedSecond = books.add(second);

        assertFalse(addedSecond);
        assertEquals(1, books.size());
        assertNotEquals(first, second);
    }

    @Test
    @DisplayName("A stream cannot be consumed twice")
    void streamCannotBeConsumedTwice() {
        BookRepository repository = repositoryWithBooks();
        var stream = repository.stream();

        assertEquals(3, stream.count());
        assertThrows(IllegalStateException.class, stream::count);
    }

    @Test
    @DisplayName("Reader searches apply text, range and two-condition criteria")
    void readerSearchesApplyAllCriteria() {
        ReaderRepository repository = new ReaderRepository();
        Reader first = reader("TKT-001", "Ivan First", 2000);
        Reader second = reader("TKT-002", "Ivan Second", 2010);
        Reader third = reader("TKT-003", "Olena Third", 2005);
        repository.addAll(List.of(first, second, third));

        assertEquals(2, repository.findByName("Ivan").size());
        assertEquals(2, repository.findByBirthYearBetween(2000, 2005).size());
        assertEquals(List.of(first), repository.findByNameAndBornBefore("Ivan", 2005));
        assertSame(first, repository.findFirstByTicketPrefix("TKT").orElseThrow());
    }

    private BookRepository repositoryWithBooks() {
        BookRepository repository = new BookRepository();
        repository.addAll(List.of(
                book("978-000-000-000-1", "Alpha", 2000),
                book("978-000-000-000-2", "Beta", 2010),
                new Book("978-000-000-000-3", "Gamma", author, Genre.BIOGRAPHY, 1990)));
        return repository;
    }

    private Book book(String isbn, String title, int year) {
        return new Book(isbn, title, author, Genre.FICTION, year);
    }

    private Reader reader(String ticket, String name, int birthYear) {
        return new Reader(ticket, name, LocalDate.of(birthYear, 1, 1));
    }
}
