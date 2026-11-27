package ua.library.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.library.model.Reader;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class ReaderRepository extends GenericRepository<Reader> {
    private static final Logger log = LoggerFactory.getLogger(ReaderRepository.class);

    public ReaderRepository() {
        super("Readers", Reader::readerTicketNumber);
    }

    public List<Reader> findByName(String name) {
        String query = requireText(name, "Reader name").toLowerCase(Locale.ROOT);
        List<Reader> result = stream()
                .filter(reader -> reader.fullName().toLowerCase(Locale.ROOT).contains(query))
                .sorted(Comparator.comparing(Reader::fullName))
                .toList();
        logSearch("name containing '" + name + "'", result.size());
        return result;
    }

    public List<Reader> findByBirthYearBetween(int from, int to) {
        if (from > to) {
            throw new IllegalArgumentException("The beginning of the birth-year range must not exceed its end");
        }
        List<Reader> result = stream()
                .filter(reader -> reader.birthDate().getYear() >= from)
                .filter(reader -> reader.birthDate().getYear() <= to)
                .sorted(Comparator.comparing(Reader::birthDate))
                .toList();
        logSearch("birth year between " + from + " and " + to, result.size());
        return result;
    }

    public List<Reader> findByNameAndBornBefore(String name, int year) {
        String query = requireText(name, "Reader name").toLowerCase(Locale.ROOT);
        List<Reader> result = stream()
                .filter(reader -> reader.fullName().toLowerCase(Locale.ROOT).contains(query))
                .filter(reader -> reader.birthDate().getYear() < year)
                .sorted(Comparator.comparing(Reader::fullName))
                .toList();
        logSearch("name containing '" + name + "' and birth year before " + year, result.size());
        return result;
    }

    public Optional<Reader> findFirstByTicketPrefix(String prefix) {
        String query = requireText(prefix, "Ticket prefix").toLowerCase(Locale.ROOT);
        Optional<Reader> result = stream()
                .filter(reader -> reader.readerTicketNumber().toLowerCase(Locale.ROOT).startsWith(query))
                .sorted(Comparator.comparing(Reader::readerTicketNumber))
                .findFirst();
        logOptionalSearch("ticket prefix '" + prefix + "'", result);
        return result;
    }

    public List<Integer> findDistinctBirthYears(int limit) {
        requirePositiveLimit(limit);
        List<Integer> result = stream()
                .map(reader -> reader.birthDate().getYear())
                .distinct()
                .sorted()
                .limit(limit)
                .toList();
        logSearch("distinct birth years limited to " + limit, result.size());
        return result;
    }

    public List<Reader> findBornAfter(int year, int limit) {
        requirePositiveLimit(limit);
        List<Reader> result = stream()
                .filter(reader -> reader.birthDate().getYear() > year)
                .sorted(Comparator.comparing(Reader::birthDate).reversed())
                .limit(limit)
                .toList();
        logSearch("readers born after " + year + " limited to " + limit, result.size());
        return result;
    }

    public List<Reader> sortByNameThenBirthDate() {
        List<Reader> result = findAllSorted(Comparator.comparing(Reader::fullName)
                .thenComparing(Reader::birthDate));
        logSort("name ascending, birth date ascending", result.size());
        return result;
    }

    public List<Reader> sortNaturally() {
        List<Reader> result = findAllSorted(Comparator.naturalOrder());
        logSort("natural ticket-number order", result.size());
        return result;
    }

    public List<Reader> sortByBirthDateDescendingThenName() {
        List<Reader> result = findAllSorted(Comparator.comparing(Reader::birthDate)
                .reversed()
                .thenComparing(Reader::fullName));
        logSort("birth date descending, name ascending", result.size());
        return result;
    }

    private void logSort(String criterion, int count) {
        log.info("Sorted readers by {}; returned {} items", criterion, count);
    }

    private void logSearch(String criterion, int count) {
        if (count == 0) {
            log.warn("Reader search by {} returned no items", criterion);
        } else {
            log.info("Reader search by {}; found {} items", criterion, count);
        }
    }

    private void logOptionalSearch(String criterion, Optional<Reader> result) {
        result.ifPresentOrElse(
                reader -> log.info("Reader search by {}; found '{}'", criterion, reader.fullName()),
                () -> log.warn("Reader search by {} returned no item", criterion));
    }

    private static void requirePositiveLimit(int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit must be positive");
        }
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.trim();
    }
}
