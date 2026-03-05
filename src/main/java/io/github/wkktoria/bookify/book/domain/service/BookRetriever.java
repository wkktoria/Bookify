package io.github.wkktoria.bookify.book.domain.service;

import io.github.wkktoria.bookify.book.domain.model.Book;
import io.github.wkktoria.bookify.book.domain.repository.BookRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@Log4j2
public class BookRetriever {

    private final BookRepository bookRepository;

    BookRetriever(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Map<Integer, Book> findAll() {
        log.info("Retrieving all books");
        return bookRepository.findAll();
    }

    public Map<Integer, Book> findAllLimitedBy(int limit) {
        log.info("Retrieving books with limit={}", limit);
        return bookRepository.findAll()
                .entrySet()
                .stream()
                .limit(limit)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}
