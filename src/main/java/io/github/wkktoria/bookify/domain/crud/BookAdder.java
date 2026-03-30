package io.github.wkktoria.bookify.domain.crud;

import io.github.wkktoria.bookify.domain.crud.dto.AuthorDto;
import io.github.wkktoria.bookify.domain.crud.dto.BookDto;
import io.github.wkktoria.bookify.domain.crud.dto.BookLanguageDto;
import io.github.wkktoria.bookify.domain.crud.dto.BookRequestDto;
import io.github.wkktoria.bookify.domain.crud.dto.GenreDto;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.stream.Collectors;

import static io.github.wkktoria.bookify.domain.crud.BookDomainMapper.mapFromBookToBookDto;

@Service
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Transactional
@Log4j2
class BookAdder {

    private final BookRepository bookRepository;
    private final AuthorRetriever authorRetriever;
    private final GenreAssigner genreAssigner;
    private final BookRetriever bookRetriever;
    private final SeriesRetriever seriesRetriever;

    BookDto addBook(final BookRequestDto requestDto) {
        log.debug("Saving new book: title='{}', publicationDate={}, isbn='{}', pages={}, authorId={}",
                requestDto.title(), requestDto.publicationDate(), requestDto.isbn(), requestDto.pages(), requestDto.authorId());

        Author authorById = authorRetriever.findAuthorById(requestDto.authorId());

        Book book = new Book(
                requestDto.title(), requestDto.publicationDate().atStartOfDay().toInstant(ZoneOffset.UTC), requestDto.isbn(), requestDto.pages()
        );
        book.setLanguage(BookLanguage.valueOf(requestDto.language().name()));
        book.addAuthor(authorById);

        Book savedBook = bookRepository.save(book);

        log.debug("Book saved with id={}", savedBook.getId());

        genreAssigner.assignDefaultGenreToBook(book.getId());

        return mapFromBookToBookDto(savedBook);
    }

    Book addBook(final String title, final Instant publicationDate,
                 final String isbn, final Integer pages, final BookLanguage language) {
        log.debug("Saving new book: title='{}', publicationDate={}, isbn='{}', pages={}",
                title, publicationDate, isbn, pages);

        Book book = new Book(
                title, publicationDate, isbn, pages
        );
        book.setLanguage(language);

        Book savedBook = bookRepository.save(book);

        genreAssigner.assignDefaultGenreToBook(savedBook.getId());

        return savedBook;
    }

    void addBookToSeries(final Long bookId, final Long seriesId) {
        log.debug("Adding book with id={} to series with id={}", bookId, seriesId);
        Book book = bookRetriever.findBookById(bookId);
        Series series = seriesRetriever.findSeriesById(seriesId);
        series.addBook(book);
    }

}
