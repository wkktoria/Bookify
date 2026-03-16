package io.github.wkktoria.bookify.domain.crud.book;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Log4j2
class BookRetriever {

    private final BookRepository bookRepository;

    List<Book> findAll(Pageable pageable) {
        log.debug("Fetching books from repository with pageable: page={}, size={}, sort={}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        List<Book> books = bookRepository.findAll(pageable);

        log.debug("Retrieved {} books from repository", books.size());

        return books;
    }

    Book findBookById(Long id) {
        log.debug("Fetching book with id={}", id);

        return bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Book with id={} not found", id);
                    return new BookNotFoundException("Could not find book with id=" + id);
                });
    }

    void existsById(Long id) {
        log.debug("Checking existence of book with id={}", id);

        if (!bookRepository.existsById(id)) {
            log.warn("Book existence check failed for id={}", id);
            throw new BookNotFoundException("Could not find book with id=" + id);

        }
    }

}
