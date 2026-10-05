package ua.library.util;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

class ValidationHelper {

    private ValidationHelper() {}

    static void checkNotBlank(String value, String fieldName) {
        checkNotNull(value, fieldName);
        if (value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be blank.");
        }
    }

    static void checkPattern(String value, String regex, String fieldName) {
        if (!value.matches(regex)) {
            throw new IllegalArgumentException("Invalid format for " + fieldName + ", got: " + value);
        }
    }

    static void checkNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " must not be null.");
        }
    }

    static void checkMinAge(LocalDate birthDate, int minAge) {
        checkNotNull(birthDate, "Birth date");
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        if (age < minAge) {
            throw new IllegalArgumentException("Age must be at least " + minAge + ", got: " + age);
        }
    }

    static void checkYear(int year, int min, int max) {
        if (year < min || year > max) {
            throw new IllegalArgumentException("Year must be between " + min + " and " + max + ", got: " + year);
        }
    }

    static void checkEnum(String value, List<String> allowed, String fieldName) {
        checkNotBlank(value, fieldName);
        if (!allowed.contains(value.toUpperCase())) {
            throw new IllegalArgumentException(fieldName + " must be one of " + allowed + ", got: " + value);
        }
    }

    static void checkDateNotFuture(LocalDate date, String fieldName) {
        checkNotNull(date, fieldName);
        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(fieldName + " must not be in the future, got: " + date);
        }
    }

    static void checkDateAfterOrEqual(LocalDate start, LocalDate end, String fieldName) {
        if (end != null && start != null && end.isBefore(start)) {
            throw new IllegalArgumentException(fieldName + " must be after or equal to " + start + ", got: " + end);
        }
    }

    static void checkDateStrictlyAfter(LocalDate start, LocalDate end, String fieldName) {
        if (end != null && start != null && !end.isAfter(start)) {
            throw new IllegalArgumentException(fieldName + " must be strictly after " + start + ", got: " + end);
        }
    }
}