package ua.library.model;

import ua.library.util.LibraryUtils;

import java.util.Objects;

public record Book(String isbn, String title, Author author, Genre genre, int publishedYear)
        implements Comparable<Book> {
    public Book {
        isbn = LibraryUtils.processIsbn(isbn);
        title = LibraryUtils.processString(title, "Book title");
        LibraryUtils.requireNotNull(author, "Author");
        LibraryUtils.requireNotNull(genre, "Genre");
        LibraryUtils.validatePublishYear(publishedYear, author.getBirthYear());
    }

    @Override
    public int compareTo(Book other) {
        return isbn.compareTo(other.isbn);
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Book other && isbn.equals(other.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }
    
}
