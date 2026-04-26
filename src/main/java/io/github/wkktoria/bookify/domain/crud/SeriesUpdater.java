package io.github.wkktoria.bookify.domain.crud;

import io.github.wkktoria.bookify.domain.crud.dto.SeriesDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Transactional
@Log4j2
class SeriesUpdater {

    private final SeriesRetriever seriesRetriever;

    SeriesDto updateById(final Long id, final String name) {
        log.debug("Updating series with id={}", id);
        Series series = seriesRetriever.findSeriesById(id);

        if (seriesRetriever.existsByName(name)) {
            log.warn("Series with name='{}' already exists", name);
            throw new SeriesNotUpdatedException("Could not update series with id=" + id);
        }

        series.setName(name);
        return SeriesDto.builder()
                .id(series.getId())
                .name(series.getName())
                .build();
    }

}
