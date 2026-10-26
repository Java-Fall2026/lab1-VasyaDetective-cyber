package ua.library;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ua.library.exception.InvalidDataException;
import ua.library.model.Author;
import ua.library.model.Book;
import ua.library.model.BookLoan;
import ua.library.model.Reader;
import ua.library.util.CsvLoader;
import ua.library.util.LibraryUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {

    @Test
    void testValidObjectCreation() {
        Author author = Author.of("AUTH-001", "Ivan", 1990);
        assertNotNull(author);
        assertEquals("Ivan", author.getName());
    }

    @Test
    void testInvalidObjectCreationThrowsException() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            Author.of("AUTH-001", "", 1990);
        });
        assertTrue(e.getMessage().contains("must not be blank"));
    }

    @Test
    void testLoanDaysCalculation() {
        Author author = Author.of("AUTH-001", "Ivan", 1990);
        Book book = Book.of("978-966-123-456-7", "Book", author, "FICTION", 2000);
        Reader reader = new Reader("TKT-001", "Petro", LocalDate.of(2000, 1, 1));
        BookLoan loan = new BookLoan(book, reader, LocalDate.now().minusDays(10), LocalDate.now().plusDays(5), null, "ACTIVE");

        assertEquals(15, LibraryUtils.loanDays(loan));
    }

    @Test
    void testOverdueDaysCalculation() {
        Author author = Author.of("AUTH-001", "Ivan", 1990);
        Book book = Book.of("978-966-123-456-7", "Book", author, "FICTION", 2000);
        Reader reader = new Reader("TKT-001", "Petro", LocalDate.of(2000, 1, 1));
        BookLoan loan = new BookLoan(book, reader, LocalDate.now().minusDays(10), LocalDate.now().minusDays(5), LocalDate.now().minusDays(2), "OVERDUE");

        assertEquals(3, LibraryUtils.overdueDays(loan));
    }

    @Test
    void testCsvLoaderWithValidAndInvalidRows(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("test.csv");
        Files.writeString(file, "code,name,year\nAUTH-001,Valid Name,1990\nAUTH-002,,1990\nAUTH-003,Invalid Year,1000");

        List<Author> authors = CsvLoader.loadAuthors(file.toString());
        assertEquals(1, authors.size());
        assertEquals("Valid Name", authors.get(0).getName());
    }

    @Test
    void testInvalidDataExceptionMessageAndCause() {
        Throwable cause = new IllegalArgumentException("Bad year");
        InvalidDataException e = new InvalidDataException("Row 2: error", cause);
        assertEquals("Row 2: error", e.getMessage());
        assertEquals(cause, e.getCause());
    }
}