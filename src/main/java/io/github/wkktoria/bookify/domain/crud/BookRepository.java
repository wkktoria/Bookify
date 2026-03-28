package io.github.wkktoria.bookify.domain.crud;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookRepository extends Repository<Book, Long> {

    Book save(Book book);

    @Query("SELECT b FROM Book b")
    List<Book> findAll(final Pageable pageable);

    @Query("SELECT b FROM Book b WHERE b.id = :id")
    Optional<Book> findById(final Long id);

    @Modifying
    @Query("DELETE FROM Book b WHERE b.id = :id")
    void deleteById(final Long id);

    @Modifying
    @Query("UPDATE Book b SET b.title = :#{#newBook.title}, b.publicationDate = :#{#newBook.publicationDate}, b.isbn = :#{#newBook.isbn}, b.pages = :#{#newBook.pages} WHERE b.id = :id")
    void updateById(final Long id, Book newBook);

    boolean existsById(final Long id);

    @Query("""
            SELECT b FROM Book b
            INNER JOIN b.authors authors
            WHERE authors.id = :id
            """)
    Set<Book> findAllByAuthorId(@Param("id") final Long id);

    @Query("""
            SELECT DISTINCT b FROM Book b
            WHERE b.genre.id = :id
            """)
    Set<Book> findAllByGenreId(@Param("id") final Long id);

    @Modifying
    @Query("DELETE FROM Book b WHERE b.id IN :ids")
    void deleteByIdIn(final Collection<Long> ids);

}
