package io.github.wkktoria.bookify.domain.crud;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Transactional
@Log4j2
class GenreDeleter {

    private final GenreRepository genreRepository;

    void deleteById(final Long id) {
        log.debug("Deleting genre with id={}", id);
        int i = genreRepository.deleteById(id);
        if (i != 1) {
            throw new GenreNotDeletedException("Could not delete genre with id=" + id);
        }
    }

}
