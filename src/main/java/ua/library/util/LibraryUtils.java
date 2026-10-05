package ua.library.util;

import ua.library.model.BookLoan;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class LibraryUtils {
    
    private static final List<String> ALLOWED_GENRES = List.of("FICTION", "SCIENCE", "HISTORY", "BIOGRAPHY");
    private static final List<String> ALLOWED_STATUSES = List.of("ACTIVE", "RETURNED", "OVERDUE");

    private LibraryUtils() {}

    public static String processString(String value, String fieldName) {
        String trimmed = FormatHelper.normalize(value);
        ValidationHelper.checkNotBlank(trimmed, fieldName);
        return trimmed;
    }

    public static String processIsbn(String value) {
        String trimmed = processString(value, "ISBN");
        ValidationHelper.checkPattern(trimmed, "^[0-9X-]{10,17}$", "ISBN");
        return trimmed;
    }

    public static String processTicketNumber(String value) {
        String trimmed = processString(value, "Reader ticket number");
        ValidationHelper.checkPattern(trimmed, "^[A-Z0-9-]{5,10}$", "Reader ticket number");
        return trimmed;
    }
    
    public static String processGenre(String genre) {
        String normalized = FormatHelper.normalizeUpper(genre);
        ValidationHelper.checkEnum(normalized, ALLOWED_GENRES, "Genre");
        return normalized;
    }
    
    public static String processStatus(String status) {
        String normalized = FormatHelper.normalizeUpper(status);
        ValidationHelper.checkEnum(normalized, ALLOWED_STATUSES, "Status");
        return normalized;
    }

    public static void requireNotNull(Object value, String fieldName) {
        ValidationHelper.checkNotNull(value, fieldName);
    }

    public static void validateAge(LocalDate birthDate, int minAge) {
        ValidationHelper.checkMinAge(birthDate, minAge);
    }

    public static void validateAuthorYear(int year) {
        ValidationHelper.checkYear(year, 1800, LocalDate.now().getYear());
    }

    public static void validatePublishYear(int publishYear, int authorBirthYear) {
        ValidationHelper.checkYear(publishYear, authorBirthYear, LocalDate.now().getYear());
    }

    public static void validateNotFuture(LocalDate date, String fieldName) {
        ValidationHelper.checkDateNotFuture(date, fieldName);
    }

    public static void validateAfterOrEqual(LocalDate start, LocalDate end, String fieldName) {
        ValidationHelper.checkDateAfterOrEqual(start, end, fieldName);
    }

    public static void validateStrictlyAfter(LocalDate start, LocalDate end, String fieldName) {
        ValidationHelper.checkDateStrictlyAfter(start, end, fieldName);
    }
    
    // --- Обчислювані методи ---
    
    public static long loanDays(BookLoan loan) {
        return ChronoUnit.DAYS.between(loan.getIssueDate(), loan.getDueDate());
    }
    
    public static long overdueDays(BookLoan loan) {
        if (loan.getReturnDate() == null) {
            LocalDate today = LocalDate.now();
            return today.isAfter(loan.getDueDate()) ? ChronoUnit.DAYS.between(loan.getDueDate(), today) : 0;
        }
        return loan.getReturnDate().isAfter(loan.getDueDate()) ? 
               ChronoUnit.DAYS.between(loan.getDueDate(), loan.getReturnDate()) : 0;
    }
}