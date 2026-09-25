package ua.library.util;

import java.time.LocalDate;

public class LibraryUtils {

    public static String processString(String value, String expected) {
        String trimmed = FormatHelper.trimString(value);
        ValidationHelper.checkNotBlank(trimmed, expected);
        return trimmed;
    }

    public static String processIsbn(String value) {
        String trimmed = processString(value, "ISBN");
        ValidationHelper.checkPattern(trimmed, "^[0-9X-]{10,17}$", "ISBN");
        return trimmed;
    }

    public static String processTicketNumber(String value) {
        String trimmed = processString(value, "номер читацького квитка");
        ValidationHelper.checkPattern(trimmed, "^[A-Z0-9-]{5,10}$", "номеру квитка (напр. TKT-001)");
        return trimmed;
    }

    public static void requireNotNull(Object value, String expected) {
        ValidationHelper.checkNotNull(value, expected);
    }

    public static void validateAge(LocalDate birthDate, int minAge) {
        ValidationHelper.checkMinAge(birthDate, minAge);
    }

    public static void validatePublishYear(int publishYear, int authorBirthYear) {
        ValidationHelper.checkYear(publishYear, 1000, LocalDate.now().getYear());
        if (publishYear < authorBirthYear) {
            throw new IllegalArgumentException("Рік видання (" + publishYear + ") не може бути раніше року народження автора (" + authorBirthYear + ")");
        }
    }

    public static void validateNotFuture(LocalDate date) {
        ValidationHelper.checkDateNotFuture(date);
    }

    public static void validateAfterOrEqual(LocalDate start, LocalDate end) {
        ValidationHelper.checkDateAfterOrEqual(start, end);
    }
}