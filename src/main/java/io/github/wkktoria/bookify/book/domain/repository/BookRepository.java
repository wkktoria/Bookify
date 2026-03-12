package io.github.wkktoria.bookify.book.domain.repository;

import io.github.wkktoria.bookify.book.domain.model.Book;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends Repository<Book, Long> {

    Book save(Book book);

    List<Book> findAll();

    Optional<Book> findById(final Long id);

    void deleteById(final Long id);

}
