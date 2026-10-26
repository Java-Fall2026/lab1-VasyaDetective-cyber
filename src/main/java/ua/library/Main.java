package ua.library;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.library.model.Author;
import ua.library.model.Book;
import ua.library.model.BookLoan;
import ua.library.model.Reader;
import ua.library.util.CsvLoader;
import java.time.LocalDate;
import java.util.List;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        log.info("--- Starting Lab 3 demonstration ---");

        List<Author> authors = CsvLoader.loadAuthors("data/authors.csv");
        System.out.println(authors);

        CsvLoader.loadAuthors("data/missing_file.csv");

        Author author = Author.of("AUTH-001", "Taras Shevchenko", 1814);
        Book book = Book.of("978-966-123-456-7", "Kobzar", author, "fiction", 1840);
        Reader reader = new Reader("TKT-001", "Ivan Franko", LocalDate.of(2005, 5, 15));
        BookLoan loan = new BookLoan(book, reader, LocalDate.now().minusDays(5), LocalDate.now().plusDays(5), null,
                "active");

        try {
            loan.setStatus("UNKNOWN");
        } catch (IllegalArgumentException e) {
            log.warn("Domain rule violation caught in setter: {}", e.getMessage());
        }

        log.info("--- Lab 3 demonstration finished ---");
    }
}

// ua.library.util.ValidationHelper.checkNotNull(author, "Author");
// The line above does not compile because ValidationHelper is package-private
// and cannot be accessed from outside the ua.library.util package.
