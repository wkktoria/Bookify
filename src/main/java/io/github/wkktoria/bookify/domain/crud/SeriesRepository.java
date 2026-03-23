package io.github.wkktoria.bookify.domain.crud;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

interface SeriesRepository extends Repository<Series, Long> {

    Series save(final Series series);

    @Query("SELECT s FROM Series s")
    List<Series> findAll();

    @Query("""
            SELECT DISTINCT s FROM Series s
            LEFT JOIN FETCH s.books b
            LEFT JOIN FETCH b.authors
            WHERE s.id = :id
            """)
    Optional<Series> findByIdWithBooksAndAuthors(@Param("id") final Long id);

}
