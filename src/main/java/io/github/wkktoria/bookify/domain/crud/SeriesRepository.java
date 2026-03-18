package io.github.wkktoria.bookify.domain.crud;

import org.springframework.data.repository.Repository;

interface SeriesRepository extends Repository<Series, Long> {

    Series save(final Series series);

}
