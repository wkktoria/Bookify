package io.github.wkktoria.bookify.domain.crud;

import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

class InMemoryBookRepository implements BookRepository {

    Map<Long, Book> db = new HashMap<>();
    AtomicLong index = new AtomicLong(0);

    @Override
    public Book save(final Book book) {
        long index = this.index.getAndIncrement();
        db.put(index, book);
        book.setId(index);
        return book;
    }

    @Override
    public List<Book> findAll(final Pageable pageable) {
        return db.values().stream().toList();
    }

    @Override
    public Optional<Book> findById(final Long id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public void deleteById(final Long id) {
        db.remove(id);
    }

    @Override
    public void updateById(final Long id, final Book newBook) {

    }

    @Override
    public boolean existsById(final Long id) {
        return db.containsKey(id);
    }

    @Override
    public Set<Book> findAllByAuthorId(final Long id) {
        return db.values().stream()
                .filter(book -> book.getAuthors().stream()
                        .anyMatch(author -> author.getId().equals(id)))
                .collect(Collectors.toSet());
    }

    @Override
    public void deleteByIdIn(final Collection<Long> ids) {
        ids.forEach(id -> db.remove(id));

    }

}
