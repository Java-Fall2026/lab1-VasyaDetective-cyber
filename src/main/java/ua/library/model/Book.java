package ua.library.model;

import ua.common.BaseEntity;
import ua.library.util.LibraryUtils;
import java.util.Objects;

public class Book extends BaseEntity {
    private final String isbn;
    private final String title;
    private final Author author;
    private final String genre;
    private final int publishedYear;

    private Book(String isbn, String title, Author author, String genre, int publishedYear) {
        super();
        this.isbn = LibraryUtils.processIsbn(isbn);
        this.title = LibraryUtils.processString(title, "Book title");
        LibraryUtils.requireNotNull(author, "Author");
        this.author = author;
        
        this.genre = LibraryUtils.processGenre(genre);
        
        LibraryUtils.validatePublishYear(publishedYear, author.getBirthYear());
        this.publishedYear = publishedYear;
    }

    public static Book of(String isbn, String title, Author author, String genre, int publishedYear) {
        return new Book(isbn, title, author, genre, publishedYear);
    }

    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public Author getAuthor() { return author; }
    public String getGenre() { return genre; }
    public int getPublishedYear() { return publishedYear; }

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
                ", author=" + author.getName() +
                ", genre='" + genre + '\'' +
                ", publishedYear=" + publishedYear +
                ", createdAt=" + getCreatedAt() +
                '}';
    }
}