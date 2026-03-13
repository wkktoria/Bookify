package io.github.wkktoria.bookify.book.domain.repository;

import io.github.wkktoria.bookify.book.domain.model.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends Repository<Book, Long> {

    Book save(Book book);

    @Query("SELECT b FROM Book b")
    List<Book> findAll(Pageable pageable);

    @Query("SELECT b FROM Book b WHERE b.id = :id")
    Optional<Book> findById(final Long id);

    @Modifying
    @Query("DELETE FROM Book b WHERE b.id = :id")
    void deleteById(final Long id);

    @Modifying
    @Query("UPDATE Book b SET b.title = :#{#newBook.title}, b.author = :#{#newBook.author} WHERE b.id = :id")
    void updateById(final Long id, Book newBook);

    boolean existsById(final Long id);

}
