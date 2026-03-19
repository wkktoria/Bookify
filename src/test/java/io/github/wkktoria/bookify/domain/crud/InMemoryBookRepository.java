package io.github.wkktoria.bookify.domain.crud;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

class InMemoryBookRepository implements BookRepository {

    @Override
    public Book save(final Book book) {
        return null;
    }

    @Override
    public List<Book> findAll(final Pageable pageable) {
        return List.of();
    }

    @Override
    public Optional<Book> findById(final Long id) {
        return Optional.empty();
    }

    @Override
    public void deleteById(final Long id) {

    }

    @Override
    public void updateById(final Long id, final Book newBook) {

    }

    @Override
    public boolean existsById(final Long id) {
        return false;
    }

}
