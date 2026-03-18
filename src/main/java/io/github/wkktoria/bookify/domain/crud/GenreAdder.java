package io.github.wkktoria.bookify.domain.crud;

import io.github.wkktoria.bookify.domain.crud.dto.GenreDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
class GenreAdder {

    private final GenreRepository genreRepository;

    GenreDto addGenre(final String name) {
        log.debug("Saving new genre: name='{}'", name);

        Genre genre = new Genre(name);
        Genre savedGenre = genreRepository.save(genre);

        log.debug("Genre saved with id={}", savedGenre.getId());

        return new GenreDto(savedGenre.getId(), savedGenre.getName());
    }

}
