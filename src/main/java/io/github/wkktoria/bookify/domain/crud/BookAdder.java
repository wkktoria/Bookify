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
class BookAdder {

    private final BookRepository bookRepository;

    Book addBook(final Book book) {
        log.debug("Saving new book: title='{}', author='{}'", book.getTitle(), book.getAuthor());

        Book savedBook = bookRepository.save(book);

        log.debug("Book saved with id={}", savedBook.getId());

        return savedBook;
    }

}
