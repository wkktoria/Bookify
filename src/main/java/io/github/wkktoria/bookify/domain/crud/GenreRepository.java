package io.github.wkktoria.bookify.domain.crud;

import org.springframework.data.repository.Repository;

interface GenreRepository extends Repository<Genre, Long> {

    Genre save(final Genre genre);

}
