package io.github.wkktoria.bookify.domain.crud;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;

interface AuthorRepository extends Repository<Author, Long> {

    Author save(final Author author);

    @Query("SELECT a FROM Author a WHERE a.id = :id")
    Optional<Author> findById(final Long id);

}
