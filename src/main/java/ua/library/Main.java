package ua.library;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.library.model.Author;
import ua.library.model.Book;
import ua.library.model.BookLoan;
import ua.library.model.Genre;
import ua.library.model.LoanStatus;
import ua.library.model.Reader;
import ua.library.exception.DuplicateIdentityException;
import ua.library.repository.GenericRepository;
import ua.library.util.CsvLoader;
import java.time.LocalDate;
import java.util.List;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        log.info("--- Starting Lab 4 demonstration ---");

        Author author = Author.of("AUTH-001", "Taras Shevchenko", 1814);
        Book book = new Book("978-966-123-456-7", "Kobzar", author, Genre.FICTION, 1840);
        Reader reader = new Reader("TKT-001", "Ivan Franko", LocalDate.of(2005, 5, 15));
        BookLoan loan = new BookLoan(book, reader, LocalDate.now().minusDays(5),
                LocalDate.now().plusDays(5), null, LoanStatus.ACTIVE);

        GenericRepository<Book> books = new GenericRepository<>("Books", Book::isbn);
        GenericRepository<Reader> readers = new GenericRepository<>("Readers", Reader::readerTicketNumber);
        GenericRepository<BookLoan> loans = new GenericRepository<>("Book loans",
                item -> item.getBook().isbn() + ":" + item.getReader().readerTicketNumber() + ":" + item.getIssueDate());

        books.add(book);
        readers.add(reader);
        loans.add(loan);
        System.out.println("Знайдено книгу: " + books.findByIdentity(book.isbn()).orElseThrow());
        System.out.println("Відсутній читач: " + readers.findByIdentity("TKT-999").isEmpty());

        List<Author> authors = CsvLoader.loadAuthors("data/authors.csv");
        GenericRepository<Author> authorsRepository = new GenericRepository<>("Authors", Author::getAuthorCode);
        for (Author csvAuthor : authors) {
            try {
                authorsRepository.add(csvAuthor);
            } catch (DuplicateIdentityException e) {
                System.out.println("Перехоплено дубліката: " + e.getMessage());
            }
        }
        System.out.println("Кількість авторів: " + authorsRepository.size());
        System.out.println("Дублікат через addIfAbsent: " + authorsRepository.addIfAbsent(author));
        System.out.println("Видалення наявного елемента: " + authorsRepository.removeByIdentity("AUTH-004"));
        System.out.println("Видалення відсутнього елемента: " + authorsRepository.removeByIdentity("AUTH-999"));

        log.info("--- Lab 4 demonstration finished ---");
    }
}
