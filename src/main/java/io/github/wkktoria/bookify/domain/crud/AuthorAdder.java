package io.github.wkktoria.bookify.domain.crud;

import io.github.wkktoria.bookify.domain.crud.dto.AuthorDto;
import io.github.wkktoria.bookify.domain.crud.dto.AuthorRequestDto;
import io.github.wkktoria.bookify.domain.crud.dto.BookRequestDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
@Log4j2
class AuthorAdder {

    private final AuthorRepository authorRepository;
    private final AuthorRetriever authorRetriever;
    private final BookRetriever bookRetriever;
    private final SeriesAdder seriesAdder;
    private final BookAdder bookAdder;

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

    AuthorDto addAuthorWithDefaultSeriesAndBook(final AuthorRequestDto requestDto) {
        log.debug("Saving new author: firstname='{}', lastname='{}' with default series and book",
                requestDto.firstname(), requestDto.lastname());
        Author saved = saveAuthorWithDefaultSeriesAndBook(requestDto.firstname(), requestDto.lastname());
        return new AuthorDto(saved.getId(), saved.getFirstname(), saved.getLastname());
    }

    private Author saveAuthorWithDefaultSeriesAndBook(final String firstname, final String lastname) {
        Series series = seriesAdder.addSeries("series-" + UUID.randomUUID());

        Book book = bookAdder.addBook("book-" + UUID.randomUUID(),
                LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC),
                "-", 0, BookLanguage.OTHER);

        Author author = new Author(firstname, lastname);

        series.addBook(book);
        author.addBook(book);

        return authorRepository.save(author);
    }

}
