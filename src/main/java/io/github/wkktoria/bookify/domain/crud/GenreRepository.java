package io.github.wkktoria.bookify.domain.crud;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;

interface GenreRepository extends Repository<Genre, Long> {

    Genre save(final Genre genre);

    @Query("SELECT g FROM Genre g WHERE g.id = :id")
    Optional<Genre> findById(final Long id);

}
