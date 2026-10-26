package ua.library.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.library.exception.InvalidDataException;
import ua.library.model.Author;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvLoader {
    private static final Logger log = LoggerFactory.getLogger(CsvLoader.class);

    public static List<Author> loadAuthors(String filePath) {
        List<Author> authors = new ArrayList<>();
        log.info("Starting to load authors from file: {}", filePath);

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();
            int lineNumber = 1;
            String line;

            while ((line = br.readLine()) != null) {
                lineNumber++;
                try {
                    authors.add(parseAuthor(line, lineNumber));
                } catch (InvalidDataException e) {
                    log.warn("Skipped invalid row {}: {}", lineNumber, e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            log.error("File not found: {}", filePath, e);
        } catch (IOException e) {
            log.error("Error reading file: {}", filePath, e);
        } finally {
            log.info("Finished reading process. Successfully loaded {} authors.", authors.size());
        }

        return authors;
    }

    private static Author parseAuthor(String line, int lineNumber) throws InvalidDataException {
        String[] parts = line.split(",");
        if (parts.length != 3) {
            throw new InvalidDataException("Row " + lineNumber + ": Invalid column count");
        }

        try {
            String code = parts[0].trim();
            String name = parts[1].trim();
            int birthYear = Integer.parseInt(parts[2].trim());
            return Author.of(code, name, birthYear);
        } catch (IllegalArgumentException e) {
            throw new InvalidDataException("Row " + lineNumber + ": " + e.getMessage(), e);
        }
    }
}