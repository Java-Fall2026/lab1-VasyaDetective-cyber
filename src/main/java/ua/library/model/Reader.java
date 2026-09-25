package ua.library.model;

import ua.common.BaseEntity;
import ua.library.util.LibraryUtils;

import java.time.LocalDate;
import java.util.Objects;

public class Reader extends BaseEntity {

    private final String readerTicketNumber;
    private final String fullName;
    private final LocalDate birthDate;

    private Reader(String readerTicketNumber, String fullName, LocalDate birthDate) {
        super();
        this.readerTicketNumber = LibraryUtils.processTicketNumber(readerTicketNumber);
        this.fullName = LibraryUtils.processString(fullName, "ПІБ читача");
        LibraryUtils.requireNotNull(birthDate, "дата народження");
        LibraryUtils.validateAge(birthDate, 14);
        this.birthDate = birthDate;
    }

    public static Reader of(String readerTicketNumber, String fullName, LocalDate birthDate) {
        return new Reader(readerTicketNumber, fullName, birthDate);
    }

    public String getReaderTicketNumber() {
        return readerTicketNumber;
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
        Reader reader = (Reader) o;
        return Objects.equals(readerTicketNumber, reader.readerTicketNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(readerTicketNumber);
    }

    @Override
    public String toString() {
        return "Reader{" +
                "readerTicketNumber='" + readerTicketNumber + '\'' +
                ", fullName='" + fullName + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }
}