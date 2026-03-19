package io.github.wkktoria.bookify.domain.crud;

import java.util.Optional;

class InMemorySeriesRepository implements SeriesRepository {

    @Override
    public Series save(final Series series) {
        return null;
    }

    @Override
    public Optional<Series> findByIdWithBooksAndAuthors(final Long id) {
        return Optional.empty();
    }
    
}
