package io.github.wkktoria.bookify.domain.crud;

import io.github.wkktoria.bookify.domain.crud.dto.AuthorDto;
import io.github.wkktoria.bookify.domain.crud.dto.BookDto;
import io.github.wkktoria.bookify.domain.crud.dto.SeriesDto;
import io.github.wkktoria.bookify.domain.crud.dto.SeriesWithAuthorsAndBooksDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Log4j2
class SeriesRetriever {

    private final SeriesRepository seriesRepository;

    List<SeriesDto> findAll() {
        log.debug("Retrieving all series");
        return seriesRepository.findAll().stream()
                .map(series -> SeriesDto.builder()
                        .id(series.getId())
                        .name(series.getName())
                        .build())
                .toList();
    }

    SeriesWithAuthorsAndBooksDto findSeriesByIdWithAuthorsAndBooks(final Long id) {
        log.debug("Retrieving series with id={}", id);

        Series series = seriesRepository.findByIdWithBooksAndAuthors(id)
                .orElseThrow(() -> {
                    log.warn("Series with id={} not found", id);
                    return new SeriesNotFoundException("Could not find series with id=" + id);
                });

        Set<Author> authors = series.getBooks().stream()
                .flatMap(book -> book.getAuthors().stream())
                .collect(Collectors.toSet());

        Set<Book> books = series.getBooks();

        SeriesDto seriesDto = new SeriesDto(series.getId(), series.getName());

        Set<BookDto> booksDto = books.stream()
                .map(BookDomainMapper::mapFromBookToBookDto)
                .collect(Collectors.toSet());

        Set<AuthorDto> authorsDto = authors.stream()
                .map(author -> new AuthorDto(author.getId(), author.getFirstname(), author.getLastname()))
                .collect(Collectors.toSet());

        return new SeriesWithAuthorsAndBooksDto(
                seriesDto,
                authorsDto,
                booksDto
        );
    }

}
