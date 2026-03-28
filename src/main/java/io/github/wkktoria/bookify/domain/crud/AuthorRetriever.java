package io.github.wkktoria.bookify.domain.crud;

import io.github.wkktoria.bookify.domain.crud.dto.AuthorDto;
import io.github.wkktoria.bookify.domain.crud.dto.AuthorWithBooksDto;
import io.github.wkktoria.bookify.domain.crud.dto.BookSimpleDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Log4j2
class AuthorRetriever {

    private final AuthorRepository authorRepository;
    private final BookRetriever bookRetriever;

    List<AuthorDto> findAllAuthors(final Pageable pageable) {
        log.debug("Retrieving authors");
        return authorRepository.findAll(pageable).stream()
                .map(author -> new AuthorDto(author.getId(), author.getFirstname(), author.getLastname()))
                .toList();
    }

    Author findAuthorById(final Long id) {
        log.debug("Retrieving author with id={}", id);

        return authorRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Author with id={} not found", id);
                    return new AuthorNotFoundException("Could not find author with id=" + id);
                });
    }

    Set<AuthorDto> findAuthorsByBookId(final Long bookId) {
        log.debug("Retrieving authors of book with id={}", bookId);

        return bookRetriever.findBookById(bookId).getAuthors().stream()
                .map(author -> new AuthorDto(author.getId(), author.getFirstname(), author.getLastname()))
                .collect(Collectors.toSet());
    }

    AuthorWithBooksDto findAuthorByIdWithBooks(final Long id) {
        log.debug("Retrieving author with id={} and their books", id);

        Author author = findAuthorById(id);
        Set<Book> books = author.getBooks();

        AuthorDto authorDto = new AuthorDto(author.getId(), author.getFirstname(), author.getLastname());
        Set<BookSimpleDto> booksDto = books.stream()
                .map(BookDomainMapper::mapFromBookToBookSimpleDto)
                .collect(Collectors.toSet());

        return new AuthorWithBooksDto(authorDto, booksDto);
    }

}
