package ua.library.model;

import ua.library.util.LibraryUtils;
import java.time.LocalDate;

public record Reader(String readerTicketNumber, String fullName, LocalDate birthDate) {
    public Reader {
        readerTicketNumber = LibraryUtils.processTicketNumber(readerTicketNumber);
        fullName = LibraryUtils.processString(fullName, "Reader full name");
        LibraryUtils.validateAge(birthDate, 14);
    }
}