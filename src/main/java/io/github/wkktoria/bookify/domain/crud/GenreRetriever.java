package io.github.wkktoria.bookify.domain.crud;

import io.github.wkktoria.bookify.domain.crud.dto.GenreDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Log4j2
class GenreRetriever {

    private final GenreRepository genreRepository;

    Set<GenreDto> findAll() {
        log.debug("Retrieving all genres");

        return genreRepository.findAll().stream()
                .map(genre -> new GenreDto(genre.getId(), genre.getName()))
                .collect(Collectors.toSet());
    }

    Genre findGenreById(final Long id) {
        log.debug("Retrieving genre with id={}", id);

        return genreRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Genre with id={} not found", id);
                    return new GenreNotFoundException("Could not find genre with id=" + id);
                });
    }

}
