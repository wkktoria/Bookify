package io.github.wkktoria.bookify.infrastructure.crud.book.controller.dto.response;

import io.github.wkktoria.bookify.domain.crud.dto.BookDto;

import java.util.Set;

public record GetAllBooksResponseDto(Set<BookDto> books) {
}
