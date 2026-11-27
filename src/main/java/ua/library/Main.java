package ua.library;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.library.model.Author;
import ua.library.model.Book;
import ua.library.model.BookLoan;
import ua.library.model.Genre;
import ua.library.model.LoanStatus;
import ua.library.model.Reader;
import ua.library.repository.BookRepository;
import ua.library.repository.ReaderRepository;
import ua.library.repository.SortOrder;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        log.info("--- Starting Lab 5 demonstration ---");

        Author shevchenko = Author.of("AUTH-001", "Taras Shevchenko", 1814);
        Author franko = Author.of("AUTH-002", "Ivan Franko", 1856);
        Author lesya = Author.of("AUTH-003", "Lesya Ukrainka", 1871);

        BookRepository books = new BookRepository();
        books.addAll(List.of(
                new Book("978-966-123-456-7", "Kobzar", shevchenko, Genre.FICTION, 1840),
                new Book("978-966-222-333-4", "Zahar Berkut", franko, Genre.FICTION, 1883),
                new Book("978-966-555-666-7", "Forest Song", lesya, Genre.FICTION, 1911),
                new Book("978-966-777-888-9", "Selected Poems", shevchenko, Genre.BIOGRAPHY, 1841)));

        ReaderRepository readers = new ReaderRepository();
        Reader firstReader = new Reader("TKT-001", "Ivan Franko", LocalDate.of(2005, 5, 15));
        Reader secondReader = new Reader("TKT-002", "Olena Koval", LocalDate.of(1998, 8, 20));
        Reader thirdReader = new Reader("TKT-003", "Ivan Petrenko", LocalDate.of(2001, 3, 10));
        readers.addAll(List.of(firstReader, secondReader, thirdReader));

        Book kobzar = books.findByTitle("Kobzar").stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Kobzar was not created"));
        Book forestSong = books.findByTitle("Forest Song").stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Forest Song was not created"));
        List<BookLoan> loans = List.of(
                new BookLoan(kobzar, firstReader, LocalDate.now().minusDays(10),
                        LocalDate.now().plusDays(5), null, LoanStatus.ACTIVE),
                new BookLoan(forestSong, secondReader, LocalDate.now().minusDays(20),
                        LocalDate.now().minusDays(5), LocalDate.now().minusDays(3), LoanStatus.RETURNED));

        demonstrateSorting(books, readers);
        demonstrateSearching(books, readers);
        demonstrateGroupingAndAggregation(books);
        demonstrateTreeSet(books);
        demonstrateOptionalEmpty(books);
        demonstrateConsumedStream(books);

        log.info("Created {} loan demonstrations", loans.size());
        log.info("--- Lab 5 demonstration finished ---");
    }

    private static void demonstrateSorting(BookRepository books, ReaderRepository readers) {
        System.out.println("Природний порядок книг: " + books.sortNaturally());
        System.out.println("ISBN за зростанням: " + books.findAllSortedByIdentity(SortOrder.ASC));
        System.out.println("ISBN за спаданням: " + books.findAllSortedByIdentity(SortOrder.DESC));
        System.out.println("Книги за роком через lambda-компаратор: "
                + books.sortByYearAscendingWithLambda());
        System.out.println("Книги за назвою і роком: " + books.sortByTitleThenYearDescending());
        System.out.println("Книги за роком і назвою: " + books.sortByYearDescendingThenTitle());

        System.out.println("Природний порядок читачів: " + readers.sortNaturally());
        System.out.println("Читачі за ім'ям і датою: " + readers.sortByNameThenBirthDate());
        System.out.println("Читачі за датою і ім'ям: " + readers.sortByBirthDateDescendingThenName());
    }

    private static void demonstrateSearching(BookRepository books, ReaderRepository readers) {
        System.out.println("Книги за назвою: " + books.findByTitle("song"));
        System.out.println("Книги 1840–1900 років: " + books.findByPublishedYearBetween(1840, 1900));
        System.out.println("Фікція після 1850 року: " + books.findByGenreAndMinimumYear("FICTION", 1850));
        System.out.println("Унікальні автори знайдених книг: "
                + books.findDistinctAuthorNamesByTitle("o", 3));
        System.out.println("Дві найновіші фікційні книги: " + books.findNewestByGenre("FICTION", 2));
        System.out.println("Перша книга Шевченка: "
                + books.findFirstByAuthorName("Shevchenko").map(Book::title).orElse("Книгу не знайдено"));

        System.out.println("Читачі за ім'ям: " + readers.findByName("Ivan"));
        System.out.println("Читачі 1998–2005 років: " + readers.findByBirthYearBetween(1998, 2005));
        System.out.println("Ivan, народжені до 2003 року: "
                + readers.findByNameAndBornBefore("Ivan", 2003));
        System.out.println("Унікальні роки народження: " + readers.findDistinctBirthYears(3));
        System.out.println("Два наймолодші читачі після 1990 року: " + readers.findBornAfter(1990, 2));
        System.out.println("Перший читач за префіксом: "
                + readers.findFirstByTicketPrefix("TKT").map(Reader::fullName).orElse("Читача не знайдено"));
    }

    private static void demonstrateGroupingAndAggregation(BookRepository books) {
        System.out.println("Книги за жанрами: " + books.groupByGenre());
        System.out.println("Кількість книг за жанрами: " + books.countByGenre());
        System.out.println("Сума років видання: " + books.sumPublishedYears());

        BookRepository emptyBooks = new BookRepository();
        System.out.println("Агрегація порожнього репозиторію: " + emptyBooks.sumPublishedYears());
    }

    private static void demonstrateTreeSet(BookRepository books) {
        TreeSet<Book> booksByTitle = new TreeSet<>(Comparator.comparing(Book::title));
        booksByTitle.addAll(books.findAll());
        Book sameTitleDifferentBook = new Book("978-000-000-000-0", "Kobzar",
                books.findAll().get(0).author(), Genre.FICTION, 2000);
        boolean added = booksByTitle.add(sameTitleDifferentBook);
        System.out.println("TreeSet за назвою: " + booksByTitle);
        System.out.println("Інша книга з тією самою назвою додана: " + added
                + "; розмір: " + booksByTitle.size());
    }

    private static void demonstrateOptionalEmpty(BookRepository books) {
        String message = books.findFirstByAuthorName("Unknown Author")
                .map(book -> "Знайдено книгу: " + book.title())
                .orElse("Книгу невідомого автора не знайдено");
        System.out.println(message);
    }

    private static void demonstrateConsumedStream(BookRepository books) {
        var consumedStream = books.stream().filter(book -> book.publishedYear() > 1850);
        System.out.println("Кількість книг після 1850 року: " + consumedStream.count());
        try {
            consumedStream.findFirst();
        } catch (IllegalStateException exception) {
            log.warn("Attempted to reuse an already consumed stream", exception);
            System.out.println("Повторне використання Stream: " + exception.getClass().getSimpleName());
        }
    }
}
