package io.github.wkktoria.bookify.domain.crud;

import io.github.wkktoria.bookify.domain.crud.dto.AuthorDto;
import io.github.wkktoria.bookify.domain.crud.dto.AuthorRequestDto;
import io.github.wkktoria.bookify.domain.crud.dto.BookDto;
import io.github.wkktoria.bookify.domain.crud.dto.BookRequestDto;
import io.github.wkktoria.bookify.domain.crud.dto.GenreDto;
import io.github.wkktoria.bookify.domain.crud.dto.GenreRequestDto;
import io.github.wkktoria.bookify.domain.crud.dto.SeriesDto;
import io.github.wkktoria.bookify.domain.crud.dto.SeriesRequestDto;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static io.github.wkktoria.bookify.domain.crud.BookDomainMapper.mapFromBookDtoToBook;
import static io.github.wkktoria.bookify.domain.crud.BookDomainMapper.mapFromBookToBookDto;

@Service
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Log4j2
public class BookifyCrudFacade {

    private final BookAdder bookAdder;
    private final BookRetriever bookRetriever;
    private final BookDeleter bookDeleter;
    private final BookUpdater bookUpdater;
    private final AuthorAdder authorAdder;
    private final GenreAdder genreAdder;
    private final SeriesAdder seriesAdder;

    public AuthorDto addAuthor(final AuthorRequestDto requestDto) {
        log.info("Adding new author: firstname='{}', lastname='{}'",
                requestDto.firstname(), requestDto.lastname());

        AuthorDto addedAuthor = authorAdder.addAuthor(requestDto.firstname(), requestDto.lastname());

        log.info("Author successfully added with id={}", addedAuthor.id());

        return addedAuthor;
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


    public List<BookDto> findAll(final Pageable pageable) {
        log.debug("Fetching all books with pageable: page={}, size={}, sort={}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        List<BookDto> books = bookRetriever.findAll(pageable)
                .stream()
                .map(BookDomainMapper::mapFromBookToBookDto)
                .toList();

        log.debug("Fetched {} books", books.size());

        return books;
    }

    public BookDto findById(final Long id) {
        log.debug("Fetching book with id={}", id);

        Book book = bookRetriever.findBookById(id);
        BookDto result = mapFromBookToBookDto(book);

        log.debug("Book with id={} successfully retrieved", id);

        return result;
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
        Book bookFromDatabase = bookRetriever.findBookById(id);

        Book toSave = new Book();

        if (bookToUpdateDto.title() != null) {
            log.debug("Updating title for book id={}", id);
            toSave.setTitle(bookToUpdateDto.title());
        } else {
            log.debug("Updating author for book id={}", id);
            toSave.setTitle(bookFromDatabase.getTitle());
        }

        toSave.setPublicationDate(bookFromDatabase.getPublicationDate());
        toSave.setIsbn(bookFromDatabase.getIsbn());
        toSave.setPages(bookFromDatabase.getPages());

        bookUpdater.updateById(id, toSave);

        log.info("Book with id={} partially updated", id);

        return mapFromBookToBookDto(toSave);
    }

}
