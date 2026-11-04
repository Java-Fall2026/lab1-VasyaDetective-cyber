package ua.library.util;

import ua.library.model.BookLoan;
import ua.library.model.Genre;
import ua.library.model.LoanStatus;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LibraryUtils {
    
    static final int MIN_AUTHOR_BIRTH_YEAR = 1800;

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

    public static void requireNotNull(Object value, String fieldName) {
        ValidationHelper.checkNotNull(value, fieldName);
    }

    public static void validateAge(LocalDate birthDate, int minAge) {
        ValidationHelper.checkMinAge(birthDate, minAge);
    }

    public static void validateAuthorYear(int year) {
        ValidationHelper.checkYear(year, MIN_AUTHOR_BIRTH_YEAR, LocalDate.now().getYear());
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
    
    public static long loanDays(BookLoan loan) {
        return ChronoUnit.DAYS.between(loan.getIssueDate(), loan.getDueDate());
    }
    
    public static long overdueDays(BookLoan loan) {
        if (loan.getReturnDate() == null) {
            return 0;
        }
        return loan.getReturnDate().isAfter(loan.getDueDate()) ? 
               ChronoUnit.DAYS.between(loan.getDueDate(), loan.getReturnDate()) : 0;
    }

    public static String getStatusDescription(LoanStatus status) {
        return switch (status) {
            case ACTIVE -> "Книга зараз у читача";
            case RETURNED -> "Книгу успішно повернуто";
            case OVERDUE -> "Увага! Термін повернення минув";
        };
    }

    public static String getGenreCategory(Genre genre) {
        return switch (genre) {
            case FICTION -> "Entertaining Literature";
            case SCIENCE, HISTORY, BIOGRAPHY -> "Educational Literature";
        };
    }
}