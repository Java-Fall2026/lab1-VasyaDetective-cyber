package ua.library.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.library.exception.DuplicateIdentityException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GenericRepository<T> {
    private static final Logger log = LoggerFactory.getLogger(GenericRepository.class);

    private final String name;
    private final IdentityExtractor<T> extractor;
    private final Map<String, T> items = new LinkedHashMap<>();

    public GenericRepository(String name, IdentityExtractor<T> extractor) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Repository name must not be blank");
        }
        if (extractor == null) {
            throw new IllegalArgumentException("Identity extractor must not be null");
        }
        this.name = name;
        this.extractor = extractor;
    }

    public void add(T item) {
        String identity = identityOf(item);
        if (items.containsKey(identity)) {
            DuplicateIdentityException exception = new DuplicateIdentityException(
                    "Duplicate identity '" + identity + "' in repository '" + name + "'");
            log.warn("Rejected duplicate identity '{}' in repository '{}'", identity, name);
            log.error("Adding duplicate identity failed", exception);
            throw exception;
        }
        items.put(identity, item);
        log.info("Added identity '{}' to repository '{}'", identity, name);
    }

    public boolean addIfAbsent(T item) {
        String identity = identityOf(item);
        if (items.containsKey(identity)) {
            log.warn("Rejected duplicate identity '{}' in repository '{}'", identity, name);
            return false;
        }
        items.put(identity, item);
        log.info("Added identity '{}' to repository '{}'", identity, name);
        return true;
    }

    public void addAll(Collection<? extends T> collection) {
        if (collection == null) {
            throw new IllegalArgumentException("Items collection must not be null");
        }
        for (T item : collection) {
            add(item);
        }
    }

    public boolean removeByIdentity(String identity) {
        validateIdentity(identity);
        T removed = items.remove(identity);
        if (removed == null) {
            log.warn("Identity '{}' was not found in repository '{}'", identity, name);
            return false;
        }
        log.info("Removed identity '{}' from repository '{}'", identity, name);
        return true;
    }

    public Optional<T> findByIdentity(String identity) {
        validateIdentity(identity);
        Optional<T> result = Optional.ofNullable(items.get(identity));
        if (result.isPresent()) {
            log.info("Found identity '{}' in repository '{}'", identity, name);
        } else {
            log.warn("Identity '{}' was not found in repository '{}'", identity, name);
        }
        return result;
    }

    public List<T> findAll() {
        log.info("Returned {} items from repository '{}'", items.size(), name);
        return new ArrayList<>(items.values());
    }

    public int size() {
        return items.size();
    }

    private String identityOf(T item) {
        if (item == null) {
            IllegalArgumentException exception = new IllegalArgumentException("Item must not be null");
            log.error("Cannot add null item to repository '{}'", name, exception);
            throw exception;
        }
        String identity = extractor.identityOf(item);
        validateIdentity(identity);
        return identity;
    }

    private void validateIdentity(String identity) {
        if (identity == null || identity.isBlank()) {
            throw new IllegalArgumentException("Identity must not be null or blank");
        }
    }
}
