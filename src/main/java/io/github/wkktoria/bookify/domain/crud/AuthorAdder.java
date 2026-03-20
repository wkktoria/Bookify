package io.github.wkktoria.bookify.domain.crud;

import io.github.wkktoria.bookify.domain.crud.dto.AuthorDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
class AuthorAdder {

    private final AuthorRepository authorRepository;
    private final AuthorRetriever authorRetriever;
    private final BookRetriever bookRetriever;

    AuthorDto addAuthor(final String firstname, final String lastname) {
        log.debug("Saving new author: firstname='{}', lastname='{}'",
                firstname, lastname);

        Author author = new Author(firstname, lastname);
        Author savedAuthor = authorRepository.save(author);

        log.debug("Author saved with id={}", savedAuthor.getId());

        return new AuthorDto(savedAuthor.getId(), savedAuthor.getFirstname(), savedAuthor.getLastname());
    }

    void addAuthorToBook(final Long authorId, final Long bookId) {
        log.debug("Adding author with authorId={} to book with bookId={}", authorId, bookId);
        Author author = authorRetriever.findAuthorById(authorId);
        Book book = bookRetriever.findBookById(bookId);
        author.addBook(book);
    }

}
