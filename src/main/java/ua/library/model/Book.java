package ua.library.model;

import ua.library.util.LibraryUtils;

public record Book(String isbn, String title, Author author, Genre genre, int publishedYear) {
    public Book {
        isbn = LibraryUtils.processIsbn(isbn);
        title = LibraryUtils.processString(title, "Book title");
        LibraryUtils.requireNotNull(author, "Author");
        LibraryUtils.requireNotNull(genre, "Genre");
        LibraryUtils.validatePublishYear(publishedYear, author.birthYear());
    }
}