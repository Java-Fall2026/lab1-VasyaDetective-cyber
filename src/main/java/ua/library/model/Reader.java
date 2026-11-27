package ua.library.model;

import ua.library.util.LibraryUtils;
import java.time.LocalDate;
import java.util.Objects;

public record Reader(String readerTicketNumber, String fullName, LocalDate birthDate)
        implements Comparable<Reader> {
    public Reader {
        readerTicketNumber = LibraryUtils.processTicketNumber(readerTicketNumber);
        fullName = LibraryUtils.processString(fullName, "Reader full name");
        LibraryUtils.validateAge(birthDate, 14);
    }

    @Override
    public int compareTo(Reader other) {
        return readerTicketNumber.compareTo(other.readerTicketNumber);
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Reader other
                && readerTicketNumber.equals(other.readerTicketNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(readerTicketNumber);
    }
}
