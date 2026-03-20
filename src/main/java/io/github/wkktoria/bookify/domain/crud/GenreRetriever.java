package io.github.wkktoria.bookify.domain.crud;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Log4j2
class GenreRetriever {

    private final GenreRepository genreRepository;

    Genre findGenreById(final Long id) {
        log.debug("Retrieving genre with id={}", id);

        return genreRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Genre with id={} not found", id);
                    return new GenreNotFoundException("Could not find genre with id=" + id);
                });
    }

}
