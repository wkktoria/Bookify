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
class BookUpdater {

    private final BookRepository bookRepository;
    private final BookRetriever bookRetriever;

    void updateById(final Long id, final Book newBook) {
        log.debug("Updating book with id={}", id);

        bookRetriever.existsById(id);
        bookRepository.updateById(id, newBook);

        log.debug("Book with id={} successfully updated", id);
    }

}
