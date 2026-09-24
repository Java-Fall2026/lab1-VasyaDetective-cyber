package ua.library.util;

import java.time.LocalDate;
import java.util.List;

public class LibraryUtils {

    public static String processString(String value, String expected) {
        String trimmed = FormatHelper.trimString(value);
        ValidationHelper.checkNotBlank(trimmed, expected);
        return trimmed;
    }

    public static void requireNotNull(Object value, String expected) {
        ValidationHelper.checkNotNull(value, expected);
    }

    public static void validateAge(LocalDate birthDate, int minAge) {
        ValidationHelper.checkMinAge(birthDate, minAge);
    }

    public static void validateYear(int year, int min, int max) {
        ValidationHelper.checkYear(year, min, max);
    }

    public static String processEnum(String value, List<String> allowed) {
        String trimmed = FormatHelper.trimString(value);
        ValidationHelper.checkEnum(trimmed, allowed);
        return trimmed.toUpperCase();
    }

    public static void validateNotFuture(LocalDate date) {
        ValidationHelper.checkDateNotFuture(date);
    }

    public static void validateAfterOrEqual(LocalDate start, LocalDate end) {
        ValidationHelper.checkDateAfterOrEqual(start, end);
    }

    public static void validateStrictlyAfter(LocalDate start, LocalDate end) {
        ValidationHelper.checkDateStrictlyAfter(start, end);
    }
}