package ua.library.repository;

import ua.library.model.Reader;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ReaderRepository extends GenericRepository<Reader> {
    public ReaderRepository() {
        super("Readers", Reader::readerTicketNumber);
    }

    public List<Reader> findByName(String name) {
        String query = requireText(name, "Reader name").toLowerCase();
        return stream()
                .filter(reader -> reader.fullName().toLowerCase().contains(query))
                .toList();
    }

    public List<Reader> findByBirthYearBetween(int from, int to) {
        if (from > to) {
            throw new IllegalArgumentException("The beginning of the birth-year range must not exceed its end");
        }
        return stream()
                .filter(reader -> reader.birthDate().getYear() >= from)
                .filter(reader -> reader.birthDate().getYear() <= to)
                .toList();
    }

    public List<Reader> findByNameAndBornBefore(String name, int year) {
        String query = requireText(name, "Reader name").toLowerCase();
        return stream()
                .filter(reader -> reader.fullName().toLowerCase().contains(query))
                .filter(reader -> reader.birthDate().getYear() < year)
                .toList();
    }

    public Optional<Reader> findFirstByTicketPrefix(String prefix) {
        String query = requireText(prefix, "Ticket prefix").toLowerCase();
        return stream()
                .filter(reader -> reader.readerTicketNumber().toLowerCase().startsWith(query))
                .findFirst();
    }

    public List<Reader> sortByNameThenBirthDate() {
        return findAllSorted(Comparator.comparing(Reader::fullName)
                .thenComparing(Reader::birthDate));
    }

    public List<Reader> sortByBirthDateDescendingThenName() {
        return findAllSorted(Comparator.comparing(Reader::birthDate)
                .reversed()
                .thenComparing(Reader::fullName));
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.trim();
    }
}
