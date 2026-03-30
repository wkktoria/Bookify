package io.github.wkktoria.bookify.domain.crud;

import io.github.wkktoria.bookify.domain.crud.dto.AuthorDto;
import io.github.wkktoria.bookify.domain.crud.dto.AuthorRequestDto;
import io.github.wkktoria.bookify.domain.crud.dto.AuthorWithBooksDto;
import io.github.wkktoria.bookify.domain.crud.dto.BookDto;
import io.github.wkktoria.bookify.domain.crud.dto.BookRequestDto;
import io.github.wkktoria.bookify.domain.crud.dto.GenreDto;
import io.github.wkktoria.bookify.domain.crud.dto.GenreRequestDto;
import io.github.wkktoria.bookify.domain.crud.dto.GenreWithBooksDto;
import io.github.wkktoria.bookify.domain.crud.dto.SeriesDto;
import io.github.wkktoria.bookify.domain.crud.dto.SeriesRequestDto;
import io.github.wkktoria.bookify.domain.crud.dto.SeriesWithAuthorsAndBooksDto;
import io.github.wkktoria.bookify.domain.crud.dto.UpdateAuthorRequestDto;
import io.github.wkktoria.bookify.domain.crud.dto.UpdateGenreRequestDto;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;

import static io.github.wkktoria.bookify.domain.crud.BookDomainMapper.mapFromBookDtoToBook;
import static io.github.wkktoria.bookify.domain.crud.BookDomainMapper.mapFromBookToBookDto;

@Service
@Transactional
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Log4j2
public class BookifyCrudFacade {

    private final BookAdder bookAdder;
    private final BookRetriever bookRetriever;
    private final BookDeleter bookDeleter;
    private final BookUpdater bookUpdater;
    private final AuthorAdder authorAdder;
    private final AuthorRetriever authorRetriever;
    private final AuthorDeleter authorDeleter;
    private final AuthorUpdater authorUpdater;
    private final GenreRetriever genreRetriever;
    private final GenreAdder genreAdder;
    private final GenreUpdater genreUpdater;
    private final GenreAssigner genreAssigner;
    private final SeriesAdder seriesAdder;
    private final SeriesRetriever seriesRetriever;

    public AuthorDto addAuthor(final AuthorRequestDto requestDto) {
        log.info("Adding new author: firstname='{}', lastname='{}'",
                requestDto.firstname(), requestDto.lastname());

        AuthorDto addedAuthor = authorAdder.addAuthor(requestDto.firstname(), requestDto.lastname());

        log.info("Author successfully added with id={}", addedAuthor.id());

        return addedAuthor;
    }

    public void deleteAuthorByIdWithBooks(final Long authorId) {
        log.info("Deleting author with id={}", authorId);

        authorDeleter.deleteAuthorByIdWithBooks(authorId);

        log.info("Author with id={} deleted successfully", authorId);
    }

    public Set<BookDto> findBooksByAuthorId(final Long authorId) {
        log.info("Finding books by authorId={}", authorId);
        return bookRetriever.findBookDtosByAuthorId(authorId);
    }

    public GenreDto addGenre(final GenreRequestDto requestDto) {
        log.info("Adding new genre: name='{}'", requestDto.name());

        GenreDto addedGenre = genreAdder.addGenre(requestDto.name());

        log.info("Genre successfully added with id={}", addedGenre.id());

        return addedGenre;
    }

    public SeriesDto addSeriesWithBook(final SeriesRequestDto requestDto) {
        log.info("Adding new series: name='{}'", requestDto.name());

        SeriesDto addedSeries = seriesAdder.addSeries(requestDto.name(), requestDto.bookId());

        log.info("Series successfully added with id={}", addedSeries.id());

        return addedSeries;
    }

    public BookDto addBookWithAuthor(final BookRequestDto requestDto) {
        log.info("Adding new book with title='{}' and authorId={}",
                requestDto.title(), requestDto.authorId());

        BookDto addedBook = bookAdder.addBook(requestDto);

        log.info("Book successfully added with id={}", addedBook.id());

        return addedBook;
    }

    public List<AuthorDto> findAllAuthors(final Pageable pageable) {
        log.debug("Fetching all authors with pageable");
        return authorRetriever.findAllAuthors(pageable);
    }


    public List<BookDto> findAllBooks(final Pageable pageable) {
        log.debug("Fetching all books with pageable");
        return bookRetriever.findAllBooks(pageable);
    }

    public BookDto findBookById(final Long id) {
        log.debug("Fetching book with id={}", id);
        return bookRetriever.findBookDtoById(id);
    }

    public SeriesWithAuthorsAndBooksDto findSeriesByIdWithAuthorsAndBooks(final Long id) {
        log.debug("Fetching series with id={}", id);
        return seriesRetriever.findSeriesByIdWithAuthorsAndBooks(id);
    }

    public void deleteById(final Long id) {
        log.info("Deleting book with id={}", id);

        bookRetriever.existsById(id);
        bookDeleter.deleteById(id);

        log.info("Book with id={} deleted successfully", id);
    }

    public void updateById(final Long id, final BookDto newBookDto) {
        log.info("Updating book with id={}", id);

        bookRetriever.existsById(id);
        Book validatedAndReadyToUpdateBook = mapFromBookDtoToBook(newBookDto);
        bookUpdater.updateById(id, validatedAndReadyToUpdateBook);

        log.info("Book with id={} updated successfully", id);
    }

    public BookDto updatePartiallyById(final Long id, final BookDto bookToUpdateDto) {
        log.info("Partially updating book with id={}", id);

        bookRetriever.existsById(id);
        BookDto bookFromDatabase = bookRetriever.findBookDtoById(id);

        Book toSave = new Book();

        if (bookToUpdateDto.title() != null) {
            log.debug("Updating title for book id={}", id);
            toSave.setTitle(bookToUpdateDto.title());
        } else {
            log.debug("Updating author for book id={}", id);
            toSave.setTitle(bookFromDatabase.title());
        }

        toSave.setPublicationDate(bookFromDatabase.publicationDate().atStartOfDay().toInstant(ZoneOffset.UTC));
        toSave.setIsbn(bookFromDatabase.isbn());
        toSave.setPages(bookFromDatabase.pages());

        bookUpdater.updateById(id, toSave);

        log.info("Book with id={} partially updated", id);

        return mapFromBookToBookDto(toSave);
    }

    long countAuthorsByBookId(final Long bookId) {
        log.info("Counting authors by bookId={}", bookId);
        return bookRetriever.countAuthorsByBookId(bookId);
    }

    Set<AuthorDto> findAuthorsByBookId(final Long bookId) {
        log.info("Finding authors by bookId={}", bookId);
        return authorRetriever.findAuthorsByBookId(bookId);
    }

    public void addAuthorToBook(final Long authorId, final Long bookId) {
        log.info("Adding author with id={} to book with id={}", authorId, bookId);
        authorAdder.addAuthorToBook(authorId, bookId);
    }

    public AuthorDto updateAuthorById(final Long authorId, final UpdateAuthorRequestDto requestDto) {
        log.info("Updating author with id={}", authorId);
        return authorUpdater.updateAuthorById(authorId, requestDto);
    }

    public GenreDto updateGenreById(final Long genreId, final UpdateGenreRequestDto requestDto) {
        log.info("Updating genre with id={}", genreId);
        return genreUpdater.updateGenreById(genreId, requestDto);
    }

    public List<SeriesDto> findAllSeries() {
        log.info("Finding all series");
        return seriesRetriever.findAll();
    }

    public Set<GenreDto> findAllGenres() {
        log.info("Finding all genres");
        return genreRetriever.findAll();
    }

    public void assignGenreToBook(final Long genreId, final Long bookId) {
        log.info("Assigning genre with id={} to book with id={}", genreId, bookId);
        genreAssigner.assignGenreToBook(genreId, bookId);
    }

    public void addBookToSeries(final Long bookId, final Long seriesId) {
        log.info("Adding book with id={} to series with id={}", bookId, seriesId);
        bookAdder.addBookToSeries(bookId, seriesId);
    }

    public GenreWithBooksDto findGenreByIdWithBooks(final long genreId) {
        log.info("Fetching genre with id={} and its books", genreId);
        return genreRetriever.findGenreByIdWithBooks(genreId);
    }

    public AuthorWithBooksDto findAuthorByIdWithBooks(final Long authorId) {
        log.info("Fetching author with id={} and their books", authorId);
        return authorRetriever.findAuthorByIdWithBooks(authorId);
    }

    public AuthorDto addAuthorWithDefaultSeriesAndBook(final AuthorRequestDto requestDto) {
        log.info("Adding author with default series and book");
        return authorAdder.addAuthorWithDefaultSeriesAndBook(requestDto);
    }

}
