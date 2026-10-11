package ua.library;

import ua.library.model.Author;
import ua.library.model.Book;
import ua.library.model.BookLoan;
import ua.library.model.Genre;
import ua.library.model.LoanStatus;
import ua.library.model.Reader;
import ua.library.util.LibraryUtils;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("--- 1. Record creation and components ---");
        Author author = new Author("AUTH-001", "Taras Shevchenko", 1814);
        System.out.println("Author created: " + author.name() + ", Born: " + author.birthYear());

        System.out.println("\n--- 2. Enum: values(), custom field, valueOf ---");
        for (Genre g : Genre.values()) {
            System.out.println("Genre: " + g + ", Max loan days: " + g.getMaxLoanDays());
        }
        try {
            LoanStatus.valueOf("INVALID_STATUS");
        } catch (IllegalArgumentException e) {
            System.out.println("Expected error for invalid enum: " + e.getMessage());
        }

        System.out.println("\n--- 3. Switch Expressions ---");
        System.out.println("Genre category for FICTION: " + LibraryUtils.getGenreCategory(Genre.FICTION));
        System.out.println("Status description for OVERDUE: " + LibraryUtils.getStatusDescription(LoanStatus.OVERDUE));

        System.out.println("\n--- 4. Record Validation ---");
        try {
            new Author("AUTH-002", "Valid Author", 1799);
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        }

        System.out.println("\n--- 5. Record equals() and hashCode() ---");
        Book book1 = new Book("978-966-123-456-7", "Kobzar", author, Genre.FICTION, 1840);
        Book book2 = new Book("978-966-123-456-7", "Kobzar", author, Genre.FICTION, 1840);
        System.out.println("book1 == book2: " + (book1 == book2));
        System.out.println("book1.equals(book2): " + book1.equals(book2));
        System.out.println("book1.hashCode() == book2.hashCode(): " + (book1.hashCode() == book2.hashCode()));

        System.out.println("\n--- 6. Record toString() ---");
        System.out.println(book1);

        Reader reader = new Reader("TKT-001", "Ivan Franko", LocalDate.of(2005, 5, 15));
        BookLoan loan = new BookLoan(book1, reader, LocalDate.now().minusDays(5), LocalDate.now().plusDays(5), null, LoanStatus.ACTIVE);
        System.out.println("\nFinal BookLoan state:\n" + loan);
    }
}