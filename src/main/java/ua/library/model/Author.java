package ua.library.model;

import ua.common.BaseEntity;
import ua.library.util.LibraryUtils;

import java.time.LocalDate;
import java.util.Objects;

public class Author extends BaseEntity {

    private final String fullName;
    private final LocalDate birthDate;

    private Author(String fullName, LocalDate birthDate) {
        super();
        this.fullName = LibraryUtils.processString(fullName, "ПІБ автора");
        LibraryUtils.requireNotNull(birthDate, "дата народження");
        LibraryUtils.validateNotFuture(birthDate);
        this.birthDate = birthDate;
    }

    public static Author of(String fullName, LocalDate birthDate) {
        return new Author(fullName, birthDate);
    }

    public String getFullName() {
        return fullName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(fullName, author.fullName) &&
               Objects.equals(birthDate, author.birthDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, birthDate);
    }

    @Override
    public String toString() {
        return "Author{" +
                "fullName='" + fullName + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }
}