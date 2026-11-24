package ua.library;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.library.model.*;
import ua.library.repository.BookRepository;
import ua.library.repository.ReaderRepository;
import ua.library.repository.SortOrder;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;

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
        Reader first = new Reader("TKT-001", "Ivan Franko", LocalDate.of(2005, 5, 15));
        Reader second = new Reader("TKT-002", "Olena Koval", LocalDate.of(1998, 8, 20));
        Reader third = new Reader("TKT-003", "Ivan Petrenko", LocalDate.of(2001, 3, 10));
        readers.addAll(List.of(first, second, third));
        List<BookLoan> loans = List.of(
                new BookLoan(books.findByTitle("Kobzar").get(0), first, LocalDate.now().minusDays(10), LocalDate.now().plusDays(5), null, LoanStatus.ACTIVE),
                new BookLoan(books.findByTitle("Forest").get(0), second, LocalDate.now().minusDays(20), LocalDate.now().minusDays(5), LocalDate.now().minusDays(3), LoanStatus.RETURNED));
        demonstrateSorting(books, readers);
        demonstrateSearching(books, readers);
        demonstrateStreams(books, loans);
        demonstrateTreeSet(books);
        demonstrateEmptyResults(books);
        log.info("--- Lab 5 demonstration finished ---");
    }

    private static void demonstrateSorting(BookRepository books, ReaderRepository readers) {
        System.out.println("Природний порядок книг: " + books.findAllSorted(Comparator.naturalOrder()));
        System.out.println("ISBN за спаданням: " + books.findAllSortedByIdentity(SortOrder.DESCENDING));
        System.out.println("ISBN за зростанням: " + books.findAllSortedByIdentity(SortOrder.ASCENDING));
        System.out.println("Книги: назва, потім рік за спаданням: " + books.sortByTitleThenYearDescending());
        System.out.println("Книги: рік за спаданням, потім назва: " + books.sortByYearDescendingThenTitle());
        System.out.println("Природний порядок читачів: " + readers.findAllSorted(Comparator.naturalOrder()));
        System.out.println("Читачі за ім'ям і датою: " + readers.sortByNameThenBirthDate());
        System.out.println("Читачі за датою народження: " + readers.sortByBirthDateDescendingThenName());
    }

    private static void demonstrateSearching(BookRepository books, ReaderRepository readers) {
        System.out.println("Пошук книг за назвою: " + books.findByTitle("song"));
        System.out.println("Книги 1840–1900 років: " + books.findByPublishedYearBetween(1840, 1900));
        System.out.println("Фікція після 1850 року: " + books.findByGenreAndMinimumYear("FICTION", 1850));
        System.out.println("Перша книга Шевченка: " + books.findFirstByAuthorName("Shevchenko").orElseThrow());
        System.out.println("Пошук читача за ім'ям: " + readers.findByName("Ivan"));
        System.out.println("Читачі 1998–2005 років: " + readers.findByBirthYearBetween(1998, 2005));
        System.out.println("Ivan, народжені до 2003: " + readers.findByNameAndBornBefore("Ivan", 2003));
        System.out.println("Перший читач за префіксом: " + readers.findFirstByTicketPrefix("TKT").orElseThrow());
    }

    private static void demonstrateStreams(BookRepository books, List<BookLoan> loans) {
        List<String> names = books.stream().map(book -> book.author().getName()).distinct().sorted().limit(2).toList();
        System.out.println("Два перші унікальні автори: " + names);
        Map<Genre, Long> byGenre = books.stream().collect(Collectors.groupingBy(Book::genre, Collectors.counting()));
        System.out.println("Кількість книг за жанрами: " + byGenre);
        Map<String, Long> byReader = loans.stream().collect(Collectors.groupingBy(loan -> loan.getReader().fullName(), Collectors.counting()));
        System.out.println("Кількість позик за читачами: " + byReader);
        double average = books.stream().mapToInt(Book::publishedYear).summaryStatistics().getAverage();
        int total = books.stream().map(Book::publishedYear).reduce(0, Integer::sum);
        System.out.println("Середній рік: " + average + "; сума років: " + total);
        List<String> loanTitles = loans.stream().flatMap(loan -> java.util.stream.Stream.of(loan.getBook())).map(Book::title).toList();
        System.out.println("Книги з позик через flatMap: " + loanTitles);
        var consumed = books.stream().filter(book -> book.publishedYear() > 1850);
        System.out.println("Кількість книг після 1850: " + consumed.count());
        try { consumed.findFirst(); } catch (IllegalStateException e) {
            System.out.println("Повторне використання Stream: " + e.getClass().getSimpleName());
        }
    }

    private static void demonstrateTreeSet(BookRepository books) {
        TreeSet<Book> byTitle = new TreeSet<>(Comparator.comparing(Book::title));
        byTitle.addAll(books.findAll());
        Book duplicateTitle = new Book("978-000-000-000-0", "Kobzar", books.findAll().get(0).author(), Genre.FICTION, 2000);
        boolean added = byTitle.add(duplicateTitle);
        System.out.println("TreeSet за назвою: " + byTitle);
        System.out.println("Інша книга з тією самою назвою додана: " + added + "; розмір: " + byTitle.size());
    }

    private static void demonstrateEmptyResults(BookRepository books) {
        Optional<Book> absent = books.findFirstByAuthorName("Unknown Author");
        System.out.println("Порожній Optional: " + absent);
        double emptyAverage = books.findByPublishedYearBetween(3000, 4000).stream().mapToInt(Book::publishedYear).summaryStatistics().getAverage();
        System.out.println("Середнє для порожньої колекції: " + emptyAverage);
    }
}
