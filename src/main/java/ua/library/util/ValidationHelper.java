package ua.library.util;

import java.time.LocalDate;
import java.time.Period;

class ValidationHelper {

    static void checkNotBlank(String value, String expected) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Очікувалось " + expected + ", отримано: " + value);
        }
    }

    static void checkPattern(String value, String regex, String expectedFormat) {
        if (!value.matches(regex)) {
            throw new IllegalArgumentException("Некоректний формат для " + expectedFormat + ". Отримано: " + value);
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
}