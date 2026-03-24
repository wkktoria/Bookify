package io.github.wkktoria.bookify.domain.crud;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
@Log4j2
class GenreAssigner {

    private final BookRetriever bookRetriever;
    private final GenreRetriever genreRetriever;

    void assignDefaultGenreToBook(final Long bookId) {
        log.debug("Assigning default genre to book with id={}", bookId);

        Book book = bookRetriever.findBookById(bookId);
        Genre genre = genreRetriever.findGenreById(1L);
        book.setGenre(genre);
    }

}
