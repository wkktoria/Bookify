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
class BookDeleter {

    private final BookRepository bookRepository;
    private final BookRetriever bookRetriever;

    void deleteById(final Long id) {
        log.debug("Deleting book with id={}", id);

        bookRetriever.existsById(id);
        bookRepository.deleteById(id);

        log.debug("Book with id={} successfully deleted", id);
    }

}
