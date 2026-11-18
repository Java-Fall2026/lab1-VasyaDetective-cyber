package ua.library;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.library.exception.DuplicateIdentityException;
import ua.library.model.Reader;
import ua.library.repository.GenericRepository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests for GenericRepository")
class GenericRepositoryTest {
    private final Reader first = new Reader("TKT-001", "First Reader", LocalDate.of(2005, 5, 15));
    private final Reader second = new Reader("TKT-002", "Second Reader", LocalDate.of(2005, 5, 15));

    private GenericRepository<Reader> repository() {
        return new GenericRepository<>("Readers", Reader::readerTicketNumber);
    }

    @Test
    @DisplayName("Should find an added item by identity")
    void addAndFindByIdentity() {
        GenericRepository<Reader> repository = repository();
        repository.add(first);
        assertEquals(first, repository.findByIdentity("TKT-001").orElseThrow());
    }

    @Test
    @DisplayName("Should return an empty Optional for an absent identity")
    void absentIdentityReturnsEmptyOptional() {
        assertTrue(repository().findByIdentity("TKT-999").isEmpty());
    }

    @Test
    @DisplayName("Should reject a duplicate identity with add")
    void duplicateAddThrowsException() {
        GenericRepository<Reader> repository = repository();
        repository.add(first);
        assertThrows(DuplicateIdentityException.class, () -> repository.add(first));
    }

    @Test
    @DisplayName("Should not add a duplicate with addIfAbsent")
    void addIfAbsentDoesNotChangeSize() {
        GenericRepository<Reader> repository = repository();
        repository.add(first);
        assertFalse(repository.addIfAbsent(first));
        assertEquals(1, repository.size());
    }

    @Test
    @DisplayName("Should remove existing and absent identities correctly")
    void removeByIdentityReturnsCorrectResult() {
        GenericRepository<Reader> repository = repository();
        repository.add(first);
        assertTrue(repository.removeByIdentity("TKT-001"));
        assertFalse(repository.removeByIdentity("TKT-999"));
    }

    @Test
    @DisplayName("Should protect repository state from changes to findAll result")
    void findAllDoesNotExposeInternalCollection() {
        GenericRepository<Reader> repository = repository();
        repository.add(first);
        List<Reader> result = repository.findAll();
        result.clear();
        assertEquals(1, repository.size());
    }

    @Test
    @DisplayName("Should work with a different type using the same repository class")
    void repositoryWorksWithDifferentTypes() {
        GenericRepository<String> strings = new GenericRepository<>("Strings", value -> value);
        strings.add("one");
        assertEquals("one", strings.findByIdentity("one").orElseThrow());
    }

    @Test
    @DisplayName("Should add a collection of items")
    void addAllAddsItems() {
        GenericRepository<Reader> repository = repository();
        repository.addAll(List.of(first, second));
        assertEquals(2, repository.size());
    }

    @Test
    @DisplayName("Should reject null items and blank identities")
    void invalidItemsAreRejected() {
        GenericRepository<Reader> repository = repository();
        assertThrows(IllegalArgumentException.class, () -> repository.add(null));
        GenericRepository<String> strings = new GenericRepository<>("Strings", value -> value);
        assertThrows(IllegalArgumentException.class, () -> strings.add(" "));
    }
}
