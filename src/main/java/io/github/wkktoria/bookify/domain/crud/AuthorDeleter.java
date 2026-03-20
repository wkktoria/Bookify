package io.github.wkktoria.bookify.domain.crud;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Log4j2
class AuthorDeleter {

    private final AuthorRepository authorRepository;
    private final AuthorRetriever authorRetriever;
    private final BookRetriever bookRetriever;
    private final BookDeleter bookDeleter;

    void deleteAuthorByIdWithBooks(final Long id) {
        log.debug("Deleting author with id={}", id);

        Author author = authorRetriever.findAuthorById(id);
        Set<Book> authorBooks = bookRetriever.findBooksByAuthorId(author.getId());

        if (authorBooks.isEmpty()) {
            log.info("Author with id={} has no books", author.getId());
            authorRepository.deleteById(author.getId());
            return;
        }

        Set<Book> booksWithOnlyOneAuthor = authorBooks.stream()
                .filter(book -> book.getAuthors().size() == 1)
                .collect(Collectors.toSet());

        Set<Book> booksWithMultipleAuthors = authorBooks.stream()
                .filter(book -> book.getAuthors().size() > 1)
                .collect(Collectors.toSet());

        booksWithMultipleAuthors.forEach(book -> book.removeAuthor(author));

        Set<Long> allBooksIdsWhereWereOnlyThisAuthor = booksWithOnlyOneAuthor.stream()
                .map(Book::getId)
                .collect(Collectors.toSet());

        log.info("Deleting all books of author with id={}", author.getId());
        bookDeleter.deleteAllBooksByIds(allBooksIdsWhereWereOnlyThisAuthor);

        authorRepository.deleteById(author.getId());

        log.debug("Author with id={} successfully deleted", id);
    }

}
