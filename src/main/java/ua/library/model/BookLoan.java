package ua.library.model;

import ua.common.BaseEntity;
import ua.library.util.LibraryUtils;

import java.time.LocalDate;
import java.util.Objects;

public class BookLoan extends BaseEntity {

    private final Book book;
    private final Reader reader;
    private final LocalDate issueDate;
    private final LocalDate returnDate;

    private BookLoan(Book book, Reader reader, LocalDate issueDate, LocalDate returnDate) {
        super();
        LibraryUtils.requireNotNull(book, "книга");
        LibraryUtils.requireNotNull(reader, "читач");
        LibraryUtils.requireNotNull(issueDate, "дата видачі");
        LibraryUtils.validateNotFuture(issueDate);
        LibraryUtils.validateAfterOrEqual(issueDate, returnDate);

        this.book = book;
        this.reader = reader;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
    }

    public static BookLoan of(Book book, Reader reader, LocalDate issueDate, LocalDate returnDate) {
        return new BookLoan(book, reader, issueDate, returnDate);
    }

    public Book getBook() {
        return book;
    }

    public Reader getReader() {
        return reader;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookLoan bookLoan = (BookLoan) o;
        return Objects.equals(book, bookLoan.book) &&
               Objects.equals(reader, bookLoan.reader) &&
               Objects.equals(issueDate, bookLoan.issueDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(book, reader, issueDate);
    }

    @Override
    public String toString() {
        return "BookLoan{" +
                "book=" + book.getTitle() +
                ", reader=" + reader.getFullName() +
                ", issueDate=" + issueDate +
                ", returnDate=" + returnDate +
                '}';
    }
}