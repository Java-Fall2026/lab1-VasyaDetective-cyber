package ua.library;

import ua.library.model.Author;
import ua.library.model.Book;
import ua.library.model.BookLoan;
import ua.library.model.Reader;
import ua.library.util.LibraryUtils;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Author author = Author.of("AUTH-001", "  Taras Shevchenko  ", 1814);
        Book book = Book.of("978-966-123-456-7", "Kobzar", author, " fiction ", 1840);
        
        Reader reader = new Reader("TKT-001", "Ivan Franko", LocalDate.of(2005, 5, 15));
        BookLoan loan = new BookLoan(book, reader, LocalDate.now().minusDays(5), LocalDate.now().plusDays(5), null, " active ");

        System.out.println(author);
        System.out.println(book);
        System.out.println(reader);
        System.out.println(loan);

        try {
            Author.of("AUTH-002", "", 1799);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        try {
            new Reader("INVALID", "Lesya Ukrainka", LocalDate.of(2015, 1, 1));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        try {
            Book.of("123", "Eneida", author, "POETRY", 1842);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        try {
            loan.setStatus("UNKNOWN");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        BookLoan loanCopy = new BookLoan(book, reader, LocalDate.now().minusDays(5), LocalDate.now().plusDays(10), null, "ACTIVE");
        System.out.println(loan == loanCopy);
        System.out.println(loan.equals(loanCopy));
        System.out.println(loan.hashCode() == loanCopy.hashCode());

        BookLoan differentLoan = new BookLoan(book, reader, LocalDate.now(), LocalDate.now().plusDays(14), null, "ACTIVE");
        System.out.println(loan.equals(differentLoan));

        System.out.println(LibraryUtils.loanDays(loan));
        System.out.println(LibraryUtils.overdueDays(loan));

        // ua.library.util.ValidationHelper.checkNotNull(author, "Author");
        // The line above does not compile because ValidationHelper is package-private and cannot be accessed from outside the ua.library.util package.
    }
}