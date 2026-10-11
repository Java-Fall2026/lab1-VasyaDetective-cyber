package ua.library.model;

import ua.library.util.LibraryUtils;

public record Author(String authorCode, String name, int birthYear) {
    public Author {
        authorCode = LibraryUtils.processString(authorCode, "Author code");
        name = LibraryUtils.processString(name, "Author name");
        LibraryUtils.validateAuthorYear(birthYear);
    }
}