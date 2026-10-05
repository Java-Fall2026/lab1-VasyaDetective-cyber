package ua.library.model;

import ua.common.BaseEntity;
import ua.library.util.LibraryUtils;
import java.time.LocalDate;
import java.util.Objects;

public class BookLoan extends BaseEntity {
    private final Book book;
    private final Reader reader;
    private final LocalDate issueDate;
    private final LocalDate dueDate;
    private LocalDate returnDate; 
    private String status;        

    public BookLoan(Book book, Reader reader, LocalDate issueDate, LocalDate dueDate, LocalDate returnDate, String status) {
        super();
        LibraryUtils.requireNotNull(book, "Book");
        LibraryUtils.requireNotNull(reader, "Reader");
        this.book = book;
        this.reader = reader;
        
        LibraryUtils.validateNotFuture(issueDate, "Issue date");
        this.issueDate = issueDate;
        
        LibraryUtils.requireNotNull(dueDate, "Due date");
        LibraryUtils.validateStrictlyAfter(issueDate, dueDate, "Due date");
        this.dueDate = dueDate;
        
        setReturnDate(returnDate);
        setStatus(status);
    }

    public Book getBook() { return book; }
    public Reader getReader() { return reader; }
    public LocalDate getIssueDate() { return issueDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public String getStatus() { return status; }

 
    public final void setReturnDate(LocalDate returnDate) {
        if (returnDate != null) {
            LibraryUtils.validateAfterOrEqual(this.issueDate, returnDate, "Return date");
        }
        this.returnDate = returnDate;
    }

    public final void setStatus(String status) {
        this.status = LibraryUtils.processStatus(status);
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
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                ", status='" + status + '\'' +
                ", createdAt=" + getCreatedAt() +
                '}';
    }
}