package io.github.wkktoria.bookify.domain.crud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

class InMemorySeriesRepository implements SeriesRepository {

    Map<Long, Series> db = new HashMap<>();
    AtomicLong index = new AtomicLong(0);

    @Override
    public List<Series> findAll() {
        return new ArrayList<>(db.values());
    }

    @Override
    public Optional<Series> findById(final Long id) {
        return Optional.empty();
    }

    @Override
    public Series save(final Series series) {
        long index = this.index.getAndIncrement();
        db.put(index, series);
        series.setId(index);
        return series;
    }

    @Override
    public Optional<Series> findByIdWithBooksAndAuthors(final Long id) {
        return db.values().stream()
                .filter(series -> series.getId().equals(id))
                .findFirst();
    }

}
