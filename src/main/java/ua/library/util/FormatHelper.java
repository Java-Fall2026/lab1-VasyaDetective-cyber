package ua.library.util;

class FormatHelper {
    private FormatHelper() {}

    static String normalize(String value) {
        if (value == null) return null;
        return value.trim();
    }

    static String normalizeUpper(String value) {
        String normalized = normalize(value);
        return normalized == null ? null : normalized.toUpperCase();
    }
}