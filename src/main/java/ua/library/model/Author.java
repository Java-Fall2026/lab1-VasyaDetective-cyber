package ua.library.model;

import ua.common.BaseEntity;
import ua.library.util.LibraryUtils;
import java.util.Objects;

public class Author extends BaseEntity {
    private final String authorCode;
    private final String name;
    private final int birthYear;

    private Author(String authorCode, String name, int birthYear) {
        super();
        this.authorCode = LibraryUtils.processString(authorCode, "Author code");
        this.name = LibraryUtils.processString(name, "Author name");
        LibraryUtils.validateAuthorYear(birthYear);
        this.birthYear = birthYear;
    }

    public static Author of(String authorCode, String name, int birthYear) {
        return new Author(authorCode, name, birthYear);
    }

    public String getAuthorCode() { return authorCode; }
    public String getName() { return name; }
    public int getBirthYear() { return birthYear; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(authorCode, author.authorCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorCode);
    }

    @Override
    public String toString() {
        return "Author{" +
                "authorCode='" + authorCode + '\'' +
                ", name='" + name + '\'' +
                ", birthYear=" + birthYear +
                ", createdAt=" + getCreatedAt() +
                '}';
    }
}