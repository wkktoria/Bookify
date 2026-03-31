package io.github.wkktoria.bookify.domain.crud;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Transactional
@Log4j2
class BookDeleter {

    private final BookRepository bookRepository;
    private final BookRetriever bookRetriever;
    private final GenreDeleter genreDeleter;
    private final GenreAssigner genreAssigner;

    void deleteById(final Long id) {
        log.debug("Deleting book with id={}", id);

        bookRetriever.existsById(id);
        bookRepository.deleteById(id);

        log.debug("Book with id={} successfully deleted", id);
    }

    void deleteBookAndGenreById(final Long bookId) {
        log.debug("Deleting book with id={} and its genre", bookId);

        Book book = bookRetriever.findBookById(bookId);
        Long genreId = book.getGenre().getId();

        Set<Book> booksWithGenre = bookRetriever.findBooksByGenreId(genreId);
        for (Book bookWithGenre : booksWithGenre) {
            genreAssigner.assignDefaultGenreToBook(bookWithGenre.getId());
        }

        deleteById(bookId);
        genreDeleter.deleteById(genreId);
    }

    void deleteAllBooksByIds(final Set<Long> bookIds) {
        log.debug("Deleting books by ids={}", bookIds);

        bookRepository.deleteByIdIn(bookIds);
    }

}
