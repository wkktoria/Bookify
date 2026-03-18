package io.github.wkktoria.bookify.domain.crud;

import io.github.wkktoria.bookify.domain.crud.dto.SeriesDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
class SeriesAdder {

    private final SeriesRepository seriesRepository;
    private final BookRetriever bookRetriever;

    SeriesDto addSeries(final String name, final Long bookId) {
        log.debug("Saving new series: name='{}', bookId={}", name, bookId);

        Book bookById = bookRetriever.findBookById(bookId);

        Series series = new Series(name);
        series.addBook(bookById);

        Series savedSeries = seriesRepository.save(series);

        log.debug("Series added with id={}", savedSeries.getId());

        return new SeriesDto(savedSeries.getId(), savedSeries.getName());
    }

}
