package io.github.wkktoria.bookify.domain.crud;

import io.github.wkktoria.bookify.domain.crud.dto.AuthorDto;
import io.github.wkktoria.bookify.domain.crud.dto.BookDto;
import io.github.wkktoria.bookify.domain.crud.dto.BookSimpleDto;
import io.github.wkktoria.bookify.domain.crud.dto.GenreDto;

import java.time.ZoneOffset;
import java.util.stream.Collectors;

class BookDomainMapper {

    static BookDto mapFromBookToBookDto(final Book book) {
        return BookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .publicationDate(book.getPublicationDate()
                        .atZone(ZoneOffset.UTC)
                        .toLocalDate())
                .isbn(book.getIsbn())
                .pages(book.getPages())
                .genre(new GenreDto(book.getGenre().getId(), book.getGenre().getName()))
                .authors(book.getAuthors().stream()
                        .map(author -> new AuthorDto(author.getId(), author.getFirstname(), author.getLastname()))
                        .collect(Collectors.toSet()))
                .build();
    }

    static BookSimpleDto mapFromBookToBookSimpleDto(final Book book) {
        return BookSimpleDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .publicationDate(book.getPublicationDate()
                        .atZone(ZoneOffset.UTC)
                        .toLocalDate())
                .isbn(book.getIsbn())
                .pages(book.getPages())
                .build();
    }

    static Book mapFromBookDtoToBook(final BookDto dto) {
        return Book.builder()
                .title(dto.title())
                .publicationDate(dto.publicationDate()
                        .atStartOfDay()
                        .toInstant(ZoneOffset.UTC))
                .isbn(dto.isbn())
                .pages(dto.pages())
                .genre(new Genre(dto.genre().id(), dto.genre().name()))
                .authors(dto.authors().stream()
                        .map(authorDto -> new Author(authorDto.id(), authorDto.firstname(), authorDto.lastname()))
                        .collect(Collectors.toSet()))
                .build();
    }

}
