package io.github.wkktoria.bookify.domain.crud;

import io.github.wkktoria.bookify.domain.crud.dto.GenreWithBooksDto;
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

    private final GenreRetriever genreRetriever;
    private final GenreRepository genreRepository;

    void deleteById(final Long id) {
        log.debug("Deleting genre with id={}", id);
        GenreWithBooksDto genre = genreRetriever.findGenreByIdWithBooks(id);

        if (!genre.books().isEmpty()) {
            log.warn("Cannot delete genre, there are books assigned to it");
            throw new GenreNotDeletedException("Could not delete genre with id=" + id);
        }

        genreRepository.deleteById(genre.genre().id());
    }

}
