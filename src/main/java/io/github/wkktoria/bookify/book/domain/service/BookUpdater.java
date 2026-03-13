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
    private final BookRetriever bookRetriever;

    BookUpdater(BookRepository bookRepository, BookRetriever bookRetriever) {
        this.bookRepository = bookRepository;
        this.bookRetriever = bookRetriever;
    }

    public void updateById(Long id, Book newBook) {
        log.info("Updating book id={} with values: title='{}', author='{}'",
                id, newBook.getTitle(), newBook.getAuthor());

        bookRetriever.existsById(id);
        bookRepository.updateById(id, newBook);

        log.debug("Book id={} successfully updated", id);
    }

    public Book updatePartiallyById(Long id, Book bookFromRequest) {
        log.info("Partially updating book id={} with provided fields", id);

        bookRetriever.existsById(id);

        Book bookFromDatabase = bookRetriever.findBookById(id);
        Book.BookBuilder builder = Book.builder();

        if (bookFromRequest.getTitle() != null) {
            log.debug("Updating title of book id={} to '{}'", id, bookFromRequest.getTitle());
            builder.title(bookFromRequest.getTitle());
        } else {
            builder.title(bookFromDatabase.getTitle());
        }

        if (bookFromRequest.getAuthor() != null) {
            log.debug("Updating author of book id={} to '{}'", id, bookFromRequest.getAuthor());
            builder.author(bookFromRequest.getAuthor());
        } else {
            builder.author(bookFromDatabase.getAuthor());
        }

        Book toSave = builder.build();
        updateById(id, toSave);

        return toSave;
    }
    
}
