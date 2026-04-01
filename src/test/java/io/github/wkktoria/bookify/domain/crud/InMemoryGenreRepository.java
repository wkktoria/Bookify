package io.github.wkktoria.bookify.domain.crud;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

class InMemoryGenreRepository implements GenreRepository {

    Map<Long, Genre> db = new HashMap<>();
    AtomicLong index = new AtomicLong(1);

    public InMemoryGenreRepository() {
        save(new Genre(1L, "default"));
    }

    @Override
    public Genre save(final Genre genre) {
        long index = this.index.getAndIncrement();
        db.put(index, genre);
        genre.setId(index);
        return genre;
    }

    @Override
    public Set<Genre> findAll() {
        return new HashSet<>(db.values());
    }

    @Override
    public Optional<Genre> findById(final Long id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public int deleteById(final Long id) {
        return 0;
    }

}
