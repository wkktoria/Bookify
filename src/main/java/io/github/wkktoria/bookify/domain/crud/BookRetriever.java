package io.github.wkktoria.bookify.domain.crud;

import io.github.wkktoria.bookify.domain.crud.dto.BookDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.github.wkktoria.bookify.domain.crud.BookDomainMapper.mapFromBookToBookDto;

@Service
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Log4j2
class BookRetriever {

    private final BookRepository bookRepository;

    List<BookDto> findAllBooks(final Pageable pageable) {
        log.debug("Retrieving books with pageable");
        return bookRepository.findAll(pageable).stream()
                .map(BookDomainMapper::mapFromBookToBookDto)
                .toList();
    }

    BookDto findBookDtoById(final Long id) {
        Book book = findBookById(id);
        return mapFromBookToBookDto(book);
    }

    Book findBookById(final Long id) {
        log.debug("Retrieving book with id={}", id);

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

    Set<Book> findBooksByAuthorId(final Long authorId) {
        return bookRepository.findAllByAuthorId(authorId);
    }

    Set<Book> findBooksByGenreId(final Long genreId) {
        return bookRepository.findAllByGenreId(genreId);
    }

    Set<BookDto> findBookDtosByAuthorId(final Long authorId) {
        return findBooksByAuthorId(authorId).stream()
                .map(BookDomainMapper::mapFromBookToBookDto)
                .collect(Collectors.toSet());
    }

    long countAuthorsByBookId(final Long id) {
        return findBookById(id).getAuthors().size();
    }

}
