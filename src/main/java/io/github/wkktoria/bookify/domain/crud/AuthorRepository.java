package io.github.wkktoria.bookify.domain.crud;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

interface AuthorRepository extends Repository<Author, Long> {

    Author save(final Author author);

    List<Author> findAll(final Pageable pageable);

    @Query("SELECT a FROM Author a WHERE a.id = :id")
    Optional<Author> findById(final Long id);

    @Modifying
    @Query("DELETE FROM Author a WHERE a.id = :id")
    void deleteById(final Long id);

}
