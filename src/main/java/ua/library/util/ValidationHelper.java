package ua.library.util;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

class ValidationHelper {

    static void checkNotBlank(String value, String expected) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Очікувалось " + expected + ", отримано: " + value);
        }
    }

    static void checkNotNull(Object value, String expected) {
        if (value == null) {
            throw new IllegalArgumentException("Очікувалось " + expected + ", отримано: null");
        }
    }

    static void checkMinAge(LocalDate date, int minAge) {
        checkNotNull(date, "дата народження");
        if (Period.between(date, LocalDate.now()).getYears() < minAge) {
            throw new IllegalArgumentException("Очікувалось мінімум " + minAge + " років, отримано дата: " + date);
        }
    }

    static void checkYear(int year, int min, int max) {
        if (year < min || year > max) {
            throw new IllegalArgumentException("Очікувався рік від " + min + " до " + max + ", отримано: " + year);
        }
    }

    static void checkEnum(String value, List<String> allowed) {
        checkNotBlank(value, "значення зі списку " + allowed);
        if (!allowed.contains(value.toUpperCase())) {
            throw new IllegalArgumentException("Очікувалось " + allowed + ", отримано: " + value);
        }
    }

    static void checkDateNotFuture(LocalDate date) {
        checkNotNull(date, "дата");
        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Очікувалась дата не в майбутньому, отримано: " + date);
        }
    }

    static void checkDateAfterOrEqual(LocalDate start, LocalDate end) {
        if (end != null && start != null && end.isBefore(start)) {
            throw new IllegalArgumentException("Очікувалась дата після або рівна " + start + ", отримано: " + end);
        }
    }

    static void checkDateStrictlyAfter(LocalDate start, LocalDate end) {
        if (end != null && start != null && !end.isAfter(start)) {
            throw new IllegalArgumentException("Очікувалась дата строго після " + start + ", отримано: " + end);
        }
    }
}