package io.github.wkktoria.bookify.domain.crud;

import io.github.wkktoria.bookify.domain.crud.dto.BookDto;

import java.time.ZoneOffset;

class BookDomainMapper {

    static BookDto mapFromBookToBookDto(final Book book) {
        return BookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
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
                .author(dto.author())
                .publicationDate(dto.publicationDate()
                        .atStartOfDay()
                        .toInstant(ZoneOffset.UTC))
                .isbn(dto.isbn())
                .pages(dto.pages())
                .build();
    }

}
