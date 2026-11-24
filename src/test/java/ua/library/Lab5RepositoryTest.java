package ua.library;

import org.junit.jupiter.api.Test;
import ua.library.model.Author;
import ua.library.model.Book;
import ua.library.model.Genre;
import ua.library.model.Reader;
import ua.library.repository.BookRepository;
import ua.library.repository.ReaderRepository;
import ua.library.repository.SortOrder;

import java.time.LocalDate;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class Lab5RepositoryTest {
    private final Author author = Author.of("AUTH-001", "Test Author", 1970);
    private final Book firstBook = new Book("978-000-000-000-1", "Alpha", author, Genre.FICTION, 2000);
    private final Book secondBook = new Book("978-000-000-000-2", "Beta", author, Genre.FICTION, 2010);

    @Test
    void supportsNaturalAndIdentitySortingInBothDirections() {
        BookRepository repository = new BookRepository();
        repository.addAll(java.util.List.of(secondBook, firstBook));
        assertEquals(firstBook, repository.findAllSorted(Comparator.naturalOrder()).get(0));
        assertEquals(firstBook, repository.findAllSortedByIdentity(SortOrder.ASCENDING).get(0));
        assertEquals(secondBook, repository.findAllSortedByIdentity(SortOrder.DESCENDING).get(0));
    }

    @Test
    void supportsDomainSearchAndEmptyOptional() {
        BookRepository repository = new BookRepository();
        repository.addAll(java.util.List.of(firstBook, secondBook));
        assertEquals(1, repository.findByTitle("alpha").size());
        assertEquals(2, repository.findByPublishedYearBetween(2000, 2010).size());
        assertEquals(1, repository.findByGenreAndMinimumYear("fiction", 2005).size());
        assertTrue(repository.findFirstByAuthorName("missing").isEmpty());
    }

    @Test
    void streamIsSingleUseAndFindAllIsIndependentCopy() {
        ReaderRepository repository = new ReaderRepository();
        repository.add(new Reader("TKT-001", "Reader", LocalDate.of(2000, 1, 1)));
        assertEquals(1, repository.findAll().size());
        var stream = repository.stream();
        assertEquals(1, stream.count());
        assertThrows(IllegalStateException.class, stream::count);
    }
}
