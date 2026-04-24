package io.github.wkktoria.bookify.domain.crud;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Transactional
@Log4j2
class SeriesDeleter {

    private final SeriesRetriever seriesRetriever;
    private final SeriesRepository seriesRepository;

    void deleteById(final Long id) {
        log.debug("Deleting series with id={}", id);
        Series series = seriesRetriever.findSeriesById(id);

        if (!series.getBooks().isEmpty()) {
            log.warn("Cannot delete series, there are books assigned to it");
            throw new SeriesNotDeletedException("Could not delete series with id=" + id);
        }

        seriesRepository.deleteById(series.getId());
    }

}
