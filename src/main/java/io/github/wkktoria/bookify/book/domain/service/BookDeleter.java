package io.github.wkktoria.bookify.book.domain.service;

import io.github.wkktoria.bookify.book.domain.repository.BookRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class BookDeleter {

    private final BookRepository bookRepository;
    private final BookRetriever bookRetriever;

    BookDeleter(BookRepository bookRepository, BookRetriever bookRetriever) {
        this.bookRepository = bookRepository;
        this.bookRetriever = bookRetriever;
    }

    public void deleteById(final Long id) {
        bookRetriever.existsById(id);
        log.info("Deleting by id={}", id);
        bookRepository.deleteById(id);
    }

}
