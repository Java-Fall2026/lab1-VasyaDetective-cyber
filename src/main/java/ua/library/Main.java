package ua.library;

import ua.library.model.Author;
import ua.library.model.Book;
import ua.library.model.BookLoan;
import ua.library.model.Reader;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Author author = Author.of("Тарас Шевченко", LocalDate.of(1814, 3, 9));
        Book book = Book.of("978-966-123-456-7", "Кобзар", author, 1840);
        Reader reader = Reader.of("TKT-001", "Іваненко Іван Іванович", LocalDate.of(2005, 5, 15));
        BookLoan loan = BookLoan.of(book, reader, LocalDate.now(), null);

        System.out.println(author);
        System.out.println(book);
        System.out.println(reader);
        System.out.println(loan);
    }
}