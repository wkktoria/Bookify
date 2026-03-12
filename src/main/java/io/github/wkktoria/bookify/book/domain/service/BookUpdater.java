package io.github.wkktoria.bookify.book.domain.service;

import io.github.wkktoria.bookify.book.domain.model.Book;
import io.github.wkktoria.bookify.book.domain.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Transactional
@Log4j2
public class BookUpdater {

    private final BookRepository bookRepository;

    BookUpdater(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void updateById(Long id, Book newBook) {
        log.info("Updating book with id={} to newBook={}", id, newBook);
        bookRepository.updateById(id, newBook);
    }

}
