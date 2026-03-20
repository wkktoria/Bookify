package io.github.wkktoria.bookify.domain.crud;

import io.github.wkktoria.bookify.domain.crud.dto.GenreDto;
import io.github.wkktoria.bookify.domain.crud.dto.UpdateGenreRequestDto;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Transactional
@Log4j2
class GenreUpdater {

    private final GenreRetriever genreRetriever;

    GenreDto updateGenreById(final Long id, final UpdateGenreRequestDto requestDto) {
        log.debug("Updating genre with id={} with values: {}", id, requestDto);

        Genre genre = genreRetriever.findGenreById(id);

        if (requestDto.name() != null && !requestDto.name().isBlank()) {
            log.debug("Setting name of genre with id={} to: {}", id, requestDto.name());
            genre.setName(requestDto.name());
        }

        log.debug("Genre with id={} successfully updated", id);

        return new GenreDto(genre.getId(), genre.getName());
    }

}
