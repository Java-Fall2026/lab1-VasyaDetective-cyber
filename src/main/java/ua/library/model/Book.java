package ua.library.model;

import ua.common.BaseEntity;
import ua.library.util.LibraryUtils;

import java.util.Objects;

public class Book extends BaseEntity {

    private final String isbn;
    private final String title;
    private final Author author;
    private final int publishYear;

    private Book(String isbn, String title, Author author, int publishYear) {
        super();
        this.isbn = LibraryUtils.processIsbn(isbn);
        this.title = LibraryUtils.processString(title, "назва книги");
        LibraryUtils.requireNotNull(author, "автор");
        LibraryUtils.validatePublishYear(publishYear, author.getBirthDate().getYear());
        
        this.author = author;
        this.publishYear = publishYear;
    }

    public static Book of(String isbn, String title, Author author, int publishYear) {
        return new Book(isbn, title, author, publishYear);
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public Author getAuthor() {
        return author;
    }

    public int getPublishYear() {
        return publishYear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

    @Override
    public String toString() {
        return "Book{" +
                "isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", author=" + author.getFullName() +
                ", publishYear=" + publishYear +
                '}';
    }
}