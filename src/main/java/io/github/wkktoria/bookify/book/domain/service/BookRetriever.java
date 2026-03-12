package io.github.wkktoria.bookify.book.domain.service;

import io.github.wkktoria.bookify.book.domain.model.Book;
import io.github.wkktoria.bookify.book.domain.model.BookNotFoundException;
import io.github.wkktoria.bookify.book.domain.repository.BookRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class BookRetriever {

    private final BookRepository bookRepository;

    BookRetriever(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll() {
        log.info("Retrieving all books");
        return bookRepository.findAll();
    }

    public List<Book> findAllLimitedBy(int limit) {
        log.info("Retrieving books with limit={}", limit);
        return bookRepository.findAll()
                .stream()
                .limit(limit)
                .toList();
    }

    public Book findBookById(Long id) {
        log.info("Retrieving book with id={}", id);
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Could not find book with id=" + id));
    }

    public void existsById(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException("Could not find book with id=" + id);

        }
    }

}
