package io.github.wkktoria.bookify.domain.crud;

import io.github.wkktoria.bookify.domain.crud.dto.BookDto;
import io.github.wkktoria.bookify.domain.crud.dto.GenreDto;

import java.time.ZoneOffset;

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
                .build();
    }

}
