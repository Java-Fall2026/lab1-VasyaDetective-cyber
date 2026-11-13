package ua.library.repository;

import ua.library.model.Reader;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SearchBenchmark {
    private static final int ITEM_COUNT = 100_000;
    private static final int REPETITIONS = 7;

    public static void main(String[] args) {
        IdentityExtractor<Reader> extractor = Reader::readerTicketNumber;
        GenericRepository<Reader> repository = new GenericRepository<>("Benchmark readers", extractor);
        for (int i = 0; i < ITEM_COUNT; i++) {
            String ticket = String.format("R%08d", i);
            repository.add(new Reader(ticket, "Reader " + i, LocalDate.of(2000, 1, 1)));
        }

        List<Reader> list = repository.findAll();
        Map<String, Reader> map = new LinkedHashMap<>();
        for (Reader reader : list) {
            map.put(extractor.identityOf(reader), reader);
        }
        String[] identities = {"R00000000", "R00099999", "R99999999"};
        String[] scenarios = {"first", "last", "missing"};

        for (int i = 0; i < identities.length; i++) {
            warmUp(map, list, identities[i], extractor);
            long mapTime = measureMap(map, identities[i]);
            long listTime = measureList(list, identities[i], extractor);
            System.out.printf("%s: Map=%d ns, List=%d ns%n", scenarios[i], mapTime, listTime);
        }
    }

    private static <E> Optional<E> linearSearch(List<E> items, String identity, IdentityExtractor<E> extractor) {
        for (E item : items) {
            if (identity.equals(extractor.identityOf(item))) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    private static void warmUp(Map<String, Reader> map, List<Reader> list,
                               String identity, IdentityExtractor<Reader> extractor) {
        for (int i = 0; i < 10_000; i++) {
            map.get(identity);
            linearSearch(list, identity, extractor);
        }
    }

    private static long measureMap(Map<String, Reader> map, String identity) {
        long[] times = new long[REPETITIONS];
        for (int i = 0; i < REPETITIONS; i++) {
            long start = System.nanoTime();
            map.get(identity);
            times[i] = System.nanoTime() - start;
        }
        return minimum(times);
    }

    private static long measureList(List<Reader> list, String identity, IdentityExtractor<Reader> extractor) {
        long[] times = new long[REPETITIONS];
        for (int i = 0; i < REPETITIONS; i++) {
            long start = System.nanoTime();
            linearSearch(list, identity, extractor);
            times[i] = System.nanoTime() - start;
        }
        return minimum(times);
    }

    private static long minimum(long[] values) {
        long minimum = values[0];
        for (long value : values) {
            minimum = Math.min(minimum, value);
        }
        return minimum;
    }
}
