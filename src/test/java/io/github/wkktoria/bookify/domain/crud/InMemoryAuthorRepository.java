package io.github.wkktoria.bookify.domain.crud;

import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

class InMemoryAuthorRepository implements AuthorRepository {

    Map<Long, Author> db = new HashMap<>();
    AtomicLong index = new AtomicLong(0);

    @Override
    public Author save(final Author author) {
        long index = this.index.getAndIncrement();
        db.put(index, author);
        author.setId(index);
        return author;
    }

    @Override
    public List<Author> findAll(final Pageable pageable) {
        return db.values().stream().toList();
    }

    @Override
    public Optional<Author> findById(final Long id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public void deleteById(final Long id) {
        db.remove(id);
    }

}
